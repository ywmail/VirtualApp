/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa.back;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.ssa.back.InterferenceGraph;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

public class LivenessAnalyzer {
    private final BitSet visitedBlocks;
    private final BitSet liveOutBlocks;
    private final int regV;
    private final SsaMethod ssaMeth;
    private final InterferenceGraph interference;
    private SsaBasicBlock blockN;
    private int statementIndex;
    private NextFunction nextFunction;

    public static InterferenceGraph constructInterferenceGraph(SsaMethod ssaMeth) {
        int szRegs = ssaMeth.getRegCount();
        InterferenceGraph interference = new InterferenceGraph(szRegs);
        for (int i = 0; i < szRegs; ++i) {
            new LivenessAnalyzer(ssaMeth, i, interference).run();
        }
        LivenessAnalyzer.coInterferePhis(ssaMeth, interference);
        return interference;
    }

    private LivenessAnalyzer(SsaMethod ssaMeth, int reg, InterferenceGraph interference) {
        int blocksSz = ssaMeth.getBlocks().size();
        this.ssaMeth = ssaMeth;
        this.regV = reg;
        this.visitedBlocks = new BitSet(blocksSz);
        this.liveOutBlocks = new BitSet(blocksSz);
        this.interference = interference;
    }

    private void handleTailRecursion() {
        block5: while (this.nextFunction != NextFunction.DONE) {
            switch (this.nextFunction) {
                case LIVE_IN_AT_STATEMENT: {
                    this.nextFunction = NextFunction.DONE;
                    this.liveInAtStatement();
                    continue block5;
                }
                case LIVE_OUT_AT_STATEMENT: {
                    this.nextFunction = NextFunction.DONE;
                    this.liveOutAtStatement();
                    continue block5;
                }
                case LIVE_OUT_AT_BLOCK: {
                    this.nextFunction = NextFunction.DONE;
                    this.liveOutAtBlock();
                    continue block5;
                }
            }
        }
    }

    public void run() {
        int nextLiveOutBlock;
        List<SsaInsn> useList = this.ssaMeth.getUseListForRegister(this.regV);
        for (SsaInsn insn : useList) {
            this.nextFunction = NextFunction.DONE;
            if (insn instanceof PhiInsn) {
                PhiInsn phi = (PhiInsn)insn;
                Iterator<SsaBasicBlock> iterator = phi.predBlocksForReg(this.regV, this.ssaMeth).iterator();
                while (iterator.hasNext()) {
                    SsaBasicBlock pred;
                    this.blockN = pred = iterator.next();
                    this.nextFunction = NextFunction.LIVE_OUT_AT_BLOCK;
                    this.handleTailRecursion();
                }
                continue;
            }
            this.blockN = insn.getBlock();
            this.statementIndex = this.blockN.getInsns().indexOf(insn);
            if (this.statementIndex < 0) {
                throw new RuntimeException("insn not found in it's own block");
            }
            this.nextFunction = NextFunction.LIVE_IN_AT_STATEMENT;
            this.handleTailRecursion();
        }
        while ((nextLiveOutBlock = this.liveOutBlocks.nextSetBit(0)) >= 0) {
            this.blockN = this.ssaMeth.getBlocks().get(nextLiveOutBlock);
            this.liveOutBlocks.clear(nextLiveOutBlock);
            this.nextFunction = NextFunction.LIVE_OUT_AT_BLOCK;
            this.handleTailRecursion();
        }
    }

    private void liveOutAtBlock() {
        if (!this.visitedBlocks.get(this.blockN.getIndex())) {
            this.visitedBlocks.set(this.blockN.getIndex());
            this.blockN.addLiveOut(this.regV);
            ArrayList<SsaInsn> insns = this.blockN.getInsns();
            this.statementIndex = insns.size() - 1;
            this.nextFunction = NextFunction.LIVE_OUT_AT_STATEMENT;
        }
    }

    private void liveInAtStatement() {
        if (this.statementIndex == 0) {
            this.blockN.addLiveIn(this.regV);
            BitSet preds = this.blockN.getPredecessors();
            this.liveOutBlocks.or(preds);
        } else {
            --this.statementIndex;
            this.nextFunction = NextFunction.LIVE_OUT_AT_STATEMENT;
        }
    }

    private void liveOutAtStatement() {
        SsaInsn statement = this.blockN.getInsns().get(this.statementIndex);
        RegisterSpec rs = statement.getResult();
        if (!statement.isResultReg(this.regV)) {
            if (rs != null) {
                this.interference.add(this.regV, rs.getReg());
            }
            this.nextFunction = NextFunction.LIVE_IN_AT_STATEMENT;
        }
    }

    private static void coInterferePhis(SsaMethod ssaMeth, InterferenceGraph interference) {
        for (SsaBasicBlock b : ssaMeth.getBlocks()) {
            List<SsaInsn> phis = b.getPhiInsns();
            int szPhis = phis.size();
            for (int i = 0; i < szPhis; ++i) {
                for (int j = 0; j < szPhis; ++j) {
                    if (i == j) continue;
                    SsaInsn first = phis.get(i);
                    SsaInsn second = phis.get(j);
                    LivenessAnalyzer.coInterferePhiRegisters(interference, first.getResult(), second.getSources());
                    LivenessAnalyzer.coInterferePhiRegisters(interference, second.getResult(), first.getSources());
                    interference.add(first.getResult().getReg(), second.getResult().getReg());
                }
            }
        }
    }

    private static void coInterferePhiRegisters(InterferenceGraph interference, RegisterSpec result, RegisterSpecList sources) {
        int resultReg = result.getReg();
        for (int i = 0; i < sources.size(); ++i) {
            interference.add(resultReg, sources.get(i).getReg());
        }
    }

    private static enum NextFunction {
        LIVE_IN_AT_STATEMENT,
        LIVE_OUT_AT_STATEMENT,
        LIVE_OUT_AT_BLOCK,
        DONE;

    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.ssa.NormalSsaInsn;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;

public class DeadCodeRemover {
    private final SsaMethod ssaMeth;
    private final int regCount;
    private final BitSet worklist;
    private final ArrayList<SsaInsn>[] useList;

    public static void process(SsaMethod ssaMethod) {
        DeadCodeRemover dc = new DeadCodeRemover(ssaMethod);
        dc.run();
    }

    private DeadCodeRemover(SsaMethod ssaMethod) {
        this.ssaMeth = ssaMethod;
        this.regCount = ssaMethod.getRegCount();
        this.worklist = new BitSet(this.regCount);
        this.useList = this.ssaMeth.getUseListCopy();
    }

    private void run() {
        int regV;
        this.pruneDeadInstructions();
        HashSet<SsaInsn> deletedInsns = new HashSet<SsaInsn>();
        this.ssaMeth.forEachInsn(new NoSideEffectVisitor(this.worklist));
        while (0 <= (regV = this.worklist.nextSetBit(0))) {
            SsaInsn insnS;
            this.worklist.clear(regV);
            if (this.useList[regV].size() != 0 && !this.isCircularNoSideEffect(regV, null) || deletedInsns.contains(insnS = this.ssaMeth.getDefinitionForRegister(regV))) continue;
            RegisterSpecList sources = insnS.getSources();
            int sz = sources.size();
            for (int i = 0; i < sz; ++i) {
                RegisterSpec source = sources.get(i);
                this.useList[source.getReg()].remove(insnS);
                if (DeadCodeRemover.hasSideEffect(this.ssaMeth.getDefinitionForRegister(source.getReg()))) continue;
                this.worklist.set(source.getReg());
            }
            deletedInsns.add(insnS);
        }
        this.ssaMeth.deleteInsns(deletedInsns);
    }

    private void pruneDeadInstructions() {
        HashSet<SsaInsn> deletedInsns = new HashSet<SsaInsn>();
        BitSet reachable = this.ssaMeth.computeReachability();
        ArrayList<SsaBasicBlock> blocks = this.ssaMeth.getBlocks();
        int blockIndex = 0;
        while ((blockIndex = reachable.nextClearBit(blockIndex)) < blocks.size()) {
            SsaBasicBlock block = blocks.get(blockIndex);
            ++blockIndex;
            for (int i = 0; i < block.getInsns().size(); ++i) {
                SsaInsn insn = block.getInsns().get(i);
                RegisterSpecList sources = insn.getSources();
                int sourcesSize = sources.size();
                if (sourcesSize != 0) {
                    deletedInsns.add(insn);
                }
                for (int j = 0; j < sourcesSize; ++j) {
                    RegisterSpec source = sources.get(j);
                    this.useList[source.getReg()].remove(insn);
                }
                RegisterSpec result = insn.getResult();
                if (result == null) continue;
                for (SsaInsn use : this.useList[result.getReg()]) {
                    if (!(use instanceof PhiInsn)) continue;
                    PhiInsn phiUse = (PhiInsn)use;
                    phiUse.removePhiRegister(result);
                }
            }
        }
        this.ssaMeth.deleteInsns(deletedInsns);
    }

    private boolean isCircularNoSideEffect(int regV, BitSet set) {
        if (set != null && set.get(regV)) {
            return true;
        }
        for (SsaInsn use : this.useList[regV]) {
            if (!DeadCodeRemover.hasSideEffect(use)) continue;
            return false;
        }
        if (set == null) {
            set = new BitSet(this.regCount);
        }
        set.set(regV);
        for (SsaInsn use : this.useList[regV]) {
            RegisterSpec result = use.getResult();
            if (result != null && this.isCircularNoSideEffect(result.getReg(), set)) continue;
            return false;
        }
        return true;
    }

    private static boolean hasSideEffect(SsaInsn insn) {
        if (insn == null) {
            return true;
        }
        return insn.hasSideEffect();
    }

    private static class NoSideEffectVisitor
    implements SsaInsn.Visitor {
        BitSet noSideEffectRegs;

        public NoSideEffectVisitor(BitSet noSideEffectRegs) {
            this.noSideEffectRegs = noSideEffectRegs;
        }

        @Override
        public void visitMoveInsn(NormalSsaInsn insn) {
            if (!DeadCodeRemover.hasSideEffect(insn)) {
                this.noSideEffectRegs.set(insn.getResult().getReg());
            }
        }

        @Override
        public void visitPhiInsn(PhiInsn phi) {
            if (!DeadCodeRemover.hasSideEffect(phi)) {
                this.noSideEffectRegs.set(phi.getResult().getReg());
            }
        }

        @Override
        public void visitNonMoveInsn(NormalSsaInsn insn) {
            RegisterSpec result = insn.getResult();
            if (!DeadCodeRemover.hasSideEffect(insn) && result != null) {
                this.noSideEffectRegs.set(result.getReg());
            }
        }
    }
}


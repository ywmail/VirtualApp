/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.ssa.DomFront;
import com.android.dx.ssa.LocalVariableExtractor;
import com.android.dx.ssa.LocalVariableInfo;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.ssa.SsaRenamer;
import com.android.dx.util.IntIterator;
import java.util.ArrayList;
import java.util.BitSet;

public class SsaConverter {
    public static final boolean DEBUG = false;

    public static SsaMethod convertToSsaMethod(RopMethod rmeth, int paramWidth, boolean isStatic) {
        SsaMethod result = SsaMethod.newFromRopMethod(rmeth, paramWidth, isStatic);
        SsaConverter.edgeSplit(result);
        LocalVariableInfo localInfo = LocalVariableExtractor.extract(result);
        SsaConverter.placePhiFunctions(result, localInfo, 0);
        new SsaRenamer(result).run();
        result.makeExitBlock();
        return result;
    }

    public static void updateSsaMethod(SsaMethod ssaMeth, int threshold) {
        LocalVariableInfo localInfo = LocalVariableExtractor.extract(ssaMeth);
        SsaConverter.placePhiFunctions(ssaMeth, localInfo, threshold);
        new SsaRenamer(ssaMeth, threshold).run();
    }

    public static SsaMethod testEdgeSplit(RopMethod rmeth, int paramWidth, boolean isStatic) {
        SsaMethod result = SsaMethod.newFromRopMethod(rmeth, paramWidth, isStatic);
        SsaConverter.edgeSplit(result);
        return result;
    }

    public static SsaMethod testPhiPlacement(RopMethod rmeth, int paramWidth, boolean isStatic) {
        SsaMethod result = SsaMethod.newFromRopMethod(rmeth, paramWidth, isStatic);
        SsaConverter.edgeSplit(result);
        LocalVariableInfo localInfo = LocalVariableExtractor.extract(result);
        SsaConverter.placePhiFunctions(result, localInfo, 0);
        return result;
    }

    private static void edgeSplit(SsaMethod result) {
        SsaConverter.edgeSplitPredecessors(result);
        SsaConverter.edgeSplitMoveExceptionsAndResults(result);
        SsaConverter.edgeSplitSuccessors(result);
    }

    private static void edgeSplitPredecessors(SsaMethod result) {
        ArrayList<SsaBasicBlock> blocks = result.getBlocks();
        for (int i = blocks.size() - 1; i >= 0; --i) {
            SsaBasicBlock block = blocks.get(i);
            if (!SsaConverter.nodeNeedsUniquePredecessor(block)) continue;
            block.insertNewPredecessor();
        }
    }

    private static boolean nodeNeedsUniquePredecessor(SsaBasicBlock block) {
        int countPredecessors = block.getPredecessors().cardinality();
        int countSuccessors = block.getSuccessors().cardinality();
        return countPredecessors > 1 && countSuccessors > 1;
    }

    private static void edgeSplitMoveExceptionsAndResults(SsaMethod ssaMeth) {
        ArrayList<SsaBasicBlock> blocks = ssaMeth.getBlocks();
        for (int i = blocks.size() - 1; i >= 0; --i) {
            SsaBasicBlock block = blocks.get(i);
            if (block.isExitBlock() || block.getPredecessors().cardinality() <= 1 || !block.getInsns().get(0).isMoveException()) continue;
            BitSet preds = (BitSet)block.getPredecessors().clone();
            int j = preds.nextSetBit(0);
            while (j >= 0) {
                SsaBasicBlock predecessor = blocks.get(j);
                SsaBasicBlock zNode = predecessor.insertNewSuccessor(block);
                zNode.getInsns().add(0, block.getInsns().get(0).clone());
                j = preds.nextSetBit(j + 1);
            }
            block.getInsns().remove(0);
        }
    }

    private static void edgeSplitSuccessors(SsaMethod result) {
        ArrayList<SsaBasicBlock> blocks = result.getBlocks();
        for (int i = blocks.size() - 1; i >= 0; --i) {
            SsaBasicBlock block = blocks.get(i);
            BitSet successors = (BitSet)block.getSuccessors().clone();
            int j = successors.nextSetBit(0);
            while (j >= 0) {
                SsaBasicBlock succ = blocks.get(j);
                if (SsaConverter.needsNewSuccessor(block, succ)) {
                    block.insertNewSuccessor(succ);
                }
                j = successors.nextSetBit(j + 1);
            }
        }
    }

    private static boolean needsNewSuccessor(SsaBasicBlock block, SsaBasicBlock succ) {
        ArrayList<SsaInsn> insns = block.getInsns();
        SsaInsn lastInsn = insns.get(insns.size() - 1);
        if (block.getSuccessors().cardinality() > 1 && succ.getPredecessors().cardinality() > 1) {
            return true;
        }
        return (lastInsn.getResult() != null || lastInsn.getSources().size() > 0) && succ.getPredecessors().cardinality() > 1;
    }

    private static void placePhiFunctions(SsaMethod ssaMeth, LocalVariableInfo localInfo, int threshold) {
        ArrayList<SsaBasicBlock> ssaBlocks = ssaMeth.getBlocks();
        int blockCount = ssaBlocks.size();
        int regCount = ssaMeth.getRegCount() - threshold;
        DomFront df = new DomFront(ssaMeth);
        DomFront.DomInfo[] domInfos = df.run();
        BitSet[] defsites = new BitSet[regCount];
        BitSet[] phisites = new BitSet[regCount];
        for (int i = 0; i < regCount; ++i) {
            defsites[i] = new BitSet(blockCount);
            phisites[i] = new BitSet(blockCount);
        }
        int s = ssaBlocks.size();
        for (int bi = 0; bi < s; ++bi) {
            SsaBasicBlock b = ssaBlocks.get(bi);
            for (SsaInsn insn : b.getInsns()) {
                RegisterSpec rs = insn.getResult();
                if (rs == null || rs.getReg() - threshold < 0) continue;
                defsites[rs.getReg() - threshold].set(bi);
            }
        }
        int s2 = regCount;
        for (int reg = 0; reg < s2; ++reg) {
            int workBlockIndex;
            BitSet worklist = (BitSet)defsites[reg].clone();
            while (0 <= (workBlockIndex = worklist.nextSetBit(0))) {
                worklist.clear(workBlockIndex);
                IntIterator dfIterator = domInfos[workBlockIndex].dominanceFrontiers.iterator();
                while (dfIterator.hasNext()) {
                    int dfBlockIndex = dfIterator.next();
                    if (phisites[reg].get(dfBlockIndex)) continue;
                    phisites[reg].set(dfBlockIndex);
                    int tReg = reg + threshold;
                    RegisterSpec rs = localInfo.getStarts(dfBlockIndex).get(tReg);
                    if (rs == null) {
                        ssaBlocks.get(dfBlockIndex).addPhiInsnForReg(tReg);
                    } else {
                        ssaBlocks.get(dfBlockIndex).addPhiInsnForReg(rs);
                    }
                    if (defsites[reg].get(dfBlockIndex)) continue;
                    worklist.set(dfBlockIndex);
                }
            }
        }
    }
}


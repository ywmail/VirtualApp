/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InsnList;
import com.android.dx.rop.code.LocalVariableInfo;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecSet;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.util.Bits;
import com.android.dx.util.IntList;

public final class LocalVariableExtractor {
    private final RopMethod method;
    private final BasicBlockList blocks;
    private final LocalVariableInfo resultInfo;
    private final int[] workSet;

    public static LocalVariableInfo extract(RopMethod method) {
        LocalVariableExtractor lve = new LocalVariableExtractor(method);
        return lve.doit();
    }

    private LocalVariableExtractor(RopMethod method) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        BasicBlockList blocks = method.getBlocks();
        int maxLabel = blocks.getMaxLabel();
        this.method = method;
        this.blocks = blocks;
        this.resultInfo = new LocalVariableInfo(method);
        this.workSet = Bits.makeBitSet(maxLabel);
    }

    private LocalVariableInfo doit() {
        int label = this.method.getFirstLabel();
        while (label >= 0) {
            Bits.clear(this.workSet, label);
            this.processBlock(label);
            label = Bits.findFirst(this.workSet, 0);
        }
        this.resultInfo.setImmutable();
        return this.resultInfo;
    }

    private void processBlock(int label) {
        RegisterSpecSet primaryState = this.resultInfo.mutableCopyOfStarts(label);
        BasicBlock block = this.blocks.labelToBlock(label);
        InsnList insns = block.getInsns();
        int insnSz = insns.size();
        boolean canThrowDuringLastInsn = block.hasExceptionHandlers() && insns.getLast().getResult() != null;
        int freezeSecondaryStateAt = insnSz - 1;
        RegisterSpecSet secondaryState = primaryState;
        for (int i = 0; i < insnSz; ++i) {
            RegisterSpec already;
            Insn insn;
            RegisterSpec result;
            if (canThrowDuringLastInsn && i == freezeSecondaryStateAt) {
                primaryState.setImmutable();
                primaryState = primaryState.mutableCopy();
            }
            if ((result = (insn = insns.get(i)).getLocalAssignment()) == null) {
                result = insn.getResult();
                if (result == null || primaryState.get(result.getReg()) == null) continue;
                primaryState.remove(primaryState.get(result.getReg()));
                continue;
            }
            if ((result = result.withSimpleType()).equals(already = primaryState.get(result))) continue;
            RegisterSpec previous = primaryState.localItemToSpec(result.getLocalItem());
            if (previous != null && previous.getReg() != result.getReg()) {
                primaryState.remove(previous);
            }
            this.resultInfo.addAssignment(insn, result);
            primaryState.put(result);
        }
        primaryState.setImmutable();
        IntList successors = block.getSuccessors();
        int succSz = successors.size();
        int primarySuccessor = block.getPrimarySuccessor();
        for (int i = 0; i < succSz; ++i) {
            RegisterSpecSet state;
            int succ = successors.get(i);
            RegisterSpecSet registerSpecSet = state = succ == primarySuccessor ? primaryState : secondaryState;
            if (!this.resultInfo.mergeStarts(succ, state)) continue;
            Bits.set(this.workSet, succ);
        }
    }
}


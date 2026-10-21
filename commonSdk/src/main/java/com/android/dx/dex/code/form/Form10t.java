/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.TargetInsn;
import com.android.dx.util.AnnotatedOutput;

public final class Form10t
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form10t();

    private Form10t() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        return Form10t.branchString(insn);
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        return Form10t.branchComment(insn);
    }

    @Override
    public int codeSize() {
        return 1;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        if (!(insn instanceof TargetInsn) || insn.getRegisters().size() != 0) {
            return false;
        }
        TargetInsn ti = (TargetInsn)insn;
        return ti.hasTargetOffset() ? this.branchFits(ti) : true;
    }

    @Override
    public boolean branchFits(TargetInsn insn) {
        int offset = insn.getTargetOffset();
        return offset != 0 && Form10t.signedFitsInByte(offset);
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        int offset = ((TargetInsn)insn).getTargetOffset();
        Form10t.write(out, Form10t.opcodeUnit(insn, offset & 0xFF));
    }
}


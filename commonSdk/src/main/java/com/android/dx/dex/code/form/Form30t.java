/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.TargetInsn;
import com.android.dx.util.AnnotatedOutput;

public final class Form30t
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form30t();

    private Form30t() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        return Form30t.branchString(insn);
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        return Form30t.branchComment(insn);
    }

    @Override
    public int codeSize() {
        return 3;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        return insn instanceof TargetInsn && insn.getRegisters().size() == 0;
    }

    @Override
    public boolean branchFits(TargetInsn insn) {
        return true;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        int offset = ((TargetInsn)insn).getTargetOffset();
        Form30t.write(out, Form30t.opcodeUnit(insn, 0), offset);
    }
}


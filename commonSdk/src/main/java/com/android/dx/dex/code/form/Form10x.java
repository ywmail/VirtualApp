/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.util.AnnotatedOutput;

public final class Form10x
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form10x();

    private Form10x() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        return "";
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        return "";
    }

    @Override
    public int codeSize() {
        return 1;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        return insn instanceof SimpleInsn && insn.getRegisters().size() == 0;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        Form10x.write(out, Form10x.opcodeUnit(insn, 0));
    }
}


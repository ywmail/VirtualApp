/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.util.AnnotatedOutput;

public final class SpecialFormat
extends InsnFormat {
    public static final InsnFormat THE_ONE = new SpecialFormat();

    private SpecialFormat() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public int codeSize() {
        throw new RuntimeException("unsupported");
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        return true;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        throw new RuntimeException("unsupported");
    }
}


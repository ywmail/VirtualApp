/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.Dop;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.AnnotatedOutput;

public abstract class FixedSizeInsn
extends DalvInsn {
    public FixedSizeInsn(Dop opcode, SourcePosition position, RegisterSpecList registers) {
        super(opcode, position, registers);
    }

    @Override
    public final int codeSize() {
        return this.getOpcode().getFormat().codeSize();
    }

    @Override
    public final void writeTo(AnnotatedOutput out) {
        this.getOpcode().getFormat().writeTo(out, this);
    }

    @Override
    public final DalvInsn withRegisterOffset(int delta) {
        return this.withRegisters(this.getRegisters().withOffset(delta));
    }

    @Override
    protected final String listingString0(boolean noteIndices) {
        return this.getOpcode().getFormat().listingString(this, noteIndices);
    }
}


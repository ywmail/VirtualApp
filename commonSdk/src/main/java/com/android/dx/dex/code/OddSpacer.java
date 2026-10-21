/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.VariableSizeInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.AnnotatedOutput;

public final class OddSpacer
extends VariableSizeInsn {
    public OddSpacer(SourcePosition position) {
        super(position, RegisterSpecList.EMPTY);
    }

    @Override
    public int codeSize() {
        return this.getAddress() & 1;
    }

    @Override
    public void writeTo(AnnotatedOutput out) {
        if (this.codeSize() != 0) {
            out.writeShort(InsnFormat.codeUnit(0, 0));
        }
    }

    @Override
    public DalvInsn withRegisters(RegisterSpecList registers) {
        return new OddSpacer(this.getPosition());
    }

    @Override
    protected String argString() {
        return null;
    }

    @Override
    protected String listingString0(boolean noteIndices) {
        if (this.codeSize() == 0) {
            return null;
        }
        return "nop // spacer";
    }
}


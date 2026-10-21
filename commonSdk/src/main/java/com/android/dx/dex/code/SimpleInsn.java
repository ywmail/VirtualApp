/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.Dop;
import com.android.dx.dex.code.FixedSizeInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;

public final class SimpleInsn
extends FixedSizeInsn {
    public SimpleInsn(Dop opcode, SourcePosition position, RegisterSpecList registers) {
        super(opcode, position, registers);
    }

    @Override
    public DalvInsn withOpcode(Dop opcode) {
        return new SimpleInsn(opcode, this.getPosition(), this.getRegisters());
    }

    @Override
    public DalvInsn withRegisters(RegisterSpecList registers) {
        return new SimpleInsn(this.getOpcode(), this.getPosition(), registers);
    }

    @Override
    protected String argString() {
        return null;
    }
}


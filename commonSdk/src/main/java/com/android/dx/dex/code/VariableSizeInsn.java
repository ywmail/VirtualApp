/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.Dop;
import com.android.dx.dex.code.Dops;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;

public abstract class VariableSizeInsn
extends DalvInsn {
    public VariableSizeInsn(SourcePosition position, RegisterSpecList registers) {
        super(Dops.SPECIAL_FORMAT, position, registers);
    }

    @Override
    public final DalvInsn withOpcode(Dop opcode) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public final DalvInsn withRegisterOffset(int delta) {
        return this.withRegisters(this.getRegisters().withOffset(delta));
    }
}


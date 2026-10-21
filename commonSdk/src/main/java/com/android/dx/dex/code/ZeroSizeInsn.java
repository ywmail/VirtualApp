/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.Dop;
import com.android.dx.dex.code.Dops;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.AnnotatedOutput;

public abstract class ZeroSizeInsn
extends DalvInsn {
    public ZeroSizeInsn(SourcePosition position) {
        super(Dops.SPECIAL_FORMAT, position, RegisterSpecList.EMPTY);
    }

    @Override
    public final int codeSize() {
        return 0;
    }

    @Override
    public final void writeTo(AnnotatedOutput out) {
    }

    @Override
    public final DalvInsn withOpcode(Dop opcode) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public DalvInsn withRegisterOffset(int delta) {
        return this.withRegisters(this.getRegisters().withOffset(delta));
    }
}


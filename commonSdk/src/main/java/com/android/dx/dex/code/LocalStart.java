/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.ZeroSizeInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.ssa.RegisterMapper;

public final class LocalStart
extends ZeroSizeInsn {
    private final RegisterSpec local;

    public static String localString(RegisterSpec spec) {
        return spec.regString() + ' ' + spec.getLocalItem().toString() + ": " + spec.getTypeBearer().toHuman();
    }

    public LocalStart(SourcePosition position, RegisterSpec local) {
        super(position);
        if (local == null) {
            throw new NullPointerException("local == null");
        }
        this.local = local;
    }

    @Override
    public DalvInsn withRegisterOffset(int delta) {
        return new LocalStart(this.getPosition(), this.local.withOffset(delta));
    }

    @Override
    public DalvInsn withRegisters(RegisterSpecList registers) {
        return new LocalStart(this.getPosition(), this.local);
    }

    public RegisterSpec getLocal() {
        return this.local;
    }

    @Override
    protected String argString() {
        return this.local.toString();
    }

    @Override
    protected String listingString0(boolean noteIndices) {
        return "local-start " + LocalStart.localString(this.local);
    }

    @Override
    public DalvInsn withMapper(RegisterMapper mapper) {
        return new LocalStart(this.getPosition(), mapper.map(this.local));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.LocalStart;
import com.android.dx.dex.code.ZeroSizeInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RegisterSpecSet;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.ssa.RegisterMapper;

public final class LocalSnapshot
extends ZeroSizeInsn {
    private final RegisterSpecSet locals;

    public LocalSnapshot(SourcePosition position, RegisterSpecSet locals) {
        super(position);
        if (locals == null) {
            throw new NullPointerException("locals == null");
        }
        this.locals = locals;
    }

    @Override
    public DalvInsn withRegisterOffset(int delta) {
        return new LocalSnapshot(this.getPosition(), this.locals.withOffset(delta));
    }

    @Override
    public DalvInsn withRegisters(RegisterSpecList registers) {
        return new LocalSnapshot(this.getPosition(), this.locals);
    }

    public RegisterSpecSet getLocals() {
        return this.locals;
    }

    @Override
    protected String argString() {
        return this.locals.toString();
    }

    @Override
    protected String listingString0(boolean noteIndices) {
        int sz = this.locals.size();
        int max = this.locals.getMaxSize();
        StringBuilder sb = new StringBuilder(100 + sz * 40);
        sb.append("local-snapshot");
        for (int i = 0; i < max; ++i) {
            RegisterSpec spec = this.locals.get(i);
            if (spec == null) continue;
            sb.append("\n  ");
            sb.append(LocalStart.localString(spec));
        }
        return sb.toString();
    }

    @Override
    public DalvInsn withMapper(RegisterMapper mapper) {
        return new LocalSnapshot(this.getPosition(), mapper.map(this.locals));
    }
}


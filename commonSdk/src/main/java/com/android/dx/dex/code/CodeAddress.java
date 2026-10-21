/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.ZeroSizeInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;

public final class CodeAddress
extends ZeroSizeInsn {
    private final boolean bindsClosely;

    public CodeAddress(SourcePosition position) {
        this(position, false);
    }

    public CodeAddress(SourcePosition position, boolean bindsClosely) {
        super(position);
        this.bindsClosely = bindsClosely;
    }

    @Override
    public final DalvInsn withRegisters(RegisterSpecList registers) {
        return new CodeAddress(this.getPosition());
    }

    @Override
    protected String argString() {
        return null;
    }

    @Override
    protected String listingString0(boolean noteIndices) {
        return "code-address";
    }

    public boolean getBindsClosely() {
        return this.bindsClosely;
    }
}


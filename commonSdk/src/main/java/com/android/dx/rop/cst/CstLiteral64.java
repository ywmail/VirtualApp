/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstLiteralBits;

public abstract class CstLiteral64
extends CstLiteralBits {
    private final long bits;

    CstLiteral64(long bits) {
        this.bits = bits;
    }

    public final boolean equals(Object other) {
        return other != null && this.getClass() == other.getClass() && this.bits == ((CstLiteral64)other).bits;
    }

    public final int hashCode() {
        return (int)this.bits ^ (int)(this.bits >> 32);
    }

    @Override
    protected int compareTo0(Constant other) {
        long otherBits = ((CstLiteral64)other).bits;
        if (this.bits < otherBits) {
            return -1;
        }
        if (this.bits > otherBits) {
            return 1;
        }
        return 0;
    }

    @Override
    public final boolean isCategory2() {
        return true;
    }

    @Override
    public final boolean fitsInInt() {
        return (long)((int)this.bits) == this.bits;
    }

    @Override
    public final int getIntBits() {
        return (int)this.bits;
    }

    @Override
    public final long getLongBits() {
        return this.bits;
    }
}


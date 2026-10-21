/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.TypedConstant;

public abstract class CstLiteralBits
extends TypedConstant {
    public abstract boolean fitsInInt();

    public abstract int getIntBits();

    public abstract long getLongBits();

    public boolean fitsIn16Bits() {
        if (!this.fitsInInt()) {
            return false;
        }
        int bits = this.getIntBits();
        return (short)bits == bits;
    }

    public boolean fitsIn8Bits() {
        if (!this.fitsInInt()) {
            return false;
        }
        int bits = this.getIntBits();
        return (byte)bits == bits;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.CstLiteral64;
import com.android.dx.rop.type.Type;
import com.android.dx.util.Hex;

public final class CstDouble
extends CstLiteral64 {
    public static final CstDouble VALUE_0 = new CstDouble(Double.doubleToLongBits(0.0));
    public static final CstDouble VALUE_1 = new CstDouble(Double.doubleToLongBits(1.0));

    public static CstDouble make(long bits) {
        return new CstDouble(bits);
    }

    private CstDouble(long bits) {
        super(bits);
    }

    public String toString() {
        long bits = this.getLongBits();
        return "double{0x" + Hex.u8(bits) + " / " + Double.longBitsToDouble(bits) + '}';
    }

    @Override
    public Type getType() {
        return Type.DOUBLE;
    }

    @Override
    public String typeName() {
        return "double";
    }

    @Override
    public String toHuman() {
        return Double.toString(Double.longBitsToDouble(this.getLongBits()));
    }

    public double getValue() {
        return Double.longBitsToDouble(this.getLongBits());
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.CstLiteral32;
import com.android.dx.rop.type.Type;
import com.android.dx.util.Hex;

public final class CstByte
extends CstLiteral32 {
    public static final CstByte VALUE_0 = CstByte.make((byte)0);

    public static CstByte make(byte value) {
        return new CstByte(value);
    }

    public static CstByte make(int value) {
        byte cast = (byte)value;
        if (cast != value) {
            throw new IllegalArgumentException("bogus byte value: " + value);
        }
        return CstByte.make(cast);
    }

    private CstByte(byte value) {
        super(value);
    }

    public String toString() {
        int value = this.getIntBits();
        return "byte{0x" + Hex.u1(value) + " / " + value + '}';
    }

    @Override
    public Type getType() {
        return Type.BYTE;
    }

    @Override
    public String typeName() {
        return "byte";
    }

    @Override
    public String toHuman() {
        return Integer.toString(this.getIntBits());
    }

    public byte getValue() {
        return (byte)this.getIntBits();
    }
}


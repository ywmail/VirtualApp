/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.CstLiteral32;
import com.android.dx.rop.type.Type;
import com.android.dx.util.Hex;

public final class CstShort
extends CstLiteral32 {
    public static final CstShort VALUE_0 = CstShort.make((short)0);

    public static CstShort make(short value) {
        return new CstShort(value);
    }

    public static CstShort make(int value) {
        short cast = (short)value;
        if (cast != value) {
            throw new IllegalArgumentException("bogus short value: " + value);
        }
        return CstShort.make(cast);
    }

    private CstShort(short value) {
        super(value);
    }

    public String toString() {
        int value = this.getIntBits();
        return "short{0x" + Hex.u2(value) + " / " + value + '}';
    }

    @Override
    public Type getType() {
        return Type.SHORT;
    }

    @Override
    public String typeName() {
        return "short";
    }

    @Override
    public String toHuman() {
        return Integer.toString(this.getIntBits());
    }

    public short getValue() {
        return (short)this.getIntBits();
    }
}


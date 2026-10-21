/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.CstLiteral64;
import com.android.dx.rop.type.Type;
import com.android.dx.util.Hex;

public final class CstLong
extends CstLiteral64 {
    public static final CstLong VALUE_0 = CstLong.make(0L);
    public static final CstLong VALUE_1 = CstLong.make(1L);

    public static CstLong make(long value) {
        return new CstLong(value);
    }

    private CstLong(long value) {
        super(value);
    }

    public String toString() {
        long value = this.getLongBits();
        return "long{0x" + Hex.u8(value) + " / " + value + '}';
    }

    @Override
    public Type getType() {
        return Type.LONG;
    }

    @Override
    public String typeName() {
        return "long";
    }

    @Override
    public String toHuman() {
        return Long.toString(this.getLongBits());
    }

    public long getValue() {
        return this.getLongBits();
    }
}


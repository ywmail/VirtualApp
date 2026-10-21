/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.CstLiteral32;
import com.android.dx.rop.type.Type;
import com.android.dx.util.Hex;

public final class CstInteger
extends CstLiteral32 {
    private static final CstInteger[] cache = new CstInteger[511];
    public static final CstInteger VALUE_M1 = CstInteger.make(-1);
    public static final CstInteger VALUE_0 = CstInteger.make(0);
    public static final CstInteger VALUE_1 = CstInteger.make(1);
    public static final CstInteger VALUE_2 = CstInteger.make(2);
    public static final CstInteger VALUE_3 = CstInteger.make(3);
    public static final CstInteger VALUE_4 = CstInteger.make(4);
    public static final CstInteger VALUE_5 = CstInteger.make(5);

    public static CstInteger make(int value) {
        int idx = (value & Integer.MAX_VALUE) % cache.length;
        CstInteger obj = cache[idx];
        if (obj != null && obj.getValue() == value) {
            return obj;
        }
        CstInteger.cache[idx] = obj = new CstInteger(value);
        return obj;
    }

    private CstInteger(int value) {
        super(value);
    }

    public String toString() {
        int value = this.getIntBits();
        return "int{0x" + Hex.u4(value) + " / " + value + '}';
    }

    @Override
    public Type getType() {
        return Type.INT;
    }

    @Override
    public String typeName() {
        return "int";
    }

    @Override
    public String toHuman() {
        return Integer.toString(this.getIntBits());
    }

    public int getValue() {
        return this.getIntBits();
    }
}


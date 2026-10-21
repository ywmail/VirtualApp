/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.CstLiteral32;
import com.android.dx.rop.type.Type;

public final class CstBoolean
extends CstLiteral32 {
    public static final CstBoolean VALUE_FALSE = new CstBoolean(false);
    public static final CstBoolean VALUE_TRUE = new CstBoolean(true);

    public static CstBoolean make(boolean value) {
        return value ? VALUE_TRUE : VALUE_FALSE;
    }

    public static CstBoolean make(int value) {
        if (value == 0) {
            return VALUE_FALSE;
        }
        if (value == 1) {
            return VALUE_TRUE;
        }
        throw new IllegalArgumentException("bogus value: " + value);
    }

    private CstBoolean(boolean value) {
        super(value ? 1 : 0);
    }

    public String toString() {
        return this.getValue() ? "boolean{true}" : "boolean{false}";
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public String typeName() {
        return "boolean";
    }

    @Override
    public String toHuman() {
        return this.getValue() ? "true" : "false";
    }

    public boolean getValue() {
        return this.getIntBits() != 0;
    }
}


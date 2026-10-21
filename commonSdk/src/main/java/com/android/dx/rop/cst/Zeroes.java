/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstBoolean;
import com.android.dx.rop.cst.CstByte;
import com.android.dx.rop.cst.CstChar;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstKnownNull;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstShort;
import com.android.dx.rop.type.Type;

public final class Zeroes {
    private Zeroes() {
    }

    public static Constant zeroFor(Type type) {
        switch (type.getBasicType()) {
            case 1: {
                return CstBoolean.VALUE_FALSE;
            }
            case 2: {
                return CstByte.VALUE_0;
            }
            case 3: {
                return CstChar.VALUE_0;
            }
            case 4: {
                return CstDouble.VALUE_0;
            }
            case 5: {
                return CstFloat.VALUE_0;
            }
            case 6: {
                return CstInteger.VALUE_0;
            }
            case 7: {
                return CstLong.VALUE_0;
            }
            case 8: {
                return CstShort.VALUE_0;
            }
            case 9: {
                return CstKnownNull.THE_ONE;
            }
        }
        throw new UnsupportedOperationException("no zero for type: " + type.toHuman());
    }
}


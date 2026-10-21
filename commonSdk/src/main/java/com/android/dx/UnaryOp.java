/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.TypeId;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;

public enum UnaryOp {
    NOT{

        @Override
        Rop rop(TypeId<?> type) {
            return Rops.opNot(type.ropType);
        }
    }
    ,
    NEGATE{

        @Override
        Rop rop(TypeId<?> type) {
            return Rops.opNeg(type.ropType);
        }
    };


    abstract Rop rop(TypeId<?> var1);
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.type.TypeList;

public enum BinaryOp {
    ADD{

        @Override
        Rop rop(TypeList types) {
            return Rops.opAdd(types);
        }
    }
    ,
    SUBTRACT{

        @Override
        Rop rop(TypeList types) {
            return Rops.opSub(types);
        }
    }
    ,
    MULTIPLY{

        @Override
        Rop rop(TypeList types) {
            return Rops.opMul(types);
        }
    }
    ,
    DIVIDE{

        @Override
        Rop rop(TypeList types) {
            return Rops.opDiv(types);
        }
    }
    ,
    REMAINDER{

        @Override
        Rop rop(TypeList types) {
            return Rops.opRem(types);
        }
    }
    ,
    AND{

        @Override
        Rop rop(TypeList types) {
            return Rops.opAnd(types);
        }
    }
    ,
    OR{

        @Override
        Rop rop(TypeList types) {
            return Rops.opOr(types);
        }
    }
    ,
    XOR{

        @Override
        Rop rop(TypeList types) {
            return Rops.opXor(types);
        }
    }
    ,
    SHIFT_LEFT{

        @Override
        Rop rop(TypeList types) {
            return Rops.opShl(types);
        }
    }
    ,
    SHIFT_RIGHT{

        @Override
        Rop rop(TypeList types) {
            return Rops.opShr(types);
        }
    }
    ,
    UNSIGNED_SHIFT_RIGHT{

        @Override
        Rop rop(TypeList types) {
            return Rops.opUshr(types);
        }
    };


    abstract Rop rop(TypeList var1);
}


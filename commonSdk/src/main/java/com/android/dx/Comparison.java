/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.type.TypeList;

public enum Comparison {
    LT{

        @Override
        Rop rop(TypeList types) {
            return Rops.opIfLt(types);
        }
    }
    ,
    LE{

        @Override
        Rop rop(TypeList types) {
            return Rops.opIfLe(types);
        }
    }
    ,
    EQ{

        @Override
        Rop rop(TypeList types) {
            return Rops.opIfEq(types);
        }
    }
    ,
    GE{

        @Override
        Rop rop(TypeList types) {
            return Rops.opIfGe(types);
        }
    }
    ,
    GT{

        @Override
        Rop rop(TypeList types) {
            return Rops.opIfGt(types);
        }
    }
    ,
    NE{

        @Override
        Rop rop(TypeList types) {
            return Rops.opIfNe(types);
        }
    };


    abstract Rop rop(TypeList var1);
}


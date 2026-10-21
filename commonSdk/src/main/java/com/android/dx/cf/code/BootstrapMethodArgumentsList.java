/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.FixedSizeList;

public class BootstrapMethodArgumentsList
extends FixedSizeList {
    public BootstrapMethodArgumentsList(int count) {
        super(count);
    }

    public Constant get(int n) {
        return (Constant)this.get0(n);
    }

    public void set(int n, Constant cst) {
        if (!(cst instanceof CstString || cst instanceof CstType || cst instanceof CstInteger || cst instanceof CstLong || cst instanceof CstFloat || cst instanceof CstDouble || cst instanceof CstMethodHandle || cst instanceof CstProtoRef)) {
            Class<?> klass = cst.getClass();
            throw new IllegalArgumentException("bad type for bootstrap argument: " + klass);
        }
        this.set0(n, cst);
    }
}


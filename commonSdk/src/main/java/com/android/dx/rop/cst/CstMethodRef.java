/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;

public final class CstMethodRef
extends CstBaseMethodRef {
    public CstMethodRef(CstType definingClass, CstNat nat) {
        super(definingClass, nat);
    }

    @Override
    public String typeName() {
        return "method";
    }
}


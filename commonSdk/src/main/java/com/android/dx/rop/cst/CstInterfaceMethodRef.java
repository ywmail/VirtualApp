/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;

public final class CstInterfaceMethodRef
extends CstBaseMethodRef {
    private CstMethodRef methodRef = null;

    public CstInterfaceMethodRef(CstType definingClass, CstNat nat) {
        super(definingClass, nat);
    }

    @Override
    public String typeName() {
        return "ifaceMethod";
    }

    public CstMethodRef toMethodRef() {
        if (this.methodRef == null) {
            this.methodRef = new CstMethodRef(this.getDefiningClass(), this.getNat());
        }
        return this.methodRef;
    }
}


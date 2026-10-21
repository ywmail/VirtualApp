/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Method;
import com.android.dx.cf.iface.StdMember;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;

public final class StdMethod
extends StdMember
implements Method {
    private final Prototype effectiveDescriptor;

    public StdMethod(CstType definingClass, int accessFlags, CstNat nat, AttributeList attributes) {
        super(definingClass, accessFlags, nat, attributes);
        String descStr = this.getDescriptor().getString();
        this.effectiveDescriptor = Prototype.intern(descStr, definingClass.getClassType(), AccessFlags.isStatic(accessFlags), nat.isInstanceInit());
    }

    @Override
    public Prototype getEffectiveDescriptor() {
        return this.effectiveDescriptor;
    }
}


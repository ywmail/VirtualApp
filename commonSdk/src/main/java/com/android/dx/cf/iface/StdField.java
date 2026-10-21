/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.attrib.AttConstantValue;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Field;
import com.android.dx.cf.iface.StdMember;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.TypedConstant;

public final class StdField
extends StdMember
implements Field {
    public StdField(CstType definingClass, int accessFlags, CstNat nat, AttributeList attributes) {
        super(definingClass, accessFlags, nat, attributes);
    }

    @Override
    public TypedConstant getConstantValue() {
        AttributeList attribs = this.getAttributes();
        AttConstantValue cval = (AttConstantValue)attribs.findFirst("ConstantValue");
        if (cval == null) {
            return null;
        }
        return cval.getConstantValue();
    }
}


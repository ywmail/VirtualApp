/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.HasAttribute;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;

public interface Member
extends HasAttribute {
    public CstType getDefiningClass();

    public int getAccessFlags();

    public CstString getName();

    public CstString getDescriptor();

    public CstNat getNat();

    @Override
    public AttributeList getAttributes();
}


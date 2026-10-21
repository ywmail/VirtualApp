/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Member;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;

public abstract class StdMember
implements Member {
    private final CstType definingClass;
    private final int accessFlags;
    private final CstNat nat;
    private final AttributeList attributes;

    public StdMember(CstType definingClass, int accessFlags, CstNat nat, AttributeList attributes) {
        if (definingClass == null) {
            throw new NullPointerException("definingClass == null");
        }
        if (nat == null) {
            throw new NullPointerException("nat == null");
        }
        if (attributes == null) {
            throw new NullPointerException("attributes == null");
        }
        this.definingClass = definingClass;
        this.accessFlags = accessFlags;
        this.nat = nat;
        this.attributes = attributes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(this.nat.toHuman());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public final CstType getDefiningClass() {
        return this.definingClass;
    }

    @Override
    public final int getAccessFlags() {
        return this.accessFlags;
    }

    @Override
    public final CstNat getNat() {
        return this.nat;
    }

    @Override
    public final CstString getName() {
        return this.nat.getName();
    }

    @Override
    public final CstString getDescriptor() {
        return this.nat.getDescriptor();
    }

    @Override
    public final AttributeList getAttributes() {
        return this.attributes;
    }
}


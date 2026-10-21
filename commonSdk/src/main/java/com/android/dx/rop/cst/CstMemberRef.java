/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.TypedConstant;

public abstract class CstMemberRef
extends TypedConstant {
    private final CstType definingClass;
    private final CstNat nat;

    CstMemberRef(CstType definingClass, CstNat nat) {
        if (definingClass == null) {
            throw new NullPointerException("definingClass == null");
        }
        if (nat == null) {
            throw new NullPointerException("nat == null");
        }
        this.definingClass = definingClass;
        this.nat = nat;
    }

    public final boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        CstMemberRef otherRef = (CstMemberRef)other;
        return this.definingClass.equals(otherRef.definingClass) && this.nat.equals(otherRef.nat);
    }

    public final int hashCode() {
        return this.definingClass.hashCode() * 31 ^ this.nat.hashCode();
    }

    @Override
    protected int compareTo0(Constant other) {
        CstMemberRef otherMember = (CstMemberRef)other;
        int cmp = this.definingClass.compareTo(otherMember.definingClass);
        if (cmp != 0) {
            return cmp;
        }
        CstString thisName = this.nat.getName();
        CstString otherName = otherMember.nat.getName();
        return thisName.compareTo(otherName);
    }

    public final String toString() {
        return this.typeName() + '{' + this.toHuman() + '}';
    }

    @Override
    public final boolean isCategory2() {
        return false;
    }

    @Override
    public final String toHuman() {
        return this.definingClass.toHuman() + '.' + this.nat.toHuman();
    }

    public final CstType getDefiningClass() {
        return this.definingClass;
    }

    public final CstNat getNat() {
        return this.nat;
    }
}


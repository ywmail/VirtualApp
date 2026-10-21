/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.type.Type;

public final class CstNat
extends Constant {
    public static final CstNat PRIMITIVE_TYPE_NAT = new CstNat(new CstString("TYPE"), new CstString("Ljava/lang/Class;"));
    private final CstString name;
    private final CstString descriptor;

    public CstNat(CstString name, CstString descriptor) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        if (descriptor == null) {
            throw new NullPointerException("descriptor == null");
        }
        this.name = name;
        this.descriptor = descriptor;
    }

    public boolean equals(Object other) {
        if (!(other instanceof CstNat)) {
            return false;
        }
        CstNat otherNat = (CstNat)other;
        return this.name.equals(otherNat.name) && this.descriptor.equals(otherNat.descriptor);
    }

    public int hashCode() {
        return this.name.hashCode() * 31 ^ this.descriptor.hashCode();
    }

    @Override
    protected int compareTo0(Constant other) {
        CstNat otherNat = (CstNat)other;
        int cmp = this.name.compareTo(otherNat.name);
        if (cmp != 0) {
            return cmp;
        }
        return this.descriptor.compareTo(otherNat.descriptor);
    }

    public String toString() {
        return "nat{" + this.toHuman() + '}';
    }

    @Override
    public String typeName() {
        return "nat";
    }

    @Override
    public boolean isCategory2() {
        return false;
    }

    public CstString getName() {
        return this.name;
    }

    public CstString getDescriptor() {
        return this.descriptor;
    }

    @Override
    public String toHuman() {
        return this.name.toHuman() + ':' + this.descriptor.toHuman();
    }

    public Type getFieldType() {
        return Type.intern(this.descriptor.getString());
    }

    public final boolean isInstanceInit() {
        return this.name.getString().equals("<init>");
    }

    public final boolean isClassInit() {
        return this.name.getString().equals("<clinit>");
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.TypeId;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;

public final class FieldId<D, V> {
    final TypeId<D> declaringType;
    final TypeId<V> type;
    final String name;
    final CstNat nat;
    final CstFieldRef constant;

    FieldId(TypeId<D> declaringType, TypeId<V> type, String name) {
        if (declaringType == null || type == null || name == null) {
            throw new NullPointerException();
        }
        this.declaringType = declaringType;
        this.type = type;
        this.name = name;
        this.nat = new CstNat(new CstString(name), new CstString(type.name));
        this.constant = new CstFieldRef(declaringType.constant, this.nat);
    }

    public TypeId<D> getDeclaringType() {
        return this.declaringType;
    }

    public TypeId<V> getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        return o instanceof FieldId && ((FieldId)o).declaringType.equals(this.declaringType) && ((FieldId)o).name.equals(this.name);
    }

    public int hashCode() {
        return this.declaringType.hashCode() + 37 * this.name.hashCode();
    }

    public String toString() {
        return this.declaringType + "." + this.name;
    }
}


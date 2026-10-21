/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.Type;

public final class CstProtoRef
extends TypedConstant {
    private final Prototype prototype;

    public CstProtoRef(Prototype prototype) {
        this.prototype = prototype;
    }

    public static CstProtoRef make(CstString descriptor) {
        Prototype prototype = Prototype.fromDescriptor(descriptor.getString());
        return new CstProtoRef(prototype);
    }

    public boolean equals(Object other) {
        if (!(other instanceof CstProtoRef)) {
            return false;
        }
        CstProtoRef otherCstProtoRef = (CstProtoRef)other;
        return this.getPrototype().equals(otherCstProtoRef.getPrototype());
    }

    public int hashCode() {
        return this.prototype.hashCode();
    }

    @Override
    public boolean isCategory2() {
        return false;
    }

    @Override
    public String typeName() {
        return "proto";
    }

    @Override
    protected int compareTo0(Constant other) {
        CstProtoRef otherCstProtoRef = (CstProtoRef)other;
        return this.prototype.compareTo(otherCstProtoRef.getPrototype());
    }

    @Override
    public String toHuman() {
        return this.prototype.getDescriptor();
    }

    public final String toString() {
        return this.typeName() + "{" + this.toHuman() + '}';
    }

    public Prototype getPrototype() {
        return this.prototype;
    }

    @Override
    public Type getType() {
        return Type.METHOD_TYPE;
    }
}


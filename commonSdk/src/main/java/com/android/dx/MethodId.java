/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.TypeId;
import com.android.dx.TypeList;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.type.Prototype;
import java.util.List;

public final class MethodId<D, R> {
    final TypeId<D> declaringType;
    final TypeId<R> returnType;
    final String name;
    final TypeList parameters;
    final CstNat nat;
    final CstMethodRef constant;

    MethodId(TypeId<D> declaringType, TypeId<R> returnType, String name, TypeList parameters) {
        if (declaringType == null || returnType == null || name == null || parameters == null) {
            throw new NullPointerException();
        }
        this.declaringType = declaringType;
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.nat = new CstNat(new CstString(name), new CstString(this.descriptor(false)));
        this.constant = new CstMethodRef(declaringType.constant, this.nat);
    }

    public TypeId<D> getDeclaringType() {
        return this.declaringType;
    }

    public TypeId<R> getReturnType() {
        return this.returnType;
    }

    public boolean isConstructor() {
        return this.name.equals("<init>");
    }

    public boolean isStaticInitializer() {
        return this.name.equals("<clinit>");
    }

    public String getName() {
        return this.name;
    }

    public List<TypeId<?>> getParameters() {
        return this.parameters.asList();
    }

    String descriptor(boolean includeThis) {
        StringBuilder result = new StringBuilder();
        result.append("(");
        if (includeThis) {
            result.append(this.declaringType.name);
        }
        for (TypeId<?> t : this.parameters.types) {
            result.append(t.name);
        }
        result.append(")");
        result.append(this.returnType.name);
        return result.toString();
    }

    Prototype prototype(boolean includeThis) {
        return Prototype.intern(this.descriptor(includeThis));
    }

    public boolean equals(Object o) {
        return o instanceof MethodId && ((MethodId)o).declaringType.equals(this.declaringType) && ((MethodId)o).name.equals(this.name) && ((MethodId)o).parameters.equals(this.parameters) && ((MethodId)o).returnType.equals(this.returnType);
    }

    public int hashCode() {
        int result = 17;
        result = 31 * result + this.declaringType.hashCode();
        result = 31 * result + this.name.hashCode();
        result = 31 * result + this.parameters.hashCode();
        result = 31 * result + this.returnType.hashCode();
        return result;
    }

    public String toString() {
        return this.declaringType + "." + this.name + "(" + this.parameters + ")";
    }
}


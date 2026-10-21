/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.cf.code.BootstrapMethodArgumentsList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstArray;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.type.Prototype;

public final class CstCallSite
extends CstArray {
    public static CstCallSite make(CstMethodHandle bootstrapHandle, CstNat nat, BootstrapMethodArgumentsList optionalArguments) {
        if (bootstrapHandle == null) {
            throw new NullPointerException("bootstrapMethodHandle == null");
        }
        if (nat == null) {
            throw new NullPointerException("nat == null");
        }
        CstArray.List list = new CstArray.List(3 + optionalArguments.size());
        list.set(0, bootstrapHandle);
        list.set(1, nat.getName());
        list.set(2, new CstProtoRef(Prototype.fromDescriptor(nat.getDescriptor().getString())));
        if (optionalArguments != null) {
            for (int i = 0; i < optionalArguments.size(); ++i) {
                list.set(i + 3, optionalArguments.get(i));
            }
        }
        list.setImmutable();
        return new CstCallSite(list);
    }

    private CstCallSite(CstArray.List list) {
        super(list);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CstCallSite) {
            return this.getList().equals(((CstCallSite)other).getList());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getList().hashCode();
    }

    @Override
    protected int compareTo0(Constant other) {
        return this.getList().compareTo(((CstCallSite)other).getList());
    }

    @Override
    public String toString() {
        return this.getList().toString("call site{", ", ", "}");
    }

    @Override
    public String typeName() {
        return "call site";
    }

    @Override
    public boolean isCategory2() {
        return false;
    }

    @Override
    public String toHuman() {
        return this.getList().toHuman("{", ", ", "}");
    }
}


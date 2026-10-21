/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSite;
import com.android.dx.rop.cst.CstInvokeDynamic;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.Type;

public class CstCallSiteRef
extends Constant {
    private final CstInvokeDynamic invokeDynamic;
    private final int id;

    CstCallSiteRef(CstInvokeDynamic invokeDynamic, int id2) {
        if (invokeDynamic == null) {
            throw new NullPointerException("invokeDynamic == null");
        }
        this.invokeDynamic = invokeDynamic;
        this.id = id2;
    }

    @Override
    public boolean isCategory2() {
        return false;
    }

    @Override
    public String typeName() {
        return "CallSiteRef";
    }

    @Override
    protected int compareTo0(Constant other) {
        CstCallSiteRef o = (CstCallSiteRef)other;
        int result = this.invokeDynamic.compareTo(o.invokeDynamic);
        if (result != 0) {
            return result;
        }
        return Integer.compare(this.id, o.id);
    }

    @Override
    public String toHuman() {
        return this.getCallSite().toHuman();
    }

    public String toString() {
        return this.getCallSite().toString();
    }

    public Prototype getPrototype() {
        return this.invokeDynamic.getPrototype();
    }

    public Type getReturnType() {
        return this.invokeDynamic.getReturnType();
    }

    public CstCallSite getCallSite() {
        return this.invokeDynamic.getCallSite();
    }
}


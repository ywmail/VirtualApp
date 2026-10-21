/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.direct;

import com.android.dx.cf.direct.AttributeFactory;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.MemberListParser;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Member;
import com.android.dx.cf.iface.StdMethod;
import com.android.dx.cf.iface.StdMethodList;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;

final class MethodListParser
extends MemberListParser {
    private final StdMethodList methods = new StdMethodList(this.getCount());

    public MethodListParser(DirectClassFile cf, CstType definer, int offset, AttributeFactory attributeFactory) {
        super(cf, definer, offset, attributeFactory);
    }

    public StdMethodList getList() {
        this.parseIfNecessary();
        return this.methods;
    }

    @Override
    protected String humanName() {
        return "method";
    }

    @Override
    protected String humanAccessFlags(int accessFlags) {
        return AccessFlags.methodString(accessFlags);
    }

    @Override
    protected int getAttributeContext() {
        return 2;
    }

    @Override
    protected Member set(int n, int accessFlags, CstNat nat, AttributeList attributes) {
        StdMethod meth = new StdMethod(this.getDefiner(), accessFlags, nat, attributes);
        this.methods.set(n, meth);
        return meth;
    }
}


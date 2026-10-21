/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.direct;

import com.android.dx.cf.direct.AttributeFactory;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.MemberListParser;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Member;
import com.android.dx.cf.iface.StdField;
import com.android.dx.cf.iface.StdFieldList;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;

final class FieldListParser
extends MemberListParser {
    private final StdFieldList fields = new StdFieldList(this.getCount());

    public FieldListParser(DirectClassFile cf, CstType definer, int offset, AttributeFactory attributeFactory) {
        super(cf, definer, offset, attributeFactory);
    }

    public StdFieldList getList() {
        this.parseIfNecessary();
        return this.fields;
    }

    @Override
    protected String humanName() {
        return "field";
    }

    @Override
    protected String humanAccessFlags(int accessFlags) {
        return AccessFlags.fieldString(accessFlags);
    }

    @Override
    protected int getAttributeContext() {
        return 1;
    }

    @Override
    protected Member set(int n, int accessFlags, CstNat nat, AttributeList attributes) {
        StdField field = new StdField(this.getDefiner(), accessFlags, nat, attributes);
        this.fields.set(n, field);
        return field;
    }
}


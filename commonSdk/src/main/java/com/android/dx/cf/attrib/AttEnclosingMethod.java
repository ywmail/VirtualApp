/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;

public final class AttEnclosingMethod
extends BaseAttribute {
    public static final String ATTRIBUTE_NAME = "EnclosingMethod";
    private final CstType type;
    private final CstNat method;

    public AttEnclosingMethod(CstType type, CstNat method) {
        super(ATTRIBUTE_NAME);
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.type = type;
        this.method = method;
    }

    @Override
    public int byteLength() {
        return 10;
    }

    public CstType getEnclosingClass() {
        return this.type;
    }

    public CstNat getMethod() {
        return this.method;
    }
}


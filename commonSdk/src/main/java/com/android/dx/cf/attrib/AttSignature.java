/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;
import com.android.dx.rop.cst.CstString;

public final class AttSignature
extends BaseAttribute {
    public static final String ATTRIBUTE_NAME = "Signature";
    private final CstString signature;

    public AttSignature(CstString signature) {
        super(ATTRIBUTE_NAME);
        if (signature == null) {
            throw new NullPointerException("signature == null");
        }
        this.signature = signature;
    }

    @Override
    public int byteLength() {
        return 8;
    }

    public CstString getSignature() {
        return this.signature;
    }
}


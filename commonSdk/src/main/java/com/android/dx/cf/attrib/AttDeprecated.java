/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;

public final class AttDeprecated
extends BaseAttribute {
    public static final String ATTRIBUTE_NAME = "Deprecated";

    public AttDeprecated() {
        super(ATTRIBUTE_NAME);
    }

    @Override
    public int byteLength() {
        return 6;
    }
}


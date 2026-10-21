/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;

public final class AttSynthetic
extends BaseAttribute {
    public static final String ATTRIBUTE_NAME = "Synthetic";

    public AttSynthetic() {
        super(ATTRIBUTE_NAME);
    }

    @Override
    public int byteLength() {
        return 6;
    }
}


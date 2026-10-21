/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAnnotations;
import com.android.dx.rop.annotation.Annotations;

public final class AttRuntimeInvisibleAnnotations
extends BaseAnnotations {
    public static final String ATTRIBUTE_NAME = "RuntimeInvisibleAnnotations";

    public AttRuntimeInvisibleAnnotations(Annotations annotations, int byteLength) {
        super(ATTRIBUTE_NAME, annotations, byteLength);
    }
}


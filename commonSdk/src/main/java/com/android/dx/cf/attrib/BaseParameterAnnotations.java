/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.util.MutabilityException;

public abstract class BaseParameterAnnotations
extends BaseAttribute {
    private final AnnotationsList parameterAnnotations;
    private final int byteLength;

    public BaseParameterAnnotations(String attributeName, AnnotationsList parameterAnnotations, int byteLength) {
        super(attributeName);
        try {
            if (parameterAnnotations.isMutable()) {
                throw new MutabilityException("parameterAnnotations.isMutable()");
            }
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("parameterAnnotations == null");
        }
        this.parameterAnnotations = parameterAnnotations;
        this.byteLength = byteLength;
    }

    @Override
    public final int byteLength() {
        return this.byteLength + 6;
    }

    public final AnnotationsList getParameterAnnotations() {
        return this.parameterAnnotations;
    }
}


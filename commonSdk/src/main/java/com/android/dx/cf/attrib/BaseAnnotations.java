/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.util.MutabilityException;

public abstract class BaseAnnotations
extends BaseAttribute {
    private final Annotations annotations;
    private final int byteLength;

    public BaseAnnotations(String attributeName, Annotations annotations, int byteLength) {
        super(attributeName);
        try {
            if (annotations.isMutable()) {
                throw new MutabilityException("annotations.isMutable()");
            }
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("annotations == null");
        }
        this.annotations = annotations;
        this.byteLength = byteLength;
    }

    @Override
    public final int byteLength() {
        return this.byteLength + 6;
    }

    public final Annotations getAnnotations() {
        return this.annotations;
    }
}


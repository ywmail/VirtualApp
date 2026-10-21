/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.MutabilityException;

public final class AttExceptions
extends BaseAttribute {
    public static final String ATTRIBUTE_NAME = "Exceptions";
    private final TypeList exceptions;

    public AttExceptions(TypeList exceptions) {
        super(ATTRIBUTE_NAME);
        try {
            if (exceptions.isMutable()) {
                throw new MutabilityException("exceptions.isMutable()");
            }
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("exceptions == null");
        }
        this.exceptions = exceptions;
    }

    @Override
    public int byteLength() {
        return 8 + this.exceptions.size() * 2;
    }

    public TypeList getExceptions() {
        return this.exceptions;
    }
}


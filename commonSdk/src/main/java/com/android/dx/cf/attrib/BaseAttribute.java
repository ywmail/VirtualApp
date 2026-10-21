/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.iface.Attribute;

public abstract class BaseAttribute
implements Attribute {
    private final String name;

    public BaseAttribute(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}


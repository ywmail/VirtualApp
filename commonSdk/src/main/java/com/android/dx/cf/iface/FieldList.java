/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.Field;

public interface FieldList {
    public boolean isMutable();

    public int size();

    public Field get(int var1);
}


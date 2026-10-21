/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.Field;
import com.android.dx.cf.iface.FieldList;
import com.android.dx.util.FixedSizeList;

public final class StdFieldList
extends FixedSizeList
implements FieldList {
    public StdFieldList(int size) {
        super(size);
    }

    @Override
    public Field get(int n) {
        return (Field)this.get0(n);
    }

    public void set(int n, Field field) {
        this.set0(n, field);
    }
}


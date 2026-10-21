/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.Method;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.util.FixedSizeList;

public final class StdMethodList
extends FixedSizeList
implements MethodList {
    public StdMethodList(int size) {
        super(size);
    }

    @Override
    public Method get(int n) {
        return (Method)this.get0(n);
    }

    public void set(int n, Method method) {
        this.set0(n, method);
    }
}


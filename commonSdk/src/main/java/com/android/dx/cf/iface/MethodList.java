/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.Method;

public interface MethodList {
    public boolean isMutable();

    public int size();

    public Method get(int var1);
}


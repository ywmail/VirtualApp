/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.Attribute;

public interface AttributeList {
    public boolean isMutable();

    public int size();

    public Attribute get(int var1);

    public int byteLength();

    public Attribute findFirst(String var1);

    public Attribute findNext(Attribute var1);
}


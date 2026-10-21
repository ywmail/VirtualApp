/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dx.util.IntIterator;

public interface IntSet {
    public void add(int var1);

    public void remove(int var1);

    public boolean has(int var1);

    public void merge(IntSet var1);

    public int elements();

    public IntIterator iterator();
}


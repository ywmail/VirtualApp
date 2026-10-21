/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.util.ToHuman;

public abstract class Constant
implements ToHuman,
Comparable<Constant> {
    public abstract boolean isCategory2();

    public abstract String typeName();

    @Override
    public final int compareTo(Constant other) {
        Class<?> otherClazz;
        Class<?> clazz = this.getClass();
        if (clazz != (otherClazz = other.getClass())) {
            return clazz.getName().compareTo(otherClazz.getName());
        }
        return this.compareTo0(other);
    }

    protected abstract int compareTo0(Constant var1);
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dx.util.MutabilityControl;
import com.android.dx.util.ToHuman;
import java.util.Arrays;

public class FixedSizeList
extends MutabilityControl
implements ToHuman {
    private Object[] arr;

    public FixedSizeList(int size) {
        super(size != 0);
        try {
            this.arr = new Object[size];
        }
        catch (NegativeArraySizeException ex) {
            throw new IllegalArgumentException("size < 0");
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        FixedSizeList list = (FixedSizeList)other;
        return Arrays.equals(this.arr, list.arr);
    }

    public int hashCode() {
        return Arrays.hashCode(this.arr);
    }

    public String toString() {
        String name = this.getClass().getName();
        return this.toString0(name.substring(name.lastIndexOf(46) + 1) + '{', ", ", "}", false);
    }

    @Override
    public String toHuman() {
        String name = this.getClass().getName();
        return this.toString0(name.substring(name.lastIndexOf(46) + 1) + '{', ", ", "}", true);
    }

    public String toString(String prefix, String separator, String suffix) {
        return this.toString0(prefix, separator, suffix, false);
    }

    public String toHuman(String prefix, String separator, String suffix) {
        return this.toString0(prefix, separator, suffix, true);
    }

    public final int size() {
        return this.arr.length;
    }

    public void shrinkToFit() {
        int sz = this.arr.length;
        int newSz = 0;
        for (int i = 0; i < sz; ++i) {
            if (this.arr[i] == null) continue;
            ++newSz;
        }
        if (sz == newSz) {
            return;
        }
        this.throwIfImmutable();
        Object[] newa = new Object[newSz];
        int at = 0;
        for (int i = 0; i < sz; ++i) {
            Object one = this.arr[i];
            if (one == null) continue;
            newa[at] = one;
            ++at;
        }
        this.arr = newa;
        if (newSz == 0) {
            this.setImmutable();
        }
    }

    protected final Object get0(int n) {
        try {
            Object result = this.arr[n];
            if (result == null) {
                throw new NullPointerException("unset: " + n);
            }
            return result;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            return this.throwIndex(n);
        }
    }

    protected final Object getOrNull0(int n) {
        return this.arr[n];
    }

    protected final void set0(int n, Object obj) {
        this.throwIfImmutable();
        try {
            this.arr[n] = obj;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            this.throwIndex(n);
        }
    }

    private Object throwIndex(int n) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("n < 0");
        }
        throw new IndexOutOfBoundsException("n >= size()");
    }

    private String toString0(String prefix, String separator, String suffix, boolean human) {
        int len = this.arr.length;
        StringBuilder sb = new StringBuilder(len * 10 + 10);
        if (prefix != null) {
            sb.append(prefix);
        }
        for (int i = 0; i < len; ++i) {
            if (i != 0 && separator != null) {
                sb.append(separator);
            }
            if (human) {
                sb.append(((ToHuman)this.arr[i]).toHuman());
                continue;
            }
            sb.append(this.arr[i]);
        }
        if (suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }
}


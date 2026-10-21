/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.util.Hex;
import com.android.dx.util.MutabilityControl;

public final class StdConstantPool
extends MutabilityControl
implements ConstantPool {
    private final Constant[] entries;

    public StdConstantPool(int size) {
        super(size > 1);
        if (size < 1) {
            throw new IllegalArgumentException("size < 1");
        }
        this.entries = new Constant[size];
    }

    @Override
    public int size() {
        return this.entries.length;
    }

    @Override
    public Constant getOrNull(int n) {
        try {
            return this.entries[n];
        }
        catch (IndexOutOfBoundsException ex) {
            return StdConstantPool.throwInvalid(n);
        }
    }

    @Override
    public Constant get0Ok(int n) {
        if (n == 0) {
            return null;
        }
        return this.get(n);
    }

    @Override
    public Constant get(int n) {
        try {
            Constant result = this.entries[n];
            if (result == null) {
                StdConstantPool.throwInvalid(n);
            }
            return result;
        }
        catch (IndexOutOfBoundsException ex) {
            return StdConstantPool.throwInvalid(n);
        }
    }

    @Override
    public Constant[] getEntries() {
        return this.entries;
    }

    public void set(int n, Constant cst) {
        Constant prev;
        boolean cat2;
        this.throwIfImmutable();
        boolean bl = cat2 = cst != null && cst.isCategory2();
        if (n < 1) {
            throw new IllegalArgumentException("n < 1");
        }
        if (cat2) {
            if (n == this.entries.length - 1) {
                throw new IllegalArgumentException("(n == size - 1) && cst.isCategory2()");
            }
            this.entries[n + 1] = null;
        }
        if (cst != null && this.entries[n] == null && (prev = this.entries[n - 1]) != null && prev.isCategory2()) {
            this.entries[n - 1] = null;
        }
        this.entries[n] = cst;
    }

    private static Constant throwInvalid(int idx) {
        throw new ExceptionWithContext("invalid constant pool index " + Hex.u2(idx));
    }
}


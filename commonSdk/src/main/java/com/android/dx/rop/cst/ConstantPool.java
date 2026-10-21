/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;

public interface ConstantPool {
    public int size();

    public Constant get(int var1);

    public Constant get0Ok(int var1);

    public Constant getOrNull(int var1);

    public Constant[] getEntries();
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.instructions.CodeCursor;

public interface CodeOutput
extends CodeCursor {
    public void write(short var1);

    public void write(short var1, short var2);

    public void write(short var1, short var2, short var3);

    public void write(short var1, short var2, short var3, short var4);

    public void write(short var1, short var2, short var3, short var4, short var5);

    public void writeInt(int var1);

    public void writeLong(long var1);

    public void write(byte[] var1);

    public void write(short[] var1);

    public void write(int[] var1);

    public void write(long[] var1);
}


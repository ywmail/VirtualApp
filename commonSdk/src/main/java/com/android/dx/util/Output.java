/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dex.util.ByteOutput;
import com.android.dx.util.ByteArray;

public interface Output
extends ByteOutput {
    public int getCursor();

    public void assertCursor(int var1);

    @Override
    public void writeByte(int var1);

    public void writeShort(int var1);

    public void writeInt(int var1);

    public void writeLong(long var1);

    public int writeUleb128(int var1);

    public int writeSleb128(int var1);

    public void write(ByteArray var1);

    public void write(byte[] var1, int var2, int var3);

    public void write(byte[] var1);

    public void writeZeroes(int var1);

    public void alignTo(int var1);
}


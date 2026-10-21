/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

public interface CodeCursor {
    public int cursor();

    public int baseAddressForCursor();

    public void setBaseAddress(int var1, int var2);
}


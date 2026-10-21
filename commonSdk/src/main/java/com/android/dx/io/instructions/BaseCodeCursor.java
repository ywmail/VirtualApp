/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.instructions.AddressMap;
import com.android.dx.io.instructions.CodeCursor;

public abstract class BaseCodeCursor
implements CodeCursor {
    private final AddressMap baseAddressMap = new AddressMap();
    private int cursor = 0;

    @Override
    public final int cursor() {
        return this.cursor;
    }

    @Override
    public final int baseAddressForCursor() {
        int mapped = this.baseAddressMap.get(this.cursor);
        return mapped >= 0 ? mapped : this.cursor;
    }

    @Override
    public final void setBaseAddress(int targetAddress, int baseAddress) {
        this.baseAddressMap.put(targetAddress, baseAddress);
    }

    protected final void advance(int amount) {
        this.cursor += amount;
    }
}


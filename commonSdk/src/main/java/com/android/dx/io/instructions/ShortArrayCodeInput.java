/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.instructions.BaseCodeCursor;
import com.android.dx.io.instructions.CodeInput;
import java.io.EOFException;

public final class ShortArrayCodeInput
extends BaseCodeCursor
implements CodeInput {
    private final short[] array;

    public ShortArrayCodeInput(short[] array) {
        if (array == null) {
            throw new NullPointerException("array == null");
        }
        this.array = array;
    }

    @Override
    public boolean hasMore() {
        return this.cursor() < this.array.length;
    }

    @Override
    public int read() throws EOFException {
        try {
            short value = this.array[this.cursor()];
            this.advance(1);
            return value & 0xFFFF;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new EOFException();
        }
    }

    @Override
    public int readInt() throws EOFException {
        int short0 = this.read();
        int short1 = this.read();
        return short0 | short1 << 16;
    }

    @Override
    public long readLong() throws EOFException {
        long short0 = this.read();
        long short1 = this.read();
        long short2 = this.read();
        long short3 = this.read();
        return short0 | short1 << 16 | short2 << 32 | short3 << 48;
    }
}


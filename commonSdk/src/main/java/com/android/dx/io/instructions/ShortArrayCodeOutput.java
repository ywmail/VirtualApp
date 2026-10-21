/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.instructions.BaseCodeCursor;
import com.android.dx.io.instructions.CodeOutput;

public final class ShortArrayCodeOutput
extends BaseCodeCursor
implements CodeOutput {
    private final short[] array;

    public ShortArrayCodeOutput(int maxSize) {
        if (maxSize < 0) {
            throw new IllegalArgumentException("maxSize < 0");
        }
        this.array = new short[maxSize];
    }

    public short[] getArray() {
        int cursor = this.cursor();
        if (cursor == this.array.length) {
            return this.array;
        }
        short[] result = new short[cursor];
        System.arraycopy(this.array, 0, result, 0, cursor);
        return result;
    }

    @Override
    public void write(short codeUnit) {
        this.array[this.cursor()] = codeUnit;
        this.advance(1);
    }

    @Override
    public void write(short u0, short u1) {
        this.write(u0);
        this.write(u1);
    }

    @Override
    public void write(short u0, short u1, short u2) {
        this.write(u0);
        this.write(u1);
        this.write(u2);
    }

    @Override
    public void write(short u0, short u1, short u2, short u3) {
        this.write(u0);
        this.write(u1);
        this.write(u2);
        this.write(u3);
    }

    @Override
    public void write(short u0, short u1, short u2, short u3, short u4) {
        this.write(u0);
        this.write(u1);
        this.write(u2);
        this.write(u3);
        this.write(u4);
    }

    @Override
    public void writeInt(int value) {
        this.write((short)value);
        this.write((short)(value >> 16));
    }

    @Override
    public void writeLong(long value) {
        this.write((short)value);
        this.write((short)(value >> 16));
        this.write((short)(value >> 32));
        this.write((short)(value >> 48));
    }

    @Override
    public void write(byte[] data) {
        int value = 0;
        boolean even = true;
        for (byte b : data) {
            if (even) {
                value = b & 0xFF;
                even = false;
                continue;
            }
            this.write((short)(value |= b << 8));
            even = true;
        }
        if (!even) {
            this.write((short)value);
        }
    }

    @Override
    public void write(short[] data) {
        for (short unit : data) {
            this.write(unit);
        }
    }

    @Override
    public void write(int[] data) {
        for (int i : data) {
            this.writeInt(i);
        }
    }

    @Override
    public void write(long[] data) {
        for (long l : data) {
            this.writeLong(l);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public final class ByteArray {
    private final byte[] bytes;
    private final int start;
    private final int size;

    public ByteArray(byte[] bytes, int start, int end) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        if (start < 0) {
            throw new IllegalArgumentException("start < 0");
        }
        if (end < start) {
            throw new IllegalArgumentException("end < start");
        }
        if (end > bytes.length) {
            throw new IllegalArgumentException("end > bytes.length");
        }
        this.bytes = bytes;
        this.start = start;
        this.size = end - start;
    }

    public ByteArray(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    public int size() {
        return this.size;
    }

    public ByteArray slice(int start, int end) {
        this.checkOffsets(start, end);
        byte[] slicedOut = Arrays.copyOfRange(this.bytes, start, end);
        return new ByteArray(slicedOut);
    }

    public int underlyingOffset(int offset) {
        return this.start + offset;
    }

    public int getByte(int off) {
        this.checkOffsets(off, off + 1);
        return this.getByte0(off);
    }

    public int getShort(int off) {
        this.checkOffsets(off, off + 2);
        return this.getByte0(off) << 8 | this.getUnsignedByte0(off + 1);
    }

    public int getInt(int off) {
        this.checkOffsets(off, off + 4);
        return this.getByte0(off) << 24 | this.getUnsignedByte0(off + 1) << 16 | this.getUnsignedByte0(off + 2) << 8 | this.getUnsignedByte0(off + 3);
    }

    public long getLong(int off) {
        this.checkOffsets(off, off + 8);
        int part1 = this.getByte0(off) << 24 | this.getUnsignedByte0(off + 1) << 16 | this.getUnsignedByte0(off + 2) << 8 | this.getUnsignedByte0(off + 3);
        int part2 = this.getByte0(off + 4) << 24 | this.getUnsignedByte0(off + 5) << 16 | this.getUnsignedByte0(off + 6) << 8 | this.getUnsignedByte0(off + 7);
        return (long)part2 & 0xFFFFFFFFL | (long)part1 << 32;
    }

    public int getUnsignedByte(int off) {
        this.checkOffsets(off, off + 1);
        return this.getUnsignedByte0(off);
    }

    public int getUnsignedShort(int off) {
        this.checkOffsets(off, off + 2);
        return this.getUnsignedByte0(off) << 8 | this.getUnsignedByte0(off + 1);
    }

    public void getBytes(byte[] out, int offset) {
        if (out.length - offset < this.size) {
            throw new IndexOutOfBoundsException("(out.length - offset) < size()");
        }
        System.arraycopy(this.bytes, this.start, out, offset, this.size);
    }

    private void checkOffsets(int s, int e) {
        if (s < 0 || e < s || e > this.size) {
            throw new IllegalArgumentException("bad range: " + s + ".." + e + "; actual size " + this.size);
        }
    }

    private int getByte0(int off) {
        return this.bytes[this.start + off];
    }

    private int getUnsignedByte0(int off) {
        return this.bytes[this.start + off] & 0xFF;
    }

    public MyDataInputStream makeDataInputStream() {
        return new MyDataInputStream(this.makeInputStream());
    }

    public MyInputStream makeInputStream() {
        return new MyInputStream();
    }

    public static class MyDataInputStream
    extends DataInputStream {
        private final MyInputStream wrapped;

        public MyDataInputStream(MyInputStream wrapped) {
            super(wrapped);
            this.wrapped = wrapped;
        }
    }

    public class MyInputStream
    extends InputStream {
        private int cursor = 0;
        private int mark = 0;

        @Override
        public int read() throws IOException {
            if (this.cursor >= ByteArray.this.size) {
                return -1;
            }
            int result = ByteArray.this.getUnsignedByte0(this.cursor);
            ++this.cursor;
            return result;
        }

        @Override
        public int read(byte[] arr, int offset, int length) {
            int maxLength;
            if (offset + length > arr.length) {
                length = arr.length - offset;
            }
            if (length > (maxLength = ByteArray.this.size - this.cursor)) {
                length = maxLength;
            }
            System.arraycopy(ByteArray.this.bytes, this.cursor + ByteArray.this.start, arr, offset, length);
            this.cursor += length;
            return length;
        }

        @Override
        public int available() {
            return ByteArray.this.size - this.cursor;
        }

        @Override
        public void mark(int reserve) {
            this.mark = this.cursor;
        }

        @Override
        public void reset() {
            this.cursor = this.mark;
        }

        @Override
        public boolean markSupported() {
            return true;
        }
    }

    public static interface GetCursor {
        public int getCursor();
    }
}


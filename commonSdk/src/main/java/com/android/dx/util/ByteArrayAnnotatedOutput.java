/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dex.Leb128;
import com.android.dex.util.ByteOutput;
import com.android.dex.util.ExceptionWithContext;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import com.android.dx.util.TwoColumnOutput;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

public final class ByteArrayAnnotatedOutput
implements AnnotatedOutput,
ByteOutput {
    private static final int DEFAULT_SIZE = 1000;
    private final boolean stretchy;
    private byte[] data;
    private int cursor;
    private boolean verbose;
    private ArrayList<Annotation> annotations;
    private int annotationWidth;
    private int hexCols;

    public ByteArrayAnnotatedOutput(byte[] data) {
        this(data, false);
    }

    public ByteArrayAnnotatedOutput() {
        this(1000);
    }

    public ByteArrayAnnotatedOutput(int size) {
        this(new byte[size], true);
    }

    private ByteArrayAnnotatedOutput(byte[] data, boolean stretchy) {
        if (data == null) {
            throw new NullPointerException("data == null");
        }
        this.stretchy = stretchy;
        this.data = data;
        this.cursor = 0;
        this.verbose = false;
        this.annotations = null;
        this.annotationWidth = 0;
        this.hexCols = 0;
    }

    public byte[] getArray() {
        return this.data;
    }

    public byte[] toByteArray() {
        byte[] result = new byte[this.cursor];
        System.arraycopy(this.data, 0, result, 0, this.cursor);
        return result;
    }

    @Override
    public int getCursor() {
        return this.cursor;
    }

    @Override
    public void assertCursor(int expectedCursor) {
        if (this.cursor != expectedCursor) {
            throw new ExceptionWithContext("expected cursor " + expectedCursor + "; actual value: " + this.cursor);
        }
    }

    @Override
    public void writeByte(int value) {
        int writeAt = this.cursor;
        int end = writeAt + 1;
        if (this.stretchy) {
            this.ensureCapacity(end);
        } else if (end > this.data.length) {
            ByteArrayAnnotatedOutput.throwBounds();
            return;
        }
        this.data[writeAt] = (byte)value;
        this.cursor = end;
    }

    @Override
    public void writeShort(int value) {
        int writeAt = this.cursor;
        int end = writeAt + 2;
        if (this.stretchy) {
            this.ensureCapacity(end);
        } else if (end > this.data.length) {
            ByteArrayAnnotatedOutput.throwBounds();
            return;
        }
        this.data[writeAt] = (byte)value;
        this.data[writeAt + 1] = (byte)(value >> 8);
        this.cursor = end;
    }

    @Override
    public void writeInt(int value) {
        int writeAt = this.cursor;
        int end = writeAt + 4;
        if (this.stretchy) {
            this.ensureCapacity(end);
        } else if (end > this.data.length) {
            ByteArrayAnnotatedOutput.throwBounds();
            return;
        }
        this.data[writeAt] = (byte)value;
        this.data[writeAt + 1] = (byte)(value >> 8);
        this.data[writeAt + 2] = (byte)(value >> 16);
        this.data[writeAt + 3] = (byte)(value >> 24);
        this.cursor = end;
    }

    @Override
    public void writeLong(long value) {
        int writeAt = this.cursor;
        int end = writeAt + 8;
        if (this.stretchy) {
            this.ensureCapacity(end);
        } else if (end > this.data.length) {
            ByteArrayAnnotatedOutput.throwBounds();
            return;
        }
        int half = (int)value;
        this.data[writeAt] = (byte)half;
        this.data[writeAt + 1] = (byte)(half >> 8);
        this.data[writeAt + 2] = (byte)(half >> 16);
        this.data[writeAt + 3] = (byte)(half >> 24);
        half = (int)(value >> 32);
        this.data[writeAt + 4] = (byte)half;
        this.data[writeAt + 5] = (byte)(half >> 8);
        this.data[writeAt + 6] = (byte)(half >> 16);
        this.data[writeAt + 7] = (byte)(half >> 24);
        this.cursor = end;
    }

    @Override
    public int writeUleb128(int value) {
        if (this.stretchy) {
            this.ensureCapacity(this.cursor + 5);
        }
        int cursorBefore = this.cursor;
        Leb128.writeUnsignedLeb128(this, value);
        return this.cursor - cursorBefore;
    }

    @Override
    public int writeSleb128(int value) {
        if (this.stretchy) {
            this.ensureCapacity(this.cursor + 5);
        }
        int cursorBefore = this.cursor;
        Leb128.writeSignedLeb128(this, value);
        return this.cursor - cursorBefore;
    }

    @Override
    public void write(ByteArray bytes) {
        int blen = bytes.size();
        int writeAt = this.cursor;
        int end = writeAt + blen;
        if (this.stretchy) {
            this.ensureCapacity(end);
        } else if (end > this.data.length) {
            ByteArrayAnnotatedOutput.throwBounds();
            return;
        }
        bytes.getBytes(this.data, writeAt);
        this.cursor = end;
    }

    @Override
    public void write(byte[] bytes, int offset, int length) {
        int writeAt = this.cursor;
        int end = writeAt + length;
        int bytesEnd = offset + length;
        if ((offset | length | end) < 0 || bytesEnd > bytes.length) {
            throw new IndexOutOfBoundsException("bytes.length " + bytes.length + "; " + offset + "..!" + end);
        }
        if (this.stretchy) {
            this.ensureCapacity(end);
        } else if (end > this.data.length) {
            ByteArrayAnnotatedOutput.throwBounds();
            return;
        }
        System.arraycopy(bytes, offset, this.data, writeAt, length);
        this.cursor = end;
    }

    @Override
    public void write(byte[] bytes) {
        this.write(bytes, 0, bytes.length);
    }

    @Override
    public void writeZeroes(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        int end = this.cursor + count;
        if (this.stretchy) {
            this.ensureCapacity(end);
        } else if (end > this.data.length) {
            ByteArrayAnnotatedOutput.throwBounds();
            return;
        }
        Arrays.fill(this.data, this.cursor, end, (byte)0);
        this.cursor = end;
    }

    @Override
    public void alignTo(int alignment) {
        int mask = alignment - 1;
        if (alignment < 0 || (mask & alignment) != 0) {
            throw new IllegalArgumentException("bogus alignment");
        }
        int end = this.cursor + mask & ~mask;
        if (this.stretchy) {
            this.ensureCapacity(end);
        } else if (end > this.data.length) {
            ByteArrayAnnotatedOutput.throwBounds();
            return;
        }
        Arrays.fill(this.data, this.cursor, end, (byte)0);
        this.cursor = end;
    }

    @Override
    public boolean annotates() {
        return this.annotations != null;
    }

    @Override
    public boolean isVerbose() {
        return this.verbose;
    }

    @Override
    public void annotate(String msg) {
        if (this.annotations == null) {
            return;
        }
        this.endAnnotation();
        this.annotations.add(new Annotation(this.cursor, msg));
    }

    @Override
    public void annotate(int amt, String msg) {
        if (this.annotations == null) {
            return;
        }
        this.endAnnotation();
        int asz = this.annotations.size();
        int lastEnd = asz == 0 ? 0 : this.annotations.get(asz - 1).getEnd();
        int startAt = lastEnd <= this.cursor ? this.cursor : lastEnd;
        this.annotations.add(new Annotation(startAt, startAt + amt, msg));
    }

    @Override
    public void endAnnotation() {
        if (this.annotations == null) {
            return;
        }
        int sz = this.annotations.size();
        if (sz != 0) {
            this.annotations.get(sz - 1).setEndIfUnset(this.cursor);
        }
    }

    @Override
    public int getAnnotationWidth() {
        int leftWidth = 8 + this.hexCols * 2 + this.hexCols / 2;
        return this.annotationWidth - leftWidth;
    }

    public void enableAnnotations(int annotationWidth, boolean verbose) {
        if (this.annotations != null || this.cursor != 0) {
            throw new RuntimeException("cannot enable annotations");
        }
        if (annotationWidth < 40) {
            throw new IllegalArgumentException("annotationWidth < 40");
        }
        int hexCols = (annotationWidth - 7) / 15 + 1 & 0xFFFFFFFE;
        if (hexCols < 6) {
            hexCols = 6;
        } else if (hexCols > 10) {
            hexCols = 10;
        }
        this.annotations = new ArrayList(1000);
        this.annotationWidth = annotationWidth;
        this.hexCols = hexCols;
        this.verbose = verbose;
    }

    public void finishAnnotating() {
        this.endAnnotation();
        if (this.annotations != null) {
            for (int asz = this.annotations.size(); asz > 0; --asz) {
                Annotation last = this.annotations.get(asz - 1);
                if (last.getStart() > this.cursor) {
                    this.annotations.remove(asz - 1);
                    continue;
                }
                if (last.getEnd() <= this.cursor) break;
                last.setEnd(this.cursor);
                break;
            }
        }
    }

    public void writeAnnotationsTo(Writer out) throws IOException {
        int width2 = this.getAnnotationWidth();
        int width1 = this.annotationWidth - width2 - 1;
        TwoColumnOutput twoc = new TwoColumnOutput(out, width1, width2, "|");
        Writer left = twoc.getLeft();
        Writer right = twoc.getRight();
        int leftAt = 0;
        int rightAt = 0;
        int rightSz = this.annotations.size();
        while (leftAt < this.cursor && rightAt < rightSz) {
            String text;
            int end;
            Annotation a = this.annotations.get(rightAt);
            int start = a.getStart();
            if (leftAt < start) {
                end = start;
                start = leftAt;
                text = "";
            } else {
                end = a.getEnd();
                text = a.getText();
                ++rightAt;
            }
            left.write(Hex.dump(this.data, start, end - start, start, this.hexCols, 6));
            right.write(text);
            twoc.flush();
            leftAt = end;
        }
        if (leftAt < this.cursor) {
            left.write(Hex.dump(this.data, leftAt, this.cursor - leftAt, leftAt, this.hexCols, 6));
        }
        while (rightAt < rightSz) {
            right.write(this.annotations.get(rightAt).getText());
            ++rightAt;
        }
        twoc.flush();
    }

    private static void throwBounds() {
        throw new IndexOutOfBoundsException("attempt to write past the end");
    }

    private void ensureCapacity(int desiredSize) {
        if (this.data.length < desiredSize) {
            byte[] newData = new byte[desiredSize * 2 + 1000];
            System.arraycopy(this.data, 0, newData, 0, this.cursor);
            this.data = newData;
        }
    }

    private static class Annotation {
        private final int start;
        private int end;
        private final String text;

        public Annotation(int start, int end, String text) {
            this.start = start;
            this.end = end;
            this.text = text;
        }

        public Annotation(int start, String text) {
            this(start, Integer.MAX_VALUE, text);
        }

        public void setEndIfUnset(int end) {
            if (this.end == Integer.MAX_VALUE) {
                this.end = end;
            }
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public String getText() {
            return this.text;
        }
    }
}


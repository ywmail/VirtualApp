/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.server.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class MemoryValue {
    private static final ByteOrder BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    public abstract byte[] toBytes();

    public static class INT8
    extends MemoryValue {
        private long val;

        public INT8(long val) {
            this.val = val;
        }

        @Override
        public byte[] toBytes() {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            return buffer.order(BYTE_ORDER).putLong(this.val).array();
        }
    }

    public static class INT4
    extends MemoryValue {
        private int val;

        public INT4(int val) {
            this.val = val;
        }

        @Override
        public byte[] toBytes() {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            return buffer.order(BYTE_ORDER).putInt(this.val).array();
        }
    }

    public static class INT2
    extends MemoryValue {
        private short val;

        public INT2(short val) {
            this.val = val;
        }

        @Override
        public byte[] toBytes() {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            return buffer.putShort(this.val).order(BYTE_ORDER).array();
        }
    }

    public static enum ValueType {
        INT2,
        INT4,
        INT8;

    }
}


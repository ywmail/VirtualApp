/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex;

import com.android.dex.DexException;
import com.android.dex.util.ByteInput;
import com.android.dex.util.ByteOutput;

public final class Leb128 {
    private Leb128() {
    }

    public static int unsignedLeb128Size(int value) {
        int remaining = value >> 7;
        int count = 0;
        while (remaining != 0) {
            remaining >>= 7;
            ++count;
        }
        return count + 1;
    }

    public static int readSignedLeb128(ByteInput in) {
        int cur;
        int result = 0;
        int count = 0;
        int signBits = -1;
        do {
            cur = in.readByte() & 0xFF;
            result |= (cur & 0x7F) << count * 7;
            signBits <<= 7;
        } while ((cur & 0x80) == 128 && ++count < 5);
        if ((cur & 0x80) == 128) {
            throw new DexException("invalid LEB128 sequence");
        }
        if ((signBits >> 1 & result) != 0) {
            result |= signBits;
        }
        return result;
    }

    public static int readUnsignedLeb128(ByteInput in) {
        int cur;
        int result = 0;
        int count = 0;
        do {
            cur = in.readByte() & 0xFF;
            result |= (cur & 0x7F) << count * 7;
        } while ((cur & 0x80) == 128 && ++count < 5);
        if ((cur & 0x80) == 128) {
            throw new DexException("invalid LEB128 sequence");
        }
        return result;
    }

    public static void writeUnsignedLeb128(ByteOutput out, int value) {
        for (int remaining = value >>> 7; remaining != 0; remaining >>>= 7) {
            out.writeByte((byte)(value & 0x7F | 0x80));
            value = remaining;
        }
        out.writeByte((byte)(value & 0x7F));
    }

    public static void writeSignedLeb128(ByteOutput out, int value) {
        int end;
        int remaining = value >> 7;
        boolean hasMore = true;
        int n = end = (value & Integer.MIN_VALUE) == 0 ? 0 : -1;
        while (hasMore) {
            hasMore = remaining != end || (remaining & 1) != (value >> 6 & 1);
            out.writeByte((byte)(value & 0x7F | (hasMore ? 128 : 0)));
            value = remaining;
            remaining >>= 7;
        }
    }
}


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.android.dex;

import com.android.dex.util.ByteInput;
import com.android.dex.util.ByteOutput;

public final class EncodedValueCodec {
    private EncodedValueCodec() {
    }

    public static void writeSignedIntegralValue(ByteOutput out, int type, long value) {
        int requiredBits = 65 - Long.numberOfLeadingZeros(value ^ value >> 63);
        int requiredBytes = requiredBits + 7 >> 3;
        out.writeByte(type | requiredBytes - 1 << 5);

        while(requiredBytes > 0) {
            out.writeByte((byte)((int)value));
            value >>= 8;
            --requiredBytes;
        }

    }

    public static void writeUnsignedIntegralValue(ByteOutput out, int type, long value) {
        int requiredBits = 64 - Long.numberOfLeadingZeros(value);
        if (requiredBits == 0) {
            requiredBits = 1;
        }

        int requiredBytes = requiredBits + 7 >> 3;
        out.writeByte(type | requiredBytes - 1 << 5);

        while(requiredBytes > 0) {
            out.writeByte((byte)((int)value));
            value >>= 8;
            --requiredBytes;
        }

    }

    public static void writeRightZeroExtendedValue(ByteOutput out, int type, long value) {
        int requiredBits = 64 - Long.numberOfTrailingZeros(value);
        if (requiredBits == 0) {
            requiredBits = 1;
        }

        int requiredBytes = requiredBits + 7 >> 3;
        value >>= 64 - requiredBytes * 8;
        out.writeByte(type | requiredBytes - 1 << 5);

        while(requiredBytes > 0) {
            out.writeByte((byte)((int)value));
            value >>= 8;
            --requiredBytes;
        }

    }

    public static int readSignedInt(ByteInput in, int zwidth) {
        int result = 0;

        for(int i = zwidth; i >= 0; --i) {
            result = result >>> 8 | (in.readByte() & 255) << 24;
        }

        result >>= (3 - zwidth) * 8;
        return result;
    }

    public static int readUnsignedInt(ByteInput in, int zwidth, boolean fillOnRight) {
        int result = 0;
        int i;
        if (!fillOnRight) {
            for(i = zwidth; i >= 0; --i) {
                result = result >>> 8 | (in.readByte() & 255) << 24;
            }

            result >>>= (3 - zwidth) * 8;
        } else {
            for(i = zwidth; i >= 0; --i) {
                result = result >>> 8 | (in.readByte() & 255) << 24;
            }
        }

        return result;
    }

    public static long readSignedLong(ByteInput in, int zwidth) {
        long result = 0L;

        for(int i = zwidth; i >= 0; --i) {
            result = result >>> 8 | ((long)in.readByte() & 255L) << 56;
        }

        result >>= (7 - zwidth) * 8;
        return result;
    }

    public static long readUnsignedLong(ByteInput in, int zwidth, boolean fillOnRight) {
        long result = 0L;
        int i;
        if (!fillOnRight) {
            for(i = zwidth; i >= 0; --i) {
                result = result >>> 8 | ((long)in.readByte() & 255L) << 56;
            }

            result >>>= (7 - zwidth) * 8;
        } else {
            for(i = zwidth; i >= 0; --i) {
                result = result >>> 8 | ((long)in.readByte() & 255L) << 56;
            }
        }

        return result;
    }
}

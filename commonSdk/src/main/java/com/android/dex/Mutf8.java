/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex;

import com.android.dex.util.ByteInput;
import java.io.UTFDataFormatException;

public final class Mutf8 {
    private Mutf8() {
    }

    public static String decode(ByteInput in, char[] out) throws UTFDataFormatException {
        int s = 0;
        while (true) {
            int b;
            char a;
            if ((a = (char)(in.readByte() & 0xFF)) == '\u0000') {
                return new String(out, 0, s);
            }
            out[s] = a;
            if (a < '\u0080') {
                ++s;
                continue;
            }
            if ((a & 0xE0) == 192) {
                b = in.readByte() & 0xFF;
                if ((b & 0xC0) != 128) {
                    throw new UTFDataFormatException("bad second byte");
                }
                out[s++] = (char)((a & 0x1F) << 6 | b & 0x3F);
                continue;
            }
            if ((a & 0xF0) != 224) break;
            b = in.readByte() & 0xFF;
            int c = in.readByte() & 0xFF;
            if ((b & 0xC0) != 128 || (c & 0xC0) != 128) {
                throw new UTFDataFormatException("bad second or third byte");
            }
            out[s++] = (char)((a & 0xF) << 12 | (b & 0x3F) << 6 | c & 0x3F);
        }
        throw new UTFDataFormatException("bad byte");
    }

    private static long countBytes(String s, boolean shortLength) throws UTFDataFormatException {
        long result = 0L;
        int length = s.length();
        for (int i = 0; i < length; ++i) {
            char ch = s.charAt(i);
            result = ch != '\u0000' && ch <= '\u007f' ? ++result : (ch <= '\u07ff' ? (result += 2L) : (result += 3L));
            if (!shortLength || result <= 65535L) continue;
            throw new UTFDataFormatException("String more than 65535 UTF bytes long");
        }
        return result;
    }

    public static void encode(byte[] dst, int offset, String s) {
        int length = s.length();
        for (int i = 0; i < length; ++i) {
            char ch = s.charAt(i);
            if (ch != '\u0000' && ch <= '\u007f') {
                dst[offset++] = (byte)ch;
                continue;
            }
            if (ch <= '\u07ff') {
                dst[offset++] = (byte)(0xC0 | 0x1F & ch >> 6);
                dst[offset++] = (byte)(0x80 | 0x3F & ch);
                continue;
            }
            dst[offset++] = (byte)(0xE0 | 0xF & ch >> 12);
            dst[offset++] = (byte)(0x80 | 0x3F & ch >> 6);
            dst[offset++] = (byte)(0x80 | 0x3F & ch);
        }
    }

    public static byte[] encode(String s) throws UTFDataFormatException {
        int utfCount = (int)Mutf8.countBytes(s, true);
        byte[] result = new byte[utfCount];
        Mutf8.encode(result, 0, s);
        return result;
    }
}


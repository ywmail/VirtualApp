/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.type.Type;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;

public final class CstString
extends TypedConstant {
    public static final CstString EMPTY_STRING = new CstString("");
    private final String string;
    private final ByteArray bytes;

    public static byte[] stringToUtf8Bytes(String string) {
        int len = string.length();
        byte[] bytes = new byte[len * 3];
        int outAt = 0;
        for (int i = 0; i < len; ++i) {
            char c = string.charAt(i);
            if (c != '\u0000' && c < '\u0080') {
                bytes[outAt] = (byte)c;
                ++outAt;
                continue;
            }
            if (c < '\u0800') {
                bytes[outAt] = (byte)(c >> 6 & 0x1F | 0xC0);
                bytes[outAt + 1] = (byte)(c & 0x3F | 0x80);
                outAt += 2;
                continue;
            }
            bytes[outAt] = (byte)(c >> 12 & 0xF | 0xE0);
            bytes[outAt + 1] = (byte)(c >> 6 & 0x3F | 0x80);
            bytes[outAt + 2] = (byte)(c & 0x3F | 0x80);
            outAt += 3;
        }
        byte[] result = new byte[outAt];
        System.arraycopy(bytes, 0, result, 0, outAt);
        return result;
    }

    public static String utf8BytesToString(ByteArray bytes) {
        int length = bytes.size();
        char[] chars = new char[length];
        int outAt = 0;
        int at = 0;
        while (length > 0) {
            char out;
            int v0 = bytes.getUnsignedByte(at);
            switch (v0 >> 4) {
                case 0: 
                case 1: 
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: 
                case 7: {
                    --length;
                    if (v0 == 0) {
                        return CstString.throwBadUtf8(v0, at);
                    }
                    out = (char)v0;
                    ++at;
                    break;
                }
                case 12: 
                case 13: {
                    if ((length -= 2) < 0) {
                        return CstString.throwBadUtf8(v0, at);
                    }
                    int v1 = bytes.getUnsignedByte(at + 1);
                    if ((v1 & 0xC0) != 128) {
                        return CstString.throwBadUtf8(v1, at + 1);
                    }
                    int value = (v0 & 0x1F) << 6 | v1 & 0x3F;
                    if (value != 0 && value < 128) {
                        return CstString.throwBadUtf8(v1, at + 1);
                    }
                    out = (char)value;
                    at += 2;
                    break;
                }
                case 14: {
                    if ((length -= 3) < 0) {
                        return CstString.throwBadUtf8(v0, at);
                    }
                    int v1 = bytes.getUnsignedByte(at + 1);
                    if ((v1 & 0xC0) != 128) {
                        return CstString.throwBadUtf8(v1, at + 1);
                    }
                    int v2 = bytes.getUnsignedByte(at + 2);
                    if ((v1 & 0xC0) != 128) {
                        return CstString.throwBadUtf8(v2, at + 2);
                    }
                    int value = (v0 & 0xF) << 12 | (v1 & 0x3F) << 6 | v2 & 0x3F;
                    if (value < 2048) {
                        return CstString.throwBadUtf8(v2, at + 2);
                    }
                    out = (char)value;
                    at += 3;
                    break;
                }
                default: {
                    return CstString.throwBadUtf8(v0, at);
                }
            }
            chars[outAt] = out;
            ++outAt;
        }
        return new String(chars, 0, outAt);
    }

    private static String throwBadUtf8(int value, int offset) {
        throw new IllegalArgumentException("bad utf-8 byte " + Hex.u1(value) + " at offset " + Hex.u4(offset));
    }

    public CstString(String string) {
        if (string == null) {
            throw new NullPointerException("string == null");
        }
        this.string = string.intern();
        this.bytes = new ByteArray(CstString.stringToUtf8Bytes(string));
    }

    public CstString(ByteArray bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        this.bytes = bytes;
        this.string = CstString.utf8BytesToString(bytes).intern();
    }

    public boolean equals(Object other) {
        if (!(other instanceof CstString)) {
            return false;
        }
        return this.string.equals(((CstString)other).string);
    }

    public int hashCode() {
        return this.string.hashCode();
    }

    @Override
    protected int compareTo0(Constant other) {
        return this.string.compareTo(((CstString)other).string);
    }

    public String toString() {
        return "string{\"" + this.toHuman() + "\"}";
    }

    @Override
    public String typeName() {
        return "utf8";
    }

    @Override
    public boolean isCategory2() {
        return false;
    }

    @Override
    public String toHuman() {
        int len = this.string.length();
        StringBuilder sb = new StringBuilder(len * 3 / 2);
        block5: for (int i = 0; i < len; ++i) {
            char c = this.string.charAt(i);
            if (c >= ' ' && c < '\u007f') {
                if (c == '\'' || c == '\"' || c == '\\') {
                    sb.append('\\');
                }
                sb.append(c);
                continue;
            }
            if (c <= '\u007f') {
                switch (c) {
                    case '\n': {
                        sb.append("\\n");
                        break;
                    }
                    case '\r': {
                        sb.append("\\r");
                        break;
                    }
                    case '\t': {
                        sb.append("\\t");
                        break;
                    }
                    default: {
                        int nextChar = i < len - 1 ? (int)this.string.charAt(i + 1) : 0;
                        boolean displayZero = nextChar >= 48 && nextChar <= 55;
                        sb.append('\\');
                        for (int shift = 6; shift >= 0; shift -= 3) {
                            char outChar = (char)((c >> shift & 7) + 48);
                            if (outChar == '0' && !displayZero) continue;
                            sb.append(outChar);
                            displayZero = true;
                        }
                        if (displayZero) continue block5;
                        sb.append('0');
                        break;
                    }
                }
                continue;
            }
            sb.append("\\u");
            sb.append(Character.forDigit(c >> 12, 16));
            sb.append(Character.forDigit(c >> 8 & 0xF, 16));
            sb.append(Character.forDigit(c >> 4 & 0xF, 16));
            sb.append(Character.forDigit(c & 0xF, 16));
        }
        return sb.toString();
    }

    public String toQuoted() {
        return '\"' + this.toHuman() + '\"';
    }

    public String toQuoted(int maxLength) {
        String ellipses;
        String string = this.toHuman();
        int length = string.length();
        if (length <= maxLength - 2) {
            ellipses = "";
        } else {
            string = string.substring(0, maxLength - 5);
            ellipses = "...";
        }
        return '\"' + string + ellipses + '\"';
    }

    public String getString() {
        return this.string;
    }

    public ByteArray getBytes() {
        return this.bytes;
    }

    public int getUtf8Size() {
        return this.bytes.size();
    }

    public int getUtf16Size() {
        return this.string.length();
    }

    @Override
    public Type getType() {
        return Type.STRING;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dx.util.Hex;

public final class HexParser {
    private HexParser() {
    }

    public static byte[] parse(String src) {
        int len = src.length();
        byte[] result = new byte[len / 2];
        int at = 0;
        int outAt = 0;
        while (at < len) {
            int quoteAt;
            int poundAt;
            int nlAt = src.indexOf(10, at);
            if (nlAt < 0) {
                nlAt = len;
            }
            String line = (poundAt = src.indexOf(35, at)) >= 0 && poundAt < nlAt ? src.substring(at, poundAt) : src.substring(at, nlAt);
            at = nlAt + 1;
            int colonAt = line.indexOf(58);
            if (colonAt != -1 && ((quoteAt = line.indexOf(34)) == -1 || quoteAt >= colonAt)) {
                String atStr = line.substring(0, colonAt).trim();
                line = line.substring(colonAt + 1);
                int alleged = Integer.parseInt(atStr, 16);
                if (alleged != outAt) {
                    throw new RuntimeException("bogus offset marker: " + atStr);
                }
            }
            int lineLen = line.length();
            int value = -1;
            boolean quoteMode = false;
            for (int i = 0; i < lineLen; ++i) {
                char c = line.charAt(i);
                if (quoteMode) {
                    if (c == '\"') {
                        quoteMode = false;
                        continue;
                    }
                    result[outAt] = (byte)c;
                    ++outAt;
                    continue;
                }
                if (c <= ' ') continue;
                if (c == '\"') {
                    if (value != -1) {
                        throw new RuntimeException("spare digit around offset " + Hex.u4(outAt));
                    }
                    quoteMode = true;
                    continue;
                }
                int digVal = Character.digit(c, 16);
                if (digVal == -1) {
                    throw new RuntimeException("bogus digit character: \"" + c + "\"");
                }
                if (value == -1) {
                    value = digVal;
                    continue;
                }
                result[outAt] = (byte)(value << 4 | digVal);
                ++outAt;
                value = -1;
            }
            if (value != -1) {
                throw new RuntimeException("spare digit around offset " + Hex.u4(outAt));
            }
            if (!quoteMode) continue;
            throw new RuntimeException("unterminated quote around offset " + Hex.u4(outAt));
        }
        if (outAt < result.length) {
            byte[] newr = new byte[outAt];
            System.arraycopy(result, 0, newr, 0, outAt);
            result = newr;
        }
        return result;
    }
}


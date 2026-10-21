/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.utils;

import com.lody.virtual.StringFog;

public class StringToAscii {
    private static String toHexUtil(int n) {
        String rt = "";
        switch (n) {
            case 10: {
                rt = rt + "A";
                break;
            }
            case 11: {
                rt = rt + "B";
                break;
            }
            case 12: {
                rt = rt + "C";
                break;
            }
            case 13: {
                rt = rt + "D";
                break;
            }
            case 14: {
                rt = rt + "E";
                break;
            }
            case 15: {
                rt = rt + "F";
                break;
            }
            default: {
                rt = rt + n;
            }
        }
        return rt;
    }

    public static String toHex(int n) {
        StringBuilder sb = new StringBuilder();
        if (n / 16 == 0) {
            return StringToAscii.toHexUtil(n);
        }
        String t = StringToAscii.toHex(n / 16);
        int nn = n % 16;
        sb.append(t).append(StringToAscii.toHexUtil(nn));
        return sb.toString();
    }

    public static String parseAscii(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (int i = 0; i < bs.length; ++i) {
            sb.append(StringToAscii.toHex(bs[i]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String s = "xyz";
        System.out.println("转换后的字符串是：" + StringToAscii.parseAscii(s));
    }
}


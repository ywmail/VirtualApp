/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex.util;

public final class Unsigned {
    private Unsigned() {
    }

    public static int compare(short ushortA, short ushortB) {
        if (ushortA == ushortB) {
            return 0;
        }
        int a = ushortA & 0xFFFF;
        int b = ushortB & 0xFFFF;
        return a < b ? -1 : 1;
    }

    public static int compare(int uintA, int uintB) {
        if (uintA == uintB) {
            return 0;
        }
        long a = (long)uintA & 0xFFFFFFFFL;
        long b = (long)uintB & 0xFFFFFFFFL;
        return a < b ? -1 : 1;
    }
}


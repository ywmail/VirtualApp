/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

public final class Bits {
    private Bits() {
    }

    public static int[] makeBitSet(int max) {
        int size = max + 31 >> 5;
        return new int[size];
    }

    public static int getMax(int[] bits) {
        return bits.length * 32;
    }

    public static boolean get(int[] bits, int idx) {
        int arrayIdx = idx >> 5;
        int bit = 1 << (idx & 0x1F);
        return (bits[arrayIdx] & bit) != 0;
    }

    public static void set(int[] bits, int idx, boolean value) {
        int arrayIdx = idx >> 5;
        int bit = 1 << (idx & 0x1F);
        if (value) {
            int n = arrayIdx;
            bits[n] = bits[n] | bit;
        } else {
            int n = arrayIdx;
            bits[n] = bits[n] & ~bit;
        }
    }

    public static void set(int[] bits, int idx) {
        int arrayIdx = idx >> 5;
        int bit = 1 << (idx & 0x1F);
        int n = arrayIdx;
        bits[n] = bits[n] | bit;
    }

    public static void clear(int[] bits, int idx) {
        int arrayIdx = idx >> 5;
        int bit = 1 << (idx & 0x1F);
        int n = arrayIdx;
        bits[n] = bits[n] & ~bit;
    }

    public static boolean isEmpty(int[] bits) {
        int len = bits.length;
        for (int i = 0; i < len; ++i) {
            if (bits[i] == 0) continue;
            return false;
        }
        return true;
    }

    public static int bitCount(int[] bits) {
        int len = bits.length;
        int count = 0;
        for (int i = 0; i < len; ++i) {
            count += Integer.bitCount(bits[i]);
        }
        return count;
    }

    public static boolean anyInRange(int[] bits, int start, int end) {
        int idx = Bits.findFirst(bits, start);
        return idx >= 0 && idx < end;
    }

    public static int findFirst(int[] bits, int idx) {
        int len = bits.length;
        int minBit = idx & 0x1F;
        for (int arrayIdx = idx >> 5; arrayIdx < len; ++arrayIdx) {
            int bitIdx;
            int word = bits[arrayIdx];
            if (word != 0 && (bitIdx = Bits.findFirst(word, minBit)) >= 0) {
                return (arrayIdx << 5) + bitIdx;
            }
            minBit = 0;
        }
        return -1;
    }

    public static int findFirst(int value, int idx) {
        int result = Integer.numberOfTrailingZeros(value &= ~((1 << idx) - 1));
        return result == 32 ? -1 : result;
    }

    public static void or(int[] a, int[] b) {
        for (int i = 0; i < b.length; ++i) {
            int n = i;
            a[n] = a[n] | b[i];
        }
    }

    public static String toHuman(int[] bits) {
        StringBuilder sb = new StringBuilder();
        boolean needsComma = false;
        sb.append('{');
        int bitsLength = 32 * bits.length;
        for (int i = 0; i < bitsLength; ++i) {
            if (!Bits.get(bits, i)) continue;
            if (needsComma) {
                sb.append(',');
            }
            needsComma = true;
            sb.append(i);
        }
        sb.append('}');
        return sb.toString();
    }
}


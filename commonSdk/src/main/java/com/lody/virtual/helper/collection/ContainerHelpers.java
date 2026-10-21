/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.collection;

class ContainerHelpers {
    static final int[] EMPTY_INTS = new int[0];
    static final long[] EMPTY_LONGS = new long[0];
    static final Object[] EMPTY_OBJECTS = new Object[0];

    ContainerHelpers() {
    }

    public static int idealIntArraySize(int need) {
        return ContainerHelpers.idealByteArraySize(need * 4) / 4;
    }

    public static int idealLongArraySize(int need) {
        return ContainerHelpers.idealByteArraySize(need * 8) / 8;
    }

    public static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; ++i) {
            if (need > (1 << i) - 12) continue;
            return (1 << i) - 12;
        }
        return need;
    }

    public static boolean equal(Object a, Object b) {
        return a == b || a != null && a.equals(b);
    }

    static int binarySearch(int[] array2, int size, int value) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            int mid = lo + hi >>> 1;
            int midVal = array2[mid];
            if (midVal < value) {
                lo = mid + 1;
                continue;
            }
            if (midVal > value) {
                hi = mid - 1;
                continue;
            }
            return mid;
        }
        return ~lo;
    }

    static int binarySearch(long[] array2, int size, long value) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            int mid = lo + hi >>> 1;
            long midVal = array2[mid];
            if (midVal < value) {
                lo = mid + 1;
                continue;
            }
            if (midVal > value) {
                hi = mid - 1;
                continue;
            }
            return mid;
        }
        return ~lo;
    }
}


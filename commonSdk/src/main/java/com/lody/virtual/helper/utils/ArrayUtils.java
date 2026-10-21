/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.utils;

import com.lody.virtual.helper.compat.ObjectsCompat;
import java.util.Arrays;

public class ArrayUtils {
    public static Object[] push(Object[] array2, Object item) {
        Object[] longer = new Object[array2.length + 1];
        System.arraycopy(array2, 0, longer, 0, array2.length);
        longer[array2.length] = item;
        return longer;
    }

    public static <T> boolean contains(T[] array2, T value) {
        return ArrayUtils.indexOf(array2, value) != -1;
    }

    public static boolean contains(int[] array2, int value) {
        if (array2 == null) {
            return false;
        }
        for (int element : array2) {
            if (element != value) continue;
            return true;
        }
        return false;
    }

    public static <T> int indexOf(T[] array2, T value) {
        if (array2 == null) {
            return -1;
        }
        for (int i = 0; i < array2.length; ++i) {
            if (!ObjectsCompat.equals(array2[i], value)) continue;
            return i;
        }
        return -1;
    }

    public static int protoIndexOf(Class<?>[] array2, Class<?> type) {
        if (array2 == null) {
            return -1;
        }
        for (int i = 0; i < array2.length; ++i) {
            if (array2[i] != type) continue;
            return i;
        }
        return -1;
    }

    public static int indexOfFirst(Object[] array2, Class<?> type) {
        if (!ArrayUtils.isEmpty(array2)) {
            int N = -1;
            for (Object one : array2) {
                ++N;
                if (one == null || type != one.getClass()) continue;
                return N;
            }
        }
        return -1;
    }

    public static int protoIndexOf(Class<?>[] array2, Class<?> type, int sequence) {
        if (array2 == null) {
            return -1;
        }
        while (sequence < array2.length) {
            if (type == array2[sequence]) {
                return sequence;
            }
            ++sequence;
        }
        return -1;
    }

    public static int indexOfObject(Object[] array2, Class<?> type, int sequence) {
        if (array2 == null) {
            return -1;
        }
        while (sequence < array2.length) {
            if (type.isInstance(array2[sequence])) {
                return sequence;
            }
            ++sequence;
        }
        return -1;
    }

    public static int indexOf(Object[] array2, Class<?> type, int sequence) {
        if (!ArrayUtils.isEmpty(array2)) {
            int N = -1;
            for (Object one : array2) {
                ++N;
                if (one == null || one.getClass() != type || --sequence > 0) continue;
                return N;
            }
        }
        return -1;
    }

    public static int indexOfLast(Object[] array2, Class<?> type) {
        if (!ArrayUtils.isEmpty(array2)) {
            for (int N = array2.length; N > 0; --N) {
                Object one = array2[N - 1];
                if (one == null || one.getClass() != type) continue;
                return N - 1;
            }
        }
        return -1;
    }

    public static <T> boolean isEmpty(T[] array2) {
        return array2 == null || array2.length == 0;
    }

    public static <T> T getFirst(Object[] args, Class<?> clazz) {
        int index = ArrayUtils.indexOfFirst(args, clazz);
        if (index != -1) {
            return (T)args[index];
        }
        return null;
    }

    public static void checkOffsetAndCount(int arrayLength, int offset, int count) throws ArrayIndexOutOfBoundsException {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
    }

    public static <T> T[] trimToSize(T[] array2, int size) {
        if (array2 == null || size == 0) {
            return null;
        }
        if (array2.length == size) {
            return array2;
        }
        return Arrays.copyOf(array2, size);
    }
}


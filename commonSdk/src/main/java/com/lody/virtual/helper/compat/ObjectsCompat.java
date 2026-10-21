/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.compat;

import java.util.Arrays;

public class ObjectsCompat {
    public static boolean equals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public static int hash(Object ... values) {
        return Arrays.hashCode(values);
    }
}


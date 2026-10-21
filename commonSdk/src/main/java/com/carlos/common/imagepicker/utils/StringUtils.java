/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.imagepicker.utils;

public class StringUtils {
    public static boolean isNotEmptyString(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isEmptyString(String str) {
        return str == null || str.length() <= 0;
    }
}


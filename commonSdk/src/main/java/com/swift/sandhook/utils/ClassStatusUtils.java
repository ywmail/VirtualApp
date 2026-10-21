/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook.utils;

import com.swift.sandhook.SandHookConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassStatusUtils {
    static Field fieldStatusOfClass;

    public static int getClassStatus(Class clazz, boolean isUnsigned) {
        if (clazz == null) {
            return 0;
        }
        int status = 0;
        try {
            status = fieldStatusOfClass.getInt(clazz);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (isUnsigned) {
            status = (int)(ClassStatusUtils.toUnsignedLong(status) >> 28);
        }
        return status;
    }

    public static long toUnsignedLong(int x) {
        return (long)x & 0xFFFFFFFFL;
    }

    public static boolean isInitialized(Class clazz) {
        if (fieldStatusOfClass == null) {
            return true;
        }
        if (SandHookConfig.SDK_INT >= 28) {
            return ClassStatusUtils.getClassStatus(clazz, true) >= 14;
        }
        if (SandHookConfig.SDK_INT == 27) {
            return ClassStatusUtils.getClassStatus(clazz, false) == 11;
        }
        return ClassStatusUtils.getClassStatus(clazz, false) == 10;
    }

    public static boolean isStaticAndNoInited(Member hookMethod) {
        if (!(hookMethod instanceof Method)) {
            return false;
        }
        Class<?> declaringClass = hookMethod.getDeclaringClass();
        return Modifier.isStatic(hookMethod.getModifiers()) && !ClassStatusUtils.isInitialized(declaringClass);
    }

    static {
        try {
            fieldStatusOfClass = Class.class.getDeclaredField("status");
            fieldStatusOfClass.setAccessible(true);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
    }
}


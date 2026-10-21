/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.swift.sandhook.utils;

import android.util.Log;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static Method forNameMethod;
    public static Method getMethodMethod;
    static Class vmRuntimeClass;
    static Method addWhiteListMethod;
    static Object vmRuntime;

    public static boolean passApiCheck() {
        try {
            ReflectionUtils.addReflectionWhiteList("Landroid/", "Lcom/android/", "Ljava/lang/", "Ldalvik/system/", "Llibcore/io/");
            return true;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public static void addReflectionWhiteList(String ... memberSigs) throws Throwable {
        addWhiteListMethod.invoke(vmRuntime, new Object[]{memberSigs});
    }

    static {
        try {
            getMethodMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
            vmRuntimeClass = (Class)forNameMethod.invoke(null, "dalvik.system.VMRuntime");
            addWhiteListMethod = (Method)getMethodMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
            Method getVMRuntimeMethod = (Method)getMethodMethod.invoke(vmRuntimeClass, "getRuntime", null);
            vmRuntime = getVMRuntimeMethod.invoke(null, new Object[0]);
        }
        catch (Exception e) {
            Log.e((String)"ReflectionUtils", (String)"error get methods", (Throwable)e);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.swift.sandhook.utils;

import android.util.Log;
import com.swift.sandhook.HookLog;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Unsafe {
    private static final String TAG = "Unsafe";
    private static Object unsafe;
    private static Class unsafeClass;
    private static Method arrayBaseOffsetMethod;
    private static Method arrayIndexScaleMethod;
    private static Method getIntMethod;
    private static Method getLongMethod;
    private static volatile boolean supported;
    private static Class objectArrayClass;

    public static boolean support() {
        return supported;
    }

    private Unsafe() {
    }

    public static int arrayBaseOffset(Class cls) {
        try {
            return (Integer)arrayBaseOffsetMethod.invoke(unsafe, cls);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static int arrayIndexScale(Class cls) {
        try {
            return (Integer)arrayIndexScaleMethod.invoke(unsafe, cls);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static int getInt(Object array2, long offset) {
        try {
            return (Integer)getIntMethod.invoke(unsafe, array2, offset);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public static long getLong(Object array2, long offset) {
        try {
            return (Long)getLongMethod.invoke(unsafe, array2, offset);
        }
        catch (Exception e) {
            return 0L;
        }
    }

    public static long getObjectAddress(Object obj) {
        try {
            Object[] array2 = new Object[]{obj};
            if (Unsafe.arrayIndexScale(objectArrayClass) == 8) {
                return Unsafe.getLong(array2, Unsafe.arrayBaseOffset(objectArrayClass));
            }
            return 0xFFFFFFFFL & (long)Unsafe.getInt(array2, Unsafe.arrayBaseOffset(objectArrayClass));
        }
        catch (Exception e) {
            HookLog.e("get object address error", e);
            return -1L;
        }
    }

    static {
        supported = false;
        objectArrayClass = Object[].class;
        try {
            unsafeClass = Class.forName("sun.misc.Unsafe");
            Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = theUnsafe.get(null);
        }
        catch (Exception e) {
            try {
                Field theUnsafe = unsafeClass.getDeclaredField("THE_ONE");
                theUnsafe.setAccessible(true);
                unsafe = theUnsafe.get(null);
            }
            catch (Exception e2) {
                Log.w((String)TAG, (String)"Unsafe not found o.O");
            }
        }
        if (unsafe != null) {
            try {
                arrayBaseOffsetMethod = unsafeClass.getDeclaredMethod("arrayBaseOffset", Class.class);
                arrayIndexScaleMethod = unsafeClass.getDeclaredMethod("arrayIndexScale", Class.class);
                getIntMethod = unsafeClass.getDeclaredMethod("getInt", Object.class, Long.TYPE);
                getLongMethod = unsafeClass.getDeclaredMethod("getLong", Object.class, Long.TYPE);
                supported = true;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}


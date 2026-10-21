/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.utils;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.util.Arrays;
import java.util.HashSet;
import mirror.android.content.AttributionSource;

public class MethodParameterUtils {
    public static <T> T getFirstParam(Object[] args, Class<T> tClass) {
        if (args == null) {
            return null;
        }
        int index = ArrayUtils.indexOfFirst(args, tClass);
        if (index != -1) {
            return (T)args[index];
        }
        return null;
    }

    public static String replaceFirstAppPkg(Object[] args) {
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; ++i) {
            if (!(args[i] instanceof String)) continue;
            String value = (String)args[i];
            if (!VirtualCore.get().isAppInstalled(value)) continue;
            args[i] = VirtualCore.get().getHostPkg();
            return value;
        }
        return null;
    }

    public static String replaceLastAppPkg(Object[] args) {
        int index = ArrayUtils.indexOfLast(args, String.class);
        if (index != -1) {
            String pkg = (String)args[index];
            args[index] = VirtualCore.get().getHostPkg();
            return pkg;
        }
        return null;
    }

    public static String replaceSequenceAppPkg(Object[] args, int sequence) {
        int index = ArrayUtils.indexOf(args, String.class, sequence);
        if (index != -1) {
            String pkg = (String)args[index];
            args[index] = VirtualCore.get().getHostPkg();
            return pkg;
        }
        return null;
    }

    public static int getParamsIndex(Class[] args, Class<?> type) {
        for (int i = 0; i < args.length; ++i) {
            Class obj = args[i];
            if (!obj.equals(type)) continue;
            return i;
        }
        return -1;
    }

    public static int getIndex(Object[] args, Class<?> type) {
        for (int i = 0; i < args.length; ++i) {
            Object obj = args[i];
            if (obj != null && obj.getClass() == type) {
                return i;
            }
            if (!type.isInstance(obj)) continue;
            return i;
        }
        return -1;
    }

    public static Class<?>[] getAllInterface(Class clazz) {
        HashSet classes = new HashSet();
        MethodParameterUtils.getAllInterfaces(clazz, classes);
        Class[] result = new Class[classes.size()];
        classes.toArray(result);
        return result;
    }

    public static void getAllInterfaces(Class clazz, HashSet<Class<?>> interfaceCollection) {
        Class<?>[] classes = clazz.getInterfaces();
        if (classes.length != 0) {
            interfaceCollection.addAll(Arrays.asList(classes));
        }
        if (clazz.getSuperclass() != Object.class) {
            MethodParameterUtils.getAllInterfaces(clazz.getSuperclass(), interfaceCollection);
        }
    }

    public static void fixAttributionSource(Object[] args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                if (!AttributionSource.TYPE.isInstance(args[i])) continue;
                ContextFixer.fixAttributionSource(args[i]);
            }
        }
    }
}


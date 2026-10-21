/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook;

import com.swift.sandhook.SandHook;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class SandHookMethodResolver {
    public static Field resolvedMethodsField;
    public static Field dexCacheField;
    public static Field dexMethodIndexField;
    public static Field artMethodField;
    public static Field fieldEntryPointFromCompiledCode;
    public static Field fieldEntryPointFromInterpreter;
    public static boolean canResolvedInJava;
    public static boolean isArtMethod;
    public static long resolvedMethodsAddress;
    public static int dexMethodIndex;
    public static long entryPointFromCompiledCode;
    public static long entryPointFromInterpreter;
    public static Method testMethod;
    public static Object testArtMethod;

    public static void init() {
        testMethod = SandHook.testOffsetMethod1;
        SandHookMethodResolver.checkSupport();
    }

    private static void checkSupport() {
        try {
            artMethodField = SandHook.getField(Method.class, "artMethod");
            testArtMethod = artMethodField.get(testMethod);
            if (SandHook.hasJavaArtMethod() && testArtMethod.getClass() == SandHook.artMethodClass) {
                SandHookMethodResolver.checkSupportForArtMethod();
                isArtMethod = true;
            } else if (testArtMethod instanceof Long) {
                SandHookMethodResolver.checkSupportForArtMethodId();
                isArtMethod = false;
            } else {
                canResolvedInJava = false;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static long getArtMethod(Member member) {
        if (artMethodField == null) {
            return 0L;
        }
        try {
            return (Long)artMethodField.get(member);
        }
        catch (IllegalAccessException e) {
            return 0L;
        }
    }

    private static void checkSupportForArtMethod() throws Exception {
        try {
            dexMethodIndexField = SandHook.getField(SandHook.artMethodClass, "dexMethodIndex");
        }
        catch (NoSuchFieldException e) {
            dexMethodIndexField = SandHook.getField(SandHook.artMethodClass, "methodDexIndex");
        }
        dexCacheField = SandHook.getField(Class.class, "dexCache");
        Object dexCache = dexCacheField.get(testMethod.getDeclaringClass());
        resolvedMethodsField = SandHook.getField(dexCache.getClass(), "resolvedMethods");
        if (resolvedMethodsField.get(dexCache) instanceof Object[]) {
            canResolvedInJava = true;
        }
        try {
            try {
                dexMethodIndex = (Integer)dexMethodIndexField.get(testArtMethod);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            try {
                fieldEntryPointFromCompiledCode = SandHook.getField(SandHook.artMethodClass, "entryPointFromQuickCompiledCode");
            }
            catch (Throwable e) {
                fieldEntryPointFromCompiledCode = SandHook.getField(SandHook.artMethodClass, "entryPointFromCompiledCode");
            }
            if (fieldEntryPointFromCompiledCode.getType() == Integer.TYPE) {
                entryPointFromCompiledCode = fieldEntryPointFromCompiledCode.getInt(testArtMethod);
            } else if (fieldEntryPointFromCompiledCode.getType() == Long.TYPE) {
                entryPointFromCompiledCode = fieldEntryPointFromCompiledCode.getLong(testArtMethod);
            }
            fieldEntryPointFromInterpreter = SandHook.getField(SandHook.artMethodClass, "entryPointFromInterpreter");
            if (fieldEntryPointFromInterpreter.getType() == Integer.TYPE) {
                entryPointFromInterpreter = fieldEntryPointFromInterpreter.getInt(testArtMethod);
            } else if (fieldEntryPointFromCompiledCode.getType() == Long.TYPE) {
                entryPointFromInterpreter = fieldEntryPointFromInterpreter.getLong(testArtMethod);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void checkSupportForArtMethodId() throws Exception {
        dexMethodIndexField = SandHook.getField(Method.class, "dexMethodIndex");
        dexMethodIndex = (Integer)dexMethodIndexField.get(testMethod);
        dexCacheField = SandHook.getField(Class.class, "dexCache");
        Object dexCache = dexCacheField.get(testMethod.getDeclaringClass());
        resolvedMethodsField = SandHook.getField(dexCache.getClass(), "resolvedMethods");
        Object resolvedMethods = resolvedMethodsField.get(dexCache);
        if (resolvedMethods instanceof Long) {
            canResolvedInJava = false;
            resolvedMethodsAddress = (Long)resolvedMethods;
        } else if (resolvedMethods instanceof long[]) {
            canResolvedInJava = true;
        } else if (resolvedMethods instanceof int[]) {
            canResolvedInJava = true;
        }
    }

    public static void resolveMethod(Method hook, Method backup) {
        if (canResolvedInJava && artMethodField != null) {
            try {
                SandHookMethodResolver.resolveInJava(hook, backup);
            }
            catch (Exception e) {
                SandHookMethodResolver.resolveInNative(hook, backup);
            }
        } else {
            SandHookMethodResolver.resolveInNative(hook, backup);
        }
    }

    private static void resolveInJava(Method hook, Method backup) throws Exception {
        Object dexCache = dexCacheField.get(hook.getDeclaringClass());
        if (isArtMethod) {
            Object artMethod = artMethodField.get(backup);
            int dexMethodIndex = (Integer)dexMethodIndexField.get(artMethod);
            Object resolvedMethods = resolvedMethodsField.get(dexCache);
            ((Object[])resolvedMethods)[dexMethodIndex] = artMethod;
        } else {
            int dexMethodIndex = (Integer)dexMethodIndexField.get(backup);
            Object resolvedMethods = resolvedMethodsField.get(dexCache);
            if (resolvedMethods instanceof long[]) {
                long artMethod;
                ((long[])resolvedMethods)[dexMethodIndex] = artMethod = ((Long)artMethodField.get(backup)).longValue();
            } else if (resolvedMethods instanceof int[]) {
                int artMethod;
                ((int[])resolvedMethods)[dexMethodIndex] = artMethod = Long.valueOf((Long)artMethodField.get(backup)).intValue();
            } else {
                throw new UnsupportedOperationException("un support");
            }
        }
    }

    private static void resolveInNative(Method hook, Method backup) {
        SandHook.ensureMethodCached(hook, backup);
    }

    static {
        canResolvedInJava = false;
        isArtMethod = false;
        resolvedMethodsAddress = 0L;
        dexMethodIndex = 0;
        entryPointFromCompiledCode = 0L;
        entryPointFromInterpreter = 0L;
    }
}


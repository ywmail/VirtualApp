/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook;

import com.carlos.common.utils.LogUtil;
import com.swift.sandhook.ArtMethodSizeTest;
import com.swift.sandhook.HookLog;
import com.swift.sandhook.PendingHookHandler;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.SandHookMethodResolver;
import com.swift.sandhook.annotation.HookMode;
import com.swift.sandhook.blacklist.HookBlackList;
import com.swift.sandhook.utils.ClassStatusUtils;
import com.swift.sandhook.utils.FileUtils;
import com.swift.sandhook.utils.ReflectionUtils;
import com.swift.sandhook.utils.Unsafe;
import com.swift.sandhook.wrapper.HookErrorException;
import com.swift.sandhook.wrapper.HookWrapper;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SandHook {

    static Map<Member, HookWrapper.HookEntity> globalHookEntityMap = new ConcurrentHashMap<Member, HookWrapper.HookEntity>();
    static Map<Method, HookWrapper.HookEntity> globalBackupMap = new ConcurrentHashMap<Method, HookWrapper.HookEntity>();
    private static HookModeCallBack hookModeCallBack;
    private static HookResultCallBack hookResultCallBack;
    public static Class artMethodClass;
    public static Field nativePeerField;
    public static Method testOffsetMethod1;
    public static Method testOffsetMethod2;
    public static Object testOffsetArtMethod1;
    public static Object testOffsetArtMethod2;
    public static int testAccessFlag;

    public static void setHookModeCallBack(HookModeCallBack hookModeCallBack) {
        SandHook.hookModeCallBack = hookModeCallBack;
    }

    public static void setHookResultCallBack(HookResultCallBack hookResultCallBack) {
        SandHook.hookResultCallBack = hookResultCallBack;
    }

    private static boolean init() {
        SandHookConfig.libLoader.loadLib();
        SandHook.initTestOffset();
        SandHook.initThreadPeer();
        SandHookMethodResolver.init();
        LogUtil.d("这里开始初始化...sandbox");
        return SandHook.initNative(SandHookConfig.SDK_INT, SandHookConfig.DEBUG);
    }

    private static void initThreadPeer() {
        try {
            nativePeerField = SandHook.getField(Thread.class, "nativePeer");
        }
        catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
    }

    public static void addHookClass(Class ... hookWrapperClass) throws HookErrorException {
        HookWrapper.addHookClass(hookWrapperClass);
    }

    public static void addHookClass(ClassLoader classLoader, Class ... hookWrapperClass) throws HookErrorException {
        HookWrapper.addHookClass(classLoader, hookWrapperClass);
    }

    public static synchronized void hook(HookWrapper.HookEntity entity) throws HookErrorException {
        HookMode hookMode;
        if (entity == null) {
            throw new HookErrorException("null hook entity");
        }
        Member target = entity.target;
        Method hook = entity.hook;
        Method backup = entity.backup;
        if (target == null || hook == null) {
            throw new HookErrorException("null input");
        }
        if (globalHookEntityMap.containsKey(entity.target)) {
            throw new HookErrorException("method <" + entity.target.toString() + "> has been hooked!");
        }
        if (HookBlackList.canNotHook(target)) {
            throw new HookErrorException("method <" + entity.target.toString() + "> can not hook, because of in blacklist!");
        }
        if (SandHookConfig.delayHook && PendingHookHandler.canWork() && ClassStatusUtils.isStaticAndNoInited(entity.target)) {
            PendingHookHandler.addPendingHook(entity);
            return;
        }
        if (entity.initClass) {
            SandHook.resolveStaticMethod(target);
            SandHook.MakeInitializedClassVisibilyInitialized(SandHook.getThreadId());
        }
        SandHook.resolveStaticMethod(backup);
        if (backup != null && entity.resolveDexCache) {
            SandHookMethodResolver.resolveMethod(hook, backup);
        }
        if (target instanceof Method) {
            ((Method)target).setAccessible(true);
        }
        int mode = 0;
        if (hookModeCallBack != null) {
            mode = hookModeCallBack.hookMode(target);
        }
        globalHookEntityMap.put(entity.target, entity);
        int res = mode != 0 ? SandHook.hookMethod(target, hook, backup, mode) : SandHook.hookMethod(target, hook, backup, (hookMode = hook.getAnnotation(HookMode.class)) == null ? 0 : hookMode.value());
        if (res > 0 && backup != null) {
            backup.setAccessible(true);
        }
        entity.hookMode = res;
        if (hookResultCallBack != null) {
            hookResultCallBack.hookResult(res > 0, entity);
        }
        if (res < 0) {
            globalHookEntityMap.remove(entity.target);
            throw new HookErrorException("hook method <" + entity.target.toString() + "> error in native!");
        }
        if (entity.backup != null) {
            globalBackupMap.put(entity.backup, entity);
        }
        HookLog.d("method <" + entity.target.toString() + "> hook <" + (res == 1 ? "inline" : "replacement") + "> success!");
    }

    public static final Object callOriginMethod(Member originMethod, Object thiz, Object ... args) throws Throwable {
        HookWrapper.HookEntity hookEntity = globalHookEntityMap.get(originMethod);
        if (hookEntity == null || hookEntity.backup == null) {
            return null;
        }
        return SandHook.callOriginMethod(hookEntity.backupIsStub, originMethod, hookEntity.backup, thiz, args);
    }

    public static final Object callOriginByBackup(Method backupMethod, Object thiz, Object ... args) throws Throwable {
        HookWrapper.HookEntity hookEntity = globalBackupMap.get(backupMethod);
        if (hookEntity == null) {
            return null;
        }
        return SandHook.callOriginMethod(hookEntity.backupIsStub, hookEntity.target, backupMethod, thiz, args);
    }

    public static final Object callOriginMethod(Member originMethod, Method backupMethod, Object thiz, Object[] args) throws Throwable {
        return SandHook.callOriginMethod(true, originMethod, backupMethod, thiz, args);
    }

    public static final Object callOriginMethod(boolean backupIsStub, Member originMethod, Method backupMethod, Object thiz, Object[] args) throws Throwable {
        if (!backupIsStub && SandHookConfig.SDK_INT >= 24) {
            Class<?> originClassHolder = originMethod.getDeclaringClass();
            SandHook.ensureDeclareClass(originMethod, backupMethod);
        }
        if (Modifier.isStatic(originMethod.getModifiers())) {
            try {
                return backupMethod.invoke(null, args);
            }
            catch (InvocationTargetException throwable) {
                if (throwable.getCause() != null) {
                    throw throwable.getCause();
                }
                throw throwable;
            }
        }
        try {
            return backupMethod.invoke(thiz, args);
        }
        catch (InvocationTargetException throwable) {
            if (throwable.getCause() != null) {
                throw throwable.getCause();
            }
            throw throwable;
        }
    }

    public static final void ensureBackupMethod(Method backupMethod) {
        if (SandHookConfig.SDK_INT < 24) {
            return;
        }
        HookWrapper.HookEntity entity = globalBackupMap.get(backupMethod);
        if (entity != null) {
            SandHook.ensureDeclareClass(entity.target, backupMethod);
        }
    }

    public static boolean resolveStaticMethod(Member method) {
        if (method == null) {
            return true;
        }
        try {
            if (method instanceof Method && Modifier.isStatic(method.getModifiers())) {
                ((Method)method).setAccessible(true);
                ((Method)method).invoke(new Object(), SandHook.getFakeArgs((Method)method));
            }
        }
        catch (ExceptionInInitializerError classInitError) {
            return false;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return true;
    }

    private static Object[] getFakeArgs(Method method) {
        Class<?>[] pars = method.getParameterTypes();
        if (pars == null || pars.length == 0) {
            return new Object[]{new Object()};
        }
        return null;
    }

    public static Object getObject(long address) {
        if (address == 0L) {
            return null;
        }
        long threadSelf = SandHook.getThreadId();
        return SandHook.getObjectNative(threadSelf, address);
    }

    public static boolean canGetObjectAddress() {
        return Unsafe.support();
    }

    public static long getObjectAddress(Object object) {
        return Unsafe.getObjectAddress(object);
    }

    private static void initTestOffset() {
        ArtMethodSizeTest.method1();
        ArtMethodSizeTest.method2();
        try {
            testOffsetMethod1 = ArtMethodSizeTest.class.getDeclaredMethod("method1", new Class[0]);
            testOffsetMethod2 = ArtMethodSizeTest.class.getDeclaredMethod("method2", new Class[0]);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException("SandHook init error", e);
        }
        SandHook.initTestAccessFlag();
    }

    private static void initTestAccessFlag() {
        if (SandHook.hasJavaArtMethod()) {
            try {
                SandHook.loadArtMethod();
                Field fieldAccessFlags = SandHook.getField(artMethodClass, "accessFlags");
                testAccessFlag = (Integer)fieldAccessFlags.get(testOffsetArtMethod1);
            }
            catch (Exception fieldAccessFlags) {}
        } else {
            try {
                Field fieldAccessFlags = SandHook.getField(Method.class, "accessFlags");
                testAccessFlag = (Integer)fieldAccessFlags.get(testOffsetMethod1);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static void loadArtMethod() {
        try {
            Field fieldArtMethod = SandHook.getField(Method.class, "artMethod");
            testOffsetArtMethod1 = fieldArtMethod.get(testOffsetMethod1);
            testOffsetArtMethod2 = fieldArtMethod.get(testOffsetMethod2);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasJavaArtMethod() {
        if (SandHookConfig.SDK_INT >= 26) {
            return false;
        }
        if (artMethodClass != null) {
            return true;
        }
        try {
            artMethodClass = SandHookConfig.initClassLoader == null ? Class.forName("java.lang.reflect.ArtMethod") : Class.forName("java.lang.reflect.ArtMethod", true, SandHookConfig.initClassLoader);
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Field getField(Class topClass, String fieldName) throws NoSuchFieldException {
        while (topClass != null && topClass != Object.class) {
            try {
                Field field = topClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            }
            catch (Exception exception) {
                topClass = topClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    public static long getThreadId() {
        if (nativePeerField == null) {
            return 0L;
        }
        try {
            if (nativePeerField.getType() == Integer.TYPE) {
                return nativePeerField.getInt(Thread.currentThread());
            }
            return nativePeerField.getLong(Thread.currentThread());
        }
        catch (IllegalAccessException e) {
            return 0L;
        }
    }

    public static Object getJavaMethod(String className, String methodName) {
        if (className == null) {
            return null;
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            return null;
        }
        try {
            return clazz.getDeclaredMethod(methodName, new Class[0]);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static long getArtMethod(Member member) {
        return SandHookMethodResolver.getArtMethod(member);
    }

    public static boolean passApiCheck() {
        return ReflectionUtils.passApiCheck();
    }

    public static boolean tryDisableProfile(String selfPackageName) {
        if (SandHookConfig.SDK_INT < 24) {
            return false;
        }
        try {
            File profile = new File("/data/misc/profiles/cur/" + SandHookConfig.curUser + "/" + selfPackageName + "/primary.prof");
            if (!profile.getParentFile().exists()) {
                return false;
            }
            try {
                profile.delete();
                profile.createNewFile();
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            FileUtils.chmod(profile.getAbsolutePath(), 256);
            return true;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static native boolean initNative(int var0, boolean var1);

    public static native void setHookMode(int var0);

    public static native void setInlineSafeCheck(boolean var0);

    public static native void skipAllSafeCheck(boolean var0);

    private static native int hookMethod(Member var0, Method var1, Method var2, int var3);

    public static native void ensureMethodCached(Method var0, Method var1);

    public static native void ensureDeclareClass(Member var0, Method var1);

    public static native boolean compileMethod(Member var0);

    public static native boolean deCompileMethod(Member var0, boolean var1);

    public static native boolean canGetObject();

    public static native Object getObjectNative(long var0, long var2);

    public static native boolean is64Bit();

    public static native boolean disableVMInline();

    public static native boolean disableDex2oatInline(boolean var0);

    public static native boolean setNativeEntry(Member var0, Member var1, long var2);

    public static native boolean initForPendingHook();

    public static native void MakeInitializedClassVisibilyInitialized(long var0);

    public static native void addPendingHookNative(Member var0);

    static {
        SandHook.init();
    }

    @FunctionalInterface
    public static interface HookResultCallBack {
        public void hookResult(boolean var1, HookWrapper.HookEntity var2);
    }

    @FunctionalInterface
    public static interface HookModeCallBack {
        public int hookMode(Member var1);
    }
}


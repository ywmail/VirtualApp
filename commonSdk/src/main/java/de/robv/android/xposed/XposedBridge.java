/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.util.Log
 */
package de.robv.android.xposed;

import android.annotation.SuppressLint;
import android.util.Log;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.xposedcompat.methodgen.DynamicBridge;
import com.swift.sandhook.xposedcompat.utils.DexLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class XposedBridge {
    public static final ClassLoader BOOTCLASSLOADER = XposedBridge.class.getClassLoader();
    public static final String TAG = "SandXposed";
    @Deprecated
    public static int XPOSED_BRIDGE_VERSION;
    static boolean isZygote;
    private static int runtime;
    private static final int RUNTIME_DALVIK = 1;
    private static final int RUNTIME_ART = 2;
    public static boolean disableHooks;
    static long BOOT_START_TIME;
    private static final Object[] EMPTY_ARRAY;
    public static final Map<Member, CopyOnWriteSortedSet<XC_MethodHook>> sHookedMethodCallbacks;
    public static final CopyOnWriteSortedSet<XC_LoadPackage> sLoadedPackageCallbacks;
    static final CopyOnWriteSortedSet<XC_InitPackageResources> sInitPackageResourcesCallbacks;

    private XposedBridge() {
    }

    public static void main(String[] args) {
    }

    private static void initXResources() throws IOException {
    }

    @SuppressLint(value={"SetWorldReadable"})
    private static File ensureSuperDexFile(String clz, Class<?> realSuperClz, Class<?> topClz) throws IOException {
        return null;
    }

    public static int getXposedVersion() {
        return 90;
    }

    public static synchronized void log(String text) {
        if (DexLog.DEBUG) {
            Log.i((String)TAG, (String)text);
        }
    }

    public static synchronized void log(Throwable t) {
        if (DexLog.DEBUG) {
            Log.e((String)TAG, (String)Log.getStackTraceString((Throwable)t));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static XC_MethodHook.Unhook hookMethod(Member hookMethod, XC_MethodHook callback) {
        if (!(hookMethod instanceof Method) && !(hookMethod instanceof Constructor)) {
            throw new IllegalArgumentException("Only methods and constructors can be hooked: " + hookMethod.toString());
        } else if (hookMethod.getDeclaringClass().isInterface()) {
            throw new IllegalArgumentException("Cannot hook interfaces: " + hookMethod.toString());
        } else if (Modifier.isAbstract(hookMethod.getModifiers())) {
            throw new IllegalArgumentException("Cannot hook abstract methods: " + hookMethod.toString());
        } else if (callback == null) {
            throw new IllegalArgumentException("callback should not be null!");
        } else {
            boolean newMethod = false;
            CopyOnWriteSortedSet callbacks;
            synchronized(sHookedMethodCallbacks) {
                callbacks = (CopyOnWriteSortedSet)sHookedMethodCallbacks.get(hookMethod);
                if (callbacks == null) {
                    callbacks = new CopyOnWriteSortedSet();
                    sHookedMethodCallbacks.put(hookMethod, callbacks);
                    newMethod = true;
                }
            }

            callbacks.add(callback);
            if (newMethod) {
                Class<?> declaringClass = hookMethod.getDeclaringClass();
                int slot;
                Class[] parameterTypes;
                Class returnType;
                if (runtime == 2) {
                    slot = 0;
                    parameterTypes = null;
                    returnType = null;
                } else if (hookMethod instanceof Method) {
                    slot = XposedHelpers.getIntField(hookMethod, "slot");
                    parameterTypes = ((Method)hookMethod).getParameterTypes();
                    returnType = ((Method)hookMethod).getReturnType();
                } else {
                    slot = XposedHelpers.getIntField(hookMethod, "slot");
                    parameterTypes = ((Constructor)hookMethod).getParameterTypes();
                    returnType = null;
                }

                AdditionalHookInfo additionalInfo = new AdditionalHookInfo(callbacks, parameterTypes, returnType);
                hookMethodNative(hookMethod, declaringClass, slot, additionalInfo);
            }

            Objects.requireNonNull(callback);
            return new XC_MethodHook.Unhook(callback, hookMethod);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public static void unhookMethod(Member hookMethod, XC_MethodHook callback) {
        CopyOnWriteSortedSet<XC_MethodHook> callbacks;
        Map<Member, CopyOnWriteSortedSet<XC_MethodHook>> map = sHookedMethodCallbacks;
        synchronized (map) {
            callbacks = sHookedMethodCallbacks.get(hookMethod);
            if (callbacks == null) {
                return;
            }
        }
        callbacks.remove(callback);
    }

    public static Set<XC_MethodHook.Unhook> hookAllMethods(Class<?> hookClass, String methodName, XC_MethodHook callback) {
        HashSet<XC_MethodHook.Unhook> unhooks = new HashSet<XC_MethodHook.Unhook>();
        for (Method method : hookClass.getDeclaredMethods()) {
            if (!method.getName().equals(methodName)) continue;
            unhooks.add(XposedBridge.hookMethod(method, callback));
        }
        return unhooks;
    }

    public static Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> hookClass, XC_MethodHook callback) {
        HashSet<XC_MethodHook.Unhook> unhooks = new HashSet<XC_MethodHook.Unhook>();
        for (Constructor<?> constructor : hookClass.getDeclaredConstructors()) {
            unhooks.add(XposedBridge.hookMethod(constructor, callback));
        }
        return unhooks;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void hookLoadPackage(XC_LoadPackage callback) {
        CopyOnWriteSortedSet<XC_LoadPackage> copyOnWriteSortedSet = sLoadedPackageCallbacks;
        synchronized (copyOnWriteSortedSet) {
            sLoadedPackageCallbacks.add(callback);
        }
    }

    public static void hookInitPackageResources(XC_InitPackageResources callback) {
    }

    private static synchronized void hookMethodNative(Member method, Class<?> declaringClass, int slot, Object additionalInfoObj) {
        DynamicBridge.hookMethod(method, (AdditionalHookInfo)additionalInfoObj);
    }

    public static Object invokeOriginalMethod(Member method, Object thisObject, Object[] args) throws NullPointerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            return SandHook.callOriginMethod(method, thisObject, args);
        }
        catch (NullPointerException e) {
            throw e;
        }
        catch (IllegalAccessException e) {
            throw e;
        }
        catch (IllegalArgumentException e) {
            throw e;
        }
        catch (InvocationTargetException e) {
            throw e;
        }
        catch (Throwable throwable) {
            throw new InvocationTargetException(throwable);
        }
    }

    static /* synthetic */ Object[] access$100() {
        return EMPTY_ARRAY;
    }

    static {
        isZygote = true;
        runtime = 2;
        disableHooks = false;
        EMPTY_ARRAY = new Object[0];
        sHookedMethodCallbacks = new HashMap<Member, CopyOnWriteSortedSet<XC_MethodHook>>();
        sLoadedPackageCallbacks = new CopyOnWriteSortedSet();
        sInitPackageResourcesCallbacks = new CopyOnWriteSortedSet();
    }

    public static class AdditionalHookInfo {
        public final CopyOnWriteSortedSet<XC_MethodHook> callbacks;
        public final Class<?>[] parameterTypes;
        public final Class<?> returnType;

        private AdditionalHookInfo(CopyOnWriteSortedSet<XC_MethodHook> callbacks, Class<?>[] parameterTypes, Class<?> returnType) {
            this.callbacks = callbacks;
            this.parameterTypes = parameterTypes;
            this.returnType = returnType;
        }
    }

    public static final class CopyOnWriteSortedSet<E> {
        private volatile transient Object[] elements = XposedBridge.access$100();

        public synchronized boolean add(E e) {
            int index = this.indexOf(e);
            if (index >= 0) {
                return false;
            }
            Object[] newElements = new Object[this.elements.length + 1];
            System.arraycopy(this.elements, 0, newElements, 0, this.elements.length);
            newElements[this.elements.length] = e;
            Arrays.sort(newElements);
            this.elements = newElements;
            return true;
        }

        public synchronized boolean remove(E e) {
            int index = this.indexOf(e);
            if (index == -1) {
                return false;
            }
            Object[] newElements = new Object[this.elements.length - 1];
            System.arraycopy(this.elements, 0, newElements, 0, index);
            System.arraycopy(this.elements, index + 1, newElements, index, this.elements.length - index - 1);
            this.elements = newElements;
            return true;
        }

        private int indexOf(Object o) {
            for (int i = 0; i < this.elements.length; ++i) {
                if (!o.equals(this.elements[i])) continue;
                return i;
            }
            return -1;
        }

        public Object[] getSnapshot() {
            return this.elements;
        }
    }
}


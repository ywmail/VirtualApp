/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hiddenapibypass;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hiddenapibypass.Helper;
import dalvik.system.VMRuntime;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import sun.misc.Unsafe;

public final class HiddenApiBypass {
    private static final String TAG = "HiddenApiBypass";
    private static final Unsafe unsafe;
    private static final long artOffset;
    private static final long infoOffset;
    private static final long methodsOffset;
    private static final long memberOffset;
    private static final long size;
    private static final long bias;
    private static final Set<String> signaturePrefixes;

    public static List<Executable> getDeclaredMethods(Class<?> clazz) {
        MethodHandle mh;
        ArrayList<Executable> list = new ArrayList<Executable>();
        if (clazz.isPrimitive() || clazz.isArray()) {
            return list;
        }
        try {
            mh = MethodHandles.lookup().unreflect(Helper.NeverCall.class.getDeclaredMethod("a", new Class[0]));
        }
        catch (IllegalAccessException | NoSuchMethodException e) {
            return list;
        }
        long methods = unsafe.getLong(clazz, methodsOffset);
        int numMethods = unsafe.getInt(methods);
        for (int i = 0; i < numMethods; ++i) {
            long method = methods + (long)i * size + bias;
            unsafe.putLong(mh, artOffset, method);
            unsafe.putObject(mh, infoOffset, null);
            try {
                MethodHandles.lookup().revealDirect(mh);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            MethodHandleInfo info = (MethodHandleInfo)unsafe.getObject(mh, infoOffset);
            Executable member = (Executable)unsafe.getObject(info, memberOffset);
            list.add(member);
        }
        return list;
    }

    public static boolean setHiddenApiExemptions(String ... signaturePrefixes) {
        List<Executable> methods = HiddenApiBypass.getDeclaredMethods(VMRuntime.class);
        Optional<Executable> getRuntime = methods.stream().filter(it -> it.getName().equals("getRuntime")).findFirst();
        Optional<Executable> setHiddenApiExemptions = methods.stream().filter(it -> it.getName().equals("setHiddenApiExemptions")).findFirst();
        if (getRuntime.isPresent() && setHiddenApiExemptions.isPresent()) {
            getRuntime.get().setAccessible(true);
            try {
                Object runtime = ((Method)getRuntime.get()).invoke(null, new Object[0]);
                setHiddenApiExemptions.get().setAccessible(true);
                ((Method)setHiddenApiExemptions.get()).invoke(runtime, new Object[]{signaturePrefixes});
                return true;
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                // empty catch block
            }
        }
        return false;
    }

    public static boolean addHiddenApiExemptions(String ... signaturePrefixes) {
        HiddenApiBypass.signaturePrefixes.addAll(Arrays.asList(signaturePrefixes));
        String[] strings = new String[HiddenApiBypass.signaturePrefixes.size()];
        HiddenApiBypass.signaturePrefixes.toArray(strings);
        return HiddenApiBypass.setHiddenApiExemptions(strings);
    }

    public static boolean clearHiddenApiExemptions() {
        signaturePrefixes.clear();
        return HiddenApiBypass.setHiddenApiExemptions(new String[0]);
    }

    static {
        signaturePrefixes = new HashSet<String>();
        try {
            unsafe = (Unsafe)Unsafe.class.getDeclaredMethod("getUnsafe", new Class[0]).invoke(null, new Object[0]);
            assert (unsafe != null);
            artOffset = unsafe.objectFieldOffset(Helper.MethodHandle.class.getDeclaredField("artFieldOrMethod"));
            infoOffset = unsafe.objectFieldOffset(Helper.MethodHandleImpl.class.getDeclaredField("info"));
            methodsOffset = unsafe.objectFieldOffset(Helper.Class.class.getDeclaredField("methods"));
            memberOffset = unsafe.objectFieldOffset(Helper.HandleInfo.class.getDeclaredField("member"));
            MethodHandle mhA = MethodHandles.lookup().unreflect(Helper.NeverCall.class.getDeclaredMethod("a", new Class[0]));
            MethodHandle mhB = MethodHandles.lookup().unreflect(Helper.NeverCall.class.getDeclaredMethod("b", new Class[0]));
            long aAddr = unsafe.getLong(mhA, artOffset);
            long bAddr = unsafe.getLong(mhB, artOffset);
            long aMethods = unsafe.getLong(Helper.NeverCall.class, methodsOffset);
            size = bAddr - aAddr;
            bias = aAddr - aMethods - size;
        }
        catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}


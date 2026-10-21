/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.hardware.Camera
 *  android.media.AudioRecord
 *  android.media.MediaRecorder
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 *  dalvik.system.DexFile
 */
package com.lody.virtual.client.natives;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Parcelable;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import dalvik.system.DexFile;
import java.lang.reflect.Method;

public class NativeMethods {
    public static int gCameraMethodType;
    public static Method gNativeMask;
    public static Method gCameraNativeSetup;
    public static Method gOpenDexFileNative;
    public static Method gAudioRecordNativeCheckPermission;
    public static Method gMediaRecorderNativeSetup;
    public static Method gAudioRecordNativeSetup;
    public static Method gNativeLoad;
    public static int gAudioRecordMethodType;

    @SuppressLint(value={"PrivateApi"})
    private static void init() {
        try {
            gNativeMask = NativeEngine.class.getDeclaredMethod("nativeMark", new Class[0]);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (BuildCompat.isR()) {
            try {
                gNativeLoad = Runtime.class.getDeclaredMethod("nativeLoad", String.class, ClassLoader.class, Class.class);
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        gMediaRecorderNativeSetup = NativeMethods.getMediaRecorderNativeSetup();
        gAudioRecordNativeSetup = NativeMethods.getAudioRecordNativeSetup();
        gAudioRecordMethodType = gAudioRecordNativeSetup != null && gAudioRecordNativeSetup.getParameterTypes().length == 10 ? 2 : 1;
        String methodName = Build.VERSION.SDK_INT >= 19 ? "openDexFileNative" : "openDexFile";
        for (Method method : DexFile.class.getDeclaredMethods()) {
            if (!method.getName().equals(methodName)) continue;
            gOpenDexFileNative = method;
            break;
        }
        if (gOpenDexFileNative == null) {
            throw new RuntimeException("Unable to find method : " + methodName);
        }
        gOpenDexFileNative.setAccessible(true);
        gCameraMethodType = -1;
        Method method = NativeMethods.getCameraNativeSetup();
        if (method != null) {
            int index = MethodParameterUtils.getParamsIndex(method.getParameterTypes(), String.class);
            gCameraNativeSetup = method;
            gCameraMethodType = 16 + index;
        }
        for (Method mth : AudioRecord.class.getDeclaredMethods()) {
            if (!mth.getName().equals("native_check_permission") || mth.getParameterTypes().length != 1 || mth.getParameterTypes()[0] != String.class) continue;
            gAudioRecordNativeCheckPermission = mth;
            mth.setAccessible(true);
            break;
        }
    }

    private static Method getCameraNativeSetup() {
        Method[] methods = Camera.class.getDeclaredMethods();
        if (methods != null) {
            for (Method method : methods) {
                if (!"native_setup".equals(method.getName())) continue;
                return method;
            }
        }
        return null;
    }

    @SuppressLint(value={"PrivateApi"})
    private static Method getMediaRecorderNativeSetup() {
        Method native_setup = null;
        try {
            native_setup = BuildCompat.isS() ? MediaRecorder.class.getDeclaredMethod("native_setup", Object.class, String.class, Parcelable.class) : MediaRecorder.class.getDeclaredMethod("native_setup", Object.class, String.class, String.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        if (native_setup == null) {
            try {
                native_setup = MediaRecorder.class.getDeclaredMethod("native_setup", Object.class, String.class);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
        }
        return native_setup;
    }

    @SuppressLint(value={"PrivateApi"})
    private static Method getAudioRecordNativeSetup() {
        Method native_setup = null;
        try {
            native_setup = AudioRecord.class.getDeclaredMethod("native_setup", Object.class, Object.class, int[].class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, int[].class, String.class, Long.TYPE);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        if (native_setup == null) {
            try {
                native_setup = AudioRecord.class.getDeclaredMethod("native_setup", Object.class, Object.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, int[].class, String.class);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
        }
        return native_setup;
    }

    static {
        try {
            NativeMethods.init();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


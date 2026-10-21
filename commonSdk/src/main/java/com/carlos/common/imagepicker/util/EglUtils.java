/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.opengl.EGL14
 *  android.opengl.EGLConfig
 *  android.opengl.EGLContext
 *  android.opengl.EGLDisplay
 *  android.opengl.EGLSurface
 *  android.opengl.GLES10
 *  android.opengl.GLES20
 *  android.os.Build$VERSION
 *  android.util.Log
 *  javax.microedition.khronos.egl.EGL10
 *  javax.microedition.khronos.egl.EGLConfig
 *  javax.microedition.khronos.egl.EGLContext
 *  javax.microedition.khronos.egl.EGLDisplay
 *  javax.microedition.khronos.egl.EGLSurface
 */
package com.carlos.common.imagepicker.util;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import com.kook.librelease.StringFog;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLSurface;

public class EglUtils {
    private static final String TAG = "EglUtils";

    private EglUtils() {
    }

    public static int getMaxTextureSize() {
        try {
            if (Build.VERSION.SDK_INT >= 17) {
                return EglUtils.getMaxTextureEgl14();
            }
            return EglUtils.getMaxTextureEgl10();
        }
        catch (Exception e) {
            Log.d((String)TAG, (String)"getMaxTextureSize: ", (Throwable)e);
            return 0;
        }
    }

    @TargetApi(value=17)
    private static int getMaxTextureEgl14() {
        EGLDisplay dpy = EGL14.eglGetDisplay((int)0);
        int[] vers = new int[2];
        EGL14.eglInitialize((EGLDisplay)dpy, (int[])vers, (int)0, (int[])vers, (int)1);
        int[] configAttr = new int[]{12351, 12430, 12329, 0, 12352, 4, 12339, 1, 12344};
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        EGL14.eglChooseConfig((EGLDisplay)dpy, (int[])configAttr, (int)0, (EGLConfig[])configs, (int)0, (int)1, (int[])numConfig, (int)0);
        if (numConfig[0] == 0) {
            return 0;
        }
        EGLConfig config = configs[0];
        int[] surfAttr = new int[]{12375, 64, 12374, 64, 12344};
        android.opengl.EGLSurface surf = EGL14.eglCreatePbufferSurface((EGLDisplay)dpy, (EGLConfig)config, (int[])surfAttr, (int)0);
        int[] ctxAttrib = new int[]{12440, 2, 12344};
        EGLContext ctx = EGL14.eglCreateContext((EGLDisplay)dpy, (EGLConfig)config, (EGLContext)EGL14.EGL_NO_CONTEXT, (int[])ctxAttrib, (int)0);
        EGL14.eglMakeCurrent((EGLDisplay)dpy, (android.opengl.EGLSurface)surf, (android.opengl.EGLSurface)surf, (EGLContext)ctx);
        int[] maxSize = new int[1];
        GLES20.glGetIntegerv((int)3379, (int[])maxSize, (int)0);
        EGL14.eglMakeCurrent((EGLDisplay)dpy, (android.opengl.EGLSurface)EGL14.EGL_NO_SURFACE, (android.opengl.EGLSurface)EGL14.EGL_NO_SURFACE, (EGLContext)EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroySurface((EGLDisplay)dpy, (android.opengl.EGLSurface)surf);
        EGL14.eglDestroyContext((EGLDisplay)dpy, (EGLContext)ctx);
        EGL14.eglTerminate((EGLDisplay)dpy);
        return maxSize[0];
    }

    @TargetApi(value=14)
    private static int getMaxTextureEgl10() {
        EGL10 egl = (EGL10)javax.microedition.khronos.egl.EGLContext.getEGL();
        javax.microedition.khronos.egl.EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers = new int[2];
        egl.eglInitialize(dpy, vers);
        int[] configAttr = new int[]{12351, 12430, 12329, 0, 12339, 1, 12344};
        javax.microedition.khronos.egl.EGLConfig[] configs = new javax.microedition.khronos.egl.EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
        if (numConfig[0] == 0) {
            return 0;
        }
        javax.microedition.khronos.egl.EGLConfig config = configs[0];
        int[] surfAttr = new int[]{12375, 64, 12374, 64, 12344};
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        int EGL_CONTEXT_CLIENT_VERSION = 12440;
        int[] ctxAttrib = new int[]{12440, 1, 12344};
        javax.microedition.khronos.egl.EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv((int)3379, (int[])maxSize, (int)0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }
}


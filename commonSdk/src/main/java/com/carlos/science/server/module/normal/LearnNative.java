/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.science.server.module.normal;

import com.carlos.libcommon.StringFog;

public class LearnNative {
    public static native void setVersion(String var0, Object var1);

    public static native String getName();

    public static native void setStringUTFChars(String var0);

    public static native void mallocException();

    public static native void hookLearn(Class var0, Object var1);

    static {
        System.loadLibrary("learn");
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.kook.common.jni;

import java.util.List;

public class FileInfoNative {
    public static native void fileTimeInfo(String var0);

    public static native List<String> getFileList(String var0);

    public static native String fileInfoToJson(String var0);

    static {
        System.loadLibrary("common");
    }
}


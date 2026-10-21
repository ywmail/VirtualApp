/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package com.carlos.science.client.hyxd;

import android.os.Build;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import mirror.dalvik.system.VMRuntime;

public class HYXDNative {
    private static final String TAG = "HYXDNative";
    private static final String LIB_NAME = "hyxd";
    private static final String LIB_NAME_64 = "hyxd_64";

    public static boolean is64bit() {
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            // empty if block
        }
        return VMRuntime.is64Bit.call(VMRuntime.getRuntime.call(new Object[0]), new Object[0]);
    }

    public static native void init(int var0, String var1);

    public static native void searchWrite(String var0, String var1, boolean var2);

    public static native void memoryTest();

    static {
        try {
            if (HYXDNative.is64bit()) {
                System.loadLibrary("hyxd_64");
            } else {
                System.loadLibrary("hyxd");
            }
        }
        catch (Throwable e) {
            HVLog.printThrowable(e);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.swift.sandhook;

import android.util.Log;
import com.swift.sandhook.SandHookConfig;

public class HookLog {
    public static final String TAG = "SandHook";
    public static boolean DEBUG = SandHookConfig.DEBUG;

    public static int v(String s) {
        return Log.v((String)TAG, (String)s);
    }

    public static int i(String s) {
        return Log.i((String)TAG, (String)s);
    }

    public static int d(String s) {
        return Log.d((String)TAG, (String)s);
    }

    public static int w(String s) {
        return Log.w((String)TAG, (String)s);
    }

    public static int e(String s) {
        return Log.e((String)TAG, (String)s);
    }

    public static int e(String s, Throwable t) {
        return Log.e((String)TAG, (String)s, (Throwable)t);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.HandlerThread
 */
package com.lody.virtual.os;

import android.os.Handler;
import android.os.HandlerThread;
import com.lody.virtual.StringFog;

public final class BackgroundThread
extends HandlerThread {
    private static BackgroundThread sInstance;
    private static Handler sHandler;

    private BackgroundThread() {
        super("va.android.bg", 10);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new BackgroundThread();
            sInstance.start();
            sHandler = new Handler(sInstance.getLooper());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static BackgroundThread get() {
        Class<BackgroundThread> clazz = BackgroundThread.class;
        synchronized (BackgroundThread.class) {
            BackgroundThread.ensureThreadLocked();
            // ** MonitorExit[var0] (shouldn't be in output)
            return sInstance;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Handler getHandler() {
        Class<BackgroundThread> clazz = BackgroundThread.class;
        synchronized (BackgroundThread.class) {
            BackgroundThread.ensureThreadLocked();
            // ** MonitorExit[var0] (shouldn't be in output)
            return sHandler;
        }
    }
}


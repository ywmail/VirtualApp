/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Looper
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  org.jdeferred.android.AndroidDeferredManager
 */
package com.carlos.common.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import org.jdeferred.android.AndroidDeferredManager;

public class ResponseProgram {
    private static final AndroidDeferredManager androidDeferredManager = new AndroidDeferredManager();
    private static final AndroidDeferredManager gDM = new AndroidDeferredManager();
    private static final Handler gUiHandler = new Handler(Looper.getMainLooper());

    public static AndroidDeferredManager defer() {
        return androidDeferredManager;
    }

    public static int dpToPx(Context context, int dp) {
        return (int)TypedValue.applyDimension((int)1, (float)dp, (DisplayMetrics)context.getResources().getDisplayMetrics());
    }

    public static void post(Runnable r) {
        gUiHandler.post(r);
    }

    public static void postDelayed(long delay, Runnable r) {
        gUiHandler.postDelayed(r, delay);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.ActivityManager$TaskDescription
 *  android.app.WallpaperManager
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.res.TypedArray
 *  android.graphics.Bitmap
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 */
package com.lody.virtual.client.fixer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import mirror.com.android.internal.R_Hide;

public final class ActivityFixer {
    private ActivityFixer() {
    }

    public static void fixActivity(Activity activity) {
        Context baseContext = activity.getBaseContext();
        try {
            TypedArray typedArray = activity.obtainStyledAttributes(R_Hide.styleable.Window.get());
            if (typedArray != null) {
                boolean fullscreen;
                boolean showWallpaper = typedArray.getBoolean(R_Hide.styleable.Window_windowShowWallpaper.get(), false);
                if (showWallpaper) {
                    activity.getWindow().setBackgroundDrawable(WallpaperManager.getInstance((Context)activity).getDrawable());
                }
                if (fullscreen = typedArray.getBoolean(R_Hide.styleable.Window_windowFullscreen.get(), false)) {
                    activity.getWindow().addFlags(1024);
                }
                typedArray.recycle();
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Intent intent = activity.getIntent();
            ApplicationInfo applicationInfo = baseContext.getApplicationInfo();
            PackageManager pm = activity.getPackageManager();
            if (intent != null && activity.isTaskRoot()) {
                try {
                    String label = applicationInfo.loadLabel(pm) + "";
                    Bitmap icon = null;
                    Drawable drawable2 = applicationInfo.loadIcon(pm);
                    if (drawable2 instanceof BitmapDrawable) {
                        icon = ((BitmapDrawable)drawable2).getBitmap();
                    }
                    activity.setTaskDescription(new ActivityManager.TaskDescription(label, icon));
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


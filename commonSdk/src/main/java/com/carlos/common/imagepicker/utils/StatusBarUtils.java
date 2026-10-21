/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.Window
 *  androidx.annotation.ColorInt
 *  androidx.appcompat.widget.Toolbar
 */
package com.carlos.common.imagepicker.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.ColorInt;
import androidx.appcompat.widget.Toolbar;
import com.kook.librelease.StringFog;
import java.lang.reflect.Field;

public class StatusBarUtils {
    private static final int DEFAULT_STATUS_BAR_ALPHA = 0;

    public static void setColor(Activity activity, @ColorInt int color2) {
        StatusBarUtils.setBarColor(activity, color2);
    }

    public static void setBarColor(Activity activity, @ColorInt int color2) {
        if (Build.VERSION.SDK_INT >= 19) {
            Window win = activity.getWindow();
            View decorView = win.getDecorView();
            win.addFlags(0x4000000);
            if (Build.VERSION.SDK_INT >= 21) {
                win.clearFlags(0x4000000);
                int option = 1280;
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | option);
                win.addFlags(Integer.MIN_VALUE);
                win.setStatusBarColor(color2);
            }
        }
    }

    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        StatusBarUtils.setColor(activity, 0);
    }

    public static void fixToolbar(Toolbar toolbar, Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            int statusHeight = StatusBarUtils.getStatusBarHeight((Context)activity);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)toolbar.getLayoutParams();
            layoutParams.setMargins(0, statusHeight, 0, 0);
        }
    }

    public static void fixTitlebar(ViewGroup titlebar, Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            int statusHeight = StatusBarUtils.getStatusBarHeight((Context)activity);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)titlebar.getLayoutParams();
            layoutParams.setMargins(0, statusHeight, 0, 0);
        }
    }

    @SuppressLint(value={"PrivateApi"})
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    private static int calculateStatusColor(@ColorInt int color2, int alpha) {
        float a = 1.0f - (float)alpha / 255.0f;
        int red = color2 >> 16 & 0xFF;
        int green = color2 >> 8 & 0xFF;
        int blue = color2 & 0xFF;
        red = (int)((double)((float)red * a) + 0.5);
        green = (int)((double)((float)green * a) + 0.5);
        blue = (int)((double)((float)blue * a) + 0.5);
        return 0xFF000000 | red << 16 | green << 8 | blue;
    }
}


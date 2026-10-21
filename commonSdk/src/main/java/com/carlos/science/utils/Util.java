/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 */
package com.carlos.science.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import com.carlos.libcommon.StringFog;

public class Util {
    public static void setBackground(View view, Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable2);
        } else {
            view.setBackgroundDrawable(drawable2);
        }
    }

    public static boolean isOnePlus() {
        return Util.getManufacturer().contains("oneplus");
    }

    public static String getManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        manufacturer = manufacturer.toLowerCase();
        return manufacturer;
    }
}


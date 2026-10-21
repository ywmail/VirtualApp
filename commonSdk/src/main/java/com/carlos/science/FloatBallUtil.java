/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.WindowManager$LayoutParams
 */
package com.carlos.science;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;
import com.carlos.libcommon.StringFog;

public class FloatBallUtil {
    public static boolean inSingleActivity;

    public static WindowManager.LayoutParams getLayoutParams(Context context) {
        return FloatBallUtil.getLayoutParams(context, false);
    }

    public static WindowManager.LayoutParams getLayoutParams(Context context, boolean listenBackEvent) {
        int sdkInt;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = 262696;
        if (listenBackEvent) {
            layoutParams.flags &= 0xFFFFFFF7;
        }
        layoutParams.type = context == null || !(context instanceof Activity) ? ((sdkInt = Build.VERSION.SDK_INT) < 19 ? 2002 : (sdkInt < 25 ? ("Xiaomi".equalsIgnoreCase(Build.MANUFACTURER) ? 2002 : 2005) : (sdkInt < 26 ? 2002 : 2038))) : 2;
        layoutParams.format = 1;
        layoutParams.gravity = 51;
        layoutParams.width = -2;
        layoutParams.height = -2;
        return layoutParams;
    }

    public static WindowManager.LayoutParams getStatusBarLayoutParams(Context context) {
        int sdkInt;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = 0;
        layoutParams.height = 0;
        layoutParams.flags = 40;
        layoutParams.gravity = 51;
        layoutParams.type = context == null || !(context instanceof Activity) ? ((sdkInt = Build.VERSION.SDK_INT) < 19 ? 2002 : (sdkInt < 25 ? 2005 : (sdkInt < 26 ? 2002 : 2038))) : 2;
        return layoutParams;
    }
}


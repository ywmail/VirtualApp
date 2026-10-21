/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.carlos.science.utils;

import android.content.Context;

public class DensityUtil {
    public static int dip2px(Context context, float dpValue) {
        float scale = DensityUtil.getScale(context);
        return (int)(dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = DensityUtil.getScale(context);
        return (int)(pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = DensityUtil.getScale(context);
        return (int)(pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = DensityUtil.getScale(context);
        return (int)(spValue * fontScale + 0.5f);
    }

    private static float getScale(Context context) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return DensityUtil.findScale(fontScale);
    }

    private static float findScale(float scale) {
        if (scale <= 1.0f) {
            scale = 1.0f;
        } else if ((double)scale <= 1.5) {
            scale = 1.5f;
        } else if (scale <= 2.0f) {
            scale = 2.0f;
        } else if (scale <= 3.0f) {
            scale = 3.0f;
        }
        return scale;
    }
}


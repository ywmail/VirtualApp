/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.RectF
 */
package com.carlos.common.imagepicker.util;

import android.graphics.RectF;

public class RectUtils {
    public static float[] getCornersFromRect(RectF r) {
        return new float[]{r.left, r.top, r.right, r.top, r.right, r.bottom, r.left, r.bottom};
    }

    public static float[] getRectSidesFromCorners(float[] corners) {
        return new float[]{(float)Math.sqrt(Math.pow(corners[0] - corners[2], 2.0) + Math.pow(corners[1] - corners[3], 2.0)), (float)Math.sqrt(Math.pow(corners[2] - corners[4], 2.0) + Math.pow(corners[3] - corners[5], 2.0))};
    }

    public static float[] getCenterFromRect(RectF r) {
        return new float[]{r.centerX(), r.centerY()};
    }

    public static RectF trapToRect(float[] array) {
        RectF r = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        for (int i = 1; i < array.length; i += 2) {
            float x = (float)Math.round(array[i - 1] * 10.0f) / 10.0f;
            float y = (float)Math.round(array[i] * 10.0f) / 10.0f;
            r.left = x < r.left ? x : r.left;
            r.top = y < r.top ? y : r.top;
            r.right = x > r.right ? x : r.right;
            r.bottom = y > r.bottom ? y : r.bottom;
        }
        r.sort();
        return r;
    }
}


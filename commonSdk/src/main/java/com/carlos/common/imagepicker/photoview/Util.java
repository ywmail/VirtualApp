/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 */
package com.carlos.common.imagepicker.photoview;

import android.widget.ImageView;
import com.kook.librelease.StringFog;

class Util {
    Util() {
    }

    static void checkZoomLevels(float minZoom, float midZoom, float maxZoom) {
        if (minZoom >= midZoom) {
            throw new IllegalArgumentException("Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value");
        }
        if (midZoom >= maxZoom) {
            throw new IllegalArgumentException("Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value");
        }
    }

    static boolean hasDrawable(ImageView imageView) {
        return imageView.getDrawable() != null;
    }

    static boolean isSupportedScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            return false;
        }
        switch (scaleType) {
            case MATRIX: {
                throw new IllegalStateException("Matrix scale type is not supported");
            }
        }
        return true;
    }

    static int getPointerIndex(int action) {
        return (action & 0xFF00) >> 8;
    }
}


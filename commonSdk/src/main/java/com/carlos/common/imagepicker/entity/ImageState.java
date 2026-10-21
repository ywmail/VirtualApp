/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.RectF
 */
package com.carlos.common.imagepicker.entity;

import android.graphics.RectF;

public class ImageState {
    private RectF mCropRect;
    private RectF mCurrentImageRect;
    private float mCurrentScale;
    private float mCurrentAngle;

    public ImageState(RectF cropRect, RectF currentImageRect, float currentScale, float currentAngle) {
        this.mCropRect = cropRect;
        this.mCurrentImageRect = currentImageRect;
        this.mCurrentScale = currentScale;
        this.mCurrentAngle = currentAngle;
    }

    public RectF getCropRect() {
        return this.mCropRect;
    }

    public RectF getCurrentImageRect() {
        return this.mCurrentImageRect;
    }

    public float getCurrentScale() {
        return this.mCurrentScale;
    }

    public float getCurrentAngle() {
        return this.mCurrentAngle;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.carlos.science.floatball;

import android.graphics.drawable.Drawable;

public class FloatBallCfg {
    public Drawable mIcon;
    public int mSize;
    public Gravity mGravity;
    public int mOffsetY = 0;
    public boolean mHideHalfLater = true;

    public FloatBallCfg(int size, Drawable icon) {
        this(size, icon, Gravity.LEFT_TOP, 0);
    }

    public FloatBallCfg(int size, Drawable icon, Gravity gravity) {
        this(size, icon, gravity, 0);
    }

    public FloatBallCfg(int size, Drawable icon, Gravity gravity, int offsetY) {
        this.mSize = size;
        this.mIcon = icon;
        this.mGravity = gravity;
        this.mOffsetY = offsetY;
    }

    public FloatBallCfg(int size, Drawable icon, Gravity gravity, boolean hideHalfLater) {
        this.mSize = size;
        this.mIcon = icon;
        this.mGravity = gravity;
        this.mHideHalfLater = hideHalfLater;
    }

    public FloatBallCfg(int size, Drawable icon, Gravity gravity, int offsetY, boolean hideHalfLater) {
        this.mSize = size;
        this.mIcon = icon;
        this.mGravity = gravity;
        this.mOffsetY = offsetY;
        this.mHideHalfLater = hideHalfLater;
    }

    public void setGravity(Gravity gravity) {
        this.mGravity = gravity;
    }

    public void setHideHalfLater(boolean hideHalfLater) {
        this.mHideHalfLater = hideHalfLater;
    }

    public static enum Gravity {
        LEFT_TOP(51),
        LEFT_CENTER(19),
        LEFT_BOTTOM(83),
        RIGHT_TOP(53),
        RIGHT_CENTER(21),
        RIGHT_BOTTOM(85);

        int mValue;

        private Gravity(int gravity) {
            this.mValue = gravity;
        }

        public int getGravity() {
            return this.mValue;
        }
    }
}


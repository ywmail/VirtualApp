/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.StateListDrawable
 */
package com.carlos.common.imagepicker.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class SelectedStateListDrawable
extends StateListDrawable {
    private int mSelectionColor;

    public SelectedStateListDrawable(Drawable drawable2, int selectionColor) {
        this.mSelectionColor = selectionColor;
        this.addState(new int[]{0x10100A1}, drawable2);
        this.addState(new int[0], drawable2);
    }

    protected boolean onStateChange(int[] states) {
        boolean isStatePressedInArray = false;
        for (int state : states) {
            if (state != 0x10100A1) continue;
            isStatePressedInArray = true;
        }
        if (isStatePressedInArray) {
            super.setColorFilter(this.mSelectionColor, PorterDuff.Mode.SRC_ATOP);
        } else {
            super.clearColorFilter();
        }
        return super.onStateChange(states);
    }

    public boolean isStateful() {
        return true;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.carlos.science.menu;

import android.graphics.drawable.Drawable;

public abstract class MenuItem {
    public Drawable mDrawable;

    public MenuItem(Drawable drawable2) {
        this.mDrawable = drawable2;
    }

    public abstract void action();
}


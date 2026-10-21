/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.DrawableRes
 */
package com.carlos.common.widget.tablayout.listener;

import androidx.annotation.DrawableRes;

public interface CustomTabEntity {
    public String getTabTitle();

    @DrawableRes
    public int getTabSelectedIcon();

    @DrawableRes
    public int getTabUnselectedIcon();
}


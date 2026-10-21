/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 */
package com.carlos.common.ui.activity.abs;

import android.app.Activity;
import android.content.Context;

public interface BaseView<T> {
    public Activity getActivity();

    public Context getContext();

    public void setPresenter(T var1);
}


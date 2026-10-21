/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.carlos.science.runner;

import android.content.Context;

public interface ICarrier {
    public Context getContext();

    public void onMove(int var1, int var2, int var3, int var4);

    public void onDone();

    public boolean post(Runnable var1);

    public boolean removeCallbacks(Runnable var1);
}


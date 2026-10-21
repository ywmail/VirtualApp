/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.carlos.science;

import android.content.Context;

public interface LocationService {
    public void onLocationChanged(int var1, int var2);

    public int[] onRestoreLocation();

    public void start(Context var1);
}


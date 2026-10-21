/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  androidx.annotation.NonNull
 */
package com.carlos.common.imagepicker.callback;

import android.net.Uri;
import androidx.annotation.NonNull;

public interface BitmapCropCallback {
    public void onBitmapCropped(@NonNull Uri var1, int var2, int var3, int var4, int var5);

    public void onCropFailure(@NonNull Throwable var1);
}


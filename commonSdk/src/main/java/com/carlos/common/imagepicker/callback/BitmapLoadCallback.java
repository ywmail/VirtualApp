/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  androidx.annotation.NonNull
 *  androidx.annotation.Nullable
 */
package com.carlos.common.imagepicker.callback;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.entity.ExifInfo;

public interface BitmapLoadCallback {
    public void onBitmapLoaded(@NonNull Bitmap var1, @NonNull ExifInfo var2, @NonNull String var3, @Nullable String var4);

    public void onFailure(@NonNull Exception var1);
}


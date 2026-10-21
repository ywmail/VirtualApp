/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Build$VERSION
 *  androidx.annotation.RequiresApi
 *  androidx.core.app.ActivityCompat
 *  androidx.core.content.ContextCompat
 */
package com.carlos.common.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.carlos.common.imagepicker.utils.PermissionsConstant;
import com.kook.librelease.StringFog;

public class PermissionsUtils {
    public static boolean checkReadStoragePermission(Activity activity) {
        boolean readStoragePermissionGranted;
        if (Build.VERSION.SDK_INT < 16) {
            return true;
        }
        int readStoragePermissionState = ContextCompat.checkSelfPermission((Context)activity, (String)"android.permission.READ_EXTERNAL_STORAGE");
        boolean bl = readStoragePermissionGranted = readStoragePermissionState == 0;
        if (!readStoragePermissionGranted) {
            ActivityCompat.requestPermissions((Activity)activity, (String[])PermissionsConstant.PERMISSIONS_EXTERNAL_READ, (int)2);
        }
        return readStoragePermissionGranted;
    }

    @RequiresApi(api=23)
    public static boolean checkWriteStoragePermission(Activity activity) {
        boolean writeStoragePermissionGranted;
        int writeStoragePermissionState = ContextCompat.checkSelfPermission((Context)activity, (String)"android.permission.WRITE_EXTERNAL_STORAGE");
        boolean bl = writeStoragePermissionGranted = writeStoragePermissionState == 0;
        if (!writeStoragePermissionGranted) {
            activity.requestPermissions(PermissionsConstant.PERMISSIONS_EXTERNAL_WRITE, 3);
        }
        return writeStoragePermissionGranted;
    }

    @RequiresApi(api=23)
    public static boolean checkCameraPermission(Activity activity) {
        boolean cameraPermissionGranted;
        int cameraPermissionState = ContextCompat.checkSelfPermission((Context)activity, (String)"android.permission.CAMERA");
        boolean bl = cameraPermissionGranted = cameraPermissionState == 0;
        if (!cameraPermissionGranted) {
            activity.requestPermissions(PermissionsConstant.PERMISSIONS_CAMERA, 1);
        }
        return cameraPermissionGranted;
    }
}


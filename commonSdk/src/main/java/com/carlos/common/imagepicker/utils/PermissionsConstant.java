/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.RequiresApi
 */
package com.carlos.common.imagepicker.utils;

import androidx.annotation.RequiresApi;
import com.kook.librelease.StringFog;

public class PermissionsConstant {
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_EXTERNAL_READ = 2;
    public static final int REQUEST_EXTERNAL_WRITE = 3;
    public static final String[] PERMISSIONS_CAMERA = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final String[] PERMISSIONS_EXTERNAL_WRITE = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
    @RequiresApi(api=16)
    public static final String[] PERMISSIONS_EXTERNAL_READ = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 */
package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.stub.StubManifest;

public class RequestExternalStorageManagerActivity
extends Activity {
    public static void request(Context context, boolean isExt) {
        Intent intent = new Intent();
        if (isExt) {
            intent.setClassName(StubManifest.EXT_PACKAGE_NAME, RequestExternalStorageManagerActivity.class.getName());
        } else {
            intent.setClassName(StubManifest.PACKAGE_NAME, RequestExternalStorageManagerActivity.class.getName());
        }
        intent.setFlags(0x10000000);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
            intent.addFlags(0x10000000);
            this.startActivity(intent);
        }
        this.finish();
    }
}


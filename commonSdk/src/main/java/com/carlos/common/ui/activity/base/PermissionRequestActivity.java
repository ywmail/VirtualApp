/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.widget.Toast
 *  androidx.annotation.NonNull
 *  androidx.annotation.Nullable
 */
package com.carlos.common.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kook.librelease.StringFog;
import com.lody.virtual.helper.compat.PermissionCompat;

@TargetApi(value=23)
public class PermissionRequestActivity
extends Activity {
    private static final int REQUEST_PERMISSION_CODE = 995;
    private static final String EXTRA_PERMISSIONS = "extra.permission";
    private static final String EXTRA_APP_NAME = "extra.app_name";
    private static final String EXTRA_USER_ID = "extra.user_id";
    private static final String EXTRA_PACKAGE_NAME = "extra.package_name";
    private int userId;
    private String appName;
    private String packageName;

    public static void requestPermission(@NonNull Activity activity, @NonNull String[] permissions, @NonNull String appName, int userId, @NonNull String packageName, int requestCode) {
        Intent intent = new Intent((Context)activity, PermissionRequestActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(EXTRA_APP_NAME, appName);
        intent.putExtra(EXTRA_PACKAGE_NAME, packageName);
        intent.putExtra(EXTRA_USER_ID, userId);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(0, 0);
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        String[] permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS);
        this.appName = intent.getStringExtra(EXTRA_APP_NAME);
        this.packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
        this.userId = intent.getIntExtra(EXTRA_USER_ID, -1);
        this.requestPermissions(permissions, 995);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionCompat.isRequestGranted(grantResults)) {
            Intent data = new Intent();
            data.putExtra("pkg", this.packageName);
            data.putExtra("user_id", this.userId);
            this.setResult(-1, data);
        } else {
            this.runOnUiThread(() -> Toast.makeText((Context)this, (CharSequence)("启动" + this.appName + "失败"), (int)0).show());
        }
        this.finish();
    }
}


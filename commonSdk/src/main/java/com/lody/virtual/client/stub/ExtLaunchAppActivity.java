/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Intent
 *  android.os.Bundle
 */
package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.ipc.VActivityManager;

public class ExtLaunchAppActivity
extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.finish();
        Intent intent = this.getIntent();
        if (intent != null) {
            int userid = intent.getIntExtra("_VA_|_userid_", -1);
            String pkg = intent.getStringExtra("_VA_|_package_");
            if (userid >= 0 && pkg != null && !pkg.isEmpty()) {
                VActivityManager.get().launchApp(userid, pkg);
            }
        }
    }
}


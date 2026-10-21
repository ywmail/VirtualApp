/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Parcelable
 */
package com.lody.virtual.remote;

import android.content.Intent;
import android.os.Parcelable;
import com.lody.virtual.StringFog;

public class BroadcastIntentData {
    public int userId;
    public Intent intent;
    public String targetPackage;

    public BroadcastIntentData(int userId, Intent intent, String targetPackage) {
        this.userId = userId;
        this.intent = intent;
        this.targetPackage = targetPackage;
    }

    public BroadcastIntentData(Intent proxyIntent) {
        this.userId = proxyIntent.getIntExtra("_VA_|_user_id_", -1);
        this.intent = (Intent)proxyIntent.getParcelableExtra("_VA_|_intent_");
        this.targetPackage = proxyIntent.getStringExtra("_VA_|_target_pkg_");
    }

    public void saveIntent(Intent proxyIntent) {
        proxyIntent.putExtra("_VA_|_user_id_", this.userId);
        proxyIntent.putExtra("_VA_|_intent_", (Parcelable)this.intent);
        proxyIntent.putExtra("_VA_|_target_pkg_", this.targetPackage);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningTaskInfo
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  androidx.annotation.Nullable
 */
package com.carlos.common.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.kook.librelease.StringFog;

public class BackHomeActivity
extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager am = (ActivityManager)this.getSystemService("activity");
        boolean existTask = false;
        Class<?> homeActivityClz = null;
        try {
            homeActivityClz = Class.forName("com.carlos.home.HomeActivity");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ComponentName homeActivity = new ComponentName((Context)this, homeActivityClz);
        for (ActivityManager.RunningTaskInfo info : am.getRunningTasks(Integer.MAX_VALUE)) {
            if (info.baseActivity != null && info.baseActivity.equals((Object)homeActivity)) {
                am.moveTaskToFront(info.id, 0);
                existTask = true;
                break;
            }
            if (info.topActivity == null || !info.topActivity.equals((Object)homeActivity)) continue;
            am.moveTaskToFront(info.id, 0);
            existTask = true;
            break;
        }
        if (!existTask) {
            Intent intent = new Intent((Context)this, homeActivityClz);
            intent.addFlags(0x10000000);
            intent.addFlags(131072);
            this.startActivity(intent);
        }
        this.finish();
    }
}


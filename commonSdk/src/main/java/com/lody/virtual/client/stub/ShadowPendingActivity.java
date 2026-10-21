/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.os.Bundle
 */
package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;

public class ShadowPendingActivity
extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.finish();
        Intent intent = this.getIntent();
        ComponentUtils.IntentSenderInfo info = null;
        try {
            info = ComponentUtils.parseIntentSenderInfo(intent, true);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        if (info != null && info.userId != -1) {
            ActivityInfo activityInfo = VirtualCore.get().resolveActivityInfo(info.intent, info.userId);
            if (activityInfo == null) {
                VLog.e(ShadowPendingActivity.class.getSimpleName(), "failed to resolve intent: " + intent);
                return;
            }
            if (info.callerActivity == null || this.isTaskRoot()) {
                info.intent.addFlags(0x10000000);
                VActivityManager.get().startActivity(info.intent, activityInfo, null, info.options, null, -1, info.targetPkg, info.userId);
            } else {
                info.intent.addFlags(0x2000000);
                VActivityManager.get().startActivity(info.intent, activityInfo, info.callerActivity, info.options, null, -1, info.targetPkg, info.userId);
            }
        }
    }
}


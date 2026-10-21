/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.os.IBinder
 */
package com.lody.virtual.client.stub;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.ComponentUtils;

public class ShadowPendingService
extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.stopSelf();
        ComponentUtils.IntentSenderInfo info = null;
        try {
            info = ComponentUtils.parseIntentSenderInfo(intent, false);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        if (info != null && info.userId != -1) {
            VActivityManager.get().startService((Context)this, info.intent, info.userId);
        }
        return 2;
    }
}


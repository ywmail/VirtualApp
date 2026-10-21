/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.Intent
 *  android.os.IBinder
 */
package com.lody.virtual.client.stub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.HiddenForeNotification;

public class KeepAliveService
extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        if (!VirtualCore.getConfig().isHideForegroundNotification()) {
            HiddenForeNotification.bindForeground(this);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }
}


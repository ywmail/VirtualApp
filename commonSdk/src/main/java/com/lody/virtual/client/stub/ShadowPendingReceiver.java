/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 */
package com.lody.virtual.client.stub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.lody.virtual.helper.utils.ComponentUtils;

public class ShadowPendingReceiver
extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        ComponentUtils.IntentSenderInfo info = null;
        try {
            info = ComponentUtils.parseIntentSenderInfo(intent, false);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        if (info != null && info.userId != -1) {
            Intent redirectIntent = ComponentUtils.proxyBroadcastIntent(info.intent, info.userId);
            context.sendBroadcast(redirectIntent);
        }
    }
}


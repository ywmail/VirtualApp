/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  com.kook.librelease.R$string
 */
package com.lody.virtual.client.stub;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import com.kook.librelease.R;
import com.lody.virtual.helper.compat.NotificationChannelCompat;

public class HiddenForeNotification
extends Service {
    private static final int ID = 2781;

    public static void bindForeground(Service service) {
        Notification.Builder builder = NotificationChannelCompat.createBuilder(service.getApplicationContext(), NotificationChannelCompat.DAEMON_ID);
        builder.setSmallIcon(17301544);
        if (Build.VERSION.SDK_INT > 24) {
            builder.setContentTitle((CharSequence)service.getString(R.string.keep_service_damon_noti_title_v24));
            builder.setContentText((CharSequence)service.getString(R.string.keep_service_damon_noti_text_v24));
        } else {
            builder.setContentTitle((CharSequence)service.getString(R.string.keep_service_damon_noti_title));
            builder.setContentText((CharSequence)service.getString(R.string.keep_service_damon_noti_text));
            builder.setContentIntent(PendingIntent.getService((Context)service, (int)0, (Intent)new Intent((Context)service, HiddenForeNotification.class), (int)0));
        }
        builder.setSound(null);
        service.startForeground(2781, builder.getNotification());
        if (Build.VERSION.SDK_INT <= 24) {
            service.startService(new Intent((Context)service, HiddenForeNotification.class));
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Notification.Builder builder = NotificationChannelCompat.createBuilder(this.getBaseContext(), NotificationChannelCompat.DAEMON_ID);
            builder.setSmallIcon(17301544);
            builder.setContentTitle((CharSequence)this.getString(R.string.keep_service_noti_title));
            builder.setContentText((CharSequence)this.getString(R.string.keep_service_noti_text));
            builder.setSound(null);
            this.startForeground(2781, builder.getNotification());
            this.stopForeground(true);
            this.stopSelf();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 2;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}


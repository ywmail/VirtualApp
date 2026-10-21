/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Notification$Builder
 *  android.app.NotificationChannel
 *  android.app.NotificationManager
 *  android.content.Context
 *  android.os.Build$VERSION
 */
package com.lody.virtual.helper.compat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;

public class NotificationChannelCompat {
    public static final String DAEMON_ID = Constants.NOTIFICATION_DAEMON_CHANNEL;
    public static final String DEFAULT_ID = Constants.NOTIFICATION_CHANNEL;

    public static boolean enable() {
        if (Build.VERSION.SDK_INT > 26) {
            return VirtualCore.get().getTargetSdkVersion() >= 26;
        }
        return false;
    }

    @TargetApi(value=26)
    public static void checkOrCreateChannel(Context context, String channelId, String name) {
        NotificationManager manager;
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= 26 && (channel = (manager = (NotificationManager)context.getSystemService("notification")).getNotificationChannel(channelId)) == null) {
            channel = new NotificationChannel(channelId, (CharSequence)name, 4);
            channel.setDescription("Compatibility of old versions");
            channel.setSound(null, null);
            channel.setShowBadge(false);
            try {
                manager.createNotificationChannel(channel);
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static Notification.Builder createBuilder(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= 26 && VirtualCore.get().getTargetSdkVersion() >= 26) {
            return new Notification.Builder(context, channelId);
        }
        return new Notification.Builder(context);
    }
}


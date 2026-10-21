/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.PendingIntent
 *  android.content.Context
 */
package mirror.android.app;

import android.app.PendingIntent;
import android.content.Context;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class Notification {
    public static Class<?> TYPE = RefClass.load(Notification.class, android.app.Notification.class);
    @MethodParams(value={Context.class, CharSequence.class, CharSequence.class, PendingIntent.class})
    public static RefMethod<Void> setLatestEventInfo;
}


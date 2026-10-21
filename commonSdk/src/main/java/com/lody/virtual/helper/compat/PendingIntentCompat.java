/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.os.IBinder
 *  android.os.Parcel
 */
package com.lody.virtual.helper.compat;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.Parcel;

public class PendingIntentCompat {
    public static PendingIntent readPendingIntent(IBinder intentSender) {
        Parcel parcel = Parcel.obtain();
        parcel.writeStrongBinder(intentSender);
        parcel.setDataPosition(0);
        try {
            PendingIntent pendingIntent = PendingIntent.readPendingIntentOrNullFromParcel((Parcel)parcel);
            return pendingIntent;
        }
        finally {
            parcel.recycle();
        }
    }
}


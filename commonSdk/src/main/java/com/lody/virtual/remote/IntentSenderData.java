/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.helper.compat.PendingIntentCompat;

public class IntentSenderData
implements Parcelable {
    public String targetPkg;
    public IBinder token;
    public int type;
    public int userId;
    public static final Parcelable.Creator<IntentSenderData> CREATOR = new Parcelable.Creator<IntentSenderData>(){

        public IntentSenderData createFromParcel(Parcel source) {
            return new IntentSenderData(source);
        }

        public IntentSenderData[] newArray(int size) {
            return new IntentSenderData[size];
        }
    };

    public IntentSenderData(String targetPkg, IBinder token, int type, int userId) {
        this.targetPkg = targetPkg;
        this.token = token;
        this.type = type;
        this.userId = userId;
    }

    public PendingIntent getPendingIntent() {
        return PendingIntentCompat.readPendingIntent(this.token);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.targetPkg);
        dest.writeStrongBinder(this.token);
        dest.writeInt(this.type);
        dest.writeInt(this.userId);
    }

    protected IntentSenderData(Parcel in) {
        this.targetPkg = in.readString();
        this.token = in.readStrongBinder();
        this.type = in.readInt();
        this.userId = in.readInt();
    }

    public void update(IntentSenderData sender) {
        this.targetPkg = sender.targetPkg;
        this.type = sender.type;
    }
}


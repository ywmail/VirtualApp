/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class AppTaskInfo
implements Parcelable {
    public static final Parcelable.Creator<AppTaskInfo> CREATOR = new Parcelable.Creator<AppTaskInfo>(){

        public AppTaskInfo createFromParcel(Parcel source) {
            return new AppTaskInfo(source);
        }

        public AppTaskInfo[] newArray(int size) {
            return new AppTaskInfo[size];
        }
    };
    public int taskId;
    public Intent baseIntent;
    public ComponentName baseActivity;
    public ComponentName topActivity;

    public AppTaskInfo(int taskId, Intent baseIntent, ComponentName baseActivity, ComponentName topActivity) {
        this.taskId = taskId;
        this.baseIntent = baseIntent;
        this.baseActivity = baseActivity;
        this.topActivity = topActivity;
    }

    protected AppTaskInfo(Parcel in) {
        this.taskId = in.readInt();
        this.baseIntent = (Intent)in.readParcelable(Intent.class.getClassLoader());
        this.baseActivity = (ComponentName)in.readParcelable(ComponentName.class.getClassLoader());
        this.topActivity = (ComponentName)in.readParcelable(ComponentName.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskId);
        dest.writeParcelable((Parcelable)this.baseIntent, flags);
        dest.writeParcelable((Parcelable)this.baseActivity, flags);
        dest.writeParcelable((Parcelable)this.topActivity, flags);
    }
}


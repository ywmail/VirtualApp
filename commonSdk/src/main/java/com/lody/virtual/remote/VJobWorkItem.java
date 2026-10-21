/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.job.JobWorkItem
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.annotation.TargetApi;
import android.app.job.JobWorkItem;
import android.os.Parcel;
import android.os.Parcelable;

@TargetApi(value=26)
public class VJobWorkItem
implements Parcelable {
    private JobWorkItem item;
    public static final Parcelable.Creator<VJobWorkItem> CREATOR = new Parcelable.Creator<VJobWorkItem>(){

        public VJobWorkItem createFromParcel(Parcel source) {
            return new VJobWorkItem(source);
        }

        public VJobWorkItem[] newArray(int size) {
            return new VJobWorkItem[size];
        }
    };

    public JobWorkItem get() {
        return this.item;
    }

    public void set(JobWorkItem item) {
        this.item = item;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable)this.item, flags);
    }

    public VJobWorkItem() {
    }

    public VJobWorkItem(JobWorkItem item) {
        this.item = item;
    }

    protected VJobWorkItem(Parcel in) {
        this.item = (JobWorkItem)in.readParcelable(JobWorkItem.class.getClassLoader());
    }
}


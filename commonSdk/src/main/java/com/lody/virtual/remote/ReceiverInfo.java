/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.IntentFilter
 *  android.content.pm.ActivityInfo
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class ReceiverInfo
implements Parcelable {
    public ActivityInfo info;
    public List<IntentFilter> filters;
    public static final Parcelable.Creator<ReceiverInfo> CREATOR = new Parcelable.Creator<ReceiverInfo>(){

        public ReceiverInfo createFromParcel(Parcel source) {
            return new ReceiverInfo(source);
        }

        public ReceiverInfo[] newArray(int size) {
            return new ReceiverInfo[size];
        }
    };

    public ReceiverInfo(ActivityInfo receiverInfo, List<IntentFilter> filters) {
        this.info = receiverInfo;
        this.filters = filters;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable)this.info, flags);
        dest.writeTypedList(this.filters);
    }

    protected ReceiverInfo(Parcel in) {
        this.info = (ActivityInfo)in.readParcelable(ActivityInfo.class.getClassLoader());
        this.filters = in.createTypedArrayList(IntentFilter.CREATOR);
    }
}


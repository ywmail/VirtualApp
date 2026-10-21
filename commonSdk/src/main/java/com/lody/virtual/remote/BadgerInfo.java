/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.os.VUserHandle;

public class BadgerInfo
implements Parcelable {
    public int userId;
    public String packageName;
    public int badgerCount;
    public String className;
    public static final Parcelable.Creator<BadgerInfo> CREATOR = new Parcelable.Creator<BadgerInfo>(){

        public BadgerInfo createFromParcel(Parcel source) {
            return new BadgerInfo(source);
        }

        public BadgerInfo[] newArray(int size) {
            return new BadgerInfo[size];
        }
    };

    public BadgerInfo() {
        this.userId = VUserHandle.myUserId();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.packageName);
        dest.writeInt(this.badgerCount);
        dest.writeString(this.className);
    }

    protected BadgerInfo(Parcel in) {
        this.userId = in.readInt();
        this.packageName = in.readString();
        this.badgerCount = in.readInt();
        this.className = in.readString();
    }
}


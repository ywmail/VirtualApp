/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;

public class SyncInfo {
    public final int authorityId;
    public final Account account;
    public final String authority;
    public final long startTime;
    public static final Parcelable.Creator<SyncInfo> CREATOR = new Parcelable.Creator<SyncInfo>(){

        public SyncInfo createFromParcel(Parcel in) {
            return new SyncInfo(in);
        }

        public SyncInfo[] newArray(int size) {
            return new SyncInfo[size];
        }
    };

    public SyncInfo(int authorityId, Account account, String authority, long startTime) {
        this.authorityId = authorityId;
        this.account = account;
        this.authority = authority;
        this.startTime = startTime;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.authorityId);
        this.account.writeToParcel(parcel, 0);
        parcel.writeString(this.authority);
        parcel.writeLong(this.startTime);
    }

    SyncInfo(Parcel parcel) {
        this.authorityId = parcel.readInt();
        this.account = new Account(parcel);
        this.authority = parcel.readString();
        this.startTime = parcel.readLong();
    }

    public android.content.SyncInfo create() {
        return mirror.android.content.SyncInfo.ctor.newInstance(this.authorityId, this.account, this.authority, this.startTime);
    }
}


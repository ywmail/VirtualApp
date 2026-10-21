/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.server.content;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.StringFog;
import mirror.android.content.SyncInfo;

public class VSyncInfo
implements Parcelable {
    private static final Account REDACTED_ACCOUNT = new Account("*****", "*****");
    public final int authorityId;
    public final Account account;
    public final String authority;
    public final long startTime;
    public static final Parcelable.Creator<VSyncInfo> CREATOR = new Parcelable.Creator<VSyncInfo>(){

        public VSyncInfo createFromParcel(Parcel in) {
            return new VSyncInfo(in);
        }

        public VSyncInfo[] newArray(int size) {
            return new VSyncInfo[size];
        }
    };

    public static VSyncInfo createAccountRedacted(int authorityId, String authority, long startTime) {
        return new VSyncInfo(authorityId, REDACTED_ACCOUNT, authority, startTime);
    }

    public VSyncInfo(int authorityId, Account account, String authority, long startTime) {
        this.authorityId = authorityId;
        this.account = account;
        this.authority = authority;
        this.startTime = startTime;
    }

    public VSyncInfo(VSyncInfo other) {
        this.authorityId = other.authorityId;
        this.account = new Account(other.account.name, other.account.type);
        this.authority = other.authority;
        this.startTime = other.startTime;
    }

    public android.content.SyncInfo toSyncInfo() {
        return SyncInfo.ctor.newInstance(this.authorityId, this.account, this.authority, this.startTime);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.authorityId);
        parcel.writeParcelable((Parcelable)this.account, flags);
        parcel.writeString(this.authority);
        parcel.writeLong(this.startTime);
    }

    VSyncInfo(Parcel parcel) {
        this.authorityId = parcel.readInt();
        this.account = (Account)parcel.readParcelable(Account.class.getClassLoader());
        this.authority = parcel.readString();
        this.startTime = parcel.readLong();
    }
}


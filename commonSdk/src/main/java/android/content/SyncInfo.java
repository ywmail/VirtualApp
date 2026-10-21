/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package android.content;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.StringFog;

public class SyncInfo
implements Parcelable {
    private static final Account REDACTED_ACCOUNT = new Account("*****", "*****");
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

    public static SyncInfo createAccountRedacted(int authorityId, String authority, long startTime) {
        throw new RuntimeException("Stub!");
    }

    public SyncInfo(int authorityId, Account account, String authority, long startTime) {
        throw new RuntimeException("Stub!");
    }

    public SyncInfo(SyncInfo other) {
        throw new RuntimeException("Stub!");
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

    SyncInfo(Parcel parcel) {
        throw new RuntimeException("Stub!");
    }
}


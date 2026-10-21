/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.server.accounts;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

public class VAccount
implements Parcelable {
    public static final Parcelable.Creator<VAccount> CREATOR = new Parcelable.Creator<VAccount>(){

        public VAccount createFromParcel(Parcel source) {
            return new VAccount(source);
        }

        public VAccount[] newArray(int size) {
            return new VAccount[size];
        }
    };
    public int userId;
    public String name;
    public String previousName;
    public String type;
    public String password;
    public long lastAuthenticatedTime;
    public Map<String, String> authTokens;
    public Map<String, String> userDatas;

    public VAccount(int userId, Account account) {
        this.userId = userId;
        this.name = account.name;
        this.type = account.type;
        this.authTokens = new HashMap<String, String>();
        this.userDatas = new HashMap<String, String>();
    }

    public VAccount(Parcel in) {
        this.userId = in.readInt();
        this.name = in.readString();
        this.previousName = in.readString();
        this.type = in.readString();
        this.password = in.readString();
        this.lastAuthenticatedTime = in.readLong();
        int authTokensSize = in.readInt();
        this.authTokens = new HashMap<String, String>(authTokensSize);
        for (int i = 0; i < authTokensSize; ++i) {
            String key = in.readString();
            String value = in.readString();
            this.authTokens.put(key, value);
        }
        int userDatasSize = in.readInt();
        this.userDatas = new HashMap<String, String>(userDatasSize);
        for (int i = 0; i < userDatasSize; ++i) {
            String key = in.readString();
            String value = in.readString();
            this.userDatas.put(key, value);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.previousName);
        dest.writeString(this.type);
        dest.writeString(this.password);
        dest.writeLong(this.lastAuthenticatedTime);
        dest.writeInt(this.authTokens.size());
        for (Map.Entry<String, String> entry : this.authTokens.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeInt(this.userDatas.size());
        for (Map.Entry<String, String> entry : this.userDatas.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }
}


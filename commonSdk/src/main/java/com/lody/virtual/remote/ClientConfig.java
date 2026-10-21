/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class ClientConfig
implements Parcelable {
    public boolean isExt;
    public int vpid;
    public int vuid;
    public String processName;
    public String packageName;
    public IBinder token;
    public static final Parcelable.Creator<ClientConfig> CREATOR = new Parcelable.Creator<ClientConfig>(){

        public ClientConfig createFromParcel(Parcel source) {
            return new ClientConfig(source);
        }

        public ClientConfig[] newArray(int size) {
            return new ClientConfig[size];
        }
    };

    public ClientConfig() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isExt ? (byte)1 : 0);
        dest.writeInt(this.vpid);
        dest.writeInt(this.vuid);
        dest.writeString(this.processName);
        dest.writeString(this.packageName);
        dest.writeStrongBinder(this.token);
    }

    protected ClientConfig(Parcel in) {
        this.isExt = in.readByte() != 0;
        this.vpid = in.readInt();
        this.vuid = in.readInt();
        this.processName = in.readString();
        this.packageName = in.readString();
        this.token = in.readStrongBinder();
    }
}


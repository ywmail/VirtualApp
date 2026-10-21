/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote.vloc;

import android.os.Parcel;
import android.os.Parcelable;

public class VWifi
implements Parcelable {
    public String ssid;
    public String bssid;
    public String capabilities;
    public int level;
    public int frequency;
    public long timestamp;
    public static final Parcelable.Creator<VWifi> CREATOR = new Parcelable.Creator<VWifi>(){

        public VWifi createFromParcel(Parcel source) {
            return new VWifi(source);
        }

        public VWifi[] newArray(int size) {
            return new VWifi[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ssid);
        dest.writeString(this.bssid);
        dest.writeString(this.capabilities);
        dest.writeInt(this.level);
        dest.writeInt(this.frequency);
        dest.writeLong(this.timestamp);
    }

    public VWifi() {
    }

    public VWifi(Parcel in) {
        this.ssid = in.readString();
        this.bssid = in.readString();
        this.capabilities = in.readString();
        this.level = in.readInt();
        this.frequency = in.readInt();
        this.timestamp = in.readLong();
    }
}


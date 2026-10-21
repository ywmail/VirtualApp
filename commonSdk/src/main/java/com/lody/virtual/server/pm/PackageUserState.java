/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.server.pm;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageUserState
implements Parcelable {
    public static final Parcelable.Creator<PackageUserState> CREATOR = new Parcelable.Creator<PackageUserState>(){

        public PackageUserState createFromParcel(Parcel source) {
            return new PackageUserState(source);
        }

        public PackageUserState[] newArray(int size) {
            return new PackageUserState[size];
        }
    };
    public boolean launched;
    public boolean hidden;
    public boolean installed;

    public PackageUserState() {
        this.installed = false;
        this.launched = true;
        this.hidden = false;
    }

    protected PackageUserState(Parcel in) {
        this.launched = in.readByte() != 0;
        this.hidden = in.readByte() != 0;
        this.installed = in.readByte() != 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.launched ? (byte)1 : 0);
        dest.writeByte(this.hidden ? (byte)1 : 0);
        dest.writeByte(this.installed ? (byte)1 : 0);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.kook.deviceinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ThermalModel
implements Parcelable {
    private final String temp;
    private final String type;
    public static final Parcelable.Creator<ThermalModel> CREATOR = new Parcelable.Creator<ThermalModel>(){

        public ThermalModel createFromParcel(Parcel in) {
            return new ThermalModel(in);
        }

        public ThermalModel[] newArray(int size) {
            return new ThermalModel[size];
        }
    };

    public ThermalModel(String temp, String type) {
        this.temp = temp;
        this.type = type;
    }

    protected ThermalModel(Parcel in) {
        this.temp = in.readString();
        this.type = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.temp);
        dest.writeString(this.type);
    }

    public int describeContents() {
        return 0;
    }

    public String getTemp() {
        return this.temp;
    }

    public String getType() {
        return this.type;
    }
}


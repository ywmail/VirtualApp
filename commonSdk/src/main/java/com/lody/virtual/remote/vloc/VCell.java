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

public class VCell
implements Parcelable {
    public int type;
    public int mcc;
    public int mnc;
    public int psc;
    public int lac;
    public int cid;
    public int baseStationId;
    public int systemId;
    public int networkId;
    public static final Parcelable.Creator<VCell> CREATOR = new Parcelable.Creator<VCell>(){

        public VCell createFromParcel(Parcel source) {
            return new VCell(source);
        }

        public VCell[] newArray(int size) {
            return new VCell[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.mcc);
        dest.writeInt(this.mnc);
        dest.writeInt(this.psc);
        dest.writeInt(this.lac);
        dest.writeInt(this.cid);
        dest.writeInt(this.baseStationId);
        dest.writeInt(this.systemId);
        dest.writeInt(this.networkId);
    }

    public VCell() {
    }

    public VCell(Parcel in) {
        this.type = in.readInt();
        this.mcc = in.readInt();
        this.mnc = in.readInt();
        this.psc = in.readInt();
        this.lac = in.readInt();
        this.cid = in.readInt();
        this.baseStationId = in.readInt();
        this.systemId = in.readInt();
        this.networkId = in.readInt();
    }
}


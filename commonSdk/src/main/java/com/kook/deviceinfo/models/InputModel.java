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
import com.kook.deviceinfo.models.BaseModel;

public class InputModel
extends BaseModel
implements Parcelable {
    private final String name;
    private final String desc;
    private final String vendorId;
    private final String proId;
    private final String hasVibrator;
    private final String keyboardType;
    private final String deviceId;
    private final String sources;
    private final String axis;
    private final String range;
    private final String flat;
    private final String fuzz;
    private final String resol;
    private final String source;
    private final boolean hasMotionRange;
    public static final Parcelable.Creator<InputModel> CREATOR = new Parcelable.Creator<InputModel>(){

        public InputModel createFromParcel(Parcel in) {
            return new InputModel(in);
        }

        public InputModel[] newArray(int size) {
            return new InputModel[size];
        }
    };

    public InputModel(String name, String desc, String vendorId, String proId, String hasVibrator, String keyboardType, String deviceId, String sources, String axis, String range, String flat, String fuzz, String resol, String source, boolean hasMotionRange) {
        this.name = name;
        this.desc = desc;
        this.vendorId = vendorId;
        this.proId = proId;
        this.hasVibrator = hasVibrator;
        this.keyboardType = keyboardType;
        this.deviceId = deviceId;
        this.sources = sources;
        this.axis = axis;
        this.range = range;
        this.flat = flat;
        this.fuzz = fuzz;
        this.resol = resol;
        this.source = source;
        this.hasMotionRange = hasMotionRange;
    }

    protected InputModel(Parcel in) {
        this.name = in.readString();
        this.desc = in.readString();
        this.vendorId = in.readString();
        this.proId = in.readString();
        this.hasVibrator = in.readString();
        this.keyboardType = in.readString();
        this.deviceId = in.readString();
        this.sources = in.readString();
        this.axis = in.readString();
        this.range = in.readString();
        this.flat = in.readString();
        this.fuzz = in.readString();
        this.resol = in.readString();
        this.source = in.readString();
        this.hasMotionRange = in.readByte() == 1;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public String getProId() {
        return this.proId;
    }

    public String getHasVibrator() {
        return this.hasVibrator;
    }

    public String getKeyboardType() {
        return this.keyboardType;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getSources() {
        return this.sources;
    }

    public String getAxis() {
        return this.axis;
    }

    public String getRange() {
        return this.range;
    }

    public String getFlat() {
        return this.flat;
    }

    public String getFuzz() {
        return this.fuzz;
    }

    public String getResol() {
        return this.resol;
    }

    public String getSource() {
        return this.source;
    }

    public boolean isHasMotionRange() {
        return this.hasMotionRange;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.vendorId);
        dest.writeString(this.proId);
        dest.writeString(this.hasVibrator);
        dest.writeString(this.keyboardType);
        dest.writeString(this.deviceId);
        dest.writeString(this.sources);
        dest.writeString(this.axis);
        dest.writeString(this.range);
        dest.writeString(this.flat);
        dest.writeString(this.fuzz);
        dest.writeString(this.resol);
        dest.writeString(this.source);
        dest.writeByte((byte)(this.hasMotionRange ? 1 : 0));
    }

    @Override
    void addProperty() {
        this.addProperty("name", this.name);
        this.addProperty("desc", this.desc);
        this.addProperty("vendorId", this.vendorId);
        this.addProperty("proId", this.proId);
        this.addProperty("hasVibrator", this.hasVibrator);
        this.addProperty("keyboardType", this.keyboardType);
        this.addProperty("deviceId", this.deviceId);
        this.addProperty("sources", this.sources);
        this.addProperty("axis", this.axis);
        this.addProperty("range", this.range);
        this.addProperty("flat", this.flat);
        this.addProperty("fuzz", this.fuzz);
        this.addProperty("resol", this.resol);
        this.addProperty("source", this.source);
        this.addProperty("hasMotionRange", this.hasMotionRange);
    }
}


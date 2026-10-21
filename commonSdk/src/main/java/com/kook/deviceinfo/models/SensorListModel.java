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

public class SensorListModel
extends BaseModel
implements Parcelable {
    int id;
    String name;
    String vendor;
    String stringType;
    float power;
    int version;
    float resolution;
    float maximumRange;
    int fifoMaxEventCount;
    int fifoReservedEventCount;
    int maxDelay;
    int minDelay;
    int reportingMode;
    public static final Parcelable.Creator<SensorListModel> CREATOR = new Parcelable.Creator<SensorListModel>(){

        public SensorListModel createFromParcel(Parcel in) {
            return new SensorListModel(in);
        }

        public SensorListModel[] newArray(int size) {
            return new SensorListModel[size];
        }
    };

    public SensorListModel(int id2, String name, String vendor, String stringType, float power, int version, float resolution, float maximumRange, int fifoMaxEventCount, int fifoReservedEventCount, int maxDelay, int minDelay, int reportingMode) {
        this.id = id2;
        this.name = name;
        this.vendor = vendor;
        this.stringType = stringType;
        this.power = power;
        this.version = version;
        this.resolution = resolution;
        this.maximumRange = maximumRange;
        this.fifoMaxEventCount = fifoMaxEventCount;
        this.fifoReservedEventCount = fifoReservedEventCount;
        this.maxDelay = maxDelay;
        this.minDelay = minDelay;
        this.reportingMode = reportingMode;
    }

    protected SensorListModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.vendor = in.readString();
        this.stringType = in.readString();
        this.power = in.readInt();
        this.version = in.readInt();
        this.resolution = in.readInt();
        this.maximumRange = in.readInt();
        this.fifoMaxEventCount = in.readInt();
        this.fifoReservedEventCount = in.readInt();
        this.maxDelay = in.readInt();
        this.minDelay = in.readInt();
        this.reportingMode = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.vendor);
        dest.writeString(this.stringType);
        dest.writeFloat(this.power);
        dest.writeInt(this.version);
        dest.writeFloat(this.resolution);
        dest.writeFloat(this.maximumRange);
        dest.writeInt(this.fifoMaxEventCount);
        dest.writeInt(this.fifoReservedEventCount);
        dest.writeInt(this.maxDelay);
        dest.writeInt(this.minDelay);
        dest.writeInt(this.reportingMode);
    }

    public int describeContents() {
        return 0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return this.vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getStringType() {
        return this.stringType;
    }

    public void setStringType(String stringType) {
        this.stringType = stringType;
    }

    public float getPower() {
        return this.power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public float getResolution() {
        return this.resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    public float getMaximumRange() {
        return this.maximumRange;
    }

    public void setMaximumRange(float maximumRange) {
        this.maximumRange = maximumRange;
    }

    public int getFifoMaxEventCount() {
        return this.fifoMaxEventCount;
    }

    public void setFifoMaxEventCount(int fifoMaxEventCount) {
        this.fifoMaxEventCount = fifoMaxEventCount;
    }

    public int getFifoReservedEventCount() {
        return this.fifoReservedEventCount;
    }

    public void setFifoReservedEventCount(int fifoReservedEventCount) {
        this.fifoReservedEventCount = fifoReservedEventCount;
    }

    public int getMaxDelay() {
        return this.maxDelay;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

    public int getMinDelay() {
        return this.minDelay;
    }

    public void setMinDelay(int minDelay) {
        this.minDelay = minDelay;
    }

    public int getReportingMode() {
        return this.reportingMode;
    }

    public void setReportingMode(int reportingMode) {
        this.reportingMode = reportingMode;
    }

    @Override
    void addProperty() {
        this.addProperty("id", this.id);
        this.addProperty("name", this.name);
        this.addProperty("vendor", this.vendor);
        this.addProperty("stringType", this.stringType);
        this.addProperty("power", Float.valueOf(this.power));
        this.addProperty("version", this.version);
        this.addProperty("resolution", Float.valueOf(this.resolution));
        this.addProperty("maximumRange", Float.valueOf(this.maximumRange));
        this.addProperty("fifoMaxEventCount", this.fifoMaxEventCount);
        this.addProperty("fifoReservedEventCount", this.fifoReservedEventCount);
        this.addProperty("maxDelay", this.maxDelay);
        this.addProperty("minDelay", this.minDelay);
        this.addProperty("reportingMode", this.reportingMode);
    }

    public String toString() {
        return "SensorListModel{id=" + this.id + ", name='" + this.name + '\'' + ", vendor='" + this.vendor + '\'' + ", stringType='" + this.stringType + '\'' + ", power=" + this.power + ", version=" + this.version + ", resolution=" + this.resolution + ", maximumRange=" + this.maximumRange + ", fifoMaxEventCount=" + this.fifoMaxEventCount + ", fifoReservedEventCount=" + this.fifoReservedEventCount + ", maxDelay=" + this.maxDelay + ", minDelay=" + this.minDelay + ", reportingMode=" + this.reportingMode + '}';
    }
}


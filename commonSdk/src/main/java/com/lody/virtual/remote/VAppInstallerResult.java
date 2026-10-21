/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.os.Parcel;
import android.os.Parcelable;

public class VAppInstallerResult
implements Parcelable {
    public static final int FLAG_IS_SPLIT_PACKAGE = 1;
    public static final int FLAG_PACKAGE_UPDATED = 2;
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAILURE_BLOCKED = 2;
    public static final int STATUS_FAILURE_ABORTED = 3;
    public static final int STATUS_FAILURE_INVALID = 4;
    public static final int STATUS_FAILURE_CONFLICT = 5;
    public static final int STATUS_FAILURE_STORAGE = 6;
    public static final int STATUS_FAILURE_INCOMPATIBLE = 7;
    public static final int STATUS_FAILURE_NO_BASE_APK = 8;
    public String packageName;
    public int status = 0;
    public int flags;
    public static final Parcelable.Creator<VAppInstallerResult> CREATOR = new Parcelable.Creator<VAppInstallerResult>(){

        public VAppInstallerResult createFromParcel(Parcel source) {
            return new VAppInstallerResult(source);
        }

        public VAppInstallerResult[] newArray(int size) {
            return new VAppInstallerResult[size];
        }
    };

    public VAppInstallerResult() {
    }

    public VAppInstallerResult(int status) {
        this.status = status;
    }

    public VAppInstallerResult(int status, int flags) {
        this.status = status;
        this.flags = flags;
    }

    public VAppInstallerResult(String packageName, int status, int flags) {
        this.packageName = packageName;
        this.status = status;
        this.flags = flags;
    }

    public VAppInstallerResult(String packageName, int status) {
        this.packageName = packageName;
        this.status = status;
    }

    public static VAppInstallerResult create(String packageName, int errorCode) {
        return new VAppInstallerResult(packageName, errorCode);
    }

    public static VAppInstallerResult create(int errorCode) {
        return new VAppInstallerResult(errorCode);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeInt(this.status);
        dest.writeInt(this.flags);
    }

    protected VAppInstallerResult(Parcel in) {
        this.packageName = in.readString();
        this.status = in.readInt();
        this.flags = in.readInt();
    }
}


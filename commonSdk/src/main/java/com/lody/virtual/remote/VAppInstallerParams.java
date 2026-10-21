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

public class VAppInstallerParams
implements Parcelable {
    public static final int FLAG_INSTALL_NOTIFY = 1;
    public static final int FLAG_INSTALL_OVERRIDE_NO_CHECK = 2;
    public static final int FLAG_INSTALL_OVERRIDE_FORBIDDEN = 4;
    public static final int FLAG_INSTALL_OVERRIDE_DONT_KILL_APP = 8;
    public static final int FLAG_INSTALL_OVERRIDE_SKIP_ODEX = 16;
    public static final int MODE_FULL_INSTALL = 1;
    public static final int MODE_INHERIT_EXISTING = 2;
    private int installFlags = 0;
    private int mode = 1;
    private String cpuAbiOverride;
    public static final Parcelable.Creator<VAppInstallerParams> CREATOR = new Parcelable.Creator<VAppInstallerParams>(){

        public VAppInstallerParams createFromParcel(Parcel source) {
            return new VAppInstallerParams(source);
        }

        public VAppInstallerParams[] newArray(int size) {
            return new VAppInstallerParams[size];
        }
    };

    public VAppInstallerParams() {
    }

    public VAppInstallerParams(int installFlags) {
        this.installFlags = installFlags;
    }

    public VAppInstallerParams(int installFlags, int mode) {
        this.installFlags = installFlags;
        this.mode = mode;
    }

    public int getInstallFlags() {
        return this.installFlags;
    }

    public void setInstallFlags(int installFlags) {
        this.installFlags = installFlags;
    }

    public void addInstallFlags(int flags) {
        this.installFlags |= flags;
    }

    public void removeInstallFlags(int flags) {
        this.installFlags &= ~flags;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getCpuAbiOverride() {
        return this.cpuAbiOverride;
    }

    public void setCpuAbiOverride(String cpuAbiOverride) {
        this.cpuAbiOverride = cpuAbiOverride;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.installFlags);
        dest.writeInt(this.mode);
        dest.writeString(this.cpuAbiOverride);
    }

    protected VAppInstallerParams(Parcel in) {
        this.installFlags = in.readInt();
        this.mode = in.readInt();
        this.cpuAbiOverride = in.readString();
    }
}


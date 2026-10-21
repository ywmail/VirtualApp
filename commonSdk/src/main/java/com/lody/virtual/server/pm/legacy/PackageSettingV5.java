/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.SparseArray
 */
package com.lody.virtual.server.pm.legacy;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.lody.virtual.server.pm.PackageUserState;

@Deprecated
public class PackageSettingV5
implements Parcelable {
    public static final int CURRENT_VERSION = 5;
    public static final int MODE_APP_COPY_APK = 0;
    public static final int MODE_APP_USE_OUTSIDE_APK = 1;
    private static final PackageUserState DEFAULT_USER_STATE = new PackageUserState();
    public int version;
    public String packageName;
    public int appId;
    public int appMode;
    public SparseArray<PackageUserState> userState = new SparseArray();
    public int flag;
    public long firstInstallTime;
    public long lastUpdateTime;
    public static final Parcelable.Creator<PackageSettingV5> CREATOR = new Parcelable.Creator<PackageSettingV5>(){

        public PackageSettingV5 createFromParcel(Parcel source) {
            return new PackageSettingV5(5, source);
        }

        public PackageSettingV5[] newArray(int size) {
            return new PackageSettingV5[size];
        }
    };

    public PackageSettingV5() {
        this.version = 5;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeInt(this.appId);
        dest.writeInt(this.appMode);
        dest.writeSparseArray(this.userState);
        dest.writeInt(this.flag);
        dest.writeLong(this.firstInstallTime);
        dest.writeLong(this.lastUpdateTime);
    }

    public PackageSettingV5(int version, Parcel in) {
        this.version = version;
        this.packageName = in.readString();
        this.appId = in.readInt();
        this.appMode = in.readInt();
        this.userState = in.readSparseArray(PackageUserState.class.getClassLoader());
        this.flag = in.readInt();
        this.firstInstallTime = in.readLong();
        this.lastUpdateTime = in.readLong();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.util.SparseArray
 */
package com.lody.virtual.server.pm.legacy;

import android.os.Parcel;
import android.util.SparseArray;
import com.lody.virtual.server.pm.PackageUserState;

@Deprecated
public class PackageSettingV1 {
    public String packageName;
    public boolean notCopyApk;
    public int appId;
    public SparseArray<PackageUserState> userState;
    public int flag;

    public void readFromParcel(Parcel in, int version) {
        this.packageName = in.readString();
        in.readString();
        in.readString();
        this.notCopyApk = in.readByte() != 0;
        this.appId = in.readInt();
        this.userState = in.readSparseArray(PackageUserState.class.getClassLoader());
        in.readByte();
        if (version > 3) {
            this.flag = in.readInt();
        }
    }
}


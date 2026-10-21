/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.pm.PackageInstaller$SessionParams
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import mirror.android.content.pm.PackageInstaller;

@TargetApi(value=21)
public class SessionParams
implements Parcelable {
    public static final int MODE_INVALID = -1;
    public static final int MODE_FULL_INSTALL = 1;
    public static final int MODE_INHERIT_EXISTING = 2;
    public int mode = -1;
    public int installFlags;
    public int installLocation = 1;
    public long sizeBytes = -1L;
    public String appPackageName;
    public Bitmap appIcon;
    public String appLabel;
    public long appIconLastModified = -1L;
    public Uri originatingUri;
    public Uri referrerUri;
    public String abiOverride;
    public String volumeUuid;
    public String[] grantedRuntimePermissions;
    public static final Parcelable.Creator<SessionParams> CREATOR = new Parcelable.Creator<SessionParams>(){

        public SessionParams createFromParcel(Parcel source) {
            return new SessionParams(source);
        }

        public SessionParams[] newArray(int size) {
            return new SessionParams[size];
        }
    };

    public SessionParams(int mode) {
        this.mode = mode;
    }

    public android.content.pm.PackageInstaller.SessionParams build() {
        if (Build.VERSION.SDK_INT >= 23) {
            android.content.pm.PackageInstaller.SessionParams params = new android.content.pm.PackageInstaller.SessionParams(this.mode);
            PackageInstaller.SessionParamsMarshmallow.installFlags.set(params, this.installFlags);
            PackageInstaller.SessionParamsMarshmallow.installLocation.set(params, this.installLocation);
            PackageInstaller.SessionParamsMarshmallow.sizeBytes.set(params, this.sizeBytes);
            PackageInstaller.SessionParamsMarshmallow.appPackageName.set(params, this.appPackageName);
            PackageInstaller.SessionParamsMarshmallow.appIcon.set(params, this.appIcon);
            PackageInstaller.SessionParamsMarshmallow.appLabel.set(params, this.appLabel);
            PackageInstaller.SessionParamsMarshmallow.appIconLastModified.set(params, this.appIconLastModified);
            PackageInstaller.SessionParamsMarshmallow.originatingUri.set(params, this.originatingUri);
            PackageInstaller.SessionParamsMarshmallow.referrerUri.set(params, this.referrerUri);
            PackageInstaller.SessionParamsMarshmallow.abiOverride.set(params, this.abiOverride);
            PackageInstaller.SessionParamsMarshmallow.volumeUuid.set(params, this.volumeUuid);
            PackageInstaller.SessionParamsMarshmallow.grantedRuntimePermissions.set(params, this.grantedRuntimePermissions);
            return params;
        }
        android.content.pm.PackageInstaller.SessionParams params = new android.content.pm.PackageInstaller.SessionParams(this.mode);
        PackageInstaller.SessionParamsLOLLIPOP.installFlags.set(params, this.installFlags);
        PackageInstaller.SessionParamsLOLLIPOP.installLocation.set(params, this.installLocation);
        PackageInstaller.SessionParamsLOLLIPOP.sizeBytes.set(params, this.sizeBytes);
        PackageInstaller.SessionParamsLOLLIPOP.appPackageName.set(params, this.appPackageName);
        PackageInstaller.SessionParamsLOLLIPOP.appIcon.set(params, this.appIcon);
        PackageInstaller.SessionParamsLOLLIPOP.appLabel.set(params, this.appLabel);
        PackageInstaller.SessionParamsLOLLIPOP.appIconLastModified.set(params, this.appIconLastModified);
        PackageInstaller.SessionParamsLOLLIPOP.originatingUri.set(params, this.originatingUri);
        PackageInstaller.SessionParamsLOLLIPOP.referrerUri.set(params, this.referrerUri);
        PackageInstaller.SessionParamsLOLLIPOP.abiOverride.set(params, this.abiOverride);
        return params;
    }

    public static SessionParams create(android.content.pm.PackageInstaller.SessionParams sessionParams) {
        if (Build.VERSION.SDK_INT >= 23) {
            SessionParams params = new SessionParams(PackageInstaller.SessionParamsMarshmallow.mode.get(sessionParams));
            params.installFlags = PackageInstaller.SessionParamsMarshmallow.installFlags.get(sessionParams);
            params.installLocation = PackageInstaller.SessionParamsMarshmallow.installLocation.get(sessionParams);
            params.sizeBytes = PackageInstaller.SessionParamsMarshmallow.sizeBytes.get(sessionParams);
            params.appPackageName = PackageInstaller.SessionParamsMarshmallow.appPackageName.get(sessionParams);
            params.appIcon = PackageInstaller.SessionParamsMarshmallow.appIcon.get(sessionParams);
            params.appLabel = PackageInstaller.SessionParamsMarshmallow.appLabel.get(sessionParams);
            params.appIconLastModified = PackageInstaller.SessionParamsMarshmallow.appIconLastModified.get(sessionParams);
            params.originatingUri = PackageInstaller.SessionParamsMarshmallow.originatingUri.get(sessionParams);
            params.referrerUri = PackageInstaller.SessionParamsMarshmallow.referrerUri.get(sessionParams);
            params.abiOverride = PackageInstaller.SessionParamsMarshmallow.abiOverride.get(sessionParams);
            params.volumeUuid = PackageInstaller.SessionParamsMarshmallow.volumeUuid.get(sessionParams);
            params.grantedRuntimePermissions = PackageInstaller.SessionParamsMarshmallow.grantedRuntimePermissions.get(sessionParams);
            return params;
        }
        SessionParams params = new SessionParams(PackageInstaller.SessionParamsLOLLIPOP.mode.get(sessionParams));
        params.installFlags = PackageInstaller.SessionParamsLOLLIPOP.installFlags.get(sessionParams);
        params.installLocation = PackageInstaller.SessionParamsLOLLIPOP.installLocation.get(sessionParams);
        params.sizeBytes = PackageInstaller.SessionParamsLOLLIPOP.sizeBytes.get(sessionParams);
        params.appPackageName = PackageInstaller.SessionParamsLOLLIPOP.appPackageName.get(sessionParams);
        params.appIcon = PackageInstaller.SessionParamsLOLLIPOP.appIcon.get(sessionParams);
        params.appLabel = PackageInstaller.SessionParamsLOLLIPOP.appLabel.get(sessionParams);
        params.appIconLastModified = PackageInstaller.SessionParamsLOLLIPOP.appIconLastModified.get(sessionParams);
        params.originatingUri = PackageInstaller.SessionParamsLOLLIPOP.originatingUri.get(sessionParams);
        params.referrerUri = PackageInstaller.SessionParamsLOLLIPOP.referrerUri.get(sessionParams);
        params.abiOverride = PackageInstaller.SessionParamsLOLLIPOP.abiOverride.get(sessionParams);
        return params;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mode);
        dest.writeInt(this.installFlags);
        dest.writeInt(this.installLocation);
        dest.writeLong(this.sizeBytes);
        dest.writeString(this.appPackageName);
        dest.writeParcelable((Parcelable)this.appIcon, flags);
        dest.writeString(this.appLabel);
        dest.writeLong(this.appIconLastModified);
        dest.writeParcelable((Parcelable)this.originatingUri, flags);
        dest.writeParcelable((Parcelable)this.referrerUri, flags);
        dest.writeString(this.abiOverride);
        dest.writeString(this.volumeUuid);
        dest.writeStringArray(this.grantedRuntimePermissions);
    }

    protected SessionParams(Parcel in) {
        this.mode = in.readInt();
        this.installFlags = in.readInt();
        this.installLocation = in.readInt();
        this.sizeBytes = in.readLong();
        this.appPackageName = in.readString();
        this.appIcon = (Bitmap)in.readParcelable(Bitmap.class.getClassLoader());
        this.appLabel = in.readString();
        this.appIconLastModified = in.readLong();
        this.originatingUri = (Uri)in.readParcelable(Uri.class.getClassLoader());
        this.referrerUri = (Uri)in.readParcelable(Uri.class.getClassLoader());
        this.abiOverride = in.readString();
        this.volumeUuid = in.readString();
        this.grantedRuntimePermissions = in.createStringArray();
    }
}


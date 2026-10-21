/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.PackageInstaller$SessionInfo
 *  android.graphics.Bitmap
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.server.pm.installer;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import mirror.android.content.pm.PackageInstaller;

public class SessionInfo
implements Parcelable {
    public int sessionId;
    public String installerPackageName;
    public String resolvedBaseCodePath;
    public float progress;
    public boolean sealed;
    public boolean active;
    public int mode;
    public long sizeBytes;
    public String appPackageName;
    public Bitmap appIcon;
    public CharSequence appLabel;
    public static final Parcelable.Creator<SessionInfo> CREATOR = new Parcelable.Creator<SessionInfo>(){

        public SessionInfo createFromParcel(Parcel source) {
            return new SessionInfo(source);
        }

        public SessionInfo[] newArray(int size) {
            return new SessionInfo[size];
        }
    };

    public PackageInstaller.SessionInfo alloc() {
        PackageInstaller.SessionInfo sessionInfo = PackageInstaller.SessionInfo.ctor.newInstance();
        PackageInstaller.SessionInfo.sessionId.set(sessionInfo, this.sessionId);
        PackageInstaller.SessionInfo.installerPackageName.set(sessionInfo, this.installerPackageName);
        PackageInstaller.SessionInfo.resolvedBaseCodePath.set(sessionInfo, this.resolvedBaseCodePath);
        PackageInstaller.SessionInfo.progress.set(sessionInfo, this.progress);
        PackageInstaller.SessionInfo.sealed.set(sessionInfo, this.sealed);
        PackageInstaller.SessionInfo.active.set(sessionInfo, this.active);
        PackageInstaller.SessionInfo.mode.set(sessionInfo, this.mode);
        PackageInstaller.SessionInfo.sizeBytes.set(sessionInfo, this.sizeBytes);
        PackageInstaller.SessionInfo.appPackageName.set(sessionInfo, this.appPackageName);
        PackageInstaller.SessionInfo.appIcon.set(sessionInfo, this.appIcon);
        PackageInstaller.SessionInfo.appLabel.set(sessionInfo, this.appLabel);
        return sessionInfo;
    }

    public static SessionInfo realloc(PackageInstaller.SessionInfo sessionInfo) {
        SessionInfo info = new SessionInfo();
        info.sessionId = PackageInstaller.SessionInfo.sessionId.get(sessionInfo);
        info.installerPackageName = PackageInstaller.SessionInfo.installerPackageName.get(sessionInfo);
        info.resolvedBaseCodePath = PackageInstaller.SessionInfo.resolvedBaseCodePath.get(sessionInfo);
        info.progress = PackageInstaller.SessionInfo.progress.get(sessionInfo);
        info.sealed = PackageInstaller.SessionInfo.sealed.get(sessionInfo);
        info.active = PackageInstaller.SessionInfo.active.get(sessionInfo);
        info.mode = PackageInstaller.SessionInfo.mode.get(sessionInfo);
        info.sizeBytes = PackageInstaller.SessionInfo.sizeBytes.get(sessionInfo);
        info.appPackageName = PackageInstaller.SessionInfo.appPackageName.get(sessionInfo);
        info.appIcon = PackageInstaller.SessionInfo.appIcon.get(sessionInfo);
        info.appLabel = PackageInstaller.SessionInfo.appLabel.get(sessionInfo);
        return info;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sessionId);
        dest.writeString(this.installerPackageName);
        dest.writeString(this.resolvedBaseCodePath);
        dest.writeFloat(this.progress);
        dest.writeByte(this.sealed ? (byte)1 : 0);
        dest.writeByte(this.active ? (byte)1 : 0);
        dest.writeInt(this.mode);
        dest.writeLong(this.sizeBytes);
        dest.writeString(this.appPackageName);
        dest.writeParcelable((Parcelable)this.appIcon, flags);
        if (this.appLabel != null) {
            dest.writeString(this.appLabel.toString());
        }
    }

    public SessionInfo() {
    }

    protected SessionInfo(Parcel in) {
        this.sessionId = in.readInt();
        this.installerPackageName = in.readString();
        this.resolvedBaseCodePath = in.readString();
        this.progress = in.readFloat();
        this.sealed = in.readByte() != 0;
        this.active = in.readByte() != 0;
        this.mode = in.readInt();
        this.sizeBytes = in.readLong();
        this.appPackageName = in.readString();
        this.appIcon = (Bitmap)in.readParcelable(Bitmap.class.getClassLoader());
        this.appLabel = in.readString();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.os;

import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.StringFog;

public class VUserInfo
implements Parcelable {
    public static final int FLAG_MASK_USER_TYPE = 255;
    public static final int FLAG_PRIMARY = 1;
    public static final int FLAG_ADMIN = 2;
    public static final int FLAG_GUEST = 4;
    public static final int FLAG_RESTRICTED = 8;
    public static final int FLAG_INITIALIZED = 16;
    public static final int FLAG_MANAGED_PROFILE = 32;
    public static final int FLAG_DISABLED = 64;
    public static final int NO_PROFILE_GROUP_ID = -1;
    public int id;
    public int serialNumber;
    public String name;
    public String iconPath;
    public int flags;
    public long creationTime;
    public long lastLoggedInTime;
    public int profileGroupId;
    public boolean partial;
    public boolean guestToRemove;
    public static final Parcelable.Creator<VUserInfo> CREATOR = new Parcelable.Creator<VUserInfo>(){

        public VUserInfo createFromParcel(Parcel source) {
            return new VUserInfo(source);
        }

        public VUserInfo[] newArray(int size) {
            return new VUserInfo[size];
        }
    };

    public VUserInfo(int id2, String name, int flags) {
        this(id2, name, null, flags);
    }

    public VUserInfo(int id2, String name, String iconPath, int flags) {
        this.id = id2;
        this.name = name;
        this.flags = flags;
        this.iconPath = iconPath;
        this.profileGroupId = -1;
    }

    public boolean isPrimary() {
        return (this.flags & 1) == 1;
    }

    public boolean isAdmin() {
        return (this.flags & 2) == 2;
    }

    public boolean isGuest() {
        return (this.flags & 4) == 4;
    }

    public boolean isRestricted() {
        return (this.flags & 8) == 8;
    }

    public boolean isManagedProfile() {
        return (this.flags & 0x20) == 32;
    }

    public boolean isEnabled() {
        return (this.flags & 0x40) != 64;
    }

    public VUserInfo() {
    }

    public VUserInfo(int id2) {
        this.id = id2;
    }

    public VUserInfo(VUserInfo orig) {
        this.name = orig.name;
        this.iconPath = orig.iconPath;
        this.id = orig.id;
        this.flags = orig.flags;
        this.serialNumber = orig.serialNumber;
        this.creationTime = orig.creationTime;
        this.lastLoggedInTime = orig.lastLoggedInTime;
        this.partial = orig.partial;
        this.profileGroupId = orig.profileGroupId;
        this.guestToRemove = orig.guestToRemove;
    }

    public String toString() {
        return "UserInfo{" + this.id + ":" + this.name + ":" + Integer.toHexString(this.flags) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.iconPath);
        dest.writeInt(this.flags);
        dest.writeInt(this.serialNumber);
        dest.writeLong(this.creationTime);
        dest.writeLong(this.lastLoggedInTime);
        dest.writeInt(this.partial ? 1 : 0);
        dest.writeInt(this.profileGroupId);
        dest.writeInt(this.guestToRemove ? 1 : 0);
    }

    private VUserInfo(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.iconPath = source.readString();
        this.flags = source.readInt();
        this.serialNumber = source.readInt();
        this.creationTime = source.readLong();
        this.lastLoggedInTime = source.readLong();
        this.partial = source.readInt() != 0;
        this.profileGroupId = source.readInt();
        this.guestToRemove = source.readInt() != 0;
    }
}


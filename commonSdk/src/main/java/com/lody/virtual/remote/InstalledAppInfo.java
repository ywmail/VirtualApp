/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.File;
import java.util.List;
import mirror.dalvik.system.VMRuntime;

public final class InstalledAppInfo
implements Parcelable {
    public static final int FLAG_ENABLED_XPOSED_MODULE = 0x40000000;
    public static final int FLAG_XPOSED_MODULE = 0x10000000;
    public static final int FLAG_EXCLUDE_XPOSED_MODULE = 0x20000000;
    public String packageName;
    public String libPath;
    public boolean dynamic;
    public int flag;
    public int appId;
    public VPackage.XposedModule xposedModule;
    public String primaryCpuAbi;
    public String secondaryCpuAbi;
    public boolean is64bit;
    public static final Parcelable.Creator<InstalledAppInfo> CREATOR = new Parcelable.Creator<InstalledAppInfo>(){

        public InstalledAppInfo createFromParcel(Parcel source) {
            return new InstalledAppInfo(source);
        }

        public InstalledAppInfo[] newArray(int size) {
            return new InstalledAppInfo[size];
        }
    };

    public InstalledAppInfo(String packageName, String libPath, boolean dynamic, int flags, int appId, String primaryCpuAbi, String secondaryCpuAbi, boolean is64bit, VPackage.XposedModule xposedModule) {
        this.packageName = packageName;
        this.libPath = libPath;
        this.dynamic = dynamic;
        this.flag = flags;
        this.appId = appId;
        this.xposedModule = xposedModule;
        this.primaryCpuAbi = primaryCpuAbi;
        this.secondaryCpuAbi = secondaryCpuAbi;
        this.is64bit = is64bit;
    }

    public String getApkPath() {
        return this.getApkPath(VirtualCore.get().isExtPackage());
    }

    public String getApkPath(boolean isExt) {
        if (this.dynamic) {
            try {
                ApplicationInfo info = VirtualCore.get().getHostPackageManager().getApplicationInfo(this.packageName, 0L);
                return info.publicSourceDir;
            }
            catch (PackageManager.NameNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
        if (isExt) {
            return VEnvironment.getPackageFileExt(this.packageName).getPath();
        }
        return VEnvironment.getPackageFile(this.packageName).getPath();
    }

    public String getOatPath() {
        return this.getOatFile().getPath();
    }

    public File getOatFile() {
        return this.getOatFile(VirtualCore.get().isExtPackage(), VMRuntime.getCurrentInstructionSet.call(new Object[0]));
    }

    public File getOatFile(boolean isExt, String instructionSet) {
        return isExt ? VEnvironment.getOatFile(this.packageName, instructionSet) : VEnvironment.getOatFileExt(this.packageName, instructionSet);
    }

    public ApplicationInfo getApplicationInfo(int userId) {
        ApplicationInfo applicationInfo = VPackageManager.get().getApplicationInfo(this.packageName, 0, userId);
        if (applicationInfo != null && !VirtualCore.get().isVAppProcess() && !new File(applicationInfo.sourceDir).exists()) {
            applicationInfo.publicSourceDir = applicationInfo.sourceDir = this.getApkPath();
        }
        return applicationInfo;
    }

    public PackageInfo getPackageInfo(int userId) {
        return VPackageManager.get().getPackageInfo(this.packageName, 0, userId);
    }

    public int[] getInstalledUsers() {
        return VirtualCore.get().getPackageInstalledUsers(this.packageName);
    }

    public boolean isLaunched(int userId) {
        return VirtualCore.get().isPackageLaunched(userId, this.packageName);
    }

    public List<String> getSplitNames() {
        return VirtualCore.get().getInstalledSplitNames(this.packageName);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.libPath);
        dest.writeByte(this.dynamic ? (byte)1 : 0);
        dest.writeInt(this.flag);
        dest.writeInt(this.appId);
        dest.writeParcelable((Parcelable)this.xposedModule, flags);
        dest.writeString(this.primaryCpuAbi);
        dest.writeString(this.secondaryCpuAbi);
        dest.writeByte(this.is64bit ? (byte)1 : 0);
    }

    protected InstalledAppInfo(Parcel in) {
        this.packageName = in.readString();
        this.libPath = in.readString();
        this.dynamic = in.readByte() != 0;
        this.flag = in.readInt();
        this.appId = in.readInt();
        this.xposedModule = (VPackage.XposedModule)in.readParcelable(VPackage.XposedModule.class.getClassLoader());
        this.primaryCpuAbi = in.readString();
        this.secondaryCpuAbi = in.readString();
        this.is64bit = in.readByte() != 0;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.os.Parcel
 *  android.util.ArrayMap
 */
package com.lody.virtual.server.pm;

import android.net.Uri;
import android.os.Parcel;
import android.util.ArrayMap;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.PersistenceLayer;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import com.lody.virtual.server.pm.PackageCacheManager;
import com.lody.virtual.server.pm.PackageSetting;
import com.lody.virtual.server.pm.VAppManagerService;
import com.lody.virtual.server.pm.legacy.PackageSettingV1;
import com.lody.virtual.server.pm.legacy.PackageSettingV5;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

class PackagePersistenceLayer
extends PersistenceLayer {
    private static final char[] MAGIC = new char[]{'v', 'p', 'k', 'g'};
    private static final int CURRENT_VERSION = 6;
    public boolean changed = false;
    private VAppManagerService mService;

    PackagePersistenceLayer(VAppManagerService service) {
        super(VEnvironment.getPackageListFile());
        this.mService = service;
    }

    @Override
    public int getCurrentVersion() {
        return 6;
    }

    @Override
    public void writeMagic(Parcel p) {
        p.writeCharArray(MAGIC);
    }

    @Override
    public boolean verifyMagic(Parcel p) {
        char[] magic = p.createCharArray();
        return Arrays.equals(magic, MAGIC);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void writePersistenceData(Parcel p) {
        ArrayMap<String, VPackage> arrayMap = PackageCacheManager.PACKAGE_CACHE;
        synchronized (arrayMap) {
            p.writeInt(PackageCacheManager.PACKAGE_CACHE.size());
            for (VPackage pkg : PackageCacheManager.PACKAGE_CACHE.values()) {
                PackageSetting ps = (PackageSetting)pkg.mExtras;
                ps.writeToParcel(p, 0);
            }
        }
    }

    @Override
    public void readPersistenceData(Parcel p, int version) {
        if (version != 6) {
            if (version <= 5) {
                int count = p.readInt();
                ArrayList<PackageSettingV5> list = new ArrayList<PackageSettingV5>(count);
                while (count-- > 0) {
                    PackageSettingV5 settingV5;
                    if (version < 5) {
                        this.changed = true;
                        PackageSettingV1 v1 = new PackageSettingV1();
                        v1.readFromParcel(p, version);
                        PackageSettingV5 v2 = new PackageSettingV5();
                        v2.packageName = v1.packageName;
                        v2.appMode = v1.notCopyApk ? 1 : 0;
                        v2.appId = v1.appId;
                        v2.flag = v1.flag;
                        v2.userState = v1.userState;
                        v2.lastUpdateTime = v2.firstInstallTime = System.currentTimeMillis();
                        settingV5 = v2;
                    } else {
                        settingV5 = new PackageSettingV5(version, p);
                    }
                    list.add(settingV5);
                }
                for (PackageSettingV5 settingV5 : list) {
                    Uri uri = null;
                    if (settingV5.appMode == 1) {
                        uri = Uri.parse((String)("package:" + settingV5.packageName));
                    } else {
                        File apkFile = VEnvironment.getPackageFile(settingV5.packageName);
                        if (!apkFile.exists()) {
                            apkFile = VEnvironment.getPackageFileExt(settingV5.packageName);
                        }
                        uri = apkFile.exists() ? Uri.fromFile((File)apkFile) : Uri.parse((String)("package:" + settingV5.packageName));
                    }
                    if (uri == null) continue;
                    VAppInstallerParams params = new VAppInstallerParams(26, 1);
                    VAppInstallerResult installerResult = VirtualCore.get().installPackage(uri, params);
                    if (installerResult.status == 0) {
                        PackageSetting ps = PackageCacheManager.getSetting(settingV5.packageName);
                        ps.userState = settingV5.userState;
                        continue;
                    }
                    VLog.e("PackagePersistenceLayer", "update package info failed : install %s failed", settingV5.packageName);
                }
                this.save();
                this.changed = true;
            } else {
                this.onPersistenceFileDamage();
            }
            return;
        }
        int count = p.readInt();
        while (count-- > 0) {
            PackageSetting setting = new PackageSetting(version, p);
            if (this.mService.loadPackage(setting)) continue;
            this.changed = true;
        }
    }

    @Override
    public void onPersistenceFileDamage() {
    }
}


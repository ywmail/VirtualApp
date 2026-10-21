/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.ArrayMap
 */
package com.lody.virtual.server.pm;

import android.util.ArrayMap;
import com.lody.virtual.server.pm.PackageSetting;
import com.lody.virtual.server.pm.VPackageManagerService;
import com.lody.virtual.server.pm.parser.PackageParserEx;
import com.lody.virtual.server.pm.parser.VPackage;

public class PackageCacheManager {
    static final ArrayMap<String, VPackage> PACKAGE_CACHE = new ArrayMap();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int size() {
        ArrayMap<String, VPackage> arrayMap = PACKAGE_CACHE;
        synchronized (arrayMap) {
            return PACKAGE_CACHE.size();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean contain(String packageName) {
        ArrayMap<String, VPackage> arrayMap = PACKAGE_CACHE;
        synchronized (arrayMap) {
            return PACKAGE_CACHE.containsKey((Object)packageName);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void put(VPackage pkg, PackageSetting ps) {
        ArrayMap<String, VPackage> arrayMap = PACKAGE_CACHE;
        synchronized (arrayMap) {
            VPackage existOne = (VPackage)PACKAGE_CACHE.remove((Object)pkg.packageName);
            if (existOne != null) {
                VPackageManagerService.get().deletePackageLocked(existOne);
            }
            PackageParserEx.initApplicationInfoBase(ps, pkg);
            PACKAGE_CACHE.put((String) pkg.packageName, (VPackage) pkg);
            pkg.mExtras = ps;
            VPackageManagerService.get().analyzePackageLocked(pkg);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static VPackage get(String packageName) {
        ArrayMap<String, VPackage> arrayMap = PACKAGE_CACHE;
        synchronized (arrayMap) {
            return (VPackage)PACKAGE_CACHE.get((Object)packageName);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static PackageSetting getSetting(String packageName) {
        ArrayMap<String, VPackage> arrayMap = PACKAGE_CACHE;
        synchronized (arrayMap) {
            VPackage p = (VPackage)PACKAGE_CACHE.get((Object)packageName);
            if (p != null) {
                return (PackageSetting)p.mExtras;
            }
            return null;
        }
    }

    public static PackageSetting getSettingLocked(String packageName) {
        VPackage p = (VPackage)PACKAGE_CACHE.get((Object)packageName);
        if (p != null) {
            return (PackageSetting)p.mExtras;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static VPackage remove(String packageName) {
        ArrayMap<String, VPackage> arrayMap = PACKAGE_CACHE;
        synchronized (arrayMap) {
            VPackage pkg = (VPackage)PACKAGE_CACHE.remove((Object)packageName);
            if (pkg != null) {
                VPackageManagerService.get().deletePackageLocked(pkg);
            }
            return pkg;
        }
    }
}


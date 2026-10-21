/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 */
package com.lody.virtual.client.env;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.HostPackageManager;
import com.lody.virtual.helper.utils.ComponentUtils;
import java.util.HashMap;
import java.util.Map;

public class LocalPackageCache {
    private static final Map<String, Boolean> sSystemPackages = new HashMap<String, Boolean>();

    public static void init() {
        HostPackageManager pm = VirtualCore.get().getHostPackageManager();
        String[] pkgs = pm.getPackagesForUid(1000);
        if (pkgs != null) {
            for (String pkg : pkgs) {
                sSystemPackages.put(pkg, !VirtualCore.get().isAppInstalled(pkg));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isSystemPackage(String pkg) {
        Map<String, Boolean> map = sSystemPackages;
        synchronized (map) {
            Boolean isSystemPkg = sSystemPackages.get(pkg);
            if (isSystemPkg == null) {
                try {
                    if (VirtualCore.get().isAppInstalled(pkg)) {
                        isSystemPkg = false;
                    } else {
                        ApplicationInfo info = VirtualCore.get().getHostPackageManager().getApplicationInfo(pkg, 0L);
                        isSystemPkg = ComponentUtils.isSystemApp(info);
                    }
                }
                catch (PackageManager.NameNotFoundException e) {
                    isSystemPkg = false;
                }
                sSystemPackages.put(pkg, isSystemPkg);
            }
            return isSystemPkg;
        }
    }

    public static boolean isOutsideVisiblePackage(String pkg) {
        if (pkg == null) {
            return false;
        }
        SettingConfig config = VirtualCore.getConfig();
        return pkg.equals(config.getMainPackageName()) || pkg.equals(config.getExtPackageName()) || config.isOutsidePackage(pkg) || LocalPackageCache.isSystemPackage(pkg);
    }
}


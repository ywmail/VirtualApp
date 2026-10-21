/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 */
package com.lody.virtual.oem.protect;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import com.lody.virtual.client.ipc.VPackageManager;

public class JniMethodHook {
    private static JniMethodHook sIns = new JniMethodHook();

    public static JniMethodHook getInstance() {
        return sIns;
    }

    public PackageInfo getPackageInfo(String packageName, int flags, int userId) {
        PackageInfo packageInfo = VPackageManager.get().getPackageInfo(packageName, flags, 0);
        return packageInfo;
    }

    public PackageInfo getPackageInfo(String packageName, long flags, int userId) {
        return this.getPackageInfo(packageName, (int)flags, userId);
    }

    ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) {
        ApplicationInfo applicationInfo = VPackageManager.get().getApplicationInfo(packageName, flags, 0);
        return applicationInfo;
    }

    ApplicationInfo getApplicationInfo(String packageName, long flags, int userId) {
        return this.getApplicationInfo(packageName, (int)flags, userId);
    }

    ApplicationInfo getApplicationInfo(String packageName, int flags) {
        return this.getApplicationInfo(packageName, flags, 0);
    }

    public static boolean isIsolated() {
        return true;
    }
}


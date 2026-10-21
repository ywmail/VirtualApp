/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.net.Uri
 */
package com.lody.virtual;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.HostPackageManager;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import java.io.File;
import java.util.HashSet;

public class GmsSupport {
    private static final String TAG;
    private static final HashSet<String> GOOGLE_APP;
    private static final HashSet<String> GOOGLE_SERVICE;
    public static final String GMS_PKG;
    public static final String GSF_PKG;
    public static final String VENDING_PKG;
    public static final String GAMES_PKG;
    public static final HashSet<String> PERMISSION_FORCE_GRANT;

    public static boolean isGoogleFrameworkInstalled() {
        return VirtualCore.get().isAppInstalled(GMS_PKG);
    }

    public static boolean isGoogleService(String packageName) {
        return GOOGLE_SERVICE.contains(packageName);
    }

    public static boolean isGoogleAppOrService(String str) {
        return GOOGLE_APP.contains(str) || GOOGLE_SERVICE.contains(str);
    }

    public static boolean isOutsideGoogleFrameworkExist() {
        return VirtualCore.get().isOutsideInstalled(GMS_PKG) && VirtualCore.get().isOutsideInstalled(GSF_PKG);
    }

    private static void installPackages(File gmsDir, int userId) {
        VirtualCore core = VirtualCore.get();
        File[] files = gmsDir.listFiles();
        VLog.d("HV-", "  安装数量 files :" + files.length + "    gmsDir:" + gmsDir.getAbsolutePath(), new Object[0]);
        if (files != null) {
            for (File file : files) {
                if (!file.getName().endsWith(".apk")) continue;
                String apkPath = file.getPath();
                VLog.d(TAG, "apkPath:" + apkPath, new Object[0]);
                VAppInstallerParams params = new VAppInstallerParams(2);
                VAppInstallerResult result = core.installPackage(Uri.fromFile((File)file), params);
                if (result.status == 0) {
                    VLog.w(TAG, "install gms pkg success:" + apkPath, new Object[0]);
                    continue;
                }
                VLog.w(TAG, "install gms pkg fail:" + apkPath + ",error : " + result.status, new Object[0]);
            }
        }
    }

    public static boolean isOutsideSupportGms() {
        HostPackageManager hostPM = VirtualCore.get().getHostPackageManager();
        ApplicationInfo gmsAppInfo = null;
        ApplicationInfo gsfAppInfo = null;
        try {
            gmsAppInfo = hostPM.getApplicationInfo(GMS_PKG, 0L);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (gmsAppInfo == null) {
            return false;
        }
        try {
            gsfAppInfo = hostPM.getApplicationInfo(GSF_PKG, 0L);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return gsfAppInfo != null;
    }

    public static void installGApps(File gmsDir, int userId) {
        GmsSupport.installPackages(gmsDir, userId);
    }

    public static void installDynamicGms(int userId) {
        VirtualCore core = VirtualCore.get();
        if (userId == 0) {
            VAppInstallerParams params = new VAppInstallerParams(2);
            VAppInstallerResult result = core.installPackage(Uri.parse((String)"package:com.google.android.gsf"), params);
            VLog.w(TAG, "install gsf result:" + result.status, new Object[0]);
            result = core.installPackage(Uri.parse((String)"package:com.google.android.gms"), params);
            VLog.w(TAG, "install gms result:" + result.status, new Object[0]);
            result = core.installPackage(Uri.parse((String)"package:com.android.vending"), params);
            VLog.w(TAG, "install vending result:" + result.status, new Object[0]);
            core.installPackage(Uri.parse((String)"package:com.google.android.gm"), params);
            core.installPackage(Uri.parse((String)"package:com.google.android.youtube"), params);
        } else {
            core.installPackageAsUser(userId, GMS_PKG);
            core.installPackageAsUser(userId, GSF_PKG);
            core.installPackageAsUser(userId, VENDING_PKG);
        }
    }

    public static void remove(String packageName) {
        GOOGLE_SERVICE.remove(packageName);
        GOOGLE_APP.remove(packageName);
    }

    public static boolean isInstalledGoogleService() {
        VLog.d("HV-", "   GMS_PKG:" + VirtualCore.get().isAppInstalled(GMS_PKG), new Object[0]);
        VLog.d("HV-", "   GSF_PKG:" + VirtualCore.get().isAppInstalled(GSF_PKG), new Object[0]);
        VLog.d("HV-", "   VENDING_PKG:" + VirtualCore.get().isAppInstalled(VENDING_PKG), new Object[0]);
        VLog.d("HV-", "   GAMES_PKG:" + VirtualCore.get().isAppInstalled(GAMES_PKG), new Object[0]);
        return VirtualCore.get().isAppInstalled(GMS_PKG) && VirtualCore.get().isAppInstalled(GSF_PKG) && VirtualCore.get().isAppInstalled(VENDING_PKG) && VirtualCore.get().isAppInstalled(GAMES_PKG);
    }

    static {
        GMS_PKG = "com.google.android.gms";
        GSF_PKG = "com.google.android.gsf";
        VENDING_PKG = "com.android.vending";
        GAMES_PKG = "com.google.android.play.games";
        TAG = "HV-" + GmsSupport.class.getSimpleName();
        GOOGLE_APP = new HashSet();
        GOOGLE_SERVICE = new HashSet();
        PERMISSION_FORCE_GRANT = new HashSet();
        GOOGLE_APP.add("com.android.vending");
        GOOGLE_APP.add("com.google.android.play.games");
        GOOGLE_APP.add("com.google.android.wearable.app");
        GOOGLE_APP.add("com.google.android.wearable.app.cn");
        GOOGLE_SERVICE.add("com.google.android.gms");
        GOOGLE_SERVICE.add("com.google.android.gsf");
        GOOGLE_SERVICE.add("com.google.android.gsf.login");
        GOOGLE_SERVICE.add("com.google.android.backuptransport");
        GOOGLE_SERVICE.add("com.google.android.backup");
        GOOGLE_SERVICE.add("com.google.android.configupdater");
        GOOGLE_SERVICE.add("com.google.android.syncadapters.contacts");
        GOOGLE_SERVICE.add("com.google.android.feedback");
        GOOGLE_SERVICE.add("com.google.android.onetimeinitializer");
        GOOGLE_SERVICE.add("com.google.android.partnersetup");
        GOOGLE_SERVICE.add("com.google.android.setupwizard");
        GOOGLE_SERVICE.add("com.google.android.syncadapters.calendar");
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Build
 */
package com.lody.virtual.client.core;

import android.content.Intent;
import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.SpecialComponentList;

public abstract class SettingConfig {
    public abstract String getMainPackageName();

    public abstract String getExtPackageName();

    public String getBinderProviderAuthority() {
        return "com.carlos.multiapp.virtual.service.BinderProvider";
    }

    public String getExtPackageHelperAuthority() {
        return "com.carlos.multiapp.virtual.service.ext_helper";
    }

    public boolean isEnableIORedirect() {
        return true;
    }

    public boolean isUseRealApkPath(String packageName) {
        return false;
    }

    public boolean isAllowCreateShortcut() {
        return true;
    }

    public boolean isUseRealDataDir(String packageName) {
        return false;
    }

    public boolean isUseRealLibDir(String packageName) {
        return false;
    }

    public boolean isEnableVirtualSdcardAndroidData() {
        return false;
    }

    public String getVirtualSdcardAndroidDataName() {
        return "Android_va";
    }

    public boolean isDisableTinker(String packageName) {
        return false;
    }

    public abstract boolean isOutsidePackage(String var1);

    public boolean isOutsideAction(String action) {
        return "android.media.action.IMAGE_CAPTURE".equals(action) || "android.media.action.VIDEO_CAPTURE".equals(action) || "android.intent.action.PICK".equals(action);
    }

    public boolean isUseRealPath(String packageName) {
        return SettingConfig.isUseNativeEngine2(packageName);
    }

    public boolean isUnProtectAction(String action) {
        return SpecialComponentList.SYSTEM_BROADCAST_ACTION.contains(action);
    }

    public Intent onHandleLauncherIntent(Intent originIntent) {
        return null;
    }

    public Intent onHandleCameraIntent(Intent originIntent) {
        return null;
    }

    public boolean isHideForegroundNotification() {
        return false;
    }

    public FakeWifiStatus getFakeWifiStatus() {
        return null;
    }

    public FakeWifiStatus getFakeWifiStatus(String packageName, int userId) {
        return null;
    }

    public boolean isHostIntent(Intent intent) {
        return false;
    }

    public boolean isDisableDrawOverlays(String packageName) {
        return false;
    }

    public boolean disableSetScreenOrientation(String packageName) {
        return false;
    }

    public boolean resumeInstrumentationInMakeApplication(String packageName) {
        return Build.BRAND.equals("HUAWEI") && Build.BOARD.equals("GLK");
    }

    public static boolean isUseNativeEngine2(String pkg) {
        return "com.example.vatest2".equals(pkg) || "cn.com.rektec.xmobile.jcy".equals(pkg);
    }

    public static class FakeWifiStatus {
        public static String DEFAULT_BSSID = "66:55:44:33:22:11";
        public static String DEFAULT_MAC = "11:22:33:44:55:66";
        public static String DEFAULT_SSID = "VA_SSID";

        public String getSSID() {
            return DEFAULT_SSID;
        }

        public String getBSSID() {
            return DEFAULT_BSSID;
        }

        public String getMAC() {
            return DEFAULT_MAC;
        }

        public void setSSID(String ssid) {
            DEFAULT_SSID = ssid;
        }

        public void setBSSID(String bssid) {
            DEFAULT_BSSID = bssid;
        }

        public void setDefaultMac(String mac) {
            DEFAULT_MAC = mac;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 */
package com.carlos.common.device;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.utils.IniFile;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.InstalledInfoCache;

public class ChannelConfig {
    public static String TAG = "ChannelConfig";
    DeviceInfo deviceInfo;
    private static ChannelConfig channelConfig;
    IniFile mIniFile;

    private ChannelConfig() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ChannelConfig getInstance() {
        Class<ChannelConfig> clazz = ChannelConfig.class;
        synchronized (ChannelConfig.class) {
            if (channelConfig == null) {
                channelConfig = new ChannelConfig();
            }
            // ** MonitorExit[var0] (shouldn't be in output)
            return channelConfig;
        }
    }

    public IniFile getIniFile() {
        if (this.mIniFile == null) {
            this.mIniFile = IniFile.getInstance();
        }
        return this.mIniFile;
    }

    public boolean needActivation(Context context) {
        return false;
    }

    public void getChannelConfig() {
    }

    public void getHidePkg() {
    }

    public void getDevicesAction(Context context) {
    }

    public ApplicationInfo getApplicationInfo(String pkgName, int userId) {
        return VPackageManager.get().getApplicationInfo(pkgName, 0, userId);
    }

    public String getApplicationName(Context context, String pkgName, int userId) {
        ApplicationInfo appInfo = this.getApplicationInfo(pkgName, userId);
        if (appInfo == null) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        try {
            InstalledInfoCache.CacheItem appInfoCache = InstalledInfoCache.get(appInfo.packageName);
            String name = appInfoCache == null ? appInfo.loadLabel(pm).toString() : appInfoCache.getLabel();
            return name;
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public void dayActivation(Context context, String metaDataFromApp) {
    }
}


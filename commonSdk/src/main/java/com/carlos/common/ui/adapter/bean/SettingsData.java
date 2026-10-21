/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.graphics.drawable.Drawable
 */
package com.carlos.common.ui.adapter.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import com.lody.virtual.remote.InstalledAppInfo;

public class SettingsData {
    public String packageName;
    public int userId;
    public String name;
    public Drawable icon;

    public SettingsData() {
    }

    public SettingsData(Context context, InstalledAppInfo installedAppInfo, int userId) {
        this.packageName = installedAppInfo == null ? null : installedAppInfo.packageName;
        this.userId = userId;
        if (installedAppInfo != null) {
            this.loadData(context, installedAppInfo.getApplicationInfo(installedAppInfo.getInstalledUsers()[0]));
        }
    }

    private void loadData(Context context, ApplicationInfo appInfo) {
        if (appInfo == null) {
            return;
        }
        PackageManager pm = context.getPackageManager();
        try {
            CharSequence sequence = appInfo.loadLabel(pm);
            this.name = sequence.toString();
            this.icon = appInfo.loadIcon(pm);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


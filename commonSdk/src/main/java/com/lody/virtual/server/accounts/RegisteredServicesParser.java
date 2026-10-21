/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.ServiceInfo
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.content.res.XmlResourceParser
 *  android.os.Bundle
 */
package com.lody.virtual.server.accounts;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import com.lody.virtual.server.pm.PackageCacheManager;
import com.lody.virtual.server.pm.PackageSetting;
import mirror.android.content.res.AssetManager;

public class RegisteredServicesParser {
    public XmlResourceParser getParser(Context context, ServiceInfo serviceInfo, String name) {
        int xmlId;
        Bundle meta = serviceInfo.metaData;
        if (meta != null && (xmlId = meta.getInt(name)) != 0) {
            try {
                return this.getResources(context, serviceInfo.applicationInfo).getXml(xmlId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Resources getResources(Context context, ApplicationInfo appInfo) {
        PackageSetting ps = PackageCacheManager.getSetting(appInfo.packageName);
        if (ps != null) {
            android.content.res.AssetManager assets = AssetManager.ctor.newInstance();
            AssetManager.addAssetPath.call(assets, ps.getPackagePath());
            Resources hostRes = context.getResources();
            return new Resources(assets, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
        }
        return null;
    }
}


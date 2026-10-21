/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Notification
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.text.TextUtils
 *  android.widget.RemoteViews
 */
package com.lody.virtual.server.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.compat.NotificationChannelCompat;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.notification.NotificationCompatCompatV14;
import mirror.android.app.NotificationO;

@TargetApi(value=21)
class NotificationCompatCompatV21
extends NotificationCompatCompatV14 {
    private static final String TAG = NotificationCompatCompatV21.class.getSimpleName();

    NotificationCompatCompatV21() {
    }

    @Override
    public boolean dealNotification(int id2, Notification notification, String packageName) {
        Context appContext = this.getAppContext(packageName);
        if (Build.VERSION.SDK_INT >= 26 && VirtualCore.get().getTargetSdkVersion() >= 26 && TextUtils.isEmpty((CharSequence)notification.getChannelId())) {
            NotificationO.mChannelId.set(notification, NotificationChannelCompat.DEFAULT_ID);
        }
        try {
            return this.resolveRemoteViews(appContext, id2, packageName, notification) || this.resolveRemoteViews(appContext, id2, packageName, notification.publicVersion);
        }
        catch (Throwable throwable) {
            VLog.e(TAG, "error deal Notification!");
            return false;
        }
    }

    private PackageInfo getOutSidePackageInfo(String packageName) {
        try {
            return VirtualCore.get().getHostPackageManager().getPackageInfo(packageName, 1024L);
        }
        catch (Throwable e) {
            return null;
        }
    }

    private boolean resolveRemoteViews(Context appContext, int id2, String packageName, Notification notification) {
        if (notification == null) {
            return false;
        }
        ApplicationInfo host = this.getHostContext().getApplicationInfo();
        PackageInfo outside = this.getOutSidePackageInfo(packageName);
        PackageInfo inside = VPackageManager.get().getPackageInfo(packageName, 1024, 0);
        boolean isInstalled = outside != null && outside.versionCode == inside.versionCode;
        this.getNotificationFixer().fixNotificationRemoteViews(appContext, notification);
        if (Build.VERSION.SDK_INT >= 23) {
            this.getNotificationFixer().fixIcon(notification.getSmallIcon(), appContext, isInstalled);
            this.getNotificationFixer().fixIcon(notification.getLargeIcon(), appContext, isInstalled);
        } else {
            this.getNotificationFixer().fixIconImage(appContext.getResources(), notification.contentView, false, notification);
        }
        notification.icon = host.icon;
        ApplicationInfo proxyApplicationInfo = isInstalled ? outside.applicationInfo : inside.applicationInfo;
        proxyApplicationInfo.targetSdkVersion = 22;
        this.fixApplicationInfo(notification.tickerView, proxyApplicationInfo);
        this.fixApplicationInfo(notification.contentView, proxyApplicationInfo);
        this.fixApplicationInfo(notification.bigContentView, proxyApplicationInfo);
        this.fixApplicationInfo(notification.headsUpContentView, proxyApplicationInfo);
        Bundle bundle = (Bundle)Reflect.on(notification).get("extras");
        if (bundle != null) {
            bundle.putParcelable("android.appInfo", (Parcelable)proxyApplicationInfo);
        }
        if (Build.VERSION.SDK_INT >= 26 && !isInstalled) {
            this.remakeRemoteViews(id2, notification, appContext);
        }
        return true;
    }

    private ApplicationInfo getApplicationInfo(Notification notification) {
        ApplicationInfo ai = this.getApplicationInfo(notification.tickerView);
        if (ai != null) {
            return ai;
        }
        ai = this.getApplicationInfo(notification.contentView);
        if (ai != null) {
            return ai;
        }
        if (Build.VERSION.SDK_INT >= 16 && (ai = this.getApplicationInfo(notification.bigContentView)) != null) {
            return ai;
        }
        if (Build.VERSION.SDK_INT >= 21 && (ai = this.getApplicationInfo(notification.headsUpContentView)) != null) {
            return ai;
        }
        return null;
    }

    private ApplicationInfo getApplicationInfo(RemoteViews remoteViews) {
        if (remoteViews != null) {
            return mirror.android.widget.RemoteViews.mApplication.get(remoteViews);
        }
        return null;
    }

    private void fixApplicationInfo(RemoteViews remoteViews, ApplicationInfo ai) {
        if (remoteViews != null) {
            mirror.android.widget.RemoteViews.mApplication.set(remoteViews, ai);
        }
    }
}


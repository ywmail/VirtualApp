/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ResolveInfo
 *  android.os.Build$VERSION
 *  android.os.RemoteException
 *  androidx.annotation.RequiresApi
 */
package com.lody.virtual.client.env;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.RemoteException;
import androidx.annotation.RequiresApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.os.VUserHandle;
import mirror.android.app.ActivityThread;

public abstract class HostPackageManager {
    private static HostPackageManager sInstance;

    public static HostPackageManager init() {
        sInstance = new HostPackageManagerImpl();
        return sInstance;
    }

    public static HostPackageManager get() {
        return sInstance;
    }

    public abstract PackageInfo getPackageInfo(String var1, long var2) throws PackageManager.NameNotFoundException;

    public abstract ApplicationInfo getApplicationInfo(String var1, long var2) throws PackageManager.NameNotFoundException;

    public abstract ResolveInfo resolveActivity(Intent var1, long var2);

    public abstract ProviderInfo resolveContentProvider(String var1, long var2);

    public abstract String[] getPackagesForUid(int var1);

    public abstract int checkPermission(String var1, String var2);

    private static class HostPackageManagerImpl
    extends HostPackageManager {
        private IPackageManager mService = ActivityThread.getPackageManager.call(new Object[0]);
        private static Boolean sIsAndroid13Beta = null;

        public static boolean isAndroid13AndUp() {
            if (sIsAndroid13Beta == null) {
                if (Build.VERSION.SDK_INT >= 31) {
                    sIsAndroid13Beta = Build.VERSION.SDK_INT >= 33;
                    if (!sIsAndroid13Beta.booleanValue()) {
                        try {
                            IPackageManager.class.getMethod("getPackageInfo", String.class, Long.TYPE, Integer.TYPE);
                            sIsAndroid13Beta = true;
                        }
                        catch (NoSuchMethodException e) {
                            sIsAndroid13Beta = false;
                        }
                    }
                } else {
                    sIsAndroid13Beta = false;
                }
            }
            return sIsAndroid13Beta;
        }

        @Override
        @RequiresApi(api=33)
        public PackageInfo getPackageInfo(String packageName, long flags) throws PackageManager.NameNotFoundException {
            PackageInfo info;
            try {
                info = BuildCompat.isTiramisu() ? this.mService.getPackageInfo(packageName, flags, VUserHandle.realUserId()) : this.mService.getPackageInfo(packageName, (int)flags, VUserHandle.realUserId());
            }
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (info == null) {
                throw new PackageManager.NameNotFoundException(packageName);
            }
            return info;
        }

        @Override
        public ApplicationInfo getApplicationInfo(String packageName, long flags) throws PackageManager.NameNotFoundException {
            ApplicationInfo info;
            try {
                info = BuildCompat.isTiramisu() ? this.mService.getApplicationInfo(packageName, flags, VUserHandle.realUserId()) : this.mService.getApplicationInfo(packageName, (int)flags, VUserHandle.realUserId());
            }
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (info == null) {
                throw new PackageManager.NameNotFoundException(packageName);
            }
            return info;
        }

        @Override
        public ResolveInfo resolveActivity(Intent intent, long flags) {
            try {
                if (BuildCompat.isTiramisu()) {
                    return this.mService.resolveIntent(intent, null, flags, VUserHandle.realUserId());
                }
                return this.mService.resolveIntent(intent, (String)null, (int)flags, VUserHandle.realUserId());
            }
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public ProviderInfo resolveContentProvider(String authority, long flags) {
            try {
                if (BuildCompat.isTiramisu()) {
                    return this.mService.resolveContentProvider(authority, flags, VUserHandle.realUserId());
                }
                return this.mService.resolveContentProvider(authority, (int)flags, VUserHandle.realUserId());
            }
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String[] getPackagesForUid(int uid) {
            try {
                return this.mService.getPackagesForUid(uid);
            }
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int checkPermission(String permName, String pkgName) {
            try {
                return this.mService.checkPermission(permName, pkgName, VUserHandle.realUserId());
            }
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.os.RemoteException
 *  androidx.annotation.RequiresApi
 */
package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;
import androidx.annotation.RequiresApi;

public interface IPackageManager {
    public String[] getPackagesForUid(int var1) throws RemoteException;

    public int getPackageUid(String var1, int var2) throws RemoteException;

    public int[] getPackageGids(String var1) throws RemoteException;

    public PackageInfo getPackageInfo(String var1, int var2, int var3) throws RemoteException;

    @RequiresApi(value=33)
    public PackageInfo getPackageInfo(String var1, long var2, int var4) throws RemoteException;

    public ApplicationInfo getApplicationInfo(String var1, int var2, int var3) throws RemoteException;

    @RequiresApi(value=33)
    public ApplicationInfo getApplicationInfo(String var1, long var2, int var4) throws RemoteException;

    public ActivityInfo getActivityInfo(ComponentName var1, int var2, int var3) throws RemoteException;

    public ActivityInfo getReceiverInfo(ComponentName var1, int var2, int var3) throws RemoteException;

    public ServiceInfo getServiceInfo(ComponentName var1, int var2, int var3) throws RemoteException;

    public ServiceInfo getServiceInfo(ComponentName var1, long var2, int var4) throws RemoteException;

    public ProviderInfo getProviderInfo(ComponentName var1, int var2, int var3) throws RemoteException;

    public ResolveInfo resolveIntent(Intent var1, String var2, int var3, int var4) throws RemoteException;

    public ResolveInfo resolveIntent(Intent var1, String var2, long var3, int var5) throws RemoteException;

    public ProviderInfo resolveContentProvider(String var1, int var2, int var3) throws RemoteException;

    public ProviderInfo resolveContentProvider(String var1, long var2, int var4) throws RemoteException;

    public int checkPermission(String var1, String var2, int var3) throws RemoteException;
}


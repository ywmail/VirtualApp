/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PermissionGroupInfo
 *  android.content.pm.PermissionInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.remote.ReceiverInfo;
import com.lody.virtual.server.IPackageInstaller;
import com.lody.virtual.server.interfaces.IPackageManager;
import java.util.List;

public class VPackageManager {
    private static final VPackageManager sMgr = new VPackageManager();
    private IPackageManager mService;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public IPackageManager getService() {
        if (IInterfaceUtils.isAlive(this.mService)) return this.mService;
        Class<VPackageManager> clazz = VPackageManager.class;
        synchronized (VPackageManager.class) {
            Object remote = this.getRemoteInterface();
            this.mService = LocalProxyUtils.genProxy(IPackageManager.class, remote);
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return this.mService;
        }
    }

    private Object getRemoteInterface() {
        return IPackageManager.Stub.asInterface(ServiceManagerNative.getService("package"));
    }

    public static VPackageManager get() {
        return sMgr;
    }

    public int checkPermission(String permission2, String pkgName, int userId) {
        try {
            return this.getService().checkPermission(VirtualCore.get().isExtPackage(), permission2, pkgName, userId);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) {
        try {
            return this.getService().resolveService(intent, resolvedType, flags, userId);
        }
        catch (RemoteException e) {
            return (ResolveInfo)VirtualRuntime.crash(e);
        }
    }

    public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) {
        try {
            return this.getService().getPermissionGroupInfo(name, flags);
        }
        catch (RemoteException e) {
            return (PermissionGroupInfo)VirtualRuntime.crash(e);
        }
    }

    public List<ApplicationInfo> getInstalledApplications(int flags, int userId) {
        try {
            return this.getService().getInstalledApplications(flags, userId).getList();
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public PackageInfo getPackageInfo(String packageName, int flags, int userId) {
        try {
            return this.getService().getPackageInfo(packageName, flags, userId);
        }
        catch (RemoteException e) {
            return (PackageInfo)VirtualRuntime.crash(e);
        }
    }

    public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) {
        try {
            return this.getService().resolveIntent(intent, resolvedType, flags, userId);
        }
        catch (RemoteException e) {
            return (ResolveInfo)VirtualRuntime.crash(e);
        }
    }

    public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) {
        try {
            return this.getService().queryIntentContentProviders(intent, resolvedType, flags, userId);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public ActivityInfo getReceiverInfo(ComponentName componentName, int flags, int userId) {
        try {
            return this.getService().getReceiverInfo(componentName, flags, userId);
        }
        catch (RemoteException e) {
            return (ActivityInfo)VirtualRuntime.crash(e);
        }
    }

    public List<PackageInfo> getInstalledPackages(int flags, int userId) {
        try {
            return this.getService().getInstalledPackages(flags, userId).getList();
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) {
        try {
            return this.getService().queryPermissionsByGroup(group, flags);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public PermissionInfo getPermissionInfo(String name, int flags) {
        try {
            return this.getService().getPermissionInfo(name, flags);
        }
        catch (RemoteException e) {
            return (PermissionInfo)VirtualRuntime.crash(e);
        }
    }

    public ActivityInfo getActivityInfo(ComponentName componentName, int flags, int userId) {
        try {
            return this.getService().getActivityInfo(componentName, flags, userId);
        }
        catch (RemoteException e) {
            return (ActivityInfo)VirtualRuntime.crash(e);
        }
    }

    public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) {
        try {
            return this.getService().queryIntentReceivers(intent, resolvedType, flags, userId);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
        try {
            return this.getService().getAllPermissionGroups(flags);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) {
        try {
            return this.getService().queryIntentActivities(intent, resolvedType, flags, userId);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags, int userId) {
        try {
            return this.getService().queryIntentServices(intent, resolvedType, flags, userId);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) {
        try {
            return this.getService().getApplicationInfo(packageName, flags, userId);
        }
        catch (RemoteException e) {
            return (ApplicationInfo)VirtualRuntime.crash(e);
        }
    }

    public ProviderInfo resolveContentProvider(String name, int flags, int userId) {
        try {
            return this.getService().resolveContentProvider(name, flags, userId);
        }
        catch (RemoteException e) {
            return (ProviderInfo)VirtualRuntime.crash(e);
        }
    }

    public ServiceInfo getServiceInfo(ComponentName componentName, int flags, int userId) {
        try {
            return this.getService().getServiceInfo(componentName, flags, userId);
        }
        catch (RemoteException e) {
            return (ServiceInfo)VirtualRuntime.crash(e);
        }
    }

    public ProviderInfo getProviderInfo(ComponentName componentName, int flags, int userId) {
        try {
            return this.getService().getProviderInfo(componentName, flags, userId);
        }
        catch (RemoteException e) {
            return (ProviderInfo)VirtualRuntime.crash(e);
        }
    }

    public boolean activitySupportsIntent(ComponentName component, Intent intent, String resolvedType) {
        try {
            return this.getService().activitySupportsIntent(component, intent, resolvedType);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags) {
        try {
            return this.getService().queryContentProviders(processName, uid, flags).getList();
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public List<String> querySharedPackages(String packageName) {
        try {
            return this.getService().querySharedPackages(packageName);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public String[] getPackagesForUid(int uid) {
        try {
            return this.getService().getPackagesForUid(uid);
        }
        catch (RemoteException e) {
            return (String[])VirtualRuntime.crash(e);
        }
    }

    public int getPackageUid(String packageName, int userId) {
        try {
            return this.getService().getPackageUid(packageName, userId);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public String getNameForUid(int uid) {
        try {
            return this.getService().getNameForUid(uid);
        }
        catch (RemoteException e) {
            return (String)VirtualRuntime.crash(e);
        }
    }

    public IPackageInstaller getPackageInstaller() {
        try {
            return IPackageInstaller.Stub.asInterface(this.getService().getPackageInstaller());
        }
        catch (RemoteException e) {
            return (IPackageInstaller)VirtualRuntime.crash(e);
        }
    }

    public int checkSignatures(String pkg1, String pkg2) {
        try {
            return this.getService().checkSignatures(pkg1, pkg2);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public String[] getDangerousPermissions(String packageName) {
        try {
            return this.getService().getDangerousPermissions(packageName);
        }
        catch (RemoteException e) {
            return (String[])VirtualRuntime.crash(e);
        }
    }

    public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) {
        try {
            this.getService().setComponentEnabledSetting(componentName, newState, flags, userId);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public int getComponentEnabledSetting(ComponentName component, int userId) {
        try {
            return this.getService().getComponentEnabledSetting(component, userId);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public List<ReceiverInfo> getReceiverInfos(String packageName, String processName, int userId) {
        try {
            return this.getService().getReceiverInfos(packageName, processName, userId);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }
}


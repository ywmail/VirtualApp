/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.ProviderCall;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.ServiceCache;
import com.lody.virtual.server.interfaces.IServiceFetcher;

public class ServiceManagerNative {
    public static final String PACKAGE = "package";
    public static final String ACTIVITY = "activity";
    public static final String USER = "user";
    public static final String APP = "app";
    public static final String ACCOUNT = "account";
    public static final String CONTENT = "content";
    public static final String JOB = "job";
    public static final String NOTIFICATION = "notification";
    public static final String VS = "vs";
    public static final String DEVICE = "device";
    public static final String VIRTUAL_LOC = "virtual-loc";
    public static final String FILE_TRANSFER = "file-transfer";
    public static final String PERMISSION = "permission";
    private static final String TAG = ServiceManagerNative.class.getSimpleName();
    private static IServiceFetcher sFetcher;

    private static String getAuthority() {
        return VirtualCore.getConfig().getBinderProviderAuthority();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static IServiceFetcher getServiceFetcher() {
        if (sFetcher != null && sFetcher.asBinder().isBinderAlive()) return sFetcher;
        Class<ServiceManagerNative> clazz = ServiceManagerNative.class;
        synchronized (ServiceManagerNative.class) {
            Context context = VirtualCore.get().getContext();
            Bundle response = new ProviderCall.Builder(context, ServiceManagerNative.getAuthority()).methodName("@").callSafely();
            if (response == null) return sFetcher;
            IBinder binder = BundleCompat.getBinder(response, "_VA_|_binder_");
            ServiceManagerNative.linkBinderDied(binder);
            sFetcher = IServiceFetcher.Stub.asInterface(binder);
            // ** MonitorExit[var0] (shouldn't be in output)
            return sFetcher;
        }
    }

    public static void ensureServerStarted() {
        new ProviderCall.Builder(VirtualCore.get().getContext(), ServiceManagerNative.getAuthority()).methodName("ensure_created").callSafely();
    }

    public static void clearServerFetcher() {
        sFetcher = null;
    }

    private static void linkBinderDied(final IBinder binder) {
        IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient(){

            public void binderDied() {
                binder.unlinkToDeath((IBinder.DeathRecipient)this, 0);
            }
        };
        try {
            binder.linkToDeath(deathRecipient, 0);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static IBinder getService(String name) {
        if (VirtualCore.get().isServerProcess()) {
            return ServiceCache.getService(name);
        }
        IServiceFetcher fetcher = ServiceManagerNative.getServiceFetcher();
        if (fetcher != null) {
            try {
                return fetcher.getService(name);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        VLog.e(TAG, "GetService(%s) return null.", name);
        return null;
    }

    public static void addService(String name, IBinder service) {
        IServiceFetcher fetcher = ServiceManagerNative.getServiceFetcher();
        if (fetcher != null) {
            try {
                fetcher.addService(name, service);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeService(String name) {
        IServiceFetcher fetcher = ServiceManagerNative.getServiceFetcher();
        if (fetcher != null) {
            try {
                fetcher.removeService(name);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void linkToDeath(IBinder.DeathRecipient deathRecipient) {
        try {
            ServiceManagerNative.getServiceFetcher().asBinder().linkToDeath(deathRecipient, 0);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}


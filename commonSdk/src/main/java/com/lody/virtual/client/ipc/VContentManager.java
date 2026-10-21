/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.PeriodicSync
 *  android.content.SyncAdapterType
 *  android.content.SyncRequest
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.accounts.Account;
import android.content.ISyncStatusObserver;
import android.content.PeriodicSync;
import android.content.SyncAdapterType;
import android.content.SyncInfo;
import android.content.SyncRequest;
import android.content.SyncStatusInfo;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.server.interfaces.IContentService;
import java.util.List;

public class VContentManager {
    private static final VContentManager sInstance = new VContentManager();
    private IContentService mService;

    public static VContentManager get() {
        return sInstance;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public IContentService getService() {
        if (!IInterfaceUtils.isAlive(this.mService)) {
            VContentManager vContentManager = this;
            synchronized (vContentManager) {
                Object binder = this.getRemoteInterface();
                this.mService = LocalProxyUtils.genProxy(IContentService.class, binder);
            }
        }
        return this.mService;
    }

    private Object getRemoteInterface() {
        return IContentService.Stub.asInterface(ServiceManagerNative.getService("content"));
    }

    public void unregisterContentObserver(IContentObserver observer) throws RemoteException {
        this.getService().unregisterContentObserver(observer);
    }

    public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int userHandle) throws RemoteException {
        this.getService().registerContentObserver(uri, notifyForDescendants, observer, userHandle);
    }

    public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork, int userHandle) throws RemoteException {
        this.getService().notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, userHandle);
    }

    public void requestSync(Account account, String authority, Bundle extras) throws RemoteException {
        this.getService().requestSync(account, authority, extras);
    }

    public void sync(SyncRequest request) throws RemoteException {
        this.getService().sync(request);
    }

    public void cancelSync(Account account, String authority) throws RemoteException {
        this.getService().cancelSync(account, authority);
    }

    public boolean getSyncAutomatically(Account account, String providerName) throws RemoteException {
        return this.getService().getSyncAutomatically(account, providerName);
    }

    public void setSyncAutomatically(Account account, String providerName, boolean sync) throws RemoteException {
        this.getService().setSyncAutomatically(account, providerName, sync);
    }

    public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName) throws RemoteException {
        return this.getService().getPeriodicSyncs(account, providerName);
    }

    public void addPeriodicSync(Account account, String providerName, Bundle extras, long pollFrequency) throws RemoteException {
        this.getService().addPeriodicSync(account, providerName, extras, pollFrequency);
    }

    public void removePeriodicSync(Account account, String providerName, Bundle extras) throws RemoteException {
        this.getService().removePeriodicSync(account, providerName, extras);
    }

    public int getIsSyncable(Account account, String providerName) throws RemoteException {
        return this.getService().getIsSyncable(account, providerName);
    }

    public void setIsSyncable(Account account, String providerName, int syncable) throws RemoteException {
        this.getService().setIsSyncable(account, providerName, syncable);
    }

    public void setMasterSyncAutomatically(boolean flag) throws RemoteException {
        this.getService().setMasterSyncAutomatically(flag);
    }

    public boolean getMasterSyncAutomatically() throws RemoteException {
        return this.getService().getMasterSyncAutomatically();
    }

    public boolean isSyncActive(Account account, String authority) throws RemoteException {
        return this.getService().isSyncActive(account, authority);
    }

    public List<SyncInfo> getCurrentSyncs() throws RemoteException {
        return this.getService().getCurrentSyncs();
    }

    public SyncAdapterType[] getSyncAdapterTypes() throws RemoteException {
        return this.getService().getSyncAdapterTypes();
    }

    public SyncStatusInfo getSyncStatus(Account account, String authority) throws RemoteException {
        return this.getService().getSyncStatus(account, authority);
    }

    public boolean isSyncPending(Account account, String authority) throws RemoteException {
        return this.getService().isSyncPending(account, authority);
    }

    public void addStatusChangeListener(int mask, ISyncStatusObserver callback) throws RemoteException {
        this.getService().addStatusChangeListener(mask, callback);
    }

    public void removeStatusChangeListener(ISyncStatusObserver callback) throws RemoteException {
        this.getService().removeStatusChangeListener(callback);
    }
}


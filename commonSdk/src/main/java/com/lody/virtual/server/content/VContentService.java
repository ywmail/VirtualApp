/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.PeriodicSync
 *  android.content.SyncAdapterType
 *  android.content.SyncRequest
 *  android.database.sqlite.SQLiteException
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.Parcel
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.lody.virtual.server.content;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ISyncStatusObserver;
import android.content.SyncAdapterType;
import android.content.SyncInfo;
import android.content.SyncStatusInfo;
import android.database.IContentObserver;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VBinder;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.content.SyncManager;
import com.lody.virtual.server.content.SyncStorageEngine;
import com.lody.virtual.server.content.VSyncInfo;
import com.lody.virtual.server.interfaces.IContentService;
import java.util.ArrayList;
import java.util.List;
import mirror.android.content.PeriodicSync;
import mirror.android.content.SyncRequest;

public final class VContentService
extends IContentService.Stub {
    private static final String TAG = "ContentService";
    private static final VContentService sInstance = new VContentService();
    private Context mContext;
    private final ObserverNode mRootNode = new ObserverNode("");
    private SyncManager mSyncManager = null;
    private final Object mSyncManagerLock = new Object();

    public static VContentService get() {
        return sInstance;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private SyncManager getSyncManager() {
        Object object = this.mSyncManagerLock;
        synchronized (object) {
            try {
                if (this.mSyncManager == null) {
                    this.mSyncManager = new SyncManager(this.mContext);
                }
            }
            catch (SQLiteException e) {
                Log.e((String)TAG, (String)"Can't create SyncManager", (Throwable)e);
            }
            return this.mSyncManager;
        }
    }

    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        }
        catch (RuntimeException e) {
            if (!(e instanceof SecurityException)) {
                e.printStackTrace();
            }
            throw e;
        }
    }

    private VContentService() {
        this.mContext = VirtualCore.get().getContext();
    }

    public static void systemReady() {
        VContentService.get().getSyncManager();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int VUserHandle2) {
        if (observer == null || uri == null) {
            throw new IllegalArgumentException("You must pass a valid uri and observer");
        }
        ObserverNode observerNode = this.mRootNode;
        synchronized (observerNode) {
            this.mRootNode.addObserverLocked(uri, observer, notifyForDescendants, this.mRootNode, VBinder.getCallingUid(), VBinder.getCallingPid(), VUserHandle2);
        }
    }

    public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer) {
        this.registerContentObserver(uri, notifyForDescendants, observer, VUserHandle.getCallingUserId());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void unregisterContentObserver(IContentObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("You must pass a valid observer");
        }
        ObserverNode observerNode = this.mRootNode;
        synchronized (observerNode) {
            this.mRootNode.removeObserverLocked(observer);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork, int VUserHandle2) {
        if (Log.isLoggable((String)TAG, (int)2)) {
            Log.v((String)TAG, (String)("Notifying update of " + uri + " for user " + VUserHandle2 + " from observer " + observer + ", syncToNetwork " + syncToNetwork));
        }
        int uid = VBinder.getCallingUid();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager;
            ArrayList<ObserverCall> calls = new ArrayList<ObserverCall>();
            ObserverNode observerNode = this.mRootNode;
            synchronized (observerNode) {
                this.mRootNode.collectObserversLocked(uri, 0, observer, observerWantsSelfNotifications, VUserHandle2, calls);
            }
            int numCalls = calls.size();
            for (int i = 0; i < numCalls; ++i) {
                ObserverCall oc = calls.get(i);
                try {
                    oc.mObserver.onChange(oc.mSelfChange, uri, VUserHandle2);
                    if (!Log.isLoggable((String)TAG, (int)2)) continue;
                    Log.v((String)TAG, (String)("Notified " + oc.mObserver + " of update at " + uri));
                    continue;
                }
                catch (RemoteException ex) {
                    ObserverNode observerNode2 = this.mRootNode;
                    synchronized (observerNode2) {
                        Log.w((String)TAG, (String)"Found dead observer, removing");
                        IBinder binder = oc.mObserver.asBinder();
                        ArrayList list = oc.mNode.mObservers;
                        int numList = list.size();
                        for (int j = 0; j < numList; ++j) {
                            ObserverNode.ObserverEntry oe = (ObserverNode.ObserverEntry)list.get(j);
                            if (oe.observer.asBinder() != binder) continue;
                            list.remove(j);
                            --j;
                            --numList;
                        }
                        continue;
                    }
                }
            }
            if (syncToNetwork && (syncManager = this.getSyncManager()) != null) {
                syncManager.scheduleLocalSync(null, VUserHandle2, uid, uri.getAuthority());
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork) {
        this.notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, VUserHandle.getCallingUserId());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void requestSync(Account account, String authority, Bundle extras) {
        ContentResolver.validateSyncExtrasBundle((Bundle)extras);
        int userId = VUserHandle.getCallingUserId();
        int uId = VBinder.getCallingUid();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                syncManager.scheduleSync(account, userId, uId, authority, extras, 0L, 0L, false);
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void sync(android.content.SyncRequest request) {
        Bundle extras = SyncRequest.mExtras.get(request);
        long flextime = SyncRequest.mSyncFlexTimeSecs.get(request);
        long runAtTime = SyncRequest.mSyncRunTimeSecs.get(request);
        int userId = VUserHandle.getCallingUserId();
        int uId = VBinder.getCallingUid();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                Account account = SyncRequest.mAccountToSync.get(request);
                String provider = SyncRequest.mAuthority.get(request);
                if (SyncRequest.mIsPeriodic.get(request)) {
                    if (runAtTime < 60L) {
                        VLog.w(TAG, "Requested poll frequency of " + runAtTime + " seconds being rounded up to 60 seconds.", new Object[0]);
                        runAtTime = 60L;
                    }
                    android.content.PeriodicSync syncToAdd = new android.content.PeriodicSync(account, provider, extras, runAtTime);
                    PeriodicSync.flexTime.set(syncToAdd, flextime);
                    this.getSyncManager().getSyncStorageEngine().addPeriodicSync(syncToAdd, userId);
                } else {
                    long beforeRuntimeMillis = flextime * 1000L;
                    long runtimeMillis = runAtTime * 1000L;
                    syncManager.scheduleSync(account, userId, uId, provider, extras, beforeRuntimeMillis, runtimeMillis, false);
                }
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cancelSync(Account account, String authority) {
        if (authority != null && authority.length() == 0) {
            throw new IllegalArgumentException("Authority must be non-empty");
        }
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                syncManager.clearScheduledSyncOperations(account, userId, authority);
                syncManager.cancelActiveSync(account, userId, authority);
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public SyncAdapterType[] getSyncAdapterTypes() {
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            SyncAdapterType[] syncAdapterTypeArray = syncManager.getSyncAdapterTypes();
            return syncAdapterTypeArray;
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean getSyncAutomatically(Account account, String providerName) {
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                boolean bl = syncManager.getSyncStorageEngine().getSyncAutomatically(account, userId, providerName);
                return bl;
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setSyncAutomatically(Account account, String providerName, boolean sync) {
        if (TextUtils.isEmpty((CharSequence)providerName)) {
            throw new IllegalArgumentException("Authority must be non-empty");
        }
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                syncManager.getSyncStorageEngine().setSyncAutomatically(account, userId, providerName, sync);
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addPeriodicSync(Account account, String authority, Bundle extras, long pollFrequency) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }
        if (TextUtils.isEmpty((CharSequence)authority)) {
            throw new IllegalArgumentException("Authority must not be empty.");
        }
        int userId = VUserHandle.getCallingUserId();
        if (pollFrequency < 60L) {
            VLog.w(TAG, "Requested poll frequency of " + pollFrequency + " seconds being rounded up to 60 seconds.", new Object[0]);
            pollFrequency = 60L;
        }
        long identityToken = VContentService.clearCallingIdentity();
        try {
            android.content.PeriodicSync syncToAdd = new android.content.PeriodicSync(account, authority, extras, pollFrequency);
            PeriodicSync.flexTime.set(syncToAdd, SyncStorageEngine.calculateDefaultFlexTime(pollFrequency));
            this.getSyncManager().getSyncStorageEngine().addPeriodicSync(syncToAdd, userId);
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void removePeriodicSync(Account account, String authority, Bundle extras) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }
        if (TextUtils.isEmpty((CharSequence)authority)) {
            throw new IllegalArgumentException("Authority must not be empty");
        }
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            android.content.PeriodicSync syncToRemove = new android.content.PeriodicSync(account, authority, extras, 0L);
            this.getSyncManager().getSyncStorageEngine().removePeriodicSync(syncToRemove, userId);
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<android.content.PeriodicSync> getPeriodicSyncs(Account account, String providerName) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }
        if (TextUtils.isEmpty((CharSequence)providerName)) {
            throw new IllegalArgumentException("Authority must not be empty");
        }
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            List<android.content.PeriodicSync> list = this.getSyncManager().getSyncStorageEngine().getPeriodicSyncs(account, userId, providerName);
            return list;
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getIsSyncable(Account account, String providerName) {
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                int n = syncManager.getIsSyncable(account, userId, providerName);
                return n;
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
        return -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setIsSyncable(Account account, String providerName, int syncable) {
        if (TextUtils.isEmpty((CharSequence)providerName)) {
            throw new IllegalArgumentException("Authority must not be empty");
        }
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                syncManager.getSyncStorageEngine().setIsSyncable(account, userId, providerName, syncable);
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean getMasterSyncAutomatically() {
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                boolean bl = syncManager.getSyncStorageEngine().getMasterSyncAutomatically(userId);
                return bl;
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setMasterSyncAutomatically(boolean flag) {
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                syncManager.getSyncStorageEngine().setMasterSyncAutomatically(flag, userId);
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isSyncActive(Account account, String authority) {
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                boolean bl = syncManager.getSyncStorageEngine().isSyncActive(account, userId, authority);
                return bl;
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<SyncInfo> getCurrentSyncs() {
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            List<VSyncInfo> vList = this.getSyncManager().getSyncStorageEngine().getCurrentSyncsCopy(userId);
            ArrayList<SyncInfo> list = new ArrayList<SyncInfo>(vList.size());
            for (VSyncInfo info : vList) {
                list.add(info.toSyncInfo());
            }
            ArrayList<SyncInfo> arrayList = list;
            return arrayList;
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public SyncStatusInfo getSyncStatus(Account account, String authority) {
        if (TextUtils.isEmpty((CharSequence)authority)) {
            throw new IllegalArgumentException("Authority must not be empty");
        }
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                SyncStatusInfo syncStatusInfo = syncManager.getSyncStorageEngine().getStatusByAccountAndAuthority(account, userId, authority);
                return syncStatusInfo;
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isSyncPending(Account account, String authority) {
        int userId = VUserHandle.getCallingUserId();
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
                boolean bl = syncManager.getSyncStorageEngine().isSyncPending(account, userId, authority);
                return bl;
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addStatusChangeListener(int mask, ISyncStatusObserver callback) {
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null && callback != null) {
                syncManager.getSyncStorageEngine().addStatusChangeListener(mask, callback);
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void removeStatusChangeListener(ISyncStatusObserver callback) {
        long identityToken = VContentService.clearCallingIdentity();
        try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null && callback != null) {
                syncManager.getSyncStorageEngine().removeStatusChangeListener(callback);
            }
        }
        finally {
            VContentService.restoreCallingIdentity((long)identityToken);
        }
    }

    public static final class ObserverNode {
        public static final int INSERT_TYPE = 0;
        public static final int UPDATE_TYPE = 1;
        public static final int DELETE_TYPE = 2;
        private String mName;
        private ArrayList<ObserverNode> mChildren = new ArrayList();
        private ArrayList<ObserverEntry> mObservers = new ArrayList();

        public ObserverNode(String name) {
            this.mName = name;
        }

        private String getUriSegment(Uri uri, int index) {
            if (uri != null) {
                if (index == 0) {
                    return uri.getAuthority();
                }
                return (String)uri.getPathSegments().get(index - 1);
            }
            return null;
        }

        private int countUriSegments(Uri uri) {
            if (uri == null) {
                return 0;
            }
            return uri.getPathSegments().size() + 1;
        }

        public void addObserverLocked(Uri uri, IContentObserver observer, boolean notifyForDescendants, Object observersLock, int uid, int pid, int VUserHandle2) {
            this.addObserverLocked(uri, 0, observer, notifyForDescendants, observersLock, uid, pid, VUserHandle2);
        }

        private void addObserverLocked(Uri uri, int index, IContentObserver observer, boolean notifyForDescendants, Object observersLock, int uid, int pid, int VUserHandle2) {
            if (index == this.countUriSegments(uri)) {
                this.mObservers.add(new ObserverEntry(observer, notifyForDescendants, observersLock, uid, pid, VUserHandle2));
                return;
            }
            String segment = this.getUriSegment(uri, index);
            if (segment == null) {
                throw new IllegalArgumentException("Invalid Uri (" + uri + ") used for observer");
            }
            int N = this.mChildren.size();
            for (int i = 0; i < N; ++i) {
                ObserverNode node = this.mChildren.get(i);
                if (!node.mName.equals(segment)) continue;
                node.addObserverLocked(uri, index + 1, observer, notifyForDescendants, observersLock, uid, pid, VUserHandle2);
                return;
            }
            ObserverNode node = new ObserverNode(segment);
            this.mChildren.add(node);
            node.addObserverLocked(uri, index + 1, observer, notifyForDescendants, observersLock, uid, pid, VUserHandle2);
        }

        public boolean removeObserverLocked(IContentObserver observer) {
            int size = this.mChildren.size();
            for (int i = 0; i < size; ++i) {
                boolean empty = this.mChildren.get(i).removeObserverLocked(observer);
                if (!empty) continue;
                this.mChildren.remove(i);
                --i;
                --size;
            }
            IBinder observerBinder = observer.asBinder();
            size = this.mObservers.size();
            for (int i = 0; i < size; ++i) {
                ObserverEntry entry = this.mObservers.get(i);
                if (entry.observer.asBinder() != observerBinder) continue;
                this.mObservers.remove(i);
                observerBinder.unlinkToDeath((IBinder.DeathRecipient)entry, 0);
                break;
            }
            return this.mChildren.size() == 0 && this.mObservers.size() == 0;
        }

        private void collectMyObserversLocked(boolean leaf, IContentObserver observer, boolean observerWantsSelfNotifications, int targetUserHandle, ArrayList<ObserverCall> calls) {
            int N = this.mObservers.size();
            IBinder observerBinder = observer == null ? null : observer.asBinder();
            for (int i = 0; i < N; ++i) {
                boolean selfChange;
                ObserverEntry entry = this.mObservers.get(i);
                boolean bl = selfChange = entry.observer.asBinder() == observerBinder;
                if (selfChange && !observerWantsSelfNotifications || targetUserHandle != -1 && entry.userHandle != -1 && targetUserHandle != entry.userHandle || !leaf && !entry.notifyForDescendants) continue;
                calls.add(new ObserverCall(this, entry.observer, selfChange));
            }
        }

        public void collectObserversLocked(Uri uri, int index, IContentObserver observer, boolean observerWantsSelfNotifications, int targetUserHandle, ArrayList<ObserverCall> calls) {
            String segment = null;
            int segmentCount = this.countUriSegments(uri);
            if (index >= segmentCount) {
                this.collectMyObserversLocked(true, observer, observerWantsSelfNotifications, targetUserHandle, calls);
            } else {
                segment = this.getUriSegment(uri, index);
                this.collectMyObserversLocked(false, observer, observerWantsSelfNotifications, targetUserHandle, calls);
            }
            int N = this.mChildren.size();
            for (int i = 0; i < N; ++i) {
                ObserverNode node = this.mChildren.get(i);
                if (segment != null && !node.mName.equals(segment)) continue;
                node.collectObserversLocked(uri, index + 1, observer, observerWantsSelfNotifications, targetUserHandle, calls);
                if (segment != null) break;
            }
        }

        private class ObserverEntry
        implements IBinder.DeathRecipient {
            public final IContentObserver observer;
            public final int uid;
            public final int pid;
            public final boolean notifyForDescendants;
            private final int userHandle;
            private final Object observersLock;

            public ObserverEntry(IContentObserver o, boolean n, Object observersLock, int _uid, int _pid, int _userHandle) {
                this.observersLock = observersLock;
                this.observer = o;
                this.uid = _uid;
                this.pid = _pid;
                this.userHandle = _userHandle;
                this.notifyForDescendants = n;
                try {
                    this.observer.asBinder().linkToDeath((IBinder.DeathRecipient)this, 0);
                }
                catch (RemoteException e) {
                    this.binderDied();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public void binderDied() {
                Object object = this.observersLock;
                synchronized (object) {
                    ObserverNode.this.removeObserverLocked(this.observer);
                }
            }
        }
    }

    public static final class ObserverCall {
        final ObserverNode mNode;
        final IContentObserver mObserver;
        final boolean mSelfChange;

        ObserverCall(ObserverNode node, IContentObserver observer, boolean selfChange) {
            this.mNode = node;
            this.mObserver = observer;
            this.mSelfChange = selfChange;
        }
    }
}


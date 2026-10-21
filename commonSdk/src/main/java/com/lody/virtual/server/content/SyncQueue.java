/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.util.Log
 *  android.util.Pair
 */
package com.lody.virtual.server.content;

import android.accounts.Account;
import android.util.Log;
import android.util.Pair;
import com.lody.virtual.StringFog;
import com.lody.virtual.server.content.SyncAdaptersCache;
import com.lody.virtual.server.content.SyncOperation;
import com.lody.virtual.server.content.SyncStorageEngine;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SyncQueue {
    private static final String TAG = "SyncManager";
    private final SyncStorageEngine mSyncStorageEngine;
    private final SyncAdaptersCache mSyncAdapters;
    private final HashMap<String, SyncOperation> mOperationsMap = new HashMap();

    public SyncQueue(SyncStorageEngine syncStorageEngine, SyncAdaptersCache syncAdapters) {
        this.mSyncStorageEngine = syncStorageEngine;
        this.mSyncAdapters = syncAdapters;
    }

    public void addPendingOperations(int userId) {
        for (SyncStorageEngine.PendingOperation op : this.mSyncStorageEngine.getPendingOperations()) {
            if (op.userId != userId) continue;
            Pair<Long, Long> backoff = this.mSyncStorageEngine.getBackoff(op.account, op.userId, op.authority);
            SyncAdaptersCache.SyncAdapterInfo syncAdapterInfo = this.mSyncAdapters.getServiceInfo(op.account, op.authority);
            if (syncAdapterInfo == null) {
                Log.w((String)TAG, (String)("Missing sync adapter info for authority " + op.authority + ", userId " + op.userId));
                continue;
            }
            SyncOperation syncOperation = new SyncOperation(op.account, op.userId, op.reason, op.syncSource, op.authority, op.extras, 0L, 0L, backoff != null ? (Long)backoff.first : 0L, this.mSyncStorageEngine.getDelayUntilTime(op.account, op.userId, op.authority), syncAdapterInfo.type.allowParallelSyncs());
            syncOperation.expedited = op.expedited;
            syncOperation.pendingOperation = op;
            this.add(syncOperation, op);
        }
    }

    public boolean add(SyncOperation operation) {
        return this.add(operation, null);
    }

    private boolean add(SyncOperation operation, SyncStorageEngine.PendingOperation pop) {
        String operationKey = operation.key;
        SyncOperation existingOperation = this.mOperationsMap.get(operationKey);
        if (existingOperation != null) {
            boolean changed = false;
            if (operation.compareTo(existingOperation) <= 0) {
                long newRunTime;
                existingOperation.expedited = operation.expedited;
                existingOperation.latestRunTime = newRunTime = Math.min(existingOperation.latestRunTime, operation.latestRunTime);
                existingOperation.flexTime = operation.flexTime;
                changed = true;
            }
            return changed;
        }
        operation.pendingOperation = pop;
        if (operation.pendingOperation == null) {
            pop = new SyncStorageEngine.PendingOperation(operation.account, operation.userId, operation.reason, operation.syncSource, operation.authority, operation.extras, operation.expedited);
            if ((pop = this.mSyncStorageEngine.insertIntoPending(pop)) == null) {
                throw new IllegalStateException("error adding pending sync operation " + operation);
            }
            operation.pendingOperation = pop;
        }
        this.mOperationsMap.put(operationKey, operation);
        return true;
    }

    public void removeUser(int userId) {
        ArrayList<SyncOperation> opsToRemove = new ArrayList<SyncOperation>();
        for (SyncOperation op : this.mOperationsMap.values()) {
            if (op.userId != userId) continue;
            opsToRemove.add(op);
        }
        for (SyncOperation op : opsToRemove) {
            this.remove(op);
        }
    }

    public void remove(SyncOperation operation) {
        SyncOperation operationToRemove = this.mOperationsMap.remove(operation.key);
        if (operationToRemove == null) {
            return;
        }
        if (!this.mSyncStorageEngine.deleteFromPending(operationToRemove.pendingOperation)) {
            String errorMessage = "unable to find pending row for " + operationToRemove;
            Log.e((String)TAG, (String)errorMessage, (Throwable)new IllegalStateException(errorMessage));
        }
    }

    public void onBackoffChanged(Account account, int userId, String providerName, long backoff) {
        for (SyncOperation op : this.mOperationsMap.values()) {
            if (!op.account.equals((Object)account) || !op.authority.equals(providerName) || op.userId != userId) continue;
            op.backoff = backoff;
            op.updateEffectiveRunTime();
        }
    }

    public void onDelayUntilTimeChanged(Account account, String providerName, long delayUntil) {
        for (SyncOperation op : this.mOperationsMap.values()) {
            if (!op.account.equals((Object)account) || !op.authority.equals(providerName)) continue;
            op.delayUntil = delayUntil;
            op.updateEffectiveRunTime();
        }
    }

    public void remove(Account account, int userId, String authority) {
        Iterator<Map.Entry<String, SyncOperation>> entries = this.mOperationsMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, SyncOperation> entry = entries.next();
            SyncOperation syncOperation = entry.getValue();
            if (account != null && !syncOperation.account.equals((Object)account) || authority != null && !syncOperation.authority.equals(authority) || userId != syncOperation.userId) continue;
            entries.remove();
            if (this.mSyncStorageEngine.deleteFromPending(syncOperation.pendingOperation)) continue;
            String errorMessage = "unable to find pending row for " + syncOperation;
            Log.e((String)TAG, (String)errorMessage, (Throwable)new IllegalStateException(errorMessage));
        }
    }

    public Collection<SyncOperation> getOperations() {
        return this.mOperationsMap.values();
    }
}


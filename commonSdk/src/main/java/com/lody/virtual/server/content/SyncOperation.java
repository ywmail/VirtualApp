/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.ComponentName
 *  android.content.pm.PackageManager
 *  android.os.Bundle
 *  android.os.SystemClock
 */
package com.lody.virtual.server.content;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import com.lody.virtual.StringFog;
import com.lody.virtual.server.content.SyncStorageEngine;

public class SyncOperation
implements Comparable {
    public static final int REASON_BACKGROUND_DATA_SETTINGS_CHANGED = -1;
    public static final int REASON_ACCOUNTS_UPDATED = -2;
    public static final int REASON_SERVICE_CHANGED = -3;
    public static final int REASON_PERIODIC = -4;
    public static final int REASON_IS_SYNCABLE = -5;
    public static final int REASON_SYNC_AUTO = -6;
    public static final int REASON_MASTER_SYNC_AUTO = -7;
    public static final int REASON_USER_START = -8;
    private static String[] REASON_NAMES = new String[]{"DataSettingsChanged", "AccountsUpdated", "ServiceChanged", "Periodic", "IsSyncable", "AutoSync", "MasterSyncAuto", "UserStart"};
    public final Account account;
    public final String authority;
    public final ComponentName service;
    public final int userId;
    public final int reason;
    public int syncSource;
    public final boolean allowParallelSyncs;
    public Bundle extras;
    public final String key;
    public boolean expedited;
    public SyncStorageEngine.PendingOperation pendingOperation;
    public long latestRunTime;
    public Long backoff;
    public long delayUntil;
    public long effectiveRunTime;
    public long flexTime;

    public SyncOperation(Account account, int userId, int reason, int source, String authority, Bundle extras, long runTimeFromNow, long flexTime, long backoff, long delayUntil, boolean allowParallelSyncs) {
        this.service = null;
        this.account = account;
        this.authority = authority;
        this.userId = userId;
        this.reason = reason;
        this.syncSource = source;
        this.allowParallelSyncs = allowParallelSyncs;
        this.extras = new Bundle(extras);
        this.cleanBundle(this.extras);
        this.delayUntil = delayUntil;
        this.backoff = backoff;
        long now = SystemClock.elapsedRealtime();
        if (runTimeFromNow < 0L || this.isExpedited()) {
            this.expedited = true;
            this.latestRunTime = now;
            this.flexTime = 0L;
        } else {
            this.expedited = false;
            this.latestRunTime = now + runTimeFromNow;
            this.flexTime = flexTime;
        }
        this.updateEffectiveRunTime();
        this.key = this.toKey();
    }

    private void cleanBundle(Bundle bundle) {
        this.removeFalseExtra(bundle, "upload");
        this.removeFalseExtra(bundle, "force");
        this.removeFalseExtra(bundle, "ignore_settings");
        this.removeFalseExtra(bundle, "ignore_backoff");
        this.removeFalseExtra(bundle, "do_not_retry");
        this.removeFalseExtra(bundle, "discard_deletions");
        this.removeFalseExtra(bundle, "expedited");
        this.removeFalseExtra(bundle, "deletions_override");
        this.removeFalseExtra(bundle, "allow_metered");
        bundle.remove("expected_upload");
        bundle.remove("expected_download");
    }

    private void removeFalseExtra(Bundle bundle, String extraName) {
        if (!bundle.getBoolean(extraName, false)) {
            bundle.remove(extraName);
        }
    }

    SyncOperation(SyncOperation other) {
        this.service = other.service;
        this.account = other.account;
        this.authority = other.authority;
        this.userId = other.userId;
        this.reason = other.reason;
        this.syncSource = other.syncSource;
        this.extras = new Bundle(other.extras);
        this.expedited = other.expedited;
        this.latestRunTime = SystemClock.elapsedRealtime();
        this.flexTime = 0L;
        this.backoff = other.backoff;
        this.allowParallelSyncs = other.allowParallelSyncs;
        this.updateEffectiveRunTime();
        this.key = this.toKey();
    }

    public String toString() {
        return this.dump(null, true);
    }

    public String dump(PackageManager pm, boolean useOneLine) {
        StringBuilder sb = new StringBuilder().append(this.account.name).append(" u").append(this.userId).append(" (").append(this.account.type).append(")").append(", ").append(this.authority).append(", ").append(SyncStorageEngine.SOURCES[this.syncSource]).append(", latestRunTime ").append(this.latestRunTime);
        if (this.expedited) {
            sb.append(", EXPEDITED");
        }
        sb.append(", reason: ");
        sb.append(SyncOperation.reasonToString(pm, this.reason));
        if (!useOneLine && !this.extras.keySet().isEmpty()) {
            sb.append("\n    ");
            SyncOperation.extrasToStringBuilder(this.extras, sb);
        }
        return sb.toString();
    }

    public static String reasonToString(PackageManager pm, int reason) {
        if (reason >= 0) {
            if (pm != null) {
                String[] packages = pm.getPackagesForUid(reason);
                if (packages != null && packages.length == 1) {
                    return packages[0];
                }
                String name = pm.getNameForUid(reason);
                if (name != null) {
                    return name;
                }
                return String.valueOf(reason);
            }
            return String.valueOf(reason);
        }
        int index = -reason - 1;
        if (index >= REASON_NAMES.length) {
            return String.valueOf(reason);
        }
        return REASON_NAMES[index];
    }

    public boolean isMeteredDisallowed() {
        return this.extras.getBoolean("allow_metered", false);
    }

    public boolean isInitialization() {
        return this.extras.getBoolean("initialize", false);
    }

    public boolean isExpedited() {
        return this.extras.getBoolean("expedited", false) || this.expedited;
    }

    public boolean ignoreBackoff() {
        return this.extras.getBoolean("ignore_backoff", false);
    }

    private String toKey() {
        StringBuilder sb = new StringBuilder();
        if (this.service == null) {
            sb.append("authority: ").append(this.authority);
            sb.append(" account {name=" + this.account.name + ", user=" + this.userId + ", type=" + this.account.type + "}");
        } else {
            sb.append("service {package=").append(this.service.getPackageName()).append(" user=").append(this.userId).append(", class=").append(this.service.getClassName()).append("}");
        }
        sb.append(" extras: ");
        SyncOperation.extrasToStringBuilder(this.extras, sb);
        return sb.toString();
    }

    public static void extrasToStringBuilder(Bundle bundle, StringBuilder sb) {
        sb.append("[");
        for (String key : bundle.keySet()) {
            sb.append(key).append("=").append(bundle.get(key)).append(" ");
        }
        sb.append("]");
    }

    public void updateEffectiveRunTime() {
        this.effectiveRunTime = this.ignoreBackoff() ? this.latestRunTime : Math.max(Math.max(this.latestRunTime, this.delayUntil), this.backoff);
    }

    public int compareTo(Object o) {
        long otherIntervalStart;
        SyncOperation other = (SyncOperation)o;
        if (this.expedited != other.expedited) {
            return this.expedited ? -1 : 1;
        }
        long thisIntervalStart = Math.max(this.effectiveRunTime - this.flexTime, 0L);
        if (thisIntervalStart < (otherIntervalStart = Math.max(other.effectiveRunTime - other.flexTime, 0L))) {
            return -1;
        }
        if (otherIntervalStart < thisIntervalStart) {
            return 1;
        }
        return 0;
    }
}


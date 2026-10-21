/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 */
package com.lody.virtual.server.am;

import android.content.ComponentName;
import android.content.Intent;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.server.am.ActivityRecord;
import java.util.ArrayList;
import java.util.List;

class TaskRecord {
    public final List<ActivityRecord> activities = new ArrayList<ActivityRecord>();
    public int taskId;
    public int userId;
    public String affinity;
    public Intent taskRoot;

    TaskRecord(int taskId, int userId, String affinity, Intent intent) {
        this.taskId = taskId;
        this.userId = userId;
        this.affinity = affinity;
        this.taskRoot = intent;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    ActivityRecord getRootActivityRecord() {
        List<ActivityRecord> list = this.activities;
        synchronized (list) {
            for (int i = 0; i < this.activities.size(); ++i) {
                ActivityRecord r = this.activities.get(i);
                if (r.started && r.marked) continue;
                return r;
            }
        }
        return null;
    }

    public ActivityRecord getTopActivityRecord() {
        return this.getTopActivityRecord(false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ActivityRecord getTopActivityRecord(boolean containFinishedActivity) {
        List<ActivityRecord> list = this.activities;
        synchronized (list) {
            if (this.activities.isEmpty()) {
                return null;
            }
            for (int i = this.activities.size() - 1; i >= 0; --i) {
                ActivityRecord r = this.activities.get(i);
                if (!r.started || !containFinishedActivity && r.marked) continue;
                return r;
            }
            return null;
        }
    }

    AppTaskInfo getAppTaskInfo() {
        int len = this.activities.size();
        if (len <= 0) {
            return null;
        }
        ComponentName top = this.activities.get((int)(len - 1)).component;
        return new AppTaskInfo(this.taskId, this.taskRoot, this.taskRoot.getComponent(), top);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isFinishing() {
        List<ActivityRecord> list = this.activities;
        synchronized (list) {
            for (ActivityRecord r : this.activities) {
                if (!r.started || r.marked) continue;
                return false;
            }
            return true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void finish() {
        List<ActivityRecord> list = this.activities;
        synchronized (list) {
            for (ActivityRecord r : this.activities) {
                r.marked = true;
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 */
package com.lody.virtual.server.am;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import com.lody.virtual.server.am.ClearTaskAction;
import com.lody.virtual.server.am.PendingNewIntent;
import com.lody.virtual.server.am.ProcessRecord;
import com.lody.virtual.server.am.TaskRecord;

class ActivityRecord
extends Binder {
    public TaskRecord task;
    public ActivityInfo info;
    public ComponentName component;
    public Intent intent;
    public IBinder token;
    public IBinder resultTo;
    String resultWho;
    int requestCode;
    Bundle options;
    public int userId;
    public ProcessRecord process;
    public boolean marked;
    public boolean started;
    public ClearTaskAction pendingClearAction = ClearTaskAction.NONE;
    public PendingNewIntent pendingNewIntent;

    public ActivityRecord(int userId, Intent intent, ActivityInfo info, IBinder resultTo) {
        this.userId = userId;
        this.intent = intent;
        this.info = info;
        this.component = info.targetActivity != null ? new ComponentName(info.packageName, info.targetActivity) : new ComponentName(info.packageName, info.name);
        this.resultTo = resultTo;
    }

    public void init(TaskRecord task, ProcessRecord process, IBinder token) {
        this.userId = task.userId;
        this.task = task;
        this.process = process;
        this.token = token;
        this.started = true;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RecentTaskInfo
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ComponentInfo
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package com.lody.virtual.server.am;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.BridgeActivity;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.helper.compat.ObjectsCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.ClassUtils;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.ShadowActivityInfo;
import com.lody.virtual.server.am.ActivityRecord;
import com.lody.virtual.server.am.AttributeCache;
import com.lody.virtual.server.am.ClearTaskAction;
import com.lody.virtual.server.am.PendingNewIntent;
import com.lody.virtual.server.am.ProcessRecord;
import com.lody.virtual.server.am.TaskRecord;
import com.lody.virtual.server.am.VActivityManagerService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.IActivityManager;
import mirror.com.android.internal.R_Hide;

class ActivityStack {
    private static final String TAG = "ActivityStack";
    private static final boolean sTrace = true;
    private final VActivityManagerService mService;
    private final Set<ActivityRecord> mPendingLaunchActivities = Collections.synchronizedSet(new HashSet());
    private final ActivityManager mAM;
    private final SparseArray<TaskRecord> mHistory = new SparseArray();

    ActivityStack(VActivityManagerService mService) {
        this.mService = mService;
        this.mAM = (ActivityManager)VirtualCore.get().getContext().getSystemService("activity");
    }

    private static void removeFlags(Intent intent, int flags) {
        intent.setFlags(intent.getFlags() & ~flags);
    }

    private static boolean containFlags(Intent intent, int flags) {
        return (intent.getFlags() & flags) != 0;
    }

    private static int removeFlags(int flags, int mask) {
        return flags & ~mask;
    }

    private static boolean containFlags(int flags, int mask) {
        return (flags & mask) != 0;
    }

    private void deliverNewIntentLocked(ActivityRecord sourceRecord, ActivityRecord launchRecord, Intent intent) {
        String creator;
        if (launchRecord == null) {
            return;
        }
        String string2 = creator = sourceRecord != null ? sourceRecord.component.getPackageName() : "android";
        if (launchRecord.started && launchRecord.process != null && launchRecord.process.client != null) {
            try {
                launchRecord.process.client.scheduleNewIntent(creator, launchRecord.token, intent);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            launchRecord.pendingNewIntent = new PendingNewIntent(creator, intent);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private TaskRecord findTaskByComponentLocked(int userId, ComponentName comp) {
        for (int i = 0; i < this.mHistory.size(); ++i) {
            TaskRecord r = this.mHistory.valueAt(i);
            if (userId != r.userId) continue;
            List<ActivityRecord> list = r.activities;
            synchronized (list) {
                for (ActivityRecord a : r.activities) {
                    if (a.marked || !a.component.equals((Object)comp)) continue;
                    return r;
                }
                continue;
            }
        }
        return null;
    }

    private TaskRecord findTaskByAffinityLocked(int userId, String affinity) {
        for (int i = 0; i < this.mHistory.size(); ++i) {
            TaskRecord r = this.mHistory.valueAt(i);
            if (userId != r.userId || !affinity.equals(r.affinity) || r.isFinishing()) continue;
            return r;
        }
        return null;
    }

    private TaskRecord findTaskByIntentLocked(int userId, Intent intent) {
        for (int i = 0; i < this.mHistory.size(); ++i) {
            TaskRecord r = this.mHistory.valueAt(i);
            if (userId != r.userId || r.taskRoot == null || !ObjectsCompat.equals(intent.getComponent(), r.taskRoot.getComponent())) continue;
            return r;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ActivityRecord findActivityByToken(int userId, IBinder token) {
        ActivityRecord target = null;
        if (token != null) {
            for (int i = 0; i < this.mHistory.size(); ++i) {
                TaskRecord task = this.mHistory.valueAt(i);
                if (task.userId != userId) continue;
                List<ActivityRecord> list = task.activities;
                synchronized (list) {
                    for (ActivityRecord r : task.activities) {
                        if (r.token != token) continue;
                        target = r;
                    }
                    continue;
                }
            }
        }
        return target;
    }

    private void optimizeTasksLocked() {
        List<ActivityManager.RecentTaskInfo> recentTask = VirtualCore.get().getRecentTasksEx(Integer.MAX_VALUE, 3);
        int N = this.mHistory.size();
        while (N-- > 0) {
            TaskRecord task = this.mHistory.valueAt(N);
            ListIterator<ActivityManager.RecentTaskInfo> iterator = recentTask.listIterator();
            boolean taskAlive = false;
            while (iterator.hasNext()) {
                ActivityManager.RecentTaskInfo info = iterator.next();
                if (info.id != task.taskId) continue;
                taskAlive = true;
                iterator.remove();
                break;
            }
            if (taskAlive) continue;
            this.mHistory.removeAt(N);
        }
    }

    int startActivitiesLocked(int userId, Intent[] intents, ActivityInfo[] infos, IBinder resultTo, Bundle options) {
        for (int i = 0; i < intents.length; ++i) {
            this.startActivityLocked(userId, intents[i], infos[i], resultTo, options, null, 0);
        }
        return 0;
    }

    private static String launchModeToString(int launchMode) {
        switch (launchMode) {
            case 0: {
                return "standard";
            }
            case 1: {
                return "singleTop";
            }
            case 2: {
                return "singleTask";
            }
            case 3: {
                return "singleInstance";
            }
        }
        return "unknown";
    }

    private static String documentLaunchModeToString(int launchMode) {
        switch (launchMode) {
            case 2: {
                return "always";
            }
            case 1: {
                return "intoExisting";
            }
            case 3: {
                return "never";
            }
            case 0: {
                return "none";
            }
        }
        return "unknown";
    }

    private static String componentInfoToString(ComponentInfo info) {
        return info.packageName + "/" + info.name;
    }

    private static String activityInfoFlagsToString(int flags) {
        StringBuilder sb = new StringBuilder();
        if (ActivityStack.containFlags(flags, 1)) {
            sb.append("FLAG_MULTIPROCESS | ");
            flags = ActivityStack.removeFlags(flags, 1);
        }
        if (ActivityStack.containFlags(flags, 0x100000)) {
            sb.append("FLAG_VISIBLE_TO_INSTANT_APP | ");
            flags = ActivityStack.removeFlags(flags, 0x100000);
        }
        if (ActivityStack.containFlags(flags, 2)) {
            sb.append("FLAG_FINISH_ON_TASK_LAUNCH | ");
            flags = ActivityStack.removeFlags(flags, 2);
        }
        if (ActivityStack.containFlags(flags, 4)) {
            sb.append("FLAG_CLEAR_TASK_ON_LAUNCH | ");
            flags = ActivityStack.removeFlags(flags, 4);
        }
        if (ActivityStack.containFlags(flags, 8)) {
            sb.append("FLAG_ALWAYS_RETAIN_TASK_STATE | ");
            flags = ActivityStack.removeFlags(flags, 8);
        }
        if (ActivityStack.containFlags(flags, 16)) {
            sb.append("FLAG_STATE_NOT_NEEDED | ");
            flags = ActivityStack.removeFlags(flags, 16);
        }
        if (ActivityStack.containFlags(flags, 64)) {
            sb.append("FLAG_ALLOW_TASK_REPARENTING | ");
            flags = ActivityStack.removeFlags(flags, 64);
        }
        if (ActivityStack.containFlags(flags, 128)) {
            sb.append("FLAG_NO_HISTORY | ");
            flags = ActivityStack.removeFlags(flags, 128);
        }
        if (ActivityStack.containFlags(flags, 256)) {
            sb.append("FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS | ");
            flags = ActivityStack.removeFlags(flags, 256);
        }
        if (ActivityStack.containFlags(flags, 512)) {
            sb.append("FLAG_HARDWARE_ACCELERATED | ");
            flags = ActivityStack.removeFlags(flags, 512);
        }
        if (ActivityStack.containFlags(flags, 0x40000000)) {
            sb.append("FLAG_SINGLE_USER | ");
            flags = ActivityStack.removeFlags(flags, 0x40000000);
        }
        if (ActivityStack.containFlags(flags, 32)) {
            sb.append("FLAG_EXCLUDE_FROM_RECENTS | ");
            flags = ActivityStack.removeFlags(flags, 32);
        }
        if (flags != 0) {
            sb.append("0x").append(Integer.toHexString(flags));
        } else if (sb.length() > 2) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    private static String activityInfoToString(ActivityInfo info) {
        StringBuilder sb = new StringBuilder();
        sb.append("launchMode: ");
        sb.append(ActivityStack.launchModeToString(info.launchMode));
        if (Build.VERSION.SDK_INT >= 21) {
            sb.append("\ndocumentLaunchMode: ");
            sb.append(ActivityStack.documentLaunchModeToString(info.documentLaunchMode));
        }
        sb.append("\naffinity: ").append(info.taskAffinity);
        sb.append("\nflags: ").append(ActivityStack.activityInfoFlagsToString(info.flags));
        return sb.toString();
    }

    private static String parseIntentFlagsToString(Intent intent) {
        int flags = intent.getFlags();
        if (flags == 0) {
            return "0x0";
        }
        StringBuilder sb = new StringBuilder();
        if (ActivityStack.containFlags(flags, 0x10000000)) {
            sb.append("FLAG_ACTIVITY_NEW_TASK | ");
            flags = ActivityStack.removeFlags(flags, 0x10000000);
        }
        if (ActivityStack.containFlags(flags, 32768)) {
            sb.append("FLAG_ACTIVITY_CLEAR_TASK | ");
            flags = ActivityStack.removeFlags(flags, 32768);
        }
        if (ActivityStack.containFlags(flags, 0x8000000)) {
            sb.append("FLAG_ACTIVITY_MULTIPLE_TASK | ");
            flags = ActivityStack.removeFlags(flags, 0x8000000);
        }
        if (ActivityStack.containFlags(flags, 131072)) {
            sb.append("FLAG_ACTIVITY_REORDER_TO_FRONT | ");
            flags = ActivityStack.removeFlags(flags, 131072);
        }
        if (ActivityStack.containFlags(flags, 131072)) {
            sb.append("FLAG_ACTIVITY_REORDER_TO_FRONT | ");
            flags = ActivityStack.removeFlags(flags, 131072);
        }
        if (ActivityStack.containFlags(flags, 0x20000000)) {
            sb.append("FLAG_ACTIVITY_SINGLE_TOP | ");
            flags = ActivityStack.removeFlags(flags, 0x20000000);
        }
        if (ActivityStack.containFlags(flags, 0x8000000)) {
            sb.append("FLAG_ACTIVITY_MULTIPLE_TASK | ");
            flags = ActivityStack.removeFlags(flags, 0x8000000);
        }
        if (ActivityStack.containFlags(flags, 0x2000000)) {
            sb.append("FLAG_ACTIVITY_FORWARD_RESULT | ");
            flags = ActivityStack.removeFlags(flags, 0x2000000);
        }
        if (ActivityStack.containFlags(flags, 16384)) {
            sb.append("FLAG_ACTIVITY_TASK_ON_HOME | ");
            flags = ActivityStack.removeFlags(flags, 16384);
        }
        if (ActivityStack.containFlags(flags, 0x4000000)) {
            sb.append("FLAG_ACTIVITY_CLEAR_TOP | ");
            flags = ActivityStack.removeFlags(flags, 0x4000000);
        }
        if (ActivityStack.containFlags(flags, 262144)) {
            sb.append("FLAG_ACTIVITY_NO_USER_ACTION | ");
            flags = ActivityStack.removeFlags(flags, 262144);
        }
        if (ActivityStack.containFlags(flags, 8192)) {
            sb.append("FLAG_ACTIVITY_RETAIN_IN_RECENTS | ");
            flags = ActivityStack.removeFlags(flags, 8192);
        }
        if (flags != 0) {
            sb.append("0x").append(Integer.toHexString(flags));
        } else if (sb.length() >= 2) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ActivityRecord findActivityByComponentName(TaskRecord task, ComponentName comp) {
        List<ActivityRecord> list = task.activities;
        synchronized (list) {
            for (int i = task.activities.size() - 1; i >= 0; --i) {
                ActivityRecord r = task.activities.get(i);
                if (r.marked || !r.component.equals((Object)comp)) continue;
                return r;
            }
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    boolean performClearTaskLocked(TaskRecord task, ComponentName comp, ClearTaskAction action, boolean singleTop) {
        boolean marked = false;
        List<ActivityRecord> list = task.activities;
        synchronized (list) {
            switch (action) {
                case TASK: {
                    for (ActivityRecord r : task.activities) {
                        r.marked = true;
                        marked = true;
                    }
                    break;
                }
                case ACTIVITY: {
                    ActivityRecord r = this.findActivityByComponentName(task, comp);
                    if (r == null) break;
                    r.marked = true;
                    marked = true;
                    break;
                }
                case TOP: {
                    ActivityRecord r;
                    int foundIndex = -1;
                    for (int i = task.activities.size() - 1; i >= 0; --i) {
                        r = task.activities.get(i);
                        if (!r.component.equals((Object)comp)) continue;
                        foundIndex = i;
                        break;
                    }
                    if (foundIndex < 0) break;
                    marked = true;
                    if (singleTop) {
                        ++foundIndex;
                    }
                    while (foundIndex < task.activities.size()) {
                        r = task.activities.get(foundIndex);
                        r.marked = true;
                        ++foundIndex;
                    }
                    break;
                }
            }
        }
        return marked;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    int startActivityLocked(int userId, Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            this.optimizeTasksLocked();
        }
        ActivityRecord sourceRecord = this.findActivityByToken(userId, resultTo);
        if (resultTo != null && sourceRecord == null) {
            VLog.e(TAG, "Not found source record: " + resultTo);
        }
        VLog.e(TAG, "startActivity:\n" + (sourceRecord == null ? "null" : ActivityStack.componentInfoToString((ComponentInfo)sourceRecord.info)) + "  -->  " + ActivityStack.componentInfoToString((ComponentInfo)info) + "\n" + ActivityStack.activityInfoToString(info) + "\nintent flags: " + ActivityStack.parseIntentFlagsToString(intent) + "\n" + intent + "\nrequestCode: " + requestCode);
        TaskRecord sourceTask = null;
        if (sourceRecord != null) {
            sourceTask = sourceRecord.task;
        } else {
            resultTo = null;
        }
        String affinity = ComponentUtils.getTaskAffinity(info);
        int mLauncherFlags = 0;
        boolean newTask = ActivityStack.containFlags(intent, 0x10000000);
        boolean clearTop = ActivityStack.containFlags(intent, 0x4000000);
        boolean clearTask = newTask && ActivityStack.containFlags(intent, 32768);
        boolean multipleTask = newTask && ActivityStack.containFlags(intent, 0x8000000);
        boolean singleTop = ActivityStack.containFlags(intent, 0x20000000);
        boolean reorderToFront = ActivityStack.containFlags(intent, 131072) && !clearTop;
        boolean forwardResult = ActivityStack.containFlags(intent, 0x2000000);
        boolean newDocument = false;
        int launchMode = info.launchMode;
        int documentLaunchMode = info.documentLaunchMode;
        if ((info.flags & 0x20) != 0 || ActivityStack.containFlags(intent, 0x800000)) {
            mLauncherFlags |= 0x800000;
        }
        if (ActivityStack.containFlags(intent, 65536)) {
            mLauncherFlags |= 0x10000;
        }
        if (ActivityStack.containFlags(intent, 8192)) {
            mLauncherFlags |= 0x2000;
        }
        if (launchMode == 1 || launchMode == 2 || launchMode == 3) {
            singleTop = true;
        }
        if (forwardResult) {
            if (sourceRecord != null && sourceRecord.resultTo != null) {
                ActivityRecord forwardTo = this.findActivityByToken(userId, sourceRecord.resultTo);
                if (forwardTo != null) {
                    mLauncherFlags |= 0x2000000;
                    resultTo = forwardTo.token;
                } else {
                    VLog.e(TAG, "forwardResult failed: " + intent);
                }
            } else {
                VLog.e(TAG, "forwardResult failed: " + intent);
            }
        }
        ComponentName launchComponent = info.targetActivity != null ? new ComponentName(info.packageName, info.targetActivity) : new ComponentName(info.packageName, info.name);
        TaskRecord reuseTask = null;
        if (documentLaunchMode == 2) {
            multipleTask = true;
        } else if ((documentLaunchMode == 1 || ActivityStack.containFlags(intent.getFlags(), 524288)) && newTask) {
            newDocument = true;
        }
        if (!multipleTask) {
            if (newDocument) {
                reuseTask = this.findTaskByIntentLocked(userId, intent);
            } else {
                if (!(newTask || sourceRecord == null || requestCode < 0 && (sourceRecord.info.launchMode == 3 || launchMode != 0 && launchMode != 1))) {
                    reuseTask = sourceTask;
                }
                if (reuseTask == null) {
                    reuseTask = launchMode == 3 ? this.findTaskByComponentLocked(userId, launchComponent) : this.findTaskByAffinityLocked(userId, affinity);
                }
            }
        }
        if (reuseTask == null || reuseTask.isFinishing()) {
            VLog.e(TAG, "startActivityInNewTask: " + intent);
            return this.startActivityInNewTaskLocked(mLauncherFlags, userId, intent, info, options);
        }
        try {
            this.mAM.moveTaskToFront(reuseTask.taskId, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        boolean startTaskToFront = false;
        if (!(launchMode != 0 || singleTop || clearTask || clearTop || reorderToFront || requestCode > 0 || resultTo != null)) {
            startTaskToFront = ComponentUtils.intentFilterEquals(reuseTask.taskRoot, intent);
        }
        if (startTaskToFront) {
            return 0;
        }
        ClearTaskAction clearTaskAction = ClearTaskAction.NONE;
        if (launchMode == 2 || launchMode == 3 || clearTop) {
            clearTaskAction = ClearTaskAction.TOP;
        }
        if (reorderToFront) {
            clearTaskAction = ClearTaskAction.ACTIVITY;
        }
        if (clearTask) {
            clearTaskAction = ClearTaskAction.TASK;
        }
        boolean cleared = false;
        if (singleTop) {
            ActivityRecord top;
            if (clearTaskAction == ClearTaskAction.TOP) {
                cleared = this.performClearTaskLocked(reuseTask, launchComponent, ClearTaskAction.TOP, true);
            } else if (clearTaskAction == ClearTaskAction.NONE) {
                cleared = true;
            }
            if (cleared && (top = reuseTask.getTopActivityRecord()) != null && top.component.equals((Object)launchComponent)) {
                this.deliverNewIntentLocked(sourceRecord, top, intent);
                this.finishMarkedActivity();
                return 0;
            }
            clearTaskAction = ClearTaskAction.NONE;
        }
        if (cleared) {
            this.finishMarkedActivity();
        }
        ActivityRecord launchRecord = this.newActivityRecord(userId, intent, info, resultTo);
        launchRecord.requestCode = requestCode;
        launchRecord.resultWho = resultWho;
        launchRecord.options = options;
        launchRecord.pendingClearAction = clearTaskAction;
        launchRecord.task = reuseTask;
        Intent destIntent = this.startActivityProcess(userId, launchRecord, intent, info);
        if (destIntent == null) {
            return -1;
        }
        reuseTask.activities.add(launchRecord);
        this.mPendingLaunchActivities.add(launchRecord);
        destIntent.addFlags(mLauncherFlags);
        ActivityRecord callerRecord = sourceTask == reuseTask ? sourceRecord : reuseTask.getTopActivityRecord(false);
        if (callerRecord == null || callerRecord.process == null) {
            VLog.e(TAG, "getCallerRecord failed: " + intent);
            return -1;
        }
        launchRecord.started = true;
        return this.startActivityFromSourceTask(callerRecord.process, callerRecord.token, destIntent, resultWho, requestCode, options);
    }

    private ActivityRecord newActivityRecord(int userId, Intent intent, ActivityInfo info, IBinder resultTo) {
        return new ActivityRecord(userId, intent, info, resultTo);
    }

    private void startActivity(Intent intent, Bundle options, boolean addon) {
        ComponentName component = intent.getComponent();
        boolean useBridgeActivity = false;
        if (component != null && !VirtualCore.get().isAppInstalled(component.getPackageName())) {
            useBridgeActivity = true;
        }
        if (addon && !VirtualCore.get().isSharedUserId()) {
            useBridgeActivity = true;
        }
        if (useBridgeActivity) {
            Intent gotoExtIntent = new Intent(VirtualCore.get().getContext(), BridgeActivity.class);
            gotoExtIntent.setFlags(0x10000000);
            gotoExtIntent.putExtra("_VA_|_intent_", (Parcelable)intent);
            gotoExtIntent.putExtra("_VA_|_bundle_", options);
            VirtualCore.get().getContext().startActivity(gotoExtIntent);
            return;
        }
        VirtualCore.get().getContext().startActivity(intent, options);
    }

    private int startActivityInNewTaskLocked(int launcherFlags, int userId, Intent intent, ActivityInfo info, Bundle options) {
        ActivityRecord launchRecord = this.newActivityRecord(userId, intent, info, null);
        launchRecord.options = options;
        Intent destIntent = this.startActivityProcess(userId, launchRecord, intent, info);
        if (destIntent == null) {
            return -1;
        }
        destIntent.addFlags(launcherFlags);
        destIntent.addFlags(0x10000000);
        destIntent.addFlags(0x8000000);
        destIntent.addFlags(0x200000);
        destIntent.addFlags(524288);
        if (options != null) {
            VirtualCore.get().getContext().startActivity(destIntent, options);
        } else {
            VirtualCore.get().getContext().startActivity(destIntent);
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void finishMarkedActivity() {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            int N = this.mHistory.size();
            while (N-- > 0) {
                TaskRecord task = this.mHistory.valueAt(N);
                List<ActivityRecord> list = task.activities;
                synchronized (list) {
                    Iterator<ActivityRecord> it = task.activities.iterator();
                    while (it.hasNext()) {
                        ActivityRecord r = it.next();
                        if (!r.marked || !r.started) continue;
                        try {
                            if (r.process != null && r.process.client != null) {
                                r.process.client.finishActivity(r.token);
                            }
                            it.remove();
                        }
                        catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean finishActivityAffinity(int userId, IBinder token) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            ActivityRecord r = this.findActivityByToken(userId, token);
            if (r == null) {
                return false;
            }
            String taskAffinity = ComponentUtils.getTaskAffinity(r.info);
            List<ActivityRecord> list = r.task.activities;
            synchronized (list) {
                for (int index = r.task.activities.indexOf((Object)r); index >= 0; --index) {
                    ActivityRecord cur = r.task.activities.get(index);
                    if (!ComponentUtils.getTaskAffinity(cur.info).equals(taskAffinity)) break;
                    cur.marked = true;
                }
            }
        }
        this.finishMarkedActivity();
        return false;
    }

    private int startActivityFromSourceTask(ProcessRecord r, IBinder resultTo, Intent intent, String resultWho, int requestCode, Bundle options) {
        return this.realStartActivityLocked(r.appThread, resultTo, intent, resultWho, requestCode, options);
    }

    private int realStartActivityLocked(IInterface appThread, IBinder resultTo, Intent intent, String resultWho, int requestCode, Bundle options) {
        Class<?>[] types = IActivityManager.startActivity.paramList();
        Object[] args = new Object[types.length];
        args[0] = appThread;
        int intentIndex = ArrayUtils.protoIndexOf(types, Intent.class);
        int resultToIndex = ArrayUtils.protoIndexOf(types, IBinder.class, 2);
        int optionsIndex = ArrayUtils.protoIndexOf(types, Bundle.class);
        int resolvedTypeIndex = intentIndex + 1;
        int resultWhoIndex = resultToIndex + 1;
        int requestCodeIndex = resultToIndex + 2;
        args[intentIndex] = intent;
        args[resultToIndex] = resultTo;
        args[resultWhoIndex] = resultWho;
        args[requestCodeIndex] = requestCode;
        if (optionsIndex != -1) {
            args[optionsIndex] = options;
        }
        args[resolvedTypeIndex] = intent.getType();
        args[intentIndex - 1] = VirtualCore.get().getHostPkg();
        ClassUtils.fixArgs(types, args);
        try {
            return IActivityManager.startActivity.call(ActivityManagerNative.getDefault.call(new Object[0]), args);
        }
        catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String selectShadowActivity(int vpid, ActivityInfo targetInfo) {
        boolean isDialogStyle;
        boolean isFloating = false;
        boolean isTranslucent = false;
        boolean showWallpaper = false;
        try {
            int[] R_Styleable_Window = R_Hide.styleable.Window.get();
            int R_Styleable_Window_windowIsTranslucent = R_Hide.styleable.Window_windowIsTranslucent.get();
            int R_Styleable_Window_windowIsFloating = R_Hide.styleable.Window_windowIsFloating.get();
            int R_Styleable_Window_windowShowWallpaper = R_Hide.styleable.Window_windowShowWallpaper.get();
            AttributeCache.Entry ent = AttributeCache.instance().get(targetInfo.packageName, targetInfo.theme, R_Styleable_Window);
            if (ent != null && ent.array != null) {
                showWallpaper = ent.array.getBoolean(R_Styleable_Window_windowShowWallpaper, false);
                isTranslucent = ent.array.getBoolean(R_Styleable_Window_windowIsTranslucent, false);
                isFloating = ent.array.getBoolean(R_Styleable_Window_windowIsFloating, false);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        boolean bl = isDialogStyle = isFloating || isTranslucent || showWallpaper;
        if (isDialogStyle) {
            return StubManifest.getStubDialogName(vpid, targetInfo);
        }
        return StubManifest.getStubActivityName(vpid, targetInfo);
    }

    private Intent startActivityProcess(int userId, ActivityRecord targetRecord, Intent intent, ActivityInfo info) {
        ProcessRecord targetApp = this.mService.startProcessIfNeeded(info.processName, userId, info.packageName, -1);
        if (targetApp == null) {
            return null;
        }
        return this.getStartShadowActivityIntentInner(intent, targetApp.isExt, targetApp.vpid, userId, targetRecord, info);
    }

    private Intent getStartShadowActivityIntentInner(Intent intent, boolean isStub, int vpid, int userId, ActivityRecord targetRecord, ActivityInfo info) {
        intent = new Intent(intent);
        Intent targetIntent = new Intent();
        if (info.screenOrientation == 3 && targetRecord.task != null && targetRecord.task.getTopActivityRecord() != null) {
            info.screenOrientation = targetRecord.task.getTopActivityRecord().info.screenOrientation;
        }
        targetIntent.setClassName(StubManifest.getStubPackageName(isStub), this.selectShadowActivity(vpid, info));
        ComponentName component = intent.getComponent();
        if (component == null) {
            component = ComponentUtils.toComponentName((ComponentInfo)info);
        }
        targetIntent.setType(component.flattenToString());
        ShadowActivityInfo saveInstance = new ShadowActivityInfo(intent, info, userId, (IBinder)targetRecord);
        saveInstance.saveToIntent(targetIntent);
        return targetIntent;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void onActivityCreated(ProcessRecord targetApp, IBinder token, int taskId, ActivityRecord record) {
        VLog.e(TAG, "onActivityCreated " + record.info + " taskId: " + taskId);
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            List<ActivityRecord> list;
            this.mPendingLaunchActivities.remove((Object)record);
            this.optimizeTasksLocked();
            TaskRecord task = this.mHistory.get(taskId);
            if (task == null) {
                if (record.task != null) {
                    task = record.task;
                } else {
                    task = new TaskRecord(taskId, targetApp.userId, ComponentUtils.getTaskAffinity(record.info), record.intent);
                    this.mHistory.put(taskId, task);
                }
            }
            if (record.task != null && record.task != task) {
                list = record.task.activities;
                synchronized (list) {
                    record.task.activities.remove((Object)record);
                }
            }
            record.task = task;
            list = record.task.activities;
            synchronized (list) {
                task.activities.remove((Object)record);
            }
            if (record.pendingClearAction != ClearTaskAction.NONE) {
                this.performClearTaskLocked(task, record.component, record.pendingClearAction, false);
                record.pendingClearAction = ClearTaskAction.NONE;
            }
            record.init(task, targetApp, token);
            task.activities.add(record);
            if (record.pendingNewIntent != null) {
                PendingNewIntent newIntent = record.pendingNewIntent;
                try {
                    record.process.client.scheduleNewIntent(newIntent.creator, record.token, newIntent.intent);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
                record.pendingNewIntent = null;
            }
            this.finishMarkedActivity();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void onActivityResumed(int userId, IBinder token) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            this.optimizeTasksLocked();
            ActivityRecord r = this.findActivityByToken(userId, token);
            if (r != null) {
                List<ActivityRecord> list = r.task.activities;
                synchronized (list) {
                    r.task.activities.remove((Object)r);
                    r.task.activities.add(r);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void onActivityFinish(int userId, IBinder token) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            ActivityRecord r = this.findActivityByToken(userId, token);
            if (r != null) {
                r.marked = true;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    ActivityRecord onActivityDestroyed(int userId, IBinder token) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            this.optimizeTasksLocked();
            ActivityRecord r = this.findActivityByToken(userId, token);
            if (r != null) {
                VLog.e(TAG, "onActivityDestroyed " + r.info + " taskId: " + r.task.taskId);
                r.marked = true;
                List<ActivityRecord> list = r.task.activities;
                synchronized (list) {
                    r.task.activities.remove((Object)r);
                }
            }
            return r;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void processDied(ProcessRecord record) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            this.optimizeTasksLocked();
            int N = this.mHistory.size();
            while (N-- > 0) {
                TaskRecord task = this.mHistory.valueAt(N);
                List<ActivityRecord> list = task.activities;
                synchronized (list) {
                    Iterator<ActivityRecord> iterator = task.activities.iterator();
                    while (iterator.hasNext()) {
                        ActivityRecord r = iterator.next();
                        if (!r.started || r.process != null && r.process.pid != record.pid) continue;
                        iterator.remove();
                        if (!task.activities.isEmpty()) continue;
                        this.mHistory.remove(task.taskId);
                    }
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void finishAllActivities(ProcessRecord record) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            int N = this.mHistory.size();
            while (N-- > 0) {
                TaskRecord task = this.mHistory.valueAt(N);
                List<ActivityRecord> list = task.activities;
                synchronized (list) {
                    for (ActivityRecord r : task.activities) {
                        if (r.process.pid != record.pid) continue;
                        r.marked = true;
                    }
                }
            }
        }
        this.finishMarkedActivity();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    String getPackageForToken(int userId, IBinder token) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            ActivityRecord r = this.findActivityByToken(userId, token);
            if (r != null) {
                return r.info.packageName;
            }
            return null;
        }
    }

    private ActivityRecord getCallingRecordLocked(int userId, IBinder token) {
        ActivityRecord r = this.findActivityByToken(userId, token);
        if (r == null) {
            return null;
        }
        return this.findActivityByToken(userId, r.resultTo);
    }

    ComponentName getCallingActivity(int userId, IBinder token) {
        ActivityRecord r = this.getCallingRecordLocked(userId, token);
        return r != null ? r.component : null;
    }

    String getCallingPackage(int userId, IBinder token) {
        ActivityRecord r = this.getCallingRecordLocked(userId, token);
        return r != null ? r.info.packageName : null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    AppTaskInfo getTaskInfo(int taskId) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            TaskRecord task = this.mHistory.get(taskId);
            if (task != null) {
                return task.getAppTaskInfo();
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    ComponentName getActivityClassForToken(int userId, IBinder token) {
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            ActivityRecord r = this.findActivityByToken(userId, token);
            if (r != null) {
                return r.component;
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int startActivityFromHistoryLocked(Intent intent) {
        VLog.e(TAG, "startActivityFromHistory: " + intent);
        SparseArray<TaskRecord> sparseArray = this.mHistory;
        synchronized (sparseArray) {
            ShadowActivityInfo info = new ShadowActivityInfo(intent);
            ActivityRecord record = (ActivityRecord)info.virtualToken;
            if (record == null || !this.mPendingLaunchActivities.contains((Object)record)) {
                VLog.e(TAG, "record not in pending list.");
                return -1;
            }
            if (record.task == null) {
                VirtualCore.get().getContext().startActivity(intent);
                return 0;
            }
            ActivityRecord callerRecord = this.findActivityByToken(record.userId, record.resultTo);
            if (callerRecord == null || callerRecord.task != record.task) {
                callerRecord = record.task.getTopActivityRecord();
            }
            return this.startActivityFromSourceTask(callerRecord.process, callerRecord.token, intent, record.resultWho, record.requestCode, record.options);
        }
    }
}


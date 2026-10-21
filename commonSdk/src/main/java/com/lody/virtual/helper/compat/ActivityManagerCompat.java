/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Intent
 *  android.os.Build$VERSION
 *  android.os.IBinder
 */
package com.lody.virtual.helper.compat;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import mirror.android.app.Activity;
import mirror.android.app.ActivityManager;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.IActivityManager;
import mirror.android.app.IActivityManagerICS;
import mirror.android.app.IActivityManagerL;
import mirror.android.app.IActivityManagerN;

public class ActivityManagerCompat {
    public static final int SERVICE_DONE_EXECUTING_ANON = 0;
    public static final int SERVICE_DONE_EXECUTING_START = 1;
    public static final int SERVICE_DONE_EXECUTING_STOP = 2;
    public static final int START_INTENT_NOT_RESOLVED = ActivityManager.START_INTENT_NOT_RESOLVED == null ? -1 : ActivityManager.START_INTENT_NOT_RESOLVED.get();
    public static final int START_NOT_CURRENT_USER_ACTIVITY = ActivityManager.START_NOT_CURRENT_USER_ACTIVITY == null ? -8 : ActivityManager.START_NOT_CURRENT_USER_ACTIVITY.get();
    public static final int START_TASK_TO_FRONT = ActivityManager.START_TASK_TO_FRONT == null ? 2 : ActivityManager.START_TASK_TO_FRONT.get();
    public static final int INTENT_SENDER_BROADCAST = 1;
    public static final int INTENT_SENDER_ACTIVITY = 2;
    public static final int INTENT_SENDER_ACTIVITY_RESULT = 3;
    public static final int INTENT_SENDER_SERVICE = 4;
    public static final int USER_OP_SUCCESS = 0;

    public static boolean finishActivity(IBinder token, int code, Intent data) {
        if (Build.VERSION.SDK_INT >= 24) {
            return IActivityManagerN.finishActivity.call(ActivityManagerNative.getDefault.call(new Object[0]), token, code, data, 0);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return IActivityManagerL.finishActivity.call(ActivityManagerNative.getDefault.call(new Object[0]), token, code, data, false);
        }
        IActivityManagerICS.finishActivity.call(ActivityManagerNative.getDefault.call(new Object[0]), token, code, data);
        return false;
    }

    public static void setActivityOrientation(android.app.Activity activity, int orientation) {
        try {
            activity.setRequestedOrientation(orientation);
        }
        catch (Throwable e) {
            e.printStackTrace();
            android.app.Activity parent = Activity.mParent.get(activity);
            while (parent != null) {
                parent = Activity.mParent.get(parent);
            }
            IBinder token = Activity.mToken.get(parent);
            try {
                IActivityManager.setRequestedOrientation.call(ActivityManagerNative.getDefault.call(new Object[0]), token, orientation);
            }
            catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.env;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.stub.ShortcutHandleActivity;

public class Constants {
    public static final int OUTSIDE_APP_UID = 9000;
    public static final int UNKNOWN_APP_UID = 9001;
    public static final String ACTION_LAUNCH_HELPER_PROCESS = "android.intent.action.LAUNCH_HELPER";
    public static final String ACTION_GMS_INITIALIZED = "android.intent.action.GMS_INITIALIZED";
    public static final String EXTRA_USER_HANDLE = "android.intent.extra.user_handle";
    public static final String EXTRA_PACKAGE_NAME = "android.intent.extra.package_name";
    public static final String FEATURE_FAKE_SIGNATURE = "fake-signature";
    public static final String ACTION_NEW_TASK_CREATED = "virtual.intent.action.APP_LAUNCHED";
    public static final String ACTION_PACKAGE_WILL_ADDED = "virtual.intent.action.PACKAGE_WILL_ADDED";
    public static final String ACTION_PACKAGE_ADDED = "virtual.android.intent.action.PACKAGE_ADDED";
    public static final String ACTION_PACKAGE_REMOVED = "virtual.android.intent.action.PACKAGE_REMOVED";
    public static final String ACTION_PACKAGE_CHANGED = "virtual.android.intent.action.PACKAGE_CHANGED";
    public static final String ACTION_USER_ADDED = "virtual.android.intent.action.USER_ADDED";
    public static final String ACTION_USER_REMOVED = "virtual.android.intent.action.USER_REMOVED";
    public static final String ACTION_USER_INFO_CHANGED = "virtual.android.intent.action.USER_CHANGED";
    public static final String ACTION_USER_STARTED = "Virtual.android.intent.action.USER_STARTED";
    public static String SERVER_PROCESS_NAME = ":va_core";
    public static String HELPER_PROCESS_NAME = ":va_helper";
    public static String SHORTCUT_PROXY_ACTIVITY_NAME = ShortcutHandleActivity.class.getName();
    public static String ACTION_SHORTCUT = ".virtual.action.shortcut";
    public static String ACTION_BADGER_CHANGE = ".virtual.action.BADGER_CHANGE";
    public static String NOTIFICATION_CHANNEL = "virtual_default";
    public static String NOTIFICATION_DAEMON_CHANNEL = "virtual_daemon";
}


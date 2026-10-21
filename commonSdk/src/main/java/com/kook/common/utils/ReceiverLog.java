/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.provider.Settings$System
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.kook.common.utils;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.kook.core.log.StringFog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReceiverLog
extends BroadcastReceiver {
    public static String LOG_LEVEL;
    public static String LOG_TAG;
    private static String SYSTEM_SETTINGS_KEY;
    Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static final String FILE_NAME;

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        Log.d((String)"kooklog", (String)"ReceiverLog onReceive");
        if (intent.hasExtra(LOG_LEVEL)) {
            int logLevel = intent.getIntExtra(LOG_LEVEL, 0);
            Log.d((String)"kooklog", (String)("logLevel:" + logLevel));
            ReceiverLog.putInt(context, LOG_LEVEL, logLevel);
        }
    }

    public void setLogTag(Context context, String logTag) {
        List<String> logTagList = ReceiverLog.getLogTag(context);
        if (logTagList == null) {
            logTagList = new ArrayList<String>();
        }
        logTagList.add(logTag);
        String listAsString = TextUtils.join((CharSequence)",", logTagList);
        Settings.System.putString((ContentResolver)context.getContentResolver(), (String)SYSTEM_SETTINGS_KEY, (String)listAsString);
    }

    public static List<String> getLogTag(Context context) {
        String listAsString = Settings.System.getString((ContentResolver)context.getContentResolver(), (String)SYSTEM_SETTINGS_KEY);
        if (listAsString != null) {
            List<String> myList = Arrays.asList(listAsString.split(","));
            return myList;
        }
        return null;
    }

    public static int getInt(Context context, String keyname) {
        return ReceiverLog.getInt(context, keyname, -1);
    }

    public static int getInt(Context context, String keyname, int def) {
        SharedPreferences shared = ReceiverLog.getSharedPreferences(context);
        int v = shared.getInt(keyname, def);
        if (v == def) {
            return def;
        }
        return v;
    }

    public static void putInt(Context context, String keyname, int values) {
        SharedPreferences shared = ReceiverLog.getSharedPreferences(context);
        SharedPreferences.Editor e = shared.edit();
        e.putInt(keyname, values);
        boolean b = e.commit();
        if (b) {
            // empty if block
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(FILE_NAME, 4);
        }
        return mSharedPreferences;
    }

    static {
        FILE_NAME = "sharedPreferences";
        LOG_LEVEL = "logLevel";
        LOG_TAG = "logTag";
        SYSTEM_SETTINGS_KEY = "tag_list_key";
    }
}


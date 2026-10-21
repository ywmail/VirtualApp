/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.lody.virtual.helper.compat;

import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Reflect;

public class SystemPropertiesCompat {
    public static String get(String key, String def) {
        try {
            return (String)Reflect.on("android.os.SystemProperties").call("get", key, def).get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    public static String get(String key) {
        try {
            return (String)Reflect.on("android.os.SystemProperties").call("get", key).get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isExist(String key) {
        return !TextUtils.isEmpty((CharSequence)SystemPropertiesCompat.get(key));
    }

    public static int getInt(String key, int def) {
        try {
            return (Integer)Reflect.on("android.os.SystemProperties").call("getInt", key, def).get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }
}


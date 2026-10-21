/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 */
package com.carlos.common.reverse.hooker;

import android.content.Context;
import android.util.Log;
import com.kook.librelease.StringFog;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;

@HookReflectClass(value="com.google.android.gms.common.GooglePlayServicesUtilLight")
public class GMSHooker1 {
    @HookMethod(value="isGooglePlayServicesAvailable")
    @MethodParams(value={Context.class})
    public static boolean isGooglePlayServicesAvailable(Context v1) throws Throwable {
        Log.d((String)"vatest", (String)"isGooglePlayServicesAvailable1");
        return true;
    }

    @HookMethod(value="isGooglePlayServicesAvailable")
    @MethodParams(value={Context.class, int.class})
    public static boolean isGooglePlayServicesAvailable2(Context v1, int v2) throws Throwable {
        Log.d((String)"vatest", (String)"isGooglePlayServicesAvailable2");
        return true;
    }

    @HookMethod(value="isPlayServicesPossiblyUpdating")
    @MethodParams(value={Context.class, int.class})
    public static boolean isGooglePlayServicesUid(Context v1, int v2) throws Throwable {
        Log.d((String)"vatest", (String)"isPlayServicesPossiblyUpdating");
        return true;
    }

    @HookMethod(value="isPlayStorePossiblyUpdating")
    @MethodParams(value={Context.class, int.class})
    public static boolean isPlayStorePossiblyUpdating(Context v1, int v2) throws Throwable {
        Log.d((String)"vatest", (String)"isPlayStorePossiblyUpdating");
        return true;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.carlos.common.reverse.hooker;

import android.content.Context;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;

@HookReflectClass(value="com.google.android.gms.common.GooglePlayServicesUtilLight")
public class GoogleServiceHooker {
    @MethodParams(value={Context.class})
    @HookMethod(value="isGooglePlayServicesAvailable")
    public static boolean m531a(Context context) throws Throwable {
        return true;
    }

    @MethodParams(value={Context.class, int.class})
    @HookMethod(value="isGooglePlayServicesAvailable")
    public static boolean m530a(Context context, int i) throws Throwable {
        return true;
    }

    @MethodParams(value={Context.class, int.class})
    @HookMethod(value="isPlayServicesPossiblyUpdating")
    public static boolean m529b(Context context, int i) throws Throwable {
        return true;
    }

    @MethodParams(value={Context.class, int.class})
    @HookMethod(value="isPlayStorePossiblyUpdating")
    public static boolean m528c(Context context, int i) throws Throwable {
        return true;
    }
}


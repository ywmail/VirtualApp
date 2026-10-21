/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.carlos.common.reverse.hooker;

import android.util.Log;
import com.kook.librelease.StringFog;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookReflectClass;

@HookReflectClass(value="com.google.android.gms.auth.api.signin.internal.zbe")
public class GMSHooker2 {
    @HookMethod(value="zba")
    public static boolean providesSignIn(Object thiz) throws Throwable {
        Log.d((String)"vatest", (String)"providesSignIn");
        return true;
    }
}


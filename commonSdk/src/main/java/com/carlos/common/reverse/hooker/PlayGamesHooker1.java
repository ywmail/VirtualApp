/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.carlos.common.reverse.hooker;

import android.util.Log;
import com.kook.librelease.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;
import mirror.android.app.Activity;

@HookReflectClass(value="defpackage.fos")
public class PlayGamesHooker1 {
    @HookMethodBackup(value="g")
    @MethodParams(value={Activity.class})
    static Method method_m1;

    @HookMethod(value="g")
    public static Activity getCallingActivity(Object thiz, Activity v1) throws Throwable {
        Log.d((String)"vatest", (String)"getCallingActivity");
        return (Activity)SandHook.callOriginByBackup(method_m1, thiz, v1);
    }
}


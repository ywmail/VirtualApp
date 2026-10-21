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
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;

@HookClass(value=Method.class)
public class Hook_Clazz {
    @HookMethodBackup(value="invoke")
    @MethodParams(value={Object.class, Object[].class})
    static Method method_m1;

    @HookMethod(value="invoke")
    @MethodParams(value={Object.class, Object[].class})
    public static Object m1(Method method, Object v1, Object[] v2) throws Throwable {
        Log.d((String)"vatest", (String)"Hook_Clazz invoke");
        return SandHook.callOriginByBackup(method_m1, method, v1, v2);
    }
}


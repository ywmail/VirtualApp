/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Message
 *  android.util.Log
 */
package com.carlos.common.reverse.hooker;

import android.os.Message;
import android.util.Log;
import com.kook.librelease.StringFog;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;

@HookReflectClass(value="com.inca.security.IiiIiiiiIi")
public class GuardHooker {
    @HookMethodBackup(value="handleMessage")
    @MethodParams(value={Message.class})
    static Method method_m1;

    @HookMethod(value="handleMessage")
    @MethodParams(value={Message.class})
    public static void m1(Message v1) throws Throwable {
        Log.d((String)"vatest", (String)"GuardHooker attachBaseContext");
    }
}


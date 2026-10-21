/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Bundle
 *  android.util.Log
 */
package com.carlos.common.reverse.hooker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.kook.librelease.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import com.swift.sandhook.annotation.ThisObject;
import com.swift.sandhook.wrapper.HookWrapper;
import java.lang.reflect.Method;

@HookClass(value=Activity.class)
public class ActivityHooker {
    @HookMethodBackup(value="onCreate")
    @MethodParams(value={Bundle.class})
    static Method onCreateBackup;
    @HookMethodBackup(value="onPause")
    static HookWrapper.HookEntity onPauseBackup;

    @HookMethod(value="onCreate")
    @MethodParams(value={Bundle.class})
    public static void onCreate(Activity thiz, Bundle bundle) throws Throwable {
        Log.e((String)"Hooker", (String)("hooked onCreate success " + thiz));
        SandHook.callOriginByBackup(onCreateBackup, thiz, bundle);
    }

    @HookMethod(value="onPause")
    public static void onPause(@ThisObject Activity thiz) throws Throwable {
        Log.e((String)"Hooker", (String)("hooked onPause success " + thiz));
        onPauseBackup.callOrigin(thiz, new Object[0]);
    }
}


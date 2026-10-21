/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 */
package com.carlos.common.reverse.grame;

import android.app.Application;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;

public class Nexon {
    public static File file = new File("/sdcard/Android/data/com.carlos.multiapp/files/Android_va/0/Android/data/com.nexon.hit2/");

    public static void hook(ClassLoader classLoader, Application application) {
        HVLog.d(" 开始 hook com.nexon.hit2  ");
        XposedHelpers.findAndHookMethod("com.nexon.pub.bar.NXPatcher$Config", classLoader, "isDebugMode", new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            }
        });
    }
}


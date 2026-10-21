/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 */
package com.carlos.common.reverse.epicgames;

import android.app.Application;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class EpicGames {
    public static void hook(ClassLoader classLoader, Application application) {
        HVLog.d(" 开始 Logger  ======================================================      2  ");
        final Class<?> Logger2 = XposedHelpers.findClass("com.epicgames.ue4.Logger", classLoader);
        XposedHelpers.callStaticMethod(Logger2, "SuppressLogs", new Object[0]);
        XposedHelpers.findAndHookMethod("com.epicgames.ue4.Logger", classLoader, "debug", String.class, new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 debug log:" + param.args[0]);
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                boolean bAllowLogging = XposedHelpers.getStaticBooleanField(Logger2, "bAllowLogging");
                boolean bAllowExceptionLogging = XposedHelpers.getStaticBooleanField(Logger2, "bAllowExceptionLogging");
                HVLog.d("epicgames 游戏 debug log:" + param.args[0] + "    bAllowLogging:" + bAllowLogging + "    bAllowExceptionLogging:" + bAllowExceptionLogging);
                XposedHelpers.setStaticBooleanField(Logger2, "bAllowLogging", true);
                XposedHelpers.setStaticBooleanField(Logger2, "bAllowExceptionLogging", true);
            }
        });
        XposedHelpers.findAndHookMethod("com.epicgames.ue4.Logger", classLoader, "error", String.class, new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 error log:" + param.args[0] + "    result:" + param.getResult());
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏error log:" + param.args[0]);
            }
        });
        XposedHelpers.findAndHookMethod("com.epicgames.ue4.Logger", classLoader, "verbose", String.class, new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 verbose log:" + param.args[0]);
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                boolean bAllowLogging = XposedHelpers.getStaticBooleanField(Logger2, "bAllowLogging");
                boolean bAllowExceptionLogging = XposedHelpers.getStaticBooleanField(Logger2, "bAllowExceptionLogging");
                HVLog.d("epicgames 游戏 verbose log:" + param.args[0] + "    bAllowLogging:" + bAllowLogging + "    bAllowExceptionLogging:" + bAllowExceptionLogging);
                XposedHelpers.setStaticBooleanField(Logger2, "bAllowLogging", true);
                XposedHelpers.setStaticBooleanField(Logger2, "bAllowExceptionLogging", true);
            }
        });
        XposedHelpers.findAndHookMethod("com.epicgames.ue4.Logger", classLoader, "warn", String.class, new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 warn log:" + param.args[0]);
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 warn log:" + param.args[0]);
            }
        });
        XposedHelpers.findAndHookMethod("com.epicgames.ue4.GameActivity", classLoader, "AndroidThunkJava_GetMetaDataInt", String.class, new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 AndroidThunkJava_GetMetaDataInt log:" + param.args[0] + "    result:" + param.getResult());
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 AndroidThunkJava_GetMetaDataInt log:" + param.args[0] + "    result:" + param.getResult());
            }
        });
        XposedHelpers.findAndHookMethod("com.epicgames.ue4.GameActivity", classLoader, "AndroidThunkJava_ForceQuit", new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 AndroidThunkJava_ForceQuit ");
                HVLog.printInfo();
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                HVLog.d("epicgames 游戏 AndroidThunkJava_ForceQuit log:");
            }
        });
    }
}


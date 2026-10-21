/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 */
package com.carlos.common.reverse.xhs;

import android.app.Application;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;

public class XHSHook {
    public static String XPOSED_MAIN = "de.robv.android.xposed.XposedHelpers";

    public static void hook(ClassLoader classLoader, Application application) {
        HVLog.d(" 开始 hook com.xingin.xhs  ");
        XposedHelpers.findAndHookMethod("java.lang.String", classLoader, "format", String.class, Object[].class, new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                String args0 = (String)param.args[0];
                Object[] args1 = (Object[])param.args[1];
                Object paramResult = param.getResult();
                if ("/data/data/%s".equals(args0)) {
                    HVLog.e("hook", "  String.format " + args0 + "   paramResult:" + paramResult);
                    if (!"/data/data/com.xingin.xhs".equals(paramResult)) {
                        param.setResult("/data/data/com.xingin.xhs.hook");
                        boolean exists = new File("/data/data/com.xingin.xhs.hook").exists();
                        HVLog.e("hook", "  String.format  /data/data/com.xingin.xhs.hook  exists:" + exists);
                    }
                }
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            }
        });
        XposedHelpers.findAndHookConstructor("java.lang.String", classLoader, byte[].class, new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                byte[] args = (byte[])param.args[0];
                HVLog.d("================================xpclz hook:");
                if (args != null) {
                    String xpclz = new String(args);
                    HVLog.d("====xpclz:" + xpclz);
                }
                super.afterHookedMethod(param);
            }

            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
        });
    }
}


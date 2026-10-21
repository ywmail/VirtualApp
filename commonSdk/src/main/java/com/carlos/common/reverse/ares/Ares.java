//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.carlos.common.reverse.ares;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.carlos.common.reverse.hooker.DialogFragmentHooker;
import com.carlos.common.reverse.hooker.DialogHooker;
import com.carlos.common.reverse.hooker.GoogleServiceHooker;
import com.carlos.common.reverse.hooker.ThreadHooker;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ares {
    public Ares() {
    }

    public static void hook(ClassLoader classLoader, Application application, String packageName, String processName) {
        if (!"com.google.android.gms".equals(packageName) && !"com.google.android.gsf".equals(packageName) && !"com.android.vending".equals(packageName)) {
            Log.d("vatest", "beforeApplicationCreate:" + packageName);
            testDev(application, packageName);
            initHooker(application, packageName);
        }
    }

    private static void testDev(Application application, String packageName) {
    }

    private static void initDSF(Application application, String mstr) {
    }

    private static void initHooker(Application application, String packageName) {
        SandHookConfig.DEBUG = false;
        List<Class> listHook = new ArrayList();
        listHook.add(DialogFragmentHooker.class);
        listHook.add(DialogHooker.class);
        if ("com.kakaogames.ares".equals(packageName)) {
            listHook.add(GoogleServiceHooker.class);
            XposedHelpers.findAndHookMethod("com.liapp.x", application.getClassLoader(), "ݲج۱֭ة", new Object[]{new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "888888888888");
                    Thread.dumpStack();
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.liapp.ݳڬׯدګ ", application.getClassLoader(), "onCancel", new Object[]{"android.content.DialogInterface", new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "onCancel");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.liapp.ֳܮֲִذ ", application.getClassLoader(), "onClick", new Object[]{"android.content.DialogInterface", Integer.TYPE, new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "onCancel");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("java.lang.Runtime", application.getClassLoader(), "exec", new Object[]{String[].class, String[].class, File.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            }});
            XposedHelpers.findAndHookMethod("java.lang.Runtime", application.getClassLoader(), "exec", new Object[]{String.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            }});
            XposedHelpers.findAndHookMethod("java.lang.Runtime", application.getClassLoader(), "exec", new Object[]{String.class, String[].class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            }});
            XposedHelpers.findAndHookMethod("java.lang.Runtime", application.getClassLoader(), "exec", new Object[]{String[].class, String[].class, File.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            }});
            XposedHelpers.findAndHookMethod("java.lang.Runtime", application.getClassLoader(), "exec", new Object[]{String[].class, String[].class, File.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "Runtime exec 4 result:" + param.getResult());
                    super.afterHookedMethod(param);
                }
            }});
            listHook.add(ThreadHooker.class);
            listHook.add(GoogleServiceHooker.class);
        }

        if ("com.joycity.gt".equals(packageName) || "com.wemade.mirm".equals(packageName) || "com.com2usholdings.zenonia.android.google.kr.normal".equals(packageName)) {
            listHook.add(GoogleServiceHooker.class);
            XposedHelpers.findAndHookMethod("com.hive.authv4.AuthV4WebViewDialog", application.getClassLoader(), "createWebViewDialog", new Object[]{"com.hive.ui.HiveWebViewClient", new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "createWebViewDialog ");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.inca.security.Proxy.iIiIiIiIii", application.getClassLoader(), "iIiIIiIiiI", new Object[]{Context.class, Object.class, new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "Proxy IiIiiIiIiI ");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.inca.security.Native.AppGuardPreAssistantNative", application.getClassLoader(), "IiIiiIiIiI", new Object[]{Context.class, new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "AppGuardPreAssistantNative IiIiiIiIiI ");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.inca.security.Proxy.AppGuardFrontApplication", application.getClassLoader(), "IiIiIiiIII", new Object[]{Integer.TYPE, new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "AppGuardFrontApplication IiIiiIiIiI ");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.inca.security.Proxy.AppGuardFrontApplication", application.getClassLoader(), "iIIiiiiIiI", new Object[]{new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "AppGuardFrontApplication iIIiiiiIiI 2");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.inca.security.Proxy.AppGuardFrontApplication", application.getClassLoader(), "IIIiIIIIii", new Object[]{new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "AppGuardFrontApplication iIIiiiiIiI 3");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.inca.security.Proxy.AppGuardFrontApplication", application.getClassLoader(), "iIiIIIiiii", new Object[]{new XC_MethodReplacement() {
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "AppGuardFrontApplication iIiIIIiiii 1");
                    return null;
                }
            }});
            XposedHelpers.findAndHookMethod("com.inca.security.Proxy.AppGuardFrontApplication", application.getClassLoader(), "iIiIIIiiii", new Object[]{String.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "iIiIIIiiii 5 " + param.args[0].toString());
                }

                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("vatest", "iIiIIIiiii 5 " + param.getResult());
                    super.afterHookedMethod(param);
                }
            }});
        }

        try {
            SandHook.addHookClass(application.getApplicationContext().getClassLoader(), (Class[])listHook.toArray(new Class[0]));
        } catch (Exception var4) {
            Log.e("vatest", "SH ERR:" + var4.getMessage());
            var4.printStackTrace();
        }

    }
}

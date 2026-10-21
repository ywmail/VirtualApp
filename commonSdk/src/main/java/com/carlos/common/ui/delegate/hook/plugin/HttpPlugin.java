/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.view.View
 */
package com.carlos.common.ui.delegate.hook.plugin;

import android.app.Application;
import android.view.View;
import com.carlos.common.ui.delegate.hook.utils.ClassUtil;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.net.URL;

public class HttpPlugin {
    private static final String TAG = "QQBrowserHookHttp";
    ClassLoader mClassLoader;
    String mVersionName;
    boolean isHooking = false;

    public void hook(String packageName, String processName, Application application) {
        this.mClassLoader = application.getClassLoader();
        if (this.isHooking) {
            return;
        }
        this.isHooking = true;
        this.hookHttp();
    }

    private void hookHttp() {
        Class<?> MttRequestBaseClass;
        HVLog.d(TAG, "QQBrowser开始HookHttp");
        try {
            Class<?> UrlParamsClass = XposedHelpers.findClass("com.tencent.mtt.browser.window.UrlParams", this.mClassLoader);
            XposedBridge.hookAllConstructors(UrlParamsClass, new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object object = param.thisObject;
                    ClassUtil.printFieldsInClassAndObject("UrlParams", object.getClass(), object);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class<?> httpUrlConnection = XposedHelpers.findClass("java.net.HttpURLConnection", this.mClassLoader);
            XposedBridge.hookAllConstructors(httpUrlConnection, new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                    if (param.args.length != 1 || param.args[0].getClass() != URL.class) {
                        return;
                    }
                    URL url = (URL)param.args[0];
                    HVLog.d("QQBrowserHookHttp", "HttpURLConnection: " + param.args[0] + "");
                    if (url.toString().contains("113.96")) {
                        StringBuilder TraceString = new StringBuilder("");
                        TraceString.append("<<<<------------------------------>>>>>  \n").append("\n <<<<------------------------------>>>>>").append("\n");
                        HVLog.e("QQBrowserHookHttp", "堆栈信息：" + TraceString.toString());
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MttRequestBaseClass = XposedHelpers.findClass("com.tencent.common.http.MttRequestBase", this.mClassLoader);
            XposedBridge.hookAllMethods(MttRequestBaseClass, "addHeaders", new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object thisObject = param.thisObject;
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MttRequestBaseClass = XposedHelpers.findClass("com.tencent.common.http.MttRequestBase", this.mClassLoader);
            XposedBridge.hookAllMethods(MttRequestBaseClass, "addHeader", new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object thisObject = param.thisObject;
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class<?> RequestClass = XposedHelpers.findClass("com.squareup.okhttp.Request", this.mClassLoader);
            XposedHelpers.findAndHookMethod("com.squareup.okhttp.OkHttpClient", this.mClassLoader, "newCall", RequestClass, new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object param0 = param.args[0];
                    ClassUtil.printFieldsInClassAndObject("OkHttpClient.newCall-param0", param0.getClass(), param0);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            XposedHelpers.findAndHookMethod("com.tencent.mtt.WindowComponentExtensionImp", this.mClassLoader, "j", new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    HVLog.d("QQBrowserHookHttp", "WindowComponentExtensionImp.j() 执行了");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            XposedHelpers.findAndHookMethod("com.tencent.mtt.browser.bra.toolbar.h", this.mClassLoader, "onClick", View.class, new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    HVLog.d("QQBrowserHookHttp", "toolbar.h.onClick() 执行了");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.util.Log
 */
package com.carlos.common.reverse.dingding;

import android.content.Intent;
import android.util.Log;
import com.carlos.common.reverse.ReflectionApplication;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import com.lody.virtual.helper.utils.VLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DingTalk
extends ReflectionApplication {
    private static final String TAG = "HV-DingTalk";

    public static void hook(ClassLoader classLoader) {
        if (!REFLECTION_DTALK) {
            return;
        }
        try {
            Class<?> ActionRequest = XposedHelpers.findClass("com.alibaba.lightapp.runtime.ActionRequest", classLoader);
            Class<?> Plugin = XposedHelpers.findClass("com.alibaba.lightapp.runtime.Plugin", classLoader);
            Class<?> Method2 = XposedHelpers.findClass("java.lang.reflect.Method", classLoader);
            Class<?> TheOneActivityBase = XposedHelpers.findClass("com.alibaba.lightapp.runtime.ariver.TheOneActivityBase", classLoader);
            XposedHelpers.findAndHookMethod("com.alibaba.android.dingtalkbase.DingtalkBaseActivity", classLoader, "onNewIntent", Intent.class, new XC_MethodHook(){

                @Override
                public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    super.afterHookedMethod(methodHookParam);
                    VLog.d("HV-DingTalk", "DingtalkBaseActivity  getIntent:" + methodHookParam.getResult(), new Object[0]);
                }

                @Override
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    super.beforeHookedMethod(methodHookParam);
                }
            });
            XposedHelpers.findAndHookMethod("com.alibaba.lightapp.runtime.plugin.internal.Util", classLoader, "getWua", ActionRequest, new XC_MethodHook(){

                @Override
                public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    super.afterHookedMethod(methodHookParam);
                    VLog.d("HV-DingTalk", "查看返回:" + methodHookParam.getResult(), new Object[0]);
                    VLog.printStackTrace("getWua");
                    methodHookParam.setResult(null);
                }

                @Override
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    super.beforeHookedMethod(methodHookParam);
                }
            });
            XposedHelpers.findAndHookMethod("com.alibaba.lightapp.runtime.plugin.internal.Util", classLoader, "getLBSWua", ActionRequest, new XC_MethodHook(){

                @Override
                public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    super.afterHookedMethod(methodHookParam);
                    VLog.d("HV-DingTalk", "查看返回:" + methodHookParam.getResult(), new Object[0]);
                    VLog.printStackTrace("getLBSWua");
                    methodHookParam.setResult(null);
                }

                @Override
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    methodHookParam.setResult(null);
                    super.beforeHookedMethod(methodHookParam);
                }
            });
            Class<?> apiPermissionInfo = XposedHelpers.findClass("com.alibaba.ariver.permission.model.ApiPermissionInfo", classLoader);
            Class<?> apiPermissionCheckResult = XposedHelpers.findClass("com.alibaba.ariver.kernel.api.security.ApiPermissionCheckResult", classLoader);
            final Object[] enumConstants = apiPermissionCheckResult.getEnumConstants();
            XposedHelpers.findAndHookMethod("com.alibaba.ariver.permission.service.DefaultAuthenticationProxyImpl", classLoader, "hasPermission", apiPermissionInfo, String.class, String.class, new XC_MethodHook(){

                @Override
                public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    super.afterHookedMethod(methodHookParam);
                }

                @Override
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    methodHookParam.setResult(enumConstants[0]);
                }
            });
        }
        catch (Throwable throwable) {
            Log.e((String)TAG, (String)("DingTalk hook exception: " + throwable.toString()));
            HVLog.printThrowable(throwable);
        }
    }

    private static void hookInterface(ClassLoader classLoader, Object pthis) {
        try {
            Class<?> previewCallback = classLoader.loadClass("android.hardware.Camera$PreviewCallback");
            Object obj_proxy = Proxy.newProxyInstance(classLoader, new Class[]{previewCallback}, new InvocationHandler(){

                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    HVLog.i("------------- method " + method.getName());
                    return null;
                }
            });
            HVLog.i("method ----------------------------------------- ");
            XposedHelpers.callMethod(pthis, "a", obj_proxy);
        }
        catch (NoClassDefFoundError fe) {
            HVLog.i("fe " + fe.getMessage());
        }
        catch (Exception e) {
            HVLog.i("e " + e.getMessage());
        }
    }
}


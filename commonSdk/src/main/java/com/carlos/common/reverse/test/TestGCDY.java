package com.carlos.common.reverse.test;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.carlos.common.utils.LogUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class TestGCDY {

    public static void hook(final ClassLoader classLoader, final Application application) {
        LogUtil.d("classLoader=>" + classLoader + " application=>" + application);

        // hook所有Activity的onResume方法
        try {
            XposedHelpers.findAndHookMethod(
                    Activity.class,
                    "onResume",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            // param.thisObject 为当前的Activity实例
                            Activity activity = (Activity) param.thisObject;
                            LogUtil.d("TestGCDY: " + activity.getClass().getName() + " onResume() before");
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Activity activity = (Activity) param.thisObject;
                            LogUtil.d("TestGCDY: " + activity.getClass().getName() + " onResume() after");
                        }
                    }
            );
            LogUtil.d("TestGCDY: Hooked Activity.onResume done");
        } catch (Throwable t) {
            LogUtil.d("TestGCDY: Hook failed " + t);
        }
    }
}
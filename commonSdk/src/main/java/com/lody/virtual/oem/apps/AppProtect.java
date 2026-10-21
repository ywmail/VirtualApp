/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.oem.apps;

import com.lody.virtual.StringFog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.util.ArrayList;

public class AppProtect {
    public static void handleForAppProject(String packageName) {
        try {
            XposedHelpers.findAndHookMethod("dalvik.system.VMStack", AppProtect.class.getClassLoader(), "getThreadStackTrace", Thread.class, new XC_MethodHook(){

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    StackTraceElement[] ret = (StackTraceElement[])param.getResult();
                    ArrayList<StackTraceElement> list = new ArrayList<StackTraceElement>();
                    for (StackTraceElement element : ret) {
                        if (element.toString().contains("sandhook") || !element.toString().contains("android.app.LoadedApk.makeApplication")) {
                            continue;
                        }
                        list.add(element);
                        StackTraceElement stackTraceElement = new StackTraceElement("android.app.ActivityThread", "handleBindApplication", "ActivityThread.java", 6963);
                        list.add(stackTraceElement);
                        StackTraceElement stackTraceElement2 = new StackTraceElement("android.app.ActivityThread", "access$1500", "ActivityThread.java", 258);
                        list.add(stackTraceElement2);
                        StackTraceElement stackTraceElement3 = new StackTraceElement("android.app.ActivityThread$H", "handleMessage", "ActivityThread.java", 1983);
                        list.add(stackTraceElement3);
                        StackTraceElement stackTraceElement4 = new StackTraceElement("android.os.Handler", "dispatchMessage", "Handler.java", 106);
                        list.add(stackTraceElement4);
                        StackTraceElement stackTraceElement5 = new StackTraceElement("android.os.Looper", "loop", "Looper.java", 236);
                        list.add(stackTraceElement5);
                        StackTraceElement stackTraceElement6 = new StackTraceElement("android.app.ActivityThread", "main", "ActivityThread.java", 8060);
                        list.add(stackTraceElement6);
                        StackTraceElement stackTraceElement7 = new StackTraceElement("java.lang.reflect.Method", "invoke", "", -2);
                        list.add(stackTraceElement7);
                        StackTraceElement stackTraceElement8 = new StackTraceElement("com.android.internal.os.RuntimeInit$MethodAndArgsCaller", "run", "RuntimeInit.java", 656);
                        list.add(stackTraceElement8);
                        StackTraceElement stackTraceElement9 = new StackTraceElement("com.android.internal.os.ZygoteInit", "main", "ZygoteInit.java", 976);
                        list.add(stackTraceElement9);
                        break;
                    }
                    param.setResult(list.toArray());
                }
            });
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}


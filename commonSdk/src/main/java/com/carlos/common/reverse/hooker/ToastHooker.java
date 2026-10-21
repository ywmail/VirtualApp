/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  android.widget.Toast
 */
package com.carlos.common.reverse.hooker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.kook.librelease.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;
import java.util.Map;

@HookClass(value=Toast.class)
public class ToastHooker {
    @HookMethodBackup(value="show")
    static Method method_m1;
    @HookMethodBackup(value="makeText")
    @MethodParams(value={Context.class, CharSequence.class, int.class})
    static Method method_m2;

    public static void e() {
        Thread.currentThread();
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        if (map != null && map.size() != 0) {
            for (Thread eachThread : map.keySet()) {
                StackTraceElement[] array = map.get(eachThread);
                System.out.println("------每个线程的基本信息");
                System.out.println(" 线程名称：" + eachThread.getName());
                System.out.println(" StackTraceElement[].length=" + array.length);
                System.out.println(" 线程的状态：" + (Object)((Object)eachThread.getState()));
                if (array.length != 0) {
                    System.out.println(" 输出StackTraceElement[]数组具体信息：");
                    for (int i = 0; i < array.length; ++i) {
                        StackTraceElement eachElement = array[i];
                        System.out.println(" " + eachElement.getClassName() + " " + eachElement.getMethodName() + " " + eachElement.getFileName() + " " + eachElement.getLineNumber());
                    }
                    continue;
                }
                System.out.println(" 没有StackTraceElement[]信息，因为线程" + eachThread.getName() + "中的stackTraceElement[].length==" + array.length);
            }
        }
    }

    @HookMethod(value="makeText")
    @MethodParams(value={Context.class, CharSequence.class, int.class})
    public static Toast makeText(Context context, CharSequence cs, int d) throws Throwable {
        Log.d((String)"ToastHooker", (String)("makeText:" + cs.toString() + " packageName:" + context.getPackageName() + "Thread:" + Thread.currentThread().getClass().getName()));
        ToastHooker.e();
        return (Toast)SandHook.callOriginByBackup(method_m2, null, context, cs, d);
    }

    @HookMethod(value="show")
    public static void show(Toast thiz) throws Throwable {
        try {
            Log.d((String)"ToastHooker", (String)" show");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        SandHook.callOriginByBackup(method_m1, thiz, new Object[0]);
    }
}


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.sandxposed;

import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.swift.sandhook.xposedcompat.utils.FileUtils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.util.Arrays;

public class EnvironmentSetup {
    public EnvironmentSetup() {
    }

    public static void init(Context context, String packageName, String processName) {
        initSystemProp(context);
        initForWeChat(context, processName);
    }

    private static void initSystemProp(Context context) {
        System.setProperty("vxp", "1");
        System.setProperty("vxp_user_dir", (new File(context.getApplicationInfo().dataDir)).getParent());
        System.setProperty("sandvxp", "1");
    }

    private static void initForWeChat(Context context, String processName) {
        if (TextUtils.equals("com.tencent.mm", processName)) {
            File dataDir = new File(context.getApplicationInfo().dataDir);
            File tinker = new File(dataDir, "tinker");
            File tinker_temp = new File(dataDir, "tinker_temp");
            File tinker_server = new File(dataDir, "tinker_server");

            try {
                FileUtils.delete(tinker);
                FileUtils.delete(tinker_temp);
                FileUtils.delete(tinker_server);
            } catch (Exception var7) {
            }

            final int mainProcessId = Process.myPid();
            XposedHelpers.findAndHookMethod(Process.class, "killProcess", new Object[]{Integer.TYPE, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    int pid = (Integer)param.args[0];
                    if (pid == mainProcessId) {
                        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                        if (stackTrace != null) {
                            StackTraceElement[] var4 = stackTrace;
                            int var5 = stackTrace.length;

                            for(int var6 = 0; var6 < var5; ++var6) {
                                StackTraceElement stackTraceElement = var4[var6];
                                if (stackTraceElement.getClassName().contains("com.tencent.mm.app")) {
                                    XposedBridge.log("do not suicide..." + Arrays.toString(stackTrace));
                                    param.setResult((Object)null);
                                    break;
                                }
                            }

                        }
                    }
                }
            }});
        }
    }
}

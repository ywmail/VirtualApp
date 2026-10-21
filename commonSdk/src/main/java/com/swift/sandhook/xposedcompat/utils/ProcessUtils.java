//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.swift.sandhook.xposedcompat.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProcessUtils {
    private static volatile String processName = null;

    public ProcessUtils() {
    }

    public static String getProcessName(Context context) {
        if (!TextUtils.isEmpty(processName)) {
            return processName;
        } else {
            processName = doGetProcessName(context);
            return processName;
        }
    }

    private static String doGetProcessName(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        } else {
            Iterator var3 = runningApps.iterator();

            ActivityManager.RunningAppProcessInfo proInfo;
            do {
                if (!var3.hasNext()) {
                    return context.getPackageName();
                }

                proInfo = (ActivityManager.RunningAppProcessInfo)var3.next();
            } while(proInfo.pid != Process.myPid() || proInfo.processName == null);

            return proInfo.processName;
        }
    }

    public static boolean isMainProcess(Context context) {
        String processName = getProcessName(context);
        String pkgName = context.getPackageName();
        return TextUtils.isEmpty(processName) || TextUtils.equals(processName, pkgName);
    }

    public static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri)null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        mainIntent.setPackage(packageName);
        List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        return (List)(apps != null ? apps : new ArrayList());
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.Signature
 *  com.alibaba.fastjson.JSONArray
 *  com.alibaba.fastjson.JSONObject
 */
package com.kook.deviceinfo.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kook.common.utils.HVLog;

public class AppInfoHelper {
    private static final String TAG = "AppInfoHelper";

    public static void printAppInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 128);
            String appName = packageManager.getApplicationLabel(appInfo).toString();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String appVersionName = packageInfo.versionName;
            int appVersionCode = packageInfo.versionCode;
            long installTime = packageInfo.firstInstallTime;
            boolean isSystem = (appInfo.flags & 1) != 0;
            String packageSign = AppInfoHelper.getSignature(packageManager, packageName);
            packageName = packageInfo.packageName;
            int minSdkVersion = appInfo.targetSdkVersion;
            HVLog.d("App Name: " + appName);
            HVLog.d("App Version Name: " + appVersionName);
            HVLog.d("App Version Code: " + appVersionCode);
            HVLog.d("Installation Time: " + installTime);
            HVLog.d("Is System App: " + isSystem);
            HVLog.d("Package Name: " + packageName);
            HVLog.d("Minimum SDK Version: " + minSdkVersion);
            HVLog.d("Package Signature: " + packageSign);
        }
        catch (PackageManager.NameNotFoundException e) {
            HVLog.d("Error getting app info: " + e.getMessage());
        }
    }

    private static String getSignature(PackageManager packageManager, String packageName) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 64);
            if (packageInfo.signatures.length > 0) {
                JSONArray jsonArray = new JSONArray();
                for (Signature signature : packageInfo.signatures) {
                    jsonArray.add((Object)signature.toCharsString());
                }
                return jsonArray.toJSONString();
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            HVLog.d("Error getting package signature: " + e.getMessage());
        }
        return null;
    }

    public static void getAppInfo(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String appName = packageManager.getApplicationLabel(applicationInfo).toString();
            int appVersionCode = packageInfo.versionCode;
            String appVersionName = packageInfo.versionName;
            long installTime = packageInfo.firstInstallTime;
            boolean isSystem = (applicationInfo.flags & 1) != 0;
            int minSdkVersion = applicationInfo.targetSdkVersion;
            AppInfoHelper.getSignature(context.getPackageManager(), packageName);
            int flag = applicationInfo.flags;
            long updateTime = packageInfo.lastUpdateTime;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appName", (Object)appName);
            jsonObject.put("appVersionCode", (Object)appVersionCode);
            jsonObject.put("appVersionName", (Object)appVersionName);
            jsonObject.put("flag", (Object)flag);
            jsonObject.put("installTime", (Object)installTime);
            jsonObject.put("isSystem", (Object)isSystem);
            jsonObject.put("minSdkVersion", (Object)minSdkVersion);
            jsonObject.put("packageName", (Object)packageName);
            jsonObject.put("updateTime", (Object)updateTime);
            AppInfoHelper.getSignature(packageManager, packageName);
            String string2 = jsonObject.toJSONString();
        }
        catch (PackageManager.NameNotFoundException e) {
            HVLog.d("Package not found: " + e.getMessage());
        }
    }

    public static String getJsonToAppInfo(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String appName = packageManager.getApplicationLabel(applicationInfo).toString();
            int appVersionCode = packageInfo.versionCode;
            String appVersionName = packageInfo.versionName;
            long installTime = packageInfo.firstInstallTime;
            boolean isSystem = (applicationInfo.flags & 1) != 0;
            int minSdkVersion = applicationInfo.targetSdkVersion;
            int flag = applicationInfo.flags;
            long updateTime = packageInfo.lastUpdateTime;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appName", (Object)appName);
            jsonObject.put("appVersionCode", (Object)appVersionCode);
            jsonObject.put("appVersionName", (Object)appVersionName);
            jsonObject.put("flag", (Object)flag);
            jsonObject.put("installTime", (Object)installTime);
            jsonObject.put("isSystem", (Object)isSystem);
            jsonObject.put("minSdkVersion", (Object)minSdkVersion);
            jsonObject.put("packageName", (Object)packageName);
            jsonObject.put("updateTime", (Object)updateTime);
            String signature = AppInfoHelper.getSignature(packageManager, packageName);
            jsonObject.put("signature", (Object)signature);
            String jsonString = jsonObject.toJSONString();
            return jsonString;
        }
        catch (PackageManager.NameNotFoundException e) {
            HVLog.d("Package not found: " + e.getMessage());
            return null;
        }
    }
}


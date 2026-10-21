/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.provider.Settings$Secure
 *  com.alibaba.fastjson.JSONObject
 */
package com.kook.deviceinfo.data;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import com.alibaba.fastjson.JSONObject;
import com.kook.common.secret.MD5Utils;
import com.kook.common.utils.HVLog;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class DeviceInfo {
    private static String META_DATA_KEY = "ROM_MOCK_SDK";
    private static String SOFT_ID = "softId";
    static DeviceInfo deviceInfo;
    JSONObject mElement = new JSONObject();
    HashMap<String, String> mHashMap = new LinkedHashMap<String, String>(16);
    private String terminalDevicesSoftID = "terminalDevicesSoftID";
    public int deviceWidth;
    public int deviceHeight;
    public String imei;
    public String board = Build.BOARD;
    public String bootloader = Build.BOOTLOADER;
    public String brand = Build.BRAND;
    public String cpuAbi = Build.CPU_ABI;
    public String cpuAbi2 = Build.CPU_ABI2;
    public String device = Build.DEVICE;
    public String display = Build.DISPLAY;
    public String fingerprint = Build.FINGERPRINT;
    public String hardware = Build.HARDWARE;
    public String host = Build.HOST;
    public String device_id = Build.ID;
    public String model = Build.MODEL;
    public String manufacturer = Build.MANUFACTURER;
    public String product = Build.PRODUCT;
    public String radio = Build.RADIO;
    public String tags = Build.TAGS;
    public long time = Build.TIME;
    public String type = Build.TYPE;
    public String user = Build.USER;
    public String release = Build.VERSION.RELEASE;
    public String codename = Build.VERSION.CODENAME;
    public String incremental = Build.VERSION.INCREMENTAL;
    public String sdk = Build.VERSION.SDK;
    public int sdkInt = Build.VERSION.SDK_INT;
    public String serial = Build.SERIAL;
    public String deviceDefaultLanguage;
    public int versionCode;
    public String channel;
    public String softId;
    public String androidId;

    public static DeviceInfo getInstance(Context context) {
        if (deviceInfo == null) {
            deviceInfo = new DeviceInfo(context);
        }
        return deviceInfo;
    }

    private DeviceInfo(Context context) {
        this.deviceWidth = DeviceInfo.getDeviceWidth(context);
        this.deviceHeight = DeviceInfo.getDeviceHeight(context);
        this.imei = DeviceInfo.getIMEI(context);
        this.deviceDefaultLanguage = DeviceInfo.getDeviceDefaultLanguage();
        this.versionCode = this.getVersionCode(context);
        this.channel = this.getMetaDataFromApp(context, META_DATA_KEY);
        this.softId = this.getMetaDataFromApp(context, SOFT_ID);
        this.androidId = DeviceInfo.getAndroidId(context);
    }

    public int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = 23202;
            return versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String versionName = packInfo.versionName;
            return versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getMetaDataFromApp(Context context, String meta) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            value = appInfo.metaData.getString(meta);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            HVLog.d("getMetaDataFromApp " + e.toString());
        }
        return value;
    }

    public String getSoftId(Context context) {
        String metaDataFromApp = this.getMetaDataFromApp(context, SOFT_ID);
        return metaDataFromApp.replace("softId-", "");
    }

    public static int getDeviceWidth(Context context) {
        try {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        catch (Exception e) {
            HVLog.printException(e);
            return 0;
        }
    }

    public static int getDeviceHeight(Context context) {
        try {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        catch (Exception e) {
            HVLog.printException(e);
            return 0;
        }
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString((ContentResolver)context.getContentResolver(), (String)"android_id");
    }

    public static String getIMEI(Context context) {
        return "";
    }

    public static String getDeviceDefaultLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public String getDevicesNo() {
        HashMap<String, String> hashMap = this.toMap();
        String devicesSoftID = "";
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (entry.getKey().equals(this.terminalDevicesSoftID)) continue;
            devicesSoftID = devicesSoftID + entry.getValue();
        }
        String decrypt = MD5Utils.md5Encode(devicesSoftID);
        return decrypt;
    }

    public HashMap<String, String> toMap() {
        this.mHashMap.put("deviceWidth", String.valueOf(this.deviceWidth));
        this.mHashMap.put("deviceHeight", String.valueOf(this.deviceHeight));
        this.mHashMap.put("imei", this.checkString(this.imei));
        this.mHashMap.put("board", this.checkString(this.board));
        this.mHashMap.put("bootloader", this.checkString(this.bootloader));
        this.mHashMap.put("brand", this.checkString(this.brand));
        this.mHashMap.put("cpuAbi", this.checkString(this.cpuAbi));
        this.mHashMap.put("cpuAbi2", this.checkString(this.cpuAbi2));
        this.mHashMap.put("device", this.checkString(this.device));
        this.mHashMap.put("display", this.checkString(this.display));
        this.mHashMap.put("fingerprint", this.checkString(this.fingerprint));
        this.mHashMap.put("hardware", this.checkString(this.hardware));
        this.mHashMap.put("host", this.checkString(this.host));
        this.mHashMap.put("device_id", this.checkString(this.device_id));
        this.mHashMap.put("model", this.checkString(this.model));
        this.mHashMap.put("manufacturer", this.checkString(this.manufacturer));
        this.mHashMap.put("product", this.product);
        this.mHashMap.put("radio", this.checkString(this.radio));
        this.mHashMap.put("tags", this.checkString(this.tags));
        this.mHashMap.put("time", String.valueOf(this.time));
        this.mHashMap.put("type", this.type);
        this.mHashMap.put("user", this.user);
        this.mHashMap.put("releaseVersion", this.release);
        this.mHashMap.put("codename", this.codename);
        this.mHashMap.put("incremental", this.incremental);
        this.mHashMap.put("sdk", this.sdk);
        this.mHashMap.put("sdkInt", String.valueOf(this.sdkInt));
        this.mHashMap.put("serial", this.serial);
        this.mHashMap.put("deviceDefaultLanguage", this.deviceDefaultLanguage);
        this.mHashMap.put("versionCode", String.valueOf(this.versionCode));
        this.mHashMap.put("channel", this.checkString(this.channel));
        this.mHashMap.put("android_id", this.androidId);
        return this.mHashMap;
    }

    public String toJSON() {
        this.elementAddProperty("deviceWidth", this.deviceWidth);
        this.elementAddProperty("deviceHeight", this.deviceHeight);
        this.elementAddProperty("imei", this.checkString(this.imei));
        this.elementAddProperty("board", this.checkString(this.board));
        this.elementAddProperty("bootloader", this.checkString(this.bootloader));
        this.elementAddProperty("brand", this.checkString(this.brand));
        this.elementAddProperty("cpuAbi", this.checkString(this.cpuAbi));
        this.elementAddProperty("cpuAbi2", this.checkString(this.cpuAbi2));
        this.elementAddProperty("device", this.checkString(this.device));
        this.elementAddProperty("display", this.checkString(this.display));
        this.elementAddProperty("fingerprint", this.checkString(this.fingerprint));
        this.elementAddProperty("hardware", this.checkString(this.hardware));
        this.elementAddProperty("host", this.checkString(this.host));
        this.elementAddProperty("device_id", this.checkString(this.device_id));
        this.elementAddProperty("model", this.checkString(this.model));
        this.elementAddProperty("manufacturer", this.checkString(this.manufacturer));
        this.elementAddProperty("product", this.product);
        this.elementAddProperty("radio", this.checkString(this.radio));
        this.elementAddProperty("tags", this.checkString(this.tags));
        this.elementAddProperty("time", this.time);
        this.elementAddProperty("type", this.type);
        this.elementAddProperty("user", this.user);
        this.elementAddProperty("release", this.release);
        this.elementAddProperty("codename", this.codename);
        this.elementAddProperty("incremental", this.incremental);
        this.elementAddProperty("sdk", this.sdk);
        this.elementAddProperty("sdkInt", this.sdkInt);
        this.elementAddProperty("serial", this.serial);
        this.elementAddProperty("deviceDefaultLanguage", this.deviceDefaultLanguage);
        this.elementAddProperty("versionCode", this.versionCode);
        this.elementAddProperty("channel", this.checkString(this.channel));
        this.elementAddProperty("android_id", this.checkString(this.androidId));
        return this.mElement.toString();
    }

    private void elementAddProperty(String property, Object object) {
        if (this.mElement.containsKey((Object)property)) {
            this.mElement.remove((Object)property);
        }
        if (object instanceof String) {
            this.mElement.put(property, (Object)((String)object));
        } else if (object instanceof Integer) {
            this.mElement.put(property, (Object)((Integer)object));
        } else if (object instanceof Boolean) {
            this.mElement.put(property, (Object)((Boolean)object));
        } else if (object instanceof Long) {
            this.mElement.put(property, (Object)((Long)object));
        } else {
            throw new NullPointerException(property + " :   " + object + "\u4e0d\u80fd\u8f6c\u6210json \u683c\u5f0f\u6570\u636e");
        }
    }

    public String checkString(String atrr) {
        if (atrr == null) {
            return "";
        }
        return atrr;
    }
}


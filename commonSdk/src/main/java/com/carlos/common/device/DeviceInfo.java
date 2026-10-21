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
 *  android.text.TextUtils
 *  androidx.annotation.RequiresApi
 *  com.alibaba.fastjson.JSONObject
 */
package com.carlos.common.device;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import com.alibaba.fastjson.JSONObject;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import com.kook.network.secret.MD5Utils;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class DeviceInfo {
    private static String SOFT_CHANNEL_NO = "SOFT_CHANNEL_NO";
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
    public String type = Build.TYPE;
    public String user = Build.USER;
    public int sdkInt = Build.VERSION.SDK_INT;
    public String serial = Build.SERIAL;
    public int versionCode;
    public String androidId;
    public String channel;
    public String softId;
    public String applicationName;

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
        this.versionCode = this.getVersionCode(context);
        this.channel = this.getMetaDataFromApp(context, SOFT_CHANNEL_NO);
        this.softId = this.getMetaDataFromApp(context, SOFT_ID);
        this.androidId = DeviceInfo.getAndroidId(context);
        this.applicationName = this.getApplicationName(context);
    }

    private String getApplicationName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        ApplicationInfo appInfo = null;
        try {
            appInfo = packageManager.getApplicationInfo(packageName, 128);
        }
        catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String appName = packageManager.getApplicationLabel(appInfo).toString();
        return appName;
    }

    public String getApplicationName() {
        return this.applicationName;
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
        if (TextUtils.isEmpty((CharSequence)metaDataFromApp)) {
            throw new NullPointerException("请在Androidmainfest中添加 softid");
        }
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

    public String getChannelNo() {
        return this.channel;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    @RequiresApi(api=26)
    public long getCurrentTimestamp() {
        ZoneId beijingZoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime beijingDateTime = ZonedDateTime.now(beijingZoneId);
        long timestampInSeconds = beijingDateTime.toEpochSecond();
        return timestampInSeconds;
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
        this.mHashMap.put("type", this.type);
        this.mHashMap.put("user", this.user);
        this.mHashMap.put("sdkInt", String.valueOf(this.sdkInt));
        this.mHashMap.put("serial", this.serial);
        this.mHashMap.put("android_id", this.androidId);
        return this.mHashMap;
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
            throw new NullPointerException(property + " :   " + object + "不能转成json 格式数据");
        }
    }

    public String checkString(String atrr) {
        if (atrr == null) {
            return "";
        }
        return atrr;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 */
package com.carlos.common.ui.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carlos.common.App;
import com.carlos.common.ui.activity.base.BaseActivity;
import com.carlos.common.utils.FileTools;
import com.carlos.common.utils.SPTools;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.client.ipc.VLocationManager;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.remote.vloc.VLocation;

public class MirrorDataParse {
    JSONObject mElement = new JSONObject();

    public String getBackupData(String packageName, int userId) {
        this.mElement.clear();
        this.elementAddProperty("packageName", packageName);
        this.elementAddProperty("userId", userId);
        SettingConfig.FakeWifiStatus fakeWifiStatus = App.getApp().mConfig.getFakeWifiStatus(packageName, userId);
        this.elementAddProperty("ssid", fakeWifiStatus == null ? "" : fakeWifiStatus.getSSID());
        this.elementAddProperty("mac", fakeWifiStatus == null ? "" : fakeWifiStatus.getMAC());
        this.elementAddProperty("bssid", fakeWifiStatus == null ? "" : fakeWifiStatus.getBSSID());
        VLocation location = VLocationManager.get().getLocation(packageName, userId);
        if (location != null) {
            this.elementAddProperty("latitude", location.latitude);
            this.elementAddProperty("longitude", location.longitude);
            this.elementAddProperty("altitude", location.altitude);
            this.elementAddProperty("accuracy", Float.valueOf(location.accuracy));
            this.elementAddProperty("speed", Float.valueOf(location.speed));
            this.elementAddProperty("bearing", Float.valueOf(location.bearing));
        }
        int deviceId = BaseActivity.getDeviceId(packageName, userId);
        VDeviceConfig deviceConfig = VDeviceManager.get().getDeviceConfig(deviceId);
        boolean enable = VDeviceManager.get().isEnable(deviceId);
        if (enable) {
            this.elementAddProperty("BRAND", deviceConfig.getProp("BRAND"));
            this.elementAddProperty("MODEL", deviceConfig.getProp("MODEL"));
            this.elementAddProperty("PRODUCT", deviceConfig.getProp("PRODUCT"));
            this.elementAddProperty("DEVICE", deviceConfig.getProp("DEVICE"));
            this.elementAddProperty("BOARD", deviceConfig.getProp("BOARD"));
            this.elementAddProperty("DISPLAY", deviceConfig.getProp("DISPLAY"));
            this.elementAddProperty("ID", deviceConfig.getProp("ID"));
            this.elementAddProperty("MANUFACTURER", deviceConfig.getProp("MANUFACTURER"));
            this.elementAddProperty("FINGERPRINT", deviceConfig.getProp("FINGERPRINT"));
            this.elementAddProperty("serial", deviceConfig.serial);
            this.elementAddProperty("deviceId", deviceConfig.deviceId);
            this.elementAddProperty("iccId", deviceConfig.iccId);
            this.elementAddProperty("wifiMac", deviceConfig.wifiMac);
            this.elementAddProperty("androidId", deviceConfig.androidId);
        } else {
            this.elementAddProperty("deviceIdEnable", "false");
        }
        return this.mElement.toString();
    }

    public void parseBackupData(String filePath) {
        String readFile = FileTools.readFile(filePath);
        HVLog.d("readFile:" + readFile + "    filePath:" + filePath);
        JSONObject jsonObject = JSON.parseObject((String)readFile);
        if (jsonObject == null) {
            HVLog.d("还原数据异常");
            return;
        }
        String packageName = this.getPropertyString(jsonObject, "packageName");
        int userId = this.getPropertyInt(jsonObject, "userId");
        String ssid = this.getPropertyString(jsonObject, "ssid");
        String mac = this.getPropertyString(jsonObject, "mac");
        String bssid = this.getPropertyString(jsonObject, "bssid");
        String SSID_KEY = "ssid_key" + packageName + "_" + userId;
        String MAC_KEY = "mac_key" + packageName + "_" + userId;
        SPTools.putString(VirtualCore.get().getContext(), SSID_KEY, ssid);
        SPTools.putString(VirtualCore.get().getContext(), MAC_KEY, mac);
        if (jsonObject.containsKey((Object)"latitude") || jsonObject.containsKey((Object)"longitude")) {
            VLocation mLatLng = new VLocation();
            mLatLng.latitude = this.getPropertyInt(jsonObject, "latitude");
            mLatLng.longitude = this.getPropertyInt(jsonObject, "longitude");
            mLatLng.altitude = this.getPropertyInt(jsonObject, "altitude");
            mLatLng.accuracy = this.getPropertyInt(jsonObject, "accuracy");
            mLatLng.speed = this.getPropertyInt(jsonObject, "speed");
            mLatLng.bearing = this.getPropertyInt(jsonObject, "bearing");
            VirtualLocationManager.get().setMode(userId, packageName, 2);
            VirtualLocationManager.get().setLocation(userId, packageName, mLatLng);
        }
        int deviceId = BaseActivity.getDeviceId(packageName, userId);
        VDeviceConfig deviceConfig = VDeviceManager.get().getDeviceConfig(deviceId);
        if (!jsonObject.containsKey((Object)"deviceIdEnable")) {
            deviceConfig.setProp("BRAND", this.getPropertyString(jsonObject, "BRAND"));
            deviceConfig.setProp("MODEL", this.getPropertyString(jsonObject, "MODEL"));
            deviceConfig.setProp("PRODUCT", this.getPropertyString(jsonObject, "PRODUCT"));
            deviceConfig.setProp("DEVICE", this.getPropertyString(jsonObject, "DEVICE"));
            deviceConfig.setProp("BOARD", this.getPropertyString(jsonObject, "BOARD"));
            deviceConfig.setProp("DISPLAY", this.getPropertyString(jsonObject, "DISPLAY"));
            deviceConfig.setProp("ID", this.getPropertyString(jsonObject, "ID"));
            deviceConfig.setProp("MANUFACTURER", this.getPropertyString(jsonObject, "MANUFACTURER"));
            deviceConfig.setProp("FINGERPRINT", this.getPropertyString(jsonObject, "FINGERPRINT"));
            deviceConfig.serial = this.getPropertyString(jsonObject, "serial");
            deviceConfig.deviceId = this.getPropertyString(jsonObject, "deviceId");
            deviceConfig.iccId = this.getPropertyString(jsonObject, "iccId");
            deviceConfig.wifiMac = this.getPropertyString(jsonObject, "wifiMac");
            deviceConfig.androidId = this.getPropertyString(jsonObject, "androidId");
            VDeviceManager.get().updateDeviceConfig(deviceId, deviceConfig);
        }
        HVLog.d("还原数据完成");
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
        } else if (object instanceof Double) {
            this.mElement.put(property, (Object)((Double)object));
        } else if (object instanceof Float) {
            this.mElement.put(property, (Object)((Float)object));
        } else {
            throw new NullPointerException(property + " :   " + object + "不能转成json 格式数据");
        }
    }

    private String getPropertyString(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey((Object)key)) {
            return jsonObject.getString(key);
        }
        return "";
    }

    private int getPropertyInt(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey((Object)key)) {
            return jsonObject.getIntValue(key);
        }
        return -1;
    }

    private long getPropertyLong(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey((Object)key)) {
            return jsonObject.getLongValue(key);
        }
        return -1L;
    }

    private boolean getPropertyBoolean(JSONObject jsonObject, String key) {
        if (jsonObject.containsKey((Object)key)) {
            return jsonObject.getBooleanValue(key);
        }
        return false;
    }
}


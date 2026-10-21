//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kook.deviceinfo.impClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaDrm;
import android.os.Build;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.InputDevice;
import android.webkit.WebSettings;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kook.common.jni.FileInfoNative;
import com.kook.common.systemutil.SystemManager;
import com.kook.common.util.FileTools;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.constant.BuildInfoConstant;
import com.kook.deviceinfo.constant.SystemFileConStant;
import com.kook.deviceinfo.constant.BuildInfoConstant.Battery;
import com.kook.deviceinfo.constant.BuildInfoConstant.Camera;
import com.kook.deviceinfo.constant.BuildInfoConstant.FingerprintInfo;
import com.kook.deviceinfo.constant.BuildInfoConstant.GeneralDataInfo;
import com.kook.deviceinfo.constant.BuildInfoConstant.Graphics;
import com.kook.deviceinfo.constant.BuildInfoConstant.InputInfo;
import com.kook.deviceinfo.constant.BuildInfoConstant.JavaProperty;
import com.kook.deviceinfo.constant.BuildInfoConstant.SensorInfo;
import com.kook.deviceinfo.constant.BuildInfoConstant.SettingsGlobalProperty;
import com.kook.deviceinfo.constant.BuildInfoConstant.SettingsSecureProperty;
import com.kook.deviceinfo.constant.BuildInfoConstant.SettingsSystemProperty;
import com.kook.deviceinfo.constant.BuildInfoConstant.StorageSpace;
import com.kook.deviceinfo.constant.BuildInfoConstant.SystemAppInfo;
import com.kook.deviceinfo.constant.BuildInfoConstant.SystemAvailableFeatures;
import com.kook.deviceinfo.constant.BuildInfoConstant.SystemFileInfo;
import com.kook.deviceinfo.constant.BuildInfoConstant.SystemInputDevice;
import com.kook.deviceinfo.constant.BuildInfoConstant.SystemMediaCodecList;
import com.kook.deviceinfo.constant.BuildInfoConstant.SystemMediaDrm;
import com.kook.deviceinfo.constant.BuildInfoConstant.SystemProperty;
import com.kook.deviceinfo.constant.BuildInfoConstant.Telephony;
import com.kook.deviceinfo.constant.BuildInfoConstant.UserAgent;
import com.kook.deviceinfo.data.GeneralData;
import com.kook.deviceinfo.models.InputModel;
import com.kook.deviceinfo.models.SensorListModel;
import com.kook.deviceinfo.persistence.IniFile;
import com.kook.deviceinfo.util.AppInfoHelper;
import com.kook.librelease.R.string;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import mirror.RefClassAttr;

public class ExportDetails {
    private final Context context;
    BuildInfo buildInfo;
    CameraManager cameraManager;
    String[] cameraIds;
    JsonData jsonData;
    ArrayList<SensorListModel> sensorListModels;
    ArrayList<InputModel> inputListModels;
    private final IniFile mIniFile;
    private boolean isSystemSign;

    public ExportDetails(Context context) {
        this.mIniFile = IniFile.getInstance(IniFile.SYSTEM_EXPORT_CONFIG);
        this.isSystemSign = false;
        this.context = context;
        this.buildInfo = new BuildInfo(context);
        this.jsonData = new JsonData(context);
        if (SystemManager.isSystemSign(context)) {
            this.isSystemSign = true;
        } else {
            this.isSystemSign = false;
        }

    }

    public IniFile getIniFile() {
        return this.mIniFile;
    }

    public void setSensorList(ArrayList<SensorListModel> sensorList) {
        this.sensorListModels = sensorList;
    }

    public void setInputList(ArrayList<InputModel> inputList) {
        this.inputListModels = inputList;
    }

    @SuppressLint({"HardwareIds"})
    public void device() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        dateFormat.format(date);
        String model = this.context.getResources().getString(string.model) + " : ";
        String manufacturer = this.context.getResources().getString(string.manu) + " : ";
        String device = this.context.getResources().getString(string.device_lav) + " : ";
        String board = this.context.getResources().getString(string.board) + " : ";
        String hardware = this.context.getResources().getString(string.hardware) + " : ";
        String device_id = this.context.getResources().getString(string.device_id) + " : ";
        String deviceType = this.context.getResources().getString(string.devicetype) + " : ";
        String buildprint = this.context.getResources().getString(string.build_print) + " : ";
        String usbhost = this.context.getResources().getString(string.usb_host) + " : ";
        String macadd = this.context.getResources().getString(string.mac_address) + " : ";
        String usbSupport = this.context.getResources().getString(string.supported);
        if (!this.context.getPackageManager().hasSystemFeature("android.hardware.usb.host")) {
            usbSupport = this.context.getResources().getString(string.not_supported);
        }

        String advertId = this.context.getResources().getString(string.advertid) + " : ";
        String timezone = this.context.getResources().getString(string.timezone) + " : ";
        SimpleDateFormat d = new SimpleDateFormat("z", Locale.getDefault());
        String time = d.format(System.currentTimeMillis());
        String t = TimeZone.getDefault().getDisplayName() + " (" + time + ")";
        this.mIniFile.set(Telephony.TYPE, Telephony.TELEPHONY_TYPE, this.buildInfo.getPhoneType());
    }

    public void deviceFeatures() {
        FeatureInfo[] featureInfo = this.context.getPackageManager().getSystemAvailableFeatures();
        FeatureInfo[] var2 = featureInfo;
        int var3 = featureInfo.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            FeatureInfo feature = var2[var4];
            this.mIniFile.set(SystemAvailableFeatures.TYPE, feature.name, JSON.toJSON(feature).toString());
        }

    }

    public void drmDetails() {
        String[] wide = new String[9];
        String[] clearkey = new String[2];

        MediaDrm mediaDrm;
        try {
            mediaDrm = new MediaDrm(UUID.fromString("edef8ba9-79d6-4ace-a3c8-27dcd51d21ed"));
            wide[0] = mediaDrm.getPropertyString("vendor");
            wide[1] = mediaDrm.getPropertyString("version");
            wide[2] = mediaDrm.getPropertyString("algorithms");
            wide[3] = mediaDrm.getPropertyString("systemId");
            wide[4] = mediaDrm.getPropertyString("securityLevel");
            wide[5] = mediaDrm.getPropertyString("maxHdcpLevel");
            wide[6] = mediaDrm.getPropertyString("maxNumberOfSessions");
            wide[7] = mediaDrm.getPropertyString("usageReportingSupport");
            wide[8] = mediaDrm.getPropertyString("hdcpLevel");
            this.mIniFile.set(SystemMediaDrm.TYPE, "vendor", wide[0]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "version", wide[1]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "algorithms", wide[2]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "systemId", wide[3]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "securityLevel", wide[4]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "maxHdcpLevel", wide[5]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "maxNumberOfSessions", wide[6]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "usageReportingSupport", wide[7]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "hdcpLevel", wide[8]);
            mediaDrm.release();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        try {
            mediaDrm = new MediaDrm(UUID.fromString("e2719d58-a985-b3c9-781a-b030af78d30e"));
            clearkey[0] = mediaDrm.getPropertyString("vendor");
            clearkey[1] = mediaDrm.getPropertyString("version");
            this.mIniFile.set(SystemMediaDrm.TYPE, "vendor", clearkey[0]);
            this.mIniFile.set(SystemMediaDrm.TYPE, "version", clearkey[1]);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public void system() {
        ActivityManager activityManager = (ActivityManager)this.context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo();
        this.mIniFile.set(Graphics.TYPE, Graphics.GL_ES_VERSION, info.getGlEsVersion());
        this.mIniFile.set(GeneralDataInfo.TYPE, GeneralDataInfo.LANGUAGE, Locale.getDefault().getDisplayLanguage());
    }

    public void cpu() {
        Iterator var1 = SystemFileConStant.SOC_FILE_LIST.iterator();

        String osArch;
        while(var1.hasNext()) {
            osArch = (String)var1.next();
            this.mIniFile.set(FingerprintInfo.TYPE, osArch, FileTools.readFile(osArch));
        }

        int coresCount = this.buildInfo.getCoresCount();

        for(int i = 0; i < coresCount; ++i) {
            String minfreq = "/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_min_freq";
            String maxfreq = "/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq";
            this.mIniFile.set(FingerprintInfo.TYPE, minfreq, FileTools.readFile(minfreq));
            this.mIniFile.set(FingerprintInfo.TYPE, maxfreq, FileTools.readFile(maxfreq));
        }

        osArch = System.getProperty("os.arch");
        this.mIniFile.set(GeneralDataInfo.TYPE, GeneralDataInfo.OS_ARCH, osArch);
        this.mIniFile.set(GeneralDataInfo.TYPE, GeneralDataInfo.SUPPORTED_ABIS, Arrays.toString(Build.SUPPORTED_ABIS));
    }

    public void battery() {
        this.mIniFile.set(Battery.TYPE, Battery.HEALTH, this.buildInfo.getBatteryHealth());
        this.mIniFile.set(Battery.TYPE, Battery.STATUS, this.buildInfo.getBatteryStatus());
        this.mIniFile.set(Battery.TYPE, Battery.LEVEL, this.buildInfo.getBatteryLevel());
        this.mIniFile.set(Battery.TYPE, Battery.VOLTAGE, this.buildInfo.getVoltage());
        this.mIniFile.set(Battery.TYPE, Battery.POWER_SOURCE, this.buildInfo.getPowerSource());
        this.mIniFile.set(Battery.TYPE, Battery.TECHNOLOGY, this.buildInfo.getBatteryTechnology());
        this.mIniFile.set(Battery.TYPE, Battery.CAPACITY, this.buildInfo.getBatteryCapacity());
    }

    @SuppressLint({"DefaultLocale"})
    public void display() {
        String fontscale = "";
        String size = "";
        String refreshrate = "";
        String hdr = "";
        String hdr_cap = "";
        String bright_level = "";
        String bright_mode = "";
        String screen_timeout = "";
        String orientation = "";
        String pixel = "";
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int h = metrics.heightPixels;
        int w = metrics.widthPixels;
        int dpi = metrics.densityDpi;
        String refreshRate = String.format("%.1f", ((Activity)this.context).getWindowManager().getDefaultDisplay().getRefreshRate()) + " Hz";
        int brightness = 0;
        int time = 0;
        float font = 0.0F;

        try {
            brightness = android.provider.Settings.System.getInt(this.context.getContentResolver(), "screen_brightness");
            time = android.provider.Settings.System.getInt(this.context.getContentResolver(), "screen_off_timeout");
            font = this.context.getResources().getConfiguration().fontScale;
        } catch (Settings.SettingNotFoundException var31) {
            var31.printStackTrace();
        }

        String bright = brightness * 100 / 255 + " %";
        String timeout = time / 1000 + " " + this.context.getResources().getString(string.seconds);
        String fontstr = String.valueOf(font);
        String hdr_support = this.context.getResources().getString(string.supported);
        String hdr_capable = this.context.getResources().getString(string.none);
        StringBuilder str = new StringBuilder();
        if (VERSION.SDK_INT >= 24) {
            Display.HdrCapabilities hdrCapabilities = ((Activity)this.context).getWindowManager().getDefaultDisplay().getHdrCapabilities();
            if (hdrCapabilities.getSupportedHdrTypes().length != 0) {
                int[] hdrtypes = hdrCapabilities.getSupportedHdrTypes();
                int[] var27 = hdrtypes;
                int var28 = hdrtypes.length;

                for(int var29 = 0; var29 < var28; ++var29) {
                    int hdrtype = var27[var29];
                    switch (hdrtype) {
                        case 1:
                            str.append("Dolby Vision HDR\n");
                            break;
                        case 2:
                            str.append("HDR10\n");
                            break;
                        case 3:
                            str.append("Hybrid Log-Gamma HDR\n");
                            break;
                        case 4:
                            str.append("HDR10+\n");
                    }
                }
            }
        }

        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.RESOLUTION, w + "x" + h);
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.DENSITY, dpi + "dpi");
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.FONTSCALE, fontstr);
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.SIZE, this.buildInfo.getScreenSize());
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.REFRESHRATE, refreshRate);
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.HDR, hdr_support);
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.HDR_CAP, hdr_cap);
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.BRIGHT_LEVEL, bright);
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.BRIGHT_MODE, this.buildInfo.getBrightnessMode());
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.SCREEN_TIMEOUT, timeout);
        this.mIniFile.set(com.kook.deviceinfo.constant.BuildInfoConstant.Display.TYPE, com.kook.deviceinfo.constant.BuildInfoConstant.Display.ORIENTATION, this.buildInfo.getOrientation());
    }

    public void memory() {
        String ram = this.context.getResources().getString(string.ram);
        String sys = this.context.getResources().getString(string.sys_store);
        String internal = this.context.getResources().getString(string.in_store);
        String external = this.context.getResources().getString(string.ext_store);
        String totalMem = this.context.getResources().getString(string.total_mem) + " : ";
        String usedMem = this.context.getResources().getString(string.used_mem) + " : ";
        String availMem = this.context.getResources().getString(string.avail_mem) + " : ";
        String system = Environment.getRootDirectory().getPath();
        String data = Environment.getDataDirectory().getPath();
        ActivityManager manager = (ActivityManager)this.context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        File[] files = ContextCompat.getExternalFilesDirs(this.context, (String)null);
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.MEMORY_INFO_TOTALMEM, memoryInfo.totalMem);
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.MEMORY_INFO_USEMEM, memoryInfo.totalMem - memoryInfo.availMem);
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.MEMORY_INFO_AVAILMEM, memoryInfo.availMem);
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.SYSTEM_INFO_TOTALMEM, this.buildInfo.getTotalStorageInfo(system));
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.SYSTEM_INFO_USEMEM, this.buildInfo.getUsedStorageInfo(system));
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.SYSTEM_INFO_AVAILMEM, this.buildInfo.getTotalStorageInfo(system) - this.buildInfo.getUsedStorageInfo(system));
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.INTERNAL_INFO_TOTALMEM, this.buildInfo.getTotalStorageInfo(data));
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.INTERNAL_INFO_USEMEM, this.buildInfo.getUsedStorageInfo(data));
        this.mIniFile.set(StorageSpace.TYPE, StorageSpace.INTERNAL_INFO_AVAILMEM, this.buildInfo.getTotalStorageInfo(data) - this.buildInfo.getUsedStorageInfo(data));
    }

    @RequiresApi(
            api = 29
    )
    public void codecs() {
        MediaCodecList codecList = new MediaCodecList(1);
        MediaCodecInfo[] mediaCodecInfo = codecList.getCodecInfos();
        MediaCodecInfo[] var3 = mediaCodecInfo;
        int var4 = mediaCodecInfo.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            MediaCodecInfo codecInfo = var3[var5];
            JSONObject mElement = new JSONObject();
            String[] supportedTypes = codecInfo.getSupportedTypes();
            mElement.put("supportedTypes", Arrays.toString(supportedTypes));
            String canonicalName = codecInfo.getCanonicalName();
            mElement.put("canonicalName", canonicalName);
            String name = codecInfo.getName();
            mElement.put("name", name);
            boolean alias = codecInfo.isAlias();
            mElement.put("alias", alias);
            boolean encoder = codecInfo.isEncoder();
            mElement.put("encoder", encoder);
            boolean softwareOnly = codecInfo.isSoftwareOnly();
            mElement.put("softwareOnly", softwareOnly);
            boolean hardwareAccelerated = codecInfo.isHardwareAccelerated();
            mElement.put("hardwareAccelerated", hardwareAccelerated);
            boolean vendor = codecInfo.isVendor();
            mElement.put("vendor", vendor);
            String toJson = mElement.toString();
            this.mIniFile.set(SystemMediaCodecList.TYPE, name, toJson);
        }

    }

    public void glExtensions() {
    }

    public void inputDevices() {
        int[] id = InputDevice.getDeviceIds();
        int[] var2 = id;
        int var3 = id.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            int facing = var2[var4];
            InputDevice inputDevice = InputDevice.getDevice(facing);
            String jsonString = JSON.toJSONString(inputDevice);
            this.mIniFile.set(SystemInputDevice.TYPE, String.valueOf(facing), jsonString);
        }

    }

    public void cameraApi21() {
        if (VERSION.SDK_INT >= 21) {
            try {
                this.cameraManager = (CameraManager)this.context.getSystemService(Context.CAMERA_SERVICE);
                this.cameraIds = this.cameraManager.getCameraIdList();
                this.mIniFile.setList(Camera.TYPE, Camera.CAMERA_IDS, Arrays.asList(this.cameraIds));
                String[] var1 = this.cameraIds;
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    String cameraId = var1[var3];
                    this.camera21Facing(Integer.parseInt(cameraId));
                }
            } catch (Exception var5) {
                HVLog.printException(var5);
            }
        }

    }

    private void camera21Facing(int facing) {
        if (VERSION.SDK_INT >= 21) {
            try {
                CameraCharacteristics cameraCharacteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
                cameraCharacteristics.getKeys();
                CameraCharacteristics characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
                JSONObject mElement = new JSONObject();
                Iterator var5 = characteristics.getKeys().iterator();

                while(var5.hasNext()) {
                    CameraCharacteristics.Key<?> key = (CameraCharacteristics.Key)var5.next();
                    Object value = characteristics.get(key);
                    mElement.put(key.getName(), this.valueToString(value));
                }

                this.mIniFile.set(Camera.TYPE, "CameraCharacteristics-" + String.valueOf(facing), mElement.toJSONString());
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

    }

    private String valueToString(Object value) {
        if (value == null) {
            return "";
        } else if (value.getClass().isArray()) {
            if (value instanceof int[]) {
                return Arrays.toString((int[])value);
            } else if (value instanceof float[]) {
                return Arrays.toString((float[])value);
            } else if (value instanceof String[]) {
                return Arrays.toString((String[])value);
            } else {
                return value instanceof boolean[] ? Arrays.toString((boolean[])value) : Arrays.toString((Object[])value);
            }
        } else {
            return value.toString();
        }
    }

    public void systemProperty() {
        Map<String, String> property = this.buildInfo.getAndroidProperty();
        Iterator var2 = property.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var2.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            this.mIniFile.set(SystemProperty.TYPE, key, value);
        }

    }

    public void javaProperty() {
        Map<String, String> property = this.buildInfo.getJavaProperties();
        HVLog.d(" ========== javaProperty 有" + property.size());
        Iterator var2 = property.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var2.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            this.mIniFile.set(JavaProperty.TYPE, key, value);
        }

    }

    public void settingsProperty() {
        Map<String, String> propertyGlobal = this.buildInfo.settingsProperty(Settings.Global.CONTENT_URI);
        HVLog.d(" ========== settingsProperty \u6709" + propertyGlobal.size());
        for (Map.Entry<String, String> entry : propertyGlobal.entrySet()) {
            String string2 = entry.getKey();
            String value = entry.getValue();
            this.mIniFile.set(BuildInfoConstant.SettingsGlobalProperty.TYPE, string2, value);
        }
        Map<String, String> propertySystem = this.buildInfo.settingsProperty(Settings.System.CONTENT_URI);
        HVLog.d(" ========== propertySystem \u6709" + propertySystem.size());
        for (Map.Entry<String, String> entry : propertySystem.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            this.mIniFile.set(BuildInfoConstant.SettingsSystemProperty.TYPE, key, value);
        }
        Map<String, String> map = this.buildInfo.settingsProperty(Settings.Secure.CONTENT_URI);
        HVLog.d(" ========== propertySecure \u6709" + map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            this.mIniFile.set(BuildInfoConstant.SettingsSecureProperty.TYPE, key, value);
        }
    }

    @SuppressLint({"DefaultLocale"})
    public String formattedValue(long l) {
        float f;
        String s;
        if ((double)l > 1.073741824E10) {
            f = (float)l / 1.07374182E9F;
            s = String.format("%.1f", f) + " GB";
        } else if ((double)l > 1.048576E7 && (double)l <= 1.073741824E10) {
            f = (float)l / 1048576.0F;
            s = String.format("%.1f", f) + " MB";
        } else if (l > 1024L && (double)l <= 1.048576E7) {
            f = (float)l / 1024.0F;
            s = String.format("%.1f", f) + " KB";
        } else {
            f = (float)l;
            s = String.format("%.2f", f) + " Bytes";
        }

        return s;
    }

    public void netlink() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.BLUETOOTH_CONNECT") != 0) {
        }

        if (bluetoothAdapter != null) {
            String address;
            if (VERSION.SDK_INT >= 31) {
                if (ContextCompat.checkSelfPermission(this.context, "android.permission.BLUETOOTH_CONNECT") != 0) {
                    return;
                }

                address = bluetoothAdapter.getAddress();
            } else {
                address = bluetoothAdapter.getAddress();
            }

            this.mIniFile.set(GeneralDataInfo.BT_INFO, "bt_address", address);
        }
    }

    public void fingerprintFile() {
        Iterator var1 = SystemFileConStant.SOC_FILE_LIST.iterator();

        while(var1.hasNext()) {
            String fingerFile = (String)var1.next();
            this.mIniFile.set(FingerprintInfo.TYPE, fingerFile, FileTools.readFile(fingerFile));
        }

    }

    public void systemFileInfo() {
        long timeMillis = System.currentTimeMillis();
        List<String> fileList = FileInfoNative.getFileList("/system");
        Iterator var4 = fileList.iterator();

        String file;
        while(var4.hasNext()) {
            file = (String)var4.next();
            file = FileInfoNative.fileInfoToJson(file);
            this.mIniFile.set(SystemFileInfo.TYPE, file, file);
        }

        int size = this.mIniFile.get(SystemFileInfo.TYPE).getValues().size();
        HVLog.d("查看将 system 下所有文件信息保存耗时" + (System.currentTimeMillis() - timeMillis) + "  文件有" + fileList.size() + "个    查看数据有" + size + " 条");
        timeMillis = System.currentTimeMillis();
        fileList = FileInfoNative.getFileList("/vendor");
        Iterator var9 = fileList.iterator();

        String fileInfoToJson;
        while(var9.hasNext()) {
            file = (String)var9.next();
            fileInfoToJson = FileInfoNative.fileInfoToJson(file);
            this.mIniFile.set(SystemFileInfo.TYPE, file, fileInfoToJson);
        }

        size = this.mIniFile.get(SystemFileInfo.TYPE).getValues().size();
        HVLog.d("查看将 vendor 下所有文件信息保存耗时" + (System.currentTimeMillis() - timeMillis) + "  文件有" + fileList.size() + "个    查看数据有" + size + " 条");
        timeMillis = System.currentTimeMillis();
        fileList = FileInfoNative.getFileList("/data/system");
        var9 = fileList.iterator();

        while(var9.hasNext()) {
            file = (String)var9.next();
            fileInfoToJson = FileInfoNative.fileInfoToJson(file);
            this.mIniFile.set(SystemFileInfo.TYPE, file, fileInfoToJson);
        }

        size = this.mIniFile.get(SystemFileInfo.TYPE).getValues().size();
        HVLog.d("查看将 /data/system 下所有文件信息保存耗时" + (System.currentTimeMillis() - timeMillis) + "  文件有" + fileList.size() + "个    查看数据有" + size + " 条");
    }

    public void systemAppInfo() {
        long timeMillis = System.currentTimeMillis();
        PackageManager packageManager = this.context.getPackageManager();
        List<ApplicationInfo> packagelist = packageManager.getInstalledApplications(128);
        Iterator var5 = packagelist.iterator();

        while(var5.hasNext()) {
            ApplicationInfo applicationInfo = (ApplicationInfo)var5.next();
            String jsonToAppInfo = AppInfoHelper.getJsonToAppInfo(this.context, applicationInfo.packageName);
            this.mIniFile.set(SystemAppInfo.TYPE, applicationInfo.packageName, jsonToAppInfo);
        }

        int count = this.mIniFile.get(SystemAppInfo.TYPE).getValues().size();
        HVLog.d("查看系统有  " + count + "  个APP,耗时" + (System.currentTimeMillis() - timeMillis));
    }

    public void userAgent() {
        String systemUserAgent = System.getProperty("http.agent");
        String webviewUserAgent = WebSettings.getDefaultUserAgent(this.context);
        this.mIniFile.set(UserAgent.TYPE, "systemUserAgent", systemUserAgent);
        this.mIniFile.set(UserAgent.TYPE, "webviewUserAgent", webviewUserAgent);
    }

    public void generalDataInfo() {
        long timeMillis = System.currentTimeMillis();
        GeneralData generalData = new GeneralData();
        List<String> classField = RefClassAttr.getClassField(GeneralData.class);
        Iterator var5 = classField.iterator();

        while(var5.hasNext()) {
            String fieldName = (String)var5.next();
            String valueByObject = RefClassAttr.getFieldStringValueByObject(fieldName, generalData);
            this.mIniFile.set(GeneralDataInfo.TYPE, fieldName, valueByObject);
        }

    }

    public void systemSensorInfo() {
        Iterator var1 = this.sensorListModels.iterator();

        while(var1.hasNext()) {
            SensorListModel sensor = (SensorListModel)var1.next();
            this.mIniFile.set(SensorInfo.TYPE, String.valueOf(sensor.getName()), sensor.toJSON());
        }

    }

    public void systemInputInfo() {
        Iterator var1 = this.inputListModels.iterator();

        while(var1.hasNext()) {
            InputModel input = (InputModel)var1.next();
            this.mIniFile.set(InputInfo.TYPE, input.getName(), input.toJSON());
        }

    }
}

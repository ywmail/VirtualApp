/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.ActivityManager
 *  android.app.ActivityManager$MemoryInfo
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.database.Cursor
 *  android.graphics.Point
 *  android.hardware.Sensor
 *  android.hardware.SensorManager
 *  android.hardware.input.InputManager
 *  android.media.AudioManager
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.Proxy
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Environment
 *  android.os.StatFs
 *  android.os.SystemClock
 *  android.provider.ContactsContract$Groups
 *  android.provider.Settings$Global
 *  android.provider.Settings$Secure
 *  android.provider.Settings$SettingNotFoundException
 *  android.provider.Settings$System
 *  android.telephony.CellInfo
 *  android.telephony.CellInfoCdma
 *  android.telephony.CellInfoGsm
 *  android.telephony.CellInfoLte
 *  android.telephony.CellInfoWcdma
 *  android.telephony.CellSignalStrengthCdma
 *  android.telephony.CellSignalStrengthGsm
 *  android.telephony.CellSignalStrengthLte
 *  android.telephony.CellSignalStrengthWcdma
 *  android.telephony.SubscriptionManager
 *  android.telephony.TelephonyManager
 *  android.text.TextUtils
 *  android.text.format.Formatter
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.InputDevice
 *  android.view.KeyCharacterMap
 *  android.view.ViewConfiguration
 *  android.view.WindowManager
 *  androidx.annotation.RequiresApi
 *  androidx.core.app.ActivityCompat
 */
package com.kook.deviceinfo.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import com.kook.deviceinfo.DeviceApplication;
import com.kook.deviceinfo.constant.SystemFileConStant;
import com.kook.deviceinfo.data.SensorData;
import com.kook.deviceinfo.util.GeneralUtils;
import com.kook.deviceinfo.util.Md5Utils;
import com.kook.deviceinfo.util.MediaFilesUtils;
import com.kook.deviceinfo.util.NetWorkUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class OtherUtils {
    private static final String LINE_SEP = System.getProperty("line.separator");

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager)DeviceApplication.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static String getBaseband_Ver() {
        String Version2 = "";
        try {
            Class<?> cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", String.class, String.class);
            Object result = m.invoke(invoker, "gsm.version.baseband", "no message");
            Version2 = (String)result;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return Version2;
    }

    @RequiresApi(api=17)
    public static String getResolutions() {
        Point outSize = new Point();
        WindowManager wm = (WindowManager)DeviceApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealSize(outSize);
        int x = outSize.x;
        int y = outSize.y;
        return x + "*" + y;
    }

    @SuppressLint("MissingPermission")
    public static String getSerialNumbers() {
        String serial = "";

        try {
            if (Build.VERSION.SDK_INT >= 28) {
                serial = Build.getSerial();
            } else if (Build.VERSION.SDK_INT > 24) {
                serial = Build.SERIAL;
            } else {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String)get.invoke(c, "ro.serialno");
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            Log.e("e", "读取设备序列号异常：" + var3.toString());
        }

        return serial;
    }

    public static String getScreenSizeOfDevice2() {
        Point point = new Point();
        WindowManager wm = (WindowManager)DeviceApplication.getApp().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = DeviceApplication.getApp().getResources().getDisplayMetrics();
        double x = Math.pow((float)point.x / dm.xdpi, 2.0);
        double y = Math.pow((float)point.y / dm.ydpi, 2.0);
        double screenInches = Math.sqrt(x + y);
        return String.valueOf(screenInches);
    }

    @SuppressLint(value={"MissingPermission"})
    public static int getJudgeSIMCount() {
        if (ActivityCompat.checkSelfPermission((Context)DeviceApplication.getApp(), (String)"android.permission.READ_PHONE_STATE") != 0) {
            return 0;
        }
        int count = 0;
        if (Build.VERSION.SDK_INT >= 22) {
            count = SubscriptionManager.from((Context)DeviceApplication.getApp()).getActiveSubscriptionInfoCount();
            return count;
        }
        return count;
    }

    public static int getPhoneSimCount() {
        if (Build.VERSION.SDK_INT >= 23) {
            return OtherUtils.getTelephonyManager().getPhoneCount();
        }
        return 0;
    }

    @SuppressLint(value={"HardwareIds,MissingPermission"})
    public static String getSimSerialNumbers() {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        } else {
             String simSerialNumber = getTelephonyManager().getSimSerialNumber();
            return simSerialNumber;
        }
    }

    public static String getSimCountryIsos() {
        return OtherUtils.getTelephonyManager().getSimCountryIso();
    }

    public static int isAppRoot() {
        int result = 0;
        if (OtherUtils.isDeviceRooted()) {
            result = 1;
        }
        return result;
    }

    public static boolean isDeviceRooted() {
        return OtherUtils.checkRootMethod1() || OtherUtils.checkRootMethod2() || OtherUtils.checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths;
        for (String path : paths = SystemFileConStant.ROOT_FILE) {
            if (!new File(path).exists()) continue;
            return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) {
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
        catch (Throwable t) {
            boolean bl = false;
            return bl;
        }
        finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static int isEmulator() {
        boolean checkDial;
        boolean checkOperatorName;
        String name;
        boolean checkProperty;
        boolean bl = checkProperty = Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.toLowerCase().contains("vbox") || Build.FINGERPRINT.toLowerCase().contains("test-keys") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") || "google_sdk".equals(Build.PRODUCT);
        if (checkProperty) {
            return 1;
        }
        String operatorName = "";
        TelephonyManager tm = (TelephonyManager)DeviceApplication.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null && (name = tm.getNetworkOperatorName()) != null) {
            operatorName = name;
        }
        if (checkOperatorName = operatorName.toLowerCase().equals("android")) {
            return 1;
        }
        String url = "tel:123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse((String)url));
        intent.setAction("android.intent.action.DIAL");
        boolean bl2 = checkDial = intent.resolveActivity(DeviceApplication.getApp().getPackageManager()) == null;
        if (checkDial) {
            return 1;
        }
        return 0;
    }

    public static int isMockLocation() {
        boolean isOpen;
        boolean bl = isOpen = Settings.Secure.getInt((ContentResolver)DeviceApplication.getApp().getContentResolver(), (String)"mock_location", (int)0) != 0;
        if (isOpen) {
            return 1;
        }
        return 0;
    }

    public static int isAppDebug() {
        boolean b;
        boolean bl = b = Settings.Secure.getInt((ContentResolver)DeviceApplication.getApp().getContentResolver(), (String)"adb_enabled", (int)0) > 0;
        if (b) {
            return 1;
        }
        return 0;
    }

    public static int isAirplaneModeOn() {
        boolean b;
        boolean bl = b = Settings.Global.getInt((ContentResolver)DeviceApplication.getApp().getContentResolver(), (String)"airplane_mode_on", (int)0) != 0;
        if (b) {
            return 1;
        }
        return 0;
    }

    public static int getPhoneMode() {
        AudioManager audioManager = (AudioManager)DeviceApplication.getApp().getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }

    public static String getProxyAddress() {
        String port = "";
        if (OtherUtils.checkVPN() == 1) {
            try {
                Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                while (en.hasMoreElements()) {
                    NetworkInterface intf = en.nextElement();
                    Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (inetAddress.isLoopbackAddress() || !intf.getName().contains("tun")) continue;
                        port = Formatter.formatIpAddress((int)inetAddress.hashCode());
                    }
                }
            }
            catch (SocketException ex) {
                ex.printStackTrace();
            }
        }
        return port;
    }

    public static int getIsWifiProxy() {
        boolean b;
        int proxyPort;
        String proxyAddress;
        boolean IS_ICS_OR_LATER;
        boolean bl = IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= 14;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt(portStr != null ? portStr : "-1");
        } else {
            proxyAddress = Proxy.getHost((Context)DeviceApplication.getApp());
            proxyPort = Proxy.getPort((Context)DeviceApplication.getApp());
        }
        boolean bl2 = b = !TextUtils.isEmpty((CharSequence)proxyAddress) && proxyPort != -1;
        if (b) {
            return 1;
        }
        return 0;
    }

    public static String getHostAndPort() {
        int proxyPort;
        String proxyAddress;
        boolean IS_ICS_OR_LATER;
        boolean bl = IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= 14;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt(portStr != null ? portStr : "-1");
        } else {
            proxyAddress = Proxy.getHost((Context)DeviceApplication.getApp());
            proxyPort = Proxy.getPort((Context)DeviceApplication.getApp());
        }
        if (TextUtils.isEmpty((CharSequence)proxyAddress) && proxyPort == -1) {
            return "";
        }
        return proxyAddress + ":" + proxyPort;
    }

    public static long getBootTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtimeNanos() / 1000000L;
    }

    @SuppressLint(value={"NewApi"})
    public static String getMobileDbm() {
        String dbm = "";
        TelephonyManager tm = getTelephonyManager();
        if (ActivityCompat.checkSelfPermission(DeviceApplication.getApp(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
            return dbm;
        } else {
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            if (Build.VERSION.SDK_INT >= 17 && null != cellInfoList) {
                Iterator var3 = cellInfoList.iterator();

                while(var3.hasNext()) {
                    CellInfo cellInfo = (CellInfo)var3.next();
                    if (cellInfo instanceof CellInfoGsm) {
                        CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm)cellInfo).getCellSignalStrength();
                        dbm = String.valueOf(cellSignalStrengthGsm.getDbm());
                    } else if (cellInfo instanceof CellInfoCdma) {
                        CellSignalStrengthCdma cellSignalStrengthCdma = ((CellInfoCdma)cellInfo).getCellSignalStrength();
                        dbm = String.valueOf(cellSignalStrengthCdma.getDbm());
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        if (Build.VERSION.SDK_INT >= 18) {
                            CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma)cellInfo).getCellSignalStrength();
                            dbm = String.valueOf(cellSignalStrengthWcdma.getDbm());
                        }
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte)cellInfo).getCellSignalStrength();
                        dbm = String.valueOf(cellSignalStrengthLte.getDbm());
                    }
                }
            }

            return dbm;
        }
    }

    public static int getBrightness() {
        int value = 255;
        return Settings.System.getInt((ContentResolver)DeviceApplication.getApp().getContentResolver(), (String)"screen_brightness", (int)value);
    }

    public static long getExternalTotalSize() {
        return OtherUtils.getFsTotalSize(OtherUtils.getSDCardPathByEnvironment());
    }

    public static long getExternalAvailableSize() {
        return OtherUtils.getFsAvailableSize(OtherUtils.getSDCardPathByEnvironment());
    }

    public static long getInternalTotalSize() {
        return OtherUtils.getFsTotalSize(Environment.getDataDirectory().getAbsolutePath());
    }

    public static long getInternalAvailableSize() {
        return OtherUtils.getFsAvailableSize(Environment.getDataDirectory().getAbsolutePath());
    }

    public static int getContactGroupCount() {
        if (ActivityCompat.checkSelfPermission((Context)DeviceApplication.getApp(), (String)"android.permission.READ_CONTACTS") != 0) {
            return 0;
        }
        Uri uri = ContactsContract.Groups.CONTENT_URI;
        ContentResolver contentResolver = DeviceApplication.getApp().getContentResolver();
        Cursor cursor = contentResolver.query(uri, new String[]{"title", "_id"}, null, null, null);
        int cursorCount = cursor.getCount();
        return cursorCount;
    }

    public static long getAvailMemory() {
        ActivityManager am = (ActivityManager)DeviceApplication.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";
        long initial_memory = 0L;
        try {
            String[] arrayOfString;
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            String str2 = localBufferedReader.readLine();
            for (String num : arrayOfString = str2.split("\\s+")) {
                Log.i((String)str2, (String)(num + "\t"));
            }
            int i = Integer.valueOf(arrayOfString[1]);
            initial_memory = new Long((long)i * 1024L);
            localBufferedReader.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return initial_memory;
    }

    public static long getFsTotalSize(String anyPathInFs) {
        long totalSize;
        long blockSize;
        if (TextUtils.isEmpty((CharSequence)anyPathInFs)) {
            return 0L;
        }
        StatFs statFs = new StatFs(anyPathInFs);
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = statFs.getBlockSizeLong();
            totalSize = statFs.getBlockCountLong();
        } else {
            blockSize = statFs.getBlockSize();
            totalSize = statFs.getBlockCount();
        }
        return blockSize * totalSize;
    }

    public static long getFsAvailableSize(String anyPathInFs) {
        long availableSize;
        long blockSize;
        if (TextUtils.isEmpty((CharSequence)anyPathInFs)) {
            return 0L;
        }
        StatFs statFs = new StatFs(anyPathInFs);
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = statFs.getBlockSizeLong();
            availableSize = statFs.getAvailableBlocksLong();
        } else {
            blockSize = statFs.getBlockSize();
            availableSize = statFs.getAvailableBlocks();
        }
        return blockSize * availableSize;
    }

    public static String getSDCardPathByEnvironment() {
        if (MediaFilesUtils.isSDCardEnableByEnvironment()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static int getScreenDensityDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public static int isTabletDevice() {
        boolean b;
        boolean bl = b = (DeviceApplication.getApp().getResources().getConfiguration().screenLayout & 0xF) >= 3;
        if (b) {
            return 1;
        }
        return 0;
    }

    public static String getDeviceid() {
        StringBuffer stringBuffer = new StringBuffer();
        String meid = "";
        String imei = "";
        String serial = "";
        String mac = "";
        String manufacturer = "";
        if (OtherUtils.getJudgeSIMCount() == 2) {
            meid = GeneralUtils.getMeid();
            if (TextUtils.isEmpty((CharSequence)meid)) {
                imei = GeneralUtils.getIMEI(0);
                stringBuffer.append(imei).append("/");
            } else {
                stringBuffer.append(meid).append("/");
            }
        } else {
            imei = GeneralUtils.getIMEI(0);
            stringBuffer.append(imei).append("/");
        }
        serial = OtherUtils.getSerialNumbers();
        manufacturer = Build.MANUFACTURER;
        if (TextUtils.isEmpty((CharSequence)serial)) {
            mac = NetWorkUtils.getMacAddress();
            stringBuffer.append(mac).append("/");
        } else {
            stringBuffer.append(serial).append("/");
        }
        stringBuffer.append(manufacturer).append("/");
        stringBuffer.append(Build.BRAND).append("/");
        stringBuffer.append(Build.DEVICE).append("/");
        stringBuffer.append(Build.HARDWARE).append("/");
        stringBuffer.append(Build.MODEL).append("/");
        stringBuffer.append(Build.PRODUCT).append("/");
        stringBuffer.append(Build.TAGS).append("/");
        stringBuffer.append(Build.TYPE).append("/");
        stringBuffer.append(Build.USER).append("/");
        if (Build.VERSION.SDK_INT >= 21) {
            stringBuffer.append(Build.SUPPORTED_ABIS[0]).append("/");
        }
        stringBuffer.append(OtherUtils.getResolutions()).append("/");
        stringBuffer.append(OtherUtils.getScreenDensity()).append("/");
        stringBuffer.append(OtherUtils.getScreenDensityDpi());
        return Md5Utils.string2MD5(stringBuffer.toString());
    }

    public static SensorData getSensorList(SensorData sensorData) {
        SensorManager sensorManager = (SensorManager)DeviceApplication.getApp().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(-1);
        Iterator var3 = sensors.iterator();

        while(var3.hasNext()) {
            Sensor item = (Sensor)var3.next();
            SensorData.SensorInfo storageDatas = new SensorData.SensorInfo();
            storageDatas.type = item.getType();
            storageDatas.name = item.getName();
            storageDatas.version = item.getVersion();
            storageDatas.vendor = item.getVendor();
            storageDatas.max_range = item.getMaximumRange();
            storageDatas.min_delay = item.getMinDelay();
            storageDatas.power = item.getPower();
            storageDatas.resolution = item.getResolution();
            sensorData.sensor_lists.add(storageDatas);
        }

        return sensorData;
    }

    public static String detectInputDeviceWithShell() {
        String deviceInfo = "";
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/bus/input/devices");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            line = in.readLine();
            if (line != null) {
                deviceInfo = line.trim();
                return deviceInfo;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }

    public static String detectUsbDeviceWithInputManager() {
        int[] devices;
        String name = "";
        InputManager im = (InputManager)DeviceApplication.getApp().getSystemService(Context.INPUT_SERVICE);
        for (int id2 : devices = im.getInputDeviceIds()) {
            InputDevice device = im.getInputDevice(id2);
            name = device.getName();
        }
        return name;
    }

    public static int checkDeviceHasNavigationBar() {
        boolean hasMenuKey = ViewConfiguration.get((Context)DeviceApplication.getApp()).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey((int)4);
        if (!hasMenuKey && !hasBackKey) {
            return 0;
        }
        return 1;
    }

    public static int checkVPN() {
        ConnectivityManager cm = (ConnectivityManager)DeviceApplication.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean b = cm.getNetworkInfo(17).isConnectedOrConnecting();
        if (b) {
            return 1;
        }
        return 0;
    }

    public static boolean isLoactionEnabled(Context context) {
        int locationMode = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                locationMode = Settings.Secure.getInt((ContentResolver)context.getContentResolver(), (String)"location_mode");
            }
            catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != 0;
        }
        String locationProviders = Settings.Secure.getString((ContentResolver)context.getContentResolver(), (String)"location_providers_allowed");
        return !TextUtils.isEmpty((CharSequence)locationProviders);
    }

    public static int isNetState() {
        ConnectivityManager connectivityManager = (ConnectivityManager)DeviceApplication.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return 0;
        }
        int type = activeNetworkInfo.getType();
        switch (type) {
            case 1: {
                return 1;
            }
            case 0: {
                return 2;
            }
        }
        return 0;
    }

    public static String getDeviceName() {
        return Settings.Secure.getString((ContentResolver)DeviceApplication.getApp().getContentResolver(), (String)"bluetooth_name");
    }
}


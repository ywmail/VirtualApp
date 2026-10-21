//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kook.deviceinfo.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.DeviceApplication;
import com.kook.deviceinfo.data.NetWorkData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class NetWorkUtils {
    public NetWorkUtils() {
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        HVLog.d("网络不通");
        return false;
    }

    public static NetWorkData getNetWorkInfo(NetWorkData wifiInfos) {
        WifiManager mWifiManager = (WifiManager)DeviceApplication.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = mWifiManager.getConnectionInfo();
        wifiInfos.current_wifi.bssid = info.getBSSID();
        String ssid = info.getSSID().replace("\"", "");
        wifiInfos.current_wifi.name = ssid;
        wifiInfos.current_wifi.ssid = ssid;
        wifiInfos.current_wifi.mac = info.getMacAddress();
        wifiInfos.ip = int2ip(info.getIpAddress());
        wifiInfos.configured_wifi.addAll(getAroundWifiDeciceInfo());
        return wifiInfos;
    }

    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 255).append(".");
        sb.append(ipInt >> 8 & 255).append(".");
        sb.append(ipInt >> 16 & 255).append(".");
        sb.append(ipInt >> 24 & 255);
        return sb.toString();
    }

    @SuppressLint("MissingPermission")
    public static List<NetWorkData.NetWorkInfo> getAroundWifiDeciceInfo() {
        new StringBuffer();
        WifiManager mWifiManager = (WifiManager)DeviceApplication.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        List<NetWorkData.NetWorkInfo> wifiLists = new ArrayList();
        Iterator var4 = scanResults.iterator();

        while(var4.hasNext()) {
            ScanResult scanResult = (ScanResult)var4.next();
            wifiLists.add(new NetWorkData.NetWorkInfo(scanResult.BSSID, scanResult.SSID, scanResult.SSID));
        }

        return wifiLists;
    }

    public static String getMacAddress() {
        String mac = "02:00:00:00:00:00";
        if (VERSION.SDK_INT < 23) {
            mac = getMacDefault();
        } else if (VERSION.SDK_INT < 24) {
            mac = getMacAddresss();
        } else if (VERSION.SDK_INT >= 24) {
            mac = getMacFromHardware();
        }

        return mac;
    }

    private static String getMacDefault() {
        String mac = "02:00:00:00:00:00";
        WifiManager wifi = (WifiManager)DeviceApplication.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        } else {
            WifiInfo info = null;

            try {
                info = wifi.getConnectionInfo();
            } catch (Exception var4) {
            }

            if (info == null) {
                return null;
            } else {
                mac = info.getMacAddress();
                if (!TextUtils.isEmpty(mac)) {
                    mac = mac.toUpperCase(Locale.ENGLISH);
                }

                return mac;
            }
        }
    }

    private static String getMacAddresss() {
        String WifiAddress = "02:00:00:00:00:00";

        try {
            WifiAddress = (new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address")))).readLine();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        return WifiAddress;
    }

    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var1 = all.iterator();

            while(var1.hasNext()) {
                NetworkInterface nif = (NetworkInterface)var1.next();
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    byte[] var5 = macBytes;
                    int var6 = macBytes.length;

                    for(int var7 = 0; var7 < var6; ++var7) {
                        byte b = var5[var7];
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }

                    return res1.toString();
                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return "02:00:00:00:00:00";
    }

    public static String getBluetoothMac() {
        String bluetoothAddress = "";
        if (VERSION.SDK_INT < 23) {
            bluetoothAddress = Secure.getString(DeviceApplication.getApp().getContentResolver(), "bluetooth_address");
        } else {
            bluetoothAddress = getBluetoothAddressSdk23(BluetoothAdapter.getDefaultAdapter());
        }

        return bluetoothAddress;
    }

    @TargetApi(23)
    public static String getBluetoothAddressSdk23(BluetoothAdapter adapter) {
        if (adapter == null) {
            return null;
        } else {
            Class<? extends BluetoothAdapter> btAdapterClass = adapter.getClass();

            try {
                Class<?> btClass = Class.forName("android.bluetooth.IBluetooth");
                Field bluetooth = btAdapterClass.getDeclaredField("mService");
                bluetooth.setAccessible(true);
                Method btAddress = btClass.getMethod("getAddress");
                btAddress.setAccessible(true);
                return (String)btAddress.invoke(bluetooth.get(adapter));
            } catch (Exception var5) {
                return "";
            }
        }
    }

    public static String getBluetoothName(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return "";
        } else {
            return VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(context, "android.permission.BLUETOOTH_CONNECT") != 0 ? "Permission not granted" : bluetoothAdapter.getName();
        }
    }
}

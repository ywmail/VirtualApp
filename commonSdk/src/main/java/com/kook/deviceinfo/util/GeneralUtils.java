/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.UiModeManager
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.NetworkInfo$State
 *  android.os.Build$VERSION
 *  android.provider.Settings$Secure
 *  android.telephony.SubscriptionInfo
 *  android.telephony.SubscriptionManager
 *  android.telephony.TelephonyManager
 *  android.telephony.gsm.GsmCellLocation
 *  android.text.TextUtils
 *  androidx.core.app.ActivityCompat
 */
package com.kook.deviceinfo.util;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import com.kook.deviceinfo.DeviceApplication;
import com.kook.deviceinfo.data.SimCardData;
import com.kook.deviceinfo.util.AdvertisingIdClient;
import com.kook.deviceinfo.util.OtherUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class GeneralUtils {
    public static String gaid = "";

    public static boolean isChekSelfPermission(String permission2) {
        return ActivityCompat.checkSelfPermission((Context)DeviceApplication.getApp(), (String)permission2) != 0;
    }

    @SuppressLint(value={"HardwareIds"})
    public static String getAndroidID() {
        String id2 = Settings.Secure.getString((ContentResolver)DeviceApplication.getApp().getContentResolver(), (String)"android_id");
        if ("9774d56d682e549c".equals(id2)) {
            return "";
        }
        return id2 == null ? "" : id2;
    }

    public static String getNetworkOperatorName() {
        return GeneralUtils.getTelephonyManager().getNetworkOperatorName();
    }

    public static String getNetworkOperator() {
        return GeneralUtils.getTelephonyManager().getNetworkOperator();
    }

    public static String getSimOperatorByMnc() {
        String operator = GeneralUtils.getTelephonyManager().getSimOperator();
        if (operator == null) {
            return "";
        }
        switch (operator) {
            case "46000": 
            case "46002": 
            case "46007": 
            case "46020": {
                return "\u4e2d\u56fd\u79fb\u52a8";
            }
            case "46001": 
            case "46006": 
            case "46009": {
                return "\u4e2d\u56fd\u8054\u901a";
            }
            case "46003": 
            case "46005": 
            case "46011": {
                return "\u4e2d\u56fd\u7535\u4fe1";
            }
        }
        return operator;
    }

    public static String getMcc() {
        String networkOperator = GeneralUtils.getTelephonyManager().getNetworkOperator();
        if (!TextUtils.isEmpty((CharSequence)networkOperator)) {
            return networkOperator.substring(0, 3);
        }
        return "";
    }

    public static String getMnc() {
        String networkOperator = GeneralUtils.getTelephonyManager().getNetworkOperator();
        if (!TextUtils.isEmpty((CharSequence)networkOperator)) {
            return networkOperator.substring(3);
        }
        return "";
    }

    public static String getNetworkType() {
        if (GeneralUtils.isChekSelfPermission("android.permission.ACCESS_NETWORK_STATE")) {
            return "NETWORK_NO";
        }
        if (GeneralUtils.isEthernet()) {
            return "NETWORK_ETHERNET";
        }
        ConnectivityManager cm = (ConnectivityManager)DeviceApplication.getApp().getSystemService("connectivity");
        if (cm == null) {
            return null;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == 1) {
                return "NETWORK_WIFI";
            }
            if (info.getType() == 0) {
                switch (info.getSubtype()) {
                    case 1: 
                    case 2: 
                    case 4: 
                    case 7: 
                    case 11: 
                    case 16: {
                        return "NETWORK_2G";
                    }
                    case 3: 
                    case 5: 
                    case 6: 
                    case 8: 
                    case 9: 
                    case 10: 
                    case 12: 
                    case 14: 
                    case 15: 
                    case 17: {
                        return "NETWORK_3G";
                    }
                    case 13: 
                    case 18: {
                        return "NETWORK_4G";
                    }
                    case 20: {
                        return "NETWORK_5G";
                    }
                }
                String subtypeName = info.getSubtypeName();
                if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                    return "NETWORK_3G";
                }
                return "NETWORK_UNKNOWN";
            }
            return "NETWORK_UNKNOWN";
        }
        return "NETWORK_NO";
    }

    public static String getPhoneType() {
        int phoneType = GeneralUtils.getTelephonyManager().getPhoneType();
        switch (phoneType) {
            case 0: {
                return "PHONE_TYPE_NONE";
            }
            case 1: {
                return "PHONE_TYPE_GSM";
            }
            case 2: {
                return "PHONE_TYPE_CDMA";
            }
            case 3: {
                return "PHONE_TYPE_SIP";
            }
        }
        return "";
    }

    private static boolean isEthernet() {
        ConnectivityManager cm = (ConnectivityManager)DeviceApplication.getApp().getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo info = cm.getNetworkInfo(9);
        if (info == null) {
            return false;
        }
        NetworkInfo.State state = info.getState();
        if (null == state) {
            return false;
        }
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
    }

    @SuppressLint(value={"HardwareIds"})
    public static String getIMSI() {
        if (GeneralUtils.isChekSelfPermission("android.permission.READ_PHONE_STATE")) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                GeneralUtils.getTelephonyManager().getSubscriberId();
            }
            catch (SecurityException e) {
                e.printStackTrace();
                return "";
            }
        }
        return GeneralUtils.getTelephonyManager().getSubscriberId();
    }

    public static String getIMSI(int subId) {
        TelephonyManager telephonyManager = GeneralUtils.getTelephonyManager();
        Class<?> telephonyManagerClass = null;
        String imsi = "";
        try {
            telephonyManagerClass = Class.forName("android.telephony.TelephonyManager");
            if (Build.VERSION.SDK_INT > 21) {
                Method method = telephonyManagerClass.getMethod("getSubscriberId", Integer.TYPE);
                imsi = (String)method.invoke(telephonyManager, subId);
            } else if (Build.VERSION.SDK_INT == 21) {
                Method method = telephonyManagerClass.getMethod("getSubscriberId", Long.TYPE);
                imsi = (String)method.invoke(telephonyManager, subId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (imsi == null) {
            imsi = "";
        }
        return imsi;
    }

    @SuppressLint(value={"MissingPermission"})
    public static SimCardData getSimCardInfo() {
        SimCardData simCardData = new SimCardData();
        simCardData.sim_count = OtherUtils.getJudgeSIMCount();
        if (Build.VERSION.SDK_INT >= 22) {
            SubscriptionManager mSubscriptionManager = (SubscriptionManager)DeviceApplication.getApp().getSystemService("telephony_subscription_service");
            List activeSubscriptionInfoList = null;
            if (mSubscriptionManager != null) {
                try {
                    activeSubscriptionInfoList = mSubscriptionManager.getActiveSubscriptionInfoList();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() > 0) {
                for (int i = 0; i < activeSubscriptionInfoList.size(); ++i) {
                    SubscriptionInfo subscriptionInfo = (SubscriptionInfo)activeSubscriptionInfoList.get(i);
                    if (i == 0) {
                        simCardData.imsi1 = GeneralUtils.getIMSI(subscriptionInfo.getSimSlotIndex());
                        simCardData.sim_country_iso1 = subscriptionInfo.getCountryIso();
                        simCardData.sim_serial_number1 = subscriptionInfo.getIccId();
                        simCardData.number1 = subscriptionInfo.getNumber();
                        continue;
                    }
                    simCardData.imsi2 = GeneralUtils.getIMSI(subscriptionInfo.getSimSlotIndex());
                    simCardData.sim_country_iso2 = subscriptionInfo.getCountryIso();
                    simCardData.sim_serial_number2 = subscriptionInfo.getIccId();
                    simCardData.number2 = subscriptionInfo.getNumber();
                }
            }
        }
        return simCardData;
    }

    public static String getImei() {
        return GeneralUtils.getImeiOrMeid(true);
    }

    public static String getMeid() {
        return GeneralUtils.getImeiOrMeid(false);
    }

    public static String getImeiOrMeid(boolean isImei) {
        if (GeneralUtils.isChekSelfPermission("android.permission.READ_PHONE_STATE")) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        TelephonyManager tm = GeneralUtils.getTelephonyManager();
        if (Build.VERSION.SDK_INT >= 26) {
            if (isImei) {
                return GeneralUtils.getMinOne(tm.getImei(0), tm.getImei(1));
            }
            return GeneralUtils.getMinOne(tm.getMeid(0), tm.getMeid(1));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            String ids = GeneralUtils.getSystemPropertyByReflect(isImei ? "ril.gsm.imei" : "ril.cdma.meid");
            if (!TextUtils.isEmpty((CharSequence)ids)) {
                String[] idArr = ids.split(",");
                if (idArr.length == 2) {
                    return GeneralUtils.getMinOne(idArr[0], idArr[1]);
                }
                return idArr[0];
            }
            String id0 = tm.getDeviceId();
            String id1 = "";
            try {
                Method method = tm.getClass().getMethod("getDeviceId", Integer.TYPE);
                id1 = (String)method.invoke(tm, isImei ? 1 : 2);
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (isImei) {
                if (id0 != null && id0.length() < 15) {
                    id0 = "";
                }
                if (id1 != null && id1.length() < 15) {
                    id1 = "";
                }
            } else {
                if (id0 != null && id0.length() == 14) {
                    id0 = "";
                }
                if (id1 != null && id1.length() == 14) {
                    id1 = "";
                }
            }
            return GeneralUtils.getMinOne(id0, id1);
        }
        String deviceId = tm.getDeviceId();
        if (isImei ? deviceId != null && deviceId.length() >= 15 : deviceId != null && deviceId.length() == 14) {
            return deviceId;
        }
        return "";
    }

    private static String getMinOne(String s0, String s1) {
        boolean empty0 = TextUtils.isEmpty((CharSequence)s0);
        boolean empty1 = TextUtils.isEmpty((CharSequence)s1);
        if (empty0 && empty1) {
            return "";
        }
        if (!empty0 && !empty1) {
            if (s0.compareTo(s1) <= 0) {
                return s0;
            }
            return s1;
        }
        if (!empty0) {
            return s0;
        }
        return s1;
    }

    private static String getSystemPropertyByReflect(String key) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method getMethod = clz.getMethod("get", String.class, String.class);
            return (String)getMethod.invoke(clz, key, "");
        }
        catch (Exception exception) {
            return "";
        }
    }

    public static String getCidNumbers() {
        if (GeneralUtils.getTelephonyManager().getPhoneType() == 1) {
            if (ActivityCompat.checkSelfPermission((Context)DeviceApplication.getApp(), (String)"android.permission.ACCESS_FINE_LOCATION") != 0) {
                return "";
            }
            GsmCellLocation location = (GsmCellLocation)GeneralUtils.getTelephonyManager().getCellLocation();
            if (location != null) {
                return String.valueOf(location.getCid());
            }
        }
        return "";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getLocalDNS() {
        Process cmdProcess = null;
        BufferedReader reader = null;
        String dnsIP = "";
        try {
            cmdProcess = Runtime.getRuntime().exec("getprop net.dns1");
            reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            String string2 = dnsIP = reader.readLine();
            return string2;
        }
        catch (IOException e) {
            String string3 = null;
            return string3;
        }
        finally {
            try {
                reader.close();
            }
            catch (IOException iOException) {}
            cmdProcess.destroy();
        }
    }

    public static String getUUids() {
        return UUID.randomUUID().toString();
    }

    public static String getMyUUID() {
        String tmSerial;
        String tmDevice;
        if (GeneralUtils.isChekSelfPermission("android.permission.READ_PHONE_STATE")) {
            return "";
        }
        TelephonyManager tm = GeneralUtils.getTelephonyManager();
        if (Build.VERSION.SDK_INT >= 29) {
            tmDevice = "";
            tmSerial = "";
        } else {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
        }
        String androidId = "" + Settings.Secure.getString((ContentResolver)DeviceApplication.getApp().getContentResolver(), (String)"android_id");
        UUID deviceUuid = new UUID(androidId.hashCode(), (long)tmDevice.hashCode() << 32 | (long)tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    public static String getIMEI(int slotId) {
        try {
            TelephonyManager manager = GeneralUtils.getTelephonyManager();
            Method method = manager.getClass().getMethod("getImei", Integer.TYPE);
            String imei = (String)method.invoke(manager, slotId);
            return imei;
        }
        catch (Exception e) {
            return "";
        }
    }

    public static void getGaid() {
        Executors.newSingleThreadExecutor().execute(new Runnable(){

            @Override
            public void run() {
                try {
                    gaid = AdvertisingIdClient.getGoogleAdId((Context)DeviceApplication.getApp());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String getUiModeType() {
        UiModeManager mUiModeManager = (UiModeManager)DeviceApplication.getApp().getSystemService("uimode");
        int uiMode = mUiModeManager.getCurrentModeType();
        switch (uiMode) {
            case 0: {
                return "UI_MODE_TYPE_UNDEFINED";
            }
            case 1: {
                return "UI_MODE_TYPE_NORMAL";
            }
            case 2: {
                return "UI_MODE_TYPE_DESK";
            }
            case 3: {
                return "UI_MODE_TYPE_CAR";
            }
            case 4: {
                return "UI_MODE_TYPE_TELEVISION";
            }
            case 5: {
                return "UI_MODE_TYPE_APPLIANCE";
            }
            case 6: {
                return "UI_MODE_TYPE_WATCH";
            }
            case 7: {
                return "UI_MODE_TYPE_VR_HEADSET";
            }
        }
        return Integer.toString(uiMode);
    }

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager)DeviceApplication.getApp().getSystemService("phone");
    }

    public static enum NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_5G,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO;

    }
}


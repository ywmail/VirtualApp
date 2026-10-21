/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 */
package com.kook.deviceinfo.data;

import android.content.Context;
import android.os.Build;
import com.kook.deviceinfo.DeviceApplication;
import com.kook.deviceinfo.impClasses.BuildInfo;
import com.kook.deviceinfo.util.GeneralUtils;
import com.kook.deviceinfo.util.LanguageUtils;
import com.kook.deviceinfo.util.NetWorkUtils;
import com.kook.deviceinfo.util.OtherUtils;

public class GeneralData {
    public String and_id = GeneralUtils.getAndroidID();
    public String gaid = GeneralUtils.gaid;
    public String network_operator_name = GeneralUtils.getNetworkOperatorName();
    public String network_operator = GeneralUtils.getNetworkOperator();
    public String network_type = GeneralUtils.getNetworkType();
    public String phone_type = GeneralUtils.getPhoneType();
    public String mcc = GeneralUtils.getMcc();
    public String bluetooth_mac;
    public String bluetooth_name;
    public String mnc = GeneralUtils.getMnc();
    public String locale_iso_3_language;
    public String locale_iso_3_country;
    public String time_zone_id;
    public String locale_display_language;
    public String cid = GeneralUtils.getCidNumbers();
    public String dns = GeneralUtils.getLocalDNS();
    public String uuid = GeneralUtils.getMyUUID();
    public int slot_count = OtherUtils.getPhoneSimCount();
    public String meid = GeneralUtils.getMeid();
    public String imei1;
    public String imei2;
    public String imsi;
    public String mac;
    public String language;
    public String ui_mode_type;
    public int screenHeight;
    public int screenWidth;
    public String security_patch;

    public GeneralData() {
        this.locale_iso_3_country = LanguageUtils.getSystemLanguage().getISO3Country();
        this.locale_iso_3_language = LanguageUtils.getSystemLanguage().getISO3Language();
        this.locale_display_language = LanguageUtils.getSystemLanguage().getDisplayLanguage();
        this.language = LanguageUtils.getSystemLanguage().getLanguage();
        this.imei1 = GeneralUtils.getIMEI(0);
        this.imei2 = GeneralUtils.getIMEI(1);
        this.imsi = GeneralUtils.getIMSI();
        this.ui_mode_type = GeneralUtils.getUiModeType();
        this.time_zone_id = LanguageUtils.getCurrentTimeZone();
        this.mac = NetWorkUtils.getMacAddress();
        this.bluetooth_mac = NetWorkUtils.getBluetoothMac();
        this.bluetooth_name = NetWorkUtils.getBluetoothName((Context)DeviceApplication.getApp());
        this.screenHeight = BuildInfo.getScreenHeight((Context)DeviceApplication.getApp());
        this.screenWidth = BuildInfo.getScreenWidth((Context)DeviceApplication.getApp());
        if (Build.VERSION.SDK_INT >= 23) {
            this.security_patch = Build.VERSION.SECURITY_PATCH;
        }
    }
}


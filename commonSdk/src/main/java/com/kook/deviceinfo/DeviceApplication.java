/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Application
 */
package com.kook.deviceinfo;

import android.annotation.SuppressLint;
import android.app.Application;
import com.kook.deviceinfo.util.GeneralUtils;

public class DeviceApplication {
    @SuppressLint(value={"StaticFieldLeak"})
    private static Application sApp;
    public static String deviceId;
    public static boolean mRegisterTag;
    private Application mApplication;
    @SuppressLint(value={"StaticFieldLeak"})
    private static DeviceApplication gCore;

    public static DeviceApplication get() {
        return gCore;
    }

    public void startup(Application application) {
        sApp = application;
        GeneralUtils.getGaid();
        DeviceApplication.initBoadcast();
    }

    public static Application getApp() {
        if (sApp != null) {
            return sApp;
        }
        if (sApp == null) {
            throw new NullPointerException("reflect failed.");
        }
        return sApp;
    }

    public static void initBoadcast() {
        mRegisterTag = true;
    }

    public static void removeBoadcast() {
        if (mRegisterTag) {
            mRegisterTag = false;
        }
    }

    static {
        deviceId = "";
        mRegisterTag = false;
        gCore = new DeviceApplication();
    }
}


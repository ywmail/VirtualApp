/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Environment
 *  android.text.TextUtils
 */
package com.lody.virtual.helper.utils;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class OSUtils {
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final OSUtils sOSUtils = new OSUtils();
    private boolean emui;
    private boolean miui;
    private boolean flyme;
    private boolean androidQ = false;
    private String miuiVersion;

    private OSUtils() {
        Properties properties;
        try {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }
        catch (IOException e) {
            properties = null;
        }
        if (properties != null) {
            this.emui = !TextUtils.isEmpty((CharSequence)properties.getProperty("ro.build.version.emui"));
            this.miuiVersion = properties.getProperty("ro.miui.ui.version.code");
            this.miui = !TextUtils.isEmpty((CharSequence)this.miuiVersion) || !TextUtils.isEmpty((CharSequence)properties.getProperty("ro.miui.ui.version.name")) || !TextUtils.isEmpty((CharSequence)properties.getProperty("ro.miui.internal.storage"));
        }
        this.flyme = this.hasFlyme();
        try {
            Class.forName("android.app.IActivityTaskManager");
            this.androidQ = true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
    }

    public static OSUtils getInstance() {
        return sOSUtils;
    }

    public String getMiuiVersion() {
        return this.miuiVersion;
    }

    public boolean isEmui() {
        return this.emui;
    }

    public boolean isMiui() {
        return this.miui;
    }

    public boolean isFlyme() {
        return this.flyme;
    }

    public boolean isAndroidQ() {
        return this.androidQ;
    }

    private boolean hasFlyme() {
        try {
            Method method = Build.class.getMethod("hasSmartBar", new Class[0]);
            return method != null;
        }
        catch (Exception e) {
            return false;
        }
    }
}


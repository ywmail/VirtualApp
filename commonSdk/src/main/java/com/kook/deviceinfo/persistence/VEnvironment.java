/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.os.Environment
 *  android.widget.Toast
 *  androidx.core.app.ActivityCompat
 */
package com.kook.deviceinfo.persistence;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import java.io.File;
import mirror.android.app.ActivityThread;

public class VEnvironment {
    public static String SYSTEM_DIRECTORY = "/system/kook/";
    public static String SYSTEM_DIRECTORY_DOWLOAD_MOCK = "/data/data/" + ActivityThread.currentPackageName.call(new Object[0]) + "/download/";
    public static String SYSTEM_MOCK_STORAGE = "/data/data/" + ActivityThread.currentPackageName.call(new Object[0]) + "/mock_config.ini";
    private static VEnvironment mVEnvironment = new VEnvironment();

    public static File buildPath(File base, String ... segments) {
        File cur = base;
        for (String segment : segments) {
            cur = cur == null ? new File(segment) : new File(cur, segment);
        }
        return cur;
    }

    public String getDownloadDirectoryPath() {
        if (VEnvironment.isExternalStorageAvailable()) {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory((String)Environment.DIRECTORY_DOWNLOADS);
            if (!externalStoragePublicDirectory.exists()) {
                externalStoragePublicDirectory.mkdirs();
            }
            return externalStoragePublicDirectory.getAbsolutePath();
        }
        return "";
    }

    public String getPersisteceDataPath() {
        String packageName = ActivityThread.currentPackageName.call(new Object[0]);
        File host = new File(SYSTEM_DIRECTORY);
        if (host.exists() && host.canRead() && host.canWrite()) {
            File hostdir = new File(SYSTEM_DIRECTORY + packageName + File.separator);
            if (!hostdir.exists()) {
                hostdir.mkdirs();
            }
            return "/data/data/" + packageName + "/";
        }
        return "/data/data/" + packageName + "/";
    }

    public String getPersisteceExternalStoragePath() {
        Application application = ActivityThread.currentApplication.call(new Object[0]);
        if (ActivityCompat.checkSelfPermission((Context)application, (String)"android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return Environment.getExternalStorageDirectory() + File.separator + "mockdata/";
        }
        Toast.makeText((Context)application, (CharSequence)"\u8bf7\u6388\u4e88\u5b58\u50a8\u6743\u9650", (int)1).show();
        return null;
    }

    public static VEnvironment get() {
        return mVEnvironment;
    }

    public File getControlFile() {
        return new File(this.getPersisteceDataPath(), "control-config.ini");
    }

    public File getDeviceInfoFile() {
        return new File(this.getPersisteceDataPath(), "device-config.ini");
    }

    public File getLibraryConfigFile() {
        return new File(this.getPersisteceDataPath(), "library-config.ini");
    }

    public File getSystemSettingConfigFile() {
        return new File(this.getPersisteceDataPath(), "setting-config.ini");
    }

    public File getVirtualLocationFile() {
        return new File(this.getPersisteceDataPath(), "virtual-loc.ini");
    }

    public File getTabMenuManagerFile() {
        return new File(this.getPersisteceDataPath(), "tab-menu-manager.ini");
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return "mounted".equals(state);
    }
}


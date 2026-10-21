/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Environment
 *  org.zeroturnaround.zip.NameMapper
 *  org.zeroturnaround.zip.ZipException
 *  org.zeroturnaround.zip.ZipUtil
 */
package com.carlos.common.utils.xapk;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.carlos.common.utils.xapk.XAPKUtils;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import java.io.File;
import java.util.ArrayList;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipException;
import org.zeroturnaround.zip.ZipUtil;

public class XAPKInstaller {
    public static void doInstallApk(Context context, String xapkFilePath) {
        if (xapkFilePath.isEmpty()) {
            return;
        }
        File xapkFile = new File(xapkFilePath);
        String unzipOutputDirPath = XAPKInstaller.getUnzipOutputDirPath(xapkFile);
        if (unzipOutputDirPath.isEmpty()) {
            return;
        }
        File unzipOutputDir = new File(unzipOutputDirPath);
        ZipUtil.unpack((File)xapkFile, (File)unzipOutputDir, (NameMapper)new NameMapper(){

            public String map(String name) {
                if (name.endsWith(".apk")) {
                    return name;
                }
                return null;
            }
        });
        File[] files = unzipOutputDir.listFiles();
        int apkSize = 0;
        for (File file : files) {
            if (!file.isFile() || !file.getName().endsWith(".apk")) continue;
            ++apkSize;
        }
        XAPKInstaller.unzipObbToAndroidObbDir(xapkFile, new File(XAPKInstaller.getMobileAndroidObbDir()));
        HVLog.i("yzh", "apkSize:  " + apkSize);
        if (apkSize > 0) {
            XAPKInstaller.doInstallApk(context, xapkFilePath, unzipOutputDir);
        }
    }

    private static String getUnzipOutputDirPath(File file) {
        String filePathPex = file.getParent() + File.separator;
        String unzipOutputDir = filePathPex + XAPKUtils.getFileNameNoExtension(file);
        HVLog.d("");
        boolean result = XAPKUtils.createOrExistsDir(unzipOutputDir);
        if (result) {
            return unzipOutputDir;
        }
        return null;
    }

    private static boolean unzipObbToAndroidObbDir(File xapkFile, File unzipOutputDir) {
        final String prefix = "Android/obb";
        ZipUtil.unpack((File)xapkFile, (File)unzipOutputDir, (NameMapper)new NameMapper(){

            public String map(String name) {
                if (name.startsWith(prefix)) {
                    return name.substring(prefix.length());
                }
                return null;
            }
        });
        return true;
    }

    public static String getMobileAndroidObbDir() {
        String path = XAPKInstaller.isSDCardEnableByEnvironment() ? Environment.getExternalStorageDirectory().getPath() + File.separator + "Android" + File.separator + "obb" : Environment.getDataDirectory().getParent().toString() + File.separator + "Android" + File.separator + "obb";
        XAPKUtils.createOrExistsDir(path);
        return path;
    }

    private static boolean isSDCardEnableByEnvironment() {
        return "mounted" == Environment.getExternalStorageState();
    }

    private static void doInstallApk(Context context, String xapkPath, File xapkUnzipOutputDir) {
        try {
            File[] files = xapkUnzipOutputDir.listFiles();
            if (files == null || files.length < 1) {
                return;
            }
            ArrayList<String> apkFilePaths = new ArrayList<String>();
            for (File file : files) {
                if (file == null || !file.isFile() || !file.getName().endsWith(".apk")) continue;
                apkFilePaths.add(file.getAbsolutePath());
            }
            Intent intent = new Intent("android.intent.action.InstallActivity");
            intent.putExtra("xapk_path", xapkPath);
            intent.putStringArrayListExtra("apk_path", apkFilePaths);
            intent.addFlags(0x10000000);
            context.startActivity(intent);
        }
        catch (ZipException e) {
            e.printStackTrace();
        }
    }
}


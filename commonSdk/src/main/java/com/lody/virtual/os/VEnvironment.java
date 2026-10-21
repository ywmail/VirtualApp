/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.Base64
 */
package com.lody.virtual.os;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.EncodeUtils;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.os.VUserHandle;
import java.io.File;
import java.io.IOException;

public class VEnvironment {
    private static final String TAG;
    private static final File ROOT;
    private static final File DATA_DIRECTORY;
    private static final File DATA_USER_DIRECTORY;
    private static final File DATA_USER_DE_DIRECTORY;
    private static final File DATA_APP_DIRECTORY;
    private static final File DATA_APP_SYSTEM_DIRECTORY;
    private static final File FRAMEWORK_DIRECTORY;
    private static final File ROOT_EXT;
    private static final File DATA_DIRECTORY_EXT;
    private static final File DATA_APP_DIRECTORY_EXT;
    private static final File DATA_USER_DIRECTORY_EXT;
    private static final File DATA_USER_DE_DIRECTORY_EXT;
    private static final File FRAMEWORK_DIRECTORY_EXT;
    public static int OUTSIDE_APP_UID;
    public static int UNKNOWN_APP_UID;
    public static int SYSTEM_UID;

    public static File buildPath(File base, String ... segments) {
        File cur = base;
        for (String segment : segments) {
            cur = cur == null ? new File(segment) : new File(cur, segment);
        }
        return cur;
    }

    public static void systemReady() {
        FileUtils.ensureDirCreate(DATA_DIRECTORY);
        FileUtils.ensureDirCreate(DATA_APP_DIRECTORY);
        FileUtils.ensureDirCreate(DATA_APP_SYSTEM_DIRECTORY);
        FileUtils.ensureDirCreate(DATA_USER_DIRECTORY);
        FileUtils.ensureDirCreate(DATA_USER_DE_DIRECTORY);
        FileUtils.ensureDirCreate(FRAMEWORK_DIRECTORY);
        FileUtils.chmod(ROOT.getAbsolutePath(), 493);
        FileUtils.chmod(DATA_DIRECTORY.getAbsolutePath(), 493);
        FileUtils.chmod(DATA_APP_DIRECTORY.getAbsolutePath(), 493);
    }

    private static Context getContext() {
        return VirtualCore.get().getContext();
    }

    public static void chmodPackageDictionary(File packageFile) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                if (FileUtils.isSymlink(packageFile)) {
                    return;
                }
                FileUtils.chmod(packageFile.getParentFile().getAbsolutePath(), 493);
                FileUtils.chmod(packageFile.getAbsolutePath(), 493);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getRoot() {
        return ROOT;
    }

    public static File getRootExt() {
        return ROOT_EXT;
    }

    public static File getDataUserPackageDirectory(int userId, String packageName) {
        return VEnvironment.buildPath(VEnvironment.getDataUserDirectory(userId), packageName);
    }

    public static File getDataUserPackageDirectoryExt(int userId, String packageName) {
        return VEnvironment.buildPath(VEnvironment.getDataUserDirectoryExt(userId), packageName);
    }

    public static File getDeDataUserPackageDirectory(int userId, String packageName) {
        return VEnvironment.buildPath(VEnvironment.getDataUserDeDirectory(userId), packageName);
    }

    public static File getDeDataUserPackageDirectoryRoot(int userId) {
        return VEnvironment.buildPath(VEnvironment.getDataUserDeDirectory(userId), new String[0]);
    }

    public static File getDeDataUserPackageDirectoryExt(int userId, String packageName) {
        return VEnvironment.buildPath(VEnvironment.getDeDataUserDirectoryExt(userId), packageName);
    }

    public static File getDeDataUserPackageDirectoryExtRoot(int userId) {
        return VEnvironment.buildPath(VEnvironment.getDeDataUserDirectoryExt(userId), new String[0]);
    }

    public static File getDataAppLibDirectoryExt(String packageName) {
        return VEnvironment.buildPath(VEnvironment.getDataAppPackageDirectoryExt(packageName), "lib");
    }

    public static File getPackageFile(String packageName) {
        return new File(VEnvironment.getDataAppPackageDirectory(packageName), EncodeUtils.decodeBase64("YmFzZS5hcGs="));
    }

    public static String getPackageFileStub(String packageName) {
        if (BuildCompat.isOreo()) {
            return String.format(EncodeUtils.decodeBase64("L2RhdGEvYXBwLyVzLSVzL2Jhc2UuYXBr"), packageName, Base64.encodeToString((byte[])packageName.getBytes(), (int)10));
        }
        return String.format(EncodeUtils.decodeBase64("L2RhdGEvYXBwLyVzLTEvYmFzZS5hcGs="), packageName);
    }

    public static String getSplitFileName(String splitName) {
        return "split_" + splitName + ".apk";
    }

    public static File getSplitPackageFile(String packageName, String splitName) {
        return new File(VEnvironment.getDataAppPackageDirectory(packageName), VEnvironment.getSplitFileName(splitName));
    }

    public static File getPackageFileExt(String packageName) {
        return new File(VEnvironment.getDataAppPackageDirectoryExt(packageName), EncodeUtils.decodeBase64("YmFzZS5hcGs="));
    }

    public static File getSplitPackageFileExt(String packageName, String splitName) {
        return new File(VEnvironment.getDataAppPackageDirectoryExt(packageName), VEnvironment.getSplitFileName(splitName));
    }

    public static File getDataAppDirectory() {
        return DATA_APP_DIRECTORY;
    }

    public static File getDataAppDirectoryExt() {
        return DATA_APP_DIRECTORY_EXT;
    }

    public static File getDingConfigFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "ding-config.ini");
    }

    public static File getUidListFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "uid-list.ini");
    }

    public static File getBakUidListFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "uid-list.ini.bak");
    }

    public static File getAccountConfigFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "account-list.ini");
    }

    public static File getSyncDirectory() {
        return VEnvironment.buildPath(DATA_APP_SYSTEM_DIRECTORY, "sync");
    }

    public static File getAccountVisibilityConfigFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "account-visibility-list.ini");
    }

    public static File getVirtualLocationFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "virtual-loc.ini");
    }

    public static File getDeviceInfoFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "device-config.ini");
    }

    public static File getPackageListFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "packages.ini");
    }

    public static File getVSConfigFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "vss.ini");
    }

    public static File getJobConfigFile() {
        return new File(DATA_APP_SYSTEM_DIRECTORY, "job-list.ini");
    }

    public static File getOatDirectory(String packageName) {
        return VEnvironment.buildPath(VEnvironment.getDataAppPackageDirectory(packageName), "oat");
    }

    public static File getOatDirectoryExt(String packageName) {
        return VEnvironment.buildPath(VEnvironment.getDataAppPackageDirectoryExt(packageName), "oat");
    }

    public static File getOatFile(String packageName, String instructionSet) {
        return VEnvironment.buildPath(VEnvironment.getOatDirectory(packageName), instructionSet, "base.odex");
    }

    public static File getOatFileExt(String packageName, String instructionSet) {
        return VEnvironment.buildPath(VEnvironment.getOatDirectoryExt(packageName), instructionSet, "base.odex");
    }

    public static File getDataAppPackageDirectory(String packageName) {
        return VEnvironment.buildPath(DATA_APP_DIRECTORY, packageName);
    }

    public static File getDataAppPackageDirectoryExt(String packageName) {
        return VEnvironment.buildPath(DATA_APP_DIRECTORY_EXT, packageName);
    }

    public static File getDataAppLibDirectory(String packageName) {
        return VEnvironment.buildPath(VEnvironment.getDataAppPackageDirectory(packageName), "lib");
    }

    public static File getUserAppLibDirectory(int userId, String packageName) {
        return new File(VEnvironment.getDataUserPackageDirectory(userId, packageName), "lib");
    }

    public static File getUserAppLibDirectoryExt(int userId, String packageName) {
        return new File(VEnvironment.getDataUserPackageDirectoryExt(userId, packageName), "lib");
    }

    public static File getPackageCacheFile(String packageName) {
        return new File(VEnvironment.getDataAppPackageDirectory(packageName), "package.ini");
    }

    public static File getSignatureFile(String packageName) {
        return new File(VEnvironment.getDataAppPackageDirectory(packageName), "signature.ini");
    }

    public static File getFrameworkDirectory(String name) {
        return VEnvironment.buildPath(FRAMEWORK_DIRECTORY, name);
    }

    public static File getOptimizedFrameworkFile(String name) {
        return new File(VEnvironment.getFrameworkDirectory(name), "classes.dex");
    }

    public static File getFrameworkFile(String name) {
        return new File(VEnvironment.getFrameworkDirectory(name), "extracted.jar");
    }

    public static File getDataUserDirectory() {
        return DATA_USER_DIRECTORY;
    }

    public static File getDataUserDirectoryExt() {
        return DATA_USER_DIRECTORY_EXT;
    }

    public static File getDataUserDeDirectory() {
        return DATA_USER_DE_DIRECTORY;
    }

    public static File getDataUserDirectory(int userId) {
        return VEnvironment.buildPath(DATA_USER_DIRECTORY, String.valueOf(userId));
    }

    public static File getDataUserDeDirectory(int userId) {
        return VEnvironment.buildPath(DATA_USER_DE_DIRECTORY, String.valueOf(userId));
    }

    public static File getDataUserDirectoryExt(int userId) {
        return VEnvironment.buildPath(DATA_USER_DIRECTORY_EXT, String.valueOf(userId));
    }

    public static File getDeDataUserDirectoryExt() {
        return DATA_USER_DE_DIRECTORY_EXT;
    }

    public static File getDeDataUserDirectoryExt(int userId) {
        return VEnvironment.buildPath(DATA_USER_DE_DIRECTORY_EXT, String.valueOf(userId));
    }

    public static File getDataSystemDirectory(int userId) {
        return VEnvironment.buildPath(VEnvironment.getDataUserDirectory(userId), "system");
    }

    public static File getDataSystemDirectoryExt(int userId) {
        return VEnvironment.buildPath(VEnvironment.getDataUserDirectoryExt(userId), "system");
    }

    public static File getWifiMacFile(int userId, boolean isExt) {
        if (isExt) {
            return new File(VEnvironment.getDataSystemDirectoryExt(userId), "wifiMacAddress");
        }
        return new File(VEnvironment.getDataSystemDirectory(userId), "wifiMacAddress");
    }

    public static File getDataDirectory() {
        return DATA_DIRECTORY;
    }

    public static File getPackageInstallerStageDir() {
        return VEnvironment.buildPath(DATA_APP_SYSTEM_DIRECTORY, ".session_dir");
    }

    public static File getNativeCacheDir(boolean isExt) {
        return VEnvironment.buildPath(isExt ? ROOT_EXT : ROOT, ".native");
    }

    public static File getFrameworkDirectory() {
        return FRAMEWORK_DIRECTORY;
    }

    public static File getFrameworkDirectoryExt() {
        return FRAMEWORK_DIRECTORY_EXT;
    }

    public static File getSystemSettingsFile(int userId) {
        return new File(VEnvironment.getDataSystemDirectory(userId), "settings.ini");
    }

    public static boolean enableMediaRedirect() {
        return false;
    }

    static {
        File hostUserDir;
        TAG = VEnvironment.class.getSimpleName();
        OUTSIDE_APP_UID = 9000;
        UNKNOWN_APP_UID = 9001;
        SYSTEM_UID = 1000;
        OUTSIDE_APP_UID = VUserHandle.getUid(VUserHandle.realUserId(), OUTSIDE_APP_UID);
        UNKNOWN_APP_UID = VUserHandle.getUid(VUserHandle.realUserId(), UNKNOWN_APP_UID);
        SYSTEM_UID = VUserHandle.getUid(VUserHandle.realUserId(), SYSTEM_UID);
        try {
            hostUserDir = new File(VEnvironment.getContext().getApplicationInfo().dataDir).getCanonicalFile().getParentFile();
        }
        catch (IOException e) {
            hostUserDir = new File(VEnvironment.getContext().getApplicationInfo().dataDir).getParentFile();
        }
        File host = new File(hostUserDir, StubManifest.PACKAGE_NAME);
        ROOT = VEnvironment.buildPath(host, "virtual");
        FRAMEWORK_DIRECTORY = VEnvironment.buildPath(ROOT, "framework");
        DATA_DIRECTORY = VEnvironment.buildPath(ROOT, "data");
        DATA_USER_DIRECTORY = VEnvironment.buildPath(DATA_DIRECTORY, "user");
        DATA_USER_DE_DIRECTORY = VEnvironment.buildPath(DATA_DIRECTORY, "user_de");
        DATA_APP_DIRECTORY = VEnvironment.buildPath(DATA_DIRECTORY, "app");
        DATA_APP_SYSTEM_DIRECTORY = VEnvironment.buildPath(DATA_APP_DIRECTORY, "system");
        File hostExt = new File(hostUserDir, StubManifest.EXT_PACKAGE_NAME);
        ROOT_EXT = VEnvironment.buildPath(hostExt, "virtual");
        DATA_DIRECTORY_EXT = VEnvironment.buildPath(ROOT_EXT, "data");
        DATA_USER_DIRECTORY_EXT = VEnvironment.buildPath(DATA_DIRECTORY_EXT, "user");
        DATA_APP_DIRECTORY_EXT = VEnvironment.buildPath(DATA_DIRECTORY_EXT, "app");
        DATA_USER_DE_DIRECTORY_EXT = VEnvironment.buildPath(DATA_DIRECTORY_EXT, "user_de");
        FRAMEWORK_DIRECTORY_EXT = VEnvironment.buildPath(ROOT_EXT, "framework");
    }
}


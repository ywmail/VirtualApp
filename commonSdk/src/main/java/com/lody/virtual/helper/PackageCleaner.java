/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.InstalledAppInfo;
import java.io.File;
import java.util.HashSet;
import java.util.List;

public class PackageCleaner {
    public static void cleanUsers(File usersDir) {
        List<VUserInfo> users = VUserManager.get().getUsers();
        HashSet<Integer> userIds = new HashSet<Integer>(users.size());
        for (VUserInfo user : users) {
            userIds.add(user.id);
        }
        File[] userDirs = usersDir.listFiles();
        if (userDirs == null) {
            return;
        }
        for (File userDir : userDirs) {
            boolean isExist = false;
            try {
                Integer id2 = Integer.parseInt(userDir.getName());
                if (userIds.contains(id2)) {
                    isExist = true;
                }
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (isExist) continue;
            FileUtils.deleteDir(userDir);
        }
    }

    public static void cleanUninstalledPackages() {
        List<InstalledAppInfo> appInfos = VirtualCore.get().getInstalledApps(0);
        HashSet<String> installedPackageNames = new HashSet<String>(appInfos.size());
        for (InstalledAppInfo info : appInfos) {
            installedPackageNames.add(info.packageName);
        }
        File dataAppDirectory = VEnvironment.getDataAppDirectoryExt();
        File[] appDirs = dataAppDirectory.listFiles();
        if (appDirs != null) {
            for (File appDir : appDirs) {
                String packageName = appDir.getName();
                if (packageName.equals("system") || installedPackageNames.contains(packageName)) continue;
                PackageCleaner.cleanAllUserPackage(VEnvironment.getDataUserDirectoryExt(), packageName);
                PackageCleaner.cleanAllUserPackage(VEnvironment.getDeDataUserDirectoryExt(), packageName);
                FileUtils.deleteDir(appDir);
            }
        }
    }

    public static void cleanAllUserPackage(File usersDir, String packageName) {
        File[] userDirs = usersDir.listFiles();
        if (userDirs == null) {
            return;
        }
        for (File userDir : userDirs) {
            File userPackageDir = new File(userDir, packageName);
            if (!userPackageDir.exists()) continue;
            FileUtils.deleteDir(userPackageDir);
        }
    }
}


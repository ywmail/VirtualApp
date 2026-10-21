//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.extension;

import android.app.ActivityManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.os.RemoteException;
import android.os.Build.VERSION;
import com.lody.virtual.IExtHelperInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.ipc.FileTransfer;
import com.lody.virtual.helper.DexOptimizer;
import com.lody.virtual.helper.PackageCleaner;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.InstalledAppInfo;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class VExtPackageHelper extends ContentProvider {
    private static final String TAG = VExtPackageHelper.class.getSimpleName();
    private final Binder mExtHelperInterface = new IExtHelperInterface.Stub() {
        public void copyPackage(InstalledAppInfo appInfo) {
            String packageName = appInfo.packageName;
            VLog.e(VExtPackageHelper.TAG, "copyPackage: " + packageName);
            FileUtils.ensureDirCreate(new File[]{VEnvironment.getDataAppPackageDirectoryExt(packageName), VEnvironment.getDataAppLibDirectoryExt(packageName)});
            FileTransfer fileTransfer = FileTransfer.get();
            fileTransfer.copyFile(VEnvironment.getPackageFile(packageName), VEnvironment.getPackageFileExt(packageName));
            Iterator var4 = appInfo.getSplitNames().iterator();

            while(var4.hasNext()) {
                String splitName = (String)var4.next();
                fileTransfer.copyFile(VEnvironment.getSplitPackageFile(packageName, splitName), VEnvironment.getSplitPackageFileExt(packageName, splitName));
            }

            fileTransfer.copyDirectory(VEnvironment.getDataAppLibDirectory(packageName), VEnvironment.getDataAppLibDirectoryExt(packageName));
            if (VirtualCore.get().isRunInExtProcess(packageName)) {
                String instructionSet = VirtualRuntime.getCurrentInstructionSet();

                try {
                    DexOptimizer.dex2oat(VEnvironment.getPackageFileExt(packageName).getPath(), VEnvironment.getOatFileExt(packageName, instructionSet).getPath());
                } catch (IOException var6) {
                    var6.printStackTrace();
                }
            }

        }

        public int syncPackages() {
            if (VirtualCore.get().isSharedUserId()) {
                return 0;
            } else {
                synchronized(this) {
                    PackageCleaner.cleanUsers(VEnvironment.getDataUserDirectoryExt());
                    PackageCleaner.cleanUninstalledPackages();
                    Iterator var2 = VirtualCore.get().getInstalledApps(0).iterator();

                    while(true) {
                        InstalledAppInfo info;
                        boolean splitChanged;
                        File appDir;
                        do {
                            if (!var2.hasNext()) {
                                return 0;
                            }

                            info = (InstalledAppInfo)var2.next();
                            List<String> splitNames = info.getSplitNames();
                            splitChanged = false;
                            if (!splitNames.isEmpty()) {
                                Iterator var6 = splitNames.iterator();

                                while(var6.hasNext()) {
                                    String splitName = (String)var6.next();
                                    if (!VEnvironment.getSplitPackageFileExt(info.packageName, splitName).exists()) {
                                        splitChanged = true;
                                    }
                                }
                            }

                            appDir = VEnvironment.getDataAppPackageDirectoryExt(info.packageName);
                        } while(appDir.exists() && !splitChanged);

                        FileUtils.ensureDirCreate(appDir);
                        if (!info.dynamic) {
                            this.copyPackage(info);
                        }
                    }
                }
            }
        }

        public void cleanPackageData(int[] userIds, String packageName) {
            synchronized(this) {
                if (packageName != null && userIds != null) {
                    int[] var4 = userIds;
                    int var5 = userIds.length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        int userId = var4[var6];
                        FileUtils.deleteDir(VEnvironment.getDataUserPackageDirectoryExt(userId, packageName));
                        FileUtils.deleteDir(VEnvironment.getDeDataUserPackageDirectoryExt(userId, packageName));
                    }

                }
            }
        }

        public void forceStop(int[] pids) {
            synchronized(this) {
                int[] var3 = pids;
                int var4 = pids.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    int pid = var3[var5];
                    Process.killProcess(pid);
                }

            }
        }

        public List<ActivityManager.RunningTaskInfo> getRunningTasks(int maxNum) {
            Context context = VExtPackageHelper.this.getContext();
            if (context == null) {
                return Collections.emptyList();
            } else {
                ActivityManager am = (ActivityManager)context.getSystemService("activity");
                return am == null ? Collections.emptyList() : am.getRunningTasks(maxNum);
            }
        }

        public List<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags) {
            Context context = VExtPackageHelper.this.getContext();
            if (context == null) {
                return Collections.emptyList();
            } else {
                ActivityManager am = (ActivityManager)context.getSystemService("activity");
                return am == null ? Collections.emptyList() : am.getRecentTasks(maxNum, flags);
            }
        }

        public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() {
            Context context = VExtPackageHelper.this.getContext();
            if (context == null) {
                return Collections.emptyList();
            } else {
                ActivityManager am = (ActivityManager)context.getSystemService("activity");
                return am == null ? Collections.emptyList() : am.getRunningAppProcesses();
            }
        }

        public boolean isExternalStorageManager() throws RemoteException {
            return VERSION.SDK_INT >= 30 ? Environment.isExternalStorageManager() : true;
        }

        public void startActivity(Intent intent, Bundle options) {
            if (!VirtualCore.get().isSharedUserId()) {
                Context context = VExtPackageHelper.this.getContext();
                if (context == null) {
                    VLog.e("VA", "addon startActivity failed!! context is NULL");
                } else {
                    context.startActivity(intent, options);
                }
            }

        }
    };

    public VExtPackageHelper() {
    }

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    public Bundle call(String method, String arg, Bundle extras) {
        if (method.equals("connect")) {
            Bundle reply = new Bundle();
            BundleCompat.putBinder(reply, "_VA_|_binder_", this.mExtHelperInterface);
            return reply;
        } else {
            return null;
        }
    }
}

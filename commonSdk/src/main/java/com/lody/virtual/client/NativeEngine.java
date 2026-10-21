/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Binder
 *  android.os.Build$VERSION
 *  android.os.Process
 *  android.text.TextUtils
 *  android.util.Pair
 */
package com.lody.virtual.client;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Pair;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.DexOverride;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.hiddenapibypass.HiddenApiBypass;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.natives.NativeMethods;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.InstalledAppInfo;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NativeEngine {
    private static final String TAG;
    private static final List<DexOverride> sDexOverrides;
    private static boolean sFlag;
    private static boolean sEnabled;
    private static final String LIB_NAME;
    private static boolean EnablePidInfoCache;
    private static HashMap<Integer, PidCacheInfo> pidCache;
    private static int MaxCachePidInfoTime;
    private static int MaxCachePidInfoZise;
    private static final List<Pair<String, String>> REDIRECT_LISTS;
    public static Field artMethodField;

    public static void startDexOverride() {
        List<InstalledAppInfo> installedApps = VirtualCore.get().getInstalledApps(0);
        for (InstalledAppInfo info : installedApps) {
            if (info.dynamic) continue;
            String originDexPath = NativeEngine.getCanonicalPath(info.getApkPath());
            DexOverride override = new DexOverride(originDexPath, null, null, info.getOatPath());
            NativeEngine.addDexOverride(override);
        }
    }

    public static void addDexOverride(DexOverride dexOverride) {
        sDexOverrides.add(dexOverride);
    }

    public static String getRedirectedPath(String origPath) {
        VirtualCore.getConfig();
        try {
            return NativeEngine.nativeGetRedirectedPath(origPath);
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
            return origPath;
        }
    }

    public static String reverseRedirectedPath(String origPath) {
        VirtualCore.getConfig();
        try {
            return NativeEngine.nativeReverseRedirectedPath(origPath);
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
            return origPath;
        }
    }

    public static void redirectDirectory(String origPath, String newPath) {
        VirtualCore.getConfig();
        if (!origPath.endsWith("/")) {
            origPath = origPath + "/";
        }
        if (!newPath.endsWith("/")) {
            newPath = newPath + "/";
        }
        REDIRECT_LISTS.add((Pair<String, String>)new Pair((Object)origPath, (Object)newPath));
    }

    public static void HideSu() {
        NativeEngine.redirectFile("/system/app/Superuser.apk", "/system/app/Superuser.apk-fake");
        NativeEngine.redirectFile("/sbin/su", "/sbin/su-fake");
        NativeEngine.redirectFile("/system/bin/su", "/system/bin/su-fake");
        NativeEngine.redirectFile("/system/xbin/su", "/system/xbin/su-fake");
        NativeEngine.redirectFile("/data/local/xbin/su", "/data/local/xbin/su-fake");
        NativeEngine.redirectFile("/data/local/bin/su", "/data/local/bin/su-fake");
        NativeEngine.redirectFile("/system/sd/xbin/su", "/system/sd/xbin/su-fake");
        NativeEngine.redirectFile("/system/bin/failsafe/su", "/system/bin/failsafe/su-fake");
        NativeEngine.redirectFile("/data/local/su", "/data/local/su-fake");
        NativeEngine.redirectFile("/su/bin/su", "/su/bin/su-fake");
    }

    public static void redirectFile(String origPath, String newPath) {
        VirtualCore.getConfig();
        if (origPath.endsWith("/")) {
            origPath = origPath.substring(0, origPath.length() - 1);
        }
        if (newPath.endsWith("/")) {
            newPath = newPath.substring(0, newPath.length() - 1);
        }
        REDIRECT_LISTS.add((Pair<String, String>)new Pair((Object)origPath, (Object)newPath));
    }

    public static void readOnlyFile(String path) {
        VirtualCore.getConfig();
        if (SettingConfig.isUseNativeEngine2(VClient.get().getCurrentPackage())) {
            return;
        }
        try {
            NativeEngine.nativeIOReadOnly(path);
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static void readOnly(String path) {
        VirtualCore.getConfig();
        if (SettingConfig.isUseNativeEngine2(VClient.get().getCurrentPackage())) {
            return;
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        try {
            NativeEngine.nativeIOReadOnly(path);
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static void whitelistFile(String path) {
        VirtualCore.getConfig();
        try {
            NativeEngine.nativeIOWhitelist(path);
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static void whitelist(String path) {
        VirtualCore.getConfig();
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        try {
            NativeEngine.nativeIOWhitelist(path);
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static void forbid(String path, boolean file) {
        VirtualCore.getConfig();
        if (!file && !path.endsWith("/")) {
            path = path + "/";
        }
        try {
            NativeEngine.nativeIOForbid(path);
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static String pathCat(String path1, String path2) {
        if (!TextUtils.isEmpty((CharSequence)path2) && !path1.endsWith("/")) {
            path1 = path1 + "/";
        }
        path1 = path1 + path2;
        return path1;
    }

    public static void enableIORedirect(InstalledAppInfo appInfo) {
        ApplicationInfo coreAppInfo;
        VirtualCore.getConfig();
        if (sEnabled) {
            return;
        }
        try {
            coreAppInfo = VirtualCore.get().getHostPackageManager().getApplicationInfo(VirtualCore.getConfig().getMainPackageName(), 0L);
        }
        catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        Collections.sort(REDIRECT_LISTS, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                String a = (String)o1.first;
                String b = (String)o2.first;
                return this.compare(b.length(), a.length());
            }

            private int compare(int x, int y) {
                return Integer.compare(x, y);
            }
        });

        for (Pair<String, String> pair : REDIRECT_LISTS) {
            try {
                NativeEngine.nativeIORedirect((String)pair.first, (String)pair.second);
            }
            catch (Throwable e) {
                VLog.e(TAG, VLog.getStackTraceString(e));
            }
        }
        try {
            String extSoPath;
            String soPath = new File(coreAppInfo.nativeLibraryDir, "libvbox.so").getAbsolutePath();
            String soPath32 = extSoPath = new File(coreAppInfo.nativeLibraryDir, "libvbox_ext.so").getAbsolutePath();
            String soPath64 = soPath;
            String nativePath = VEnvironment.getNativeCacheDir(VirtualCore.get().isExtPackage()).getPath();
            NativeEngine.nativeEnableIORedirect(soPath32, soPath64, nativePath, Build.VERSION.SDK_INT, appInfo.packageName, VirtualCore.get().getHostPkg());
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
        sEnabled = true;
    }

    public static void launchEngine(Context context, String packageName) {
        VirtualCore.getConfig();
        if (sFlag) {
            return;
        }
        Object[] methods = new Object[]{NativeMethods.gNativeMask, NativeMethods.gOpenDexFileNative, NativeMethods.gCameraNativeSetup, NativeMethods.gAudioRecordNativeCheckPermission, NativeMethods.gMediaRecorderNativeSetup, NativeMethods.gAudioRecordNativeSetup, NativeMethods.gNativeLoad};
        try {
            NativeEngine.nativeLaunchEngine(context, methods, VirtualCore.get().getHostPkg(), packageName, VirtualRuntime.isArt(), BuildCompat.isR() ? 30 : Build.VERSION.SDK_INT, NativeMethods.gCameraMethodType, NativeMethods.gAudioRecordMethodType);
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
        sFlag = true;
    }

    public static void bypassHiddenAPIEnforcementPolicyIfNeeded() {
        if (BuildCompat.isR()) {
            HiddenApiBypass.setHiddenApiExemptions("L");
        } else if (BuildCompat.isPie()) {
            try {
                Method forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
                Class clazz = (Class)forNameMethod.invoke(null, "dalvik.system.VMRuntime");
                Method getMethodMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Method getRuntime = (Method)getMethodMethod.invoke(clazz, "getRuntime", new Class[0]);
                Method setHiddenApiExemptions = (Method)getMethodMethod.invoke(clazz, "setHiddenApiExemptions", new Class[]{String[].class});
                Object runtime = getRuntime.invoke(null, new Object[0]);
                setHiddenApiExemptions.invoke(runtime, new Object[]{new String[]{"L"}});
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean onKillProcess(int pid, int signal) {
        VLog.e(TAG, "killProcess: pid = %d, signal = %d.", pid, signal);
        if (pid == Process.myPid()) {
            VLog.e(TAG, VLog.getStackTraceString(new Throwable()));
        }
        return true;
    }

    public static int onGetCallingUid(int originUid) {
        try {
            return NativeEngine.onGetCallingUid0(originUid);
        }
        catch (Throwable e) {
            VLog.e("VA", e);
            return originUid;
        }
    }

    public static int onGetCallingUid0(int originUid) {
        int uidRet;
        if (VClient.get().getClientConfig() == null) {
            return originUid;
        }
        if (originUid != VirtualCore.get().myUid() && originUid != VirtualCore.get().remoteUid()) {
            return originUid;
        }
        int callingPid = Binder.getCallingPid();
        if (callingPid == 0) {
            if (BuildCompat.isS()) {
                return VClient.get().getBaseVUid();
            }
            return 9001;
        }
        if (callingPid == Process.myPid()) {
            return VClient.get().getBaseVUid();
        }
        if (callingPid == VClient.get().getCorePid()) {
            return VEnvironment.SYSTEM_UID;
        }
        if (EnablePidInfoCache) {
            long curTime = System.currentTimeMillis();
            PidCacheInfo pidCacheInfo = pidCache.get(callingPid);
            if (pidCacheInfo != null) {
                if (curTime - pidCacheInfo.lastTime > (long)MaxCachePidInfoTime) {
                    pidCache.remove(callingPid);
                } else {
                    if (pidCacheInfo.uid == -1) {
                        return originUid;
                    }
                    pidCacheInfo.lastTime = curTime;
                    return pidCacheInfo.uid;
                }
            }
            pidCache.put(callingPid, new PidCacheInfo(callingPid, -1, curTime));
        }
        if ((uidRet = VActivityManager.get().getUidByPid(callingPid)) == 9000) {
            uidRet = 1000;
        }
        if (EnablePidInfoCache) {
            long curTime2 = System.currentTimeMillis();
            if (pidCache.size() >= MaxCachePidInfoZise) {
                Iterator<Map.Entry<Integer, PidCacheInfo>> iterator = pidCache.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, PidCacheInfo> entry = iterator.next();
                    if (curTime2 - entry.getValue().lastTime <= (long)MaxCachePidInfoTime) continue;
                    iterator.remove();
                }
            }
            pidCache.put(callingPid, new PidCacheInfo(callingPid, uidRet, curTime2));
        }
        return uidRet;
    }

    private static Field getField(Class topClass, String fieldName) throws NoSuchFieldException {
        while (topClass != null && topClass != Object.class) {
            try {
                Field field = topClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            }
            catch (Exception exception) {
                topClass = topClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    public static long getArtMethod(Member member) {
        if (artMethodField == null) {
            try {
                artMethodField = NativeEngine.getField(Method.class, "artMethod");
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
        }
        if (artMethodField == null) {
            return 0L;
        }
        try {
            return (Long)artMethodField.get(member);
        }
        catch (IllegalAccessException e) {
            return 0L;
        }
    }

    private static DexOverride findDexOverride(String originDexPath) {
        for (DexOverride dexOverride : sDexOverrides) {
            if (!dexOverride.originDexPath.equals(originDexPath)) continue;
            return dexOverride;
        }
        return null;
    }

    public static void onOpenDexFileNative(String[] params) {
        String dexCanonicalPath;
        DexOverride override;
        String dexPath = params[0];
        if (dexPath != null && (override = NativeEngine.findDexOverride(dexCanonicalPath = NativeEngine.getCanonicalPath(dexPath))) != null) {
            VLog.e(TAG, "override: " + override.newOdexPath);
            if (override.newDexPath != null) {
                params[0] = override.newDexPath;
            }
            String oatPath = override.newDexPath;
            if (override.originOdexPath != null) {
                String oatCanonicalPath = NativeEngine.getCanonicalPath(oatPath);
                if (oatCanonicalPath.equals(override.originOdexPath)) {
                    params[1] = override.newOdexPath;
                }
            } else {
                params[1] = override.newOdexPath;
            }
        }
        VLog.i(TAG, "OpenDexFileNative(\"%s\", \"%s\")", params[0], params[1]);
    }

    private static String getCanonicalPath(String path) {
        File file = new File(path);
        try {
            return file.getCanonicalPath();
        }
        catch (IOException e) {
            e.printStackTrace();
            return file.getAbsolutePath();
        }
    }

    private static native void nativeLaunchEngine(Context var0, Object[] var1, String var2, String var3, boolean var4, int var5, int var6, int var7);

    private static native void nativeMark();

    private static native String nativeReverseRedirectedPath(String var0);

    private static native String nativeGetRedirectedPath(String var0);

    private static native void nativeIORedirect(String var0, String var1);

    private static native void nativeIOWhitelist(String var0);

    private static native void nativeIOForbid(String var0);

    private static native void nativeIOReadOnly(String var0);

    private static native void nativeEnableIORedirect(String var0, String var1, String var2, int var3, String var4, String var5);

    public static int onGetUid(int uid) {
        if (VClient.get().getClientConfig() == null) {
            return uid;
        }
        return VClient.get().getBaseVUid();
    }

    static {
        LIB_NAME = "vbox";
        TAG = NativeEngine.class.getSimpleName();
        sDexOverrides = new ArrayList<DexOverride>();
        sFlag = false;
        sEnabled = false;
        EnablePidInfoCache = false;
        try {
            System.loadLibrary(VirtualRuntime.adjustLibName("vbox"));
        }
        catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
        EnablePidInfoCache = true;
        pidCache = new HashMap();
        MaxCachePidInfoTime = 10000;
        MaxCachePidInfoZise = 64;
        REDIRECT_LISTS = new LinkedList<Pair<String, String>>();
    }

    static class PidCacheInfo {
        long lastTime;
        int pid;
        int uid;

        public PidCacheInfo(int pid, int uid, long lastTime) {
            this.pid = pid;
            this.uid = uid;
            this.lastTime = lastTime;
        }

        public String toString() {
            return "PidCacheInfo{pid=" + this.pid + ", uid=" + this.uid + ", lastTime=" + this.lastTime + '}';
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 */
package com.lody.virtual.helper.compat;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.VLog;
import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import mirror.com.android.internal.content.NativeLibraryHelper;

public class NativeLibraryHelperCompat {
    private static String TAG = NativeLibraryHelperCompat.class.getSimpleName();
    private Object handle;

    public NativeLibraryHelperCompat(File packageFile) {
        try {
            this.handle = NativeLibraryHelper.Handle.create.callWithException(packageFile);
        }
        catch (Throwable e) {
            VLog.e(TAG, "failed to create NativeLibraryHelper handle.");
            e.printStackTrace();
        }
    }

    public int copyNativeBinaries(File sharedLibraryDir, String abi) {
        try {
            return NativeLibraryHelper.copyNativeBinaries.callWithException(this.handle, sharedLibraryDir, abi);
        }
        catch (Throwable e) {
            VLog.e(TAG, "failed to copyNativeBinaries.");
            e.printStackTrace();
            return -1;
        }
    }

    @TargetApi(value=21)
    public static boolean is64bitAbi(String abi) {
        return "arm64-v8a".equals(abi) || "mips64".equals(abi);
    }

    public static boolean is32bitAbi(String abi) {
        return "armeabi".equals(abi) || "armeabi-v7a".equals(abi) || "mips".equals(abi);
    }

    public static boolean isHandledAbi(String abi) {
        return NativeLibraryHelperCompat.is32bitAbi(abi) || NativeLibraryHelperCompat.is64bitAbi(abi);
    }

    public static String findSupportedAbi(String[] supportAbis, Set<String> abis) {
        for (String supportAbi : supportAbis) {
            if (!abis.contains(supportAbi)) continue;
            return supportAbi;
        }
        return null;
    }

    @TargetApi(value=21)
    public static boolean contain64bitAbi(Set<String> supportedAbis) {
        for (String supportedAbi : supportedAbis) {
            if (!NativeLibraryHelperCompat.is64bitAbi(supportedAbi)) continue;
            return true;
        }
        return false;
    }

    public static boolean contain32bitAbi(Set<String> abiList) {
        for (String supportedAbi : abiList) {
            if (!NativeLibraryHelperCompat.is32bitAbi(supportedAbi)) continue;
            return true;
        }
        return false;
    }

    public static Set<String> getPackageAbiList(String apk) {
        try {
            ZipFile apkFile = new ZipFile(apk);
            Enumeration<? extends ZipEntry> entries = apkFile.entries();
            HashSet<String> supportedABIs = new HashSet<String>();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.contains("../") || !name.startsWith("lib/") || entry.isDirectory() || !name.endsWith(".so")) continue;
                String supportedAbi = name.substring(name.indexOf("/") + 1, name.lastIndexOf("/"));
                supportedABIs.add(supportedAbi);
            }
            return supportedABIs;
        }
        catch (Exception e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    @TargetApi(value=21)
    public static boolean support64bitAbi(Set<String> supportedABIs) {
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        String[] cpuABIs = Build.SUPPORTED_64_BIT_ABIS;
        if (ArrayUtils.isEmpty(cpuABIs) || supportedABIs == null || supportedABIs.isEmpty()) {
            return false;
        }
        for (String cpuABI : cpuABIs) {
            for (String supportedABI : supportedABIs) {
                if (!TextUtils.equals((CharSequence)cpuABI, (CharSequence)supportedABI)) continue;
                return true;
            }
        }
        return false;
    }

    public static Set<String> getSupportAbiList(String apk) {
        try {
            ZipFile apkFile = new ZipFile(apk);
            Enumeration<? extends ZipEntry> entries = apkFile.entries();
            HashSet<String> supportedABIs = new HashSet<String>();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.contains("../") || !name.startsWith("lib/") || entry.isDirectory() || !name.endsWith(".so")) continue;
                String supportedAbi = name.substring(name.indexOf("/") + 1, name.lastIndexOf("/"));
                supportedABIs.add(supportedAbi);
            }
            return supportedABIs;
        }
        catch (Exception e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    public static class SoLib {
        public String ABI;
        public String path;

        public SoLib() {
        }

        public SoLib(String ABI, String path) {
            this.ABI = ABI;
            this.path = path;
        }

        public boolean is64Bit() {
            return this.ABI != null && NativeLibraryHelperCompat.is64bitAbi(this.ABI);
        }
    }
}


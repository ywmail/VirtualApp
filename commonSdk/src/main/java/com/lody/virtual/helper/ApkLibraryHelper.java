/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkLibraryHelper {
    public static final String[] DEFAULT_SUPPORTED_ABI_32BIT = new String[]{"armeabi", "armeabi-v7a"};
    public static final String[] DEFAULT_SUPPORTED_ABI_64BIT = new String[]{"arm64-v8a"};
    public static final String[] ABI_32BIT = new String[]{"armeabi", "armeabi-v7a", "x86"};
    public static final String[] ABI_64BIT = new String[]{"arm64-v8a", "x86_64"};
    public static final int INSTALL_SUCCEEDED = 1;
    public static final int INSTALL_FAILED_INVALID_APK = -2;
    public static final int INSTALL_FAILED_NO_MATCHING_ABIS = -113;
    public static final int NO_NATIVE_LIBRARIES = -114;
    private ZipFile apkFile;

    public ApkLibraryHelper(File file) {
        try {
            this.apkFile = new ZipFile(file, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int copyNativeBinaries(File sharedLibraryDir, String abi) {
        if (this.apkFile == null) {
            return -2;
        }
        Enumeration<? extends ZipEntry> entries = this.apkFile.entries();
        byte[] buf = new byte[8192];
        while (entries.hasMoreElements()) {
            String prefix;
            String name;
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory() || !(name = entry.getName()).startsWith(prefix = "lib/" + abi + "/")) continue;
            String fileName = name.substring(prefix.length());
            File extractFile = new File(sharedLibraryDir, fileName);
            VLog.e("ApkLibraryHelper", "Writing: " + extractFile);
            try {
                int rc;
                extractFile.createNewFile();
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(extractFile));
                InputStream is = this.apkFile.getInputStream(entry);
                while ((rc = is.read(buf, 0, 100)) > 0) {
                    os.write(buf, 0, rc);
                }
                os.flush();
                os.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return 1;
    }

    public int findSupportedAbi(String[] supportedAbis) {
        if (this.apkFile == null) {
            return -2;
        }
        int status = -114;
        Enumeration<? extends ZipEntry> entries = this.apkFile.entries();
        while (entries.hasMoreElements()) {
            String name;
            status = -113;
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory() || !(name = entry.getName()).startsWith("lib/")) continue;
            for (int i = 0; i < supportedAbis.length; ++i) {
                String supportAbi = "lib/" + supportedAbis[i];
                if (!name.startsWith(supportAbi)) continue;
                return i;
            }
        }
        return status;
    }
}


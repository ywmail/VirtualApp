//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.swift.sandhook.utils;

import android.os.Build.VERSION;
import android.system.Os;
import android.text.TextUtils;
import com.swift.sandhook.HookLog;
import com.swift.sandhook.SandHookConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static final boolean IS_USING_PROTECTED_STORAGE;

    public FileUtils() {
    }

    public static void delete(File file) throws IOException {
        File[] var1 = file.listFiles();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            File childFile = var1[var3];
            if (childFile.isDirectory()) {
                delete(childFile);
            } else if (!childFile.delete()) {
                throw new IOException();
            }
        }

        if (!file.delete()) {
            throw new IOException();
        }
    }

    public static String readLine(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String var2;
            try {
                var2 = reader.readLine();
            } catch (Throwable var5) {
                try {
                    reader.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            reader.close();
            return var2;
        } catch (Throwable var6) {
            return "";
        }
    }

    public static void writeLine(File file, String line) {
        try {
            file.createNewFile();
        } catch (IOException var8) {
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            try {
                writer.write(line);
                writer.flush();
            } catch (Throwable var6) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            writer.close();
        } catch (Throwable var7) {
            HookLog.e("error writing line to file " + file + ": " + var7.getMessage());
        }

    }

    public static String getPackageName(String dataDir) {
        if (TextUtils.isEmpty(dataDir)) {
            HookLog.e("getPackageName using empty dataDir");
            return "";
        } else {
            int lastIndex = dataDir.lastIndexOf("/");
            return lastIndex < 0 ? dataDir : dataDir.substring(lastIndex + 1);
        }
    }

    public static void chmod(String path, int mode) throws Exception {
        if (SandHookConfig.SDK_INT >= 21) {
            try {
                Os.chmod(path, mode);
                return;
            } catch (Exception var5) {
            }
        }

        File file = new File(path);
        String cmd = "chmod ";
        if (file.isDirectory()) {
            cmd = cmd + " -R ";
        }

        String cmode = String.format("%o", mode);
        Runtime.getRuntime().exec(cmd + cmode + " " + path).waitFor();
    }

    public static String getDataPathPrefix() {
        return IS_USING_PROTECTED_STORAGE ? "/data/user_de/0/" : "/data/data/";
    }

    static {
        IS_USING_PROTECTED_STORAGE = VERSION.SDK_INT >= 24;
    }

    public interface FileMode {
        int MODE_ISUID = 2048;
        int MODE_ISGID = 1024;
        int MODE_ISVTX = 512;
        int MODE_IRUSR = 256;
        int MODE_IWUSR = 128;
        int MODE_IXUSR = 64;
        int MODE_IRGRP = 32;
        int MODE_IWGRP = 16;
        int MODE_IXGRP = 8;
        int MODE_IROTH = 4;
        int MODE_IWOTH = 2;
        int MODE_IXOTH = 1;
        int MODE_755 = 493;
    }
}

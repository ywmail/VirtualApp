//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.swift.sandhook.xposedcompat.utils;

import android.os.Build.VERSION;
import android.text.TextUtils;
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
            DexLog.e("error writing line to file " + file + ": " + var7.getMessage());
        }

    }

    public static String getPackageName(String dataDir) {
        if (TextUtils.isEmpty(dataDir)) {
            DexLog.e("getPackageName using empty dataDir");
            return "";
        } else {
            int lastIndex = dataDir.lastIndexOf("/");
            return lastIndex < 0 ? dataDir : dataDir.substring(lastIndex + 1);
        }
    }

    public static String getDataPathPrefix() {
        return IS_USING_PROTECTED_STORAGE ? "/data/user_de/0/" : "/data/data/";
    }

    static {
        IS_USING_PROTECTED_STORAGE = VERSION.SDK_INT >= 24;
    }
}

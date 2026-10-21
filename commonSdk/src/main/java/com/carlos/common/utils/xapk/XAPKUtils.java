/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.utils.xapk;

import java.io.File;

public class XAPKUtils {
    public static boolean createOrExistsDir(String dirPath) {
        return XAPKUtils.createOrExistsDir(XAPKUtils.getFileByPath(dirPath));
    }

    public static boolean createOrExistsDir(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            return true;
        }
        return file.mkdirs();
    }

    public static File getFileByPath(String filePath) {
        if (XAPKUtils.isSpace(filePath)) {
            return null;
        }
        return new File(filePath);
    }

    public static String getFileName(File file) {
        if (file == null) {
            return "";
        }
        return XAPKUtils.getFileName(file.getAbsolutePath());
    }

    public static String getFileName(String filePath) {
        if (XAPKUtils.isSpace(filePath)) {
            return "";
        }
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return filePath;
        }
        return filePath.substring(lastSep + 1);
    }

    public static String getFileNameNoExtension(File file) {
        if (file == null) {
            return "";
        }
        return XAPKUtils.getFileNameNoExtension(file.getPath());
    }

    public static String getFileNameNoExtension(String filePath) {
        if (XAPKUtils.isSpace(filePath)) {
            return "";
        }
        int lastPoi = filePath.lastIndexOf(46);
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            if (lastPoi == -1) {
                return filePath;
            }
            return filePath.substring(0, lastPoi);
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    private static boolean isSpace(String s) {
        if (s == null) {
            return true;
        }
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            if (Character.isWhitespace(s.charAt(i))) continue;
            return false;
        }
        return true;
    }
}


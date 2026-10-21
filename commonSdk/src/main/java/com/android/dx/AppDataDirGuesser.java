/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

class AppDataDirGuesser {
    public static final int PER_USER_RANGE = 100000;

    AppDataDirGuesser() {
    }

    public File guess() {
        try {
            ClassLoader classLoader = this.guessSuitableClassLoader();
            Class<?> clazz = Class.forName("dalvik.system.PathClassLoader");
            clazz.cast(classLoader);
            String pathFromThisClassLoader = this.getPathFromThisClassLoader(classLoader, clazz);
            File[] results = this.guessPath(pathFromThisClassLoader);
            if (results.length > 0) {
                return results[0];
            }
        }
        catch (ClassCastException classCastException) {
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        return null;
    }

    private ClassLoader guessSuitableClassLoader() {
        return AppDataDirGuesser.class.getClassLoader();
    }

    private String getPathFromThisClassLoader(ClassLoader classLoader, Class<?> pathClassLoaderClass) {
        try {
            Field pathField = pathClassLoaderClass.getDeclaredField("path");
            pathField.setAccessible(true);
            return (String)pathField.get(classLoader);
        }
        catch (NoSuchFieldException pathField) {
        }
        catch (IllegalAccessException pathField) {
        }
        catch (ClassCastException pathField) {
            // empty catch block
        }
        String result = classLoader.toString();
        return AppDataDirGuesser.processClassLoaderString(result);
    }

    static String processClassLoaderString(String input) {
        if (input.contains("DexPathList")) {
            return AppDataDirGuesser.processClassLoaderString43OrLater(input);
        }
        return AppDataDirGuesser.processClassLoaderString42OrEarlier(input);
    }

    private static String processClassLoaderString42OrEarlier(String input) {
        int index = input.lastIndexOf(91);
        input = index == -1 ? input : input.substring(index + 1);
        index = input.indexOf(93);
        input = index == -1 ? input : input.substring(0, index);
        return input;
    }

    private static String processClassLoaderString43OrLater(String input) {
        int start = input.indexOf("DexPathList") + "DexPathList".length();
        if (input.length() > start + 4) {
            String trimmed = input.substring(start);
            int end = trimmed.indexOf(93);
            if (trimmed.charAt(0) == '[' && trimmed.charAt(1) == '[' && end >= 0) {
                trimmed = trimmed.substring(2, end);
                String[] split = trimmed.split(",");
                for (int i = 0; i < split.length; ++i) {
                    int quoteStart = split[i].indexOf(34);
                    int quoteEnd = split[i].lastIndexOf(34);
                    if (quoteStart <= 0 || quoteStart >= quoteEnd) continue;
                    split[i] = split[i].substring(quoteStart + 1, quoteEnd);
                }
                StringBuilder sb = new StringBuilder();
                for (String s : split) {
                    if (sb.length() > 0) {
                        sb.append(':');
                    }
                    sb.append(s);
                }
                return sb.toString();
            }
        }
        return input;
    }

    File[] guessPath(String input) {
        ArrayList<File> results = new ArrayList<File>();
        for (String potential : AppDataDirGuesser.splitPathList(input)) {
            File cacheDir;
            if (!potential.startsWith("/data/app/")) continue;
            int start = "/data/app/".length();
            int end = potential.lastIndexOf(".apk");
            if (end != potential.length() - 4) continue;
            int dash = potential.indexOf("-");
            if (dash != -1) {
                end = dash;
            }
            String packageName = potential.substring(start, end);
            File dataDir = this.getWriteableDirectory("/data/data/" + packageName);
            if (dataDir == null) {
                dataDir = this.guessUserDataDirectory(packageName);
            }
            if (dataDir == null || !this.fileOrDirExists(cacheDir = new File(dataDir, "cache")) && !cacheDir.mkdir() || !this.isWriteableDirectory(cacheDir)) continue;
            results.add(cacheDir);
        }
        return results.toArray(new File[results.size()]);
    }

    static String[] splitPathList(String input) {
        String trimmed = input;
        if (input.startsWith("dexPath=")) {
            int start = "dexPath=".length();
            int end = input.indexOf(44);
            trimmed = end == -1 ? input.substring(start) : input.substring(start, end);
        }
        return trimmed.split(":");
    }

    boolean fileOrDirExists(File file) {
        return file.exists();
    }

    boolean isWriteableDirectory(File file) {
        return file.isDirectory() && file.canWrite();
    }

    Integer getProcessUid() {
        try {
            Method myUid = Class.forName("android.os.Process").getMethod("myUid", new Class[0]);
            return (Integer)myUid.invoke(null, new Object[0]);
        }
        catch (Exception e) {
            return null;
        }
    }

    File guessUserDataDirectory(String packageName) {
        Integer uid = this.getProcessUid();
        if (uid == null) {
            return null;
        }
        int userId = uid / 100000;
        return this.getWriteableDirectory(String.format("/data/user/%d/%s", userId, packageName));
    }

    private File getWriteableDirectory(String pathName) {
        File dir = new File(pathName);
        return this.isWriteableDirectory(dir) ? dir : null;
    }
}


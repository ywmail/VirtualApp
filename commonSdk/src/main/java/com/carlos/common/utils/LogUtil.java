package com.carlos.common.utils;

import android.util.Log;

/**
 * 简易 Android 日志工具类
 */
public class LogUtil {

    // 是否允许日志输出
    private static boolean isDebug = true;
    // 全局TAG
    private static String globalTag = "com.carlos##";

    private LogUtil() {
        // 防止实例化
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    /**
     * 设置是否允许日志输出
     */
    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    /**
     * 设置全局TAG
     */
    public static void setGlobalTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            globalTag = tag;
        }
    }

    // Verbose
    public static void v(String msg) {
        v(globalTag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    // Debug
    public static void d(String msg) {
        d(globalTag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    // Info
    public static void i(String msg) {
        i(globalTag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    // Warn
    public static void w(String msg) {
        w(globalTag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    // Error
    public static void e(String msg) {
        e(globalTag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    // 打印异常
    public static void e(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.e(tag, msg, tr);
        }
    }
}
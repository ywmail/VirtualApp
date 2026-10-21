/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package com.kook.common.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.kook.common.utils.ReceiverLog;
import com.kook.core.log.StringFog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HVLog {
    private static Context mContext;
    private static String SIMPLE_NAME;
    public static Boolean LOG_SWITCH;
    private static Boolean LOG_WRITE_TO_FILE;
    public static String LOG_PATH_SDCARD_DIR;
    private static int SDCARD_LOG_FILE_SAVE_DAYS;
    private static String LOG_FILE_NAME;
    private static SimpleDateFormat myLogSdf;
    private static SimpleDateFormat logfile;
    public static String TAG;
    public static boolean DEBUG;

    public static void w(Object msg) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, msg.toString(), 'w');
    }

    public static void w(String tag, Object msg) {
        HVLog.log(tag, msg.toString(), 'w');
    }

    public static void e(Object msg) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, msg.toString(), 'e');
    }

    public static void e(String tag, Object msg) {
        HVLog.log(tag, msg.toString(), 'e');
    }

    public static void d(Object msg) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, msg.toString(), 'd');
    }

    public static void d(String tag, Object msg) {
        HVLog.log(tag, msg.toString(), 'd');
    }

    public static void i(Object msg) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, msg.toString(), 'i');
    }

    public static void i(String tag, Object msg) {
        HVLog.log(tag, msg.toString(), 'i');
    }

    public static void v(Object msg) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, msg.toString(), 'v');
    }

    public static void v(String tag, Object msg) {
        HVLog.log(tag, msg.toString(), 'v');
    }

    public static void w(String text) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, text, 'w');
    }

    public static void w(String tag, String text) {
        HVLog.log(tag, text, 'w');
    }

    public static void e(String text) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, text, 'e');
    }

    public static void e(String tag, String text) {
        HVLog.log(tag, text, 'e');
    }

    public static void d(String text) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, text, 'd');
    }

    public static void d(String tag, String text) {
        HVLog.log(tag, text, 'd');
    }

    public static void i(String text) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, text, 'i');
    }

    public static void i(String tag, String text) {
        HVLog.log(tag, text, 'i');
    }

    public static void v(String text) {
        String tag = HVLog.getLogTag();
        HVLog.log(tag, text, 'v');
    }

    public static void v(String tag, String text) {
        HVLog.log(tag, text, 'v');
    }

    public static String getLogTag() {
        int stackLength;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int index = 0;
        for (StackTraceElement element : stackTrace) {
            ++index;
            String className = element.getClassName();
            if (className.contains(SIMPLE_NAME)) break;
        }
        StackTraceElement element = (stackLength = stackTrace.length) > index ? stackTrace[index + 1] : stackTrace[index - 1];
        String className = HVLog.getSimpleName(element.getClassName());
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();
        String tag = TAG + "-[" + className + "][" + methodName + "][" + lineNumber + "]";
        return tag;
    }

    private static void log(String tag, String msg, char level) {
        if (LOG_SWITCH.booleanValue()) {
            if (!TAG.equals(tag)) {
                boolean bit = false;
                tag = bit ? TAG + "-" + tag : TAG + "-" + tag;
            }
            int logLevel = HVLog.getSystemSettings();
            Object tagList = null;
            boolean TAG_SHOW = false;
            if (mContext != null) {
                // empty if block
            }
            if ('e' == level && (logLevel == 1 || logLevel == 5 || TAG_SHOW)) {
                Log.e((String)tag, (String)msg);
            } else if ('w' == level && (logLevel == 2 || logLevel == 5 || TAG_SHOW)) {
                Log.w((String)tag, (String)msg);
            } else if ('d' == level && (logLevel == 3 || logLevel == 5 || TAG_SHOW)) {
                Log.d((String)tag, (String)msg);
            } else if ('i' == level && (logLevel == 4 || logLevel == 5 || TAG_SHOW)) {
                Log.i((String)tag, (String)msg);
            }
            if (LOG_WRITE_TO_FILE.booleanValue()) {
                HVLog.writeLogtoFile(String.valueOf(level), tag, msg);
            }
        }
    }

    private static void writeLogtoFile(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype + "    " + tag + "    " + text;
        File fileDir = new File(LOG_PATH_SDCARD_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(LOG_PATH_SDCARD_DIR, needWriteFiel + LOG_FILE_NAME);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e((String)TAG, (String)("log 写入异常:" + e.toString()));
        }
    }

    public static void delFile() {
        String needDelFiel = logfile.format(HVLog.getDateBefore());
        File file = new File(LOG_PATH_SDCARD_DIR, needDelFiel + LOG_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(5, now.get(5) - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }

    public static void printException(String subtag, Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        Throwable cause = e.getCause();
        HVLog.d(subtag, "Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
        if (cause != null) {
            String stackTraceString = Log.getStackTraceString((Throwable)cause);
            HVLog.e(subtag, "异常 cause:" + stackTraceString);
        } else {
            Log.e((String)subtag, (String)("异常:" + e.toString() + "     cause is null ?" + (cause == null)));
            for (int i = 0; i < stackTrace.length; ++i) {
                HVLog.e(subtag, "Exception e:" + stackTrace[i].toString());
            }
        }
    }

    public static void printException(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        Throwable cause = e.getCause();
        HVLog.d("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
        if (cause != null) {
            String stackTraceString = Log.getStackTraceString((Throwable)cause);
            HVLog.e("异常 cause(printException):" + stackTraceString);
        } else {
            HVLog.e("异常:" + e.toString() + "     cause is null ?" + (cause == null));
            for (int i = 0; i < stackTrace.length; ++i) {
                HVLog.e("Exception e:" + stackTrace[i].toString());
            }
        }
    }

    public static void printThrowable(Throwable throwable) {
        String stackTraceString = Log.getStackTraceString((Throwable)throwable);
        HVLog.d("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
        HVLog.e(TAG, "printThrowable e:" + stackTraceString);
    }

    public static void printThrowable(String subtag, Throwable throwable) {
        String stackTraceString = Log.getStackTraceString((Throwable)throwable);
        HVLog.d("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
        HVLog.e(subtag, "printThrowable e:" + stackTraceString);
    }

    public static void printInfo() {
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; ++i) {
                StackTraceElement stackTraceElement = stackElements[i];
                String output = String.format("%s():%s, %s (%s)", stackTraceElement.getMethodName(), stackTraceElement.getLineNumber(), HVLog.getSimpleName(stackTraceElement.getClassName()), HVLog.getPackageName(stackTraceElement.getClassName()));
                Log.i((String)TAG, (String)output);
            }
        }
    }

    public static void printInfo(String tag) {
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; ++i) {
                StackTraceElement stackTraceElement = stackElements[i];
                String output = String.format("%s():%s, %s (%s)", stackTraceElement.getMethodName(), stackTraceElement.getLineNumber(), HVLog.getSimpleName(stackTraceElement.getClassName()), HVLog.getPackageName(stackTraceElement.getClassName()));
                HVLog.i(tag, output);
            }
        }
    }

    private static String getSimpleName(String className) {
        return className.substring(className.lastIndexOf(46) + 1);
    }

    private static String getPackageName(String className) {
        return className.substring(0, className.lastIndexOf(46));
    }

    public static int getSystemSettings() {
        if (mContext == null) {
            mContext = HVLog.getApplication();
        }
        if (mContext != null) {
            return ReceiverLog.getInt(mContext, ReceiverLog.LOG_LEVEL);
        }
        return 0;
    }

    static Context getApplication() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentApplicationMethod = activityThreadClass.getMethod("currentApplication", new Class[0]);
            Object application = currentApplicationMethod.invoke(null, new Object[0]);
            return (Context)application;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        SIMPLE_NAME = "kooklog";
        LOG_SWITCH = true;
        LOG_WRITE_TO_FILE = false;
        LOG_PATH_SDCARD_DIR = "/sdcard/Theme";
        SDCARD_LOG_FILE_SAVE_DAYS = 0;
        LOG_FILE_NAME = "";
        myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logfile = new SimpleDateFormat("yyyy-MM-dd");
        TAG = "kooklog-Rommock";
        DEBUG = true;
    }
}


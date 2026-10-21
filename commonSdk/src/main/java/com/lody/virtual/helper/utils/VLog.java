//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.helper.utils;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import com.lody.virtual.StringFog;
import java.util.Iterator;
import java.util.Set;

public class VLog {
    public static boolean OPEN_LOG = true;
    public static final String TAG_DEFAULT = "VA-";

    public VLog() {
    }

    public static void i(String tag, String msg, Object... format) {
        if (OPEN_LOG) {
            Log.i(tag, String.format(msg, format));
        }

    }

    public static void d(String tag, String msg, Object... format) {
        if (OPEN_LOG) {
            Log.d(tag, String.format(msg, format));
        }

    }

    public static void d(String msg, Object... format) {
        if (OPEN_LOG) {
            Log.d(TAG_DEFAULT, String.format(msg, format));
        }

    }

    public static void logbug(String tag, String msg) {
        d(tag, msg);
    }

    public static void w(String tag, String msg, Object... format) {
        if (OPEN_LOG) {
            Log.w(tag, String.format(msg, format));
        }

    }

    public static void e(String tag, String msg) {
        if (OPEN_LOG) {
            Log.e(tag, msg);
        }

    }

    public static void e(String tag, String msg, Object... format) {
        if (OPEN_LOG) {
            Log.e(tag, String.format(msg, format));
        }

    }

    public static void v(String tag, String msg) {
        if (OPEN_LOG) {
            Log.v(tag, msg);
        }

    }

    public static void v(String tag, String msg, Object... format) {
        if (OPEN_LOG) {
            Log.v(tag, String.format(msg, format));
        }

    }

    public static String toString(Bundle bundle) {
        if (bundle == null) {
            return null;
        } else if (Reflect.on(bundle).get("mParcelledData") == null) {
            return bundle.toString();
        } else {
            Set<String> keys = bundle.keySet();
            StringBuilder stringBuilder = new StringBuilder("Bundle[");
            if (keys != null) {
                Iterator var3 = keys.iterator();

                while(var3.hasNext()) {
                    String key = (String)var3.next();
                    stringBuilder.append(key);
                    stringBuilder.append("=");
                    stringBuilder.append(bundle.get(key));
                    stringBuilder.append(",");
                }
            }

            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    public static void printStackTrace(String tag) {
        Log.e(tag, getStackTraceString(new Exception()));
    }

    public static void e(String tag, Throwable e) {
        Log.e(tag, getStackTraceString(e));
    }

    public static void printException(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        Throwable cause = e.getCause();
        d(TAG_DEFAULT, "Build.VERSION.SDK_INT = " + VERSION.SDK_INT);
        if (cause != null) {
            String stackTraceString = Log.getStackTraceString(cause);
            e(TAG_DEFAULT, "异常 cause(printException):" + stackTraceString);
        } else {
            e(TAG_DEFAULT, "异常:" + e.toString() + "     cause is null ?" + (cause == null));

            for(int i = 0; i < stackTrace.length; ++i) {
                e(TAG_DEFAULT, "Exception e:" + stackTrace[i].toString());
            }
        }

    }
}

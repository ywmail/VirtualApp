/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.os.Build$VERSION
 */
package com.kook.deviceinfo.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import java.util.Locale;
import java.util.TimeZone;

public class LanguageUtils {
    public static Locale getSystemLanguage() {
        return LanguageUtils.getLocal(Resources.getSystem().getConfiguration());
    }

    public static Locale getLocal(Configuration configuration) {
        if (Build.VERSION.SDK_INT >= 24) {
            return configuration.getLocales().get(0);
        }
        return configuration.locale;
    }

    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return LanguageUtils.createGmtOffsetString(true, true, tz.getRawOffset());
    }

    public static String createGmtOffsetString(boolean includeGmt, boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        LanguageUtils.appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        LanguageUtils.appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    public static void appendNumber(StringBuilder builder, int count, int value) {
        String string2 = Integer.toString(value);
        for (int i = 0; i < count - string2.length(); ++i) {
            builder.append('0');
        }
        builder.append(string2);
    }
}


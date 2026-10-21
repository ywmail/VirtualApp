/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.kook.librelease.R$array
 */
package com.carlos.common.utils;

import android.content.Context;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class StringUtils {
    private static boolean isTencentMap = true;

    public static String replaceChar2JsonString(String baseString) {
        return baseString.replace("\\\\\\\"", "\"").replace("\\\\\\\"", "\"").replace("\\\"", "\"").replace("\"{", "{").replace("\"[", "[").replace("\\\\n", "\n").replace("\\n", "").replace("\\\\t", "").replace("]\"", "]").replace("}\"", "}").replace("\\\\t", "").replace("\\t", "").replace("&amp;", "&");
    }

    public static boolean isString(String tag) {
        return tag != null && !"".equals(tag);
    }

    public static double doubleFor6(double num) {
        if (isTencentMap) {
            return num;
        }
        return Double.parseDouble(StringUtils.doubleFor6String(num));
    }

    public static double doubleFor8(double num) {
        if (isTencentMap) {
            return num;
        }
        return Double.parseDouble(StringUtils.doubleFor8String(num));
    }

    public static String doubleFor8String(double num) {
        if (isTencentMap) {
            return String.valueOf(num);
        }
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(8);
        return formater.format(num);
    }

    public static String doubleFor6String(double num) {
        if (isTencentMap) {
            return String.valueOf(num);
        }
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(6);
        return formater.format(num);
    }

    public static String getTimeNoHour(long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        calendar.setTimeInMillis(time);
        int h = calendar.get(11);
        int m = calendar.get(12);
        int s = calendar.get(13);
        return String.format("%02d:%02d", h * 60 + m, s);
    }

    public static String getTime(long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        calendar.setTimeInMillis(time);
        int h = calendar.get(11);
        int m = calendar.get(12);
        int s = calendar.get(13);
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public static String timeForString(Context context, long tag) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(tag);
        int week = calendar.get(7);
        String[] strs = context.getResources().getStringArray(R.array.weeks);
        String str = strs[week - 1];
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd [" + str + "] hh:mm");
        return dateFm.format(tag);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.imagepicker.utils;

import com.kook.librelease.StringFog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String getImageTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Calendar imageTime = Calendar.getInstance();
        imageTime.setTimeInMillis(time);
        if (DateUtils.sameDay(calendar, imageTime)) {
            return "今天";
        }
        if (DateUtils.sameWeek(calendar, imageTime)) {
            return "本周";
        }
        if (DateUtils.sameMonth(calendar, imageTime)) {
            return "本月";
        }
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CANADA);
        return sdf.format(date);
    }

    public static boolean sameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(1) == calendar2.get(1) && calendar1.get(6) == calendar2.get(6);
    }

    public static boolean sameWeek(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(1) == calendar2.get(1) && calendar1.get(3) == calendar2.get(3);
    }

    public static boolean sameMonth(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(1) == calendar2.get(1) && calendar1.get(2) == calendar2.get(2);
    }
}


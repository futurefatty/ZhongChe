package com.crcc.commonlib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {


    public static String formatDateTime(long time, String format) {
        return formatDateTime(new Date(time), format);
    }

    public static String formatDateTime(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(date);
    }

    public static Date parseDate(String content, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

package com.neusoft.zcapplication.tools;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */

public class DateUtils {

    private static final String[] dayAry = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    /**
     * 生成日期选择器的开始日期
     *
     * @return
     */
    public static String generalBeginDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.format(date);
    }

    /**
     * 生成日期选择器的最大日期
     *
     * @return
     */
    public static String generalEndDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) + 5;
        calendar.set(year, 1, 1);

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.format(date);
    }

    public static String generalEndDate(int years) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) + years;
        calendar.set(year, 1, 1);

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.format(date);
    }

    /**
     * 判断是否是过去的时间
     *
     * @param day
     * @return
     */
    public static boolean isPaseByTime(String day) {
        Calendar calendar = Calendar.getInstance();
//        Date nowDate = calendar.getTime();

        long nowMils = calendar.getTimeInMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = sdf.parse(day);
            calendar.setTime(date);
            long time1 = calendar.getTimeInMillis();
            long differ = time1 - nowMils;
//            int result = (int)(differ / (1000 * 60 * 60 * 24));
            if (differ >= 0) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
//           Log.i("--->","exception:"+e);
        }
        return true;
    }

    /**
     * 获取日期为星期几
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDayOfDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date d = sdf.parse(date);
        calendar.setTime(d);
        //星期的下标
        int dayIndex = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        return dayAry[dayIndex];

    }

    public static boolean isFirstDayBeforeSecondDay(String firstDay, String secondDay) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(firstDay);
            calendar.setTime(d1);
            long time1 = calendar.getTimeInMillis();

            Date d2 = sdf.parse(secondDay);
            calendar.setTime(d2);
            long time2 = calendar.getTimeInMillis();
            if (time2 < time1) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 酒店入住日期是否在离店日期之前(两个日期不能是同一天)
     *
     * @param checkInDay
     * @param checkOutDay
     * @return
     */
    public static boolean checkinDayUsable(String checkInDay, String checkOutDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(checkInDay);
            calendar.setTime(d1);
            long time1 = calendar.getTimeInMillis();

            Date d2 = sdf.parse(checkOutDay);
            calendar.setTime(d2);
            long time2 = calendar.getTimeInMillis();

            if (time2 > time1) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 获得某一天的日期
     *
     * @param days 需要加上的天数（从当前日期算起）
     * @return
     */
    public static String getDate(int days) {
        String result = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (days > 0) {
            calendar.add(Calendar.DATE, days);
        }
        result = df.format(calendar.getTime());
        return result;
    }

    /**
     * 获取date之后 days 天的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static String generalNextDayOfOneDay(String date, int days) {
        String result;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar calendar = Calendar.getInstance();
        try {
            Date d1 = df.parse(date);
            calendar.setTime(d1);
            calendar.add(Calendar.DATE, days);
            result = df.format(calendar.getTime());
            return result;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 计算两个日期之间的时分
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static String differTimeWithDays(String firstDate, String secondDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(firstDate);
            calendar.setTime(d1);
            long time1 = calendar.getTimeInMillis();

            Date d2 = sdf.parse(secondDate);
            calendar.setTime(d2);
            long time2 = calendar.getTimeInMillis();
            long differ = Math.abs(time2 - time1);
            int hours = (int) (differ / 1000 / 3600);
            int mins = (int) differ / 60000 - hours * 60;
            String str = hours + "h" + mins + "m";
            return str;
        } catch (ParseException e) {
            return "";
        }
    }


    /**
     * 计算两个日期之间的毫秒
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static long differTimeWithMsec(String firstDate, String secondDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(firstDate);
            calendar.setTime(d1);
            long time1 = calendar.getTimeInMillis();
            Date d2 = sdf.parse(secondDate);
            calendar.setTime(d2);
            long time2 = calendar.getTimeInMillis();
            long differ = time2 - time1;
            if (differ > 0) {
                return differ;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 计算两个日期之间的毫秒
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static long differTimeWithMsecForLaterHour(String firstDate, String secondDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(firstDate);
            calendar.setTime(d1);
            long time1 = calendar.getTimeInMillis();
            Date d2 = sdf.parse(secondDate);
            calendar.setTime(d2);
            long time2 = calendar.getTimeInMillis();
            long differ = time1 + 1000 * 60 * 60 * 24 - time2;
            if (differ > 0) {
                return differ;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            return 0;
        }
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int differDaysWithDays(String firstDate, String secondDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(firstDate);
            calendar.setTime(d1);
            long time1 = calendar.getTimeInMillis();

            Date d2 = sdf.parse(secondDate);
            calendar.setTime(d2);
            long time2 = calendar.getTimeInMillis();
            long differ = Math.abs(time2 - time1);
            int days = (int) (differ / 1000 / 3600 / 24);
            return days;
        } catch (ParseException e) {
            return -1;
        }
    }

    public static int differDays(String firstDate, String secondDate) {
        try {
            Date d1 = parseDate(firstDate, "yyyy-MM-dd");
            Date d2 = parseDate(secondDate, "yyyy-MM-dd");
            if (d1 != null && d2 != null) {
                long differ = Math.abs(d2.getTime() - d1.getTime());
                return (int) (differ / 1000 / 3600 / 24);
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param date
     * @return
     */
    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 获取时
     *
     * @return
     */
    public static String getCurrentHour() {
        Calendar c = Calendar.getInstance();//
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        return String.valueOf(mHour);
    }

    /**
     * 获取分
     *
     * @return
     */
    public static String getCurrentMinute() {
        Calendar c = Calendar.getInstance();//
        int mMinute = c.get(Calendar.MINUTE);//时
        return String.valueOf(mMinute);
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToDayStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToMinuteStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = format.format(date);
        return str;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToSecondStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param time
     * @return 时:分:秒
     */
    public static String getHours(long time) {
        long second = time / 1000;
        long hour = second / 60 / 60;
        long minute = (second - hour * 60 * 60) / 60;
        long sec = (second - hour * 60 * 60) - minute * 60;

        String rHour = "";
        String rMin = "";
        String rSs = "";
        // 时
        if (hour < 10) {
            rHour = "0" + hour;
        } else {
            rHour = hour + "";
        }
        // 分
        if (minute < 10) {
            rMin = "0" + minute;
        } else {
            rMin = minute + "";
        }
        // 秒
        if (sec < 10) {
            rSs = "0" + sec;
        } else {
            rSs = sec + "";
        }
        // return hour + "小时" + minute + "分钟" + sec + "秒";
        return rHour + ":" + rMin + ":" + rSs;
    }


    public static String formatToDate(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(formatStr).format(date);
    }


    /**
     * @param datetime 日期 例:2017-10-17
     * @return String 例:星期二
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 指示一个星期中的某天。
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static Date parseDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 分钟转换为小时 1h30m
     *
     * @param duration 分钟
     * @return
     */
    public static String minuteToHour(String duration) {
        if (!TextUtils.isEmpty(duration)) {
            int hour = Integer.valueOf(duration) / 60;
            int minute = Integer.valueOf(duration) - hour * 60;
            String str = "";
            if (hour != 0) {
                str = hour + "h";
            }
            if (minute != 0) {
                str += minute + "m";
            }
            return str;
        }
        return "";
    }


}

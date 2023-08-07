package com.simon.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String YMD = "yyyy-MM-dd";

    public static final String YMDHM = "yyyy-MM-dd HH:mm";

    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";

    public static final String pattern = "yyyy年MM月dd日 HH时mm分ss秒";


    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar ca = Calendar.getInstance();
        return ca.get(Calendar.YEAR);
    }


    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1);
    }


    /**
     * 获取当前哪一天
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar ca = Calendar.getInstance();
        return ca.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取当前的年月日
     *
     * @return
     */
    public static String getYearMothDay() {
        return getCurrentYear() + "-" + getCurrentMonth() + "-" + getCurrentDay();
    }


    /**
     * 获取当前年月日，时分秒
     *
     * @return
     */
    public static String getCurrentDate() {
        String s = getDateFormat(System.currentTimeMillis(), YMDHMS);
        return s;
    }


    /**
     * 将时间转换为时间戳
     *
     * @param s
     * @param pattern
     * @return
     */
    public static long dateToStamp(String s, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = simpleDateFormat.parse(s);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 指定时间戳的 月日以及星期
     *
     * @param time
     * @return
     */
    public static String getMothDayWeek(String time) {
        if (TextUtils.isEmpty(time))
            return "";
        try {
            long aLong = Long.parseLong(time);
            SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日  HH:mm  #");
            return sdr.format(new Date(aLong)).replaceAll("#", getWeek(aLong));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String getWeek(long timeStamp) {
        int mydate = 0;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (mydate == 1) {
            week = "周日";
        } else if (mydate == 2) {
            week = "周一";
        } else if (mydate == 3) {
            week = "周二";
        } else if (mydate == 4) {
            week = "周三";
        } else if (mydate == 5) {
            week = "周四";
        } else if (mydate == 6) {
            week = "周五";
        } else if (mydate == 7) {
            week = "周六";
        }
        return week;
    }


    /**
     * 今天的最晚时间
     *
     * @return
     */
    public static String getYearMothDayLatest() {
        return getDateFormat(System.currentTimeMillis(), YMD) + " 23:59";
    }


    /**
     * 今天最早时间
     *
     * @return
     */
    public static String getYearMothDayFirst() {
        return getDateFormat(System.currentTimeMillis(), YMD) + " 00:00";
    }


    /**
     * 获取昨天的日期
     *
     * @param isFirst true 开始时间 false 结束时间
     * @return
     */
    public static String getLastDay(boolean isFirst) {
        SimpleDateFormat format = new SimpleDateFormat(YMD);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(c.DATE, -1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        String time;
        if (isFirst)
            time = format.format(c.getTime()) + " 00:00";
        else
            time = format.format(c.getTime()) + " 23:59";

        return time;
    }


    /**
     * 当前时间的前一个星期 开始时间
     *
     * @return
     */
    public static String getFirstDayOfLastWeek() {
        SimpleDateFormat format = new SimpleDateFormat(YMD);
        Calendar c = Calendar.getInstance();
        // 获取上周周一的开始时间
        c.add(Calendar.WEEK_OF_YEAR, -1);
        // 设置日期为周一
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String start = format.format(c.getTime()) + " 00:00";
        return start;
    }


    /**
     * 当前时间的前一个星期 结束时间
     *
     * @return
     */
    public static String getLastDayOfLastWeek() {
        SimpleDateFormat format = new SimpleDateFormat(YMD);
        Calendar c = Calendar.getInstance();
        // 设置日期为周日
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String end = format.format(c.getTime()) + " 23:59";
        return end;
    }


    /**
     * 获取当前时间的周一时间
     *
     * @return
     */
    public static String getFirstDayOfWeek() {
        SimpleDateFormat format = new SimpleDateFormat(YMD);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String weekStart = format.format(c.getTime()) + " 00:00";
        return weekStart;
    }


    /**
     * 当前时间的周日时间
     *
     * @return
     */
    public static String getLastDayOfWeek() {
        SimpleDateFormat format = new SimpleDateFormat(YMD);
        Calendar ca = Calendar.getInstance();
        ca.setFirstDayOfWeek(Calendar.MONDAY);
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek() + 6); // Sunday
        String weekEnd = format.format(ca.getTime()) + " 23:59";
        return weekEnd;
    }


    /**
     * 当前时所在月份的第一天
     *
     * @return
     */
    public static String getFirstDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat(YMD);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String monthStart = format.format(c.getTime()) + " 00:00";
        return monthStart;
    }


    /**
     * 当前时所在月份的最后一天
     *
     * @return
     */
    public static String getLastDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat(YMD);
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String monthEnd = format.format(ca.getTime()) + " 23:59";
        return monthEnd;
    }


    /**
     * 获取上个月的第一天
     */
    public static String getFirstDayOfLastMonth() {
        SimpleDateFormat format = new SimpleDateFormat(YMD);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String lastMonthStart = format.format(calendar.getTime()) + " 00:00";
        return lastMonthStart;
    }

    /**
     * 获取上个月的最后一天
     */
    public static String getLastDayOfLastMonth() {
        SimpleDateFormat sf = new SimpleDateFormat(YMD);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastMonthEnd = sf.format(calendar.getTime()) + " 23:59";
        return lastMonthEnd;
    }

    /**
     * 获取指定时间的年月日，时分
     *
     * @return
     */
    public static String getYearMothDayHourMinute(long currentTime) {
        return getDateFormat(currentTime, YMDHM);
    }


    public static String getDateFormat(long currentTime, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(currentTime);
    }


    /**
     * 获取24小时前的年月日，时分
     *
     * @return
     */
    public static String getYestodayYearMothDayHourMinute() {
        long currentTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        return getYearMothDayHourMinute(currentTime);
    }


    /**
     * 获取当前时间一个月之前的年月日 时分
     *
     * @return
     */
    public static String getLastMoneyYearMothDayHourMinute() {
        SimpleDateFormat sf = new SimpleDateFormat(YMD);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = sf.format(m);
        return mon + " 00:00";
    }


    /**
     * 获取当前年月日，时分
     *
     * @return
     */
    public static String getYearMothDayHourMinute() {
        return getYearMothDayHourMinute(System.currentTimeMillis());
    }


    /**
     * 比较两个两期，第一个日期是否大
     *
     * @param str1    第一个日期
     * @param str2    第二个日期
     * @param pattern 当前需要比较的日期格式
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2, String pattern) {

        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

}

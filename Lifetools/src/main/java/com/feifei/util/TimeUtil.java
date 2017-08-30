package com.feifei.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    private static final String[] WEEK = {"天", "一", "二", "三", "四", "五", "六"};
    public static final String XING_QI = "星期";
    public static final String ZHOU = "周";

    /**
     * @return 2014-05-16 21:27:37
     * 获取当前系统时间
     */
    public static String getCurTime() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat(
                "yyyy年MM月dd日HH时mm分", Locale.getDefault());
        Time = newSimpleDate.format(new java.util.Date());
        return Time;
    }

    /**
     * @param format
     * @return 自定义格式
     * 获取当前系统时间，"yyyy年MM月dd日 HH时mm分ss秒"
     */
    public static String getCurTime(String format) {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat(format,
                Locale.getDefault());
        Time = newSimpleDate.format(new java.util.Date());
        return Time;
    }

    /**
     * @return 年份int
     * 获取年份
     */
    public static int getYear() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("yyyy",
                Locale.getDefault());
        Time = newSimpleDate.format(new java.util.Date());
        return Integer.valueOf(Time);
    }

    /**
     * @return 月份int
     * 获取月份
     */
    public static int getMonth() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("MM",
                Locale.getDefault());
        Time = newSimpleDate.format(new java.util.Date());
        return Integer.valueOf(Time);
    }

    /**
     * @return 日期int
     * 获取日期
     */
    public static int getDay() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("dd",
                Locale.getDefault());
        Time = newSimpleDate.format(new java.util.Date());
        return Integer.valueOf(Time);
    }

    /**
     * @return 05/16 周五
     * 获取星期几
     */
    public static String getZhouWeek() {
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("MM/dd",
                Locale.getDefault());
        return newSimpleDate.format(new Date(System.currentTimeMillis())) + " "
                + getWeek(0, ZHOU);
    }

    public static String getWeek(int num, String format) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int weekNum = c.get(Calendar.DAY_OF_WEEK) + num;
        if (weekNum > 7) {
            weekNum = weekNum - 7;
        }
        return format + WEEK[weekNum - 1];
    }

    /**
     * @param timesamp
     * @return 昨天，今天，或几天前
     * 获取距离天数
     */
    public static String getDay(long timesamp) {
        String result = "";
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("dd",
                Locale.getDefault());
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(newSimpleDate.format(today))
                - Integer.parseInt(newSimpleDate.format(otherDay));
        switch (temp) {
            case 0:
                result = "今天";
                break;
            case 1:
                result = "昨天";
                break;
            case 2:
                result = "前天";
                break;
            default:
                result = temp + "天前";
                break;
        }
        return result;
    }

    /**
     * @param month
     * @param day
     * @return 星座
     * 根据月日获取星座
     */
    public static String getConstellation(int month, int day) {
        if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
            return "水瓶座";
        } else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
            return "双鱼座";
        } else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
            return "白羊座";
        } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
            return "金牛座";
        } else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
            return "双子座";
        } else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
            return "巨蟹座";
        } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
            return "狮子座";
        } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
            return "处女座";
        } else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
            return "天秤座";
        } else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
            return "天蝎座";
        } else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
            return "射手座";
        } else if ((((month != 12) || (day < 22)))
                && (((month != 1) || (day > 19)))) {
            return "魔蝎座";
        }
        return "";
    }

    /**
     * 根据年月日获取年龄
     */
    public static int getAge(int year, int month, int day) {
        int age = 0;
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == year) {
            if (calendar.get(Calendar.MONTH) == month) {
                if (calendar.get(Calendar.DAY_OF_MONTH) >= day) {
                    age = calendar.get(Calendar.YEAR) - year + 1;
                } else {
                    age = calendar.get(Calendar.YEAR) - year;
                }
            } else if (calendar.get(Calendar.MONTH) > month) {
                age = calendar.get(Calendar.YEAR) - year + 1;
            } else {
                age = calendar.get(Calendar.YEAR) - year;
            }
        } else {
            age = calendar.get(Calendar.YEAR) - year;
        }
        if (age < 0) {
            return 0;
        }
        return age;
    }

    // 根据年份和月份判断这个月的天数
    public static int getDay(int year, int month) {
        int day;
        if (year % 4 == 0 && year % 100 != 0) { // 闰年
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 29;
            } else {
                day = 30;
            }
        } else { // 平年
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 28;
            } else {
                day = 30;
            }
        }
        return day;
    }

}

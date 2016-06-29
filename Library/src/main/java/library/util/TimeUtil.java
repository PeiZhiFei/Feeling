package library.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import my.library.R;

public class TimeUtil {

    public static String long2StringDateChater2(Context context, long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return df.format(new Date(timeMills));
    }

    public static String long2StringDateUnit(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        return df.format(new Date(timeMills));
    }

    public static String long2StringDateUnit2(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return df.format(new Date(timeMills));
    }

    public static String hhmm(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("hh" + "-" + "mm", Locale.getDefault());
        return df.format(new Date(timeMills));
    }

    public static String HHmm(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("HH" + "-" + "mm", Locale.getDefault());
        return df.format(new Date(timeMills));
    }

    public static String yyMM(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("yy" + "-" + "MM", Locale.getDefault());
        return df.format(new Date(timeMills));
    }


    public static String yyMMdd(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("yy" + "-" + "MM" + "-" + "dd", Locale.getDefault());
        return df.format(new Date(timeMills));
    }

    public static String yyyyMMdd(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy" + "-" + "MM" + "-" + "dd", Locale.getDefault());
        return df.format(new Date(timeMills));
    }

    public static String yyMMddhhmm(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("yy" + "-" + "MM" + "-" + "dd" + " HH" + ":" + "mm", Locale.getDefault());
        return df.format(new Date(timeMills));
    }
    public static String meetyyyyMMddhhmm(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy" + "-" + "MM" + "-" + "dd" + "    HH" + ":" + "mm", Locale.getDefault());
        return df.format(new Date(timeMills));
    }

    public static String yyyyMMddToday(Context context, long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("yy" + "-" + "MM" + "-" + "dd", Locale.getDefault());
        if (isToday(timeMills)) {
            return context.getResources().getString(R.string.time_text_jinTian);
        } else {
            return df.format(new Date(timeMills));
        }
    }

    public static String yyMMddhhmmToday(Context context, long timeMills) {
        SimpleDateFormat df1 = new SimpleDateFormat("yy" + "-" + "MM" + "-" + "dd" + " HH" + ":" + "mm", Locale.getDefault());
        SimpleDateFormat df3 = new SimpleDateFormat(" HH" + ":" + "mm", Locale.getDefault());

        if (isToday(timeMills)) {
            return context.getResources().getString(R.string.time_text_jinTian) + df3.format(new Date(timeMills));
        } else {
            return df1.format(new Date(timeMills));
        }
    }

    public static String yyMMddhhmmToday2(Context context, long timeMills) {

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM" + "-" + "dd", Locale.getDefault());
        SimpleDateFormat df3 = new SimpleDateFormat(" HH" + ":" + "mm", Locale.getDefault());

        if (isToday(timeMills)) {
            return context.getResources().getString(R.string.time_text_jinTian) + df3.format(new Date(timeMills));
        } else {
            return df1.format(new Date(timeMills));
        }
    }

    //判断是否是今天
    public static boolean isToday(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("yy" + "-" + "MM" + "-" + "dd", Locale.getDefault());
        String today = df.format(System.currentTimeMillis());
        String result = df.format(new Date(timeMills));
        return today.equals(result);
    }

    public static String long2StringDate(Context context, long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM" + "-" + "dd", Locale.getDefault());
        String today = df.format(Calendar.getInstance().getTime());
        String result = df.format(new Date(timeMills));
        return today.equals(result) ? context.getResources().getString(R.string.time_text_jinTian) : result;
    }

    public static String long2StringTimeHour(long timeMills) {
        SimpleDateFormat df = new SimpleDateFormat("HH" + ":" + "mm", Locale.getDefault());
        return df.format(new Date(timeMills));
    }


    public static String long2StringDetailDate(Context context, long timeMills) {
        return long2StringDate(context, timeMills) + " " + long2StringTimeHour(timeMills);
    }

    public static String long2StringDetailDate1(Context context, long timeMills) {
        return yyyyMMddToday(context, timeMills) + " " + long2StringTimeHour(timeMills);
    }

    public static String long2ChatterDetailData(Context context, long timeMills) {
        String result = long2StringDate(context, timeMills);
        if (result.equals(context.getResources().getString(R.string.time_text_jinTian))) {
            return long2StringDateChater2(context, timeMills);
        } else {
            return result;
        }
    }

    public static String long2NoticeDate(Context context, long timeMills) {
        Date currentDate = new Date();
        long intervalTime = currentDate.getTime() - timeMills;
        //一小时之内
        if (intervalTime < (60 * 60 * 1000)) {
            long minute = intervalTime / (1000 * 60);
            return minute + context.getResources().getString(R.string.notice_time_overdue_in_one_hour);
        }
        return long2StringDate(context, timeMills);
    }

    //这里学会自定义时间
    public static String long2NoticeDate2(Context context, long timeMills) {
        Date currentDate = new Date();
        long intervalTime = currentDate.getTime() - timeMills;
        //一小时之内
        if (intervalTime < (60 * 60 * 1000)) {
            long minute = intervalTime / (1000 * 60);
            if (minute == 0) {
                return "1分钟内";
            } else {
                return minute + "分钟前";
            }
        } else if (intervalTime >= (60 * 60 * 1000) && intervalTime < (24 * 60 * 60 * 1000)) {
            long hour = intervalTime / (1000 * 60 * 60);
            return hour + "小时前";
        } else if (intervalTime >= (24 * 60 * 60 * 1000)) {
            long day = intervalTime / (24 * 1000 * 60 * 60);
            return day + "天前";
        } else {
            return "";
        }
    }


    /**
     * 超过一小时的时间
     */
    public static String getMills2S(long totalMills, long currtTime) {

        long yu_mills = totalMills - currtTime;
        long yu_s = yu_mills / 1000;

        long diaplay_m = yu_s / 60;
        long diaplay_s = yu_s % 60;
        String mm = "";
        String ss = "";
        if (diaplay_m < 10) {
            mm = "0" + diaplay_m;
        } else {
            mm = "" + diaplay_m;
        }
        if (diaplay_s < 10) {
            ss = "0" + diaplay_s;
        } else {
            ss = "" + diaplay_s;
        }
        return mm + ":" + ss;
    }

    private static final String[] WEEK = {"天", "一", "二", "三", "四", "五", "六"};
    public static final String XING_QI = "星期";
    public static final String ZHOU = "周";

    /**
     * 昨天，今天，或几天前
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


    public String getDescription(long time) {
        long currentTime = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String endTime = format.format(date);
        int a = 0;
        try {
            a = isYeaterday(new Date(time), new Date(currentTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (a == 0) {
            return "昨天 " + endTime;
        } else if (a == -1) {
            return "今天 " + endTime;
        } else if (a == 1) {
            return "前天 " + endTime;
        } else {
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm",
                    Locale.getDefault());
            return format2.format(date);
        }
    }

    /**
     * @return 2014-05-16 21:27:37
     * @notice 获取当前系统时间
     */
    public static String getCurTime() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat(
                "yyyy年MM月dd日HH时mm分", Locale.getDefault());
        Time = newSimpleDate.format(new Date());
        return Time;
    }

    /**
     * @param format
     * @return 自定义格式
     * @notice 获取当前系统时间，"yyyy年MM月dd日 HH时mm分ss秒"
     */
    public static String getCurTime(String format) {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat(format,
                Locale.getDefault());
        Time = newSimpleDate.format(new Date());
        return Time;
    }

    /**
     * @return 年份int
     * @notice 获取年份
     */
    public static int getYear() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("yyyy",
                Locale.getDefault());
        Time = newSimpleDate.format(new Date());
        return Integer.valueOf(Time);
    }

    /**
     * @return 月份int
     * @notice 获取月份
     */
    public static int getMonth() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("MM",
                Locale.getDefault());
        Time = newSimpleDate.format(new Date());
        return Integer.valueOf(Time);
    }

    /**
     * @return 日期int
     * @notice 获取日期
     */
    public static int getDay() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("dd",
                Locale.getDefault());
        Time = newSimpleDate.format(new Date());
        return Integer.valueOf(Time);
    }


    /**
     * @return 05/16 周五
     * @notice 获取星期几
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
     * 是不是昨天
     */
    private int isYeaterday(Date oldTime, Date newTime) throws ParseException {
        if (newTime == null) {
            newTime = new Date();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        String todayStr = format.format(newTime);
        Date today = format.parse(todayStr);

        if ((today.getTime() - oldTime.getTime()) > 0
                && (today.getTime() - oldTime.getTime()) <= 86400000) {
            return 0;
        } else if ((today.getTime() - oldTime.getTime()) <= 0) {
            return -1;
        } else if ((today.getTime() - oldTime.getTime()) >= 86400000
                && (today.getTime() - oldTime.getTime()) <= 2 * 86400000) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
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
     * 根据年份和月份判断这个月的天数
     */
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

}

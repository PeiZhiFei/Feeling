package library.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tools {

    public static String getVersionName(Context app) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = app.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(app.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }


    /**
     * @param context
     * @return int
     * @notice 获取屏幕的宽度
     */
    public static int getWidth(Context context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * @param context
     * @return int
     * @notice 获取屏幕的高度
     */
    public static int getHeight(Context context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /**
     * @param context
     * @return float
     * @notice 获取屏幕的密度
     */
    public static float getDensity(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.density;
    }

    //这在代码中用到，做下适配
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * @param context
     * @param dp
     * @return int
     * @notice DP转换为PX
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * @param context
     * @param px
     * @return int
     * @notice PX转换为DP
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * @param value
     * @param context
     * @return
     * @notice SP转PX
     */
    public static int sp2px(float value, Context context) {
        float scale = value * context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scale + 0.5f);
    }

    /**
     * @param context
     * @param value
     * @return
     * @notice PX转SP
     */
    public static int px2sp(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + 0.5f);
    }

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || s.equals("");
    }

    /**
     * 以某种格式去格式化日期
     *
     * @param timestamp 等同于long型的时间数值
     * @param pattern   格式字符串，如HHmmss
     * @return
     */
    public static String formatTimes(String timestamp, String pattern) {
        Date date = new Date(Long.valueOf(timestamp) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 获取字符串资源
     *
     * @param context
     * @param str     字符串资源
     * @return
     */
    public static String getStr(Context context, int str) {
        return context != null ? context.getString(str) : "111";
    }

    /**
     * 截取小数后2位四舍五入
     */
    public static float scale(float number) {
        BigDecimal bd = new BigDecimal(number);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float scale1(float number) {
        BigDecimal bd = new BigDecimal(number);
        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 截取小数后2位四舍五入
     */
    public static double scale(double number) {
        BigDecimal bd = new BigDecimal(number);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double scale2(double fee) {
        BigDecimal bd = new BigDecimal(fee);
        //直接截断
        return bd.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public static double scale6(double fee) {
        BigDecimal bd = new BigDecimal(fee);
        //直接截断
        return bd.setScale(6, BigDecimal.ROUND_DOWN).doubleValue();
    }

}

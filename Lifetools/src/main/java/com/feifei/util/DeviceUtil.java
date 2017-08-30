package com.feifei.util;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.feifei.lifetools.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class DeviceUtil {

    /**
     * @notice 全屏
     */
    public static void fullScreen(Context context) {
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
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
     * @param view
     * @return int
     * @notice 获取状态栏高度
     */
    public static int getStatusbarHeight(View view) {
        Rect outRect = new Rect();
        view.getWindowVisibleDisplayFrame(outRect);
        return outRect.top;
    }

    /**
     * @param context
     * @notice 启动activity时不自动弹出键盘
     */
    public static void keyboardsNotPopup(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * @param view
     * @notice 弹出键盘
     */
    public static void keyboardShow(TextView view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    /**
     * @param context
     * @notice 关闭键盘
     */
    public static void keyboardClose(Context context) {
        InputMethodManager imm = (InputMethodManager) ((Activity) context)
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (((Activity) context).getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * @param view
     * @notice TextView获取焦点
     */
    public static void focus(TextView view) {
        view.setEnabled(true);
        view.setCursorVisible(true);
        view.requestFocus();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
    }

    /**
     * @param view
     * @notice TextView获得焦点并弹出键盘
     */
    public static void focusAndShow(TextView view) {
        focus(view);
        keyboardShow(view);
    }

    /**
     * @param textView
     * @param start：开始位置
     * @param end：结束位置
     * @notice TextView添加下划线
     */
    public static void addUnderlineText(TextView textView, int start, int end) {
        textView.setFocusable(true);
        textView.setClickable(true);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText().toString().trim());
        spannableStringBuilder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableStringBuilder);
    }

    /**
     * @param context
     * @return boolean
     * @notice 判断当前屏幕是否是横屏
     */
    public static boolean isLandscape(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param context
     * @return String
     * @notice 获得版本信息
     */
    public static String getVersion(Context context) {
        PackageInfo packInfo = null;
        try {
            packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }

    /**
     * @param context
     * @return String
     * @notice 获取包名
     */
    public static String getPackage(Context context) {
        PackageInfo packInfo = null;
        try {
            packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
        }
        return packInfo.packageName;
    }

    /**
     * @return String
     * @notice 获取设备型号
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * @param context
     * @return String
     * @notice 获取设备ID，需要加权限
     * @notice <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     */
    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tManager.getDeviceId();
        } catch (Exception e) {
        }
        return deviceId;
    }

    /**
     * @param context
     * @return boolean
     * @notice 判断是否为平板
     */
    public static boolean isPad(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches >= 7.0 ? true : false;
    }

    /**
     * @return boolean
     * @notice 是否是小米设备
     */
    public static boolean isMi() {
        return android.os.Build.MANUFACTURER.equals("Xiaomi") ? true : false;
    }

    /**
     * @return boolean
     * @notice 是否是简体中文
     */
    public static boolean isZhCN() {
        return Locale.getDefault().toString().equals("zh_CN") ? true : false;
    }

    /**
     * @param context
     * @return boolean
     * @notice 判断设备是否支持闪光灯
     */
    public static boolean hasFlashlight(Context context) {
        boolean result = false;
        PackageManager pm = context.getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        for (FeatureInfo f : features) {
            if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * @param context
     * @return boolean
     * @notice GPS是否打开
     */
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    long currentTime = System.currentTimeMillis();
    private static long touchTime = 0;
    private static long waitTime = 2000;

    public static void doubleQuit(Activity activity) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            touchTime = currentTime;
            activity.finish();
        }
    }

    /**
     * @param context
     * @notice 使ActionBar的overflow显示，不被菜单键屏蔽
     */
    public static void setOverflowShow(Context context) {
        try {
            ViewConfiguration config = ViewConfiguration.get(context);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param searchView
     * @notice 反射修改SerachView
     */
    public static void searchViewTheme(Context context, SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();

            // 设置提示图片
            Field hintField = argClass.getDeclaredField("mSearchHintIcon");
            hintField.setAccessible(true);
            View hintIcon = (View) hintField.get(searchView);
            hintIcon.setVisibility(View.GONE);

            // 搜索面板背景
            Field plate = argClass.getDeclaredField("mSearchPlate");
            plate.setAccessible(true);
            View mView = (View) plate.get(searchView);
            mView.setBackgroundResource(R.drawable.edit_normal);

            // 清除按钮图标
            Field clearButton = argClass.getDeclaredField("mCloseButton");
            clearButton.setAccessible(true);
            ImageView clear = (ImageView) clearButton.get(searchView);
            clear.setImageResource(R.drawable.actionbar_clear);

            // 搜索按钮图标
            Field search = argClass.getDeclaredField("mSearchButton");
            search.setAccessible(true);
            ImageView searchImage = (ImageView) search.get(searchView);
            searchImage.setImageResource(R.drawable.actionbar_search);

            // 设置文字区域
            Field queryTextView = argClass.getDeclaredField("mQueryTextView");
            queryTextView.setAccessible(true);
            TextView textView = (TextView) queryTextView.get(searchView);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setHintTextColor(Color.parseColor("#ffffff"));

            // 设置光标属性
            Field cursorDrawable = argClass.getDeclaredField("mCursorDrawableRes");
            cursorDrawable.setAccessible(true);
            ImageView cursor = (ImageView) cursorDrawable.get(searchView);
            cursor.setImageResource(R.drawable.actionbar_search);

        } catch (Exception e) {
        }
    }

    /**
     * @param context
     * @param actionbar
     * @notice 反射修改actionbar的宽度
     */
    public static void actionbarWidth(Context context, ActionBar actionbar) {
        try {
            Class<?> cActionBarImpl = Class.forName("com.android.internal.app.ActionBarImpl");
            // Class<?> cActionBarView = Class
            // .forName("com.android.internal.widget.ActionBarView");
            Field fActionView = cActionBarImpl.getDeclaredField("mActionView");
            fActionView.setAccessible(true);
            Object objActionView = fActionView.get(actionbar);
            // 这里是FrameLayout.LayoutParams
            ((ViewGroup) objActionView).setLayoutParams(new FrameLayout.LayoutParams((int) (DeviceUtil
                    .getWidth(context) * 0.705), FrameLayout.LayoutParams.MATCH_PARENT));

        } catch (Exception e) {
        }

    }


    /**
     * @param textView
     * @param context
     * @notice 变回车键为搜索键
     */
    public static void enterKeySearch(TextView textView, final Context context) {
        textView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    DeviceUtil.keyboardClose(context);
                }
                return false;
            }
        });
    }

    /**
     * @param size
     * @return
     * @notice 换算文件大小
     */
    public static String getFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "未知大小";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * @param context
     * @param packageName
     * @return boolean
     * @notice 判断指定包名的进程是否运行
     */
    public static boolean isRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (RunningAppProcessInfo rapi : infos) {
            Log.v("notity", rapi.processName.toString());
            if (rapi.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param context
     * @param packageName
     * @notice 杀掉进程
     */
    public static void killProcess(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(packageName);

    }

    /**
     * @param context
     * @notice 彻底杀掉自己
     */
    public static void killSelf(Context context) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /**
     * @param min：起始值
     * @param max：结束值
     * @return
     * @notice 获取随机数
     */
    public static int getRandom(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }

    /**
     * 备份data/app目录下本程序的apk安装文件到SD卡根目录下
     *
     * @param packageName
     * @throws IOException
     */
    public static void backupApp(String packageName, Context context) throws IOException {
        // 存放位置
        String newFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        String oldFile = null;
        try {
            // 原始位置
            oldFile = context.getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        File in = new File(oldFile);
        File out = new File(newFile + packageName + ".apk");
        if (!out.exists()) {
            out.createNewFile();
            LogUtil.log("文件备份成功！" + "存放于" + newFile + "目录下");
        } else {
            LogUtil.log("文件备份成功！" + "存放于" + newFile + "目录下");
        }
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        int count;
        // 文件太大的话，我觉得需要修改
        byte[] buffer = new byte[256 * 1024];
        while ((count = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
        }
        fis.close();
        fos.flush();
        fos.close();
    }

    public static int obj2Int(Object object) {
        return ((Number) object).intValue();
    }

    public static float obj2Float(Object object) {
        return ((Number) object).floatValue();
    }

    /**
     * 角度转弧度
     *
     * @param angle
     * @return
     */
    public static double angle2radians(float angle) {
        return angle / 180f * Math.PI;
    }

    /**
     * 弧度转角度
     *
     * @param radians
     * @return
     */
    public static double radians2angle(double radians) {
        return 180f * radians / Math.PI;
    }
}

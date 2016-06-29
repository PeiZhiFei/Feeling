package library.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;


public class DeviceUtil {

    /**
     * 全屏
     */
    public static void fullScreen(Context context) {
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    /**
     * @param view
     * @return int
     * 获取状态栏高度
     */
    public static int getStatusbarHeight(View view) {
        Rect outRect = new Rect();
        view.getWindowVisibleDisplayFrame(outRect);
        return outRect.top;
    }

    /**
     * @param context 启动activity时不自动弹出键盘
     */
    public static void keyboardsNotPopup(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * @param view 弹出键盘
     */
    public static void keyboardShow(TextView view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    /**
     * @param context 关闭键盘
     */
    public static void keyboardClose(Context context) {
        InputMethodManager imm = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (((Activity) context).getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * TextView获取焦点
     */
    public static void focus(TextView view) {
        view.setEnabled(true);
        view.setCursorVisible(true);
        view.requestFocus();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
    }

    /**
     * TextView获得焦点并弹出键盘
     */
    public static void focusAndShow(TextView view) {
        focus(view);
        keyboardShow(view);
    }

    /**
     * @param textView
     * @param start    ：开始位置
     * @param end      ：结束位置
     *                 TextView添加下划线
     */
    public static void addUnderlineText(TextView textView, int start, int end) {
        textView.setFocusable(true);
        textView.setClickable(true);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText().toString().trim());
        spannableStringBuilder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableStringBuilder);
    }

    /**
     * 判断当前屏幕是否是横屏
     */
    public static boolean isLandscape(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得版本信息
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取包名
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
     * 获取设备型号
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取设备ID，需要加权限
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
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
     * 判断是否为平板
     */
    public static boolean isPad(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches >= 7.0;
    }

    /**
     * 是否是小米设备
     */
    public static boolean isMi() {
        return android.os.Build.MANUFACTURER.equals("Xiaomi");
    }

    /**
     * 是否是简体中文
     */
    public static boolean isZhCN() {
        return Locale.getDefault().toString().equals("zh_CN");
    }

    /**
     * 判断设备是否支持闪光灯
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
     * GPS是否打开
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
     * 变回车键为搜索键
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
     * 换算文件大小
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
     * @return boolean
     * 判断指定包名的进程是否运行
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
     * 彻底杀掉自己
     */
    public static void killSelf(Context context) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * @param min ：起始值
     * @param max ：结束值
     * @return 获取随机数
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
            L.l("文件备份成功！" + "存放于" + newFile + "目录下");
        } else {
            L.l("文件备份成功！" + "存放于" + newFile + "目录下");
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
     */
    public static double angle2radians(float angle) {
        return angle / 180f * Math.PI;
    }

    /**
     * 弧度转角度
     */
    public static double radians2angle(double radians) {
        return 180f * radians / Math.PI;
    }

    public static String getThreadSign() {
        Thread thread = Thread.currentThread();
        long l = thread.getId();
        String name = thread.getName();
        String gname = thread.getThreadGroup().getName();
        long p = thread.getPriority();
        return (name + ":(id)" + l + ":(priority)" + p + ":(group)" + gname);
    }

    protected int distance(Point point1, Point point2) {
        int dx = point2.x - point1.x;
        int dy = point2.y - point1.y;

        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * angle in radians
     *
     * @param angle
     */
    protected int getDegrees(double angle) {
        angle = Math.toDegrees(angle);
        return (int) (angle <= -90 && angle >= -180 ? 450 + angle : angle + 90);
    }

    public static String getDeviceInfo(Context context) {
        try {
            JSONObject json = new JSONObject();
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tm.getDeviceId();
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = Tools.getWidth(activity);
        int height = Tools.getHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = Tools.getWidth(activity);
        int height = Tools.getHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    // 从manifest里面获取ApiKey，某个字段
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    //APP是否在后台运行
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    //---------------------------------------ViewUtil------------------------------------------
    public static int getListViewHeightBasedOnChildren(ListView view) {
        int height = getAbsListViewHeightBasedOnChildren(view);
        ListAdapter adapter;
        int adapterCount;
        if (view != null && (adapter = view.getAdapter()) != null && (adapterCount = adapter.getCount()) > 0) {
            height += view.getDividerHeight() * (adapterCount - 1);
        }
        return height;
    }

    public static int getAbsListViewHeightBasedOnChildren(AbsListView view) {
        ListAdapter adapter;
        if (view == null || (adapter = view.getAdapter()) == null) {
            return 0;
        }

        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, view);
            if (item instanceof ViewGroup) {
                item.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            }
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        height += view.getPaddingTop() + view.getPaddingBottom();
        return height;
    }


    public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }


    public static void setListViewHeightBasedOnChildren(ListView view) {
        setViewHeight(view, getListViewHeightBasedOnChildren(view));
    }


    public static void setAbsListViewHeightBasedOnChildren(AbsListView view) {
        setViewHeight(view, getAbsListViewHeightBasedOnChildren(view));
    }

    public static boolean uninstall(Context context, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return false;
        }

        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder(32).append("package:")
                .append(packageName).toString()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }

        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    public static Boolean isTopActivity(Context context, String packageName) {
        if (context == null || StringUtil.isEmpty(packageName)) {
            return null;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (isEmpty(tasksInfo)) {
            return null;
        }
        try {
            return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readAssertHtml(Context context, String file) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = context.getAssets().open(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
            return buf.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


}

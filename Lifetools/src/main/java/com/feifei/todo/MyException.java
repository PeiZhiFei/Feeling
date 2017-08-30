package com.feifei.todo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

import com.feifei.util.DialogUtil;
import com.feifei.util.DialogView;
import com.feifei.util.LogUtil;
import com.feifei.util.SendUtil;
import com.feifei.util.ToastUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;


//在application里面使用：MyException.getInstance().init (this);
//在原来的基础上去掉了写文件，增加了获取设备参数信息，弹出友好toast提示
public class MyException implements UncaughtExceptionHandler {

    private static MyException myException;
    private Context context;
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd-HH-mm-ss", Locale.getDefault ());
    // 用来存储设备信息和异常信息
    private final Map<String, String> infos = new HashMap<String, String> ();
    //    String path = FileUtil.getRootPath() + "log/";

    private MyException () {
    }

    /**
     * 同步方法，以免单例多线程环境下出现异常
     */
    public synchronized static MyException getInstance () {
        if (myException == null) {
            myException = new MyException ();
        }
        return myException;
    }

    public void init (Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler (myException);
    }

    @Override
    public void uncaughtException (Thread thread, Throwable ex) {
        //使用Toast来显示异常信息
        new Thread () {
            @Override
            public void run () {
                Looper.prepare ();
                ToastUtil.toast(context, "这样的bug都被你发现了！");
                Looper.loop ();
            }
        }.start ();

        // 收集设备参数信息
        collectCrash (context);
        saveCrash (ex);
        //showDialog();
        //退出程序,这是application级别的，finish不管用
        android.os.Process.killProcess (android.os.Process.myPid ());
    }


    private void showDialog () {
        new Thread () {
            @Override
            public void run () {
                Looper.prepare ();
                ToastUtil.toast (context, "123");
                ArrayList<String> list = new ArrayList<String> ();
                list.add ("很抱歉，生活小助手崩溃了");
                DialogUtil.dialogStyle1(context, "提示", "我知道了!", true, list, new DialogView.DialogViewListener() {
                            @Override
                            public void onRightClick() {

                            }

                            @Override
                            public void onLeftClick() {

                            }

                            @Override
                            public void onListItemClick(int i, String s) {

                            }

                            @Override
                            public void onButtonClick() {
                                ((Activity) context).finish();
                            }
                        }

                );
                Looper.loop ();
            }
        }.start ();
    }


    /**
     * 保存错误信息到文件中，返回文件名称
     */

    private String saveCrash (Throwable ex) {
        StringBuffer sb = new StringBuffer ();
        String time = formatter.format (new Date ());
        sb.append ("\n" + time + "----");
        for (Map.Entry<String, String> entry : infos.entrySet ()) {
            String key = entry.getKey ();
            String value = entry.getValue ();
            sb.append (key + "=" + value + "\n");
        }

        Writer writer = new StringWriter ();
        PrintWriter printWriter = new PrintWriter (writer);
        ex.printStackTrace (printWriter);
        Throwable cause = ex.getCause ();
        while (cause != null) {
            cause.printStackTrace (printWriter);
            cause = cause.getCause ();
        }
        printWriter.close ();

        String result = writer.toString ();
        sb.append (result);
        SendUtil.sendMail(result + mDeviceCrashInfo);
        LogUtil.log(result);
        //写下log文件
        //        try {
        //            String fileName = "exception.log";
        //            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        //                File dir = new File(path);
        //                if (!dir.exists()) {
        //                    dir.mkdirs();
        //                }
        //                FileOutputStream fos = new FileOutputStream(path + fileName, true);
        //                fos.write(sb.toString().getBytes());
        //                fos.close();
        //            }
        //            return fileName;
        //        } catch (Exception e) {
        //            LogUtil.log("写文件的时候发生了错误" + e);
        //        }
        return null;
    }

    private Properties mDeviceCrashInfo = new Properties ();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";

    /**
     * 收集程序崩溃的设备信息，设备号，系统版本，软件版本
     */
    public void collectCrash (Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager ();
            PackageInfo pi = pm.getPackageInfo (ctx.getPackageName (),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                mDeviceCrashInfo.put (VERSION_NAME,
                        pi.versionName == null ? "not set" : pi.versionName);
                mDeviceCrashInfo.put (VERSION_CODE, "" + pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.log ("Error while collect package info");
        }
        //使用反射来收集设备信息.在Build类中包含各种设备信息,
        //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        Field[] fields = Build.class.getDeclaredFields ();
        for (Field field : fields) {
            try {
                field.setAccessible (true);
                mDeviceCrashInfo.put (field.getName (), "" + field.get (null));
                LogUtil.log (field.getName () + " : " + field.get (null));
            } catch (Exception e) {
                LogUtil.log ("Error while collect crash info");
            }
        }
    }

}

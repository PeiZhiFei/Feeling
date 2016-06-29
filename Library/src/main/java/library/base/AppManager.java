package library.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import java.util.Calendar;
import java.util.Stack;

import library.util.S;

/**
 * 功能描述: Activity管理类
 */
public class AppManager {

    private static Stack<Activity> visiableActivityStack;
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 功能描述:获取AppManager对象，单例模式
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 功能描述: 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 功能描述: 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 功能描述: 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 功能描述: 结束所有Activity
     */
    public static void finishAllActivity() {
        //这里可能会报数组越界的错误提前异常捕获
        Stack<Activity> activityStackTemp = new Stack<>();
        activityStackTemp.addAll(activityStack);
        try {
            for (int i = 0, size = activityStackTemp.size(); i < size; i++) {
                if (null != activityStackTemp.get(i)) {
                    if (!activityStackTemp.get(i).isFinishing()) {
                        activityStackTemp.get(i).finish();
                    }
                }
            }
            activityStack.clear();
            visiableActivityStack.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述: 退出应用程序
     *
     * @param context      上下文
     * @param isBackground 是否开启后台运行
     */
    public void AppExit(Context context, Boolean isBackground) {
        try {
            stopTongji(context);
            quitKill(context);
            finishAllActivity();
/*
            ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.killBackgroundProcesses(context.getPackageName());
*/
//			android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if (!isBackground) {
//				System.exit(0);    // 程序在下载时开启了下载service，此处不要杀掉任何进程
            }
        }
    }

    private void stopTongji(Context context) {

        setTime(context, recLen);
//        L.l("stopTongji111111111111111111+" + recLen);
//        recLen = 0;
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void stopTongji2(Activity activity) {
        if (visiableActivityStack.size() <= 1) {
//            L.l("stopTongji2");
            stopTongji(activity);
        }
        if (activity != null && visiableActivityStack != null) {
            visiableActivityStack.remove(activity);
        }

    }

    public void startTongji2(Activity activity) {

        if (visiableActivityStack == null) {
            visiableActivityStack = new Stack<>();
        }
        visiableActivityStack.add(activity);

        if (isFirstToday(activity)) {
            clearData(activity);
            setFirstToday(activity);
        }
        //这里判断一下uid不为空再加入，主页前的那几个都不计入
        if (visiableActivityStack.size() <= 1) {
            //开始定时器
            if (getTime(activity) < 600) {
//                L.l("startTongji2");
                startTongji(activity);
            } else {
                if (getTime(activity) < 999) {
                    //打点积分：新的网络请求
                    //随机任务完成
                    S.s(activity, "jifen2", "20007", true);
                    if (tongji != null && tongji.isLogin()) {
                        tongji.getData();
                    }
                }
                setTime(activity, 1000);
            }
        }

    }

    private void quitKill(Context context) {
        // 如果开发者调用 Process.kill 或者 System.exit 之类的方法杀死进程，请务必在此之前调用此方法，用来保存统计数据
//        MobclickAgent.onKillProcess(context);
    }


    private int recLen = 0;
    Handler handler = new Handler();
    TimeRunnable runnable = null;

    class TimeRunnable implements Runnable {
        private Context context;

        public TimeRunnable(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            if (recLen < 600) {
                recLen++;
                handler.postDelayed(this, 1000);
            } else {
                stopTongji(context);
            }
        }
    }

    //统计app前台使用时间，如果只是登陆后的呢，那就给key加上uid，为空就不统计
    private void startTongji(Context context) {
        recLen = getTime(context);
//        L.l("startTongji111111111111111111+" + recLen);
        if (runnable == null) {
            runnable = new TimeRunnable(context);
        }
        handler.postDelayed(runnable, 1000);
    }

    private void clearData(Context context) {
        S.clearSP(context, "file_tongji");
    }

    private void setFirstToday(Context context) {
        S.s(context, "file_tongji", "date", Calendar.getInstance().getTimeInMillis());
    }

    //不知道他是怎么判断今天的，相差24小时好判断
    public static boolean isFirstToday(Context context) {
        long lastRewardedTime = S.readLong(context, "file_tongji", "date", 0);
        Calendar current = Calendar.getInstance();
        current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long todayBeginningTime = current.getTimeInMillis();
        return lastRewardedTime < todayBeginningTime;
    }

    private void setTime(Context context, int second) {
        S.s(context, "file_tongji", "time", second);
    }

    private int getTime(Context context) {
        return S.readInt(context, "file_tongji", "time", 0);
    }

    public interface Tongji {
        void getData();

        boolean isLogin();
    }

    public void setCall(Tongji tongji) {
        this.tongji = tongji;
    }

    private Tongji tongji;
}

package feifei.material.main;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import library.util.L;

public class MyApp extends Application {

    public static boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    public void initialize() {
        //发布模式开启异常上报，统计信息，关闭log
//            TCAgent.init(this);
//            TCAgent.setReportUncaughtExceptions(true);
//            CrashReport.initCrashReport(this, getCrashId(), DEBUG);
        L.setDEBUG(DEBUG);
        Fresco.initialize(this);
    }

}


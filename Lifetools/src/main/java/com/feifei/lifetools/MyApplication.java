package com.feifei.lifetools;

import android.app.Application;


public class MyApplication extends Application {
    private static MyApplication mInstance = null;
//    public BMapManager mBMapManager = null;

    public void onCreate() {
        super.onCreate();
        com.feifei.todo.MyException.getInstance().init (this);
        mInstance = this;
    }


    public static MyApplication getInstance() {
        return mInstance;
    }




}

package com.feifei.lifetools;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.feifei.util.AnimUtil;
import com.feifei.util.ConfigUtil;
import com.feifei.util.DeviceUtil;
import com.feifei.util.FileUtil;

public class BaseActivity extends MyActionbar {
    // 没有闪光灯则自动开启屏幕光，智能
    protected boolean hasFlashlight;
    protected boolean auto;
    // protected boolean th;
    protected boolean mainFirst2;
    protected boolean packageFirst;
    protected boolean scanFIrst;
    protected boolean torchFirst;
    protected boolean recoderFirst;
    protected boolean wordFirst;

    public static String path = FileUtil.getRootPath() + "lifetools/";
    public static String pathString = path + "Voice/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasFlashlight = DeviceUtil.hasFlashlight(this);
        initSetting();
        initFirst();
        //这个是带界面的
        MyException.getInstance().setContext(this);
    }

    private void initFirst() {
        scanFIrst = ConfigUtil.readBoolean(this, "scanFIrst", true);
        recoderFirst = ConfigUtil.readBoolean(this, "recoderFirst", true);
        wordFirst = ConfigUtil.readBoolean(this, "wordFirst", true);
        packageFirst = ConfigUtil.readBoolean(this, "packageFirst", true);
        mainFirst2 = ConfigUtil.readBoolean(this, "mainFirst2", true);
        torchFirst = ConfigUtil.readBoolean(this, "torchFirst", true);
    }

    // 获取设置数据，前两项默认开启
    private void initSetting() {
        auto = ConfigUtil.readBoolean(this, "auto", false);
        // th = ConfigUtil.readBoolean(this, "th", false);
    }

    protected void onStart() {
        super.onStart();
    }

    // 读取设置
    protected void onResume() {
        super.onResume();
        initSetting();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        AnimUtil.animToSlide(this);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AnimUtil.animBackSlideFinish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
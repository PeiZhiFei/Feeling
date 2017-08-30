package com.feifei.todo;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.feifei.util.ToastUtil;


//接受精简模式下通知栏点击事件的广播
public class NotifyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int i = intent.getIntExtra("i", -1);
        if (action.equals("cancel") & i != -1) {
            //这段代码如何重构一下
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(i);
            ToastUtil.toast(context, "已完成");
        }
    }
}

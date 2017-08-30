package com.feifei.todo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.feifei.util.ToastUtil;


//重启监听器，不起作用
public class BootReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive (Context context, Intent intent) {
        if (intent.getAction ().equals (action_boot)) {
            ToastUtil.toast(context, "BootBroadcastReceiver启动");
        }
    }

}

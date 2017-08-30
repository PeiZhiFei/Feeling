package com.feifei.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.feifei.lifetools.R;
import com.feifei.util.LogUtil;

import java.util.ArrayList;
import java.util.Map;


public class MyService extends Service {
    NotifyDbHelper helper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.log("服务开启");
        helper = new NotifyDbHelper(this);
        notice2(this);
        //SendUtil.sendAttachment();
        return super.onStartCommand(intent, flags, startId);
    }

    private void notice2(Context context) {
        ArrayList<NotifyTODO> array = helper.queryAll();
        for (NotifyTODO notifyTODO : array) {
            int i = Integer.valueOf(notifyTODO.getId());
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(context);
            Intent intent = new Intent(context, NotifyActivityDialog.class);
//            intent.putExtra ("p", PG.convertParcelable (notifyTODO));
            intent.putExtra("p", notifyTODO);
            //不同的intent需要不同的id，否则会被覆盖，第二个参数
            PendingIntent contentIndent = PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setSmallIcon(R.mipmap.icon)//细通知栏的图标，必须的,刚发出的通知图标
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon))//下拉下拉列表里面的图标（左侧大图标）
                    .setTicker("别忘了做哦！")//细通知栏的文字,首次出现的
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(notifyTODO.getItem())//设置下拉列表里的标题
                    .setAutoCancel(false)//是否点击取消，false有数字
                    .setOngoing(true)//设置不可清除
                    .setContentIntent(contentIndent);//设置intent
            notificationManager.notify(i, builder.build());
        }
    }


    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    int i;

    public void notice(Context context) {

        SharedPreferences share = context.getSharedPreferences("test", Context.MODE_PRIVATE);
        Map<String, String> map = (Map<String, String>) share.getAll();

        for (i = 1; i <= map.size(); i++) {
            String result = map.get(i + "");
            //之前把他写空了
            if (!TextUtils.isEmpty(result) && !result.equals("")) {
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                builder = new NotificationCompat.Builder(context);
                Intent intent = new Intent(context, NotifyActivityDialog.class);
                TODO todo = new TODO(result, "", false);
                todo.setId(i + "");
                intent.putExtra("i", i);
                intent.putExtra("p", todo);
                //不同的intent需要不同的id，否则会被覆盖，第二个参数
                PendingIntent contentIndent = PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setSmallIcon(R.mipmap.icon)//细通知栏的图标，必须的,刚发出的通知图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon))//下拉下拉列表里面的图标（左侧大图标）
                        .setTicker("别忘了做哦！")//细通知栏的文字,首次出现的
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(result)//设置下拉列表里的标题
                        .setAutoCancel(false)//是否点击取消，false有数字
                        .setOngoing(true)//设置不可清除
                        .setContentIntent(contentIndent);//设置intent
                notificationManager.notify(i, builder.build());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

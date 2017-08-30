package com.feifei.todo;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.feifei.lifetools.R;


//如果这里有了layout，那么以这里的为主而不是xml文件
public class DeskWidget extends AppWidgetProvider {

    //onUpdate 会在widget创建及被更新时调用
    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager,
                          int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            Log.e ("log", "this is [" + appWidgetId + "] onUpdate!");
            //只是id写错了
            RemoteViews remoteViews = new RemoteViews (context.getPackageName (), R.layout.desk);
            Intent intent = new Intent (context, NotifyActivityAdd.class);
            PendingIntent pendingIntent = PendingIntent.getActivity (context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent (R.id.desktext, pendingIntent);
            appWidgetManager.updateAppWidget (appWidgetIds[i], remoteViews);
        }
        super.onUpdate (context, appWidgetManager, appWidgetIds);
    }


    //onDeleted 会在widget被删除时调用
    @Override
    public void onDeleted (Context context, int[] appWidgetIds) {
        super.onDeleted (context, appWidgetIds);
    }

    //当第一个App widget实例被创建时，会调用
    @Override
    public void onEnabled (Context context) {
        super.onEnabled (context);
    }


    @Override
    public void onDisabled (Context context) {
        super.onDisabled (context);
    }


    //    String action = "feifei.todo.send";
    @Override
    public void onReceive (Context context, Intent intent) {
        super.onReceive (context, intent);
        //        if (intent.getAction().equals(action)){
        //            context.startActivity(new Intent(context,NotifyActivityAdd.class));
        //        }
    }

}

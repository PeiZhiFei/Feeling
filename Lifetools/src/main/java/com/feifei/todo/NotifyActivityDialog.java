package com.feifei.todo;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.feifei.lifetools.R;
import com.feifei.util.ConfigUtil;
import com.feifei.util.ToastUtil;


//加入一个小统计的功能
public class NotifyActivityDialog extends Activity {
    TextView textView;
    Button button1;
    Button button2;
    NotifyTODO notifyTODO;
    NotifyDbHelper notifyDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_dialog);
        notifyDbHelper = new NotifyDbHelper(this);
        textView = (TextView) findViewById(R.id.text);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        change(ConfigUtil.readInt(this, "theme", 1));
        //获取随机完成语
        final String[] welcome = getResources().getStringArray(R.array.welcome_tips_todo);
        final Intent intent = getIntent();

        notifyTODO = intent.getParcelableExtra("p");
        final int i = Integer.valueOf(notifyTODO.getId());
        textView.setText(Html.fromHtml("<font color=\"red\">" + notifyTODO.getItem() + "</font>" + " 完成了吗？"));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != -1) {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(i);
                    ConfigUtil.writeString(NotifyActivityDialog.this,notifyTODO.getId(),notifyTODO.getItem());
                    notifyDbHelper.delete(notifyTODO.getId());
                    //toast换成白字，dialog样式调调，美化一下界面
                    //count是取之前的数
                    int count = ConfigUtil.readInt(NotifyActivityDialog.this, "count", 0);
                    ConfigUtil.writeInt(NotifyActivityDialog.this, "count", count + 1);
                    if (count == 9) {
                        ToastUtil.toast(NotifyActivityDialog.this, "恭喜你：今天完成任务数达到10！", 2);
                    } else {
                        if (count == 49) {
                            ToastUtil.toast(NotifyActivityDialog.this, "恭喜你：今天完成任务数达到50！", 2);
                        } else {
                            if (count == 0) {
                                ToastUtil.toast(NotifyActivityDialog.this, "恭喜你完成第一条任务", 2);
                            } else if (i == 1) {
                                ToastUtil.toast(NotifyActivityDialog.this, "恭喜你完成创建的第一条任务", 2);
                            } else {
                                int index = (int) (Math.random() * (welcome.length - 1));
                                ToastUtil.toast(NotifyActivityDialog.this, welcome[index]);
                            }

                        }
                    }
                }
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence string = Html.fromHtml("别忘了 " + "<font color=\"red\">" + notifyTODO.getItem() + "</font>");
                ToastUtil.toast(NotifyActivityDialog.this, string);
                finish();
            }
        });


    }


    private void change(int i) {
        switch (i) {
            case 1:
                button1.setBackgroundResource(R.drawable.button_blue2);
                button2.setBackgroundResource(R.drawable.button_green);
                break;
            case 2:
                button1.setBackgroundResource(R.drawable.button_red2);
                button2.setBackgroundResource(R.drawable.button_yellow2);
                break;
        }
    }

}

package com.feifei.todo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.evernote.client.android.EvernoteSession;
import com.feifei.game.Tools;
import com.feifei.lifetools.R;
import com.feifei.util.ConfigUtil;
import com.feifei.util.DeviceUtil;
import com.feifei.util.DialogUtil;
import com.feifei.util.IntentUtil;
import com.feifei.util.NetworkUtil;
import com.feifei.util.SendUtil;
import com.feifei.util.ToastCustom;
import com.feifei.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



//这个程序也用不到数据库，也就不用sharepreference了，如果想重启或杀掉进程后数据仍在，需要重新设计
//灵感来源来当初做库的时候的style，透传推送，当时还做jar包，使用helper来找资源，现在有了aar就不需要了
public class NotifyActivityAdd extends Activity {
    EditText editText;
    Button button;
    View layout;
    SimpleDateFormat df;
    //id不会重，因为进程被杀死后通知也就全部取消了，QQ也是这样，只有不被杀死才会有id唯一的问题，那需要root
    //    static int i = 1;
    boolean notlite, open;
    PopupMenu popupMenu;
    private String url;
    Intent intent;
    boolean serviceOn = false;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_notify_add);
        notifyDbHelper = new NotifyDbHelper (this);
        intent = new Intent (this, MyService.class);

        boolean first = ConfigUtil.readBoolean(this, "first", true);
        if (first) {

            ConfigUtil.writeBoolean (this, "first", false);
            //延迟弹出键盘，很优雅
            new Handler ().postDelayed (new Runnable () {

                @Override
                public void run () {
                    ToastCustom.superToast(NotifyActivityAdd.this, "长按有惊喜哦！");

                }
            }, 1500);
            //延迟弹出键盘，很优雅
            new Handler ().postDelayed (new Runnable () {

                @Override
                public void run () {
                    DeviceUtil.keyboardShow(editText);
                }
            }, 4000);
        } else {
            startService (intent);
            serviceOn = true;
            //延迟弹出键盘，很优雅
            new Handler ().postDelayed (new Runnable () {

                @Override
                public void run () {
                    DeviceUtil.keyboardShow (editText);
                }
            }, 1000);
        }
        editText = (EditText) findViewById (R.id.text);
        button = (Button) findViewById (R.id.time);
        layout = findViewById (R.id.layout);
        //加载用户定义的皮肤
        change (ConfigUtil.readInt (this, "theme", 0));
        //最大是12月，int最大是2147483647，这样可以保证int的id唯一性在一年内
        df = new SimpleDateFormat ("MMddHHmmss", Locale.getDefault ());

        editText.setOnKeyListener (new View.OnKeyListener () {
            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {

                if (event.getAction () == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                            || keyCode == KeyEvent.KEYCODE_ENTER) {
                        sendNotice ();
                    }
                }
                return false;
            }
        });
        button.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                sendNotice ();
            }
        });

        //长按弹出菜单
        button.setOnLongClickListener (new View.OnLongClickListener () {
            @Override
            public boolean onLongClick (View v) {
                Menu menu = popupMenu.getMenu ();
                //        menu.findItem(R.id.action2).setChecked(open);
                //        menu.findItem(R.id.action3).setChecked(notlite);
                button.setText ("设置");
//                button.setTextColor(getResources().getColor(R.color.yigreen));
                button.setTextColor (Color.parseColor ("#33ff99"));
                notlite = ConfigUtil.readBoolean (NotifyActivityAdd.this, "notlite", true);
                open = ConfigUtil.readBoolean (NotifyActivityAdd.this, "open", true);
                menu.findItem (R.id.action2).setTitle (open ? "关闭振动" : "开启振动");
                menu.findItem (R.id.action3).setTitle (notlite ? "精简模式" : "完整模式");
                popupMenu.show ();
                return false;
            }
        });


        //设置主题管用
        Context wrapper = new ContextThemeWrapper (this, R.style.popupMenu);
        popupMenu = new PopupMenu (wrapper, button);
        popupMenu.inflate (R.menu.setting);
        popupMenu.setOnMenuItemClickListener (
                new PopupMenu.OnMenuItemClickListener () {
                    public boolean onMenuItemClick (MenuItem item) {
                        switch (item.getItemId ()) {
                            case R.id.action1:
                                //换肤
                                changeTheme ();
                                break;
                            case R.id.action2:
                                //                                                             boolean result = item.isChecked();
                                //                                                             item.setChecked(!result);
                                ConfigUtil.writeBoolean (NotifyActivityAdd.this, "open", !open);
                                break;
                            case R.id.action3:
                                //                                                             boolean result2 = item.isChecked();
                                //                                                             item.setChecked(!result2);
                                ConfigUtil.writeBoolean (NotifyActivityAdd.this, "notlite", !notlite);
                                if (notlite) {
                                    ArrayList<String> arrayList = new ArrayList<String> ();
                                    arrayList.add ("1.可以批量删除");
                                    arrayList.add ("2.点击无需确认");
                                    arrayList.add ("3.数据不会持久存储");
                                    arrayList.add ("4.数据无法保存到印象笔记");
                                    arrayList.add ("5.与完整模式的数据互不干扰");
                                    DialogUtil.dialogStyle0(NotifyActivityAdd.this, "精简模式说明", false, arrayList, null);
//                                    ToastCustom.toast (NotifyActivityAdd.this, "开启精简模式可以批量删除，点击无确认弹窗，程序退出后数据会清空，但原有完整模式的数据不受影响", Gravity.CENTER, true);

                                }
                                //                                item.setChecked(!item.isChecked());
                                break;
                            case R.id.action4:
                                dialogFeedback ();
                                break;
                            case R.id.action5:
                                saveToEvernote ();
                                break;
                            case 5:
                                //一句话完成反馈，包括空检查，发送，弹框
                                //                                DialogUtil.dialogFeedback(NotifyActivityAdd.this,null);
                                //                                DownloadUtil.downloadFile(NotifyActivityAdd.this, url);
                                new Download ().download (NotifyActivityAdd.this, url);
                                break;

                        }
                        return true;
                    }
                }

        );
        popupMenu.setOnDismissListener (new PopupMenu.OnDismissListener () {
            @Override
            public void onDismiss (PopupMenu menu) {
                button.setText ("添加");
                button.setTextColor (Color.parseColor ("#ffd54f"));
            }
        });


    }

    EverNote everNote;

    private void saveToEvernote () {
        everNote = new EverNote (this);
        boolean Login = ConfigUtil.readBoolean (this, "evernote", false);
        if (!Login) {
            everNote.author ();
        } else {
            //todo 这里该开个服务
            everNote.saveNote2 (notifyDbHelper.queryAll ());
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        switch (requestCode) {
            case EvernoteSession.REQUEST_CODE_OAUTH:
                if (resultCode == Activity.RESULT_OK) {
                    ConfigUtil.writeBoolean (NotifyActivityAdd.this, "evernote", true);
                    //todo 这里该开个服务
                    everNote.saveNote2 (notifyDbHelper.queryAll ());
                }
                break;
        }
    }

    //回车和按钮都可以触发
    private void sendNotice () {
        //是要在这里取才对
        notlite = ConfigUtil.readBoolean (NotifyActivityAdd.this, "notlite", true);
        open = ConfigUtil.readBoolean (NotifyActivityAdd.this, "open", true);
        String s = editText.getText ().toString ();
        if (TextUtils.isEmpty (s) | s.equals ("")) {
            ToastCustom.superToast(NotifyActivityAdd.this, "请输入点内容吧");
        } else {
            String time = df.format (new Date ());
            NotifyTODO notifyTODO = new NotifyTODO (s, time);
            editText.setText ("");
            notice (notifyTODO);
        }
    }

    //精简模式下不保存数据,其实用不到set
    //    private void saveSet (TODO todo) {
    //        Set<String> set = new HashSet ();
    //        set.add (i + "");
    //        set.add (todo.getItem ());
    //        BootReceiver.writeSet (this, "set", set);
    //    }

    public void changeTheme () {
        if (ConfigUtil.readInt (this, "theme", 1) == 1) {
            change (2);
            ConfigUtil.writeInt (this, "theme", 2);
        } else {
            change (1);
            ConfigUtil.writeInt (this, "theme", 1);
        }
    }

    private void change (int i) {
        switch (i) {
            case 1:
                button.setTextColor (Color.parseColor ("#ffd54f"));
                button.setBackgroundResource (R.drawable.button_right1);

                editText.setHintTextColor (getResources ().getColor (R.color.greydark));
                editText.setTextColor (Color.parseColor ("#ffffff"));
                editText.setBackgroundResource (R.drawable.edittext1);

                layout.setBackgroundResource (R.drawable.notify);
                break;
            case 2:
                button.setTextColor (getResources ().getColor (R.color.red));
                button.setBackgroundResource (R.drawable.button_right2);

                editText.setHintTextColor (Color.parseColor ("#d7ccc8"));
                editText.setTextColor (Color.parseColor ("#795548"));
                editText.setBackgroundResource (R.drawable.edittext2);

                layout.setBackgroundResource (R.drawable.notify2);
                break;
        }
    }

    private void notice (NotifyTODO notifyTODO) {
        //使用兼容库是NotificationCompat，不用就只是API14之后的可用
        //以后低版本的用不着了，有兼容库  Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
        NotificationManager notificationManager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder (this);
        PendingIntent contentIndent;
        int i = Integer.valueOf (notifyTODO.getId ());
        if (notlite) {
            Intent intent = new Intent (this, NotifyActivityDialog.class);
            //            intent.putExtra ("i", i);
//            intent.putExtra ("p", PG.convertParcelable (notifyTODO));
            intent.putExtra ("p",notifyTODO);
            //不同的intent需要不同的id，否则会被覆盖，第二个参数
            contentIndent = PendingIntent.getActivity (this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            Intent intent2 = new Intent ();
            //            intent2.putExtra ("i", i);
            intent2.putExtra ("p", notifyTODO);
            intent2.setAction ("cancel");
            //            sendBroadcast(intent2);//这里并没有使用发送广播
            contentIndent = PendingIntent.getBroadcast (this, i, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder.setSmallIcon (R.mipmap.icon)//细通知栏的图标，必须的,刚发出的通知图标
                .setLargeIcon (BitmapFactory.decodeResource (getResources (), R.mipmap.icon))//下拉下拉列表里面的图标（左侧大图标）
                .setTicker ("别忘了做哦！")//细通知栏的文字,首次出现的
                .setWhen (System.currentTimeMillis ())
                .setContentTitle (notifyTODO.getItem ())//设置下拉列表里的标题
                .setAutoCancel (!notlite)//是否点击取消，false有数字
                .setOngoing (notlite)//设置不可清除
                        // .setNumber(3)//显示在右下角和小图标一样
                        // .setContentInfo("")
                        // .setContentText(todo.getItem())//设置上下文内容
                        // .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        // .setContentIntent(notlite ? contentIndent : contentIndent2);//设置intent
                .setContentIntent (contentIndent);//设置intent
        if (open) {
            builder.setDefaults (Notification.DEFAULT_SOUND)//声音震动闪光灯都在这里
                    .setVibrate (new long[]{0, 250, 0, 0});//延迟0ms，然后振动300ms，在延迟500ms，接着在振动700ms
        }
        Notification notification = builder.build ();
        //如果id不变，则只会更新内容而不会再添加
        notificationManager.notify (i, notification);
        if (notlite) {
            notifyDbHelper.insert (notifyTODO);
        }
        // i++;//原来就是这句顺序错了
        //原来用的sharepreference，一开始是集合，后来是键值对，现在用数据库，为了删除一行
        // BootReceiver.writeString (this, "" + i, todo.getItem ());

    }

    NotifyDbHelper notifyDbHelper;

    //意见反馈，我这里的样式有变，这里的dialogutil应该重写，传一个layout进来，有固定的id
    public void dialogFeedback () {
        final Dialog dialog_feedback = new Dialog (this, R.style.dialog_white_style);
        View feedbackDialog = LayoutInflater.from (this).inflate (R.layout.activity_notify_add, (ViewGroup) null);
        final EditText edit_feedback = (EditText) feedbackDialog.findViewById (R.id.text);
        Button send = (Button) feedbackDialog.findViewById (R.id.time);
        edit_feedback.setHint ("请给我一些宝贵的建议吧……");
        send.setText ("发送");
        dialog_feedback.setContentView (feedbackDialog, new android.view.ViewGroup.LayoutParams ((int) ((double) Tools.getWidth(this) * 0.9D), DeviceUtil.dp2px(NotifyActivityAdd.this, 50)));
        dialog_feedback.setCanceledOnTouchOutside (false);
        send.setOnClickListener (new View.OnClickListener () {
                                     public void onClick (View v) {
                                         if (!edit_feedback.getText ().toString ().trim ().equals ("")) {
                                             if (!NetworkUtil.isNetworkAvailable(NotifyActivityAdd.this)) {
                                                 dialog_feedback.dismiss ();
                                                 IntentUtil.intentToWifi(NotifyActivityAdd.this);
                                             } else {
                                                 dialog_feedback.dismiss ();
                                                 SendUtil.sendMail(edit_feedback.getText().toString());
                                                 new Handler ().postDelayed (new Runnable () {
                                                     @Override
                                                     public void run () {
                                                         ToastUtil.toast (NotifyActivityAdd.this, "感谢你的反馈！");
                                                     }
                                                 }, 1000);

                                             }
                                         } else {
                                             ToastUtil.toast(NotifyActivityAdd.this, "请输入点内容吧^_^");
                                         }
                                     }
                                 }

        );
        dialog_feedback.show ();
        new Handler ().postDelayed (new Runnable () {

            @Override
            public void run () {
                DeviceUtil.keyboardShow (edit_feedback);
            }
        }, 1000);
        DeviceUtil.keyboardShow (edit_feedback);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        if (serviceOn) {
            stopService (intent);
            serviceOn = false;
        }
    }
}

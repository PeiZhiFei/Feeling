package com.feifei.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.feifei.lifetools.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 裴智飞
 * @date 2014-7-15
 * @date 下午1:35:09
 * @file DialogListView.java
 * @content 分三种模式的Dialog
 */
public class DialogView extends Dialog {
    private final Context context;
    private String title = "";
    private String button = "";
    private String left = "";
    private String right = "";
    private boolean noButton = false;
    private boolean oneButton = false;
    private boolean twoButton = false;
    public boolean gravity = false;
    public boolean wrapContent = true;
    private DialogViewListener dialogListener = null;

    private LinearLayout progressbar;
    private ListView listview;
    private DialogViewAdapter adapter;
    private final ArrayList<String> list;

    /**
     * @param context
     * @param title
     * @param gravity
     * @param list
     * @param dialogListener
     * @content noButton模式，自定义是否居中，传list的监听器
     */
    public DialogView(Context context, String title, boolean gravity,
                      ArrayList<String> list, DialogViewListener dialogListener,
                      boolean wrapContent) {
        super(context, R.style.dialog_white_style);
        this.context = context;
        this.title = title;
        this.gravity = gravity;
        this.list = list;
        this.dialogListener = dialogListener;
        this.wrapContent = wrapContent;
        oneButton = false;
        noButton = true;
        twoButton = false;

    }

    /**
     * @param context
     * @param title
     * @param button
     * @param gravity
     * @param list
     * @param dialogListener
     * @param wrapContent
     * @content oneButton模式，自定义是否居中，传监听器
     */
    public DialogView(Context context, String title, String button,
                      boolean gravity, ArrayList<String> list,
                      DialogViewListener dialogListener, boolean wrapContent) {
        super(context, R.style.dialog_white_style);
        this.context = context;
        this.title = title;
        this.button = button;
        this.gravity = gravity;
        this.list = list;
        this.dialogListener = dialogListener;
        this.wrapContent = wrapContent;
        oneButton = true;
        noButton = false;
        twoButton = false;
    }

    /**
     * @param context
     * @param title
     * @param left
     * @param right
     * @param gravity
     * @param list
     * @param dialogListener
     * @content twoButton模式，自定义是否居中，传list的监听器
     */
    public DialogView(Context context, String title, String left, String right,
                      boolean gravity, ArrayList<String> list,
                      DialogViewListener dialogListener) {
        super(context, R.style.dialog_white_style);
        this.context = context;
        this.title = title;
        this.left = left;
        this.right = right;
        this.gravity = gravity;
        this.list = list;
        this.dialogListener = dialogListener;
        oneButton = false;
        noButton = false;
        twoButton = true;
        wrapContent = true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.yuntu_dialog_layout, null);
        TextView titleText = (TextView) layout.findViewById(R.id.yuntu_dialog_layout_title);
        TextView oneButtonText = (TextView) layout.findViewById(R.id.yuntu_dialog_layout_onebutton);
        View deliver = layout.findViewById(R.id.yuntu_dialog_layout_deliver);
        LinearLayout twoButtonLayout = (LinearLayout) layout
                .findViewById(R.id.yuntu_dialog_layout_twobutton);
        TextView leftButton = (TextView) layout.findViewById(R.id.yuntu_dialog_layout_leftbutton);
        TextView rightButton = (TextView) layout.findViewById(R.id.yuntu_dialog_layout_rightbutton);
        titleText.setText(title);

        if (noButton) {
            oneButtonText.setVisibility(View.GONE);
            deliver.setVisibility(View.GONE);
        }
        if (oneButton) {
            oneButtonText.setText(button);
        }
        if (twoButton) {
            oneButtonText.setVisibility(View.GONE);
            deliver.setVisibility(View.GONE);
            twoButtonLayout.setVisibility(View.VISIBLE);
            leftButton.setText(left);
            rightButton.setText(right);
        }
        progressbar = (LinearLayout) layout.findViewById(R.id.yuntu_dialog_layout_wait);
        listview = (ListView) layout.findViewById(R.id.yuntu_dialog_layout_listview);
        int height;
        if (wrapContent) {
            height = LinearLayout.LayoutParams.WRAP_CONTENT;
        } else {
            height = (int) (DeviceUtil.getHeight(context) * 0.5);
        }
        listview.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height));
        adapter = new DialogViewAdapter(context, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (noButton) {
                    dismiss();
                }
                if (dialogListener != null) {
                    dialogListener.onListItemClick(position, list.get(position));
                }
            }
        });

        this.setCanceledOnTouchOutside(true);
        this.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                dismiss();
            }
        });

        oneButtonText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
                if (dialogListener != null) {
                    dialogListener.onButtonClick();
                }

            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
                if (dialogListener != null) {
                    dialogListener.onLeftClick();
                }
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
                if (dialogListener != null) {
                    dialogListener.onRightClick();
                }
            }
        });

        this.setContentView(layout);
        WindowManager.LayoutParams windowParams = getWindow().getAttributes();
        if (DeviceUtil.isPad(context) && DeviceUtil.isLandscape(context)) {
            windowParams.width = (int) (DeviceUtil.getWidth(context) * 0.35);
        } else {
            windowParams.width = (int) (DeviceUtil.getWidth(context) * 0.8);
        }
        windowParams.height = LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(windowParams);
    }

    public void postShow() {
        progressbar.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
    }

    class DialogViewAdapter extends BaseAdapter {
        private final Context context;
        private List<String> list = new ArrayList<String>();

        public DialogViewAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.yuntu_dialog_listitem,
                        null);
                holder = new Holder();
                holder.blend_dialog_list_item_textview = (TextView) convertView
                        .findViewById(R.id.yuntu_dialog_listitem_textview);
                if (gravity) {
                    holder.blend_dialog_list_item_textview
                            .setGravity(Gravity.CENTER);
                }
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.blend_dialog_list_item_textview.setText(list.get(position));
            return convertView;
        }

        class Holder {
            TextView blend_dialog_list_item_textview;
        }

    }

    public interface DialogViewListener {

        /**
         * @notice 右边确定按钮监听器
         */
        public void onRightClick();

        /**
         * @notice 左边取消按钮监听器
         */
        public void onLeftClick();

        /**
         * @param position
         * @param string
         * @notice item每一项的点击事件
         */
        public void onListItemClick(int position, String string);

        /**
         * @notice 中间一个大按钮的监听器
         */
        public void onButtonClick();
    }
}

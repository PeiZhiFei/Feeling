package library.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import library.other.OnButtonClick;
import library.widget.PromptDialog;
import library.widget.blurdialog.BlurDialog;
import my.library.R;

public class D {

    /**
     * 含有确定取消按钮的dialog，以后写一个单独的类提供各种set方法定制 todo
     *
     * @param context           上下文
     * @param title             标题
     * @param content           内容
     * @param single            是否只要一个按钮
     * @param center            内容文字居中
     * @param can               是否可以取消
     * @param confirm           确定按钮文字(右边)
     * @param cancel            取消按钮文字(左边)
     * @param onConfirmListener 确定按钮监听器
     * @param onCancelListener  取消按钮监听器
     */
    public static void db(Context context, String title, String content, boolean single, boolean center, boolean can, String confirm, String cancel, final OnButtonClick onConfirmListener, final OnButtonClick onCancelListener) {
        final PromptDialog dialog = new PromptDialog(context);
        dialog.setTitle(title).setContent(content).setType(single ? 1 : 2).setConfirmButton(confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onConfirmListener != null) {
                    onConfirmListener.buttonClick();
                }
            }
        }).setCancelButton(cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onCancelListener != null) {
                    onCancelListener.buttonClick();
                }
            }
        }).setCancelable(can);
        if (center) {
            dialog.setCenter();
        }
        dialog.show();

        /*
        final Dialog d = new Dialog(context, R.style.dialog_loading_style);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_prompt, null);
        d.setContentView(view, new ViewGroup.LayoutParams((int) (Tools.getWidth(context) * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView titleTV = (TextView) view.findViewById(R.id.prompt_title);
        TextView contentTV = (TextView) view.findViewById(R.id.prompt_content);
        View btnDivider = view.findViewById(R.id.prompt_vertical_divider);
        Button cancelBtn = (Button) view.findViewById(R.id.prompt_cancel);
        Button confirmBtn = (Button) view.findViewById(R.id.prompt_confirm);
        titleTV.setText(title);
        contentTV.setText(content);
        d.setCancelable(can);
        if (single) {
            btnDivider.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
            confirmBtn.setBackgroundResource(R.drawable.bt_bottom);
            confirmBtn.setText(confirm);
        } else {
            btnDivider.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
            confirmBtn.setBackgroundResource(R.drawable.bt_right);
            cancelBtn.setBackgroundResource(R.drawable.bt_left);
            confirmBtn.setText(confirm);
            cancelBtn.setText(cancel);
        }
        if (content.length() > 12) {
            contentTV.setGravity(Gravity.LEFT);
        } else if (center) {
            contentTV.setGravity(Gravity.CENTER);
        } else {
            contentTV.setGravity(Gravity.LEFT);
        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmListener != null) {
                    onConfirmListener.buttonClick();
                }
                d.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelListener != null) {
                    onCancelListener.buttonClick();
                }
                d.dismiss();
            }
        });
        d.show();
        */
    }


    public static void d(Context context, String title, boolean single, int confirm, int cancel, final OnButtonClick onConfirmListener) {
        d(context, title, single, confirm, cancel, onConfirmListener, null, true, true);
    }

    //不能被取消的dialog
    public static void dnc(Context context, String title, boolean single, int confirm, int cancel, final OnButtonClick onConfirmListener) {
        d(context, title, single, confirm, cancel, onConfirmListener, null, false, true);
    }

    public static void d(Context context, String title, boolean single, int confirm, int cancel, final OnButtonClick onConfirmListener, final OnButtonClick onCancelListener, boolean can, boolean hasTitle) {
        BlurDialog fragment
                = BlurDialog.newInstance(title, single, confirm, cancel, can, hasTitle, -1, -1);
        fragment.OnConfirmListener(onConfirmListener);
        fragment.OnCancelListener(onCancelListener);
        fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "blur_sample");
    }

    //取消按钮的文字颜色
    public static void d(Context context, String title, boolean single, int confirm, int cancel, final OnButtonClick onConfirmListener, final OnButtonClick onCancelListener, boolean can, boolean hasTitle, int cancelColor) {
        BlurDialog fragment
                = BlurDialog.newInstance(title, single, confirm, cancel, can, hasTitle, cancelColor, -1);
        fragment.OnConfirmListener(onConfirmListener);
        fragment.OnCancelListener(onCancelListener);
        fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "blur_sample");
    }

    //取消按钮的文字颜色
    public static void d(Context context, String title, boolean single, int confirm, int cancel, final OnButtonClick onConfirmListener, final OnButtonClick onCancelListener, boolean can, boolean hasTitle, int cancelColor, int confirmColor) {
        BlurDialog fragment
                = BlurDialog.newInstance(title, single, confirm, cancel, can, hasTitle, cancelColor, confirmColor);
        fragment.OnConfirmListener(onConfirmListener);
        fragment.OnCancelListener(onCancelListener);
        fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "blur_sample");
    }


    public interface OnItemListener {
        void onItemClick(int pos);
    }


    /**
     * 显示listview的dialog
     * dl(context, content, Arrays.asList(new String[] { "复制" } ),new OnItemListener()
     *
     * @param context
     * @param title
     * @param data
     * @param onItemListener
     * @param can
     */
    public static void dl(Context context, String title, List<String> data, final OnItemListener onItemListener, boolean can) {
        final Dialog dialog = new Dialog(context, R.style.dialog_white_style);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_listview, null);

        TextView titleTv = (TextView) view.findViewById(R.id.dialog_title);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }

        ListView listView = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.dialog_listview, R.id.title);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_expandable_list_item_1);
        for (String s : data) {
            adapter.add(s);
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemListener != null) {
                    onItemListener.onItemClick(position);
                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(
                view,
                new ViewGroup.LayoutParams((int) (DensityUtil.getWidthInPx((Activity) context) * 0.8),
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setCancelable(can);
        dialog.show();
    }

    /**
     * 自定义view的dialog
     *
     * @param context
     * @param view
     * @param params
     * @param can
     */
    public static void dv(Context context, View view, ViewGroup.LayoutParams params, boolean can) {
        final Dialog dialog = new Dialog(context, R.style.dialog_white_style);
        if (params == null) {
            dialog.setContentView(view, new ViewGroup.LayoutParams((int) (DensityUtil.getWidthInPx((Activity) context) * 0.8),
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            dialog.setContentView(view, params);
        }
        dialog.setCancelable(can);
        dialog.show();
    }

}

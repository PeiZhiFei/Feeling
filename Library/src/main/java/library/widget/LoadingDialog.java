package library.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import my.library.R;

/**
 * 含有省略号动画的dialog
 */
public class LoadingDialog extends Dialog {

    //弱引用，防止崩溃
    private WeakReference<Context> context;
    private TextView text;
    private DotsTextView dot;
    private boolean open = false;

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.dialog_white_style2);
        this.context = new WeakReference<Context>(context);
        init(msg);

    }

    public LoadingDialog(Context context, int resId) {
        super(context, R.style.dialog_white_style2);
        this.context = new WeakReference<Context>(context);
        try {
            init(context.getResources().getString(resId));
        } catch (NotFoundException e) {
            init("加载中");
            e.printStackTrace();
        }
    }

    public LoadingDialog(Context context) {
        this(context, "加载中");
    }

    private void init(String message) {
        if (context.get() != null) {
            setContentView(R.layout.dialog_dots);
            text = (TextView) findViewById(R.id.title);
            dot = (DotsTextView) findViewById(R.id.dot);
            if (!TextUtils.isEmpty(message)) {
                text.setText(message);
            }
            setCanceledOnTouchOutside(false);
//            setCancelable(false);
        }
    }

    public void setContent(String message) {
        if (context.get() != null) {
            if (TextUtils.isEmpty(message)) {
                text.setText("加载中");
            } else {
                text.setText(message);
            }
        }
    }

    public void setContent(int message) {
        if (context.get() != null) {
            try {
                setContent(context.get().getResources().getString(message));
            } catch (NotFoundException e) {
                setContent("加载中");
                e.printStackTrace();
            }
        }
    }

    public void setMessage(String message) {
        setContent(message);
    }

    //activity在才显示
    @Override
    public void show() {
        if (!open && context.get() != null && !((Activity) context.get()).isFinishing()) {
            dot.show();
            super.show();
            open = true;
        }
    }

    @Override
    public void dismiss() {
        if (open && context.get() != null && !((Activity) context.get()).isFinishing()) {
            if (dot.isPlaying()) {
                dot.hideAndStop();
            }
            super.dismiss();
            open = false;
        }
    }
}

package feifei.material.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;

import java.lang.ref.WeakReference;

import feifei.material.R;
import library.util.S;
import library.widget.NewtonCradleLoading;

public class LoadingDialogFour extends Dialog {

    //弱引用，防止崩溃
    private WeakReference<Context> context;
    private NewtonCradleLoading newtonCradleLoading;
    private boolean open = false;


    public LoadingDialogFour(Context context, String msg) {
        super(context, R.style.dialog_loading_style);
        this.context = new WeakReference<Context>(context);
        init(msg);

    }

    public LoadingDialogFour(Context context, int resId) {
        super(context,  R.style.dialog_loading_style);
        this.context = new WeakReference<Context>(context);
        try {
            init(context.getResources().getString(resId));
        } catch (Resources.NotFoundException e) {
            init("正在加载");
            e.printStackTrace();
        }
    }

    public LoadingDialogFour(Context context) {
        this(context, "正在加载");
    }


    private void init(String message) {
        if (context.get() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
            setContentView(R.layout.dialog_four);
            newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
            if (!TextUtils.isEmpty(message) && message.length() == 4) {
                newtonCradleLoading.setText(message);
            } else {
                newtonCradleLoading.setText("正在加载");
            }
            setCanceledOnTouchOutside(false);
            /* 彻底锁死dialog
            setCancelable(false);
            setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            */
        }
    }

    public void setContent(String message) {
        if (context.get() != null) {
            if (!TextUtils.isEmpty(message) && message.length() == 4) {
                newtonCradleLoading.setText(message);
            } else {
                newtonCradleLoading.setText("正在加载");
            }
        }
    }

    public void setContent(int message) {
        if (context.get() != null) {
            try {
                setContent(context.get().getResources().getString(message));
            } catch (Resources.NotFoundException e) {
                setContent("加载中");
                e.printStackTrace();
            }
        }
    }


    //activity在才显示
    @Override
    public void show() {
        if (!open && context.get() != null && !((Activity) context.get()).isFinishing()) {
            if (S.readBoolear(context.get(), "2", false)) {
                newtonCradleLoading.start();
            }
            super.show();
            open = true;
        }
    }

    @Override
    public void dismiss() {
        if (open && context.get() != null && !((Activity) context.get()).isFinishing()) {
            if (newtonCradleLoading.isStart()) {
                newtonCradleLoading.stop();
            }
            super.dismiss();
            open = false;
        }
    }
}

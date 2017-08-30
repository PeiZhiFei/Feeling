package feifei.material.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import feifei.material.R;
import library.util.AnimUtil;
import library.util.TS;
import library.widget.swipeback.SwipeBackActivity;

public class BaseActivity extends SwipeBackActivity {
    protected Activity mActivity;
    private LoadingDialogFour dialogFour;
    protected View statuebar = null;


    @Override
    protected void onCreate(@Nullable Bundle onSaveInstance) {
        super.onCreate(onSaveInstance);
        mActivity = this;
        dialogFour = new LoadingDialogFour(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setSwipeBackEnable(false);
        }
    }


    public void tint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            try {
                statuebar = findViewById(R.id.decorder);
                //不能用R.color的形式
                statuebar.setBackgroundColor(getResources().getColor(getTintColor()));
            } catch (Exception e) {

            }
            int resId = getResources().getIdentifier("status_bar_height",
                    "dimen", "android");
            int height = 0;
            if (resId > 0) {
                height = getResources().getDimensionPixelSize(resId);
            }
            if (height > 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        height);
                statuebar.setLayoutParams(lp);
            } else {
                statuebar.setVisibility(View.GONE);
            }
        }
    }

    protected int getTintColor() {
        return R.color.colorPrimary;
    }

    // TODO: 2016/1/23 0023 这里需要一个颜色过度动画
    protected void setTintColor(int color) {
        if (statuebar != null) {
/*            ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(statuebar,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    0xff529dff, 0xffffffff);
            backgroundColorAnimator.setDuration(300);
            backgroundColorAnimator.start();*/
            statuebar.setBackgroundColor(getResources().getColor(color));
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.animBack(this);
    }

    public void d(String s) {
//        dialogLbsq.setContent(s);
//        dialogLbsq.show();
        dialogFour.setContent("数据统计");
        dialogFour.show();
    }

    public void d() {
//        dialogLbsq.show();
        dialogFour.setContent("数据统计");
        dialogFour.show();
    }

    public void dd() {
//        dialogLbsq.dismiss();
        dialogFour.dismiss();
    }

    public void t(CharSequence message) {
        TS.t(mActivity, message);
    }

    public void s(CharSequence message) {
        TS.s(mActivity, message);
    }

    public void s(CharSequence message, int lev) {
        TS.s(mActivity, message, lev);
    }

}

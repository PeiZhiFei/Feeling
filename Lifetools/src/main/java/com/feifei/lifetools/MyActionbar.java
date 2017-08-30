/**
 *
 */
package com.feifei.lifetools;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifei.view.SearchText;
import com.feifei.util.AnimUtil;
import com.feifei.util.ConfigUtil;
import com.feifei.util.ToastCustom;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <include
 * android:id="@+id/activity_actionbar"
 * android:layout_width="match_parent"
 * layout="@layout/actionbar_default" />
 */
public class MyActionbar extends FragmentActivity implements OnClickListener {
    /**
     * 一个全局的context
     */
    protected Activity activity;
    /**
     * actionbar的标题
     */
    protected TextView actionbar_title;
    /**
     * 左边的标题返回和logo，右边的3个按钮区域
     */
    protected LinearLayout actionbar_left_layout, actionbar_right_layout;
    /**
     * actionbar右边可以点击的3个区域，也是onclick里面的3个id
     */
    protected LinearLayout actionbar_first_layout, actionbar_second_layout,
            actionbar_third_layout;
    /**
     * 这个只负责图片的显示
     */
    protected ImageView actionbar_first_image, actionbar_second_image,
            actionbar_third_image, actionbar_logo_image;
    /**
     * 整个actionbar
     */
    protected View actionbar;
    protected SearchText mClearEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
    }

    /**
     * @notice 初始化Actionbar
     * @notice 找的不是actionbar的id，而是include的id
     */
    protected void initActionbar() {
        actionbar = findViewById(R.id.activity_actionbar);
        actionbar_title = (TextView) actionbar
                .findViewById(R.id.actionbar_default_title);
        actionbar_first_layout = (LinearLayout) actionbar
                .findViewById(R.id.actionbar_default_first_layout);
        actionbar_second_layout = (LinearLayout) actionbar
                .findViewById(R.id.actionbar_default_second_layout);
        actionbar_third_layout = (LinearLayout) actionbar
                .findViewById(R.id.actionbar_default_third_layout);
        actionbar_left_layout = (LinearLayout) actionbar
                .findViewById(R.id.actionbar_left_layout);
        actionbar_right_layout = (LinearLayout) actionbar
                .findViewById(R.id.actionbar_right_layout);
        actionbar_second_image = (ImageView) actionbar
                .findViewById(R.id.actionbar_default_second);
        actionbar_first_image = (ImageView) actionbar
                .findViewById(R.id.actionbar_default_first);
        actionbar_third_image = (ImageView) actionbar
                .findViewById(R.id.actionbar_default_third);
        actionbar_logo_image = (ImageView) actionbar
                .findViewById(R.id.actionbar_default_icon);
        mClearEditText = (SearchText) actionbar.findViewById(R.id.filter_edit);
        actionbar_first_layout.setOnClickListener(this);
        actionbar_third_layout.setOnClickListener(this);
        actionbar_second_layout.setOnClickListener(this);
        actionbar_left_layout.setOnClickListener(this);
    }

    /**
     * @param title：标题
     * @param logoDrawable：主程序的logo图片
     * @notice 该样式只有标题，logo和返回箭头，右边隐藏
     */
    protected void actionbarStyle0(String title, int logoDrawable) {
        initActionbar();
        setTitle(title);
        setGone(actionbar_right_layout);
        setLogoImage(logoDrawable);
        actionbarDownAnim();
    }

    /**
     * @param title：标题
     * @param logoDrawable：主程序的logo图片
     * @param thirdImage：第三区域(overflow)的图片
     * @notice 该样式右边只有第三区域显示
     */
    protected void actionbarStyle1(String title, int logoDrawable,
                                   int thirdImage) {
        initActionbar();
        setTitle(title);
        setGone(actionbar_first_layout);
        setGone(actionbar_second_layout);
        setLogoImage(logoDrawable);
        setThirdImage(thirdImage);
        actionbarDownAnim();
    }

    /**
     * @param title：标题
     * @param logoDrawable：主程序的logo图片
     * @param secondImage：第二区域的图片
     * @param thirdImage：第三区域(overflow)的图片
     * @notice 该样式右边只有第二区域和第三区域显示
     */
    protected void actionbarStyle2(String title, int logoDrawable,
                                   int secondImage, int thirdImage) {
        initActionbar();
        setTitle(title);
        setGone(actionbar_first_layout);
        setLogoImage(logoDrawable);
        setSecondImage(secondImage);
        setThirdImage(thirdImage);
        actionbarDownAnim();
    }

    /**
     * @param title：标题
     * @param logoDrawable：主程序的logo图片
     * @param firstImage：第一区域的图片
     * @param secondImage：第二区域的图片
     * @param thirdImage：第三区域(overflow)的图片
     * @notice 该样式需要全部的自定义
     */
    protected void actionbarStyle3(String title, int logoDrawable,
                                   int firstImage, int secondImage, int thirdImage) {
        initActionbar();
        setTitle(title);
        setLogoImage(logoDrawable);
        setFirstImage(firstImage);
        setSecondImage(secondImage);
        setThirdImage(thirdImage);
        actionbarDownAnim();
    }

    /**
     * @param activityFirst：boolean值，是否是第一次
     * @param key：要写入sharePreference的键
     * @param toast：文字内容
     * @notice actionbar第一次出现的动画，配合一个运动的toast
     */
    protected void actionbarFirstAnim(boolean activityFirst, String key,
                                      String toast) {
        if (activityFirst) {
            ConfigUtil.writeBoolean(this, key, false);
            actionbarToastAnim(toast);
        }
    }

    /**
     * @param drawable
     * @notice 更改第二区域图标，默认为加号
     */
    protected void setSecondImage(int drawable) {
        actionbar_second_image.setImageResource(drawable);
    }

    /**
     * @param drawable
     * @notice 更改第一区域图标，默认为搜索
     */
    protected void setFirstImage(int drawable) {
        actionbar_first_image.setImageResource(drawable);
    }

    /**
     * @param drawable
     * @notice 更改第三区域图标，默认为更多
     */
    protected void setThirdImage(int drawable) {
        actionbar_third_image.setImageResource(drawable);
    }

    /**
     * @param drawable
     * @notice 更改logo图标，默认为toast图标
     */
    protected void setLogoImage(int drawable) {
        actionbar_logo_image.setImageResource(drawable);
    }

    /**
     * @param text
     * @notice 更改标题文字
     */
    protected void setTitle(final String text) {
        runOnUiThread(new Runnable() {

            public void run() {
                actionbar_title.setText(text);

            }
        });

    }

    /**
     * @param view
     * @notice 设置控件可见，这里要传layout
     */
    protected void setVisible(View view) {
        if (view.getVisibility() == View.GONE
                || view.getVisibility() == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param view
     * @notice 设置控件消失，这里要传layout
     */
    protected void setGone(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * @param view
     * @notice 设置控件不可见，这里要传layout
     */
    protected void setInvisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @notice actionbar向上消失的动画
     */
    protected void actionbarUpAnim() {
        AnimUtil.animActionbarUp(actionbar);
    }

    /**
     * @notice actionbar向下出现的动画
     */
    protected void actionbarDownAnim() {
        AnimUtil.animActionbarDown(actionbar);
    }

    /**
     * @param toast：文字内容
     * @notice actionbar配合toast的动画
     */
    protected void actionbarToastAnim(String toast) {
        Timer timer0 = new Timer();
        timer0.schedule(new Task0(), 2000);
        Timer timer1 = new Timer();
        timer1.schedule(new Task1(toast), 2000);
        Timer timer2 = new Timer();
        timer2.schedule(new Task2(), 4000);
    }

    public void onClick(View arg0) {
        // 库里面不能用switch-case，同R.id类似
        int id = arg0.getId();
        if (id == R.id.actionbar_left_layout) {
            AnimUtil.animBackFinish(activity);
        }
    }

    class Task0 extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    actionbarUpAnim();
                    setInvisible(actionbar);
                }
            });

        }
    }

    class Task1 extends TimerTask {
        String toast;

        public Task1(String toast) {
            this.toast = toast;
        }

        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    ToastCustom.toast(activity, toast, Gravity.TOP,false);
                }
            });

        }
    }

    class Task2 extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    setVisible(actionbar);
                    actionbarDownAnim();
                }
            });

        }
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

}

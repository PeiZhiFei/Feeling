package com.feifei.util;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.feifei.lifetools.R;


/**
 * @author 裴智飞
 * @date 2014-7-28
 * @date 下午8:54:13
 * @file AnimUtil.java
 * @content 动画相关类
 */
public class AnimUtil {
    // activity现在提供2种过渡动画，alpha和slide
    private static final int ALPHA = 1;
    private static final int SLIDE = 2;

    // actionbar的动画时间
    private static final int duration_actionbar = 800;
    // 控件出现消失的短时间
    private static final int duration_short = 100;

    /**
     * @param activity
     * @notice activity向下跳转
     */
    public static void animTo(Activity activity) {
        animActivity(activity, ALPHA, false);
    }

    /**
     * @param activity
     * @notice activity向下跳转并结束
     */
    public static void animToFinish(Activity activity) {
        activityFinish(activity);
        animActivity(activity, ALPHA, false);
    }

    /**
     * @param activity
     * @notice activity向上返回
     */
    public static void animBack(Activity activity) {
        animActivity(activity, ALPHA, true);
    }

    /**
     * @param activity
     * @notice activity向上返回并结束
     */
    public static void animBackFinish(Activity activity) {
        activityFinish(activity);
        animActivity(activity, ALPHA, true);
    }

    /**
     * @param activity
     * @notice 切换动画为左右滑动的activity向下跳转
     */
    public static void animToSlide(Activity activity) {
        animActivity(activity, SLIDE, false);
    }

    /**
     * @param activity
     * @notice 切换动画为左右滑动的activity向下跳转并finish
     */
    public static void animToSlideFinish(Activity activity) {
        activityFinish(activity);
        animActivity(activity, SLIDE, false);
    }

    /**
     * @param activity
     * @notice 切换动画为左右滑动的activity向上返回
     */
    public static void animBackSlide(Activity activity) {
        animActivity(activity, SLIDE, true);
    }

    /**
     * @param activity
     * @notice 切换动画为左右滑动的activity向上返回并finish
     */
    public static void animBackSlideFinish(Activity activity) {
        activityFinish(activity);
        animActivity(activity, SLIDE, true);
    }

    /**
     * @param view
     * @notice 从无到有的动画，0.8秒
     */
    public static void animShow(View view) {
        view.startAnimation(getaAlphaAnimation(0, 1));
    }

    /**
     * @param view
     * @notice 从无到有的短动画，0.1秒
     */
    public static void animShowShort(View view) {
        view.startAnimation(getaAlphaAnimation(0, 1, duration_short));
    }

    /**
     * @param view
     * @notice 消失的动画，0.8秒
     */
    public static void animGone(View view) {
        view.startAnimation(getaAlphaAnimation(1, 0));
    }

    /**
     * @param view
     * @notice 从右边滑动到中心
     */
    public static void animRightToCenter(View view) {
        // true为共享加速器，统一为set设置即可，不用单项设置
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getTranslateAnimation(0.8f, 0, 0, 0));
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    /**
     * @param view
     * @notice 从右边滑动到左边
     */
    public static void animRightToLeft(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getTranslateAnimation(0, -0.8f, 0, 0));
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    /**
     * @param view
     * @notice 从左边滑动到中心
     */
    public static void animLeftToCenter(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getTranslateAnimation(-0.8f, 0, 0, 0));
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    /**
     * @param view
     * @notice 从上边滑动到下边
     */
    public static void animUpToDown(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.addAnimation(AnimUtil.getTranslateAnimation(0, 0, -0.5f,
                0, 1000));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    /**
     * @param view
     * @notice 从下边滑动到上边
     */
    public static void animDownToUp(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.addAnimation(AnimUtil.getTranslateAnimation(0, 0, 0.5f, 0,
                1000));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    /**
     * @param view
     * @notice 文本晃动的动画
     */
    public static void animShakeText(View view) {
        Animation shakeAnim = new TranslateAnimation(0, 10, 0, 0);
        shakeAnim.setInterpolator(new CycleInterpolator(5));
        shakeAnim.setDuration(500);
        view.startAnimation(shakeAnim);
    }

    /**
     * @param view
     * @notice 二维码扫描动画
     */
    public static void animScan(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE,
                0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.95f);
        translateAnimation.setDuration(1500);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setInterpolator(new LinearInterpolator());
        view.setAnimation(translateAnimation);
    }

    /**
     * @param view
     * @notice actionbar向上消失移动的动画
     */
    public static void animActionbarUp(View view) {
        AnimationSet actionbarUp = new AnimationSet(true);
        actionbarUp.addAnimation(getTranslateAnimation(0, 0, 0, -1));
        actionbarUp.addAnimation(getaAlphaAnimation(1, 0));
        view.startAnimation(actionbarUp);
    }

    /**
     * @param view
     * @notice actionbar向下出现的动画
     */
    public static void animActionbarDown(View view) {
        AnimationSet actionbarDown = new AnimationSet(true);
        actionbarDown.addAnimation(getTranslateAnimation(0, 0, -1, 0));
        actionbarDown.addAnimation(getaAlphaAnimation(0, 1));
        view.startAnimation(actionbarDown);
    }

    /**
     * @param view
     * @notice 控件下落振动反弹的动画
     */
    public static void animFallDown(View view) {
        AnimationSet fallDown = new AnimationSet(true);
        fallDown.addAnimation(getaAlphaAnimation(0, 1));
        fallDown.addAnimation(getTranslateAnimation(0, 0, -1.0f, 0, 800));
        fallDown.setInterpolator(new BounceInterpolator());
        view.startAnimation(fallDown);
    }

    /**
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return TranslateAnimation
     * @notice 获取位移动画，默认8秒
     */
    public static TranslateAnimation getTranslateAnimation(float x1, float x2,
                                                           float y1, float y2) {
        // 中心坐标，以及xml中相对的写法
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, x1, Animation.RELATIVE_TO_SELF, x2,
                Animation.RELATIVE_TO_SELF, y1, Animation.RELATIVE_TO_SELF, y2);
        translateAnimation.setDuration(duration_actionbar);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    /**
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @param duration：自定义时间
     * @return TranslateAnimation
     * @notice 获取位移动画，自定义时间
     */
    public static TranslateAnimation getTranslateAnimation(float x1, float x2,
                                                           float y1, float y2, int duration) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, x1, Animation.RELATIVE_TO_SELF, x2,
                Animation.RELATIVE_TO_SELF, y1, Animation.RELATIVE_TO_SELF, y2);
        translateAnimation.setDuration(duration);
        translateAnimation.setFillAfter(true);
        return translateAnimation;

    }

    /**
     * @param from
     * @param to
     * @return AlphaAnimation
     * @notice 获取渐变动画，默认8秒
     */
    public static AlphaAnimation getaAlphaAnimation(int from, int to) {
        AlphaAnimation alphaAnimationShow = new AlphaAnimation(from, to);
        alphaAnimationShow.setDuration(duration_actionbar);
        return alphaAnimationShow;
    }

    /**
     * @param from
     * @param to
     * @param duration：自定义时间
     * @return AlphaAnimation
     * @notice 获取渐变动画，自定义时间
     */
    public static AlphaAnimation getaAlphaAnimation(int from, int to,
                                                    int duration) {
        AlphaAnimation alphaAnimationShow = new AlphaAnimation(from, to);
        alphaAnimationShow.setDuration(duration);
        return alphaAnimationShow;
    }

    /**
     * @param from
     * @param to
     * @param duration：自定义时间
     * @return RotateAnimation
     * @notice 获取旋转动画
     */
    public static RotateAnimation getRotateAnimation(float from, float to,
                                                     int duration) {
        RotateAnimation rotateAnimation = new RotateAnimation(from, to,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        return rotateAnimation;
    }

    /**
     * @return RotateAnimation
     * @notice 获取dialog的旋转动画
     */
    public static RotateAnimation getDialogRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setRepeatCount(-1);// 设置重复次数
        rotateAnimation.setStartOffset(-1);// 执行前的等待时间
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        return rotateAnimation;
    }

    /**
     * @return RotateAnimation
     * @notice 有缓冲的dialog旋转动画，适合围绕着一个中心
     */
    public static RotateAnimation getDialogRotateAnimation2() {
        RotateAnimation mAnim = new RotateAnimation(0, 360, Animation.RESTART,
                0.5f, Animation.RESTART, 0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
        return mAnim;
    }

    /**
     * @param start：起始比例
     * @param end：结束比例
     * @param duration：时间
     * @return
     * @notice 获取缩放动画
     */
    public static ScaleAnimation getScaleAnimation(float start, float end,
                                                   int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(start, end, start,
                end, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    /**
     * @return
     * @notice 获取点击后变大并消失的缩放动画
     */
    public static Animation getScaleGoneAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
        animationSet.setDuration(500);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setFillAfter(true);
        return animationSet;
    }

    /**
     * @param view
     * @notice 设置gone
     */
    public static void setGone(View view) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * @param view
     * @notice 设置invisible
     */
    public static void setInvisible(View view) {
        if (view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @param view
     * @notice 设置visible
     */
    public static void setVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private static void animActivity(Activity activity, int animType,
                                     boolean back) {
        int INRIGHT = R.anim.activity_in_from_right;
        int OUTLEFT = R.anim.activity_out_to_left;
        int INLEFT = R.anim.activity_in_from_left;
        int OUTRIGHT = R.anim.activity_out_to_right;

        int INFADE = R.anim.fade_in;
        int HOLD = R.anim.fade_hold;
        int OUTFADE = R.anim.fade_out;

        if (animType == ALPHA) {
            if (!back) {
                // 向下跳的alpha
                activity.overridePendingTransition(INFADE, HOLD);
            } else {
                // 返回的alpha
                activity.overridePendingTransition(INFADE, OUTFADE);
            }

        } else {
            if (animType == SLIDE) {
                if (!back) {
                    // 向下跳的slide
                    activity.overridePendingTransition(INRIGHT, OUTLEFT);
                } else {
                    // 返回的slide
                    activity.overridePendingTransition(INLEFT, OUTRIGHT);
                }
            }
        }
    }

    private static void activityFinish(Activity activity) {
        activity.finish();
    }

    public static void animScaleAlpha(View view) {
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(getaAlphaAnimation(0, 1, 500));
        set.addAnimation(getScaleAnimation(0.5f, 1, 500));
        view.startAnimation(set);
    }

    /**
     * @param imageView
     * @param mPicture1
     * @param mPicture2
     * @param mPicture3
     * @notice 三张图片的幻灯片动画
     */
    public static void animImageScreen(final ImageView imageView,
                                       final Drawable mPicture1, final Drawable mPicture2,
                                       final Drawable mPicture3) {
        final Animation mFadeIn = AnimUtil.getaAlphaAnimation(0, 1, 1000);
        final Animation mFadeOut = AnimUtil.getaAlphaAnimation(1, 0, 1000);
        final AnimationSet anim = new AnimationSet(true);
        anim.addAnimation(mFadeOut);
        final ScaleAnimation helperAnimation = new ScaleAnimation(1.1f, 1.0f,
                1.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        helperAnimation.setFillAfter(false);
        helperAnimation.setInterpolator(new DecelerateInterpolator());
        helperAnimation.setDuration(1500);
        anim.addAnimation(helperAnimation);

        final ScaleAnimation mFadeInScale = new ScaleAnimation(1f, 1.1f, 1f,
                1.1f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mFadeInScale.setFillAfter(false);
        mFadeInScale.setInterpolator(new DecelerateInterpolator());
        mFadeInScale.setDuration(6000);
        imageView.setImageDrawable(mPicture1);
        imageView.startAnimation(mFadeIn);
        mFadeIn.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(anim);
            }
        });
        anim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (imageView.getDrawable().equals(mPicture1)) {
                    imageView.setImageDrawable(mPicture2);
                } else if (imageView.getDrawable().equals(mPicture2)) {
                    imageView.setImageDrawable(mPicture3);
                } else if (imageView.getDrawable().equals(mPicture3)) {
                    imageView.setImageDrawable(mPicture1);
                }
                imageView.startAnimation(mFadeIn);
            }
        });

    }

    /**
     * @param view
     * @notice 图片的重力感应旋转
     */
    public static void animImageRotate(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(-1);// 设置重复次数
        rotateAnimation.setInterpolator(new OvershootInterpolator());
        view.startAnimation(rotateAnimation);
    }

    public static void animListShow(ViewGroup viewGroup) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        viewGroup.setLayoutAnimation(controller);
    }

    /**
     * @param view
     * @notice PullDoor文字的
     */
    public static void animDoorTextHint(View view) {
        Animation anim = getaAlphaAnimation(0, 1, 1500);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

    /**
     * @param layout
     * @notice 设置LinearLayout的出现和消失动画
     */
    public static void animLayoutAlpha(LinearLayout layout) {
        LayoutTransition transition = new LayoutTransition();
        ObjectAnimator appearing = ObjectAnimator.ofFloat(layout, "alpha", 0f,
                1f);
        ObjectAnimator disappearing = ObjectAnimator.ofFloat(layout, "alpha",
                1f, 0f);
        appearing.setDuration(300);
        disappearing.setDuration(300);
        transition.setAnimator(LayoutTransition.APPEARING, appearing);
        transition.setAnimator(LayoutTransition.DISAPPEARING, disappearing);
    }
}

package library.util;

import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
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

import my.library.R;

public class AnimUtil {
    private static final int ALPHA              = 1;
    private static final int SLIDE              = 2;
    private static final int duration_actionbar = 500;
    private static final int duration_short     = 100;

    public static void animTo(Activity activity) {
        animActivity(activity, ALPHA, false);
    }

    public static void animToFinish(Activity activity) {
        activityFinish(activity);
        animActivity(activity, ALPHA, false);
    }

    public static void animBack(Activity activity) {
        animActivity(activity, ALPHA, true);
    }

    public static void animBackFinish(Activity activity) {
        activityFinish(activity);
        animActivity(activity, ALPHA, true);
    }

    public static void animToSlide(Activity activity) {
        animActivity(activity, SLIDE, false);
    }

    public static void animToSlideFinish(Activity activity) {
        activityFinish(activity);
        animActivity(activity, SLIDE, false);
    }

    public static void animBackSlide(Activity activity) {
        animActivity(activity, SLIDE, true);
    }

    public static void animBackSlideFinish(Activity activity) {
        activityFinish(activity);
        animActivity(activity, SLIDE, true);
    }

    public static void animShow(View view) {
        view.startAnimation(getaAlphaAnimation(0.3f, 1));
    }

    public static void animShowShort(View view) {
        view.startAnimation(getaAlphaAnimation(0, 1, duration_short));
    }

    public static void animGone(View view) {
        view.startAnimation(getaAlphaAnimation(1, 0));
    }

    public static void animGone2(View view) {
        view.startAnimation(getaAlphaAnimation(1, 0, 300));
    }

    public static void animRightToCenter(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getTranslateAnimation(0.8f, 0, 0, 0));
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    public static void animRightToLeft(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getTranslateAnimation(0, -0.8f, 0, 0));
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    public static void animRightToLeft2(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getTranslateAnimation(0, -0.8f, 0, 0, 300));
        animationSet.addAnimation(getaAlphaAnimation(0, 1, 300));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    public static void animLeftToCenter(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getTranslateAnimation(-0.8f, 0, 0, 0));
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    public static void animUpToDown(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.addAnimation(AnimUtil.getTranslateAnimation(0, 0, -0.5f,
                0, 500));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    public static void animUpToDown2(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getaAlphaAnimation(0, 1, 300));
        animationSet.addAnimation(AnimUtil.getTranslateAnimation(0, 0, -0.5f,
                0, 300));
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
    }

    public static void animDownToUp(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getaAlphaAnimation(0, 1));
        animationSet.addAnimation(AnimUtil.getTranslateAnimation(0, 0, 0.5f, 0,
                500));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    public static void animDownToUp2(View view) {
        AnimationSet animationSet = new AnimationSet(true);
//        animationSet.addAnimation(getaAlphaAnimation(0.99f, 1));
        animationSet.addAnimation(AnimUtil.getTranslateAnimation(0, 0, 1f, 0,
                500));
        animationSet.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animationSet);
    }

    public static void animDownToUp3(View view) {
        AnimationSet animationSet = new AnimationSet(true);
//        animationSet.addAnimation(getaAlphaAnimation(0.99f, 1));
        animationSet.addAnimation(AnimUtil.getTranslateAnimation(0, 0, 0, -0.5f,
                300));
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
    }

    public static void animShakeText(View view) {
        Animation shakeAnim = new TranslateAnimation(0, 10, 0, 0);
        shakeAnim.setInterpolator(new CycleInterpolator(5));
        shakeAnim.setDuration(500);
        view.startAnimation(shakeAnim);
    }

    public static void animScan(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
                0.95f);
        translateAnimation.setDuration(1500);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setInterpolator(new LinearInterpolator());
        view.setAnimation(translateAnimation);
    }

    public static void animActionbarUp(View view) {
        AnimationSet actionbarUp = new AnimationSet(true);
        actionbarUp.addAnimation(getTranslateAnimation(0, 0, 0, -1));
        actionbarUp.addAnimation(getaAlphaAnimation(1, 0));
        view.startAnimation(actionbarUp);
    }

    public static void animActionbarDown(View view) {
        AnimationSet actionbarDown = new AnimationSet(true);
        actionbarDown.addAnimation(getTranslateAnimation(0, 0, -1, 0));
        actionbarDown.addAnimation(getaAlphaAnimation(0, 1));
        view.startAnimation(actionbarDown);
    }

    public static void animFallDown(View view) {
        AnimationSet fallDown = new AnimationSet(true);
        fallDown.addAnimation(getaAlphaAnimation(0, 1));
        fallDown.addAnimation(getTranslateAnimation(0, 0, -1.0f, 0, 600));
        fallDown.setInterpolator(new BounceInterpolator());
        view.startAnimation(fallDown);
    }

    public static void animFallDown2(View view) {
        AnimationSet fallDown = new AnimationSet(true);
//        fallDown.addAnimation(getaAlphaAnimation(0, 1));
        fallDown.addAnimation(getTranslateAnimation(0, 0, -1.0f, 0, 200));
        fallDown.setInterpolator(new BounceInterpolator());
        view.startAnimation(fallDown);
    }

    public static TranslateAnimation getTranslateAnimation(float x1, float x2,
                                                           float y1, float y2) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, x1, Animation.RELATIVE_TO_SELF, x2,
                Animation.RELATIVE_TO_SELF, y1, Animation.RELATIVE_TO_SELF, y2);
        translateAnimation.setDuration(duration_actionbar);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    public static TranslateAnimation getTranslateAnimation(float x1, float x2,
                                                           float y1, float y2, int duration) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, x1, Animation.RELATIVE_TO_SELF, x2,
                Animation.RELATIVE_TO_SELF, y1, Animation.RELATIVE_TO_SELF, y2);
        translateAnimation.setDuration(duration);
        translateAnimation.setFillAfter(true);
        return translateAnimation;

    }

    public static AlphaAnimation getaAlphaAnimation(float from, int to) {
        AlphaAnimation alphaAnimationShow = new AlphaAnimation(from, to);
        alphaAnimationShow.setDuration(duration_actionbar);
        return alphaAnimationShow;
    }

    public static AlphaAnimation getaAlphaAnimation(int from, int to,
                                                    int duration) {
        AlphaAnimation alphaAnimationShow = new AlphaAnimation(from, to);
        alphaAnimationShow.setDuration(duration);
        return alphaAnimationShow;
    }

    public static RotateAnimation getRotateAnimation(float from, float to,
                                                     int duration) {
        RotateAnimation rotateAnimation = new RotateAnimation(from, to,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        return rotateAnimation;
    }

    public static RotateAnimation getDialogRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setStartOffset(-1);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        return rotateAnimation;
    }

    public static RotateAnimation getDialogRotateAnimation2() {
        RotateAnimation mAnim = new RotateAnimation(0, 360, Animation.RESTART,
                0.5f, Animation.RESTART, 0.5f);
        mAnim.setDuration(1500);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.INFINITE);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
        mAnim.setInterpolator(new LinearInterpolator());
        return mAnim;
    }

    public static ScaleAnimation getScaleAnimation(float start, float end,
                                                   int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(start, end, start,
                end, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    public static Animation getScaleGoneAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
        animationSet.setDuration(500);
        animationSet.setInterpolator(new DecelerateInterpolator());
        // animationSet.setFillAfter(true);
        return animationSet;
    }

    public static Animation getScaleGoneAnimation3() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 2.6f, 1.0f, 2.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
        animationSet.setDuration(200);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setFillAfter(true);
        return animationSet;
    }

    public static Animation getScaleGoneAnimation2() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
        animationSet.setDuration(500);
        animationSet.setInterpolator(new DecelerateInterpolator());
        // animationSet.setFillAfter(true);
        return animationSet;
    }

    public static void setGone(View view) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    public static void setInvisible(View view) {
        if (view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void setVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void animToGuide(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_in, R.anim.hold);
    }

    public static void animToGuideFinish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.hold);
    }

    private static void animActivity(Activity activity, int animType,
                                     boolean back) {
        int INRIGHT = R.anim.slide_in_from_right;
        int OUTLEFT = R.anim.slide_out_to_left;
        int INLEFT = R.anim.slide_in_from_left;
        int OUTRIGHT = R.anim.slide_out_to_right;

//        int INRIGHT = R.anim.zoom_enter;
//        int OUTLEFT = R.anim.zoom_exit;
//        int INLEFT = R.anim.unzoom_in;
//        int OUTRIGHT = R.anim.unzoom_out;

//        int INFADE = R.anim.fade_in;
//        int HOLD = R.anim.fade_hold;
//        int OUTFADE = R.anim.fade_out;

        if (animType == ALPHA) {
            if (!back) {
                //原来有黑
//                activity.overridePendingTransition(INRIGHT, OUTLEFT);
                // TODO: 2015/11/18 0018 改成了新的进入
                activity.overridePendingTransition(INRIGHT, R.anim.hold);
                // TODO: 2015/11/19 0019 下滑
//                activity.overridePendingTransition(R.anim.toast_in_bottom, R.anim.hold);
            } else {
//                activity.overridePendingTransition(INLEFT, OUTRIGHT);
                // TODO: 2015/11/18 0018
                activity.overridePendingTransition(R.anim.hold, OUTRIGHT);
                // TODO: 2015/11/19 0019 下滑
//                activity.overridePendingTransition(R.anim.hold, R.anim.toast_out_bottom);
            }

        } else {
            if (animType == SLIDE) {
                if (!back) {
                    activity.overridePendingTransition(INRIGHT, OUTLEFT);
                } else {
                    activity.overridePendingTransition(INLEFT, OUTRIGHT);
                }
            }
        }
    }

    // TODO: 2016/1/12 0012 重构
    public static void animToTop(Activity activity) {
        activity.overridePendingTransition(R.anim.toast_in_top, R.anim.hold);
    }

    public static void animBackTop(Activity activity) {
        activity.overridePendingTransition(R.anim.hold, R.anim.toast_out_top);
    }

    public static void animToTopFinish(Activity activity) {
        activityFinish(activity);
        activity.overridePendingTransition(R.anim.toast_in_top, R.anim.hold);
    }

    public static void animBackTopFinish(Activity activity) {
        activityFinish(activity);
        activity.overridePendingTransition(R.anim.hold, R.anim.toast_out_top);
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

    public static void animScaleAlpha2(View view) {
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(getaAlphaAnimation(0, 1, 500));
        set.addAnimation(getScaleAnimation(0.6f, 1, 500));
        view.startAnimation(set);
    }

    public static void animScaleAlpha3(final View view) {
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(getaAlphaAnimation(0, 1, 300));
        set.addAnimation(getScaleAnimation(0.1f, 1, 300));
        view.startAnimation(set);
        set.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void animScaleAlphaBun(View view) {
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(getaAlphaAnimation(0, 1, 500));
        set.addAnimation(getScaleAnimation(0.6f, 1, 600));
        set.setDuration(600);
        set.setInterpolator(new BounceInterpolator());
        view.startAnimation(set);
    }

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
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(anim);
            }
        });
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
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

    public static void animImageRotate(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(-1);
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
        animation.setDuration(300);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        viewGroup.setLayoutAnimation(controller);
    }

    public static void animListShow2(ViewGroup viewGroup) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(100);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        viewGroup.setLayoutAnimation(controller);
    }

    //列表从右向左进
    public static void animListShowRightIn(ViewGroup viewGroup) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(300);
        set.addAnimation(animation);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        viewGroup.setLayoutAnimation(controller);
    }

    public static void animDoorTextHint(View view) {
        Animation anim = getaAlphaAnimation(0, 1, 1500);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

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


    //左右摇晃的动画
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ObjectAnimator nope(View view) {
        // int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);
        int delta = 10;
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.30f, delta),
                Keyframe.ofFloat(.50f, -delta),
                Keyframe.ofFloat(.70f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(300);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void shakeOne(View view) {
        tada(view, 1f).start();
    }


    //晃动缩放的动画
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
                View.SCALE_X, Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
                View.SCALE_Y, Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(
                View.ROTATION, Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0));

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX,
                pvhScaleY, pvhRotate).setDuration(1000);
    }

    public static void shake(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator object = nope(view);
//            object.setRepeatCount(3);
            object.start();
        }
    }

    //类似YoYo的动画
    public static ScaleAnimation getScaleBounceAnimation(float start, float end,
                                                         int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(start, end, start,
                end, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        return scaleAnimation;
    }

    /**
     * 位移动画
     */
    public static void animateTo(Activity activity, View aniView, int[] startLoc, int[] endLoc,
                                 int durationMills, AnimationListener listener) {
        ViewGroup rootView = (ViewGroup) activity.getWindow()
                .getDecorView();
        LinearLayout animLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);

        int x = startLoc[0];
        int y = startLoc[1];
        LinearLayout.LayoutParams aniViewLP = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        aniViewLP.leftMargin = x;
        aniViewLP.topMargin = y;
        aniView.setLayoutParams(aniViewLP);
        animLayout.addView(aniView);

        int endX = endLoc[0] - startLoc[0];// 动画位移的X坐标
        int endY = endLoc[1] - startLoc[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(durationMills);// 动画的执行时间

        if (listener != null) {
            set.setAnimationListener(listener);
        }

        aniView.startAnimation(set);
    }

}

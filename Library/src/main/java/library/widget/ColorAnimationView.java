package library.widget;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 裴智飞
 * @date 2014-8-8
 * @date 下午4:26:30
 * @file ColorAnimationView.java
 * @content 引导页滑动颜色变化
 * viewpager.setAdapter(adapter);
 * // viewpager的颜色过渡效果
 * ColorAnimationView colorAnimationView = (ColorAnimationView) findViewById(R.id.ColorAnimationView);
 * colorAnimationView.setmViewPager(viewpager, adapter.getCount());
 * colorAnimationView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 * public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
 * }
 * public void onPageSelected(int position) {
 * setCurDot(position);
 * viewHandler.removeCallbacks(runnable);
 * viewHandler.postDelayed(runnable, autoChangeTime);
 * }
 * public void onPageScrollStateChanged(int state) {
 * }
 * });
 */
public class ColorAnimationView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private static final int COLOR4 = 0xff87CEEB;
    private static final int COLOR2 = 0xff27c8a7;
    private static final int COLOR1 = 0xff40E0D0;
    private static final int COLOR3 = 0xff00FFFF;
    private static final int DURATION = 1000;
    ValueAnimator colorAnim = null;

    private final PageChangeListener mPageChangeListener;

    ViewPager.OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 首先，你必须在设置Viewpager的adapter之后在调用这个方法； 第二点，setmViewPager(ViewPager mViewPager,Object obj, int count, int... colors)
     * 第二个参数是一个实现了 ColorAnimationView.OnPageChangeListener接口的Object,用来实现回调，第三个参数是viewpager的孩子数量 第四个参数int...
     * colors，你需要设置的颜色变化值~~ 如何你传人空，那么触发默认设置的颜色动画
     */
    public void setmViewPager(ViewPager mViewPager, int count, int... colors) {
        // this.mViewPager = mViewPager;
        if (mViewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mPageChangeListener.setViewPagerChildCount(count);

        mViewPager.setOnPageChangeListener(mPageChangeListener);
        if (colors.length == 0) {
            createDefaultAnimation();
        } else {
            createAnimation(colors);
        }

    }

    public ColorAnimationView(Context context) {
        this(context, null, 0);

    }

    public ColorAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPageChangeListener = new PageChangeListener();
    }

    private void seek(long seekTime) {
        if (colorAnim == null) {
            createDefaultAnimation();
        }
        colorAnim.setCurrentPlayTime(seekTime);
    }

    private void createAnimation(int... colors) {
        if (colorAnim == null) {
            colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", colors);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.setDuration(DURATION);
            colorAnim.addUpdateListener(this);
        }
    }

    private void createDefaultAnimation() {
        // colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", WHITE, RED,
        // BLUE, GREEN, WHITE);
        colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", COLOR1, COLOR2, COLOR3, COLOR4);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(DURATION);
        colorAnim.addUpdateListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
        // long playtime = colorAnim.getCurrentPlayTime();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        private int viewPagerChildCount;

        public void setViewPagerChildCount(int viewPagerChildCount) {
            this.viewPagerChildCount = viewPagerChildCount;
        }

        public int getViewPagerChildCount() {
            return viewPagerChildCount;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            int count = getViewPagerChildCount() - 1;
            if (count != 0) {
                float length = (position + positionOffset) / count;
                int progress = (int) (length * DURATION);
                ColorAnimationView.this.seek(progress);
            }
            // call the method by default
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

        }

        @Override
        public void onPageSelected(int position) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }
}

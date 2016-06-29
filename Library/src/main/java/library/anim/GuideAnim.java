package library.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @date 2014-11-27
 * @date 上午9:49:54
 * @file GuideAnim.java
 * @content viewpager的动画效果
 * viewpager.setPageTransformer(true, new GuideAnim());
 */
public class GuideAnim implements ViewPager.PageTransformer {
    private static float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) {
            view.setAlpha(0);
        } else if (position <= 0) {
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) {
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            view.setAlpha(0);
        }
    }

}

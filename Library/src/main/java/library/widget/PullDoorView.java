package library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * @author 谁谁谁
 * @date 2014-8-4
 * @date 下午10:18:03
 * @file PullDoorView.java
 * @content 配合Framelayout使用，在最上层
 */
public class PullDoorView extends RelativeLayout {

    private final Context mContext;

    private Scroller mScroller;

    private int mScreenHeigh = 0;

    private int mLastDownY = 0;

    private int mCurryY;

    private int mDelY;

    private boolean mCloseFlag = false;

    private ImageView mImgView;
//    private SimpleDraweeView mImgView;

    public PullDoorView(Context context) {
        super(context);
        mContext = context;
        setupView();
    }

    public PullDoorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setupView();
    }

    private void setupView() {
        // 这个Interpolator你可以设置别的 我这里选择的是有弹跳效果的Interpolator
        Interpolator polator = new BounceInterpolator();
        mScroller = new Scroller(mContext, polator);

        // 获取屏幕分辨率
        WindowManager wm = (WindowManager) (mContext
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenHeigh = dm.heightPixels;
        setGravity(Gravity.CENTER);
        // 这里你一定要设置成透明背景,不然会影响你看到底层布局
        setBackgroundColor(Color.parseColor("#00000000"));
//        mImgView = new ImageView(mContext);
        mImgView = new ImageView(mContext);
        LayoutParams params2 = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params2.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImgView.setLayoutParams(params2);
//        GenericDraweeHierarchyBuilder builder =
//                new GenericDraweeHierarchyBuilder(getResources());
//        GenericDraweeHierarchy hierarchy = builder
//                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
//                .build();
//        mImgView.setHierarchy(hierarchy);
//        mImgView.setImageURI(UriUtil.getHttpUri(SPHelper.getFlashUrl()));

        mImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 填充整个屏幕
//        // TODO 这里设置背景图片，改成attr属性吧，不用attr，直接set
//        mImgView.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(FileUtils.getSDPath(mContext) + "douxing/splashimg.jpg", mScreenHeigh, dm.widthPixels));
        addView(mImgView);
    }

    // 设置推动门背景
    public void setBgImage(int id) {
        mImgView.setImageResource(id);
    }

    // 设置推动门背景
    public void setBgImage(Drawable drawable) {
        mImgView.setImageDrawable(drawable);
    }

    public void setBgImage(Bitmap bitmap) {
        mImgView.setImageBitmap(bitmap);
    }

    // 推动门的动画
    public void startBounceAnim(int startY, int dy, int duration) {
        mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastDownY = (int) event.getY();
                System.err.println("ACTION_DOWN=" + mLastDownY);
                return true;
            case MotionEvent.ACTION_MOVE:
                mCurryY = (int) event.getY();
                System.err.println("ACTION_MOVE=" + mCurryY);
                mDelY = mCurryY - mLastDownY;
                // 只准上滑有效
                if (mDelY < 0) {
                    scrollTo(0, -mDelY);
                }
                System.err.println("-------------  " + mDelY);

                break;
            case MotionEvent.ACTION_UP:
                mCurryY = (int) event.getY();
                mDelY = mCurryY - mLastDownY;
                if (mDelY < 0) {

                    if (Math.abs(mDelY) > mScreenHeigh / 2) {

                        // 向上滑动超过半个屏幕高的时候 开启向上消失动画
                        startBounceAnim(this.getScrollY(), mScreenHeigh, 450);
                        mCloseFlag = true;
                    } else {
                        // 向上滑动未超过半个屏幕高的时候 开启向下弹动动画
                        startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);

                    }
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            Log.i("scroller", "getCurrX()= " + mScroller.getCurrX()
                    + "     getCurrY()=" + mScroller.getCurrY()
                    + "  getFinalY() =  " + mScroller.getFinalY());
            // 不要忘记更新界面
            postInvalidate();
        } else {
            if (mCloseFlag) {
                this.setVisibility(View.GONE);
            }
        }
    }

}

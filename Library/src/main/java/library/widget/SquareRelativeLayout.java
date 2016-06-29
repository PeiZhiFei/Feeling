package library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class SquareRelativeLayout extends RelativeLayout {
    private static final String TAG = "SquareRelativeLayout";

    public SquareRelativeLayout(Context context) {
        super(context);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (newWidth < getSuggestedMinimumWidth()) {
            newWidth = getSuggestedMinimumWidth();
        }

        int newHeight = newWidth;

        if (newHeight < getSuggestedMinimumHeight()) {
            newHeight = getSuggestedMinimumHeight();
        }
        setMeasuredDimension(newWidth, newHeight);
    }

    /**
     * 当前所有子控件垂直居中，如果有需要再加新特性
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();

        if (childCount == 0) {
            return;
        }

        super.onLayout(changed, l, t, r, b);

        int mViewGroupHeight = getMeasuredHeight();

        int minTop = getChildAt(0).getTop();
        int maxBottom = getChildAt(0).getBottom();
        if (childCount > 1) {
            for (int i = 1; i < childCount; i++) {

                View childView = getChildAt(i);

                if (childView.getTop() < minTop) {
                    minTop = childView.getTop();
                }

                if (childView.getBottom() > maxBottom) {
                    maxBottom = childView.getBottom();
                }
            }
        }

        int center = mViewGroupHeight >> 1;
        int childCenter = (maxBottom - minTop) >> 1;
        int offset = center - childCenter;

        Log.i(TAG, "offest = " + offset);

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();
            int newLeft = childView.getLeft();
            int newRight = newLeft + width;
            int newTop = childView.getTop();
            newTop += offset;
            int newBottom = newTop + height;

            childView.layout(newLeft, newTop, newRight, newBottom);
        }

    }
}

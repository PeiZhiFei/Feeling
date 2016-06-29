package library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

//滑动速度减缓的scrollview
public class SlowScrollView extends ScrollView {
    public SlowScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SlowScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlowScrollView(Context context) {
        super(context);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 2);
    }
}

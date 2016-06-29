package library.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//recycleview的分隔线,分割线深浅居然在这里
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;
    private boolean isPaddingFirst = true;
    private Paint mPaintBg;
    private Paint mPaintFg;


    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        initPaint();
    }

    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight, boolean isPaddingFirst) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        this.isPaddingFirst = isPaddingFirst;
        initPaint();
    }

    private void initPaint() {
        mPaintBg = new Paint();
//        mPaintFg.setColor(Color.rgb(0xf6, 0xf6, 0xf9));
//        mPaintBg.setColor(Color.rgb(0xe5, 0xe5, 0xe5));
//        mPaintBg.setStyle(Paint.Style.FILL_AND_STROKE);

        mPaintFg = new Paint();
        mPaintFg.setColor(Color.argb(60, 0xf6, 0xf6, 0xf9));
//        mPaintFg.setColor(Color.argb(60,0xe1, 0xe1, 0xe1));
        mPaintFg.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) != 0 || isPaddingFirst) {
            outRect.top = mVerticalSpaceHeight;
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = isPaddingFirst ? 0 : 1, size = parent.getChildCount(); i < size; i++) {
            View child = parent.getChildAt(i);
            float l = child.getLeft();
            float r = child.getRight();
            float t = child.getTop();
            c.drawRect(l, t - mVerticalSpaceHeight, r, t, mPaintFg);
//            c.drawRect(l, t - mVerticalSpaceHeight, r, t, mPaintBg);
//            c.drawRect(l, t - mVerticalSpaceHeight + 1, r, t - 1, mPaintFg);
        }
    }
}
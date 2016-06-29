package library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import java.util.List;

/*
    List<String> list = new ArrayList<String>();
	list.add(mainActivity.getShop().getDaliver_description());
	marquee.setData(list);
	marquee.setFocusable(false);
	marquee.startScroll();
 */
public class MarqueeText extends TextView implements Runnable {
    private int currentScrollX;
    private boolean isStop = false;
    private int textWidth;
    private List<String> mList;
    private final int REPEAT = 100;
    private int repeatCount = 0;
    private int currentNews = 0;

    public MarqueeText(Context context) {
        super(context);
        init();
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        setClickable(true);
        setSingleLine(true);
        setEllipsize(TruncateAt.MARQUEE);
        setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    public void setData(List<String> mList) {
        if (mList == null || mList.size() == 0) {
            return;
        }
        this.mList = mList;
        currentNews = 0;
        String n = mList.get(currentNews);
        setText(n);
        setTag(n);
        startScroll();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        MeasureTextWidth();
    }

    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if (screenState == SCREEN_STATE_ON) {
            startScroll();
        } else {
            stopScroll();
        }
    }

    private void MeasureTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }

    @Override
    public void run() {
        if (textWidth < 1) {
            // title null api error.
            if (mList != null && mList.size() > 0) {
                nextNews();
            } else {
                return;
            }
        }
        currentScrollX += 2;
        scrollTo(currentScrollX, 0);
        if (isStop) {
            return;
        }
        // Log.e("", "currentScrollX:"+currentScrollX);
        // not full a line
        // if(textWidth <= getWidth()){
        if (getScrollX() >= textWidth) {
            currentScrollX = -getWidth();
            scrollTo(currentScrollX, 0);
            if (repeatCount >= REPEAT) {
                // reach max times
                nextNews();
                // return;
            } else {
                repeatCount++;
            }

            // return;
        }
        // }else{
        // if(getScrollX() >= textWidth-getWidth()+50)
        // currentScrollX = -getWidth();
        // scrollTo(currentScrollX, 0);
        // }

        postDelayed(this, 50);
    }

    private void nextNews() {
        repeatCount = 0;
        currentNews++;
        currentNews = currentNews % mList.size();// cycle index
        String n = mList.get(currentNews);
        setText(n);
        setTag(n);
        // startScroll();
    }

    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);
    }

    public void stopScroll() {
        isStop = true;
    }
}

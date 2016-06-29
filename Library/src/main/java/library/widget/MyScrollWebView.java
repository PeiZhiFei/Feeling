package library.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/*
    定义了一个OnScrollChangedCallback接口，其中dx和dy分别是滚动的时候，x和y方向上的偏移量
 */
public class MyScrollWebView extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public MyScrollWebView(final Context context) {
        super(context);
    }

    public MyScrollWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollWebView(final Context context, final AttributeSet attrs,
                           final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }
}
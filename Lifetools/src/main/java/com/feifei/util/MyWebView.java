package com.feifei.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.feifei.lifetools.R;

/**
 * @author 裴智飞
 * @date 2014-7-25
 * @date 上午9:51:51
 * @file WebViewActivity.java
 * @content 通用的WebView，用intent传参
 * @content 需要在mainfest里面添加，dialog还需要style
 * IntentUtil.startWebview(this,WebviewActivity.class,"http://42.96.172.134/YuToo/us.html",true);
 * <activity
 * android:name="com.yuntu.util.view.WebViewActivity"
 * android:theme="@style/dialog_white" />
 */

public class MyWebView extends Activity {
    protected WebView mWebView;

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        int dp5 = DeviceUtil.dp2px(this, 5);
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        layout.setLayoutParams(params);
        layout.setBackgroundResource(R.drawable.white_round);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(dp5, dp5, dp5, dp5);
        mWebView.setLayoutParams(params2);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        mWebView.requestFocus();
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        layout.addView(mWebView);
        setContentView(layout);
        if (getIntent().getBooleanExtra("dialog", false)) {
            WindowManager.LayoutParams windowParams = getWindow().getAttributes();
            windowParams.width = (int) (DeviceUtil.getWidth(this) * 0.9);
            windowParams.height = (int) (DeviceUtil.getHeight(this) * 0.7);
            windowParams.gravity = Gravity.CENTER;
            getWindow().setAttributes(windowParams);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(getIntent().getStringExtra("url"));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
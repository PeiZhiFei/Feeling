package library.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;
import my.library.R;


/*
        精简版的
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(this);
        menuView.setCancelButtonTitle("cancel");// before add items
        menuView.addItems("Item1", "Item2", "Item3", "Item4");
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.setItemClickListener(new MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                Toast.makeText(MainActivity.this, (itemPosition + 1) + " click", 0).show();
            }
        });
        menuView.showMenu();
 */

public class ActionSheet extends Dialog implements OnClickListener {
    /* 控件的id */
    private static final int CANCEL_BUTTON_ID = 100;
    private static final int BG_VIEW_ID = 10;
    private static final int TRANSLATE_DURATION = 300;
    private static final int ALPHA_DURATION = 300;

    private Context mContext;
    private Attributes mAttrs;
    private MenuItemClickListener mListener;
    private View mView;
    private LinearLayout mPanel;
    private View mBg;
    private List<String> items;
    private String cancelTitle = "";
    private boolean mCancelableOnTouchOutside;
    private boolean mDismissed = true;
    private boolean isCancel = true;

    public ActionSheet(Context context) {
        super(context, android.R.style.Theme_Light_NoTitleBar);// 全屏
//        super(context, R.style.dialog_tint);// 全屏
        this.mContext = context;
        initViews();
        getWindow().setGravity(Gravity.BOTTOM);
        Drawable drawable = new ColorDrawable();
        drawable.setAlpha(0);// 去除黑色背景
        getWindow().setBackgroundDrawable(drawable);
    }

    public void initViews() {
        /* 隐藏软键盘 */
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = ((Activity) mContext).getCurrentFocus();
            if (focusView != null)
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }
        mAttrs = readAttribute();// 获取主题属性
        mView = createView();
        mBg.startAnimation(createAlphaInAnimation());
        mPanel.startAnimation(createTranslationInAnimation());
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
        an.setDuration(TRANSLATE_DURATION);
        return an;
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(ALPHA_DURATION);
        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type, 0, type, 1);
        an.setDuration(TRANSLATE_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(ALPHA_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private View createView() {
        FrameLayout parent = new FrameLayout(mContext);
        FrameLayout.LayoutParams parentParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        parentParams.gravity = Gravity.BOTTOM;
        parent.setLayoutParams(parentParams);
        mBg = new View(mContext);
        mBg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
        mBg.setId(BG_VIEW_ID);
        mBg.setOnClickListener(this);

        mPanel = new LinearLayout(mContext);
        FrameLayout.LayoutParams mPanelParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mPanelParams.gravity = Gravity.BOTTOM;
        mPanel.setLayoutParams(mPanelParams);
        mPanel.setOrientation(LinearLayout.VERTICAL);
        parent.addView(mBg);
        parent.addView(mPanel);
        return parent;
    }

    private void createItems() {
        if (items != null && items.size() > 0)
            for (int i = 0; i < items.size(); i++) {
                Button bt = new Button(mContext);
                bt.setId(CANCEL_BUTTON_ID + i + 1);
                bt.setOnClickListener(this);
                bt.setBackgroundDrawable(getOtherButtonBg(items.toArray(new String[items.size()]), i));
                bt.setText(items.get(i));
                bt.setTextColor(mAttrs.otherButtonTextColor);
                bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
                if (i > 0) {
                    LinearLayout.LayoutParams params = createButtonLayoutParams();
                    params.topMargin = mAttrs.otherButtonSpacing;
                    mPanel.addView(bt, params);
                } else
                    mPanel.addView(bt);
            }
        Button bt = new Button(mContext);
//        bt.getPaint().setFakeBoldText(true);
        bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
        bt.setId(CANCEL_BUTTON_ID);
        bt.setBackgroundDrawable(mAttrs.cancelButtonBackground);
        bt.setText(cancelTitle);
        bt.setTextColor(mAttrs.cancelButtonTextColor);
        bt.setOnClickListener(this);
        LinearLayout.LayoutParams params = createButtonLayoutParams();
        params.topMargin = mAttrs.cancelButtonMarginTop;
        mPanel.addView(bt, params);

        mPanel.setBackgroundDrawable(mAttrs.background);
        mPanel.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding, mAttrs.padding);
    }

    public LinearLayout.LayoutParams createButtonLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        return params;
    }

    //item按钮的颜色
    private Drawable getOtherButtonBg(String[] titles, int i) {
        if (titles.length == 1)
            return mAttrs.otherButtonSingleBackground;
        else if (titles.length == 2)
            switch (i) {
                case 0:
                    return mAttrs.otherButtonTopBackground;
                case 1:
                    return mAttrs.otherButtonBottomBackground;
            }
        else if (titles.length > 2) {
            if (i == 0)
                return mAttrs.otherButtonTopBackground;
            else if (i == (titles.length - 1))
                return mAttrs.otherButtonBottomBackground;
            return mAttrs.otherButtonMiddleBackground;
        }
        return null;
    }

    public void showMenu() {
        if (!mDismissed)
            return;
        show();
        getWindow().setContentView(mView);
        mDismissed = false;
    }

    //dissmiss Menu菜单
    public void dismissMenu() {
        if (mDismissed)
            return;
        onDismiss();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
                mDismissed = true;
            }
        }, TRANSLATE_DURATION);

    }

    //dismiss时的处理
    private void onDismiss() {
        mPanel.startAnimation(createTranslationOutAnimation());
        mBg.startAnimation(createAlphaOutAnimation());
    }

    public ActionSheet setCancelButtonTitle(String title) {
        this.cancelTitle = title;
        return this;
    }

    public ActionSheet setCancelButtonTitle(int strId) {
        return setCancelButtonTitle(mContext.getString(strId));
    }

    //点击外部边缘是否可取消
    public ActionSheet setCancelableOnTouchMenuOutside(boolean cancelable) {
        mCancelableOnTouchOutside = cancelable;
        return this;
    }

    public ActionSheet addItems(String... titles) {
        if (titles == null || titles.length == 0)
            return this;
        items = Arrays.asList(titles);
        createItems();
        return this;
    }

    public ActionSheet addItems(List<String> titles) {
        if (titles == null || titles.size() == 0)
            return this;
        items = titles;
        createItems();
        return this;
    }

    public ActionSheet setItemClickListener(MenuItemClickListener listener) {
        this.mListener = listener;
        return this;
    }


    private Attributes readAttribute() {
        Attributes attrs = new Attributes(mContext);
        attrs.background = mContext.getResources().getDrawable(android.R.color.transparent);
        attrs.cancelButtonBackground = mContext.getResources().getDrawable(R.drawable.bt_single);
        attrs.otherButtonTopBackground = mContext.getResources().getDrawable(R.drawable.bt_top);
        attrs.otherButtonMiddleBackground = mContext.getResources().getDrawable(R.drawable.bt_middle);
        attrs.otherButtonBottomBackground = mContext.getResources().getDrawable(R.drawable.bt_bottom);
        attrs.otherButtonSingleBackground = mContext.getResources().getDrawable(R.drawable.bt_single);
        attrs.cancelButtonTextColor = Color.parseColor("#1E82FF");
        attrs.otherButtonTextColor = Color.parseColor("#1E82FF");
        attrs.padding = dp2px(10);
        attrs.otherButtonSpacing = dp2px(0);
        attrs.cancelButtonMarginTop = dp2px(10);
        attrs.actionSheetTextSize = dp2px(16);
        return attrs;
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == BG_VIEW_ID && !mCancelableOnTouchOutside)
            return;
        dismissMenu();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (v.getId() != CANCEL_BUTTON_ID && v.getId() != BG_VIEW_ID) {
                    if (mListener != null)
                        mListener.onItemClick(v.getId() - CANCEL_BUTTON_ID - 1);
                    isCancel = false;
                }
            }
        }, TRANSLATE_DURATION);

    }

    //自定义属性的控件主题
    private class Attributes {
        private Context mContext;
        private Drawable background;
        private Drawable cancelButtonBackground;
        private Drawable otherButtonTopBackground;
        private Drawable otherButtonMiddleBackground;
        private Drawable otherButtonBottomBackground;
        private Drawable otherButtonSingleBackground;
        private int cancelButtonTextColor;
        private int otherButtonTextColor;
        private int padding;
        private int otherButtonSpacing;
        private int cancelButtonMarginTop;
        private float actionSheetTextSize;

        public Attributes(Context context) {
            mContext = context;
            this.background = new ColorDrawable(Color.TRANSPARENT);
            this.cancelButtonBackground = new ColorDrawable(Color.BLACK);
            ColorDrawable gray = new ColorDrawable(Color.GRAY);
            this.otherButtonTopBackground = gray;
            this.otherButtonMiddleBackground = gray;
            this.otherButtonBottomBackground = gray;
            this.otherButtonSingleBackground = gray;
            this.cancelButtonTextColor = Color.WHITE;
            this.otherButtonTextColor = Color.BLACK;
            this.padding = dp2px(20);
            this.otherButtonSpacing = dp2px(2);
            this.cancelButtonMarginTop = dp2px(10);
            this.actionSheetTextSize = dp2px(16);
        }


//        public Drawable getOtherButtonMiddleBackground() {
//            if (otherButtonMiddleBackground instanceof StateListDrawable) {
//                TypedArray a = mContext.getTheme().obtainStyledAttributes(null, R.styleable.ActionSheet,
//                        R.attr.actionSheetStyle, 0);
//                otherButtonMiddleBackground = a
//                        .getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
//                a.recycle();
//            }
//            return otherButtonMiddleBackground;
//        }

    }

    public static interface MenuItemClickListener {
        void onItemClick(int itemPosition);
    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources()
                .getDisplayMetrics());
    }

}
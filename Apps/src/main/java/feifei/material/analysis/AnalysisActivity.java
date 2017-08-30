package feifei.material.analysis;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import feifei.material.R;
import feifei.material.main.MyConst;
import library.util.AnimUtil;
import library.util.Tools;
import library.widget.PagerSlidingTabStrip;
import library.widget.percent.PercentLinearLayout;

public class AnalysisActivity extends DataActivity {

    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.pager)
    ViewPager pager;

    @Bind(R.id.order_money)
    TextView orderMoney;
    @Bind(R.id.order_count)
    TextView orderCount;
    @Bind(R.id.store_count)
    TextView storeCount;
    @Bind(R.id.all_money)
    TextView allOrderMoney;
    @Bind(R.id.all_count)
    TextView allOrderCount;

    @Bind(R.id.test)
    PercentLinearLayout testLayout;
    private MyPagerAdapter adapter;

    @Override
    protected int getTintColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_analisis);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setSwipeBackEnable(false);
        }
        ButterKnife.bind(this);
        tint();

        adapter = new MyPagerAdapter(getFragmentManager());

        tabs.setTextColor(new ColorStateList(new int[][]{
                new int[]{android.R.attr.state_pressed}, //pressed
                new int[]{android.R.attr.state_selected}, // enabled
                new int[]{} //default
        },
                new int[]{
                        getResources().getColor(R.color.red),
                        getResources().getColor(R.color.red),
                        getResources().getColor(R.color.darkgray2)
                }));
        pager.setCurrentItem(0);
        tabs.setBackgroundColor(getResources().getColor(R.color.white));


        orderCount.setText(todayCount);
        orderMoney.setText(todayMoney);
        storeCount.setText(allStore);
        allOrderCount.setText("订单总数:  " + allCount);
        allOrderMoney.setText("营业总额:  " + allMoney);

        SharedPreferences sp = getSharedPreferences(MyConst.KEY_LOGIN_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        boolean first = sp.getBoolean(MyConst.KEY_FIRST, true);
        if (first) {
            initHotWords();
            editor.putBoolean(MyConst.KEY_FIRST, false).apply();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimUtil.animScaleAlpha(testLayout);
                testLayout.setVisibility(View.VISIBLE);
                AnimUtil.animScaleAlpha(allOrderMoney);
                allOrderMoney.setVisibility(View.VISIBLE);
                AnimUtil.animScaleAlpha(allOrderCount);
                allOrderCount.setVisibility(View.VISIBLE);
            }
        }, 100);

        getData(true);
    }

    @OnClick(R.id.money_layout)
    public void money(final View view) {
        AnimUtil.shake(view);
        if (allOrderCount.getVisibility() == View.VISIBLE) {
            allOrderCount.setVisibility(View.GONE);
        } else {
            allOrderCount.setVisibility(View.VISIBLE);
        }

        if (allOrderMoney.getVisibility() == View.VISIBLE) {
            allOrderMoney.setVisibility(View.GONE);
        } else {
            allOrderMoney.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.search_layout)
    public void search(final View view) {
        startActivity(new Intent(mActivity, SearchActivity.class));
        AnimUtil.animToTop(mActivity);
    }

    @OnClick(R.id.order_layout)
    public void orderList(final View view) {
        startActivity(new Intent(mActivity, AnalysisOrderListActivity.class));
        AnimUtil.animToTop(mActivity);
    }


    @Override
    protected void onResult(int type) {
        switch (type) {
            case 1:
                orderCount.setText(todayCount);
                orderMoney.setText(todayMoney);
                storeCount.setText(allStore);
                Double money = Double.valueOf(allMoney);
                Double count = Double.valueOf(allCount);
                allOrderCount.setText("订单总数:  " + Tools.scale(count / 10000) + "万");
                allOrderMoney.setText("营业总额:  " + Tools.scale(money / 10000) + "万");
                AnimUtil.animShow(orderCount);
                AnimUtil.animShow(orderMoney);
                AnimUtil.animShow(storeCount);
                AnimUtil.animShow(allOrderCount);
                AnimUtil.animShow(allOrderMoney);
                break;
            case 2:
                if (first) {
                    pager.setAdapter(adapter);
                    tabs.setViewPager(pager);
                    first = false;
                } else {
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    boolean first = true;


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"趋势概览", "7日营业额", "7日订单数", "30天营业额", "30天订单数"};
        private int mChildCount = 0;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LineChartFragmentBase.newInstance();
                case 1:
                    return PieChartFragmentMoney.newInstance();
                case 2:
                    return PieChartFragmentCount.newInstance();
                case 3:
                    return HorizontalFragmentMoney.newInstance();
                case 4:
                    return HorizontalFragmentCount.newInstance();
                default:
                    return HorizontalFragmentCount.newInstance();
            }
        }
    }

    private void initHotWords() {
        SharedPreferences sp = getSharedPreferences(MyConst.KEY_HOTWORD_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("32", "益万家超市");
        editor.putString("83", "翡翠清河超市");
        editor.putString("23", "瑞隆超市");
        editor.putString("89", "时尚涮吧");
        editor.putString("92", "瑞鑫超市");
        editor.putString("29", "零步社区");
        editor.putString("100", "快客超市");
        editor.putString("14", "零步社区车库超市");
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        finish();
        AnimUtil.animBackTopFinish(this);
    }

}
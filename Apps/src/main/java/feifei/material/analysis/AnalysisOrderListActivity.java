package feifei.material.analysis;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import butterknife.Bind;
import butterknife.ButterKnife;
import feifei.material.R;
import feifei.material.main.BaseActivity;
import library.util.AnimUtil;
import library.widget.PagerSlidingTabStrip;

public class AnalysisOrderListActivity extends BaseActivity {

    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.pager)
    ViewPager pager;
    MyPagerAdapter adapter;

    @Override
    protected int getTintColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_order_list);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            setSwipeBackEnable(false);
//        }
        ButterKnife.bind(this);
        tint();
        adapter = new MyPagerAdapter(getFragmentManager());
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setTextColor(new ColorStateList(new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_selected},
                new int[]{}
        },
                new int[]{
                        getResources().getColor(R.color.main_green),
                        getResources().getColor(R.color.main_green),
                        getResources().getColor(R.color.darkgray2)
                }));
        tabs.setViewPager(pager);


    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"今天", "本周", "本月"};

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
        public Fragment getItem(int position) {
            return AnalysisOrderFragment.newInstance(position);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        AnimUtil.animBackTopFinish(this);
    }
}

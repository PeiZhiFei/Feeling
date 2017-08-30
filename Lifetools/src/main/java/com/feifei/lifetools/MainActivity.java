package com.feifei.lifetools;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifei.calculator.CalculatorActivity;
import com.feifei.checkpackage.PackageActivity;
import com.feifei.compass.CompassActivity;
import com.feifei.game.GameActivity;
import com.feifei.todo.NotifyActivityAdd;
import com.feifei.util.AnimUtil;
import com.feifei.util.ConfigUtil;
import com.feifei.util.DialogUtil;
import com.feifei.util.IntentUtil;
import com.feifei.util.ToastCustom;
import com.feifei.view.MyGridLayout;
import com.zbar.lib.ScanActivity;

import java.util.ArrayList;
import java.util.List;

//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;

public class MainActivity extends BaseActivity {

    private ImageView imageLogo;
    private MyGridLayout grid;
    ArrayList<String> gridTitle = new ArrayList<String>();
    ArrayList<Integer> gridSrc = new ArrayList<Integer>();

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
        if (mainFirst2) {
            ConfigUtil.writeBoolean(activity, "mainFirst2", false);
            initUpdateDialog();
        }

        for (int i = 0; i < grid.getChildCount(); i++) {
            AnimUtil.animFallDown(grid.getChildAt(i));
        }
    }


    private void initEvents() {
        grid.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                switch (index) {
                    case 0:
                        IntentUtil.intentTo(activity, ScanActivity.class);
                        break;
                    case 1:
                        IntentUtil.intentTo(activity, PackageActivity.class);
                        break;
                    case 3:
                        IntentUtil.intentTo(activity, CalculatorActivity.class);
                        break;
                    case 4:
                        startActivity(new Intent(activity, TorchActivity.class));
                        AnimUtil.animToSlide(activity);
                        break;
                    case 5:
                        IntentUtil.intentTo(activity, CompassActivity.class);
                        break;
                    case 6:
                        IntentUtil.intentTo(activity, GameActivity.class);
                        break;
                    case 7:
                        IntentUtil.intentNoAnim(activity, NotifyActivityAdd.class);
                        break;
                    case 8:
                        IntentUtil.intentTo(activity, SettingActivity.class);
                        break;
                }

            }
        });

    }


    private void initData() {
        gridTitle.add("扫一扫");
        gridTitle.add("查快递");
        gridTitle.add("算工资");
        gridTitle.add("计算器");
        gridTitle.add("手电筒");
        gridTitle.add("指南针");
        gridTitle.add("三国争霸");
        gridTitle.add("TODO");
        gridTitle.add("设置");
        gridSrc.add(R.drawable.circle_scan);
        gridSrc.add(R.drawable.circle_package);
        gridSrc.add(R.drawable.circle_money);
        gridSrc.add(R.drawable.circle_calculator);
        gridSrc.add(R.drawable.circle_torch);
        gridSrc.add(R.drawable.circle_compass);
        gridSrc.add(R.drawable.circle_computer);
        gridSrc.add(R.drawable.circle_note);
        gridSrc.add(R.drawable.circle_setting);
    }

    public List<View> mListViews = new ArrayList<>();

    public class MyPagerAdapter extends PagerAdapter {


        public MyPagerAdapter() {
        }


        @Override
        public int getCount() {
            return mListViews.size();
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }


        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }


        @Override
        public void startUpdate(View arg0) {
        }

    }

    private void initView() {
        grid = new MyGridLayout(this);
        grid.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        grid.setColums(3);
        grid.setMargin(1);
        grid.setBackgroundColor(Color.parseColor("#1e1d1d"));
        initData();
        grid.setGridAdapter(new MyGridLayout.GridAdatper() {
            @Override
            public View getView(int index) {
                View view = getLayoutInflater().inflate(R.layout.gridview_item, null);
                ImageView iv = (ImageView) view.findViewById(R.id.iv);
                TextView tv = (TextView) view.findViewById(R.id.tv);
                iv.setImageResource(gridSrc.get(index));
                tv.setText(gridTitle.get(index));
                AnimUtil.animFallDown(iv);
                return view;
            }

            @Override
            public int getCount() {
                return gridSrc.size();

            }
        });

        mListViews.add(grid);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter());

        imageLogo = (ImageView) findViewById(R.id.image_logo);
        AnimUtil.animRightToCenter(imageLogo);
        AnimUtil.animShow(grid);
    }


    private void initUpdateDialog() {
        final ArrayList<String> list = new ArrayList<String>();

        list.add("1.修复几个用户反馈的问题");
        list.add("2.查快递和记事本加入语音识别");
        list.add("3.更换部分功能模块");
        list.add("4.扫一扫支持扫描图书");
        list.add("5.计算器更换清新界面");
        DialogUtil.dialogStyle1(activity, "生活小助手2.5", "开启！", false, list);
    }


    private final long waitTime = 2000;
    private long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long currentTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((currentTime - touchTime) >= waitTime) {
                ToastCustom.toast(this, "再按一次退出程序");
                touchTime = currentTime;
            } else {
                AnimUtil.animBackFinish(this);
            }
        }
        return true;
    }


}

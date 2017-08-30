package feifei.material.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import feifei.material.R;
import feifei.material.analysis.AnalysisActivity;
import feifei.material.analysis.AnalysisOrderFragment;
import feifei.material.analysis.LineChartFragment7;
import library.util.TS;
import library.util.Tools;


public class MainActivity extends BaseActivity implements
        OnClickListener {

    @Bind(R.id.add)
    ImageView add;

    @Bind(R.id.add_layout)
    RelativeLayout addLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind({R.id.home_text, R.id.friend_text, R.id.message_text, R.id.my_text})
    List<TextView> textViews;
    @Bind({R.id.home_image, R.id.friend_image, R.id.message_image, R.id.my_image})
    List<ImageView> imageViews;
    @Bind({R.id.home_layout, R.id.friend_layout, R.id.message_layout, R.id.my_layout})
    List<LinearLayout> linearLayouts;
    int[] pressed = new int[]{R.drawable.wechat_address_s, R.drawable.wechat_wechat_s, R.drawable.wechat_moment_s, R.drawable.wechat_me_s};
    int[] normal = new int[]{R.drawable.wechat_address_n, R.drawable.wechat_wechat_n, R.drawable.wechat_moment_n, R.drawable.wechat_me_n};

    private LineChartFragment7 fragment1;
    private LineChartFragment7 fragment2;
    private LineChartFragment7 fragment3;
    private AnalysisOrderFragment fragment4;

    Fragment[] fragments = new Fragment[]{fragment1, fragment2, fragment3, fragment4};

    private int current = -1;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private int textcolornormal = Color.BLACK;
    private int textcolor = Color.parseColor("#39ac69");//原来的绿色，只能先定义，在初始化

    //成组的view事件监听的写法，有序数组，监听事件类似


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.ui_tabs);
        ButterKnife.bind(this);
        ButterKnife.apply(linearLayouts, OnClick);
        tint();
        setSwipeBackEnable(false);
        toolbar.setTitle("谁谁谁");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }

    final ButterKnife.Action<View> OnClick = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, final int index) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index >= 0 && index < linearLayouts.size()) {
                        setTabSelection(index);
                    }
                }
            });
        }
    };

    @butterknife.OnClick(R.id.add_layout)
    public void onClick(View v) {
        startActivity(new Intent(mActivity, AnalysisActivity.class));
    }

    public void setTabSelection(int index) {
        if (current == index) {
            return;
        }
        current = index;
        preClick();
        imageViews.get(index).setImageResource(pressed[index]);
        imageViews.get(index).setColorFilter(getResources().getColor(R.color.colorPrimary));
        textViews.get(index).setTextColor(getResources().getColor(R.color.colorPrimary));
        if (fragments[index] == null) {
            fragments[index] = getIndexFragment(index);
            transaction.add(R.id.content, fragments[index]);
        } else {
            transaction.show(fragments[index]);
        }
        transaction.commit();

        if (index == 3 || index == 2 || index == 1 || index == 0) {
            //不要用Color.WHITE,因为封装
            setTintColor(R.color.white);
        } else {
            setTintColor(R.color.colorPrimary);
        }
    }

    private Fragment getIndexFragment(int index) {
        switch (index) {
            case 0:
                fragment1 = new LineChartFragment7();
                return fragment1;
            case 1:
                fragment2 = new LineChartFragment7();
                return fragment2;
            case 2:
                fragment3 = new LineChartFragment7();
                return fragment3;
            case 3:
                fragment4 = new AnalysisOrderFragment();
                return fragment4;
            default:
                return fragment1;

        }
    }

    // 每次选中之前先清楚掉上次的选中状态
    private void preClick() {
        //回归正常样式
        for (int i = 0; i < linearLayouts.size(); i++) {
            textViews.get(i).setTextColor(textcolornormal);
            imageViews.get(i).setImageResource(normal[i]);
            imageViews.get(i).setColorFilter(getResources().getColor(R.color.black));
            // linearLayouts.get(i).setBackgroundColor(textcolornormal);
            // background不需要保持
        }
        transaction = fragmentManager.beginTransaction();
        //要用属性动画
        // transaction.setCustomAnimations (R.anim.push_left_in,R.anim.push_right_out);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        for (int i = 0; i < fragments.length; i++) {
            if (fragments[i] != null) {
                transaction.hide(fragments[i]);
            }
        }
    }

    private long exitTime = 0;

    //这里有问题，因为onstart不是每次切换都执行，因为不是replace
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            TS.t(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.hold, R.anim.popup_exit);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String s = intent.getStringExtra("types");
        if (!Tools.isEmpty(s) && s.equals("shopcard")) {
            setTabSelection(2);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //更新SP里面的用户，红包等等
        //点击购物车
        if (requestCode == 11 && resultCode == 2) {
            setTabSelection(2);
            //点击个人中心
        } else if (requestCode == 12 && resultCode == 2) {
            setTabSelection(3);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //更新sp中的memberId
        String x = getIntent().getStringExtra("type");
        if (x != null) {
            if (x.contains("login")) {
                setTabSelection(3);
            } else {
                if (x.equals("shopcard")) {
                    setTabSelection(2);
                }
            }
        }
    }


}

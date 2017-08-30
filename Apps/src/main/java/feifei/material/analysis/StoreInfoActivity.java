package feifei.material.analysis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import feifei.material.R;
import feifei.material.main.BaseActivity;
import library.util.AnimUtil;

public class StoreInfoActivity extends BaseActivity {

    public String[] orderTime7 = new String[7];
    public float[] orderCount7 = new float[7];
    public float[] orderMoney7 = new float[7];

    public float[] orderCount30 = new float[30];
    public float[] orderMoney30 = new float[30];
    public String[] orderTime30 = new String[30];

    @Bind(R.id.scrollview)
    ScrollView scrollview;

    @Override
    protected int getTintColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_store_info);
        ButterKnife.bind(this);
        tint();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        ((TextView) findViewById(R.id.store_name)).setText(intent.getStringExtra("storeName"));
        t("正在查询数据并生成报表");
        getData();
    }

    protected void getData() {
        for (int i = 0; i < orderCount7.length; i++) {
            orderCount7[i] = 5 + i * i * i;
            orderMoney7[i] = 1000 + i * i * i + 3;
            orderTime7[i] = "090" + i;
        }
        for (int i = 0; i < orderCount30.length; i++) {
            orderCount30[i] = 5 + i * i * i;
            orderMoney30[i] = 1000 + i * i * i + 3;
            orderTime30[i] = "090" + i;
        }
        scrollview.setVisibility(View.VISIBLE);
        getFragmentManager().beginTransaction().replace(R.id.fragment1, LineChartFragment7.newInstance()).commit();
        getFragmentManager().beginTransaction().replace(R.id.fragment2, LineChartFragment30.newInstance()).commit();
        getFragmentManager().beginTransaction().replace(R.id.fragment3, PieChartFragmentMoney.newInstance()).commit();
        getFragmentManager().beginTransaction().replace(R.id.fragment4, PieChartFragmentCount.newInstance()).commit();
        getFragmentManager().beginTransaction().replace(R.id.fragment5, HorizontalFragmentMoney.newInstance()).commit();
        getFragmentManager().beginTransaction().replace(R.id.fragment6, HorizontalFragmentCount.newInstance()).commit();
    }

    @Override
    public void onBackPressed() {
        finish();
        AnimUtil.animBackTopFinish(mActivity);
    }
}


package feifei.material.analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import feifei.material.R;

public class TestHorizontalFragmentMoney extends MainBaseFragment {

    protected int type;
    protected HorizontalBarChart mChart;

    public static TestHorizontalFragmentMoney newInstance(int type) {
        TestHorizontalFragmentMoney myBuyBasefragment = new TestHorizontalFragmentMoney();
        Bundle args = new Bundle();
        args.putInt("type", type);
        myBuyBasefragment.setArguments(args);
        return myBuyBasefragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_horizontal, container, false);
        initType();
        TextView button = (TextView) view.findViewById(R.id.button);
        button.setVisibility(View.GONE);
        mChart = (HorizontalBarChart) view.findViewById(R.id.chart1);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setDrawMarkerViews(false);
        mChart.setExtraOffsets(10, 10, 20, 0);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(300);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setAxisLineColor(getResources().getColor(R.color.pink));
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);
        xl.setTextColor(getResources().getColor(R.color.pink));
        xl.setTextSize(12);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setAxisLineColor(getResources().getColor(R.color.main_green));
        yl.setDrawGridLines(false);
        yl.setGridLineWidth(0.3f);
        yl.setTextSize(12);

        //这2个条件同时控制只显示最值
        yl.setShowOnlyMinMax(true);
        yl.setDrawTopYLabelEntry(true);

        yl.setYOffset(10);
        yl.setTextColor(getResources().getColor(R.color.main_green));

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setAxisLineColor(getResources().getColor(R.color.main_green));
        yr.setDrawGridLines(false);
        yr.setDrawTopYLabelEntry(false);
        yr.setTextSize(12);
        yr.setShowOnlyMinMax(true);
        yr.setTextColor(getResources().getColor(R.color.main_green));
        //设置反向 yr.setInverted(true);

        mChart.animateY(2500);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
        l.setFormSize(10f);
        l.setXEntrySpace(4f);

        // mChart.setDrawLegend(false);


        if (type == 0) {
//            loadData(true, "1");
            try {
                getData(null, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
//            loadData(false, "1");
            try {
                getData(null, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void setData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        for (int i = storeName.length - 1; i >= 0; i--) {
            xVals.add(storeName[i]);
            yVals1.add(new BarEntry(money[i], Math.abs(money.length - 1 - i)));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "营业额");
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(12f);
        data.setValueTextColor(getResources().getColor(R.color.deep_red));
        mChart.setData(data);
    }

    protected void initType() {
        switch (type) {
            case 0:
                url = DataActivity.URL + "getDay";
                break;
            case 1:
                url = DataActivity.URL + "getWeek";
                break;
            case 2:
                url = DataActivity.URL + "getMonth";
                break;
        }
    }

    protected String url = DataActivity.URL;


    float[] money;
    String[] storeName;

    private void getData(JSONArray jsonArray, String tag) throws JSONException {
//        money = new float[jsonArray.length()];
//        storeName = new String[jsonArray.length()];
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonobj = jsonArray.getJSONObject(i);
//            money[i] = (float) jsonobj.getDouble("store_money");
//            storeName[i] = jsonobj.getString("store_name");
//        }
//        setData();
        money = new float[30];
        storeName = new String[30];
        for (int i = 0; i < 30; i++) {
            money[i] = 1000 * i / 3f + 356;
            storeName[i] = "超市" + i;
        }
        setData();
    }


}

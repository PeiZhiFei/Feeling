package feifei.material.analysis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import feifei.material.main.ListBaseFragment;
import library.util.AnimUtil;
import library.util.TS;
import library.util.Tools;

/**
 * 这是一个通用的分页的Listview
 */
public class AnalysisOrderFragment extends ListBaseFragment<Order> {

    protected int type;

    public static AnalysisOrderFragment newInstance(int type) {
        AnalysisOrderFragment myBuyBasefragment = new AnalysisOrderFragment();
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
        page = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        listView.setDivider(null);
        listView.setDividerHeight(0);
        //viewpager只有第一个加载对话框
 /*       if (type == 1) {
            loadData(true);
        } else {
            loadData(false);
        }*/
        for (int i = 0; i < 30; i++) {
            Order order = new Order();
            order.setOrder_count(String.valueOf(Tools.scale(i / 2f * 31.6)));
            order.setOrder_money(String.valueOf(Tools.scale(i * 56.8)));
            order.setStore_id(i + "");
            order.setStore_name("超市" + i);
            data.add(order);
        }
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    protected void itemClick(int position) {
        if (Float.valueOf(data.get(position).getOrder_money()) > 0) {
            TS.tip(getActivity(), "订单列表有数据" + data.get(position).getStore_name());
            Intent intent = new Intent(getActivity(), StoreInfoActivity.class);
            intent.putExtra("id", data.get(position).getStore_id());
            intent.putExtra("storeName", data.get(position).getStore_name());
            startActivity(intent);
            AnimUtil.animToTop((Activity) getActivity());
        } else {
            TS.t(getActivity(), "该超市暂无营业额");
            TS.tip(getActivity(), "订单列表无数据" + data.get(position).getStore_name());
        }
    }


    //子类设置rp，list，adapter,class实体类
    protected void initType() {
        adapter = new AnalysisOrderAdapter(getActivity(), data);
//        mParams.put("page_number", String.valueOf(pageNum++));
//        mParams.put("page_size", String.valueOf(pageSize));
        cls = Order.class;
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


}

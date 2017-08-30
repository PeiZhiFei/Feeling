package feifei.material.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import feifei.material.R;
import library.util.EmptyView;
import library.util.NetUtil;
import library.util.TS;

/**
 * 这是一个通用的分页的Listview
 */
public abstract class ListBaseFragment<T> extends Fragment {
    protected ListView listView;
    protected TextView emptyTextView;

    protected ProgressBar footerPB;
    protected TextView footerTV;
    protected View footer;
    protected View view;
    //第一次开启对话框
    protected boolean first = true;
    protected int pageSize = 10;
    protected int pageNum = 1;
    protected List<T> data = new ArrayList<>();
    protected boolean scroll = false;
    protected boolean more = true;

    //需要子类补全的4个参数
    protected Map<String, String> mParams = new HashMap<>();
    protected String url;
    protected BaseAdapter adapter;
    protected Class<T> cls;
    protected boolean page = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_base, null);
        initView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view != footer) {
                    itemClick(i);
//                    Order order = orders.get(i);
//                    Intent intent = new Intent(mActivity, OrderInfoActivity.class);
//                    intent.putExtra("order", order.getOrder_sn());
//                    startActivity(intent);
//                    AnimUtil.animTo((Activity) mActivity);
                }
            }
        });
        if (page) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    scroll = false;
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            //小细节
                            if (listView.getTag().equals("true")) {
                                if (!scroll && more) {
                                    loadData(false);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    scroll = true;
                }
            });
        }
        initType();
        listView.setAdapter(adapter);
        loadData(true);
        return view;
    }

    protected abstract void itemClick(int position);

    //子类设置rp，list，adapter,class实体类
    protected abstract void initType();

    protected void initView() {
        listView = (ListView) view.findViewById(R.id.list);
        EmptyView tools = new EmptyView();
        View view2 = tools.getEmptyView(getActivity(), 0);
        ((ViewGroup) listView.getParent()).addView(view2);
        emptyTextView = tools.getEmptyText();
        emptyTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //todo emptyview的点击事件
                loadData(false);
            }
        });
        listView.setEmptyView(view2);
        if (page) {
//            footer = LayoutInflater.from(getActivity()).inflate(
//                    R.layout.listview_footer, null);
//            footerTV = (TextView) footer.findViewById(R.id.footer_tv);
//            footerPB = (ProgressBar) footer.findViewById(R.id.footer_pb);
            listView.addFooterView(footer);
        }
    }

    public void reset() {
        data.clear();
        pageNum = 1;
        adapter.notifyDataSetChanged();
    }

    //原来加了一个type作为tag

    protected void loadData(final boolean dialog) {
        if (!NetUtil.isNetConnected(getActivity())) {
            error("网络开小差了");
        } else {
            if (page) {
                mParams.put("pagenumber", String.valueOf(pageNum++));
                mParams.put("pagesize", String.valueOf(pageSize));
            }
        }

    }

    public long exitTime = 0;

    protected void error(String s) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
            if (s != null) {
                TS.t(getActivity(), s);
            }
        }
    }


    //更新footer状态
    protected void updateFooter(boolean loading, String info) {
        footerPB.setVisibility(loading ? View.VISIBLE : View.GONE);
        footerTV.setText(info);
    }
}

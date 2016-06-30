package feifei.feeling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import feifei.feeling.adapter.ContentA;
import feifei.feeling.bean.Content;
import feifei.feeling.select.ConstellationAdapter;
import feifei.feeling.select.DropDownMenu;
import feifei.feeling.select.GirdDropDownAdapter;
import feifei.feeling.select.ListDropDownAdapter;
import library.util.L;
import library.util.TS;

public class NewsFragment extends Fragment {

    @Bind(R.id.list)
    XRecyclerView list;

    @Bind(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;

    ContentA       contentA;
    CatLoadingView loadingView;

    //长度必须相等
    private String     headers[]  = {"图片", "作者", "标签"};
    private List<View> popupViews = new ArrayList<>();

    private GirdDropDownAdapter  cityAdapter;
    private ListDropDownAdapter  ageAdapter;
    private ListDropDownAdapter  sexAdapter;
    private ConstellationAdapter constellationAdapter;

    private String citys[]          = {"不限", "有图", "无图"};
    //    private String ages[]           = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};
    private String sexs[]           = {"不限", "我自己", "男作家", "女作家"};
    private String constellations[] = {"不限", "情感", "故事", "技术", "搞笑", "寻人启事"};

    private int constellationPosition = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
//        list = new XRecyclerView(getActivity());
//        list.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        list.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.grid_layout_animation));

        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        list.setRefreshHeader();
//        list.addItemDecoration(new VerticalSpaceItemDecoration(30, true));
        list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refresh(false);
            }

            @Override
            public void onLoadMore() {
                list.loadMoreComplete();
            }
        });
        contentA = new ContentA(getActivity(), v -> {
            String objid = (String) v.getTag();
            int pos = contentA.getItemPosById(objid);
            this.pos = pos;
            L.l(pos);
            Intent intent = new Intent(getActivity(), DetailUI.class);
            intent.putExtra("content", contentA.getItem(pos));

//            View transitionImage = v.findViewById(R.id.content_image);
//            if (transitionImage != null) {
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), transitionImage, EXTRA_IMAGE);
//                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
//            } else {
//            startActivity(intent);
//            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && linear) {
//                View sharedView = view.findViewById(R.id.content_image);
//                String transitionName = "anim";
//                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
//                startActivity(intent, transitionActivityOptions.toBundle());
//            } else {
            startActivityForResult(intent, 3);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }

        });
        list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                if (!hide && dy > 10) {
                if (dy > 10) {
                    activity.down();
//                    hidesToolbar();
//                    hide = true;
                } else if (dy < -10) {
                    activity.up();
//                    showsToolbar();
//                    hide = false;

                }
            }

        });
        list.setAdapter(contentA);
        initSelect();
        loadingView = new CatLoadingView();
        refresh(true);

        return view;
    }

    int pos = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3 && resultCode == Activity.RESULT_OK && data != null) {
            if (pos != -1) {
//                contentA.notifyDataSetChanged();
//                Content content = (Content) data.getSerializableExtra("result");
//                contentA.setItem(pos, content);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    boolean first = true;

    private void initSelect() {
        popupViews.clear();
//        if (first) {
        final ListView cityView = new ListView(getActivity());
        cityAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(citys));
        cityView.setDividerHeight(0);
        cityView.setAdapter(cityAdapter);

//        final ListView ageView = new ListView(getActivity());
//        ageView.setDividerHeight(0);
//        ageAdapter = new ListDropDownAdapter(getActivity(), Arrays.asList(ages));
//        ageView.setAdapter(ageAdapter);

//        final TextView ageView = new TextView(getActivity());
//        ageView.setText("切换");

        final ListView sexView = new ListView(getActivity());
        sexView.setDividerHeight(0);
        sexAdapter = new ListDropDownAdapter(getActivity(), Arrays.asList(sexs));
        sexView.setAdapter(sexAdapter);

        final View constellationView = getActivity().getLayoutInflater().inflate(R.layout.custom_layout, null);
        GridView constellation = ButterKnife.findById(constellationView, R.id.constellation);
        constellationAdapter = new ConstellationAdapter(getActivity(), Arrays.asList(constellations));
        constellation.setAdapter(constellationAdapter);
        TextView ok = ButterKnife.findById(constellationView, R.id.ok);
        ok.setOnClickListener(v -> {
            mDropDownMenu.setTabText(constellationPosition == 0 ? headers[2] : constellations[constellationPosition]);
            mDropDownMenu.closeMenu();
            if (constellationPosition == 0) {
                refresh(false);
            } else {
                search();
            }
        });

        popupViews.add(cityView);
        popupViews.add(sexView);
        popupViews.add(constellationView);
//        popupViews.add(ageView);

        cityView.setOnItemClickListener((parent, view, position, id) -> {
            cityAdapter.setCheckItem(position);
            mDropDownMenu.setTabText(position == 0 ? headers[0] : citys[position]);
            mDropDownMenu.closeMenu();
            if (position == 0) {
                refresh(false);
            } else {
                search();
            }
        });

//        ageView.setOnItemClickListener((parent, view, position, id) -> {
//            ageAdapter.setCheckItem(position);
//            mDropDownMenu.setTabText(position == 0 ? headers[1] : ages[position]);
//            mDropDownMenu.closeMenu();
//            search();
//        });

        sexView.setOnItemClickListener((parent, view, position, id) -> {
            sexAdapter.setCheckItem(position);
            mDropDownMenu.setTabText(position == 0 ? headers[1] : sexs[position]);
            mDropDownMenu.closeMenu();
            if (position == 0) {
                refresh(false);
            } else {
                search();
            }
        });

        constellation.setOnItemClickListener((parent, view, position, id) -> {
            constellationAdapter.setCheckItem(position);
            constellationPosition = position;
        });
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, list);
//            first = false;
//        }
    }

    boolean linear = false;

    //    @OnClick(value = R.id.change)
    public void change() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        }
        if (linear) {
            list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            linear = false;
        } else {
            list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            linear = true;
        }

    }

    MainActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    public void refresh(boolean anim) {
        if (!anim || first) {
            loadingView.show(getActivity().getSupportFragmentManager(), "正在加载中……");
        }
        BmobQuery<Content> query = new BmobQuery<>();
        query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
        query.setLimit(50);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
//        query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
        query.include("author");

        query.findObjects(getActivity(), new FindListener<Content>() {

            @Override
            public void onSuccess(List<Content> list) {
                contentA.setList(list);
                if (anim) {
                    anim();
                }
            }

            @Override
            public void onFinish() {
                if (list != null) {
                    list.refreshComplete();
                }
                if ((first || !anim) && loadingView != null) {
                    try {
                        loadingView.dismiss();
                    } catch (Exception e) {
                    }
                    first = false;
                }


//                LinearLayout l = mDropDownMenu.getTabMenuView();
//                for (int i = 0; i < l.getChildCount(); i++) {
//                    TextView tv = (TextView) l.getChildAt(i);
//                    tv.setText(headers[i]);
//                }

                super.onFinish();
            }

            @Override
            public void onError(int arg0, String arg1) {
                TS.s(getActivity(), "加载失败" + arg0 + "%%%%" + arg1);
            }
        });
    }

    public void search() {
        loadingView.show(getActivity().getSupportFragmentManager(), "正在加载中……");
        BmobQuery<Content> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setLimit(50);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
        query.include("author");

        //搜索功能
        query.addWhereContains("content", "人");

        query.findObjects(getActivity(), new FindListener<Content>() {

            @Override
            public void onSuccess(List<Content> list) {
                contentA.setList(list);
            }

            @Override
            public void onFinish() {
                if (list != null) {
                    list.refreshComplete();
                }
                if (loadingView != null) {
                    loadingView.dismiss();
                }
                super.onFinish();
            }

            @Override
            public void onError(int arg0, String arg1) {
                TS.s(getActivity(), "加载失败" + arg0 + "%%%%" + arg1);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void anim() {
        list.scheduleLayoutAnimation();
    }


    public boolean back() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
            return true;
        } else {
            return false;
        }
    }
}

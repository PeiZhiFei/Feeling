package feifei.feeling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import feifei.feeling.bean.Content;
import library.util.TS;

public class NewsFragment extends Fragment {

    @Bind(R.id.list)
    XRecyclerView list;

    ContentA       contentA;
    CatLoadingView loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, view);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
//        list.addItemDecoration(new VerticalSpaceItemDecoration(30, true));
        list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                list.loadMoreComplete();
            }
        });
        contentA = new ContentA(getActivity(), v -> {
            String objid = (String) v.getTag();
            int pos = contentA.getItemPosById(objid);
            Intent intent = new Intent(getActivity(), DetailUI.class);
            intent.putExtra("content", (Parcelable) contentA.getItem(pos));

//            View transitionImage = v.findViewById(R.id.content_image);
//            if (transitionImage != null) {
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), transitionImage, EXTRA_IMAGE);
//                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
//            } else {
            startActivity(intent);
//            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                View sharedView = view.findViewById(R.id.content_image);
//                String transitionName = "anim";
//                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
//                startActivity(intent, transitionActivityOptions.toBundle());
//            } else {
//                startActivity(intent);
//            }


        });
        list.setAdapter(contentA);
        loadingView = new CatLoadingView();
        refresh();
        return view;
    }

    public void refresh() {
        loadingView.show(getActivity().getSupportFragmentManager(), "正在加载中……");
        BmobQuery<Content> query = new BmobQuery<>();
        query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
        query.setLimit(20);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
//        query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
        query.include("author");
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

}

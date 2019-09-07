package com.java.tamhou.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.tamhou.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment implements NewsInterface.View
{
    private NewsAdapter adapter;
    private NewsPresenter Presenter;

    private boolean isViewPrepared; // 标识fragment视图已经初始化完毕
    private boolean hasFetchData; // 标识已经触发过懒加载数据
    private int type;
    static private String keyword = "";
    static private String blockedWord = "";
    public void setKeyword(String w) {
        keyword = w;
    }
    static public void setBlockedWord(String w)
    {
        blockedWord = w;
    }
    private static int counter;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private int pageIndex = 1;

    public static NewsFragment newInstance(int type)
    {
        Bundle bundle = new Bundle();
        NewsFragment fragment = new NewsFragment();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        Log.d("create", "News " + type + " created");
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        counter = 0;
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
//        storage = new SPUtils();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, root);
        Log.d("create", "News " + type + " view created");


        adapter = new NewsAdapter(getActivity());
        Presenter = new NewsPresenter(this, getContext(), adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 设置主线程访问网络，后期应改用多线程
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Presenter.loadData(type, pageIndex, keyword);
        //添加边框


        //下拉刷新的另一种实现

        /*recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                pageIndex += 1;
                adapter.clear();
                Presenter.loadData(type, pageIndex, keyword, blockedWord);
            }
        });*/

        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex += 1;
                        adapter.clear();
                        Presenter.loadData(type, pageIndex, keyword, blockedWord);
                    }
                }, 0);
            }
        });

        //更多加载
        adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageIndex += 1;
                Presenter.loadData(type, pageIndex, keyword, blockedWord);

            }
        });




        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position)
            {
                //                adapter.clear();
                ArrayList<String> data = new ArrayList<String>();
                News news = adapter.getItem(position);

//                SPUtils.put(getContext(), String.valueOf(SPUtils.size())/*news.getNewsId()*/, news.toString());

                data.add(news.toString());
                Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("data", data);
                intent.putExtras(bundle);
                startActivity(intent);
                news.setViewed();

                adapter.update(news, position);

            }
        });


        isViewPrepared = true;
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }


    public void returnData(List<News> datas)
    {
        adapter.addAll(datas);
        Log.e("adapter",adapter.getAllData().size()+"");
    }

    public void onResume()
    {
        super.onResume();
    }


    private void lazyFetchDataIfPrepared() {
        Log.d("data",type+""+isViewPrepared+"&&&"+hasFetchData);
        if (isViewPrepared && getUserVisibleHint() && !hasFetchData) {
            lazyFetchData();
            hasFetchData = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //视图销毁 数据设置为空
        hasFetchData=false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyFetchDataIfPrepared();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyFetchDataIfPrepared();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hasFetchData = false;
        isViewPrepared = false;
    }

    protected void lazyFetchData() {
        //mPresenter.loadData(type,pageIndex);
        pageIndex=pageIndex+1;
    }
}

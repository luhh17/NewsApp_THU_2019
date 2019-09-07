package com.java.tamhou.ui.seen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.tamhou.R;
import com.java.tamhou.ui.news.News;
import com.java.tamhou.ui.news.NewsAdapter;
import com.java.tamhou.ui.news.NewsDetailsActivity;
import com.java.tamhou.ui.news.NewsPresenter;
import com.java.tamhou.ui.storage.SPUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlideshowFragment extends Fragment
{

    private NewsAdapter adapter;
    private NewsPresenter Presenter;

    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, root);
        adapter = new NewsAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Map<String, String> newsMap = (Map<String, String>) SPUtils.getAll(getContext());
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                adapter.clear();
                ArrayList<News> newsList = new ArrayList<>();
                HashSet<String> idList = new HashSet<>();
//                Presenter.loadData(type, 1);
                for(int i = SPUtils.size() - 1; i >= 0; i--)
                {
                    String str = "";
                    str = (String) SPUtils.get(getContext(), String.valueOf(i), (String) "123");
                    News news = News.parseString(str);
                    if(idList.contains(news.getNewsId()))
                        continue;
                    newsList.add(news);
                    idList.add(news.getNewsId());

                }
                adapter.addAll(newsList);
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

        return root;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}
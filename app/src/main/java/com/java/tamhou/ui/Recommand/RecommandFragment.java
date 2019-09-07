package com.java.tamhou.ui.Recommand;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.tamhou.R;
import com.java.tamhou.ui.news.JsonParser;
import com.java.tamhou.ui.news.News;
import com.java.tamhou.ui.news.NewsAdapter;
import com.java.tamhou.ui.news.NewsDetailsActivity;
import com.java.tamhou.ui.news.NewsPresenter;
import com.java.tamhou.ui.storage.SPUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommandFragment extends Fragment
{

    private NewsAdapter adapter;
    private NewsPresenter Presenter;
    HashMap<String, Integer> typeMap;
    private JsonParser parser;
    private int page = 1;
    HashMap<Integer, String> int2Type;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;

    private int findMostViewed()
    {
        int[] count = new int[11];
        HashSet<String> idList = new HashSet<>();
        for(int i = SPUtils.size() - 1; i >= 0; i--)
        {
            String str = "";
            str = (String) SPUtils.get(getContext(), String.valueOf(i), (String) "123");
            News news = News.parseString(str);
            if(idList.contains(news.getNewsId()))
                continue;
            idList.add(news.getNewsId());
            count[typeMap.get(news.getCategory())] += 1;
        }
        int max = 0;
        int maxId = 0;
        for(int i = 0; i < 11; i++)
        {
            if(count[i] > max)
            {
                max = count[i];
                maxId = i;
            }
        }
        return maxId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        parser = new JsonParser();
        this.typeMap = new HashMap<>();
        this.int2Type = new HashMap<>();


        int2Type.put(0, "综合");
        int2Type.put(1, "娱乐");
        int2Type.put(2, "军事");
        int2Type.put(3, "教育");
        int2Type.put(4, "文化");
        int2Type.put(5, "健康");
        int2Type.put(6, "财经");
        int2Type.put(7, "体育");
        int2Type.put(8, "汽车");
        int2Type.put(9, "科技");
        int2Type.put(10, "社会");
        typeMap.put("", 0);
        typeMap.put("娱乐", 1);
        typeMap.put("军事", 2);
        typeMap.put("教育", 3);
        typeMap.put("文化", 4);
        typeMap.put("健康", 5);
        typeMap.put("财经", 6);
        typeMap.put("体育", 7);
        typeMap.put("汽车", 8);
        typeMap.put("科技", 9);
        typeMap.put("社会", 10);

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
                int type = findMostViewed();
                String mostViewType = int2Type.get(type);
                ArrayList<News> newsList = parser.getNews(type, 8*page, "", "");
                //ArrayList<News> newsStoredList = new ArrayList<>();
                HashSet<String> idList = new HashSet<>();
                // newsStoredList加载所有已经阅读过的新闻
                HashSet<String> markedIdList = new HashSet<>();
                for(int i = SPUtils.size() - 1; i >= 0; i--)
                {
                    String str = "";
                    str = (String) SPUtils.get(getContext(), String.valueOf(i), (String) "123");
                    News news = News.parseString(str);
                    //newsStoredList.add(news);
                    idList.add(news.getNewsId());
                    if(!news.isMarked())
                        continue;
                    markedIdList.add(news.getNewsId());
                }

                for(int i = 0; i < newsList.size(); i++)
                {
                    News news = newsList.get(i);
                    if(idList.contains(news.getNewsId()))
                    {
                        newsList.remove(i);
                        if(markedIdList.contains(news.getNewsId()))
                            news.setMarked();
                        news.setViewed();
                        newsList.add(i, news);
                    }

                }
                adapter.addAll(newsList);
                page += 1;
                Toast.makeText(getContext(), "当前为您推荐的新闻类别为"+ mostViewType, Toast.LENGTH_LONG).show();
            }
        });


        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position)
            {
                //                adapter.clear();
                ArrayList<String> data = new ArrayList<String>();
                News news = adapter.getItem(position);

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
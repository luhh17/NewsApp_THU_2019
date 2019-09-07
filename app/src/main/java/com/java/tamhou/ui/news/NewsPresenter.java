package com.java.tamhou.ui.news;

import android.content.Context;

import com.java.tamhou.ui.storage.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class NewsPresenter implements NewsInterface.Presenter,
        NewsInterface.OnLoadFirstDataListener
{
    private NewsInterface.View view;
    private NewsInterface.Model model;
    private Context context;
    private NewsAdapter adapter;
    JsonParser parser;
    static int i;
    HashMap<String, Integer> typeMap;


    public NewsPresenter(NewsInterface.View view, Context context, NewsAdapter adapter)
    {
        this.typeMap = new HashMap<>();
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
        this.view = view;
        this.model = new NewsModel();
        this.context=context;
        this.adapter = adapter;
        parser = new JsonParser();
    }




    public void loadData(int type, int page, String keyword, String blockedWord)
    {

        ArrayList<News> newsList = parser.getNews(type, 8*page, keyword,
                blockedWord);
        //ArrayList<News> newsStoredList = new ArrayList<>();
        HashSet<String> idList = new HashSet<>();
        // newsStoredList加载所有已经阅读过的新闻
        HashSet<String> markedIdList = new HashSet<>();
        for(int i = SPUtils.size() - 1; i >= 0; i--)
        {
            String str = "";
            str = (String) SPUtils.get(context, String.valueOf(i), (String) "123");
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
    }




    @Override
    public void onSuccess(List<News> list) {
        view.returnData(list);
    }

    @Override
    public void loadData(int type, int page)
    {
        this.loadData(type, page, "", "");
    }

//    @Override
//    public void onFailure(String str, Throwable e) {
//        NetWorkUtil.checkNetworkState(context);
//    }

}

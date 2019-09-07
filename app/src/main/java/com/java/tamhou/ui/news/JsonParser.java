package com.java.tamhou.ui.news;

import android.icu.util.Calendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

public class JsonParser
{
    // read from file and return as String
    private static String readFile(String path)
    {
        String content = "";
        StringBuilder builder = new StringBuilder();
        try
        {
            FileInputStream fis = new FileInputStream(path);
            try
            {
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String tempString = "";
                try
                {
                    while ((tempString = reader.readLine()) != null)
                    {
                        builder.append(tempString);
                    }
                    reader.close();
                    content = builder.toString();
                }catch(IOException e)
                {
                    System.out.println("IOException");
                }
            }catch(UnsupportedEncodingException e)
            {
                System.out.println("UnsupportedEncodingException");
            }

        }catch(FileNotFoundException e)
        {
            System.out.println("FileNotFoundException");
        }
        return content;
    }

    private static HashSet<String> loadedNewsSet;
    // path -- the path to json file
    // newsNumber -- number of news you want to get
    // return String[newsNumber]
    public ArrayList<News> getNews(int type, int newsNumber, String keyword,
                                   String blockWord)
    {
        String categories = "";
        switch (type)
        {
            case 0:
            {
                break;
            }

            case 1:
                categories = "娱乐";
                break;
            case 2:
                categories = "军事";
                break;
            case 3:
                categories = "教育";
                break;
            case 4:
                categories = "文化";
                break;
            case 5:
                categories = "健康";
                break;
            case 6:
                categories = "财经";
                break;
            case 7:
                categories = "体育";
                break;
            case 8:
                categories = "汽车";
                break;
            case 9:
                categories = "科技";
                break;
            case 10:
                categories = "社会";
                break;

        }

        // 获取当前系统时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        String _month = String.valueOf(month);
        if(month < 10)
        {
             _month = "0" + month;
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String endDate = year + "-" + _month + "-" + day;

        String url = UrlBuilder.getUrl(newsNumber, ""/*"2019-08-30"*/,
                endDate, keyword, categories);
        String temp = GetFromUrl.getJsonFromUrl(url);
        // parse String into JSONObject
        JSONObject object = JSONObject.fromObject(temp);
        // retrieve news from JSONObject as JSONArray
        JSONArray dataArray = JSONArray.fromObject(object.getJSONArray("data"));
        if(newsNumber > dataArray.size())
            newsNumber = dataArray.size();
        ArrayList<News> newsArray = new ArrayList<>();
        for(int i = 0; i < dataArray.size(); i++)
        {
            boolean isBlocked = false;
            JSONObject news = dataArray.getJSONObject(i);
            String newsId = news.getString("newsID");
            if(loadedNewsSet.contains(newsId))
                continue;
            loadedNewsSet.add(newsId);
            if(!blockWord.equals(""))
            {

                JSONArray keywordsJsonArray = JSONArray.fromObject(news.get("keywords"));
                for (int j = 0; j < keywordsJsonArray.size(); j++)
                {
                    JSONObject keywordJsonObject = keywordsJsonArray.getJSONObject(j);
                    String word = keywordJsonObject.getString("word");
                    if (word.contains(blockWord))
                    {
                        isBlocked = true;
                        break;
                    }
                }
            }
            if(isBlocked)
                continue;
            String title = news.getString("title");
//            if(!title.contains(keyword))
//                continue;
            String content = news.getString("content");
            String image = news.getString("image");
            String category = news.getString("category");
            String publishTime = news.getString("publishTime");
            String video = news.getString("video");
            String publisher = news.getString("publisher");
            if(title.contains(blockWord) && !blockWord.equals(""))
                continue;
            if (publisher.equals("其他"))
            {
                publisher = "权威发布";
            }
            newsArray.add(new News(title, content, image, publishTime, publisher, video,
                    category, newsId, "false"));
            if(newsArray.size() == newsNumber)
                break;
        }
        return newsArray;
    }

    public JsonParser()
    {
        loadedNewsSet = new HashSet<>();
    }


}

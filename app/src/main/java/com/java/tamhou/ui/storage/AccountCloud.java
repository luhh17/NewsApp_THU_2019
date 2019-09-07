package com.java.tamhou.ui.storage;


import android.content.Context;

import com.java.tamhou.ui.news.News;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class AccountCloud
{
    private static boolean loginStatus;
    public static void save(String username, String password, Context context)
    {
        //final AVObject testObject = new AVObject("TestObject");
//        testObject.put("words", "Hello world!");
//        testObject.saveInBackground().blockingSubscribe();
        AVObject account = new AVObject("Account");
        account.put("username", username);
        account.put("password", password);

        HashSet<String> favIdSet = new HashSet<>();
        HashSet<String> viewedIdSet= new HashSet<>();
        ArrayList<String> favNewsStrList = new ArrayList<>();
        ArrayList<String> viewedNewsStrList = new ArrayList<>();
        for(int i = SPUtils.size() - 1; i >= 0; i--)
        {
            String str = "";
            str = (String) SPUtils.get(context, String.valueOf(i), (String) "123");
            News news = News.parseString(str);
            if(viewedIdSet.contains(news.getNewsId()))
                continue;
            if(news.isMarked())
            {
                favIdSet.add(news.getNewsId());
                favNewsStrList.add(str);
            }
            viewedIdSet.add(news.getNewsId());
            viewedNewsStrList.add(str);
        }

        account.put("favNewsIdList", favIdSet);
        account.put("viewedNewsList", viewedNewsStrList);
        account.saveInBackground().subscribe(new Observer<AVObject>() {
            public void onSubscribe(Disposable disposable) {
            }
            public void onNext(AVObject avObject) {
                System.out.println("remove field finished.");
            }
            public void onError(Throwable throwable) {
            }
            public void onComplete() {
            }
        });
    }

    public static void clear()
    {

    }

    public static String create(String username, String password)
    {
        //final AVObject testObject = new AVObject("TestObject");
        AVObject account = new AVObject("Account");
        account.put("username", username);
        account.put("password", password);
        account.saveInBackground().subscribe(new Observer<AVObject>() {
            public void onSubscribe(Disposable disposable) {
            }
            public void onNext(AVObject avObject) {
                //System.out.println("remove field finished.");
            }
            public void onError(Throwable throwable) {
            }
            public void onComplete() {
            }
        });
        return "创建成功";
    }

    public static class MyObserver implements Observer<List<AVObject>>
    {
        private String username;
        private String password;
        private Context context;
//        private static boolean loginStatus=false;
        public void onSubscribe(Disposable disposable) {}
        public void onNext(List<AVObject> userFiles)
        {
            if(userFiles.size() == 0)
            {
                //System.out.println("not find");
                loginStatus = false;

                return;
            }
            else {
                loginStatus = true;
            }
            //Log.d("234", String.valueOf(loginStatus));

            AVObject userFile = userFiles.get(0);
            List<String> favNewsIdList = new ArrayList<String>();
            List<String> viewedNewsList = new ArrayList<String>();
            //System.out.println(userFile);

            if(!userFile.containsKey("favNewsIdList"))
                return;
            if(!userFile.containsKey("viewedNewsList"))
                return;
            favNewsIdList = userFile.getList("favNewsIdList");

            viewedNewsList = userFile.getList("viewedNewsList");
            SPUtils.clear(context);
            for(int i = 0; i < viewedNewsList.size(); i++)
            {
                //System.out.println(viewedNewsList.get(i));
                News news = News.parseString(viewedNewsList.get(i));
                news.setViewed();
                if(favNewsIdList.contains(news.getNewsId()))
                    news.setMarked();
                SPUtils.put(context, String.valueOf(SPUtils.size()), news.toString());
            }
        }
        public void onError(Throwable throwable) {}
        public void onComplete() {
        }
        public boolean getLoginStatus()
        {
            return loginStatus;
        }
        public void setUsername(String name)
        {
            this.username = name;
        }
        public void setPassword(String password)
        {
            this.password = password;
        }
        public void setContext(Context context)
        {
            this.context = context;
        }
    }

    public static boolean load(String username, String inputPassword, Context context)
    {
        AVQuery<AVObject> query = new AVQuery<>("Account");
        query.whereEqualTo("username", username);
        query.whereEqualTo("password", inputPassword);
        query.orderByDescending("createdAt");
        MyObserver observer = new MyObserver();
        observer.setContext(context);
        observer.setPassword(inputPassword);
        observer.setUsername(username);
        query.findInBackground().subscribe(observer);
       // System.out.println(observer.getLoginStatus());
       // return observer.getLoginStatus();
        return loginStatus;
    }
}

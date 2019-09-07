package com.java.tamhou.ui.news;

public class NewsModel implements NewsInterface.Model
{

    @Override
    public void loadData( int type, final NewsInterface.OnLoadFirstDataListener listener,int page) {
//        Observable<NewsJson>.subscribeOn(Schedulers.io())
////                .map(new Func1<NewsJson, List<News>>()
////                {
////                    @Override
////                    public List<News> call(NewsJson newsgson)
////                    { //
////                        List<News> newsList = new ArrayList<News>();
////                        for (News news : newsgson.getNewsList())
////                        {
////                            News new1 = new News("title", "content", "image");
////                            newsList.add(new1);
////                        }
////                        return newsList; // 返回类型
////                    }
////                })
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Subscriber<List<News>>()
////                {
////
////                    public void onNext(List<News> newsList) {
////                        //成功  数据传出去
////                        listener.onSuccess(newsList);
////                    }
////
////
////                    public void onCompleted() {
////                    }
////
////
////                    public void onError(Throwable e) {
////                        Log.e("error",e.getMessage());
////                        //listener.onFailure("请求失败",e);
////                    }
////                });
////
////
////    }
    }
}

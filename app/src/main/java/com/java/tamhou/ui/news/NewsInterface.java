package com.java.tamhou.ui.news;

import java.util.List;

public interface NewsInterface
{
    interface View{
        void returnData(List<News> datas);
    }

    interface OnLoadFirstDataListener
    {
        void  onSuccess(List<News> list);
 //       void  onFailure(String str,Throwable e);
    }

    interface Presenter  {
        void loadData(int type, int page);
    }

    interface Model {
        void loadData(int type, OnLoadFirstDataListener listener, int page);
    }
}

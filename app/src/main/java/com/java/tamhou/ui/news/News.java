package com.java.tamhou.ui.news;


import java.util.ArrayList;
import java.util.Collections;

public class News
{
    private String title;
    private String content;
    private String picture;
    private String publishTime;
    private String publisher;
    private String video;
    private String category;
    private boolean isLoaded;
    private boolean isViewed;
    private String newsId;
    private boolean isMarked;


    public News(String title, String content, String picture,
                String publishTime, String publisher, String video, String category
            , String newsId, String isMarked)
    {
        this.title = title;
        this.content = content;
        this.picture = picture.replaceAll("[\\[\\]]", "");
        this.publisher = publisher;
        this.publishTime = publishTime;
        this.video = video;
        this.category = category;
        this.isViewed = false;
        this.newsId = newsId;
        if(isMarked.equals("true"))
            this.isMarked = true;
        else
            this.isMarked = false;
    }

    public String getTitle()
    {
        return this.title;
    }
    public String getContent()
    {
        return this.content;
    }
    public ArrayList<String> getPicture()
    {
        String[] image = this.picture.split(",");
        ArrayList<String> imageList = new ArrayList<>() ;
        Collections.addAll(imageList, image);
        return imageList;
    }
    public String getPublishTime() {return this.publishTime;}
    public String getPublisher() {return this.publisher;}
    public String getVideo() {
        return this.video;
    }
    public String getCategory() {
        return this.category;
    }
    public String getNewsId(){
        return this.newsId;
    }
    public boolean isLoaded(){
        return this.isLoaded;
    }
    public boolean isViewed(){
        return this.isViewed;
    }
    public void setViewed()
    {
        isViewed = true;
    }
    public boolean isMarked(){
        return this.isMarked;
    }
    public void setMarked()
    {
        this.isMarked = true;
    }
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.title + "!!!");
        builder.append(this.content + "!!!");
        builder.append(this.picture + "!!!");
        builder.append(this.publishTime + "!!!");
        builder.append(this.publisher + "!!!");
        builder.append(this.video + "!!!");
        builder.append(this.category + "!!!");
        builder.append(this.newsId + "!!!");
        builder.append(this.isMarked);
        return builder.toString();
    }
    public void setUnMarked()
    {
        this.isMarked = false;
    }
    public static News parseString(String str)
    {
        String[] ele = str.split("!!!");
        return new News(ele[0], ele[1], ele[2], ele[3], ele[4], ele[5], ele[6],
                ele[7], ele[8]);
    }
}

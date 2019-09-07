package com.java.tamhou.ui.news;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class UrlBuilder
{
    public static final String basicUrl = "https://api2.newsminer.net/svc/news/queryNewsList?";
    public static String getUrl(int size, String startDate, String endDate, String words, String categories)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(basicUrl);
        if(size != 0)
            builder.append("size=" + size);
//        if(startDate != "")
        builder.append("&startDate=" + startDate);
        builder.append("&endDate=" + endDate);
        try
        {
            builder.append("&words=" + URLEncoder.encode(words, "utf8"));
            builder.append("&categories=" + URLEncoder.encode(categories, "utf8"));
            return builder.toString();
        } catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

}

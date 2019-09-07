package com.java.tamhou.ui.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.java.tamhou.R;
import com.java.tamhou.SharedPref;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsViewHolder extends BaseViewHolder<News>
{
    private TextView titleView;
    private ImageView imageView;
    private TextView publishTimeView;
    private TextView publisherView;

    public Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    public NewsViewHolder(ViewGroup parent)
    {
        super(parent, R.layout.news_view_holder);
        titleView = $(R.id.titleView);
        imageView = $(R.id.imageView);
        publishTimeView = $(R.id.publishTimeView);
        publisherView = $(R.id.publisherView);
    }

    @Override
    public void setData(final News data)
    {

        titleView.setText(data.getTitle());
        publishTimeView.setText(data.getPublishTime());
        publisherView.setText(data.getPublisher());
        Glide.with(getContext())
                .load(data.getPicture().get(0))
                .into(imageView);
//        imageView.setImageBitmap(getImageBitmap(data.getPicture().get(0)));
        if(data.isViewed()) titleView.setTextColor(Color.GRAY);
        else {
            SharedPref sharedPref = new SharedPref(this.getContext());
            if (sharedPref.loadNightModeState())
                titleView.setTextColor(Color.WHITE);
            else
                titleView.setTextColor(Color.BLACK);
        }
    }


}


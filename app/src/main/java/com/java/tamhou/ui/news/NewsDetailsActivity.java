package com.java.tamhou.ui.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.java.tamhou.R;
import com.java.tamhou.ShareAnyWhereUtil;
import com.java.tamhou.SharedPref;
import com.java.tamhou.ui.storage.AccountCloud;
import com.java.tamhou.ui.storage.SPUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/24.
 */

public class NewsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.NSV)
    NestedScrollView nsv;
    @BindView(R.id.CL)
    ConstraintLayout cl;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.contentViewDetail)
    TextView contentView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.shareFab)
    FloatingActionButton shareFAB;
    @BindView(R.id.videoView)
    VideoView videoView;
    static private News news;
    private SharedPref sharedPref;
    private boolean isMarked = false;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);

        toolbar.setTitle("新闻详情");

        setSupportActionBar(toolbar);
//        设置返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        final ArrayList<String> data = bundle.getStringArrayList("data");
        String newsStr = data.get(0);
        news = News.parseString(newsStr);
        Log.d("", news.getVideo());
        if(!news.getVideo().equals("")) {
            Uri video = Uri.parse(news.getVideo());
            //Uri video = Uri.fromFile(new File(news.getVideo().substring(6)));

            System.out.println(video);
            videoView.setVideoPath(video.toString());
            //videoView.setVideoURI(video);
            videoView.start();

        }
        //显示收藏
        for(int i = SPUtils.size() - 1; i >= 0; i--)
        {
            String str = "";
            str = (String) SPUtils.get(getApplicationContext(), String.valueOf(i), (String) "123");
            News _news = News.parseString(str);
            if(news.getNewsId().equals(_news.getNewsId()))
            {
                if(_news.isMarked()) {
                    news.setMarked();
                    isMarked = true;
                }
                else
                {
                    news.setUnMarked();
                    isMarked = false;
                }
                break;
            }
        }
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        /*Glide.with(this)
                .load(news.getPicture())
                .into(ivImage);*/
        ivImage.setImageBitmap(getImageBitmap(news.getPicture().get(0)));
        contentView.setText(news.getContent());
        sharedPref = new SharedPref(getApplicationContext());
        if (sharedPref.loadNightModeState()) {
            contentView.setTextColor(Color.WHITE);
            contentView.setBackgroundColor(Color.DKGRAY);
            nsv.setBackgroundColor(Color.DKGRAY);
            cl.setBackgroundColor(Color.DKGRAY);
        } else {
            contentView.setTextColor(Color.BLACK);
            contentView.setBackgroundColor(Color.WHITE);
            nsv.setBackgroundColor(Color.WHITE);
            cl.setBackgroundColor(Color.WHITE);
        }
        if (news.isMarked())
            fab.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_on));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AccountCloud.save("user", "password", getApplicationContext());
                String msg;
                if (isMarked) {
                    fab.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_off));
                    //setFavorite(false);
                    news.setUnMarked();
                    isMarked = false;
                    msg = "Deleted from Favorite";
                } else {
                    fab.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_on));
                    //setFavorite(true);
                    news.setMarked();
                    isMarked = true;
                    msg = "Added to Favorite";
                }
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //                SPUtils.put(getContext(), String.valueOf(SPUtils.size())/*news.getNewsId()*/, news.toString());
                SPUtils.put(getApplicationContext(), String.valueOf(SPUtils.size())/*news.getNewsId()*/, news.toString());
            }
        });

        shareFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(NewsDetailsActivity.this, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ArrayList<Uri> uris = new ArrayList<>();
                        for(int i=0; i < news.getPicture().size(); i++)
                            //if (news.getPicture().get(i) != null)
                        {
                            Bitmap bitmap = getImageBitmap(news.getPicture().get(i));
                            if (bitmap == null) continue;
                            Uri uri = ShareAnyWhereUtil.bitmapToUri(NewsDetailsActivity.this, bitmap);
                            if (uri == null) continue;
                            uris.add(uri);
                        }

                        switch (item.getItemId()) {
                            case R.id.share_pics_to_wechat:
                                if (uris.size()!=0) {
                                    ShareAnyWhereUtil.shareWechat(NewsDetailsActivity.this, uris, news.getTitle());
                                    Toast.makeText(NewsDetailsActivity.this, "Sharing pictures to Wechat", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewsDetailsActivity.this, "No picture to share", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            case R.id.share_text_to_wechat:
                                ShareAnyWhereUtil.shareWechatText(NewsDetailsActivity.this, news.getTitle());
                                Toast.makeText(NewsDetailsActivity.this, "Sharing text to Wechat", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.share_to_QQ:
                                if (uris.size()!=0) {
                                    ShareAnyWhereUtil.shareQQ(NewsDetailsActivity.this, uris, news.getTitle());
                                    Toast.makeText(NewsDetailsActivity.this, "Sharing pictures to QQ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewsDetailsActivity.this, "No picture to share", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            /*case R.id.share_to_weibo:
                                Toast.makeText(NewsDetailsActivity.this, "Shared to Weibo", Toast.LENGTH_SHORT).show();
                                ShareAnyWhereUtil.shareWeibo(NewsDetailsActivity.this, uris, "News shared from NewsApp");
                                return true;*/
                            default:
                                return true;
                        }
                    }
                });
                popupMenu.getMenu().add(1, R.id.share_pics_to_wechat, 1, "Share pictures to Wechat");
                popupMenu.getMenu().add(1, R.id.share_text_to_wechat, 1, "Share text to Wechat");
                popupMenu.getMenu().add(1, R.id.share_to_QQ, 1, "Share pictures to QQ");
                //popupMenu.getMenu().add(1, R.id.share_to_weibo, 1, "Share to Weibo");
                popupMenu.show();
            }
        });

        SPUtils.put(getApplicationContext(), String.valueOf(SPUtils.size())/*news.getNewsId()*/, news.toString());
    }


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

}

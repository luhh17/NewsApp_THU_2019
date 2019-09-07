package com.java.tamhou.ui;

import android.app.Application;
import cn.leancloud.AVOSCloud;

public class NewsApp extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
//         // 配置 SDK 储存
//        AVOSCloud.setServer(AVOSService.API, "https://xxx.example.com");
//        // 配置 SDK 云引擎
//        AVOSCloud.setServer(AVOSService.ENGINE, "https://xxx.example.com");
//        // 配置 SDK 推送
//        AVOSCloud.setServer(AVOSService.PUSH, "https://xxx.example.com");
//        // 配置 SDK 即时通讯
//        AVOSCloud.setServer(AVOSService.RTM, "https://xxx.example.com");

        // 提供 this、App ID 和 App Key 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.AVOSCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        AVOSCloud.initialize(this, "DMhewGacjWR87Ho55jtldPQ0-gzGzoHsz", "rbSIF4xeD8lfGhevVYrhjL7K");



    }
}

package com.zhouzining.geetolsdkutils;

import android.app.Application;

import com.zzn.geetolsdk.yuanlilib.util.GeetolUtils;

/**
 * Created by Walter on 2018/11/15.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GeetolUtils.initSDK(MyApp.this);
    }
}

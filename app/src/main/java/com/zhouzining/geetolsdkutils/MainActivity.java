package com.zhouzining.geetolsdkutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gtdev5.geetolsdk.mylibrary.beans.PayBean;
import com.gtdev5.geetolsdk.mylibrary.callback.PayListener;
import com.gtdev5.geetolsdk.mylibrary.callback.UpdateDataListener;
import com.gtdev5.geetolsdk.mylibrary.util.GeetolUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GeetolUtils.initSDK(MainActivity.this).startSDK("电子相册", "channel", new UpdateDataListener() {
            @Override
            public void onSuccess() {
                Log.e("LogUtils", "getGoods" + GeetolUtils.getGoods());
                Log.e("LogUtils", "getWelcome" + GeetolUtils.getWelcome());
                Log.e("LogUtils", "getBanner" + GeetolUtils.getBanner());
                Log.e("LogUtils", "getContact" + GeetolUtils.getContact());
                Log.e("LogUtils", "getHpUrl" + GeetolUtils.getHpUrl());
                Log.e("LogUtils", "getGoodByName" + GeetolUtils.getGoodByName("三个月"));
            }

            @Override
            public void onFail(int errCode, Exception e) {
                Log.e("LogUtils ", "startSDK  onFail");

            }
        });

        GeetolUtils.pay(GeetolUtils.getPidByGoodName("三个月"), "wx", new PayListener() {
            @Override
            public void onSuccess(PayBean payBean) {
                Log.e("LogUtils ", "wx  " + payBean.getBean("wx"));
            }

            @Override
            public void onFail(int errCode, Exception e) {
                Log.e("LogUtils ", "wx  onFail");

            }
        });

        GeetolUtils.pay(GeetolUtils.getPidByGoodName("三个月"), "zfb", new PayListener() {
            @Override
            public void onSuccess(PayBean payBean) {
                Log.e("LogUtils ", "zfb  " + payBean.getBean("zfb"));

            }

            @Override
            public void onFail(int errCode, Exception e) {
                Log.e("LogUtils ", "zfb  onFail");

            }
        });


//        Error:com.android.builder.dexing.DexArchiveBuilderException
    }
}

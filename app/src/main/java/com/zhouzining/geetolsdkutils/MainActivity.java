package com.zhouzining.geetolsdkutils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gtdev5.geetolsdk.mylibrary.callback.UpdateDataListener;
import com.gtdev5.geetolsdk.mylibrary.callback.YuanliPayListener;
import com.gtdev5.geetolsdk.mylibrary.util.GeetolUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GeetolUtils.getSDK(MainActivity.this).startSDK("channel"
                , new UpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("LogUtils", "getGoods" + GeetolUtils.getGoods());
                        Log.e("LogUtils", "getWelcome" + GeetolUtils.getWelcome());
                        Log.e("LogUtils", "getBanner" + GeetolUtils.getBanner());
                        Log.e("LogUtils", "getContact" + GeetolUtils.getContact());
                        Log.e("LogUtils", "getHpUrl" + GeetolUtils.getHpUrl());
                        Log.e("LogUtils", "getGoodByName" + GeetolUtils.getGoodByName("三个月"));

                        GeetolUtils.getGoods().get(0).getPayWay();
                    }

                    @Override
                    public void onFail(int errCode, Exception e) {
                        Log.e("LogUtils ", "startSDK  onFail");
                    }
                });
        HashMap<String, String> remarkMap = new HashMap<>();
        GeetolUtils.payOrder(GeetolUtils.getPidByGoodName("一个月VIP"), "zfb", "13888888888"
                , remarkMap, new YuanliPayListener() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFail(int errCode, Exception e) {
                        Log.e("LogUtils ", "wx  onFail");
                    }
                });
//        GeetolUtils.payOrder(GeetolUtils.getPidByGoodName("三个月"), "wx", remarkMap
//                , new YuanliPayListener() {
//            @Override
//            public void onSuccess() {
//            }
//
//            @Override
//            public void onFail(int errCode, Exception e) {
//                Log.e("LogUtils ", "wx  onFail");
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GeetolUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

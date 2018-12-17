package com.zhouzining.geetolsdkutils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zzn.geetolsdk.yuanlilib.callback.UpdateDataListener;
import com.zzn.geetolsdk.yuanlilib.callback.YuanliPayListener;
import com.zzn.geetolsdk.yuanlilib.util.GeetolUtils;

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
                        Log.e("LogUtils", "getGoodByName" + GeetolUtils.getGoodByName("三个月VIP"));


                        HashMap<String, String> prepearRemarkMap = new HashMap<>();
                        HashMap<String, String> updateRemarkMap = new HashMap<>();
//                        GeetolUtils.payOrder(GeetolUtils.getPidByGoodName("制作音乐相册")
//                                , "zfb", new YuanliPayListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                    }
//
//                                    @Override
//                                    public void onFail(int errCode, Exception e) {
//                                        Log.e("LogUtils ", "wx  onFail");
//                                    }
//                                });
//                        GeetolUtils.payOrder(GeetolUtils.getPidByGoodName("制作音乐相册")
//                                , "zfb", remarkMap, new YuanliPayListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                    }
//
//                                    @Override
//                                    public void onFail(int errCode, Exception e) {
//                                        Log.e("LogUtils ", "wx  onFail");
//                                    }
//                                });
//                        GeetolUtils.payOrderGeetol(GeetolUtils.getPidByGoodName("一个月VIP")
//                                , "zfb", "18573676330"
//                                , new YuanliPayListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                    }
//
//                                    @Override
//                                    public void onFail(int errCode, Exception e) {
//                                        Log.e("LogUtils ", "wx  onFail");
//                                    }
//                                });
//

//                        GeetolUtils.payOrderGeetol(GeetolUtils.getPidByGoodName("制作音乐相册"), "zfb"
//                                , "13555558888", new YuanliPayListener() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onFail(int errCode, Exception e) {
//
//                                    }
//                                });

//                        GeetolUtils.payOrderYuanli("制作音乐相册", "zfb"
//                                , "8.8","13888888888", new YuanliPayListener() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onFail(int errCode, Exception e) {
//
//                                    }
//                                });
//
                        GeetolUtils.payOrderYuanli("制作音乐相册", "zfb"
                                , "8.8", prepearRemarkMap, updateRemarkMap, new YuanliPayListener() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onFail(int errCode, Exception e) {

                                    }
                                });
//
//                        GeetolUtils.payOrderYuanli("一个月VIP", "zfb"
//                                , "13888888888", "8.8"
//                                , new YuanliPayListener() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onFail(int errCode, Exception e) {
//
//                                    }
//                                });
//                        GeetolUtils.payOrderYuanli("一个月VIP", "zfb"
//                                , "13888888888", "8.8", remarkMap
//                                , new YuanliPayListener() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onFail(int errCode, Exception e) {
//
//                                    }
//                                });
//                        GeetolUtils.payOrder(GeetolUtils.getPidByGoodName("一个月VIP")
//                                , "zfb", "18573676330"
//                                , remarkMap, new YuanliPayListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                    }
//
//                                    @Override
//                                    public void onFail(int errCode, Exception e) {
//                                        Log.e("LogUtils ", "wx  onFail");
//                                    }
//                                });
                    }

                    @Override
                    public void onFail(int errCode, Exception e) {
                        Log.e("LogUtils ", "startSDK  onFail");
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

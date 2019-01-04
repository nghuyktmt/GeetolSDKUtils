package com.zhouzining.geetolsdkutils;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zzn.geetolsdk.yuanlilib.beans.ResultBean;
import com.zzn.geetolsdk.yuanlilib.callback.BaseCallback;
import com.zzn.geetolsdk.yuanlilib.callback.UpdateDataListener;
import com.zzn.geetolsdk.yuanlilib.callback.YuanliPayListener;
import com.zzn.geetolsdk.yuanlilib.util.GeetolUtils;
import com.zzn.geetolsdk.yuanlilib.util.ToastUtils;

import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("LogUtils", "beforeRequest  ");
        GeetolUtils.getSDK(MainActivity.this).startSDK("channel", new UpdateDataListener() {
            @Override
            public void onSuccess() {
                Log.e("LogUtils", "startSDKSuccess  ");
//                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                startActivity(intent);
                GeetolUtils.payOrderYuanli("文字转语音", "zfb", "0.1", new YuanliPayListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("LogUtils", "onSuccess  ");

                    }

                    @Override
                    public void onFail(int i, Exception e) {

                    }
                });
            }

            @Override
            public void onFail(int errCode, Exception e) {

            }
        });
        Log.e("LogUtils", "afterRequest  ");
//        GeetolUtils.getSDK(MainActivity.this).startSDK("channel"
//                , new UpdateDataListener() {
//                    @Override
//                    public void onSuccess() {
//                        Log.e("LogUtils", "getGoods" + GeetolUtils.getGoods());
//                        Log.e("LogUtils", "getWelcome" + GeetolUtils.getWelcome());
//                        Log.e("LogUtils", "getBanner" + GeetolUtils.getBanner());
//                        Log.e("LogUtils", "getContact" + GeetolUtils.getContact());
//                        Log.e("LogUtils", "getHpUrl" + GeetolUtils.getHpUrl());
//                        Log.e("LogUtils", "getGoodByName" + GeetolUtils.getGoodByName("三个月VIP"));
//
//                        HashMap<String, String> prepearRemarkMap = new HashMap<>();
//                        HashMap<String, String> updateRemarkMap = new HashMap<>();
////                        GeetolUtils.payOrder(GeetolUtils.getPidByGoodName("制作音乐相册")
////                                , "zfb", new YuanliPayListener() {
////                                    @Override
////                                    public void onSuccess() {
////                                    }
////
////                                    @Override
////                                    public void onFail(int errCode, Exception e) {
////                                        Log.e("LogUtils ", "wx  onFail");
////                                    }
////                                });
////                        GeetolUtils.payOrder(GeetolUtils.getPidByGoodName("制作音乐相册")
////                                , "zfb", remarkMap, new YuanliPayListener() {
////                                    @Override
////                                    public void onSuccess() {
////                                    }
////
////                                    @Override
////                                    public void onFail(int errCode, Exception e) {
////                                        Log.e("LogUtils ", "wx  onFail");
////                                    }
////                                });
////                        GeetolUtils.payOrderGeetol(GeetolUtils.getPidByGoodName("一个月VIP")
////                                , "zfb", "18573676330"
////                                , new YuanliPayListener() {
////                                    @Override
////                                    public void onSuccess() {
////                                    }
////
////                                    @Override
////                                    public void onFail(int errCode, Exception e) {
////                                        Log.e("LogUtils ", "wx  onFail");
////                                    }
////                                });
////
//
////                        GeetolUtils.payOrderGeetol(GeetolUtils.getPidByGoodName("制作音乐相册"), "zfb"
////                                , "13555558888", new YuanliPayListener() {
////                                    @Override
////                                    public void onSuccess() {
////
////                                    }
////
////                                    @Override
////                                    public void onFail(int errCode, Exception e) {
////
////                                    }
////                                });
//
////                        GeetolUtils.payOrderYuanli("制作音乐相册", "zfb"
////                                , "8.8","13888888888", new YuanliPayListener() {
////                                    @Override
////                                    public void onSuccess() {
////
////                                    }
////
////                                    @Override
////                                    public void onFail(int errCode, Exception e) {
////
////                                    }
////                                });
////
//                        GeetolUtils.payOrderYuanli("制作音乐相册", "zfb"
//                                , "8.8", prepearRemarkMap, updateRemarkMap, new YuanliPayListener() {
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
////
////                        GeetolUtils.payOrderYuanli("一个月VIP", "zfb"
////                                , "13888888888", "8.8"
////                                , new YuanliPayListener() {
////                                    @Override
////                                    public void onSuccess() {
////
////                                    }
////
////                                    @Override
////                                    public void onFail(int errCode, Exception e) {
////
////                                    }
////                                });
////                        GeetolUtils.payOrderYuanli("一个月VIP", "zfb"
////                                , "13888888888", "8.8", remarkMap
////                                , new YuanliPayListener() {
////                                    @Override
////                                    public void onSuccess() {
////
////                                    }
////
////                                    @Override
////                                    public void onFail(int errCode, Exception e) {
////
////                                    }
////                                });
////                        GeetolUtils.payOrder(GeetolUtils.getPidByGoodName("一个月VIP")
////                                , "zfb", "18573676330"
////                                , remarkMap, new YuanliPayListener() {
////                                    @Override
////                                    public void onSuccess() {
////                                    }
////
////                                    @Override
////                                    public void onFail(int errCode, Exception e) {
////                                        Log.e("LogUtils ", "wx  onFail");
////                                    }
////                                });
//                    }
//
//                    @Override
//                    public void onFail(int errCode, Exception e) {
//                        Log.e("LogUtils ", "startSDK  onFail");
//                    }
//                });
//
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
        GeetolUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("LogUtils", "requestReturn  ");
    }

    public void requestPermission(String permissionName) {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                permissionName) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permissionName}, 1);
        } else {
            com.zzn.geetolsdk.yuanlilib.http.HttpUtils.getInstance().postRegister(
                    new BaseCallback<ResultBean>() {
                        @Override
                        public void onRequestBefore() {
                        }

                        @Override
                        public void onFailure(Request request, Exception e) {
                            ToastUtils.showShortToast("注册失败");
                        }

                        @Override
                        public void onSuccess(Response response, ResultBean resultBean) {
                        }

                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            ToastUtils.showShortToast("注册失败");
                        }
                    });
        }
    }
}

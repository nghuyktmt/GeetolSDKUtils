package com.gtdev5.geetolsdk.mylibrary.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.gtdev5.geetolsdk.mylibrary.beans.Ads;
import com.gtdev5.geetolsdk.mylibrary.beans.Advert;
import com.gtdev5.geetolsdk.mylibrary.beans.ApliyBean;
import com.gtdev5.geetolsdk.mylibrary.beans.Gds;
import com.gtdev5.geetolsdk.mylibrary.beans.GetNewBean;
import com.gtdev5.geetolsdk.mylibrary.beans.Good;
import com.gtdev5.geetolsdk.mylibrary.beans.OdResultBean;
import com.gtdev5.geetolsdk.mylibrary.beans.PayBean;
import com.gtdev5.geetolsdk.mylibrary.beans.ResultBean;
import com.gtdev5.geetolsdk.mylibrary.beans.UpdateBean;
import com.gtdev5.geetolsdk.mylibrary.callback.BaseCallback;
import com.gtdev5.geetolsdk.mylibrary.callback.PayListener;
import com.gtdev5.geetolsdk.mylibrary.callback.UpdateDataListener;
import com.gtdev5.geetolsdk.mylibrary.http.HttpUtils;
import com.gtdev5.geetolsdk.mylibrary.initialization.GeetolSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Walter on 2018/11/8.
 */

public class GeetolUtils {
    @SuppressLint("StaticFieldLeak")
    private static Activity mactivity;
    public static GeetolUtils geetolUtils;
    private static Gson gson;
    public String appName = "测试";


    public static GeetolUtils initSDK(Activity activity) {
        mactivity = activity;
        GeetolSDK.init(activity);
        if (geetolUtils == null) {
            geetolUtils = new GeetolUtils();
        }
        return geetolUtils;
    }

    public void startSDK(String appName, String channelName
            , UpdateDataListener dataListener) {
        this.appName = appName;
        gson = new Gson();
        if (SpUtils.getInstance().getBoolean("isFirst", true)) {
            requestPermission(Manifest.permission.READ_PHONE_STATE);
            channelStatistics(appName, channelName);
            SpUtils.getInstance().putBoolean("isFirst", false);
        }
        activateStatistics();
        checkVersion();
        updateData(dataListener);
    }


    public static void pay(int goodId, String payType, PayListener listener) {
        if (payType.equals("wx")) {
            HttpUtils.getInstance().PostOdOrder(1, goodId, 0, 1
                    , new BaseCallback<OdResultBean>() {
                        @Override
                        public void onRequestBefore() {

                        }

                        @Override
                        public void onFailure(Request request, Exception e) {
                            ToastUtils.showShortToast("微信支付失败");
                        }

                        @Override
                        public void onSuccess(Response response, OdResultBean odResultBean) {
                            listener.onSuccess(new PayBean(odResultBean));
                            ToastUtils.showShortToast("微信支付成功");

                        }


                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            ToastUtils.showShortToast("微信支付失败");
                        }
                    });
        }
        if (payType.equals("zfb")) {
            HttpUtils.getInstance().PostOdOrder(1, goodId, 0, 2
                    , new BaseCallback<ApliyBean>() {
                        @Override
                        public void onRequestBefore() {

                        }

                        @Override
                        public void onFailure(Request request, Exception e) {
                            ToastUtils.showShortToast("支付宝支付成功");

                        }

                        @Override
                        public void onSuccess(Response response, ApliyBean apliyBean) {
                            listener.onSuccess(new PayBean(apliyBean));
                            ToastUtils.showShortToast("支付宝支付成功");

                        }


                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            ToastUtils.showShortToast("支付宝支付成功");

                        }
                    });
        }
    }

    private void updateData(UpdateDataListener dataListener) {
        HttpUtils.getInstance().postUpdate(new BaseCallback<UpdateBean>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                ToastUtils.showShortToast("数据更新失败");
            }

            @Override
            public void onSuccess(Response response, UpdateBean updateBean) {
                ToastUtils.showShortToast("数据更新成功");
                Advert adverts = new Advert();
                if (updateBean == null || updateBean.getAds() == null || updateBean.getGds() == null)
                    return;
                for (int i = 0; i < updateBean.getAds().size(); i++) {
                    Ads ads = updateBean.getAds().get(i);
                    if ("welcome".equals(ads.getName())) {
                        adverts.setWelcome(
                                new Advert.Img(ads.getLink(), ads.getName(), ads.getImg()));
                        SpUtils.getInstance().putString("welcome"
                                , gson.toJson(adverts.getWelcome()));
                    }
                    if ("banner".equals(ads.getName())) {
                        adverts.getBanners().add(
                                new Advert.Img(ads.getLink(), ads.getName(), ads.getImg()));
                    }
                }
                SpUtils.getInstance().putString("banner"
                        , gson.toJson(adverts.getBanners()));
                ArrayList<Good> goods = new ArrayList<>();
                for (int i = 0; i < updateBean.getGds().size(); i++) {
                    Gds gds = updateBean.getGds().get(i);
                    goods.add(new Good(gds.getName(), gds.getPrice(), gds.getXwprice()
                            , gds.getOriginal(), gds.getPayway(), gds.getGid()));
                }
                SpUtils.getInstance().putString("good", gson.toJson(goods));

                String contact = updateBean.getContract().toString();
                String hpUrl = updateBean.getHpurl();

                dataListener.onSuccess(adverts, goods, contact, hpUrl);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                ToastUtils.showShortToast("数据更新失败");
                dataListener.onFail(errorCode, e);
            }
        });
    }

    private void checkVersion() {
        HttpUtils.getInstance().postNews(new BaseCallback<GetNewBean>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                ToastUtils.showShortToast("新版本检测失败");
            }

            @Override
            public void onSuccess(Response response, GetNewBean getNewBean) {
                ToastUtils.showShortToast("新版本检测成功    " + getNewBean.toString());
                if (getNewBean.isIssucc()) {
                    if (getNewBean.getVercode() > SystemUtils.getSystemVersionCode(mactivity)) {
                        DownLoadUtils.showDownLoadDialog(mactivity, getNewBean);
                    }
                }
            }


            @Override
            public void onError(Response response, int errorCode, Exception e) {
                ToastUtils.showShortToast("新版本检测失败");
            }
        });
    }

    private void activateStatistics() {
        String client_type = Build.BRAND;
        @SuppressLint("HardwareIds") String pid = Settings.Secure.getString(
                mactivity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        HashMap<String, String> map = new HashMap<>();
        map.put("appname", appName);
        map.put("Client_type", client_type);
        map.put("pid", pid);
        HttpUtils.getInstance().get("http://101.37.76.151:1013/API/Insert_Activation.aspx?"
                + GsonUtils.mapToUrl(map), new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {
                ToastUtils.showShortToast("激活统计失败");
            }

            @Override
            public void onSuccess(Response response, String s) {
                Log.e("LogUtils", "激活统计成功   " + s);
                ToastUtils.showShortToast("激活统计成功   " + s);
            }


            @Override
            public void onError(Response response, int errorCode, Exception e) {
                ToastUtils.showShortToast("激活统计失败");
            }
        });
    }

    private void channelStatistics(String appName, String channelName) {
        HashMap<String, String> map = new HashMap<>();
        map.put("appname", appName);
        map.put("StoreName", channelName == null ? "" : channelName);
        HttpUtils.getInstance().get("http://101.37.76.151:1013/API/DownLoad_Open.aspx?" + GsonUtils.mapToUrl(map)
                , new BaseCallback<String>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        ToastUtils.showShortToast("渠道统计失败");

                    }

                    @Override
                    public void onSuccess(Response response, String s) {
                        Log.e("LogUtils", "渠道统计成功   " + s);

                        ToastUtils.showShortToast("渠道统计成功    " + s);

                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        ToastUtils.showShortToast("渠道统计失败");
                    }
                });
    }

    private void requestPermission(String permissionName) {
        if (ContextCompat.checkSelfPermission(mactivity,
                permissionName) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(mactivity, new String[]{permissionName}, 1);
        } else {
            com.gtdev5.geetolsdk.mylibrary.http.HttpUtils.getInstance().postRegister(
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
                            ToastUtils.showShortToast("注册成功    " + resultBean.toString());
                        }

                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            ToastUtils.showShortToast("注册失败");
                        }
                    });
        }
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            com.gtdev5.geetolsdk.mylibrary.http.HttpUtils.getInstance().postRegister(
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
                            ToastUtils.showShortToast("注册成功    " + resultBean.toString());
                        }

                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            ToastUtils.showShortToast("注册失败");
                        }
                    });
        } else {
            ToastUtils.showLongToast("已经拒绝权限，请重新安装后同意该权限");
        }
    }


    public static Advert.Img getWelcome() {
        Advert.Img welcome = gson.fromJson(
                SpUtils.getInstance().getString("welcome"), Advert.Img.class);
        return welcome;
    }

    public static ArrayList<Advert.Img> getBanner() {
        ArrayList<Advert.Img> banners = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(SpUtils.getInstance().getString("banner"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                banners.add(gson.fromJson(object.toString(), Advert.Img.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return banners;
    }

    public static ArrayList<Good> getGoods() {
        ArrayList<Good> goods = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(SpUtils.getInstance().getString("good"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                goods.add(gson.fromJson(object.toString(), Good.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goods;
    }

    public static Good getGoodByPid(int pid) {
        Good good = new Good();
        try {
            JSONArray array = new JSONArray(SpUtils.getInstance().getString("good"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (object.getInt("goodId") == pid) {
                    good = gson.fromJson(object.toString(), Good.class);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return good;

    }

    public static Good getGoodByName(String name) {
        Good good = new Good();
        int pid = getPidByGoodName(name);
        try {
            JSONArray array = new JSONArray(SpUtils.getInstance().getString("good"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (object.getInt("goodId") == pid) {
                    good = gson.fromJson(object.toString(), Good.class);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return good;

    }

    public static int getPidByGoodName(String name) {
        try {
            JSONArray array = new JSONArray(SpUtils.getInstance().getString("good"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (object.getString("name").equals(name)) {
                    return object.getInt("goodId");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

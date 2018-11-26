package com.gtdev5.geetolsdk.mylibrary.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.gtdev5.geetolsdk.mylibrary.beans.Ads;
import com.gtdev5.geetolsdk.mylibrary.beans.Advert;
import com.gtdev5.geetolsdk.mylibrary.beans.ApliyBean;
import com.gtdev5.geetolsdk.mylibrary.beans.Contract;
import com.gtdev5.geetolsdk.mylibrary.beans.Gds;
import com.gtdev5.geetolsdk.mylibrary.beans.GetNewBean;
import com.gtdev5.geetolsdk.mylibrary.beans.Good;
import com.gtdev5.geetolsdk.mylibrary.beans.OdResultBean;
import com.gtdev5.geetolsdk.mylibrary.beans.ResultBean;
import com.gtdev5.geetolsdk.mylibrary.beans.UpdateBean;
import com.gtdev5.geetolsdk.mylibrary.callback.BaseCallback;
import com.gtdev5.geetolsdk.mylibrary.callback.UpdateDataListener;
import com.gtdev5.geetolsdk.mylibrary.callback.YuanliPayListener;
import com.gtdev5.geetolsdk.mylibrary.http.HttpUtils;
import com.gtdev5.geetolsdk.mylibrary.initialization.GeetolSDK;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
    private static IWXAPI msgapi;


    public static void initSDK(Context context) {
        GeetolSDK.init(context);
        msgapi = WXAPIFactory.createWXAPI(context, CPResourceUtils.getString("wxId"));
        msgapi.registerApp(CPResourceUtils.getString("wxId"));
    }

    public static GeetolUtils getSDK(Activity activity) {
        mactivity = activity;
        if (geetolUtils == null) {
            geetolUtils = new GeetolUtils();
        }
        return geetolUtils;
    }

    public static IWXAPI getMsgApi() {
        return msgapi;
    }

    public void startSDK(String channelName
            , UpdateDataListener dataListener) {
        gson = new Gson();
        if (SpUtils.getInstance().getBoolean("isFirst", true)) {
            requestPermission(Manifest.permission.READ_PHONE_STATE);
            channelStatistics(CPResourceUtils.getString("yuanli_app_name"), channelName);
            SpUtils.getInstance().putBoolean("isFirst", false);
        }
        activateStatistics();
        checkVersion();
        updateData(dataListener);
    }

    public static void payOrder(int goodId, String payType, YuanliPayListener listener) {
        payOrder(goodId, payType, "", null, listener);
    }

    public static void payOrder(int goodId, String payType, String phoneNum, YuanliPayListener listener) {
        payOrder(goodId, payType, phoneNum, null, listener);
    }

    public static void payOrder(int goodId, String payType, HashMap<String, String> remarkMap, YuanliPayListener listener) {
        payOrder(goodId, payType, "", remarkMap, listener);
    }

    public static void payOrder(int goodId, String payType, String phoneNum, HashMap<String, String> remarkMap, YuanliPayListener listener) {
        HashMap<String, String> yuanliMap = new HashMap<>();
        String pay_type = "支付宝官方支付";
        String price = "88.88";
        String commodity = getGoodByPid(goodId).getName();
        if ("wx".equals(payType)) {
            pay_type = "微信官方支付";
            price = getGoodByPid(goodId).getWxPrice();
        }
        if ("zfb".equals(payType)) {
            pay_type = "支付宝官方支付";
            price = getGoodByPid(goodId).getZfbPrice();
        }

        yuanliMap.put("user_phone", phoneNum);
        yuanliMap.put("pay_type", pay_type);
        yuanliMap.put("price", price);
        yuanliMap.put("app_name", CPResourceUtils.getString("yuanli_app_name"));
        yuanliMap.put("commodity", (commodity.contains("VIP")) ? "开通VIP" : commodity);
        HttpUtils.doAsk("http://101.37.76.151:8045/CoolAlbumWeChatpay/PreOrder"
                , Utils.getStringByMap(yuanliMap), new HttpUtils.HttpListener() {
                    @Override
                    public void success(String result) {
                        String orderNum = "8888";
                        try {
                            JSONObject object = new JSONObject(result);
                            orderNum = object.getString("Orderno");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pay(goodId, orderNum, payType, remarkMap, listener);
                    }

                    @Override
                    public void error(String result) {
                        listener.onFail(8045, null);
                    }
                });
    }

    private static void pay(int goodId, String orderNum, String payType, HashMap<String, String> remarkMap, YuanliPayListener listener) {
        if (payType.equals("wx")) {
            HttpUtils.getInstance().PostOdOrder(1, goodId, 0, 1
                    , new BaseCallback<OdResultBean>() {
                        @Override
                        public void onRequestBefore() {

                        }

                        @Override
                        public void onFailure(Request request, Exception e) {
                            listener.onFail(139, e);
                        }

                        @Override
                        public void onSuccess(Response response, OdResultBean odResultBean) {
                            PayUtils.getPay(mactivity).goPay(odResultBean, orderNum, remarkMap, listener);
                        }


                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            listener.onFail(errorCode, e);
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
                            listener.onFail(165, e);
                        }

                        @Override
                        public void onSuccess(Response response, ApliyBean apliyBean) {
                            PayUtils.getPay(mactivity).goPay(apliyBean, orderNum, remarkMap, listener);
                        }


                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            listener.onFail(errorCode, e);
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
                dataListener.onFail(192, e);
            }

            @Override
            public void onSuccess(Response response, UpdateBean updateBean) {
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
                            , gds.getOriginal(), gds.getPayway(), gds.getGid(), gds.getRemark()));
                }
                SpUtils.getInstance().putString("good", gson.toJson(goods));

                String contact = gson.toJson(updateBean.getContract());
                String hpUrl = updateBean.getHpurl();
                SpUtils.getInstance().putString("contact", contact);
                SpUtils.getInstance().putString("hpUrl", hpUrl);

                dataListener.onSuccess();
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
        map.put("appname", CPResourceUtils.getString("yuanli_app_name"));
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

    public static Contract getContact() {
        Contract contract = null;
        try {
            contract = gson.fromJson(SpUtils.getInstance().getString("contact", ""), Contract.class);
        } catch (Exception e) {

        }
        return contract;
    }

    public static String getHpUrl() {
        return SpUtils.getInstance().getString("hpUrl", "");
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

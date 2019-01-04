package com.zzn.geetolsdk.yuanlilib.util;

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
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zzn.geetolsdk.yuanlilib.beans.Ads;
import com.zzn.geetolsdk.yuanlilib.beans.Advert;
import com.zzn.geetolsdk.yuanlilib.beans.ApliyBean;
import com.zzn.geetolsdk.yuanlilib.beans.Contract;
import com.zzn.geetolsdk.yuanlilib.beans.Gds;
import com.zzn.geetolsdk.yuanlilib.beans.GetNewBean;
import com.zzn.geetolsdk.yuanlilib.beans.Good;
import com.zzn.geetolsdk.yuanlilib.beans.OdResultBean;
import com.zzn.geetolsdk.yuanlilib.beans.ResultBean;
import com.zzn.geetolsdk.yuanlilib.beans.Swt;
import com.zzn.geetolsdk.yuanlilib.beans.UpdateBean;
import com.zzn.geetolsdk.yuanlilib.callback.BaseCallback;
import com.zzn.geetolsdk.yuanlilib.callback.UpdateDataListener;
import com.zzn.geetolsdk.yuanlilib.callback.YuanliPayListener;
import com.zzn.geetolsdk.yuanlilib.http.HttpUtils;
import com.zzn.geetolsdk.yuanlilib.initialization.GeetolSDK;

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
        try {
            msgapi = WXAPIFactory.createWXAPI(context, CPResourceUtils.getString("wxId"));
            msgapi.registerApp(CPResourceUtils.getString("wxId"));
        } catch (Exception e) {
            msgapi = WXAPIFactory.createWXAPI(context, "id不存在");
            msgapi.registerApp("id不存在");
        }

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

    public static void payOrderGeetol(int goodId, String payType, YuanliPayListener listener) {
        payOrderGeetol(goodId, payType, "", null, null, listener);
    }

    public static void payOrderGeetol(int goodId, String payType, String phoneNum, YuanliPayListener listener) {
        payOrderGeetol(goodId, payType, phoneNum, null, null, listener);
    }

    public static void payOrderGeetol(int goodId, String payType, HashMap<String, String> prepareRemarkMap
            , HashMap<String, String> updateRemarkMap, YuanliPayListener listener) {
        payOrderGeetol(goodId, payType, "", prepareRemarkMap, updateRemarkMap, listener);
    }

    public static void payOrderGeetol(int goodId, String payType, String phoneNum
            , HashMap<String, String> prepareRemarkMap, HashMap<String, String> updateRemarkMap, YuanliPayListener listener) {
        if (payType.equals("wx")) {
            HttpUtils.getInstance().PostOdOrderGeetol(1, goodId, 0, 1
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
                            Log.e("odResultBean   ", odResultBean.toString());
                            String orderNo = odResultBean.getOrder_no();
                            if (orderNo == null || orderNo.equals("") || orderNo.equals("null"))
                                orderNo = odResultBean.getNo();
                            payGeetol(goodId, payType, orderNo, phoneNum, odResultBean, prepareRemarkMap
                                    , updateRemarkMap, listener);

                        }


                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            listener.onFail(errorCode, e);
                        }
                    });
        }
        if (payType.equals("zfb")) {
            HttpUtils.getInstance().PostOdOrderGeetol(1, goodId, 0, 2
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
                            String orderNo = apliyBean.getOrder_no();
                            if (orderNo == null || orderNo.equals("") || orderNo.equals("null"))
                                orderNo = apliyBean.getNo();
                            payGeetol(goodId, payType, orderNo, phoneNum, apliyBean, prepareRemarkMap
                                    , updateRemarkMap, listener);

                        }


                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            listener.onFail(errorCode, e);
                        }
                    });
        }
    }

    public static void payOrderYuanli(String goodTitle, String payType, String price
            , YuanliPayListener listener) {
        payOrderYuanli(goodTitle, payType, "", price, null, null, listener);
    }


    public static void payOrderYuanli(String goodTitle, String payType, String phoneNum, String price
            , YuanliPayListener listener) {
        payOrderYuanli(goodTitle, payType, phoneNum, price, null, null, listener);
    }

    public static void payOrderYuanli(String goodTitle, String payType, String price
            , HashMap<String, String> prepearRemarkMap, HashMap<String, String> updateRemarkMap, YuanliPayListener listener) {
        payOrderYuanli(goodTitle, payType, "", price, prepearRemarkMap, updateRemarkMap, listener);
    }

    public static void payOrderYuanli(String goodTitle, String payType, String phoneNum, String price
            , HashMap<String, String> prepearRemarkMap, HashMap<String, String> updateRemarkMap, YuanliPayListener listener) {
        String remark = prepearRemarkMap == null ? null : prepearRemarkMap.get("remark");
        if (payType.equals("wx")) {
            HttpUtils.getInstance().PostOdOrderYuanli(goodTitle, 1, price
                    , remark != null && !remark.equals("") ? "" : remark
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
                            Log.e("odResultBean   ", odResultBean.toString());
                            String orderNo = odResultBean.getNo();
                            if (orderNo == null || orderNo.equals("") || orderNo.equals("null"))
                                orderNo = odResultBean.getNo();
                            payYuanli(goodTitle, payType, orderNo, phoneNum, odResultBean
                                    , price, prepearRemarkMap, updateRemarkMap, listener);

                        }


                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            listener.onFail(errorCode, e);
                        }
                    });
        }
        if (payType.equals("zfb")) {
            HttpUtils.getInstance().PostOdOrderYuanli(goodTitle, 2, price
                    , remark != null && !remark.equals("") ? "" : remark
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
                            String orderNo = apliyBean.getNo();
                            Log.e("apliyBean   ", apliyBean.toString());
                            if (orderNo == null || orderNo.equals("") || orderNo.equals("null"))
                                orderNo = apliyBean.getNo();
                            payYuanli(goodTitle, payType, orderNo, phoneNum, apliyBean
                                    , price, prepearRemarkMap, updateRemarkMap, listener);
                        }

                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            listener.onFail(errorCode, e);
                        }
                    });
        }
    }

    private static int askCount = 0;

    private static void payYuanli(String goodTitle, String payType, String orderNum, String phoneNum
            , Object obj, String price, HashMap<String, String> prepearRemarkMap,
                                  HashMap<String, String> updateRemarkMap, YuanliPayListener listener) {

        HashMap<String, String> yuanliMap = new HashMap<>();
        String pay_type = "支付宝官方支付";
        if ("wx".equals(payType)) {
            pay_type = "微信官方支付";
        }
        if ("zfb".equals(payType)) {
            pay_type = "支付宝官方支付";
        }


        yuanliMap.put("user_phone", phoneNum);
        yuanliMap.put("pay_type", pay_type);
        yuanliMap.put("price", price);
        yuanliMap.put("Version", CPResourceUtils.getString("version"));
        yuanliMap.put("order_no", orderNum);
        yuanliMap.put("Client_Id", SystemUtils.getClientId(mactivity));
        yuanliMap.put("app_name", CPResourceUtils.getString("yuanli_app_name"));
        yuanliMap.put("commodity", (goodTitle.contains("VIP")) ? "开通VIP" : goodTitle);
        if (prepearRemarkMap != null && prepearRemarkMap.size() > 0)
            yuanliMap.putAll(prepearRemarkMap);

        HttpUtils.doAsk("http://101.37.76.151:8045/NewOrder/PreOrder"
                , Utils.getStringByMap(yuanliMap), new HttpUtils.HttpListener() {
                    @Override
                    public void success(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getString("code") == null
                                    || object.getString("code").equals("")
                                    || object.getString("code").equals("0")) {
                                askCount++;
                                if (askCount >= 4) {
                                    listener.onFail(500, null);
                                    return;
                                }
                                payYuanli(goodTitle, payType, orderNum, phoneNum, obj
                                        , price, prepearRemarkMap, updateRemarkMap, listener);
                            } else if (object.getString("code").equals("1")) {
                                mactivity.runOnUiThread(() -> PayUtils.getPay(mactivity).goPay(obj, orderNum
                                        , updateRemarkMap, listener));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(String result) {
                        listener.onFail(8045, null);
                    }
                });
    }

    private static void payGeetol(int goodId, String payType, String orderNum, String phoneNum
            , Object obj, HashMap<String, String> prepearRemarkMap, HashMap<String, String> updateRemarkMap, YuanliPayListener listener) {

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
        yuanliMap.put("Version", CPResourceUtils.getString("version"));
        yuanliMap.put("order_no", orderNum);
        yuanliMap.put("app_name", CPResourceUtils.getString("yuanli_app_name"));
        yuanliMap.put("commodity", (commodity.contains("VIP")) ? "开通VIP" : commodity);
        if (prepearRemarkMap != null && prepearRemarkMap.size() > 0)
            yuanliMap.putAll(prepearRemarkMap);
        HttpUtils.doAsk("http://101.37.76.151:8045/NewOrder/PreOrder"
                , Utils.getStringByMap(yuanliMap), new HttpUtils.HttpListener() {
                    @Override
                    public void success(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getString("code") == null
                                    || object.getString("code").equals("")
                                    || object.getString("code").equals("0")) {
                                payGeetol(goodId, payType, orderNum, phoneNum
                                        , obj, prepearRemarkMap
                                        , updateRemarkMap, listener);
                            } else if (object.getString("code").equals("1")) {
                                mactivity.runOnUiThread(() -> PayUtils.getPay(mactivity)
                                        .goPay(obj, orderNum, updateRemarkMap, listener));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(String result) {
                        listener.onFail(8045, null);
                    }
                });
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
                Log.e("onSuccess ", "updateBean  " + updateBean.toString());
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
                            , gds.getOriginal(), gds.getPayway(), gds.getGid(), gds.getValue()
                            , gds.getRemark()));
                }
                SpUtils.getInstance().putString("good", gson.toJson(goods));

                SpUtils.getInstance().putString("swt", gson.toJson(updateBean.getSwt()));

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

    public static void requestPermission(String permissionName) {
        if (ContextCompat.checkSelfPermission(mactivity,
                permissionName) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(mactivity, new String[]{permissionName}, 1);
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

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    public static ArrayList<Swt> getSwts() {
        ArrayList<Swt> swts = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(SpUtils.getInstance().getString("swt"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                swts.add(gson.fromJson(object.toString(), Swt.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return swts;
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

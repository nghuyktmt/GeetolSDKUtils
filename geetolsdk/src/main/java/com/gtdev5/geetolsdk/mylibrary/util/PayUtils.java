package com.gtdev5.geetolsdk.mylibrary.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.gtdev5.geetolsdk.mylibrary.beans.AliResultBean;
import com.gtdev5.geetolsdk.mylibrary.beans.ApliyBean;
import com.gtdev5.geetolsdk.mylibrary.beans.OdResultBean;
import com.gtdev5.geetolsdk.mylibrary.callback.YuanliPayListener;
import com.gtdev5.geetolsdk.mylibrary.http.HttpUtils;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Walter on 2018/11/14.
 */

public class PayUtils {
    private static PayUtils payUtils;
    private static Activity mActivity;
    private YuanliPayListener listener;
    private String orderNum = "7777";

    public static PayUtils getPay(Activity activity) {
        mActivity = activity;
        if (payUtils == null)
            payUtils = new PayUtils();
        return payUtils;
    }

    public void goPay(Object object, String orderNum, YuanliPayListener listener) {
        this.listener = listener;
        this.orderNum = orderNum;
        if (object instanceof OdResultBean) {
            OdResultBean payBean = (OdResultBean) object;
            payWx(payBean);
        }
        if (object instanceof ApliyBean) {
            ApliyBean payBean = (ApliyBean) object;
            payAli(payBean);
        }
    }

    private void payWx(OdResultBean payBean) {
        IWXAPI msgApi = GeetolUtils.getMsgApi();
        Runnable payRunnable = () -> {
            PayReq request = new PayReq();
            request.appId = payBean.getAppid();
            request.partnerId = payBean.getPartnerId();
            request.prepayId = payBean.getPrepayid();
            request.packageValue = "Sign=WXPay";
            request.nonceStr = payBean.getNonce_str();
            request.timeStamp = payBean.getTimestramp();
            request.sign = payBean.getSign();
            msgApi.sendReq(request);
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void payAli(ApliyBean payBean) {
        new Thread(() -> {
            PayTask alipay = new PayTask(mActivity);
            Map<String, String> resultMap = alipay.payV2(payBean.getPackage_str(), true);
            Message msg = new Message();
            msg.what = 200;
            msg.obj = resultMap;
            mHandler.sendMessage(msg);
        }).start();

    }

    public void onRes(BaseResp resp) {
        if (listener == null)
            return;
        if (resp.errCode == 0) {
            synchroData();
        } else {
            listener.onFail(resp.errCode, null);
        }
    }

    public void synchroData() {
        HashMap<String, String> updataMap = new HashMap<>();
        updataMap.put("order_no", orderNum);
        HttpUtils.doAsk("http://101.37.76.151:8045/CoolAlbumWeChatpay/updatestate"
                , Utils.getStringByMap(updataMap), new HttpUtils.HttpListener() {
                    @Override
                    public void success(String result) {
                        listener.onSuccess();
                    }

                    @Override
                    public void error(String result) {
                        listener.onFail(104,null);
                    }
                });
    }

    private MyHandler mHandler = new MyHandler();

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<String, String> result = (Map<String, String>) msg.obj;
            if (msg.what == 200 && result.get("result") != null && !result.get("result").equals("")) {
                Gson gson = new Gson();
                AliResultBean ali = gson.fromJson(result.get("result"), AliResultBean.class);
                if (ali.getAlipay_trade_app_pay_response().getCode().equals("9000")
                        || ali.getAlipay_trade_app_pay_response().getCode().equals("10000")) {
                    synchroData();
                } else {
                    listener.onFail(Integer.parseInt(ali.getAlipay_trade_app_pay_response().getCode())
                            , null);
                }
            }
        }
    }
}

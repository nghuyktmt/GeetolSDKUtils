package com.gtdev5.geetolsdk.mylibrary.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gtdev5.geetolsdk.mylibrary.util.GeetolUtils;
import com.gtdev5.geetolsdk.mylibrary.util.PayUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Administrator on 2017/11/8.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = GeetolUtils.getMsgApi();
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {
        PayUtils.getPay(WXPayEntryActivity.this).onRes(resp);
        WXPayEntryActivity.this.finish();
    }


}

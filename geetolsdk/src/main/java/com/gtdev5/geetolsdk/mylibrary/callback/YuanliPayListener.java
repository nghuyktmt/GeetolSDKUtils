package com.gtdev5.geetolsdk.mylibrary.callback;

/**
 * Created by Walter on 2018/11/8.
 */

public abstract class YuanliPayListener {
    public abstract void onSuccess();

    public abstract void onFail(int errCode,Exception e);
}

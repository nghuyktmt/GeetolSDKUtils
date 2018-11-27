package com.zzn.geetolsdk.yuanlilib.callback;

import com.zzn.geetolsdk.yuanlilib.beans.PayBean;

/**
 * Created by Walter on 2018/11/8.
 */

public abstract class PayListener {
    public abstract void onSuccess(PayBean payBean);

    public abstract void onFail(int errCode,Exception e);
}

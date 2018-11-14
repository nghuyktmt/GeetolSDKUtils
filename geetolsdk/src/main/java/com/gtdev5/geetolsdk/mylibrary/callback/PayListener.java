package com.gtdev5.geetolsdk.mylibrary.callback;

import com.gtdev5.geetolsdk.mylibrary.beans.PayBean;

/**
 * Created by Walter on 2018/11/8.
 */

public abstract class PayListener {
    public abstract void onSuccess(PayBean payBean);

    public abstract void onFail(int errCode,Exception e);
}

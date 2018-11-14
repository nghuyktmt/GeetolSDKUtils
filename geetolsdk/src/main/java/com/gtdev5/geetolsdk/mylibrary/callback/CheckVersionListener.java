package com.gtdev5.geetolsdk.mylibrary.callback;

import com.gtdev5.geetolsdk.mylibrary.beans.GetNewBean;

/**
 * Created by Walter on 2018/11/8.
 */

public abstract class CheckVersionListener {
    public abstract void onSuccess(GetNewBean getNewBean);

    public abstract void onFail(int errCode,Exception e);
}

package com.zzn.geetolsdk.yuanlilib.callback;

import com.zzn.geetolsdk.yuanlilib.beans.GetNewBean;

/**
 * Created by Walter on 2018/11/8.
 */

public abstract class CheckVersionListener {
    public abstract void onSuccess(GetNewBean getNewBean);

    public abstract void onFail(int errCode,Exception e);
}

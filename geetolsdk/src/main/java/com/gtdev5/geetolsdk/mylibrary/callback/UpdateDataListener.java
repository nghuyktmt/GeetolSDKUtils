package com.gtdev5.geetolsdk.mylibrary.callback;

import com.gtdev5.geetolsdk.mylibrary.beans.Advert;
import com.gtdev5.geetolsdk.mylibrary.beans.Good;

import java.util.ArrayList;

/**
 * Created by Walter on 2018/11/8.
 */

public abstract class UpdateDataListener {
    public abstract void onSuccess(Advert adverts, ArrayList<Good> goods,String contact,String hpUrl);

    public abstract void onFail(int errCode, Exception e);
}

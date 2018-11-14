package com.gtdev5.geetolsdk.mylibrary.initialization;

import android.content.Context;

import com.gtdev5.geetolsdk.mylibrary.util.CPResourceUtils;
import com.gtdev5.geetolsdk.mylibrary.util.MapUtils;
import com.gtdev5.geetolsdk.mylibrary.util.SpUtils;
import com.gtdev5.geetolsdk.mylibrary.util.ToastUtils;

/**
 * Created by cheng
 * PackageName ModelTest
 * 2018/1/6 10:33
 *      所有东西初始化
 */

public class GeetolSDK {
    public static String TAG = "GeetolSDK";

    private static Context mContext;

    public static void init(Context context){
        try {
            if (mContext == null){
                mContext = context;
            }
            SpUtils.getInstance().init(mContext);
            CPResourceUtils.init(mContext);
            ToastUtils.init(mContext);
            MapUtils.init(mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

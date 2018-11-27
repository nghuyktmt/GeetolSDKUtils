package com.zzn.geetolsdk.yuanlilib.initialization;

import android.content.Context;

import com.zzn.geetolsdk.yuanlilib.util.CPResourceUtils;
import com.zzn.geetolsdk.yuanlilib.util.MapUtils;
import com.zzn.geetolsdk.yuanlilib.util.SpUtils;
import com.zzn.geetolsdk.yuanlilib.util.ToastUtils;

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

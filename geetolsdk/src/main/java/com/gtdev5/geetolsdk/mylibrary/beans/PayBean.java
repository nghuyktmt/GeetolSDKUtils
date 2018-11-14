package com.gtdev5.geetolsdk.mylibrary.beans;

import com.gtdev5.geetolsdk.mylibrary.util.ToastUtils;

/**
 * Created by Walter on 2018/11/8.
 */

public class PayBean {
    public ApliyBean aliBean;
    public OdResultBean wxBean;

    public PayBean(ApliyBean aliBean) {
        this.aliBean = aliBean;
    }

    public PayBean(OdResultBean wxBean) {
        this.wxBean = wxBean;
    }

    public void setBean(ApliyBean bean) {
        this.aliBean = bean;
    }

    public void setBean(OdResultBean bean) {
        this.wxBean = bean;
    }

    public Object getBean(String payType) {
        if (wxBean == null && aliBean == null) {
            ToastUtils.showLongToast("类型错误");
            return null;
        }
        if ("wx".equals(payType)) {
            if (wxBean != null) {
                aliBean = null;
                return wxBean;
            }
        }
        if ("zfb".equals(payType)) {
            if (aliBean != null) {
                wxBean = null;
                return aliBean;
            }
        }
        return null;

    }
}

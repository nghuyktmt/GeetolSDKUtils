package com.zzn.geetolsdk.yuanlilib.beans;

import java.io.Serializable;

/**
 * Created by cheng
 * PackageName ModelTest
 * 2018/1/26 13:57
 */

public class ApliyBean implements Serializable {
    private boolean issucc;
    private String msg;
    private String code;
    private String appid;
    private float amount;
    private String package_str;
    //    这个是商品信息在原力后台获取的时候使用的
    private String no;
    //    这个是商品信息在集拓后台获取的时候使用的
    private String order_no;


    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public boolean isIssucc() {
        return issucc;
    }

    public void setIssucc(boolean issucc) {
        this.issucc = issucc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPackage_str() {
        return package_str;
    }

    public void setPackage_str(String package_str) {
        this.package_str = package_str;
    }

    @Override
    public String toString() {
        return "ApliyBean{" +
                "issucc=" + issucc +
                ", msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", appid='" + appid + '\'' +
                ", amount=" + amount +
                ", package_str='" + package_str + '\'' +
                ", no='" + no + '\'' +
                ", order_no='" + order_no + '\'' +
                '}';
    }
}

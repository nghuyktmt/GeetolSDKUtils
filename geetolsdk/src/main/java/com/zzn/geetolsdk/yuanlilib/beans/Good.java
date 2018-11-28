package com.zzn.geetolsdk.yuanlilib.beans;

/**
 * Created by Walter on 2018/11/10.
 */

public class Good {
    //    商品名
    private String name;
    //    支付宝价格
    private String zfbPrice;
    //    微信价格
    private String wxPrice;
    //    商品原价
    private String original;
    //    支付方式
    private String payWay = "";
    //    商品ID（调用总部后台的支付时需要传）
    private int goodId;
    //    备注
    private String remark;

    private String value;

    public Good() {
    }

    public Good(String name, String zfbPrice, String wxPrice, String original, String payWay, int goodId, String value, String remark) {
        this.name = name;
        this.zfbPrice = zfbPrice;
        this.wxPrice = wxPrice;
        this.original = original;
        this.payWay = payWay;
        this.goodId = goodId;
        this.remark = remark;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZfbPrice() {
        return zfbPrice;
    }

    public void setZfbPrice(String zfbPrice) {
        this.zfbPrice = zfbPrice;
    }

    public String getWxPrice() {
        return wxPrice;
    }

    public void setWxPrice(String wxPrice) {
        this.wxPrice = wxPrice;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getPayWay() {
        if (payWay.contains("1") && payWay.contains("2"))
            return "wx&zfb";
        if (payWay.contains("1") && !payWay.contains("2"))
            return "wx";
        if (!payWay.contains("1") && payWay.contains("2"))
            return "zfb";
        return "zfb";
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Good{" +
                "name='" + name + '\'' +
                ", zfbPrice='" + zfbPrice + '\'' +
                ", wxPrice='" + wxPrice + '\'' +
                ", original='" + original + '\'' +
                ", payWay='" + getPayWay() + '\'' +
                ", goodId=" + goodId +
                ", remark='" + remark + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

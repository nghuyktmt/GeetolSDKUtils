package com.zzn.geetolsdk.yuanlilib.beans;

import java.io.Serializable;

/**
 * Created by cheng
 * PackageName APP_Lock
 * 2018/1/24 17:25
 */

public class Vip  implements Serializable {
    private String viplevel;
    private int count;
    private String time;
    private boolean isout;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isIsout() {
        return isout;
    }

    public void setIsout(boolean isout) {
        this.isout = isout;
    }

    public String getViplevel() {
        return viplevel;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel;
    }

    @Override
    public String toString() {
        return "Vip{" +
                "viplevel='" + viplevel + '\'' +
                ", count=" + count +
                ", time='" + time + '\'' +
                ", isout=" + isout +
                '}';
    }
}

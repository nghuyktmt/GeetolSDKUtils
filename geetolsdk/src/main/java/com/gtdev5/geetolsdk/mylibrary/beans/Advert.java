package com.gtdev5.geetolsdk.mylibrary.beans;

import java.util.ArrayList;

/**
 * Created by Walter on 2018/11/10.
 */

public class Advert {

    public Img welcome;
    public ArrayList<Img> banners = new ArrayList<>();

    public Img getWelcome() {
        return welcome;
    }

    public void setWelcome(Img welcome) {
        this.welcome = welcome;
    }

    public ArrayList<Img> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Img> banners) {
        this.banners = banners;
    }

    public static class Img {
        //    图片跳转位置（可以传递商品ID或者网页链接）
        private String link;
        //    图片的位置（banner或者启动页等）
        private String pos;
        //    图片的显示位置
        private String imgUrl;

        public Img(String link, String pos, String imgUrl) {
            this.link = link;
            this.pos = pos;
            this.imgUrl = imgUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        @Override
        public String toString() {
            return "Advert{" +
                    "link='" + link + '\'' +
                    ", pos='" + pos + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    '}';
        }
    }


}

package com.gtdev5.geetolsdk.mylibrary.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by cheng
 * PackageName ModelTest
 * 2018/1/5 10:49
 *          json解析工具
 */

public class GsonUtils {

    /**
     *          解析对象
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getFromClass(String json,Class<T> tClass){
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(json,tClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     *          解析集合
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T>List<T> getFromList(String json,Class<T> tClass){
        ArrayList list = new ArrayList();

        try {
            Gson gson = new Gson();
            JsonArray arry = (new JsonParser()).parse(json).getAsJsonArray();
            Iterator var5 = arry.iterator();

            while(var5.hasNext()) {
                JsonElement jsonElement = (JsonElement)var5.next();
                list.add(gson.fromJson(jsonElement, tClass));
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return list;
    }


    public static String mapToUrl(Map<String, ?> map) {
        StringBuffer result = new StringBuffer();
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                result.append(key + "=");
                if (map.get(key).equals("")) {
                    result.append("&");
                } else {
                    String value = (String) map.get(key);
                    try {
                        value = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    result.append(value + "&");
                }
            }
            result.deleteCharAt(result.length() - 1);

        }
        return result.toString();
    }


    public static <T> String getStringFromBean(T bean) {
        Gson gson = new Gson();
        return gson.toJson(bean);
    }

}

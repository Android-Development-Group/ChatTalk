package com.jusenr.library.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;

/**
 * json工具类
 * Created by guchenkai on 2015/10/27.
 */
public final class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    /**
     * 判断json类型
     *
     * @param json json
     * @return json类型
     */
    public static JsonType getJSONType(String json) {
        if (TextUtils.isEmpty(json))
            return JsonType.JSON_TYPE_ERROR;
        final char[] strChar = json.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];
        switch (firstChar) {
            case '{':
                return JsonType.JSON_TYPE_OBJECT;
            case '[':
                return JsonType.JSON_TYPE_ARRAY;
            default:
                return JsonType.JSON_TYPE_ERROR;
        }
    }

    /**
     * json类型枚举
     */
    public enum JsonType {
        JSON_TYPE_OBJECT,//JSONObject
        JSON_TYPE_ARRAY,//JSONArray
        JSON_TYPE_ERROR//不是JSON格式的字符串
    }

    /**
     * 格式化json
     *
     * @param json json
     * @return 格式化后的json
     */
    public static String jsonFormatter(String json) throws NullPointerException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String formatJson = gson.toJson(je);
        return formatJson;
    }

    /**
     * json格式化
     *
     * @param jsonString json数据源
     * @param cls        序列化对象
     * @param <T>
     * @return
     */
    public static <T> T parseData(String jsonString, Class<T> cls) {
        return new Gson().fromJson(jsonString, cls);
    }

    /**
     * 将json字符串转换成java对象
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成java List对象
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj
     * @return
     */
    public static String beanToJson(Object obj) {
        String result = JSON.toJSONString(obj);
        Log.e(TAG, "beanToJson: " + result);
        return result;
    }
}

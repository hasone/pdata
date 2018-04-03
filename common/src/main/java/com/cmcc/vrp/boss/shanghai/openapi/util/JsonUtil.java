package com.cmcc.vrp.boss.shanghai.openapi.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lgk8023
 *
 */
public class JsonUtil {
    /**
     * @param text
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T toBean(String text, Class<T> clazz) throws Exception {
        T t = JSON.parseObject(text, clazz);
        return t;
    }

    /**
     * @param obj
     * @return
     */
    public static String toJsonString(Object obj) {
        String jsonString = JSON.toJSONString(obj);
        return jsonString;
    }

    /**
     * @param text
     * @return
     */
    public static JSONObject toJson(String text) {
        return JSON.parseObject(text);
    }
}

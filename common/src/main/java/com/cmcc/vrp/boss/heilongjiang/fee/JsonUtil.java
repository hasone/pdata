package com.cmcc.vrp.boss.heilongjiang.fee;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:33:38
*/
public class JsonUtil {
    private static Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    public static String getJson(Object o) {
        String json = null;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOG.error("{}",e);
        }
        return json;
    }

    /**
     * @param json
     * @param c
     * @return
     */
    public static <T> Object fromJson(String json, Class<T> c) {
        T obj = null;
        try {
            obj = new ObjectMapper().readValue(json, c);
        } catch (IOException e) {
            LOG.error("{}",e);
        }
        return obj;
    }

}

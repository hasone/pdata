package com.cmcc.vrp.boss.liaoning.util;

import com.alibaba.fastjson.JSONObject;
import java.io.Reader;
import java.io.StringReader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月16日 下午4:40:01
*/
public class ParseUtil {
    public static String parseRespCode(String response, String format) throws Exception {
        String respCode = "00000";
        if (StringUtils.isBlank(response)) {
            return respCode;
        }

        if ("json".equals(format)) {
            respCode = (String) JSONObject.parseObject(response).get("respCode");
        } else if ("xml".equals(format)) {
            Reader reader = new StringReader(response);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(reader);
            Element root = document.getRootElement();
            respCode = root.elementText("respCode");
        }

        return respCode;
    }
}
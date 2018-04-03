package com.cmcc.vrp.province.common.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.junit.Test;

/**
 * Created by sunyiwei on 2016/5/31.
 */
public class JsonTest {
    @Test
    public void testEncode() throws Exception {
        System.out.println(new Gson().toJson(new TestClass("GUID-PROD-06b436fc0cbee1516fe1b1290ed96ca76b836268")));
    }

    @Test
    public void testDecode() throws Exception {
        String content = "{\"PRODUCT_GUID\":\"GUID-PROD-06b436fc0cbee1516fe1b1290ed96ca76b836268\"}";

        JSONObject jsonObject = JSONObject.parseObject(content);
        String productGuid = jsonObject.getString("PRODUCT_GUID");
        System.out.println(productGuid);
    }

    private class TestClass {
        private String PRODUCT_GUID;

        public TestClass(String PRODUCT_GUID) {
            this.PRODUCT_GUID = PRODUCT_GUID;
        }

        public String getPRODUCT_GUID() {
            return PRODUCT_GUID;
        }
    }
}

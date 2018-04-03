package com.cmcc.vrp.async;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.queue.pojo.ChargePojo;
import org.junit.Test;

/**
 * Created by sunyiwei on 2016/6/12.
 */
public class Deserialize {
    @Test
    public void testDesc() throws Exception {
        final String content = "{\"chargeRecordId\":1829877,\"ecSerialNum\":\"20160607113401\",\"enterpriseId\":45,\"mobile\":\"13699415473\",\"productId\":14,\"systemNum\":\"9337abe610d8477f8b469ed9c1b38292a85baa41\"}";
        ChargePojo chargePojo = JSONObject.parseObject(content, ChargePojo.class);

        System.out.println(chargePojo);
    }
}

package com.cmcc.vrp.boss.neimenggu;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sunyiwei on 2016/4/21.
 */
@Ignore
public class NMBossServiceImplTest {

    @Autowired
    NMBossServiceImpl nmBossService;

    @Ignore
    @Test
    public void testGetSendUrl() throws Exception {
        final String json = "{\"pubInfo\":{\"transactionId\":\"de8f10fd-26a1-4b18-a8aa-7efbe4ae222c\",\"transactionTime\":\"2016-04-2120:27:47\"},\"request\":{\"busiParams\":{\"outSysSn\":\"a95a29f987e3b66df5a042322e0d8c95\",\"groupId\":\"471710008909\",\"phoneId\":\"18867103685\",\"prodId\":\"40006095\"}}}";
        nmBossService.getSendUrl(json);
    }
}
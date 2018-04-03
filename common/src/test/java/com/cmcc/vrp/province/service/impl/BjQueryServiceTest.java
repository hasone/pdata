package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.beijing.BjBossQueryServiceImpl;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lilin on 2016/9/20.
 */
@RunWith(MockitoJUnitRunner.class)
public class BjQueryServiceTest {

    @Mock
    private TaskProducer producer;

    @Autowired
    private BjBossQueryServiceImpl bjBossQueryService;

    @Test
    public void queryTest() {
        BjBossQueryServiceImpl mock = Mockito.mock(BjBossQueryServiceImpl.class);
        Mockito.when(mock.queryStatus("192905b57c665b64e0fda3a61c4986c34ffb7120")).thenReturn(BossQueryResult.FAILD);
        ChargeQueryPojo pojo = new ChargeQueryPojo();
        pojo.setFingerPrint("beijing123456789");
        pojo.setEntId(190L);
        pojo.setSystemNum("192905b57c665b64e0fda3a61c4986c34ffb7120");
        producer.produceAsynChargeQueryMsg(pojo);
    }

//    @Test
//    public void bjQueryStatusTest() {
//        QueryWebResponse response = new QueryWebResponse();
//        QueryRespBody queryRespBody = new QueryRespBody();
//        QueryRetInfo retInfo = new QueryRetInfo();
//        PhoneList list = new PhoneList();
//        list.setState("1");
//        retInfo.setPhoneList(list);
//        queryRespBody.setQueryRetInfo(retInfo);
//        response.setRespBody(queryRespBody);
//        XStream mock = Mockito.mock(XStream.class);
//        String xml = "<WebResponse>\n" +
//                "  <Header>\n" +
//                "    <TransactionID>34161346</TransactionID>\n" +
//                "    <ResponseTime>2016-09-20 21:34:46</ResponseTime>\n" +
//                "    <RetCode>1</RetCode>\n" +
//                "    <RetDesc>用户信息未通过验证.</RetDesc>\n" +
//                "  </Header>\n" +
//                "  <WebBody>\n" +
//                "    <RetInfo/>\n" +
//                "  </WebBody>\n" +
//                "</WebResponse>";
//        Mockito.when(mock.fromXML(xml)).thenReturn(response);
////        bjBossQueryService = new BjBossQueryServiceImpl();
//        bjBossQueryService.queryStatus("'e104503c0b4e89d7b91e537abd1079815065234b'");
//    }

}

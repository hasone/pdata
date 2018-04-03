package com.cmcc.vrp.queue.task;


import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.MdrcSmsChargePojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/11/9.
 */
@RunWith(MockitoJUnitRunner.class)
public class MdrcSmsChargeWorkerTest {
    @InjectMocks
    MdrcSmsChargeWorker mdrcSmsChargeWorker = new MdrcSmsChargeWorker();
    @Mock
    MdrcCardInfoService mdrcCardInfoService;
    @Mock
    TaskProducer taskProducer;

    @Test
    public void testExec1() {
        MdrcSmsChargePojo pojo = createMdrcSmsChargePojo();
        String pojoStr = JSON.toJSONString(pojo);

        mdrcSmsChargeWorker.setTaskString(pojoStr);
        Mockito.when(mdrcCardInfoService.use(anyString(), anyString(), anyString(),
            anyString(), anyString())).thenReturn(true);
        mdrcSmsChargeWorker.exec();
        Mockito.verify(mdrcCardInfoService).use(anyString(), anyString(), anyString(),
            anyString(), anyString());
    }

    @Test
    public void testExec2() {
        MdrcSmsChargePojo pojo = createMdrcSmsChargePojo();
        String pojoStr = JSON.toJSONString(pojo);

        mdrcSmsChargeWorker.setTaskString(pojoStr);
        Mockito.when(mdrcCardInfoService.use(anyString(), anyString(), anyString(),
            anyString(), anyString())).thenReturn(false);
        Mockito.when(taskProducer.produceDeliverNoticeSmsMsg(any(SmsPojo.class))).thenReturn(true);
        mdrcSmsChargeWorker.exec();
        Mockito.verify(mdrcCardInfoService).use(anyString(), anyString(), anyString(),
            anyString(), anyString());
        Mockito.verify(taskProducer).produceDeliverNoticeSmsMsg(any(SmsPojo.class));
    }

    private MdrcSmsChargePojo createMdrcSmsChargePojo() {
        MdrcSmsChargePojo pojo = new MdrcSmsChargePojo();
        pojo.setMobile("18867101111");
        pojo.setContent("test#test");
        return pojo;
    }


}

package com.cmcc.vrp.queue.task;

import static org.mockito.Matchers.anyString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.MdrcChargePojo;

/**
 * Created by qinqinyan on 2016/11/9.
 */
@RunWith(MockitoJUnitRunner.class)
public class MdrcAsyncChargeWorkerTest {
    @InjectMocks
    MdrcAsyncChargeWorker mdrcAsyncChargeWorker = new MdrcAsyncChargeWorker();

    @Mock
    MdrcCardInfoService mdrcCardInfoService;
    
    @Mock
    GlobalConfigService globalConfigService;

    @Test
    public void testExec1() {
        mdrcAsyncChargeWorker.exec();
    }

    @Test
    public void testExec2() {
        MdrcChargePojo pojo = createMdrcChargePojo();
        String jsonStr = JSON.toJSONString(pojo);
        mdrcAsyncChargeWorker.setTaskString(jsonStr);

        Mockito.when(mdrcCardInfoService.use(anyString(), anyString(),
            anyString(), anyString(), anyString())).thenReturn(true);

        Mockito.when(globalConfigService.get(anyString())).thenReturn(null);
        
//        mdrcAsyncChargeWorker.exec();

//        Mockito.verify(mdrcCardInfoService).use(anyString(), anyString(),
//            anyString(), anyString(), anyString());
    }

    @Test
    public void testExec3() {
        MdrcChargePojo pojo = createMdrcChargePojo();
        String jsonStr = JSON.toJSONString(pojo);
        mdrcAsyncChargeWorker.setTaskString(jsonStr);

        Mockito.when(mdrcCardInfoService.use(anyString(), anyString(),
            anyString(), anyString(), anyString())).thenReturn(false);

//        mdrcAsyncChargeWorker.exec();
//
//        Mockito.verify(mdrcCardInfoService).use(anyString(), anyString(),
//            anyString(), anyString(), anyString());
    }

    @Test
    public void testExec4() {
        MdrcChargePojo pojo = createMdrcChargePojo();
        pojo.setSerialNum(null);
        String jsonStr = JSON.toJSONString(pojo);
        mdrcAsyncChargeWorker.setTaskString(jsonStr);
        mdrcAsyncChargeWorker.exec();
    }


    private MdrcChargePojo createMdrcChargePojo() {
        MdrcChargePojo pojo = new MdrcChargePojo();
        pojo.setCardNum("test");
        pojo.setPassword("test");
        pojo.setMobile("18867101111");
        pojo.setIp("172.0.0.1");
        pojo.setSerialNum("test");
        return pojo;
    }


}

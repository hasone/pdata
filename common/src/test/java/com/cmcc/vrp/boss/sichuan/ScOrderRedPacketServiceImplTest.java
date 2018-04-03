package com.cmcc.vrp.boss.sichuan;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.model.flowredpacket.OrderRedPacketReq;
import com.cmcc.vrp.boss.sichuan.service.ScOrderRedPacketService;
import com.cmcc.vrp.boss.sichuan.service.impl.ScOrderRedPacketServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;

/**
 * ScOrderRedPacketServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月23日
 */
@RunWith(MockitoJUnitRunner.class)
public class ScOrderRedPacketServiceImplTest {
    @InjectMocks
    ScOrderRedPacketService serivce = new ScOrderRedPacketServiceImpl();
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Test
    public void testGenerateRequestString(){
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("test");    
        assertNotNull(serivce.generateRequestString(new OrderRedPacketReq()));
    }
    
    @Test
    public void testSendRequest(){        
        assertNull(serivce.sendRequest(new OrderRedPacketReq()));
    }
}

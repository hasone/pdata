package com.cmcc.vrp.boss.sichuan;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.model.SCBalanceRequest;
import com.cmcc.vrp.boss.sichuan.service.SCBalanceService;
import com.cmcc.vrp.boss.sichuan.service.impl.SCBalanceServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;

/**
 * ScBalanceQryServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月23日
 */
@RunWith(MockitoJUnitRunner.class)
public class ScBalanceQryServiceImplTest {
    @InjectMocks
    SCBalanceService serivce = new SCBalanceServiceImpl();
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Test
    public void testGenerateRequestString(){
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("test");    
        assertNotNull(serivce.generateRequestString(new SCBalanceRequest()));
    }
    
    @Test
    public void testParseResponse(){        
        assertNotNull(serivce.parseResponse("{\"resCode\":\"0000000\",\"resMsg\":\"test\"}"));
    }
    
    @Test
    public void testSendBalanceRequest(){        
        assertNotNull(serivce.sendBalanceRequest(new SCBalanceRequest()));
    }
}

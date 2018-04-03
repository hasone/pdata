package com.cmcc.vrp.boss.sichuan;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.service.ScMemberInquiryService;
import com.cmcc.vrp.boss.sichuan.service.impl.ScMemberInquiryServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;

/**
 * ScMemberInquireServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月23日
 */
@RunWith(MockitoJUnitRunner.class)
public class ScMemberInquireServiceImplTest {
    @InjectMocks
    ScMemberInquiryService serivce = new ScMemberInquiryServiceImpl();
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Test
    public void testGenerateRequestString(){
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("test");    
        assertNotNull(serivce.generateRequestString("18867103685"));
    }
    
    @Test
    public void testParseResponse(){        
        assertNotNull(serivce.parseResponse("{\"resCode\":\"0000000\",\"resMsg\":\"test\"}"));
    }
    
    
    @Test
    public void testSendRequest(){        
        assertNull(serivce.sendInquiryRequest("18867103685"));
    }
}

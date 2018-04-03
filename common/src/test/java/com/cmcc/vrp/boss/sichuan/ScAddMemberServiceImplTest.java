package com.cmcc.vrp.boss.sichuan;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.model.SCChargeRequest;
import com.cmcc.vrp.boss.sichuan.service.SCAddMemberService;
import com.cmcc.vrp.boss.sichuan.service.impl.SCAddMemberServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;

/**
 * ScAddMemberServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月23日
 */
@RunWith(MockitoJUnitRunner.class)
public class ScAddMemberServiceImplTest {
    @InjectMocks
    SCAddMemberService serivce = new SCAddMemberServiceImpl();
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Test
    public void testGenerateRequestString(){
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("test");    
        assertNotNull(serivce.generateRequestString(new SCChargeRequest()));
    }
    
    @Test
    public void testParseResponse(){        
        assertNull(serivce.parseResponse(""));
    }
    
    @Test
    public void testSendChargeRequest(){        
        assertNull(serivce.sendChargeRequest(new SCChargeRequest()));
    }
}

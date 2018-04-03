package com.cmcc.vrp.boss.sichuan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.model.SCDelMemberRequest;
import com.cmcc.vrp.boss.sichuan.service.SCDelMemberService;
import com.cmcc.vrp.boss.sichuan.service.impl.SCDelMemberServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;

/**
 * ScDelMemberServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月23日
 */
@RunWith(MockitoJUnitRunner.class)
public class ScDelMemberServiceImplTest {
    @InjectMocks
    SCDelMemberService serivce = new SCDelMemberServiceImpl();
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Test
    public void testGenerateRequestString(){
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("test");    
        assertNotNull(serivce.generateRequestString(new SCDelMemberRequest()));
    }

    @Test
    public void testSendDelMemberRequest(){        
        assertFalse(serivce.sendDelMemberRequest(new SCDelMemberRequest()));
    }
}

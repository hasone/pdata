package com.cmcc.vrp.boss.sichuan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.model.SCCancelRequest;
import com.cmcc.vrp.boss.sichuan.service.SCCancelService;
import com.cmcc.vrp.boss.sichuan.service.impl.SCCancelServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;

/**
 * ScCancelServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月23日
 */
@RunWith(MockitoJUnitRunner.class)
public class ScCancelServiceImplTest {
    @InjectMocks
    SCCancelService serivce = new SCCancelServiceImpl();
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Test
    public void testGenerateRequestString(){
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("test");    
        assertNotNull(serivce.generateRequestString(new SCCancelRequest()));
    }

    @Test
    public void testSendCancelRequest(){        
        assertFalse(serivce.sendCancelRequest(new SCCancelRequest()));
    }
}

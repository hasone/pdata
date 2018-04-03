package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.service.GlobalConfigService;

@RunWith(MockitoJUnitRunner.class)
public class PresentSingleRateLimitServiceImplTest {
    
    @InjectMocks
    PresentSingleRateLimitServiceImpl service = new PresentSingleRateLimitServiceImpl();

    @Mock
    GlobalConfigService globalConfigService;
    
    @Test
    public void testGetLimitSwitchKey(){
        assertNotNull(service.getLimitSwitchKey());
    }
    
    @Test
    public void testGetLimitTimeRangeKey(){
        assertNotNull(service.getLimitTimeRangeKey());
    }
    
    @Test
    public void testGetLimitCountKey(){
        assertNotNull(service.getLimitCountKey());
    }
}

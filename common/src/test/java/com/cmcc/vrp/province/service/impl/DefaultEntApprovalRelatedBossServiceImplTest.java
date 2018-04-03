package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.service.EntApprovalRelatedBossService;

@RunWith(MockitoJUnitRunner.class)
public class DefaultEntApprovalRelatedBossServiceImplTest {
    @InjectMocks
    EntApprovalRelatedBossService service = new DefaultEntApprovalRelatedBossServiceImpl();
    
    @Test
    public void testSynchronizeFromBoss(){
        assertTrue(service.synchronizeFromBoss(1L));
    }
    
}

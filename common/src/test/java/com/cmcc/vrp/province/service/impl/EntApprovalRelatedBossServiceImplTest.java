package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.mockito.InjectMocks;

import com.cmcc.vrp.province.service.EntApprovalRelatedBossService;

public class EntApprovalRelatedBossServiceImplTest {
    @InjectMocks
    EntApprovalRelatedBossService service = new EntApprovalRelatedBossServiceImpl();
    
    @Test
    public void testSynchronizeFromBoss(){
        assertFalse(service.synchronizeFromBoss(1L));
    }
}

package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.chongqing.CQBossServiceImpl;
import com.cmcc.vrp.province.service.EntApprovalRelatedBossService;

@RunWith(MockitoJUnitRunner.class)
public class CQEntApprovalRelatedBossServiceImplTest {
    @InjectMocks
    EntApprovalRelatedBossService service = new CQEntApprovalRelatedBossServiceImpl();
    
    @Mock
    CQBossServiceImpl cqBossService;
    
    @Test
    public void testSynchronizeFromBoss(){
        when(cqBossService.syncronizedPrdsByEnterCode(1L)).thenReturn(true);
        assertTrue(service.synchronizeFromBoss(1L));
    }
    
}

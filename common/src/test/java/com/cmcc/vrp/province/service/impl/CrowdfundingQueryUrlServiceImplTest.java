package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.CrowdfundingQueryUrlMapper;
import com.cmcc.vrp.province.model.CrowdfundingQueryUrl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月8日 下午1:54:52
*/
@RunWith(MockitoJUnitRunner.class)
public class CrowdfundingQueryUrlServiceImplTest {
    @InjectMocks
    CrowdfundingQueryUrlServiceImpl crowdfundingQueryUrlServiceImpl = new CrowdfundingQueryUrlServiceImpl();
    
    @Mock
    CrowdfundingQueryUrlMapper crowdfundingQueryUrlMapper;
    
    /**
     * 
     */
    @Test
    public void testInsert() {
        assertFalse(crowdfundingQueryUrlServiceImpl.insert(null));
        when(crowdfundingQueryUrlMapper.insert(any(CrowdfundingQueryUrl.class)))
            .thenReturn(1).thenReturn(0);
        assertTrue(crowdfundingQueryUrlServiceImpl.insert(new CrowdfundingQueryUrl()));
        assertFalse(crowdfundingQueryUrlServiceImpl.insert(new CrowdfundingQueryUrl()));
    }
    /**
     * 
     */
    @Test
    public void testDelete() {
        assertFalse(crowdfundingQueryUrlServiceImpl.delete(null));
        when(crowdfundingQueryUrlMapper.delete(any(Long.class)))
            .thenReturn(1).thenReturn(0);
        assertTrue(crowdfundingQueryUrlServiceImpl.delete(1l));
        assertFalse(crowdfundingQueryUrlServiceImpl.delete(1l));
    }
    /**
     * 
     */
    @Test
    public void testGetByCrowdfundingActivityDetailId() {
        assertNull(crowdfundingQueryUrlServiceImpl.getByCrowdfundingActivityDetailId(null));
        when(crowdfundingQueryUrlMapper.getByCrowdfundingActivityDetailId(any(Long.class)))
            .thenReturn(null);
        assertNull(crowdfundingQueryUrlServiceImpl.getByCrowdfundingActivityDetailId(1l));
    }
    
    /**
     * 
     */
    @Test
    public void testGetById() {
        assertNull(crowdfundingQueryUrlServiceImpl.getById(null));
        when(crowdfundingQueryUrlMapper.getById(any(Long.class)))
            .thenReturn(null);
        assertNull(crowdfundingQueryUrlServiceImpl.getById(1l));
    }
    
    /**
     * 
     */
    @Test
    public void testUpdateByCrowdfundingActivityDetailId() {
        assertFalse(crowdfundingQueryUrlServiceImpl.updateByCrowdfundingActivityDetailId(null));
        when(crowdfundingQueryUrlMapper.updateByCrowdfundingActivityDetailId(any(CrowdfundingQueryUrl.class)))
            .thenReturn(1).thenReturn(0);
        assertTrue(crowdfundingQueryUrlServiceImpl.updateByCrowdfundingActivityDetailId(new CrowdfundingQueryUrl()));
        assertFalse(crowdfundingQueryUrlServiceImpl.updateByCrowdfundingActivityDetailId(new CrowdfundingQueryUrl()));
    }
}

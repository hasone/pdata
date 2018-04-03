package com.cmcc.vrp.province.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import com.cmcc.vrp.province.dao.ECSyncInfoMapper;
import com.cmcc.vrp.province.model.ECSyncInfo;

/**
 * @author lgk8023
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ECSyncInfoServiceImplTest {
    @InjectMocks
    ECSyncInfoServiceImpl ecSyncInfoServiceImpl = new ECSyncInfoServiceImpl();

    @Mock
    ECSyncInfoMapper ecSyncInfoMapper;
    
    /**
     * 
     */
    @Test
    public void testInsert() {
        Assert.assertFalse(ecSyncInfoServiceImpl.insert(null));
        Mockito.when(ecSyncInfoMapper.insert(Mockito.any(ECSyncInfo.class)))
            .thenReturn(1).thenReturn(0);
        Assert.assertTrue(ecSyncInfoServiceImpl.insert(new ECSyncInfo()));
        Assert.assertFalse(ecSyncInfoServiceImpl.insert(new ECSyncInfo()));
    }
    @Test
    public void testUpdateByPrimaryKeySelective() {
        Assert.assertFalse(ecSyncInfoServiceImpl.updateByPrimaryKeySelective(null));
        Mockito.when(ecSyncInfoMapper.updateByPrimaryKeySelective(Mockito.any(ECSyncInfo.class)))
            .thenReturn(1).thenReturn(0);
        Assert.assertTrue(ecSyncInfoServiceImpl.updateByPrimaryKeySelective(new ECSyncInfo()));
        Assert.assertFalse(ecSyncInfoServiceImpl.updateByPrimaryKeySelective(new ECSyncInfo()));
    }
    @Test
    public void testSelectByECCode() {
        Assert.assertNull(ecSyncInfoServiceImpl.selectByECCode(null));
        Assert.assertNull(ecSyncInfoServiceImpl.selectByECCode(""));
        Mockito.when(ecSyncInfoMapper.selectByECCode(Mockito.any(String.class)))
            .thenReturn(null).thenReturn(new ECSyncInfo());
        Assert.assertNull(ecSyncInfoServiceImpl.selectByECCode("123"));
        Assert.assertNotNull(ecSyncInfoServiceImpl.selectByECCode("123"));
    }
    @Test
    public void testUpdateOrInsert() {
        ECSyncInfo newEcSyncInfo = new ECSyncInfo();
        Assert.assertFalse(ecSyncInfoServiceImpl.updateOrInsert(null));
        Assert.assertFalse(ecSyncInfoServiceImpl.updateOrInsert(newEcSyncInfo));
        newEcSyncInfo.setEcCode("123");
        newEcSyncInfo.setId(1l);
        Mockito.when(ecSyncInfoMapper.selectByECCode(Mockito.any(String.class)))
            .thenReturn(newEcSyncInfo);
        Mockito.when(ecSyncInfoMapper.updateByPrimaryKeySelective(Mockito.any(ECSyncInfo.class)))
            .thenReturn(1);
        Assert.assertTrue(ecSyncInfoServiceImpl.updateOrInsert(newEcSyncInfo));
    }
}

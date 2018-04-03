package com.cmcc.vrp.province.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MaskingMapper;
import com.cmcc.vrp.province.model.Masking;

/**
 * @author lgk8023
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MaskingServiceImplTest {

    @InjectMocks
    MaskingServiceImpl maskingServiceImpl = new MaskingServiceImpl();
    @Mock
    MaskingMapper maskingMapper;
    
    /**
     * 
     */
    @Test
    public void testInsert() {
        Assert.assertFalse(maskingServiceImpl.insert(null));
        Mockito.when(maskingMapper.insert(Mockito.any(Masking.class))).thenReturn(1).thenReturn(0);
        Assert.assertTrue(maskingServiceImpl.insert(new Masking()));
        Assert.assertFalse(maskingServiceImpl.insert(new Masking()));
    }
    /**
     * 
     */
    @Test
    public void testGetByAdminId() {
        Assert.assertNull(maskingServiceImpl.getByAdminId(null));
        Mockito.when(maskingMapper.getByAdminId(Mockito.anyLong())).thenReturn(new Masking()).thenReturn(null);
        Assert.assertNotNull(maskingServiceImpl.getByAdminId(1l));
        Assert.assertNull(maskingServiceImpl.getByAdminId(1l));
    }
    /**
     * 
     */
    @Test
    public void testUpdateByPrimaryKeySelective() {
        Assert.assertFalse(maskingServiceImpl.updateByPrimaryKeySelective(null));
        Mockito.when(maskingMapper.updateByPrimaryKeySelective(Mockito.any(Masking.class))).thenReturn(1).thenReturn(0);
        Assert.assertTrue(maskingServiceImpl.updateByPrimaryKeySelective(new Masking()));
        Assert.assertFalse(maskingServiceImpl.updateByPrimaryKeySelective(new Masking()));
    }
    /**
     * 
     */
    @Test
    public void testInsertOrUpdate() {
        Masking masking = new Masking();
        masking.setId(1l);
        masking.setAdminId(1l);
        Assert.assertFalse(maskingServiceImpl.insertOrUpdate(null));
        Mockito.when(maskingMapper.getByAdminId(Mockito.anyLong())).thenReturn(masking).thenReturn(null);
        Mockito.when(maskingMapper.updateByPrimaryKeySelective(Mockito.any(Masking.class))).thenReturn(1);
        Mockito.when(maskingMapper.insert(Mockito.any(Masking.class))).thenReturn(1);
        Assert.assertTrue(maskingServiceImpl.insertOrUpdate(masking));
        Assert.assertTrue(maskingServiceImpl.insertOrUpdate(masking));
    }
}

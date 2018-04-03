package com.cmcc.vrp.province.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ShOrderListMapper;
import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.util.QueryObject;




/**
 * @author lgk8023
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ShOrderListServiceImplTest {
    @InjectMocks
    ShOrderListServiceImpl shOrderListService = new ShOrderListServiceImpl();

    @Mock
    ShOrderListMapper shOrderListMapper;
    
    /**
     * 
     */
    @Test
    public void testInsert() {
        ShOrderList shOrderList = new ShOrderList();
        shOrderList.setMainBillId("xxxxx");
        Assert.assertFalse(shOrderListService.insert(null));
        Mockito.when(shOrderListMapper.getByMainBillId(Mockito.anyString()))
            .thenReturn(new ShOrderList()).thenReturn(null);
        Mockito.when(shOrderListMapper.insert(Mockito.any(ShOrderList.class)))
            .thenReturn(1).thenReturn(0);
        Assert.assertFalse(shOrderListService.insert(shOrderList));
        Assert.assertTrue(shOrderListService.insert(shOrderList));
        Assert.assertFalse(shOrderListService.insert(shOrderList));
    }
    
    /**
     * 
     */
    @Test
    public void testGetByEnterId() {
        Assert.assertNull(shOrderListService.getByEnterId(null));
        Mockito.when(shOrderListMapper.getByEnterId(Mockito.anyLong()))
            .thenReturn(null);
        Assert.assertNull(shOrderListService.getByEnterId(1l));
    }
    
    /**
     * 
     */
    @Test
    public void testGetByMainBillId() {
        Assert.assertNull(shOrderListService.getByMainBillId(null));
        Mockito.when(shOrderListMapper.getByMainBillId(Mockito.anyString()))
            .thenReturn(null);
        Assert.assertNull(shOrderListService.getByMainBillId("123456"));
    }
    
    /**
     * 
     */
    @Test
    public void testShowForPageResultCount() {
        Assert.assertEquals(0, shOrderListService.showForPageResultCount(null));
        Mockito.when(shOrderListMapper.showForPageResultCount(Mockito.anyMap()))
            .thenReturn(0);
        Assert.assertEquals(0, shOrderListService.showForPageResultCount(new QueryObject()));
    }
    
    /**
     * 
     */
    @Test
    public void testShowForPageResultList() {
        Assert.assertNull(shOrderListService.showForPageResultList(null));
        Mockito.when(shOrderListMapper.showForPageResultList(Mockito.anyMap()))
            .thenReturn(null);
        Assert.assertNull(shOrderListService.showForPageResultList(new QueryObject()));
    }
    
    /**
     * 
     */
    @Test
    public void testGetById() {
        Mockito.when(shOrderListMapper.getById(Mockito.anyLong()))
            .thenReturn(null);
        Assert.assertNull(shOrderListService.getById(1l));
    }
    
    /**
     * 
     */
    @Test
    public void testUpdateAlertSelective() {
        Mockito.when(shOrderListMapper.updateAlertSelective(Mockito.any(ShOrderList.class)))
            .thenReturn(1).thenReturn(0);
        Assert.assertTrue(shOrderListService.updateAlertSelective(new ShOrderList()));
        Assert.assertFalse(shOrderListService.updateAlertSelective(new ShOrderList()));
    }
}

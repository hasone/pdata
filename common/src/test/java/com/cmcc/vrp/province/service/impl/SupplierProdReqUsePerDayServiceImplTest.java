package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SupplierProdReqUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
import com.cmcc.vrp.province.service.SupplierProdReqUsePerDayService;

@RunWith(MockitoJUnitRunner.class)
public class SupplierProdReqUsePerDayServiceImplTest {
    
    @InjectMocks
    SupplierProdReqUsePerDayService supplierProdReqUsePerDayService = new SupplierProdReqUsePerDayServiceImpl();
    @Mock
    SupplierProdReqUsePerDayMapper mapper;
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(SupplierProdReqUsePerDay.class))).thenReturn(1);
        assertTrue(supplierProdReqUsePerDayService.insertSelective(new SupplierProdReqUsePerDay()));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new SupplierProdReqUsePerDay());
        assertNotNull(supplierProdReqUsePerDayService.selectByPrimaryKey(1L));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(SupplierProdReqUsePerDay.class))).thenReturn(1);
        assertTrue(supplierProdReqUsePerDayService.updateByPrimaryKeySelective(new SupplierProdReqUsePerDay()));
    }
    
    /**
     * 
     */
    @Test
    public void testDeleteByPrimaryKey() {
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1).thenReturn(0);
        assertTrue(supplierProdReqUsePerDayService.deleteByPrimaryKey(1l));
        assertFalse(supplierProdReqUsePerDayService.deleteByPrimaryKey(1l));
    }
    /**
     * 
     */
    @Test
    public void testUpdateUsedMoney(){
        Mockito.when(mapper.updateUsedMoney(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(1).thenReturn(0);
        assertTrue(supplierProdReqUsePerDayService.updateUsedMoney(1l,1D));
        assertFalse(supplierProdReqUsePerDayService.updateUsedMoney(1l,1D));
    }
    /**
     * 
     */
    @Test
    public void testSelectByMap() {
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(null);
        Assert.assertNull(supplierProdReqUsePerDayService.selectByMap(new HashMap()));
    }
    
    /**
     * 
     */
    @Test
    public void testGetTodayRecord() {
        Mockito.when(mapper.selectByMap(Mockito.anyMap()))
            .thenReturn(null);
        Assert.assertNull(supplierProdReqUsePerDayService.getTodayRecord(1l));
    }
}

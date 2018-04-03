package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SupplierReqUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
import com.cmcc.vrp.province.service.SupplierReqUsePerDayService;
@RunWith(MockitoJUnitRunner.class)
public class SupplierReqUsePerDayServiceImplTest {
    
    @InjectMocks
    SupplierReqUsePerDayService supplierReqUsePerDayService =
    new SupplierReqUsePerDayServiceImpl();
    
    @Mock
    SupplierReqUsePerDayMapper mapper;
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(SupplierReqUsePerDay.class))).thenReturn(1);
        assertTrue(supplierReqUsePerDayService.insertSelective(new SupplierReqUsePerDay()));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new SupplierReqUsePerDay());
        assertNotNull(supplierReqUsePerDayService.selectByPrimaryKey(1L));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(SupplierReqUsePerDay.class))).thenReturn(1);
        assertTrue(supplierReqUsePerDayService.updateByPrimaryKeySelective(new SupplierReqUsePerDay()));
    }
    
    @Test
    public void testDeleteByPrimaryKey(){
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
        assertTrue(supplierReqUsePerDayService.deleteByPrimaryKey(1L));
    }
    
    @Test
    public void testBatchDelete(){
        Mockito.when(mapper.batchDelete(Mockito.anyMap())).thenReturn(1);
        assertTrue(supplierReqUsePerDayService.batchDelete(new HashMap()));
    }
    
    @Test
    public void testUpdateUsedMoney(){
        Mockito.when(mapper.updateUsedMoney(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(1);
        assertTrue(supplierReqUsePerDayService.updateUsedMoney(1L, 2.0d));
    }
    
    @Test
    public void testGetTodayRecord(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<SupplierReqUsePerDay>());
        assertNull(supplierReqUsePerDayService.getTodayRecord(1L));
    }
    
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<SupplierReqUsePerDay>());
        assertNotNull(supplierReqUsePerDayService.selectByMap(new HashMap()));
    }


}

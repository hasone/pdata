package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SupplierFinanceRecordMapper;
import com.cmcc.vrp.province.model.SupplierFinanceRecord;
import com.cmcc.vrp.province.service.SupplierFinanceRecordService;

/**
 * 
 *  @desc:
 *  @author: wuguoping 
 *  @data: 2017年6月8日
 */
@RunWith(MockitoJUnitRunner.class)
public class SupplierFinanceRecordServiceImplTest {
    
    @InjectMocks
    SupplierFinanceRecordService supplierFinanceRecordService = 
    new SupplierFinanceRecordServiceImpl();
    @Mock
    SupplierFinanceRecordMapper mapper;
    
    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(supplierFinanceRecordService.deleteByPrimaryKey(null));
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
        assertTrue(supplierFinanceRecordService.deleteByPrimaryKey(1L));
    }
    
    @Test
    public void testInsert(){
        assertFalse(supplierFinanceRecordService.insert(null));
        Mockito.when(mapper.insert(Mockito.any(SupplierFinanceRecord.class))).thenReturn(1);
        assertTrue(supplierFinanceRecordService.insert(new SupplierFinanceRecord()));
    }
    
    @Test
    public void testInsertSelective(){
        assertFalse(supplierFinanceRecordService.insertSelective(null));
        Mockito.when(mapper.insertSelective(Mockito.any(SupplierFinanceRecord.class))).thenReturn(1);
        assertTrue(supplierFinanceRecordService.insertSelective(new SupplierFinanceRecord()));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(supplierFinanceRecordService.updateByPrimaryKeySelective(null));
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(SupplierFinanceRecord.class))).thenReturn(1);
        assertTrue(supplierFinanceRecordService.updateByPrimaryKeySelective(new SupplierFinanceRecord()));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(supplierFinanceRecordService.updateByPrimaryKey(null));
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(SupplierFinanceRecord.class))).thenReturn(1);
        assertTrue(supplierFinanceRecordService.updateByPrimaryKey(new SupplierFinanceRecord()));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        assertNull(supplierFinanceRecordService.selectByPrimaryKey(null));
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new SupplierFinanceRecord());
        assertNotNull(supplierFinanceRecordService.selectByPrimaryKey(1L));
    }
    
    @Test
    public void testSelectBySupplierId(){
        assertNull(supplierFinanceRecordService.selectBySupplierId(null));
        Mockito.when(mapper.selectBySupplierId(Mockito.anyLong())).thenReturn(new SupplierFinanceRecord());
        assertNotNull(supplierFinanceRecordService.selectBySupplierId(1L));
    }
    
    @Test
    public void testCountSupplierFinanceRecords(){
        Mockito.when(mapper.countSupplierFinanceRecords(Mockito.anyMap())).thenReturn(1);
        assertSame(1, supplierFinanceRecordService.countSupplierFinanceRecords(new HashMap<String, String>()));
    }
    
    @Test
    public void testQuerySupplierFinanceRecords(){
        Mockito.when(mapper.querySupplierFinanceRecords(Mockito.anyMap())).thenReturn(new ArrayList<SupplierFinanceRecord>());
        assertNotNull(supplierFinanceRecordService.querySupplierFinanceRecords(new HashMap<String, String>()));
    }
    
    @Test
    public void testDeleteBysupplierId(){
        Mockito.when(mapper.deleteBysupplierId(Mockito.anyLong())).thenReturn(1);
        assertTrue(supplierFinanceRecordService.deleteBysupplierId(1L));
    }
    
    @Test
    public void testBatchUpdate(){
        Mockito.when(mapper.batchUpdate(Mockito.anyList())).thenReturn(1);
        assertTrue(supplierFinanceRecordService.batchUpdate(new ArrayList<SupplierFinanceRecord>()));
    }

}

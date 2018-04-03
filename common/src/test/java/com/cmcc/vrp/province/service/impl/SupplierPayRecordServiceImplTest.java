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

import com.cmcc.vrp.province.dao.SupplierPayRecordMapper;
import com.cmcc.vrp.province.model.SupplierFinanceRecord;
import com.cmcc.vrp.province.model.SupplierPayRecord;
import com.cmcc.vrp.province.service.SupplierFinanceRecordService;
import com.cmcc.vrp.province.service.SupplierPayRecordService;

@RunWith(MockitoJUnitRunner.class)
public class SupplierPayRecordServiceImplTest {
    @InjectMocks
    SupplierPayRecordService supplierPayRecordService = new SupplierPayRecordServiceImpl();
    @Mock
    SupplierPayRecordMapper mapper;
    @Mock
    SupplierFinanceRecordService supplierFinanceRecordService;
    
    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(supplierPayRecordService.deleteByPrimaryKey(null));
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
        assertTrue(supplierPayRecordService.deleteByPrimaryKey(1L));
    }
    
    @Test
    public void testInsert(){
        assertFalse(supplierPayRecordService.insert(null));
        Mockito.when(mapper.insert(Mockito.any(SupplierPayRecord.class))).thenReturn(1);
        assertTrue(supplierPayRecordService.insert(new SupplierPayRecord()));
    }
    
    @Test
    public void testInsertSelective(){
        assertFalse(supplierPayRecordService.insertSelective(null));
        Mockito.when(mapper.insertSelective(Mockito.any(SupplierPayRecord.class))).thenReturn(1);
        assertTrue(supplierPayRecordService.insertSelective(new SupplierPayRecord()));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(supplierPayRecordService.updateByPrimaryKeySelective(null));
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(SupplierPayRecord.class))).thenReturn(1);
        assertTrue(supplierPayRecordService.updateByPrimaryKeySelective(new SupplierPayRecord()));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(supplierPayRecordService.updateByPrimaryKey(null));
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(SupplierPayRecord.class))).thenReturn(1);
        assertTrue(supplierPayRecordService.updateByPrimaryKey(new SupplierPayRecord()));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        assertNull(supplierPayRecordService.selectByPrimaryKey(null));
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new SupplierPayRecord());
        assertNotNull(supplierPayRecordService.selectByPrimaryKey(1L));
    }
    
    @Test
    public void testDeleteBysupplierId(){
        assertFalse(supplierPayRecordService.deleteBysupplierId(null));
        
        Mockito.when(mapper.deleteBysupplierId(Mockito.anyLong())).thenReturn(1);
        assertTrue(supplierPayRecordService.deleteBysupplierId(1L));
    }
    
    @Test
    public void testDeleteSupplierPayRecord(){
        assertFalse(supplierPayRecordService.deleteSupplierPayRecord(null));
        
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(createSupplierPayRecord());
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(-1).thenReturn(1);
        
        assertFalse(supplierPayRecordService.deleteSupplierPayRecord(1L));
        
        Mockito.when(supplierFinanceRecordService.selectBySupplierId(Mockito.anyLong())).thenReturn(createSupplierFinanceRecord());
        Mockito.when(supplierFinanceRecordService.updateByPrimaryKeySelective(Mockito.any(SupplierFinanceRecord.class))).thenReturn(true);
        assertTrue(supplierPayRecordService.deleteSupplierPayRecord(2L));
    }
    
    @Test
    public void testSaveSupplierPayRecord(){
        Mockito.when(mapper.insertSelective(Mockito.any(SupplierPayRecord.class))).thenReturn(0).thenReturn(1);
        
        assertFalse(supplierPayRecordService.saveSupplierPayRecord(new SupplierPayRecord()));
        
        Mockito.when(supplierFinanceRecordService.selectBySupplierId(Mockito.anyLong())).thenReturn(createSupplierFinanceRecord());
        Mockito.when(supplierFinanceRecordService.updateByPrimaryKeySelective(Mockito.any(SupplierFinanceRecord.class))).thenReturn(true);
        assertTrue(supplierPayRecordService.saveSupplierPayRecord(createSupplierPayRecord()));
    }
    
    @Test
    public void testQuerySupplierPayRecords(){
        Mockito.when(mapper.querySupplierPayRecords(Mockito.anyMap())).thenReturn(new ArrayList<SupplierPayRecord>());
        assertNotNull(supplierPayRecordService.querySupplierPayRecords(new HashMap<String, String>()));
    }
    
    @Test
    public void testCountSupplierPayRecords(){
        Mockito.when(mapper.countSupplierPayRecords(Mockito.anyMap())).thenReturn(1);
        assertSame(1, supplierPayRecordService.countSupplierPayRecords(new HashMap<String, String>()));
    }
    
    
    
    private SupplierPayRecord createSupplierPayRecord(){
        SupplierPayRecord supplierPayRecord = new SupplierPayRecord();
        supplierPayRecord.setId(1L);
        supplierPayRecord.setSupplierId(1L);
        supplierPayRecord.setPayMoney(10D);
        return supplierPayRecord;
    }
    
    private SupplierFinanceRecord createSupplierFinanceRecord(){
        SupplierFinanceRecord record = new SupplierFinanceRecord();
        record.setTotalMoney(1000D);
        record.setUsedMoney(50D);
        record.setBalance(50D);
        record.setSupplierId(1L);
        record.setId(1L);
        return record;
    }
    
    
}

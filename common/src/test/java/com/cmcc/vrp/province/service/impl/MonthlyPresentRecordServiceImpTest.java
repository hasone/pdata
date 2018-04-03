/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.MonthlyPresentRecordMapper;
import com.cmcc.vrp.province.model.MonthlyPresentRecord;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: MonthlyPresentRuleServiceImpTest 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年7月18日 上午9:41:38
 */
@RunWith(MockitoJUnitRunner.class)
public class MonthlyPresentRecordServiceImpTest {

    @InjectMocks
    MonthlyPresentRecordServiceImpl monthlyPresentRecordService = new MonthlyPresentRecordServiceImpl();

    @Mock
    MonthlyPresentRecordMapper monthlyPresentRecordMapper;

    @Test
    public void testInsert() {
        MonthlyPresentRecord record = new MonthlyPresentRecord();
        when(monthlyPresentRecordMapper.insertSelective(Mockito.any(MonthlyPresentRecord.class))).thenReturn(1);
        assertTrue(monthlyPresentRecordService.insert(record));
        
        when(monthlyPresentRecordMapper.insertSelective(Mockito.any(MonthlyPresentRecord.class))).thenReturn(0);
        assertFalse(monthlyPresentRecordService.insert(record));
    }

    @Test
    public void testGetRecords() {
        List<MonthlyPresentRecord> records = new ArrayList<MonthlyPresentRecord>();
        when(monthlyPresentRecordMapper.getRecords(Mockito.anyMap())).thenReturn(records);
        assertSame(records, monthlyPresentRecordService.getRecords(new QueryObject()));
    }

    @Test
    public void testCountRecords() {
        Long count = 1L;
        when(monthlyPresentRecordMapper.countRecords(Mockito.anyMap())).thenReturn(count);
        assertSame(count, monthlyPresentRecordService.countRecords(new QueryObject()));
    }

    @Test
    public void testBatchInsert() {
        List<MonthlyPresentRecord> list = new ArrayList<MonthlyPresentRecord>();
        list.add(new MonthlyPresentRecord());
        when(monthlyPresentRecordMapper.batchInsert(list)).thenReturn(list.size());
        assertTrue(monthlyPresentRecordService.batchInsert(list));
        
        when(monthlyPresentRecordMapper.batchInsert(list)).thenReturn(0);
        assertFalse(monthlyPresentRecordService.batchInsert(list));
    }
    
    @Test
    public void testUpdateActivityStatus() {
        when(monthlyPresentRecordMapper.updateStatusAndStatusCode
                (Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
        
        assertTrue(monthlyPresentRecordService.updateActivityStatus(1L,ActivityWinRecordStatus.SUCCESS,"aaa"));
        when(monthlyPresentRecordMapper.updateStatusAndStatusCode
                (Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(0);
        assertFalse(monthlyPresentRecordService.updateActivityStatus(1L,ActivityWinRecordStatus.SUCCESS,"aaa"));
        
        assertFalse(monthlyPresentRecordService.updateActivityStatus(1L,null,"aaa"));  
        assertFalse(monthlyPresentRecordService.updateActivityStatus(null,ActivityWinRecordStatus.SUCCESS,"aaa"));
    }
    
    @Test
    public void testBatchUpdateChargeResult() {
        List<MonthlyPresentRecord> records = new ArrayList<MonthlyPresentRecord>();
        records.add(new MonthlyPresentRecord());
        
        when(monthlyPresentRecordMapper.batchUpdateChargeResult(Mockito.anyList())).thenReturn(1);
        assertTrue(monthlyPresentRecordService.batchUpdateChargeResult(records));
        
        when(monthlyPresentRecordMapper.batchUpdateChargeResult(Mockito.anyList())).thenReturn(0);
        assertFalse(monthlyPresentRecordService.batchUpdateChargeResult(records));
        
        assertFalse(monthlyPresentRecordService.batchUpdateChargeResult(new ArrayList<MonthlyPresentRecord>()));
        assertFalse(monthlyPresentRecordService.batchUpdateChargeResult(null));
    }
    
    @Test
    public void testUpdatePresentStatus() {
        when(monthlyPresentRecordMapper.updateStatus(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString())).thenReturn(1);
        assertTrue(monthlyPresentRecordService.updatePresentStatus(1L, ChargeRecordStatus.COMPLETE, "aaa"));
        
        when(monthlyPresentRecordMapper.updateStatus(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString())).thenReturn(0);
        assertFalse(monthlyPresentRecordService.updatePresentStatus(1L, ChargeRecordStatus.COMPLETE, "aaa"));
        
        assertFalse(monthlyPresentRecordService.updatePresentStatus(1L, null, "aaa"));
        assertFalse(monthlyPresentRecordService.updatePresentStatus(null, ChargeRecordStatus.COMPLETE, "aaa"));  
    }
    
    @Test
    public void testUpdateStatusCode() {
        when(monthlyPresentRecordMapper.updateStatusCode(Mockito.anyLong(),Mockito.anyString())).thenReturn(1);
        assertTrue(monthlyPresentRecordService.updateStatusCode(1L,  "aaa"));
        
        when(monthlyPresentRecordMapper.updateStatusCode(Mockito.anyLong(),Mockito.anyString())).thenReturn(0);
        assertFalse(monthlyPresentRecordService.updateStatusCode(1L,  "aaa"));
        
        assertFalse(monthlyPresentRecordService.updateStatusCode(null,  "aaa"));
        
    }
    
    @Test
    public void testBatchUpdateStatusCode() {
        List<Long> records = new ArrayList<Long>();
        records.add(1L);
        when(monthlyPresentRecordMapper.batchUpdateStatusCode(Mockito.anyList(),Mockito.anyString())).thenReturn(1);
        
        assertTrue(monthlyPresentRecordService.batchUpdateStatusCode(records, "aaa"));
        
        when(monthlyPresentRecordMapper.batchUpdateStatusCode(Mockito.anyList(),Mockito.anyString())).thenReturn(0);
        assertFalse(monthlyPresentRecordService.batchUpdateStatusCode(records, "aaa"));
        assertFalse(monthlyPresentRecordService.batchUpdateStatusCode(new ArrayList<Long>(), "aaa"));
        assertFalse(monthlyPresentRecordService.batchUpdateStatusCode(null, "aaa"));
        
    }
}

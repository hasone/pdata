package com.cmcc.vrp.province.service.impl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.EntStatusRecordMapper;
import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.province.service.EntStatusRecordService;
import com.cmcc.vrp.province.service.EntStatusRecordServiceImpl;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: EntStatusRecordServiceImplTest 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年4月25日 下午4:39:30
 */
@RunWith(MockitoJUnitRunner.class)
public class EntStatusRecordServiceImplTest {
    
    @InjectMocks
    EntStatusRecordService entStatusRecordService = new EntStatusRecordServiceImpl();
    
    @Mock
    EntStatusRecordMapper entStatusRecordMapper;

    @Test
    public void insertTest(){
        assertFalse(entStatusRecordService.insert(null));
        
        when(entStatusRecordMapper.insertSelective(any(EntStatusRecord.class))).thenReturn(1);
        assertTrue(entStatusRecordService.insert(new EntStatusRecord()));

    }
    
    @Test
    public void selectByPrimaryKeyTest(){
        assertNull(entStatusRecordService.selectByPrimaryKey(null));
        
        
        EntStatusRecord record = new EntStatusRecord();
        when(entStatusRecordMapper.selectByPrimaryKey(any(Long.class))).thenReturn(record);
        assertEquals(record, entStatusRecordService.selectByPrimaryKey(1L));
        
    }
    
    @Test
    public void showEntStatusRecordForPageResult(){
        assertNull(entStatusRecordService.showEntStatusRecordForPageResult(null));
        
        
        List<EntStatusRecord> records = new ArrayList<EntStatusRecord>();
        when(entStatusRecordMapper.showEntStatusRecordForPageResult(any(Map.class))).thenReturn(records);
        assertEquals(records, entStatusRecordService.showEntStatusRecordForPageResult(new QueryObject()));
    }
    
    @Test
    public void showEntStatusRecordCount(){
        assertEquals(0L, entStatusRecordService.showEntStatusRecordCount(null));
        
        when(entStatusRecordMapper.showEntStatusRecordCount(any(Map.class))).thenReturn(10L);
        assertEquals(10L, entStatusRecordService.showEntStatusRecordCount(new QueryObject()));
    }
}

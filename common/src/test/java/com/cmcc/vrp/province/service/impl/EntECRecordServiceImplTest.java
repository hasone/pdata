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

import com.cmcc.vrp.province.dao.EntECRecordMapper;
import com.cmcc.vrp.province.model.EntECRecord;
import com.cmcc.vrp.province.service.EntECRecordService;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: EntECRecordServiceImplTest 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年4月25日 下午5:04:21
 */
@RunWith(MockitoJUnitRunner.class)
public class EntECRecordServiceImplTest {

    @InjectMocks
    EntECRecordService entECRecordService = new EntECRecordServiceImpl();

    @Mock
    EntECRecordMapper entECRecordMapper;

    @Test
    public void insertTest() {
        assertFalse(entECRecordService.insert(null));

        when(entECRecordMapper.insertSelective(any(EntECRecord.class))).thenReturn(1);
        assertTrue(entECRecordService.insert(new EntECRecord()));

    }

    @Test
    public void selectByPrimaryKeyTest() {
        assertNull(entECRecordService.selectByPrimaryKey(null));

        EntECRecord record = new EntECRecord();
        when(entECRecordMapper.selectByPrimaryKey(any(Long.class))).thenReturn(record);
        assertEquals(record, entECRecordService.selectByPrimaryKey(1L));

    }

    @Test
    public void showEntEcRecordForPageResultTest() {
        assertNull(entECRecordService.showEntEcRecordForPageResult(null));

        List<EntECRecord> records = new ArrayList<EntECRecord>();
        when(entECRecordMapper.showEntEcRecordForPageResult(any(Map.class))).thenReturn(records);
        assertEquals(records, entECRecordService.showEntEcRecordForPageResult(new QueryObject()));
    }

    @Test
    public void showEntEcRecordCountTest() {
        assertEquals(0L, entECRecordService.showEntEcRecordCount(null));

        when(entECRecordMapper.showEntEcRecordCount(any(Map.class))).thenReturn(10L);
        assertEquals(10L, entECRecordService.showEntEcRecordCount(new QueryObject()));
    }
    
    /**
     * 
     */
    @Test
    public void testGetLatestEntEcRecords(){
        Long entId = 1L;
        when(entECRecordMapper.getLatestEntEcRecords(entId)).thenReturn(null);
        assertNull(entECRecordService.getLatestEntEcRecords(entId));
    }
}

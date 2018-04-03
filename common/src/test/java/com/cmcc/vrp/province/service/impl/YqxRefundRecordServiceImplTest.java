package com.cmcc.vrp.province.service.impl;



import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.YqxRefundRecordMapper;
import com.cmcc.vrp.province.model.YqxRefundRecord;
import com.cmcc.vrp.province.service.YqxRefundRecordService;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * YqxRefundRecordServiceImplTest
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class YqxRefundRecordServiceImplTest {
    @InjectMocks
    YqxRefundRecordService yqxRefundRecordService = new YqxRefundRecordServiceImpl();
    
    @Mock
    YqxRefundRecordMapper yqxRefundRecordMapper;
    
    /**
     * testInsert
     */
    @Test
    public void testInsert(){
        YqxRefundRecord record = initRecord();
        Mockito.when(yqxRefundRecordMapper.insertSelective(record)).thenReturn(1);
        Assert.assertTrue(yqxRefundRecordService.insert(record));
        Mockito.when(yqxRefundRecordMapper.insertSelective(record)).thenReturn(0);
        Assert.assertFalse(yqxRefundRecordService.insert(record));
    }
    
    /**
     * testSelectByPrimaryKey
     */
    @Test
    public void testSelectByPrimaryKey(){
        Long id = 1L;
        YqxRefundRecord record = initRecord();
        Mockito.when(yqxRefundRecordMapper.selectByPrimaryKey(id)).thenReturn(record);
        Assert.assertEquals(yqxRefundRecordService.selectByPrimaryKey(id), record);
    }
    
    /**
     * testUpdateByPrimaryKeySelective
     */
    @Test
    public void testUpdateByPrimaryKeySelective(){
        YqxRefundRecord record = initRecord();
        Mockito.when(yqxRefundRecordMapper.updateByPrimaryKeySelective(record)).thenReturn(1);
        Assert.assertTrue(yqxRefundRecordService.updateByPrimaryKeySelective(record));
        Mockito.when(yqxRefundRecordMapper.updateByPrimaryKeySelective(record)).thenReturn(0);
        Assert.assertFalse(yqxRefundRecordService.updateByPrimaryKeySelective(record));   
    }
    
    @Test
    public void testUpdateByDoneCodeAcceptedRecord(){
        String doneCode = "aaa";
        int status = 1;
        String msg = "aaa";
        
        Mockito.when(yqxRefundRecordMapper.updateByDoneCodeAcceptedRecord
                (doneCode, status, msg)).thenReturn(1);
        Assert.assertTrue(yqxRefundRecordService.updateByDoneCodeAcceptedRecord(doneCode, status, msg));
        Mockito.when(yqxRefundRecordMapper.updateByDoneCodeAcceptedRecord
                (doneCode, status, msg)).thenReturn(0);
        Assert.assertFalse(yqxRefundRecordService.updateByDoneCodeAcceptedRecord(doneCode, status, msg));
        
    }
    
    /**
     * testQueryPaginationRefundList
     */
    @Test
    public void testQueryPaginationRefundCount(){
        Mockito.when(yqxRefundRecordMapper.queryPaginationRefundCount(Mockito.anyMap())).thenReturn(0);
        QueryObject map = new QueryObject();
        map.getQueryCriterias().put("searchTime", "2017-01-01 12:00:00~2017-01-02 12:00:00");
        Assert.assertEquals(yqxRefundRecordService.queryPaginationRefundCount(map), 0);
    }
    
    /**
     * testQueryPaginationRefundList
     */
    @Test
    public void testQueryPaginationRefundList(){
        List<YqxRefundRecord> list = new ArrayList<YqxRefundRecord>();
        Mockito.when(yqxRefundRecordMapper.queryPaginationRefundList(Mockito.anyMap())).thenReturn(list);
        QueryObject map = new QueryObject();
        map.getQueryCriterias().put("searchTime", "2017-01-01 12:00:00~2017-01-02 12:00:00");        
        Assert.assertEquals(yqxRefundRecordService.queryPaginationRefundList(map), list);
    }
    
    @Test
    public void testSelectByRefundSerialNum(){
        Mockito.when(yqxRefundRecordMapper.selectByRefundSerialNum(Mockito.anyString())).thenReturn(new YqxRefundRecord());
        assertNotNull(yqxRefundRecordService.selectByRefundSerialNum("test"));
    }
    
    @Test
    public void testSelectByDoneCodeAndStatus(){
        Mockito.when(yqxRefundRecordMapper.selectByDoneCodeAndStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<YqxRefundRecord>());
        assertNotNull(yqxRefundRecordService.selectByDoneCodeAndStatus("test","1"));
    }
    
    /**
     * initRecord
     */
    private YqxRefundRecord initRecord(){
        YqxRefundRecord record = new YqxRefundRecord();
        record.setId(1L);
        record.setDoneCode("aaa");
        return record;
    }
}

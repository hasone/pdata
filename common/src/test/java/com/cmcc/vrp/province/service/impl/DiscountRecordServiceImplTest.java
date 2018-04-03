package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.DiscountRecordMapper;
import com.cmcc.vrp.province.model.DiscountRecord;
import com.cmcc.vrp.province.service.DiscountRecordService;
import com.cmcc.vrp.util.DateUtil;

/**
 * 
 * @ClassName: DiscountRecordServiceImplTest 
 * @Description: TODO
 */
@RunWith(MockitoJUnitRunner.class)
public class DiscountRecordServiceImplTest {
    @InjectMocks
    DiscountRecordService discountRecordService = new DiscountRecordServiceImpl();

    @Mock
    DiscountRecordMapper discountRecordMapper;

    /**
     * 
     */
    @Test
    public void testInsert() {

        DiscountRecord discountRecord = new DiscountRecord();
        discountRecord.setPrdCode("aaa");
        discountRecord.setUserId("bbb");

        //正常返回
        Mockito.when(discountRecordMapper.insertSelective(discountRecord)).thenReturn(1);
        Assert.assertTrue(discountRecordService.insert(discountRecord));

        //错误1返回
        Mockito.when(discountRecordMapper.insertSelective(discountRecord)).thenReturn(0);
        Assert.assertFalse(discountRecordService.insert(discountRecord));

        //错误2返回
        discountRecord.setPrdCode(null);
        Assert.assertFalse(discountRecordService.insert(discountRecord));

        //错误3返回
        discountRecord.setUserId(null);
        Assert.assertFalse(discountRecordService.insert(discountRecord));
    }

    /**
     * 
     */
    @Test
    public void testBatchInsert() {
        List<DiscountRecord> list = null;

        //参数校验		 
        //错误返回
        Assert.assertTrue(discountRecordService.batchInsert(list));

        //错误返回
        list = new ArrayList<DiscountRecord>();
        ;
        Assert.assertTrue(discountRecordService.batchInsert(list));

        //正常返回
        DiscountRecord discountRecord = new DiscountRecord();
        discountRecord.setPrdCode("aaa");
        discountRecord.setUserId("bbb");
        list.add(discountRecord);

        Mockito.when(discountRecordMapper.batchInsert(list)).thenReturn(list.size());
        Assert.assertTrue(discountRecordService.batchInsert(list));

        //错误返回
        Mockito.when(discountRecordMapper.batchInsert(list)).thenReturn(list.size() + 1);
        Assert.assertFalse(discountRecordService.batchInsert(list));
    }
    
    /**
     * 
     */
    @Test
    public void testGetOneDayDiscount() {
        List<DiscountRecord> list = new ArrayList<DiscountRecord>();
        Mockito.when(discountRecordMapper.getOneDayDiscount(Matchers.any(Date.class),
                Matchers.any(Date.class),Matchers.anyString(),Matchers.anyString()))
                .thenReturn(list);
        
        Assert.assertEquals(list, discountRecordService.getOneDayDiscount("20161118", "aaa", "119201"));
    }
    
    /**
     * 
     */
    @Test
    public void testFindDiscount(){
        String format = "yyyy-MM-dd HH:mm:ss";
        String pCode = "109201";
        String userId = "aaa";
         
        DiscountRecord record1 = new DiscountRecord();
        record1.setCreateTime(DateUtil.parse(format, "2016-11-18 16:00:00"));
        record1.setUserId(userId);
        record1.setPrdCode(pCode);
        record1.setDiscount(10);
        
        DiscountRecord record2 = new DiscountRecord();
        record2.setCreateTime(DateUtil.parse(format, "2016-11-18 12:00:00"));
        record2.setUserId(userId);
        record2.setPrdCode(pCode);
        record2.setDiscount(6);
        
        List<DiscountRecord> list = new LinkedList<DiscountRecord>();
        list.add(record1);
        list.add(record2);
        
        Assert.assertEquals("10", discountRecordService.findDiscount(list, "20161118190000"));
        Assert.assertEquals("6", discountRecordService.findDiscount(list, "20161118140000"));
        Assert.assertNull(discountRecordService.findDiscount(list, "20161118100000"));
        
        //测试错误参数
        Assert.assertNull(discountRecordService.findDiscount(null,""));
        Assert.assertNull(discountRecordService.findDiscount(new ArrayList<DiscountRecord>(),""));
        
    }
}

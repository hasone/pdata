package com.cmcc.vrp.province.service.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.pay.model.PayBillModel;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.dao.PayBillModelMapper;
import com.cmcc.vrp.province.dao.YqxPayRecordMapper;
import com.cmcc.vrp.province.model.YqxPayReconcileRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.YqxPayRecordService;
import com.cmcc.vrp.province.service.YqxRefundRecordService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * YqxPayRecordServiceImplTest.java
 * @author qihang
 * @date 2017年5月9日
 */
@RunWith(MockitoJUnitRunner.class)
public class YqxPayRecordServiceImplTest {
    @InjectMocks
    YqxPayRecordService service = new YqxPayRecordServiceImpl();
    
    @Mock
    YqxPayRecordMapper mapper;
    
    @Mock
    CacheService cacheService;
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    YqxRefundRecordService yqxRefundRecordService;
    
    @Mock
    PayBillModelMapper payBillModelMapper;
    
    @Before
    public void initMocks() {      
        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("chongqing");      
    }
    
    /**
     * testInsert
     */
    @Test
    public void testInsert() {
        YqxPayRecord record = initRecord();
        Mockito.when(mapper.insertSelective(record)).thenReturn(1);
        Assert.assertTrue(service.insert(record));
        
        Mockito.when(mapper.insertSelective(record)).thenReturn(0);
        Assert.assertFalse(service.insert(record));
    }
    
    /**
     * testSelectByPrimaryKey
     */
    @Test
    public void testSelectByPrimaryKey() {
        YqxPayRecord record = initRecord();
        Mockito.when(mapper.selectByPrimaryKey(record.getId())).thenReturn(record);
        Assert.assertEquals(service.selectByPrimaryKey(record.getId()), record);
    }
    
    /**
     * testSelectByOrderSerialNum
     */
    @Test
    public void testSelectByOrderSerialNum() {
        YqxPayRecord record = initRecord();
        List<YqxPayRecord> list = initList();
        Mockito.when(mapper.selectByOrderSerialNum(record.getOrderSerialNum())).thenReturn(list);
        Assert.assertEquals(service.selectByOrderSerialNum(record.getOrderSerialNum()), list);
    }
    
    /**
     * testSelectByPayIds
     */
    @Test
    public void testSelectByPayIds() {
        YqxPayRecord record = initRecord();
        List<YqxPayRecord> list = initList();
        Mockito.when(mapper.selectByPayIds(record.getPayOrderId(), record.getPayTransactionId())).thenReturn(list);
        Assert.assertEquals(service.selectByPayIds(record.getPayOrderId(), record.getPayTransactionId()), list.get(0));
    }
    
    /**
     * testUpdateByPrimaryKeySelective
     */
    @Test
    public void testUpdateByPrimaryKeySelective() {
        YqxPayRecord record = initRecord();

        Mockito.when(mapper.updateByPrimaryKeySelective(record)).thenReturn(1);
        Assert.assertEquals(service.updateByPrimaryKeySelective(record), true);
    }
    
    /**
     * testGetNewTransactionId
     */
    @Test
    public void testGetNewTransactionId() {
        Mockito.when(cacheService.getIncrOrUpdate(Mockito.anyString(), Mockito.anyInt())).thenReturn(1L);
        Assert.assertNotNull(service.getNewTransactionId());
    }
    
    /** 
     * @Title: testSelectNewestSuccessRecord 
     */
    @Test
    public void testSelectNewestSuccessRecord() {
        Mockito.when(mapper.selectNewestSuccessRecord(Mockito.anyString())).thenReturn(new YqxPayRecord());
        Assert.assertNotNull(service.selectNewestSuccessRecord("test"));
    }
    
    /** 
     * @Title: testSelectByTransactionId 
     */
    @Test
    public void testSelectByTransactionId() {
        Mockito.when(mapper.selectByTransactionId(Mockito.anyString())).thenReturn(new YqxPayRecord());
        Assert.assertNotNull(service.selectByTransactionId("test"));
    }
    
    /**
     * testSelectByDoneCode
     */
    @Test
    public void testSelectByDoneCode() {
        List<YqxPayRecord> list = initList();
        String doneCode = "aaa";
        Mockito.when(mapper.selectByDoneCode(doneCode)).thenReturn(list);
        Assert.assertNotNull(service.selectByDoneCode(doneCode));
        
        list = new ArrayList<YqxPayRecord>();
        Mockito.when(mapper.selectByDoneCode(doneCode)).thenReturn(list);
        Assert.assertNull(service.selectByDoneCode(doneCode));
    }
    
    @Test
    public void testCountRepeatPayByMap() {
        Map map = new HashMap();
        map.put("searchTime", "2017051912:30~2017051917:30");
        
        Mockito.when(mapper.countRepeatPayByMap(Mockito.anyMap())).thenReturn(1);
        
        Assert.assertEquals(service.countRepeatPayByMap(map).intValue(), 1);
    }
    
    @Test
    public void testSelectRepeatPayByMap() {
        Map map = new HashMap();
        map.put("searchTime", "2017051912:30~2017051917:30");
        
        Mockito.when(mapper.selectRepeatPayByMap(Mockito.anyMap())).thenReturn(new ArrayList());
        
        Assert.assertNotNull(service.selectRepeatPayByMap(map));
    }
    
    @Test
    public void testCountByMap() {
        Map map = new HashMap();
        map.put("searchTime", "2017051912:30~2017051917:30");        
        Mockito.when(mapper.countByMap(Mockito.anyMap())).thenReturn(1);        
        Assert.assertEquals(service.countByMap(map).intValue(), 1);
    }
    
    @Test
    public void testSelectByMap() {
        Map map = new HashMap();
        map.put("searchTime", "2017051912:30~2017051917:30");
        List<YqxPayRecord> records = new ArrayList<YqxPayRecord>();
        YqxPayRecord payRecord = new YqxPayRecord();
        payRecord.setChargeStatus(ChargeRecordStatus.FAILED.getCode());
        payRecord.setDoneCode("test");
        records.add(payRecord);
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(records);        
        Mockito.when(yqxRefundRecordService.selectByDoneCodeAndStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        Assert.assertNotNull(service.selectByMap(map));
    }
    
    /**
     * testReconcileCountByMap
     */
    @Test
    public void testReconcileCountByMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("searchTime", "2017-01-01 12:00:00~2017-01-02 12:00:00");
        Mockito.when(mapper.reconcileCount(Mockito.anyMap())).thenReturn(1);
        Assert.assertEquals(service.reconcileCountByMap(map).intValue(), 1);  
    }
    
    /**
     * testReconcileSelectByMap
     */
    @Test
    public void testReconcileSelectByMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Mockito.when(mapper.reconcileRecords(Mockito.anyMap())).thenReturn(new ArrayList<YqxPayReconcileRecord>());
        Assert.assertNotNull(service.reconcileSelectByMap(map));
    }
    
    @Test
    public void testPayBillCountByMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("searchTime", "2017-01-01 12:00:00~2017-01-02 12:00:00");
        Mockito.when(payBillModelMapper.billCount(Mockito.anyMap())).thenReturn(1);
        Assert.assertEquals(service.payBillCountByMap(map).intValue(), 1);  
    }
    
    @Test
    public void testPayBillSelectByMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Mockito.when(payBillModelMapper.billRecords(Mockito.anyMap())).thenReturn(new ArrayList());
        Assert.assertNotNull(service.payBillSelectByMap(map));  
    }
    
    @Test
    public void testReconcileRangeTime() {
        Mockito.when(mapper.reconcileRangeTime(Mockito.anyMap())).thenReturn(new ArrayList());
        Assert.assertNotNull(service.reconcileRangeTime("2017-01-01"));  
    }
    
    @Test
    public void testUpdateReconcileInfo() {
        Mockito.when(mapper.updateReconcileInfo(Mockito.any(YqxPayRecord.class))).thenReturn(1);
        Assert.assertEquals(service.updateReconcileInfo(new YqxPayRecord()),1);  
    }
    
    @Test
    public void testInsertYqxBill() {
        Mockito.when(payBillModelMapper.insertSelective(Mockito.any(PayBillModel.class))).thenReturn(1);
        Assert.assertTrue(service.insertYqxBill(new PayBillModel()));  
        
        Mockito.when(payBillModelMapper.insertSelective(Mockito.any(PayBillModel.class))).thenReturn(0);
        Assert.assertFalse(service.insertYqxBill(new PayBillModel()));  
    }
    
    @Test
    public void testUpdateChargeStatus() {
        Mockito.when(mapper.updateChargeStatus(Mockito.anyInt(), Mockito.anyString(), Mockito.any(Date.class))).thenReturn(1);
        Assert.assertTrue(service.updateChargeStatus(1,"test", new Date()));
    }
    
    /**
     * initRecord()
     */
    private YqxPayRecord initRecord(){
        YqxPayRecord record = new YqxPayRecord();
        record.setId(1L);
        record.setCreateTime(new Date());
        record.setOrderSerialNum("111");
        record.setPayOrderId("111");
        record.setPayTransactionId("111");
        record.setResultReturnTime(new Date());
        record.setStatus(1);
        record.setStatusInfo("success");
        record.setUpdateTime(new Date());
        return record;
    }
    
    /**
     * initList()
     */
    private List<YqxPayRecord> initList(){
        YqxPayRecord record = initRecord();
        List<YqxPayRecord> list = new ArrayList<YqxPayRecord>();
        list.add(record);
        return list;
    }
}

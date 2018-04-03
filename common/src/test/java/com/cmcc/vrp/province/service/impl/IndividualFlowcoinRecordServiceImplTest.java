package com.cmcc.vrp.province.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.dao.IndividualFlowcoinRecordMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;
import com.cmcc.vrp.province.model.IndividualFlowcoinPurchase;
import com.cmcc.vrp.province.model.IndividualFlowcoinRecord;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.IndividualFlowcoinExchangeService;
import com.cmcc.vrp.province.service.IndividualFlowcoinPurchaseService;
import com.cmcc.vrp.province.service.IndividualFlowcoinRecordService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 
 * @author wujiamin
 * @date 2016年11月2日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualFlowcoinRecordServiceImplTest {
    @InjectMocks
    IndividualFlowcoinRecordService service = new IndividualFlowcoinRecordServiceImpl();
    @Mock
    IndividualFlowcoinRecordMapper mapper;
    @Mock
    IndividualFlowcoinExchangeService exchangeService;
    @Mock
    IndividualFlowcoinPurchaseService purchaseService;
    @Mock
    ActivitiesService activitiesService;
    @Mock
    ActivityWinRecordService activityWinRecordService;
    
    @Test
    public void testInsert(){
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(1);
        assertTrue(service.insert(new IndividualFlowcoinRecord()));
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(0);
        assertFalse(service.insert(new IndividualFlowcoinRecord()));
        
        verify(mapper,times(2)).insert(Mockito.any(IndividualFlowcoinRecord.class));
    }
    
    @Test
    public void testCountByMap(){
        when(mapper.countByMap(Mockito.anyMap())).thenReturn(1);       
        assertEquals(service.countByMap(new HashMap()),1); 

        verify(mapper).countByMap(Mockito.anyMap());
    }
    
    @Test
    public void testSelectByMap(){
        when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList());       
        assertNotNull(service.selectByMap(new HashMap())); 

        verify(mapper).selectByMap(Mockito.anyMap());
    }
    
    @Test
    public void testCreateRecord1(){
        IndividualAccountRecord accountRecord = createOutAccountRecord();
        accountRecord.setType((int) AccountRecordType.OUTGO.getValue());
        accountRecord.setActivityType(111);
        assertTrue(service.createRecord(accountRecord));
        
       
        accountRecord.setActivityType(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getCode());
        when(exchangeService.selectBySystemSerial(Mockito.anyString())).thenReturn(null);
        assertFalse(service.createRecord(accountRecord)); 
        
        when(exchangeService.selectBySystemSerial(Mockito.anyString())).thenReturn(new IndividualFlowcoinExchange());
        assertFalse(service.createRecord(accountRecord)); 
        
        IndividualFlowcoinExchange exchangeRecord = new IndividualFlowcoinExchange();
        exchangeRecord.setMobile("18867103685");
        when(exchangeService.selectBySystemSerial(Mockito.anyString())).thenReturn(exchangeRecord);
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(1);
        assertTrue(service.createRecord(accountRecord)); 
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(0);
        assertFalse(service.createRecord(accountRecord)); 
    }
    
    @Test
    public void testCreateRecord2(){
        IndividualAccountRecord accountRecord = createOutAccountRecord();
        accountRecord.setType((int) AccountRecordType.INCOME.getValue());
        
        accountRecord.setActivityType(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode());
        when(purchaseService.selectBySystemSerial(Mockito.anyString())).thenReturn(null);
        assertFalse(service.createRecord(accountRecord)); 
        
        when(purchaseService.selectBySystemSerial(Mockito.anyString())).thenReturn(new IndividualFlowcoinPurchase());        
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(1);
        assertTrue(service.createRecord(accountRecord)); 
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(0);
        assertTrue(service.createRecord(accountRecord)); 
    }
    
    @Test
    public void testCreateRecord3(){
        IndividualAccountRecord accountRecord = createOutAccountRecord();
        accountRecord.setType((int) AccountRecordType.INCOME.getValue());
              
        accountRecord.setBack(1);
        accountRecord.setActivityType(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getCode());
        
        when(exchangeService.selectBySystemSerial(Mockito.anyString())).thenReturn(null);
        assertFalse(service.createRecord(accountRecord)); 
        
        when(exchangeService.selectBySystemSerial(Mockito.anyString())).thenReturn(new IndividualFlowcoinExchange());
        assertFalse(service.createRecord(accountRecord)); 
        
        IndividualFlowcoinExchange exchangeRecord = new IndividualFlowcoinExchange();
        exchangeRecord.setMobile("18867103685");
        when(exchangeService.selectBySystemSerial(Mockito.anyString())).thenReturn(exchangeRecord);
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(1);
        assertTrue(service.createRecord(accountRecord)); 
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(0);
        assertFalse(service.createRecord(accountRecord)); 
    }
    
    @Test
    public void testCreateRecord4(){
        IndividualAccountRecord accountRecord = createOutAccountRecord();
        accountRecord.setType((int) AccountRecordType.INCOME.getValue());
              
        accountRecord.setBack(1);
        accountRecord.setActivityType(999);
        
        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(null);
        assertFalse(service.createRecord(accountRecord)); 

        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(new Activities());
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(1);
        assertTrue(service.createRecord(accountRecord)); 
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(0);
        assertFalse(service.createRecord(accountRecord)); 
    }
    
    @Test
    public void testCreateRecord5(){
        IndividualAccountRecord accountRecord = createOutAccountRecord();
        accountRecord.setType((int) AccountRecordType.INCOME.getValue());
              
        accountRecord.setBack(0);
        accountRecord.setActivityType(ActivityType.FLOWCOIN_PRESENT.getCode());
        
        when(activityWinRecordService.selectByRecordId(Mockito.anyString())).thenReturn(null);
        assertFalse(service.createRecord(accountRecord)); 

        when(activityWinRecordService.selectByRecordId(Mockito.anyString())).thenReturn(new ActivityWinRecord());
        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(null);
        assertFalse(service.createRecord(accountRecord)); 
        
        Activities activities = new Activities();
        activities.setType(ActivityType.FLOWCOIN_PRESENT.getCode());
        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(activities);
        when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(1);
        assertTrue(service.createRecord(accountRecord)); 
       
        try{
            when(mapper.insert(Mockito.any(IndividualFlowcoinRecord.class))).thenReturn(0);
            assertTrue(service.createRecord(accountRecord)); 
        }catch(RuntimeException e){
            System.out.print(e.getMessage());
        }
 
      
    }
    
    
    private IndividualAccountRecord createOutAccountRecord(){
        IndividualAccountRecord accountRecord = new IndividualAccountRecord();
        accountRecord.setSerialNum("123");
        accountRecord.setId(1L);
        accountRecord.setCount(new BigDecimal(1));
        accountRecord.setAccountId(1L);
        return accountRecord;
    }
    
}

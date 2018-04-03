package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.dao.IndividualFlowcoinExchangeMapper;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowcoinExchangeService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.FlowcoinExchangePojo;

/**
 * @author wujiamin
 * @date 2016年11月2日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualFlowcoinExchangeServiceImplTest {
    @InjectMocks
    IndividualFlowcoinExchangeService service = new IndividualFlowcoinExchangeServiceImpl();
    
    @Mock
    IndividualAccountService accountService;
    
    @Mock
    IndividualProductService productService;
    
    @Mock
    IndividualFlowcoinExchangeMapper mapper;
    
    @Mock
    TaskProducer producer;
    
    @Mock
    IndividualAccountRecordService accountRecordService;
    
    @Test
    public void testInsert(){
        when(mapper.insert(Mockito.any(IndividualFlowcoinExchange.class))).thenReturn(1);
        assertTrue(service.insert(new IndividualFlowcoinExchange()));
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinExchange.class))).thenReturn(0);
        assertFalse(service.insert(new IndividualFlowcoinExchange()));
        
        verify(mapper,times(2)).insert(Mockito.any(IndividualFlowcoinExchange.class));
    }
    
    @Test(expected = RuntimeException.class)
    public void testCreateExchange1(){
        assertFalse(service.createExchange(null,1L,1,"18867103685"));
        assertFalse(service.createExchange(1L,null,1,"18867103685"));
        assertFalse(service.createExchange(1L,1L,null,"18867103685"));
        assertFalse(service.createExchange(1L,1L,0,"18867103685"));
        assertFalse(service.createExchange(1L,1L,1,""));
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinExchange.class))).thenReturn(0);
        service.createExchange(1L,1L,1,"18867103685");
        verify(mapper).insert(Mockito.any(IndividualFlowcoinExchange.class));
    }
    
    @Test(expected = RuntimeException.class)
    public void testCreateExchange2(){
        when(mapper.insert(Mockito.any(IndividualFlowcoinExchange.class))).thenReturn(1);       
        when(accountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(new IndividualAccount());
        when(accountService.createFlowcoinExchangeAndPurchaseAccount(Mockito.any(IndividualAccount.class), Mockito.anyString(),
               Mockito.eq("流量币兑换"), Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getCode()))).thenReturn(false);
        
        assertFalse(service.createExchange(1L,1L,1,"18867103685"));
        verify(mapper).insert(Mockito.any(IndividualFlowcoinExchange.class));
    }
    
    @Test(expected = RuntimeException.class)
    public void testCreateExchange3(){
        when(mapper.insert(Mockito.any(IndividualFlowcoinExchange.class))).thenReturn(1);       
        when(accountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(new IndividualAccount());
        when(accountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);       
        assertFalse(service.createExchange(1L,1L,1,"18867103685"));
        verify(mapper).insert(Mockito.any(IndividualFlowcoinExchange.class));
    }
    
    @Test
    public void testCreateExchange4(){
        when(mapper.insert(Mockito.any(IndividualFlowcoinExchange.class))).thenReturn(1);       
        when(accountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(new IndividualAccount());
        when(accountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(producer.produceFlowcoinExchangeMsg(Mockito.any(FlowcoinExchangePojo.class))).thenReturn(true);       
        assertTrue(service.createExchange(1L,1L,1,"18867103685"));
        verify(mapper).insert(Mockito.any(IndividualFlowcoinExchange.class));
    }
    
    @Test(expected = RuntimeException.class)
    public void testCreateExchange5(){
        when(mapper.insert(Mockito.any(IndividualFlowcoinExchange.class))).thenReturn(1);       
        when(accountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(new IndividualAccount());
        when(accountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(producer.produceFlowcoinExchangeMsg(Mockito.any(FlowcoinExchangePojo.class))).thenReturn(false);       
        assertTrue(service.createExchange(1L,1L,1,"18867103685"));
        verify(mapper).insert(Mockito.any(IndividualFlowcoinExchange.class));
    }
    
    @Test
    public void testUpdateStatus(){
        when(mapper.updateStatus(Mockito.anyLong(),Mockito.anyInt())).thenReturn(1,0);       
        assertTrue(service.updateStatus(1L,  1)); 
        assertFalse(service.updateStatus(1L,  1));
        
        verify(mapper,times(2)).updateStatus(Mockito.anyLong(),Mockito.anyInt());
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
    public void testSelectBySystemSerial(){
        when(mapper.selectBySystemSerial(Mockito.anyString())).thenReturn(new IndividualFlowcoinExchange());       
        assertNotNull(service.selectBySystemSerial(Mockito.anyString())); 

        verify(mapper).selectBySystemSerial(Mockito.anyString());
    }
}

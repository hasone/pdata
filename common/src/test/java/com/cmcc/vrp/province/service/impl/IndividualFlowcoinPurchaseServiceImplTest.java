package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.cmcc.vrp.province.dao.IndividualFlowcoinPurchaseMapper;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualFlowcoinPurchase;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowcoinPurchaseService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;

/**
 * 
 * @author wujiamin
 * @date 2016年11月2日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualFlowcoinPurchaseServiceImplTest {
    @InjectMocks
    IndividualFlowcoinPurchaseService service = new IndividualFlowcoinPurchaseServiceImpl();
    @Mock
    IndividualFlowcoinPurchaseMapper mapper;
    @Mock
    IndividualProductMapService productMapService;
    @Mock
    IndividualProductService productService;
    @Mock
    IndividualAccountRecordService accountRecordService;
    @Mock
    IndividualAccountService accountService;
    
    @Test
    public void testInsert(){
        when(mapper.insert(Mockito.any(IndividualFlowcoinPurchase.class))).thenReturn(1);
        assertTrue(service.insert(new IndividualFlowcoinPurchase()));
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinPurchase.class))).thenReturn(0);
        assertFalse(service.insert(new IndividualFlowcoinPurchase()));
        
        verify(mapper,times(2)).insert(Mockito.any(IndividualFlowcoinPurchase.class));
    }
    
    @Test
    public void testSaveFlowcoinPurchase(){
        when(productService.getFlowcoinProduct()).thenReturn(new IndividualProduct());
        when(productMapService.getByAdminIdAndProductId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);        
        assertFalse(service.saveFlowcoinPurchase(new IndividualFlowcoinPurchase()));
        
        IndividualProductMap map = new IndividualProductMap();
        map.setDiscount(100);
        map.setPrice(1000);
        
        IndividualFlowcoinPurchase record = createIndividualFlowcoinPurchase();
        
        when(productMapService.getByAdminIdAndProductId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(map); 
        when(mapper.insert(Mockito.any(IndividualFlowcoinPurchase.class))).thenReturn(0);
        assertFalse(service.saveFlowcoinPurchase(record));
        
        when(mapper.insert(Mockito.any(IndividualFlowcoinPurchase.class))).thenReturn(1);
        assertTrue(service.saveFlowcoinPurchase(record));
        
        verify(mapper,times(2)).insert(Mockito.any(IndividualFlowcoinPurchase.class));
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
    public void testSelectByPrimaryKey(){
        when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new IndividualFlowcoinPurchase());       
        assertNotNull(service.selectByPrimaryKey(1L)); 

        verify(mapper).selectByPrimaryKey(Mockito.anyLong());
    }   
    
    @Test
    public void testSelectBySystemSerial(){
        when(mapper.selectBySystemSerial(Mockito.anyString())).thenReturn(new IndividualFlowcoinPurchase());       
        assertNotNull(service.selectBySystemSerial(Mockito.anyString())); 

        verify(mapper).selectBySystemSerial(Mockito.anyString());
    }
    
    @Test
    public void testUpdateStatus(){
        when(mapper.updateStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(1,0);       
        assertTrue(service.updateStatus("",  1)); 
        assertFalse(service.updateStatus("",  1));
        
        verify(mapper,times(2)).updateStatus(Mockito.anyString(),Mockito.anyInt());
    }
    
    @Test
    public void testUpdateBySystemSerial(){
        when(mapper.updateBySystemSerial(Mockito.any(IndividualFlowcoinPurchase.class))).thenReturn(1,0);       
        assertTrue(service.updateBySystemSerial(new IndividualFlowcoinPurchase())); 
        assertFalse(service.updateBySystemSerial(new IndividualFlowcoinPurchase()));
        
        verify(mapper,times(2)).updateBySystemSerial(Mockito.any(IndividualFlowcoinPurchase.class));
    }
    
    @Test
    public void testPay1(){
        when(mapper.selectBySystemSerial(Mockito.anyString())).thenReturn(new IndividualFlowcoinPurchase());       
        when(productService.getPhoneFareProduct()).thenReturn(null);
        when(productService.getFlowcoinProduct()).thenReturn(null);
        assertFalse(service.pay(1L, "123"));
       
        when(mapper.selectBySystemSerial(Mockito.anyString())).thenReturn(null);       
        when(productService.getPhoneFareProduct()).thenReturn(null);
        when(productService.getFlowcoinProduct()).thenReturn(null);
        assertFalse(service.pay(1L, "123"));
        
        when(mapper.selectBySystemSerial(Mockito.anyString())).thenReturn(new IndividualFlowcoinPurchase());       
        when(productService.getPhoneFareProduct()).thenReturn(new IndividualProduct());
        when(productService.getFlowcoinProduct()).thenReturn(null);
        assertFalse(service.pay(1L, "123"));       
    }
    
    @Test(expected = RuntimeException.class)
    public void testPay2(){
        when(mapper.selectBySystemSerial(Mockito.anyString())).thenReturn(new IndividualFlowcoinPurchase());       
        when(productService.getPhoneFareProduct()).thenReturn(createProduct());
        when(productService.getFlowcoinProduct()).thenReturn(createProduct());
       
        when(accountService.createFlowcoinExchangeAndPurchaseAccount(Mockito.any(IndividualAccount.class), Mockito.anyString(),
                Mockito.eq("流量币购买"), Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()))).thenReturn(false);
        
        assertFalse(service.pay(1L, "123"));
    }
    
    @Test
    public void testPay3(){
        when(mapper.selectBySystemSerial(Mockito.anyString())).thenReturn(createIndividualFlowcoinPurchase());       
        when(productService.getPhoneFareProduct()).thenReturn(createProduct());
        when(productService.getFlowcoinProduct()).thenReturn(createProduct());
       
        when(accountService.createFlowcoinExchangeAndPurchaseAccount(Mockito.any(IndividualAccount.class), Mockito.anyString(),
                Mockito.eq("流量币购买"), Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()))).thenReturn(true);
        
        when(mapper.updateStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(0);       
        assertFalse(service.pay(1L, "123")); 
        
        when(mapper.updateStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(1);       
        when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyString(),
                Mockito.eq((int) AccountRecordType.INCOME.getValue()), Mockito.eq("流量币购买，请求boss增加流量币(3)"),
                        Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()), Mockito.eq(0))).thenReturn(true);
        when(mapper.updateBySystemSerial(Mockito.any(IndividualFlowcoinPurchase.class))).thenReturn(1); 
        assertTrue(service.pay(1L, "123"));  
       
        when(mapper.updateBySystemSerial(Mockito.any(IndividualFlowcoinPurchase.class))).thenReturn(0); 
        when(accountService.changeFrozenAccount(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(BigDecimal.class),
                Mockito.anyLong(), Mockito.anyString(), Mockito.eq((int) AccountRecordType.OUTGO.getValue()),
                Mockito.eq("流量币购买成功，扣除冻结账户(4)"),  Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()),  Mockito.eq(0))).thenReturn(true);
        
        assertTrue(service.pay(1L, "123"));
        
        when(accountService.changeFrozenAccount(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(BigDecimal.class),
                Mockito.anyLong(), Mockito.anyString(), Mockito.eq((int) AccountRecordType.OUTGO.getValue()),
                Mockito.eq("流量币购买成功，扣除冻结账户(4)"),  Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()),  Mockito.eq(0))).thenReturn(false);
        
        assertTrue(service.pay(1L, "123"));
    }
    
    @Test
    public void testPay4(){
        when(mapper.selectBySystemSerial(Mockito.anyString())).thenReturn(createIndividualFlowcoinPurchase());       
        when(productService.getPhoneFareProduct()).thenReturn(createProduct());
        when(productService.getFlowcoinProduct()).thenReturn(createProduct());
       
        when(accountService.createFlowcoinExchangeAndPurchaseAccount(Mockito.any(IndividualAccount.class), Mockito.anyString(),
                Mockito.eq("流量币购买"), Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()))).thenReturn(true);

        when(mapper.updateStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(1);       
        when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyString(),
                Mockito.eq((int) AccountRecordType.INCOME.getValue()), Mockito.eq("流量币购买，请求boss增加流量币(3)"),
                        Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()), Mockito.eq(0))).thenReturn(false);

        when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyString(),
                Mockito.eq((int) AccountRecordType.INCOME.getValue()), Mockito.eq("流量币购买，请求boss增加流量币(3)"),
                        Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()), Mockito.eq(0))).thenReturn(false);
        when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyString(),
                Mockito.eq((int) AccountRecordType.INCOME.getValue()), Mockito.eq("购买流量币失败，请求boss增加话费(3)"),
                        Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()), Mockito.eq(1))).thenReturn(false);
                
        assertTrue(service.pay(1L, "123"));
        
        when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyString(),
                Mockito.eq((int) AccountRecordType.INCOME.getValue()), Mockito.eq("购买流量币失败，请求boss增加话费(3)"),
                        Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()), Mockito.eq(1))).thenReturn(true);
        
        when(accountService.changeFrozenAccount(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(BigDecimal.class),
                Mockito.anyLong(), Mockito.anyString(), Mockito.eq((int) AccountRecordType.OUTGO.getValue()),
                Mockito.eq("购买流量币失败，BOSS话费增加成功，扣除冻结账户(4)"),  Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()),  Mockito.eq(0))).thenReturn(false);
        
        assertTrue(service.pay(1L, "123"));
        
        when(accountService.changeFrozenAccount(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(BigDecimal.class),
                Mockito.anyLong(), Mockito.anyString(), Mockito.eq((int) AccountRecordType.OUTGO.getValue()),
                Mockito.eq("购买流量币失败，BOSS话费增加成功，扣除冻结账户(4)"),  Mockito.eq(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode()),  Mockito.eq(0))).thenReturn(true);
        
        assertTrue(service.pay(1L, "123"));
    }
    
    private IndividualFlowcoinPurchase createIndividualFlowcoinPurchase(){
        IndividualFlowcoinPurchase record = new IndividualFlowcoinPurchase();
        record.setCount(1);
        record.setAdminId(1L);
        return record;
    }
    
    private IndividualProduct createProduct(){
        IndividualProduct product = new IndividualProduct();
        product.setPrice(1000);
        product.setType(1);
        product.setProductSize(10240L);
        product.setId(1L);
        return product;
    }
}

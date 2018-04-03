package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.ecinvoker.EcBossOperationResultImpl;
import com.cmcc.vrp.boss.ecinvoker.impl.YqxEcBossServiceImpl;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.AsyncCallbackReq;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.YqxChargeInfo;
import com.cmcc.vrp.province.model.YqxOrderRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.YqxChargeInfoService;
import com.cmcc.vrp.province.service.YqxChargeService;
import com.cmcc.vrp.province.service.YqxOrderRecordService;
import com.cmcc.vrp.province.service.YqxPayRecordService;

/**
 * YqxChargeServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月10日
 */
@RunWith(MockitoJUnitRunner.class)
public class YqxChargeServiceImplTest {
    @InjectMocks
    YqxChargeService service = new YqxChargeServiceImpl();
    
    @Mock
    YqxEcBossServiceImpl bossService;
    @Mock
    YqxOrderRecordService yqxOrderRecordService;
    @Mock
    YqxPayRecordService yqxPayRecordService;
    @Mock
    ProductService productService;
    @Mock
    IndividualProductService individualProductService;
    @Mock
    YqxChargeInfoService yqxChargeInfoService;
    
    @Test
    public void testCharge(){
        Mockito.when(yqxPayRecordService.selectByTransactionId(Mockito.anyString())).thenReturn(null);
        assertFalse(service.charge("test"));
        
        YqxPayRecord yqxPayRecord = new YqxPayRecord();
        yqxPayRecord.setOrderSerialNum("orderSerialNum");
        
        Mockito.when(yqxPayRecordService.selectByTransactionId(Mockito.anyString())).thenReturn(yqxPayRecord);
        Mockito.when(yqxOrderRecordService.selectBySerialNum(Mockito.anyString())).thenReturn(null);
        assertFalse(service.charge("test"));
        
        YqxOrderRecord orderRecord = new YqxOrderRecord();
        orderRecord.setChargeStatus(3);
        Mockito.when(yqxOrderRecordService.selectBySerialNum(Mockito.anyString())).thenReturn(orderRecord);
        assertFalse(service.charge("test"));
    }
    
    @Test
    public void testCharge1(){
        YqxPayRecord yqxPayRecord = new YqxPayRecord();
        yqxPayRecord.setOrderSerialNum("orderSerialNum");
        
        Mockito.when(yqxPayRecordService.selectByTransactionId(Mockito.anyString())).thenReturn(yqxPayRecord);
        YqxOrderRecord orderRecord = new YqxOrderRecord();
        Mockito.when(yqxOrderRecordService.selectBySerialNum(Mockito.anyString())).thenReturn(orderRecord);
        Mockito.when(individualProductService.selectByPrimaryId(Mockito.anyLong())).thenReturn(null);
        assertFalse(service.charge("test"));
        
        Mockito.when(individualProductService.selectByPrimaryId(Mockito.anyLong())).thenReturn(new IndividualProduct());
        Mockito.when(productService.selectByCode(Mockito.anyString())).thenReturn(null);
        assertFalse(service.charge("test"));
        
        Mockito.when(productService.selectByCode(Mockito.anyString())).thenReturn(new Product());
        Mockito.when(yqxChargeInfoService.insert(Mockito.any(YqxChargeInfo.class))).thenReturn(false);        
        assertFalse(service.charge("test"));
        
        Mockito.when(yqxChargeInfoService.insert(Mockito.any(YqxChargeInfo.class))).thenReturn(true);
        Mockito.when(yqxOrderRecordService.updateByPrimaryKey(Mockito.any(YqxOrderRecord.class))).thenReturn(false);
        assertFalse(service.charge("test"));
        
    }
    
    @Test
    public void testCharge2(){
        YqxPayRecord yqxPayRecord = new YqxPayRecord();
        yqxPayRecord.setOrderSerialNum("orderSerialNum");
        
        Mockito.when(yqxPayRecordService.selectByTransactionId(Mockito.anyString())).thenReturn(yqxPayRecord);
        YqxOrderRecord orderRecord = new YqxOrderRecord();
        Mockito.when(yqxOrderRecordService.selectBySerialNum(Mockito.anyString())).thenReturn(orderRecord);
        
        
        Mockito.when(individualProductService.selectByPrimaryId(Mockito.anyLong())).thenReturn(new IndividualProduct());
        
        Mockito.when(productService.selectByCode(Mockito.anyString())).thenReturn(new Product());
       
        Mockito.when(yqxChargeInfoService.insert(Mockito.any(YqxChargeInfo.class))).thenReturn(true);
      
        Mockito.when(yqxOrderRecordService.updateByPrimaryKey(Mockito.any(YqxOrderRecord.class))).thenReturn(true);
        Mockito.when(bossService.charge(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(EcBossOperationResultImpl.SUCCESS);
        assertTrue(service.charge("test"));
    }
    
    @Test
    public void testCharge3(){
        YqxPayRecord yqxPayRecord = new YqxPayRecord();
        yqxPayRecord.setOrderSerialNum("orderSerialNum");
        
        Mockito.when(yqxPayRecordService.selectByTransactionId(Mockito.anyString())).thenReturn(yqxPayRecord);
        YqxOrderRecord orderRecord = new YqxOrderRecord();
        Mockito.when(yqxOrderRecordService.selectBySerialNum(Mockito.anyString())).thenReturn(orderRecord);
        
        
        Mockito.when(individualProductService.selectByPrimaryId(Mockito.anyLong())).thenReturn(new IndividualProduct());
        
        Mockito.when(productService.selectByCode(Mockito.anyString())).thenReturn(new Product());
       
        Mockito.when(yqxChargeInfoService.insert(Mockito.any(YqxChargeInfo.class))).thenReturn(true);
      
        Mockito.when(yqxOrderRecordService.updateByPrimaryKey(Mockito.any(YqxOrderRecord.class))).thenReturn(true).thenReturn(false);

        Mockito.when(bossService.charge(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(EcBossOperationResultImpl.FAIL);
        
        assertFalse(service.charge("test"));
        
    }
    
    @Test
    public void testProcessingCallback1(){
        Mockito.when(yqxPayRecordService.selectByTransactionId(Mockito.anyString())).thenReturn(null);
        assertFalse(service.processingCallback(new AsyncCallbackReq()));
        
        Mockito.when(yqxPayRecordService.selectByTransactionId(Mockito.anyString())).thenReturn(new YqxPayRecord());
        Mockito.when(yqxOrderRecordService.selectBySerialNum(Mockito.anyString())).thenReturn(null);
        assertFalse(service.processingCallback(new AsyncCallbackReq()));
        
        YqxOrderRecord orderRecord = new YqxOrderRecord();
        orderRecord.setPayTransactionId("test");
        Mockito.when(yqxOrderRecordService.selectBySerialNum(Mockito.anyString())).thenReturn(orderRecord);

        Mockito.when(yqxOrderRecordService.updateByPrimaryKey(Mockito.any(YqxOrderRecord.class))).thenReturn(true).thenReturn(false);
        AsyncCallbackReq request = new AsyncCallbackReq();
        request.setSystemSerialNum("test");
        assertTrue(service.processingCallback(request));
        request.setSystemSerialNum("test1");
        assertFalse(service.processingCallback(request));
        
    }
    
    @Test
    public void testProcessingCallback2(){
        Mockito.when(yqxPayRecordService.selectByTransactionId(Mockito.anyString())).thenReturn(new YqxPayRecord());
        YqxOrderRecord orderRecord = new YqxOrderRecord();
        orderRecord.setPayTransactionId("test");
        orderRecord.setChargeStatus(ChargeRecordStatus.COMPLETE.getCode());
        Mockito.when(yqxOrderRecordService.selectBySerialNum(Mockito.anyString())).thenReturn(orderRecord);
        AsyncCallbackReq request = new AsyncCallbackReq();
        request.setSystemSerialNum("test");
        assertFalse(service.processingCallback(request));        
    }
    
    
    
}

package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.shangdong.boss.service.impl.SdCloudBossQueryServiceImpl;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.google.gson.Gson;

/**
 * 测试ChargeRecordQueryServiceImpl
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ChargeRecordQueryServiceImplTest {
    @InjectMocks
    ChargeRecordQueryServiceImpl chargeRecordQueryService = new ChargeRecordQueryServiceImpl();
    
    @Mock
    ChargeRecordService chargeRecordService;
    
    @Mock
    ApplicationContext applicationContext;
    
    @Mock
    SupplierService supplierService;
    
    @Mock
    SupplierProductService supplierProductService;
    
    @Mock
    SdCloudBossQueryServiceImpl sdQueryService;
    
    @Mock
    AccountService accountService;
    
    @Mock
    CacheService cacheService;
    
    private final String fingerPrint = "shandongcloud";
    
    @Before
    public void init(){
       
    }
    
    /**
     * 测试chooseBossQuery(String fingerPrint)
     */
    @Test
    public void testQueryResultFromBoss(){
        String systemNum = "aaa";
        ChargeRecordQueryServiceImpl service =  (ChargeRecordQueryServiceImpl) Mockito.spy(chargeRecordQueryService);
        
        Mockito.when(sdQueryService.queryStatusAndMsg(systemNum)).thenReturn(null);
        Mockito.doReturn(sdQueryService).when(service).chooseBossQuery(Mockito.anyString());
        Assert.assertNull(service.queryResultFromBoss(systemNum,fingerPrint));
        
        Mockito.doReturn(null).when(service).chooseBossQuery(Mockito.anyString());
        Assert.assertNull(service.queryResultFromBoss(systemNum,fingerPrint));
        
    }
    
    /**
     * 测试chooseBossQuery(String fingerPrint)
     */
    @Test
    public void testRefund(){
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setStatus(3);
        chargeRecord.setSystemNum("aaa");
        
        Mockito.when(accountService.returnFunds(Mockito.anyString())).thenReturn(true);        
        assertTrue(chargeRecordQueryService.refund(chargeRecord));
        
        Mockito.when(accountService.returnFunds(Mockito.anyString())).thenReturn(false);
        assertFalse(chargeRecordQueryService.refund(chargeRecord));
        
        chargeRecord.setStatus(4);
        assertTrue(chargeRecordQueryService.refund(chargeRecord));
        
    }
    
    /**
     * testUpdateResultFromBoss()
     */
    @Test
    public void testUpdateResultFromBoss(){
        ChargeRecordQueryServiceImpl service =  (ChargeRecordQueryServiceImpl) Mockito.spy(chargeRecordQueryService);
        ChargeRecord chargeRecord = initChargeRecord(2);
        
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setSupplierId(1L);
        
        Supplier sdSupplier = new Supplier();
        sdSupplier.setFingerprint(fingerPrint);
        
        Mockito.when(supplierProductService.selectById(chargeRecord.getSupplierProductId())).thenReturn(supplierProduct);
        Mockito.when(supplierService.get(supplierProduct.getSupplierId())).thenReturn(sdSupplier);
        Mockito.when(cacheService.setNxAndExpireTime(Mockito.anyString(),Mockito.anyString(),Mockito.anyInt())).thenReturn(true);
        Mockito.when(cacheService.delete(Mockito.anyString())).thenReturn(true);
        
        //1.测试正常流程的充值成功
        BossQueryResult queryResult =  BossQueryResult.SUCCESS;
        Mockito.doReturn(queryResult).when(service).queryResultFromBoss(chargeRecord.getSystemNum(),sdSupplier.getFingerprint());
        Mockito.when(chargeRecordService.updateStatus(chargeRecord.getId(),ChargeRecordStatus.COMPLETE,queryResult.getMsg())).thenReturn(true);
        assertTrue(service.updateResultFromBoss(chargeRecord));
        
        //2.测试正常流程的充值失败
        chargeRecord = initChargeRecord(2);
        queryResult =  BossQueryResult.FAILD;
        Mockito.doReturn(queryResult).when(service).queryResultFromBoss(chargeRecord.getSystemNum(),sdSupplier.getFingerprint());
        Mockito.doReturn(true).when(service).refund(chargeRecord);
        Mockito.when(chargeRecordService.updateStatus(chargeRecord.getId(),ChargeRecordStatus.FAILED,queryResult.getMsg())).thenReturn(true);
        assertTrue(service.updateResultFromBoss(chargeRecord));
        
        //3.测试正常流程boss返回空
        chargeRecord = initChargeRecord(2);
        Mockito.doReturn(null).when(service).queryResultFromBoss(chargeRecord.getSystemNum(),sdSupplier.getFingerprint());
        assertFalse(service.updateResultFromBoss(chargeRecord));
        
        //4.测试其它参数错误
        Mockito.when(cacheService.setNxAndExpireTime(Mockito.anyString(),Mockito.anyString(),Mockito.anyInt())).thenReturn(false);
        assertFalse(service.updateResultFromBoss(chargeRecord));
        
        Mockito.when(supplierService.get(supplierProduct.getSupplierId())).thenReturn(null);
        assertFalse(service.updateResultFromBoss(chargeRecord));
        
        Mockito.when(supplierProductService.selectById(chargeRecord.getSupplierProductId())).thenReturn(null);
        assertFalse(service.updateResultFromBoss(chargeRecord));
        
        chargeRecord = initChargeRecord(4);
        assertTrue(service.updateResultFromBoss(chargeRecord));
        
        chargeRecord = initChargeRecord(3);
        assertTrue(service.updateResultFromBoss(chargeRecord));
        
        chargeRecord.setStatus(null);
        assertTrue(service.updateResultFromBoss(chargeRecord));
        
        assertTrue(service.updateResultFromBoss(null));
        
    }
    
    /**
     * testQueryStatusBySystemNun()
     */
    @Test
    public void testQueryStatusBySystemNun(){
        ChargeRecordQueryServiceImpl service =  (ChargeRecordQueryServiceImpl) Mockito.spy(chargeRecordQueryService);
        
        String systemNun ="aaa";
        String cacheKey = ChargeRecordQueryServiceImpl.CACHE_KEY_SYSTEMNUN + systemNun;
        
        ChargeRecord record = initChargeRecord(3);
        
        Mockito.when(cacheService.get(cacheKey)).thenReturn(null);
        Mockito.when(chargeRecordService.getRecordBySN(systemNun)).thenReturn(record);
        Mockito.doReturn(true).when(service).updateResultFromBoss(record);
        Mockito.when(cacheService.add(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        
        Assert.assertEquals(service.queryStatusBySystemNun(systemNun),record);
        
        record = initChargeRecord(3);
        Mockito.doReturn(false).when(service).updateResultFromBoss(record);
        Assert.assertNotNull(service.queryStatusBySystemNun(systemNun));
        
        
        Mockito.when(chargeRecordService.getRecordBySN(systemNun)).thenReturn(null);
        Assert.assertNull(service.queryStatusBySystemNun(systemNun));
            
        Mockito.when(cacheService.get(cacheKey)).thenReturn(new Gson().toJson(record));
        Assert.assertNotNull(service.queryStatusBySystemNun(systemNun));
        
        Mockito.when(cacheService.get(cacheKey)).thenReturn("aaa");
        Assert.assertNull(service.queryStatusBySystemNun(systemNun));
    }
    
    /**
     * testQueryStatusBySerialNum()
     */
    @Test
    public void testQueryStatusBySerialNum(){
        ChargeRecordQueryServiceImpl service =  (ChargeRecordQueryServiceImpl) Mockito.spy(chargeRecordQueryService);
        
        String systemNun ="aaa";
        //String cacheKey = ChargeRecordQueryServiceImpl.KEY_SERIALNUM + systemNun;
        
        List<ChargeRecord> list = initChargeRecordList();
        
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(chargeRecordService.selectRecordByEnterIdAndSerialNum(Mockito.anyLong(), Mockito.anyString())).thenReturn(list);
        Mockito.doReturn(true).when(service).updateResultFromBoss(Mockito.any(ChargeRecord.class));
        Mockito.when(cacheService.add(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        
        Assert.assertNotNull(service.queryStatusBySerialNum(1L, systemNun));
        
        Mockito.doReturn(false).when(service).updateResultFromBoss(Mockito.any(ChargeRecord.class));
        Assert.assertNotNull(service.queryStatusBySerialNum(1L, systemNun));
        
        
        Mockito.when(chargeRecordService.selectRecordByEnterIdAndSerialNum(Mockito.anyLong(), Mockito.anyString())).thenReturn(new ArrayList<ChargeRecord>());
        Assert.assertNotNull(service.queryStatusBySerialNum(1L, systemNun));
        
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn(new Gson().toJson(list));
        Assert.assertNotNull(service.queryStatusBySerialNum(1L, systemNun));
        
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("aaa");
        Assert.assertNotNull(service.queryStatusBySerialNum(1L, systemNun));
        
    }
    
    
    
    /**
     * testGetPrefix()
     */
    @Test
    public void testGetPrefix(){
        Assert.assertEquals(chargeRecordQueryService.getPrefix(), "chargeRecord.");
    }
    
    
    /**
     * initChargeRecord  初始化
     */
    private ChargeRecord initChargeRecord(int status){
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setId(1L);
        chargeRecord.setStatus(status);
        chargeRecord.setSystemNum("aaa");
        chargeRecord.setSupplierProductId(1L);
        return chargeRecord;
    }
    
    /**
     * initChargeRecordList  初始化
     */
    private List<ChargeRecord> initChargeRecordList(){
        ChargeRecord chargeRecord = initChargeRecord(3);
        List<ChargeRecord> list = new ArrayList<ChargeRecord>();
        list.add(chargeRecord);

        return list;
    }
    
}

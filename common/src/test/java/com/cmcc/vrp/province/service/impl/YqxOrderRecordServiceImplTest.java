package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.dao.YqxOrderRecordMapper;
import com.cmcc.vrp.province.model.YqxOrderRecord;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.YqxOrderRecordService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * YqxOrderRecordServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月9日
 */
@RunWith(MockitoJUnitRunner.class)
public class YqxOrderRecordServiceImplTest {
    @InjectMocks
    YqxOrderRecordService service = new YqxOrderRecordServiceImpl();
    
    @Mock
    YqxOrderRecordMapper mapper;
    
    @Mock
    CacheService cacheService;
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Test
    public void testInsert() {
        Mockito.when(mapper.insert(Mockito.any(YqxOrderRecord.class))).thenReturn(1);
        assertTrue(service.insert(new YqxOrderRecord()));
    }
    
    @Test
    public void testSelectByMap() {
        Map map = new HashMap();
        map.put("searchTime", "2017051912:30~2017051917:30");
        map.put("refundStatus", "refundStatus");
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(service.selectByMap(map));
    }
    
    @Test
    public void testSelectBySerialNum() {
        Mockito.when(mapper.selectBySerialNum(Mockito.anyString())).thenReturn(new YqxOrderRecord());
        assertNotNull(service.selectBySerialNum("test"));
    }
    
    @Test
    public void testUpdateByPrimaryKey() {
        YqxOrderRecord record = new YqxOrderRecord();
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(YqxOrderRecord.class))).thenReturn(1);
        assertFalse(service.updateByPrimaryKey(null));
        
        assertTrue(service.updateByPrimaryKey(record));        
    }
    
    @Test
    public void testIfOverNum(){
    	Mockito.when(cacheService.get(Mockito.anyString())).thenReturn(null);
    	assertFalse(service.ifOverNum("18867103685"));
    	
    	Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("3");
    	Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("2");
    	assertTrue(service.ifOverNum("18867103685"));
    }
    
    @Test
    public void testCreate(){
    	Mockito.when(cacheService.getIncrOrUpdate(Mockito.anyString(), Mockito.anyInt())).thenReturn(1L);
    	Mockito.when(mapper.insert(Mockito.any(YqxOrderRecord.class))).thenReturn(1);
    	assertTrue(service.create(new YqxOrderRecord()));
    }
    
    
    @Test
    public void testCountByMap() {
        Map map = new HashMap();
        map.put("searchTime", "2017051912:30~2017051917:30");     
        map.put("refundStatus", "refundStatus");
        Mockito.when(mapper.countByMap(Mockito.anyMap())).thenReturn(1);        
        Assert.assertEquals(service.countByMap(map).intValue(), 1);
    }
    
    @Test
    public void testDuringAccountCheckDate(){
        assertFalse(service.duringAccountCheckDate());
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey())).thenReturn("23:00:00");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey())).thenReturn("1");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey())).thenReturn("1:00:00");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey())).thenReturn("1");
    
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey())).thenReturn("0");
        assertFalse(service.duringAccountCheckDate());
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey())).thenReturn("1");
        assertFalse(service.duringAccountCheckDate());
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey())).thenReturn("2");
        assertFalse(service.duringAccountCheckDate());
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey())).thenReturn("23:00:00");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey())).thenReturn("2");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey())).thenReturn("1:00:00");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey())).thenReturn("1");
    
        assertNotNull(service.duringAccountCheckDate());
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey())).thenReturn("23:00:00");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey())).thenReturn("32");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey())).thenReturn("1:00:00");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey())).thenReturn("0");
    
        assertNotNull(service.duringAccountCheckDate());
    }
    
    @Test
    public void testGetVirtualCqNum(){
        Mockito.when(cacheService.incrby(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);
        assertNotNull(service.getVirtualCqNum());
        
        Mockito.when(cacheService.incrby(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1L);
        assertNotNull(service.getVirtualCqNum());        
    }
    
    @Test
    public void testGetPrefix() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        YqxOrderRecordServiceImpl y = new YqxOrderRecordServiceImpl();
        Class y1 = YqxOrderRecordServiceImpl.class;//获得class类
        Method method = y1.getDeclaredMethod("getPrefix");//获得method.注意,这里不能使用getMethod方法,因为这个方法只能获取public修饰的方法..
        method.setAccessible(true);//这个设置为true.可以无视java的封装..不设置这个也无法或者这个Method
        Object result = method.invoke(y);
        Assert.assertEquals("yqx.order.", result);//这里自定拆箱..

    }
        
}

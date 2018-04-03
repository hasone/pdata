/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.dao.GlobalConfigMapper;
import com.cmcc.vrp.province.model.GlobalConfig;
import com.cmcc.vrp.province.service.GlobalConfigService;

/**
 * GlobalConfigServiceImplTest.java
 * @author wujiamin
 * @date 2016年12月1日
 */
@RunWith(MockitoJUnitRunner.class)
public class GlobalConfigServiceImplTest {

    @Mock
    GlobalConfigMapper mapper;
    
    @Mock
    CacheService cacheService;

    @InjectMocks
    GlobalConfigService service = new GlobalConfigServiceImpl();

    @Test
    public void validateTest() {
        
        Throwable re = null;
        try {
            service.validate(null);
            Assert.fail();
        } catch (InvalidParameterException e) {
            re = e;
        }
        assertEquals(InvalidParameterException.class, re.getClass());
        
        GlobalConfig globalConfig = new GlobalConfig();
        re = null;
        try {
            service.validate(globalConfig);
            Assert.fail();
        } catch (InvalidParameterException e) {
            re = e;
        }
        assertEquals(InvalidParameterException.class, re.getClass());
        
        globalConfig.setName("test");
        re = null;
        try {
            service.validate(globalConfig);
            Assert.fail();
        } catch (InvalidParameterException e) {
            re = e;
        }
        assertEquals(InvalidParameterException.class, re.getClass());
        
        globalConfig.setConfigKey("test");
        re = null;
        try {
            service.validate(globalConfig);
            Assert.fail();
        } catch (InvalidParameterException e) {
            re = e;
        }
        assertEquals(InvalidParameterException.class, re.getClass());
        
        globalConfig.setConfigValue("test");
        re = null;
        try {
            service.validate(globalConfig);
            Assert.fail();
        } catch (InvalidParameterException e) {
            re = e;
        }
        assertEquals(InvalidParameterException.class, re.getClass());
        
        globalConfig.setDescription("test");
        Assert.assertTrue(service.validate(globalConfig));
    }
    
    @Test
    public void testInsert(){
        Mockito.when(mapper.insert(Mockito.any(GlobalConfig.class))).thenReturn(1);
        
        Mockito.when(cacheService.add(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        Assert.assertTrue(service.insert(createConfig()));
    }
    
    @Test
    public void testGet(){
        Long id = null;
        Assert.assertNull(service.get(id));
        
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("1");
        service.get(1L);
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        GlobalConfig record = createConfig();
    
        Mockito.when(cacheService.delete(Mockito.anyList())).thenReturn(true);
        
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(GlobalConfig.class))).thenReturn(1);

        Assert.assertTrue(service.updateByPrimaryKeySelective(createConfig()));
    }
    
    @Test
    public void testCountGlobalConfig(){
        Mockito.when(mapper.countGlobalConfig(Mockito.anyMap())).thenReturn(1);

        Assert.assertEquals(service.countGlobalConfig(new HashMap()),1);
        Assert.assertEquals(service.countGlobalConfig(null),0);
    }
    
    @Test
    public void testSelectGlobalConfigPage(){
        Mockito.when(mapper.selectGlobalConfigForPages(Mockito.anyMap())).thenReturn(new ArrayList());

        Assert.assertNotNull(service.selectGlobalConfigPage(new HashMap()));
        Assert.assertNull(service.selectGlobalConfigPage(null));
    }
    
    @Test
    public void testDelete(){
        Mockito.when(mapper.delete(Mockito.anyLong())).thenReturn(1);
        Mockito.when(cacheService.delete(Mockito.anyList())).thenReturn(true);
        Mockito.when(cacheService.get(String.valueOf(Mockito.anyLong()))).thenReturn("1");
        Mockito.when(mapper.get(Mockito.anyLong())).thenReturn(createConfig());
        Mockito.when(cacheService.add(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        
        Assert.assertTrue(service.delete(1L));
    }
    
    @Test
    public void testGet2(){
        Mockito.when(mapper.getAllConfigs()).thenReturn(new ArrayList());
       
        Assert.assertNotNull(service.get());
    }
    
    @Test
    public void testGet3(){
        
        Mockito.when(cacheService.get(String.valueOf(Mockito.anyLong()))).thenReturn("1");
        Mockito.when(mapper.getByConfigKey(Mockito.anyString())).thenReturn(createConfig());
        
        Assert.assertNotNull(service.get("1"));
    }
    
    @Test
    public void testIsEnabled(){
        
        Mockito.when(cacheService.get(String.valueOf(Mockito.anyLong()))).thenReturn("OK");
        Mockito.when(mapper.getByConfigKey(Mockito.anyString())).thenReturn(createConfig());
        
        Assert.assertFalse(service.isEnabled("OK"));
    }
    
    @Test
    public void testUpdateValue(){
        
        Mockito.when(cacheService.get(String.valueOf(Mockito.anyLong()))).thenReturn("OK");
        Mockito.when(mapper.getByConfigKey(Mockito.anyString())).thenReturn(createConfig());
        Mockito.when(cacheService.delete(Mockito.anyList())).thenReturn(true);
        Mockito.when(mapper.updateValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
        Assert.assertTrue(service.updateValue("1","1","1"));
    }
    
    
    private GlobalConfig createConfig(){
        GlobalConfig config = new GlobalConfig();
        config.setId(1L);
        config.setName("name");
        config.setConfigKey("OK");
        config.setConfigValue("value");
        config.setDescription("desc");
        return config;
    }
   
}

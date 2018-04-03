package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.LabelConfigMapper;
import com.cmcc.vrp.province.model.LabelConfig;
import com.cmcc.vrp.province.service.LabelConfigService;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/11/7.
 */
@RunWith(MockitoJUnitRunner.class)
public class LabelConfigServiceImplTest {
    @InjectMocks
    LabelConfigService labelConfigService = new LabelConfigServiceImpl();
    @Mock
    LabelConfigMapper labelConfigMapper;

    @Test
    public void testInsert(){
        LabelConfig labelConfig = new LabelConfig();
        labelConfig.setName("test");
        labelConfig.setDefaultValue("1");
        labelConfig.setDescription("test");

        Mockito.when(labelConfigMapper.insert(any(LabelConfig.class))).thenReturn(1);
        assertTrue(labelConfigService.insert(labelConfig));
        Mockito.verify(labelConfigMapper).insert(any(LabelConfig.class));
    }

    @Test
    public void testGet(){
        assertNull(labelConfigService.get(null));

        Long id = 1L;
        Mockito.when(labelConfigMapper.get(anyLong())).thenReturn(new LabelConfig());
        assertNotNull(labelConfigService.get(id));
        Mockito.verify(labelConfigMapper).get(anyLong());
    }

    @Test
    public void testUpdateByPrimaryKeySelective(){
        LabelConfig record = new LabelConfig();
        record.setId(1L);
        Mockito.when(labelConfigMapper.updateByPrimaryKeySelective(any(LabelConfig.class))).thenReturn(1);
        assertTrue(labelConfigService.updateByPrimaryKeySelective(record));
        Mockito.verify(labelConfigMapper).updateByPrimaryKeySelective(any(LabelConfig.class));
    }

    @Test
    public void testCountLabelConfig(){
        assertSame(0, labelConfigService.countLabelConfig(null));
        Map<String, Object> map = new HashedMap();
        Mockito.when(labelConfigMapper.countLabelConfig(anyMap())).thenReturn(1);
        assertSame(1, labelConfigService.countLabelConfig(map));
        Mockito.verify(labelConfigMapper).countLabelConfig(anyMap());
    }

    @Test
    public void testSelectLabelConfigPage(){
        assertNull(labelConfigService.selectLabelConfigPage(null));

        Map<String, Object> map = new HashedMap();
        Mockito.when(labelConfigMapper.selectLabelConfigForPages(anyMap())).thenReturn(new ArrayList<LabelConfig>());
        assertNotNull(labelConfigService.selectLabelConfigPage(map));
        Mockito.verify(labelConfigMapper).selectLabelConfigForPages(anyMap());
    }

    @Test
    public void testDelete(){
        Mockito.when(labelConfigMapper.delete(anyLong())).thenReturn(1);
        assertTrue(labelConfigService.delete(1L));
        Mockito.verify(labelConfigMapper).delete(anyLong());
    }

    @Test
    public void testGet1(){
        Mockito.when(labelConfigMapper.getAllConfigs()).thenReturn(new ArrayList<LabelConfig>());
        assertNotNull(labelConfigService.get());
        Mockito.verify(labelConfigMapper).getAllConfigs();
    }

    @Test
    public void testGetLabelConfigMap(){
        List<LabelConfig> labelConfigList = new ArrayList<LabelConfig>();
        LabelConfig record = new LabelConfig();
        record.setName("test");
        record.setDefaultValue("test");
        labelConfigList.add(record);
        String keyword = "test";

        Mockito.when(labelConfigMapper.getByName(anyString())).thenReturn(labelConfigList);
        assertNotNull(labelConfigService.getLabelConfigMap(keyword));
        Mockito.verify(labelConfigMapper).getByName(anyString());
    }

    @Test(expected = InvalidParameterException.class)
    public void testValidate(){
        assertFalse(labelConfigService.validate(null));
    }

    @Test(expected = InvalidParameterException.class)
    public void testValidate1(){
        LabelConfig labelConfig = new LabelConfig();
        assertFalse(labelConfigService.validate(labelConfig));
    }

    @Test(expected = InvalidParameterException.class)
    public void testValidate2(){
        LabelConfig labelConfig = new LabelConfig();
        labelConfig.setName("test");
        assertFalse(labelConfigService.validate(labelConfig));
    }

    @Test(expected = InvalidParameterException.class)
    public void testValidate3(){
        LabelConfig labelConfig = new LabelConfig();
        labelConfig.setName("test");
        labelConfig.setDefaultValue("test");
        assertFalse(labelConfigService.validate(labelConfig));
    }

    @Test
    public void testValidate4(){
        LabelConfig labelConfig = new LabelConfig();
        labelConfig.setName("test");
        labelConfig.setDefaultValue("test");
        labelConfig.setDescription("test");
        assertTrue(labelConfigService.validate(labelConfig));
    }

}

package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ProductTemplateEnterpriseMapMapper;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import com.cmcc.vrp.province.service.ProductTemplateEnterpriseMapService;

@RunWith(MockitoJUnitRunner.class)
public class ProductTemplateEnterpriseMapServiceImplTest {
    @InjectMocks
    ProductTemplateEnterpriseMapService productTemplateEnterpriseMapService = 
            new ProductTemplateEnterpriseMapServiceImpl();
    @Mock
    ProductTemplateEnterpriseMapMapper mapper;
    
    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(productTemplateEnterpriseMapService.deleteByPrimaryKey(null));
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
        assertTrue(productTemplateEnterpriseMapService.deleteByPrimaryKey(1L));
        Mockito.verify(mapper).deleteByPrimaryKey(Mockito.anyLong());
    }
    
    @Test
    public void testInsert(){
        assertFalse(productTemplateEnterpriseMapService.insert(null));
        Mockito.when(mapper.insert(Mockito.any(ProductTemplateEnterpriseMap.class))).thenReturn(1);
        assertTrue(productTemplateEnterpriseMapService.insert(new ProductTemplateEnterpriseMap()));
        Mockito.verify(mapper).insert(Mockito.any(ProductTemplateEnterpriseMap.class));
    }
    
    @Test
    public void testInsertSelective(){
        assertFalse(productTemplateEnterpriseMapService.insertSelective(null));
        Mockito.when(mapper.insertSelective(Mockito.any(ProductTemplateEnterpriseMap.class))).thenReturn(1);
        assertTrue(productTemplateEnterpriseMapService.insertSelective(new ProductTemplateEnterpriseMap()));
        Mockito.verify(mapper).insertSelective(Mockito.any(ProductTemplateEnterpriseMap.class));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(productTemplateEnterpriseMapService.updateByPrimaryKeySelective(null));
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(ProductTemplateEnterpriseMap.class))).thenReturn(1);
        assertTrue(productTemplateEnterpriseMapService.updateByPrimaryKeySelective(new ProductTemplateEnterpriseMap()));
        Mockito.verify(mapper).updateByPrimaryKeySelective(Mockito.any(ProductTemplateEnterpriseMap.class));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(productTemplateEnterpriseMapService.updateByPrimaryKey(null));
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(ProductTemplateEnterpriseMap.class))).thenReturn(1);
        assertTrue(productTemplateEnterpriseMapService.updateByPrimaryKey(new ProductTemplateEnterpriseMap()));
        Mockito.verify(mapper).updateByPrimaryKey(Mockito.any(ProductTemplateEnterpriseMap.class));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        assertNull(productTemplateEnterpriseMapService.selectByPrimaryKey(null));
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new ProductTemplateEnterpriseMap());
        assertNotNull(productTemplateEnterpriseMapService.selectByPrimaryKey(1L));
        Mockito.verify(mapper).selectByPrimaryKey(Mockito.anyLong());
    }
}

package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ProductTemplateMapper;
import com.cmcc.vrp.province.model.PlatformProductTemplateMap;
import com.cmcc.vrp.province.model.ProductTemplate;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PlatformProductTemplateMapService;
import com.cmcc.vrp.province.service.ProductTemplateEnterpriseMapService;
import com.cmcc.vrp.province.service.ProductTemplateService;
import com.cmcc.vrp.util.QueryObject;

@RunWith(MockitoJUnitRunner.class)
public class ProductTemplateServiceImplTest {
    
    @InjectMocks
    ProductTemplateService productTemplateService = 
            new ProductTemplateServiceImpl();
    @Mock
    ProductTemplateMapper mapper;
    @Mock
    PlatformProductTemplateMapService platformProductTemplateMapService;
    @Mock
    ProductTemplateEnterpriseMapService productTemplateEnterpriseMapService;
    @Mock
    EntProductService entProductService;
    
    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(productTemplateService.deleteByPrimaryKey(null));
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
        assertTrue(productTemplateService.deleteByPrimaryKey(1L));
        Mockito.verify(mapper).deleteByPrimaryKey(Mockito.anyLong());
    }
    
    @Test
    public void testInsert(){
        assertFalse(productTemplateService.insert(null));
        Mockito.when(mapper.insert(Mockito.any(ProductTemplate.class))).thenReturn(1);
        assertTrue(productTemplateService.insert(new ProductTemplate()));
        Mockito.verify(mapper).insert(Mockito.any(ProductTemplate.class));
    }
    
    @Test
    public void testInsertSelective(){
        assertFalse(productTemplateService.insertSelective(null));
        Mockito.when(mapper.insertSelective(Mockito.any(ProductTemplate.class))).thenReturn(1);
        assertTrue(productTemplateService.insertSelective(new ProductTemplate()));
        Mockito.verify(mapper).insertSelective(Mockito.any(ProductTemplate.class));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(productTemplateService.updateByPrimaryKeySelective(null));
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(ProductTemplate.class))).thenReturn(1);
        assertTrue(productTemplateService.updateByPrimaryKeySelective(new ProductTemplate()));
        Mockito.verify(mapper).updateByPrimaryKeySelective(Mockito.any(ProductTemplate.class));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(productTemplateService.updateByPrimaryKey(null));
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(ProductTemplate.class))).thenReturn(1);
        ProductTemplate productTemplate = new ProductTemplate();
        productTemplate.setId(1L);
        assertTrue(productTemplateService.updateByPrimaryKey(productTemplate));
        Mockito.verify(mapper).updateByPrimaryKey(Mockito.any(ProductTemplate.class));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        assertNull(productTemplateService.selectByPrimaryKey(null));
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new ProductTemplate());
        assertNotNull(productTemplateService.selectByPrimaryKey(1L));
        Mockito.verify(mapper).selectByPrimaryKey(Mockito.anyLong());
    }
    
    @Test
    public void testCountByMap(){
        QueryObject queryObject = new QueryObject();
        Mockito.when(mapper.countByMap(Mockito.anyMap())).thenReturn(1L);
        assertSame(1L, productTemplateService.countByMap(queryObject.toMap()));
    }
    
    @Test
    public void testCreateProductTemplate(){
        assertFalse(productTemplateService.createProductTemplate(null, null));
        
        ProductTemplate productTemplate = createProductTemplate();
        List<PlatformProductTemplateMap> maps = createPlatformProductTemplateMaps();
        
        Mockito.when(mapper.insertSelective(Mockito.any(ProductTemplate.class)))
        .thenReturn(0).thenReturn(1).thenReturn(1);
        
        assertFalse(productTemplateService.createProductTemplate(productTemplate, maps));
        
        Mockito.when(platformProductTemplateMapService.batchInsert(Mockito.anyList()))
        .thenReturn(true).thenReturn(false);
        
        assertTrue(productTemplateService.createProductTemplate(productTemplate, maps));
        try{
            assertFalse(productTemplateService.createProductTemplate(productTemplate, maps));
        }catch(RuntimeException e){
            
        }
    }
    
    @Test
    public void testDeleteProductTemplate(){
        List<ProductTemplateEnterpriseMap> list = createProductTemplateEnterpriseMap();
        Mockito.when(productTemplateEnterpriseMapService.selectByProductTemplateId(Mockito.anyLong()))
        .thenReturn(list);
        
        Mockito.when(productTemplateEnterpriseMapService.deleteByProductTemplateId(Mockito.anyLong()))
        .thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true);
        
        assertFalse(productTemplateService.deleteProductTemplate(1L));
        
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(-1).thenReturn(1).thenReturn(1);
        
        Mockito.when(entProductService.batchDeleteByEnterId(Mockito.anyList()))
        .thenReturn(true).thenReturn(false);
        
        
        try{
            assertFalse(productTemplateService.deleteProductTemplate(1L));
        }catch(RuntimeException e){
            
        }
        
        assertTrue(productTemplateService.deleteProductTemplate(1L));
        
        try{
            assertFalse(productTemplateService.deleteProductTemplate(1L));
        }catch(RuntimeException e){
            
        }
    }
    
    @Test
    public void testEditProductTemplate(){
        assertFalse(productTemplateService.editProductTemplate(null, null));
        ProductTemplate productTemplate = createProductTemplate();
        
        List<ProductTemplateEnterpriseMap> productTemplateEnterpriseMaps = createProductTemplateEnterpriseMap();
        productTemplateEnterpriseMaps.get(0).setId(2L);
        productTemplateEnterpriseMaps.get(0).setProductTemplateId(2L);
        
        List<PlatformProductTemplateMap> platformProductTemplateMaps = createPlatformProductTemplateMaps();
        List<PlatformProductTemplateMap> oldPlatformProductTemplateMaps = createPlatformProductTemplateMaps();
        oldPlatformProductTemplateMaps.get(0).setId(2L);
        oldPlatformProductTemplateMaps.get(0).setPlatformProductId(2L);
        
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(ProductTemplate.class)))
        .thenReturn(-1).thenReturn(1);
        
        assertFalse(productTemplateService.editProductTemplate(productTemplate, null));
        
        Mockito.when(platformProductTemplateMapService.selectByTemplateId(Mockito.anyLong()))
        .thenReturn(oldPlatformProductTemplateMaps);
        
        
        Mockito.when(productTemplateEnterpriseMapService.selectByProductTemplateId(Mockito.anyLong()))
        .thenReturn(productTemplateEnterpriseMaps);
        
        Mockito.when(platformProductTemplateMapService.batchDelete(Mockito.anyList()))
        .thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true);
        Mockito.when(entProductService.batchDeleteByEnterIdAndProductId(Mockito.anyList()))
        .thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(true);
        Mockito.when(platformProductTemplateMapService.batchInsert(Mockito.anyList()))
        .thenReturn(true).thenReturn(false).thenReturn(true);
        Mockito.when(entProductService.batchInsertEntProduct(Mockito.anyList())).thenReturn(true);
        assertTrue(productTemplateService.editProductTemplate(productTemplate, platformProductTemplateMaps));
    
        try{
            assertFalse(productTemplateService.editProductTemplate(productTemplate, platformProductTemplateMaps));
            assertFalse(productTemplateService.editProductTemplate(productTemplate, platformProductTemplateMaps));
            assertFalse(productTemplateService.editProductTemplate(productTemplate, platformProductTemplateMaps));
        }catch(RuntimeException e){
            
        }
    }
    
    private List<ProductTemplateEnterpriseMap> createProductTemplateEnterpriseMap(){
        List<ProductTemplateEnterpriseMap> list = new ArrayList<ProductTemplateEnterpriseMap>();
        ProductTemplateEnterpriseMap productTemplateEnterpriseMap = new ProductTemplateEnterpriseMap();
        productTemplateEnterpriseMap.setId(1L);;
        productTemplateEnterpriseMap.setEnterpriseId(1L);;
        productTemplateEnterpriseMap.setProductTemplateId(1L);
        list.add(productTemplateEnterpriseMap);
        return list;
    }
    
    private ProductTemplate createProductTemplate(){
        ProductTemplate productTemplate = new ProductTemplate();
        productTemplate.setId(1L);
        productTemplate.setName("test");
        productTemplate.setDefaultFlag(1);
        return productTemplate;
    }
    
    private List<PlatformProductTemplateMap> createPlatformProductTemplateMaps(){
        List<PlatformProductTemplateMap> maps = new ArrayList<PlatformProductTemplateMap>();
        PlatformProductTemplateMap map = new PlatformProductTemplateMap();
        map.setPlatformProductId(1L);
        maps.add(map);
        return maps;
    }
    
    @Test
    public void testSelectAllProductTemplates(){
        Mockito.when(mapper.selectAllProductTemplates()).thenReturn(new ArrayList<ProductTemplate>());
        assertNotNull(productTemplateService.selectAllProductTemplates());
    }
    
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<ProductTemplate>());
        assertNotNull(productTemplateService.selectByMap(new HashMap<String, String>()));
    }
    
    @Test
    public void testWhetherUseProdTemplate(){
        List<ProductTemplateEnterpriseMap> maps = createProductTemplateEnterpriseMap();
        Mockito.when(productTemplateEnterpriseMapService.selectByProductTemplateId(Mockito.anyLong()))
        .thenReturn(maps).thenReturn(null);
        assertTrue(productTemplateService.whetherUseProdTemplate(1L));
        assertFalse(productTemplateService.whetherUseProdTemplate(1L));
    }
    
    

}

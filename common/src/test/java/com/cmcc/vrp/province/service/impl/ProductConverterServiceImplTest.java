package com.cmcc.vrp.province.service.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ProductConverterType;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.dao.ProductConverterMapper;
import com.cmcc.vrp.province.model.ProductConverter;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductConverterService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

/**
 * ProductConverterServiceImplTest
 * @author qihang
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductConverterServiceImplTest {
    
    @InjectMocks
    ProductConverterService converterService = new ProductConverterServiceImpl();
    
    @Mock
    ProductConverterMapper converterMapper;
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    CacheService cacheService;
    
    /**
     * testInsert()
     */
    @Test
    public void testInsert() {
        ProductConverter converter = initConverter();
        
        Mockito.when(converterMapper.insert(converter)).thenReturn(1);
        Mockito.when(cacheService.add(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        
        Assert.assertTrue(converterService.insert(converter));
        
        //test error
        Mockito.when(cacheService.add(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Assert.assertFalse(converterService.insert(converter));
        
        Mockito.when(converterMapper.insert(converter)).thenReturn(0);
        Assert.assertFalse(converterService.insert(converter));
        
        converter.setDestPrdId(null);
        try {
            converterService.validate(converter);
            Assert.fail();
        } catch (InvalidParameterException e) {
        }
        
        converter.setSourcePrdId(null);
        try {
            converterService.validate(converter);
            Assert.fail();
        } catch (InvalidParameterException e) {
        }
        
        try {
            converterService.insert(null);
            Assert.fail();
        } catch (InvalidParameterException e) {
        }
        
        
    }
    
    /**
     * testInsertBatch
     */
    @Test
    public void testInsertBatch() {
        List<ProductConverter> list = initConverterList();
        
        Mockito.when(converterMapper.batchInsert(list)).thenReturn(1);
        Mockito.when(cacheService.add(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Assert.assertTrue(converterService.insertBatch(list));
       
        
        //test error
        Mockito.when(cacheService.add(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Assert.assertFalse(converterService.insertBatch(list));
        
        
        Mockito.when(converterMapper.batchInsert(list)).thenReturn(0);
        Assert.assertFalse(converterService.insertBatch(list));
        
        ProductConverter converter1 = initConverter();
        converter1.setSourcePrdId(null);
        list.add(converter1);
        
        try {
            converterService.insertBatch(list);
            Assert.fail();
        } catch (InvalidParameterException e) {
        }
        
        list.clear();
        Assert.assertTrue(converterService.insertBatch(list));
        
    }
    
    /**
     * testGet
     */
    @Test
    public void testGet() {
        ProductConverter converter = initConverter();
        
        Mockito.when(converterMapper.selectByPrimaryKey(converter.getId())).thenReturn(converter);
        Assert.assertEquals(converterService.get(converter.getId()), converter);
        
        Assert.assertEquals(converterService.get(null), null);
    }
    
    /**
     * testDelete
     */
    @Test
    public void testDelete() {
        ProductConverter converter = initConverter();
        
        Mockito.when(converterMapper.selectByPrimaryKey(converter.getId())).thenReturn(converter);
        Mockito.when(cacheService.delete(Mockito.anyString())).thenReturn(true);
        Mockito.when(converterMapper.delete(converter.getId())).thenReturn(1);
        
        Assert.assertTrue(converterService.delete(converter.getId()));
        
        //testError
        Mockito.when(converterMapper.delete(converter.getId())).thenReturn(0);
        Assert.assertFalse(converterService.delete(converter.getId()));
        
        Mockito.when(cacheService.delete(Mockito.anyString())).thenReturn(false);
        Assert.assertFalse(converterService.delete(converter.getId()));
        
        Mockito.when(converterMapper.selectByPrimaryKey(converter.getId())).thenReturn(null);
        Assert.assertFalse(converterService.delete(converter.getId()));
        
        Assert.assertFalse(converterService.delete(null));
        
    }
    
    /**
     * testDeleteBatch
     */
    @Test
    public void testDeleteBatch() {
        List<ProductConverter> list = initConverterList();
        
        Mockito.when(cacheService.delete(Mockito.anyListOf(String.class))).thenReturn(true);
        Mockito.when(converterMapper.batchDelete(list)).thenReturn(1);
        
        Assert.assertTrue(converterService.deleteBatch(list));
        
        //testError
        Mockito.when(converterMapper.batchDelete(list)).thenReturn(0);
        Assert.assertFalse(converterService.deleteBatch(list));
        
        Mockito.when(cacheService.delete(Mockito.anyListOf(String.class))).thenReturn(false);
        Assert.assertFalse(converterService.deleteBatch(list));
        
        list.clear();
        Assert.assertTrue(converterService.deleteBatch(list));
    }
    
    /**
     * testGetAll
     */
    @Test
    public void testGetAll(){
        List<ProductConverter> list = initConverterList();
        Mockito.when(converterMapper.getAll()).thenReturn(list);
        Assert.assertEquals(converterService.get(), list);
    }
    
    /**
     * testGetSouDest
     */
    @Test
    public void testGetSouDest(){
        ProductConverter converter = initConverter();
        List<ProductConverter> list = new ArrayList<ProductConverter>();
        list.add(converter);
        
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(converterMapper.getBySrcDestId(converter.getSourcePrdId(), 
                converter.getDestPrdId())).thenReturn(list);
        Mockito.when(cacheService.add(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        
        Assert.assertEquals(converterService.get(converter.getSourcePrdId(), converter.getDestPrdId()), converter);
        
        Mockito.when(cacheService.add(Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Assert.assertEquals(converterService.get(converter.getSourcePrdId(), converter.getDestPrdId()), converter);
        
        Mockito.when(converterMapper.getBySrcDestId(converter.getSourcePrdId(), 
                converter.getDestPrdId())).thenReturn(new ArrayList<ProductConverter>());
        Assert.assertEquals(converterService.get(converter.getSourcePrdId(), converter.getDestPrdId()), null);
        
        
        
        
        String json = "{\"id\":1,\"sourcePrdId\":1,\"destPrdId\":2,\"createTime\":\"Dec 26, 2016 11:07:55 AM\",\"updateTime\":\"Dec 26, 2016 11:07:55 AM\",\"deleteFlag\":0}";
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn(json);
        
        Assert.assertEquals(converterService.get(converter.getSourcePrdId(), converter.getDestPrdId()).getSourcePrdId(),
                converter.getSourcePrdId());
        
        
        json = "dfdf";
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn(json);
        Assert.assertEquals(converterService.get(converter.getSourcePrdId(), converter.getDestPrdId()),
                null);
        
        
        Assert.assertEquals(converterService.get(converter.getSourcePrdId(), null),
                null);
        
        Assert.assertEquals(converterService.get(null, converter.getDestPrdId()),
                null);
    }
    
    /**
     * testInsertDeleteBatch
     */
    @Test
    public void testInsertDeleteBatch(){        
        List<ProductConverter> addList = initConverterList();
        List<ProductConverter> deleteList = initConverterList();
        converterService  = Mockito.spy(converterService);
        Mockito.doReturn(true).when(converterService).insertBatch(addList);
        Mockito.doReturn(true).when(converterService).deleteBatch(deleteList);
        
        Assert.assertTrue(converterService.insertDeleteBatch(addList, deleteList));
        
        Mockito.doReturn(false).when(converterService).deleteBatch(deleteList);
        try {
            converterService.insertDeleteBatch(addList, deleteList);
            Assert.fail();
        } catch (TransactionException e) {
        }
        
        Mockito.doReturn(false).when(converterService).insertBatch(addList);
        Assert.assertFalse(converterService.insertDeleteBatch(addList, deleteList));
    }
    
    /**
     * testSave
     */
    @Test
    public void testSave(){
        List<ProductConverter> addList = initConverterList();
        List<ProductConverter> deleteList = initConverterListDiff();
        
        addList.add(initConverterDiff());
        deleteList.add(initConverter());
        
        Mockito.when(converterMapper.batchInsert(Mockito.anyListOf(ProductConverter.class))).thenReturn(1);
        Mockito.when(cacheService.add(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
       
        Mockito.when(cacheService.delete(Mockito.anyListOf(String.class))).thenReturn(true);
        Mockito.when(converterMapper.batchDelete(Mockito.anyListOf(ProductConverter.class))).thenReturn(1);
        
        Assert.assertTrue(converterService.save(addList, deleteList));
    }
    
    /**
     * count
     */
    @Test
    public void testCount(){
        QueryObject queryObject = new QueryObject();
        int count = 1;
        Mockito.when(converterMapper.count(queryObject.toMap())).thenReturn(count);
        Assert.assertEquals(count, converterService.count(queryObject));
    }
    
    /**
     * testPage
     */
    @Test
    public void testPage(){
        QueryObject queryObject = new QueryObject();
        List<ProductConverter> list = initConverterList();          
        Mockito.when(converterMapper.page(queryObject.toMap())).thenReturn(list);

        Assert.assertEquals(list, converterService.page(queryObject));
    }
    
    /**
     * testIsInterdictConvert
     */
    @Test
    public void testIsInterdictConvert(){
        Assert.assertFalse(converterService.isInterdictConvert(1L, 1L));
        
        //test Black
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.PRODUCT_CONVERTER_TYPE.getKey())).
            thenReturn(ProductConverterType.BLACKLIST.getType());
        
        ProductConverter converter = initConverter();
        List<ProductConverter> list = new ArrayList<ProductConverter>();
        list.add(converter);
        
        //black true
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(converterMapper.getBySrcDestId(converter.getSourcePrdId(), 
                converter.getDestPrdId())).thenReturn(list);
        Mockito.when(cacheService.add(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        
        Assert.assertTrue(converterService.isInterdictConvert(converter.getSourcePrdId(), 
                converter.getDestPrdId()));
        
        //black false
        Mockito.when(converterMapper.getBySrcDestId(converter.getSourcePrdId(), 
                converter.getDestPrdId())).thenReturn(new ArrayList<ProductConverter>());
        Assert.assertFalse(converterService.isInterdictConvert(converter.getSourcePrdId(), 
                converter.getDestPrdId()));
        
        
        //test white
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.PRODUCT_CONVERTER_TYPE.getKey())).
            thenReturn(ProductConverterType.WHITELIST.getType());
        //white true
        Assert.assertTrue(converterService.isInterdictConvert(converter.getSourcePrdId(), 
                converter.getDestPrdId()));
        //white false
        Mockito.when(converterMapper.getBySrcDestId(converter.getSourcePrdId(), 
                converter.getDestPrdId())).thenReturn(list);
        Assert.assertFalse(converterService.isInterdictConvert(converter.getSourcePrdId(), 
                converter.getDestPrdId()));
        
        
        //test nouse
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.PRODUCT_CONVERTER_TYPE.getKey())).
            thenReturn("xxx");
        Assert.assertFalse(converterService.isInterdictConvert(converter.getSourcePrdId(), 
                converter.getDestPrdId()));  
    }

    private ProductConverter initConverter(){        
        ProductConverter converter = new ProductConverter();
        
        converter.setId(1L);
        converter.setSourcePrdId(1L);
        converter.setDestPrdId(2L);
        converter.setCreateTime(new Date());
        converter.setUpdateTime(new Date());
        converter.setDeleteFlag(0);
        
        return converter;
    }
    
    private ProductConverter initConverterDiff(){        
        ProductConverter converter = new ProductConverter();
        
        converter.setId(3L);
        converter.setSourcePrdId(5L);
        converter.setDestPrdId(6L);
        converter.setCreateTime(new Date());
        converter.setUpdateTime(new Date());
        converter.setDeleteFlag(0);
        
        return converter;
    }
    
    private List<ProductConverter> initConverterList(){
        ProductConverter converter = initConverter();
        List<ProductConverter> list = new ArrayList<ProductConverter>();
        
        list.add(converter);
        
        ProductConverter converter2 = initConverter();
        converter2.setId(2L);
        converter2.setSourcePrdId(3L);
        converter2.setDestPrdId(4L);
        
        list.add(converter2);

       
        return list;
    }
    
    private List<ProductConverter> initConverterListDiff(){
        ProductConverter converter = initConverterDiff();
       
        List<ProductConverter> list = new ArrayList<ProductConverter>();
        
        list.add(converter);
        
        ProductConverter converter2 = initConverter();
        converter2.setId(4L);
        converter2.setSourcePrdId(7L);
        converter2.setDestPrdId(8L);
        
        list.add(converter2);
        
        ProductConverter converter3 = initConverter();
        converter3.setId(5L);
        converter3.setSourcePrdId(1L);
        converter3.setDestPrdId(4L);
        list.add(converter3);

       
        return list;
    }
}

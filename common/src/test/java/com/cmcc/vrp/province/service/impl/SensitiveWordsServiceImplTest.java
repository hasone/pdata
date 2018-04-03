package com.cmcc.vrp.province.service.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SensitiveWordsMapper;
import com.cmcc.vrp.province.model.SensitiveWords;
import com.cmcc.vrp.util.QueryObject;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年12月23日 下午2:35:58
*/
@RunWith(MockitoJUnitRunner.class)
public class SensitiveWordsServiceImplTest {
    @InjectMocks
    SensitiveWordsServiceImpl sensitiveWordsService = new SensitiveWordsServiceImpl();
    
    @Mock
    SensitiveWordsMapper sensitiveWordsMapper;
    /**
     * 
     */
    @Test
    public void insertTest() {
        assertFalse(sensitiveWordsService.insert(null));
        SensitiveWords sensitiveWords = new SensitiveWords();
        assertFalse(sensitiveWordsService.insert(sensitiveWords));
        sensitiveWords.setName("xx");
        assertFalse(sensitiveWordsService.insert(sensitiveWords));
        sensitiveWords.setCreatorId(1l);
        when(sensitiveWordsMapper.selectByName(any(String.class)))
            .thenReturn(sensitiveWords);
        assertFalse(sensitiveWordsService.insert(sensitiveWords));
        when(sensitiveWordsMapper.selectByName(any(String.class)))
            .thenReturn(null);
        when(sensitiveWordsMapper.insert(any(SensitiveWords.class)))
            .thenReturn(0);
        assertFalse(sensitiveWordsService.insert(sensitiveWords));
        when(sensitiveWordsMapper.insert(any(SensitiveWords.class)))
            .thenReturn(1);
        assertTrue(sensitiveWordsService.insert(sensitiveWords));
        
    }
    
    /**
     * 
     */
    @Test
    public void batchInsertTest() {
        assertFalse(sensitiveWordsService.batchInsert(null));
        List<SensitiveWords> sensitiveWordsList = new ArrayList<SensitiveWords>();
        assertFalse(sensitiveWordsService.batchInsert(sensitiveWordsList));
        SensitiveWords sensitiveWords = new SensitiveWords();
        sensitiveWordsList.add(sensitiveWords);
        
        when(sensitiveWordsMapper.batchInsert(anyList()))
            .thenReturn(0);
        assertFalse(sensitiveWordsService.batchInsert(sensitiveWordsList));
        when(sensitiveWordsMapper.batchInsert(anyList()))
            .thenReturn(1);
        assertTrue(sensitiveWordsService.batchInsert(sensitiveWordsList));    
    }
    
    /**
     * 
     */
    @Test
    public void deleteByNameTest() {
        assertFalse(sensitiveWordsService.deleteByName(null));
        
        when(sensitiveWordsMapper.deleteByName(anyString()))
            .thenReturn(0);
        assertFalse(sensitiveWordsService.deleteByName("xx"));
        when(sensitiveWordsMapper.deleteByName(anyString()))
            .thenReturn(1);
        assertTrue(sensitiveWordsService.deleteByName("xx"));    
    }
    /**
     * 
     */
    @Test
    public void deleteByIdTest() {
        assertFalse(sensitiveWordsService.deleteById(null));
        
        when(sensitiveWordsMapper.deleteById(any(Long.class)))
            .thenReturn(0);
        assertFalse(sensitiveWordsService.deleteById(1l));
        when(sensitiveWordsMapper.deleteById(any(Long.class)))
            .thenReturn(1);
        assertTrue(sensitiveWordsService.deleteById(1l));    
    }
    
    /**
     * 
     */
    @Test
    public void selectByNameTest() {
        assertNull(sensitiveWordsService.selectByName(null));    
    }
    
    /**
     * 
     */
    @Test
    public void selectByIdTest() {
        assertNull(sensitiveWordsService.selectById(null));   
        when(sensitiveWordsMapper.selectById(any(Long.class)))
            .thenReturn(null);
        assertNull(sensitiveWordsService.selectById(1l));
        
    }
    
    /**
     * 
     */
    @Test
    public void updateByIdTest() {
        assertFalse(sensitiveWordsService.updateById(null, "xx", 1l));
        assertFalse(sensitiveWordsService.updateById(1l, "", 1l));
        assertFalse(sensitiveWordsService.updateById(1l, null, 1l));
        assertFalse(sensitiveWordsService.updateById(1l, "xx", null));
        
        when(sensitiveWordsMapper.updateById(any(Long.class), any(String.class), any(Long.class)))
            .thenReturn(0);
        assertFalse(sensitiveWordsService.updateById(1l, "xx", 1l));
        when(sensitiveWordsMapper.updateById(any(Long.class), any(String.class), any(Long.class)))
            .thenReturn(1);
        assertTrue(sensitiveWordsService.updateById(1l, "xx", 1l));
        
    }

    
    /**
     * 
     */
    @Test
    public void showForPageResultCountTest() {
        assertSame(0, sensitiveWordsService.showForPageResultCount(null));
        
        when(sensitiveWordsMapper.showSensitiveWordsForPageResultCount(anyMap()))
            .thenReturn(1);
        QueryObject queryObject = new QueryObject();
        assertSame(1, sensitiveWordsService.showForPageResultCount(queryObject));
        
    }
    
    /**
     * 
     */
    @Test
    public void showSensitiveWordsForPageResultTest() {
        assertNull(sensitiveWordsService.showSensitiveWordsForPageResult(null));
        
        when(sensitiveWordsMapper.showSensitiveWordsForPageResult(anyMap()))
            .thenReturn(null);
        QueryObject queryObject = new QueryObject();
        assertNull( sensitiveWordsService.showSensitiveWordsForPageResult(queryObject));
        
    }
    
    /**
     * 
     */
    @Test
    public void getAllSensitiveWordsTest() {
        
        when(sensitiveWordsMapper.getAllSensitiveWords())
            .thenReturn(null);
        assertNull( sensitiveWordsService.getAllSensitiveWords());
        
    }
    /**
     * 
     */
    @Test
    public void selectSensitiveWordsByMapTest() {
        
        when(sensitiveWordsMapper.selectSensitiveWordsByMap(anyMap()))
            .thenReturn(null);
        Map map = new HashMap();
        assertNull( sensitiveWordsService.selectSensitiveWordsByMap(map));
        
    }
}

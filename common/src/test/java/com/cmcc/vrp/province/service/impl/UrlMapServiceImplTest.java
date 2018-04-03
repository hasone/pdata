package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.UrlMapMapper;
import com.cmcc.vrp.province.model.UrlMap;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月14日 上午9:03:26
*/
@RunWith(MockitoJUnitRunner.class)
public class UrlMapServiceImplTest {
    @InjectMocks
    UrlMapServiceImpl urlMapServiceImpl = new UrlMapServiceImpl();
    
    @Mock
    UrlMapMapper urlMapMapper;
    
    /**
     * 
     */
    @Test
    public void testQueryByUUID() {
        assertNull(urlMapServiceImpl.queryByUUID(null));
        assertNull(urlMapServiceImpl.queryByUUID(""));
        when(urlMapMapper.selectByUUID(any(String.class)))
            .thenReturn(null);
        assertNull(urlMapServiceImpl.queryByUUID("11"));
    }

    /**
     * 
     */
    @Test
    public void testCreateUrlMap() {
        assertFalse(urlMapServiceImpl.createUrlMap(null));       
        
        when(urlMapMapper.insertSelective(any(UrlMap.class)))
            .thenReturn(1);
        UrlMap urlMap = new UrlMap();
        assertTrue(urlMapServiceImpl.createUrlMap(urlMap));
        
        when(urlMapMapper.insertSelective(any(UrlMap.class)))
            .thenReturn(0);
        assertFalse(urlMapServiceImpl.createUrlMap(urlMap));        
    }

}

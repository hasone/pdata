package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.EntSyncListMapper;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年11月30日 上午9:49:52
*/
@RunWith(MockitoJUnitRunner.class)
public class EntSyncListServiceImplTest {
    @InjectMocks
    EntSyncListService entSyncListService = new EntSyncListServiceImpl();
    
    @Mock
    EntSyncListMapper entSyncListMapper;
	
    @Mock
    EnterprisesService enterprisesService;
	
    /**
     * 
     */
    @Test
    public void insertTest() {
    	assertFalse(entSyncListService.insert(null));
    	EntSyncList entSyncList = new EntSyncList();
    	entSyncList.setId((long) 1);
    	when(entSyncListMapper.insert(any(EntSyncList.class)))
    	    .thenReturn(0);
    	assertFalse(entSyncListService.insert(entSyncList));
    	when(entSyncListMapper.insert(any(EntSyncList.class)))
    		.thenReturn(1);
    	assertTrue(entSyncListService.insert(entSyncList));
    }
    
    @Test
    public void getByEntProCodeTest() {
    	assertNull(entSyncListService.getByEntProCode(null));
    	EntSyncList entSyncList = new EntSyncList();
    	entSyncList.setId((long) 1);
    	List<EntSyncList> entSyncLists = new ArrayList<EntSyncList>();
    	entSyncLists.add(entSyncList);
    	when(entSyncListMapper.getByEntProCode(any(String.class)))
    	    .thenReturn(entSyncLists);
    	assertNotNull(entSyncListService.getByEntProCode("1234"));
    	when(entSyncListMapper.getByEntProCode(any(String.class)))
    		.thenReturn(null);
    	assertNull(entSyncListService.getByEntProCode("1234"));
    }
    
    @Test
    public void getByEntIdTest() {
    	assertNull(entSyncListService.getByEntId(null));
    	
    	when(enterprisesService.selectById(any(Long.class)))
	    .thenReturn(null);
    	assertNull(entSyncListService.getByEntId((long) 1));
    	when(enterprisesService.selectById(any(Long.class)))
	    .thenReturn(new Enterprise());
    	EntSyncList entSyncList = new EntSyncList();
    	entSyncList.setId((long) 1);
    	List<EntSyncList> entSyncLists = new ArrayList<EntSyncList>();
    	entSyncLists.add(entSyncList);
    	when(entSyncListMapper.getByEntId(any(Long.class)))
    	    .thenReturn(entSyncLists);
    	assertNotNull(entSyncListService.getByEntId(1L));
    	when(entSyncListMapper.getByEntId(any(Long.class)))
    		.thenReturn(null);
    	assertNull(entSyncListService.getByEntId((long) 1));
    }
    
    @Test
    public void getByEntIdAndEntProCodeTest() {
    	assertNull(entSyncListService.getByEntIdAndEntProCode(null, "1234"));
    	assertNull(entSyncListService.getByEntIdAndEntProCode((long) 1, ""));
    	assertNull(entSyncListService.getByEntIdAndEntProCode((long) 1, null));
    	when(enterprisesService.selectById(any(Long.class)))
    		.thenReturn(null);
    	assertNull(entSyncListService.getByEntIdAndEntProCode((long) 1, "1234"));
    	when(enterprisesService.selectById(any(Long.class)))
	    .thenReturn(new Enterprise());
    	EntSyncList entSyncList = new EntSyncList();
    	entSyncList.setId((long) 1);
    	when(entSyncListMapper.getByEntIdAndEntProCode(any(Long.class), any(String.class)))
    		.thenReturn(entSyncList);
    	assertNotNull(entSyncListService.getByEntIdAndEntProCode((long) 1, "1234"));
    	when(entSyncListMapper.getByEntIdAndEntProCode(any(Long.class), any(String.class)))
    	    .thenReturn(null);
    	assertNull(entSyncListService.getByEntIdAndEntProCode((long) 1, "1234"));
    }
    
    @Test
    public void updateSelectiveTest() {
    	when(entSyncListMapper.updateSelective(any(Long.class), any(Integer.class), any(String.class)))
    	    .thenReturn(1);
    	EntSyncList entSyncList = new EntSyncList();
    	entSyncList.setId((long) 1);
    	entSyncList.setStatus(1);
    	entSyncList.setSyncInfo("ok");
    	assertSame(1, entSyncListService.updateSelective(entSyncList));
    }
    @Test
    public void testGetById() {
        Mockito.when(entSyncListMapper.getById(Mockito.anyLong())).thenReturn(new EntSyncList());
        Assert.assertNotNull(entSyncListService.getById(1l));
    }
}

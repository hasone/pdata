package com.cmcc.vrp.province.mdrc.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MdrcBatchConfigMapper;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.util.QueryObject;

 /**
  * 
  * @ClassName: MdrcBatchConfigServiceImplTest 
  * @Description: TODO
  * @author: Rowe
  * @date: 2017年8月18日 上午10:28:35
  */
@RunWith(MockitoJUnitRunner.class)
public class MdrcBatchConfigServiceImplTest {

    @InjectMocks
    MdrcBatchConfigServiceImpl mdrcBatchConfigService = new MdrcBatchConfigServiceImpl();   
    @Mock
    MdrcBatchConfigMapper mdrcBatchConfigMapper;
    
    @Test
    public void testInsertSelective() {
        MdrcBatchConfig config = new MdrcBatchConfig();
        Mockito.when(mdrcBatchConfigMapper.insertSelective(config)).thenReturn(1L);
        assertTrue(mdrcBatchConfigService.insertSelective(config).equals(1L));
    }
    
    @Test
    public void testSelectByPrimaryKey() {
        MdrcBatchConfig config = new MdrcBatchConfig();
        Mockito.when(mdrcBatchConfigMapper.selectByPrimaryKey(anyLong())).thenReturn(config);
        assertTrue(mdrcBatchConfigService.selectByPrimaryKey(1L).equals(config));
    }
    
    @Test
    public void testSelect() {
        assertNull(mdrcBatchConfigService.select(null));
        
        MdrcBatchConfig config = new MdrcBatchConfig();
        Mockito.when(mdrcBatchConfigMapper.select(anyLong())).thenReturn(config);
        assertTrue(mdrcBatchConfigService.select(1L).equals(config));
    }

    @Test
    public void testSelectAllConfig() {
        assertNull(mdrcBatchConfigService.select(null));
        List<MdrcBatchConfig> list = new ArrayList<MdrcBatchConfig>();
        Mockito.when(mdrcBatchConfigMapper.selectAllConfig()).thenReturn(list);
        assertTrue(mdrcBatchConfigService.selectAllConfig().equals(list));
    }

    @Test
    public void testQueryCounts() {
        assertTrue(mdrcBatchConfigService.queryCounts(null) == 0);
        
        Mockito.when(mdrcBatchConfigMapper.queryCounts(any(Map.class))).thenReturn(1);
        assertTrue(mdrcBatchConfigService.queryCounts(new QueryObject()) == 1);
    }
    
    @Test
    public void testQueryPagination() {
        assertNull(mdrcBatchConfigService.queryPagination(null));
        
        List<MdrcBatchConfig> list = new ArrayList<MdrcBatchConfig>();
        Mockito.when(mdrcBatchConfigMapper.queryPagination(any(Map.class))).thenReturn(list);
        assertTrue(mdrcBatchConfigService.queryPagination(new QueryObject()).equals(list));
    }
    
    @Test
    public void testSelectModuleByPrimaryKey() {
        assertNull(mdrcBatchConfigService.queryPagination(null));
        
        MdrcBatchConfig config = new MdrcBatchConfig();
        Mockito.when(mdrcBatchConfigMapper.selectModuleByPrimaryKey(any(Long.class))).thenReturn(config);
        assertTrue(mdrcBatchConfigService.selectModuleByPrimaryKey(1L).equals(config));
    }
    
    
    @Test
    public void testUpdate() {
        assertFalse(mdrcBatchConfigService.update(null));
        
        MdrcBatchConfig config = new MdrcBatchConfig();
        Mockito.when(mdrcBatchConfigMapper.updateByPrimaryKeySelective(any(MdrcBatchConfig.class))).thenReturn(1);
        assertTrue(mdrcBatchConfigService.update(config));
    }
    
    @Test
    public void testSelectByCreatorIdAndStatus() {
        assertNull(mdrcBatchConfigService.selectByCreatorIdAndStatus(null, null));
        
        List<MdrcBatchConfig> list = new ArrayList<MdrcBatchConfig>();
        Mockito.when(mdrcBatchConfigMapper.selectByCreatorIdAndStatus(any(Map.class))).thenReturn(list);
        assertTrue(mdrcBatchConfigService.selectByCreatorIdAndStatus(1L, 1L).equals(list));
    }
    
    @Test
    public void testSelectByCardmaker() {
        assertNull(mdrcBatchConfigService.selectByCardmaker(null));
        
        List<MdrcBatchConfig> list = new ArrayList<MdrcBatchConfig>();
        Mockito.when(mdrcBatchConfigMapper.selectByCardmaker(any(Map.class))).thenReturn(list);
        assertTrue(mdrcBatchConfigService.selectByCardmaker(new QueryObject()).equals(list));
    }
    
    @Test
    public void testCountByCardmaker() {
        assertTrue(mdrcBatchConfigService.countByCardmaker(null) == 0);
        Mockito.when(mdrcBatchConfigMapper.countByCardmaker(any(Map.class))).thenReturn(1);
        assertTrue(mdrcBatchConfigService.countByCardmaker(new QueryObject()) == 1);
    }
    
}


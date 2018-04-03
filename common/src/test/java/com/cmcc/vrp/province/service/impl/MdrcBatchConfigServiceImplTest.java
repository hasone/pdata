package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.province.dao.MdrcBatchConfigMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.impl.MdrcBatchConfigServiceImpl;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.service.ManagerService;
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
    @Mock
    ManagerService managerService;
    
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
    
    @Test
    public void testSelectByEntIdAndStatus() {
        List<MdrcBatchConfig> list = new ArrayList<MdrcBatchConfig>();
        Mockito.when(mdrcBatchConfigMapper.selectByEntIdAndStatus(anyLong(), any(Integer.class))).thenReturn(list);
        assertTrue(mdrcBatchConfigService.selectByEntIdAndStatus(1L,MdrcBatchConfigStatus.ACTIVATED).equals(list));
    }
    
    
    @Test
    public void testGetConfigDetailsByIdAndStatus() {
        MdrcBatchConfigService spy = spy(mdrcBatchConfigService);
        Mockito.when(spy.selectByPrimaryKey(anyLong())).thenReturn(null);
        assertFalse(spy.getConfigDetailsByIdAndStatus(1L,2) == null);
        
        MdrcBatchConfig config = new MdrcBatchConfig();
        Mockito.when(spy.selectByPrimaryKey(anyLong())).thenReturn(config);
        assertFalse(spy.getConfigDetailsByIdAndStatus(1L,2) == null);
        
        config.setThisYear("2017");
        Mockito.when(spy.selectByPrimaryKey(anyLong())).thenReturn(config);
        Mockito.when(mdrcBatchConfigMapper.getConfigDetailsByIdAndStatus(anyLong(),any(String.class), any(Integer.class))).thenReturn(config);
        assertFalse((spy.getConfigDetailsByIdAndStatus(1L,2).equals(config)));

    }
    
    
    @Test
    public void testIsOverAuth(){
        assertTrue(mdrcBatchConfigService.isOverAuth(null, null));
        
        when(managerService.getManagerByAdminId(Mockito.any(Long.class))).thenReturn(null);
        assertTrue(mdrcBatchConfigService.isOverAuth(1L, 1L));

        
        when(mdrcBatchConfigMapper.selectByPrimaryKey(Mockito.any(Long.class))).thenReturn(null);
        assertTrue(mdrcBatchConfigService.isOverAuth(1L, 1L));

        
        Manager fatherManager = new Manager();
        MdrcBatchConfig config = new MdrcBatchConfig();
        config.setCreatorId(1L);
        when(mdrcBatchConfigMapper.selectByPrimaryKey(Mockito.any(Long.class))).thenReturn(config);
        when(managerService.isProOrCityOrMangerOrEnt(Mockito.any(Long.class))).thenReturn(true);
        when(managerService.getManagerByAdminId(Mockito.any(Long.class))).thenReturn(fatherManager);
        when(managerService.isParentManage(Mockito.any(Long.class),Mockito.any(Long.class))).thenReturn(true);
        assertFalse(mdrcBatchConfigService.isOverAuth(1L, 1L));
        
        when(managerService.isParentManage(Mockito.any(Long.class),Mockito.any(Long.class))).thenReturn(false);
        assertTrue(mdrcBatchConfigService.isOverAuth(1L, 1L));

    }
}


package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ManagerMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.QueryObject;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ManagerServiceImpl测试
 *
 * @author wujiamin
 * @date 2016年10月27日
 */
@RunWith(MockitoJUnitRunner.class)
public class ManagerServiceImplTest {
    @InjectMocks
    ManagerServiceImpl managerService = new ManagerServiceImpl();

    @Mock
    ManagerMapper managerMapper;
    @Mock
    AdministerService administerService;

    @Test
    public void testSelectByAdminId() {
        when(managerMapper.selectByAdminId(1L)).thenReturn(new Manager());
        assertNotNull(managerService.selectByAdminId(1L));
        verify(managerMapper, times(1)).selectByAdminId(1L);
    }

    @Test
    public void testCreateManager() {
        Manager manager = new Manager();
        assertFalse(managerService.createManager(manager, 1L));
        manager.setRoleId(1L);
        assertFalse(managerService.createManager(manager, 1L));
        manager.setName("ss");
        assertFalse(managerService.createManager(manager, null));

        when(managerMapper.insert(Mockito.any(Manager.class))).thenReturn(0);
        assertFalse(managerService.createManager(manager, 1L));

        when(managerMapper.insert(Mockito.any(Manager.class))).thenReturn(1);
        assertTrue(managerService.createManager(manager, 1L));

        verify(managerMapper, times(2)).insert(Mockito.any(Manager.class));
    }

    @Test
    public void testSelectByParentIdCount() {
        when(managerMapper.selectByParentIdCount(Mockito.anyMap())).thenReturn(1);
        assertEquals(managerService.selectByParentIdCount(1L, new QueryObject()), 1);

        verify(managerMapper, times(1)).selectByParentIdCount(Mockito.anyMap());
    }

    @Test
    public void testSelectByParentId() {
        when(managerMapper.selectByParentId(1L)).thenReturn(new ArrayList());
        assertNotNull(managerService.selectByParentId(1L));

        verify(managerMapper, times(1)).selectByParentId(1L);
    }

    /**
     * 获取子节点,异常情况
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetChildNodeString() {
        final Long rootId = 1L;
        when(managerMapper.getDirectChild(any(List.class))).thenReturn(null);
        assertEquals(managerService.getChildNodeString(rootId), String.valueOf(rootId));
        verify(managerMapper, times(1)).getDirectChild(any(List.class));
    }

    /**
     * 获取子节点,happy path
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetChildNodeString2() throws Exception {
        final Long rootId = 1L;

        List<Long> list1 = randomList();
        List<Long> list2 = randomList();

        List<Long> result = combile(rootId, list1, list2);
        String resultStr = StringUtils.join(result, ',');

        when(managerMapper.getDirectChild(any(List.class))).thenReturn(list1).thenReturn(list2).thenReturn(null);
        assertTrue(resultStr.equals(managerService.getChildNodeString(rootId)));
        verify(managerMapper, times(3)).getDirectChild(any(List.class));
    }

    private List<Long> combile(Long rootChild, List<Long> first, List<Long> second) {
        List<Long> result = new LinkedList<Long>();
        result.add(rootChild);

        for (Long aFirst : first) {
            result.add(aFirst);
        }

        for (Long aSecond : second) {
            result.add(aSecond);
        }

        return result;
    }

    private List<Long> randomList() {
        Random r = new Random();
        final int MAX = 10;
        final int COUNT = r.nextInt(MAX) + 1;
        Set<Long> set = new LinkedHashSet<Long>();

        for (int i = 0; i < COUNT; i++) {
            set.add((long) r.nextInt(10000));
        }

        return new LinkedList<Long>(set);
    }

    @Test
    public void testSelectByPrimaryKey() {
        when(managerMapper.selectByPrimaryKey(1L)).thenReturn(new Manager());
        assertNotNull(managerService.selectByPrimaryKey(1L));
        verify(managerMapper, times(1)).selectByPrimaryKey(1L);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        when(managerMapper.updateByPrimaryKeySelective(Mockito.any(Manager.class))).thenReturn(1);
        assertTrue(managerService.updateByPrimaryKeySelective(new Manager()));

        when(managerMapper.updateByPrimaryKeySelective(Mockito.any(Manager.class))).thenReturn(0);
        assertFalse(managerService.updateByPrimaryKeySelective(new Manager()));

        verify(managerMapper, times(2)).updateByPrimaryKeySelective(Mockito.any(Manager.class));
    }

    @Test
    public void testSelectByParentIdForPage() {
        when(managerMapper.selectByParentIdForPage(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(managerService.selectByParentIdForPage(1L, new QueryObject()));

        verify(managerMapper, times(1)).selectByParentIdForPage(Mockito.anyMap());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetSonTreeByManangId() {
        assertNull(managerService.getSonTreeByManangId(null));

        ManagerService spy = spy(managerService);
        List<Long> result = randomList();
        doReturn(result).when(spy).getSonTreeIdByManageId(anyLong());
        when(managerMapper.getManagers(any(List.class))).thenReturn(new LinkedList());

        assertNotNull(spy.getSonTreeByManangId(1L));

        verify(spy, times(1)).getSonTreeIdByManageId(anyLong());
        verify(managerMapper).getManagers(any(List.class));
    }

    @Test
    public void testGetManagerByAdminId() {
        assertNull(managerService.getManagerByAdminId(null));

        when(managerMapper.selectManagerByadminId(1L)).thenReturn(new Manager());
        assertNotNull(managerService.getManagerByAdminId(1L));

        verify(managerMapper, times(1)).selectManagerByadminId(1L);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetSonTreeIdByManageId() {
        assertNull(managerService.getSonTreeIdByManageId(null));

        final Long rootId = 1L;
        List<Long> result = new LinkedList<Long>();
        result.add(rootId);

        when(managerMapper.getDirectChild(any(List.class))).thenReturn(null);
        assert (managerService.getSonTreeIdByManageId(rootId).equals(result));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetSonTreeIdByManageId2() {
        final Long rootId = 1L;

        List<Long> list1 = randomList();
        List<Long> list2 = randomList();
        List<Long> result = combile(rootId, list1, list2);

        when(managerMapper.getDirectChild(any(List.class))).thenReturn(list1).thenReturn(list2).thenReturn(null);
        assert (managerService.getSonTreeIdByManageId(rootId).equals(result));
    }

    @Test
    public void testSelectSonIdsByParentId() {
        assertNull(managerService.selectSonIdsByParentId(null));

        when(managerMapper.selectSonIdsByParentId(1L)).thenReturn(new ArrayList());
        assertNotNull(managerService.selectSonIdsByParentId(1L));

        verify(managerMapper, times(1)).selectSonIdsByParentId(1L);
    }

    @Test
    public void testSelectHigherFatherNodeByParentId() {
        assertNull(managerService.selectHigherFatherNodeByParentId(null));

        when(managerMapper.selectHigherFatherNodeByParentId(1L)).thenReturn(new Manager());
        assertNotNull(managerService.selectHigherFatherNodeByParentId(1L));

        verify(managerMapper, times(1)).selectHigherFatherNodeByParentId(1L);
    }

    @Test
    public void testGetFullNameByCurrentManagerId() {
        String fullname = "123";
        when(managerMapper.selectByPrimaryKey(1L)).thenReturn(null);

        Manager manager = new Manager();
        manager.setId(1L);
        manager.setParentId(1L);
        when(managerMapper.selectByPrimaryKey(1L)).thenReturn(manager);
        assertEquals(managerService.getFullNameByCurrentManagerId(fullname, 1L), fullname);

        Manager manager2 = new Manager();
        manager2.setName("123");
        manager2.setId(2L);
        manager2.setParentId(1L);
        Manager manager3 = new Manager();
        manager3.setId(1L);
        manager3.setParentId(0L);
        manager3.setName("123");
        when(managerMapper.selectByPrimaryKey(1L)).thenReturn(manager2);
        when(managerMapper.selectByPrimaryKey(2L)).thenReturn(manager3);
        assertEquals(managerService.getFullNameByCurrentManagerId(fullname, 2L), fullname);
    }

    @Test
    public void testGet() {
        assertNull(managerService.get(null, "123"));
        assertNull(managerService.get(1L, ""));

        when(managerMapper.getByRoleIdAndName(1L, "123")).thenReturn(new Manager());
        assertNotNull(managerService.get(1L, "123"));

        verify(managerMapper, times(1)).getByRoleIdAndName(1L, "123");
    }

    /**
     * 根据当前管理员ID获取管理员ID
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetChildAdminByCurrentManageId() {
        //invalid
        assertNull(managerService.getChildAdminByCurrentManageId(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetChildAdminByCurrentManageId2() throws Exception {
        Long managerId = 1L;
        List<Long> result = randomList();
        result.add(managerId);

        ManagerService spy = spy(managerService);
        doReturn(result).when(spy).getSonTreeIdByManageId(managerId);
        when(administerService.getByManageIds(result)).thenReturn(new LinkedList<Administer>());

        assertNotNull(spy.getChildAdminByCurrentManageId(managerId));

        verify(spy, times(1)).getSonTreeIdByManageId(managerId);
        verify(administerService, times(1)).getByManageIds(result);
    }

    /**
     *
     */
    @Test
    public void testSelectManagerIdByEntIdAndRoleId() {
        assertNull(managerService.selectManagerIdByEntIdAndRoleId(null, 1l));
        assertNull(managerService.selectManagerIdByEntIdAndRoleId(1l, null));
        when(managerMapper.selectManagerIdByEntIdAndRoleId(any(Long.class), any(Long.class))).thenReturn(null);
        assertNull(managerService.selectManagerIdByEntIdAndRoleId(1l, 1l));
    }

    @Test
    public void testGetByPhone() {
        assertNull(managerService.getByPhone(null));
        Mockito.when(managerMapper.getByPhone(Mockito.anyString())).thenReturn(new Manager());
        assertNotNull(managerService.getByPhone("18867101234"));
    }

    @Test
    public void testSelectEntParentNodeByEnterIdOrRoleId() {
        assertNull(managerService.selectEntParentNodeByEnterIdOrRoleId(null, null));
        Mockito.when(managerMapper.selectEntParentNodeByEnterIdOrRoleId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(new ArrayList<Manager>());
        assertNotNull(managerService.selectEntParentNodeByEnterIdOrRoleId(1L, 1L));
    }
    
    
    /**
     * 
     */
    @Test
    public void testGetParentTreeByManangId(){
        ManagerServiceImpl managerService2 = Mockito.spy(managerService);
        List<Long> list = new ArrayList<Long>();
        Mockito.doReturn(list).when(managerService2).getParentTreeIds(Mockito.anyLong());
        Mockito.when(managerMapper.getManagers(Mockito.anyList())).thenReturn(new ArrayList<Manager>());
        
        assertNotNull(managerService2.getParentTreeByManangId(1L));
        assertNotNull(managerService2.getParentTreeByManangId(null));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetParentTreeIds(){
        ManagerServiceImpl managerService2 = Mockito.spy(managerService);

        
        Manager manager1 = new Manager();
        manager1.setParentId(1L);
        Manager manager2 = new Manager();
        manager2.setParentId(1L);
        
        Mockito.doReturn(manager1).when(managerService2).selectByPrimaryKey(2L);
        Mockito.doReturn(manager2).when(managerService2).selectByPrimaryKey(1L);
        
        assertNotNull(managerService2.getParentTreeIds(2L));
        
    }
    
    /**
     * 
     */
    @Test
    public void testGetParentNodeByRoleId(){
        ManagerServiceImpl managerService2 = Mockito.spy(managerService);
        Manager manager1 = new Manager();
        manager1.setParentId(1L);
        manager1.setRoleId(1L);
        
        List<Manager> list = new ArrayList<Manager>();
        list.add(manager1);
        
        Mockito.doReturn(list).when(managerService2).getParentTreeByManangId(1L);
        assertNotNull(managerService2.getParentNodeByRoleId(1L,1L));
        assertNull(managerService2.getParentNodeByRoleId(1L,2L));
    }
}

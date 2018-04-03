package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.AdminRoleMapper;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.RoleService;

/**
 * AdminRoleServiceImpl测试
 * @author wujiamin
 * @date 2016年10月27日
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminRoleServiceImplTest {
    @InjectMocks
    private final AdminRoleService service = new AdminRoleServiceImpl();
    @Mock
    AdminRoleMapper adminRoleMapper;
    @Mock
    private RoleService roleService;

    @Test
    public void testInsertAdminRole1() {
        Long adminId = 1L;
        Long roleId = 1L;
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(adminId);
        adminRole.setRoleId(roleId);
        when(adminRoleMapper.insert(Mockito.any(AdminRole.class))).thenReturn(1);
        assertTrue(service.insertAdminRole(adminId, roleId));
        when(adminRoleMapper.insert(Mockito.any(AdminRole.class))).thenReturn(0);
        assertFalse(service.insertAdminRole(adminId, roleId));
        verify(adminRoleMapper,times(2)).insert(Mockito.any(AdminRole.class));

    }
    
    @Test
    public void testInsertAdminRole2() {
        assertFalse(service.insertAdminRole(null));
        AdminRole adminRole = new AdminRole();
        assertFalse(service.insertAdminRole(adminRole));
        
        adminRole.setAdminId(1l);
        assertFalse(service.insertAdminRole(adminRole));
        
        adminRole.setRoleId(1L);
        when(adminRoleMapper.selectCountByQuery(Mockito.any(AdminRole.class))).thenReturn(1);
        assertTrue(service.insertAdminRole(adminRole));
        
        adminRole.setAdminId(1L);
        adminRole.setRoleId(1L);
        when(adminRoleMapper.selectCountByQuery(Mockito.any(AdminRole.class))).thenReturn(0);
        when(adminRoleMapper.insert(Mockito.any(AdminRole.class))).thenReturn(1);
        assertTrue(service.insertAdminRole(adminRole));
        verify(adminRoleMapper,times(1)).insert(Mockito.any(AdminRole.class));

    }
    

    @Test
    public void testSelectCountByQuery() {
        AdminRole adminRole = null;      
        assertEquals(service.selectCountByQuery(adminRole),0);
        verify(adminRoleMapper,times(0)).selectCountByQuery(Mockito.any(AdminRole.class));
    }

    @Test
    public void testGetRoleIdByAdminId() {
        assertNull(service.getRoleIdByAdminId(null));
        
        when(adminRoleMapper.selectRoleIdByAdminId(1L)).thenReturn(1L);
        assertNotNull(service.getRoleIdByAdminId(1L));
        verify(adminRoleMapper,times(1)).selectRoleIdByAdminId(1L);
    }

    @Test
    public void testDeleteByRoleId() {
        assertFalse(service.deleteByRoleId(null));
        
        List<AdminRole> list = new ArrayList();
        when(adminRoleMapper.selectAdminRoleByRoleId(1L)).thenReturn(list);
        assertTrue(service.deleteByRoleId(1L));
        
        AdminRole ar = new AdminRole();
        ar.setAdminId(1L);
        ar.setRoleId(1L);
        list.add(ar);
        when(adminRoleMapper.selectAdminRoleByRoleId(1L)).thenReturn(list);
        when(adminRoleMapper.deleteByRoleId(1L)).thenReturn(1);
        assertTrue(service.deleteByRoleId(1L));
        
        when(adminRoleMapper.deleteByRoleId(1L)).thenReturn(0);
        assertFalse(service.deleteByRoleId(1L));
        
        verify(adminRoleMapper,times(3)).selectAdminRoleByRoleId(1L);
        verify(adminRoleMapper,times(2)).deleteByRoleId(1L);
    }

    @Test
    public void testDeleteByAdminId() {
        assertFalse(service.deleteByAdminId(null));

        List<AdminRole> list = new ArrayList();
        when(adminRoleMapper.selectAdminRoleByAdminId(1L)).thenReturn(list);
        assertTrue(service.deleteByAdminId(1L));

        when(adminRoleMapper.selectAdminRoleByAdminId(1L)).thenReturn(null);
        assertTrue(service.deleteByAdminId(1L));        
        
        AdminRole ar = new AdminRole();
        ar.setAdminId(1L);
        ar.setRoleId(1L);
        list.add(ar);
        when(adminRoleMapper.selectAdminRoleByAdminId(1L)).thenReturn(list);
        when(adminRoleMapper.deleteByAdminId(1L)).thenReturn(1);
        assertTrue(service.deleteByAdminId(1L));
        
        when(adminRoleMapper.deleteByAdminId(1L)).thenReturn(0);
        assertFalse(service.deleteByAdminId(1L));
        
        verify(adminRoleMapper,times(4)).selectAdminRoleByAdminId(1L);
        verify(adminRoleMapper,times(2)).deleteByAdminId(1L);
    }
    
    @Test
    public void testGetRoleNameByAdminId(){
        assertNull(service.getRoleNameByAdminId(null));
        
        when(adminRoleMapper.selectRoleIdByAdminId(1L)).thenReturn(null);
        assertNull(service.getRoleNameByAdminId(1L));
        
        when(adminRoleMapper.selectRoleIdByAdminId(1L)).thenReturn(1L);
        when(adminRoleMapper.getRoleNameByAdminId(1L)).thenReturn("");
        assertEquals(service.getRoleNameByAdminId(1L),"");
        
        verify(adminRoleMapper,times(2)).selectRoleIdByAdminId(1L);
        verify(adminRoleMapper,times(1)).getRoleNameByAdminId(1L);
    }
}

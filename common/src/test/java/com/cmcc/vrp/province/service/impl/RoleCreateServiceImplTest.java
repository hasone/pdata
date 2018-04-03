package com.cmcc.vrp.province.service.impl;

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

import com.cmcc.vrp.province.dao.RoleCreateMapper;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.RoleCreate;
import com.cmcc.vrp.province.service.RoleCreateService;

@RunWith(MockitoJUnitRunner.class)
public class RoleCreateServiceImplTest {
    @InjectMocks
    RoleCreateService service = new RoleCreateServiceImpl();
    
    @Mock
    RoleCreateMapper mapper;
    
    @Test
    public void testGetCreateRolesByRoleId(){
        when(mapper.getCreateRolesByRoleId(1L)).thenReturn(new ArrayList());
        assertNotNull(service.getCreateRolesByRoleId(1L));
        verify(mapper,times(1)).getCreateRolesByRoleId(1L);
    }
    
    @Test
    public void testSelectRoleIdsCreateByRoleId(){
        assertNull(service.selectRoleIdsCreateByRoleId(null));
        
        when(mapper.selectRoleIdsCreateByRoleId(1L)).thenReturn(new ArrayList());
        assertNotNull(service.selectRoleIdsCreateByRoleId(1L));
        verify(mapper,times(1)).selectRoleIdsCreateByRoleId(1L);
    }
    
    @Test
    public void testUpdateRoleWithRoleIdsCreate1()throws Exception{
        List<Long> roleIdsList = new ArrayList();
        roleIdsList.add(1L);
        assertFalse(service.updateRoleWithRoleIdsCreate(null, roleIdsList));
        assertFalse(service.updateRoleWithRoleIdsCreate(new Role(), roleIdsList));
        
        Role role = new Role();
        role.setRoleId(1L);
        when(mapper.selectRoleIdsCreateByRoleId(role.getRoleId())).thenReturn(null);
        assertTrue(service.updateRoleWithRoleIdsCreate(role, null));
        
        when(mapper.selectRoleIdsCreateByRoleId(role.getRoleId())).thenReturn(new ArrayList());
        assertTrue(service.updateRoleWithRoleIdsCreate(role, null));

        when(mapper.selectRoleIdsCreateByRoleId(role.getRoleId())).thenReturn(new ArrayList());
        when(mapper.select(Mockito.anyMap())).thenReturn(new RoleCreate());
        when(mapper.insert(Mockito.any(RoleCreate.class))).thenReturn(0);
        assertTrue(service.updateRoleWithRoleIdsCreate(role, createRoleIdList()));

        when(mapper.selectRoleIdsCreateByRoleId(role.getRoleId())).thenReturn(new ArrayList());
        when(mapper.select(Mockito.anyMap())).thenReturn(null);
        when(mapper.insert(Mockito.any(RoleCreate.class))).thenReturn(1);
        assertTrue(service.updateRoleWithRoleIdsCreate(role, createRoleIdList()));
        
        when(mapper.selectRoleIdsCreateByRoleId(role.getRoleId())).thenReturn(createRoleIdList2());
        assertTrue(service.updateRoleWithRoleIdsCreate(role, createRoleIdList()));
        
        when(mapper.selectRoleIdsCreateByRoleId(role.getRoleId())).thenReturn(createRoleIdList());
        when(mapper.select(Mockito.anyMap())).thenReturn(new RoleCreate());
        when(mapper.deleteRoleCreateByRoleId(Mockito.any(RoleCreate.class))).thenReturn(1);
        assertTrue(service.updateRoleWithRoleIdsCreate(role, createRoleIdList2()));


    }
    
    @Test(expected = RuntimeException.class)
    public void testUpdateRoleWithRoleIdsCreate2()throws Exception{
        Role role = new Role();
        role.setRoleId(1L);
        when(mapper.selectRoleIdsCreateByRoleId(role.getRoleId())).thenReturn(createRoleIdList());
        when(mapper.select(Mockito.anyMap())).thenReturn(new RoleCreate());
        when(mapper.deleteRoleCreateByRoleId(Mockito.any(RoleCreate.class))).thenReturn(0);
        assertTrue(service.updateRoleWithRoleIdsCreate(role, createRoleIdList2()));
    }
    
    @Test(expected = RuntimeException.class)
    public void testUpdateRoleWithRoleIdsCreate3()throws Exception{
        Role role = new Role();
        role.setRoleId(1L);
    
        List<Long> roleIdsList2 = new ArrayList();
        roleIdsList2.add(1L);
        roleIdsList2.add(2L);
        when(mapper.selectRoleIdsCreateByRoleId(role.getRoleId())).thenReturn(new ArrayList());
        when(mapper.select(Mockito.anyMap())).thenReturn(null);
        when(mapper.insert(Mockito.any(RoleCreate.class))).thenReturn(0);
        assertTrue(service.updateRoleWithRoleIdsCreate(role, roleIdsList2));
    }
    
    private List<Long> createRoleIdList(){
        List<Long> roleIdsList2 = new ArrayList();
        roleIdsList2.add(1L);
        roleIdsList2.add(2L);
        return roleIdsList2;
    }
    
    private List<Long> createRoleIdList2(){
        List<Long> roleIdsList2 = new ArrayList();
        roleIdsList2.add(1L);
        return roleIdsList2;
    }
    
}

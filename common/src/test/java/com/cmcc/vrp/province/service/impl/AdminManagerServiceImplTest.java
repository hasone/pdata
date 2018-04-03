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

import com.cmcc.vrp.province.dao.AdminManagerMapper;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.ManagerService;

/**
 * AdminManagerService单元测试类
 * @author wujiamin
 * @date 2016年10月26日
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminManagerServiceImplTest {

    @InjectMocks
    AdminManagerService adminManagerService = new AdminManagerServiceImpl();

    @Mock
    AdminManagerMapper adminManagerMapper;

    @Mock
    EntManagerService entManagerService;

    @Mock
    ManagerService managerService;

    @Mock
    AdminRoleService adminRoleService;

    /**
     * 
     */
    @Test
    public void testInsertAdminManager1() {
        AdminManager record = new AdminManager();
        record.setManagerId(1L);
        record.setCreatorId(1L);
        record.setAdminId(1L);

        when(adminManagerMapper.insert(record)).thenReturn(1);
        assertTrue(adminManagerService.insertAdminManager(record));

        record.setAdminId(null);
        assertFalse(adminManagerService.insertAdminManager(record));

        verify(adminManagerMapper, times(1)).insert(record);
    }

    /**
     * 
     */
    @Test
    public void testInsertAdminManager2() {
        AdminManager record = new AdminManager();
        record.setCreatorId(1L);
        record.setAdminId(1L);
        assertFalse(adminManagerService.insertAdminManager(record));
        verify(adminManagerMapper, times(0)).insert(record);
    }

    /**
     * 
     */
    @Test
    public void testInsertAdminManager3() {
        AdminManager record = new AdminManager();
        record.setManagerId(1L);
        record.setAdminId(1L);

        when(adminManagerMapper.insert(record)).thenReturn(1);
        assertFalse(adminManagerService.insertAdminManager(record));
        verify(adminManagerMapper, times(0)).insert(record);
    }

    /**
     * 
     */
    @Test
    public void testGetAdminIdForEnter() {
        Long entId = 1L;
        when(adminManagerMapper.getAdminIdForEnter(entId)).thenReturn(new ArrayList<Long>());

        assertNotNull(adminManagerService.getAdminIdForEnter(entId));

        verify(adminManagerMapper, times(1)).getAdminIdForEnter(entId);
    }

    /**
     * 
     */
    @Test
    public void testDeleteAdminByEntId() {
        Long entId = 1L;
        when(adminManagerMapper.deleteAdminByEntId(entId)).thenReturn(0);

        assertFalse(adminManagerService.deleteAdminByEntId(entId));

        when(adminManagerMapper.deleteAdminByEntId(entId)).thenReturn(1);
        assertTrue(adminManagerService.deleteAdminByEntId(entId));

        verify(adminManagerMapper, times(2)).deleteAdminByEntId(entId);
    }

    /**
     * 
     */
    @Test
    public void testSelectAdminIdByManagerId() {
        Long managerId = 1L;
        when(adminManagerMapper.selectAdminIdByManagerId(managerId)).thenReturn(new ArrayList<Long>());

        assertNotNull(adminManagerService.selectAdminIdByManagerId(managerId));

        verify(adminManagerMapper, times(1)).selectAdminIdByManagerId(managerId);
    }

    /**
     * 
     */
    @Test
    public void testGetAdminForEnter() {
        Long entId = 1L;
        when(adminManagerMapper.getAdminForEnter(entId)).thenReturn(new ArrayList<Administer>());

        assertNotNull(adminManagerService.getAdminForEnter(entId));

        verify(adminManagerMapper, times(1)).getAdminForEnter(entId);
    }

    /**
     * 
     */
    @Test
    public void testSelectManagerIdByAdminId() {
        Long adminId = 1L;
        when(adminManagerMapper.selectManagerIdByAdminId(adminId)).thenReturn(1L);
        assertNotNull((adminManagerService.selectManagerIdByAdminId(adminId)));
        verify(adminManagerMapper, times(1)).selectManagerIdByAdminId(adminId);
    }

    /**
     * 
     */
    @Test
    public void testGetCustomerManagerByEntId() {
        Long entId = null;
        assertNull((adminManagerService.getCustomerManagerByEntId(entId)));

        entId = 1L;

        when(entManagerService.getManagerForEnter(entId)).thenReturn(createManager());
        when(managerService.selectHigherFatherNodeByParentId(1L)).thenReturn(createManager());
        when(adminManagerService.getAdminByManageId(1L)).thenReturn(new ArrayList<Administer>());

        assertNotNull((adminManagerService.getCustomerManagerByEntId(1L)));
        verify(entManagerService, times(1)).getManagerForEnter(1L);
        verify(managerService, times(1)).selectHigherFatherNodeByParentId(1L);
        verify(adminManagerMapper, times(1)).getAdminByManageId(1L);
    }

    /**
     * 
     */
    @Test
    public void testGetAdminByManageId() {
        assertNull((adminManagerService.getAdminByManageId(null)));

        when(adminManagerMapper.getAdminByManageId(1L)).thenReturn(new ArrayList<Administer>());

        assertNotNull((adminManagerService.getAdminByManageId(1L)));
        verify(adminManagerMapper, times(1)).getAdminByManageId(1L);
    }

    /**
     * 
     */
    @Test
    public void testEditUserManager() {
        AdminManager adminManager = new AdminManager();
        adminManager.setAdminId(1L);
        adminManager.setManagerId(-1L);
        adminManager.setCreatorId(1L);

        AdminManager adminManagerNew = new AdminManager();
        adminManagerNew.setAdminId(adminManager.getAdminId());
        adminManagerNew.setManagerId(-1L);
        adminManagerNew.setCreatorId(adminManager.getCreatorId());

        when(adminManagerMapper.deleteByAdminId(1L)).thenReturn(1);
        when(adminRoleService.deleteByAdminId(1L)).thenReturn(true);
        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(true);
        when(adminManagerMapper.insert(Mockito.any(AdminManager.class))).thenReturn(1);

        assertTrue((adminManagerService.editUserManager(adminManager)));

        adminManager.setManagerId(1L);
        when(managerService.selectByPrimaryKey(1L)).thenReturn(new Manager());
        assertTrue((adminManagerService.editUserManager(adminManager)));

        try {
            when(adminManagerMapper.insert(Mockito.any(AdminManager.class))).thenReturn(0);
            adminManagerService.editUserManager(adminManager);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * 
     */
    @Test
    public void testDeleteManagerByAdmin() {
        when(adminManagerMapper.selectManagerIdByAdminId(1L)).thenReturn(1L);
        when(adminManagerMapper.deleteByAdminId(1L)).thenReturn(1);

        assertTrue(adminManagerService.deleteManagerByAdmin(1L));

        when(adminManagerMapper.selectManagerIdByAdminId(1L)).thenReturn(null);
        assertTrue(adminManagerService.deleteManagerByAdmin(1L));

        verify(adminManagerMapper, times(1)).deleteByAdminId(1L);
    }

    private Manager createManager() {
        Manager manager = new Manager();
        manager.setParentId(1L);
        manager.setId(1L);
        return manager;
    }

    /**
     * 
     */
    @Test
    public void updateByPrimaryKeySelective() {
        //参数校验
        assertFalse(adminManagerService.updateByPrimaryKeySelective(null));

        assertFalse(adminManagerService.updateByPrimaryKeySelective(new AdminManager()));

        AdminManager adminManager = new AdminManager();
        adminManager.setId(1L);
        when(adminManagerMapper.updateByPrimaryKeySelective(Mockito.any(AdminManager.class))).thenReturn(0);
        assertFalse(adminManagerService.updateByPrimaryKeySelective(adminManager));

        when(adminManagerMapper.updateByPrimaryKeySelective(Mockito.any(AdminManager.class))).thenReturn(1);
        assertTrue(adminManagerService.updateByPrimaryKeySelective(adminManager));
    }

    /**
     * 
     */
    @Test
    public void testSelectByAdminId() {
        assertNull(adminManagerService.selectByAdminId(null));

        when(adminManagerMapper.selectByAdminId(Mockito.anyLong())).thenReturn(null);
        assertNull(adminManagerService.selectByAdminId(1L));
    }
    
    /**
     * 
     */
    @Test
    public void testGetAdminByManageIds() {
        List<Manager> manageIds = new ArrayList<Manager>();
        manageIds.add(new Manager());
        
        when(adminManagerMapper.getAdminByManageIds(Mockito.anyMap())).thenReturn(null);
        assertNull(adminManagerService.getAdminByManageIds(manageIds));
        
        manageIds.clear();
        assertNotNull(adminManagerService.getAdminByManageIds(manageIds));
    }
}


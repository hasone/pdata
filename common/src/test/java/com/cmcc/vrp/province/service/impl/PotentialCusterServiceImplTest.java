package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.dao.PotentialCustomerMapper;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.RoleAuthority;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PotentialCusterService;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PotentialCusterServiceImplTest {

    @InjectMocks
    PotentialCusterService pcService = new PotentialCusterServiceImpl();

    @Mock
    PotentialCustomerMapper potentialCustomerMapper;

    @Mock
    DistrictMapper districtMapper;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;

    @Mock
    AdministerService administerService;

    @Mock
    ManagerService managerService;

    @Mock
    EntManagerService entManagerService;

    @Mock
    AdminManagerService adminManagerService;

    @Mock
    AdminRoleService adminRoleService;

    @Mock
    RoleAuthorityService roleAuthorityService;
    
    @Mock
    GlobalConfigService globalConfigService;

    @Test
    public void testSavePotentialEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        String cmPhone = "18867101111";
        String emPhone = "18867102222";
        String emName = "test";
        Long currentUserId = 1L;
        when(globalConfigService.get(GlobalConfigKeyEnum.LOGIN_TYPE.getKey())).thenReturn("false");
        when(enterprisesService.insertSelective(enterprise)).thenReturn(false).thenReturn(true);
        try {
            pcService.savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        doNothing().when(enterpriseSmsTemplateService).insertDefaultSmsTemplate(enterprise.getId());
        Administer cm = new Administer();
        cm.setId(1L);
        Manager manager = new Manager();
        manager.setId(1L);
        List<RoleAuthority> ars = new ArrayList();
        when(administerService.selectByMobilePhone(cmPhone)).thenReturn(cm);
        when(managerService.selectByAdminId(cm.getId())).thenReturn(manager);
        when(roleAuthorityService.selectExistingRoleAuthorityByAuthorityName(Mockito.anyString())).thenReturn(null).thenReturn(ars);
        try {
            pcService.savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            pcService.savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        RoleAuthority ra = new RoleAuthority();
        ra.setRoleId(1L);
        ars.add(ra);
        when(managerService.createManager(Mockito.any(Manager.class), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        try {
            pcService.savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        when(entManagerService.insertEntManager(Mockito.any(EntManager.class))).thenReturn(false).thenReturn(true);
        try {
            pcService.savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Administer emUser = new Administer();
        when(administerService.selectByMobilePhone(emPhone)).thenReturn(emUser);
        when(administerService.createAdminister(Mockito.anyLong(), Mockito.any(Administer.class),
            Mockito.any(Administer.class), Mockito.anyLong())).thenReturn(false).thenReturn(true);

        try {
            pcService.savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(pcService.savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, currentUserId));
    }


    @Test
    public void testSaveEditPotential() {
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        String cmPhone = "18867101111";
        String emPhone = "18867102222";
        String emName = "test";
        Long currentUserId = 1L;

        when(enterprisesService.updateByPrimaryKeySelective(enterprise)).thenReturn(false).thenReturn(true);
        try {
            pcService.saveEditPotential(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Long managerId = 1L;
        Manager entManagerOrg = new Manager();
        entManagerOrg.setId(1L);
        when(entManagerService.getManagerIdForEnter(enterprise.getId())).thenReturn(managerId);
        when(managerService.selectByPrimaryKey(managerId)).thenReturn(entManagerOrg);
        Administer cm = new Administer();
        cm.setId(1L);
        Manager manager = new Manager();
        manager.setId(1L);
        when(administerService.selectByMobilePhone(cmPhone)).thenReturn(cm);
        when(managerService.selectByAdminId(cm.getId())).thenReturn(manager);
        when(managerService.updateByPrimaryKeySelective(Mockito.any(Manager.class))).thenReturn(false).thenReturn(true);
        try {
            pcService.saveEditPotential(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<Long> adminIds = new ArrayList();
        adminIds.add(1L);
        adminIds.add(2L);
        when(adminManagerService.selectAdminIdByManagerId(managerId)).thenReturn(adminIds);
        when(adminRoleService.deleteByAdminId(Mockito.anyLong())).thenReturn(false).thenReturn(true);
        try {
            pcService.saveEditPotential(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(false).thenReturn(true);
        try {
            pcService.saveEditPotential(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(false).thenReturn(true);
        try {
            pcService.saveEditPotential(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(adminManagerService.deleteAdminByEntId(Mockito.anyLong())).thenReturn(false).thenReturn(true);
        try {
            pcService.saveEditPotential(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Administer emUser = new Administer();
        when(administerService.selectByMobilePhone(emPhone)).thenReturn(emUser);
        when(administerService.createAdminister(Mockito.anyLong(), Mockito.any(Administer.class),
            Mockito.any(Administer.class), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        try {
            pcService.saveEditPotential(enterprise, cmPhone, emPhone, emName, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(pcService.saveEditPotential(enterprise, cmPhone, emPhone, emName, currentUserId));

    }


}

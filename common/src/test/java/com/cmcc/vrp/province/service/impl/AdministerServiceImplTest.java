package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.AdministerMapper;
import com.cmcc.vrp.province.dao.TmpaccountMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.AdminEnter;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.PasswordEncoder;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.wx.model.Tmpaccount;

/**
 * @ClassName: AdministerServiceImplTest
 * @Description: 测试后台管理员实现类
 * @author: qihang
 * @date: 2015年3月19日上午10:16:13
 */
@RunWith(MockitoJUnitRunner.class)
public class AdministerServiceImplTest {
    @InjectMocks
    AdministerService administerService = new AdministerServiceImpl();

    @Mock
    AdministerMapper administerMapper;

    @Mock
    AdminRoleService adminRoleService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    AdminEnterService adminEnterService;

    @Mock
    EntManagerService entManagerService;

    @Mock
    ManagerService managerService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    MdrcCardmakerService mdrcCardMakerService;

    @Mock
    AdminManagerService adminManagerService;

    @Mock
    IndividualProductService individualProductService;

    @Mock
    IndividualProductMapService individualProductMapService;

    @Mock
    IndividualAccountService individualAccountService;

    @Mock
    PasswordEncoder passwordEncoder;
    
    @Mock
    TmpaccountMapper tmpaccountMapper;

    /**
     *
     */
    @Test
    public void testSelectByAdministerById() {
        Long id = null;
        assertNull(administerService.selectAdministerById(id));

        Long id2 = 1L;
        when(administerMapper.selectByPrimaryKey(id2)).thenReturn(new Administer());
        when(managerService.selectByAdminId(id2)).thenReturn(new Manager());

        assertNotNull(administerService.selectAdministerById(id2));
    }

    /**
     *
     */
    @Test
    public void testSelectByUserName() {
        String userName = null;
        assertNull(administerService.selectByUserName(userName));

        userName = "";
        when(administerMapper.selectByUserName(userName)).thenReturn(new ArrayList<Administer>());
        assertNotNull(administerService.selectByUserName(userName));
        verify(administerMapper, times(1)).selectByUserName(userName);
    }

    /**
     * @Title:testInsertAdminister
     * @Description: 测试AdministerService的InsertAdminister函数
     * @author: qihang
     */
    @Test
    public void testInsertAdminister() {
        when(globalConfigService.get("ENTERPRISE_CONTACTOR_ROLE_ID")).thenReturn("3");
        when(globalConfigService.get("FLOW_CARD_MANAGER_ROLE_ID")).thenReturn("4");

        Administer admin = new Administer();
        assertFalse(administerService.insertAdminister(admin, 1L, 1L));

        admin = getValidAdminister();
        assertFalse(administerService.insertAdminister(admin, null, 1L));
        assertFalse(administerService.insertAdminister(admin, 3L, null));
        assertFalse(administerService.insertAdminister(admin, 4L, null));

        when(administerMapper.insertSelective(admin)).thenReturn(0);
        assertFalse(administerService.insertAdminister(admin, 3L, 1L));

        when(administerMapper.insertSelective(admin)).thenReturn(1);
        when(adminRoleService.insertAdminRole(admin.getId(), 3L)).thenReturn(false);
        try {
            administerService.insertAdminister(admin, 3L, 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(administerMapper.insertSelective(admin)).thenReturn(1);
        when(adminRoleService.insertAdminRole(admin.getId(), 3L)).thenReturn(true);
        when(mdrcCardMakerService.selectByPrimaryKey(1L)).thenReturn(null);
        try {
            administerService.insertAdminister(admin, 3L, 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(administerMapper.insertSelective(admin)).thenReturn(1);
        when(adminRoleService.insertAdminRole(admin.getId(), 4L)).thenReturn(true);
        when(mdrcCardMakerService.selectByPrimaryKey(1L)).thenReturn(null);
        try {
            administerService.insertAdminister(admin, 4L, 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(administerMapper.insertSelective(admin)).thenReturn(1);
        when(adminRoleService.insertAdminRole(admin.getId(), 4L)).thenReturn(true);
        MdrcCardmaker mdrcCardmaker = new MdrcCardmaker();
        when(mdrcCardMakerService.selectByPrimaryKey(1L)).thenReturn(mdrcCardmaker);

        assertTrue(administerService.insertAdminister(admin, 4L, 1L));

    }

    /**
     *
     */
    @Test
    public void testInsert() {
        String mobile = "18600000000";
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        try {
            administerService.insert(mobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(false);
        try {
            administerService.insert(mobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(false);
        try {
            administerService.insert(mobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(true);

        assertTrue(administerService.insert(mobile));

    }

    /**
     *
     */
    @Test
    public void testInsertForScJizhong() {
        String mobile = "18600000000";
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        try {
            administerService.insertForScJizhong(mobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(false);
        try {
            administerService.insertForScJizhong(mobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(false);
        try {
            administerService.insertForScJizhong(mobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<IndividualProduct> products = new ArrayList<IndividualProduct>();
        IndividualProduct p = new IndividualProduct();
        p.setPrice(0);
        p.setId(1L);
        products.add(p);
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(true);
        when(individualProductService.selectByDefaultValue(1)).thenReturn(products);
        when(individualProductMapService.batchInsert(Mockito.any(ArrayList.class))).thenReturn(false);
        try {
            administerService.insertForScJizhong(mobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(true);
        when(individualProductService.selectByDefaultValue(1)).thenReturn(products);
        when(individualProductMapService.batchInsert(Mockito.any(ArrayList.class))).thenReturn(true);
        when(individualAccountService.batchInsert(Mockito.any(ArrayList.class))).thenReturn(false);
        try {
            administerService.insertForScJizhong(mobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(true);
        when(individualProductMapService.batchInsert(Mockito.any(ArrayList.class))).thenReturn(true);
        when(individualAccountService.batchInsert(Mockito.any(ArrayList.class))).thenReturn(true);
        assertTrue(administerService.insertForScJizhong(mobile));

    }

    /**
     *
     */
    @Test
    public void testInsertSelective() {
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        assertTrue(administerService.insertSelective(new Administer()));
        verify(administerMapper, times(1)).insertSelective(Mockito.any(Administer.class));

        assertFalse(administerService.insertSelective(null));
    }

    /**
     *
     */
    @Test
    public void testUpdateAdminister() {
        Administer administer = null;
        assertFalse(administerService.updateAdminister(administer, 1L, 1L));

        administer = getValidAdminister();
        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(0);
        assertFalse(administerService.updateAdminister(administer, 1L, 1L));

        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminRoleService.deleteByAdminId(1L)).thenReturn(true);
        when(adminRoleService.insertAdminRole(administer.getId(), 1L)).thenReturn(false);
        try {
            administerService.updateAdminister(administer, 1L, 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminRoleService.deleteByAdminId(1L)).thenReturn(true);
        when(adminRoleService.insertAdminRole(administer.getId(), 1L)).thenReturn(true);
        when(globalConfigService.get("ENTERPRISE_CONTACTOR_ROLE_ID")).thenReturn("3");
        when(adminEnterService.deleteByAdminId(1L)).thenReturn(true);
        when(adminEnterService.insert(1L, 1L)).thenReturn(true);

        assertTrue(administerService.updateAdminister(administer, 1L, 1L));

        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminRoleService.deleteByAdminId(1L)).thenReturn(true);
        when(adminRoleService.insertAdminRole(administer.getId(), 1L)).thenReturn(true);
        when(globalConfigService.get("ENTERPRISE_CONTACTOR_ROLE_ID")).thenReturn("1");
        when(adminEnterService.deleteByAdminId(1L)).thenReturn(true);
        when(adminEnterService.insert(1L, 1L)).thenReturn(true);

        assertTrue(administerService.updateAdminister(administer, 1L, 1L));

    }

    /**
     *
     */
    @Test
    public void testUpdateSelective() {
        Administer admin = new Administer();
        assertFalse(administerService.updateSelective(admin));

        admin = getValidAdminister();
        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        assertTrue(administerService.updateSelective(admin));
        verify(administerMapper, times(1)).updateByPrimaryKeySelective(admin);
    }

    /**
     *
     */
    @Test
    public void testUpdateAdministerPassword() {
        Long userId = null;
        String szNewPassword = "fdkasl_34KJLKJ";
        String salt = "fdkjslda";
        assertFalse(administerService.updateAdministerPassword(userId, szNewPassword));

        userId = 1L;
        String encodedPwd = DigestUtils.sha256Hex(szNewPassword + salt);
        when(passwordEncoder.encode(szNewPassword, salt)).thenReturn(encodedPwd);
        when(administerMapper.updatePasswordByKey(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(1);

        assertTrue(administerService.updateAdministerPassword(userId, szNewPassword));
        verify(administerMapper, times(1)).updatePasswordByKey(Mockito.anyLong(), Mockito.anyString(),
            Mockito.anyString());

    }

    /**
     *
     */
    @Test
    public void deleteById() {
        Long id = null;
        assertFalse(administerService.deleteById(id));

        id = 1L;
        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(0);
        assertFalse(administerService.deleteById(id));

        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        assertTrue(administerService.deleteById(id));

        when(adminRoleService.selectCountByQuery(Mockito.any(AdminRole.class))).thenReturn(1);
        try {
            administerService.deleteById(id);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        when(adminRoleService.selectCountByQuery(Mockito.any(AdminRole.class))).thenReturn(0);
        when(adminEnterService.selectCountByQuery(Mockito.any(AdminEnter.class))).thenReturn(1);
        when(adminEnterService.deleteByAdminId(id)).thenReturn(false);
        try {
            administerService.deleteById(id);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     *
     */
    @Test
    public void queryPaginationAdminCount() {
        when(managerService.getChildNode(Mockito.anyLong())).thenReturn(null);
        when(administerMapper.queryPaginationAdminCount(Mockito.any(Map.class))).thenReturn(0);
        assertEquals(administerService.queryPaginationAdminCount(new QueryObject()), 0);

        List<Long> nodes = new ArrayList<Long>();
        nodes.add(1L);
        when(managerService.getChildNode(Mockito.anyLong())).thenReturn(nodes);
        assertEquals(administerService.queryPaginationAdminCount(new QueryObject()), 0);

        verify(administerMapper, times(2)).queryPaginationAdminCount(Mockito.any(Map.class));
        verify(managerService, times(2)).getChildNode(Mockito.anyLong());
    }

    /**
     *
     */
    @Test
    public void queryPaginationAdminList() {
        when(managerService.getChildNode(Mockito.anyLong())).thenReturn(null);
        when(administerMapper.queryPaginationAdminList(Mockito.any(Map.class))).thenReturn(new ArrayList());
        assertNotNull(administerService.queryPaginationAdminList(new QueryObject()));

        List<Long> nodes = new ArrayList<Long>();
        nodes.add(1L);
        when(managerService.getChildNode(Mockito.anyLong())).thenReturn(nodes);
        assertNotNull(administerService.queryPaginationAdminList(new QueryObject()));

        verify(administerMapper, times(2)).queryPaginationAdminList(Mockito.any(Map.class));
        verify(managerService, times(2)).getChildNode(Mockito.anyLong());
    }

    /**
     *
     */
    @Test
    public void testSelectByMobilePhone() {
        Administer administer = getValidAdminister();
        when(administerMapper.selectByMobilePhone(administer.getMobilePhone())).thenReturn(administer);
        assertNotNull(administerService.selectByMobilePhone(administer.getMobilePhone()));
        verify(administerMapper, times(1)).selectByMobilePhone(administer.getMobilePhone());
    }

    /**
     *
     */
    @Test
    public void testQueryUserAuthoriesByMobile() {
        String mobilePhone = null;
        assertNull(administerService.queryUserAuthoriesByMobile(mobilePhone));

        mobilePhone = "18600000000";
        when(administerMapper.queryUserAuthoriesByMobile(mobilePhone)).thenReturn(new ArrayList());
        assertNotNull(administerService.queryUserAuthoriesByMobile(mobilePhone));
        verify(administerMapper, times(1)).queryUserAuthoriesByMobile(mobilePhone);
    }

    /**
     *
     */
    @Test
    public void testSelectAllAdministers() {
        when(administerMapper.selectAllAdministers()).thenReturn(new ArrayList());
        assertNotNull(administerService.selectAllAdministers());
        verify(administerMapper, times(1)).selectAllAdministers();
    }

    /**
     *
     */
    @Test
    public void testCheckUnique() {
        Administer administer = null;
        assertTrue(administerService.checkUnique(administer));

        administer = getValidAdminister();
        ArrayList list = new ArrayList();
        list.add(administer);
        when(administerMapper.selectAllAdministers()).thenReturn(list);

        assertTrue(administerService.checkUnique(administer));

        Administer administer2 = getValidAdminister();
        administer2.setId(2L);
        list.add(administer2);
        assertFalse(administerService.checkUnique(administer));
    }

    /**
     *
     */
    @Test
    public void testCheckNameUnique() {
        Long id = 1L;
        String name = null;
        assertFalse(administerService.checkNameUnique(id, name));

        name = "1";
        Administer administer = getValidAdminister();
        ArrayList list = new ArrayList();
        list.add(administer);
        when(administerMapper.selectAllAdministers()).thenReturn(list);
        assertFalse(administerService.checkNameUnique(id, name));

        name = "2";
        assertTrue(administerService.checkNameUnique(id, name));
    }

    //    /**
    //     * 
    //     */
    //    @Test
    //    public void tetsSelectManagerByEnterIdAndRoleCode() {
    //        when(administerMapper.selectManagerByEnterIdAndRoleCode(new HashMap())).thenReturn(new ArrayList());
    //        assertNotNull(administerService.selectManagerByEnterIdAndRoleCode(new HashMap()));
    //        verify(administerMapper, times(1)).selectManagerByEnterIdAndRoleCode(new HashMap());
    //    }

    /**
     *
     */
    @Test
    public void tetsCheckPassword() {
        String password = "123";
        String mobilePhone = "18600000000";

        when(administerMapper.selectByMobilePhone(mobilePhone)).thenReturn(null);
        assertFalse(administerService.checkPassword(password, mobilePhone));

        Administer admin = getValidAdminister();
        admin.setPassword(DigestUtils.md5Hex("123"));
        when(administerMapper.selectByMobilePhone(mobilePhone)).thenReturn(admin);

        assertFalse(administerService.checkPassword(password, mobilePhone));

        admin.setPasswordNew("123");
        assertFalse(administerService.checkPassword(password, mobilePhone));
    }

    /**
     *
     */
    @Test
    public void testQueryCMByDistrictId() {
        Long districtId = null;
        Long roleId = null;
        assertNull(administerService.queryCMByDistrictId(districtId, roleId));

        districtId = 1L;
        roleId = 1L;
        when(administerMapper.selectCustomerByDistrictId(districtId, roleId)).thenReturn(new ArrayList());

        assertNotNull(administerService.queryCMByDistrictId(districtId, roleId));
        verify(administerMapper, times(1)).selectCustomerByDistrictId(districtId, roleId);
    }

    /**
     *
     */
    @Test
    public void testSelectEMByEnterpriseId() {
        Long entId = null;
        assertNull(administerService.selectEMByEnterpriseId(entId));

        entId = 1L;
        when(administerMapper.selectEMByEnterpriseId(entId)).thenReturn(new ArrayList());

        assertNotNull(administerService.selectEMByEnterpriseId(entId));
        verify(administerMapper, times(1)).selectEMByEnterpriseId(entId);
    }

    /**
     *
     */
    @Test
    public void testCreateAdminister() {
        when(managerService.selectByPrimaryKey(1L)).thenReturn(null);
        assertFalse(administerService.createAdminister(1L, new Administer(), new Administer(), 1L));

        when(managerService.selectByPrimaryKey(null)).thenReturn(new Manager());
        assertFalse(administerService.createAdminister(null, getValidAdminister(), null, 1L));

        when(managerService.selectByPrimaryKey(1L)).thenReturn(new Manager());
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        try {
            administerService.createAdminister(1L, getValidAdminister(), null, 1L);
        } catch (Exception e) {

        }

        when(managerService.selectByPrimaryKey(1L)).thenReturn(new Manager());
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        when(adminManagerService.deleteManagerByAdmin(Mockito.anyLong())).thenReturn(false);
        try {
            administerService.createAdminister(1L, getValidAdminister(), getValidAdminister(), 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(managerService.selectByPrimaryKey(1L)).thenReturn(new Manager());
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        when(adminManagerService.deleteManagerByAdmin(Mockito.anyLong())).thenReturn(true);
        try {
            administerService.createAdminister(1L, getValidAdminister(), getValidAdminister(), 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(managerService.selectByPrimaryKey(1L)).thenReturn(new Manager());
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        when(adminManagerService.deleteManagerByAdmin(Mockito.anyLong())).thenReturn(true);
        when(adminRoleService.deleteByAdminId(Mockito.anyLong())).thenReturn(true);
        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(0);
        try {
            administerService.createAdminister(1L, getValidAdminister(), getValidAdminister(), 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(managerService.selectByPrimaryKey(1L)).thenReturn(new Manager());
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        when(adminManagerService.deleteManagerByAdmin(Mockito.anyLong())).thenReturn(true);
        when(adminRoleService.deleteByAdminId(Mockito.anyLong())).thenReturn(true);
        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        try {
            administerService.createAdminister(1L, getValidAdminister(), getValidAdminister(), 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(managerService.selectByPrimaryKey(1L)).thenReturn(new Manager());
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        when(adminManagerService.deleteManagerByAdmin(Mockito.anyLong())).thenReturn(true);
        when(adminRoleService.deleteByAdminId(Mockito.anyLong())).thenReturn(true);
        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        try {
            administerService.createAdminister(1L, getValidAdminister(), getValidAdminister(), 1L);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        when(managerService.selectByPrimaryKey(1L)).thenReturn(new Manager());
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        when(adminManagerService.deleteManagerByAdmin(Mockito.anyLong())).thenReturn(true);
        when(adminRoleService.deleteByAdminId(Mockito.anyLong())).thenReturn(true);
        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        when(adminRoleService.insertAdminRole(Mockito.any(AdminRole.class))).thenReturn(true);

        assertTrue(administerService.createAdminister(1L, getValidAdminister(), getValidAdminister(), 1L));
    }

    /**
     *
     */
    @Test
    public void testStatisticRoleCountByManangerId() {
        Long managerId = 1L;
        when(managerService.getSonTreeIdByManageId(managerId)).thenReturn(new ArrayList());
        when(administerMapper.selectRoleCountByManagerId(new HashMap())).thenReturn(0);
        assertEquals(administerService.statisticRoleCountByManangerId(managerId), 0);
        verify(managerService, times(1)).getSonTreeIdByManageId(managerId);
        verify(administerMapper, times(1)).selectRoleCountByManagerId(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testStatisticRoleByManagerId() {
        Long managerId = 1L;
        when(managerService.getSonTreeIdByManageId(managerId)).thenReturn(new ArrayList());
        when(administerMapper.selectRoleCountByManagerId(new HashMap())).thenReturn(0);

        assertNotNull(administerService.statisticRoleByManagerId(managerId, null));
        assertNotNull(administerService.statisticRoleByManagerId(managerId, new QueryObject()));
        verify(managerService, times(2)).getSonTreeIdByManageId(managerId);
        verify(administerMapper, times(2)).selectRoleByManagerId(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testStatisticOneRoleCountByManangerId() {
        Long managerId = 1L;
        Long roleId = 1L;
        Map map = new HashMap();
        map.put("managerId", managerId);
        map.put("managerIds", new ArrayList());
        map.put("roleId", roleId);
        when(managerService.getSonTreeIdByManageId(managerId)).thenReturn(new ArrayList());
        when(administerMapper.selectOneRoleCountByManangerId(Mockito.anyMap())).thenReturn(0);

        assertEquals(administerService.statisticOneRoleCountByManangerId(roleId, managerId), 0);
        verify(managerService, times(1)).getSonTreeIdByManageId(managerId);
        verify(administerMapper, times(1)).selectOneRoleCountByManangerId(Mockito.anyMap());

    }

    /**
     *
     */
    @Test
    public void testStatisticOneRoleByManagerId() {
        Long roleId = 1L;
        Long managerId = 1L;
        when(managerService.getSonTreeIdByManageId(managerId)).thenReturn(new ArrayList());
        when(administerMapper.selectOneRoleByManagerId(new HashMap())).thenReturn(new ArrayList());

        assertNotNull(administerService.statisticOneRoleByManagerId(roleId, managerId, null));
        assertNotNull(administerService.statisticOneRoleByManagerId(roleId, managerId, new QueryObject()));
        verify(managerService, times(2)).getSonTreeIdByManageId(managerId);
        verify(administerMapper, times(2)).selectOneRoleByManagerId(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testCheckPasswordUpdateTime() {
        String mobile = "18600000000";
        Administer administer = getValidAdminister();
        administer.setPasswordUpdateTime(DateUtil.getDateBefore(new Date(), 10));
        when(administerMapper.selectByMobilePhone(mobile)).thenReturn(administer);
        when(globalConfigService.get(GlobalConfigKeyEnum.PASSWORD_EXPIRE_TIME.getKey())).thenReturn("0");
        assertFalse(administerService.checkPasswordUpdateTime(mobile));

        when(globalConfigService.get(GlobalConfigKeyEnum.PASSWORD_EXPIRE_TIME.getKey())).thenReturn(null);
        assertTrue(administerService.checkPasswordUpdateTime(mobile));
    }

    /**
     *
     */
    @Test
    public void testQueryAllUsersByAuthName() {
        String authName = null;
        assertNull(administerService.queryAllUsersByAuthName(authName));

        authName = "1";
        when(administerMapper.queryAllUsersByAuthName(authName)).thenReturn(new ArrayList());
        assertNotNull(administerService.queryAllUsersByAuthName(authName));
        verify(administerMapper, times(1)).queryAllUsersByAuthName(authName);
    }

    /**
     * @Title:Administer
     * @Description: 产生一个管理员
     * @author: qihang
     */
    public Administer getValidAdminister() {
        Administer administer = new Administer();
        administer.setId(1L);
        administer.setEmail("1@163.com");
        administer.setMobilePhone("13811112222");
        administer.setPassword("123");
        administer.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        administer.setEnterpriseCode("12313");
        administer.setCreateTime(new Date());
        administer.setUserName("1");
        administer.setCreatorId(1L);
        return administer;
    }

    public Enterprise getValidEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        enterprise.setCode("123");
        return enterprise;
    }

    /**
     *
     */
    @Test
    public void testCreateAdminister2() {
        //参数校验
        Administer administer = null;
        Assert.assertFalse(administerService.createAdminister(administer));

        administer = new Administer();
        Assert.assertFalse(administerService.createAdminister(administer));

        administer.setMobilePhone("12345678901");

        //校验手机号码是否存在
        AdministerService spy = Mockito.spy(administerService);
        administer = getValidAdminister();
        Mockito.doReturn(getValidAdminister()).when(spy).selectByMobilePhone(Mockito.anyString());
        Assert.assertFalse(spy.createAdminister(administer));

        //校验企业是否存在
        administer.setEnterpriseCode("123456");
        Mockito.doReturn(null).when(spy).selectByMobilePhone(Mockito.anyString());
        Mockito.when(enterprisesService.selectByCode(administer.getEnterpriseCode())).thenReturn(null);
        Assert.assertFalse(spy.createAdminister(administer));

        //校验企业管理员职位节点
        Mockito.doReturn(null).when(spy).selectByMobilePhone(Mockito.anyString());
        Mockito.when(enterprisesService.selectByCode(administer.getEnterpriseCode())).thenReturn(getValidEnterprise());
        Mockito.when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(null);
        Assert.assertFalse(spy.createAdminister(administer));

        //校验操作：生成用户基本信息    	
        try {
            Mockito.doReturn(null).when(spy).selectByMobilePhone(Mockito.anyString());
            Mockito.when(enterprisesService.selectByCode(administer.getEnterpriseCode())).thenReturn(
                    getValidEnterprise());
            Mockito.when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(12L);
            Mockito.when(administerMapper.insertSelective(administer)).thenReturn(0);
            spy.createAdminister(administer);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        //校验操作：建立用户与企业管理员职位节点的关联关系
        try {
            Mockito.doReturn(null).when(spy).selectByMobilePhone(Mockito.anyString());
            Mockito.when(enterprisesService.selectByCode(administer.getEnterpriseCode())).thenReturn(
                    getValidEnterprise());
            Mockito.when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(12L);
            Mockito.when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
            Mockito.when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(false);
            spy.createAdminister(administer);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        //校验操作：建立用户与角色关联关系
        try {
            Mockito.doReturn(null).when(spy).selectByMobilePhone(Mockito.anyString());
            Mockito.when(enterprisesService.selectByCode(administer.getEnterpriseCode())).thenReturn(
                    getValidEnterprise());
            Mockito.when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(12L);
            Mockito.when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
            Mockito.when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
            when(globalConfigService.get(Mockito.anyString())).thenReturn("123");
            when(adminRoleService.insertAdminRole(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
            spy.createAdminister(administer);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        //正常操作
        Administer admin = getValidAdminister();
        Enterprise ent = new Enterprise();
        ent.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        //用户不存在
        when(administerService.selectByMobilePhone(admin.getMobilePhone())).thenReturn(null);
        //企业存在
        when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(ent);
        //企业管理员职位节点存在
        when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(123L);
        //生成用户基本信息成功
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        //建立企业与企业管理员职位节点关联关系成功
        when(adminManagerService.insertAdminManager(Mockito.any(AdminManager.class))).thenReturn(true);
        //建立用户与角色关联关系
        when(globalConfigService.get(Mockito.anyString())).thenReturn("123");
        when(adminRoleService.insertAdminRole(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        //操作成功
        assertTrue(administerService.createAdminister(admin));
    }

    /**
     *
     */
    @Test
    public void testUpdateAdminister2() {
        //参数校验
        assertFalse(administerService.updateAdminister(null));

        Administer administer = getValidAdminister();

        AdministerService as = Mockito.spy(administerService);
        Mockito.doReturn(true).when(as).updateSelective(Mockito.any(Administer.class));

        //校验用户是否存在
        when(administerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(null);
        assertFalse(administerService.updateAdminister(administer));

        //校验企业是否存在
        when(administerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(getValidAdminister());
        when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(null);
        assertFalse(administerService.updateAdminister(administer));

        //校验企业管理员职位节点是否存在
        when(administerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(getValidAdminister());
        when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(getValidEnterprise());
        when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(null);
        assertFalse(administerService.updateAdminister(administer));

        //校验操作：更新用户基本信息
        try {
            when(administerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(getValidAdminister());
            when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(getValidEnterprise());
            when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(12L);
            when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(0);
            assertFalse(administerService.updateAdminister(administer));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //校验用户与企业管理员职位节点的关联关系
        try {
            when(administerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(getValidAdminister());
            when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(getValidEnterprise());
            when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(12L);
            when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(10);
            when(adminManagerService.selectByAdminId(Mockito.anyLong())).thenReturn(null);
            assertFalse(administerService.updateAdminister(administer));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //校验操作：更新用户与企业管理员职位指点的关联关系
        try {
            when(administerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(getValidAdminister());
            when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(getValidEnterprise());
            when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(12L);
            when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
            when(adminManagerService.selectByAdminId(Mockito.anyLong())).thenReturn(getValidAdminManager());
            when(adminManagerService.updateByPrimaryKeySelective(Mockito.any(AdminManager.class))).thenReturn(false);
            administerService.updateAdminister(administer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //正常操作
        AdminManager adminManager = new AdminManager();
        adminManager.setAdminId(1L);
        adminManager.setManagerId(2L);

        when(administerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(getValidAdminister());
        when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(getValidEnterprise());
        when(entManagerService.getManagerIdForEnter(Mockito.anyLong())).thenReturn(12L);
        when(administerMapper.updateByPrimaryKeySelective(Mockito.any(Administer.class))).thenReturn(1);
        when(adminManagerService.selectByAdminId(Mockito.anyLong())).thenReturn(getValidAdminManager2());
        when(adminManagerService.updateByPrimaryKeySelective(Mockito.any(AdminManager.class))).thenReturn(true);
        assertTrue(administerService.updateAdminister(getValidAdminister()));
    }

    private AdminManager getValidAdminManager() {
        AdminManager adminManager = new AdminManager();
        adminManager.setAdminId(1L);
        adminManager.setManagerId(1L);
        adminManager.setDeleteFlag((byte) 1);
        return adminManager;
    }

    private AdminManager getValidAdminManager2() {
        AdminManager adminManager = new AdminManager();
        adminManager.setAdminId(1L);
        adminManager.setManagerId(1L);
        adminManager.setDeleteFlag((byte) 0);
        return adminManager;
    }

    @Test
    public void testGetByManageIds(){
        List<Long> manageIds = new ArrayList<Long>();
        assertNull(administerService.getByManageIds(manageIds));

        manageIds.add(1L);
        Mockito.when(administerMapper.getByMap(anyMap())).thenReturn(new ArrayList<Administer>());
        assertNotNull(administerService.getByManageIds(manageIds));
        Mockito.verify(administerMapper).getByMap(anyMap());
    }
    
    @Test
    public void testInsertForWx(){
        String mobile = "18600000000";
        String openid = "openid";
        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(0);
        try {
            administerService.insertForWx(mobile, openid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerMapper.insertSelective(Mockito.any(Administer.class))).thenReturn(1);
        List<IndividualProduct> products = new ArrayList<IndividualProduct>();
        IndividualProduct p = new IndividualProduct();
        p.setPrice(0);
        p.setId(1L);
        products.add(p);
        when(individualProductService.selectByDefaultValue(1)).thenReturn(products);     
        when(individualAccountService.batchInsert(Mockito.any(ArrayList.class))).thenReturn(false);
        try {
            administerService.insertForWx(mobile, openid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(individualAccountService.batchInsert(Mockito.any(ArrayList.class))).thenReturn(true);
        List<Tmpaccount> tmpAccounts = new ArrayList<Tmpaccount>();
        Tmpaccount tmpAccount = new Tmpaccount();
        tmpAccount.setCount(new BigDecimal(100));
        tmpAccounts.add(tmpAccount);
        when(tmpaccountMapper.selectByOpenid(openid)).thenReturn(tmpAccounts);
        when(individualProductService.getIndivialPointProduct()).thenReturn(new IndividualProduct());
        when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt()))
            .thenReturn(new IndividualAccount());
        when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(), 
                Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        try {
            administerService.insertForWx(mobile, openid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(), 
                Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        assertTrue(administerService.insertForWx(mobile, openid));
    }
    
    @Test
    public void testIsOverAuth(){
        assertTrue(administerService.isOverAuth(null, null));
        
        when(managerService.getManagerByAdminId(Mockito.any(Long.class))).thenReturn(null);
        assertTrue(administerService.isOverAuth(1L, 1L));

        
        Manager fatherManager = new Manager();
        when(managerService.getManagerByAdminId(Mockito.any(Long.class))).thenReturn(fatherManager);
        when(managerService.isProOrCityOrMangerOrEnt(Mockito.any(Long.class))).thenReturn(true);
        when(managerService.isParentManage(Mockito.any(Long.class),Mockito.any(Long.class))).thenReturn(true);
        assertFalse(administerService.isOverAuth(1L, 1L));
        
        when(managerService.isParentManage(Mockito.any(Long.class),Mockito.any(Long.class))).thenReturn(false);
        assertTrue(administerService.isOverAuth(1L, 1L));

    }


}

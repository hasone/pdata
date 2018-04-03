package com.cmcc.vrp.boss.shangdong.boss;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.shangdong.boss.service.impl.SdAdministerServiceImpl;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;

/**
* 
* SdAdministerServiceImpl测试类
* @author panxin
* @date 2016年11月13日 上午10:19:20
*/
@RunWith(MockitoJUnitRunner.class)
public class SdAdministerServiceImplTest {
    @InjectMocks
    SdAdministerServiceImpl sdAdministerService = new SdAdministerServiceImpl();

    @Mock
    AdministerService administerService;

    @Mock
    AdminRoleService adminRoleService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    ManagerService managerService;

    @Mock
    EntManagerService entManagerService;

    @Mock
    AdminManagerService adminManagerService;

    /**
     * testInsertManager
     */
    @Test
    public void testInsertManager(){
        Administer administer = initAdminister();
        when(administerService.selectByMobilePhone(Matchers.anyString())).thenReturn(null);
        when(administerService.insertSelective(administer)).thenReturn(true);
        when(adminRoleService.insertAdminRole(administer.getId(), 2L)).thenReturn(true);
        Manager parentManager = new Manager();
        parentManager.setId(1L);
        when(managerService.get(7L, "济南市")).thenReturn(parentManager);
        when(managerService.createManager(Matchers.any(Manager.class), Matchers.anyLong())).thenReturn(true);
        List<String> enterpriseCode = new ArrayList<String>();
        enterpriseCode.add("01");
        Manager childManager = new Manager();
        when(entManagerService.getManagerForEnterCode(Matchers.anyString())).thenReturn(childManager);
        when(managerService.updateByPrimaryKeySelective(Matchers.any(Manager.class))).thenReturn(true);
        when(adminManagerService.insertAdminManager(Matchers.any(AdminManager.class))).thenReturn(true);
        Assert.assertTrue(sdAdministerService.insertManager(administer, 2L, enterpriseCode, 7L));

        //插入客户经理节点关系表失败
        when(adminManagerService.insertAdminManager(Matchers.any(AdminManager.class))).thenReturn(false);
        Throwable re = null;
        try {
            sdAdministerService.insertManager(administer, 2L, enterpriseCode, 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());

        //插入客户经理节点关系表失败
        when(managerService.updateByPrimaryKeySelective(Matchers.any(Manager.class))).thenReturn(false);
        re = null;
        try {
            sdAdministerService.insertManager(administer, 2L, enterpriseCode, 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());

        //修改企业节点失败
        when(managerService.createManager(Matchers.any(Manager.class), Matchers.anyLong())).thenReturn(false);
        re = null;
        try {
            sdAdministerService.insertManager(administer, 2L, anyListOf(String.class), 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());

        //创建客户经理节点失败
        when(managerService.get(7L, "济南市")).thenReturn(null);
        re = null;
        try {
            sdAdministerService.insertManager(administer, 2L, anyListOf(String.class), 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());

        //获取父节点失败
        when(adminRoleService.insertAdminRole(administer.getId(), 2L)).thenReturn(false);
        re = null;
        try {
            sdAdministerService.insertManager(administer, 2L, anyListOf(String.class), 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());

        // 插入角色用户关系表(adminRole)失败
        when(administerService.insertSelective(administer)).thenReturn(false);
        Assert.assertFalse(sdAdministerService.insertManager(administer, 2L, anyListOf(String.class), 7L));

        //插入用户失败
        when(administerService.selectByMobilePhone("18867105827")).thenReturn(new Administer());
        Assert.assertFalse(sdAdministerService.insertManager(administer, 2L, anyListOf(String.class), 7L));

//        Assert.assertFalse(sdAdministerService.insertManager(null, 2L, anyListOf(String.class), 7L));
    }

    /**
     * testUpdateManager
     */
    @Test
    public void testUpdateManager() {
        Administer administer = initAdminister();
        when(administerService.updateSelective(administer)).thenReturn(true);
        Manager parentManager = new Manager();
        parentManager.setId(1L);
        when(managerService.get(7L, "济南市")).thenReturn(parentManager);
        
        Manager manager = new Manager();
        when(managerService.getByPhone("18867105827")).thenReturn(manager);
        
        when(managerService.updateByPrimaryKeySelective(Matchers.any(Manager.class))).thenReturn(true);
        
        List<String> enterpriseCode = new ArrayList<String>();
        enterpriseCode.add("01");
        List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setCode("01");
        enterpriseList.add(enterprise);
        Manager childManager = new Manager();
        when(enterprisesService.getEnterpriseListByAdminId(Matchers.any(Administer.class))).thenReturn(enterpriseList);
        when(entManagerService.getManagerForEnter(1L)).thenReturn(new Manager());
        when(entManagerService.getManagerForEnterCode(Matchers.anyString())).thenReturn(childManager);
        
        Assert.assertTrue(sdAdministerService.updateManager(administer, anyListOf(String.class), 2L, 7L));
        
        when(managerService.updateByPrimaryKeySelective(Matchers.any(Manager.class))).thenReturn(false);
        Throwable re = null;
        try {
            sdAdministerService.updateManager(administer, anyListOf(String.class), 2L, 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());
        
        
        when(managerService.getByPhone("18867105827")).thenReturn(null);
        when(managerService.createManager(Matchers.any(Manager.class), Matchers.anyLong())).thenReturn(true);
        when(adminManagerService.insertAdminManager(Matchers.any(AdminManager.class))).thenReturn(false);
        re = null;
        try {
            sdAdministerService.updateManager(administer, anyListOf(String.class), 2L, 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());
        
        when(managerService.getByPhone("18867105827")).thenReturn(null);
        when(managerService.createManager(Matchers.any(Manager.class), Matchers.anyLong())).thenReturn(false);
        re = null;
        try {
            sdAdministerService.updateManager(administer, anyListOf(String.class), 2L, 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());
        
        when(managerService.get(7L, "济南市")).thenReturn(null);
        re = null;
        try {
            sdAdministerService.updateManager(administer, anyListOf(String.class), 2L, 7L);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());
        
        when(administerService.updateSelective(administer)).thenReturn(false);
        Assert.assertFalse(sdAdministerService.updateManager(administer, anyListOf(String.class), 2L, 7L));
    }

    /**
     * initAdminister
     * @return
     */
    private Administer initAdminister() {
        Administer administer = new Administer();
        administer.setId(1L);
        administer.setCitys("济南市");
        administer.setMobilePhone("18867105827");
        return administer;

    }
}

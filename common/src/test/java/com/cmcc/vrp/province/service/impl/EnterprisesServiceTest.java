package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.SchedulerType;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.dao.EntProductMapper;
import com.cmcc.vrp.province.dao.EnterpriseMapper;
import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.GiveMoneyEnter;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.module.EnterpriseBenefitModule;
import com.cmcc.vrp.province.module.EnterpriseStatisticModule;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseExpireJob;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseExpireJobPojo;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseLicenceExpireJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.Job;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 企业管理service单元测试
 *
 * @author kok
 */
@RunWith(MockitoJUnitRunner.class)
public class EnterprisesServiceTest {

    @InjectMocks
    EnterprisesService eService = new EnterprisesServiceImpl();

    @Mock
    AdminEnterService adminEnterService;

    @Mock
    EnterpriseMapper mapper;

    @Mock
    ProductMapper productMapper;

    @Mock
    EntProductMapper entProductMapper;

    @Mock
    ProductService productService;

    @Mock
    AccountService accountService;

    @Mock
    ManagerService managerService;

    @Mock
    AdminManagerService adminManagerService;

    @Mock
    ScheduleService scheduleService;

    @Mock
    AdminRoleService adminRoleService;

    @Mock
    EntManagerService entManagerService;

    @Mock
    AdministerService administerService;

    @Mock
    EnterpriseFileService enterpriseFileService;

    @Mock
    FileStoreService fileStoreService;

    @Mock
    DiscountMapper discountMapper;

    @Mock
    GiveMoneyEnterService gmeService;

    @Mock
    EntProductService entProductService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;

    /**
     *
     */
    @Test
    public void testInsertSelective() {

        assertFalse(eService.insertSelective(null));

        Enterprise record = new Enterprise();
        record.setId(1L);
        assertFalse(eService.insertSelective(record));

        record.setName("asd");
        assertFalse(eService.insertSelective(record));

        record.setEntName("qwe");
        when(mapper.insertSelective((any(Enterprise.class)))).thenReturn(0);
        try {
            eService.insertSelective(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        record.setDeleteFlag(0);
        when(mapper.insertSelective((any(Enterprise.class)))).thenReturn(0);
        try {
            eService.insertSelective(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<Product> products = new ArrayList();
        Product product = new Product();
        products.add(product);
        when(mapper.insertSelective((any(Enterprise.class)))).thenReturn(1);
        when(productMapper.selectDefaultProductByCustomerType(Mockito.anyLong())).thenReturn(products);
        when(entProductMapper.batchInsert(Mockito.anyList())).thenReturn(0);
        try {
            eService.insertSelective(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(entProductMapper.batchInsert(Mockito.anyList())).thenReturn(1);
        try {
            eService.insertSelective(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(productMapper.selectDefaultProductByCustomerType(Mockito.anyLong())).thenReturn(new ArrayList<Product>());

        Product currentProd = new Product();
        List<Product> defaultProducts = new ArrayList();

        when(productService.getCurrencyProduct()).thenReturn(null);

        when(accountService.createEnterAccount(Mockito.anyLong(), Mockito.anyMap(), Mockito.anyString())).thenReturn(
                true);

        assertTrue(eService.insertSelective(record));

        currentProd.setId(1L);
        Product p = new Product();
        p.setId(1L);
        defaultProducts.add(p);
        when(productService.getCurrencyProduct()).thenReturn(currentProd);
        //      when(productMapper.selectDefaultProduct()).thenReturn(null);

        assertTrue(eService.insertSelective(record));

        when(productMapper.selectDefaultProductByCustomerType(Mockito.anyLong())).thenReturn(null);
        p.setId(2L);
        assertTrue(eService.insertSelective(record));

    }

    /**
     *
     */
    @Test
    public void testSelectByPrimaryKey() {
        Long entId = 1L;
        Enterprise enterprise = new Enterprise();

        assertNull(eService.selectByPrimaryKey(null));
        when(mapper.selectByPrimaryKey(entId)).thenReturn(enterprise);
        assertSame(enterprise, eService.selectByPrimaryKey(entId));

        verify(mapper, times(1)).selectByPrimaryKey(entId);
    }

    /**
     *
     */
    @Test
    public void testSelectByPrimaryKeyForActivity() {
        Long entId = 1L;
        Enterprise enterprise = new Enterprise();

        assertNull(eService.selectByPrimaryKeyForActivity(null));
        when(mapper.selectByPrimaryKeyForActivity(entId)).thenReturn(enterprise);
        assertSame(enterprise, eService.selectByPrimaryKeyForActivity(entId));

        verify(mapper, times(1)).selectByPrimaryKeyForActivity(entId);
    }

    /**
     *
     */
    @Test
    public void testSelectByCode() {

        assertNull(eService.selectByCode(null));

        String code = "aaa";
        when(mapper.selectEnterprisesByCode(code)).thenReturn(null);
        assertNull(eService.selectByCode(code));

        List<Enterprise> listEnterList = new ArrayList();
        when(mapper.selectEnterprisesByCode(code)).thenReturn(listEnterList);
        assertNull(eService.selectByCode(code));

        Enterprise enterprise = new Enterprise();
        listEnterList.add(enterprise);
        when(mapper.selectEnterprisesByCode(code)).thenReturn(listEnterList);
        assertSame(enterprise, eService.selectByCode(code));

        verify(mapper, times(3)).selectEnterprisesByCode(code);
    }

    /**
     *
     */
    @Test
    public void testUpdateByPrimaryKeySelective() {

        assertFalse(eService.updateByPrimaryKeySelective(null));

        Enterprise enterprise = new Enterprise();
        assertFalse(eService.updateByPrimaryKeySelective(enterprise));

        enterprise.setId(1L);
        when(mapper.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(0);
        assertFalse(eService.updateByPrimaryKeySelective(enterprise));

        when(mapper.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(1);
        assertTrue(eService.updateByPrimaryKeySelective(enterprise));

        verify(mapper, times(2)).updateByPrimaryKeySelective(enterprise);
    }

    /**
     *
     */
    @Test
    public void testShowEnterprisesAccountsCount() {

        assertSame(0, eService.showEnterprisesAccountsCount(null));

        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("managerId", 1L);
        String managerIds = "1,2,3";
        when(managerService.getChildNodeString(1L)).thenReturn(managerIds);
        when(mapper.showEnterprisesAccountsCount(Mockito.anyMap())).thenReturn(1);

        queryObject.getQueryCriterias().put("endTime", "2016-10-27");
        assertSame(1, eService.showEnterprisesAccountsCount(queryObject));

    }

    @Test
    public void testShowEnterprisesAccountsForPageResult() {

        assertNull(eService.showEnterprisesAccountsForPageResult(null));

        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("managerId", 1L);
        String managerIds = "1,2,3";
        when(managerService.getChildNodeString(1L)).thenReturn(managerIds);
        List<Enterprise> list = new ArrayList();
        when(mapper.showEnterprisesAccountsForPageResult(Mockito.anyMap())).thenReturn(list);
        assertSame(list, eService.showEnterprisesAccountsForPageResult(queryObject));

        queryObject.getQueryCriterias().put("endTime", "2016-10-27");
        assertSame(list, eService.showEnterprisesAccountsForPageResult(queryObject));

        verify(mapper, times(2)).showEnterprisesAccountsForPageResult(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testShowEnterprisesForPageResult() {

        assertNull(eService.showEnterprisesForPageResult(null));

        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("managerId", 1L);
        String managerIds = "1,2,3";
        when(managerService.getChildNodeString(1L)).thenReturn(managerIds);
        List<Enterprise> list = new ArrayList();
        when(mapper.showEnterprisesForPageResult(Mockito.anyMap())).thenReturn(list);
        assertSame(list, eService.showEnterprisesForPageResult(queryObject));

        queryObject.getQueryCriterias().put("endTime", "2016-10-27");
        assertSame(list, eService.showEnterprisesForPageResult(queryObject));

        verify(mapper, times(2)).showEnterprisesForPageResult(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testSelectEnterpriseByMap() {

        Map map = new HashMap();
        map.put("managerId", "");
        map.put("endTime", "");
        List<Enterprise> list = new ArrayList();
        when(mapper.selectEnterpriseByMap(Mockito.anyMap())).thenReturn(list);
        assertSame(list, eService.selectEnterpriseByMap(map));

        map.put("managerId", "1");
        when(managerService.getChildNodeString(1L)).thenReturn("1,2,3");
        map.put("endTime", "2016-10-27");
        when(mapper.selectEnterpriseByMap(Mockito.anyMap())).thenReturn(list);
        assertSame(list, eService.selectEnterpriseByMap(map));

        verify(mapper, times(2)).selectEnterpriseByMap(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testGetEnterpriseList() {

        List<Enterprise> list = new ArrayList();
        when(mapper.queryAllEnterpriseList()).thenReturn(list);
        assertSame(list, eService.getEnterpriseList());
        verify(mapper, times(1)).queryAllEnterpriseList();
    }

    /**
     *
     */
    @Test
    public void testShowForPageResultCount() {

        assertSame(0, eService.showForPageResultCount(null));

        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("managerId", 1L);
        String managerIds = "1,2,3";
        when(managerService.getChildNodeString(1L)).thenReturn(managerIds);
        when(mapper.showEnterprisesForPageResultCount(Mockito.anyMap())).thenReturn(1);
        assertSame(1, eService.showForPageResultCount(queryObject));

        queryObject.getQueryCriterias().put("endTime", "2016-10-27");
        assertSame(1, eService.showForPageResultCount(queryObject));

        verify(mapper, times(2)).showEnterprisesForPageResultCount(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testGetEnterpriseListByAdminId() {

        assertNull(eService.getEnterpriseListByAdminId(null));

        Administer admin = new Administer();
        assertNull(eService.getEnterpriseListByAdminId(admin));

        admin.setId(1L);
        when(adminManagerService.selectManagerIdByAdminId(1L)).thenReturn(null);
        assertNull(eService.getEnterpriseListByAdminId(admin));

        when(adminManagerService.selectManagerIdByAdminId(1L)).thenReturn(1L);
        when(managerService.getChildNodeString(1L)).thenReturn("1,2,3");
        List<Enterprise> list = new ArrayList();
        when(mapper.getEnterByManagerId("1,2,3")).thenReturn(list);
        assertSame(list, eService.getEnterpriseListByAdminId(admin));

        verify(mapper, times(1)).getEnterByManagerId(Mockito.anyString());
    }

    /**
     *
     */
    @Test
    public void testQueryEnterpriseByCode() {

        String code = "aaa";
        Enterprise enterprise = new Enterprise();
        when(mapper.queryEnterpriseByCode(code)).thenReturn(enterprise);
        assertSame(enterprise, eService.queryEnterpriseByCode(code));

        verify(mapper, times(1)).queryEnterpriseByCode(code);
    }

    /**
     *
     */
    @Test
    public void testCountExists() {

        Long exceptId = 1L;
        String name = "a";
        String code = "b";
        String phone = "18867100000";
        when(mapper.countExists(exceptId, name, code, phone)).thenReturn(0);
        assertFalse(eService.countExists(exceptId, name, code, phone));

        when(mapper.countExists(exceptId, name, code, phone)).thenReturn(1);
        assertTrue(eService.countExists(exceptId, name, code, phone));

        verify(mapper, times(2)).countExists(exceptId, name, code, phone);
    }

    /**
     *
     */
    @Test
    public void testStatistictEnterpriseByCreateDay() {

        QueryObject queryObject = new QueryObject();
        Long managerId = 1L;
        List<Long> managerIds = new ArrayList();
        List<EnterpriseStatisticModule> list = new ArrayList();
        when(managerService.getSonTreeIdByManageId(managerId)).thenReturn(managerIds);
        when(mapper.statistictEnterpriseByCreateDay(Mockito.anyMap())).thenReturn(list);
        assertSame(list, eService.statistictEnterpriseByCreateDay(managerId, queryObject));

        queryObject.getQueryCriterias().put("endTime", "2016-10-27");
        assertSame(list, eService.statistictEnterpriseByCreateDay(managerId, queryObject));

        verify(mapper, times(2)).statistictEnterpriseByCreateDay(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testStatistictBenefitForEnterprise() {

        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("managerId", "1");
        List<Long> managerIds = new ArrayList();
        List<EnterpriseBenefitModule> list = new ArrayList();
        when(managerService.getSonTreeIdByManageId(1L)).thenReturn(managerIds);
        when(mapper.statistictBenefitForEnterprise(Mockito.anyMap())).thenReturn(list);
        assertSame(list, eService.statistictBenefitForEnterprise(queryObject));

        queryObject.getQueryCriterias().put("endTime", "2016-10-27");
        assertSame(list, eService.statistictBenefitForEnterprise(queryObject));

        verify(mapper, times(2)).statistictBenefitForEnterprise(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testSelectByAppKey() {

        assertNull(eService.selectByAppKey(null));

        String appKey = "ABCD";
        Enterprise enterprise = new Enterprise();
        when(mapper.selectByAppKey(appKey)).thenReturn(enterprise);
        assertSame(enterprise, eService.selectByAppKey(appKey));

        verify(mapper, times(1)).selectByAppKey(appKey);
    }

    /**
     *
     */
    @Test
    public void testCreateEnterpriseExpireSchedule() {

        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setEndTime(new Date());
        EnterpriseExpireJobPojo pojo = new EnterpriseExpireJobPojo(enterprise.getId());
        String jsonStr = JSON.toJSONString(pojo);
        when(
                scheduleService.createScheduleJob(EnterpriseExpireJob.class, SchedulerType.ENTERPRISE_EXPIRE.getCode(),
                        jsonStr, enterprise.getId().toString(), enterprise.getEndTime())).thenReturn("success");
        assertTrue(eService.createEnterpriseExpireSchedule(enterprise));

        when(
                scheduleService.createScheduleJob(EnterpriseExpireJob.class, SchedulerType.ENTERPRISE_EXPIRE.getCode(),
                        jsonStr, enterprise.getId().toString(), enterprise.getEndTime())).thenReturn("fail");
        assertFalse(eService.createEnterpriseExpireSchedule(enterprise));

    }

    /**
     *
     */
    @Test
    public void testChangeEnterManager() {

        assertFalse(eService.changeEnterManager(null, null, null));

        Long enterId = 1L;
        Enterprise enterprise = new Enterprise();
        Administer newAdmin = new Administer();
        Long currentUserId = 1L;
        when(mapper.selectByPrimaryKey(enterId)).thenReturn(enterprise);
        when(adminManagerService.getAdminIdForEnter(enterId)).thenReturn(null).thenReturn(new ArrayList<Long>());
        try {
            eService.changeEnterManager(null, enterId, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            eService.changeEnterManager(null, enterId, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<Long> enterpriseManagerIds = new ArrayList();
        enterpriseManagerIds.add(1L);
        Administer administerDB = new Administer();
        when(adminManagerService.getAdminIdForEnter(enterId)).thenReturn(enterpriseManagerIds);
        when(adminRoleService.deleteByAdminId(1L)).thenReturn(false).thenReturn(true);
        when(adminManagerService.deleteAdminByEntId(enterId)).thenReturn(false).thenReturn(true);
        when(adminManagerService.insertAdminManager(any(AdminManager.class))).thenReturn(false)
                .thenReturn(true);
        when(adminRoleService.insertAdminRole(any(AdminRole.class))).thenReturn(false).thenReturn(true);
        when(entManagerService.getManagerIdForEnter(enterId)).thenReturn(1L);
        when(administerService.selectByMobilePhone("18867100000")).thenReturn(administerDB);
        when(
                administerService.createAdminister(Mockito.anyLong(), any(Administer.class),
                        any(Administer.class), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        try {
            eService.changeEnterManager(newAdmin, enterId, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            eService.changeEnterManager(newAdmin, enterId, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            eService.changeEnterManager(newAdmin, enterId, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            eService.changeEnterManager(newAdmin, enterId, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            eService.changeEnterManager(newAdmin, enterId, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            eService.changeEnterManager(newAdmin, enterId, currentUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        verify(adminRoleService, times(6)).deleteByAdminId(1L);
        verify(adminManagerService, times(5)).deleteAdminByEntId(enterId);
        verify(adminManagerService, times(4)).insertAdminManager(any(AdminManager.class));
        verify(adminRoleService, times(3)).insertAdminRole(any(AdminRole.class));
        verify(administerService, times(2)).createAdminister(Mockito.anyLong(),
                any(Administer.class), any(Administer.class), Mockito.anyLong());
    }

    /**
     *
     */
    @Test
    public void testSaveEditQualification() throws IOException {

        Long currentUserId = 1L;
        Enterprise enterprise = new Enterprise();
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        enterprise.setLicenceEndTime(new Date());
        enterprise.setId(1L);
        EnterpriseFile ef = new EnterpriseFile();
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(0).thenReturn(1);
        //更新企业合作信息或者创建定时任务失败
        try {
            eService.saveEditQualification(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(eService.saveEditQualification(currentUserId, enterprise, request));
        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile multipartFile = new MockMultipartFile("aaa", new BufferedInputStream(new FileInputStream(
                tempFile)));
        MockMultipartFile multipartFile0 = new MockMultipartFile("bbb", "bbb", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile1 = new MockMultipartFile("licenceImage", "licenceImage", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile2 = new MockMultipartFile("authorization", "authorization", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile3 = new MockMultipartFile("identification", "identification", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile4 = new MockMultipartFile("identificationBack", "identificationBack", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile5 = new MockMultipartFile("customerFile", "customerFile", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile6 = new MockMultipartFile("contract", "contract", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile7 = new MockMultipartFile("image", "image", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        request.addFile(multipartFile);
        request.addFile(multipartFile0);
        request.addFile(multipartFile1);
        request.addFile(multipartFile2);
        request.addFile(multipartFile3);
        request.addFile(multipartFile4);
        request.addFile(multipartFile5);
        request.addFile(multipartFile6);
        request.addFile(multipartFile7);
        EnterpriseFile efOrigin = new EnterpriseFile();
        efOrigin.setLicenceKey("aa");
        efOrigin.setAuthorizationKey("bb");
        efOrigin.setIdentificationKey("cc");
        efOrigin.setIdentificationBackKey("dd");
        efOrigin.setCustomerfileKey("ee");
        efOrigin.setContractKey("ff");
        efOrigin.setImageKey("gg");
        when(enterpriseFileService.selectByEntId(enterprise.getId())).thenReturn(efOrigin);
        doReturn(true).when(fileStoreService).save(Mockito.anyString(), any(File.class));
        when(enterpriseFileService.update(any(EnterpriseFile.class))).thenReturn(false).thenReturn(true);
        try {
            eService.saveEditQualification(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveEditQualification(currentUserId, enterprise, request));
    }

    /**
     *
     */
    @Test
    public void testSaveQualification() throws IOException {

        Long currentUserId = 1L;
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setLicenceEndTime(new Date());
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();

        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile multipartFile = new MockMultipartFile("aaa", new BufferedInputStream(new FileInputStream(
                tempFile)));
        MockMultipartFile multipartFile0 = new MockMultipartFile("bbb", "bbb", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile1 = new MockMultipartFile("licenceImage", "licenceImage", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile2 = new MockMultipartFile("authorization", "authorization", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile3 = new MockMultipartFile("identification", "identification", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile4 = new MockMultipartFile("identificationBack", "identificationBack", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile5 = new MockMultipartFile("customerFile", "customerFile", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile6 = new MockMultipartFile("contract", "contract", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile7 = new MockMultipartFile("image", "image", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        request.addFile(multipartFile);
        request.addFile(multipartFile0);
        request.addFile(multipartFile1);
        request.addFile(multipartFile2);
        request.addFile(multipartFile3);
        request.addFile(multipartFile4);
        request.addFile(multipartFile5);
        request.addFile(multipartFile6);
        request.addFile(multipartFile7);
        EnterpriseFile efOrigin = new EnterpriseFile();
        efOrigin.setLicenceKey("aa");
        efOrigin.setAuthorizationKey("bb");
        efOrigin.setIdentificationKey("cc");
        efOrigin.setIdentificationBackKey("dd");
        efOrigin.setCustomerfileKey("ee");
        efOrigin.setContractKey("ff");
        efOrigin.setImageKey("gg");
        when(enterpriseFileService.selectByEntId(Mockito.anyLong())).thenReturn(efOrigin);
        doReturn(true).when(fileStoreService).save(Mockito.anyString(), any(File.class));

        //      when(enterpriseFileService.selectByEntId(enterprise.getId())).thenReturn(null);
        //      when(mapper.updateByPrimaryKeySelective(Mockito.any(Enterprise.class))).thenReturn(0).thenReturn(1);
        when(enterpriseFileService.update(any(EnterpriseFile.class))).thenReturn(false).thenReturn(true);
        //1更新数据库失败
        try {
            eService.saveQualification(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(0).thenReturn(1);
        try {
            eService.saveQualification(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveQualification(currentUserId, enterprise, request));

        when(enterpriseFileService.selectByEntId(enterprise.getId())).thenReturn(null);
        when(enterpriseFileService.insert(any(EnterpriseFile.class))).thenReturn(0).thenReturn(1);
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(0).thenReturn(1);
        //2更新数据库失败
        try {
            eService.saveQualification(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            eService.saveQualification(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveQualification(currentUserId, enterprise, request));
    }

    /**
     *
     */
    @Test
    public void testCreateEnterpriseLicenceExpireSchedule() {
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setLicenceEndTime(new Date());
        EnterpriseExpireJobPojo pojo = new EnterpriseExpireJobPojo(enterprise.getId());
        String jsonStr = JSON.toJSONString(pojo);

        when(
                scheduleService.createScheduleJob(EnterpriseLicenceExpireJob.class,
                        SchedulerType.ENTERPRISE_LICENCE_EXPIRE.getCode(), jsonStr, enterprise.getId().toString(),
                        enterprise.getLicenceEndTime())).thenReturn("success").thenReturn("fail");

        assertTrue(eService.createEnterpriseLicenceExpireSchedule(enterprise));
        assertFalse(eService.createEnterpriseLicenceExpireSchedule(enterprise));

        verify(scheduleService, times(2)).createScheduleJob(EnterpriseLicenceExpireJob.class,
                SchedulerType.ENTERPRISE_LICENCE_EXPIRE.getCode(), jsonStr, enterprise.getId().toString(),
                enterprise.getLicenceEndTime());

    }

    /**
     *
     */
    @Test
    public void testSaveEditEntInfo() throws IOException {

        Long currentUserId = 1L;
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        assertFalse(eService.saveEditEntInfo(null, currentUserId, null));

        Enterprise enterprise = new Enterprise();
        assertFalse(eService.saveEditEntInfo(enterprise, currentUserId, null));

        enterprise.setId(1L);
        enterprise.setLicenceEndTime(new Date());
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(0).thenReturn(1);
        //更新企业合作信息或者创建定时任务失败
        try {
            eService.saveEditEntInfo(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        enterprise.setEndTime(new Date());
        request.addParameter("customerManagerEmail", "aa@163.com");
        Discount discount = new Discount();
        discount.setDiscount(1.0);
        when(discountMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null).thenReturn(discount);
        assertTrue(eService.saveEditEntInfo(enterprise, currentUserId, request));

        enterprise.setGiveMoneyId(1L);
        when(gmeService.selectByEnterId(enterprise.getId())).thenReturn(new GiveMoneyEnter());
        when(gmeService.updateByEnterId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        //存送比更新失败
        try {
            eService.saveEditEntInfo(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        request.addParameter("minCount", "10.0");
        when(accountService.setMinCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(false).thenReturn(true);
        //信用额度保存失败
        try {
            eService.saveEditEntInfo(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(entProductService.updateDiscountByEnterId(Mockito.anyLong(), Mockito.anyInt())).thenReturn(false)
                .thenReturn(true);
        //企业产品折扣信息更新失败
        try {
            eService.saveEditEntInfo(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            eService.saveEditEntInfo(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(gmeService.selectByEnterId(enterprise.getId())).thenReturn(null);
        when(gmeService.insert(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        //存送比插入失败
        try {
            eService.saveEditEntInfo(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(1).thenReturn(0);
        //更新企业信息失败！
        try {
            eService.saveEditEntInfo(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(1);
        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile multipartFile = new MockMultipartFile("aaa", new BufferedInputStream(new FileInputStream(
                tempFile)));
        MockMultipartFile multipartFile0 = new MockMultipartFile("bbb", "bbb", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile1 = new MockMultipartFile("licenceImage", "licenceImage", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile2 = new MockMultipartFile("authorization", "authorization", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile3 = new MockMultipartFile("identification", "identification", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile4 = new MockMultipartFile("identificationBack", "identificationBack", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile5 = new MockMultipartFile("customerFile", "customerFile", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile6 = new MockMultipartFile("contract", "contract", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile7 = new MockMultipartFile("image", "image", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        request.addFile(multipartFile);
        request.addFile(multipartFile0);
        request.addFile(multipartFile1);
        request.addFile(multipartFile2);
        request.addFile(multipartFile3);
        request.addFile(multipartFile4);
        request.addFile(multipartFile5);
        request.addFile(multipartFile6);
        request.addFile(multipartFile7);
        EnterpriseFile efOrigin = new EnterpriseFile();
        efOrigin.setLicenceKey("aa");
        efOrigin.setAuthorizationKey("bb");
        efOrigin.setIdentificationKey("cc");
        efOrigin.setIdentificationBackKey("dd");
        efOrigin.setCustomerfileKey("ee");
        efOrigin.setContractKey("ff");
        efOrigin.setImageKey("gg");
        when(enterpriseFileService.selectByEntId(Mockito.anyLong())).thenReturn(efOrigin);
        doReturn(true).when(fileStoreService).save(Mockito.anyString(), any(File.class));
        when(enterpriseFileService.update(any(EnterpriseFile.class))).thenReturn(false).thenReturn(true);
        try {
            eService.saveEditEntInfo(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveEditEntInfo(enterprise, currentUserId, request));

    }

    /**
     *
     */
    @Test
    public void testSaveEditCooperation() throws IOException {

        Long currentUserId = 1L;
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        assertFalse(eService.saveEditCooperation(null, currentUserId, request));

        Enterprise enterprise = new Enterprise();
        assertFalse(eService.saveEditCooperation(enterprise, currentUserId, request));

        enterprise.setId(1L);
        Discount discount = new Discount();
        discount.setDiscount(1.0);
        when(discountMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null).thenReturn(discount);
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(0).thenReturn(1);
        //更新企业信息失败
        try {
            eService.saveEditCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        enterprise.setGiveMoneyId(1L);
        when(gmeService.selectByEnterId(enterprise.getId())).thenReturn(new GiveMoneyEnter());
        when(gmeService.updateByEnterId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        //存送比更新失败
        try {
            eService.saveEditCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(gmeService.selectByEnterId(enterprise.getId())).thenReturn(null);
        when(gmeService.insert(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        //存送比插入失败
        try {
            eService.saveEditCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        request.addParameter("minCount", "10.0");
        when(accountService.setMinCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(false).thenReturn(true);
        //信用额度保存失败
        try {
            eService.saveEditCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(entProductService.updateDiscountByEnterId(Mockito.anyLong(), Mockito.anyInt())).thenReturn(false)
                .thenReturn(true);
        //企业产品折扣信息更新失败
        try {
            eService.saveEditCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile multipartFile = new MockMultipartFile("aaa", new BufferedInputStream(new FileInputStream(
                tempFile)));
        MockMultipartFile multipartFile0 = new MockMultipartFile("bbb", "bbb", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile1 = new MockMultipartFile("customerFile", "customerFile", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile2 = new MockMultipartFile("contract", "contract", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile3 = new MockMultipartFile("image", "image", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        request.addFile(multipartFile);
        request.addFile(multipartFile0);
        request.addFile(multipartFile1);
        request.addFile(multipartFile2);
        request.addFile(multipartFile3);
        EnterpriseFile efOrigin = new EnterpriseFile();
        efOrigin.setCustomerfileKey("ee");
        efOrigin.setContractKey("ff");
        efOrigin.setImageKey("gg");
        when(enterpriseFileService.selectByEntId(Mockito.anyLong())).thenReturn(efOrigin);
        doReturn(true).when(fileStoreService).save(Mockito.anyString(), any(File.class));
        when(enterpriseFileService.update(any(EnterpriseFile.class))).thenReturn(false).thenReturn(true);
        //企业文件表更新失败或企业更新失败
        try {
            eService.saveEditCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveEditCooperation(enterprise, currentUserId, request));

    }

    /**
     *
     */
    @Test
    public void testSaveCooperation() throws IOException {

        Enterprise enterprise = new Enterprise();
        Long currentUserId = 1L;
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        enterprise.setId(1L);
        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile multipartFile = new MockMultipartFile("aaa", new BufferedInputStream(new FileInputStream(
                tempFile)));
        MockMultipartFile multipartFile0 = new MockMultipartFile("bbb", "bbb", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile1 = new MockMultipartFile("customerFile", "customerFile", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile2 = new MockMultipartFile("contract", "contract", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile3 = new MockMultipartFile("image", "image", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        request.addFile(multipartFile);
        request.addFile(multipartFile0);
        request.addFile(multipartFile1);
        request.addFile(multipartFile2);
        request.addFile(multipartFile3);
        EnterpriseFile efOrigin = new EnterpriseFile();
        efOrigin.setCustomerfileKey("ee");
        efOrigin.setContractKey("ff");
        efOrigin.setImageKey("gg");
        when(enterpriseFileService.selectByEntId(Mockito.anyLong())).thenReturn(efOrigin);
        doReturn(true).when(fileStoreService).save(Mockito.anyString(), any(File.class));
        Discount discount = new Discount();
        discount.setDiscount(1.0);
        when(discountMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null).thenReturn(discount);
        when(enterpriseFileService.update(any(EnterpriseFile.class))).thenReturn(false).thenReturn(true);

        //企业文件表更新失败或企业更新失败
        try {
            eService.saveCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        enterprise.setGiveMoneyId(1L);
        when(gmeService.selectByEnterId(enterprise.getId())).thenReturn(new GiveMoneyEnter()).thenReturn(null);
        when(gmeService.updateByEnterId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        //存送比更新失败
        try {
            eService.saveCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //存送比插入失败
        when(gmeService.insert(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        try {
            eService.saveCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        request.addParameter("minCount", "10.0");
        when(accountService.setMinCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(false).thenReturn(true);
        //信用额度保存失败
        try {
            eService.saveCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(entProductService.updateDiscountByEnterId(Mockito.anyLong(), Mockito.anyInt())).thenReturn(false)
                .thenReturn(true);
        //企业产品折扣信息更新失败
        try {
            eService.saveCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(0).thenReturn(1);
        //企业文件表更新失败或企业更新失败
        try {
            eService.saveCooperation(enterprise, currentUserId, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveCooperation(enterprise, currentUserId, request));
    }

    /**
     *
     */
    @Test
    public void testQueryPotentialEnterList() {
        QueryObject queryObject = new QueryObject();
        Long currentManagerId = 1L;
        String managerIds = "1,2,3";
        List<Enterprise> list = new ArrayList();

        when(managerService.getChildNodeString(currentManagerId)).thenReturn(managerIds);
        when(mapper.queryPotentialEnt(Mockito.anyMap())).thenReturn(list);

        assertNull(null, eService.queryPotentialEnterList(null, currentManagerId));
        assertNull(null, eService.queryPotentialEnterList(queryObject, null));

        //参数中没有delete_flag
        assertSame(list, eService.queryPotentialEnterList(queryObject, currentManagerId));

        //参数中有delete_flag
        queryObject.getQueryCriterias().put("deleteFlag", "1");
        assertSame(list, eService.queryPotentialEnterList(queryObject, currentManagerId));

        verify(mapper, times(2)).queryPotentialEnt(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testGetNormalEnterpriseListByAdminId() {
        Administer admin = new Administer();
        admin.setId(1L);
        Long managerId = 1L;
        String managerIds = "1,2,3";
        List<Enterprise> list = new ArrayList();

        when(adminManagerService.selectManagerIdByAdminId(1L)).thenReturn(null).thenReturn(managerId);
        when(managerService.getChildNodeString(managerId)).thenReturn(managerIds);
        when(mapper.getNormalEnterByManagerId(managerIds)).thenReturn(list);

        assertNull(eService.getNormalEnterpriseListByAdminId(null));
        assertNull(eService.getNormalEnterpriseListByAdminId(new Administer()));
        assertNull(eService.getNormalEnterpriseListByAdminId(admin));
        assertSame(list, eService.getNormalEnterpriseListByAdminId(admin));

        verify(mapper, times(1)).getNormalEnterByManagerId(managerIds);
    }

    /**
     *
     */
    @Test
    public void testGetAllEnterpriseListByAdminId() {

        Administer admin = new Administer();
        admin.setId(1L);
        Long managerId = 1L;
        String managerIds = "1,2,3";
        List<Enterprise> list = new ArrayList();
        when(adminManagerService.selectManagerIdByAdminId(admin.getId())).thenReturn(null).thenReturn(managerId);
        when(managerService.getChildNodeString(managerId)).thenReturn(managerIds);
        when(mapper.getAllEnterByManagerId(managerIds)).thenReturn(list);

        assertNull(eService.getAllEnterpriseListByAdminId(null));
        assertNull(eService.getAllEnterpriseListByAdminId(new Administer()));
        assertNull(eService.getAllEnterpriseListByAdminId(admin));
        assertSame(list, eService.getAllEnterpriseListByAdminId(admin));

        verify(mapper, times(1)).getAllEnterByManagerId(managerIds);
    }

    /**
     *
     */
    @Test
    public void testGetNormalEnterpriseList() {
        List<Enterprise> list = new ArrayList();
        when(mapper.getNormalEnter()).thenReturn(list);
        assertSame(list, eService.getNormalEnterpriseList());
        verify(mapper, Mockito.timeout(1)).getNormalEnter();
    }

    /**
     *
     */
    @Test
    public void testGetPotentialEnterByManagerId() {
        Long managerId = 1L;
        String managerIds = "1,2,3";
        List<Enterprise> list = new ArrayList();
        when(managerService.getChildNodeString(managerId)).thenReturn(managerIds);

        when(mapper.getPotentialEnterByManagerId(managerIds)).thenReturn(list);
        assertNull(eService.getPotentialEnterByManagerId(null));
        assertNull(eService.getPotentialEnterByManagerId(managerId));
        Enterprise e = new Enterprise();
        list.add(e);
        when(mapper.getPotentialEnterByManagerId(managerIds)).thenReturn(list);
        assertSame(list, eService.getPotentialEnterByManagerId(managerId));

        verify(mapper, times(2)).getPotentialEnterByManagerId(Mockito.anyString());
    }

    /**
     *
     */
    @Test
    public void testGetEnterpriseIdByAdminId() {

        Administer admin = new Administer();
        admin.setId(1L);
        Long managerId = 1L;
        List<Long> ids = new ArrayList();
        ids.add(1L);
        ids.add(2L);
        Enterprise e1 = new Enterprise();
        Enterprise e2 = new Enterprise();
        e1.setId(1L);
        e2.setId(2L);
        List<Enterprise> list = new ArrayList();
        list.add(e1);
        list.add(e2);
        String managerIds = "1,2";

        when(adminManagerService.selectManagerIdByAdminId(admin.getId())).thenReturn(null).thenReturn(managerId);
        when(managerService.getChildNodeString(managerId)).thenReturn(managerIds);
        when(mapper.getEnterByManagerId(managerIds)).thenReturn(list);

        assertNull(eService.getEnterpriseIdByAdminId(null));
        assertNull(eService.getEnterpriseIdByAdminId(new Administer()));
        assertNull(eService.getEnterpriseIdByAdminId(admin));
        assertNotNull(eService.getEnterpriseIdByAdminId(admin));
        verify(mapper, times(1)).getEnterByManagerId(managerIds);
    }

    /**
     *
     */
    @Test
    public void testStatistictEnterpriseByManagerTree() {
        QueryObject queryObject = new QueryObject();
        List<EnterpriseStatisticModule> list = new ArrayList();
        when(mapper.selectEnterpriseByManagerTree(Mockito.anyMap())).thenReturn(list);
        assertSame(list, eService.statistictEnterpriseByManagerTree(queryObject));
        verify(mapper, times(1)).selectEnterpriseByManagerTree(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testStatistictEnterpriseCountByManagerTree() {
        QueryObject queryObject = new QueryObject();
        int count = 1;
        when(mapper.selectEnterpriseCountByManagerTree(Mockito.anyMap())).thenReturn(count);
        assertSame(count, eService.statistictEnterpriseCountByManagerTree(queryObject));
        verify(mapper, Mockito.timeout(1)).selectEnterpriseCountByManagerTree(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testSaveQualificationCooperation() throws IOException {
        Long currentUserId = 1L;
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setLicenceEndTime(new Date());
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        request.setParameter("customerManagerEmail", "aa@qq.com");
        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile multipartFile = new MockMultipartFile("aaa", new BufferedInputStream(new FileInputStream(
                tempFile)));
        MockMultipartFile multipartFile0 = new MockMultipartFile("bbb", "bbb", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile1 = new MockMultipartFile("licenceImage", "licenceImage", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile2 = new MockMultipartFile("authorization", "authorization", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile3 = new MockMultipartFile("identification", "identification", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile4 = new MockMultipartFile("identificationBack", "identificationBack", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile5 = new MockMultipartFile("customerFile", "customerFile", "",
                new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile6 = new MockMultipartFile("contract", "contract", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        MockMultipartFile multipartFile7 = new MockMultipartFile("image", "image", "", new BufferedInputStream(
                new FileInputStream(tempFile)));
        request.addFile(multipartFile);
        request.addFile(multipartFile0);
        request.addFile(multipartFile1);
        request.addFile(multipartFile2);
        request.addFile(multipartFile3);
        request.addFile(multipartFile4);
        request.addFile(multipartFile5);
        request.addFile(multipartFile6);
        request.addFile(multipartFile7);
        EnterpriseFile efOrigin = new EnterpriseFile();
        efOrigin.setLicenceKey("aa");
        efOrigin.setAuthorizationKey("bb");
        efOrigin.setIdentificationKey("cc");
        efOrigin.setIdentificationBackKey("dd");
        efOrigin.setCustomerfileKey("ee");
        efOrigin.setContractKey("ff");
        efOrigin.setImageKey("gg");
        doReturn(true).when(fileStoreService).save(Mockito.anyString(), any(File.class));
        when(enterpriseFileService.selectByEntId(Mockito.anyLong())).thenReturn(null);
        when(enterpriseFileService.insert(any(EnterpriseFile.class))).thenReturn(0).thenReturn(1);
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(0).thenReturn(1);
        //2更新数据库失败(insert失败)
        try {
            eService.saveQualificationCooperation(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //2更新数据库失败
        try {
            eService.saveQualificationCooperation(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveQualificationCooperation(currentUserId, enterprise, request));

        when(enterpriseFileService.selectByEntId(Mockito.anyLong())).thenReturn(efOrigin);
        when(enterpriseFileService.update(any(EnterpriseFile.class))).thenReturn(false).thenReturn(true);
        when(mapper.updateByPrimaryKeySelective(enterprise)).thenReturn(0).thenReturn(1);
        //1更新数据库失败(update失败)
        try {
            eService.saveQualificationCooperation(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //1更新数据库失败
        try {
            eService.saveQualificationCooperation(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveQualificationCooperation(currentUserId, enterprise, request));

        enterprise.setEndTime(new Date());
        enterprise.setGiveMoneyId(1L);
        when(gmeService.selectByEnterId(enterprise.getId())).thenReturn(null);
        when(gmeService.insert(enterprise.getGiveMoneyId(), enterprise.getId())).thenReturn(false).thenReturn(true);
        //存送比插入失败
        try {
            eService.saveQualificationCooperation(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveQualificationCooperation(currentUserId, enterprise, request));

        when(gmeService.selectByEnterId(enterprise.getId())).thenReturn(new GiveMoneyEnter());
        when(gmeService.updateByEnterId(enterprise.getGiveMoneyId(), enterprise.getId())).thenReturn(false).thenReturn(
                true);
        //存送比更新失败
        try {
            eService.saveQualificationCooperation(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveQualificationCooperation(currentUserId, enterprise, request));

        request.setParameter("minCount", "10.0");
        when(accountService.setMinCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(false).thenReturn(true);
        //信用额度保存失败
        try {
            eService.saveQualificationCooperation(currentUserId, enterprise, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(eService.saveQualificationCooperation(currentUserId, enterprise, request));

    }

    /**
     *
     */
    @Test
    public void testHasAuthToFillInQuafilication() {
        Long roleId = 1L;

        when(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey())).thenReturn("1").thenReturn(
                "2");
        when(globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey())).thenReturn("1")
                .thenReturn("2");
        assertFalse(eService.hasAuthToFillInQuafilication(null));
        assertTrue(eService.hasAuthToFillInQuafilication(roleId));
        assertTrue(eService.hasAuthToFillInQuafilication(roleId));
        assertFalse(eService.hasAuthToFillInQuafilication(roleId));

    }

    /**
     *
     */
    @Test
    public void testHasAuthToFillInCooperation() {
        Long roleId = 1L;

        when(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey())).thenReturn("1").thenReturn(
                "2");
        assertFalse(eService.hasAuthToFillInCooperation(null));
        assertTrue(eService.hasAuthToFillInCooperation(roleId));
        assertFalse(eService.hasAuthToFillInCooperation(roleId));

    }

    /**
     *
     */
    @Test
    public void testHasAuthToFillInForProvince() {
        Long roleId = 1L;

        when(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey())).thenReturn("1").thenReturn(
                "2");
        assertFalse(eService.hasAuthToFillInForProvince(null));
        assertTrue(eService.hasAuthToFillInForProvince(roleId));
        assertFalse(eService.hasAuthToFillInForProvince(roleId));

    }

    /**
     *
     */
    @Test
    public void testHasAuthToEdit() {
        Long roleId = 1L;

        when(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey())).thenReturn("1").thenReturn(
                "2");
        when(globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey())).thenReturn("1")
                .thenReturn("2");
        assertFalse(eService.hasAuthToEdit(null));
        assertTrue(eService.hasAuthToEdit(roleId));
        assertTrue(eService.hasAuthToEdit(roleId));
        assertFalse(eService.hasAuthToEdit(roleId));

    }

    /**
     *
     */
    @Test
    public void testCountPotentialEnterList() {
        QueryObject queryObject = new QueryObject();
        Long currentManagerId = 1L;
        String managerIds = "1,2,3";
        Long result = 1L;

        when(managerService.getChildNodeString(currentManagerId)).thenReturn(managerIds);
        when(mapper.countPotentialEnt(Mockito.anyMap())).thenReturn(result);
        assertNull(eService.countPotentialEnterList(null, null));
        assertNull(eService.countPotentialEnterList(queryObject, null));
        assertSame(result, eService.countPotentialEnterList(queryObject, currentManagerId));
        queryObject.getQueryCriterias().put("deleteFlag", 1);
        assertSame(result, eService.countPotentialEnterList(queryObject, currentManagerId));

        verify(mapper, times(2)).countPotentialEnt(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testJudgeEnterprise() {
        Long entId = 1L;
        Enterprise enter = new Enterprise();
        enter.setDeleteFlag(1);

        when(mapper.selectByPrimaryKey(entId)).thenReturn(null).thenReturn(enter);

        assertSame("查找不到相关企业", eService.judgeEnterprise(null));
        assertSame("企业处于非正常状态！", eService.judgeEnterprise(entId));
        assertSame("企业处于非正常状态！", eService.judgeEnterprise(entId));

        enter.setDeleteFlag(0);
        assertSame("success", eService.judgeEnterprise(entId));

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        //当前时间
        Date date = new Date();
        cal.setTime(date);
        //后一天
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date dateAfter = cal.getTime();
        cal2.add(Calendar.DAY_OF_MONTH, -1);
        Date dateBefore = cal2.getTime();
        enter.setStartTime(dateAfter);
        assertSame("企业处于非正常状态！", eService.judgeEnterprise(entId));

        enter.setStartTime(dateBefore);
        enter.setEndTime(dateBefore);
        assertSame("企业处于非正常状态！", eService.judgeEnterprise(entId));

        enter.setEndTime(dateAfter);
        enter.setLicenceStartTime(dateAfter);
        assertSame("企业处于非正常状态！", eService.judgeEnterprise(entId));

        enter.setLicenceStartTime(dateBefore);
        enter.setLicenceEndTime(dateBefore);
        assertSame("企业处于非正常状态！", eService.judgeEnterprise(entId));

        enter.setLicenceEndTime(dateAfter);
        assertSame("success", eService.judgeEnterprise(entId));
    }

    /**
     *
     */
    @Test
    public void testUndoEnterpriseExireSchedule() {

        Enterprise enter = new Enterprise();
        enter.setId(1L);
        enter.setEndTime(new Date());
        when(
                scheduleService.undoScheduleJob(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        any(Date.class))).thenReturn("success").thenReturn("fail");

        assertTrue(eService.undoEnterpriseExireSchedule(enter));
        assertFalse(eService.undoEnterpriseExireSchedule(enter));

        verify(scheduleService, times(2)).undoScheduleJob(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), any(Date.class));

    }

    /**
     *
     */
    @Test
    public void testUndoEnterpriseLicenceExpireSchedule() {

        Enterprise enter = new Enterprise();
        enter.setId(1L);
        enter.setEndTime(new Date());
        when(
                scheduleService.undoScheduleJob(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        any(Date.class))).thenReturn("success").thenReturn("fail");

        assertTrue(eService.undoEnterpriseLicenceExpireSchedule(enter));
        assertFalse(eService.undoEnterpriseLicenceExpireSchedule(enter));

        verify(scheduleService, times(2)).undoScheduleJob(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), any(Date.class));

    }

    /**
     *
     */
    @Test
    public void testSelectById() {
        Long entId = 1L;
        Enterprise enter = new Enterprise();

        when(mapper.selectById(entId)).thenReturn(enter);
        assertNull(eService.selectById(null));
        assertSame(enter, eService.selectById(entId));

        verify(mapper, times(1)).selectById(entId);
    }

    /**
     *
     */
    @Test
    public void testQueryByEntName() {
        String entName = "aaa";
        List<Enterprise> list = new ArrayList();

        when(mapper.queryByEntName(entName)).thenReturn(list);
        assertNull(eService.queryByEntName(null));
        assertSame(list, eService.queryByEntName(entName));

        verify(mapper, times(1)).queryByEntName(entName);

    }

    /**
     *
     */
    @Test
    public void testChangeAppkey() {
        Long id = 1L;

        Enterprise enterprise = new Enterprise();
        enterprise.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setAppKey(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setId(id);

        when(scheduleService.undoScheduleJob(Mockito.anyString())).thenReturn("");
        when(globalConfigService.get(Mockito.anyString())).thenReturn(null).thenReturn("-1").thenReturn("90")
                .thenReturn("8").thenReturn("3").thenReturn("1");
        when(
                scheduleService.createScheduleJobForEnterpriseInterfaceExpire(
                        (Class<? extends Job>) Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString(), any(Date.class))).thenReturn("success");
        when(mapper.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(0).thenReturn(1);

        assertFalse(eService.changeAppkey(id));
        assertTrue(eService.changeAppkey(id));
        assertTrue(eService.changeAppkey(id));
        assertTrue(eService.changeAppkey(id));
        assertTrue(eService.changeAppkey(id));
        assertTrue(eService.changeAppkey(id));
    }

    /**
     *
     */
    @Test
    public void testCreateAppkey() {
        Long id = 1L;

        Enterprise enterprise = new Enterprise();
        enterprise.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setAppKey(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setId(id);

        when(globalConfigService.get(Mockito.anyString())).thenReturn(null).thenReturn("-1").thenReturn("90")
                .thenReturn("8").thenReturn("3").thenReturn("1");
        when(
                scheduleService.createScheduleJobForEnterpriseInterfaceExpire(
                        (Class<? extends Job>) Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString(), any(Date.class))).thenReturn("success");
        when(mapper.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(0).thenReturn(1);

        assertFalse(eService.createAppkey(id));
        assertTrue(eService.createAppkey(id));
        assertTrue(eService.createAppkey(id));
        assertTrue(eService.createAppkey(id));
        assertTrue(eService.createAppkey(id));
        assertTrue(eService.createAppkey(id));
    }

    /**
     *
     */
    @Test
    public void testCreateNewEnterprise() {
        //参数校验
        assertFalse(eService.createNewEnterprise(null));

        List<Enterprise> list = new ArrayList<Enterprise>();
        list.add(getValiEnterprise());
        when(mapper.selectEnterprisesByCode(Mockito.anyString())).thenReturn(list);
        assertFalse(eService.createNewEnterprise(getValiEnterprise()));

        when(globalConfigService.get(Mockito.anyString())).thenReturn(null);
        assertFalse(eService.createNewEnterprise(getValiEnterprise()));

        when(managerService.get(Mockito.anyLong(), Mockito.anyString())).thenReturn(null);
        assertFalse(eService.createNewEnterprise(getValiEnterprise()));

        try {
            when(mapper.insertSelective(getValiEnterprise())).thenReturn(0);
            eService.createNewEnterprise(getValiEnterprise());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        com.cmcc.vrp.province.model.Manager manager = new com.cmcc.vrp.province.model.Manager();
        manager.setRoleId(1L);
        manager.setName("aaa");

        when(managerService.createManager(manager, Long.valueOf(1L))).thenReturn(false);
        assertFalse(eService.createNewEnterprise(getValiEnterprise()));

        try {
            when(entManagerService.insertEntManager(any(EntManager.class))).thenReturn(false);
            eService.createNewEnterprise(getValiEnterprise());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //正常返回
        Enterprise enterprise = getValiEnterprise();
        enterprise.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());

        com.cmcc.vrp.province.model.Manager cityManager = new com.cmcc.vrp.province.model.Manager();
        cityManager.setDeleteFlag(new Integer(DELETE_FLAG.UNDELETED.getValue()).byteValue());
        cityManager.setId(21L);
        List<Enterprise> list2 = new ArrayList<Enterprise>();
        list2.add(enterprise);

        EnterprisesService enterprisesService = Mockito.spy(eService);
        Mockito.doReturn(true).when(enterprisesService).insertSelective(any(Enterprise.class));
        Mockito.doReturn(enterprise).when(enterprisesService).selectByCode(Mockito.anyString());

        when(mapper.selectEnterprisesByCode(Mockito.anyString())).thenReturn(list2);
        when(globalConfigService.get(Mockito.anyString())).thenReturn("1");
        when(managerService.get(Mockito.anyLong(), Mockito.anyString())).thenReturn(cityManager);
        when(managerService.createManager(any(com.cmcc.vrp.province.model.Manager.class), Mockito.anyLong()))
                .thenReturn(true);
        when(entManagerService.insertEntManager(any(EntManager.class))).thenReturn(true);
        assertTrue(enterprisesService.createNewEnterprise(enterprise));

    }

    /**
     *
     */
    @Test
    public void testCreateOrUpdateEnterprise() {

        //参数校验
        assertFalse(eService.createOrUpdateEnterprise(null));

        //新增操作
        EnterprisesService enterprisesService = Mockito.spy(eService);
        Mockito.doReturn(null).when(enterprisesService).selectByCode(Mockito.anyString());
        Mockito.doReturn(true).when(enterprisesService).createNewEnterprise(any(Enterprise.class));
        assertTrue(enterprisesService.createOrUpdateEnterprise(getValiEnterprise()));

        //更新操作
        Mockito.doReturn(getValiEnterprise()).when(enterprisesService).selectByCode(Mockito.anyString());
        Mockito.doReturn(true).when(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));

        //获取企业管理员职位节点信息失败
        try {
            when(entManagerService.getManagerForEnter(Mockito.anyLong())).thenReturn(null);
            enterprisesService.createOrUpdateEnterprise(getValiEnterprise());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //获取客户经理角色ID失败
        try {
            com.cmcc.vrp.province.model.Manager entAmdin = new com.cmcc.vrp.province.model.Manager();
            entAmdin.setDeleteFlag(new Integer(DELETE_FLAG.UNDELETED.getValue()).byteValue());
            when(entManagerService.getManagerForEnter(Mockito.anyLong())).thenReturn(entAmdin);
            when(globalConfigService.get(Mockito.anyString())).thenReturn(null);
            enterprisesService.createOrUpdateEnterprise(getValiEnterprise());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        com.cmcc.vrp.province.model.Manager entAmdin = new com.cmcc.vrp.province.model.Manager();
        entAmdin.setDeleteFlag(new Integer(DELETE_FLAG.UNDELETED.getValue()).byteValue());
        when(entManagerService.getManagerForEnter(Mockito.anyLong())).thenReturn(entAmdin);
        when(globalConfigService.get(Mockito.anyString())).thenReturn("12312");
        when(managerService.get(Mockito.anyLong(), Mockito.anyString())).thenReturn(entAmdin);
        when(managerService.updateByPrimaryKeySelective(any(com.cmcc.vrp.province.model.Manager.class)))
                .thenReturn(true);
        assertTrue(enterprisesService.createOrUpdateEnterprise(getValiEnterprise()));

        try {
            when(entManagerService.getManagerForEnter(Mockito.anyLong())).thenReturn(entAmdin);
            when(globalConfigService.get(Mockito.anyString())).thenReturn("12312");
            when(managerService.get(Mockito.anyLong(), Mockito.anyString())).thenReturn(entAmdin);
            when(managerService.updateByPrimaryKeySelective(any(com.cmcc.vrp.province.model.Manager.class)))
                    .thenReturn(false);
            enterprisesService.createOrUpdateEnterprise(getValiEnterprise());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void getNomalEnterByManagerIdTest() {
        when(managerService.getChildNodeString(Mockito.anyLong()))
                .thenReturn("1");
        when(mapper.getNomalEnterByManagerId(Mockito.anyString()))
                .thenReturn(null);
        assertNull(eService.getNomalEnterByManagerId(1l));

    }

    /**
     * 测试批量获取企业余额
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testQueryAccounts() throws Exception {
        when(accountService.getCurrencyAccounts(any(List.class)))
                .thenReturn(null).thenReturn(new LinkedList<Account>());

        assertNull(eService.queryAccounts(null));

        assertNotNull(eService.queryAccounts(validList()));

        verify(accountService, times(2)).getCurrencyAccounts(any(List.class));
    }

    private List<Long> validList() {
        List<Long> list = new LinkedList<Long>();
        Random random = new Random();

        int count = random.nextInt(100) + 1;
        for (int i = 0; i < count; i++) {
            list.add(random.nextLong());
        }

        return list;
    }


    private Enterprise getValiEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("11111");
        enterprise.setEnterpriseCity("22222");
        enterprise.setName("aaaa");
        enterprise.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        return enterprise;
    }
}

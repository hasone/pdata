package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.enums.ProductChangeOperation;
import com.cmcc.vrp.province.dao.ApprovalRequestMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.EcApprovalDetail;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.model.EntWhiteList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterprises;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.model.ProductChangeDetail;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.SdAccApprovalRequest;
import com.cmcc.vrp.province.service.AccountChangeDetailService;
import com.cmcc.vrp.province.service.AccountChangeOperatorService;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityApprovalDetailService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EcApprovalDetailService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntWhiteListService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.HistoryEnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;
import com.cmcc.vrp.province.service.ProductChangeDetailService;
import com.cmcc.vrp.province.service.ProductChangeOperatorService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by qinqinyan on 2016/11/1.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApprovalRequestServiceImplTest {
    @InjectMocks
    ApprovalRequestService approvalRequestService = new ApprovalRequestServiceImpl();
    @Mock
    ApprovalRequestMapper approvalRequestMapper;
    @Mock
    ApprovalRecordService approvalRecordService;
    @Mock
    ActivitiesService activitiesService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    AccountChangeDetailService accountChangeDetailService;
    @Mock
    AccountChangeOperatorService accountChangeOperatorService;
    @Mock
    AccountService accountService;
    @Mock
    ProductChangeDetailService productChangeDetailService;
    @Mock
    ProductChangeOperatorService productChangeOperatorService;
    @Mock
    EntProductService entProductService;
    @Mock
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Mock
    ApprovalDetailDefinitionService approvalDetailDefinitionService;
    @Mock
    RoleService roleService;
    @Mock
    ActivityApprovalDetailService activityApprovalDetailService;
    @Mock
    EcApprovalDetailService ecApprovalDetailService;
    @Mock
    EntWhiteListService entWhiteListService;
    @Mock
    EntCallbackAddrService entCallbackAddrService;
    @Mock
    AdministerService administerService;
    @Mock
    EnterpriseFileService enterpriseFileService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    GiveMoneyEnterService giveMoneyEnterService;
    @Mock
    HistoryEnterprisesService historyEnterprisesService;
    @Mock
    MdrcActiveDetailService mdrcActiveDetailService;
    @Mock
    ManagerService managerService;
    @Mock
    ProductService productService;
    @Mock
    AccountRecordService accountRecordService;

    @Test
    public void testInsert() {
        assertFalse(approvalRequestService.insert(null));

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        assertTrue(approvalRequestService.insert(new ApprovalRequest()));
        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
    }

    @Test
    public void testSelectByPrimaryKey() {
        assertNull(approvalRequestService.selectByPrimaryKey(null));

        Long id = 1L;
        Mockito.when(approvalRequestMapper.selectByPrimaryKey(anyLong())).thenReturn(new ApprovalRequest());
        assertNotNull(approvalRequestService.selectByPrimaryKey(id));
        Mockito.verify(approvalRequestMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        assertFalse(approvalRequestService.updateByPrimaryKeySelective(null));

        Mockito.when(approvalRequestMapper.updateApprovalRequest(any(ApprovalRequest.class))).thenReturn(1);
        assertTrue(approvalRequestService.updateByPrimaryKeySelective(new ApprovalRequest()));
        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
    }

    @Test
    public void testGetApprovalRequestList() {
        assertNull(approvalRequestService.getApprovalRequestList(null, null, null, null,
                false));
        
        
        QueryObject queryObject = new QueryObject();
        Long processId = 1L;
        List<Integer> preconditions = new ArrayList<Integer>();

        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        assertNull(approvalRequestService.getApprovalRequestList(queryObject, enterprises, processId, preconditions,
                false));
        preconditions.add(1);
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprises.add(enterprise);

        Mockito.when(approvalRequestMapper.getActivityApprovalRequestsByMap(anyMap())).thenReturn(
                new ArrayList<ApprovalRequest>());
        Mockito.when(approvalRequestMapper.getApprovalRequestListByMap(anyMap())).thenReturn(
                new ArrayList<ApprovalRequest>());

        assertNotNull(approvalRequestService.getApprovalRequestList(queryObject, enterprises, processId, preconditions,
                false));
        queryObject.getQueryCriterias().put("approvalType", 3);
        assertNotNull(approvalRequestService.getApprovalRequestList(queryObject, enterprises, processId, preconditions,
                false));

        Mockito.when(approvalRequestMapper.getApprovalRequestForMdrcActive(anyMap())).thenReturn(
                new ArrayList<ApprovalRequest>());
        queryObject.getQueryCriterias().remove("approvalType");
        queryObject.getQueryCriterias().put("approvalType", 6);
        assertNotNull(approvalRequestService.getApprovalRequestList(queryObject, enterprises, processId, preconditions,
                false));

        Mockito.verify(approvalRequestMapper).getActivityApprovalRequestsByMap(anyMap());
        Mockito.verify(approvalRequestMapper).getApprovalRequestListByMap(anyMap());
        Mockito.verify(approvalRequestMapper).getApprovalRequestForMdrcActive(anyMap());
    }

    @Test
    public void testCountApprovalRequest() {
        QueryObject queryObject = new QueryObject();
        Long processId = 1L;
        List<Integer> preconditions = new ArrayList<Integer>();

        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        assertEquals(0L,
                approvalRequestService.countApprovalRequest(queryObject, enterprises, processId, preconditions, false)
                        .longValue());

        preconditions.add(1);
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprises.add(enterprise);

        Mockito.when(approvalRequestMapper.countApprovalRequestByMap(anyMap())).thenReturn(1L);
        assertEquals(1L,
                approvalRequestService.countApprovalRequest(queryObject, enterprises, processId, preconditions, false)
                        .longValue());
        assertEquals(1L, approvalRequestService
                .countApprovalRequest(null, enterprises, processId, preconditions, false).longValue());

        Mockito.when(approvalRequestMapper.countActivityApprovalRequestsByMap(anyMap())).thenReturn(2L);
        queryObject.getQueryCriterias().put("approvalType", 3);
        assertEquals(2L,
                approvalRequestService.countApprovalRequest(queryObject, enterprises, processId, preconditions, false)
                        .longValue());

        Mockito.when(approvalRequestMapper.countApprovalRequestForMdrcActive(anyMap())).thenReturn(2L);
        queryObject.getQueryCriterias().remove("approvalType");
        queryObject.getQueryCriterias().put("approvalType", 6);
        assertEquals(2L,
                approvalRequestService.countApprovalRequest(queryObject, enterprises, processId, preconditions, false)
                        .longValue());

        Mockito.verify(approvalRequestMapper, Mockito.times(2)).countApprovalRequestByMap(anyMap());
        Mockito.verify(approvalRequestMapper).countActivityApprovalRequestsByMap(anyMap());
        Mockito.verify(approvalRequestMapper).countApprovalRequestForMdrcActive(anyMap());
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第一分支，第二分支异常
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord() throws Exception {
        assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(null, null, null));

        //1、更新审批记录，模拟返回false
        ApprovalRecord approvalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord1)).thenReturn(false);
        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(approvalRecord1, null, null));

        //2、更新审批请求记录，模拟抛出异常
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(approvalRequest2)).thenReturn(0);
        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(approvalRecord2, approvalRequest2, null));

        Mockito.verify(approvalRecordService, Mockito.times(2)).updateApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第三分支异常
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord1() throws Exception {
        //3、更新新的审批记录,模拟抛出异常
        ApprovalRecord approvalRecord3 = initApprovalRecord(3L);
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        ApprovalRecord newApprovalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord3)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(approvalRequest3)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord1)).thenReturn(false);
        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(approvalRecord3, approvalRequest3,
                newApprovalRecord1));

        Mockito.verify(approvalRecordService).updateApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }
    
    
    /**
     * updateLastRecordAndInsertNewRecord
     * 第三分支异常
     */
    @Test
    public void testUpdateLastRecordAndInsertNewRecord20() throws Exception {
        AccountChangeDetail accountChangeDetail = new AccountChangeDetail();
        com.cmcc.vrp.province.model.Product p = new com.cmcc.vrp.province.model.Product();
        Account account = new Account();

        accountChangeDetail.setCount(1.23);
        accountChangeDetail.setChangeType(ApprovalType.Account_Change_Approval.getCode());
        p.setType(1);
        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(p);
        Mockito.when(accountService.get(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(account);
        Mockito.when(
                accountService.addCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class),
                        Mockito.any(Double.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(
                true);
        assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(null, null, null, null, null,
                accountChangeDetail, null, null, null, null, null, null));

        accountChangeDetail.setCount(1.23);
        accountChangeDetail.setChangeType(ApprovalType.Account_Min_Change_Approval.getCode());
        p.setType(1);
        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(p);
        Mockito.when(accountService.updateMinCount(Mockito.anyLong(), Mockito.any(Double.class))).thenReturn(true);
        Mockito.when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(true);        
        assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(null, null, null, null, null,
                accountChangeDetail, null, null, null, null, null, null));
        try {
            Mockito.when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false);        
            assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(null, null, null, null, null,
                    accountChangeDetail, null, null, null, null, null, null));
        } catch (Exception e) {
            // TODO: handle exception
        }

        accountChangeDetail.setCount(1.23);
        accountChangeDetail.setChangeType(ApprovalType.Account_Alert_Change_Approval.getCode());
        p.setType(1);
        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(p);
        Mockito.when(accountService.updateAlertCount(Mockito.anyLong(), Mockito.any(Double.class))).thenReturn(true);
        Mockito.when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(true);
        assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(null, null, null, null, null,
                accountChangeDetail, null, null, null, null, null, null));
        try {
            Mockito.when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false);        
            assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(null, null, null, null, null,
                    accountChangeDetail, null, null, null, null, null, null));
        } catch (Exception e) {
            // TODO: handle exception
        }

        accountChangeDetail.setCount(1.23);
        accountChangeDetail.setChangeType(ApprovalType.Account_Stop_Change_Approval.getCode());
        p.setType(1);
        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(p);
        Mockito.when(accountService.updateStopCount(Mockito.anyLong(), Mockito.any(Double.class))).thenReturn(true);
        Mockito.when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(true);
        assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(null, null, null, null, null,
                accountChangeDetail, null, null, null, null, null, null));
        try {
            Mockito.when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false);        
            assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(null, null, null, null, null,
                    accountChangeDetail, null, null, null, null, null, null));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * approvalForActivity
     * 1、测试插入审批流程分支，返回false
     * 2、测试整个正常流程
     */
    @Test
    public void testApprovalForActivity1() {
        //1、审批操作，模拟返回false
        ApprovalRecord approvalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord1)).thenReturn(false);
        assertFalse(approvalRequestService.approvalForActivity(approvalRecord1, null, null, null));

        //2、整个正常流程
        ApprovalRecord approvalRecord3 = initApprovalRecord(3L);
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        ApprovalRecord newApprovalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord3)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(approvalRequest3)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord1)).thenReturn(true);

        //最后一个实参为null
//        assertFalse(approvalRequestService.approvalForActivity(approvalRecord3, approvalRequest3, newApprovalRecord1,
//                null));

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setApprovalStatus("0");
        activityApprovalDetail.setActivityType(ActivityType.LOTTERY.getCode());
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(true);

//        assertTrue(approvalRequestService.approvalForActivity(approvalRecord3, approvalRequest3, newApprovalRecord1,
//                activityApprovalDetail));

//        Mockito.verify(approvalRecordService, Mockito.times(3)).updateApprovalRecord(any(ApprovalRecord.class));
//        Mockito.verify(approvalRequestMapper, Mockito.times(2)).updateApprovalRequest(any(ApprovalRequest.class));
//        Mockito.verify(approvalRecordService, Mockito.times(2)).insertApprovalRecord(any(ApprovalRecord.class));
    }

    /**
     * approvalForActivity
     * 测试大转盘异常分支
     */
    @Test(expected = RuntimeException.class)
    public void testApprovalForActivity2() {
        ApprovalRecord approvalRecord3 = initApprovalRecord(3L);
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        ApprovalRecord newApprovalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord3)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(approvalRequest3)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord1)).thenReturn(true);

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setApprovalStatus("0");
        activityApprovalDetail.setActivityType(ActivityType.LOTTERY.getCode());
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(approvalRequestService.approvalForActivity(approvalRecord3, approvalRequest3, newApprovalRecord1,
                activityApprovalDetail));

        Mockito.verify(approvalRecordService).updateApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    /**
     * approvalForActivity
     * 测试砸金蛋异常分支
     */
    @Test(expected = RuntimeException.class)
    public void testApprovalForActivity3() {
        ApprovalRecord approvalRecord3 = initApprovalRecord(3L);
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        ApprovalRecord newApprovalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord3)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(approvalRequest3)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord1)).thenReturn(true);

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setApprovalStatus("0");
        activityApprovalDetail.setActivityType(ActivityType.GOLDENBALL.getCode());
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(approvalRequestService.approvalForActivity(approvalRecord3, approvalRequest3, newApprovalRecord1,
                activityApprovalDetail));

        Mockito.verify(approvalRecordService).updateApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    /**
     * approvalForActivity
     * 测试红包异常分支
     */
    @Test(expected = RuntimeException.class)
    public void testApprovalForActivity4() {
        ApprovalRecord approvalRecord3 = initApprovalRecord(3L);
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        ApprovalRecord newApprovalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord3)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(approvalRequest3)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord1)).thenReturn(true);

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setApprovalStatus("0");
        activityApprovalDetail.setActivityType(ActivityType.REDPACKET.getCode());
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(approvalRequestService.approvalForActivity(approvalRecord3, approvalRequest3, newApprovalRecord1,
                activityApprovalDetail));

        Mockito.verify(approvalRecordService).updateApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    /**
     * approvalForActivity
     * 测试其他活动异常分支
     */
    @Test(expected = RuntimeException.class)
    public void testApprovalForActivity5() {
        ApprovalRecord approvalRecord3 = initApprovalRecord(3L);
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        ApprovalRecord newApprovalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(approvalRecord3)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(approvalRequest3)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord1)).thenReturn(true);

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setApprovalStatus("0");
        activityApprovalDetail.setActivityType(ActivityType.QRCODE.getCode());
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(approvalRequestService.approvalForActivity(approvalRecord3, approvalRequest3, newApprovalRecord1,
                activityApprovalDetail));

        Mockito.verify(approvalRecordService).updateApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第一和第二分支
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord2() {
        ApprovalRecord updateApprovalRecord1 = initApprovalRecord(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord1)).thenReturn(false);
        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord1, null, null, null,
                null, null, null, null, null, null, null, null));

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(0);

        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, null, null, null, null, null, null, null, null, null, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第三分支
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord3() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(false);

        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, null, null, null, null, null, null, null, null, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第四分支（插入企业信息抛出异常）
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord4() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(enterprise)).thenReturn(false);

        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, enterprise, null, null, null, null, null, null, null, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第五分支（插入账户信息抛出异常）
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord5() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);

        AccountChangeDetail accountChangeDetail = new AccountChangeDetail();
        accountChangeDetail.setEntId(1L);
        accountChangeDetail.setProductId(1L);
        accountChangeDetail.setCount(1.0);
        accountChangeDetail.setSerialNum("test");

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(
                accountService.addCount(accountChangeDetail.getEntId(), accountChangeDetail.getProductId(),
                        AccountType.ENTERPRISE, accountChangeDetail.getCount() * 100,
                        accountChangeDetail.getSerialNum(), "充值请求审批通过，增加余额")).thenReturn(false);

        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, null, null, accountChangeDetail, null, null, null, null,
                null, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第五分支（插入账户信息抛出异常）
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord6() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);

        List<ProductChangeDetail> ProductChangeDetails = new ArrayList<ProductChangeDetail>();
        ProductChangeDetail p1 = initProductChangeDetail(0);
        ProductChangeDetail p2 = initProductChangeDetail(1);
        ProductChangeDetail p3 = initProductChangeDetail(2);
        ProductChangeDetails.add(p1);
        ProductChangeDetails.add(p2);
        ProductChangeDetails.add(p3);

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(entProductService.updateEnterProductByRecords(anyLong(), anyList(), anyList(), anyList()))
                .thenReturn(false);

        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, null, ProductChangeDetails, null, null, null, null, null,
                null, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第七分支（插入ec变更信息抛出异常）
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord7() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);

        EntCallbackAddr entCallbackAddr = new EntCallbackAddr();
        entCallbackAddr.setEntId(1L);
        entCallbackAddr.setCallbackAddr("test");

        List<EntWhiteList> entWhiteLists = new ArrayList<EntWhiteList>();
        EntWhiteList entWhiteList = new EntWhiteList();
        entWhiteList.setId(1L);
        entWhiteLists.add(entWhiteList);

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.batchInsert(anyList())).thenReturn(false);
        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, null, null, null, null, null, null, entCallbackAddr,
                entWhiteLists, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第七分支（插入ec变更信息抛出异常）
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord8() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);

        EntCallbackAddr entCallbackAddr = new EntCallbackAddr();
        entCallbackAddr.setEntId(1L);
        entCallbackAddr.setCallbackAddr("test");

        List<EntWhiteList> entWhiteLists = new ArrayList<EntWhiteList>();
        EntWhiteList entWhiteList = new EntWhiteList();
        entWhiteList.setId(1L);
        entWhiteLists.add(entWhiteList);

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(entCallbackAddrService.delete(anyLong())).thenReturn(true);
        Mockito.when(entCallbackAddrService.insert(any(EntCallbackAddr.class))).thenReturn(false);

        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, null, null, null, null, null, null, entCallbackAddr,
                entWhiteLists, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第七分支（插入ec变更信息抛出异常）
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord9() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);

        EntCallbackAddr entCallbackAddr = new EntCallbackAddr();
        entCallbackAddr.setEntId(1L);
        //entCallbackAddr.setCallbackAddr("test");

        List<EntWhiteList> entWhiteLists = new ArrayList<EntWhiteList>();
        EntWhiteList entWhiteList = new EntWhiteList();
        entWhiteList.setId(1L);
        entWhiteLists.add(entWhiteList);

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(entCallbackAddrService.delete(anyLong())).thenReturn(false);

        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, null, null, null, null, null, null, entCallbackAddr,
                entWhiteLists, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第七分支（插入ec变更信息抛出异常）
     */
    @Test(expected = RuntimeException.class)
    public void testUpdateLastRecordAndInsertNewRecord10() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);

        EntCallbackAddr entCallbackAddr = new EntCallbackAddr();
        entCallbackAddr.setEntId(1L);
        //entCallbackAddr.setCallbackAddr("test");

        List<EntWhiteList> entWhiteLists = new ArrayList<EntWhiteList>();
        EntWhiteList entWhiteList = new EntWhiteList();
        entWhiteList.setId(1L);
        entWhiteLists.add(entWhiteList);

        Enterprise ent = new Enterprise();
        ent.setId(1L);
        //ent.setAppKey("test");

        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(entCallbackAddrService.delete(anyLong())).thenReturn(true);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(ent);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);
        Mockito.when(enterprisesService.createAppkey(anyLong())).thenReturn(false);

        assertFalse(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, enterprise, null, null, null, null, null, entCallbackAddr,
                entWhiteLists, null));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 第七分支（插入ec变更信息抛出异常）
     */
    @Test
    public void testUpdateLastRecordAndInsertNewRecord11() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);

        EntCallbackAddr entCallbackAddr = new EntCallbackAddr();
        entCallbackAddr.setEntId(1L);
        //entCallbackAddr.setCallbackAddr("test");

        List<EntWhiteList> entWhiteLists = new ArrayList<EntWhiteList>();
        EntWhiteList entWhiteList = new EntWhiteList();
        entWhiteList.setId(1L);
        entWhiteLists.add(entWhiteList);

        Enterprise ent = new Enterprise();
        ent.setId(1L);
        ent.setAppKey("test");

        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(entCallbackAddrService.delete(anyLong())).thenReturn(true);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(ent);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);

        assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, enterprise, null, null, null, null, null, entCallbackAddr,
                entWhiteLists, null));

        Mockito.verify(approvalRecordService, Mockito.atLeastOnce()).updateApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(approvalRequestMapper, Mockito.atLeastOnce()).updateApprovalRequest(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService, Mockito.atLeastOnce()).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(entWhiteListService, Mockito.atLeastOnce()).deleteByEntId(anyLong());
        Mockito.verify(entWhiteListService, Mockito.atLeastOnce()).batchInsert(anyList());
        Mockito.verify(entCallbackAddrService, Mockito.atLeastOnce()).delete(anyLong());
        Mockito.verify(enterprisesService, Mockito.atLeastOnce()).selectById(anyLong());
        Mockito.verify(enterprisesService, Mockito.atLeastOnce()).updateByPrimaryKeySelective(any(Enterprise.class));
    }

    /**
     * updateLastRecordAndInsertNewRecord
     * 企业信息变更
     */
    @Test
    public void testUpdateLastRecordAndInsertNewRecord12() {

        ApprovalRecord updateApprovalRecord2 = initApprovalRecord(2L);
        ApprovalRequest updateApprovalRequest = initApprovalRequest(1L);
        ApprovalRecord newApprovalRecord = initApprovalRecord(3L);
        HistoryEnterprises historyEnterprises = initNomalEnterprises();
        EnterpriseFile enterpriseFile = new EnterpriseFile();

        Mockito.when(approvalRecordService.updateApprovalRecord(updateApprovalRecord2)).thenReturn(true);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(updateApprovalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(newApprovalRecord)).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(Mockito.any(Enterprise.class))).thenReturn(false)
                .thenReturn(true);
        Mockito.when(enterpriseFileService.update(enterpriseFile)).thenReturn(false).thenReturn(true);
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("sc");
        Mockito.when(giveMoneyEnterService.updateByEnterId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false)
                .thenReturn(true);
        Mockito.when(historyEnterprisesService.updateStatusByPrimaryKey(Mockito.any(HistoryEnterprises.class)))
                .thenReturn(false).thenReturn(true);
        try {
            approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2, updateApprovalRequest,
                    newApprovalRecord, null, null, null, null, historyEnterprises, enterpriseFile, null, null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2, updateApprovalRequest,
                    newApprovalRecord, null, null, null, null, historyEnterprises, enterpriseFile, null, null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2, updateApprovalRequest,
                    newApprovalRecord, null, null, null, null, historyEnterprises, enterpriseFile, null, null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2, updateApprovalRequest,
                    newApprovalRecord, null, null, null, null, historyEnterprises, enterpriseFile, null, null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, null, null, null, null, historyEnterprises, enterpriseFile,
                null, null, null));

        historyEnterprises.setDeleteFlag(8);
        Mockito.when(historyEnterprisesService.updateStatusByPrimaryKey(Mockito.any(HistoryEnterprises.class)))
                .thenReturn(false).thenReturn(true);
        try {
            approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2, updateApprovalRequest,
                    newApprovalRecord, null, null, null, null, historyEnterprises, enterpriseFile, null, null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord2,
                updateApprovalRequest, newApprovalRecord, null, null, null, null, historyEnterprises, enterpriseFile,
                null, null, null));

    }

    /**
     * 无审批流程 submitWithoutApproval
     * 第一个和第二个(企业)异常分支
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitWithoutApproval() {
        ApprovalRequest approvalRequest1 = initApprovalRequest(1L);

        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);

        //参数校验不通过
        List<ProductChangeDetail> productChangeDetails = new ArrayList<ProductChangeDetail>();
        assertFalse(approvalRequestService.submitWithoutApproval(approvalRequest1, null, null, productChangeDetails,
                null));

        //第一分支：返回false
        Mockito.when(approvalRequestMapper.insert(approvalRequest1)).thenReturn(0);
        assertFalse(approvalRequestService.submitWithoutApproval(approvalRequest1, enterprise, null, null, null));

        //第二分支：抛出异常
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(false);

        assertFalse(approvalRequestService.submitWithoutApproval(approvalRequest2, enterprise, null, null, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));
    }

    /**
     * 无审批流程 submitWithoutApproval
     * 第三个（账户变更）异常分支
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitWithoutApproval2() {

        ApprovalRequest approvalRequest = initApprovalRequest(1L);

        AccountChangeDetail accountChangeDetail = new AccountChangeDetail();
        accountChangeDetail.setSerialNum("test");

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(accountChangeDetailService.insert(any(AccountChangeDetail.class))).thenReturn(true);
        Mockito.when(accountChangeOperatorService.deleteBySerialNum(anyString())).thenReturn(false);

        assertFalse(approvalRequestService
                .submitWithoutApproval(approvalRequest, null, accountChangeDetail, null, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(accountChangeDetailService).insert(any(AccountChangeDetail.class));
        Mockito.verify(accountChangeOperatorService).deleteBySerialNum(anyString());
    }

    /**
     * 无审批流程 submitWithoutApproval
     * 第三个（账户变更）异常分支(充值异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitWithoutApproval3() {

        ApprovalRequest approvalRequest = initApprovalRequest(1L);

        AccountChangeDetail accountChangeDetail = new AccountChangeDetail();
        accountChangeDetail.setSerialNum("test");
        accountChangeDetail.setProductId(1L);
        accountChangeDetail.setCount(1.0);

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(accountChangeDetailService.insert(any(AccountChangeDetail.class))).thenReturn(true);
        Mockito.when(accountChangeOperatorService.deleteBySerialNum(anyString())).thenReturn(true);
        Mockito.when(
                accountService.addCount(approvalRequest.getEntId(), accountChangeDetail.getProductId(),
                        AccountType.ENTERPRISE, accountChangeDetail.getCount() * 100,
                        accountChangeDetail.getSerialNum(), "充值请求无须审批，增加余额")).thenReturn(false);

        assertFalse(approvalRequestService
                .submitWithoutApproval(approvalRequest, null, accountChangeDetail, null, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(accountChangeDetailService).insert(any(AccountChangeDetail.class));
        Mockito.verify(accountChangeOperatorService).deleteBySerialNum(anyString());
        Mockito.verify(accountService).addCount(approvalRequest.getEntId(), accountChangeDetail.getProductId(),
                AccountType.ENTERPRISE, accountChangeDetail.getCount() * 100, accountChangeDetail.getSerialNum(),
                "充值请求无须审批，增加余额");
    }

    /**
     * 无审批流程 submitWithoutApproval
     * 第四个（产品变更）异常分支
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitWithoutApproval4() {

        ApprovalRequest approvalRequest = initApprovalRequest(1L);

        List<ProductChangeDetail> productChangeDetails = new ArrayList<ProductChangeDetail>();
        ProductChangeDetail productChangeDetail = new ProductChangeDetail();
        productChangeDetail.setId(1L);
        productChangeDetails.add(productChangeDetail);

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(productChangeDetailService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(productChangeOperatorService.deleteProductChangeRecordByEntId(anyLong())).thenReturn(false);

        assertFalse(approvalRequestService.submitWithoutApproval(approvalRequest, null, null, productChangeDetails,
                null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(productChangeDetailService).batchInsert(anyList());
        Mockito.verify(productChangeOperatorService).deleteProductChangeRecordByEntId(anyLong());
    }

    /**
     * 无审批流程 submitWithoutApproval
     * 第四个（产品变更）异常分支(跟新企业与产品关系异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitWithoutApproval5() {

        ApprovalRequest approvalRequest = initApprovalRequest(1L);

        List<ProductChangeDetail> productChangeDetails = new ArrayList<ProductChangeDetail>();
        ProductChangeDetail addProduct = initProductChangeDetail(ProductChangeOperation.ADD.getValue());
        ProductChangeDetail delProduct = initProductChangeDetail(ProductChangeOperation.DELETE.getValue());
        ProductChangeDetail changeProduct = initProductChangeDetail(ProductChangeOperation.CHANGE_DISCOUNT.getValue());
        productChangeDetails.add(addProduct);
        productChangeDetails.add(delProduct);
        productChangeDetails.add(changeProduct);

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(productChangeDetailService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(productChangeOperatorService.deleteProductChangeRecordByEntId(anyLong())).thenReturn(true);
        Mockito.when(entProductService.updateEnterProductByRecords(anyLong(), anyList(), anyList(), anyList()))
                .thenReturn(false);

        assertFalse(approvalRequestService.submitWithoutApproval(approvalRequest, null, null, productChangeDetails,
                null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(productChangeDetailService).batchInsert(anyList());
        Mockito.verify(productChangeOperatorService).deleteProductChangeRecordByEntId(anyLong());
        Mockito.verify(entProductService).updateEnterProductByRecords(anyLong(), anyList(), anyList(), anyList());
    }

    /**
     * submitEnterpriseChange
     * 提交企业信息变更（无审核流程下的正常流程）
     */
    @Test
    public void testSubmitEnterpriseChange() {
        Long entId = 1L;
        Long creatorId = 1L;

        assertNull(approvalRequestService.submitEnterpriseChange(entId, null));

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setStage(0);

        Mockito.when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcess);
        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);

        assertNull(approvalRequestService.submitEnterpriseChange(entId, creatorId));

        Mockito.verify(approvalProcessDefinitionService, Mockito.times(2)).selectByType(anyInt());
        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
    }

    /**
     * submitEnterpriseChange
     * 提交企业信息变更（有审核流程下的非正常流程）
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitEnterpriseChange1() {
        Long entId = 1L;
        Long creatorId = 1L;

        assertNull(approvalRequestService.submitEnterpriseChange(entId, null));

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setStage(1);

        ApprovalDetailDefinition approvalDetailDefinition = new ApprovalDetailDefinition();
        approvalDetailDefinition.setRoleId(1L);

        Role role = new Role();
        role.setName("测试管理员");

        Mockito.when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcess);
        Mockito.when(approvalDetailDefinitionService.getCurrentApprovalDetailByProccessId(anyLong(), anyInt()))
                .thenReturn(approvalDetailDefinition);
        Mockito.when(roleService.getRoleById(anyLong())).thenReturn(role);
        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(false);

        assertNull(approvalRequestService.submitEnterpriseChange(entId, creatorId));

        Mockito.verify(approvalProcessDefinitionService, Mockito.times(2)).selectByType(anyInt());
        Mockito.verify(approvalDetailDefinitionService).getCurrentApprovalDetailByProccessId(anyLong(), anyInt());
        Mockito.verify(roleService).getRoleById(anyLong());
        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    /**
     * 提交审核(插入审核请求异常分支和插入审批记录异常分支)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitApproval() {
        ApprovalRequest approvalRequest1 = initApprovalRequest(1L);

        Mockito.when(approvalRequestMapper.insert(approvalRequest1)).thenReturn(0);
        assertFalse(approvalRequestService.submitApproval(approvalRequest1, null, null, null, null, null, null));

        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);
        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(false);
        assertFalse(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, null, null, null, null,
                null));

        Mockito.verify(approvalRequestMapper, Mockito.times(2)).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    /**
     * 提交审核(企业开户分支异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitApproval1() {
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(false);

        assertFalse(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, enterprise, null, null,
                null, null));

        Mockito.verify(approvalRequestMapper, Mockito.times(2)).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));
    }

    /**
     * 提交审核(企业开户分支正常常)
     */
    @Test
    public void testSubmitApproval0() {
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);

        assertTrue(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, enterprise, null, null,
                null, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));
    }

    /**
     * 提交审核(账户变更分支异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitApproval2() {
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);

        AccountChangeDetail accountChangeDetail = new AccountChangeDetail();
        accountChangeDetail.setSerialNum("test");

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(accountChangeDetailService.insert(any(AccountChangeDetail.class))).thenReturn(true);
        Mockito.when(accountChangeOperatorService.deleteBySerialNum(anyString())).thenReturn(false);

        assertFalse(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, null, accountChangeDetail,
                null, null, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(accountChangeDetailService).insert(any(AccountChangeDetail.class));
        Mockito.verify(accountChangeOperatorService).deleteBySerialNum(anyString());
    }

    /**
     * 提交审核(产品变更分支异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitApproval3() {
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);

        List<ProductChangeDetail> productChangeDetails = new ArrayList<ProductChangeDetail>();
        ProductChangeDetail record = new ProductChangeDetail();
        record.setId(1L);
        productChangeDetails.add(record);

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(productChangeDetailService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(productChangeOperatorService.deleteProductChangeRecordByEntId(anyLong())).thenReturn(false);

        assertFalse(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, null, null,
                productChangeDetails, null, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(productChangeDetailService).batchInsert(anyList());
        Mockito.verify(productChangeOperatorService).deleteProductChangeRecordByEntId(anyLong());
    }

    /**
     * 提交审核(大转盘活动分支异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitApproval5() {
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setActivityType(ActivityType.LOTTERY.getCode());

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(activityApprovalDetailService.insert(any(ActivityApprovalDetail.class))).thenReturn(true);
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, null, null, null,
                activityApprovalDetail, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(activityApprovalDetailService).insert(any(ActivityApprovalDetail.class));
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
    }

    /**
     * 提交审核(砸金蛋活动分支异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitApproval6() {
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setActivityType(ActivityType.GOLDENBALL.getCode());

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(activityApprovalDetailService.insert(any(ActivityApprovalDetail.class))).thenReturn(true);
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, null, null, null,
                activityApprovalDetail, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(activityApprovalDetailService).insert(any(ActivityApprovalDetail.class));
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
    }

    /**
     * 提交审核(红包活动分支异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitApproval7() {
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setActivityType(ActivityType.REDPACKET.getCode());

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(activityApprovalDetailService.insert(any(ActivityApprovalDetail.class))).thenReturn(true);
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, null, null, null,
                activityApprovalDetail, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(activityApprovalDetailService).insert(any(ActivityApprovalDetail.class));
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
    }

    /**
     * 提交审核(红包活动分支异常)
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitApproval8() {
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);

        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setActivityType(ActivityType.QRCODE.getCode());

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(activityApprovalDetailService.insert(any(ActivityApprovalDetail.class))).thenReturn(true);
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(approvalRequestService.submitApproval(approvalRequest2, approvalRecord2, null, null, null,
                activityApprovalDetail, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(activityApprovalDetailService).insert(any(ActivityApprovalDetail.class));
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
    }

    @Test
    public void testSelectByEntIdAndProcessId() {
        Long entId = 1L;
        Long processId = 1L;
        assertNull(approvalRequestService.selectByEntIdAndProcessId(entId, null));
        Mockito.when(approvalRequestMapper.selectByEntIdAndProcessId(anyLong(), anyLong())).thenReturn(
                new ArrayList<ApprovalRequest>());
        assertNotNull(approvalRequestService.selectByEntIdAndProcessId(entId, processId));
        Mockito.verify(approvalRequestMapper).selectByEntIdAndProcessId(anyLong(), anyLong());
    }

    @Test
    public void testQueryApprovalRequestsForAccountChange() {
        QueryObject queryObject = new QueryObject();
        Long adminId = 1L;
        Integer approvalType = 1;

        Administer administer = new Administer();
        administer.setId(1L);

        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        Enterprise record = new Enterprise();
        record.setId(1L);
        enterprises.add(record);

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);

        assertNull(approvalRequestService.queryApprovalRequestsForAccountChange(queryObject, adminId, null));

        queryObject.getQueryCriterias().put("endTime", "2017-11-11");

        Mockito.when(administerService.selectAdministerById(anyLong())).thenReturn(administer);
        Mockito.when(enterprisesService.getAllEnterpriseListByAdminId(any(Administer.class))).thenReturn(enterprises);
        Mockito.when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcess);
        Mockito.when(approvalRequestMapper.queryApprovalRequestsForAccountChange(anyMap())).thenReturn(
                new ArrayList<ApprovalRequest>());

        assertNotNull(approvalRequestService.queryApprovalRequestsForAccountChange(queryObject, adminId, approvalType));

        Mockito.verify(administerService).selectAdministerById(anyLong());
        Mockito.verify(enterprisesService).getAllEnterpriseListByAdminId(any(Administer.class));
        Mockito.verify(approvalProcessDefinitionService).selectByType(anyInt());
        Mockito.verify(approvalRequestMapper).queryApprovalRequestsForAccountChange(anyMap());
    }
    
    @Test
    public void testQueryRecordForAccountChange() {
        QueryObject queryObject = new QueryObject();
        Long adminId = 1L;
        Integer approvalType = 1;

        Administer administer = new Administer();
        administer.setId(1L);

        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        Enterprise record = new Enterprise();
        record.setId(1L);
        enterprises.add(record);

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);

        assertNull(approvalRequestService.queryRecordForAccountChange(queryObject, adminId, null));

        queryObject.getQueryCriterias().put("endTime", "2017-11-11");

        Mockito.when(administerService.selectAdministerById(anyLong())).thenReturn(administer);
        Mockito.when(enterprisesService.getAllEnterpriseListByAdminId(any(Administer.class))).thenReturn(enterprises);
        Mockito.when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcess);
        Mockito.when(approvalRequestMapper.queryRecordForAccountChange(anyMap())).thenReturn(
                new ArrayList<ApprovalRequest>());

        assertNotNull(approvalRequestService.queryRecordForAccountChange(queryObject, adminId, approvalType));

        Mockito.verify(administerService).selectAdministerById(anyLong());
        Mockito.verify(enterprisesService).getAllEnterpriseListByAdminId(any(Administer.class));
        Mockito.verify(approvalProcessDefinitionService).selectByType(anyInt());
        Mockito.verify(approvalRequestMapper).queryRecordForAccountChange(anyMap());
    }

    @Test
    public void testCountApprovalRequestsForAccountChange() {
        QueryObject queryObject = new QueryObject();
        Long adminId = 1L;
        Integer approvalType = 1;

        Administer administer = new Administer();
        administer.setId(1L);

        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        Enterprise record = new Enterprise();
        record.setId(1L);
        enterprises.add(record);

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);

        assertEquals(0L, approvalRequestService.countApprovalRequestsForAccountChange(queryObject, adminId, null)
                .longValue());

        queryObject.getQueryCriterias().put("endTime", "2017-11-11");

        Mockito.when(administerService.selectAdministerById(anyLong())).thenReturn(administer);
        Mockito.when(enterprisesService.getAllEnterpriseListByAdminId(any(Administer.class))).thenReturn(enterprises);
        Mockito.when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcess);
        Mockito.when(approvalRequestMapper.countApprovalRequestsForAccountChange(anyMap())).thenReturn(1L);

        assertEquals(1L,
                approvalRequestService.countApprovalRequestsForAccountChange(queryObject, adminId, approvalType)
                        .longValue());

        Mockito.verify(administerService).selectAdministerById(anyLong());
        Mockito.verify(enterprisesService).getAllEnterpriseListByAdminId(any(Administer.class));
        Mockito.verify(approvalProcessDefinitionService).selectByType(anyInt());
        Mockito.verify(approvalRequestMapper).countApprovalRequestsForAccountChange(anyMap());
    }

    @Test
    public void testGetCurrentStatus() {
        //分支1
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setEntId(1L);
        assertEquals("不需要审批", approvalRequestService.getCurrentStatus(approvalRequest));

        //分支2
        ApprovalRequest approvalRequest1 = initApprovalRequest(1L);
        approvalRequest1.setProcessId(1L);
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setStage(0);
        Mockito.when(approvalProcessDefinitionService.getApprovalProcessById(approvalRequest1.getProcessId()))
                .thenReturn(approvalProcessDefinition);
        assertEquals("审批结束", approvalRequestService.getCurrentStatus(approvalRequest1));

        //分支3
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        approvalRequest2.setProcessId(2L);
        approvalRequest2.setDeleteFlag(0);

        ApprovalProcessDefinition approvalProcessDefinition1 = new ApprovalProcessDefinition();
        approvalProcessDefinition1.setStage(1);

        ApprovalDetailDefinition approvalDetail = new ApprovalDetailDefinition();

        Role role = new Role();
        role.setName("测试管理员");

        Mockito.when(approvalProcessDefinitionService.getApprovalProcessById(approvalRequest2.getProcessId()))
                .thenReturn(approvalProcessDefinition1);
        Mockito.when(
                approvalDetailDefinitionService.getCurrentApprovalDetailByProccessId(approvalRequest2.getProcessId(),
                        approvalRequest2.getStatus())).thenReturn(approvalDetail);
        Mockito.when(roleService.getRoleById(anyLong())).thenReturn(role);

        assertEquals("待" + role.getName() + "审核", approvalRequestService.getCurrentStatus(approvalRequest2));

        //分支4
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        approvalRequest3.setProcessId(3L);
        approvalRequest3.setDeleteFlag(1);

        ApprovalProcessDefinition approvalProcessDefinition2 = new ApprovalProcessDefinition();
        approvalProcessDefinition2.setStage(1);

        Mockito.when(approvalProcessDefinitionService.getApprovalProcessById(approvalRequest3.getProcessId()))
                .thenReturn(approvalProcessDefinition2);
        Mockito.when(
                approvalDetailDefinitionService.getCurrentApprovalDetailByProccessId(approvalRequest3.getProcessId(),
                        approvalRequest3.getStatus())).thenReturn(approvalDetail);

        assertEquals("已驳回", approvalRequestService.getCurrentStatus(approvalRequest3));

        Mockito.verify(approvalProcessDefinitionService, Mockito.atLeastOnce()).getApprovalProcessById(anyLong());
        Mockito.verify(approvalDetailDefinitionService, Mockito.atLeastOnce()).getCurrentApprovalDetailByProccessId(
                anyLong(), anyInt());
        Mockito.verify(roleService, Mockito.atLeastOnce()).getRoleById(anyLong());
    }

    @Test
    public void testGetCurrentStatus1() {
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        approvalRequest3.setProcessId(3L);
        approvalRequest3.setDeleteFlag(1);
        approvalRequest3.setStatus(1);

        ApprovalProcessDefinition approvalProcessDefinition2 = new ApprovalProcessDefinition();
        approvalProcessDefinition2.setStage(1);

        ApprovalDetailDefinition lastApprovalDetail = new ApprovalDetailDefinition();
        lastApprovalDetail.setApprovalCode(1);
        lastApprovalDetail.setPrecondition(0);

        Mockito.when(approvalProcessDefinitionService.getApprovalProcessById(approvalRequest3.getProcessId()))
                .thenReturn(approvalProcessDefinition2);
        Mockito.when(
                approvalDetailDefinitionService.getCurrentApprovalDetailByProccessId(approvalRequest3.getProcessId(),
                        approvalRequest3.getStatus())).thenReturn(null);
        Mockito.when(approvalDetailDefinitionService.getLastApprovalDetailByProccessId(anyLong())).thenReturn(
                lastApprovalDetail);

        assertEquals("审批结束", approvalRequestService.getCurrentStatus(approvalRequest3));

        Mockito.verify(approvalProcessDefinitionService, Mockito.atLeastOnce()).getApprovalProcessById(anyLong());
        Mockito.verify(approvalDetailDefinitionService, Mockito.atLeastOnce()).getCurrentApprovalDetailByProccessId(
                anyLong(), anyInt());
        Mockito.verify(approvalDetailDefinitionService, Mockito.atLeastOnce()).getLastApprovalDetailByProccessId(
                anyLong());
    }

    @Test
    public void testGetCurrentStatus2() {
        ApprovalRequest approvalRequest3 = initApprovalRequest(3L);
        approvalRequest3.setProcessId(3L);
        approvalRequest3.setDeleteFlag(1);
        approvalRequest3.setStatus(3);

        ApprovalProcessDefinition approvalProcessDefinition2 = new ApprovalProcessDefinition();
        approvalProcessDefinition2.setStage(1);

        ApprovalDetailDefinition lastApprovalDetail = new ApprovalDetailDefinition();
        lastApprovalDetail.setApprovalCode(1);
        lastApprovalDetail.setPrecondition(0);

        Mockito.when(approvalProcessDefinitionService.getApprovalProcessById(approvalRequest3.getProcessId()))
                .thenReturn(approvalProcessDefinition2);
        Mockito.when(
                approvalDetailDefinitionService.getCurrentApprovalDetailByProccessId(approvalRequest3.getProcessId(),
                        approvalRequest3.getStatus())).thenReturn(null);
        Mockito.when(approvalDetailDefinitionService.getLastApprovalDetailByProccessId(anyLong())).thenReturn(
                lastApprovalDetail);

        assertEquals("不需要审批", approvalRequestService.getCurrentStatus(approvalRequest3));

        Mockito.verify(approvalProcessDefinitionService, Mockito.atLeastOnce()).getApprovalProcessById(anyLong());
        Mockito.verify(approvalDetailDefinitionService, Mockito.atLeastOnce()).getCurrentApprovalDetailByProccessId(
                anyLong(), anyInt());
        Mockito.verify(approvalDetailDefinitionService, Mockito.atLeastOnce()).getLastApprovalDetailByProccessId(
                anyLong());
    }

    @Test
    public void testSelectByActivityId() {
        assertNull(approvalRequestService.selectByActivityId(null));

        String activityId = "test";
        Mockito.when(approvalRequestMapper.selectByActivityId(anyString()))
                .thenReturn(new ArrayList<ApprovalRequest>());
        assertNotNull(approvalRequestService.selectByActivityId(activityId));
        Mockito.verify(approvalRequestMapper).selectByActivityId(anyString());
    }

    @Test
    public void testQueryForEntChange() {
        QueryObject queryObject = new QueryObject();
        List<ApprovalProcessDefinition> approvalProcessDefinitions = new ArrayList<ApprovalProcessDefinition>();
        Mockito.when(approvalProcessDefinitionService.selectApprovalProcessesByType(anyInt())).thenReturn(
                approvalProcessDefinitions);
        assertNull(approvalRequestService.queryForEntChange(queryObject));
        Mockito.verify(approvalProcessDefinitionService).selectApprovalProcessesByType(anyInt());
    }

    @Test
    public void testQueryForEntChange1() {
        QueryObject queryObject = new QueryObject();
        List<ApprovalProcessDefinition> approvalProcessDefinitions = new ArrayList<ApprovalProcessDefinition>();
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setId(1L);
        approvalProcessDefinitions.add(approvalProcessDefinition);

        Mockito.when(approvalProcessDefinitionService.selectApprovalProcessesByType(anyInt())).thenReturn(
                approvalProcessDefinitions);
        Mockito.when(approvalRequestMapper.queryForEntChange(anyMap())).thenReturn(new ArrayList<ApprovalRequest>());
        assertNotNull(approvalRequestService.queryForEntChange(queryObject));
        Mockito.verify(approvalProcessDefinitionService).selectApprovalProcessesByType(anyInt());
        Mockito.verify(approvalRequestMapper).queryForEntChange(anyMap());
    }

    /**
     * countForEntChange
     *
     */
    @Test
    public void testCountForEntChange() {
        QueryObject queryObject = new QueryObject();
        List<ApprovalProcessDefinition> approvalProcessList = new ArrayList<ApprovalProcessDefinition>();
        Mockito.when(approvalProcessDefinitionService.selectApprovalProcessesByType(anyInt())).thenReturn(
                approvalProcessList);
        assertEquals(0L, approvalRequestService.countForEntChange(queryObject).longValue());
        Mockito.verify(approvalProcessDefinitionService).selectApprovalProcessesByType(anyInt());
    }

    /**
     * countForEntChange
     *
     */
    @Test
    public void testCountForEntChange1() {
        QueryObject queryObject = new QueryObject();

        List<ApprovalProcessDefinition> approvalProcessList = new ArrayList<ApprovalProcessDefinition>();
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setId(1L);
        approvalProcessList.add(approvalProcessDefinition);

        Mockito.when(approvalProcessDefinitionService.selectApprovalProcessesByType(anyInt())).thenReturn(
                approvalProcessList);
        Mockito.when(approvalRequestMapper.countForEntChange(anyMap())).thenReturn(1L);

        assertEquals(1L, approvalRequestService.countForEntChange(queryObject).longValue());

        Mockito.verify(approvalProcessDefinitionService).selectApprovalProcessesByType(anyInt());
        Mockito.verify(approvalRequestMapper).countForEntChange(anyMap());
    }

    @Test(expected = RuntimeException.class)
    public void testSubmitEcApproval() {
        Integer interfaceFlag = InterfaceStatus.CLOSE.getCode();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);

        Mockito.when(approvalRequestMapper.insert(approvalRequest)).thenReturn(0);
        assertFalse(approvalRequestService.submitEcApproval(interfaceFlag, approvalRequest, null, null));

        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);
        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest2)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(approvalRecord2)).thenReturn(false);
        assertFalse(approvalRequestService.submitEcApproval(interfaceFlag, approvalRequest2, approvalRecord2, null));

        Mockito.verify(approvalRequestMapper, Mockito.times(2)).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    @Test(expected = RuntimeException.class)
    public void testSubmitEcApproval1() {
        Integer interfaceFlag = InterfaceStatus.CLOSE.getCode();
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(false);

        assertFalse(approvalRequestService.submitEcApproval(interfaceFlag, approvalRequest2, approvalRecord2, null));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));
    }

    @Test(expected = RuntimeException.class)
    public void testSubmitEcApproval2() {
        Integer interfaceFlag = InterfaceStatus.APPROVING.getCode();
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);
        EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
        ecApprovalDetail.setId(1L);

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);
        Mockito.when(ecApprovalDetailService.insert(any(EcApprovalDetail.class))).thenReturn(false);

        assertFalse(approvalRequestService.submitEcApproval(interfaceFlag, approvalRequest2, approvalRecord2,
                ecApprovalDetail));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));
        Mockito.verify(ecApprovalDetailService).insert(any(EcApprovalDetail.class));
    }

    /**
     * submitEcApproval
     * 正常流程
     */
    @Test
    public void testSubmitEcApproval3() {
        Integer interfaceFlag = InterfaceStatus.APPROVING.getCode();
        ApprovalRequest approvalRequest2 = initApprovalRequest(2L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(2L);
        EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
        ecApprovalDetail.setId(1L);

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);
        Mockito.when(ecApprovalDetailService.insert(any(EcApprovalDetail.class))).thenReturn(true);

        assertTrue(approvalRequestService.submitEcApproval(interfaceFlag, approvalRequest2, approvalRecord2,
                ecApprovalDetail));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));
        Mockito.verify(ecApprovalDetailService).insert(any(EcApprovalDetail.class));
    }

    /**
     * submitEcWithoutApproval
     * 分支1：返回false
     */
    @Test
    public void testSubmitEcWithoutApproval() {
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        Mockito.when(approvalRequestMapper.insert(any(ApprovalRequest.class))).thenReturn(0);
        assertFalse(approvalRequestService.submitEcWithoutApproval(null, approvalRequest, null, null, null));
        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
    }

    /**
     * submitEcWithoutApproval
     * 分支2：抛异常
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitEcWithoutApproval1() {
        Integer interfaceFlag = InterfaceStatus.APPROVING.getCode();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        ApprovalRecord approvalRecord = initApprovalRecord(1L);
        EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
        ecApprovalDetail.setEntId(1L);
        List<String> ips = new ArrayList<String>();

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);
        Mockito.when(ecApprovalDetailService.insert(any(EcApprovalDetail.class))).thenReturn(true);

        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.insertIps(anyList(), anyLong())).thenReturn(false);

        assertFalse(approvalRequestService.submitEcWithoutApproval(interfaceFlag, approvalRequest, approvalRecord,
                ecApprovalDetail, ips));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));
        Mockito.verify(ecApprovalDetailService).insert(any(EcApprovalDetail.class));

        Mockito.verify(entWhiteListService).deleteByEntId(anyLong());
        Mockito.verify(entWhiteListService).insertIps(anyList(), anyLong());
    }

    /**
     * submitEcWithoutApproval
     * 分支3：抛异常
     */
    @Test(expected = RuntimeException.class)
    public void testSubmitEcWithoutApproval2() {
        Integer interfaceFlag = InterfaceStatus.APPROVING.getCode();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        ApprovalRecord approvalRecord = initApprovalRecord(1L);

        EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
        ecApprovalDetail.setEntId(1L);
        ecApprovalDetail.setCallbackUrl("test");

        List<String> ips = new ArrayList<String>();

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);
        Mockito.when(ecApprovalDetailService.insert(any(EcApprovalDetail.class))).thenReturn(true);

        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.insertIps(anyList(), anyLong())).thenReturn(true);

        Mockito.when(entCallbackAddrService.delete(anyLong())).thenReturn(true);
        Mockito.when(entCallbackAddrService.insert(any(EntCallbackAddr.class))).thenReturn(false);

        assertFalse(approvalRequestService.submitEcWithoutApproval(interfaceFlag, approvalRequest, approvalRecord,
                ecApprovalDetail, ips));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService).updateByPrimaryKeySelective(any(Enterprise.class));
        Mockito.verify(ecApprovalDetailService).insert(any(EcApprovalDetail.class));

        Mockito.verify(entWhiteListService).deleteByEntId(anyLong());
        Mockito.verify(entWhiteListService).insertIps(anyList(), anyLong());

        Mockito.verify(entCallbackAddrService).delete(anyLong());
        Mockito.verify(entCallbackAddrService).insert(any(EntCallbackAddr.class));
    }

    /**
     * submitEcWithoutApproval
     * 分支4：抛异常
     */
    @Test
    public void testSubmitEcWithoutApproval3() {
        Integer interfaceFlag = InterfaceStatus.APPROVING.getCode();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        ApprovalRecord approvalRecord = initApprovalRecord(1L);

        EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
        ecApprovalDetail.setEntId(1L);
        ecApprovalDetail.setCallbackUrl("test");

        List<String> ips = new ArrayList<String>();

        Enterprise enterprise = new Enterprise();
        enterprise.setInterfaceFlag(InterfaceStatus.APPROVING.getCode());

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true)
                .thenReturn(false);
        Mockito.when(ecApprovalDetailService.insert(any(EcApprovalDetail.class))).thenReturn(true);

        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.insertIps(anyList(), anyLong())).thenReturn(true);

        Mockito.when(entCallbackAddrService.delete(anyLong())).thenReturn(true);
        Mockito.when(entCallbackAddrService.insert(any(EntCallbackAddr.class))).thenReturn(true);

        Mockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise);
        Mockito.when(enterprisesService.createAppkey(anyLong())).thenReturn(false).thenReturn(true);

        try {
            approvalRequestService.submitEcWithoutApproval(interfaceFlag, approvalRequest, approvalRecord,
                    ecApprovalDetail, ips);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true)
                .thenReturn(false);
        try {
            approvalRequestService.submitEcWithoutApproval(interfaceFlag, approvalRequest, approvalRecord,
                    ecApprovalDetail, ips);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Mockito.verify(approvalRequestMapper, Mockito.times(2)).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService, Mockito.times(2)).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService, Mockito.times(3)).updateByPrimaryKeySelective(any(Enterprise.class));
        Mockito.verify(ecApprovalDetailService, Mockito.times(2)).insert(any(EcApprovalDetail.class));

        Mockito.verify(entWhiteListService, Mockito.times(2)).deleteByEntId(anyLong());
        Mockito.verify(entWhiteListService, Mockito.times(2)).insertIps(anyList(), anyLong());

        Mockito.verify(entCallbackAddrService, Mockito.times(2)).delete(anyLong());
        Mockito.verify(entCallbackAddrService, Mockito.times(2)).insert(any(EntCallbackAddr.class));

        Mockito.verify(enterprisesService, Mockito.times(2)).selectByPrimaryKey(anyLong());
        Mockito.verify(enterprisesService, Mockito.times(2)).createAppkey(anyLong());
    }

    /**
     * submitEcWithoutApproval
     * 正常流程
     */
    @Test
    public void testSubmitEcWithoutApproval4() {
        Integer interfaceFlag = InterfaceStatus.APPROVING.getCode();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        ApprovalRecord approvalRecord = initApprovalRecord(1L);

        EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
        ecApprovalDetail.setEntId(1L);
        ecApprovalDetail.setCallbackUrl("test");

        List<String> ips = new ArrayList<String>();

        Enterprise enterprise = new Enterprise();
        enterprise.setInterfaceFlag(InterfaceStatus.APPROVING.getCode());

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);
        Mockito.when(ecApprovalDetailService.insert(any(EcApprovalDetail.class))).thenReturn(true);

        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.insertIps(anyList(), anyLong())).thenReturn(true);

        Mockito.when(entCallbackAddrService.delete(anyLong())).thenReturn(true);
        Mockito.when(entCallbackAddrService.insert(any(EntCallbackAddr.class))).thenReturn(true);

        Mockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise);
        Mockito.when(enterprisesService.createAppkey(anyLong())).thenReturn(true);

        assertTrue(approvalRequestService.submitEcWithoutApproval(interfaceFlag, approvalRequest, approvalRecord,
                ecApprovalDetail, ips));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService, Mockito.times(2)).updateByPrimaryKeySelective(any(Enterprise.class));
        Mockito.verify(ecApprovalDetailService).insert(any(EcApprovalDetail.class));

        Mockito.verify(entWhiteListService).deleteByEntId(anyLong());
        Mockito.verify(entWhiteListService).insertIps(anyList(), anyLong());

        Mockito.verify(entCallbackAddrService).delete(anyLong());
        Mockito.verify(entCallbackAddrService).insert(any(EntCallbackAddr.class));

        Mockito.verify(enterprisesService).selectByPrimaryKey(anyLong());
        Mockito.verify(enterprisesService).createAppkey(anyLong());
    }

    /**
     * submitEcWithoutApproval
     * 正常流程
     */
    @Test
    public void testSubmitEcWithoutApproval5() {
        Integer interfaceFlag = InterfaceStatus.OPEN.getCode();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        ApprovalRecord approvalRecord = initApprovalRecord(1L);

        EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
        ecApprovalDetail.setEntId(1L);
        ecApprovalDetail.setCallbackUrl("test");

        List<String> ips = new ArrayList<String>();

        Enterprise enterprise = new Enterprise();
        enterprise.setInterfaceFlag(InterfaceStatus.OPEN.getCode());

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(any(Enterprise.class))).thenReturn(true);
        Mockito.when(ecApprovalDetailService.insert(any(EcApprovalDetail.class))).thenReturn(true);

        Mockito.when(entWhiteListService.deleteByEntId(anyLong())).thenReturn(true);
        Mockito.when(entWhiteListService.insertIps(anyList(), anyLong())).thenReturn(true);

        Mockito.when(entCallbackAddrService.delete(anyLong())).thenReturn(true);
        Mockito.when(entCallbackAddrService.insert(any(EntCallbackAddr.class))).thenReturn(true);

        Mockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise);

        assertTrue(approvalRequestService.submitEcWithoutApproval(interfaceFlag, approvalRequest, approvalRecord,
                ecApprovalDetail, ips));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(enterprisesService, Mockito.times(2)).updateByPrimaryKeySelective(any(Enterprise.class));
        Mockito.verify(ecApprovalDetailService).insert(any(EcApprovalDetail.class));

        Mockito.verify(entWhiteListService).deleteByEntId(anyLong());
        Mockito.verify(entWhiteListService).insertIps(anyList(), anyLong());

        Mockito.verify(entCallbackAddrService).delete(anyLong());
        Mockito.verify(entCallbackAddrService).insert(any(EntCallbackAddr.class));

        Mockito.verify(enterprisesService).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testQueryApprovalRequestsForEcChange() {
        QueryObject queryObject = new QueryObject();
        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        Mockito.when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcess);
        Mockito.when(approvalRequestMapper.queryApprovalRequestsForEcChange(anyMap())).thenReturn(
                new ArrayList<ApprovalRequest>());
        assertNotNull(approvalRequestService.queryApprovalRequestsForEcChange(queryObject));
        Mockito.verify(approvalProcessDefinitionService).selectByType(anyInt());
        Mockito.verify(approvalRequestMapper).queryApprovalRequestsForEcChange(anyMap());
    }

    @Test
    public void testCountApprovalRequestsForEcChange() {
        QueryObject queryObject = new QueryObject();
        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        Mockito.when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcess);
        Mockito.when(approvalRequestMapper.countApprovalRequestsForEcChange(anyMap())).thenReturn(1L);
        assertEquals(1, approvalRequestService.countApprovalRequestsForEcChange(queryObject).longValue());
        Mockito.verify(approvalProcessDefinitionService).selectByType(anyInt());
        Mockito.verify(approvalRequestMapper).countApprovalRequestsForEcChange(anyMap());
    }

    @Test
    public void testSelectByEntIdAndProcessType() {
        Long entId = 1L;
        Integer type = 1;
        assertNull(approvalRequestService.selectByEntIdAndProcessType(entId, null));
        Mockito.when(approvalRequestMapper.selectByEntIdAndProcessType(anyMap())).thenReturn(
                new ArrayList<ApprovalRequest>());
        assertNotNull(approvalRequestService.selectByEntIdAndProcessType(entId, type));
        Mockito.verify(approvalRequestMapper).selectByEntIdAndProcessType(anyMap());
    }

    /**
     * 0:删除产品；1：增加产品；2：变更产品
     */
    private ProductChangeDetail initProductChangeDetail(int type) {
        ProductChangeDetail record = new ProductChangeDetail();
        record.setOperate(type);
        return record;
    }

    private ApprovalRecord initApprovalRecord(Long id) {
        ApprovalRecord approvalRecord = new ApprovalRecord();
        approvalRecord.setId(id);
        return approvalRecord;
    }

    private ApprovalRequest initApprovalRequest(Long id) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setId(1L);
        approvalRequest.setEntId(1L);
        return approvalRequest;
    }

    private HistoryEnterprises initNomalEnterprises() {
        HistoryEnterprises he = new HistoryEnterprises();
        he.setEntId(1L);
        he.setName("ent");
        he.setEntName("eee");
        he.setCode("280280");
        he.setDiscount(1L);
        he.setPhone("18867100000");
        he.setEmail("ent@163.com");
        he.setLicenceStartTime(new Date());
        he.setLicenceEndTime(new Date());
        he.setEndTime(new Date());
        he.setStartTime(new Date());
        he.setUpdateTime(new Date());
        he.setDeleteFlag(0);
        return he;
    }

    @Test
    public void testSubmitApprovalForActivity1() {
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        Mockito.when(approvalRequestMapper.insert(approvalRequest)).thenReturn(0);
        assertFalse(approvalRequestService.submitApprovalForActivity(approvalRequest, null, null));
        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
    }

    @Test(expected = RuntimeException.class)
    public void testSubmitApprovalForActivity2() {
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        ApprovalRecord approvalRecord = initApprovalRecord(1L);

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(false);
        assertFalse(approvalRequestService.submitApprovalForActivity(approvalRequest, approvalRecord, null));
    }

    @Test(expected = RuntimeException.class)
    public void testSubmitApprovalForActivity3() {
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        ApprovalRecord approvalRecord = initApprovalRecord(1L);
        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setId(1L);

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(activityApprovalDetailService.insert(any(ActivityApprovalDetail.class))).thenReturn(true);
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);
        assertFalse(approvalRequestService.submitApprovalForActivity(approvalRequest, approvalRecord,
                activityApprovalDetail));
    }

    @Test
    public void testSubmitApprovalForActivity4() {
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        ApprovalRecord approvalRecord = initApprovalRecord(1L);
        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setId(1L);

        Mockito.when(approvalRequestMapper.insertSelective(approvalRequest)).thenReturn(1);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);
        Mockito.when(activityApprovalDetailService.insert(any(ActivityApprovalDetail.class))).thenReturn(true);
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(true);
        assertTrue(approvalRequestService.submitApprovalForActivity(approvalRequest, approvalRecord,
                activityApprovalDetail));
        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
        Mockito.verify(activityApprovalDetailService).insert(any(ActivityApprovalDetail.class));
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
    }

    @Test
    public void testGetApprovalRequestForMdrcActive() {
        Mockito.when(approvalRequestMapper.getApprovalRequestForMdrcActive(anyMap())).thenReturn(
                new ArrayList<ApprovalRequest>());
        assertNotNull(approvalRequestService.getApprovalRequestForMdrcActive(null));
        Mockito.verify(approvalRequestMapper).getApprovalRequestForMdrcActive(anyMap());
    }

    @Test
    public void testCountApprovalRequestForMdrcActive() {
        Mockito.when(approvalRequestMapper.countApprovalRequestForMdrcActive(anyMap())).thenReturn(1L);
        assertEquals(1L, approvalRequestService.countApprovalRequestForMdrcActive(null).longValue());
        Mockito.verify(approvalRequestMapper).countApprovalRequestForMdrcActive(anyMap());
    }

    @Test(expected = RuntimeException.class)
    public void testSubmitApprovalForMdrcActive() {
        assertFalse(approvalRequestService.submitApprovalForMdrcActive(null, null));

        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        MdrcActiveDetail mdrcActiveDetail = createMdrcActiveDetail();

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(mdrcActiveDetailService.insertSelective(any(MdrcActiveDetail.class))).thenReturn(false);
        assertFalse(approvalRequestService.submitApprovalForMdrcActive(approvalRequest, mdrcActiveDetail));
    }

    @Test(expected = RuntimeException.class)
    public void testSubmitApprovalForMdrcActive1() {
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        MdrcActiveDetail mdrcActiveDetail = createMdrcActiveDetail();

        approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
        approvalRequest.setId(null);

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(mdrcActiveDetailService.insertSelective(any(MdrcActiveDetail.class))).thenReturn(true);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(false);

        assertFalse(approvalRequestService.submitApprovalForMdrcActive(approvalRequest, mdrcActiveDetail));
    }

    @Test
    public void testSubmitApprovalForMdrcActive2() {
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        MdrcActiveDetail mdrcActiveDetail = createMdrcActiveDetail();

        approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
        approvalRequest.setId(null);

        Mockito.when(approvalRequestMapper.insertSelective(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(mdrcActiveDetailService.insertSelective(any(MdrcActiveDetail.class))).thenReturn(true);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);

        assertTrue(approvalRequestService.submitApprovalForMdrcActive(approvalRequest, mdrcActiveDetail));

        Mockito.verify(approvalRequestMapper).insertSelective(any(ApprovalRequest.class));
        Mockito.verify(mdrcActiveDetailService).insertSelective(any(MdrcActiveDetail.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    @Test
    public void testEditApprovalForMdrcActive() {
        MdrcActiveDetail mdrcActiveDetail = createMdrcActiveDetail();
        mdrcActiveDetail.setRequestId(null);
        assertFalse(approvalRequestService.editApprovalForMdrcActive(null, mdrcActiveDetail));

        mdrcActiveDetail.setRequestId(1L);
        ApprovalRequest approvalRequest = initApprovalRequest(1L);

        Mockito.when(approvalRequestMapper.updateApprovalRequest(any(ApprovalRequest.class))).thenReturn(0);
        assertFalse(approvalRequestService.editApprovalForMdrcActive(approvalRequest, mdrcActiveDetail));

        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
    }

    @Test(expected = RuntimeException.class)
    public void testEditApprovalForMdrcActive1() {
        MdrcActiveDetail mdrcActiveDetail = createMdrcActiveDetail();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        Mockito.when(approvalRequestMapper.updateApprovalRequest(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(mdrcActiveDetailService.updateByRequestIdSelective(any(MdrcActiveDetail.class))).thenReturn(false);
        assertFalse(approvalRequestService.editApprovalForMdrcActive(approvalRequest, mdrcActiveDetail));
    }

    @Test(expected = RuntimeException.class)
    public void testEditApprovalForMdrcActive2() {
        MdrcActiveDetail mdrcActiveDetail = createMdrcActiveDetail();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
        approvalRequest.setId(null);

        Mockito.when(approvalRequestMapper.updateApprovalRequest(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(mdrcActiveDetailService.updateByRequestIdSelective(any(MdrcActiveDetail.class))).thenReturn(true);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(false);

        assertFalse(approvalRequestService.editApprovalForMdrcActive(approvalRequest, mdrcActiveDetail));
    }

    @Test
    public void testEditApprovalForMdrcActive3() {
        MdrcActiveDetail mdrcActiveDetail = createMdrcActiveDetail();
        ApprovalRequest approvalRequest = initApprovalRequest(1L);
        approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
        approvalRequest.setId(null);

        Mockito.when(approvalRequestMapper.updateApprovalRequest(any(ApprovalRequest.class))).thenReturn(1);
        Mockito.when(mdrcActiveDetailService.updateByRequestIdSelective(any(MdrcActiveDetail.class))).thenReturn(true);
        Mockito.when(approvalRecordService.insertApprovalRecord(any(ApprovalRecord.class))).thenReturn(true);

        assertTrue(approvalRequestService.editApprovalForMdrcActive(approvalRequest, mdrcActiveDetail));

        Mockito.verify(approvalRequestMapper).updateApprovalRequest(any(ApprovalRequest.class));
        Mockito.verify(mdrcActiveDetailService).updateByRequestIdSelective(any(MdrcActiveDetail.class));
        Mockito.verify(approvalRecordService).insertApprovalRecord(any(ApprovalRecord.class));
    }

    private MdrcActiveDetail createMdrcActiveDetail() {
        MdrcActiveDetail mdrcActiveDetail = new MdrcActiveDetail();
        mdrcActiveDetail.setRequestId(1L);
        mdrcActiveDetail.setId(1L);
        return mdrcActiveDetail;
    }

    @Test
    public void testApprovalForMdrcCardmake() {

    }

    @Test
    public void testGetRecords() {
        List<ApprovalRequest> list = new ArrayList<ApprovalRequest>();
        QueryObject queryObject = new QueryObject();
        Mockito.when(approvalRequestMapper.getRecords(anyMap())).thenReturn(list);
        assertNotNull(approvalRequestService.getRecords(queryObject));
    }

    @Test
    public void testCountRecords() {
        QueryObject queryObject = new QueryObject();
        Long result = 10L;
        Mockito.when(approvalRequestMapper.countRecords(anyMap())).thenReturn(result);
        assertNotNull(approvalRequestService.countRecords(queryObject));
    }
    
    @Test
    public void testIsOverAuth(){
        assertTrue(approvalRequestService.isOverAuth(null, null));
        
        when(managerService.getManagerByAdminId(Mockito.any(Long.class))).thenReturn(null);
        assertTrue(approvalRequestService.isOverAuth(1L, 1L));
       
        when(approvalRequestMapper.getById(Mockito.any(Long.class))).thenReturn(null);
        assertTrue(approvalRequestService.isOverAuth(1L, 1L));

        
        Manager fatherManager = new Manager();
        ApprovalRequest request = new ApprovalRequest();
        request.setCreatorId(1L);
        when(managerService.isProOrCityOrMangerOrEnt(Mockito.any(Long.class))).thenReturn(true);
        when(managerService.getManagerByAdminId(Mockito.any(Long.class))).thenReturn(fatherManager);
        when(approvalRequestMapper.getById(Mockito.any(Long.class))).thenReturn(request);
        when(managerService.isParentManage(Mockito.any(Long.class),Mockito.any(Long.class))).thenReturn(true);
        assertFalse(approvalRequestService.isOverAuth(1L, 1L));
        
        when(managerService.isParentManage(Mockito.any(Long.class),Mockito.any(Long.class))).thenReturn(false);
        assertTrue(approvalRequestService.isOverAuth(1L, 1L));
    }

    /**
     * querySdAccountChangeRecord
     */
    @Test
    public void querySdAccountChangeRecord() {
        QueryObject queryObject = new QueryObject();
        Long adminId = 1L;
        Administer admin = new Administer();
        admin.setId(adminId);
        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        enterprises.add(new Enterprise());
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setId(1L);
        
        Integer approvalType = ApprovalType.Account_Change_Approval.getCode();
        assertNotNull(approvalRequestService.querySdAccountChangeRecord(queryObject, null, approvalType));
        assertNotNull(approvalRequestService.querySdAccountChangeRecord(queryObject, adminId, null));
        
        queryObject.getQueryCriterias().put("endTime", "2017-09-07");
        Mockito.when(enterprisesService.getAllEnterpriseListByAdminId(Mockito.any(Administer.class))).thenReturn(enterprises);
        Mockito.when(approvalProcessDefinitionService.selectByType(Mockito.anyInt())).thenReturn(approvalProcessDefinition);
        Mockito.when(approvalRequestMapper.exportSDAccountChangeRecords(Mockito.anyMap()))
            .thenReturn(new ArrayList<SdAccApprovalRequest>());
        
        assertNotNull(approvalRequestService.querySdAccountChangeRecord(queryObject, adminId, approvalType));
        
        //error
        enterprises = new ArrayList<Enterprise>();
        Mockito.when(enterprisesService.getAllEnterpriseListByAdminId(Mockito.any(Administer.class))).thenReturn(enterprises);
        assertNotNull(approvalRequestService.querySdAccountChangeRecord(queryObject, adminId, approvalType));
        
        Mockito.when(enterprisesService.getAllEnterpriseListByAdminId(Mockito.any(Administer.class))).thenReturn(null);
        assertNotNull(approvalRequestService.querySdAccountChangeRecord(queryObject, adminId, approvalType));
        
        Mockito.when(administerService.selectAdministerById(adminId)).thenReturn(null);
        assertNotNull(approvalRequestService.querySdAccountChangeRecord(queryObject, adminId, approvalType));
        
        assertNotNull(approvalRequestService.querySdAccountChangeRecord(new QueryObject(), adminId, approvalType));
    }

    
    /** 
    * @Title: testGetMakecardRecordsOrderBy 
    * @Description: 
    * @throws 
    */
    //@Test
    public void testGetMakecardRecordsOrderBy(){
        Mockito.when(approvalRequestMapper.getMakecardRecordsOrderBy(Mockito.anyMap())).thenReturn(new ArrayList<ApprovalRequest>());
        assertNull(approvalRequestService.getMakecardRecordsOrderBy(Mockito.any(QueryObject.class)));
    }
    /** 
    * @Title: testGetApprovalRequests 
    * @Description: 
    * @throws 
    */
    @Test
    public void testGetApprovalRequests() {
        List<ApprovalRequest> list = new ArrayList<ApprovalRequest>();
        Mockito.when(approvalRequestMapper.getApprovalRequests(Mockito.anyLong(), Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(list);
        assertEquals(list, approvalRequestService.getApprovalRequests(new Long(1), new Integer(1), new Integer(1)));

    }
}

package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MobileBlackList;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MobileBlackListService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.province.service.SendMsgService;

/**
 * 
 * @author Administrator
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ApprovalRequestSmsServiceImplTest {
    
    @InjectMocks
    ApprovalRequestSmsServiceImpl approvalRequestSmsService= new ApprovalRequestSmsServiceImpl(); 
    
    @Mock
    ApprovalRequestService approvalRequestService;
    
    @Mock
    ApprovalDetailDefinitionService approvalDetailDefinitionService;
    
    @Mock
    ApprovalRecordService approvalRecordService;
    
    @Mock
    AdminManagerService adminManagerService;
    
    @Mock
    ManagerService managerService;
    
    @Mock
    EntManagerService entManagerService;
    
    @Mock
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    
    @Mock
    RoleService roleService;
    
    @Mock
    AdministerService administerService;
    
    @Mock
    EnterprisesService enterprisesService;
    
    @Mock
    SendMsgService sendMsgService;
    
    @Mock
    MobileBlackListService mobileBlackListService;
    
    /**
     * testSendNoticeSms
     */
    @Test
    public void testSendNoticeSms(){
        ApprovalRequestSmsServiceImpl service = Mockito.spy(approvalRequestSmsService);
        
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setProcessId(1L);
        approvalRequest.setStatus(1);
        approvalRequest.setResult(0);
        
        Mockito.when(approvalRequestService.selectByPrimaryKey(1L)).thenReturn(approvalRequest);
        
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setType(0);
        approvalProcessDefinition.setMsg(1);
        approvalProcessDefinition.setRecvmsg(1);
        
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(approvalProcessDefinition);
        
        List<ApprovalDetailDefinition> defis = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition defi1 = new ApprovalDetailDefinition();
        ApprovalDetailDefinition defi2 = new ApprovalDetailDefinition();
        ApprovalDetailDefinition defi3 = new ApprovalDetailDefinition();
        defis.add(defi1);
        defis.add(defi2);
        defis.add(defi3);
        
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(1L)).thenReturn(defis);
        
        Mockito.doReturn(1).when(service).getLevelByRequestStatus(approvalRequest.getStatus());
        
        List<ApprovalRecord> approvalRecords = new ArrayList<ApprovalRecord>();
        Mockito.when(approvalRecordService.selectByRequestId(approvalRequest.getId())).thenReturn(approvalRecords);
        List<Administer> listAdmins = new ArrayList<Administer>();
        
        Mockito.doReturn(listAdmins).when(service).getSmsAdmins(Mockito.anyLong(),Mockito.anyLong(),Mockito.anyList());
        Mockito.doReturn(true).when(service).sendNoticeSmsToAll(Mockito.anyList(),Mockito.any(ApprovalType.class));
        Mockito.doReturn(true).when(service).sendProcessSms(Mockito.anyBoolean(), Mockito.anyInt(), 
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ApprovalType.class), Mockito.anyLong(), Mockito.anyLong());
        
        Assert.assertTrue(service.sendNoticeSms(1L));
        
        Mockito.doReturn(3).when(service).getLevelByRequestStatus(approvalRequest.getStatus());
        Assert.assertFalse(service.sendNoticeSms(1L));
        
        Mockito.doReturn(null).when(service).getLevelByRequestStatus(approvalRequest.getStatus());
        Assert.assertFalse(service.sendNoticeSms(1L));
        
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(1L)).thenReturn(null);
        Assert.assertFalse(service.sendNoticeSms(1L));
        
        approvalProcessDefinition.setMsg(0);
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(approvalProcessDefinition);
        Assert.assertFalse(service.sendNoticeSms(1L)); 
        
        approvalProcessDefinition.setType(100);
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(approvalProcessDefinition);
        Assert.assertFalse(service.sendNoticeSms(1L)); 
        
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(null);
        Assert.assertFalse(service.sendNoticeSms(1L)); 
        
        Mockito.when(approvalRequestService.selectByPrimaryKey(1L)).thenReturn(null);
        Assert.assertFalse(service.sendNoticeSms(1L)); 
    }
    
    /**
     * testSendNoticeSms2
     */
    @Test
    public void testSendNoticeSms2(){
        ApprovalRequestSmsServiceImpl service = Mockito.spy(approvalRequestSmsService);
        
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setProcessId(1L);
        approvalRequest.setStatus(1);
        approvalRequest.setResult(1);
        
        Mockito.when(approvalRequestService.selectByPrimaryKey(1L)).thenReturn(approvalRequest);
        
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setType(0);
        approvalProcessDefinition.setMsg(1);
        
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(approvalProcessDefinition);
        
        List<ApprovalDetailDefinition> defis = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition defi1 = new ApprovalDetailDefinition();
        ApprovalDetailDefinition defi2 = new ApprovalDetailDefinition();
        ApprovalDetailDefinition defi3 = new ApprovalDetailDefinition();
        defis.add(defi1);
        defis.add(defi2);
        defis.add(defi3);
        
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(1L)).thenReturn(defis);
        
        Mockito.doReturn(1).when(service).getLevelByRequestStatus(approvalRequest.getStatus());
        
        List<ApprovalRecord> approvalRecords = new ArrayList<ApprovalRecord>();
        Mockito.when(approvalRecordService.selectByRequestId(approvalRequest.getId())).thenReturn(approvalRecords);
        List<Administer> listAdmins = new ArrayList<Administer>();
        
        Mockito.doReturn(listAdmins).when(service).getSmsAdmins(Mockito.anyLong(),Mockito.anyLong(),Mockito.anyList());
        Mockito.doReturn(true).when(service).sendNoticeSmsToAll(Mockito.anyList(),Mockito.any(ApprovalType.class));
        Mockito.doReturn(true).when(service).sendProcessSms(Mockito.anyBoolean(), Mockito.anyInt(), 
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ApprovalType.class), Mockito.anyLong(), Mockito.anyLong());
        
        Assert.assertTrue(service.sendNoticeSms(1L));
    }
    
    /**
     * testSendNoticeSms
     */
    @Test
    public void testSendNoticeSms3(){
        ApprovalRequestSmsServiceImpl service = Mockito.spy(approvalRequestSmsService);
        
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setProcessId(1L);
        approvalRequest.setStatus(0);
        approvalRequest.setResult(0);
        
        Mockito.when(approvalRequestService.selectByPrimaryKey(1L)).thenReturn(approvalRequest);
        
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setType(0);
        approvalProcessDefinition.setMsg(0);
        approvalProcessDefinition.setRecvmsg(0);
        
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(approvalProcessDefinition);
        
        List<ApprovalDetailDefinition> defis = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition defi1 = new ApprovalDetailDefinition();
        ApprovalDetailDefinition defi2 = new ApprovalDetailDefinition();
        ApprovalDetailDefinition defi3 = new ApprovalDetailDefinition();
        defis.add(defi1);
        defis.add(defi2);
        defis.add(defi3);
        
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(1L)).thenReturn(defis);
        
        Mockito.doReturn(1).when(service).getLevelByRequestStatus(approvalRequest.getStatus());
        
        List<ApprovalRecord> approvalRecords = new ArrayList<ApprovalRecord>();
        Mockito.when(approvalRecordService.selectByRequestId(approvalRequest.getId())).thenReturn(approvalRecords);
        List<Administer> listAdmins = new ArrayList<Administer>();
        
        Mockito.doReturn(listAdmins).when(service).getSmsAdmins(Mockito.anyLong(),Mockito.anyLong(),Mockito.anyList());
        Mockito.doReturn(true).when(service).sendNoticeSmsToAll(Mockito.anyList(),Mockito.any(ApprovalType.class));
        Mockito.doReturn(true).when(service).sendProcessSms(Mockito.anyBoolean(), Mockito.anyInt(), 
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ApprovalType.class), Mockito.anyLong(), Mockito.anyLong());
        
        Assert.assertTrue(service.sendNoticeSms(1L));
        
        Mockito.doReturn(3).when(service).getLevelByRequestStatus(approvalRequest.getStatus());
        Assert.assertFalse(service.sendNoticeSms(1L));
        
        Mockito.doReturn(null).when(service).getLevelByRequestStatus(approvalRequest.getStatus());
        Assert.assertFalse(service.sendNoticeSms(1L));
        
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(1L)).thenReturn(null);
        Assert.assertFalse(service.sendNoticeSms(1L));
        
        approvalProcessDefinition.setMsg(0);
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(approvalProcessDefinition);
        Assert.assertFalse(service.sendNoticeSms(1L)); 
        
        approvalProcessDefinition.setType(100);
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(approvalProcessDefinition);
        Assert.assertFalse(service.sendNoticeSms(1L)); 
        
        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(1L)).thenReturn(null);
        Assert.assertFalse(service.sendNoticeSms(1L)); 
        
        Mockito.when(approvalRequestService.selectByPrimaryKey(1L)).thenReturn(null);
        Assert.assertFalse(service.sendNoticeSms(1L)); 
    }
    
    /**
     * testSendNoticeSms2
     */
    @Test
    public void testgetSmsAdmins(){
        ApprovalRequestSmsServiceImpl service = Mockito.spy(approvalRequestSmsService);
        List<Administer> allAdmins = new ArrayList<Administer>();
        Administer admin1 = new Administer();
        admin1.setId(1L);
        Administer admin2 = new Administer();
        admin2.setId(4L);
        allAdmins.add(admin1);
        allAdmins.add(admin2);
        
        List<ApprovalRecord> approvalRecords = new ArrayList<ApprovalRecord>();
        ApprovalRecord record1 = new ApprovalRecord();
        record1.setCreatorId(1L);
        record1.setOperatorId(2L);
        approvalRecords.add(record1);
        ApprovalRecord record3 = new ApprovalRecord();
        record3.setCreatorId(1L);
        record3.setOperatorId(3L);
        approvalRecords.add(record3);
        
        Mockito.doReturn(allAdmins).when(service).getRelatedAdminsByEntIdRoleId(1L,1L);
        Assert.assertNotNull(service.getSmsAdmins(1L, 1L, approvalRecords));
        
    }
    
    /**
     * testSendNoticeSms2
     */
    @Test
    public void testgetRelatedAdminsByEntIdRoleId(){
        ApprovalRequestSmsServiceImpl service = Mockito.spy(approvalRequestSmsService);
        Mockito.when(entManagerService.getManagerIdForEnter(1L)).thenReturn(1L);
        Mockito.when(managerService.getParentNodeByRoleId(1L, 1L)).thenReturn(new Manager());
        Mockito.when(adminManagerService.getAdminByManageIds(Mockito.anyList())).thenReturn(new ArrayList<Administer>());
        
        Assert.assertNotNull(service.getRelatedAdminsByEntIdRoleId(1L, 1L));
        
        Mockito.when(managerService.getParentNodeByRoleId(1L, 1L)).thenReturn(null);
        Assert.assertNotNull(service.getRelatedAdminsByEntIdRoleId(1L, 1L));
    }
    
    /**
     * testSendNoticeSms2
     */
    @Test
    public void testsendNoticeSmsToAll(){
        ApprovalRequestSmsServiceImpl service = Mockito.spy(approvalRequestSmsService);
        Mockito.doReturn(false).when(service).isMobileInBlack(Mockito.anyString(),Mockito.any(ApprovalType.class));
        List<Administer> list = new ArrayList<Administer>();
        Administer administer = new Administer();
        administer.setMobilePhone("18867102087");
        list.add(administer);
        Assert.assertNotNull(service.sendNoticeSmsToAll(list, ApprovalType.Activity_Approval));
        
        Mockito.doReturn(true).when(service).isMobileInBlack(Mockito.anyString(),Mockito.any(ApprovalType.class));
        Assert.assertNotNull(service.sendNoticeSmsToAll(list, ApprovalType.Activity_Approval));
    }
    
    /**
     * testSendNoticeSms2
     */
    @Test
    public void testsendProcessSms(){
        ApprovalRequestSmsServiceImpl service = Mockito.spy(approvalRequestSmsService);
        Mockito.doReturn(false).when(service).isMobileInBlack(Mockito.anyString(),Mockito.any(ApprovalType.class));
        Role currentRole = new Role();
        currentRole.setName("1");
        
        Role nextRole = new Role();
        nextRole.setName("2");
        
        Administer admin = new Administer();
        admin.setMobilePhone("18867102087");
        
        Enterprise enterprise = new Enterprise();
        enterprise.setName("1");
        
        Mockito.when(roleService.getRoleById(1L)).thenReturn(currentRole);
        Mockito.when(roleService.getRoleById(2L)).thenReturn(nextRole);
        Mockito.when(administerService.selectAdministerById(1L)).thenReturn(admin);
        Mockito.when(enterprisesService.selectById(1L)).thenReturn(enterprise);
        Assert.assertNotNull(service.sendProcessSms(true, 0, 1L, 1L, ApprovalType.Activity_Approval, 1L, 2L));
        Assert.assertNotNull(service.sendProcessSms(false, 0, 1L, 1L, ApprovalType.Activity_Approval, 1L, 2L));
        
        Mockito.when(roleService.getRoleById(2L)).thenReturn(null);
        Assert.assertNotNull(service.sendProcessSms(true, 0, 1L, 1L, ApprovalType.Activity_Approval, 1L, 2L));
        
        Mockito.when(roleService.getRoleById(1L)).thenReturn(null);
        Assert.assertNotNull(service.sendProcessSms(true, 0, 1L, 1L, ApprovalType.Activity_Approval, 1L, 2L));
        
        
        Mockito.doReturn(true).when(service).isMobileInBlack(Mockito.anyString(),Mockito.any(ApprovalType.class));
        Assert.assertNotNull(service.sendProcessSms(true, 0, 1L, 1L, ApprovalType.Activity_Approval, 1L, 2L));
    }
    
    /**
     * 
     */
    @Test
    public void testgetLevelByRequestStatus(){
        approvalRequestSmsService.getLevelByRequestStatus(0);
        approvalRequestSmsService.getLevelByRequestStatus(1);
        approvalRequestSmsService.getLevelByRequestStatus(3);
        approvalRequestSmsService.getLevelByRequestStatus(7);
        approvalRequestSmsService.getLevelByRequestStatus(15);
        approvalRequestSmsService.getLevelByRequestStatus(5);
    }
    
    /**
     * 
     */
    @Test
    public void testisMobileInBlack(){
        ApprovalRequestSmsServiceImpl service = Mockito.spy(approvalRequestSmsService);
        
        Mockito.doReturn(false).when(service).isSmsBlack(Mockito.anyString(), Mockito.anyString());
        
        Assert.assertNotNull(service.isMobileInBlack("18867102087", ApprovalType.Ec_Approval));
        Assert.assertNotNull(service.isMobileInBlack("18867102087", ApprovalType.Enterprise_Approval));
        Assert.assertNotNull(service.isMobileInBlack("18867102087", ApprovalType.MDRC_MakeCard_Approval));
        
        Mockito.doReturn(true).when(service).isSmsBlack(Mockito.anyString(), Mockito.anyString());
        Assert.assertNotNull(service.isMobileInBlack("18867102087", ApprovalType.Ec_Approval));
        Assert.assertNotNull(service.isMobileInBlack("18867102087", ApprovalType.Enterprise_Approval));
        Assert.assertNotNull(service.isMobileInBlack("18867102087", ApprovalType.MDRC_MakeCard_Approval));
    }
    
    /**
     * 
     */
    @Test
    public void testisSmsBlack(){
        Mockito.when(mobileBlackListService.getByMobileAndType(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        Assert.assertNotNull(approvalRequestSmsService.isSmsBlack("1", "1"));
        
        Mockito.when(mobileBlackListService.getByMobileAndType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(new MobileBlackList());
        Assert.assertNotNull(approvalRequestSmsService.isSmsBlack("1", "1"));
    }
}

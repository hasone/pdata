package com.cmcc.vrp.province.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.ApprovalRequestSmsService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MobileBlackListService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.province.service.SendMsgService;

/**
 * 审核流程 - 发短信类服务类
 * 
 * qihang
 *
 */
@Service
public class ApprovalRequestSmsServiceImpl implements ApprovalRequestSmsService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalRequestSmsServiceImpl.class);
    
    /**
     * 所有将要审核用户收到
     * 【平台名称】您有企业【审批名称】任务待处理。
     */
    private static final String NOTICE_SMS = "{0}您有{1}任务待处理 ";
    
    /**
     * 发起人收到
     * 【平台名称】【企业名称】的【审批名称】，办理人：【角色名称】，审批结果：【审批结果】；下一办理人：【角色名称】。
     */
    private static final String PROCCESS_UNFINI_SMS = "{0}{1}的{2}，办理人：{3}，审批结果：{4}；下一办理人：{5}。";
    
    /**
     * 发起人收到
     * 【平台名称】【企业名称】的【审批名称】，办理人：【角色名称】，审批结果【审批结果】，流程结束。
     */
    private static final String PROCCESS_FINI_SMS = "{0}{1}的{2}，办理人：{3}，审批结果: {4}，流程结束。 ";

    @Autowired
    ApprovalRequestService approvalRequestService;
    
    @Autowired
    ApprovalDetailDefinitionService approvalDetailDefinitionService;
    
    @Autowired
    ApprovalRecordService approvalRecordService;
    
    @Autowired
    AdminManagerService adminManagerService;
    
    @Autowired
    ManagerService managerService;
    
    @Autowired
    EntManagerService entManagerService;
    
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    
    @Autowired
    RoleService roleService;
    
    @Autowired
    AdministerService administerService;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    SendMsgService sendMsgService;
    
    @Autowired
    MobileBlackListService mobileBlackListService;

    
    /**
     * sendNoticeSms
     * 触发点为提交且更新了相关审核之后
     * 
     * 实现流程
     * 1. 找到ApprovalRequest 得到ProcessId 和 entId,status，result
     * 2. 找到所有 approval_detail_definition(根据ProcessId),按顺序排列
     * 3. 发短信
     *    (1) result = 0,status = 0 发送短信(1)
     *    (2) result = 0,status > 0 发送短信(1) 和 (2)
     *    (3) result != 0, status != 0 发送短信(3)
     * 4. 如果需要发送短信(1)(2) result = 0 ,则
     *    (1) 根据ApprovalRequest的status 找到 相应approval_detail_definition的roleId
     *    (2) 根据roleId和entId 找到所有manager和admin  
     *    (3) 发送(1)时，找到approvalRecords 已发送过的成员不会再次发送
     *    (4) 发送(2)时，找到最新   approvalRecord ，填充相关企业，类型，操作人
     * 5. 发送短信(3)时    参考(2)
     */
    @Override
    public boolean sendNoticeSms(Long requestId) {
        //流程1.
        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);
        if(approvalRequest == null){
            LOGGER.error("无法找到相关ApprovalRequest，id=" + requestId);
            return false;
        }
        
        //获取ApprovalType 审批类型
        ApprovalType approvalType;
        ApprovalProcessDefinition approvalProcessDefinition =approvalProcessDefinitionService.
                selectByPrimaryKey(approvalRequest.getProcessId());
        if(approvalProcessDefinition == null || 
                (approvalType=ApprovalType.getByCode(approvalProcessDefinition.getType())) == null){
            LOGGER.error("无法找到相关ApprovalProcessDefinition，processId =" + approvalRequest.getProcessId());
            return false;
        }
        
        /**
         * 1.短信状态，是否需要发送
         */
       /* if(new Integer(0).equals(approvalProcessDefinition.getMsg())){
            LOGGER.info("approvalProcessDefinition的短信状态为0，无需发送短信.Id={}" , approvalProcessDefinition.getId());
            return true;
        }*/
        
        //流程2.
        List<ApprovalDetailDefinition> defis = approvalDetailDefinitionService.getByApprovalProcessId(approvalRequest.getProcessId());
        if(defis == null){
            LOGGER.error("无法找到ApprovalDetailDefinition,processId = " + approvalRequest.getProcessId());
            return false;
        }
            
        //流程3.
        Integer level = getLevelByRequestStatus(approvalRequest.getStatus()); //得到当前的审批级数
        if(level == null){
            LOGGER.error("approvalRequest的status出错,Id={},status={}",approvalRequest.getId(),approvalRequest.getStatus());
            return false;
        }
        if(approvalRequest.getResult()==null || approvalRequest.getResult().equals(0)){ //需要继续审核
            
            if(level >= defis.size()){
                LOGGER.error("找到的ApprovalDetailDefinition个数为{}，当前status为{},数据错误",defis.size(),approvalRequest.getStatus());
                return false;
            }
            
            //发送短信(1)全流程
            //得到下次审核对应的ApprovalDetailDefinition,得到roleId
            ApprovalDetailDefinition nextDefinition = defis.get(level);
            
            //得到需要发送的管理员们(通过企业和下一次审核的角色),找到所有已审核记录,过滤其中的admin
            List<ApprovalRecord> approvalRecords =approvalRecordService.selectByRequestId(requestId);
            List<Administer> listAdmins = getSmsAdmins(approvalRequest.getEntId(),nextDefinition.getRoleId(),approvalRecords);
            
            if(new Integer(1).equals(approvalProcessDefinition.getRecvmsg())){
                sendNoticeSmsToAll(listAdmins,approvalType);
            }else{
                LOGGER.info("该次审批不会所有管理员不会收到短信.defId={}",approvalProcessDefinition.getId());
            }
            //发送短信(1)fini 
            
            //发送短信(2),需要status>0
            if(approvalRequest.getStatus() > 0){
                //获取当前审核的定义，获取角色
                ApprovalDetailDefinition currentDefinition = 
                        defis.get(level - 1);
                
                //发送短信(2)
                if(new Integer(1).equals(approvalProcessDefinition.getMsg())){
                    sendProcessSms(true,approvalRequest.getResult(),approvalRequest.getCreatorId(),approvalRequest.getEntId(),
                            approvalType,currentDefinition.getRoleId(),nextDefinition.getRoleId());
                }else{
                    LOGGER.info("该次审批流程发起者不会发送短信.defId={}",approvalProcessDefinition.getId());
                }
           
            }
            
        }else{ //不需要继续审核

            //发送(3)，类似(2)
            ApprovalDetailDefinition currentDefinition;
            if(approvalRequest.getResult().equals(1)){
                currentDefinition = defis.get(level - 1);
            }else{
                currentDefinition = defis.get(level);
            }
            if(new Integer(1).equals(approvalProcessDefinition.getMsg())){
                sendProcessSms(false,approvalRequest.getResult(),approvalRequest.getCreatorId(),approvalRequest.getEntId(),
                    approvalType,currentDefinition.getRoleId(),null);
            }else{
                LOGGER.info("该次审批流程发起者不会发送短信.defId={}"+approvalProcessDefinition.getId());
            }
        }
        
        LOGGER.info("发送短信给所有管理员完成");
        return true;
        
        
    }
    
    /**
     * 获取到相关需要发送短信的管理员 - 已发送过的管理员 - 发起者
     */
    protected List<Administer> getSmsAdmins(Long entId,Long roleId,List<ApprovalRecord> approvalRecords){
        List<Administer> allAdmins = getRelatedAdminsByEntIdRoleId(entId,roleId);
        List<Administer> needList = new ArrayList<Administer>();//记录需要发短信的用户
        //组装approvalRecords 中所有的operratorId
        Set<Long> listSendAdmins = new HashSet<Long>();//记录已发短信的用户
        for(ApprovalRecord record : approvalRecords){
            listSendAdmins.add(record.getOperatorId());
            listSendAdmins.add(record.getCreatorId());
        }
        
        for(Administer admin : allAdmins){
            if(!listSendAdmins.contains(admin.getId())){
                needList.add(admin);
            }
        }
        
        return needList;
    }
    
    
    /**
     * 通过企业Id和roleId得到父树相关管理员
     */
    protected List<Administer> getRelatedAdminsByEntIdRoleId(Long entId,Long roleId){
        Long managerId = entManagerService.getManagerIdForEnter(entId);
        //List<Manager> listManagers = managerService.getParentTreeByManangId(managerId);
        
        List<Manager> listManagers = new ArrayList<Manager>();
        Manager manager = managerService.getParentNodeByRoleId(managerId, roleId);
        if(manager != null){
            listManagers.add(manager);
        }
        return adminManagerService.getAdminByManageIds(listManagers);
    }
    
    
    /**
     * 发送短信NOTICE_SMS给所有的相关管理员
     */
    protected boolean sendNoticeSmsToAll(List<Administer> list,ApprovalType approvalType){
               
        for(Administer admin : list){
            String sms = MessageFormat.format(NOTICE_SMS, "流量平台",approvalType.getMessage());
            LOGGER.info("发送手机号[{}],短信内容:[{}]",admin.getMobilePhone(),sms);
            if(!isMobileInBlack(admin.getMobilePhone(),approvalType)){
                sendMsgService.sendMessage(admin.getMobilePhone(), sms, null);
            }
        }
        
        return true;
    }
    
    /**
     * 【平台名称】【企业名称】的【审批名称】，办理人：【角色名称】，审批结果：【审批结果】；下一办理人：【角色名称】。
     * 【平台名称】【企业名称】的【审批名称】，办理人：【角色名称】，审批结果【审批结果】，流程结束。
     */
    protected boolean sendProcessSms(Boolean hasNext,Integer result,Long adminId,Long entId,ApprovalType approvalType,Long currentRoleId,Long nextRoleId){
        Role currentRole = roleService.getRoleById(currentRoleId);
        
        Administer admin = administerService.selectAdministerById(adminId);
        
        Enterprise enterprise = enterprisesService.selectById(entId);
        
        if(currentRole == null || admin == null || enterprise == null){
            LOGGER.error("发送短信,数据库参数错误");
            return false;
        }
        
        
        String msg;
        if(hasNext){
            Role nextRole = roleService.getRoleById(nextRoleId);
            if(nextRole == null){
                LOGGER.error("发送短信,数据库参数错误");
                return false;
            }
            msg = MessageFormat.format(PROCCESS_UNFINI_SMS, "流量平台",enterprise.getName(),approvalType.getMessage(),currentRole.getName(),
                    "审核通过",nextRole.getName());
            
        }else{
            msg = MessageFormat.format(PROCCESS_FINI_SMS, "流量平台",enterprise.getName(),approvalType.getMessage(),currentRole.getName(),
                    result.equals(1)? "审核通过" : "已驳回");
        }

        LOGGER.info("发送手机号[{}],短信内容:[{}]",admin.getMobilePhone(),msg);
        if(!isMobileInBlack(admin.getMobilePhone(),approvalType)){
            sendMsgService.sendMessage(admin.getMobilePhone(), msg, null);
        }
        return true;
    }
    
    /**
     * 通过approvalRequest的审批status得到审批级数
     */
    protected Integer getLevelByRequestStatus(Integer status){
        switch (status) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 3:
                return 2;
            case 7:
                return 3;
            case 15:
                return 4;
            default:
                return null;
        }
    }
    
    /**
     * 
     * 审批类型：0，企业开户，1产品变更，2账户变更 ...
     * mobile_black_list :1、开户审批；2、Ec审批；3、平台短信验证码；4、充值到账；5、预警提醒；6、暂停值提醒；7、营销活动；8、初始化静态密码.
     * 
     * PS 根本对不上
     */
    public boolean isMobileInBlack(String mobile,ApprovalType approvalType){
        Boolean isInBlackList = false;
        if(approvalType.equals(ApprovalType.Ec_Approval)){
            isInBlackList = isSmsBlack(mobile, "2");
            if(isInBlackList){
                LOGGER.error("手机号{}在审批类型{}的黑名单中，将不会发送短信",mobile,"EC审批");
            }

        }else if(approvalType.equals(ApprovalType.Enterprise_Approval)){
            isInBlackList = isSmsBlack(mobile, "1");
            if(isInBlackList){
                LOGGER.error("手机号{}在审批类型{}的黑名单中，将不会发送短信",mobile,"开户审批");
            }

        }
        return isInBlackList;
        
    }
    
    /**
     * 1、开户审批；2、Ec审批；3、平台短信验证码；4、充值到账；5、预警提醒；6、暂停值提醒；7、营销活动；8、初始化静态密码
     */
    public boolean isSmsBlack(String mobile, String type) {
        if (mobileBlackListService.getByMobileAndType(mobile, type) != null) {
            return true;
        }
        return false;
    }
}


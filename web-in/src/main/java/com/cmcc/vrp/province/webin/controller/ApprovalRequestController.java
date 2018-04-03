package com.cmcc.vrp.province.webin.controller;

import static com.cmcc.vrp.enums.ApprovalType.Product_Change_Approval;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.InterfaceApprovalStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.enums.SchedulerType;
import com.cmcc.vrp.province.dao.CustomerTypeMapper;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.model.AccountChangeOperator;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.model.ActivityCreator;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.AdminChangeDetail;
import com.cmcc.vrp.province.model.AdminChangeOperator;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.CustomerType;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.EcApprovalDetail;
import com.cmcc.vrp.province.model.EcApprovalRequest;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.EntWhiteList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.HistoryEnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterprises;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductChangeDetail;
import com.cmcc.vrp.province.model.ProductChangeOperator;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.service.AccountChangeDetailService;
import com.cmcc.vrp.province.service.AccountChangeOperatorService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityApprovalDetailService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.AdminChangeDetailService;
import com.cmcc.vrp.province.service.AdminChangeOperatorService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.ApprovalRequestSmsService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.DiscountService;
import com.cmcc.vrp.province.service.EcApprovalDetailService;
import com.cmcc.vrp.province.service.EntApprovalRelatedBossService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EntWhiteListService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.HistoryEnterpriseFileService;
import com.cmcc.vrp.province.service.HistoryEnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.ProductChangeDetailService;
import com.cmcc.vrp.province.service.ProductChangeOperatorService;
import com.cmcc.vrp.province.service.ProductOnlineService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ProductTemplateService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.province.service.RuleTemplateService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.province.service.impl.GoldenEggServiceImpl;
import com.cmcc.vrp.province.service.impl.LotteryServiceImpl;
import com.cmcc.vrp.province.service.impl.RedpacketServiceImpl;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.MdrcMakeCardPojo;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.impl.GdZcBossServiceImpl;

/**
 * 审核控制器
 *
 * @author qinqinyan
 */
@Controller
@RequestMapping("manage/approval")
public class ApprovalRequestController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalRequestController.class);
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalDetailDefinitionService approvalDetailDefinitionService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    RoleService roleService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    AdministerService administerService;
    @Autowired
    EnterpriseFileService enterpriseFileService;
    @Autowired
    AccountService accountService;
    @Autowired
    ProductService productService;
    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    AccountChangeOperatorService accountChangeOperatorService;
    @Autowired
    AccountChangeDetailService accountChangeDetailService;
    @Autowired
    ProductChangeOperatorService productChangeOperatorService;
    @Autowired
    EntManagerService entManagerService;
    @Autowired
    ManagerService managerService;
    @Autowired
    DiscountService discountService;
    @Autowired
    GiveMoneyEnterService giveMoneyEnterService;
    @Autowired
    ProductChangeDetailService productChangeDetailService;
    @Autowired
    EntApprovalRelatedBossService entApprovalRelatedBossService;
    @Autowired
    ActivityApprovalDetailService activityApprovalDetailService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    RuleTemplateService ruleTemplateService;
    @Autowired
    HistoryEnterprisesService historyEnterprisesService;
    @Autowired
    HistoryEnterpriseFileService historyEnterpriseFileService;
    @Autowired
    EcApprovalDetailService ecApprovalDetailService;
    @Autowired
    EntWhiteListService entWhiteListService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    CustomerTypeMapper customerTypeMapper;
    @Autowired
    @Qualifier("lotteryService")
    LotteryServiceImpl lotteryService;
    @Autowired
    @Qualifier("goldenEggService")
    GoldenEggServiceImpl goldenEggService;
    @Autowired
    @Qualifier("redpacketService")
    RedpacketServiceImpl redpacketService;
    @Autowired
    MdrcActiveDetailService mdrcActiveDetailService;
    @Autowired
    MdrcTemplateService mdrcTemplateService;
    @Autowired
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Autowired
    MdrcCardmakerService mdrcCardmakerService;
    @Autowired
    ActivityTemplateService activityTemplateService;
    @Autowired
    EntSyncListService entSyncListService;
    @Autowired
    ProductOnlineService productOnlineService;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    @Autowired
    ProductTemplateService productTemplateService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AdminChangeOperatorService adminChangeOperatorService;
    @Autowired
    private AdminChangeDetailService adminChangeDetailService;
    @Autowired
    MdrcBatchConfigInfoService MdrcBatchConfigInfoService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    S3Service s3Service;
    @Autowired
    XssService xssService;
    @Autowired
    private EntProductService entProductService;
    @Autowired
    private DiscountMapper discountMapper;
    @Autowired
    private ApprovalRequestSmsService approvalRequestSmsService;
    @Autowired
    ActivityCreatorService activityCreatorService;

    /**
     * @Title: approvalIndex
     * @Description:
     */
    @RequestMapping("approvalIndex")
    public String approvalIndex(ModelMap modelmap, QueryObject queryObject, HttpServletRequest request,
            Integer approvalType) {
        modelmap.addAttribute("ApprovalType", ApprovalType.toMap());// 传入审批类型
        modelmap.addAttribute("approvalType", approvalType);// 传入当前审批类型
        modelmap.addAttribute("ApprovalRequestStatus", ApprovalRequestStatus.toMap());// 传入审核的所有状态

        if (queryObject != null) {//传入是否返回页面标识
            modelmap.addAttribute("back", queryObject.getBack());
        }

        // test 初始化admin
        // Administer admin = init();
        // 暂时注释
        Administer admin = getCurrentUser();
        if (admin == null) {
            return getLoginAddress();
        }

        Manager manager = getCurrentUserManager();

        Role role = roleService.getRoleById(manager.getRoleId());

        modelmap.addAttribute("currUserId", admin.getId());
        modelmap.addAttribute("managerId", manager.getId());
        modelmap.addAttribute("roleId", manager.getRoleId());
        modelmap.addAttribute("roleName", role.getName());

        if (approvalType.toString().equals(ApprovalType.Enterprise_Approval.getCode().toString())) {
            // 企业审核
            return "entApproval/index.ftl";
        } else if (approvalType.toString().equals(Product_Change_Approval.getCode().toString())) {
            // 产品审核
            Boolean authFlag = approvalProcessDefinitionService.hasAuthToSubmitApproval(manager.getRoleId(),
                    ApprovalType.Product_Change_Approval.getCode());
            modelmap.addAttribute("authFlag", authFlag.toString());
            if ("chongqing".equals(getProvinceFlag())) {
                return "redirect:/cq/product/index.html";
            } else {
                return "productChange/approvalIndex.ftl";
            }
        } else if (approvalType.toString().equals(ApprovalType.Account_Change_Approval.getCode().toString())) {
            // 充值审核
            return "account_change/accountApprovalIndex.ftl";
        } else if (approvalType.toString().equals(ApprovalType.Activity_Approval.getCode().toString())) {
            // 活动审核
            return "activityApproval/index.ftl";
        } else if (approvalType.toString().equals(ApprovalType.Ent_Information_Change_Approval.getCode().toString())) {
            return "entInfoChangeApproval/index.ftl";
        } else if (approvalType.toString().equals(ApprovalType.MDRC_Active_Approval.getCode().toString())) {
            // 营销卡激活审核
            return "mdrcActiveApproval/approvalIndex.ftl";
        } else if (approvalType.toString().equals(ApprovalType.MDRC_MakeCard_Approval.getCode().toString())) {
            // 营销卡制卡审核
            return "mdrcMakeCardApproval/approvalIndex.ftl";
        } else if (approvalType.toString().equals(ApprovalType.Admin_Change_Approval.getCode().toString())) {
            // 用户修改审批
            return "adminChange/approvalIndex.ftl";
        } else if (approvalType.toString().equals(ApprovalType.Account_Min_Change_Approval.getCode().toString())) {
            //企业最小值变更审批
            return "account_change/minIndex.ftl";

        } else if (approvalType.toString().equals(ApprovalType.Account_Alert_Change_Approval.getCode().toString())) {
            //企业账户预警值变更审批 
            return "account_change/alertIndex.ftl";

        } else if (approvalType.toString().equals(ApprovalType.Account_Stop_Change_Approval.getCode().toString())) {
            //企业账户暂停值变更审批 
            return "account_change/stopIndex.ftl";

        } else {
            // 山东显示EC审批记录
            String provinceFlag = getProvinceFlag();
            if (org.apache.commons.lang.StringUtils.isNotBlank(provinceFlag) && "sd".equalsIgnoreCase(provinceFlag)) {
                modelmap.put("isShowECRecord", true);
            }
            return "entEcApproval/index.ftl";
        }
    }

    /**
     * 查找
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res, Long managerId, Long roleId,
            Integer approvalType, Long currentUserId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        Manager currentManager = getCurrentUserManager();
        /**
         * 查询参数
         */
        setQueryParameter("name", queryObject);
        setQueryParameter("code", queryObject);
        setQueryParameter("result", queryObject);// 审批
        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime") + " 23:59:59");
        }

        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(approvalType);
        List<ApprovalDetailDefinition> approvalDetails = approvalDetailDefinitionService
                .getByApprovalProcessId(approvalProcessDefinition.getId());
        List<Enterprise> enterprises;

        if (approvalType.toString().equals(ApprovalType.Enterprise_Approval.getCode().toString())) {
            // 企业开户查询潜在客户企业status=1
            enterprises = enterprisesService.getPotentialEnterByManagerId(managerId);
        } else {
            enterprises = enterprisesService.getEnterByManagerId(managerId);
        }

        if (approvalType.toString().equals(ApprovalType.Activity_Approval.getCode().toString())
                || approvalType.toString().equals(ApprovalType.MDRC_Active_Approval.getCode().toString())
                || approvalType.toString().equals(ApprovalType.MDRC_MakeCard_Approval.getCode().toString())) {
            queryObject.getQueryCriterias().put("approvalType", approvalType.toString());
        }
        queryObject.getQueryCriterias().put("deleteFlag", 0);

        List<ApprovalRequest> totalApprovalRequests = null;
        Long totalCount = 0L;

        List<Integer> preconditions = new ArrayList<Integer>();
        for (ApprovalDetailDefinition approvalDetail : approvalDetails) {
            if (approvalDetail.getRoleId().toString().equals(roleId.toString())) {
                // 存在同一种角色可以进行多级审批：如省级管理员可能可以进行三级和四级审批
                preconditions.add(approvalDetail.getPrecondition());
            }
        }

        boolean isMakecardRole = false;
        if (roleId.toString().equals(getCardMaker().toString())) {
            isMakecardRole = true;
        }

        // 制卡审核时除了总部制卡专员外，其余用adminId实现层级关系限制
        if (approvalType.toString().equals(ApprovalType.MDRC_MakeCard_Approval.getCode().toString())) {
            List<Administer> creators = managerService.getChildAdminByCurrentManageId(currentManager.getId());
            queryObject.getQueryCriterias().put("creators", creators);
        }

        totalApprovalRequests = approvalRequestService.getApprovalRequestList(queryObject, enterprises,
                approvalProcessDefinition.getId(), preconditions, isMakecardRole);
        totalCount = approvalRequestService.countApprovalRequest(queryObject, enterprises,
                approvalProcessDefinition.getId(), preconditions, isMakecardRole);

        //同一个人不能连续两次审批一个记录
//        if (totalApprovalRequests != null) {
//            Iterator<ApprovalRequest> iter = totalApprovalRequests.iterator();
//            Long count = 0L;
//            while (iter.hasNext()) {
//                ApprovalRequest item = iter.next();
//                ApprovalRecord approvalRecord = approvalRecordService.selectNewRecordByRequestId(item.getId(),
//                        currentUserId);
//                if (approvalRecord == null) {
//                    iter.remove();
//                    count += 1;
//                }
//            }
//            totalCount -= count;
//        }

        // 同一个人不能审核自己发起的任务
        if (totalApprovalRequests != null) {
            Iterator<ApprovalRequest> iter = totalApprovalRequests.iterator();
            Long count = 0L;
            while (iter.hasNext()) {
                ApprovalRequest item = iter.next();
                if (currentUserId.toString().equals(item.getCreatorId().toString())) {
                    iter.remove();
                    count += 1;
                }
            }
            totalCount -= count;
        }

        if (totalApprovalRequests != null) {
            for (ApprovalRequest item : totalApprovalRequests) {

                item.setCanOperate("0");
                if (item.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
                    // 正在审核中
                    for (Integer precondition : preconditions) {
                        // 判断是否能进行审批
                        if (item.getStatus().intValue() == precondition.intValue()
                                && !item.getCreatorId().toString().equals(getCurrentUser().getId().toString())) {
                            ApprovalRecord record = approvalRecordService.selectNewRecordByRequestId(item.getId(),
                                    getCurrentUser().getId());
                            if (record != null) {
                                item.setCanOperate("1");
                            }
                        }
                    }
                }

                // 地区全称
                Manager manager = entManagerService.getManagerForEnter(item.getEntId());
                if (manager != null) {
                    String fullname = "";
                    item.setDistrictName(managerService.getFullNameByCurrentManagerId(fullname, manager.getParentId()));
                }

                if (approvalType.toString().equals(ApprovalType.Account_Change_Approval.getCode().toString())
                        || approvalType.toString()
                                .equals(ApprovalType.Account_Min_Change_Approval.getCode().toString())
                        || approvalType.toString().equals(
                                ApprovalType.Account_Alert_Change_Approval.getCode().toString())
                        || approvalType.toString().equals(
                                ApprovalType.Account_Stop_Change_Approval.getCode().toString())) {
                    AccountChangeDetail accountChangeDetail = accountChangeDetailService
                            .selectByRequestId(item.getId());
                    Product product = productService.get(accountChangeDetail.getProductId());
                    item.setProductName(product == null ? "" : product.getName());
                    item.setProductType(product == null ? null : product.getType());
                    item.setProductTypeName(product == null ? "" : ProductType.fromValue(product.getType().byteValue())
                            .getDesc());
                    item.setCount(accountChangeDetail.getCount());
                }

                // 活动审核显示的是企业管理员和联系方式
                if (approvalType.toString().equals(ApprovalType.Activity_Approval.getCode().toString())) {
                    //旧逻辑，通过表内的企业，找企业关键人
                    /*List<Administer> administers = administerService.selectEMByEnterpriseId(item.getEntId());
                    if (administers != null && administers.size() > 0) {
                        item.setManagerName(administers.get(0).getUserName());
                        item.setManagerPhone(administers.get(0).getMobilePhone());
                    }*/

                    //新逻辑，通过activity_id 找到activity,再找到activity_creator
                    Activities activities = activitiesService.selectByActivityId(item.getActivityId());
                    if (activities == null) {
                        item.setManagerName("-");
                        item.setManagerPhone("-");
                    } else {
                        ActivityCreator creator = activityCreatorService.getByActId(
                                ActivityType.fromValue(activities.getType()), activities.getId());
                        if (creator == null) {
                            item.setManagerName("-");
                            item.setManagerPhone("-");
                        } else {
                            item.setManagerName(creator.getUserName());
                            item.setManagerPhone(creator.getMobilePhone());
                        }
                    }
                }

                // 充值审核，产品变更审核，显示的是客户经理和联系方式
                if (approvalType.toString().equals(ApprovalType.Account_Change_Approval.getCode().toString())
                        || approvalType.toString().equals(Product_Change_Approval.getCode().toString())
                        || approvalType.toString()
                                .equals(ApprovalType.Account_Min_Change_Approval.getCode().toString())
                        || approvalType.toString().equals(
                                ApprovalType.Account_Alert_Change_Approval.getCode().toString())
                        || approvalType.toString().equals(
                                ApprovalType.Account_Stop_Change_Approval.getCode().toString())) {

                    // List<Administer> managers =
                    // adminManagerService.getCustomerManagerByEntId(item.getEntId());
                    // if(managers!=null && managers.size()>0){
                    // item.setManagerName(managers.get(0).getUserName());
                    // item.setManagerPhone(managers.get(0).getMobilePhone());
                    // }

                    // 获取客户经理角色
                    String customManagerID = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID
                            .getKey());
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(customManagerID)) {
                        Long customManagerId = Long.parseLong(customManagerID);
                        List<Manager> managers = managerService.selectEntParentNodeByEnterIdOrRoleId(item.getEntId(),
                                customManagerId);
                        if (managers != null && !managers.isEmpty()) {
                            List<Administer> administers = adminManagerService.getAdminByManageId(managers.get(0)
                                    .getId());
                            if (administers != null && !administers.isEmpty()) {
                                item.setManagerName(administers.get(0).getUserName());
                                item.setManagerPhone(administers.get(0).getMobilePhone());
                            }
                        }
                    }

                }
            }
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", totalApprovalRequests);
        json.put("total", totalCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: mdrcActiveApprovalDetail
     * @Description:
     */
    @RequestMapping("mdrcActiveApprovalDetail")
    public String mdrcActiveApprovalDetail(ModelMap modelmap, Long id, Integer canOperate) {
        Manager manager = getCurrentUserManager();
        if (id != null && manager != null && !approvalRequestService.isOverAuth(getCurrentAdminID(), id)) {
            // canOperate : 0 查看详情；1 审批
            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);

            if (approvalRequest != null && approvalRequest.getEntId() != null) {

                // 获取当前审批层级详情
                ApprovalDetailDefinition appprovalDetail = approvalDetailDefinitionService
                        .getCurrentApprovalDetailByProccessId(approvalRequest.getProcessId(),
                                approvalRequest.getStatus());

                // 校验审核记录是否已经被审核
                if ((canOperate == 1)
                        && (!approvalRequest.getResult().toString()
                                .equals(ApprovalRequestStatus.APPROVING.getCode().toString()) || !appprovalDetail
                                .getRoleId().toString().equals(manager.getRoleId().toString()))) {
                    modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                    modelmap.addAttribute("flag", ApprovalType.MDRC_Active_Approval.getCode().toString());
                    return "entApproval/error.ftl";
                }

                MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(approvalRequest.getId());

                // 审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);
                modelmap.addAttribute("opinions", approvalRecords);

                if (mdrcActiveDetail.getTemplateId() != null && mdrcActiveDetail.getProductId() != null
                        && mdrcActiveDetail.getEntId() != null && approvalRecords != null && approvalRecords.size() > 0) {

                    MdrcTemplate mdrcTemplate = mdrcTemplateService
                            .selectByPrimaryKey(mdrcActiveDetail.getTemplateId());
                    Product product = productService.selectProductById(mdrcActiveDetail.getProductId());
                    Enterprise enterprise = enterprisesService.selectById(mdrcActiveDetail.getEntId());
                    Administer administer = administerService.selectAdministerById(approvalRequest.getCreatorId());
                    modelmap.addAttribute("mdrcActiveDetail", mdrcActiveDetail);
                    modelmap.addAttribute("mdrcTemplate", mdrcTemplate);
                    modelmap.addAttribute("product", product);
                    modelmap.addAttribute("enterprise", enterprise);
                    modelmap.addAttribute("administer", administer);
                    // 待审核记录id
                    modelmap.addAttribute("approvalRecordId", approvalRecords.get(approvalRecords.size() - 1).getId());
                    // 审核请求记录id
                    modelmap.addAttribute("requestId", id);
                    // 审批流程id
                    modelmap.addAttribute("processId", approvalRequest.getProcessId());

                    String provinceFlag = getProvinceFlag();
                    modelmap.addAttribute("provinceFlag", provinceFlag);
                    modelmap.addAttribute("canOperate", canOperate);
                    return "mdrcActiveApproval/approvalDetail.ftl";

                }
            }
        }
        return "redirect:approvalIndex.html?approvalType=" + ApprovalType.MDRC_Active_Approval.getCode();
    }

    /**
     * 申请卡详情
     * 
     * @param requestId
     * @param map
     * @author qinqinyan
     */
    @RequestMapping("requestDetail")
    public String requestDetail(ModelMap map, Long id) {
        Manager manager = getCurrentUserManager();
        if (manager != null && id != null) {
            MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(id);
            if (mdrcCardmakeDetail != null) {
                // 判断是否有权限查看这个详情
                if (!managerService.managedByManageId(mdrcCardmakeDetail.getEnterpriseId(), manager.getId())) {
                    map.addAttribute("errorMsg", "对不起，您无权限查看该其企业制卡申请审核详情!");
                    return "error.ftl";
                }

                MdrcBatchConfigInfo mdrcBatchConfigInfo = MdrcBatchConfigInfoService
                        .selectByPrimaryKey(mdrcCardmakeDetail.getConfigInfoId());
                Enterprise enterprise = enterprisesService.selectById(mdrcCardmakeDetail.getEnterpriseId());
                Product product = productService.get(mdrcCardmakeDetail.getProductId());
                // 审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);
                map.addAttribute("opinions", approvalRecords);

                MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcCardmakeDetail.getTemplateId());
                // MdrcCardmaker mdrcCardmaker =
                // mdrcCardmakerService.selectByPrimaryKeyForshow(mdrcCardmakeDetail.getCardmakerId());

                String provinceFlag = getProvinceFlag();
                map.addAttribute("provinceFlag", provinceFlag);

                map.put("mdrcCardmakeDetail", mdrcCardmakeDetail);
                map.put("mdrcTemplate", mdrcTemplate);
                map.put("MdrcBatchConfigInfo", mdrcBatchConfigInfo);
                map.put("enterprise", enterprise);
                map.put("product", product);

                // 是否需要审核
                ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                        .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
                Boolean wheatherNeedApproval = true;
                if (approvalProcessDefinition.getStage().toString().equals("0")) {
                    wheatherNeedApproval = false;
                }
                map.addAttribute("wheatherNeedApproval", wheatherNeedApproval.toString());

                ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);
                map.put("approvalRequest", approvalRequest);

                DecimalFormat df = new DecimalFormat("#.00");
                map.put("totalPrice", df.format(mdrcCardmakeDetail.getCost() / 100.00));

                return "mdrcMakeCardApproval/requestDetail.ftl";
            }
        }
        map.addAttribute("errorMsg", "未查找到该激活申请!");
        return "error.ftl";
    }

    /**
     * 图片显示
     * 
     * @author qinqinyan
     */
    @RequestMapping(value = "/getImage")
    public void getImage(String type, String filename, HttpServletResponse response, HttpServletRequest request) {
        if (!StringUtils.isEmpty(filename)) { // "qrCode".equals(type) &&
            s3Service.getImageFromS3(response, filename, request);
        }
    }

    /**
     * 营销卡制卡审批页
     * 
     * @author qinqinyan
     */
    @RequestMapping("operateMakecardApproval")
    public String mdrcCardmakeApprovalDetail(ModelMap modelmap, Long id) {
        Manager currentManager = getCurrentUserManager();
        // if (!approvalRequestService.isOverAuth(getCurrentAdminID(), id))
        // {//是否横向越权操作
        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);

        if (approvalRequest != null && approvalRequest.getCreatorId() != null) {
            // 判断是否有权限查看这个详情
            if (!managerService.managedByManageId(approvalRequest.getEntId(), currentManager.getId())) {
                modelmap.addAttribute("errorMsg", "对不起，您无权限审批该其企业制卡申请记录!");
                return "error.ftl";
            }

            // 获取当前审批层级详情
            ApprovalDetailDefinition appprovalDetail = approvalDetailDefinitionService
                    .getCurrentApprovalDetailByProccessId(approvalRequest.getProcessId(), approvalRequest.getStatus());
            // 校验审核记录是否已经被审核
            if (!approvalRequest.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())
                    || !appprovalDetail.getRoleId().toString().equals(currentManager.getRoleId().toString())) {
                modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                modelmap.addAttribute("flag", ApprovalType.MDRC_MakeCard_Approval.getCode().toString());
                return "entApproval/error.ftl";
            }

            MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(id);
            modelmap.addAttribute("mdrcCardmakeDetail", mdrcCardmakeDetail);

            // 审批记录列表
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);
            modelmap.addAttribute("opinions", approvalRecords);

            if (mdrcCardmakeDetail != null && mdrcCardmakeDetail.getCardmakerId() != null
                    && mdrcCardmakeDetail.getTemplateId() != null && approvalRecords != null
                    && approvalRecords.size() > 0) {

                // 制卡商
                MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKeyForshow(mdrcCardmakeDetail
                        .getCardmakerId());
                // 营销模板
                MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcCardmakeDetail.getTemplateId());

                Administer administer = administerService.selectAdministerById(approvalRequest.getCreatorId());

                Manager manager = managerService.getManagerByAdminId(administer.getId());
                String roleName = adminRoleService.getRoleNameByAdminId(administer.getId());

                MdrcBatchConfigInfo mdrcBatchConfigInfo = MdrcBatchConfigInfoService
                        .selectByPrimaryKey(mdrcCardmakeDetail.getConfigInfoId());
                Enterprise enterprise = enterprisesService.selectById(mdrcCardmakeDetail.getEnterpriseId());
                Product product = productService.get(mdrcCardmakeDetail.getProductId());

                modelmap.addAttribute("mdrcCardmaker", mdrcCardmaker);
                modelmap.addAttribute("mdrcTemplate", mdrcTemplate);
                modelmap.addAttribute("administer", administer);
                modelmap.addAttribute("manager", manager);
                modelmap.addAttribute("roleName", roleName);
                modelmap.addAttribute("enterprise", enterprise);
                modelmap.addAttribute("product", product);
                modelmap.addAttribute("MdrcBatchConfigInfo", mdrcBatchConfigInfo);

                // 待审核记录id
                modelmap.addAttribute("approvalRecordId", approvalRecords.get(approvalRecords.size() - 1).getId());
                // 审核请求记录id
                modelmap.addAttribute("requestId", id);
                // 审批流程id
                modelmap.addAttribute("processId", approvalRequest.getProcessId());

                String provinceFlag = getProvinceFlag();
                modelmap.addAttribute("provinceFlag", provinceFlag);

                DecimalFormat df = new DecimalFormat("#.00");
                modelmap.put("totalPrice", df.format(mdrcCardmakeDetail.getCost() / 100.00));

                return "mdrcMakeCardApproval/approvalDetail.ftl";
            }
        }
        // }
        return "redirect:approvalIndex.html?approvalType=" + ApprovalType.MDRC_MakeCard_Approval.getCode();
    }

    @RequestMapping(value = "productDetail")
    public String productDetail(ModelMap modelMap, Long productId) {
        Product product = productService.get(productId);
        if (product == null) {
            modelMap.addAttribute("errorMsg", "未查找到该产品");
            return "error.ftl";
        }
        modelMap.addAttribute("product", product);
        return "platformProduct/detail.ftl";
    }

    /**
     * @param id
     *            审核请求id
     */
    @RequestMapping("productApprovalDetail")
    public String productApprovalDetail(ModelMap modelmap, HttpServletRequest request, Long id) {
        Manager manager = getCurrentUserManager();
        if (id != null && !approvalRequestService.isOverAuth(getCurrentAdminID(), id)) {

            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);

            if (approvalRequest != null && approvalRequest.getEntId() != null) {
                // 企业产品信息
                List<ProductChangeDetail> productChangeDetails = productChangeDetailService
                        .getProductChangeDetailsByRequestId(approvalRequest.getId());

                if (productChangeDetails != null) {
                    modelmap.addAttribute("productChangeDetails", productChangeDetails);

                    Enterprise enterprise = enterprisesService.selectByPrimaryKey(approvalRequest.getEntId());

                    // 地区全称
                    Manager entManager = entManagerService.getManagerForEnter(enterprise.getId());
                    if (entManager != null) {
                        String fullname = "";
                        enterprise.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                                entManager.getParentId()));
                    }

                    modelmap.addAttribute("enterprise", enterprise);

                    // 客户经理
                    // Administer customerManager =
                    // administerService.selectByMobilePhone(enterprise.getCmPhone());
                    Administer customerManager = null;
                    String customManagerID = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID
                            .getKey());
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(customManagerID)) {
                        Long customManagerId = Long.parseLong(customManagerID);
                        List<Manager> managers = managerService.selectEntParentNodeByEnterIdOrRoleId(
                                enterprise.getId(), customManagerId);
                        if (managers != null && !managers.isEmpty()) {
                            List<Administer> administers = adminManagerService.getAdminByManageId(managers.get(0)
                                    .getId());
                            if (administers != null && !administers.isEmpty()) {
                                customerManager = administers.get(0);
                            }
                        }
                    }
                    if (customerManager == null) {
                        customerManager = new Administer();
                    }
                    modelmap.addAttribute("customerManager", customerManager);

                    // 企业管理员
                    List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterprise.getId());
                    modelmap.addAttribute("enterpriseManagers", enterpriseManagers.get(0));

                    // 审批记录列表
                    List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);

                    if (!varifyRequestRecord(manager.getRoleId(), approvalRecords)) {
                        modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                        modelmap.addAttribute("flag", 1);
                        return "entApproval/error.ftl";
                    }

                    modelmap.addAttribute("opinions", approvalRecords);
                    // 待审核记录id
                    modelmap.addAttribute("approvalRecordId", approvalRecords.get(approvalRecords.size() - 1).getId());
                    // 审核请求记录id
                    modelmap.addAttribute("requestId", id);
                    // 审批流程id
                    modelmap.addAttribute("processId", approvalRequest.getProcessId());

                    modelmap.addAttribute("approvalType", Product_Change_Approval.getCode());
                    // 传递标识：是否是自营的企业
                    if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                        modelmap.addAttribute("flag", 1);
                    } else {
                        modelmap.addAttribute("flag", 0);
                    }

                    String provinceFlag = getProvinceFlag();
                    modelmap.addAttribute("provinceFlag", provinceFlag);

                    // 判断是否采用产品模板
                    Boolean useProductTemplate = getUseProductTemplate();
                    modelmap.addAttribute("useProductTemplate", useProductTemplate);
                    if (useProductTemplate) {
                        ProductChangeDetail productChangeDetail = productChangeDetails.get(0);
                        Long oldTemplateProductId = productChangeDetail.getOldProductTemplateId();
                        Long newTemplateProductId = productChangeDetail.getNewProductTemplateId();
                        modelmap.addAttribute("oldTemplateProductName", oldTemplateProductId == null ? "未采用产品模板"
                                : productTemplateService.selectByPrimaryKey(oldTemplateProductId).getName());
                        modelmap.addAttribute("newTemplateProductName", newTemplateProductId == null ? "未采用产品模板"
                                : productTemplateService.selectByPrimaryKey(newTemplateProductId).getName());

                    }
                    return "productChange/approvalDetail.ftl";
                }
                logger.info("数据库中没有相关企业Id: " + approvalRequest.getEntId());
            }
            logger.info("数据库中没有相关审批记录Id: " + id);
        }
        return "redirect:approvalIndex.html?approvalType=1";
    }

    /**
     * @param id
     *            审核请求id
     */
    @RequestMapping("entApprovalDetail")
    public String entApprovalDetail(ModelMap modelmap, HttpServletRequest request, Long id) {
        Manager manager = getCurrentUserManager();
        if (id != null && !approvalRequestService.isOverAuth(getCurrentAdminID(), id)) {

            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);

            if (approvalRequest != null && approvalRequest.getEntId() != null) {
                // 企业信息
                Enterprise enterprise = enterprisesService.selectByPrimaryKey(approvalRequest.getEntId());

                if (enterprise != null && enterprise.getCmEmail() != null) {
                    modelmap.addAttribute("hasEmail", "true");
                } else {
                    modelmap.addAttribute("hasEmail", "false");
                }

                if (enterprise != null) {
                    // 地区全称
                    Manager entManager = entManagerService.getManagerForEnter(enterprise.getId());
                    if (entManager != null) {
                        String fullname = "";
                        enterprise.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                                entManager.getParentId()));
                    }

                    modelmap.addAttribute("enterprise", enterprise);

                    // 客户经理
                    Administer customerManager = administerService.selectByMobilePhone(enterprise.getCmPhone());
                    if (customerManager == null) {
                        customerManager = new Administer();
                    }
                    modelmap.addAttribute("customerManager", customerManager);

                    // 企业管理员
                    List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterprise.getId());
                    modelmap.addAttribute("enterpriseManagers", enterpriseManagers);

                    // 审批记录列表
                    List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(
                            enterprise.getId(), ApprovalType.Enterprise_Approval.getCode());

                    modelmap.addAttribute("opinions", approvalRecords);
                    // 待审核记录id
                    List<ApprovalRecord> approvalRecordList = approvalRecordService.selectByRequestId(id);
                    if (!varifyRequestRecord(manager.getRoleId(), approvalRecordList)) {
                        modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                        modelmap.addAttribute("flag", 0);
                        return "entApproval/error.ftl";
                    }

                    modelmap.addAttribute("approvalRecordId", approvalRecordList.get(approvalRecordList.size() - 1)
                            .getId());
                    // 审核请求记录id
                    modelmap.addAttribute("requestId", id);
                    // 审批流程id
                    modelmap.addAttribute("processId", approvalRequest.getProcessId());

                    // 文件下载内容
                    EnterpriseFile ef = enterpriseFileService.selectByEntId(enterprise.getId());
                    if (ef != null) {
                        modelmap.addAttribute("businessLicence", ef.getBusinessLicence());
                        modelmap.addAttribute("authorization", ef.getAuthorizationCertificate());
                        modelmap.addAttribute("identification", ef.getIdentificationCard());
                        modelmap.addAttribute("identificationBack", ef.getIdentificationBack());
                        modelmap.addAttribute("customerfile", ef.getCustomerfileName());
                        modelmap.addAttribute("contract", ef.getContractName());
                        modelmap.addAttribute("image", ef.getImageName());
                    }

                    // 传递标识：是否是自营的企业
                    if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                        modelmap.addAttribute("flag", 1);
                        // 判断是否填写合作信息，如果未填写，合作时间、信用合度、客服说明、合作协议、审批截图不显示
                        if (ef.getCustomerfileName() == null && ef.getContractName() == null
                                && ef.getImageName() == null) {
                            modelmap.addAttribute("isCooperate", "false");
                        } else {
                            modelmap.addAttribute("isCooperate", "true");
                        }
                    } else {
                        modelmap.addAttribute("flag", 0);
                        modelmap.addAttribute("isCooperate", "true");
                    }

                    // 获取信用额度值
                    Account account = accountService.get(enterprise.getId(), productService.getCurrencyProduct()
                            .getId(), AccountType.ENTERPRISE.getValue());
                    if (account != null) {
                        modelmap.addAttribute("minCount", account.getMinCount() == 0 ? 0 : account.getMinCount()
                                / (-100.0));
                    }

                    String provinceFlag = getProvinceFlag();
                    modelmap.addAttribute("provinceFlag", provinceFlag);

                    if ("gd_mdrc".equals(getProvinceFlag())) {
                        if (needExtEntInfo()) {
                            modelmap.addAttribute("crowdfundingFlag", getIsCrowdfundingPlatform());
                            EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(enterprise.getId());
                            modelmap.put("extEntInfo", enterprisesExtInfo);
                        }

                        return "entApproval/approvalDetail_gd.ftl";
                    } else {
                        return "entApproval/approvalDetail.ftl";
                    }
                }
                logger.info("数据库中没有相关企业Id: " + approvalRequest.getEntId());
            }
            logger.info("数据库中没有相关审批记录Id: " + id);
        }
        return "redirect:approvalIndex.html?approvalType=0";
    }

    private boolean varifyRequestRecord(Long roleId, List<ApprovalRecord> approvalRecordList) {
        if (roleId == null || approvalRecordList == null || approvalRecordList.size() < 1) {
            return false;
        } else {
            ApprovalRecord varifyApprovalRecord = approvalRecordList.get(approvalRecordList.size() - 1);
            if (varifyApprovalRecord.getIsNew().toString().equals("0")) {
                return false;
            } else {
                ApprovalRequest currApprovalRequest = approvalRequestService.selectByPrimaryKey(varifyApprovalRecord
                        .getRequestId());
                // ApprovalProcessDefinition approvalProcess =
                // approvalProcessDefinitionService.getApprovalProcessById(currApprovalRequest.getProcessId());
                List<ApprovalDetailDefinition> approvalDetails = approvalDetailDefinitionService
                        .getByApprovalProcessId(currApprovalRequest.getProcessId());
                if (approvalDetails != null && approvalDetails.size() > 0) {
                    for (ApprovalDetailDefinition item : approvalDetails) {
                        if (item.getPrecondition().toString().equals(currApprovalRequest.getStatus().toString())) {
                            if (!item.getRoleId().toString().equals(roleId.toString())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * @Title: accountChangeDetail
     */
    @RequestMapping("accountChangeDetail")
    public String accountChangeDetail(ModelMap modelmap, HttpServletRequest request, Long id, Integer approvalType) {
        // String errorMsg = "";
        if (id != null && approvalType != null && !approvalRequestService.isOverAuth(getCurrentAdminID(), id)) {
            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);
            if (approvalRequest != null && approvalRequest.getEntId() != null) {
                // 充值请求记录
                modelmap.addAttribute("approvalRequest", approvalRequest);
                // 请求充值额度
                AccountChangeDetail accountChangeDetail = accountChangeDetailService.selectByRequestId(approvalRequest
                        .getId());
                modelmap.addAttribute("requestCount", accountChangeDetail.getCount());
                //                switch(accountChangeDetail.getDiscountType()){
                //                    case 0:
                //                        modelmap.addAttribute("discountType", "无");
                //                        break;
                //                    case 1:
                //                        modelmap.addAttribute("discountType", "存送比例");
                //                        break;
                //                    default:
                //                        modelmap.addAttribute("discountType", "无");
                //                        break;
                //                }
                //                modelmap.addAttribute("discountValue", accountChangeDetail.getDiscountValue().toString() + "%");

                if (accountChangeDetail.getDiscountType() != null) {
                    switch (accountChangeDetail.getDiscountType()) {
                    case 0:
                        modelmap.addAttribute("discountType", "无");
                        break;
                    case 1:
                        modelmap.addAttribute("discountType", "存送比例");
                        break;
                    default:
                        modelmap.addAttribute("discountType", "无");
                        break;
                    }
                } else {
                    modelmap.addAttribute("discountType", "无");
                }
                if (accountChangeDetail.getDiscountValue() != null) {
                    modelmap.addAttribute("discountValue", accountChangeDetail.getDiscountValue().toString() + "%");
                } else {
                    modelmap.addAttribute("discountValue", "0");
                }
                modelmap.addAttribute("provinceFlag", getProvinceFlag());
                // 企业信息
                Enterprise enterprise = enterprisesService.selectByPrimaryKey(approvalRequest.getEntId());
                if (enterprise == null) {
                    modelmap.addAttribute("errorMsg", "数据库中没有相关企业Id: " + approvalRequest.getEntId());
                    return "error.ftl";
                }

                Manager manager = entManagerService.getManagerForEnter(approvalRequest.getEntId());
                if (manager != null) {
                    String fullname = "";
                    enterprise.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                            manager.getParentId()));
                }

                modelmap.addAttribute("Enterprises", enterprise);

                // Administer customerManager =
                // administerService.selectByMobilePhone(enterprise.getCmPhone());
                Administer customerManager = null;
                // 获取客户经理角色
                String customManagerID = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
                if (org.apache.commons.lang.StringUtils.isNotEmpty(customManagerID)) {
                    Long customManagerId = Long.parseLong(customManagerID);
                    List<Manager> managers = managerService.selectEntParentNodeByEnterIdOrRoleId(enterprise.getId(),
                            customManagerId);
                    if (managers != null && !managers.isEmpty()) {
                        List<Administer> administers = adminManagerService.getAdminByManageId(managers.get(0).getId());
                        if (administers != null && !administers.isEmpty()) {
                            customerManager = administers.get(0);
                        }
                    }
                }
                if (customerManager == null) {
                    customerManager = new Administer();
                }
                modelmap.addAttribute("customerManager", customerManager);

                // 获得当前企业余额
                Account account = accountService.get(enterprise.getId(), accountChangeDetail.getProductId(),
                        AccountType.ENTERPRISE.getValue());
                Product product = null;
                if (account != null && (product = productService.get(accountChangeDetail.getProductId())) != null) {
                    account.setProductName(product.getName());
                    account.setProductType(product.getType());
                    account.setProductTypeName(ProductType.fromValue(product.getType().byteValue()).getDesc());
                    modelmap.addAttribute("account", account);
                }

                // 显示历史审批意见
                // 审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);

                if (!varifyRequestRecord(getCurrentUserManager().getRoleId(), approvalRecords)) {
                    modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                    modelmap.addAttribute("flag", 2);
                    return "entApproval/error.ftl";
                }

                modelmap.addAttribute("approvalRecords", approvalRecords);

                modelmap.addAttribute("approvalRecordId",
                        approvalRecords.size() > 0 ? (approvalRecords.get(approvalRecords.size() - 1).getId()) : "");
                // 审核请求记录id
                modelmap.addAttribute("requestId", id);
                // 审批流程id
                modelmap.addAttribute("processId", approvalRequest.getProcessId());
                //审批类型
                modelmap.addAttribute("approvalType", approvalType);
                return "account_change/accountChangeDetail.ftl";
            }
            logger.info("数据库中没有相关审批记录Id: " + id);
        }
        if (approvalType != null) {
            return "redirect:approvalIndex.html?approvalType=" + approvalType.toString();
        } else {
            return "redirect:approvalIndex.html?approvalType=" + "";
        }
    }

    /**
     * 活动审批页面
     *
     * @param id
     *            审批请求id
     * @author qinqinyan
     */
    @RequestMapping("activityApprovalDetail")
    public String activityApprovalDetail(ModelMap modelmap, HttpServletRequest request, Long id, Integer canOperate) {
        Manager manager = getCurrentUserManager();
        if (id != null && manager != null && !approvalRequestService.isOverAuth(getCurrentAdminID(), id)) {
            // 获取活动信息
            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);
            ActivityApprovalDetail activityApprovalDetail = activityApprovalDetailService.selectByRequestId(id);

            // 与当前活动相关的所有审批请求
            List<ApprovalRequest> approvalRequestList = approvalRequestService
                    .selectByActivityId(activityApprovalDetail.getActivityId());
            // 审批记录列表
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByApprovalRequests(approvalRequestList);
            modelmap.addAttribute("opinions", approvalRecords);
            // 待审核记录id
            List<ApprovalRecord> approvalRecordList = approvalRecordService.selectByRequestId(id);
            if ((canOperate == 1) && !varifyRequestRecord(manager.getRoleId(), approvalRecordList)) {
                modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                modelmap.addAttribute("flag", ApprovalType.Activity_Approval.getCode().toString());
                return "entApproval/error.ftl";
            }
            modelmap.addAttribute("approvalRecordId", approvalRecordList.get(approvalRecordList.size() - 1).getId());
            // 审核请求记录id
            modelmap.addAttribute("requestId", id);
            // 审批流程id
            modelmap.addAttribute("processId", approvalRequest.getProcessId());

            Enterprise enterprise = enterprisesService.selectByPrimaryKeyForActivity(approvalRequest.getEntId());
            modelmap.put("enterprise", enterprise);
            modelmap.put("canOperate", canOperate);
            Activities activity = activitiesService.selectByActivityId(activityApprovalDetail.getActivityId());
            if (activity != null) {
                modelmap.put("activity", activity);
                // 奖品信息
                List<ActivityPrize> prizes = activityPrizeService.getDetailByActivityId(activity.getActivityId());
                if (activity.getType().toString().equals(ActivityType.CROWD_FUNDING.getCode().toString())) {
                    CrowdfundingActivityDetail crowdfundingActivityDetail = crowdfundingActivityDetailService
                            .selectByActivityId(activity.getActivityId());
                    modelmap.put("prizes", prizes);
                    modelmap.put("crowdfundingActivityDetail", crowdfundingActivityDetail);
                    return "activityApproval/crowdfunding_activityDetail.ftl";
                } else {
                    ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityApprovalDetail
                            .getActivityId());
                    if (activityInfo != null) {
                        // 活动规模（估计）
                        modelmap.put("activityInfo", activityInfo);

                        setCqFlag(modelmap);

                        ActivityTemplate activityTemplate = null;
                        if (activity.getType().toString().equals(ActivityType.LOTTERY.getCode().toString())

                                || activity.getType().toString().equals(ActivityType.REDPACKET.getCode().toString())
                                || activity.getType().toString().equals(ActivityType.GOLDENBALL.getCode().toString())
                                || activity.getType().toString()
                                        .equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {

                            activityTemplate = activityTemplateService.selectByActivityId(activity.getActivityId());
                            modelmap.put("activityTemplate", activityTemplate);

                            if (activity.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
                                Product product = productService.selectProductById(prizes.get(0).getProductId());
                                if (product.getFlowAccountFlag().toString()
                                        .equals(Constants.FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().toString())) {
                                    // 虚拟产品
                                    modelmap.addAttribute("realProduct",
                                            productService.get(product.getFlowAccountProductId()));
                                } else {
                                    modelmap.addAttribute("realProduct", product);
                                }
                                modelmap.put("prize", prizes.get(0));
                            }
                            modelmap.put("prizes", JSONObject.toJSONString(lotteryService.sortActivityPrizes(prizes)));

                            return "activityApproval/activityDetail1.ftl";
                        }
                        modelmap.put("prizes", prizes);
                        return "activityApproval/activityDetail.ftl";
                    }
                }
            }
        }
        modelmap.addAttribute("errorMsg", "未查到该条审批记录详情！");
        return "error.ftl";
    }

    /**
     * @Title: saveAccountChangeApproval
     */
    @RequestMapping("saveAccountChangeApproval")
    public String saveAccountChangeApproval(HttpServletRequest request, ModelMap map, Long enterId, String comment,
            String approvalStatus, Long requestId, Long approvalRecordId, Long processId, Integer approvalType) {
        Administer currentUser = getCurrentUser();

        if (enterId == null || StringUtils.isEmpty(approvalStatus) || StringUtils.isEmpty(comment)
                || approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            map.addAttribute("errorMsg", "审批提交参数异常，审批失败！");
            return entApprovalDetail(map, request, requestId);
        }
        map.addAttribute("approvalType", approvalType);
        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;
        AccountChangeDetail accountChangeDetail = null;

        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Account_Change_Approval.getCode());
            return "entApproval/error.ftl";
        }
        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());
        Role nextApprovalRole = null;
        if (nextApprovalDetail != null) {
            nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
        }

        if ("1".equals(approvalStatus)) {
            // 当请求审核当前状态等于审核等级前置状态
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            if (nextApprovalDetail == null) {
                // 当前审批是最后一级，更新账户
                accountChangeDetail = accountChangeDetailService.selectByRequestId(requestId);
                accountChangeDetail.setEntId(enterId);
                accountChangeDetail.setChangeType(approvalType);
            } else {
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(), processId,
                        nextApprovalRole.getName());
            }
        } else {
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", 2);
            return "entApproval/error.ftl";
        }

        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);
        try {
            if (approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord, null, null, accountChangeDetail, null, null, null, null, null, null)) {
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());
                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                if (accountChangeDetail != null) {
                    logger.info("企业帐户余额增加成功. EntId:" + accountChangeDetail.getEntId() + " ,PrdId:"
                            + accountChangeDetail.getProductId() + " ,Count:" + accountChangeDetail.getCount()
                            + " ,SerialNum:" + accountChangeDetail.getSerialNum());
                }
                return "redirect:approvalIndex.html?approvalType=" + approvalType;
            }
        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败");
            return accountChangeDetail(map, request, requestId, approvalType);
        }
        return accountChangeDetail(map, request, requestId, approvalType);
    }

    /**
     * @param approvalStatus
     *            1通过 0驳回
     */
    @RequestMapping(value = "/saveApproval")
    public String saveApproval(HttpServletRequest request, ModelMap map, Long enterId, String comment,
            String approvalStatus, Long requestId, Long approvalRecordId, Long processId) {
        Administer currentUser = getCurrentUser();

        if (enterId == null || StringUtils.isEmpty(approvalStatus) || StringUtils.isEmpty(comment)
                || approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            map.addAttribute("errorMsg", "审批提交参数异常，审批失败！");
            return entApprovalDetail(map, request, requestId);
        }

        /**
         * 1、更新当前要审批的记录（两张表：request的当前状态，record的审批意见）
         * 2、如果审批通过，判断当前审批是否完结，如果未完结，创建一条新的审批记录；如果完结，则更新enterprises表
         */
        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;
        Enterprise enterprise = null;

        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(processId, approvalRequest.getStatus());

        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", 0);
            return "entApproval/error.ftl";
        }
        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());
        Role nextApprovalRole = null;
        if (nextApprovalDetail != null) {
            nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
        }

        if ("1".equals(approvalStatus)) {
            // 当请求审核当前状态等于审核等级前置状态
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            // 当前审批是最后一级，根据配置决定是否去同步渠道商的产品，并建立起渠道商产品、平台产品、企业的关系
            if (nextApprovalDetail == null && productOnlineService.isOnlineProduct(approvalRequest.getEntId())) {
                if (getIsCrowdfundingPlatform()) {
                    GdZcBossServiceImpl bossService = applicationContext.getBean("gdZcBossService",
                            GdZcBossServiceImpl.class);
                    EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(enterId);
                    if (enterprisesExtInfo == null) {
                        logger.info("审核记录id:" + approvalRecordId + "审核失败" + "企业扩展信息为空");
                        return entApprovalDetail(map, request, requestId);
                    }

                    if ("1".equals(enterprisesExtInfo.getJoinType().toString())
                            && !bossService.orderRelation(enterId, "0")) {
                        logger.info("审核记录id:" + approvalRecordId + "审核失败" + "大企业版消息通知失败");
                        return entApprovalDetail(map, request, requestId);
                    }
                }
                // 当前审批是最后一级，更新企业表
                try {
                    enterprise = initUpdateEnterprise(approvalRequest.getEntId(), approvalStatus);
                } catch (RuntimeException e) {
                    logger.info("审核记录id:" + approvalRecordId + "审核失败" + e.getMessage());
                    return entApprovalDetail(map, request, requestId);
                }
            } else {
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(), processId,
                        nextApprovalRole.getName());
            }
        } else {
            enterprise = initUpdateEnterprise(approvalRequest.getEntId(), approvalStatus);

            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", 0);
            return "entApproval/error.ftl";
        }

        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);

        try {

            if (approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord, enterprise, null, null, null, null, null, null, null, null)) {
                /*if ("1".equals(approvalStatus) && needApprovalNotice() && nextApprovalDetail != null) {
                    if (currentUser.getRoleId() != 6) {
                        Long mangerId = getCurrentUserManager().getId();

                        // Long fatherManagerId =
                        // managerService.selectByAdminId(currUserId).getParentId();
                        Long fatherManagerId = managerService.selectManagerIdByEntIdAndRoleId(mangerId,
                                nextApprovalRole.getRoleId());

                        // Long fatherManagerId =
                        // managerService.selectByAdminId(currentUser.getId()).getParentId();
                        List<Long> manageIds = new ArrayList<Long>();
                        manageIds.add(fatherManagerId);
                        List<Administer> administers = administerService.getByManageIds(manageIds);
                        for (Administer admininter : administers) {
                            String content = admininter.getUserName() + ", 您好！"
                                    + globalConfigService.get(GlobalConfigKeyEnum.LOGO_NAME.getKey())
                                    + "\"企业开户审批\"的申请等待您审批。";
                            if (!sendMsgService.sendMessage(admininter.getMobilePhone(), content,
                                    MessageType.APPROVAL_NOTICE.getCode())) {
                                logger.info("审批通知发送短信失败;手机号-" + admininter.getMobilePhone() + ", 审批人-"
                                        + admininter.getUserName());
                            }
                        }
                    } else {

                        List<Long> manageIds = new ArrayList<Long>();
                        manageIds.add(getCurrentUserManager().getId());
                        List<Administer> administers = administerService.getByManageIds(manageIds);

                        for (Administer admininter : administers) {
                            ApprovalRecord approvalRecord = approvalRecordService.selectNewRecordByRequestId(requestId,
                                    admininter.getId());
                            if (approvalRecord != null) {
                                String content = admininter.getUserName() + ", 您好！"
                                        + globalConfigService.get(GlobalConfigKeyEnum.LOGO_NAME.getKey())
                                        + "\"企业开户审批\"的申请等待您审批。";
                                if (!sendMsgService.sendMessage(admininter.getMobilePhone(), content,
                                        MessageType.APPROVAL_NOTICE.getCode())) {
                                    logger.info("审批通知发送短信失败;手机号-" + admininter.getMobilePhone() + ", 审批人-"
                                            + admininter.getUserName());
                                }
                            }

                        }
                    }
                }*/
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());

                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                return "redirect:approvalIndex.html?approvalType=0";
            }

        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败" + e.getMessage());
            return entApprovalDetail(map, request, requestId);
        }

        return entApprovalDetail(map, request, requestId);
    }

    /**
     * @Title: saveApprovalForMdrcCardmake
     * @Description: Long approvalRecordId, Long processId
     */
    @RequestMapping("saveApprovalForMdrcCardmake")
    public String saveApprovalForMdrcCardmake(ModelMap map, String comment, String approvalStatus, Long requestId) {
        Administer currentUser = getCurrentUser();
        Manager manager = getCurrentUserManager();

        if (manager == null || StringUtils.isEmpty(comment) || StringUtils.isEmpty(approvalStatus) || requestId == null) {
            map.addAttribute("flag", ApprovalType.MDRC_MakeCard_Approval.getCode().toString());
            map.addAttribute("errorMsg", "参数异常,提交审批意见失败!");
            return "entApproval/error.ftl";
        }

        comment = xssService.stripXss(comment);

        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;
        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

        if (approvalRequest == null) {
            logger.info("查到到的审批请求为空requestId=" + requestId);
            return mdrcCardmakeApprovalDetail(map, requestId);
        }

        // 校验这条记录是否允许审批
        if (!managerService.managedByManageId(approvalRequest.getEntId(), manager.getId())) {
            map.addAttribute("flag", ApprovalType.MDRC_MakeCard_Approval.getCode().toString());
            map.addAttribute("errorMsg", "对不起，您没有权限审批该条制卡申请记录!");
            return "entApproval/error.ftl";
        }

        // 获取待审核的记录
        Long approvalRecordId = approvalRecordService.getNewApprovalRecord(approvalRequest.getId());
        if (approvalRecordId == null) {
            map.addAttribute("flag", ApprovalType.MDRC_MakeCard_Approval.getCode().toString());
            map.addAttribute("errorMsg", "参数异常,提交审批意见失败!");
            return "entApproval/error.ftl";
        }

        if (!approvalRequest.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.MDRC_MakeCard_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(approvalRequest.getProcessId(), approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(approvalRequest.getProcessId(), approvalRequest.getStatus());

        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.MDRC_MakeCard_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }

        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());

        if (Constants.APPROVAL_RESULT.PASS.getResult().equals(approvalStatus)) {
            // 通过
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            updateApprovalRequest.setEntId(approvalRequest.getEntId());

            if (nextApprovalDetail != null) {
                // 不是最后一级审批
                Role nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(),
                        approvalRequest.getProcessId(), nextApprovalRole.getName());
            }

        } else {
            // 驳回
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.MDRC_MakeCard_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }

        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);

        try {
            if (approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord)) {
                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());
                if (newApprovalRecord == null && Constants.APPROVAL_RESULT.PASS.getResult().equals(approvalStatus)) {
                    logger.info("将生成卡数据任务扔到队列里 request = {} ...", updateApprovalRequest.getId());
                    MdrcMakeCardPojo mdrcMakeCardPojo = new MdrcMakeCardPojo();
                    mdrcMakeCardPojo.setAdminId(approvalRequest.getCreatorId());
                    mdrcMakeCardPojo.setRequestId(updateApprovalRequest.getId());
                    if (!taskProducer.produceMdrcMakeCard(mdrcMakeCardPojo)) {
                        logger.info("将生成卡数据任务扔到队列里失败,  request = {}", updateApprovalRequest.getId());
                    }
                }
                return "redirect:approvalIndex.html?approvalType="
                        + ApprovalType.MDRC_MakeCard_Approval.getCode().toString();
            }
        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败" + e.getMessage());
            return mdrcCardmakeApprovalDetail(map, requestId);
        }
        return mdrcCardmakeApprovalDetail(map, requestId);

    }

    /**
     * @param map
     * @param comment
     * @param approvalStatus
     * @param requestId
     * @param approvalRecordId
     * @param processId
     */
    @RequestMapping("saveApprovalForMdrcActive")
    public String saveApprovalForMdrcActive(ModelMap map, String comment, String approvalStatus, Long requestId,
            Long approvalRecordId, Long processId) {
        Administer currentUser = getCurrentUser();

        if (StringUtils.isEmpty(comment) && StringUtils.isEmpty(approvalStatus) && requestId == null
                && approvalRecordId == null && processId == null) {
            map.addAttribute("flag", ApprovalType.MDRC_Active_Approval.getCode().toString());
            map.addAttribute("errorMsg", "参数异常,提交审批意见失败!");
            return "entApproval/error.ftl";
        }

        comment = xssService.stripXss(comment);

        // 校验是否横向越权
        if (approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            map.addAttribute("flag", ApprovalType.MDRC_MakeCard_Approval.getCode().toString());
            map.addAttribute("errorMsg", "无权访问");
            return "entApproval/error.ftl";
        }

        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;
        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

        if (approvalRequest == null) {
            logger.info("查到到的审批请求为空requestId=" + requestId);
            return mdrcActiveApprovalDetail(map, requestId, 1);
        }

        if (!approvalRequest.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.MDRC_Active_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(processId, approvalRequest.getStatus());

        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.MDRC_Active_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }
        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());

        if (Constants.APPROVAL_RESULT.PASS.getResult().equals(approvalStatus)) {
            // 通过
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            updateApprovalRequest.setEntId(approvalRequest.getEntId());

            if (nextApprovalDetail == null) {
                // 这个要做什么呢？？？？？
                // mdrcActiveDetail =
                // mdrcActiveDetailService.selectByRequestId(requestId);
            } else {
                Role nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(), processId,
                        nextApprovalRole.getName());
            }

        } else {
            // 驳回
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.MDRC_Active_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }

        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);

        try {
            if (approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord)) {
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());
                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                return "redirect:approvalIndex.html?approvalType="
                        + ApprovalType.MDRC_Active_Approval.getCode().toString();
            }
        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败" + e.getMessage());
            return mdrcActiveApprovalDetail(map, requestId, 1);
        }
        return mdrcActiveApprovalDetail(map, requestId, 1);
    }

    /**
     * @param approvalStatus
     *            1通过 0驳回
     */
    @RequestMapping(value = "/saveApprovalProductChange")
    public String saveApprovalProductChange(HttpServletRequest request, ModelMap map, Long enterId, String comment,
            String approvalStatus, Long requestId, Long approvalRecordId, Long processId) {
        Administer currentUser = getCurrentUser();

        if (enterId == null || StringUtils.isEmpty(approvalStatus) || StringUtils.isEmpty(comment)
                || approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            map.addAttribute("errorMsg", "审批提交参数异常，审批失败！");
            return "productChange/approvalDetail.ftl";
        }

        /**
         * 1、更新当前要审批的记录（两张表：request的当前状态，record的审批意见）
         * 2、如果审批通过，判断当前审批是否完结，如果未完结，创建一条新的审批记录；如果完结，则更新enterprises表
         */
        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;
        List<ProductChangeDetail> productChangeDetails = null;
        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Product_Change_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }
        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());

        if (Constants.APPROVAL_RESULT.PASS.getResult().equals(approvalStatus)) {
            // 当请求审核当前状态等于审核等级前置状态
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            updateApprovalRequest.setEntId(enterId);
            if (nextApprovalDetail == null) {
                // 当前审批是最后一级，更新企业产品表
                productChangeDetails = productChangeDetailService.getProductChangeDetailsByRequestId(approvalRequest
                        .getId());

            } else {
                Role nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(), processId,
                        nextApprovalRole.getName());
            }
        } else {
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Product_Change_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }
        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);

        try {
            if (approvalRequestService.approvalForProductChange(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord, productChangeDetails)) {
                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());
                return "redirect:approvalIndex.html?approvalType=1";
            } else {
                logger.info("审核记录id:" + approvalRecordId + "审核失败");
            }
        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败.失败原因：" + e.getMessage());
        }
        return productApprovalDetail(map, request, requestId);
    }

    /**
     * @Title: saveActivityApproval
     */
    @RequestMapping("saveActivityApproval")
    public String saveActivityApproval(HttpServletRequest request, ModelMap map, Long enterId, String comment,
            String approvalStatus, Long requestId, Long approvalRecordId, Long processId) {
        Administer currentUser = getCurrentUser();
        if (currentUser == null) {
            return getLoginAddress();
        }

        comment = xssService.stripXss(comment);

        if (enterId == null || StringUtils.isEmpty(approvalStatus) || StringUtils.isEmpty(comment)
                || approvalRecordId == null || processId == null
                || approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            map.addAttribute("errorMsg", "审批提交参数异常，审批失败！");
            return "error.ftl";
        }

        /**
         * 1、更新当前要审批的记录（两张表：request的当前状态，record的审批意见）
         * 2、如果审批通过，判断当前审批是否完结，如果未完结，创建一条新的审批记录；如果完结，则更新enterprises表
         */
        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;
        ActivityApprovalDetail activityApprovalDetail = null;
        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Activity_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }
        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());

        if ("1".equals(approvalStatus)) {
            // 当请求审核当前状态等于审核等级前置状态
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            updateApprovalRequest.setEntId(enterId);
            if (nextApprovalDetail == null) {
                // 当前审批是最后一级，更新企业产品表
                activityApprovalDetail = activityApprovalDetailService.selectByRequestId(requestId);
                activityApprovalDetail.setApprovalStatus(approvalStatus);
            } else {
                Role nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(), processId,
                        nextApprovalRole.getName());
            }
        } else {
            activityApprovalDetail = activityApprovalDetailService.selectByRequestId(requestId);
            activityApprovalDetail.setApprovalStatus(approvalStatus);
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Activity_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }
        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);

        try {
            String result = "";
            if (approvalRequestService.approvalForActivity(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord, activityApprovalDetail)) {

                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());
                /**
                 * 活动审核完成，判断企业状态、企业余额 状态正常：直接上架；否则仅仅是审核完成状态
                 */
                if (activityApprovalDetail != null && "1".equals(approvalStatus)) {

                    if (activityApprovalDetail.getActivityType().toString()
                            .equals(ActivityType.FLOWCARD.getCode().toString())
                            || activityApprovalDetail.getActivityType().toString()
                                    .equals(ActivityType.QRCODE.getCode().toString())
                            || activityApprovalDetail.getActivityType().toString()
                                    .equals(ActivityType.CROWD_FUNDING.getCode().toString())) {
                        // 流量券、二维码审核完成，如果企业状态正常直接上架
                        logger.info("开始上架。。。。。");
                        long onshelfStartTime = System.currentTimeMillis();
                        result = activitiesService.onShelf(activityApprovalDetail.getActivityId());
                        long onshelfEndTime = System.currentTimeMillis();
                        logger.info("上架需要花费的时间（秒）：=================" + (onshelfEndTime - onshelfStartTime) / 1000);

                    } else if (activityApprovalDetail.getActivityType().toString()
                            .equals(ActivityType.LOTTERY.getCode().toString())) {
                        // 大转盘
                        result = lotteryService.onShelf(activityApprovalDetail.getActivityId(),
                                SchedulerType.LOTTERY_START.getCode(), SchedulerType.LOTTERY_END.getCode());
                    } else if (activityApprovalDetail.getActivityType().toString()
                            .equals(ActivityType.GOLDENBALL.getCode().toString())) {
                        // 砸金蛋
                        result = goldenEggService.onShelf(activityApprovalDetail.getActivityId(),
                                SchedulerType.GOLDENEGG_START.getCode(), SchedulerType.GOLDENEGG_END.getCode());
                    } else {
                        // 红包
                        result = redpacketService.onShelf(activityApprovalDetail.getActivityId(),
                                SchedulerType.REDPACKET_START.getCode(), SchedulerType.REDPACKET_END.getCode());
                    }

                    if (!"success".equals(result)) {
                        logger.info("由于" + result + "原因，上架失败，活动ID-" + activityApprovalDetail.getActivityId());
                    }
                }
                return "redirect:approvalIndex.html?approvalType=3";
            }
        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败");
            return activityApprovalDetail(map, request, requestId, 1);
        }
        return activityApprovalDetail(map, request, requestId, 1);
    }

    private ApprovalRecord createApprovalRecord(Long requestId, Long currUserId, Long processId, String roleName) {
        ApprovalRecord approvalRecord = new ApprovalRecord();
        approvalRecord.setRequestId(requestId);
        approvalRecord.setCreatorId(currUserId);
        approvalRecord.setDescription("待" + roleName + "审核");
        approvalRecord.setIsNew(1);
        approvalRecord.setDeleteFlag(0);
        approvalRecord.setCreateTime(new Date());

        approvalRecord.setOperatorId(null);
        approvalRecord.setManagerId(null);
        approvalRecord.setComment(null);
        approvalRecord.setUpdateTime(null);
        return approvalRecord;
    }

    private ApprovalRecord initUpdateApprovalRecord(Long approvalRecordId, Long currUserId, String comment,
            String roleName, String approvalStatus) {
        ApprovalRecord approvalRecord = new ApprovalRecord();
        approvalRecord.setId(approvalRecordId);
        approvalRecord.setOperatorId(currUserId);
        approvalRecord.setManagerId(adminManagerService.selectManagerIdByAdminId(currUserId));
        approvalRecord.setComment(comment);
        approvalRecord.setUpdateTime(new Date());
        approvalRecord.setIsNew(0);
        if ("1".equals(approvalStatus)) {
            approvalRecord.setDescription(roleName + "审核通过");
        } else {
            approvalRecord.setDescription("已驳回");
        }
        return approvalRecord;
    }

    private ApprovalRequest initUpdateApprovalRequest(Long requestId, Integer status, String approvalStatus,
            ApprovalDetailDefinition nextApprovalDetail) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setId(requestId);
        approvalRequest.setUpdateTime(new Date());
        if ("1".equals(approvalStatus)) {
            approvalRequest.setStatus(status);
            if (nextApprovalDetail == null) {
                approvalRequest.setResult(ApprovalRequestStatus.APPROVED.getCode());
            }
        } else {
            approvalRequest.setDeleteFlag(1);
            approvalRequest.setResult(ApprovalRequestStatus.REJECT.getCode());
        }
        return approvalRequest;
    }

    private Enterprise initUpdateEnterprise(Long entId, String approvalStatus) {
        Enterprise enterprise = new Enterprise();
        enterprise.setId(entId);
        if (!StringUtils.isEmpty(approvalStatus)) {
            if ("1".equals(approvalStatus)) {
                enterprise.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());
                enterprise.setStatus((byte) 3);

                // 根据各平台的情况同步
                if (entApprovalRelatedBossService.synchronizeFromBoss(entId)) {
                    logger.info("向boss同步成功,entId=" + entId);
                } else {
                    throw new RuntimeException("boss同步失败");
                }

                // 通过时判断合作时间
                Enterprise enter = enterprisesService.selectByPrimaryKey(entId);
                if (enter != null && enter.getEndTime() != null) {
                    if (enter.getEndTime().after(new Date())) {
                        if (!enterprisesService.createEnterpriseExpireSchedule(enter)) {
                            logger.info("创建新的企业合同到期定时任务失败,entId=" + enter.getId());
                        }
                    } else {
                        enterprise.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
                        enterprise.setInterfaceFlag(0);
                        logger.info("企业在审核最后一步校验时合同已过期,企业状态改为暂停,EC关闭,entId=" + enter.getId() + ",entCode="
                                + enter.getCode());
                        enterprise.setStatus((byte) 3);
                    }
                }

                // 通过时判断营业执照时间
                if (enter != null && enter.getLicenceEndTime() != null) {
                    if (!enter.getLicenceEndTime().after(new Date())) {
                        enterprise.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
                        enterprise.setInterfaceFlag(0);
                        logger.info("企业在审核最后一步校验时营业执照已过期,企业状态改为暂停,EC关闭,entId=" + enter.getId() + ",entCode="
                                + enter.getCode());
                        enterprise.setStatus((byte) 3);
                    } else {
                        if (!enterprisesService.createEnterpriseLicenceExpireSchedule(enter)) {
                            logger.info("创建新的企业合同到期定时任务失败,entId=" + enter.getId());
                        }
                    }

                }

                //add by wujiamin， 20170727
                //审核通过，开户过程中假设存在产品分类，可能客户经理会进行编辑操作，在之前的编辑过程中，我们都不对企业关联的产品和账户进行变更
                //在最后开户审核通过时，我们检查当前的账户信息和产品关联是否与开户初始时一致，不一致的进行变更               
                checkProductAndAccount(entId);

            } else if ("0".equals(approvalStatus)) {
                enterprise.setDeleteFlag(EnterpriseStatus.REJECTED.getCode());
            }
        } else {
            enterprise.setDeleteFlag(EnterpriseStatus.SUBMIT_APPROVAL.getCode());
        }
        return enterprise;
    }

    /** 
     * 检查当前的账户信息和产品关联是否与开户初始时一致
     * @Title: checkProductAndAccount 
     */
    @Transactional
    private boolean checkProductAndAccount(Long entId) {
        Enterprise enter = enterprisesService.selectById(entId);
        if (enter == null || enter.getCustomerTypeId() == null) {
            logger.info("企业信息存在异常，id=" + entId);
            return false;
        }

        //没有使用产品模板的平台，才需要校验账户和产品
        if (!getUseProductTemplate()) {
            // 向ent_product表中填入product表中default标识为1的产品，同时要根据该企业创建时的产品分类进行筛选
            // 企业合作信息保存了企业折扣，需要更新到ent_product表中
            Discount discount = discountMapper.selectByPrimaryKey(enter.getDiscount());
            Integer dis = 100;
            if (discount != null) {
                dis = (int) (discount.getDiscount() * 100);
            }

            //1、检查产品
            //筛选出当前企业的产品
            List<EntProduct> originRecords = entProductService.selectByEnterprizeID(entId);
            //构造审核通过时的企业产品
            List<Product> products = productService.selectDefaultProductByCustomerType(enter.getCustomerTypeId());
            List<EntProduct> records = new ArrayList();
            if (products != null && products.size() > 0) {
                for (Product p : products) {
                    EntProduct entProduct = new EntProduct();
                    entProduct.setEnterprizeId(entId);
                    entProduct.setProductId(p.getId());
                    entProduct.setCreateTime(new Date());
                    entProduct.setUpdateTime(new Date());
                    entProduct.setDiscount(dis);
                    entProduct.setDeleteFlag(0);
                    records.add(entProduct);
                }
            }
            if (!compaireList(originRecords, records)) {
                logger.info("审核过程中企业关联的产品发生了变化，需要重新关联产品及创建账户");
                //当前用户关联的产品和审核通过时的产品不一致
                if (!originRecords.isEmpty()) {
                    if (!entProductService.batchDeleteByEnterIdAndProductId(originRecords)) {
                        logger.error("原企业-产品批量删除失败！");
                        throw new RuntimeException();
                    }
                }
                if (!records.isEmpty()) {
                    if (!entProductService.batchInsertEntProduct(records)) {
                        logger.error("新企业-产品批量插入失败！");
                        throw new RuntimeException();
                    }
                }

                //2、产品有变更，则账户也有变更
                Map<Long, Double> infos = new HashMap<Long, Double>();
                //判断当前的entProduct有没有账户
                for (EntProduct record : records) {
                    if (!accountService
                            .checkAccountByEntIdAndProductId(record.getEnterprizeId(), record.getProductId())) {
                        infos.put(record.getProductId(), 0.0D);
                    }
                }
                //为没有账户的entProduct创建账户
                if (!infos.isEmpty()) {
                    if (accountService.createEnterAccount(entId, infos,
                            DigestUtils.md5Hex(UUID.randomUUID().toString()))) {
                        logger.info("创建了新的账户，entId={}, 新增的账户{}", entId, JSONObject.toJSONString(infos));
                        return true;
                    } else {
                        logger.error("创建企业帐户失败.entId={}, 新增的账户{}", entId, JSONObject.toJSONString(infos));
                        throw new RuntimeException();
                    }
                }

            }
        }

        return true;
    }

    private boolean compaireList(List l1, List l2) {
        // make a copy of the list so the original list is not changed, and remove() is supported
        ArrayList cp = new ArrayList(l1);
        for (Object o : l2) {
            if (!cp.remove(o)) {
                return false;
            }
        }
        return cp.isEmpty();
    }

    /**
     * 提交企业基本信息 仅更改企业状态，可以进行填写后续信息
     */
    @RequestMapping("submitBasicEnt")
    public void submitBasicEnt(HttpServletRequest request, HttpServletResponse resp, Long entId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (entId != null) {
            Enterprise enterprise = new Enterprise();
            enterprise.setId(entId);
            enterprise.setDeleteFlag(EnterpriseStatus.SUBMIT_BASIC.getCode());
            enterprise.setUpdateTime(new Date());
            if (enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                map.put("submitRes", "success");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
        map.put("submitRes", "fail");
        resp.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 企业审核提交按钮点击时，检查是否是重复的审核提交
     *
     * @Title: checkEnterpriseApproval
     */
    @RequestMapping("checkEnterpriseApproval")
    public void checkEnterpriseApproval(ModelMap modelMap, Long entId, HttpServletResponse resp) {
        Map map = new HashMap();
        Enterprise enter = enterprisesService.selectByPrimaryKey(entId);
        if (enter != null
                && (EnterpriseStatus.NORMAL.getCode().equals(enter.getDeleteFlag()) || EnterpriseStatus.SUBMIT_APPROVAL
                        .getCode().equals(enter.getDeleteFlag()))) {// 判断是否重复提交
            map.put("msg", "fail");

        } else {
            map.put("msg", "success");
        }
        try {
            resp.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 提交企业审核信息
     */
    @RequestMapping("submitEntApproval")
    public String submitEntApproval(ModelMap modelMap, Long entId, Integer type) {

        if (getProvinceFlag().equals("chongqing")) {
            modelMap.addAttribute("cqFlag", 1);
        } else {
            modelMap.addAttribute("cqFlag", 0);
        }

        modelMap.addAttribute("type", type);// 用来判断提交审核页面跳转到潜在用户还是企业开户列表，type=1企业开户

        Administer currUser = getCurrentUser();
        Manager manager = getCurrentUserManager();
        modelMap.addAttribute("currUserId", currUser.getId());
        modelMap.addAttribute("roleId", manager.getRoleId());

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        modelMap.addAttribute("enterprise", enterprise);

        if (enterprise != null && enterprise.getCmEmail() != null) {
            modelMap.addAttribute("hasEmail", "true");
        } else {
            modelMap.addAttribute("hasEmail", "false");
        }

        // 获取企业管理员
        List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(entId);
        // 目前设计为一个企业只可能有一个企业管理员
        if (null != enterpriseManagers && enterpriseManagers.size() == 1) {
            modelMap.addAttribute("enterpriseManager", enterpriseManagers.get(0));
        }
        // 获取客户经理
        // 目前设计为一个企业只可能有一个客户经理
        /*
         * List<Administer> customerManagers =
         * adminManagerService.getCustomerManagerByEntId(entId); if
         * (customerManagers != null && customerManagers.size() == 1) {
         * modelMap.addAttribute("customerManager", customerManagers.get(0)); }
         */
        Administer customerManager = null;
        if (enterprise != null) {
            customerManager = administerService.selectByMobilePhone(enterprise.getCmPhone());
        }

        if (customerManager == null) {
            customerManager = new Administer();
        }
        modelMap.addAttribute("customerManager", customerManager);

        // 文件下载内容
        EnterpriseFile ef = enterpriseFileService.selectByEntId(entId);
        if (ef != null) {
            modelMap.addAttribute("businessLicence", ef.getBusinessLicence());
            modelMap.addAttribute("authorization", ef.getAuthorizationCertificate());
            modelMap.addAttribute("identification", ef.getIdentificationCard());
            modelMap.addAttribute("identificationBack", ef.getIdentificationBack());

            modelMap.addAttribute("customerfile", ef.getCustomerfileName());
            modelMap.addAttribute("contract", ef.getContractName());
            modelMap.addAttribute("image", ef.getImageName());
        }

        // 获取信用额度值
        Account account = null;
        if (enterprise != null) {
            account = accountService.get(enterprise.getId(), productService.getCurrencyProduct().getId(),
                    AccountType.ENTERPRISE.getValue());
        }

        if (account != null && account.getId() != null) {
            Double minCount = account.getMinCount() / (-100.0);
            if (account.getMinCount() == 0) {
                minCount = 0D;
            }
            modelMap.addAttribute("minCount", minCount);
        }

        // 获取折扣信息
        if (enterprise != null) {
            Discount discount = discountService.selectByPrimaryKey(enterprise.getDiscount());
            modelMap.addAttribute("discount", discount.getName());
        }

        // 获取存送比
        // GiveMoneyEnter giveMoneyEnter =
        // giveMoneyEnterService.selectByEnterId(enterprise.getDiscount());
        // modelMap.addAttribute("giveMoneyId",
        // giveMoneyEnter.getGiveMoneyId());

        if (ef != null) {
            if (!StringUtils.isEmpty(ef.getCustomerfileName()) || !StringUtils.isEmpty(ef.getContractName())
                    || !StringUtils.isEmpty(ef.getImageName())) { // ||
                // account
                // !=
                // null&&account.getCount()!=null
                // 只要有一个文件不问空就需要翻页
                modelMap.addAttribute("nextPage", "true");
            } else {
                modelMap.addAttribute("nextPage", "false");
            }
        }

        // 地区信息
        // 地区全称
        Manager entManager = null;
        if (enterprise != null) {
            entManager = entManagerService.getManagerForEnter(enterprise.getId());
        }

        String fullname = "";
        if (entManager != null) {
            fullname = managerService.getFullNameByCurrentManagerId(fullname, entManager.getParentId());
        }
        modelMap.addAttribute("fullDistrictName", fullname);

        String provinceFlag = getProvinceFlag();
        modelMap.addAttribute("provinceFlag", provinceFlag);
        if (provinceFlag.equals(zyProvinceFlagValue)) {
            modelMap.addAttribute("flag", 1);
        } else {
            modelMap.addAttribute("flag", 0);
        }

        // 获取历史审批记录
        List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(entId,
                ApprovalType.Enterprise_Approval.getCode());
        modelMap.addAttribute("opinions", approvalRecords);
        if (approvalRecords != null && approvalRecords.size() > 0) {
            modelMap.addAttribute("hasApproval", "true");
        } else {
            modelMap.addAttribute("hasApproval", "false");
        }

        if ("gd_mdrc".equals(getProvinceFlag())) { // 广东流量卡
            if (needExtEntInfo()) {
                modelMap.addAttribute("crowdfundingFlag", getIsCrowdfundingPlatform());
                EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(entId);
                modelMap.put("extEntInfo", enterprisesExtInfo);
            }
            return "entApproval/qualificationDetail_gd.ftl";
        } else {
            return "entApproval/qualificationDetail.ftl";
        }
    }

    /**
     * 企业开户提交审批申请
     *
     * @Param isCooperate 是否填写合作信息
     */
    @RequestMapping("submitAjax")
    public void submitAjax(HttpServletRequest request, HttpServletResponse resp, Long entId, Long currUserId,
            Long roleId, String isCooperate) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        Enterprise enter = enterprisesService.selectByPrimaryKey(entId);
        if (entId == null || enter == null) {
            map.put("submitRes", "fail");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }

        if (EnterpriseStatus.NORMAL.getCode().equals(enter.getDeleteFlag())
                || EnterpriseStatus.SUBMIT_APPROVAL.getCode().equals(enter.getDeleteFlag())) {// 判断是否重复提交
            map.put("submitRes", "repeat");
            resp.getWriter().write(JSON.toJSONString(map));
            return;

        } else {
            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.Enterprise_Approval.getCode());

            // 判断是否需要审核
            if (approvalProcessDefinition.getStage().toString().equals("0")) {
                // 不需要审核，直接更新企业状态，完成审核
                Enterprise enterprise = new Enterprise();
                enterprise.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());
                enterprise.setStatus((byte) 3);
                enterprise.setId(entId);

                if (enter.getEndTime() != null) {
                    if (enter.getEndTime().after(new Date())) {
                        if (!enterprisesService.createEnterpriseExpireSchedule(enter)) {
                            logger.info("创建新的企业合同到期定时任务失败,entId=" + enter.getId());
                        }
                    } else {
                        enterprise.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
                        enterprise.setInterfaceFlag(0);
                        logger.info("企业在审核最后一步校验时合同已过期,企业状态改为暂停,EC关闭,entId=" + enter.getId() + ",entCode="
                                + enter.getCode());
                        enterprise.setStatus((byte) 3);
                    }
                }

                // 通过时判断营业执照时间
                if (enter.getLicenceEndTime() != null) {
                    if (!enter.getLicenceEndTime().after(new Date())) {
                        enterprise.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
                        enterprise.setInterfaceFlag(0);
                        logger.info("企业在审核最后一步校验时营业执照已过期,企业状态改为暂停,EC关闭,entId=" + enter.getId() + ",entCode="
                                + enter.getCode());
                        enterprise.setStatus((byte) 3);
                    } else {
                        if (!enterprisesService.createEnterpriseLicenceExpireSchedule(enter)) {
                            logger.info("创建新的企业合同到期定时任务失败,entId=" + enter.getId());
                        }
                    }

                }

                if ("false".equals(isCooperate)) {
                    // 未填写合作信息，需要默认关联客户类型和支付类型
                    // 判断当前的企业信息记录是否有客户分类和支付类型，如果已有值，不需要在重设默认值
                    if (enter.getCustomerTypeId() == null) {
                        enterprise.setCustomerTypeId(1L);
                    }
                    if (enter.getPayTypeId() == null) {
                        enterprise.setPayTypeId(1L);
                    }
                }

                if (approvalRequestService.submitWithoutApproval(
                        createApprovalRequest(approvalProcessDefinition.getId(), entId, currUserId), enterprise, null,
                        null, null)) {
                    // 根据各平台的情况同步
                    if (entApprovalRelatedBossService.synchronizeFromBoss(entId)) {
                        logger.info("向boss同步成功,entId=" + entId);
                    }

                    map.put("submitRes", "success");
                    resp.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }

            List<ApprovalDetailDefinition> approvalRecordList = approvalDetailDefinitionService
                    .getByApprovalProcessId(approvalProcessDefinition.getId());
            Role role = roleService.getRoleById(approvalRecordList.get(0).getRoleId());

            Enterprise enterprise = initUpdateEnterprise(entId, null);

            if ("false".equals(isCooperate)) {
                // 未填写合作信息，需要默认关联客户类型和支付类型
                // 判断当前的企业信息记录是否有客户分类和支付类型，如果已有值，不需要在重设默认值
                if (enter.getCustomerTypeId() == null) {
                    enterprise.setCustomerTypeId(1L);
                }
                if (enter.getPayTypeId() == null) {
                    enterprise.setPayTypeId(1L);
                }
            }
            ApprovalRequest approvalRequest = createApprovalRequest(approvalProcessDefinition.getId(), entId,
                    currUserId);
            if (approvalRequestService.submitApproval(
                    approvalRequest,
                    createApprovalRecord(null, currUserId, approvalProcessDefinition.getId(), role.getName()),
                    enterprise, null, null, null, null)) {
                approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
               /* if (needApprovalNotice()) {
                    Long entMangerId = getCurrentUserManager().getId();

                    // Long fatherManagerId =
                    // managerService.selectByAdminId(currUserId).getParentId();
                    Long fatherManagerId = managerService
                            .selectManagerIdByEntIdAndRoleId(entMangerId, role.getRoleId());
                    List<Long> manageIds = new ArrayList<Long>();
                    manageIds.add(fatherManagerId);
                    List<Administer> administers = administerService.getByManageIds(manageIds);
                    for (Administer admininter : administers) {
                        String content = admininter.getUserName() + ", 您好！"
                                + globalConfigService.get(GlobalConfigKeyEnum.LOGO_NAME.getKey())
                                + "\"企业开户申请\"的申请等待您审批。";
                        if (!sendMsgService.sendMessage(admininter.getMobilePhone(), content,
                                MessageType.APPROVAL_NOTICE.getCode())) {
                            logger.info("审批通知发送短信失败;手机号-" + admininter.getMobilePhone() + ", 审批人-"
                                    + admininter.getUserName());
                        }
                    }
                }*/

                map.put("submitRes", "success");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            } else {
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
    }

    /**
     * 充值提交审批
     */
    @RequestMapping("submitAccountAjax")
    public void submitAccountAjax(HttpServletRequest request, HttpServletResponse resp, Long entId, Long currentId,
            Long roleId, Long accountId, Integer approvalType) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        // 对同时提交审核的判断
        AccountChangeOperator record = accountChangeOperatorService.selectByPrimaryKey(accountId);
        if (record == null || record.getDeleteFlag() == 1) {
            map.put("submitRes", "该记录已被其他用户提交审核！");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }

        if (entId == null) {
            map.put("submitRes", "fail");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        } else {
            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(approvalType);

            //暂停值审批前一个审批完下一个才允许提交
            if (ApprovalType.Account_Min_Change_Approval.getCode().equals(approvalType)
                    || ApprovalType.Account_Stop_Change_Approval.getCode().equals(approvalType)
                    || ApprovalType.Account_Alert_Change_Approval.getCode().equals(approvalType)) {
                //判断是否有处于审核中的记录
                List<ApprovalRequest> list = approvalRequestService.getApprovalRequests(entId, approvalType,
                        ApprovalRequestStatus.APPROVING.getCode());
                if (list != null && list.size() > 0) {
                    map.put("submitRes", "同一时间提交多个审批，每个企业同时只能提交一个暂停值审批！");
                    resp.getWriter().write(JSON.toJSONString(map));
                    return;
                }
            }

            // 判断是否需要审核
            if (approvalProcessDefinition.getStage().toString().equals("0")) {
                logger.info("不需要审批，直接更新账户，审批流程id" + approvalProcessDefinition.getId());
                if (approvalRequestService.submitWithoutApproval(
                        createApprovalRequest(approvalProcessDefinition.getId(), entId, currentId), null,
                        createAccountChangeDetail(accountId), null, null)) {
                    map.put("submitRes", "success");
                    resp.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }

            List<ApprovalDetailDefinition> approvalRecordList = approvalDetailDefinitionService
                    .getByApprovalProcessId(approvalProcessDefinition.getId());
            Role role = roleService.getRoleById(approvalRecordList.get(0).getRoleId());

            ApprovalRequest approvalRequest = createApprovalRequest(approvalProcessDefinition.getId(), entId, currentId);
            if (approvalRequestService.submitApproval(approvalRequest,
                    createApprovalRecord(null, currentId, approvalProcessDefinition.getId(), role.getName()), null,
                    createAccountChangeDetail(accountId), null, null, null)) {
                approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
                map.put("submitRes", "success");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            } else {
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
    }

    /**
     * 产品变更提交审批申请
     */
    @RequestMapping("submitProductChangeAjax")
    public void submitAccountAjax(HttpServletRequest request, HttpServletResponse resp, Long entId, Long currentId,
            Long roleId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        if (entId == null) {
            map.put("submitRes", "fail");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        } else {

            List<ProductChangeDetail> productChangeDetails = createProductChangeDetails(entId);
            // 判断是否有变更的产品
            if (productChangeDetails == null || productChangeDetails.size() < 1) {
                map.put("submitRes", "noprd");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }

            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.Product_Change_Approval.getCode());

            // 判断是否需要审核
            if (approvalProcessDefinition.getStage().toString().equals("0")) {
                logger.info("不需要审批，直接更新产品列表，审批流程id" + approvalProcessDefinition.getId());
                if (approvalRequestService.submitWithoutApprovalForProductChange(
                        createApprovalRequest(approvalProcessDefinition.getId(), entId, currentId),
                        productChangeDetails)) {
                    map.put("submitRes", "success");
                    resp.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }

            List<ApprovalDetailDefinition> approvalRecordList = approvalDetailDefinitionService
                    .getByApprovalProcessId(approvalProcessDefinition.getId());
            Role role = roleService.getRoleById(approvalRecordList.get(0).getRoleId());

            ApprovalRequest approvalRequest = createApprovalRequest(approvalProcessDefinition.getId(), entId, currentId);
            if (approvalRequestService.submitApproval(approvalRequest,
                    createApprovalRecord(null, currentId, approvalProcessDefinition.getId(), role.getName()), null,
                    null, productChangeDetails, null, null)) {
                approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
                map.put("submitRes", "success");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            } else {
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
    }

    /**
     * @param id
     *            审核请求id
     */
    @RequestMapping("entInfoChangeDetail")
    public String entInfoChangeDetail(ModelMap modelmap, HttpServletRequest request, Long id) {
        // String errorMsg = "";
        if (id != null) {
            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);
            if (approvalRequest != null && approvalRequest.getEntId() != null) {
                // 充值请求记录
                modelmap.addAttribute("approvalRequest", approvalRequest);
                // 企业变更信息
                HistoryEnterprises historyEnterprises = historyEnterprisesService
                        .selectHistoryEnterpriseByRequestId(approvalRequest.getId());
                HistoryEnterpriseFile historyEnterpriseFile = historyEnterpriseFileService
                        .selectByRequestId(approvalRequest.getId());
                // modelmap.addAttribute("historyEnterprises",
                // historyEnterprises);
                modelmap.addAttribute("historyEnterpriseFile", historyEnterpriseFile);

                if (historyEnterprises == null) {
                    modelmap.addAttribute("errorMsg", "数据库中没有相关企业变更信息，企业Id: " + approvalRequest.getEntId());
                    return "error.ftl";
                }

                // 地区全称
                Manager manager = entManagerService.getManagerForEnter(approvalRequest.getEntId());
                if (manager != null) {
                    String fullname = "";
                    historyEnterprises.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                            manager.getParentId()));
                }

                modelmap.addAttribute("enterprise", historyEnterprises);

                // 客户经理信息
                Administer customerManager = administerService.selectByMobilePhone(historyEnterprises.getCmPhone());
                if (customerManager == null) {
                    customerManager = new Administer();
                }
                modelmap.addAttribute("customerManager", customerManager);

                // 企业管理员
                List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(historyEnterprises
                        .getEntId());
                modelmap.addAttribute("enterpriseManagers", enterpriseManagers);

                // 显示历史审批意见
                // 审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);

                if (!varifyRequestRecord(getCurrentUserManager().getRoleId(), approvalRecords)) {
                    modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                    modelmap.addAttribute("flag", ApprovalType.Ent_Information_Change_Approval.getCode());
                    return "entApproval/error.ftl";
                }

                modelmap.addAttribute("approvalRecords", approvalRecords);

                modelmap.addAttribute("approvalRecordId",
                        approvalRecords.size() > 0 ? (approvalRecords.get(approvalRecords.size() - 1).getId()) : "");
                // 审核请求记录id
                modelmap.addAttribute("requestId", id);
                // 审批流程id
                modelmap.addAttribute("processId", approvalRequest.getProcessId());

                // 文件下载内容
                HistoryEnterpriseFile ef = historyEnterpriseFileService.selectByRequestId(id);
                if (ef != null) {
                    modelmap.addAttribute("businessLicence", ef.getBusinessLicence());
                    modelmap.addAttribute("authorization", ef.getAuthorizationCertificate());
                    modelmap.addAttribute("identification", ef.getIdentificationCard());
                    modelmap.addAttribute("identificationBack", ef.getIdentificationBack());
                    modelmap.addAttribute("customerfile", ef.getCustomerfileName());
                    modelmap.addAttribute("contract", ef.getContractName());
                    modelmap.addAttribute("image", ef.getImageName());
                }

                // 获取信用额度值
                Account account = accountService.get(approvalRequest.getEntId(), productService.getCurrencyProduct()
                        .getId(), AccountType.ENTERPRISE.getValue());
                if (account != null && account.getMinCount() != null) {
                    modelmap.addAttribute("minCount", account.getMinCount() == 0 ? 0 : account.getMinCount() / (-100.0));
                }

                // 传递标识：是否是自营的企业
                if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                    modelmap.addAttribute("flag", 1);
                } else {
                    modelmap.addAttribute("flag", 0);
                }

                CustomerType customerType = customerTypeMapper.selectById(historyEnterprises.getCustomerTypeId());
                modelmap.addAttribute("customerName", customerType.getName());

                if ("gd_mdrc".equals(getProvinceFlag())) { // 广东流量卡
                    EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(approvalRequest.getEntId());
                    modelmap.put("extEntInfo", enterprisesExtInfo);
                    return "entInfoChangeApproval/entInfoChangeDetail_gd.ftl";
                } else {
                    return "entInfoChangeApproval/entInfoChangeDetail.ftl";
                }
            }
            logger.info("数据库中没有相关审批记录Id: " + id);
        }
        return "redirect:approvalIndex.html?approvalType="
                + ApprovalType.Ent_Information_Change_Approval.getCode().toString();
    }

    /**
     * @param approvalStatus
     *            1通过 0驳回
     */
    @RequestMapping(value = "/saveEntInfoChangeApproval")
    public String saveEntInfoChangeApproval(HttpServletRequest request, ModelMap map, Long enterId, String comment,
            String approvalStatus, Long requestId, Long approvalRecordId, Long processId) {
        Administer currentUser = getCurrentUser();

        if (enterId == null || StringUtils.isEmpty(approvalStatus) || StringUtils.isEmpty(comment)
                || approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            map.addAttribute("errorMsg", "审批提交参数异常，审批失败！");
            return entInfoChangeDetail(map, request, requestId);
        }

        /**
         * 1、更新当前要审批的记录（两张表：request的当前状态，record的审批意见）
         * 2、如果审批通过，判断当前审批是否完结，如果未完结，创建一条新的审批记录；如果完结，则更新enterprises表
         */
        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;
        HistoryEnterprises historyEnterprises = null;

        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(processId, approvalRequest.getStatus());

        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Ent_Information_Change_Approval.getCode());
            return "entApproval/error.ftl";
        }
        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());
        Role nextApprovalRole = null;
        if (nextApprovalDetail != null) {
            nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
        }

        if ("1".equals(approvalStatus)) {
            // 当请求审核当前状态等于审核等级前置状态
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            // 当前审批是最后一级，根据配置决定是否去同步渠道商的产品，并建立起渠道商产品、平台产品、企业的关系
            if (nextApprovalDetail == null) {
                // 当前审批是最后一级，更新企业历史表
                historyEnterprises = initUpdateHistoryEnterprise(approvalRequest.getEntId(), requestId, approvalStatus);
                // enterprise = initUpdateEnterprise(approvalRequest.getEntId(),
                // approvalStatus);
            } else {
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(), processId,
                        nextApprovalRole.getName());
            }
        } else {
            historyEnterprises = initUpdateHistoryEnterprise(approvalRequest.getEntId(), requestId, approvalStatus);

            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Ent_Information_Change_Approval.getCode());
            return "entApproval/error.ftl";
        }

        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);

        try {
            EnterpriseFile enterpriseFile = convertToEnterpriseFile(historyEnterpriseFileService
                    .selectByRequestId(requestId));
            if (approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord, null, null, null, null, historyEnterprises, enterpriseFile, null, null, null)) {
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());
                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                return "redirect:approvalIndex.html?approvalType=4";
            }
        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败" + e.getMessage());
            return entInfoChangeDetail(map, request, requestId);
        }

        return entInfoChangeDetail(map, request, requestId);
    }

    /**
     * @param id
     *            审核请求id
     */
    @RequestMapping("entEcDetail")
    public String entEcDetail(ModelMap modelmap, HttpServletRequest request, Long id) {
        // String errorMsg = "";
        if (id != null && !approvalRequestService.isOverAuth(getCurrentAdminID(), id)) {
            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);
            if (approvalRequest != null && approvalRequest.getEntId() != null) {
                // 充值请求记录
                modelmap.addAttribute("approvalRequest", approvalRequest);
                // EC变更信息
                EcApprovalDetail ecApprovalDetail = ecApprovalDetailService.selectByRequestId(approvalRequest.getId());

                if (ecApprovalDetail == null) {
                    modelmap.addAttribute("errorMsg", "数据库中没有相关EC变更信息，企业Id: " + approvalRequest.getEntId());
                    return "error.ftl";
                }
                modelmap.addAttribute("ecApprovalDetail", ecApprovalDetail);

                // 显示历史审批意见
                // 审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);

                if (!varifyRequestRecord(getCurrentUserManager().getRoleId(), approvalRecords)) {
                    modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                    modelmap.addAttribute("flag", ApprovalType.Ec_Approval.getCode());
                    return "entApproval/error.ftl";
                }

                modelmap.addAttribute("approvalRecords", approvalRecords);

                modelmap.addAttribute("approvalRecordId",
                        approvalRecords.size() > 0 ? (approvalRecords.get(approvalRecords.size() - 1).getId()) : "");
                // 审核请求记录id
                modelmap.addAttribute("requestId", id);
                // 审批流程id
                modelmap.addAttribute("processId", approvalRequest.getProcessId());

                return "entEcApproval/entEcApprovalDetail.ftl";
            }
            logger.info("数据库中没有相关审批记录Id: " + id);
        }
        return "redirect:approvalIndex.html?approvalType=" + ApprovalType.Ec_Approval.getCode().toString();
    }

    /**
     * @param approvalStatus
     *            1通过 0驳回
     */
    @RequestMapping(value = "/saveEnterpriseEcApproval")
    public String saveEnterpriseEcApproval(HttpServletRequest request, ModelMap map, Long enterId, String comment,
            String approvalStatus, Long requestId, Long approvalRecordId, Long processId) {
        Administer currentUser = getCurrentUser();

        if (enterId == null || StringUtils.isEmpty(approvalStatus) || StringUtils.isEmpty(comment)
                || approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            map.addAttribute("errorMsg", "审批提交参数异常，审批失败！");
            return entEcDetail(map, request, requestId);
        }

        /**
         * 1、更新当前要审批的记录（两张表：request的当前状态，record的审批意见）
         * 2、如果审批通过，判断当前审批是否完结，如果未完结，创建一条新的审批记录；如果完结，则更新enterprises表
         */
        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;
        Enterprise enterprise = null;
        List<EntWhiteList> entWhiteList = null;
        EntCallbackAddr entCallbackAddr = null;

        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);
        EcApprovalDetail ecApprovalDetail = ecApprovalDetailService.selectByRequestId(requestId);

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(processId, approvalRequest.getStatus());

        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Ec_Approval.getCode());
            return "entApproval/error.ftl";
        }
        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());
        Role nextApprovalRole = null;
        if (nextApprovalDetail != null) {
            nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
        }

        if ("1".equals(approvalStatus)) {
            // 当请求审核当前状态等于审核等级前置状态
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            // 当前审批是最后一级，根据配置决定是否去同步渠道商的产品，并建立起渠道商产品、平台产品、企业的关系
            if (nextApprovalDetail == null) {
                // 当前审批是最后一级，更新企业白名单和回调地址

                entWhiteList = initEntWhiteList(ecApprovalDetail);
                entCallbackAddr = initEntCallbackAddr(ecApprovalDetail);
                enterprise = initEnterpriseEcStatus(enterprisesService.selectById(ecApprovalDetail.getEntId()),
                        approvalStatus);

            } else {
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(), processId,
                        nextApprovalRole.getName());
            }
        } else {
            enterprise = initEnterpriseEcStatus(enterprisesService.selectById(ecApprovalDetail.getEntId()),
                    approvalStatus);
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Ec_Approval.getCode());
            return "entApproval/error.ftl";
        }

        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);

        try {
            if (approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord, enterprise, null, null, null, null, null, entCallbackAddr, entWhiteList, null)) {

               /* if ("1".equals(approvalStatus) && needECApprovalNotice() && nextApprovalDetail != null) {
                    if (!currentUser.getRoleId().equals(nextApprovalRole.getRoleId())) {
                        Long mangerId = getCurrentUserManager().getId();

                        // Long fatherManagerId =
                        // managerService.selectByAdminId(currUserId).getParentId();
                        Long fatherManagerId = managerService.selectManagerIdByEntIdAndRoleId(mangerId,
                                nextApprovalRole.getRoleId());

                        // Long fatherManagerId =
                        // managerService.selectByAdminId(currentUser.getId()).getParentId();
                        List<Long> manageIds = new ArrayList<Long>();
                        manageIds.add(fatherManagerId);
                        List<Administer> administers = administerService.getByManageIds(manageIds);
                        for (Administer admininter : administers) {
                            String content = admininter.getUserName() + ", 您好！"
                                    + globalConfigService.get(GlobalConfigKeyEnum.LOGO_NAME.getKey())
                                    + "有企业EC接口审批任务待处理。";
                            if (!isSmsBlack(admininter.getMobilePhone(), SmsBlackType.Ec_Approval.getCode())
                                    && !sendMsgService.sendMessage(admininter.getMobilePhone(), content,
                                            MessageType.APPROVAL_NOTICE.getCode())) {
                                logger.info("审批通知发送短信失败;手机号-" + admininter.getMobilePhone() + ", 审批人-"
                                        + admininter.getUserName());
                            }
                        }
                    } else {

                        List<Long> manageIds = new ArrayList<Long>();
                        manageIds.add(getCurrentUserManager().getId());
                        List<Administer> administers = administerService.getByManageIds(manageIds);

                        for (Administer admininter : administers) {
                            ApprovalRecord approvalRecord = approvalRecordService.selectNewRecordByRequestId(requestId,
                                    admininter.getId());
                            if (approvalRecord != null) {
                                String content = admininter.getUserName() + ", 您好！"
                                        + globalConfigService.get(GlobalConfigKeyEnum.LOGO_NAME.getKey())
                                        + "有企业EC接口审批任务待处理。";
                                if (!isSmsBlack(admininter.getMobilePhone(), SmsBlackType.Ec_Approval.getCode())
                                        && !sendMsgService.sendMessage(admininter.getMobilePhone(), content,
                                                MessageType.APPROVAL_NOTICE.getCode())) {
                                    logger.info("审批通知发送短信失败;手机号-" + admininter.getMobilePhone() + ", 审批人-"
                                            + admininter.getUserName());
                                }
                            }
                        }
                    }
                }*/
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());
                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                return "redirect:approvalIndex.html?approvalType=5";
            }
        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败" + e.getMessage());
            return entEcDetail(map, request, requestId);
        }

        return entEcDetail(map, request, requestId);
    }

    private Enterprise initEnterpriseEcStatus(Enterprise enterprise, String approvalStatus) {
        Enterprise ent = new Enterprise();
        ent.setId(enterprise.getId());
        if ("1".equals(approvalStatus)) {
            if (enterprise.getInterfaceFlag().equals(InterfaceStatus.APPROVING.getCode())) {
                if (EnterpriseStatus.NORMAL.getCode().equals(enterprise.getDeleteFlag())) {// 企业状态正常情况下，EC审核通过后，EC未开启状态，其他默认为关闭
                    ent.setInterfaceFlag(InterfaceStatus.OPEN.getCode());
                } else {
                    ent.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());
                }
                ent.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVED.getCode());
            } else {
                ent.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVED.getCode());
            }
        } else {
            if (enterprise.getInterfaceFlag().equals(InterfaceStatus.APPROVING.getCode())) {
                ent.setInterfaceFlag(InterfaceStatus.REJECT.getCode());
                ent.setInterfaceApprovalStatus(InterfaceApprovalStatus.REJECT.getCode());
            } else {
                ent.setInterfaceApprovalStatus(InterfaceApprovalStatus.REJECT.getCode());
            }
        }
        return ent;
    }

    private EntCallbackAddr initEntCallbackAddr(EcApprovalDetail ecApprovalDetail) {
        EntCallbackAddr entCallbackAddr = new EntCallbackAddr();
        entCallbackAddr.setEntId(ecApprovalDetail.getEntId());
        entCallbackAddr.setCallbackAddr(ecApprovalDetail.getCallbackUrl());
        entCallbackAddr.setCreateTime(new Date());
        entCallbackAddr.setUpdateTime(new Date());
        entCallbackAddr.setDeleteFlag(0);
        return entCallbackAddr;
    }

    private List<EntWhiteList> initEntWhiteList(EcApprovalDetail ecApprovalDetail) {
        List<EntWhiteList> entWhiteLists = new ArrayList<EntWhiteList>();
        if (ecApprovalDetail.getIp1() != null && !StringUtils.isEmpty(ecApprovalDetail.getIp1())) {
            EntWhiteList entWhiteList = new EntWhiteList();
            entWhiteList.setEntId(ecApprovalDetail.getEntId());
            entWhiteList.setIpAddress(ecApprovalDetail.getIp1());
            entWhiteList.setCreateTime(new Date());
            entWhiteList.setUpdateTime(new Date());
            entWhiteList.setDeleteFlag(0);
            entWhiteLists.add(entWhiteList);
        }
        if (ecApprovalDetail.getIp2() != null && !StringUtils.isEmpty(ecApprovalDetail.getIp2())) {
            EntWhiteList entWhiteList = new EntWhiteList();
            entWhiteList.setEntId(ecApprovalDetail.getEntId());
            entWhiteList.setIpAddress(ecApprovalDetail.getIp2());
            entWhiteList.setCreateTime(new Date());
            entWhiteList.setUpdateTime(new Date());
            entWhiteList.setDeleteFlag(0);
            entWhiteLists.add(entWhiteList);
        }
        if (ecApprovalDetail.getIp3() != null && !StringUtils.isEmpty(ecApprovalDetail.getIp3())) {
            EntWhiteList entWhiteList = new EntWhiteList();
            entWhiteList.setEntId(ecApprovalDetail.getEntId());
            entWhiteList.setIpAddress(ecApprovalDetail.getIp3());
            entWhiteList.setCreateTime(new Date());
            entWhiteList.setUpdateTime(new Date());
            entWhiteList.setDeleteFlag(0);
            entWhiteLists.add(entWhiteList);
        }

        return entWhiteLists;
    }

    private HistoryEnterprises initUpdateHistoryEnterprise(Long entId, Long requestId, String approvalStatus) {
        HistoryEnterprises he = new HistoryEnterprises();
        he.setRequestId(requestId);
        he.setId(entId);
        if (!StringUtils.isEmpty(approvalStatus)) {
            if ("1".equals(approvalStatus)) {
                he = historyEnterprisesService.selectByRequestId(requestId);
                Enterprise enterpriseUpdate = convertToEnterprise(he);

                he.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());
                he.setStatus((byte) 3);

                // 通过时判断合作时间
                Enterprise enterRaw = enterprisesService.selectByPrimaryKey(entId);
                logger.info("企业历史记录修改，企业id={}，原企业信息为：{}", entId, JSONObject.toJSONString(enterRaw));
                if (enterRaw != null && enterRaw.getEndTime() != null
                        && !enterRaw.getEndTime().equals(enterpriseUpdate.getEndTime())) {
                    if (enterRaw.getEndTime().after(new Date())) {
                        if (!enterprisesService.undoEnterpriseExireSchedule(enterRaw)) {
                            logger.info("取消企业合同到期定时任务失败,entId=" + enterRaw.getId());
                        }
                        if (!enterprisesService.createEnterpriseExpireSchedule(enterpriseUpdate)) {
                            logger.info("创建新的企业合同到期定时任务失败,entId=" + enterpriseUpdate.getId());
                        }
                    } else {
                        enterpriseUpdate.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
                        enterpriseUpdate.setInterfaceFlag(0);
                        logger.info("企业在审核最后一步校验时合同已过期,企业状态改为暂停,EC关闭,entId=" + enterpriseUpdate.getId() + ",entCode="
                                + enterpriseUpdate.getCode());
                        enterpriseUpdate.setStatus((byte) 3);
                    }
                }

                // 通过时判断营业执照时间
                if (enterRaw != null && enterRaw.getLicenceEndTime() != null
                        && !enterRaw.getLicenceEndTime().equals(enterpriseUpdate.getLicenceEndTime())) {
                    if (enterRaw.getLicenceEndTime().after(new Date())) {
                        if (!enterprisesService.undoEnterpriseLicenceExpireSchedule(enterRaw)) {
                            logger.info("取消企业营业执照到期定时任务失败,entId=" + enterRaw.getId());
                        }
                        if (!enterprisesService.createEnterpriseLicenceExpireSchedule(enterpriseUpdate)) {
                            logger.info("创建新的企业合同到期定时任务失败,entId=" + enterpriseUpdate.getId());

                        } else {
                            enterpriseUpdate.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());
                            enterpriseUpdate.setInterfaceFlag(0);
                            logger.info("企业在审核最后一步校验时营业执照已过期,企业状态改为暂停,EC关闭,entId=" + enterpriseUpdate.getId()
                                    + ",entCode=" + enterpriseUpdate.getCode());
                            enterpriseUpdate.setStatus((byte) 3);
                        }
                    }

                }
            } else if ("0".equals(approvalStatus)) {
                he.setDeleteFlag(EnterpriseStatus.REJECTED.getCode());
            }
        } else {
            he.setDeleteFlag(EnterpriseStatus.SUBMIT_APPROVAL.getCode());
        }
        return he;
    }

    private Enterprise convertToEnterprise(HistoryEnterprises he) {
        String jsonStr = JSON.toJSONString(he);
        return JSON.parseObject(jsonStr, Enterprise.class);
    }

    private EnterpriseFile convertToEnterpriseFile(HistoryEnterpriseFile hef) {
        String jsonStr = JSON.toJSONString(hef);
        return JSON.parseObject(jsonStr, EnterpriseFile.class);
    }

    private List<ProductChangeDetail> createProductChangeDetails(Long entId) {
        if (entId == null) {
            return null;
        }
        List<ProductChangeDetail> productChangeDetails = new ArrayList<ProductChangeDetail>();

        List<ProductChangeOperator> productChangeOperators = productChangeOperatorService
                .getProductChangeRecordByEntId(entId);
        if (productChangeOperators != null && productChangeOperators.size() > 0) {
            for (ProductChangeOperator item : productChangeOperators) {
                ProductChangeDetail productChangeDetail = new ProductChangeDetail();
                productChangeDetail.setDiscount(item.getDiscount());
                productChangeDetail.setProductId(item.getPrdId());
                productChangeDetail.setDeleteFlag(0);
                productChangeDetail.setOperate(item.getOperator());

                productChangeDetail.setOldProductTemplateId(item.getOldProductTemplateId());
                productChangeDetail.setNewProductTemplateId(item.getNewProductTemplateId());
                productChangeDetails.add(productChangeDetail);
            }
        }
        return productChangeDetails;
    }

    private AccountChangeDetail createAccountChangeDetail(Long id) {
        if (id == null) {
            return null;
        }
        AccountChangeDetail accountChangeDetail = new AccountChangeDetail();
        AccountChangeOperator record = accountChangeOperatorService.selectByPrimaryKey(id);
        accountChangeDetail.setAccountId(record.getAccountId());
        accountChangeDetail.setCount(record.getCount());
        accountChangeDetail.setDeleteFlag(0);
        accountChangeDetail.setProductId(record.getPrdId());
        accountChangeDetail.setSerialNum(record.getSerialNum());
        accountChangeDetail.setChangeType(record.getChangeType());
        if (record.getDiscountType() != null) {
            accountChangeDetail.setDiscountType(record.getDiscountType());
        }
        if (record.getDiscountValue() != null) {
            accountChangeDetail.setDiscountValue(record.getDiscountValue());
        }
        return accountChangeDetail;
    }

    private ApprovalRequest createApprovalRequest(Long processId, Long entId, Long currUserId) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setProcessId(processId);
        approvalRequest.setEntId(entId);
        approvalRequest.setCreatorId(currUserId);
        approvalRequest.setStatus(0);
        approvalRequest.setCreateTime(new Date());
        approvalRequest.setUpdateTime(new Date());
        approvalRequest.setDeleteFlag(0);
        approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
        return approvalRequest;
    }

    /**
     * EC信息修改
     *
     * @Title: submitEcInfoAjax
     * @Author: wujiamin
     * @date 2016年10月21日
     */
    @RequestMapping("submitEcInfoAjax")
    public void submitEcInfoAjax(String ipString, Long entId, String callbackUrl, Integer interfaceFlag,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<String> ips = parseIpList(ipString);
        Map result = new HashMap();
        if (entId == null) {
            result.put("msg", "企业ID为空");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise != null) {
            if (enterprise.getInterfaceApprovalStatus() != null
                    && enterprise.getInterfaceApprovalStatus().equals(InterfaceApprovalStatus.APPROVING.getCode())) {
                result.put("msg", "该企业已存在审核中的EC信息，无法重复提交!");
                response.getWriter().write(JSON.toJSONString(result));
                return;
            }
        }

        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.Ec_Approval.getCode());

        // 判断是否需要审核
        if (approvalProcessDefinition.getStage().toString().equals("0")) {
            logger.info("不需要审批，直接修改EC信息，审批流程id" + approvalProcessDefinition.getId());
            if (approvalRequestService.submitEcWithoutApproval(interfaceFlag,
                    createApprovalRequest(approvalProcessDefinition.getId(), entId, getCurrentUser().getId()), null,
                    createEcApprovalDetail(ips, entId, callbackUrl), ips)) {
                result.put("msg", "success");
                response.getWriter().write(JSON.toJSONString(result));
                return;
            } else {
                result.put("msg", "提交失败!");
                response.getWriter().write(JSON.toJSONString(result));
                return;
            }
        }

        List<ApprovalDetailDefinition> approvalRecordList = approvalDetailDefinitionService
                .getByApprovalProcessId(approvalProcessDefinition.getId());
        Role role = roleService.getRoleById(approvalRecordList.get(0).getRoleId());


        ApprovalRequest approvalRequest = createApprovalRequest(approvalProcessDefinition.getId(), entId, getCurrentUser().getId());

        if (approvalRequestService
                .submitEcApproval(
                        interfaceFlag,
                        approvalRequest,
                        createApprovalRecord(null, getCurrentUser().getId(), approvalProcessDefinition.getId(),
                                role.getName()), createEcApprovalDetail(ips, entId, callbackUrl))) {
            
            
            approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
            /*if (needECApprovalNotice()) {
                Long entMangerId = getCurrentUserManager().getId();

                // Long fatherManagerId =
                // managerService.selectByAdminId(currUserId).getParentId();
                Long fatherManagerId = managerService.selectManagerIdByEntIdAndRoleId(entMangerId, role.getRoleId());
                List<Long> manageIds = new ArrayList<Long>();
                manageIds.add(fatherManagerId);
                List<Administer> administers = administerService.getByManageIds(manageIds);
                for (Administer admininter : administers) {
                    String content = admininter.getUserName() + ", 您好！"
                            + globalConfigService.get(GlobalConfigKeyEnum.LOGO_NAME.getKey()) + "有企业EC接口审批任务待处理。";
                    if (!isSmsBlack(admininter.getMobilePhone(), SmsBlackType.Ec_Approval.getCode())
                            && !sendMsgService.sendMessage(admininter.getMobilePhone(), content,
                                    MessageType.APPROVAL_NOTICE.getCode())) {
                        logger.info("审批通知发送短信失败;手机号-" + admininter.getMobilePhone() + ", 审批人-"
                                + admininter.getUserName());
                    }
                }
            }*/
            result.put("msg", "success");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        result.put("msg", "提交失败!");
        response.getWriter().write(JSON.toJSONString(result));
        return;
    }

    /**
     * 解析ip，得到list
     */
    private ArrayList<String> parseIpList(String ip) {
        ArrayList<String> ips = JSON.parseObject(ip, ArrayList.class);
        ArrayList<String> ipList = new ArrayList();
        for (int i = 0; i < ips.size(); i++) {
            if (ips.get(i).length() > 3) {
                ipList.add(ips.get(i));
            }
        }
        return ipList;
    }

    /**
     * 创建EC信息修改详细内容
     *
     * @Title: createEcApprovalDetail
     * @Author: wujiamin
     * @date 2016年10月21日
     */
    private EcApprovalDetail createEcApprovalDetail(ArrayList<String> ips, Long entId, String callbackUrl) {
        if (ips.size() != 3 && ips.get(0).length() <= 3) {
            return null;
        }
        EcApprovalDetail detail = new EcApprovalDetail();
        if (!StringUtils.isEmpty(callbackUrl)) {
            detail.setCallbackUrl(callbackUrl);
        }
        detail.setEntId(entId);

        if (ips.size() == 3) {
            detail.setIp1(ips.get(0));
            detail.setIp2(ips.get(1));
            detail.setIp3(ips.get(2));
        }
        if (ips.size() == 2) {
            detail.setIp1(ips.get(0));
            detail.setIp2(ips.get(1));
        }
        if (ips.size() == 1) {
            detail.setIp1(ips.get(0));
        }

        detail.setDeleteFlag(0);

        return detail;
    }

    /**
     * submitAdminChangeAjax
     */
    @RequestMapping("submitAdminChangeAjax")
    public void submitAdminChangeAjax(HttpServletRequest request, HttpServletResponse resp, Long adminId)
            throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        if (adminId == null || administerService.isOverAuth(getCurrentAdminID(), adminId)) {
            map.put("submitRes", "fail");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        } else {
            List<AdminChangeOperator> adminChangeDetailList = adminChangeOperatorService.getByAdminId(adminId);
            Administer administer = administerService.selectAdministerById(adminId);
            if (adminChangeDetailList == null || adminChangeDetailList.isEmpty() || administer == null) {
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }

            AdminChangeDetail adminChangeDetail = createAdminChangeDetail(adminChangeDetailList.get(0), administer);

            // 检测手机号是否重复
            if (checkMobileExist(adminChangeDetail.getDestPhone())) {
                map.put("submitRes", "mobileConflict");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }

            Long currentId = getCurrentAdminID();

            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.Admin_Change_Approval.getCode());

            // 判断是否需要审核
            if (approvalProcessDefinition.getStage().toString().equals("0")) {
                logger.info("不需要审批，直接更新产品列表，审批流程id" + approvalProcessDefinition.getId());
                if (approvalRequestService.submitWithoutApproval(
                        createApprovalRequest(approvalProcessDefinition.getId(), adminChangeDetail.getEnterId(),
                                currentId), null, null, null, adminChangeDetail)) {
                    map.put("submitRes", "success");
                    resp.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }

            List<ApprovalDetailDefinition> approvalRecordList = approvalDetailDefinitionService
                    .getByApprovalProcessId(approvalProcessDefinition.getId());
            Role role = roleService.getRoleById(approvalRecordList.get(0).getRoleId());

            ApprovalRequest approvalRequest = createApprovalRequest(approvalProcessDefinition.getId(),
                    adminChangeDetail.getEnterId(), currentId);
            if (approvalRequestService
                    .submitApproval(
                            createApprovalRequest(approvalProcessDefinition.getId(), adminChangeDetail.getEnterId(),
                                    currentId),
                            createApprovalRecord(null, currentId, approvalProcessDefinition.getId(), role.getName()),
                            null, null, null, null, adminChangeDetail)) {
                approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
                map.put("submitRes", "success");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            } else {
                map.put("submitRes", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }

        }
    }

    /**
     * 创建AdminChangeDetail对象 注：requestId没有创建，需要后面代码生成requestId注入
     */
    private AdminChangeDetail createAdminChangeDetail(AdminChangeOperator adminChangeOperator, Administer admin) {
        AdminChangeDetail detail = new AdminChangeDetail();
        detail.setEnterId(adminChangeOperator.getEnterId());
        detail.setAdminId(admin.getId());
        detail.setComment(adminChangeOperator.getComment());
        detail.setDeleteFlag(0);
        detail.setDestName(adminChangeOperator.getDestName());
        detail.setDestPhone(adminChangeOperator.getDestPhone());
        detail.setSrcName(admin.getUserName());
        detail.setSrcPhone(admin.getMobilePhone());
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        return detail;
    }

    /**
     * @param id
     *            审核请求id
     */
    @RequestMapping("adminApprovalDetail")
    public String adminApprovalDetail(ModelMap modelmap, HttpServletRequest request, Long id) {
        Manager manager = getCurrentUserManager();
        if (id != null && !approvalRequestService.isOverAuth(getCurrentAdminID(), id)) {

            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);

            if (approvalRequest != null) {
                // 用户更改信息
                /*
                 * List<AdminChangeDetail> adminChangeDetails = new
                 * ArrayList<AdminChangeDetail>(); if (adminChangeDetails !=
                 * null) { modelmap.addAttribute("adminChangeDetails",
                 * adminChangeDetails); }
                 */
                AdminChangeDetail adminChangeDetail = adminChangeDetailService.getDetailByRequestId(id);
                if (adminChangeDetail == null) {
                    modelmap.addAttribute("errorMsg", "相关记录错误！");
                    modelmap.addAttribute("flag", 1);
                    return "entApproval/error.ftl";
                }

                modelmap.addAttribute("adminChangeDetail", adminChangeDetail);

                // 审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);

                if (!varifyRequestRecord(manager.getRoleId(), approvalRecords)) {
                    modelmap.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
                    modelmap.addAttribute("flag", 1);
                    return "entApproval/error.ftl";
                }

                // 审批意见
                modelmap.addAttribute("opinions", approvalRecords);
                // 待审核记录id
                modelmap.addAttribute("approvalRecordId", approvalRecords.get(approvalRecords.size() - 1).getId());
                // 审核请求记录id
                modelmap.addAttribute("requestId", id);
                // 审批流程id
                modelmap.addAttribute("processId", approvalRequest.getProcessId());

                modelmap.addAttribute("approvalType", Product_Change_Approval.getCode());
                // 传递标识：是否是自营的企业
                if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                    modelmap.addAttribute("flag", 1);
                } else {
                    modelmap.addAttribute("flag", 0);
                }

                String provinceFlag = getProvinceFlag();
                modelmap.addAttribute("provinceFlag", provinceFlag);
                return "adminChange/product_change_subject.ftl";

            }
            logger.info("数据库中没有相关审批记录Id: " + id);
        }
        return "redirect:approvalIndex.html?approvalType=8";
    }

    /**
     * @param approvalStatus
     *            1通过 0驳回
     */
    @RequestMapping(value = "/saveApprovalAdminChange")
    public String saveApprovalAdminChange(HttpServletRequest request, ModelMap map, Long enterId, String comment,
            String approvalStatus, Long requestId, Long approvalRecordId, Long processId) {
        Administer currentUser = getCurrentUser();

        if (requestId == null || StringUtils.isEmpty(approvalStatus) || StringUtils.isEmpty(comment)
                || approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            map.addAttribute("errorMsg", "审批提交参数异常，审批失败！");
            return "enterprises/product_change_subject.ftl";
        }

        /**
         * 1、更新当前要审批的记录（两张表：request的当前状态，record的审批意见）
         * 2、如果审批通过，判断当前审批是否完结，如果未完结，创建一条新的审批记录；如果完结，则更新enterprises表
         */
        ApprovalRecord newApprovalRecord = null;
        ApprovalRecord updateApprovalRecord = null;
        ApprovalRequest updateApprovalRequest = null;

        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

        ApprovalDetailDefinition currApprovalDetail = approvalDetailDefinitionService
                .getCurrentApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        ApprovalDetailDefinition nextApprovalDetail = approvalDetailDefinitionService
                .getNextApprovalDetailByProccessId(processId, approvalRequest.getStatus());
        if (currApprovalDetail == null) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Admin_Change_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }
        Role currApprovalRole = roleService.getRoleById(currApprovalDetail.getRoleId());
        AdminChangeDetail adminChangeDetail = null;
        if (Constants.APPROVAL_RESULT.PASS.getResult().equals(approvalStatus)) {
            // 当请求审核当前状态等于审核等级前置状态
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), currApprovalDetail
                    .getApprovalCode().intValue() + approvalRequest.getStatus().intValue(), approvalStatus,
                    nextApprovalDetail);
            updateApprovalRequest.setEntId(enterId);
            if (nextApprovalDetail == null) {
                // 当前审批是最后一级，更新企业产品表
                adminChangeDetail = adminChangeDetailService.getByRequestId(requestId);

            } else {
                Role nextApprovalRole = roleService.getRoleById(nextApprovalDetail.getRoleId());
                newApprovalRecord = createApprovalRecord(requestId, currentUser.getId(), processId,
                        nextApprovalRole.getName());
            }
        } else {
            // enterprise = initUpdateEnterprise(approvalRequest.getEntId(),
            // approvalStatus);
            //
            updateApprovalRequest = initUpdateApprovalRequest(approvalRequest.getId(), 0, approvalStatus,
                    nextApprovalDetail);
        }

        // 再次判断该条审批记录是否已经被其他人审批
        ApprovalRecord varifyRecord = approvalRecordService.selectByPrimaryKey(approvalRecordId);
        if (varifyRecord.getIsNew().toString().equals("0")) {
            // 已经被审批
            map.addAttribute("errorMsg", "该条记录已经被其他管理员处理！");
            map.addAttribute("flag", ApprovalType.Admin_Change_Approval.getCode().toString());
            return "entApproval/error.ftl";
        }
        updateApprovalRecord = initUpdateApprovalRecord(approvalRecordId, currentUser.getId(), comment,
                currApprovalRole.getName(), approvalStatus);

        try {
            if (approvalRequestService.updateLastRecordAndInsertNewRecord(updateApprovalRecord, updateApprovalRequest,
                    newApprovalRecord, null, null, null, null, null, null, null, null, adminChangeDetail)) {
                approvalRequestSmsService.sendNoticeSms(updateApprovalRequest.getId());
                logger.info("审核记录id:" + approvalRecordId + "审核成功");
                return "redirect:approvalIndex.html?approvalType=8";
            }
        } catch (RuntimeException e) {
            logger.info("审核记录id:" + approvalRecordId + "审核失败");
            return productApprovalDetail(map, request, requestId);
        }

        return adminApprovalDetail(map, request, requestId);
    }

    /**
     * 检测是否手机号重复，或者有已提交审核但是没有审核完毕
     */
    private boolean checkMobileExist(String mobile) {
        if (administerService.selectByMobilePhone(mobile) != null) {
            return true;
        }
        if (adminChangeDetailService.getVerifyingCountByMobile(mobile) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查找制卡审核记录
     * 
     * @author qinqinyan
     * @date 2017/08/07
     */
    @RequestMapping("searchMakecardRecords")
    public void searchMakecardRecords(QueryObject queryObject, HttpServletResponse response) {
        // 查询条件
        setQueryParameter("entCode", queryObject);// 企业编码
        setQueryParameter("entName", queryObject);// 企业名称
        setQueryParameter("result", queryObject);// 状态

        List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(getCurrentUserManager().getId());
        if (enterprises == null || enterprises.size() < 1) {
            // 如果是不负责管理企业的角色负责审核，则获取所有企业
            enterprises = enterprisesService.getNormalEnterpriseList();
        }

        long count = 0l;
        //List<ApprovalRequest> list = null;

        List<ApprovalRequest> newList = null;

        if (enterprises != null && enterprises.size() > 0) {
            queryObject.getQueryCriterias().put("type", ApprovalType.MDRC_MakeCard_Approval.getCode());
            queryObject.getQueryCriterias().put("enterprises", enterprises);

            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
            queryObject.getQueryCriterias().put("processId", approvalProcessDefinition.getId());

            

            List<ApprovalDetailDefinition> approvalDetails = approvalDetailDefinitionService
                    .getByApprovalProcessId(approvalProcessDefinition.getId());

            List<Integer> preconditions = new ArrayList<Integer>();
            for (ApprovalDetailDefinition approvalDetail : approvalDetails) {
                if (approvalDetail.getRoleId().toString().equals(getCurrentUserManager().getRoleId().toString())) {
                    // 存在同一种角色可以进行多级审批：如省级管理员可能可以进行三级和四级审批
                    preconditions.add(approvalDetail.getPrecondition());
                }
            }
            queryObject.getQueryCriterias().put("preconditions", preconditions);
            queryObject.getQueryCriterias().put("currentUserId", getCurrentUser().getId().toString());

            //edit by qinqinyan on 2017/11/10 for pdata-1910
            newList = approvalRequestService.getMakecardRecordsOrderBy(queryObject);
            
            count = approvalRequestService.countMakecardRecords(queryObject);
            //list = approvalRequestService.getMakecardRecords(queryObject);

            // 遍历是否可以进行审批
            /*for (ApprovalRequest item : list) {
                item.setCanOperate("0");
                if (item.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
                    // 正在审核中
                    for (Integer precondition : preconditions) {
                        // 判断是否能进行审批
                        if (item.getStatus().intValue() == precondition.intValue()
                                && !item.getCreatorId().toString().equals(getCurrentUser().getId().toString())) {
                            ApprovalRecord record = approvalRecordService.selectNewRecordByRequestId(item.getId(),
                                    getCurrentUser().getId());
                            if (record != null) {
                                item.setCanOperate("1");
                            }
                        }
                    }
                }
            }*/
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        //json.put("data", list);
        json.put("data", newList);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Title: searchRecord
     * @Description: EC审批记录
     * @param queryObject
     * @param response
     * @return: void
     */
    @RequestMapping("searchECRecord")
    public void searchECRecord(QueryObject queryObject, HttpServletResponse response, HttpServletRequest request) {
        // 查询条件
        setQueryParameter("entCode", queryObject);// 企业编码
        setQueryParameter("entName", queryObject);// 企业名称
        setQueryParameter("result", queryObject);// 状态

        if (isSdEnvironment()) {
            setQueryParameter("ipPart1", queryObject);
            setQueryParameter("ipPart2", queryObject);
            setQueryParameter("ipPart3", queryObject);
            setQueryParameter("ipPart4", queryObject);
            getIPFromQueryObject(queryObject);
        }

        queryObject.getQueryCriterias().put("type", ApprovalType.Ec_Approval.getCode());// 类型,默认为EC

        String managerIds = managerService.getChildNodeString(getCurrentUserManager().getId());// 当前角色下所有企业的审批记录
        queryObject.getQueryCriterias().put("managerIds", managerIds);
        long count = approvalRequestService.countECRecords(queryObject);
        List<ApprovalRequest> list = approvalRequestService.getECRecords(queryObject);

        // 填充地区信息
        Enterprise enterprise = null;
        Manager entManager = null;
        if (list != null && list.size() > 0) {
            for (ApprovalRequest record : list) {
                if (org.apache.commons.lang.StringUtils.isNotBlank(record.getEntCode())
                        && (enterprise = enterprisesService.selectByCode(record.getEntCode())) != null) {
                    // 地区全称
                    entManager = entManagerService.getManagerForEnter(enterprise.getId());
                    if (entManager != null) {
                        String fullname = "";
                        record.setEntDistrictName(managerService.getFullNameByCurrentManagerId(fullname,
                                entManager.getParentId()));
                    }
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        //json.put("data", list);
        if (isSdEnvironment()) {//是否山东环境
            List<EcApprovalRequest> sdList = sdGetEcApprovalRequest(list);
            json.put("data", sdList);
        } else {
            json.put("data", list);
        }
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getIPFromQueryObject
     */
    private void getIPFromQueryObject(QueryObject queryObject) {
        String ipPart1 = (String) queryObject.getQueryCriterias().get("ipPart1");
        String ipPart2 = (String) queryObject.getQueryCriterias().get("ipPart2");
        String ipPart3 = (String) queryObject.getQueryCriterias().get("ipPart3");
        String ipPart4 = (String) queryObject.getQueryCriterias().get("ipPart4");

        StringBuffer sb = new StringBuffer();
        sb.append(NumberUtils.isNumber(ipPart1) ? ipPart1 : "%");
        sb.append(".");
        sb.append(NumberUtils.isNumber(ipPart2) ? ipPart2 : "%");
        sb.append(".");
        sb.append(NumberUtils.isNumber(ipPart3) ? ipPart3 : "%");
        sb.append(".");
        sb.append(NumberUtils.isNumber(ipPart4) ? ipPart4 : "%");
        queryObject.getQueryCriterias().put("callbackAddr", sb.toString());

    }

    /**
     * 
     * @Title: searchActiveRecord
     * @Description: 激活审批记录
     * @param queryObject
     * @param response
     * @return: void
     */
    @RequestMapping("searchActiveRecord")
    public void searchActiveRecord(QueryObject queryObject, HttpServletResponse response) {
        // 查询条件
        setQueryParameter("enterpriseName", queryObject);// 企业编码
        setQueryParameter("serialNumber", queryObject);// 企业名称
        setQueryParameter("result", queryObject);// 审批

        List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(getCurrentUserManager().getId());
        if (enterprises == null || enterprises.size() < 1) {
            // 如果是不负责管理企业的角色负责审核，则获取所有企业
            enterprises = enterprisesService.getNormalEnterpriseList();
        }
        long count = 0;
        List<ApprovalRequest> list = new ArrayList<ApprovalRequest>();
        if (enterprises != null && enterprises.size() > 0) {
            queryObject.getQueryCriterias().put("type", ApprovalType.MDRC_Active_Approval.getCode());
            queryObject.getQueryCriterias().put("enterprises", enterprises);

            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.MDRC_Active_Approval.getCode());
            queryObject.getQueryCriterias().put("processId", approvalProcessDefinition.getId());

            count = approvalRequestService.countMdrcActiveRecords(queryObject.toMap());
            list = approvalRequestService.getMdrcActiveRecords(queryObject.toMap());

            List<ApprovalDetailDefinition> approvalDetails = approvalDetailDefinitionService
                    .getByApprovalProcessId(approvalProcessDefinition.getId());

            List<Integer> preconditions = new ArrayList<Integer>();
            for (ApprovalDetailDefinition approvalDetail : approvalDetails) {
                if (approvalDetail.getRoleId().toString().equals(getCurrentUserManager().getRoleId().toString())) {
                    // 存在同一种角色可以进行多级审批：如省级管理员可能可以进行三级和四级审批
                    preconditions.add(approvalDetail.getPrecondition());
                }
            }

            // 遍历是否可以进行审批
            for (ApprovalRequest item : list) {
                item.setCanOperate("0");
                if (item.getResult().toString().equals(ApprovalRequestStatus.APPROVING.getCode().toString())) {
                    // 正在审核中
                    for (Integer precondition : preconditions) {
                        // 判断是否能进行审批
                        if (item.getStatus().intValue() == precondition.intValue()
                                && !item.getCreatorId().toString().equals(getCurrentUser().getId().toString())) {
                            ApprovalRecord record = approvalRecordService.selectNewRecordByRequestId(item.getId(),
                                    getCurrentUser().getId());
                            if (record != null) {
                                item.setCanOperate("1");
                            }
                        }
                    }
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment() {
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }

    /**
     * 山东获取ec审批记录列表
     */
    private List<EcApprovalRequest> sdGetEcApprovalRequest(List<ApprovalRequest> list) {
        List<EcApprovalRequest> targetList = new ArrayList<EcApprovalRequest>();
        for (ApprovalRequest approvalRequest : list) {
            EcApprovalRequest ecApprovalRequest = EcApprovalRequest.init(approvalRequest);
            EcApprovalDetail ecApprovalDetail = ecApprovalDetailService.selectByRequestId(approvalRequest.getId());
            if (ecApprovalDetail != null) {
                ecApprovalRequest.setIp1(StringUtils.isEmpty(ecApprovalDetail.getIp1()) ? "-" : ecApprovalDetail
                        .getIp1());
                ecApprovalRequest.setIp2(StringUtils.isEmpty(ecApprovalDetail.getIp2()) ? "-" : ecApprovalDetail
                        .getIp2());
                ecApprovalRequest.setIp3(StringUtils.isEmpty(ecApprovalDetail.getIp3()) ? "-" : ecApprovalDetail
                        .getIp3());
                ecApprovalRequest.setCallbackUrl(StringUtils.isEmpty(ecApprovalDetail.getCallbackUrl()) ? "-"
                        : ecApprovalDetail.getCallbackUrl());
            }
            targetList.add(ecApprovalRequest);
        }
        return targetList;

    }
}

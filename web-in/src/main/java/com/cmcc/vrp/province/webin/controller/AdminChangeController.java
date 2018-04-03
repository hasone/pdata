package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.ProductChangeRequestStatus;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.model.AdminChangeDetail;
import com.cmcc.vrp.province.model.AdminChangeOperator;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.AdminChangeDetailService;
import com.cmcc.vrp.province.service.AdminChangeOperatorService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.Provinces;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * AdminChangeController 用户管理
 * 
 * 客户经理用户管理，修改管理员的手机号和姓名
 * 
 * @author qihang
 *
 */
@Controller
@RequestMapping("/manage/adminChange")
public class AdminChangeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AdminChangeController.class);
    
    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Autowired
    private ManagerService managerService;
    
    @Autowired
    private AdministerService administerService;
    
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    
    @Autowired
    ApprovalRequestService approvalRequestService;
    
    @Autowired
    AdminChangeOperatorService adminChangeOperatorService;
    
    @Autowired
    AdminChangeDetailService adminChangeDetailService;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    ApprovalRecordService approvalRecordService;
    
    @Autowired
    ApprovalDetailDefinitionService approvalDetailDefinitionService;
    
    @Autowired
    PhoneRegionService phoneRegionService;
    
    /**
     * 获取用户列表
     */
    @RequestMapping("/index")
    public String index(ModelMap modelMap, QueryObject queryObject) {

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识
        //当前用户的managerId
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            modelMap.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }
        
        //判断是否有提交审核的权限
        if (manager.getRoleId() != null) {
            /*Boolean authFlag = approvalProcessDefinitionService.hasAuthToSubmitApproval(manager.getRoleId(),
                    ApprovalType.Admin_Change_Approval.getCode());
            modelMap.addAttribute("authFlag", authFlag.toString());
            modelMap.addAttribute("managerId", manager.getId());*/
            modelMap.addAttribute("authFlag", "true");
        } else {
            modelMap.addAttribute("authFlag", "false");
        }
        
        
        //判断当前用户的manager是否有下属的manager
        if (managerService.getChildNodeString(manager.getId()) != null) {
            modelMap.addAttribute("tag", "true");
        } else {
            modelMap.addAttribute("tag", "false");
        }

        //判断是否是四川的平台
        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        if ("sc".equalsIgnoreCase(provinceFlag)) {
            modelMap.addAttribute("sc", "true");
        }

        modelMap.addAttribute("currentManagerId", manager.getId());

        //判断是否为客户经理
        Boolean isCustomManager = false;
        Long roleId = manager.getRoleId();
        if(roleId.toString().equals(getCustomManager())){
            isCustomManager = true;
        }
        modelMap.addAttribute("isCustomManager", isCustomManager.toString());
        
        //使用企业关键人企业列表中的更新，还是用户更改中的修改和提交审核
        String isUseEnterList = globalConfigService.get(GlobalConfigKeyEnum.ADMIN_CHANGE_USE_ENTERLIST.getKey());
        if("true".equals(isUseEnterList)){
            modelMap.addAttribute("isUseEnterList", "true");
        }else{
            modelMap.addAttribute("isUseEnterList", "false");
        }
        
        return "adminChange/index.ftl";
    }
    
    /**
     * 用户列表查找
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        /**
         * 查询参数: 姓名、手机号码
         */
        setQueryParameter("userName", queryObject);
        setQueryParameter("mobilePhone", queryObject);

        queryObject.getQueryCriterias().put("currentUserId", getCurrentUser().getId());
        queryObject.getQueryCriterias().put("managerId", getCurrentUserManager().getId());

        // 数据库查找符合查询条件的个数
        int administerCount = administerService.queryPaginationAdminCount(queryObject);
        List<Administer> administerList = administerService.queryPaginationAdminList(queryObject);

        if (administerList != null && administerList.size() > 0) {
            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.Admin_Change_Approval.getCode());
            
            
            for (Administer admin : administerList) {
                //用户信息模糊处理
                administerService.blurAdministerInfo(admin);
                
                //判断是否存在保存更改记录
                List<AdminChangeOperator> adminChangeOperators = 
                        adminChangeOperatorService.getByAdminId(admin.getId());
                
                if (adminChangeOperators != null && adminChangeOperators.size() > 0) {
                    admin.setAdminChangeStatus(ProductChangeRequestStatus.SAVED.getValue());
                    admin.setDescription("已保存变更记录");
                    continue;
                }
                
                //是否正在审核的记录
                if(approvalProcessDefinition == null){
                    admin.setAdminChangeStatus(ProductChangeRequestStatus.ONLYDETAIL.getValue());
                    admin.setDescription("");
                    continue;
                }
                
                

                List<Long> enterIds = enterprisesService.getEnterpriseIdByAdminId(admin);
                if(enterIds == null || enterIds.isEmpty()){
                    continue;
                }
                
                List<ApprovalRequest> approvalRequests = approvalRequestService.selectByEntIdAndProcessId(enterIds.get(0),
                        approvalProcessDefinition.getId());
                if (approvalRequests != null && approvalRequests.size() > 0) {
                    if (approvalRequests.get(approvalRequests.size() - 1).getDeleteFlag().toString().equals("1")) {
                        //最近的一条记录已经被驳回，即不存在审核中记录
                        admin.setAdminChangeStatus(ProductChangeRequestStatus.NOCHANGE.getValue());
                        admin.setDescription("已驳回");
                        continue;
                    } else if (approvalRequests.get(approvalRequests.size() - 1).getStatus().toString()
                            .equals(approvalProcessDefinition.getTargetStatus().toString())) {
                        //最近的一条记录审批完成，即不存在审批记录
                        admin.setAdminChangeStatus(ProductChangeRequestStatus.REJECT.getValue());
                        admin.setDescription("");
                        continue;
                    } else {
                        //存在审批中的记录
                        admin.setAdminChangeStatus(ProductChangeRequestStatus.APPROVING.getValue());
                        String statusDes = approvalRequestService.getCurrentStatus(approvalRequests
                                .get(approvalRequests.size() - 1));
                        admin.setDescription(statusDes);
                        continue;
                    }
                } else {
                    admin.setAdminChangeStatus(ProductChangeRequestStatus.NOCHANGE.getValue());
                    admin.setDescription("");
                }
            }
        }
        
        

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", administerList);
        json.put("total", administerCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /** 
     * @Title: 更改用户名和手机号
    */
    @RequestMapping(value = "/updateAdmin")
    public String updateAdmin(ModelMap model,Long adminId) {

        Administer administer = administerService.selectAdministerById(adminId);
        List<AdminChangeOperator> list = adminChangeOperatorService.getByAdminId(adminId);
        if(list!=null && !list.isEmpty()){
            AdminChangeOperator adminChangeOperator = list.get(0);
            administer.setMobilePhone(adminChangeOperator.getDestPhone());
            administer.setUserName(adminChangeOperator.getDestName());
            model.addAttribute("comment", adminChangeOperator.getComment());
        }

        model.addAttribute("admin", administer);
        
        List<Enterprise> listEnters = enterprisesService.getEnterpriseListByAdminId(administer);
        if(listEnters != null && !listEnters.isEmpty()){
            Enterprise enterprise = listEnters.get(0);
            model.addAttribute("enterName", enterprise.getName());
        }
        


        return "adminChange/updateAdmin.ftl";

    }
    
    /**
     * 保存用户
     */
    @RequestMapping(value = "/saveAdmin")
    public String saveAdmin(ModelMap model,AdminChangeOperator adminChangeOperator){
        if(!checkValidate(adminChangeOperator)){
            model.addAttribute("errMsg", "参数为空");
            return updateAdmin(model, adminChangeOperator.getAdminId());
        }
        //检测手机号
        Provinces configProvince = Provinces.fromCode(getPhoneRegion());//全局变量里面设置的省，映射到Provinces的枚举中
        if (needCheckPhoneRegion() && configProvince!=null) {
            
            PhoneRegion phoneRegion = phoneRegionService.query(adminChangeOperator.getDestPhone());
            
            String provinceCode = (phoneRegion == null)
                    ? Provinces.Unknown.getCode()
                    : Provinces.fromName(phoneRegion.getProvince()).getCode();
            String supplier = (phoneRegion == null)
                    ? Provinces.Unknown.getCode()
                    : phoneRegion.getSupplier(); 
                    
            if (!provinceCode.equals(configProvince.getCode())
                    || !"M".equals(supplier)) {

                model.addAttribute("errMsg", "请输入"+configProvince.getName()+"省手机号");
                return updateAdmin(model, adminChangeOperator.getAdminId());
            }
        }
        
        if(checkMobileExist(adminChangeOperator.getDestPhone())){
            model.addAttribute("errMsg", "该用户已存在");
            return updateAdmin(model, adminChangeOperator.getAdminId());
        }
        
        //通过
        adminChangeOperator.setDeleteFlag(0);
        
        Administer admin = new Administer();
        admin.setId(adminChangeOperator.getAdminId());
        List<Long> enterIds = enterprisesService.getEnterpriseIdByAdminId(admin);
        if(enterIds == null || enterIds.isEmpty()){
            model.addAttribute("errMsg", "插入失败");
            return updateAdmin(model, adminChangeOperator.getAdminId());
        }
        
        try{
            adminChangeOperator.setEnterId(enterIds.get(0));
            if(!adminChangeOperatorService.insertAndDelByAdminId(adminChangeOperator)){
                model.addAttribute("errMsg", "插入失败");
                return updateAdmin(model, adminChangeOperator.getAdminId());
            }
        }catch(TransactionException e){
            model.addAttribute("errMsg", "插入失败");
            return updateAdmin(model, adminChangeOperator.getAdminId());
        }

        return "redirect:index.html";
        
    }
    
    /**
    * 检测是否手机号重复，或者有已提交审核但是没有审核完毕
    *
    */
    @RequestMapping(value = "/checkMobileExistAjax")
    public void checkMobileExistAjax(String mobile,
                                   HttpServletResponse resp) throws IOException {

        resp.setContentType("text/xml;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = resp.getWriter();
        
        if (!StringUtils.isValidMobile(mobile)) {
            pw.print("非合法手机号，请重新输入");
        } else if(checkMobileExist(mobile)){
            pw.print("手机号已存在，请重新输入");
        }else {
            pw.print("true");
        }
    }
    
    
    /**
     * 检测是否手机号重复，或者有已提交审核但是没有审核完毕
     */
    private boolean checkMobileExist(String mobile){
        if(administerService.selectByMobilePhone(mobile) != null){
            return true;
        }
        if(adminChangeDetailService.getVerifyingCountByMobile(mobile)>0){
            return true;
        }
        return false;
    }
    
    /**
     * 检查页面提交是否有效
     */
    private Boolean checkValidate(AdminChangeOperator adminChangeOperator){
        if(adminChangeOperator.getAdminId() == null || adminChangeOperator.getDestName() == null
                || adminChangeOperator.getComment() ==null || adminChangeOperator.getDestPhone() == null){
            return false;
        }
        return true;
    }
    
    
    
    /** 
     *  详情
    */
    @RequestMapping(value = "/detail")
    public String showChangeDetail(ModelMap model,Long adminId) {
        Manager manager = managerService.getManagerByAdminId(adminId);
        Manager currentManager = getCurrentUserManager();
        if (manager == null
                || currentManager == null
                || !managerService.isParentManage(manager.getId(), currentManager.getId())) {
            model.put("errorMsg", "请求异常");
            return "error.ftl";
        }
        
        model.addAttribute("adminId", adminId);
        Administer administer = administerService.selectAdministerById(adminId);

        //用户信息模糊化处理
        administerService.blurAdministerInfo(administer);
        model.addAttribute("admin", administer);
        
        String roleName = adminRoleService.getRoleNameByAdminId(adminId);
        model.addAttribute("roleName", roleName);
        

//        List<Enterprise> listEnters = enterprisesService.getEnterpriseListByAdminId(administer);
//        if (listEnters != null && !listEnters.isEmpty()) {
//            Enterprise enterprise = listEnters.get(0);
//            model.addAttribute("enterName", enterprise.getName());
//        }

        
        List<AdminChangeOperator> listAdminChanges = adminChangeOperatorService.getByAdminId(adminId);

        //修改审核中，如需查看具体信息请点击上方修改历史
        //具有审核中的记录时，文案才显示，如果支持修改则不显示该文案
        /*if(!listAdminChanges.isEmpty()){
            AdminChangeOperator adOperator = listAdminChanges.get(0);
            administer.setUserName(adOperator.getDestName());
            administer.setMobilePhone(adOperator.getDestPhone());
        }else{
            int verifyingCount = adminChangeDetailService.getVerifyingCount(adminId);
            model.addAttribute("verifyingCount", verifyingCount);
        }*/
        if(listAdminChanges.isEmpty()){
            int verifyingCount = adminChangeDetailService.getVerifyingCount(adminId);
            model.addAttribute("verifyingCount", verifyingCount);
        }
        
        //使用企业关键人企业列表中的更新，还是用户更改中的修改和提交审核
        String isUseEnterList = globalConfigService.get(GlobalConfigKeyEnum.ADMIN_CHANGE_USE_ENTERLIST.getKey());
        if("true".equals(isUseEnterList)){
            model.addAttribute("isUseEnterList", "true");
        }else{
            model.addAttribute("isUseEnterList", "false");
        }
        
        //添加角色Id 
        model.addAttribute("roleId", adminRoleService.getRoleIdByAdminId(adminId));
        
        
        //判断是否有审核中的记录
        model.addAttribute("administer", administer);
        return "adminChange/change_detail.ftl";

    }
    
    /**
     * 历史列表
     */
    @RequestMapping("/historyList")
    public String historyList(ModelMap modelMap, QueryObject queryObject,Long adminId) {
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        
        if(adminId == null){
            modelMap.put("errorMsg", "参数错误");
            return "error.ftl";
        }
        
        modelMap.addAttribute("adminId", adminId);
        
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.Admin_Change_Approval.getCode());
        

        if(approvalProcessDefinition!=null && approvalProcessDefinition.getTargetStatus() > 0){
            modelMap.addAttribute("needVirify", "true");
        }

        return "adminChange/historyList.ftl";
    }
    
    /**
     * 历史记录列表
     */
    @RequestMapping(value = "/searchHistory")
    public void searchHistory(QueryObject queryObject, HttpServletResponse res) {
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.Admin_Change_Approval.getCode());
        
        boolean needVirify = false;
        if(approvalProcessDefinition!=null && approvalProcessDefinition.getTargetStatus() > 0){
            needVirify = true;
        }
        
        setQueryParameter("adminId", queryObject);
        
        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime") + " 00:00:00");
        }
        
        if (!StringUtils.isEmpty(getRequest().getParameter("endTime"))) {
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime") + " 23:59:59");
        }
        
        List<AdminChangeDetail> list = adminChangeDetailService.queryPaginationAdminList(queryObject,needVirify);
        int count = adminChangeDetailService.queryPaginationAdminCount(queryObject,needVirify);
       
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * @param id 审核请求id
     */
    @RequestMapping("historyDetail")
    public String historyDetail(ModelMap modelmap, HttpServletRequest request, Long id) {
        if (id != null) {

            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);
            
            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.Admin_Change_Approval.getCode());

            if (approvalRequest != null) {
                // 用户更改信息
                /*List<AdminChangeDetail> adminChangeDetails = new ArrayList<AdminChangeDetail>();
                if (adminChangeDetails != null) {
                    modelmap.addAttribute("adminChangeDetails", adminChangeDetails);
                }*/
                AdminChangeDetail adminChangeDetail = adminChangeDetailService.getDetailByRequestId(id);
                if(adminChangeDetail == null){
                    modelmap.addAttribute("errorMsg", "相关记录错误！");
                    modelmap.addAttribute("flag", 1);
                    return "entApproval/error.ftl";
                }
                
                modelmap.addAttribute("adminChangeDetail", adminChangeDetail);
                
                if(approvalProcessDefinition != null && approvalProcessDefinition.getTargetStatus()>0){
                    // 审批记录列表
                    List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(id);     
                    //审批意见
                    modelmap.addAttribute("opinions", approvalRecords);
                    // 待审核记录id
                    modelmap.addAttribute("approvalRecordId", approvalRecords.get(approvalRecords.size() - 1).getId());
                    // 审核请求记录id
                    modelmap.addAttribute("requestId", id);
                    // 审批流程id
                    modelmap.addAttribute("processId", approvalRequest.getProcessId());
                }

                return "adminChange/historyDetail.ftl";
               
            }
            logger.info("数据库中没有相关审批记录Id: " + id);
        }
        return "redirect:historyList.html?adminId=" + id;
    }
    
}

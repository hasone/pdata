package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.province.dao.AdminDistrictMapper;
import com.cmcc.vrp.province.dao.AdminManagerEnterMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.AuthorityService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PotentialCusterService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.QueryObject;
import com.google.gson.Gson;


/**
 * 潜在客户服务类
 * <p>
 * Created by sunyiwei on 2016/3/28.
 */
@RequestMapping("/manage/potentialCustomer")
@Controller
public class PotentialCustomerController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PotentialCustomerController.class);
    private final Byte entType = 1;
    @Autowired
    PotentialCusterService potentialCusterService;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    AdminDistrictMapper adminDistrictMapper;

    @Autowired
    AdminRoleService adminRoleService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    ProductService productService;

    @Autowired
    AccountService accountService;

    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;

    @Autowired
    AdministerService administerService;

    @Autowired
    AdminManagerEnterMapper adminManagerEnterMapper;

    @Autowired
    AuthorityService authorityService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private AdminManagerService adminManagerService;
    @Autowired
    private EntManagerService entManagerService;

    /** 
     * @Title: indexPotential 
    */
    @RequestMapping("indexPotential")
    public String indexPotential(ModelMap modelmap, QueryObject queryObject) {
        
        if(queryObject != null){
            modelmap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        Administer currentAdmin = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if (currentAdmin == null) {
            return getLoginAddress();
        }
        Boolean submitApprovalFlag = false;
        Boolean hasAuthToFillInQuafilication = false;
        Boolean hasAuthToFillInCooperation = false;
        Boolean hasAuthToFillInForProvince = false;
        Boolean hasAuthToEdit = false;

        if (manager != null) {
            //判断是否有发起审核流程的权限
            submitApprovalFlag = approvalProcessDefinitionService.hasAuthToSubmitApproval(manager.getRoleId(), ApprovalType.Enterprise_Approval.getCode());

            //传递标识：是否是自营的企业
            if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                modelmap.addAttribute("flag", 1);

                hasAuthToFillInQuafilication = enterprisesService.hasAuthToFillInQuafilication(manager.getRoleId());
                hasAuthToFillInCooperation = enterprisesService.hasAuthToFillInCooperation(manager.getRoleId());

            } else {
                modelmap.addAttribute("flag", 0);

                hasAuthToFillInForProvince = enterprisesService.hasAuthToFillInForProvince(manager.getRoleId());
            }

            hasAuthToEdit = enterprisesService.hasAuthToEdit(manager.getRoleId());
        }
        modelmap.addAttribute("submitApprovalFlag", submitApprovalFlag.toString());
        modelmap.addAttribute("quafilicationFlag", hasAuthToFillInQuafilication.toString());
        modelmap.addAttribute("cooperationFlag", hasAuthToFillInCooperation.toString());
        modelmap.addAttribute("entInfoFlag", hasAuthToFillInForProvince.toString());
        modelmap.addAttribute("editFlag", hasAuthToEdit.toString());

        return "potentialCustomer/index_potential.ftl";
    }

    /** 
     * @Title: searchPotential 
    */
    @RequestMapping(value = "searchPotential")
    public void searchPotential(QueryObject queryObject, HttpServletResponse res, Integer status) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        // 设置前端筛选字段
        setQueryParameter("name", queryObject);
        setQueryParameter("deleteFlag", queryObject);

        JSONObject json = new JSONObject();

        /**
         * 当前登录用户
         */
        Manager currentManager = getCurrentUserManager();
        Long enterpriseCount = 0L;
        List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
        if (currentManager == null) {
            logger.info("当前用户无管理员身份");
        } else {
            enterpriseCount = enterprisesService.countPotentialEnterList(queryObject, currentManager.getId());
            enterpriseList = enterprisesService.queryPotentialEnterList(queryObject, currentManager.getId());
        }

        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", enterpriseList);
        json.put("total", enterpriseCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 企业搜索框查询
     *
     * @param name     企业名称
     * @param response 响应对象
     */
    @RequestMapping("/query")
    public void query(@RequestParam("keyword") String name, HttpServletResponse response) {
        if (StringUtils.isBlank(name)) {
            return;
        }

        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("name", transferQuery(name));

        //当前登录用户
        Manager currentManager = getCurrentUserManager();
        List<Enterprise> enterpriseList = enterprisesService.queryPotentialEnterList(queryObject, currentManager.getId());
        List<Info> infos = convert(enterpriseList);
        if (infos == null || infos.isEmpty()) {
            return;
        }

        try {
            StreamUtils.copy(new Gson().toJson(infos), Charsets.UTF_8, response.getOutputStream());
        } catch (Exception e) {
            logger.error("输出响应时出错，错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * 将企业对象转化成前端需要的格式
     *
     * @param ents 企业列表
     * @return
     */
    private List<Info> convert(List<Enterprise> ents) {
        if (ents == null || ents.isEmpty()) {
            return null;
        }

        List<Info> infos = new LinkedList<Info>();
        for (Enterprise ent : ents) {
            infos.add(new Info(ent.getName(), ent.getName()));
        }

        return infos;
    }

    /**
     * 编辑潜在企业
     *
     * @param model
     * @param enterpriseId
     * @param enterprise
     * @return
     */
    @RequestMapping("editPotential")
    public String editPotential(ModelMap model, Long enterpriseId, Enterprise enterprise) {
        //判断登录用户是否有修改该企业的权限


        enterprise = enterprisesService.selectByPrimaryKey(enterpriseId);
        if (enterprise == null) {
            return "potentialCustomer/index_potential.ftl";
        }

        //获取历史审批记录
        List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(enterpriseId, ApprovalType.Enterprise_Approval.getCode());
        model.addAttribute("approvalRecords", approvalRecords);
        if (approvalRecords != null && approvalRecords.size() > 0) {
            model.addAttribute("hasApproval", "true");
        } else {
            model.addAttribute("hasApproval", "false");
        }

        //获取企业管理员用户       
        List<Administer> emList = adminManagerService.getAdminForEnter(enterpriseId);
        if (emList != null
            && emList.size() != 0) {
            enterprise.setEnterpriseManagerName(emList.get(0).getUserName());
            enterprise.setEnterpriseManagerPhone(emList.get(0).getMobilePhone());
        }

        Administer cm = administerService.selectByMobilePhone(enterprise.getCmPhone());
        Manager manager = new Manager();
        if (cm != null) {
            manager = managerService.selectByAdminId(cm.getId());
        }

        //判断是否有权限操作该企业
        if (!hasAuthToEdit(enterpriseId)) {
            model.addAttribute("errorMsg", "对不起，您无权限操作该企业！");
            return "error.ftl";
        }

        model.put("pc", enterprise);

        model.put("cmManager", manager);

        return "potentialCustomer/edit_potential.ftl";
    }

    private boolean hasAuthToEdit(Long enterId) {
        Manager currManager = getCurrentUserManager();
        List<Enterprise> enterprises = enterprisesService.getAllEnterByManagerId(currManager.getId());
        if (enterprises != null && enterprises.size() > 0) {
            for (Enterprise item : enterprises) {
                if (item.getId().toString().equals(enterId.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 保存新建潜在企业信息
     *
     * @param map
     * @param enterprise
     * @param request
     * @return
     */
    @RequestMapping(value = "saveEditPotential")
    public String saveEditPotential(ModelMap map, Enterprise enterprise, HttpServletRequest request) {
        //1. 得到当前登录用户
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        String cmPhone = request.getParameter("customerManagerPhone");
        String emPhone = request.getParameter("enterpriseManagerPhone");
        String emName = request.getParameter("enterpriseManagerName");

        String managerId = request.getParameter("managerId");
        if (StringUtils.isEmpty(managerId)) {
            map.addAttribute("errorMsg", "企业上级管理员范围为空!");
            return editPotential(map, enterprise.getId(), enterprise);
        }

        //客户经理号码为空随机分配一个号码
        if (StringUtils.isEmpty(cmPhone)) {
            //根据managerId随机分配一个号码为客户经理
            Boolean flag = authorityService.ifHaveAuthority(Long.parseLong(managerId), "ROLE_ENTERPRISE_MANAGER_PARENT");
//            JSONObject jsonObject = new JSONObject();

            if (flag) {
                List<Long> adminIds = adminManagerService.selectAdminIdByManagerId(Long.parseLong(managerId));
                if (adminIds != null && adminIds.size() > 0) {
                    Administer admin = administerService.selectAdministerById(adminIds.get(0));
                    cmPhone = admin.getMobilePhone();
                } else {
                    map.addAttribute("errorMsg", "该地区没有暂时客户经理");
                    return editPotential(map, enterprise.getId(), enterprise);
                }
            } else {
                map.addAttribute("errorMsg", "请选择到区");
                return editPotential(map, enterprise.getId(), enterprise);
            }
        }

        // 2.检验所有参数是否通过校验
        if (!validatePotEnterprise(map, managerId, enterprise, cmPhone, emPhone, emName)) {
            return editPotential(map, enterprise.getId(), enterprise);
        }

        // 3.更新潜在企业、企业管理员、客户经理的信息
        Enterprise enterprised = enterprisesService.selectByPrimaryKey(enterprise.getId());

        String name;
        String entName;
        String phone;
        String email;
        Long adminId;
        if (StringUtils.isNotBlank(name = enterprise.getName())) {
            enterprised.setName(name);
        }
        if (StringUtils.isNotBlank(entName = enterprise.getEntName())) {
            enterprised.setEntName(entName);
        }
        if (StringUtils.isNotBlank(phone = enterprise.getPhone())) {
            enterprised.setPhone(phone);
        }
        if (StringUtils.isNotBlank(email = enterprise.getEmail())) {
            enterprised.setEmail(email);
        }
        if ((adminId = administer.getId()) != null) {
            enterprised.setCreatorId(adminId);
        }

        enterprised.setCmPhone(cmPhone);

        try {
            if (potentialCusterService.saveEditPotential(enterprised, cmPhone, emPhone, emName, administer.getId())) {
                logger.info("用户ID:{}编辑潜在用户成功，enterpriseId={}，cmPhone={}，emPhone={}，emName={}",
                    administer.getId(), enterprised.getId(), cmPhone, emPhone, emName);
                return "redirect:indexPotential.html";
            }
        } catch (Exception e) {
            map.put("errorMsg", "编辑潜在用户失败！" + e.getMessage());
            return editPotential(map, enterprise.getId(), enterprise);
        }
        map.put("errorMsg", "编辑潜在用户失败！");
        return editPotential(map, enterprise.getId(), enterprise);

    }

    /** 
     * @Title: savePotentialEnterprise 
    */
    @RequestMapping(value = "savePotential", method = RequestMethod.POST)
    public String savePotentialEnterprise(ModelMap map, Enterprise enterprise, HttpServletRequest request) {
        //1. 得到当前登录用户
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        String cmPhone = request.getParameter("customerManagerPhone");
        String emPhone = request.getParameter("enterpriseManagerPhone");
        String emName = request.getParameter("enterpriseManagerName");

        String managerId = request.getParameter("managerId");

        //客户经理号码为空随机分配一个号码
        if (StringUtils.isEmpty(cmPhone)) {
            //根据managerId随机分配一个号码为客户经理
            Boolean flag = authorityService.ifHaveAuthority(Long.parseLong(managerId), "ROLE_ENTERPRISE_MANAGER_PARENT");
            if (flag) {
                List<Long> adminIds = adminManagerService.selectAdminIdByManagerId(Long.parseLong(managerId));
                if (adminIds != null && adminIds.size() > 0) {
                    Administer admin = administerService.selectAdministerById(adminIds.get(0));
                    cmPhone = admin.getMobilePhone();
                } else {
                    map.addAttribute("errorMsg", "该地区没有暂时客户经理");
                    return addCustomer(map, enterprise, request);
                }
            } else {
                map.addAttribute("errorMsg", "请选择到区");
                return addCustomer(map, enterprise, request);
            }
        }

        // 2.检验所有参数是否通过校验
        if (!validatePotEnterprise(map, managerId, enterprise, cmPhone, emPhone, emName)) {
            return addCustomer(map, enterprise, request);
        }

        enterprise.setCmPhone(cmPhone);
        // 3.初始化潜在企业的信息
        initPotentialEnt(enterprise, administer);

        // 4.创建潜在企业
        try {
            if (potentialCusterService.savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, administer.getId())) {
                logger.info("用户ID:" + getCurrentUser().getId() + " 创建新企业成功"
                    + " 企业名称：" + enterprise.getName() + " 企业编码：" + enterprise.getCode()
                    + "企业品牌名：" + enterprise.getEntName());
                return "redirect:" + "/manage/potentialCustomer/indexPotential.html";
            } else {
                map.addAttribute("errorMsg", "创建潜在用户失败！");
                return addCustomer(map, enterprise, request);
            }
        } catch (Exception e) {
            map.addAttribute("errorMsg", "创建潜在用户失败！" + e.getMessage());
            return addCustomer(map, enterprise, request);
        }
    }


    /**
     * 初始化体验企业的信息
     *
     * @param enterprise
     * @param administer
     */
    private void initPotentialEnt(Enterprise enterprise, Administer administer) {
        //enterprise.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
        //enterprise.setAppKey(UUID.randomUUID().toString().replace("-", ""));
        enterprise.setStatus(entType);
        enterprise.setCreateTime(new Date());
        enterprise.setUpdateTime(new Date());
        enterprise.setDeleteFlag(EnterpriseStatus.PROBATION.getCode());
        enterprise.setCreatorId(administer.getId());
        //是自营的企业,需要填入默认的
        if (getProvinceFlag().equals(zyProvinceFlagValue)) {
            enterprise.setCode(UUID.randomUUID().toString().replace("-", ""));
        }

    }

    /** 
     * @Title: check 
    */
    @RequestMapping(value = "/check")
    public void check(HttpServletResponse response, Enterprise enterprise)
        throws IOException {
        Boolean bFlag = checkUnique(enterprise);
        response.getWriter().write(bFlag.toString());
    }

    private boolean checkUnique(Enterprise enterprise) {
        if (enterprise == null) {
            return true;
        }
        return !enterprisesService.countExists(enterprise.getId(), enterprise.getName(),
            enterprise.getCode(), enterprise.getPhone());
    }

    /** 
     * @Title: checkEnterpriseManagerValid 
    */
    @RequestMapping(value = "/checkEnterpriseManagerValid")
    public void checkEnterpriseManagerValid(HttpServletResponse resp, String enterpriseManagerPhone)
        throws IOException {
        String str = "true";
        if (!com.cmcc.vrp.util.StringUtils.isValidMobile(enterpriseManagerPhone)) {
            str = "false";
            resp.getWriter().write(str);
            return;
        }
        //判断该用户身份
        Administer admin = administerService.selectByMobilePhone(enterpriseManagerPhone);

        //该号码已经存在
        if (admin != null) {
            Long managerId = adminManagerService.selectManagerIdByAdminId(admin.getId());
            if (managerId != null && !managerId.equals(-1L)) {
                str = "false";
            }
            Long roleId = adminRoleService.getRoleIdByAdminId(admin.getId());
            if (roleId != null && !roleId.equals(-1L)) {
                str = "false";
            }
        }
        resp.getWriter().write(str);
    }

    /** 
     * @Title: checkCustomerManagerValid 
    */
    @RequestMapping(value = "/checkCustomerManagerValid")
    public void checkCustomerManagerValid(ModelMap model, HttpServletResponse resp, String customerManagerPhone)
        throws IOException {
        String str = "true";
        Administer admin = administerService.selectByMobilePhone(customerManagerPhone);

        if (admin == null) {
            str = "false";
            resp.getWriter().write(str);
            return;
        } else {
            Manager manager = managerService.selectByAdminId(admin.getId());
            if (manager == null) {
                model.addAttribute("errorMsg", "客户经理手机号码的用户无客户经理身份");
                str = "false";
                resp.getWriter().write(str);
                return;
            } else {
                //查找该用户的管理员身份所对应的角色，是否具有成为企业管理员父节点的权限
                boolean roleFlag = authorityService.ifHaveAuthority(manager.getId(), "ROLE_ENTERPRISE_MANAGER_PARENT");

                if (!roleFlag) {
                    str = "false";
                    resp.getWriter().write(str);
                    return;
                } else {
                    resp.getWriter().write(str);
                    return;
                }

            }
        }
    }


    /**
     * 根据号码获取管理员区域
     *
     * @param response
     * @param phone
     * @throws IOException
     * @date 2016年7月22日
     * @author wujiamin
     */
    @RequestMapping(value = "getCustomerPhoneArea")
    public void getCustomerPhoneArea(HttpServletResponse response, String phone) throws IOException {
        Administer admin;
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isBlank(phone)
            || !com.cmcc.vrp.util.StringUtils.isValidMobile(phone)
            || (admin = administerService.selectByMobilePhone(phone)) == null) {
            response.getWriter().write(jsonObject.toString());
            return;
        } else {
            Long adminId = admin.getId();
            Manager manager = managerService.selectByAdminId(adminId);

            if (manager != null) {
                jsonObject.put("managerId", manager.getId());
                jsonObject.put("managerName", manager.getName());
            }
        }
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据管理员区域获取号码
     *
     * @param response
     * @param phone
     * @throws IOException
     * @date 2016年7月22日
     * @author wujiamin
     */
    @RequestMapping(value = "getAreaPhone")
    public void getAreaPhone(HttpServletResponse response, Long managerId) throws IOException {
        if (managerId == null) {
            return;
        }

        Boolean flag = authorityService.ifHaveAuthority(managerId, "ROLE_ENTERPRISE_MANAGER_PARENT");
        JSONObject jsonObject = new JSONObject();

        if (flag) {
            List<Long> adminIds = adminManagerService.selectAdminIdByManagerId(managerId);
            if (adminIds != null && adminIds.size() > 0) {
                Administer admin = administerService.selectAdministerById(adminIds.get(0));
                jsonObject.put("phone", admin.getMobilePhone());
            } else {
                jsonObject.put("error", "该地区没有暂时客户经理");
            }
        } else {
            jsonObject.put("error", "请选择到区");
        }


        try {
            response.getWriter().write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 校验潜在客户
     *
     * @param model
     * @param enterprise
     * @return
     */
    private boolean validatePotEnterprise(ModelMap model, String managerId, Enterprise enterprise, String CMPhone, String EMPhone, String EMName) {
        if (enterprise == null) {
            model.addAttribute("errorMsg", "无效的企业对象!");
            return false;
        }
        if (StringUtils.isEmpty(managerId)) {
            model.addAttribute("errorMsg", "企业上级管理员范围为空!");
            return false;
        }

        try {
            enterprise.selfCheck();
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return false;
        }

        return checkCM2Ent(model, Long.parseLong(managerId), CMPhone) && checkEM2Ent(model, enterprise, EMPhone, EMName);
    }

    /**
     * 校验企业与客户经理之间的关系
     *
     * @param model
     * @param enterprise
     * @return
     */
    private boolean checkCM2Ent(ModelMap model, Long managerId, String cmPhone) {
        //判断用户是否已经有managerId
        Administer admin = administerService.selectByMobilePhone(cmPhone);
        if (admin == null) {
            model.addAttribute("errorMsg", "客户经理的手机号码不存在");
            logger.info("该号码的客户经理不存在phone={}", cmPhone);
            return false;
        } else {
            Manager manager = managerService.selectByAdminId(admin.getId());
            if (manager == null) {
                model.addAttribute("errorMsg", "客户经理手机号码的用户无客户经理身份");
                logger.info("该号码的用户没有管理员身份phone={},adminId={}", cmPhone, admin.getId());
                return false;
            } else {
                //查找该用户的管理员身份所对应的角色，是否具有成为企业管理员父节点的权限
                boolean roleFlag = authorityService.ifHaveAuthority(managerId, "ROLE_ENTERPRISE_MANAGER_PARENT");

                if (roleFlag) {
                    //检查该手机号码用户的managerId是否选定的管理员的managerId
                    if (!managerId.equals(manager.getId())) {
                        model.addAttribute("errorMsg", "所选管理员区域与填写的手机号码不符");
                        logger.info("所选管理员区域与填写的手机号码不符phone={},phone.managerId={},managerId={}", cmPhone, manager.getId(), managerId);
                        return false;
                    }
                } else {
                    model.addAttribute("errorMsg", "客户经理手机号码对应的用户不能成为企业的上级");
                    logger.info("客户经理手机号码对应的用户的角色，不能成为企业管理员父节点");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 检查企业与企业管理员之间的关系
     *
     * @param model
     * @param enterprise
     * @param EMPhone
     * @return
     */
    private boolean checkEM2Ent(ModelMap model, Enterprise enterprise, String emPhone, String emName) {

        //判断用户是否已经有managerId
        Administer admin = administerService.selectByMobilePhone(emPhone);
        if (admin == null) {
            return true;
        } else {
            //企业ID已存在，说明是编辑
            if (enterprise.getId() != null) {
                //如果企业管理员的用户已经是该企业的企业管理员对应的用户，就不报错
                List<Long> adminIds = adminManagerService.getAdminIdForEnter(enterprise.getId());
                if (adminIds != null && adminIds.size() > 0 && adminIds.contains(admin.getId())) {
                    return true;
                }
            }

            Manager manager = managerService.selectByAdminId(admin.getId());
            if (manager != null && !manager.getId().equals(-1L)) {
                model.addAttribute("errorMsg", "企业管理员手机号码的用户已有其他职位");
                return false;
            }

        }

        return true;
    }

    /**
     * 新增潜在企业
     *
     * @param modelMap
     * @param enterprise
     * @return
     */
    @RequestMapping(value = "addPotential")
    public String addCustomer(ModelMap modelMap, Enterprise enterprise, HttpServletRequest request) {
        Administer admin = getCurrentUser();
        Manager manager = getCurrentUserManager();
        Boolean flag = false;
        if (manager != null) {
            flag = authorityService.ifHaveAuthority(manager.getId(), "ROLE_ENTERPRISE_MANAGER_PARENT");
        }

        if (flag) {
            enterprise.setCustomerManagerPhone(admin.getMobilePhone());
        }
        modelMap.addAttribute("pc", enterprise);

        return "potentialCustomer/add_potential.ftl";
    }

    /**
     * 查看潜在企业详情
     *
     * @param modelMap
     * @param enterpriseId
     * @param enterprise
     * @return
     */
    @RequestMapping(value = "potentailDetail")
    public String showPotentialDetail(ModelMap modelMap, Long enterpriseId, Enterprise enterprise) {
        enterprise = enterprisesService.selectByPrimaryKey(enterpriseId);
        List<Administer> emList = administerService.selectEMByEnterpriseId(enterpriseId);
        if (enterprise != null
            && emList != null
            && emList.size() != 0) {
            enterprise.setEnterpriseManagerName(emList.get(0).getUserName());
            enterprise.setEnterpriseManagerPhone(emList.get(0).getMobilePhone());
        }
        /*List<Administer> cmList = administerService.selectCMByEnterpriseId(enterpriseId);
        if (enterprise != null
                && cmList != null
                && cmList.size() != 0) {
            enterprise.setCustomerManagerPhone(cmList.get(0).getMobilePhone());
        }*/
        modelMap.put("pc", enterprise);

        return "potentialCustomer/detail_potential.ftl";
    }

    /**
     * 潜在用户页面树结构获取根节点
     *
     * @param resp
     * @param districtId
     * @param modelMap
     * @date 2016年7月22日
     * @author wujiamin
     */
    @RequestMapping(value = "getManagerRoot")
    public void getManagerRoot(HttpServletResponse resp) {
        List<Manager> managerRoots = managerService.selectByParentId(1L);

        List list = new ArrayList();
        if (managerRoots != null && managerRoots.size() > 0) {
            for (Manager manager : managerRoots) {
                if (!manager.getParentId().equals(manager.getId())) {
                    Map map = new HashMap();
                    map.put("id", manager.getId());
                    map.put("text", manager.getName());
                    map.put("children", new ArrayList());
                    list.add(map);
                }
            }
        }
        try {
            resp.getWriter().write(JSONObject.toJSONString(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 潜在用户树获取子节点
     *
     * @param resp
     * @param districtId
     * @param modelMap
     * @date 2016年7月22日
     * @author wujiamin
     */
    @RequestMapping(value = "getChildNode")
    public void getChildNode(HttpServletResponse resp, Long parentId) {
        List<Manager> managerRoots = managerService.selectByParentId(parentId);
        boolean flag = authorityService.ifHaveAuthority(parentId, "ROLE_ENTERPRISE_MANAGER_PARENT");
        if (flag) {
            return;
        }
        List list = new ArrayList();
        if (managerRoots != null && managerRoots.size() > 0) {
            for (Manager manager : managerRoots) {
                Map map = new HashMap();
                map.put("id", manager.getId());
                map.put("text", manager.getName());
                map.put("children", new ArrayList());
                list.add(map);
            }
        }
        try {
            resp.getWriter().write(JSONObject.toJSONString(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Info {
        private String id;
        private String text;

        public Info(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}

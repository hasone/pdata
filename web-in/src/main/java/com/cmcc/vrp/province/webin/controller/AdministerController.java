
package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.service.SCAddMemberService;
import com.cmcc.vrp.boss.sichuan.service.SCCancelService;
import com.cmcc.vrp.boss.sichuan.service.SCOpenService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.AdminDistrictMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.AdminDistrict;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.security.MySessionListener;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.AuthorityService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.sso.service.SSOService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * @ClassName: AdministerController
 * @Description: 用户管理控制器
 * @author: sunyiwei
 * @date: 2015年4月17日 上午10:06:08
 */
@Controller
@RequestMapping("/manage/user")
public class AdministerController extends BaseController {
    @Autowired
    MdrcCardmakerService mdrcCardMakerService;
    @Autowired
    EnterpriseUserIdService enterpriseUserIdService;
    @Autowired
    SCAddMemberService scAddMemberService;

    @Autowired
    SCOpenService scOpenService;

    @Autowired
    SCCancelService scCancelService;

    private Logger logger = Logger.getLogger(AdministerController.class);

    @Autowired
    private AdministerService administerService;
    @Autowired
    private EnterprisesService enterpriseService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private AdminEnterService adminEnterService;
    @Autowired
    private PhoneRegionService phoneRegionService;
    @Autowired
    private DistrictMapper districtMapper;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private AdminDistrictMapper adminDistrictMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AdminManagerService adminManagerService;

    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Autowired
    SendMsgService sendMsgService;
    
    @Autowired
    SSOService ssoService;

    /**
     * 给用户分配权限，即给用户分配
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.GET)
    public String addRole(ModelMap modelMap, Administer administer) {

        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }

        Long currentRoleId = currentAdmin.getRoleId();

        //查找所有角色
        List<Role> roleList = roleService.getCreateRoleByRoleId(currentRoleId);

        //有地区关联的管理员在分配其他管理员角色时只能获取其同级及下级的地区
        List<AdminDistrict> ad = adminDistrictMapper.selectAdminDistrictByAdminId(currentAdmin.getId());
        if (ad == null || ad.size() != 1) {
            modelMap.addAttribute("districtId", 1);
        } else {
            modelMap.addAttribute("districtId", ad.get(0).getDistrictId());
        }

        modelMap.addAttribute("administer", administer);
        modelMap.addAttribute("roles", roleList);
        modelMap.addAttribute("currentRoleId", currentRoleId);

        return "admin/addRole.ftl";
    }

    /**
     * 保存用户新的角色信息
     */
    @RequestMapping(value = "/roleSave")
    public String roleSave(ModelMap modelMap, Administer administer, Long roleId, Long enterpriseId, Long districtIdSelect) {

        if (administer.getMobilePhone() == null) {
            modelMap.addAttribute("errorMsg", "请输入手机号码！");
            return addRole(modelMap, administer);
        }

        //在数据库中查找用户账号
        Administer administerDB = administerService.selectByMobilePhone(administer.getMobilePhone());
        //先检查该用户是否已分配了角色，如果已分配角色则不能修改
        if (administerDB != null) {
            if (null != adminRoleService.getRoleIdByAdminId(administerDB.getId())) {
                modelMap.addAttribute("errorMsg", "该用户已分配了角色！");
                return addRole(modelMap, administer);
            }
        }

        /**
         * 1、检查区域
         * 如果创建的是省市管理员及客户经理，则必须包含区域
         */
        String provinceManager = getProvinceManager();
        final String CUSTOM_MANAGER = getCustomManager();
        String cityManager = getCityManager();

        if (roleId.toString().equals(provinceManager) || roleId.toString().equals(CUSTOM_MANAGER) || roleId.toString().equals(cityManager)) {
            if (districtIdSelect == null) {
                modelMap.addAttribute("errorMsg", "省市区管理员及客户经理必须选定区域！");
                return addRole(modelMap, administer);
            }
            /**
             * 市级管理员必须地区范围到市
             */
            if (roleId.toString().equals(cityManager) && districtMapper.selectById(districtIdSelect).getLevel() != 2) {
                modelMap.addAttribute("errorMsg", "市级管理员区域需选择到市！");
                return addRole(modelMap, administer);
            }
            /**
             * 客户经理必须地区范围到区
             */
            if (roleId.toString().equals(CUSTOM_MANAGER) && districtMapper.selectById(districtIdSelect).getLevel() != 3) {
                modelMap.addAttribute("errorMsg", "客户经理需选择到区！");
                return addRole(modelMap, administer);
            }
            /**
             * 市级管理员必须地区范围到省
             */
            if (roleId.toString().equals(provinceManager) && districtMapper.selectById(districtIdSelect).getLevel() != 1) {
                modelMap.addAttribute("errorMsg", "省级管理员区域需选择到省！");
                return addRole(modelMap, administer);
            }
        }


        /**
         * 2、如果不存在该用户，则新创建一个用户账号
         */
        if (administerDB == null) {
            Administer adminNew = new Administer();
            adminNew.setUserName(administer.getUserName());
            adminNew.setMobilePhone(administer.getMobilePhone());
            adminNew.setCreatorId(getCurrentUser().getId());
            administerService.insertAdminister(adminNew, roleId, enterpriseId);

            logger.info("用户ID-" + getCurrentUser().getId() + "创建手机号" + administer.getMobilePhone() + "用户并设置为roleId:" + roleId);

            administerDB = administerService.selectByMobilePhone(administer.getMobilePhone());
        } else {

            /**
             * 保存用户角色分配的createId
             */
            administerDB.setCreatorId(getCurrentUser().getId());
            administerDB.setUpdateTime(new Date());
            //清空密码以防密码重置
            administerDB.setPassword(null);
            administerDB.setUserName(administer.getUserName());
            administerService.updateAdminister(administerDB, roleId, enterpriseId);
        }

        /**
         * 4、角色区域表新增或更新
         * 保存用户地区关系，更新admin_district表
         */
        if (roleId.toString().equals(provinceManager) || roleId.toString().equals(CUSTOM_MANAGER) || roleId.toString().equals(cityManager)) {
            AdminDistrict adminDistrict = new AdminDistrict();
            adminDistrict.setAdminId(administerDB.getId());
            adminDistrict.setDistrictId(districtIdSelect);

            adminDistrictMapper.deleteByAdminId(administerDB.getId());
            adminDistrictMapper.insert(adminDistrict);
        }


        logger.info("完成角色分配: 用户ID-" + getCurrentUser().getId() + "将手机号"
            + administer.getMobilePhone() + "设置为roleId" + roleId
            + ",地区设置为districtId" + districtIdSelect);


        return "redirect:index.html";
    }

    /**
     * @param modelMap
     * @param administer ， id==null时表示新建，否则表示更新
     * @return
     * @throws
     * @Title:create
     * @Description: 创建或更新用户对象，客户经理具备创建企业关键人和企业的功能
     * @author: sunyiwei，qihang，qinqinyan
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(ModelMap modelMap, Administer administer) {
        /**
         * 校验
         */
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }

        Long currentRoleId = currentAdmin.getRoleId();

        //只有客户经理和超级管理员才允许添加用户
        final String CUSTOM_MANAGER = getCustomManager();
        String superAdminRoleId = getSuperAdminRoleId();
        final String ENTERPRISE_CONTACTOR = getENTERPRISE_CONTACTOR();

        if (!currentRoleId.toString().equals(CUSTOM_MANAGER)
            && !currentRoleId.toString().equals(superAdminRoleId)) {
            modelMap.addAttribute("errorMsg", "对不起，您无权限新建用户！");
            return "error.ftl";
        }

        List<Role> roleList = new ArrayList<Role>();
        if (currentRoleId.toString().equals(CUSTOM_MANAGER)) {//客户经理角色
            //如果是客户经理，下拉角色有企业关键人、制卡专员
            List<Role> list = roleService.getAvailableRoles();
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i).getCode().equals(ENTERPRISE_CONTACTOR)) {
                    roleList.add(list.get(i));
                    break;
                }
            }
        } else {
            roleList = roleService.getAvailableRoles();
        }

        if (administer != null) {
            if (administer.getRoleId() != null) {
                if (administer.getRoleId().toString().equals(ENTERPRISE_CONTACTOR)) {//企业关键人，显示企业列表
                    List<Long> enterpriseIds = adminEnterService.selecteEntIdByAdminId(administer.getRoleId());
                    if (enterpriseIds != null && enterpriseIds.size() == 1) {
                        modelMap.addAttribute("currentEnterpriseId", enterpriseIds.get(0));
                    }
                    List<Enterprise> enterpriseList = enterpriseService.getEnterpriseListByAdminId(currentAdmin);
                    modelMap.addAttribute("enterprises", enterpriseList);
                }
            }
            modelMap.addAttribute("administer", administer);
        }
        modelMap.addAttribute("roles", roleList);
        modelMap.addAttribute("currentRoleId", currentRoleId);
//		modelMap.addAttribute("currentUserId", currentAdmin.getId());


        return "admin/create.ftl";
    }

    /**
     * @param modelMap
     * @param administer ， id==null时表示新建，否则表示更新
     * @return
     * @throws
     * @Title:Update
     * @Description: 创建或更新用户对象
     * @author: sunyiwei，qihang
     */
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Administer administer, QueryObject queryObject) {
        /**
         * 校验
         */
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }
        Long currentRoleId = currentAdmin.getRoleId();

        if (administer.getId() == null) {
            return index(modelMap, queryObject);
        }
        Long administerId = administer.getId();
        administer = administerService.selectAdministerById(administerId);
        if (administer == null) {
            modelMap.addAttribute("errorMsg", "数据库中没有相关用户信息Id: " + administerId);
            return "error.ftl";
        }

        //超级管理员可以修改所有记录;客户经理只能修改自己创建的记录
        if (currentAdmin.getId().longValue() == administer.getCreatorId().longValue()
            || currentRoleId.toString().equals(getSuperAdminRoleId())) {

            Role role = new Role();
            if (administer.getRoleId() != null) {
                role = roleService.getRoleByCode(administer.getRoleId().toString());

                if (administer.getRoleId().toString().equals(getENTERPRISE_CONTACTOR())) {
                    List<Long> enterpriseIds = adminEnterService.selecteEntIdByAdminId(administerId);
                    if (enterpriseIds != null && enterpriseIds.size() == 1) {
                        modelMap.addAttribute("currentEnterpriseId", enterpriseIds.get(0));
                        Enterprise e = enterpriseService.selectByPrimaryKey(enterpriseIds.get(0));
                        if (e != null) {
                            modelMap.addAttribute("currentEnterpriseName", e.getName());
                        }
                    }

                    List<Enterprise> enterpriseList = enterpriseService.getEnterpriseListByAdminId(currentAdmin);
                    modelMap.addAttribute("enterprises", enterpriseList);
                }
//                else if (administer.getRoleId().toString().equals(FLOWCARD_ADMIN)) {
//                    List<MdrcCardmaker> MdrcCardmakerList = mdrcCardMakerService.selectByOperatorId(administerId);
//                    if (MdrcCardmakerList != null && MdrcCardmakerList.size() > 0) {
//                        modelMap.addAttribute("currentCardMakerId", MdrcCardmakerList.get(0).getId());
//                    }
//
//                    List<MdrcCardmaker> mdrcCardmakerList = mdrcCardMakerService.selectUnbindCardmaker(administer.getId());
//                    modelMap.put("cardmarkers", mdrcCardmakerList);
//                }
            }


            modelMap.addAttribute("administer", administer);
            modelMap.addAttribute("role", role);
            modelMap.addAttribute("enterpriseRoleId", getENTERPRISE_CONTACTOR());  //在企业关键人的时候，才能选择企业

            //by qihang
            modelMap.addAttribute("flowcardAdminId", getFLOWCARD_ADMIN());  //在流量卡关键人的时候，才能选择企业

            return "admin/update.ftl";
        } else {
            modelMap.addAttribute("errorMsg", "对不起，您无权限修改该条记录！");
            return "error.ftl";
        }

    }


    /**
     * 校验手机号
     */
    @RequestMapping("/checkPhoneAjax")
    public void checkMinCardNumAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        String mobilePhone = request.getParameter("mobilePhone");
        String configId = request.getParameter("configId");

        //判断手机号是否合法
        if (!StringUtils.isValidMobile(mobilePhone) || !StringUtils.isValidMobile(mobilePhone)) {
            map.put("failure", "inValid");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }
        //校验重复
        Administer admin = administerService.selectByMobilePhone(mobilePhone);
        if (admin == null || admin.getId().toString().equals(configId)) {
            map.put("success", "success");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        } else {
            map.put("failure", "used");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }

    }

    /**
     * 校验姓名
     */
    @RequestMapping("/checkUserNameAjax")
    public void checkUserNameAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        String userName = request.getParameter("userName").replaceAll(" ", "");
        String configId = request.getParameter("configId");


        //校验重复
        List<Administer> list = administerService.selectAllAdministers();
        if (list != null) {
            for (Administer admin : list) {
                if (admin.getId().toString().equals(configId)) {
                    continue;
                }
                if (admin.getUserName().equals(userName)) {
                    map.put("failure", "used");
                    resp.getWriter().write(JSON.toJSONString(map));
                    return;
                }

            }
        }
    }

    /**
     * @param modelMap
     * @param administer
     * @return
     * @throws
     * @Title:save
     * @Description: 保存用户对象
     * @author: qihang
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(ModelMap modelMap, Administer administer, Long roleId,
                       Long enterpriseId, QueryObject queryObject) {
        HttpServletRequest request = getRequest();

        Administer currentAdminister = getCurrentUser();
        if (currentAdminister == null) {
            return getLoginAddress();
        }

        //过滤输入的空格
        if (!StringUtils.isEmpty(administer.getUserName())) {
            administer.setUserName(administer.getUserName().replaceAll(" ", ""));
        }


        // 1.检验所有参数是否通过校验

        if (!validateSaveAdminister(modelMap, administer, roleId, enterpriseId)) {
            return create(modelMap, administer);
        }

        // 2.进行插入或更新操作，如果id属性为空说明是插入操作，反之是更新操作
        if (administer.getId() == null) {// 插入处理

            administer.setCreatorId(currentAdminister.getId());


            if (!insertNewUser(administer, roleId, enterpriseId)) {
                modelMap.addAttribute("errorMsg", "插入失败");
                return create(modelMap, administer);
            }
        } else {// 编辑处理
            String isPassChanged = request.getParameter("isPassChanged");//从页面上得到用户密码是否已经修改过

            if (isPassChanged != null && "false".equals(isPassChanged)) {//用户密码没有修改
                administer.setPassword(null);//数据库不需要更新密码
            }

            if (!updateExistUser(administer, isPassChanged, roleId, enterpriseId)) {
                modelMap.addAttribute("errorMsg", "更新用户失败");
                return update(modelMap, administer, queryObject);
            }
        }
        return "redirect:index.html";
    }

    /**
     * 私有类新建用户，包括捕获TransactionException，失败返回false
     */
    private boolean insertNewUser(Administer administer, Long roleId, Long enterpriseId) {
        String userInfo = "用户ID:" + getCurrentUser().getId() + " 创建新用户";
        String adminInfo = "新姓名" + administer.getUserName() + "新用户手机号:" + administer.getMobilePhone() + "角色Id：" + roleId + "企业Id：" + enterpriseId;

        try {
            if (administerService.insertAdminister(administer, roleId, enterpriseId)) {
                logger.info(userInfo + "成功 " + adminInfo);
                return true;
            } else {
                logger.info(userInfo + "失败 " + adminInfo);
                return false;
            }
        } catch (TransactionException e) {
            logger.info(userInfo + "失败，事务回滚 " + adminInfo);
            return false;
        }
    }

    /**
     * 私有类更新用户，包括捕获TransactionException，失败返回false
     */
    private boolean updateExistUser(Administer administer, String isPassChanged, Long roleId, Long enterpriseId) {
        if (isPassChanged != null && "false".equals(isPassChanged)) {//用户密码没有修改
            administer.setPassword(null);//数据库不需要更新密码
        }

        String userInfo = "用户ID:" + getCurrentUser().getId() + " 更新用户" + administer.getId();
        String adminInfo = "新姓名" + administer.getUserName() + "新用户手机号:" + administer.getMobilePhone() + "新角色Id：" + roleId + "新企业Id：" + enterpriseId;


        try {
            if (administerService.updateAdminister(administer, roleId, enterpriseId)) {
                logger.info(userInfo + "成功 " + adminInfo);
                return true;
            } else {
                logger.info(userInfo + "失败 " + adminInfo);
                return false;
            }
        } catch (TransactionException e) {
            logger.info(userInfo + "失败 " + adminInfo);
            return false;
        }
    }

    /**
     * @param modelMap
     * @param queryObject
     * @return
     * @throws
     * @Title:index
     * @Description: 获取用户列表
     * @author: sunyiwei
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
        
        /**
         * 0426更新 ，如果当前角色是客户经理，则跳转到客户经理专用的用户管理页面
         */
        if(manager.getRoleId().equals(2L)){
            return "redirect:/manage/adminChange/index.html";
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

        return "admin/index.ftl";
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
        
        String managerIdStr = getRequest().getParameter("managerId");
        if (org.apache.commons.lang.StringUtils.isNotBlank(managerIdStr)) {
            Long managerId = NumberUtils.toLong(managerIdStr);
            if (!isParentOf(managerId)) { //当前用户不是指定用户或不是它的父节点，没有权限查看相应的节点信息
                res.setStatus(HttpStatus.SC_FORBIDDEN);
                return;
            }
        }

        queryObject.getQueryCriterias().put("currentUserId", getCurrentUser().getId());
        /**
         * 当前登录用户的管理员层级
         */
        Manager manager = getCurrentUserManager();
        queryObject.getQueryCriterias().put("managerId", manager.getId());

        //页面查询参数中设定的管理员层级，如果设定了就会将上面的值覆盖
        setQueryParameter("managerId", queryObject);

        // 数据库查找符合查询条件的个数
        int administerCount = administerService.queryPaginationAdminCount(queryObject);
        List<Administer> administerList = administerService.queryPaginationAdminList(queryObject);
        
        if(administerList!=null){
            for (Administer administer : administerList){
                //地区全称
                Manager districtName =adminManagerService.getManagerByAdminId(administer.getId());
                if (districtName != null) {           
                    String fullname = "";
                    if(districtName.getRoleId().equals(Long.parseLong(getENTERPRISE_CONTACTOR()))){//企业管理员，地区全称到自己的父节点
                        administer.setDistrictName(managerService.getFullNameByCurrentManagerId(fullname,
                            districtName.getParentId()));
                    }else{//其他用户，地区全称就是自己节点名称开始
                        administer.setDistrictName(managerService.getFullNameByCurrentManagerId(fullname,
                                districtName.getId()));
                    }
                }
                
                //模糊处理用户信息
                administerService.blurAdministerInfo(administer);
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
     * @param modelMap
     * @param administerId
     * @return
     * @throws
     * @Title:delete
     * @Description: 删除指定的用户对象
     * @author: sunyiwei
     */
    @RequestMapping("/delete")
    public String delete(ModelMap modelMap, QueryObject queryObject,
                         Long administerId) {
        if (administerId == null) {
            modelMap.addAttribute("errorMsg", "对不起，没有传入管理员Id");
            return "error.ftl";
        } else if (administerId.equals(1L)) {
            modelMap.addAttribute("errorMsg", "对不起，超级管理员不能删除");
            return "error.ftl";
        }

        String resultInfo = "用户ID:" + getCurrentUser().getId() + " 删除用户id" + administerId;
        try {
//            mdrcCardMakerService.eraseOperator(administerId);
            if (!administerService.deleteById(administerId)) {
                modelMap.addAttribute("errorMsg", "删除用户失败");
                logger.info(resultInfo + " 失败");
                return "error.ftl";
            }
            logger.info(resultInfo + " 成功");
        } catch (TransactionException e) {
            logger.info(resultInfo + " 失败，事务回滚");
        }

        return index(modelMap, queryObject);
    }

    /**
     * 用户登录
     *
     * @param model   模型
     * @param request 请求对象
     * @return
     * 1: 只用静态密码（如吉林）
     * 2: 只用短信呢验证码（如广东）
     * 3: 可以配置只用静态密码或者只用短信验证码
     * 4: 登陆必须要静态密码或者短信验证码（如重庆）
     * edit by qinqinyan on 2017/09/07 for v1.13.0
     * 生成一个用于加密密码的key，存储在session中
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException{
        if(isGDUseSSO()){//是广东且单点登录，redirect
            response.sendRedirect(getGDlogoutUrl());
            return "";
        }
        
        //从request中得到姓名
        String loginUserName = request.getParameter("username");


        //将前台得到的username进行编码，防止xss攻击问题
        String userNameEncode = null;
        try {
            if (!StringUtils.isEmpty(loginUserName)) {
                userNameEncode = URLEncoder.encode(loginUserName, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            userNameEncode = null;
        }

        //将前次姓名填写到页面上
        if (!StringUtils.isEmpty(userNameEncode)) {
            model.addAttribute("username", userNameEncode);
        }

        //在请求登陆页面时，生成用于加密密码的key值，存在session中
        String key = RandomStringUtils.randomAlphanumeric(16);
        getSession().setAttribute("SESSION_LOGIN_TOKEN", key);

        if (StringUtils.isEmpty(loginType())) {
            model.addAttribute("logintype", "3");
        } else if (!"1".equals(loginType())
                && !"2".equals(loginType())
                && !"3".equals(loginType())
                && !"4".equals(loginType())) {
            model.addAttribute("logintype", "3");
        }else {
            model.addAttribute("logintype", loginType());
        }

        if (globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()).equals("sc_jizhong")) {
            return "personal/login.ftl";
        }
        model.addAttribute("resetPwd", true);
        if (globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()).equals("sd")) {
            model.addAttribute("resetPwd", false);
        }
//        if ("chongqing".equals(getProvinceFlag())) {
//            return "login_cq.ftl";
//        } 
        return "login.ftl";
    }
    
    /**
     * 平台登出地址，广东环境会跳转回单点登录平台
     *
     */
    @RequestMapping(value = "/loginout")
    public String loginout(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Administer administer;
        if(isGDUseSSO() && (administer = getCurrentUser()) !=null){
            ssoService.gdSendLogoutReq(administer.getMobilePhone());
            MySessionListener.removeAndInvalidByMobile(administer.getMobilePhone());
        }
        return "redirect:/j_spring_security_logout";
    }
    
    /**
     * 平台登出地址，广东环境会跳转回单点登录平台
     *
     */
    @RequestMapping(value = "/gdGoBack")
    public String gdGoBack(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "redirect:/j_spring_security_logout";
    }
    
    
    /**
     * 检测是广东环境,且使用sso登录
     */
    private boolean isGDUseSSO(){
        return "gd_mdrc".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())) &&
                "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_IS_USE.getKey()));
    }
    
    /**
     * 获取广东平台，退出登录的跳转地址
     */
    private String getGDlogoutUrl(){
        return globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_FIRSTPAGE_URL.getKey());
    }


    /**
     * @param modelMap
     * @param administerId
     * @return
     * @throws
     * @Title:getAdminister
     * @Description: 获得个人信息详情页面
     * @author: sunyiwei
     */
    @RequestMapping("showDetails")
    public String getAdminister(ModelMap modelMap, Long administerId) {
        Administer administer = administerService
            .selectAdministerById(administerId);
        if (administerId == null || administer == null) {
            modelMap.addAttribute("errorMsg", "无效的用户ID");
            return "error.ftl";
        }

        Manager userManager = managerService.getManagerByAdminId(administerId);
        if (userManager == null || !isChildNode(userManager.getId())) {
            modelMap.addAttribute("errorMsg", "无操作权限");
            return "error.ftl";
        }

        logger.info("用户ID:" + getCurrentUser().getId() + "查看管理员详情。 adminid=" + administerId);

        String roleName = adminRoleService.getRoleNameByAdminId(administerId);

        Long roleId = adminRoleService.getRoleIdByAdminId(administerId);

        if (roleId != null && (roleId.toString().equals(getENTERPRISE_CONTACTOR()))) {//该角色是企业关键人
            List<String> enterpriseNames = adminEnterService.selectByAdminId(administerId);
            if (enterpriseNames != null && enterpriseNames.size() == 1) {
                modelMap.addAttribute("enterpriseName", enterpriseNames.get(0));
            }
        }

//        if (roleId != null && (roleId.toString().equals(FLOWCARD_ADMIN))) {//该角色是制卡专员
//            List<String> cardmarkerNames = mdrcCardMakerService.selectByOperatorId(administerId);
//            if (cardmarkerNames != null && cardmarkerNames.size() == 1)
//                modelMap.addAttribute("cardmarkerName", cardmarkerNames.get(0));
//        }
        /*
         * 显示该用户的地区
		 */
        String districtName = null;
        List<AdminDistrict> ad = adminDistrictMapper.selectAdminDistrictByAdminId(administerId);
        if (ad != null && ad.size() == 1) {
            Map map = new HashMap();
            map.put("id", ad.get(0).getDistrictId());
            District d1 = districtMapper.selectByMap(map).get(0);
            if (d1.getLevel() == 1) {
                districtName = d1.getName();
            } else if (d1.getLevel() == 2) {
                map.put("id", d1.getParentId());
                District d2 = districtMapper.selectByMap(map).get(0);
                districtName = d2.getName() + d1.getName();
            } else {
                map.put("id", d1.getParentId());
                District d2 = districtMapper.selectByMap(map).get(0);
                map.put("id", d2.getParentId());
                District d3 = districtMapper.selectByMap(map).get(0);
                districtName = d3.getName() + d2.getName() + d1.getName();
            }
        }

        modelMap.addAttribute("roleName", roleName);
        modelMap.addAttribute("districtName", districtName);
        
        //模糊处理用户信息
        administerService.blurAdministerInfo(administer);
        modelMap.addAttribute("administer", administer);

        return "admin/details.ftl";
    }

    /**
     * @param administer
     * @param resp
     * @throws IOException
     * @Title: checkMobilePhoneUnique
     */
    @RequestMapping(value = "/check")
    public void checkMobilePhoneUnique(Administer administer,
                                       HttpServletResponse resp) throws IOException {
        Boolean bFlag = administerService.checkUnique(administer);
        resp.getWriter().write(bFlag.toString());
    }

    /**
     * @param userName
     * @param id
     * @param request
     * @param response
     * @throws IOException
     * @Title: checkuserName
     */
    @RequestMapping(value = "/checkuserName")
    public void checkuserName(String userName, Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String szName = userName.toLowerCase();

        Boolean bFlag = administerService.checkNameUnique(id, szName);

        response.getWriter().write(bFlag.toString());
    }

    /**
     * 判断号码唯一性及是否是某地号码
     *
     * @param administer
     * @param resp
     * @throws IOException
     */
    @RequestMapping(value = "/checkMobileValid")
    public void checkMobileValid(Administer administer,
                                 HttpServletResponse resp) throws IOException {
        String mobile = administer.getMobilePhone();

        resp.setContentType("text/xml;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = resp.getWriter();

        if (!administerService.checkUnique(administer)) {
            pw.print("该手机号码已存在，请重新输入");
        } else if (!StringUtils.isValidMobile(mobile)) {
            pw.print("非本省移动手机号，请重新输入");
        } else {
            pw.print("true");
        }


    }

    /**
     * 只判断是否为某地号码
     *
     * @param administer
     * @param resp
     * @throws IOException
     */
    @RequestMapping(value = "/checkMobileDistrict")
    public void checkMobileDistrict(Administer administer,
                                    HttpServletResponse resp) throws IOException {
        String mobile = administer.getMobilePhone();

        resp.setContentType("text/xml;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = resp.getWriter();

        if (!StringUtils.isValidMobile(mobile)) {
            pw.print("非本省移动手机号，请重新输入");
        } else {
            pw.print("true");
        }
    }

    /**
     * @param administer
     * @return
     * @throws
     * @Title: validateSaveAdminister
     * @Description: 用于新建和更新时字段的校验
     * @author: sunyiwei
     */
    private boolean validateSaveAdminister(ModelMap model, Administer administer, Long roleId, Long enterpriseId) {
        if (administer == null) {
            model.addAttribute("errorMsg", "无效的用户对象!");
            return false;
        }

        try {
            administer.selfCheck();
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.toString());
            return false;
        }


        //唯一性校验
        if (!administerService.checkUnique(administer)) {
            model.addAttribute("errorMsg", "姓名称或手机号码已存在!");
            return false;
        }

        //检验角色ID必须存在
        if (roleId == null || StringUtils.isEmpty(roleId.toString())) {
            model.addAttribute("errorMsg", "用户角色不能为空!");
            return false;
        }

        // 如果是企业关键人，必须要关联企业.如果是流量卡关键人，必须要关联企业
        if ((roleId.toString().equals(getENTERPRISE_CONTACTOR()) || roleId.toString().equals(getFLOWCARD_ADMIN()))
            && (enterpriseId == null || enterpriseId == 0)) {
            model.addAttribute("errorMsg", "请选择要关联的企业!");
            return false;
        }

        return true;
    }

    /**
     * @param resp
     */
    @RequestMapping(value = "getEntersAjax")
    public void getEntersAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值

        String roleId = getRequest().getParameter("roleId");
        Administer currentAdminister = getCurrentUser();
        if (roleId == null || currentAdminister == null) {
            /*map.put("fail", "paramsMissing");
            resp.getWriter().write(JSON.toJSONString(map));*/
            return;
        }

        if (roleId.equals(getENTERPRISE_CONTACTOR())) {

            List<Enterprise> enterpriseList = enterpriseService.getEnterpriseListByAdminId(currentAdminister);
            map.put("enters", enterpriseList);
            resp.getWriter().write(JSON.toJSONString(map));
            return;

        }
//        else if (roleId.equals(FLOWCARD_ADMIN)) {
//            List<MdrcCardmaker> MdrcCardmakerList = mdrcCardMakerService.selectUnbindCardmaker(currentAdminister.getId());
//            map.put("enters", MdrcCardmakerList);
//            resp.getWriter().write(JSON.toJSONString(map));
//            return;
//        }
    }

    /**
     * @param resp
     */
    @RequestMapping(value = "getDistrictCityAjax")
    public void getDistrictCityAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值

        String province = getRequest().getParameter("province");

        map.put("parentId", province);

        List<District> district = districtMapper.selectByMap(map);
        map.put("districts", district);

        resp.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * @param resp
     */
    @RequestMapping(value = "getDistrictAjax")
    public void getDistrictAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        String districtId = getRequest().getParameter("districtId");
        String parentId = getRequest().getParameter("parentId");


        List<District> district = new ArrayList<District>();
        if (districtId != null) {
            map.put("id", districtId);
            district = districtMapper.selectByMap(map);
        }
        if (parentId != null) {
            map.put("parentId", parentId);
            district = districtMapper.selectByMap(map);
        }

        List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < district.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", district.get(i).getId());
            item.put("text", district.get(i).getName());
            item.put("state", "closed");
            // item.put("children", itemsList2);
            itemsList.add(item);
        }

        String data = JSON.toJSONString(itemsList);

        resp.getWriter().write(data);
    }

    /**
     * 根据手机号码返回当前用户姓名
     */
    @RequestMapping(value = "getNameByPhone")
    public void getNameByPhone(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String phone = request.getParameter("phone");
        Map returnMap = new HashMap();
        Administer admin = administerService.selectByMobilePhone(phone);
        if (admin != null) {
            String userName = admin.getUserName();
            if (StringUtils.isEmpty(userName)) {
                returnMap.put("exist", "no");
            } else {
                returnMap.put("exist", "yes");
                returnMap.put("name", userName);
            }
        } else {
            returnMap.put("exist", "no");
        }
        String data = JSON.toJSONString(returnMap);
        resp.getWriter().write(data);
    }

    /**
     * 为用户设置管理员身份的页面
     *
     * @param map
     * @return
     * @date 2016年7月18日
     * @author wujiamin
     */
    @RequestMapping("setManager")
    public String setManagerIndex(ModelMap map) {
        Manager currentManager = getCurrentUserManager();
        if (currentManager == null) {
            map.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }
        List<Manager> managers = managerService.selectByParentId(currentManager.getId());
        map.put("managers", managers);
        //判断是否是四川的平台
        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        if ("sc".equalsIgnoreCase(provinceFlag)) {
            map.addAttribute("sc", "true");
        }
        return "admin/set_manager.ftl";
    }

    @RequestMapping("saveUserManager")
    public String saveUserManager(ModelMap modelMap, Administer administer, Long managerId) {
        if (administer.getMobilePhone() == null) {
            modelMap.addAttribute("errorMsg", "请输入手机号码！");
            return setManagerIndex(modelMap);
        }

        if (!isChildNode(managerId)) {
            modelMap.addAttribute("errorMsg", "无操作权限，无法分配该层级的职位！");
            return setManagerIndex(modelMap);
        }

        //在数据库中查找用户账号
        Administer administerDB = administerService.selectByMobilePhone(administer.getMobilePhone());
        //先检查该用户是否已分配了角色，如果已分配角色则不能修改
        Manager managerDB = null;
        if (administerDB != null) {
            managerDB = managerService.selectByAdminId(administerDB.getId());
            if (managerDB != null && !managerDB.getId().equals(-1L)) {
                modelMap.addAttribute("errorMsg", "该用户已有职位，不能重新分配职位！");
                return setManagerIndex(modelMap);
            }
        }

        if ("1".equals(loginType())) {
            logger.info("初始化静态密码" + "123456");
            administer.setPassword("123456");
        }
        
        if ("4".equals(loginType())) {
            String password = SerialNumGenerator.buildBossReqSerialNum(6);
            
            String content = "流量平台初始化静态密码" + password + ", 请妥善保管，登录平台后请尽快更改密码。";
            logger.info(content);
            sendMsgService.sendMessage(administer.getMobilePhone(), content, MessageType.RANDOM_CODE.getCode());
            administer.setPassword(password);
        }
        try {
            if (administerService.createAdminister(managerId, administer, administerDB, getCurrentUser().getId())) {
                logger.info("用户ID-" + getCurrentUser().getId() + "将手机号" + administer.getMobilePhone() + "用户设置为ManagerId:" + managerId);
            }
        } catch (Exception e) {
            modelMap.addAttribute("errorMsg", "分配职位失败！" + e.getMessage());
            return setManagerIndex(modelMap);
        }

        return "redirect:index.html";
    }

    /**
     * 根据managerId获取
     *
     * @param modelMap
     * @param managerId
     * @return
     * @date 2016年7月27日
     * @author wujiamin
     */
    @RequestMapping("getRoleNameByManagerId")
    public void getRoleNameByManagerId(HttpServletResponse resp, ModelMap modelMap, Long managerId) {
        Map returnMap = new HashMap();
        Manager manager = managerService.selectByPrimaryKey(managerId);
        if (manager != null) {
            Role role = roleService.getRoleById(manager.getRoleId());
            if (role != null) {
                returnMap.put("roleName", role.getName());
            }
        }
        String data = JSON.toJSONString(returnMap);
        try {
            resp.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 编辑用户职位
     *
     * @param map
     * @return
     * @date 2016年7月28日
     * @author wujiamin
     */
    @RequestMapping("editManager")
    public String editManagerIndex(ModelMap map, Long administerId) {
        Manager currentManager = getCurrentUserManager();
        if (currentManager == null) {
            map.put("errorMsg", "当前用户无管理员职位");
            return "error.ftl";
        }
        if (administerId == null) {
            map.put("errorMsg", "当前用户Id为空，无法编辑职位");
            return "error.ftl";
        }
        
        //只能编辑自己下级的职位
        Manager editManager = managerService.selectByAdminId(administerId);
        if(editManager == null){
            map.put("errorMsg", "待编辑用户无职位");
            return "error.ftl";
        }
        if(!editManager.getParentId().equals(currentManager.getId())){
            map.put("errorMsg", "只能修改直属下级用户的职位");
            return "error.ftl";
        }
        Administer admin = administerService.selectAdministerById(administerId);

        List<Manager> managers = managerService.selectByParentId(currentManager.getId());
        map.put("managers", managers);

        map.put("currentManager", managerService.selectByAdminId(administerId));

        map.put("admin", admin);


        return "admin/edit_manager.ftl";
    }

    @RequestMapping("editUserManager")
    public String editUserManager(ModelMap modelMap, Long administerId, Long managerId) {
        Administer currentUser = getCurrentUser();
        if (currentUser == null) {
            return getLoginAddress();
        }
        Manager originManager = managerService.getManagerByAdminId(administerId);
        if (originManager == null || !isChildNode(originManager.getId())) {
            modelMap.addAttribute("errorMsg", "无操作权限，无法分配该层级的职位！");
            return editManagerIndex(modelMap, administerId);
        }

        if (!isChildNode(managerId) && !managerId.equals(-1L)) {
            modelMap.addAttribute("errorMsg", "无操作权限，无法分配该层级的职位！");
            return editManagerIndex(modelMap, administerId);
        }

        Long currentUserId = currentUser.getId();
        AdminManager am = new AdminManager();
        am.setAdminId(administerId);
        am.setManagerId(managerId);
        am.setCreatorId(currentUserId);
        try {
            if (adminManagerService.editUserManager(am)) {
                logger.info("用户ID:" + currentUserId + "修改用户管理员身份成功,新的adminManage：" + JSON.toJSONString(am));
                return "redirect:index.html";
            } else {
                modelMap.put("errorMsg", "修改用户职位失败");
                return editManagerIndex(modelMap, administerId);
            }
        } catch (Exception e) {
            modelMap.put("errorMsg", "修改用户职位失败");
            return editManagerIndex(modelMap, administerId);
        }
    }

    /**
     * 判断传入的节点是否是当前用户的子节点
     *
     * @param managerId
     * @return
     * @Title: isChildNode
     * @Description: TODO
     * @Author: wujiamin
     * @date 2016年8月18日下午11:46:29
     */
    private boolean isChildNode(Long managerId) {
        Long currentManageId = getCurrentUserManager().getId();

        //传入的id需要是当前用户的子节点
        String childrenStr = managerService.getChildNodeString(currentManageId);
        if (org.apache.commons.lang.StringUtils.isBlank(childrenStr)) {
            return false;
        }

        String[] children = childrenStr.split(",");
        for (String child : children) {
            if (child.equals(String.valueOf(managerId))) {
                return true;
            }
        }

        return false;
    }
}

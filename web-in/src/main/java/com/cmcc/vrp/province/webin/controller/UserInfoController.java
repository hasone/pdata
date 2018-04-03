package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.cmcc.vrp.util.AESUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.province.dao.AdminDistrictMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.AdminDistrict;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.AdministerDTO;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.province.sms.login.LoginSmsPojo;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.PropertyValidator;
import com.cmcc.vrp.util.StringUtils;

/**
 * 用户管理
 *
 * @author kok
 */
@Controller
@RequestMapping("/manage/userInfo")
public class UserInfoController extends BaseController {
    private final String config_key = "RANDOMPASS_CHECK";//数据库中globalconfig的随机验证码验证key值
    private final String check_pass_key = "OK";//globalconfig需要检验随机密码的value值
    @Autowired
    AdministerService administerService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    RoleService roleService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminEnterService adminEnterService;
    @Autowired
    SmsRedisListener smsRedisListener;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    MdrcCardmakerService mdrcCardMakerService;
    private Logger logger = Logger.getLogger(UserInfoController.class);
    @Autowired
    private AdminDistrictMapper adminDistrictMapper;
    @Autowired
    private DistrictMapper districtMapper;

    /**
     * 修改用户信息
     *
     * @param model
     * @param administer
     * @param newRoleId
     * @param enterpriseId
     * @return
     */
    @RequestMapping("/edit")
    public String update(ModelMap model, Administer administer, Long roleId,
                         Long enterpriseId) {

        if (administer != null && administer.getId() != null
            && administer.getId() > 0) {
            if (!StringUtils.isEmpty(administer.getUserName())) {
                if (!StringUtils.isEmpty(administer.getPassword())) {
                    if (StringUtils.isValidMobile(administer.getMobilePhone())) {
                        if (administerService.updateAdminister(administer,
                            roleId, enterpriseId)) {
                            // 修改成功 跳转到index页面
                            getRequest().setAttribute("falg", "1");
                            return "redirect:getAdminister.html";
                        } else {
                            // 修改失败 跳转到update页面
                            model.addAttribute("errorMsg", "修改失败!");
                        }
                    }
                }
            }

        }

        if (getCurrentUser() == null || getCurrentUser().getId() == null) {
            return getLoginAddress();
        }

        // 参数为空 跳转到update页面
        // administer =
        // administerService.selectAdministerById(getCurrentUser().getId());
        administer = administerService.selectAdministerById(getCurrentUser()
            .getId());
        model.addAttribute("administer", administer);

        List<Role> list = roleService.getAvailableRoles();

        List<Enterprise> liste = enterprisesService.getEnterpriseList();

        model.addAttribute("list", list);

        model.addAttribute("liste", liste);

        model.addAttribute("superAdminId", getSuperAdminRoleId());
        return "/user/userInfoUpdate.ftl";
    }

    /**
     * 根据ID查询用户
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/showDetails")
    public String getAdminister(ModelMap modelMap, Long administerId) {
        Administer administer = null;
        Administer currentUser = getCurrentUser();

        if (administerId == null
            || !currentUser.getId().equals(administerId)  //只能查看自己的详情信息
            || (administer = administerService.selectAdministerById(administerId)) == null) {
            modelMap.addAttribute("errorMsg", "无效的用户ID");
            return "error.ftl";
        }

        String roleName = adminRoleService.getRoleNameByAdminId(administerId);
        modelMap.addAttribute("roleName", roleName);

        Long roleId = adminRoleService.getRoleIdByAdminId(administerId);
        if (roleId != null && roleId.toString().equals(getENTERPRISE_CONTACTOR())){ //企业关键人时才显示企业
            List<String> enterpriseNames = adminEnterService.selectByAdminId(administerId);
            if (enterpriseNames != null && enterpriseNames.size() == 1) {
                modelMap.addAttribute("enterpriseName", enterpriseNames.get(0));
            }
        }

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

        modelMap.addAttribute("districtName", districtName);

        modelMap.addAttribute("administer", administer);
        
        //判断山东环境是否企业关键人或客户经理登录，如果是关闭密码修改按钮
        isSdCloseChangePass(roleId , modelMap);
        
        return "user/details.ftl";
    }
    
    
    /**
     * 判断山东环境是否企业关键人或客户经理登录，如果是关闭密码修改按钮
     */
    private void isSdCloseChangePass(Long roleId , ModelMap modelMap){
        String role = String.valueOf(roleId);

        //判断 1.是否为sd环境
        //2.是否为企业关键人或客户经理
        //以上都满足则不显示修改密码按钮
        if(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()).equalsIgnoreCase("sd") 
                && (globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey()).equalsIgnoreCase(role) 
                        || globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey()).equalsIgnoreCase(role))){
            
            modelMap.addAttribute("closeChangePass", "true");

        }else{
            modelMap.addAttribute("closeChangePass", "false");
        }
    }

    /**
     * @param modelMap
     * @param administer
     * @return
     * @throws
     * @Title:updateAdministerInfo
     * @Description: 获取当前登录用户的信息，显示其信息用于更改
     * @author: sunyiwei
     */
    @RequestMapping(value = "getCurrentUserInfo", method = RequestMethod.GET)
    public String getCurrentUserInfo(ModelMap modelMap) {
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        modelMap.addAttribute("administer", administer);
        return "user/currentUserInfo.ftl";
    }

    /**
     * @param modelMap
     * @param administer
     * @return
     * @throws
     * @Title:updateUserInfo
     * @Description: 更新当前登录用户的信息
     * @author: sunyiwei
     */
    @RequestMapping(value = "updateCurrentUserInfo", method = RequestMethod.POST)
    public String updateCurrentUserInfo(ModelMap modelMap, Administer administer) {
        Administer currentAdminister = getCurrentUser();
        if (currentAdminister == null) {
            return getLoginAddress();
        }

        if (administer == null) {
            modelMap.addAttribute("errorMsg", "无效的用户对象!");
            return "error.ftl";
        }

        administer.setId(currentAdminister.getId());


        if (!validateCurrentUserInfo(modelMap, administer)) {
            return getCurrentUserInfo(modelMap);
        }

        if (!administerService.updateSelective(administer)) {
            modelMap.addAttribute("errorMsg", "更新用户信息失败!");
            return "error.ftl";
        }/*
         * else { // 如果更新成功，当前session中的用户要改一下 tmpAdminister =
		 * administerService.selectAdministerById(id);
		 * getSession().setAttribute("currentUser", tmpAdminister); }
		 */

        return "redirect:/manage/userInfo/showCurrentUserDetails.html";
    }

    public Role getRole(Long id) {
        Long roleId = adminRoleService.getRoleIdByAdminId(id);
        if (roleId != null) {
            return roleService.getRoleById(roleId);
        }
        return null;
    }

    /** 
     * @Title: updatePassword 
     * @author wujiamin
    */
    @RequestMapping(value = "/updatePassword")
    public String updatePassword(ModelMap model) {

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }


        model.addAttribute("admin", administer);
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

        return "user/updatePassword.ftl";

    }

    /**
     * @param modelMap
     * @return
     * @throws
     * @Title:showUserInfo
     * @Description: 得到当前登录用户的信息
     * @author: sunyiwei
     */
    @RequestMapping("showCurrentUserDetails")
    public String showCurrentUserDetails(ModelMap modelMap) {
        Administer administer = getCurrentUser();
        if (administer == null) {
            return "redirect: /manage/user/login.html";
        }

        Long administerId = administer.getId();
        return getAdminister(modelMap, administerId);
    }

    /**
     * 用户密码修改
     *
     * @param model
     * @param administerDTO
     * @return
     */
    @RequestMapping("/savePassword")
    public String savePassword(ModelMap model, AdministerDTO administerDTO) {
        Administer administer = getCurrentUser();
        if (!administer.getMobilePhone().equals(administerDTO.getMobilePhone())) {
            String responseMsg = "您更改的账户密码不是您登陆的账户！";
            model.addAttribute("errorMsg", responseMsg);
            return updatePassword(model);
        }


        String checkResult = updatePassUserCheck(administerDTO);
        String responseMsg = "";
        if ("success".equals(checkResult)) {
            if (administerService.updateAdministerPassword(getCurrentUser()
                .getId(), administerDTO.getPassWord())) {
                logger.info("用户ID:" + getCurrentUser().getId() + "修改密码成功");
                return getAdminister(model, getCurrentUser().getId());
            } else {
                responseMsg = "密码修改失败！";
            }
        } else {
            responseMsg = checkResult;
        }

        logger.info("用户ID:" + getCurrentUser().getId() + "修改密码失败");
        model.addAttribute("errorMsg", responseMsg);
        return updatePassword(model);
    }


    private String updatePassUserCheck(AdministerDTO administerDTO) {
        if (administerDTO.getMobilePhone() != null
            && administerDTO.getSjmm() != null) {

            if (!StringUtils.isValidMobile(administerDTO.getMobilePhone())) {

            }

            if (StringUtils.isEmpty(administerDTO.getPassWord())) {
                return "新建密码不能为空!";
            }

            if (StringUtils.isEmpty(administerDTO.getPassWord2())) {
                return "确认密码不能为空!";
            }

            if (!administerDTO.getPassWord().equals(administerDTO.getPassWord2())) {
                return "两次密码不匹配!";
            }

            if (!StringUtils.isValidPassword(administerDTO.getPassWord())) {
                return "密码为6~16位的字母、数字及下划线组成的字符";
            }
            if ("1".equals(loginType())) {
                if (!administerService.checkPassword(administerDTO.getSjmm(), administerDTO.getMobilePhone())) {
                    return "旧密码验证失败";
                }
            } else {
              //从数据库的global_config表中得到config_key对应的value值，如果不存在或者值不为"OK"则不进行检测验证码，返回
                String config_value = globalConfigService.get(config_key);
                if (config_value == null || !check_pass_key.equals(config_value)) {
                    return "success";
                }


                LoginSmsPojo pojo = smsRedisListener.getLoginInfo(administerDTO.getMobilePhone(), SmsType.UPDATEPASS_SMS);
                if (pojo == null) {
                    return "请先获取验证码!";
                }

                if (!administerDTO.getSjmm().equals(pojo.getPassword())) {
                    return "验证码不匹配!";
                }

                //判断是否已超过5分钟
                Date after = DateUtil.getDateAfterMinutes(pojo.getLastTime(), 5);
                if (after.getTime() < (new Date()).getTime()) {
                    return "验证码已超时!";
                }
            }

            return "success";

        } else {
            return "缺少相关字段";
        }

    }


    /**
     * 用户密码修改 对输入的信息进行校验
     *
     * @param request
     * @param response
     * @return
     */
    public AdministerDTO userCheck(AdministerDTO administerDTO) {

        if (administerDTO.getMobilePhone() != null
            && administerDTO.getSjmm() != null) {
            AdministerDTO adt = (AdministerDTO) getSession().getAttribute(
                administerDTO.getMobilePhone());
            if (adt != null) {
                if (adt.getMsgTime()) {
                    if (administerDTO.getSjmm().equals(adt.getMessage())) {
                        if (administerDTO.getPassWord().equals(
                            administerDTO.getPassWord2())) {
                            administerDTO.setErrorMsg("NO");
                            return administerDTO;
                        }
                        administerDTO.setErrorMsg("二次密码输入不匹配!");
                        return administerDTO;
                    }
                    administerDTO.setErrorMsg("验证码错误!");
                    return administerDTO;
                }
                administerDTO.setErrorMsg("验证码已超时!");
                return administerDTO;
            }
            administerDTO.setErrorMsg("请先获取验证码!");
        }

        return null;
    }
   
    /**
     * @param administer
     * @param resp
     * @throws IOException
     * @throws
     * @Title:check
     * @Description: 校验是否重复性
     * @author: sunyiwei
     */
    @RequestMapping(value = "/check")
    public void check(Administer administer, HttpServletResponse resp)
        throws IOException {
        Boolean bFlag = administerService.checkUnique(administer);
        resp.getWriter().write(bFlag.toString());
    }

    /**
     * 邮箱更新
     *
     * @param modelMap
     * @param adminId
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/changeEmail")
    public String changeEmail(ModelMap modelMap, HttpServletResponse resp)
        throws IOException {

        Administer administer = getCurrentUser();
        modelMap.addAttribute("administer", administer);
        return "/user/account_email_change.ftl";
    }

    /**
     * 邮箱保存
     *
     * @param adminId
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/saveEmail")
    public String saveEmail(Administer administer, ModelMap modelMap, HttpServletResponse resp)
        throws IOException {
        if (getCurrentUser() == null || getCurrentUser().getId() == null) {
            return getLoginAddress();
        }
        if (!administer.getId().equals(getCurrentUser().getId())) {
            String responseMsg = "您更改的账户邮箱不是您登陆的账户！";
            modelMap.addAttribute("errorMsg", responseMsg);
            return "/user/account_email_change.ftl";
        }

        String newEmail = administer.getEmail();
        try {
            PropertyValidator.isEmail(newEmail, "邮件");
        } catch (SelfCheckException e) {
            logger.error("无效的邮箱地址.", e);

            modelMap.addAttribute("errorMsg", "无效的邮箱地址.");
            return "error.ftl";
        }

        administerService.updateSelective(administer);

        return "redirect:/manage/userInfo/showDetails.html?administerId=" + administer.getId();
    }

    /**
     * 姓名更新
     *
     * @param modelMap
     * @param adminId
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/changeUserName")
    public String changeUserName(ModelMap modelMap, Long adminId, HttpServletResponse resp)
        throws IOException {

        if (adminId == null) {
            modelMap.addAttribute("errorMsg", "无效的用户ID");
            return "error.ftl";
        }

        Administer admin = administerService.selectAdministerById(adminId);

        modelMap.addAttribute("administer", admin);
        return "/user/userName_change.ftl";
    }

    /**
     * 姓名保存
     *
     * @param adminId
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/saveUserName")
    public String saveUserName(Administer administer, ModelMap modelMap, HttpServletResponse resp)
        throws IOException {
        String newName = administer.getUserName();
        try {
            PropertyValidator.isChineseLowerNumberUnderline(newName, "用户名");
        } catch (SelfCheckException e) {
            logger.error("无效的用户名.", e);

            modelMap.addAttribute("errorMsg", "无效的用户名.");
            return "error.ftl";
        }

        administerService.updateSelective(administer);

        getSession().setAttribute("currentUserName", administer.getUserName());
        //request.getSession().setAttribute("currentUserName", currentUser.getUserName());

        return "redirect:/manage/userInfo/showDetails.html?administerId=" + administer.getId();
    }

    /**
     * 初始化administer
     *
     * @param administer
     */
    void initAdminister(Administer administer) {
        if (administer == null) {
            administer = new Administer();
        }

        if (administer.getUserName() == null) {
            administer.setUserName("");
        }
        if (administer.getPassword() == null) {
            administer.setPassword("");
        }
        if (administer.getMobilePhone() == null) {
            administer.setMobilePhone("");
        }
        if (administer.getEmail() == null) {
            administer.setEmail("");
        }
    }

    /**
     * 校验
     *
     * @param administer
     */
    String checkParameter(Administer administer) {
        String msg = "";
        if (administer != null) {
            if (!StringUtils.isEmpty(administer.getUserName())) {

                if (StringUtils.isValidPassword(administer.getPassword())) {
                    if (StringUtils.isValidMobile(administer.getMobilePhone())) {
                        return null;
                    } else {
                        msg = "手机号码格式不正确!";
                    }
                } else {
                    msg = "密码为空,或者长度不足6位!";
                }
            } else {
                msg = "姓名不能为空!";
            }
        }
        return msg;
    }

    /**
     * @param modelMap
     * @param administer
     * @return
     * @throws
     * @Title:validateCurrentUserInfo
     * @Description: 检验更新当前用户信息时的输入内容是否合法有效
     * @author: sunyiwei
     */
    private boolean validateCurrentUserInfo(ModelMap modelMap,
                                            Administer administer) {
        if (administer == null) {
            modelMap.addAttribute("errorMsg", "无效的用户对象!");
            return false;
        }

        try {
            /**
             * 检查姓名没有特殊字符且长度不大于64
             */
            String userName = administer.getUserName();
            PropertyValidator.isChineseLowerNumberUnderline(userName, "姓名称");
            PropertyValidator.maxLengthCheck(userName, 64, "姓名称");

            // 检查phone是否是电话格式
            PropertyValidator.isPhoneNum(administer.getMobilePhone(), "用户手机号码");

            /**
             * 邮箱地址
             */
            PropertyValidator.isEmail(administer.getEmail(), "邮箱地址");

        } catch (Exception e) {
            modelMap.addAttribute("errorMsg", e.toString());
            return false;
        }

        /**
         * 唯一性校验
         */
        if (!administerService.checkUnique(administer)) {
            modelMap.addAttribute("errorMsg", "姓名称或手机号码已存在!");
            return false;
        }

        return true;
    }
    
    /**
     * 检查密码有效期
     * @Title: checkPasswordUpdateTime 
     * @param map
     * @param mobile
     * @param password
     * @param type
     * @param imgCode
     * @param verifyCode
     * @param resp
     * @Author: wujiamin
     * @date 2016年10月14日下午4:07:31
     */
    @RequestMapping("checkPasswordUpdateTime")
    public void checkPasswordUpdateTime(ModelMap map, String mobile, String password, String type, String imgCode, String verifyCode, HttpServletResponse resp) {
        Map returnMap = new HashMap();
        
        returnMap.put("expire", "false");

        String token = (String)getSession().getAttribute("SESSION_LOGIN_TOKEN");
        try {
            password = AESUtil.aesDecrypt(password, token);
            
            if(checkLoginMsg(mobile, password, type, imgCode, verifyCode)){
                if(!administerService.checkPasswordUpdateTime(mobile)){
                    returnMap.put("expire", "true");
                    returnMap.put("date", globalConfigService.get(GlobalConfigKeyEnum.PASSWORD_EXPIRE_TIME.getKey()));
                }
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            logger.info("解密密码失败，失败原因："+e1.getMessage());
            returnMap.put("expire", "true");
            returnMap.put("date", globalConfigService.get(GlobalConfigKeyEnum.PASSWORD_EXPIRE_TIME.getKey()));
        }
        String data = JSON.toJSONString(returnMap);
        try {
            resp.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //校验登录参数
    /**
    * @Title: checkLoginMsg
    * @Description: 
    */ 
    private boolean checkLoginMsg(String mobile, String password, String type, String imgCode, String verifyCode){
        String config_value = globalConfigService.get(config_key);
        if (config_value != null && !check_pass_key.equals(config_value)) {
            return true;
        }

        //1、校验图形验证码
        String sessionCode = (String) getSession().getAttribute("virifyCode");//得到session中的验证码
        if (!(imgCode.equalsIgnoreCase(sessionCode))) {
            return false;
        }
        
        //2、校验静态密码验证
        if ("1".equals(type)) {
            // 检查静态密码
            if (!administerService.checkPassword(password, mobile)) {
                logger.info("静态密码登录，静态密码错误");
                return false;
            }
        }
        //动态密码验证
        else {
            //开始和redis中存储的最新验证码值进行检测，验证是否一致
            LoginSmsPojo pojo = smsRedisListener.getLoginInfo(mobile, SmsType.WEBINLOGIN_SMS);//从redis中得到相关用户短信信息

            if (pojo == null) {//未发送短信
                logger.info("get LoginSmsPojo from redis failed, please check redis connection, with mobile=" + mobile);
                return false;

            } else if (!verifyCode.equals(pojo.getPassword())) {//密码不一致
                return false;
            }

            //判断是否已超过5分钟
            Date after = DateUtil.getDateAfterMinutes(pojo.getLastTime(), 5);
            if (after.getTime() < (new Date()).getTime()) {
                logger.info("verify code expired, with mobile=" + mobile);
                return false;
            }
        }
        return true;
    }
}

package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.AdministerDTO;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.sms.login.LoginSmsPojo;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.util.AESUtil;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.StringUtils;

/**
 * 平台用户密码重置
 * ResetPasswordController.java
 * @author wujiamin
 * @date 2017年6月19日
 */
@Controller
@RequestMapping("manage/resetpwd")
public class ResetPasswordController extends BaseController {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ResetPasswordController.class); 
    
    private final String config_key = "RANDOMPASS_CHECK";//数据库中globalconfig的随机验证码验证key值
    private final String check_pass_key = "OK";//globalconfig需要检验随机密码的value值
    
    @Autowired
    private AdministerService administerService;
    
    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Autowired
    private SmsRedisListener smsRedisListener;
    
    @Value("#{settings['redis.sessiontimeout']}")  
    private Integer sessionTimeOut;
    
    /** 
     * 重置密码的首页
     * @Title: resetPasswordIndex 
     */
    @RequestMapping(value = "/reset")
    public String resetPasswordIndex(){
        return "resetPassword/chooseRole.ftl";
    }
    
    /** 
     * 验证密码重置时提交的验证信息是否正确
     * @Title: verifyInfo 
     */
    @RequestMapping(value = "/verifyInfo")
    public void verifyInfo(Integer type, String entName, String customerManagerMobile, String mobile, String userName, HttpServletResponse res) {
        //重新设置session过期时间
        getSession().setMaxInactiveInterval(sessionTimeOut);
        JSONObject json = new JSONObject();
        json.put("verify", false);
        boolean verify = administerService.verifyResetPsd(type, entName, customerManagerMobile, mobile, userName);
        if(verify){//验证通过，当前用户信息存在session，并设置session有效期
            json.put("verify", true);
            Administer currentUser = administerService.selectByMobilePhone(mobile);
            getSession().setAttribute("currentUserId", currentUser.getId());
            getSession().setAttribute("currentUserName", currentUser.getUserName());
            getSession().setAttribute("mobile", mobile);
        }
        
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    
    /** 
     * 修改密码页面
     * @Title: modifyPwd 
     */
    @RequestMapping(value = "/modifyPwd")
    public String modifyPwd(ModelMap model){
        Administer administer = getCurrentUser();
        if (administer == null) {
            LOGGER.error("session中不存在用户！");
            String responseMsg = "修改密码失败";
            model.addAttribute("errorMsg", responseMsg);
            return "error.ftl";
        }
        return "resetPassword/modifyPassword.ftl";
    }
    
    /**
     * 用户密码修改
     *
     * @param model
     * @param administerDTO
     * @return
     */
    @RequestMapping("/savePassword")
    public void savePassword(String code, String newPwd, String confirmPwd, HttpServletResponse res) {
        JSONObject json = new JSONObject();
        json.put("done", false);
        Administer administer = getCurrentUser();
        if (administer == null) {
            LOGGER.error("session中不存在用户！");
            String responseMsg = "修改密码失败";
            json.put("errorMsg", responseMsg);
            try {
                res.getWriter().write(json.toString());                
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                
            }
            return;
        }
        
        //解密密码
        String key = RandomStringUtils.randomAlphanumeric(16);
        String token = (String)getSession().getAttribute("SESSION_LOGIN_TOKEN");
        try {
            newPwd = AESUtil.aesDecrypt(newPwd, token);
            confirmPwd = AESUtil.aesDecrypt(confirmPwd, token);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.info("解密密码报错,错误原因："+e.getMessage());
            getSession().setAttribute("SESSION_LOGIN_TOKEN", key);
        }
        
        //复用登录后的修改密码的方法
        AdministerDTO administerDTO = new AdministerDTO();
        administerDTO.setSjmm(code);
        administerDTO.setPassWord(newPwd);
        administerDTO.setPassWord2(confirmPwd);
        administerDTO.setMobilePhone(administer.getMobilePhone());

        String checkResult = updatePassUserCheck(administerDTO);
        String responseMsg = "";
        if ("success".equals(checkResult)) {
            if (administerService.updateAdministerPassword(getCurrentUser()
                .getId(), administerDTO.getPassWord())) {
                responseMsg = "修改密码成功";
                json.put("done", true);
                getSession().removeAttribute("SESSION_LOGIN_TOKEN");
            } else {
                responseMsg = "密码修改失败！";
                getSession().setAttribute("SESSION_LOGIN_TOKEN", key);
            }
        } else {
            responseMsg = checkResult;
        }
        json.put("errorMsg", responseMsg);
        
        LOGGER.info("用户ID:" + getCurrentUser().getId() + responseMsg);
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    
    private String updatePassUserCheck(AdministerDTO administerDTO) {
        if (administerDTO.getMobilePhone() != null
            && administerDTO.getSjmm() != null) {

            if (!StringUtils.isValidMobile(administerDTO.getMobilePhone())) {
                return "手机号码异常!";
            }

            if (StringUtils.isEmpty(administerDTO.getPassWord())) {
                LOGGER.error("新建密码为空!");
                return "请输入包含字母、数字、特殊字符的10-20位密码";
            }

            if (StringUtils.isEmpty(administerDTO.getPassWord2())) {
                LOGGER.error("确认密码为空!");
                return "请输入包含字母、数字、特殊字符的10-20位密码";
            }

            if (!administerDTO.getPassWord().equals(administerDTO.getPassWord2())) {
                return "确认密码与新密码不一致";
            }

            if (!StringUtils.isValidPassword(administerDTO.getPassWord())) {
                return "请输入包含字母、数字、特殊字符的10-20位密码";
            }

            //从数据库的global_config表中得到config_key对应的value值，如果不存在或者值不为"OK"则不进行检测验证码，返回
            String config_value = globalConfigService.get(config_key);
            if (config_value == null || !check_pass_key.equals(config_value)) {
                return "success";
            }
            
            //增加验证的次数
            String timesKey = administerDTO.getMobilePhone() + SmsType.RESET_PASSWORD.getSuffix() + "Times";
            String verifyCodeKey = administerDTO.getMobilePhone() + SmsType.RESET_PASSWORD.getSuffix();
            Long verifyTimes = smsRedisListener.incrVerifyTimes(administerDTO.getMobilePhone(), SmsType.RESET_PASSWORD);
            LOGGER.info("当前用户{}重置密码已验证{}次验证码", administerDTO.getMobilePhone(), verifyTimes);
            if(verifyTimes==null || verifyTimes>3L){                
                //删除验证码的缓存
                if(!smsRedisListener.delete(verifyCodeKey)){
                    LOGGER.error("redis中删除验证码失败，redis-key={}", verifyCodeKey);
                }
                
                //删除验证码次数的缓存
                if(!smsRedisListener.delete(timesKey)){
                    LOGGER.error("redis中删除验证码验证次数失败，redis-key={}", timesKey);
                }
                
                LOGGER.info("验证码已失效，删除了缓存中的验证码和验证次数!");
                return "请输入正确的验证码";
            }
            
            LoginSmsPojo pojo = smsRedisListener.getLoginInfo(administerDTO.getMobilePhone(), SmsType.RESET_PASSWORD);
            if (pojo == null) {
                LOGGER.info("缓存中无验证码，请先获取验证码!");                
                return "请输入正确的验证码";
            }

            
            if (!administerDTO.getSjmm().equals(pojo.getPassword())) {
                LOGGER.info("用户输入的验证码{}，与缓存中的验证码{}不匹配!", administerDTO.getSjmm(), pojo.getPassword());                
                return "请输入正确的验证码";
            }

            //判断是否已超过5分钟
            Date after = DateUtil.getDateAfterMinutes(pojo.getLastTime(), 5);
            if (after.getTime() < (new Date()).getTime()) {
                LOGGER.info("验证码已超时!");                
                return "请输入正确的验证码";
            }
            
            //删除验证码验证的次数
            if(!smsRedisListener.delete(timesKey)){
                LOGGER.error("redis中删除验证码验证次数失败,redis-key={}", timesKey);
            }
            return "success";

        } else {
            return "缺少相关字段";
        }
    }

    /** 
     * 修改密码成功后的确认页
     * @Title: confirmBack 
     */
    @RequestMapping(value = "/confirmBack")
    public String confirmBack(ModelMap model){
       return "resetPassword/confirmBack.ftl";
    }
    
}

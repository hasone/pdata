/**
 * @Title: RandomCodeAuthenticationFilter.java
 * @Package com.cmcc.vrp.province.security
 * @author: qihang
 * @date: 2015年6月10日 下午6:12:12
 * @version V1.0
 */
package com.cmcc.vrp.province.security;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.shangdong.boss.service.SdManagerService;
import com.cmcc.vrp.boss.shangdong.boss.service.SdUserInfoService;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.impl.LoginFailRateLimitServiceImpl;
import com.cmcc.vrp.province.service.impl.LoginSuccessRateLimitServiceImpl;
import com.cmcc.vrp.province.sms.login.LoginSmsPojo;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.util.AESUtil;
import com.cmcc.vrp.util.AESdecry;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @ClassName: RandomCodeAuthenticationFilter
 * @Description: 设置的spring_security的拦截器，增加了对登陆短信随机验证码的检验
 * @author: qihang
 * @date: 2015年6月10日 下午6:12:12
 */
public class RandomCodeAuthenticationFilter extends
        UsernamePasswordAuthenticationFilter {

    private final String config_key = "RANDOMPASS_CHECK";//数据库中globalconfig的随机验证码验证key值
    private final String check_pass_key = "OK";//globalconfig需要检验随机密码的value值

    
    @Autowired
    SmsRedisListener smsRedisListener;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    AdministerService administerService;

    @Autowired
    AdminManagerService adminManagerService;

    @Autowired
    SdManagerService sdManagerService;

    @Autowired
    SdUserInfoService sdkUserInfo;

    @Autowired
    LoginSuccessRateLimitServiceImpl loginSuccessRateLimitService;

    @Autowired
    LoginFailRateLimitServiceImpl loginFailRateLimitService;

    @Autowired
    ManagerService managerService;

    private Logger logger = Logger.getLogger(RandomCodeAuthenticationFilter.class);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest = null;
        boolean isManager = false;
        String userToken = request.getParameter("userToken");
        String isManagerStr = request.getParameter("isManager");
        // logger.info("链接跳转请求参数：" + request.getParameter("isManager")+"测试");
        if (isManagerStr != null && isManagerStr.trim().equals("true")) {
            isManager = true;
        }

        if(isGDUseSSO()){//是广东所属环境,解密ssotoken登录,无但单次登录限制
            String ssoToken = request.getParameter("ssoToken");
            String mobile = gdAnalyseSSOToken(ssoToken);
            
            Administer admin = administerService.selectByMobilePhone(mobile);
            if (admin == null) {
                logger.info("用户不存在！gusetLogin is 1 and admin is null");
                throw new UsernameNotFoundException(guestLoginMsg("账户信息错误!"), new Object[]{mobile});
            }
            authRequest = new UsernamePasswordAuthenticationToken(
                    mobile, "");
        }
        else if (StringUtils.isEmpty(userToken)) {// userToken为空时，为非企业管理员及客户经理角色登录
            String mobile = request.getParameter("j_username");
            String loginPass = request.getParameter("j_code");
            String type = request.getParameter("type");
            
            //如果出现登陆校验失败，则更行session中的key
            HttpSession session = request.getSession();
            //String key = RandomStringUtils.randomAlphanumeric(16);;
            String token = (String)session.getAttribute("SESSION_LOGIN_TOKEN");
            
            
            //解密手机号
            try {
                mobile = AESUtil.aesDecrypt(mobile, token);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.info("解密密码报错,错误原因："+e.getMessage());
                //session.setAttribute("SESSION_LOGIN_TOKEN", key);
                throw new UsernameNotFoundException("账户信息错误，请核对姓名及密码是否正确!", new Object[]{mobile});
            }
            
            logger.info("解密后的手机号：mobile = " + mobile);
            //登录频率限制验证
            try {
                loginSuccessRateLimitService.allowToContinue(mobile);
                loginFailRateLimitService.allowToContinue(mobile);
            } catch (RuntimeException e) {
                logger.error("用户登录操作过于频繁，已被阻止登录. Mobile = " + mobile);
                throw new UsernameNotFoundException(e.getMessage(), new Object[]{mobile});
            }

            Administer admin = administerService.selectByMobilePhone(mobile);

            //检验图片验证码
            checkImgCode(request);

            //游客登录开关
            String gusetLogin = globalConfigService.get(GlobalConfigKeyEnum.GUEST_LOGIN.getKey());


            //不允许游客登录
            if ("1".equals(gusetLogin)) {
                if (admin == null) {
                    logger.info("用户不存在！gusetLogin is 1 and admin is null");
                    throw new UsernameNotFoundException(guestLoginMsg("用户不存在!"), new Object[]{request.getParameter("j_username")});
                }

                //测试山东是否从云平台登录，有配置项
                sdCheckRoleValid(admin);
            }

            //不允许游客和无职位的用户登录
            if ("2".equals(gusetLogin)) {
                if (admin == null) {
                    logger.info("用户不存在！gusetLogin is 2 and admin is null");
                    throw new UsernameNotFoundException(guestLoginMsg("用户不存在!"), new Object[]{request.getParameter("j_username")});
                }
                Long managerId = adminManagerService.selectManagerIdByAdminId(admin.getId());
                if (managerId == null || managerId.equals(-1L)) {
                    logger.info("用户无登录权限！gusetLogin is 2 and managerId is" + managerId);
                    throw new UsernameNotFoundException(guestLoginMsg("用户无登录权限!"), new Object[]{request.getParameter("j_username")});
                }

                //测试山东是否从云平台登录，有配置项
                sdCheckRoleValid(admin);
            }
            

            //静态密码验证
            if ("1".equals(type)) {
                //传入的是加密后的密码，解密

                //解密密码
                try {
                    loginPass = AESUtil.aesDecrypt(loginPass, token);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.info("解密密码报错,错误原因："+e.getMessage());
                    //session.setAttribute("SESSION_LOGIN_TOKEN", key);
                    throw new UsernameNotFoundException("账户信息错误，请核对姓名及密码是否正确!", new Object[]{mobile});
                }

                // 检查静态密码
                if (!administerService.checkPassword(loginPass, mobile)) {
                    logger.info("静态密码登录，静态密码错误");

                    //静态密码错误需要更新session中的key值
                    //在请求登陆页面时，生成用于加密密码的key值，存在session中
                    //session.setAttribute("SESSION_LOGIN_TOKEN", key);

                    throw new UsernameNotFoundException("账户信息错误，请核对姓名及密码是否正确!", new Object[]{mobile});
                }
            } else if ("2".equals(type)){
            	//动态密码验证
                //检验手机收到的随机验证码是否正确
                checkRandomMobileCode(request, mobile);

                //验证是否是首次登陆
                Administer administer = administerService.selectByMobilePhone(mobile);
                if (administer == null) {
                    //四川集中化平台,新用户的创建有所不同
                    if (globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()).equals("sc_jizhong")) {
                        if (!administerService.insertForScJizhong(mobile)) {
                            logger.info("四川集中化平台首次登陆自动注册账户失败！");
                            throw new UsernameNotFoundException("首次登陆自动注册账户失败!", new Object[]{request.getParameter("j_username")});
                        }
                    } else {
                        if (!administerService.insert(mobile)) {
                            logger.info("首次登陆自动注册账户失败！");
                            throw new UsernameNotFoundException("首次登陆自动注册账户失败!", new Object[]{request.getParameter("j_username")});
                        }
                    }
                }
            } else {
                Administer administer = administerService.selectByMobilePhone(mobile);
                //解密密码
                try {
                    loginPass = AESUtil.aesDecrypt(loginPass, token);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    logger.info("解密密码报错,错误原因："+e.getMessage());
                    //session.setAttribute("SESSION_LOGIN_TOKEN", key);
                    throw new UsernameNotFoundException("账户信息错误，请核对姓名及密码是否正确!", new Object[]{mobile});
                }
                

                if (administer != null
                        && !administerService.checkPassword(loginPass, mobile)) {
                    logger.info("静态密码登录，静态密码错误");
                    //静态密码错误需要更新session中的key值
                    //在请求登陆页面时，生成用于加密密码的key值，存在session中
                    //session.setAttribute("SESSION_LOGIN_TOKEN", key);

                    throw new UsernameNotFoundException("账户信息错误，请核对姓名及密码是否正确!", new Object[]{mobile});
                }
                //动态密码验证
                //检验手机收到的随机验证码是否正确
                checkRandomMobileCode(request, mobile);

                //验证是否是首次登陆
//                Administer administer = administerService.selectByMobilePhone(mobile);
                if (administer == null) {
                    //四川集中化平台,新用户的创建有所不同
                    if (globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()).equals("sc_jizhong")) {
                        if (!administerService.insertForScJizhong(mobile)) {
                            logger.info("四川集中化平台首次登陆自动注册账户失败！");
                            throw new UsernameNotFoundException("首次登陆自动注册账户失败!", new Object[]{request.getParameter("j_username")});
                        }
                    } else {
                        if (!administerService.insert(mobile)) {
                            logger.info("首次登陆自动注册账户失败！");
                            throw new UsernameNotFoundException("首次登陆自动注册账户失败!", new Object[]{request.getParameter("j_username")});
                        }
                    }
                }
            }

            //重复登录验证
            if (admin != null) {
                Boolean hasLogin = MySessionListener.checkIfHasLogin(admin.getId());

                // 如果重复登录，控制端则打印信息，返回登录页面
                if (hasLogin) {
                    // savagechen11 test session
                    logger.info("重复登录啦[" + admin.getId() + "]");
                    
                    MySessionListener.removeUserSession(admin.getId());
                    throw new UsernameNotFoundException("该用户已登录，无法重复登录!", new Object[]{mobile});
                }
            }
            // UsernamePasswordAuthenticationToken实现Authentication校验  
            authRequest = new UsernamePasswordAuthenticationToken(
                    mobile, loginPass);
        } else {
            //userToken不为空时，为山东平台的企业管理员及客户经理角色登录
            logger.info("获取到userToken:" + userToken + "；山东客户经理或企业管理员登录!");
            Administer administer = sdkUserInfo.getUserInfo(userToken, isManager);
            //如果本地数据库存在用户
            if (administer != null) {
                //如果是客户经理，更新客户经理关联的企业信息
                if (isManager) {
                    logger.info("客户经理已存在，数据库信息为：" + JSONObject.toJSONString(administer));
                    sdManagerService.updateManager(userToken);
                    authRequest = new UsernamePasswordAuthenticationToken(administer.getMobilePhone(), null);
                } else {
                    authRequest = new UsernamePasswordAuthenticationToken(administer.getMobilePhone(), null);
                }
            } else {
                //不存在该用户，如果是客户经理第一次登录则创建企业
                if (isManager) {
                    String mobile = sdManagerService.createManager(userToken);
                    if (mobile == null) {
                        throw new UsernameNotFoundException("客户经理登录流量平台失败");
                    }
                    if ("-1".equals(mobile)) {
                        throw new UsernameNotFoundException("该用户无权限使用流量平台");
                    }
                    authRequest = new UsernamePasswordAuthenticationToken(mobile, null);
                } else {
                    throw new UsernameNotFoundException("未找到指定账号信息");
                }
            }
        }
        // 允许子类设置详细属性  
        setDetails(request, authRequest);
        
        // savagechen11 test session
        logger.info("验证完成了");

        // 运行UserDetailsService的loadUserByUsername 再次封装Authentication  
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 检测验证码的是否正确
     *
     * @param request 请求参数
     */
    public void checkValidateCode(HttpServletRequest request) {
        String mobile = request.getParameter("j_username");//手机号

        //检测图片验证码是否正确
        checkImgCode(request);

        //检验手机收到的随机验证码是否正确
        checkRandomMobileCode(request, mobile);
    }

    /**
     * 检测图片验证码是否正确
     *
     * @param request 请求参数
     */
    private void checkImgCode(HttpServletRequest request) {
        String configValue = globalConfigService.get(config_key);
        if (configValue != null && !check_pass_key.equals(configValue)) {
            return;
        }
        String verify = request.getParameter("verify"); //得到图形验证码
        String sessionCode = (String) request.getSession(true).getAttribute("virifyCode");//得到session中的验证码
        if (!(verify.equalsIgnoreCase(sessionCode))) {
            throw new UsernameNotFoundException("图片验证码信息错误，请核对!", new Object[]{request.getParameter("j_username")});
        }
    }


    //检验手机收到的随机验证码是否正确
    private void checkRandomMobileCode(HttpServletRequest request, String mobile) {
        String randomPassword = request.getParameter("randomPassword");//随机验证码

        //从数据库的global_config表中得到config_key对应的value值，如果不存在或者值不为"OK"则不进行检测验证码，返回
        String configValue = globalConfigService.get(config_key);
        if (configValue == null || !check_pass_key.equals(configValue)) {
            return;
        }

        //开始和redis中存储的最新验证码值进行检测，验证是否一致
        LoginSmsPojo pojo = smsRedisListener.getLoginInfo(mobile, SmsType.WEBINLOGIN_SMS);//从redis中得到相关用户短信信息

        if (pojo == null) {//未发送短信

            logger.info("get LoginSmsPojo from redis failed, please check redis connection, with mobile=" + mobile);

            throw new UsernameNotFoundException("短信验证码错误，请重试!", new Object[]{mobile});

        } else if (!randomPassword.equals(pojo.getPassword())) {//密码不一致

            logger.info("verify code validate failed, expected " + pojo.getPassword() + ", actual " + randomPassword + ", with mobile=" + mobile);

            throw new UsernameNotFoundException("短信验证码错误，请重试!", new Object[]{mobile});
        }

        //判断是否已超过5分钟
        Date after = DateUtil.getDateAfterMinutes(pojo.getLastTime(), 5);
        if (after.getTime() < (new Date()).getTime()) {

            logger.info("verify code expired, with mobile=" + mobile);

            throw new UsernameNotFoundException("短信验证码错误，请重试!", new Object[]{mobile});
        }
    }

    /**
     * 测试山东是否从云平台登录，有配置项 当打开时，若客户经理或企业关键人登录，返回异常,信息为企业管理员或客户经理请通过云门户登录！
     */
    private boolean sdCheckRoleValid(Administer admin) {
        if ("ON".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.SD_LOGIN_ON_CLOUDPLATFORM.getKey()))) {
            Manager manager = managerService.selectByAdminId(admin.getId());
            String accountManagerRole = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
            String enterpriseContactorRole = globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());

            if (manager == null
                    || manager.getRoleId().equals(NumberUtils.toLong(accountManagerRole))
                    || manager.getRoleId().equals(NumberUtils.toLong(enterpriseContactorRole))) {
                throw new UsernameNotFoundException("企业管理员或客户经理请通过云门户登录！", new Object[]{admin.getMobilePhone()});
            }
        }

        return true;

    }

    private String guestLoginMsg(String defaultValue) {
        String guestLoginMsg = globalConfigService.get(GlobalConfigKeyEnum.GUEST_LOGIN_MSG.getKey());
        return org.apache.commons.lang.StringUtils.isBlank(guestLoginMsg) ? defaultValue : guestLoginMsg;
    }
    
    /**
     * 检测是广东环境,且使用sso登录
     */
    private boolean isGDUseSSO(){
        return "gd_mdrc".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())) &&
                "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_IS_USE.getKey()));
    }
    
    /**
     * 分析ssotoken，获取手机号
     * 
     */
    private String gdAnalyseSSOToken(String token){
        String decryData = AESdecry.decrypt(token, getAESPrivateKey());
        if(org.apache.commons.lang.StringUtils.isBlank(decryData)){
            return "";
        }
        //解密后数据格式为 时间戳&手机号 例如 123456&18867101234
        String[] parties = decryData.split("&");
        
        String timeStamp = parties.length<2 ? "":parties[0];
        
        //判断时间戳，不得和当前时间大于1分钟
        if(!StringUtils.isNumeric(timeStamp)){
            return "";
        }else if(Math.abs(new DateTime().getMillis() - NumberUtils.toLong(timeStamp)) 
                > 60 * 1000){
            return "";
        }

        String mobile = parties.length<2 ? "":parties[1];
        return mobile;
    }
    
    /**
     * 广东单点登录加密登录地址使用私钥
     */
    private String getAESPrivateKey(){
        return globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_PRIVATE_KEY.getKey());
    }
    
}

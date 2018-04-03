package com.cmcc.vrp.province.security;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.impl.LoginSuccessRateLimitServiceImpl;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: LoginSuccessHandle
 * @Description: springsecurity登陆成功后的处理类
 * @author: qihang
 * @date: 2015年4月3日 下午3:39:00
 */
public class LoginSuccessHandle implements AuthenticationSuccessHandler {

    @Autowired
    EntProductService entProductService;

    @Autowired
    ManagerService managerService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    LoginSuccessRateLimitServiceImpl loginSuccessRateLimitService;

    private Logger logger = Logger.getLogger(LoginSuccessHandle.class);

    /**
     *  (non-Javadoc)
     * AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
     */
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //0.清除session中验证码
        HttpSession session = request.getSession();
        //删除session中用于加密密码的key
        session.removeAttribute("SESSION_LOGIN_TOKEN");
        session.invalidate();

        //1.session中加入登陆用户的所有权限
        //从springsecurity中得到登陆用户拥有的权限
        Collection<GrantedAuthority> authCollection = (Collection<GrantedAuthority>) authentication.getAuthorities();
        if (authCollection.isEmpty()) {
            return;
        }

        //将所有权限名取出组成List添加到session中
        List<String> authNames = new ArrayList<String>();

    	/*//潜在客户功能所有人都有
        authNames.add("ROLE_POTENTIAL_CUSTOMER");*/
        for (GrantedAuthority grantedAuthority : authCollection) {
            authNames.add(grantedAuthority.getAuthority());
        }
        request.getSession().setAttribute("authNames", authNames);

        //2.session中加入登陆用户的所有权限
        //得到登陆用户的相关信息
        UserAuthorityDetailImpl userAuthorityDetailImpl = (UserAuthorityDetailImpl) authentication.getPrincipal();

        //得到姓名
        String userName = userAuthorityDetailImpl.getUsername();

        //得到AdministerService,然后从数据库中查找该电话号码对应的administer类
        AdministerService service = userAuthorityDetailImpl.getAdministerService();

        //得到administer存入到session中
        Administer currentUser = service.selectByMobilePhone(userName);
        request.getSession().setAttribute("currentUserId", currentUser.getId());
        request.getSession().setAttribute("currentUserName", currentUser.getUserName());

        //1.登录成功，更新限制信息
        String mobile = currentUser.getMobilePhone();
        loginSuccessRateLimitService.add(mobile);

        // 如果没有重复登录，则将该登录的用户信息添加入session中
        request.getSession().setAttribute("mobile", mobile);
        // 比较保存所有用户session的静态变量中，是否含有当前session的键值映射，如果含有就删除  
        if (MySessionListener.containsKey(request.getSession().getId())) {
            // savagechen11 test session
            logger.info("发现已有session[" + request.getSession().getId() + "]");
            MySessionListener.removeSession(request.getSession().getId());
        }
        
        // savagechen11 test session
        logger.info("新建session[" + request.getSession().getId() + "]");
        
        //把当前用户封装的session按，sessionID和session进行键值封装，添加到静态变量map中。  
        MySessionListener.addUserSession(request.getSession());
        
        //如果用户是企业管理员,企业合作信息过期或企业营业执照过期，request中存储expire标记        
        Manager manager = managerService.selectByAdminId(currentUser.getId());
        if (manager != null && manager.getRoleId().toString().equals(getEnterpriseContactor())) {
            List<Enterprise> enters = enterprisesService.getAllEnterByManagerId(manager.getId());
            if (enters != null && enters.size() > 0) {
                for (Enterprise enter : enters) {
                    if ((enter.getEndTime() != null && enter.getEndTime().before(new Date()))
                        || (enter.getLicenceEndTime() != null && enter.getLicenceEndTime().before(new Date()))) {
                        request.getSession().setAttribute("expire", "true");
                    }
                }
            }
        }

        logger.info("用户ID:" + currentUser.getId() + " 成功登录,手机号：" + userName);

        //redirect
        if (globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()).equals("sc_jizhong")) {
            response.sendRedirect(request.getContextPath() + "/manage/portal/loginIndex.html");
        } else if(isGDUseSSO()){
            response.sendRedirect(request.getContextPath() + "/manage/gdIndex.html");
        }  else {           
            response.sendRedirect(request.getContextPath() + "/manage/index.html");
        }
        
        // savagechen11 test session
        return;
    }

    private String getEnterpriseContactor() {
        return globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
    }
    
    /**
     * 检测是广东环境,且使用sso登录
     */
    private boolean isGDUseSSO(){
        return "gd_mdrc".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())) &&
                "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_IS_USE.getKey()));
    }
}

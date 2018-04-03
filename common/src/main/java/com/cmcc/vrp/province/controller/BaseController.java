package com.cmcc.vrp.province.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cmcc.vrp.province.model.AdminRole;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:04:50
*/
public abstract class BaseController {

    private static Logger logger = Logger.getLogger(BaseController.class);

    @Autowired
    private AdministerService administerService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private RoleService roleService;

    /**
     * @return
     * @throws
     * @Title:getSession
     * @Description: 返回当前会话
     * @author: sunyiwei
     */
    public HttpSession getSession() {
        HttpSession session = null;
        try {
            session = getRequest().getSession();
        } catch (Exception e) {
            // e.printStackTrace();
            logger.error("获取session异常", e);
        }
        return session;
    }

    /**
     * @return
     * @throws
     * @Title:getRequest
     * @Description: 返回请求
     * @author: sunyiwei
     */
    public HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getRequest();
    }

    /**
     * @return
     * @throws
     * @Title:getCurrentUser
     * @Description: 得到当前登录的用户对象
     * @author: sunyiwei
     */
    public Administer getCurrentUser() {
        Long administerId = (Long) getSession().getAttribute("currentUserId");
        if (administerId == null) {
            return null;
        }

        return administerService.selectAdministerById(administerId);
    }

    /**
     * @return
     * @throws
     * @Title:getAuthorities
     * @Description: 得到当前会话的登录用户的权限项
     * @author: sunyiwei
     */
    public List<String> getAuthorities() {
        Administer administer = getCurrentUser();
        if (administer == null) {
            return null;
        }

        return (List<String>) getSession().getAttribute("authNames");
    }

    /**
     * 判断角色是否为客户经理
     *
     * @return
     */
    protected boolean isCustomManager() {
        Administer admin = getCurrentUser();
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(admin.getId().longValue());
        adminRole.setRoleId(Long.parseLong(getCUSTOM_MANAGER()));
        if (adminRoleService.selectCountByQuery(adminRole) > 0) {
            return true;
        }
        return false;
    }

    /**
     * @param name
     * @param queryObject
     * @throws
     * @Title:setQueryParameter
     * @Description: 获取并设置查询参数
     * @author: sunyiwei
     */
    public void setQueryParameter(String name, QueryObject queryObject) {

        Integer pageSize = (Integer) getSession().getAttribute("pageSize");
        if (pageSize == null) {
            pageSize = 10;
        }
        queryObject.setPageSize(pageSize);
        setQueryParameter(name, queryObject, null);
    }

    /**
     * @param name
     * @param queryObject
     * @param defaultValue
     * @throws
     * @Title:setQueryParameter
     * @Description: 获取并设置参数
     * @author: sunyiwei
     */
    public void setQueryParameter(String name, QueryObject queryObject, Object defaultValue) {
        if (getRequest() == null) {
            return;
        }
        //	 String value = StringUtils.trimString(getRequest().getParameter(name));
        String value = StringUtils.trimAllString(getRequest().getParameter(name));
        String newValue = transferQuery(value);
        if (!org.apache.commons.lang.StringUtils.isBlank(newValue)) {
            queryObject.getQueryCriterias().put(name, newValue);
        } else {
            if (defaultValue != null) {
                queryObject.getQueryCriterias().put(name, defaultValue);
            }/*else {
                //参数为空，去掉改参数
             queryObject.getQueryCriterias().remove(name);
             }*/
        }
    }

    /**
     * @param query
     * @return
     * @throws
     * @Title:transferQuery
     * @Description: 在模糊查询时，对输入的内容中含有：%和_ 进行转义
     * @author: xuwanlin
     */
    public String transferQuery(String query) {
        if (org.apache.commons.lang.StringUtils.isBlank(query)) {
            return null;
        }
        char[] chars = query.toCharArray();
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '%' || chars[i] == '_') {
                strBuffer.append("\\" + chars[i]);
            } else {
                strBuffer.append(chars[i]);
            }
        }

        return strBuffer.toString();
    }

    /**
     * 
     * @Title: getLoginAddress 
     * @Description: TODO
     * @return
     * @return: String
     */
    protected String getLoginAddress() {
        return "redirect:/manage/user/login.html";
    }

    /**
     * 
     * @Title: getCUSTOM_MANAGER 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getCUSTOM_MANAGER() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
    }

    /**
     * 
     * @Title: getENTERPRISE_CONTACTOR 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getENTERPRISE_CONTACTOR() {
        return globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_CONTACTOR_ROLE_ID.getKey());
    }

    /**
     * 
     * @Title: getSuperAdminRoleId 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getSuperAdminRoleId() {
        return globalConfigService.get(GlobalConfigKeyEnum.SUPER_ADMIN_ROLE_ID.getKey());
    }

    /**
     * 
     * @Title: getFLOWCARD_ADMIN 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getFLOWCARD_ADMIN() {
        return globalConfigService.get(GlobalConfigKeyEnum.FLOW_CARD_MANAGER_ROLE_ID.getKey());
    }
}

/**
 * @Title: RoleController.java
 * @Package com.cmcc.vrp.province.webin.controller
 * @author: sunyiwei
 * @date: 2015年3月12日 下午3:37:45
 * @version V1.0
 */
package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.service.AuthorityService;
import com.cmcc.vrp.province.service.RoleAuthorityService;
import com.cmcc.vrp.province.service.RoleCreateService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;

/**
 * RoleController.java
 */
@Controller("manage.roleController")
@RequestMapping("/manage/role")
public class RoleController extends BaseController {

    private Logger logger = Logger.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private RoleAuthorityService roleAuthorityService;

    @Autowired
    private RoleCreateService roleCreateService;

    /**
     * @param model
     *            如果为null， 则返回新建页面，否则返回编辑页面
     * @return
     * @throws
     * @Title:createOrEdit
     * @Description: 返回相应的新建或编辑页面
     * @author: sunyiwei
     */
    @RequestMapping("addOrEdit")
    public String addOrEditShow(ModelMap model, Role role) {
        List<Authority> authorities = authorityService.selectAllAuthority();

        Long roleId = role.getRoleId();
        if (roleId != null) {

            role = roleService.getRoleById(roleId);

            /**
             * 获取当前角色的ID
             */
            List<Long> authIds = roleAuthorityService
                .selectAuthsByRoleId(roleId);
            model.addAttribute("authIds", authIds);
        }

        model.addAttribute("role", role);
        model.addAttribute("authorities", authorities);
        return "role/addOrEdit.ftl";
    }

    /**
     * @param model
     * @param role
     * @return
     * @throws
     * @Title:save
     * @Description: 保存新建或编辑的角色对象
     * @author: sunyiwei
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String addOrEditSubmit(ModelMap model, Role role, Long[] authorityIds) {

        role.setName(role.getName().replaceAll(" ", ""));//过滤掉角色名称中间的空格

        if (!validateRole(model, role)) {
            model.addAttribute("errorMsg", "无效的角色");
            return addOrEditShow(model, role);
        }

        /**
         * 保存或更新
         */
        List<Long> authorities = (authorityIds == null) ? null : Arrays
            .asList(authorityIds);

        if (!insertOrUpdateRole(role, authorities)) {
            model.addAttribute("errorMsg", "数据库操作失败!");
            return addOrEditShow(model, role);
        }

        return "redirect:index.html";
    }

    private boolean insertOrUpdateRole(Role role, List<Long> authorities) {

        String userInfo = "用户ID:" + getCurrentUser().getId();
        String typeInfo = role.getRoleId() == null ? " 创建新角色" : " 更新角色";
        String roleInfo = "角色ID:" + role.getRoleId() + " 角色名称" + role.getName() + " 所有权限ID：" + authorities;

        boolean bFlag = false;
        try {
            if (role.getRoleId() == null) {// 新增角色
                setDefaultProperty(role); // 设置默认属性
                bFlag = roleService.insertNewRoleWithAuthIds(role, authorities);

            } else {// 修改角色
                role.setUpdateTime(new Date());
                bFlag = roleService.updateRoleWithAuthIds(role, authorities);
            }

        } catch (TransactionException e) {
            logger.error(userInfo + typeInfo + "失败 " + roleInfo + " 事务回滚");
            return false;
        }

        if (bFlag) {
            logger.info(userInfo + typeInfo + "成功 " + roleInfo);
            return true;
        } else {
            logger.info(userInfo + typeInfo + "失败 " + roleInfo);
            return false;
        }

    }

    /**
     * @param pageResult
     * @return
     * @throws
     * @Title:index
     * @Description: 获取角色列表
     * @author: sunyiwei
     */
    @RequestMapping("index")
    public String index(ModelMap model, QueryObject queryObject) {
        if(queryObject != null){
            model.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "role/index.ftl";
    }
    
    /** 
     * @Title: search
     * 角色列表搜索
     */
    @RequestMapping("search")
    public void search(HttpServletResponse res, QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数
         */
        setQueryParameter("roleId", queryObject);
        setQueryParameter("name", queryObject);

        // 数据库查找符合查询条件的个数
        int count = roleService.
                queryPaginationRoleCount(queryObject);

        List<Role> list = roleService.
                queryPaginationRoleList(queryObject);

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
     * @param roleId
     * @return
     * @throws
     * @Title:delete
     * @Description: 删除指定ID的用户
     * @author: sunyiwei
     */
    @RequestMapping("delete")
    public String delete(ModelMap modelMap, Long roleId) {

        try {
            if (roleService.deleteRoleById(roleId)) {
                logger.info("用户ID:" + getCurrentUser().getId() + "删除角色Id:" + roleId + "成功");
            } else {
                logger.info("用户ID:" + getCurrentUser().getId() + "删除角色Id:" + roleId + "失败");
            }
        } catch (TransactionException e) {
            logger.error("用户ID:" + getCurrentUser().getId() + "删除角色Id:" + roleId + "失败，事务回滚");
        }

        return "redirect:index.html";
    }

    /**
     * @param request
     * @param resp
     * @throws IOException
     * @throws
     * @Title:check
     * @Description: 异步校验
     * @author: sunyiwei
     */
    @RequestMapping(value = "/check")
    public void check(Role role, HttpServletResponse resp) throws IOException {
        // 验证是否已存在相应的角色, 条件是角色名称唯一、角色编码唯一
        Boolean bFlag = checkUnique(role);
        resp.getWriter().write(bFlag.toString());
    }

    /**
     * @param model
     * @param role
     * @return
     * @throws
     * @Title:validateRole
     * @Description: 全量校验角色对象
     * @author: sunyiwei
     */
    private boolean validateRole(ModelMap model, Role role) {
        if (role == null) {
            model.put("errorMsg", "无效的角色对象!");
            return false;
        }

        try {
            role.selfCheck();
        } catch (SelfCheckException e) {
            model.put("errorMsg", e.getMessage());
            return false;
        }

        /**
         * 唯一性校验
         */
        if (!checkUnique(role)) {
            model.put("errorMsg", "角色名称或编码已存在!");
            return false;
        }

        return true;
    }

    /**
     * @param szRoleId
     * @param szCurrentName
     * @param szRoleCode
     * @return true代表校验通过，false代表校验不通过
     * @throws
     * @Title:checkUnique
     * @Description: 检验角色名字和编码的唯一性
     */
    private boolean checkUnique(Role currentRole) {
        if (currentRole == null) {
            return true;
        }

        Long currentRoleId = currentRole.getRoleId();
        String szCurrentName = currentRole.getName();
        String szRoleCode = currentRole.getCode();


        List<Role> roles = roleService.getAvailableRoles();
        roles.add(roleService.getRoleById(1L));
        for (Role role : roles) {
            // 编辑时的验证，略过当前编辑的角色
            if (currentRoleId != null && (role.getRoleId().longValue() == currentRoleId.longValue())) {
                continue;
            }

            // 角色名字验证、角色编码验证
            if (role.getName().equalsIgnoreCase(szCurrentName)
                || role.getCode().equalsIgnoreCase(szRoleCode)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param role
     * @throws
     * @Title:setRoleDefaultProperty
     * @Description: 赋予角色默认属性
     * @author: sunyiwei
     */
    private void setDefaultProperty(Role role) {
        /**
         * 设置默认属性
         */
        Administer administer = getCurrentUser();
        role.setCreator(administer == null ? 0L : administer.getId());
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setRoleStatus(Constants.ROLE_STATUS.ON.getValue());
        role.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        role.setCanBeDeleted(Constants.ROLE_CAN_BE_DELETED.YES.getValue()); // 默认为可删除
        return;
    }

    /**
     * @param model
     * @param roleId
     * @return
     * @throws
     * @Title:editRoleCreate
     * @Description: 返回相应的角色创建权限的编辑页面
     * @author: xujue
     */
    @RequestMapping("editRoleCreate")
    public String editRoleCreateShow(ModelMap model, Role role) {
        List<Role> roles = roleService.getAllRoles();

        Long roleId = role.getRoleId();
        if (roleId != null) {

            role = roleService.getRoleById(roleId);

            /**
             * 获取当前角色的ID
             */
            List<Long> roleIds = roleCreateService.selectRoleIdsCreateByRoleId(roleId);
            model.addAttribute("roleIds", roleIds);
        }

        model.addAttribute("role", role);
        model.addAttribute("roles", roles);
        return "role/editRoleCreate.ftl";
    }

    /**
     * @param model
     * @param roleId
     * @return
     * @throws
     * @Title:saveRolesCreate
     * @Description: 保存可创建角色的权限
     * @author: xujue
     */
    @RequestMapping(value = "saveRolesCreate", method = RequestMethod.POST)
    public String saveRolesCreate(ModelMap model, Role role, Long[] roleIds) {

        /**
         * 保存或更新
         */
        List<Long> roleIdsList = (roleIds == null) ? null : Arrays
            .asList(roleIds);

        if (!updateRolesCreate(role, roleIdsList)) {
            model.addAttribute("errorMsg", "数据库操作失败!");
            return editRoleCreateShow(model, role);
        }
        return "redirect:index.html";
    }

    private boolean updateRolesCreate(Role role, List<Long> roleIdsList) {

        String userInfo = "用户ID:" + getCurrentUser().getId();
        String roleInfo = "角色ID:" + role.getRoleId() + " 所有角色ID：" + roleIdsList;

        boolean bFlag = false;
        try {
            bFlag = roleCreateService.updateRoleWithRoleIdsCreate(role, roleIdsList);

        } catch (TransactionException e) {
            logger.error(userInfo + "更新角色创建权限失败 " + roleInfo + " 事务回滚");
            return false;
        }

        if (bFlag) {
            logger.info(userInfo + "更新角色创建权限成功 " + roleInfo);
            return true;
        } else {
            logger.info(userInfo + "更新角色创建权限失败 " + roleInfo);
            return false;
        }

    }

}
package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.service.DistrictService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.RoleCreateService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.QueryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ManagerController.java
 */
@Controller
@RequestMapping("manage/manager")
public class ManagerController extends BaseController {
    @Autowired
    RoleCreateService roleCreateService;
    @Autowired
    ManagerService managerService;
    @Autowired
    DistrictService districtService;
    @Autowired
    RoleService roleService;

    /**
     * 管理员首页
     *
     * @return
     * @date 2016年7月15日
     * @author wujiamin
     */
    @RequestMapping("index")
    public String index(ModelMap map, QueryObject queryObject) {
        if(queryObject != null){
            map.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        Manager currentManager = getCurrentUserManager();
        if (currentManager == null) {
            map.put("errorMsg", "该用户无职位");
            return "error.ftl";
        }
        map.put("parentId", getCurrentUserManager().getId());

        List<Role> roles = roleCreateService.getCreateRolesByRoleId(currentManager.getRoleId());
        map.put("roles", roles);

        return "manager/index.ftl";
    }

    /**
     * 已创建的管理员列表查找
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数
         */
        setQueryParameter("roleId", queryObject);
        setQueryParameter("name", queryObject);

        // 数据库查找符合查询条件的个数
        int count = managerService.selectByParentIdCount(getCurrentUserManager().getId(), queryObject);

        List<Manager> list = managerService.selectByParentIdForPage(getCurrentUserManager().getId(), queryObject);

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
     * 创建管理员
     *
     * @param map
     * @return
     * @date 2016年7月15日
     * @author wujiamin
     */
    @RequestMapping("createManager")
    public String createManager(ModelMap map) {
        Manager currentManager = getCurrentUserManager();
        List<Role> roles = roleCreateService.getCreateRolesByRoleId(currentManager.getRoleId());
        map.put("roles", roles);

        List<District> districts = districtService.selectChildByName(currentManager.getName());
        if (currentManager.getParentId().equals(currentManager.getId())) {
            districts = districtService.selectByParentId(0L);
        }

        map.put("districts", districts);

        return "manager/create_manager.ftl";
    }

    /**
     * 保存管理员
     *
     * @param manager
     * @param map
     * @return
     * @date 2016年7月15日
     * @author wujiamin
     */
    @RequestMapping("saveManager")
    public String saveManager(Manager manager, ModelMap map) {
        Manager currentManager = getCurrentUserManager();

        if (!managerService.createManager(manager, currentManager.getId())) {
            map.put("errorMsg", "创建职位失败！");
            return createManager(map);
        }

        return "redirect:index.html";
    }

    @RequestMapping(value = "hierarchy", method = RequestMethod.POST)
    public void getHierarchyAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String managerId = getRequest().getParameter("managerId");
        String parentId = getRequest().getParameter("parentId");

        List<Manager> managers = new ArrayList<Manager>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(managerId)) {
            managers.add(managerService.selectByPrimaryKey(Long.parseLong(managerId)));
        }

        if (org.apache.commons.lang.StringUtils.isNotBlank(parentId)) {
            managers = managerService.selectByParentId(Long.parseLong(parentId));
        }
        List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();
        for (Manager manager : managers) {
            Map<String, Object> manMap = new HashMap<String, Object>();
            manMap.put("id", manager.getId());
            manMap.put("text", manager.getName());
            manMap.put("state", "closed");
            itemsList.add(manMap);
        }
        String data = JSON.toJSONString(itemsList);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(data);
    }


    /**
     * 编辑
     *
     * @param map
     * @return
     * @date 2016年7月29日
     * @author wujiamin
     */
    @RequestMapping("editManager")
    public String editManager(ModelMap map, Long managerId) {
        Manager manager = null;
        if (managerId == null || (manager = managerService.selectByPrimaryKey(managerId)) == null) {
            map.put("errorMsg", "该职位不存在!");
            return "error.ftl";
        }
        //只能修改自己直属下级的职位
        if(!manager.getParentId().equals(getCurrentUserManager().getId())){
            map.put("errorMsg", "只能编辑直属下级职位");
            return "error.ftl";
        }
        
        manager.setRoleName(roleService.getRoleById(manager.getRoleId()).getName());

        map.put("manager", manager);
        List<District> nowDistricts = districtService.selectByName(manager.getName());

        Manager currentUserManager = getCurrentUserManager();
        List<District> districts = districtService.selectChildByName(currentUserManager.getName());
        //当前用户是平台管理员，并且当前用户的管理员身份(职位名称)不在树里
        if (currentUserManager.getParentId().equals(currentUserManager.getId())) {
            districts = districtService.selectByParentId(0L);
        }

        //根据现有名称，判断之前的职位名称是选择的还是自定义的
        if (nowDistricts != null && nowDistricts.size() > 0 && districts != null && districts.size() > 0) {
            map.put("select", "true");
        } else {
            map.put("select", "false");
        }

        map.put("districts", districts);

        return "manager/edit_manager.ftl";
    }

    /** 
     * @Title: saveEditManager 
    */
    @RequestMapping("saveEditManager")
    public String saveEditManager(Manager manager, ModelMap map) {
        if (!managerService.updateByPrimaryKeySelective(manager)) {
            map.put("errorMsg", "编辑职位失败！");
            return editManager(map, manager.getId());
        }

        return "redirect:index.html";
    }

}

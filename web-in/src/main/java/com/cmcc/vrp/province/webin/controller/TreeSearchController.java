package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.ManagerService;

/**
 * 树形接口查询controller
 * TreeSearchController.java
 * @author wujiamin
 * @date 2016年11月14日
 */
@Controller
@RequestMapping("/manage/tree")
public class TreeSearchController extends BaseController {
    @Autowired
    ManagerService managerService;

    @RequestMapping(value = "getRoot")
    public void getManagerRoot(HttpServletResponse resp) {
        Manager manager = getCurrentUserManager();
        List list = new ArrayList();

        Map map = new HashMap();
        map.put("id", manager.getId());
        map.put("text", manager.getName());
        map.put("children", new ArrayList());
        list.add(map);

        try {
            resp.getWriter().write(JSONObject.toJSONString(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "getChildNode")
    public void getChildNode(HttpServletResponse resp, Long parentId, Long roleId) {
        Manager root = managerService.selectByPrimaryKey(parentId);
        //当前角色与最小层次角色相同，不向下搜索
        if (roleId != null && root.getRoleId().equals(roleId)) {
            return;
        }

        //只到客户经理层级
        String customManagerRoleId = getCustomManager();

        if (root.getRoleId().toString().equals(customManagerRoleId)) {
            return;
        }

        List<Manager> managerRoots = managerService.selectByParentId(parentId);
        List list = new ArrayList();
        if (managerRoots != null && managerRoots.size() > 0) {
            for (Manager manager : managerRoots) {
                //山东过滤掉客户经理这角色
                if (!"sd".equalsIgnoreCase(getProvinceFlag())
                        || !manager.getRoleId().toString().equals(customManagerRoleId)) {
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
}

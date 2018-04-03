package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.module.EnterpriseBenefitModule;
import com.cmcc.vrp.province.module.EnterpriseStatisticModule;
import com.cmcc.vrp.province.module.UserStatisticModule;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.QueryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计controller(用户统计、企业统计)
 *
 * @author JamieWu
 */
@Controller
@RequestMapping("/manage/statistic")
public class StatisticController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdministerService administerServcie;

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private ManagerService managerService;


    @RequestMapping("/userIndex")
    public String getUserStatistics(ModelMap model, QueryObject queryObject) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            return "redirect:/manage/user/login.html";
        }
        // 获取当前用户的对应的管理员
        Manager manager = managerService.getManagerByAdminId(admin.getId());
        Long managerId;
        if (manager == null || (managerId = manager.getId()) == null) {
            return "redirect:/manage/user/login.html";
        }

        model.addAttribute("managerId", managerId);

        Long currentRoleId = manager.getRoleId();

        //传递人员类型参数
        List<Role> roles = roleService.getAvailableRoles();
        List<Role> roleTypes = new ArrayList<Role>();
        if (currentRoleId.toString().equals(getProvinceManager())) {
            for (Role role : roles) {
                if (role.getRoleId().toString().equals(getProvinceManager())
                    || role.getRoleId().toString().equals(getCityManager())
                    || role.getRoleId().toString().equals(getCustomManager())
                    || role.getRoleId().toString().equals(getENTERPRISE_CONTACTOR())) {
                    roleTypes.add(role);
                }
            }
        }

        if (currentRoleId.toString().equals(getCityManager())) {
            for (Role role : roles) {
                if (role.getRoleId().toString().equals(getCityManager())
                    || role.getRoleId().toString().equals(getCustomManager())
                    || role.getRoleId().toString().equals(getENTERPRISE_CONTACTOR())) {
                    roleTypes.add(role);
                }
            }
        }
        
        if (currentRoleId.toString().equals(getCustomManager())) {
            for (Role role : roles) {
                if (role.getRoleId().toString().equals(getCustomManager())
                    || role.getRoleId().toString().equals(getENTERPRISE_CONTACTOR())) {
                    roleTypes.add(role);
                }
            }
        }

        model.addAttribute("roleTypes", roleTypes);

        return "statistic/userStatistic.ftl";

    }

    /**
     * 查找
     */
    @RequestMapping(value = "/userSearch")
    public void userSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        String managerId = getRequest().getParameter("managerId");
        String roleId = getRequest().getParameter("roleId");

        if (org.apache.commons.lang.StringUtils.isBlank(managerId)) {
            Manager manager = managerService.getManagerByAdminId(getCurrentAdminID());
            managerId = String.valueOf(manager.getId());
        }
        
        int count = 0;
        List<UserStatisticModule> list = new ArrayList();
        //判断当前用户是否有传入managerId的查看权限
        if(managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())){
            //roleId为0，表示的是搜索的是全部角色，统计根据角色分类，数据是各角色占该地区的所有人数的占比
            if ("0".equals(roleId)) {
                // 数据库查找符合查询条件的个数
                count = administerServcie.statisticRoleCountByManangerId(Long.parseLong(managerId));
                list = administerServcie.statisticRoleByManagerId(Long.parseLong(managerId), queryObject);
            }else {//roleId不为0，表示的是搜索的是某个角色，统计根据地区分类，数据是各角色占该地区的所有人数的占比
                // 数据库查找符合查询条件的个数
                count = administerServcie.statisticOneRoleCountByManangerId(Long.parseLong(roleId), Long.parseLong(managerId));
                list = administerServcie.statisticOneRoleByManagerId(Long.parseLong(roleId), Long.parseLong(managerId), queryObject);
            }
        }

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
     * 根据条件获取饼图数据
     */
    @RequestMapping(value = "/getUserPieData")
    public void getUserPieData(HttpServletResponse res) {

        String managerId = getRequest().getParameter("managerId");
        String roleId = getRequest().getParameter("roleId");

        if (org.apache.commons.lang.StringUtils.isBlank(managerId)) {
            Manager manager = managerService.getManagerByAdminId(getCurrentAdminID());
            managerId = String.valueOf(manager.getId());
        }

        List<UserStatisticModule> list = null;
        //判断当前用户是否有传入managerId的查看权限
        if(managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())){
            //roleId为0，表示的是搜索的是全部角色，统计根据角色分类，数据是各角色占该地区的所有人数的占比
            if ("0".equals(roleId)) {
                list = administerServcie.statisticRoleByManagerId(Long.parseLong(managerId), null);
            }
            //roleId不为0，表示的是搜索的是某个角色，统计根据地区分类，数据是各角色占该地区的所有人数的占比
            else {
                list = administerServcie.statisticOneRoleByManagerId(Long.parseLong(roleId), Long.parseLong(managerId), null);
            }
    
            if (list != null) {
                for (UserStatisticModule s : list) {
                    if (StringUtils.isEmpty(s.getRoleName())) {
                        s.setRoleName("普通用户");
                    }
                }
            }
        }

        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            res.getWriter().write(JSON.toJSONString(parseUserPieData(list)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据库搜索的数据组装成能适应饼图的数据
     */
    List<Object> parseUserPieData(List<UserStatisticModule> list) {
        List<Object> data = new ArrayList<Object>();
        if (list != null) {
            int i = 0;
            int otherValue = 0;
            for (UserStatisticModule m : list) {
                i++;
                if (i >= 8) {
                    otherValue = otherValue + m.getNumber();
                } else {
                    Map pieData = new HashMap();
                    pieData.put("value", m.getNumber());
                    pieData.put("name", m.getManagerName() + " " + m.getRoleName());
                    data.add(pieData);
                }
            }
            if (list.size() > 8) {
                Map pieData = new HashMap();
                pieData.put("value", otherValue);
                pieData.put("name", "其他");
                data.add(pieData);
            }
        }
        return data;
    }

    /**
     * 获取企业统计首页
     */
    @RequestMapping("/enterpriseIndex")
    public String getEnterpriseStatistics(ModelMap model, QueryObject queryObject) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            return "redirect:/manage/user/login.html";
        }

        Manager manager = managerService.getManagerByAdminId(admin.getId());
        Long managerId;
        if (manager == null
            || (managerId = manager.getId()) == null) {
            return "redirect:/manage/user/login.html";
        }

        model.addAttribute("managerId", managerId);

        return "statistic/enterpriseStatistic.ftl";

    }

    /**
     * 查找企业
     */
    @RequestMapping(value = "/enterpriseSearch")
    public void enterpriseListSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        String managerId = getRequest().getParameter("managerId");
        Manager manager;
        if (org.apache.commons.lang.StringUtils.isBlank(managerId)) {
            manager = managerService.getManagerByAdminId(getCurrentAdminID());
            managerId = String.valueOf(manager.getId());
        } else {
            manager = managerService.selectByPrimaryKey(Long.parseLong(managerId));
        }

        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);

        String roleId = String.valueOf(manager.getRoleId());

        int count = 0;
        List<EnterpriseStatisticModule> list = new ArrayList<EnterpriseStatisticModule>();
        //判断当前用户是否有传入managerId的查看权限
        if(managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())){
            // 当前用户是超级管理员、省级管理员、市级管理员、客户经理时，通过他们的儿子节点统计企业
            List<Long> sonIds = managerService.selectSonIdsByParentId(Long.parseLong(managerId));
            if (sonIds != null && sonIds.size() > 0) {
                queryObject.getQueryCriterias().put("sonIds", sonIds);
                if (roleId.toString().equals(getSuperAdminRoleId())) {
                    queryObject.getQueryCriterias().put("level", 1);
                } else if (roleId.toString().equals(getProvinceManager())) {
                    queryObject.getQueryCriterias().put("level", 2);
                } else if (roleId.toString().equals(getCityManager())) {
                    queryObject.getQueryCriterias().put("level", 3);
                } else if (roleId.toString().equals(getCustomManager())) {
                    queryObject.getQueryCriterias().put("level", 4);
                }
                count = enterprisesService.statistictEnterpriseCountByManagerTree(queryObject);
                list = enterprisesService.statistictEnterpriseByManagerTree(queryObject);
            }
        }

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
     * 根据条件获取企业统计的饼图数据
     */
    @RequestMapping(value = "/getEnterprisePieData")
    public void getEnterprisePieData(HttpServletResponse res, QueryObject queryObject) {
        String managerId = getRequest().getParameter("managerId");
        Manager manager;
        if (org.apache.commons.lang.StringUtils.isBlank(managerId)) {
            manager = managerService.getManagerByAdminId(getCurrentAdminID());
            managerId = String.valueOf(manager.getId());
        } else {
            manager = managerService.selectByPrimaryKey(Long.parseLong(managerId));
        }

        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);

        queryObject.setPageNum(0);
        queryObject.setPageSize(0);

        String roleId = String.valueOf(manager.getRoleId());

        List<EnterpriseStatisticModule> list = new ArrayList<EnterpriseStatisticModule>();

        //判断当前用户是否有传入managerId的查看权限
        if(managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())){
            // 当前用户是超级管理员、省级管理员、市级管理员、客户经理时，通过他们的儿子节点统计企业
            List<Long> sonIds = managerService.selectSonIdsByParentId(Long.parseLong(managerId));
            if (sonIds != null && sonIds.size() > 0) {
                queryObject.getQueryCriterias().put("sonIds", sonIds);
                if (roleId.equals(getSuperAdminRoleId())) {
                    queryObject.getQueryCriterias().put("level", 1);
                } else if (roleId.equals(getProvinceManager())) {
                    queryObject.getQueryCriterias().put("level", 2);
                } else if (roleId.equals(getCityManager())) {
                    queryObject.getQueryCriterias().put("level", 3);
                } else if (roleId.equals(getCustomManager())) {
                    queryObject.getQueryCriterias().put("level", 4);
                }
    
                list = enterprisesService.statistictEnterpriseByManagerTree(queryObject);
            }
        }

        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            JSONObject json = new JSONObject();
            List<String> districts = new ArrayList<String>();
            List<Object> datas = new ArrayList<Object>();
            parseEnterprisePieData(list, datas, districts);

            json.put("districts", districts);
            json.put("datas", datas);

            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据库搜索的数据组装成能适应饼图的数据
     */
    void parseEnterprisePieData(List<EnterpriseStatisticModule> list,
                                List<Object> datas, List<String> districts) {
        if (list != null) {
            int i = 0;
            int otherValue = 0;
            for (EnterpriseStatisticModule m : list) {
                i++;
                if (i >= 8) {
                    otherValue = otherValue + m.getNumber();
                } else {
                    Map pieData = new HashMap();
                    pieData.put("value", m.getNumber());
                    pieData.put("name", m.getDistrictName());
                    datas.add(pieData);
                    districts.add(m.getDistrictName());
                }
            }
            if (list.size() > 8) {
                Map pieData = new HashMap();
                pieData.put("value", otherValue);
                pieData.put("name", "其他");
                datas.add(pieData);
                districts.add("其他");
            }
        }
    }

    /**
     * 根据条件获取企业统计的折线图数据
     */
    @RequestMapping(value = "/getEnterpriseLineData")
    public void getEnterpriseLineData(HttpServletResponse res, QueryObject queryObject) {
        String managerId = getRequest().getParameter("managerId");
        Manager manager;
        if (org.apache.commons.lang.StringUtils.isBlank(managerId)) {
            manager = managerService.getManagerByAdminId(getCurrentAdminID());
            managerId = String.valueOf(manager.getId());
        }

        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);

        queryObject.setPageNum(0);
        queryObject.setPageSize(0);
        
        List<EnterpriseStatisticModule> list = new ArrayList();
        //判断当前用户是否有传入managerId的查看权限
        if(managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())){
            list = enterprisesService.statistictEnterpriseByCreateDay(Long.parseLong(managerId), queryObject);
        }

        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            JSONObject json = new JSONObject();
            List<String> categories = new ArrayList<String>();
            List<Integer> series = new ArrayList<Integer>();
            parseEnterpriseLineData(list, categories, series);

            json.put("categories", categories);
            json.put("series", series);

            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据库搜索的数据组装成能适应企业统计折线图的数据
     */
    void parseEnterpriseLineData(List<EnterpriseStatisticModule> list,
                                 List<String> categories, List<Integer> series) {
        if (list != null) {
            for (EnterpriseStatisticModule m : list) {
                categories.add(m.getDate());
                series.add(m.getNumber());
            }
        }
    }

    /**
     * 根据条件获取企业效益数据
     */
    @RequestMapping(value = "/getEnterpriseCircleData")
    public void getEnterpriseCircleData(HttpServletResponse res, QueryObject queryObject) {
        String managerId = getRequest().getParameter("managerId");
        Manager manager;
        if (org.apache.commons.lang.StringUtils.isBlank(managerId)) {
            manager = managerService.getManagerByAdminId(getCurrentAdminID());
            managerId = String.valueOf(manager.getId());
        }

        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);

        queryObject.setPageNum(0);
        queryObject.setPageSize(0);

        //共有多少家企业，从充值记录中统计会漏掉还没有充值成功过的企业数
        Map map = new HashMap();
        map.put("managerId", managerId);
        map.put("beginTime", getRequest().getParameter("startTime"));
        map.put("endTime", getRequest().getParameter("endTime"));
        if (!StringUtils.isEmpty(getRequest().getParameter("endTime"))) {
            map.put("endTime", getRequest().getParameter("endTime") + " 23:59:59");
        }

        List<EnterpriseBenefitModule> list = new ArrayList();
        Integer count = 0;
        
        //判断当前用户是否有传入managerId的查看权限
        if(managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())){
            List<Enterprise> enterLists = enterprisesService.selectEnterpriseByMap(map);            
            if (enterLists != null) {
                count = enterLists.size();
            }
    
            queryObject.getQueryCriterias().put("managerId", managerId);
    
            list = enterprisesService.statistictBenefitForEnterprise(queryObject);
        }

        Integer totalNum = 0;
        Integer vhNum = 0;
        Integer hNum = 0;
        Integer mNum = 0;
        Integer oNum = 0;
        Integer lNum = 0;

        if (list != null && list.size() > 0) {
            for (EnterpriseBenefitModule module : list) {
                double benefit = module.getBenefit();
                if (benefit >= 0 && benefit < 100000) {
                    lNum = lNum + 1;
                }
                if (benefit >= 100000 && benefit < 300000) {
                    oNum = oNum + 1;
                }
                if (benefit >= 300000 && benefit < 500000) {
                    mNum = mNum + 1;
                }
                if (benefit >= 500000 && benefit < 1000000) {
                    hNum = hNum + 1;
                }
                if (benefit >= 1000000) {
                    vhNum = vhNum + 1;
                }
            }
        }


        totalNum = count;

        lNum = count - oNum - mNum - hNum - vhNum;

        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            JSONObject json = new JSONObject();

            Map vh = new HashMap();
            Map h = new HashMap();
            Map m = new HashMap();
            Map l = new HashMap();
            Map o = new HashMap();

            vh.put("number", vhNum);
            vh.put("from", 0);

            h.put("number", hNum);
            h.put("from", 0);

            m.put("number", mNum);
            m.put("from", 0);

            l.put("number", lNum);
            l.put("from", 0);

            o.put("number", oNum);
            o.put("from", 0);

            if (totalNum != 0) {
                vh.put("to", 1.0 * vhNum / totalNum * 100.00);
                vh.put("percent", 1.0 * vhNum / totalNum * 100.00);

                h.put("percent", 1.0 * hNum / totalNum * 100.00);
                h.put("to", 1.0 * hNum / totalNum * 100.00);

                m.put("percent", 1.0 * mNum / totalNum * 100.00);
                m.put("to", 1.0 * mNum / totalNum * 100.00);

                l.put("to", 1.0 * lNum / totalNum * 100.00);
                l.put("percent", 1.0 * lNum / totalNum * 100.00);

                o.put("percent", 1.0 * oNum / totalNum * 100.00);
                o.put("to", 1.0 * oNum / totalNum * 100.00);
            } else {
                vh.put("to", 0);
                vh.put("percent", 0);

                h.put("percent", 0);
                h.put("to", 0);

                m.put("percent", 0);
                m.put("to", 0);

                l.put("to", 0);
                l.put("percent", 0);

                o.put("percent", 0);
                o.put("to", 0);
            }


            json.put("vh", vh);
            json.put("h", h);
            json.put("m", m);
            json.put("o", o);
            json.put("l", l);

            res.getWriter().write(json.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

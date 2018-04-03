package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.dao.AdminDistrictMapper;
import com.cmcc.vrp.province.model.AdminDistrict;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.QueryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StatisticActivityController.java
 */
@Controller
@RequestMapping("/manage/statisticActivity")
public class StatisticActivityController extends BaseController {

    @Autowired
    AdminDistrictMapper adminDistrictMapper;

    @Autowired
    EnterprisesService enterprisesService;

    /**
     * 活动统计首页
     *
     * @param model
     * @param queryObject
     * @return
     */
    @RequestMapping("activityStatisticIndex")
    public String activityStatisticIndex(ModelMap model, QueryObject queryObject) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            return "redirect:/manage/user/login.html";
        }
        Long currentRoleId = admin.getRoleId();

        //传递当前用户地区
        //有地区关联的管理员在分配其他管理员角色时只能获取其同级及下级的地区
        List<AdminDistrict> ad1 = adminDistrictMapper.selectAdminDistrictByAdminId(getCurrentUser().getId());
        if (ad1 == null || ad1.size() != 1) {
            model.addAttribute("districtId", "1");
            model.addAttribute("districtIdSelect", "1");
        } else {
            model.addAttribute("districtId", ad1.get(0).getDistrictId().toString());
            model.addAttribute("districtIdSelect", ad1.get(0).getDistrictId().toString());
        }

        if (currentRoleId.toString().equals(getENTERPRISE_CONTACTOR())) {
            model.addAttribute("adminId", admin.getId());

        }

        return "statistic/activityStatistic.ftl";

    }

    /**
     * 根据地区ID查询可选的企业
     *
     * @param resp
     * @param districtId
     * @return
     * @throws IOException
     */
    @RequestMapping("getEnterprise")
    public void getEnterprise(HttpServletRequest request, HttpServletResponse resp, String districtId) throws IOException {

        Administer admin = getCurrentUser();
        if (admin == null) {
            return;
        }
        Long currentRoleId = admin.getRoleId();

        Map map = new HashMap();
        map.put("districtId", districtId);
        if (currentRoleId.toString().equals(getENTERPRISE_CONTACTOR())) {
            map.put("adminId", admin.getId());

        }
        List<Enterprise> enterprises = enterprisesService.selectEnterpriseByMap(map);

        Map<String, Object> map1 = new HashMap<String, Object>();//ajax传输的值
        map1.put("enterprises", enterprises);

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(JSON.toJSONString(map1));
    }


    /*@RequestMapping("getActivityName")
    public void getActivityName(HttpServletRequest request, HttpServletResponse resp, Long enterId, Integer activityType) throws IOException {

        *//*Administer admin = getCurrentUser();
        if (admin == null) {
            return;
        }
//        Long currentRoleId = admin.getRoleId();

        List<Object> lists = new ArrayList<Object>();
        if (activityType == 0) {
            List<EntRedpacket> entRedpackets = entRedpacketService.getByEnterId(enterId);
            Map<String, Object> map1 = new HashMap<String, Object>();//ajax传输的值
            map1.put("lists", entRedpackets);

            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(JSON.toJSONString(map1));
        }
        if (activityType == 1) {
            Map<String, Object> map1 = new HashMap<String, Object>();//ajax传输的值
            map1.put("lists", lists);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(JSON.toJSONString(map1));
        }
        if (activityType == -1) {
            Map<String, Object> map1 = new HashMap<String, Object>();//ajax传输的值
            map1.put("lists", lists);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(JSON.toJSONString(map1));
        }*//*

    }
*/
    /**
     * 活动
     *
     * @param model
     * @param queryObject
     * @return
     */
    @RequestMapping("activityListIndex")
    public String activityListIndex(ModelMap model, QueryObject queryObject) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            return "redirect:/manage/user/login.html";
        }
        Long currentRoleId = admin.getRoleId();

        //传递当前用户地区
        //有地区关联的管理员在分配其他管理员角色时只能获取其同级及下级的地区
        List<AdminDistrict> ad1 = adminDistrictMapper.selectAdminDistrictByAdminId(getCurrentUser().getId());
        if (ad1 == null || ad1.size() != 1) {
            model.addAttribute("districtId", "1");
            model.addAttribute("districtIdSelect", "1");
        } else {
            model.addAttribute("districtId", ad1.get(0).getDistrictId().toString());
            model.addAttribute("districtIdSelect", ad1.get(0).getDistrictId().toString());
        }

        if (currentRoleId.toString().equals(getENTERPRISE_CONTACTOR())) {
            model.addAttribute("adminId", admin.getId());

        }

        return "statistic/activityStatistic.ftl";

    }

}

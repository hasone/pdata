package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by leelyn on 2016/6/1.
 */
@Controller
@RequestMapping(value = "district")
public class DistrictController extends BaseController {

    @Autowired
    private DistrictService districtService;

    @Autowired
    private AdministerService administerService;

    /** 
     * @Title: queryDistrict 
    */
    @RequestMapping(value = "queryDistrict")
    public void queryDistrict(Long parentId, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        if (parentId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("[]");
        }
        List<District> districts;
        if ((districts = districtService.queryByParentId(parentId)) != null) {
            response.getWriter().write(JSONArray.toJSONString(districts));
        }
    }

    /** 
     * @Title: queryCustomerPhone 
    */
    @RequestMapping(value = "searchCMPhome")
    public void queryCustomerPhone(Long districtId, HttpServletResponse response) throws IOException {
        if (districtId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("[]");
        }
        List<Administer> cms = null;
        try {
            cms = administerService.queryCMByDistrictId(districtId, Long.parseLong(getCustomManager()));
        } catch (Exception e) {
            System.out.println(e);
        }
        if (cms != null
            && !cms.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", cms.get(0).getMobilePhone());
            response.getWriter().write(jsonObject.toString());
        }
    }
}

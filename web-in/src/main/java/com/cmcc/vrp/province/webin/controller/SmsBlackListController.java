package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MobileBlackList;
import com.cmcc.vrp.province.service.MobileBlackListService;
import com.cmcc.vrp.util.QueryObject;

/**
 * @author lgk8023
 *
 */
@Controller
@RequestMapping("/sms/blackList")
public class SmsBlackListController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(ShProductController.class);
    @Autowired
    private MobileBlackListService mobileBlackListService;
    /**
     * @param modelMap
     * @param queryObject
     * @return
     */
    @RequestMapping("index")
    public String showEnterpriseList(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        Manager manager = getCurrentUserManager();
        modelMap.addAttribute("managerId", manager.getId());
        return "smsBlackList/index.ftl";
    }
    
    /**
     * @param queryObject
     * @param request
     * @param res
     */
    @RequestMapping("search")
    public void search(HttpServletRequest request, HttpServletResponse res) {
        String mobile = request.getParameter("tel");
        
        List<MobileBlackList> mobileBlackLists = mobileBlackListService.getByMobile(mobile);
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("data", mobileBlackLists);
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
    /**
     * @param response
     * @param request
     * @param modelMap
     */
    @RequestMapping("add")
    public void add(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
        String mobile = request.getParameter("mobile");
        String type = request.getParameter("type");
        if (mobileBlackListService.getByMobileAndType(mobile, type) != null) {
            try {
                modelMap.addAttribute("isDup", "重复添加");
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        MobileBlackList mobileBlackList = new MobileBlackList();
        mobileBlackList.setMobile(mobile);
        mobileBlackList.setType(type);
        mobileBlackList.setDeleteFlag(0);
        try {
            if (mobileBlackListService.insert(mobileBlackList)) {
                modelMap.addAttribute("success", "添加成功");
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            } else {
                modelMap.addAttribute("failure", "添加失败");
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            } 
        } catch (IOException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;  
    }
    /**
     * @param response
     * @param request
     * @param modelMap
     */
    @RequestMapping("delete")
    public void delete(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
        Long id = NumberUtils.toLong(request.getParameter("id"));

        try {
            if (mobileBlackListService.deleteById(id)) {
                modelMap.addAttribute("result", true);
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            } else {
                modelMap.addAttribute("result", false);
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            } 
        } catch (IOException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;  
    }
}

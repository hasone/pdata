package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.util.QueryObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by leelyn on 2016/7/2.
 */
@Controller
@RequestMapping(value = "manage/portal")
public class PortalController extends BaseController {

    /** 
     * @Title: index 
    */
    @RequestMapping(value = "index")
    public String index(HttpServletRequest request, HttpServletResponse response, QueryObject queryObject, ModelMap modelMap) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "portal.ftl";
    }

    /** 
     * @Title: enterpriselogin 
    */
    @RequestMapping(value = "enterpriselogin")
    public String enterpriselogin() {
        return "redirect:http://sc.4ggogo.com";
    }

    /** 
     * @Title: personallogin 
    */
    @RequestMapping(value = "personallogin")
    public String personallogin() {
        return "personal/login.ftl";
    }

    /** 
     * @Title: loginIndex 
    */
    @RequestMapping(value = "loginIndex")
    public String loginIndex() {
        Administer admin = getCurrentUser();
        if (admin == null) {
            return getLoginAddress();
        }

        return "individual/index.ftl";
    }
}

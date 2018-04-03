package com.cmcc.vrp.individual.controller;

import com.cmcc.vrp.province.webin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
@Controller
@RequestMapping("individual/userInfo")
public class IndividualUserInfoController extends BaseController {

    /** 
     * @Title: index 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    @RequestMapping("index")
    public String index() {
        return "individual/manage/user_manage.ftl";
    }

}

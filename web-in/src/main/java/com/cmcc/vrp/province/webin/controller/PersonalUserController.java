package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;

/**
 * Created by leelyn on 2016/6/28.
 */
@Controller
@RequestMapping(value = "mamage/personal")
public class PersonalUserController {
    private static Logger logger = LoggerFactory.getLogger(PersonalUserController.class);
    private final String configKey = "RANDOMPASS_CHECK";//数据库中globalconfig的随机验证码验证key值
    private final String checkPassKey = "OK";//globalconfig需要检验随机密码的value值
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    private PhoneRegionService phoneRegionService;

    /** 
     * @Title: login 
    */
    @RequestMapping(value = "login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "personal/accountEnquiry.ftl";
    }

    /** 
     * @Title: checkSCMobile 
    */
    @RequestMapping("checkSCMobile")
    public void checkSCMobile(ModelMap map, String mobile, HttpServletResponse resp) {
        Map returnMap = new HashMap();
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);

        if (phoneRegion != null && phoneRegion.getProvince().equals("四川") && phoneRegion.getSupplier().equals("M")) {
            returnMap.put("result", "true");
        } else {
            returnMap.put("result", "false");
        }
        String data = JSON.toJSONString(returnMap);
        try {
            resp.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * @Title: checkImgCode 
    */
    @RequestMapping("checkImgCode")
    public void checkImgCode(ModelMap map, String verifyCode, HttpServletRequest request, HttpServletResponse resp) {
        logger.info("用户输入的图形验证码为：{}", verifyCode);
        Map returnMap = new HashMap();
        String configValue = globalConfigService.get(configKey);
        if (configValue != null && !checkPassKey.equals(configValue)) {
            returnMap.put("result", "true");
        } else {
            String sessionCode = (String) request.getSession(true).getAttribute("virifyCode");//得到session中的验证码
            logger.info("session中的图形验证码为：{}", sessionCode);
            if (verifyCode.equalsIgnoreCase(sessionCode)) {
                returnMap.put("result", "true");
            } else {
                returnMap.put("result", "false");
            }
        }

        String data = JSON.toJSONString(returnMap);
        try {
            resp.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

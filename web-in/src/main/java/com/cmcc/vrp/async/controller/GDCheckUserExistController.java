package com.cmcc.vrp.async.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 广东检测用户是否存在
 * 暂时使用get方法，只有广东环境可以使用，其它环境直接返回400
 */
@Controller
@RequestMapping
public class GDCheckUserExistController {
    private static final Logger logger = LoggerFactory.getLogger(GDCheckUserExistController.class);
    
    @Autowired
    AdministerService administerService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    /**
     * 检测用户
     */
    @RequestMapping(value = "gdCheckUser", method = RequestMethod.GET)
    @ResponseBody
    public void gdCheckUser(HttpServletRequest request, HttpServletResponse response,String mobilePhone) {
        if(!isGDEnvironment()){
            logger.error("非广东环境，该接口非法使用,mobilePhone={}",mobilePhone);
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            return;
        }
        
        String appKey = "";
        if (StringUtils.isBlank(mobilePhone)
                || StringUtils.isBlank(appKey = (String)request.getAttribute(Constants.APP_KEY_ATTR))) {
            logger.error("校验失败，mobilePhone={},appKey={}",mobilePhone,appKey);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        if(administerService.selectByMobilePhone(mobilePhone) ==null){
            logger.error("用户在平台不存在， mobilePhone={}",mobilePhone);
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        }else{
            response.setStatus(HttpStatus.SC_OK);
        }
    }
    
    /**
     * 检测是广东环境
     */
    public boolean isGDEnvironment(){
        return "gd_mdrc".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}

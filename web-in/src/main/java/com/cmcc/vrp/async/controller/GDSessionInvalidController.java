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
import com.cmcc.vrp.province.security.MySessionListener;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 广东invalidsession
 *
 */
@Controller
@RequestMapping
public class GDSessionInvalidController {
    private static final Logger logger = LoggerFactory.getLogger(GDSessionInvalidController.class);
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    /**
     * 检测用户
     */
    @RequestMapping(value = "gdInvalidUser", method = RequestMethod.GET)
    @ResponseBody
    public void gdInvalidUser(HttpServletRequest request, HttpServletResponse response,String mobilePhone) {
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
        
        
        MySessionListener.removeAndInvalidByMobile(mobilePhone);
        response.setStatus(HttpStatus.SC_OK);
    }
    
    /**
     * 检测是广东环境
     */
    public boolean isGDEnvironment(){
        return "gd_mdrc".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}

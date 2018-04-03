package com.cmcc.vrp.weixin.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.WetchatService;

@Controller
@RequestMapping("/manage/wxtest")
public class WxTestController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(WxTestController.class);
    
    @Autowired
    WetchatService wetchatService;

    /*@RequestMapping("test")
    public String test(HttpServletRequest request){
        //test
        String mobile = getMobile();
        if(!StringUtils.isEmpty(mobile)){
            logger.info("mobile = {} in session.", mobile);
            return "crowd-funding/charge.ftl";
        }else{            
            if(StringUtils.isEmpty(request.getParameter("code"))){
                //换取code， 需要封装方法            
                String getCodeUrl = wetchatService.getCode(request.getRequestURI());//TODO：要替换成平台的域名
                logger.info("需要换取code：getCodeUrl:{}", getCodeUrl);
                return getCodeUrl;
            }
            else{           
                String result = wetchatService.getUserType(request, "crowd-funding/charge.ftl");
                return result;          
            }
        }

    }*/
    
    @RequestMapping("test")
    public String test(HttpServletRequest request){
        String wxAspect = (String) request.getAttribute("wxAspect");
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            return "crowd-funding/charge.ftl";//TODO:替换业务页面
        }else{
            return wxAspect;
        }
        
    }
    
}

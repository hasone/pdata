package com.cmcc.vrp.individual.controller;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.beans.WxUserInfo;

@Controller
@RequestMapping("/api/phoneQuery")
public class ActivityCheckPhoneQueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityCheckPhoneQueryController.class);
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    WetchatService wetchatService;
    
    /**
     * 
     * @Title: sichuanFlowRedpacket 
     * @Description: TODO
     * @param request
     * @param response
     * @throws IOException
     * @return: void
     */
    @RequestMapping(value = "sichuan", method = RequestMethod.POST)
    public void sichuanFlowRedpacket(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String result = "false";
        String mobile = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        if(phoneRegion!=null && "四川".equals(phoneRegion.getProvince()) && IspType.CMCC.getValue().equals(phoneRegion.getSupplier())){
            result = "true";
        }
        response.getWriter().write(result);
    }
    
    /**
     * 
     * @Title: gdcrowdfundingLottery 
     * @Description: TODO
     * @param request
     * @param response
     * @throws IOException
     * @return: void
     */
    @RequestMapping(value = "gdcrowdfunding", method = RequestMethod.POST)
    public void gdcrowdfundingLottery(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String result = "true";
        String mobile = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        WxUserInfo wxUserInfo = wetchatService.getWxUserInfo(mobile);
        if(wxUserInfo == null){
            LOGGER.info("在微信公众号侧查询不到用户mobile={}的详细信息", mobile);
            result = "false";
        }else{
            PhoneRegion phoneRegion = phoneRegionService.query(mobile);
            if(phoneRegion==null || !"广东".equals(phoneRegion.getProvince()) || !IspType.CMCC.getValue().equals(phoneRegion.getSupplier())){
                result = "false";
            }
        }
        
        response.getWriter().write(result);
    }

}

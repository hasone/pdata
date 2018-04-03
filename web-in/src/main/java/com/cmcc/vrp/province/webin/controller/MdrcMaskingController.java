package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Masking;
import com.cmcc.vrp.province.service.MaskingService;

/**
 * @ClassName: MdrcMaskingController
 * @Description: 营销卡蒙版
 * @author: qinqinyan
 * @date:2017/08/14
 */
@Controller
@RequestMapping("/manage/mdrc/masking")
public class MdrcMaskingController extends BaseController {
    
    @Autowired
    MaskingService maskingService;
    
    /**
     * @param modelMap
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/maskingAjax")
    public void maskingAjax(HttpServletRequest request, HttpServletResponse response,
            Integer flag) throws IOException {
        
        Administer administer = getCurrentUser();
        Masking masking = new Masking();
        masking.setAdminId(administer.getId());
        masking.setMdrcMask(flag);
        
        Boolean result = maskingService.insertOrUpdate(masking);
        
        Map<String, String> returnMap = new HashMap();
        returnMap.put("result", result.toString());
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 1、卡待审核列表
     * */
    @RequestMapping("cardApprovalIntro")
    public String cardApprovalIntro(){
        return "mdrcMasking/cardApprovalIntro.ftl";
    }
    
    /**
     * 2、卡待下载列表
     * */
    @RequestMapping("cardDownloadIntro")
    public String cardDownloadIntro(){
        return "mdrcMasking/cardDownloadIntro.ftl";
    }
    
    /**
     * 3、签收列表
     * */
    @RequestMapping("cardSignIntro")
    public String cardSignIntro(){
        return "mdrcMasking/cardSignIntro.ftl";
    }
    
    /**
     * 4、激活申请列表
     * */
    @RequestMapping("activeApplicationIntro")
    public String activeApplicationIntro(){
        return "mdrcMasking/activeApplicationIntro.ftl";
    }
    
    /**
     * 5、激活审核列表
     * */
    @RequestMapping("activeApprovalIntro")
    public String activeApprovalIntro(){
        return "mdrcMasking/activeApprovalIntro.ftl";
    }

    /**
     * 6、营销卡统计
     * */
    @RequestMapping("cardStatisticsIntro")
    public String cardStatisticsIntro(){
        return "mdrcMasking/cardStatisticsIntro.ftl";
    }
    
}

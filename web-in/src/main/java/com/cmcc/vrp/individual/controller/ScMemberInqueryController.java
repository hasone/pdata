package com.cmcc.vrp.individual.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.boss.sichuan.service.ScMemberInquiryService;
import com.cmcc.vrp.ec.bean.individual.MemberInqueryResp;
import com.cmcc.vrp.ec.bean.individual.MemberInqueryRespData;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.thoughtworks.xstream.XStream;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月13日 下午5:38:45
*/
@Controller
@RequestMapping(value = "memberInquery")
public class ScMemberInqueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScMemberInqueryController.class);
    
    @Autowired
    ScMemberInquiryService scMemberInquiryService;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    /**
     * @param mobile
     * @param request
     * @param response
     */
    @RequestMapping(value = "{mobile}", method = RequestMethod.GET)
    @ResponseBody
    public void memberInquery(@PathVariable String mobile, HttpServletRequest request, HttpServletResponse response) {
        MemberInqueryResp memberInqueryResp;
        if (StringUtils.isBlank(mobile)
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String dateRanage = scMemberInquiryService.sendInquiryRequest(mobile);
        if (dateRanage == null) {
            LOGGER.error("记录不存在. mobile = {}", mobile);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        memberInqueryResp = buildResponse(dateRanage, mobile);
        XStream xStream = new XStream();
        xStream.alias("Response", MemberInqueryResp.class);
        xStream.autodetectAnnotations(true);

        try {
            StreamUtils.copy(xStream.toXML(memberInqueryResp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("响应v网查询出错，错误信息为{}.", e.toString());
        }
    }

    private MemberInqueryResp buildResponse(String dateRanage, String mobile) {
        MemberInqueryResp memberInqueryResp = new MemberInqueryResp();
        memberInqueryResp.setResponseTime(String.valueOf(System.currentTimeMillis()));
        MemberInqueryRespData memberInqueryRespData = new MemberInqueryRespData();
        memberInqueryRespData.setMobile(mobile);
        memberInqueryRespData.setEffDate(dateRanage);
        memberInqueryResp.setMemberInqueryRespData(memberInqueryRespData);
        return memberInqueryResp;
    }

}

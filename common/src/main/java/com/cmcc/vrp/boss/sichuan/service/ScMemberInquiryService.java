package com.cmcc.vrp.boss.sichuan.service;

import java.util.Map;

import com.cmcc.vrp.boss.sichuan.model.ScMemberInquiryResponse;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月13日 下午4:27:33
*/
public interface ScMemberInquiryService {
    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(String mobile);

    /**
     * 解析输出参数
     *
     * @param responseStr
     * @return
     */
    ScMemberInquiryResponse parseResponse(String responseStr);


    /**
     * 发送请求
     *
     * @param params
     * @return
     */
    String sendInquiryRequest(String mobile);

    
    /**
     * @param mobile
     * @return
     */
    Integer getdayRange(String mobile);
    
    /**
     * @param mobile
     * @return
     */
    int[] getdateRange(String mobile);

    /** 
     * 获取V网网龄的信息
     * @Title: getVpmnYearInfo 
     */
    Map getVpmnYearInfo(String mobile);
}

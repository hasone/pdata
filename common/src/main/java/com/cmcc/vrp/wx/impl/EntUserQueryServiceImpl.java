package com.cmcc.vrp.wx.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.gansu.HttpUtil;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.CrowdfundingQueryUrl;
import com.cmcc.vrp.province.service.CrowdfundingQueryUrlService;
import com.cmcc.vrp.wx.EntUserQueryService;
import com.cmcc.vrp.wx.beans.QueryInfoReq;
import com.cmcc.vrp.wx.beans.QueryInfoReqData;
import com.cmcc.vrp.wx.beans.QueryResp;
import com.thoughtworks.xstream.XStream;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月9日 下午3:26:32
*/
@Service
public class EntUserQueryServiceImpl implements EntUserQueryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntUserQueryServiceImpl.class);
    private static XStream xStream = null;
    @Autowired
    CrowdfundingQueryUrlService crowdfundingQueryUrlService;
    
    static {
        xStream = new XStream();
        xStream.alias("Request", QueryInfoReq.class);
        xStream.alias("Response", QueryResp.class);
        xStream.autodetectAnnotations(true);
    }
    
    @Override
    public boolean checkMobile(String mobile, Long crowdfundingActivityDetailId) {
        String queryUrl = null;
        CrowdfundingQueryUrl crowdfundingQueryUrl = 
                crowdfundingQueryUrlService.getByCrowdfundingActivityDetailId(crowdfundingActivityDetailId);
        if (crowdfundingQueryUrl == null
                || (queryUrl = crowdfundingQueryUrl.getQueryUrl()) == null) {
            LOGGER.info("企业配置查询地址为空，mobile={}, crowdfundingActivityDetailId={}", mobile, crowdfundingActivityDetailId);
            return false;
        }
        String systemNum = SerialNumGenerator.buildBossReqSerialNum(10);
        QueryInfoReq queryInfoReq = new QueryInfoReq();
        QueryInfoReqData queryInfoReqData = new QueryInfoReqData();
        queryInfoReqData.setMobile(mobile);
        queryInfoReqData.setSystemNum(systemNum);
        queryInfoReq.setQueryInfo(queryInfoReqData);
        QueryResp queryResp = null;
        try {
            String req = xStream.toXML(queryInfoReq);
            LOGGER.info("企业查询接口地址{}", queryUrl);
            LOGGER.info("企业查询接口请求报文{}", req);
            String response = HttpUtil.doPost(queryUrl, req, "UTF-8", true);
            LOGGER.info("企业配置查询接口返回，mobile={}, response={}", mobile, response);
            if (response == null
                    || (queryResp = (QueryResp) xStream.fromXML(response)) == null
                    || queryResp.getQueryRespData() == null
                    || !"0000".equals(queryResp.getQueryRespData().getCode())) {
                LOGGER.error("企业校验号码失败，mobile={}, response={}", mobile, response);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }
}

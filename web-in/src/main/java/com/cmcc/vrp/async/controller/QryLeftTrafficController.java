package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.boss.chongqing.pojo.model.DetailInfo;
import com.cmcc.vrp.boss.chongqing.pojo.model.QryLeftTrafficResult;
import com.cmcc.vrp.boss.chongqing.service.UserQryService;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.QryLeftTraffic;
import com.cmcc.vrp.ec.bean.QryLeftTrafficReq;
import com.cmcc.vrp.ec.bean.QryLeftTrafficResp;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * 重庆个人套餐查询
 * @author lgk8023
 *
 */
@Controller
@RequestMapping(value = "cq")
public class QryLeftTrafficController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QryLeftTrafficController.class);
    @Autowired
    UserQryService userQryService;
    private XStream xStream;
    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", QryLeftTrafficReq.class);
        xStream.alias("Response", QryLeftTrafficResp.class);
        xStream.autodetectAnnotations(true);
    }
    /**
     * @param request
     * @param response
     * @throws IOException 
     */
    @RequestMapping(value = "qryLeftTraffic", method = RequestMethod.POST)
    @ResponseBody
    public void qryLeftTraffic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String qryReqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
        LOGGER.info("收到查询请求报文{}", qryReqStr);
        QryLeftTrafficResp qryLeftTrafficResp = new QryLeftTrafficResp();
        QryLeftTrafficReq qryLeftTrafficReq = (QryLeftTrafficReq) xStream.fromXML(qryReqStr);
        String type;
        String mobile;
        String cycle;
        QryLeftTraffic qryLeftTraffic;
        Gson gson = new Gson();
        if ((qryLeftTraffic = qryLeftTrafficReq.getQryLeftTraffic()) == null
                || StringUtils.isEmpty(mobile = qryLeftTraffic.getTelnum())
                || !StringUtils.isValidMobile(mobile)
                || StringUtils.isEmpty(type = qryLeftTraffic.getType())
                || StringUtils.isEmpty(cycle = qryLeftTraffic.getCycle())
                || !validate(type, cycle)) {
            LOGGER.error("请求参数有误");
            qryLeftTrafficResp.setResCode("11");
            qryLeftTrafficResp.setResDesc("请求参数有误");
            try {
                StreamUtils.copy(xStream.toXML(qryLeftTrafficResp), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("查询响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
    
        String userQryStr = userQryService.qryLeftTraffic(mobile, type, cycle);
        if (userQryStr == null) {
            qryLeftTrafficResp.setResCode("12");
            qryLeftTrafficResp.setResDesc("查询结果为空");
            try {
                StreamUtils.copy(xStream.toXML(qryLeftTrafficResp), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("查询响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        com.cmcc.vrp.boss.chongqing.pojo.model.QryLeftTrafficResp userQryResp = 
                gson.fromJson(userQryStr, com.cmcc.vrp.boss.chongqing.pojo.model.QryLeftTrafficResp.class);
        qryLeftTrafficResp = build(userQryResp);
        try {
            StreamUtils.copy(xStream.toXML(qryLeftTrafficResp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("查询响应出错，错误信息为{}.", e.toString());
        }
    }
    private QryLeftTrafficResp build(com.cmcc.vrp.boss.chongqing.pojo.model.QryLeftTrafficResp userQryResp) {
        QryLeftTrafficResp qryLeftTrafficResp = new QryLeftTrafficResp();
        qryLeftTrafficResp.setResCode(userQryResp.getResCode());
        qryLeftTrafficResp.setResDesc(userQryResp.getResDesc());
        QryLeftTrafficResult qryLeftTrafficResult;
        List<DetailInfo> detailInfos;
        List<com.cmcc.vrp.ec.bean.DetailInfo> results = 
                new ArrayList<com.cmcc.vrp.ec.bean.DetailInfo>();
        if ((qryLeftTrafficResult = userQryResp.getResult()) != null
                && (detailInfos = qryLeftTrafficResult.getDetailInfo()) != null
                && detailInfos.size() != 0) {
            for(DetailInfo detailInfo:detailInfos) {
                com.cmcc.vrp.ec.bean.DetailInfo result = 
                        new com.cmcc.vrp.ec.bean.DetailInfo();
                result.setType(detailInfo.getType());
                result.setTypeName(detailInfo.getTypeName());
                result.setServiceStatus(detailInfo.getServiceStatus());
                result.setServiceName(detailInfo.getServiceName());
                result.setSumNum(detailInfo.getSumNum());
                result.setUsedNum(detailInfo.getUserNum());
                result.setLeftNum(detailInfo.getLeftNum());
                result.setTfFlag(detailInfo.getTfFlag());
                result.setCustProduct(detailInfo.getCustProduct());
                result.setPrivid(detailInfo.getPrivId());
                result.setHcof(detailInfo.getHcof());
                results.add(result);
            }
        }
        qryLeftTrafficResp.setResult(results);
        return qryLeftTrafficResp;
    }
    private boolean validate(String type, String cycle) {
        if (!("0".equals(type)
                || "4a".equals(type)
                || "4b".equals(type))) {
            LOGGER.error("查询类型错误-" + type);
            return false;
        }
        Date date = DateUtil.parse("yyyyMM", cycle);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        int rang = DateUtil.getMonthsOfAge(calendar1, calendar2);
        if (rang < 0
                || rang > 1) {
            LOGGER.error("日期超过可查区间-" + cycle);
            return false;
        }
        
        return true;
    }
}

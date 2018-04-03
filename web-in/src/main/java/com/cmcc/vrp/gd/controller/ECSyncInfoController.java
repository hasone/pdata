package com.cmcc.vrp.gd.controller;

import java.io.IOException;
import java.util.List;

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

import com.cmcc.vrp.ec.bean.gd.ContactInfo;
import com.cmcc.vrp.ec.bean.gd.ECSyncInfoReq;
import com.cmcc.vrp.ec.bean.gd.ECSyncInfoResp;
import com.cmcc.vrp.province.model.ECSyncInfo;
import com.cmcc.vrp.province.service.ECSyncInfoService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GDRegion;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * @author lgk8023
 *
 */
@Controller
@RequestMapping(value = "/gd")
public class ECSyncInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ECSyncInfoController.class);
    
    private static XStream xStream = null;
    
    static {
        xStream = new XStream();
        xStream.alias("ECSyncInfoReq", ECSyncInfoReq.class);
        xStream.alias("ECSyncInfoResp", ECSyncInfoResp.class);
        xStream.autodetectAnnotations(true);
    }
    
    @Autowired
    ECSyncInfoService ecSyncInfoService;
    @Autowired
    GlobalConfigService globalConfigService;
    
    public static void main(String[] args) {
        ECSyncInfoResp ecSyncInfoResp = new ECSyncInfoResp();
        ecSyncInfoResp.setResult("0");
        ecSyncInfoResp.setResultMsg("success");
        String result = xStream.toXML(ecSyncInfoResp);
        Response response = new Response();
        response.setCode("0");
        response.setMsg(result);
        Gson gson = new Gson();
        result = gson.toJson(response);
        System.out.println(result);
        response = gson.fromJson(result, Response.class);
        System.out.println(response);
    }
    /**
     * @param request
     * @param response
     */
    @RequestMapping(value = "ecSyncInfo", method = RequestMethod.POST)
    @ResponseBody
    public void ecSyncInfo(HttpServletRequest request, HttpServletResponse response) {
        Response result = new Response();
        Gson gson = new Gson();
        ECSyncInfoResp ecSyncInfoResp = new ECSyncInfoResp();
        ECSyncInfoReq ecSyncInfoReq = parse(request);
        if (ecSyncInfoReq == null) {
            LOGGER.error("解析报文出错");
            ecSyncInfoResp.setResult("1");
            ecSyncInfoResp.setResultMsg("解析报文出错");
            try {
                result.setCode("1");
                result.setMsg(xStream.toXML(ecSyncInfoResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        
        if (!check(ecSyncInfoReq)) {
            LOGGER.error("必要字段缺失或为空");
            ecSyncInfoResp.setResult("2");
            ecSyncInfoResp.setResultMsg("必要字段缺失或为空");
            try {
                result.setCode("2");
                result.setMsg(xStream.toXML(ecSyncInfoResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }

        ECSyncInfo newEcSyncInfo = initECSyncInfo(ecSyncInfoReq);

        if (!ecSyncInfoService.updateOrInsert(newEcSyncInfo)) {
            LOGGER.error("更新数据库失败!");
            ecSyncInfoResp.setResult("3");
            ecSyncInfoResp.setResultMsg("更新数据库失败!");
            try {
                result.setCode("3");
                result.setMsg(xStream.toXML(ecSyncInfoResp));
                StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("响应出错，错误信息为{}.", e.toString());
            }
            return;
        }

        ecSyncInfoResp.setResult("0");
        ecSyncInfoResp.setResultMsg("success");
        try {
            result.setCode("0");
            result.setMsg(xStream.toXML(ecSyncInfoResp));
            StreamUtils.copy(gson.toJson(result), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("响应出错，错误信息为{}.", e.toString());
        }
        return;
    }
    private ECSyncInfo initECSyncInfo(ECSyncInfoReq ecSyncInfoReq) {
        
        List<ContactInfo> contactInfos = ecSyncInfoReq.getContactInfo();
        ContactInfo contactInfo = getContactInfo(contactInfos);
        
        ECSyncInfo ecSyncInfo = new ECSyncInfo();
        ecSyncInfo.setEcCode(ecSyncInfoReq.getEcCode());
        ecSyncInfo.setEcName(ecSyncInfoReq.getEcName());       
        ecSyncInfo.setRegion(GDRegion.getAreaName(ecSyncInfoReq.getRegion()));
        ecSyncInfo.setLegalPerson(ecSyncInfoReq.getLegalPerson());
        ecSyncInfo.setEntPermit(ecSyncInfoReq.getEntPermit());
        ecSyncInfo.setUserName(contactInfo.getUserName());
        ecSyncInfo.setMobile(contactInfo.getMobile());
        ecSyncInfo.setEmail(contactInfo.getEmail());
        ecSyncInfo.setMainContact(Integer.valueOf(contactInfo.getMainContact()));
        ecSyncInfo.setEcLevel(ecSyncInfoReq.getEcLevel());
        ecSyncInfo.setUnitKind(ecSyncInfoReq.getUnitKind());
        ecSyncInfo.setDistrict(GDRegion.getAreaName(ecSyncInfoReq.getDistrict()));
        ecSyncInfo.setInnetDate(DateUtil.parse("yyyy-MM-dd", ecSyncInfoReq.getInnetDate()));
        ecSyncInfo.setVipType(ecSyncInfoReq.getVipType());
        ecSyncInfo.setVipTypeStateDate(DateUtil.parse("yyyy-MM-dd", ecSyncInfoReq.getVipTypeStateDate()));
        ecSyncInfo.setCreditLevel(ecSyncInfoReq.getCreditLevel());
        ecSyncInfo.setDevChannel(ecSyncInfoReq.getDevChannel());
        ecSyncInfo.setDevUserId(ecSyncInfoReq.getDevUserId());
        if ("01".equals(ecSyncInfoReq.getOprCode())) {
            ecSyncInfo.setDeleteFlag(0);
        } else {
            ecSyncInfo.setDeleteFlag(1);
        }
        return ecSyncInfo;
    }
    private ContactInfo getContactInfo(List<ContactInfo> contactInfos) {
        ContactInfo buildContactInfo = new ContactInfo();
        buildContactInfo.setEmail("-");
        buildContactInfo.setUserName("-");
        buildContactInfo.setMobile("-");
        buildContactInfo.setMainContact("1");
        if (contactInfos == null
                || contactInfos.size() == 0) {
            return buildContactInfo;
        }
        for(ContactInfo contactInfo:contactInfos) {
            if ("1".equals(contactInfo.getMainContact())) {
                return contactInfo;
            }
        }
        
        return contactInfos.get(0);
    }
    private boolean check(ECSyncInfoReq ecSyncInfoReq) {
        return !StringUtils.isEmpty(ecSyncInfoReq.getEcCode())
                && !StringUtils.isEmpty(ecSyncInfoReq.getOprCode())
                && !StringUtils.isEmpty(ecSyncInfoReq.getEcName())
                && !StringUtils.isEmpty(ecSyncInfoReq.getRegion())
                && !StringUtils.isEmpty(ecSyncInfoReq.getEcLevel())
                && !StringUtils.isEmpty(ecSyncInfoReq.getUnitKind())
                && !StringUtils.isEmpty(ecSyncInfoReq.getDistrict())
                && !StringUtils.isEmpty(ecSyncInfoReq.getInnetDate())
                && !StringUtils.isEmpty(ecSyncInfoReq.getVipType())
                && !StringUtils.isEmpty(ecSyncInfoReq.getVipTypeStateDate())
                && !StringUtils.isEmpty(ecSyncInfoReq.getCreditLevel())
                && !StringUtils.isEmpty(ecSyncInfoReq.getDevChannel())
                && !StringUtils.isEmpty(ecSyncInfoReq.getDevUserId());
    }
    
    private ECSyncInfoReq parse(HttpServletRequest request) {
        String reqStr = null;
        try {
            reqStr = StreamUtils.copyToString(request.getInputStream(), Charsets.UTF_8);
            LOGGER.info("从BOSS侧接收到订购关系状态变更信息， 信息内容为{}.", reqStr == null ? "空" : reqStr);
            return (ECSyncInfoReq) xStream.fromXML(reqStr);
        } catch (Exception e) {
            LOGGER.error("解析回调参数时出错.");
            return null;
        }
    }
}

package com.cmcc.vrp.async.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.MemOperateInfo;
import com.cmcc.vrp.ec.bean.MemOperateReq;
import com.cmcc.vrp.ec.bean.MemOperateResp;
import com.cmcc.vrp.util.StringUtils;
import com.thoughtworks.xstream.XStream;

/**
 * 重庆本地流量成员变更
 * @author lgk8023
 *
 */
@Controller
@RequestMapping(value = "cq")
public class MemOperateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemOperateController.class);
    
    private XStream xStream;
    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", MemOperateReq.class);
        xStream.alias("Response", MemOperateResp.class);
        xStream.autodetectAnnotations(true);
    }
    
    /**
     * @param request
     * @param response
     */
    @RequestMapping(value = "memOperate", method = RequestMethod.POST)
    public void memOperate(HttpServletRequest request, HttpServletResponse response) {
        String oprReqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
        LOGGER.info("收到查询请求报文{}", oprReqStr);
        MemOperateResp memOperateResp = new MemOperateResp();
        MemOperateReq memOperateReq = (MemOperateReq) xStream.fromXML(oprReqStr);
        MemOperateInfo memOperateInfo;
        if ((memOperateInfo = memOperateReq.getMemOperateInfo()) == null
                || StringUtils.isEmpty(memOperateInfo.getEnterCode())
                || StringUtils.isEmpty(memOperateInfo.getEnterPrdCode())
                || StringUtils.isEmpty(memOperateInfo.getMobile())
                || StringUtils.isEmpty(memOperateInfo.getPrdCode())
                || StringUtils.isEmpty(memOperateInfo.getOprType())
                || StringUtils.isEmpty(memOperateInfo.getOprTime())
                || StringUtils.isEmpty(memOperateInfo.getEffectTime())) {
            LOGGER.error("请求参数有误");
            memOperateResp.setCode("11");
            memOperateResp.setMessage("请求参数有误");
            try {
                StreamUtils.copy(xStream.toXML(memOperateResp), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("查询响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        memOperateResp.setCode("0000");
        memOperateResp.setMessage("成功");
        try {
            StreamUtils.copy(xStream.toXML(memOperateResp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("查询响应出错，错误信息为{}.", e.toString());
        }
    }
}

package com.cmcc.vrp.async.controller;

import java.io.IOException;

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

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.exception.ForbiddenException;
import com.cmcc.vrp.ec.exception.ParaErrorException;
import com.cmcc.vrp.ec.service.AuthService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.IpUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.UnknownFieldException;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

/**
 * Created by leelyn on 2016/5/17.
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthService authService;

    @Autowired
    GlobalConfigService globalConfigService;

    /** 
     * @Title: auth 
     * @param request
     * @param response
     * @throws IOException
     * @Author: wujiamin
     * @date 2016年10月17日下午4:55:18
    */
    @RequestMapping(value = "auth", method = RequestMethod.POST)
    @ResponseBody
    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthResp authResp;
        String authReqStr = StreamUtils.copyToString(request.getInputStream(), Charsets.UTF_8);
        logger.info("收到认证请求报文{}", authReqStr);
        XStream xStream = new XStream();
        String ipAddress = IpUtils.getRemoteAddr(request);
        if (needCheck()) {
            String contentType = request.getContentType();
            if (contentType == null) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                logger.info("认证失败：content-type未设置，请求报文：" + authReqStr + ",请求IP：" + ipAddress);
                return;
            }
            if (!(contentType.indexOf("application/xml") != -1 || contentType.indexOf("text/xml") != -1)) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                logger.info("认证失败：content-type格式错误，支持的格式为：application/xml，text/xml，实际格式：" + contentType + "，请求报文："
                        + authReqStr + ",请求IP：" + ipAddress);
                return;
            }
        }

        try {
            xStream.alias("Request", AuthReq.class);
            xStream.autodetectAnnotations(true);
            AuthReq authReq = (AuthReq) xStream.fromXML(authReqStr);
            if ((authResp = authService.getToken(authReq, ipAddress)) != null) {
                xStream.alias("Response", AuthResp.class);
                StreamUtils.copy(xStream.toXML(authResp), Charsets.UTF_8, response.getOutputStream());

                return;
            }
        } catch (ParaErrorException e) {
            logger.error("Auth faild:{}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (ForbiddenException e) {
            logger.error("Auth faild:{}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        } catch (UnknownFieldException e) {
            logger.error("Auth faild:{}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        } catch (ConversionException e) {
            logger.error("Auth faild:{}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        } catch (CannotResolveClassException e) {
            logger.error("Auth faild:{}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private boolean needCheck() {

        String checkFlag = globalConfigService.get(GlobalConfigKeyEnum.EC_NEED_CHECK.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(checkFlag) ? "false" : checkFlag;
        return Boolean.parseBoolean(finalFlag);
    }
}

/**
 * 
 */
package com.cmcc.vrp.ec.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmcc.vrp.ec.bean.AuthResp;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.cmcc.vrp.util.ECValidateUtil;

/**
 * @author: wuguoping
 * @data: 2017年5月9日
 */
@Controller
@RequestMapping("ecValidate")
public class ProductValidateController {
    private static final Logger logger = LoggerFactory.getLogger(AuthValidateController.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * 产品查询接口验证
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "products")
    public void productValidateController(@RequestParam(value = "appKey", required = true) String appKey,
            @RequestParam(value = "appSecret", required = false) String appSecret,
            @RequestParam(value = "serverAddress", required = true) String url,
            @RequestParam(value = "submitType", required = true) String submitType,
            @RequestParam(value = "apiRequest", required = false) String apiRequestBodyStr,
            HttpServletResponse response) throws IOException, DocumentException {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("----产品查询接口验证 开始----");
        // 1、验证请求方式是否正确
        if (!"GET".equals(submitType)) {
            logger.error("产品查询接口为 “GET” 方法");
            resultMap.put("message", "请选择正确的提交方式：  ");
            resultMap.put("errorMessage", "产品查询接口为 “GET” 方法。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 2、获取Token
        String authUrl = url.substring(0, url.lastIndexOf("/web-in")) + "/web-in/auth.html";
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String authRequestTime = sdf.format(new Date());
        String xml = appKey + authRequestTime + appSecret;
        String authRequesTokenSign = DigestUtils.sha256Hex(xml);
        resultMap.put("warning", "已将请求时间设置为当前时间。\n");
        Map<String, String> getTokenMap = ECValidateUtil.getToken(authUrl, appKey, appSecret, authRequestTime, authRequesTokenSign);

        // 判断认证是否成功
        if (!getTokenMap.get("code").equals("200 OK")) {
            logger.info("认证失败 :" + getTokenMap.get("message"));
            resultMap.put("message", "认证失败");
            resultMap.put("errorMessage", "认证返回code = " + getTokenMap.get("code") + "	" + "错误信息为：" + getTokenMap.get("message"));
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        XStream xStream = new XStream();
        xStream.processAnnotations(AuthResp.class);
        AuthResp authResp = null;
        try {
            authResp = (AuthResp) xStream.fromXML(getTokenMap.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取token，转换成 AuthResp 时失败!");
        }

        String token = "";
        try {
            if (authResp == null) {
                logger.info("AuthResp is null !");
                return;
            }
            token = authResp.getAuthRespData().getToken();
        } catch (Exception e) {
            logger.info("获取token出错，错误为{}", e);
        }
        
        String requestXml = "";
        String authRequesSign = DigestUtils.sha256Hex(requestXml + appSecret);
        Map<String, String> map = ECValidateUtil.doGet(url, requestXml, token, authRequesSign, "utf-8", false);

        // 判断请求返回信息
        if (!map.get("code").equals("200 OK")) {
            logger.info("产品查询失败 :" + map.get("message"));
            resultMap.put("message", "产品查询失败！");
            resultMap.put("errorMessage", "请求返回code = " + getTokenMap.get("code") + "	" + "错误信息为：" + getTokenMap.get("message"));
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        resultMap.put("message", "请求产品查询API成功" + "  \n" + "认证返回的 token = " + token);
        resultMap.put("errorMessage", null);
        resultMap.put("apiResponse", map);
        StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
        logger.info("----产品查询接口验证 结束----");
        return;
    }
}

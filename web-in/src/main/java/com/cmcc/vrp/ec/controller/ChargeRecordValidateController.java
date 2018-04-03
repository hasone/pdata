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
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.util.ECValidateUtil;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * @author: wuguoping
 * @data: 2017年5月9日
 */
@Controller
@RequestMapping("ecValidate")
public class ChargeRecordValidateController {
    private static final Logger logger = LoggerFactory.getLogger(AuthValidateController.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * 
     * title: chargeRecord desc:
     * 
     * @param appKey
     * @param appSecret
     * @param url
     * @param submitType
     * @param apiRequestBodyStr
     * @param response
     * @throws IOException
     * @throws DocumentException
     *             wuguoping 2017年5月31日
     */
    @RequestMapping("chargeRecord")
    @ResponseBody
    public void chargeRecord(@RequestParam(value = "appKey", required = true) String appKey,
            @RequestParam(value = "appSecret", required = true) String appSecret, 
            @RequestParam(value = "serverAddress", required = true) String url,
            @RequestParam(value = "submitType", required = true) String submitType,
            @RequestParam(value = "apiRequest", required = false) String apiRequestBodyStr, 
            HttpServletResponse response) throws IOException, DocumentException {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("----充值记录查询接口验证 开始----");
        // 1、验证请求方式是否正确
        if (!"GET".equals(submitType)) {
            logger.error("充值查询接口为 “GET” 方法");
            resultMap.put("message", "请选择正确的提交方式：  ");
            resultMap.put("errorMessage", "充值查询接口为 “GET” 方法。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 2、获取token
        String authUrl = url.substring(0, url.lastIndexOf("/web-in")) + "/web-in/auth.html";
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String authRequestTime = sdf.format(new Date());

        String xml = appKey + authRequestTime + appSecret;
        String authRequesTokenSign = DigestUtils.sha256Hex(xml);
        resultMap.put("warning", "已将请求时间设置为当前时间。\n");

        Map<String, String> getTokenMap = ECValidateUtil.getToken(authUrl, appKey, appSecret, authRequestTime, authRequesTokenSign);

        // 判断认证请求
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
            if (authResp == null) {
                logger.info("AuthResp is null !");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取token，转换成 AuthResp 时失败");
        }

        String token="";
        try {
            token = authResp.getAuthRespData().getToken();
        } catch (NullPointerException e) {
            logger.info("获取token出错，错误为{}", e);
        }
        String requestXml = "";
        String chargeRecordeSign = DigestUtils.sha256Hex(requestXml + appSecret);
        String chargeRecordeURL = url;

        Map<String, String> map = ECValidateUtil.doGet(chargeRecordeURL, requestXml, token, chargeRecordeSign, "utf_8", false);

        // 判断充值查询返回信息
        if (!map.get("code").equals("200 OK")) {
            logger.info("充值查询请求失败 :" + map.get("message"));
            resultMap.put("message", "充值查询请求失败 ！  ");
            resultMap.put("errorMessage", "请求返回code = " + map.get("code") + "	" + "错误信息为：" + map.get("message"));
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        resultMap.put("message", "请求充值API成功   " + "code: " + map.get("code") + "  \n" + "认证返回的 token = " + token);
        resultMap.put("errorMessage", null);
        resultMap.put("apiResponse", map);
        StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
        logger.info("----充值记录查询接口验证 结束----");
        return;
    }

}

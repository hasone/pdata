/**
 * 
 */
package com.cmcc.vrp.ec.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.springframework.web.util.HtmlUtils;

import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.bean.ChargeReq;
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
public class ChargeValidateController {
    private static final Logger logger = LoggerFactory.getLogger(AuthValidateController.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * 充值接口验证
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("charge")
    @ResponseBody
    public void chargeValidate(@RequestParam(value = "appKey", required = true) String appKey,
            @RequestParam(value = "appSecret", required = false) String appSecret, 
            @RequestParam(value = "serverAddress", required = true) String url,
            @RequestParam(value = "submitType", required = true) String submitType,
            @RequestParam(value = "apiRequest", required = false) String apiRequestBodyStr,
            HttpServletResponse response) throws IOException, DocumentException {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("----充值接口验证 开始----");
        // 1、验证请求方式是否正确
        if (!"POST".equals(submitType)) {
            logger.error("认证接口为 “POST” 方法");
            resultMap.put("message", "请选择正确的提交方式：  ");
            resultMap.put("errorMessage", "认证接口为 “POST” 方法。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        String apiRequestBody = HtmlUtils.htmlUnescape(apiRequestBodyStr);

        // 2、xml 转换成 JavaBean,通过转换来验证格式是否有误
        XStream xStream = new XStream();
        xStream.processAnnotations(ChargeReq.class);
        ChargeReq chargeReq = null;
        try {
            chargeReq = (ChargeReq) xStream.fromXML(apiRequestBody);
        } catch (Exception e) {
            resultMap.put("message", "发送请求前，有错误。");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String strs = sw.toString();
            String result = ECValidateUtil.getInfo(strs);
            logger.info("请求体格式有误， 错误信息如下：  " + "\n" + result);
            resultMap.put("errorMessage", "请求体格式有误， 错误信息如下：  " + "\n" + result);
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 3、时间节点校验
        String requestTime = chargeReq.getRequestTime();
        if ("".equals(requestTime) || requestTime == null) {
            logger.error("时间节点为空");
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "时间节点为空。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        } else if (!ECValidateUtil.validateDateFormat(requestTime)) {
            logger.error("时间格式不正确");
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "时间格式不正确。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 4、验证手机号码
        String mobile = chargeReq.getChargeReqData().getMobile();
        if ("".equals(mobile) || mobile == null) {
            logger.error("Mobile 节点为空");
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "Mobile 节点为空。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        } else if (!(ECValidateUtil.isChinaPhoneLegal(mobile) || ECValidateUtil.isHKPhoneLegal(mobile))) {
            logger.error("手机号码格式不正确");
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "手机号码格式不正确。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 5、验证ProductId 节点
        String productId = chargeReq.getChargeReqData().getProductCode();
        if (ECValidateUtil.isBlank(resultMap, productId)) {
            logger.error("ProductId 节点为空");
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 6、验证SerialNum 节点
        String serialNum = chargeReq.getChargeReqData().getSerialNum();
        if (ECValidateUtil.isBlank(resultMap, serialNum)) {
            logger.error("serialNum 节点为空");
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 7、 验证时间是否失效 30s
        if (ECValidateUtil.getInterval(chargeReq.getRequestTime()) >= 30000) {
            logger.info("报文请求时间已超时， 已将请求时间设置为当前时间。");
            resultMap.put("warning", "报文请求时间已超时， 已将请求时间设置为当前时间。");
        }

        // 8、获取Token
        String authUrl = url.substring(0, url.lastIndexOf("/web-in")) + "/web-in/auth.html";
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String authRequestTime = sdf.format(new Date());
        String xml = appKey + authRequestTime + appSecret;
        String chargeRequesTokenSign = DigestUtils.sha256Hex(xml);
        Map<String, String> getTokenMap = ECValidateUtil.getToken(authUrl, appKey, appSecret, authRequestTime, chargeRequesTokenSign);

        // 判断认证是否成功
        if (!getTokenMap.get("code").equals("200 OK")) {
            logger.info("认证失败 :" + getTokenMap.get("message"));
            resultMap.put("message", "认证失败");
            resultMap.put("errorMessage", "认证返回code = " + getTokenMap.get("code") + "	" + "错误信息为：" + getTokenMap.get("message"));
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        xStream.processAnnotations(AuthResp.class);
        AuthResp authResp = null;
        try {
            authResp = (AuthResp) xStream.fromXML(getTokenMap.get("message"));
        } catch (Exception e) {
            logger.info("获取token，转换成 AuthResp 时失败!");
        }

        // 9、发送充值请求
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
        
        String requestBody = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Request><Datetime>" + authRequestTime + "</Datetime>" + "<ChargeData><Mobile>"
                + mobile + "</Mobile>" + "<ProductId>" + productId + "</ProductId>" + "<SerialNum>" + serialNum + "</SerialNum>" + "</ChargeData></Request>";
        String signatrue = DigestUtils.sha256Hex(requestBody + appSecret);
        Map<String, String> map = ECValidateUtil.doPost(url, requestBody, token, signatrue, "utf-8", false, "application/xml");

        // 判断充值返回信息
        if (!map.get("code").equals("200 OK")) {
            logger.info("充值失败 :" + map.get("message"));
            resultMap.put("message", "充值不成功");
            resultMap.put("errorMessage", "请求返回code = " + getTokenMap.get("code") + "	" + "错误信息为：" + getTokenMap.get("message"));
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        resultMap.put("message", "请求充值API成功   " + "code: " + map.get("code") + "  \n" + "认证返回的 token = " + token);
        resultMap.put("errorMessage", null);
        resultMap.put("apiResponse", map);
        StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
        logger.info("----充值接口验证 结束----");
        return;
    }
}

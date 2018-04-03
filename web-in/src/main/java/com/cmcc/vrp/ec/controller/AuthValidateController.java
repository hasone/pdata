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
import org.apache.commons.io.Charsets;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.util.ECValidateUtil;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * @author: wuguoping
 * @data: 2017年5月9日
 */
@Controller
@RequestMapping("/ecValidate")
public class AuthValidateController {
    private static final Logger logger = LoggerFactory.getLogger(AuthValidateController.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * 认证接口验证
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    @ResponseBody
    public void authValidate(@RequestParam(value = "appKey", required = true) String appKey,
            @RequestParam(value = "appSecret", required = true) String appSecret, @RequestParam(value = "serverAddress", required = true) String url,
            @RequestParam(value = "submitType", required = true) String submitType,
            @RequestParam(value = "apiRequest", required = true) String apiRequestBodyStr, HttpServletResponse response) throws IOException, DocumentException {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("----认证接口验证 开始----");
        // 1、验证请求方式是否正确
        if (!"POST".equals(submitType)) {
            logger.error("认证接口为 “POST” 方法. ");
            resultMap.put("message", "请选择正确的提交方式：  ");
            resultMap.put("errorMessage", "认证接口为 “POST” 方法。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        String apiRequestBody = HtmlUtils.htmlUnescape(apiRequestBodyStr);

        // 2、xml 转换成 JavaBean,通过转换来验证格式是否有误
        XStream xStream = new XStream();
        xStream.processAnnotations(AuthReq.class);
        AuthReq authReq = null;
        try {
            authReq = (AuthReq) xStream.fromXML(apiRequestBody);
        } catch (Exception e) {
            resultMap.put("message", "发送请求前，有错误。");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String strs = sw.toString();
            String result = ECValidateUtil.getInfo(strs);
            resultMap.put("errorMessage", "请求体格式有误， 错误信息如下：  " + "\n" + result);
            logger.error("请求体格式有误， 错误信息如下：  " + "\n" + result);
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 3、时间节点校验
        String requestTime = authReq.getRequestTime();
        if ("".equals(requestTime) || requestTime == null) {
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "时间节点为空。");
            logger.error("时间节点为空");
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

        // 4、验证AppKey与报文中是否正确
        String requestAppKey = authReq.getAuthorization().getAppKey().trim();
        if ("".equals(requestAppKey) || requestAppKey == null) {
            logger.error("缺少AppKey节点");
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "缺少AppKey节点。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        } else if (!requestAppKey.equals(appKey)) {
            logger.error("缺少AppKey节点");
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "输入的 AppKey 不正确。");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 5、验证签名是否正确
        String xml = requestAppKey + requestTime + appSecret;// 顺序有关系
        String origSign = authReq.getAuthorization().getSign();
        if ("".equals(origSign) || origSign == null) {
            logger.error("缺少签名（sign）节点");
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "缺少签名（sign）节点");
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        } else if (!ECValidateUtil.verifySign(origSign, xml)) {
            logger.error("签名（sign）不正确");
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", "签名（sign）不正确，正确的签名为：  " + "sign = " + DigestUtils.sha256Hex(xml));
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        // 6、 验证时间是否失效 30s
        if (ECValidateUtil.getInterval(authReq.getRequestTime()) >= 30000) {
            logger.info("报文请求时间已超时， 已将请求时间设置为当前时间。");
            resultMap.put("warning", "报文请求时间已超时， 已将请求时间设置为当前时间。");
        }

        // 7、发送请求，返回请求信息
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String authRequestTime = sdf.format(new Date());
        String authRequesSign = DigestUtils.sha256Hex(appKey + authRequestTime + appSecret);

        String requestXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Request><Datetime>" + authRequestTime + "</Datetime><Authorization><AppKey>" + appKey
                + "</AppKey><Sign>" + authRequesSign + "</Sign></Authorization></Request>";

        Map<String, String> map = ECValidateUtil.doPost(url, requestXML, null, null, "utf-8", false, "application/xml");

        // 判断认证返回信息
        if (!map.get("code").equals("200 OK")) {
            logger.info("认证失败 :" + map.get("message"));
            resultMap.put("message", "认证失败 ！  ");
            resultMap.put("errorMessage", "请求返回code = " + map.get("code") + "	" + "错误信息为：" + map.get("message"));
            resultMap.put("apiResponse", null);
            StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
            return;
        }

        resultMap.put("message", "请求认证API成功   " + "code: " + map.get("code"));
        resultMap.put("errorMessage", null);
        resultMap.put("apiResponse", map);
        StreamUtils.copy(new Gson().toJson(resultMap), Charsets.UTF_8, response.getOutputStream());
        logger.info("----认证接口验证 结束----");
        return;

    }

}

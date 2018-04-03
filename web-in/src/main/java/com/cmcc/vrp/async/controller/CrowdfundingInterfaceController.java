package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.utils.CallbackResult;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.webservice.crowdfunding.CrowdfundingInterfaceService;
import com.cmcc.webservice.crowdfunding.pojo.ActivityResultResp;
import com.cmcc.webservice.crowdfunding.pojo.CFChargeResultResp;
import com.cmcc.webservice.crowdfunding.pojo.PaymentReq;
import com.cmcc.webservice.crowdfunding.pojo.PaymentResp;
import com.cmcc.webservice.crowdfunding.pojo.QueryActResReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityResp;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResResp;
import com.thoughtworks.xstream.XStream;


/**
 * Created by qinqinyan on 2017/2/8. 广东众筹web接口
 */
@Controller
@RequestMapping(value = "crowdFundingInterface")
public class CrowdfundingInterfaceController {
    private static final Logger logger = LoggerFactory.getLogger(CrowdfundingInterfaceController.class);

    @Autowired
    CrowdfundingInterfaceService crowdfundingInterfaceService;

    @Autowired
    ActivitiesService activitiesService;

    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;

    /**
     * 广东众筹活动查询接口
     *
     * @Title: queryActivity
     */
    @RequestMapping(value = "queryActivity", method = RequestMethod.POST)
    @ResponseBody
    public void queryActivity(final HttpServletRequest request, final HttpServletResponse response) {
        //1. 校验参数
        QueryActivityReq req = null;

        //所有请求都需要校验请求类型
        if (!requestContentTypeCheck(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        XStream xStream = new XStream();
        xStream.alias("Request", QueryActivityReq.class);
        xStream.alias("Response", QueryActivityResp.class);
        xStream.autodetectAnnotations(true);

        try {
            String requestString = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            req = (QueryActivityReq) xStream.fromXML(requestString);
        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        logger.info("收到众筹活动查询请求, QueryActivityReq = {}.", JSONObject.toJSONString(req));


        if (!crowdfundingInterfaceService.validateQueryActivityRequest(req)) {
            logger.error("无效的众筹活动查询请求参数, QueryActivityReq = {}.", JSONObject.toJSONString(req));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //业务处理（直接获取返回response对象）
        QueryActivityResp resp = crowdfundingInterfaceService.queryActivity(req);
        //设置响应参数为200
        response.setStatus(HttpServletResponse.SC_OK);

        //封装参数             
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("StreamUtils.copy IOException");
            e.printStackTrace();
        }

        logger.info("响应众筹活动查询请求, Response = {}.", JSONObject.toJSONString(resp));
        return;
    }


    /**
     * 校验请求参数头
     *
     * @Title: requestContentTypeCheck
     */
    private boolean requestContentTypeCheck(HttpServletRequest request) {
        String type = request.getContentType();
        if (StringUtils.isBlank(type)) {
            return false;
        }

        String contentType = request.getContentType();
        if (!(contentType.indexOf("application/xml") != -1 || contentType.indexOf("text/xml") != -1)) {
            return false;
        }
        return true;
    }


    /**
     * 广东众筹活动报名接口
     *
     * @Title: joinActivity
     */
    /*@RequestMapping(value = "joinActivity", method = RequestMethod.POST)
    @ResponseBody
    public void joinActivity(final HttpServletRequest request, final HttpServletResponse response) {
        //1. 校验参数
        JoinActivityReq req = null;

        //所有请求都需要校验请求类型
        if (!requestContentTypeCheck(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        XStream xStream = new XStream();
        xStream.alias("Request", JoinActivityReq.class);
        xStream.alias("Response", JoinActivityResp.class);
        xStream.autodetectAnnotations(true);

        try {
            String requestString = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            req = (JoinActivityReq) xStream.fromXML(requestString);
        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        logger.info("收到众筹活动报名请求, JoinActivityRequest = {}.", JSONObject.toJSONString(req));

        if (!crowdfundingInterfaceService.validateJoinActivityRequest(req)) {
            logger.error("无效的众筹活动报名请求参数, JoinActivityRequest = {}.", JSONObject.toJSONString(req));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //业务处理（直接获取返回response对象）
        JoinActivityResp resp = crowdfundingInterfaceService.joinActivity(req);
        //设置响应参数为200
        response.setStatus(HttpServletResponse.SC_OK);

        //封装参数
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("StreamUtils.copy IOException");
            e.printStackTrace();
        }
        logger.info("响应众筹活动报名请求, JoinActivityResponse = {}.", JSONObject.toJSONString(resp));

        return;
    }
*/
    /**
     * 众筹结果查询接口
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "queryActivityResult", method = RequestMethod.POST)
    @ResponseBody
    public void queryActivityResult(final HttpServletRequest request, final HttpServletResponse response) {
        //1、校验参数
        QueryActResReq req = null;

        //所有请求都需要校验请求类型
        if (!requestContentTypeCheck(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        XStream xStream = new XStream();
        xStream.alias("Request", QueryActResReq.class);
        xStream.alias("Response", ActivityResultResp.class);
        xStream.autodetectAnnotations(true);

        try {
            String requestString = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            logger.info("充值请求参数{}-", requestString);
            req = (QueryActResReq) xStream.fromXML(requestString);

        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!crowdfundingInterfaceService.validateQueryActResReq(req)) {
            logger.error("存在请求参数为空.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Map<String, String> map = activitiesService.decryptActivityId(req.getQueryActResPojo().getActivityId(),
                req.getQueryActResPojo().getEnterpriseCode(), req.getQueryActResPojo().getEcProductCode());
        ActivityResultResp resp = null;
        if (map.get("code").equals("fail")) {
            logger.error("请求参数异常.");
            //resp = new ActivityResultResp();
            //ActivityResultPojo respPojo = new ActivityResultPojo();
            //resp.setDateTime(new DateTime().toString());
            //resp.setCode(CallbackResult.OTHERS.getCode());
            //resp.setMessage("请求参数异常");

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            //替换掉未解密的活动ID
            req.getQueryActResPojo().setActivityId(map.get("message"));
            logger.info("通过参数校验，查询众筹结果...");

            resp = crowdfundingInterfaceService.queryActivityResult(req.getQueryActResPojo());
        }

        //设置响应参数为200
        response.setStatus(HttpServletResponse.SC_OK);
        //封装参数
        logger.info("响应充值请求, resp = {}.", JSONObject.toJSONString(resp));
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("StreamUtils.copy IOException, {}" + e.getMessage());
        }
        return;
    }

    /**
     * 支付完成通知流量平台接口
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "notifyPayResult", method = RequestMethod.POST)
    @ResponseBody
    public void notifyPayResult(final HttpServletRequest request, final HttpServletResponse response) {
        //1、校验参数
        PaymentReq req = null;

        //所有请求都需要校验请求类型
        if (!requestContentTypeCheck(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        XStream xStream = new XStream();
        xStream.alias("Request", PaymentReq.class);
        xStream.alias("Response", PaymentResp.class);
        xStream.autodetectAnnotations(true);

        try {
            String requestString = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            logger.info("通知支付完成结果请求参数{}-", requestString);
            req = (PaymentReq) xStream.fromXML(requestString);

        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!crowdfundingInterfaceService.validatePaymentReq(req)) {
            logger.error("存在请求参数为空.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        Map<String, String> decryptMap = activitiesService.decryptActivityId(req.getPaymentPojo().getActivityId(), 
                req.getPaymentPojo().getEnterpriseCode(), req.getPaymentPojo().getEcProductCode());
        if(decryptMap.get("code").equals("fail")){
            logger.error("活动ID解密异常.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        req.getPaymentPojo().setActivityId(decryptMap.get("message"));
        
        logger.info("通过参数校验，更新报名用户支付状态...");
        
        PaymentResp resp = new PaymentResp();

        String systemSerial = SerialNumGenerator.buildSerialNum();

        Map<String, String> map = new HashMap();
        try {
            map = crowdfundingInterfaceService.notifyPayment(req.getPaymentPojo(), systemSerial);
            if (map.get("code").equals("fail")) {
                resp.setCode(CallbackResult.OTHERS.getCode());
            } else {
                resp.setCode(CallbackResult.SUCCESS.getCode());
            }
            resp.setMessage(map.get("msg"));

        } catch (RuntimeException e) {
            logger.error("更新支付信息失败，失败原因：" + e.getMessage());
            resp.setCode(CallbackResult.OTHERS.getCode());
            resp.setMessage("未知异常");
        }

        resp.setSystemNum(map.get("recordId"));
        resp.setDateTime((new DateTime()).toString());

        //设置响应参数为200
        response.setStatus(HttpServletResponse.SC_OK);
        //封装参数
        logger.info("响应支付通知请求, resp = {}.", JSONObject.toJSONString(resp));
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("StreamUtils.copy IOException, {}" + e.getMessage());
        }
        return;
    }

    /**
     * 充值接口
     *
     * @author qinqinyan
     */
    /*@RequestMapping(value = "charge", method = RequestMethod.POST)
    @ResponseBody
    public void charge(final HttpServletRequest request, final HttpServletResponse response) {
        //1. 校验参数
        CFChargeReq req = null;

        //所有请求都需要校验请求类型
        if (!requestContentTypeCheck(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        XStream xStream = new XStream();
        xStream.alias("Request", CFChargeReq.class);
        xStream.alias("Response", CFChargeResp.class);
        xStream.autodetectAnnotations(true);

        try {
            String requestString = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            logger.info("充值请求参数{}-", requestString);
            req = (CFChargeReq) xStream.fromXML(requestString);

        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!crowdfundingInterfaceService.validateCFChargeReq(req)) {
            logger.error("存在请求参数为空.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        logger.info("通过参数校验，调用充值...");
        //系统充值流水号
        CFChargeResp cfChargeResp = new CFChargeResp();
        CFChargeResultPojo cfChargeResultPojo = new CFChargeResultPojo();

        //校验支付状态，校验是否已经充值过
        Map<String, String> validateMap = crowdfundingInterfaceService
                .validateChargeRequirement(req.getCfChargePojo());
        //中奖纪录ID
        //String winRecordId = validateMap.get("winRecordId");
        if (validateMap.get("code").equals("fail")) {
            cfChargeResp.setCode(CallbackResult.OTHERS.getCode());
            cfChargeResp.setMessage(validateMap.get("msg"));

        } else {
            //充值
            try {
                if (crowdfundingInterfaceService.chargeForInterface(req.getCfChargePojo())) {
                    logger.info("成功插入充值队列,mobile={}, activityId={}", req.getCfChargePojo().getMobile(),
                            req.getCfChargePojo().getRecordId());
                    cfChargeResp.setCode(CallbackResult.SUCCESS.getCode());
                    cfChargeResp.setMessage("已成功发送充值请求");
                } else {
                    cfChargeResp.setCode(CallbackResult.OTHERS.getCode());
                    cfChargeResp.setMessage("发送充值请求失败");
                }
            } catch (RuntimeException e) {
                logger.error("充值异常，异常原因-{}", e.getMessage());
                cfChargeResp.setCode(CallbackResult.OTHERS.getCode());
                cfChargeResp.setMessage("发送充值请求失败");
            }
        }
        cfChargeResultPojo.setSystemNum(req.getCfChargePojo().getRecordId());
        cfChargeResultPojo.setSerialNum(req.getCfChargePojo().getSerialNum());
        cfChargeResp.setDateTime(new DateTime().toString());
        cfChargeResp.setCfChargeResultPojo(cfChargeResultPojo);

        //设置响应参数为200
        response.setStatus(HttpServletResponse.SC_OK);
        //封装参数
        logger.info("响应充值请求, cfChargeResp = {}.", JSONObject.toJSONString(cfChargeResp));
        try {
            StreamUtils.copy(xStream.toXML(cfChargeResp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("StreamUtils.copy IOException, {}" + e.getMessage());
        }
        return;
    }*/

    /**
     * 充值查询接口
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/{recordId}/queryChargeResult", method = RequestMethod.GET)
    @ResponseBody
    public void queryChargeResult(@PathVariable("recordId") String recordId,
                                  final HttpServletRequest request, final HttpServletResponse response) {
        //所有请求都需要校验请求类型
        //String type = request.getContentType();
        //System.out.println("request type -" + type);

        if (StringUtils.isBlank(recordId)) {
            logger.error("充值ID缺失");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        XStream xStream = new XStream();
        xStream.alias("Response", CFChargeResultResp.class);
        xStream.autodetectAnnotations(true);

        CFChargeResultResp resp = crowdfundingInterfaceService.queryCFChargeResult(recordId);
        if (resp == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //设置响应参数为200
        response.setStatus(HttpServletResponse.SC_OK);
        //封装参数
        logger.info("响应查询请求, resp = {}.", JSONObject.toJSONString(resp));
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("StreamUtils.copy IOException, {}" + e.getMessage());
        }
        return;
    }

    /**
     * 广东众筹报名结果查询
     *
     * @Title: queryJoinResult
     */
    @RequestMapping(value = "queryJoinResult", method = RequestMethod.POST)
    @ResponseBody
    public void queryJoinResult(final HttpServletRequest request, final HttpServletResponse response) {
        //1. 校验参数
        QueryJoinResReq req = null;

        //所有请求都需要校验请求类型
        if (!requestContentTypeCheck(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        XStream xStream = new XStream();
        xStream.alias("Request", QueryJoinResReq.class);
        xStream.alias("Response", QueryJoinResResp.class);
        xStream.autodetectAnnotations(true);

        try {
            String requestString = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            req = (QueryJoinResReq) xStream.fromXML(requestString);
        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        logger.info("收到众筹报名结果查询请求, request = {}.", JSONObject.toJSONString(req));


        if (!crowdfundingInterfaceService.validateQueryJoinResultRequest(req)) {
            logger.error("无效的众筹活动查询请求参数, QueryActivityReq = {}.", JSONObject.toJSONString(req));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        //业务处理（直接获取返回response对象）
        QueryJoinResResp resp = crowdfundingInterfaceService.queryJoinResult(req);
        //设置响应参数为200
        response.setStatus(HttpServletResponse.SC_OK);

        //封装参数             
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("StreamUtils.copy IOException");
            e.printStackTrace();
        }

        logger.info("响应众筹报名结果查询请求, Response = {}.", JSONObject.toJSONString(resp));
        return;
    }

}


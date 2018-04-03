package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.individual.AccountQueryResp;
import com.cmcc.vrp.ec.bean.individual.AccountQueryRespData;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketActivityParam;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketReq;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketResp;
import com.cmcc.vrp.ec.bean.individual.OrderRequest;
import com.cmcc.vrp.ec.bean.individual.OrderResponse;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualActivitiesService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.util.FormValidate;
import com.thoughtworks.xstream.XStream;


@RequestMapping("/redpacket")
@Controller
public class IndividualRedpacketController {
    private static final Logger logger = LoggerFactory.getLogger(IndividualRedpacketController.class);
    
    @Autowired
    AdministerService administerService;
    
    @Autowired
    IndividualAccountService individualAccountService;
    
    @Autowired
    IndividualProductService individualProductService;
    
    @Autowired
    IndividualFlowOrderService individualFlowOrderService;
    
    @Autowired
    IndividualActivitiesService individualActivitiesService;

    /**
     * @param mobile
     * @param outputStream
     */
    @RequestMapping(value = "{mobile}/query", method = RequestMethod.GET)
    public void query(@PathVariable("mobile") String mobile, OutputStream outputStream, HttpServletResponse response) {
        Administer admin = administerService.selectByMobilePhone(mobile);
        
        if(admin == null){ 
            logger.info("个人红包账户余额查询时用户不存在,mobile={}", mobile);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        IndividualProduct product = individualProductService.getDefaultFlowProduct(); 
        if(product == null){
            logger.info("个人红包账户余额查询时,默认流量产品不存在，mobile={}", mobile);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } 
        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(),
                IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if(account == null){
            logger.info("个人红包账户余额查询时,账户不存在, mobile={}", mobile);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } 
        
 
        AccountQueryResp resp = new AccountQueryResp();
        AccountQueryRespData user = new AccountQueryRespData();
        user.setAccount(account.getCount().intValue());
        user.setMobile(mobile);
        resp.setUser(user);
        response.setStatus(HttpServletResponse.SC_OK);
        
        writeQueryResp(resp, response);
    }

    //输出响应信息
    private void writeQueryResp(AccountQueryResp resp, HttpServletResponse response) {
        XStream xStream = new XStream();
        xStream.alias("Response", AccountQueryResp.class);
        xStream.autodetectAnnotations(true);
    
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("响应个人红包账户余额查询时出错，错误信息为{}.", e.toString());
        }
    }


    /** 
     * 红包订购接口
     * @Title: query 
     */
    @RequestMapping(value = "order", method = RequestMethod.POST)
    public void order(final HttpServletRequest request, final HttpServletResponse response) {
        //校验参数
        String type = request.getContentType();
        if (StringUtils.isBlank(type)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return ;
        }

        String contentType = request.getContentType();
        if (!(contentType.indexOf("application/xml") != -1 || contentType.indexOf("text/xml") != -1)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return ;
        }
        
        OrderRequest req = null;
        if ((req = retrieveOrderReq(request)) == null
                || StringUtils.isBlank(req.getSerialNum()) || StringUtils.isBlank(req.getMobile())
                || StringUtils.isBlank(req.getProductCode())) {
            logger.error("无效的充值请求参数,OrderRequest={}", JSONObject.toJSONString(req));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        logger.info("收到红包订购请求, OrderRequest = {}.", JSONObject.toJSONString(req));
        //红包订购
        OrderResponse resp = individualFlowOrderService.orderFlow(req.getMobile(), req.getProductCode(), req.getSerialNum());
        response.setStatus(HttpServletResponse.SC_OK);
        writeOrderResp(resp, response);
    }
    
    //获取订购请求参数
    private OrderRequest retrieveOrderReq(HttpServletRequest request) {
        XStream xStream = new XStream();
        xStream.alias("OrderRequest", OrderRequest.class);
        xStream.autodetectAnnotations(true);
        try {
            String reqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            return (OrderRequest) xStream.fromXML(reqStr);
        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            return null;
        }
    }

    //输出响应信息
    private void writeOrderResp(OrderResponse resp, HttpServletResponse response) {
        XStream xStream = new XStream();
        xStream.alias("OrderResponse", OrderResponse.class);
        xStream.autodetectAnnotations(true);
    
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("响应个人红包订购时出错，错误信息为{}.", e.toString());
        }
    }
    
    /** 
     * 创建红包接口
     * @Title: create 
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public void createRedpacket(final HttpServletRequest request, final HttpServletResponse response) {   
        //校验参数
        String type = request.getContentType();
        if (StringUtils.isBlank(type)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        String contentType = request.getContentType();
        if (!(contentType.indexOf("application/xml") != -1 || contentType.indexOf("text/xml") != -1)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }
        
        IndividualRedpacketReq req = null;
        if ((req = retrieveCreateReq(request)) == null
                || !validateCreateParam(req)) {
            logger.error("无效的充值请求参数,CreateRequest={}", JSONObject.toJSONString(req));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        logger.info("收到创建红包请求, IndividualRedpacketReq = {}.", JSONObject.toJSONString(req));
        
        //创建红包
        IndividualRedpacketResp resp = individualActivitiesService.generateFlowRedpacket(req);
        if(resp != null){
            response.setStatus(HttpServletResponse.SC_OK);
            writeCreateResp(resp, response);
        }else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    //获取创建红包请求参数
    private IndividualRedpacketReq retrieveCreateReq(HttpServletRequest request) {
        XStream xStream = new XStream();
        xStream.alias("CreateRequest", IndividualRedpacketReq.class);
        xStream.autodetectAnnotations(true);
       
        try {
            String reqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            
            reqStr = new String(reqStr.getBytes(request.getCharacterEncoding()), "UTF-8");
            
            return (IndividualRedpacketReq) xStream.fromXML(reqStr);
        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            return null;
        }
    }
    
    //校验充值请求参数
    private boolean validateCreateParam(IndividualRedpacketReq req) {
        if(StringUtils.isEmpty(req.getEcSerialNumber())){
            logger.info("创建红包接口，企业序列号为空");
            return false;
        }
        IndividualRedpacketActivityParam param = req.getParam();
        //随机红包，count需要小于size
        if(param.getType().equals(ActivityType.LUCKY_REDPACKET.getCode())){
            if(param.getCount()>param.getSize()){
                logger.info("创建红包接口，创建参数红包总数count>红包总大小size,count={},size={}", param.getCount(), param.getSize());
                return false;
            }
        }
        Pattern p = Pattern.compile(FormValidate.MOBILE_PHONE.getValidate_pattern());
        Matcher m = p.matcher(param.getMobile());
        if(StringUtils.isEmpty(param.getMobile()) || !m.matches()){
            logger.info("创建红包接口，非正确的手机号,mobile={}", param.getMobile());
            return false;
        }
        if(StringUtils.isEmpty(param.getActivityName()) || param.getActivityName().length()>50){
            logger.info("创建红包接口，活动名称为空或过长，activityName={}", param.getActivityName());
            return false;
        }
        if(param.getType() ==null || (param.getType().intValue() != 7 && param.getType().intValue() != 8 )){
            logger.info("创建红包接口，活动类型错误，type={}", param.getType());
            return false;
        }
        if(StringUtils.isEmpty(param.getStartTime()) || param.getSize()<=0
                || param.getCount()<=0){
            logger.info("创建红包接口，活动开始时间、产品总大小或红包总数量异常，startTime={},size={},count={}", 
                    param.getStartTime(), param.getSize(), param.getCount());
            return false;
        }
        if(StringUtils.isEmpty(param.getObject()) || param.getObject().length()>256){
            logger.info("创建红包接口，活动对象为空或过长，Object={}", param.getObject());
            return false;
        }
        if(StringUtils.isEmpty(param.getRules()) || param.getRules().length()>256){
            logger.info("创建红包接口，规则为空或过长，Rules={}", param.getRules());
            return false;
        }
 
        return true;
    }
    
    //输出响应信息
    private void writeCreateResp(IndividualRedpacketResp resp, HttpServletResponse response) {
        XStream xStream = new XStream();
        xStream.alias("CreateResponse", IndividualRedpacketResp.class);
        xStream.autodetectAnnotations(true);
    
        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("响应个人红包订购时出错，错误信息为{}.", e.toString());
        }
    }

}

package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.YqxReconcileStatus;
import com.cmcc.vrp.pay.enums.RefundType;
import com.cmcc.vrp.pay.enums.YqxRefundReturnType;
import com.cmcc.vrp.pay.model.Result;
import com.cmcc.vrp.pay.service.PayPlatformService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.YqxOrderRecord;
import com.cmcc.vrp.province.model.YqxPayReconcileRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.model.YqxRefundRecord;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.YqxOrderRecordService;
import com.cmcc.vrp.province.service.YqxPayRecordService;
import com.cmcc.vrp.province.service.YqxRefundRecordService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * 云企信后台管理controller
 * YqxManageController.java
 * @author wujiamin
 * @date 2017年5月18日
 */
@Controller
@RequestMapping("/manage/yqx/order")
public class YqxOrderManageController extends BaseController {
    private final Logger LOGGER = LoggerFactory.getLogger(YqxOrderManageController.class);
    
    @Autowired
    YqxOrderRecordService yqxOrderRecordService;
    
    @Autowired
    YqxPayRecordService yqxPayRecordService;
    
    @Autowired
    IndividualProductService individualProductService;
    
    @Autowired
    YqxRefundRecordService yqxRefundRecordService;
    
    @Autowired
    InterfaceRecordService interfaceRecordService;
    
    @Autowired
    PayPlatformService payPlatformService;
    
    /** 
     * 订单管理首页-订单列表
     * @Title: list 
     */
    @RequestMapping("list")
    public String orderlist(){
        return "yunqixin/manage/order_manage.ftl";
    }
    
    /**
     * 搜索订购列表
     * @param queryObject
     * @param res
     */
    @RequestMapping(value = "/searchOrderlist")
    public void searchOrderlist(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("orderSerialNum", queryObject);
        setQueryParameter("mobile", queryObject);
        setQueryParameter("searchTime", queryObject);
        
        Integer count = yqxOrderRecordService.countByMap(queryObject.toMap());
        List<YqxOrderRecord> list = yqxOrderRecordService.selectByMap(queryObject.toMap());
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /** 
     * 订单列表-订单详情
     * @Title: showOrderDetail 
     */
    @RequestMapping("showOrderDetail")
    public String showOrderDetail(String orderSerialNum, ModelMap map){
        YqxOrderRecord record = yqxOrderRecordService.selectBySerialNum(orderSerialNum);
        if(record == null){
            LOGGER.info("无法找到订购详情，serialNum={}", orderSerialNum);
            map.put("errorMsg", "无法找到订购详情");
            return "error.ftl";
        }
        map.put("record", record);
        IndividualProduct product = individualProductService.selectByPrimaryId(record.getIndividualProductId());
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,###.##");
        if(product.getProductSize()>=1024*1024){
            map.put("productName", myformat.format(product.getProductSize()/1024.0/1024.0) + "G");
        }else if(product.getProductSize()>=1024){
            map.put("productName", myformat.format(product.getProductSize()/1024.0) + "M");
        }else{
            map.put("productName", myformat.format(product.getProductSize()) + "K");
        }

        return "yunqixin/manage/order_detail.ftl";
    }
        
    /** 
     * 搜索支付记录列表
     * @Title: searchPayRecord 
     */
    @RequestMapping(value = "/searchPayRecord")
    public void searchPayRecord(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("orderSerialNum", queryObject);

        Integer count = yqxPayRecordService.countByMap(queryObject.toMap());
        List<YqxPayRecord> list = yqxPayRecordService.selectByMap(queryObject.toMap());
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    /** 
     * 异常订单
     * @Title: list 
     */
    @RequestMapping("errorlist")
    public String errorlist(){
        return "yunqixin/manage/error_list.ftl";
    }
    
    /** 
     * 搜索退款申请列表（异常订单中的退款申请列表）
     * @Title: searchRefundlist 
     */
    @RequestMapping(value = "/searchRefundlist")
    public void searchRefundlist(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("orderSerialNum", queryObject);
        setQueryParameter("refundStatus", queryObject);
        setQueryParameter("searchTime", queryObject);
        queryObject.getQueryCriterias().put("approvalRefund", 1);//标识用户申请过退款的订购记录

        Integer count = yqxOrderRecordService.countByMap(queryObject.toMap());
        List<YqxOrderRecord> list = yqxOrderRecordService.selectByMap(queryObject.toMap());
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    
    /** 
     * 搜索重复支付列表
     * @Title: searchRepeatPay 
     */
    @RequestMapping(value = "/searchRepeatPay")
    public void searchRepeatPay(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("orderSerialNum", queryObject);
        setQueryParameter("searchTime", queryObject);
        
        Integer count = yqxPayRecordService.countRepeatPayByMap(queryObject.toMap());
        List<YqxPayRecord> list = yqxPayRecordService.selectRepeatPayByMap(queryObject.toMap());
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    
    /** 
     * 下载退款列表
     * @Title: downloadRefundList 
     */
    @RequestMapping("/downloadRefundList")
    public void downloadRefundList(HttpServletRequest request, HttpServletResponse response, String orderSerialNum, 
            String refundStatus, String searchTime) {

        Map map = new HashMap();
        map.put("orderSerialNum", orderSerialNum);
        map.put("refundStatus", refundStatus);
        map.put("searchTime", searchTime);
        map.put("approvalRefund", 1);//标识用户申请过退款的订购记录

        List<YqxOrderRecord> list = yqxOrderRecordService.selectByMap(map);

        List<String> title = new ArrayList<String>();

        title.add("订单号");
        title.add("订单创建时间");
        title.add("手机号码");
        title.add("购买产品");
        title.add("支付宝流水号");
        title.add("充值状态");
        title.add("申请时间");
        title.add("退款状态");

        List<String> rowList = new ArrayList<String>();

        for (YqxOrderRecord module : list) {
            rowList.add("'" + module.getSerialNum());
            
            rowList.add(DateUtil.dateToString(module.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            
            rowList.add(module.getMobile());           
            
            if(module.getProductSize()!=null){
                DecimalFormat myformat = new DecimalFormat();
                myformat.applyPattern("##,###.##");
                if(module.getProductSize()>1024*1024){
                    rowList.add(myformat.format((module.getProductSize()/1024.0/1024.0)) + "G");
                }else if(module.getProductSize()>1024){
                    rowList.add(myformat.format((module.getProductSize()/1024.0)) + "M");
                }else{
                    rowList.add(myformat.format((module.getProductSize())) + "K");
                }
                
            }else{
                rowList.add("-");
            }            
            
            rowList.add(module.getDoneCode());
            
            if (module.getChargeStatus() == null){
                rowList.add("未充值");
            }else if (module.getChargeStatus().intValue() == 1) {
                rowList.add("未充值");
            }else if (module.getChargeStatus().intValue() == 2){
                rowList.add("已发送充值请求");
            }else if (module.getChargeStatus().intValue() == 3){
                rowList.add("充值成功");
            }else if (module.getChargeStatus().intValue() == 4){
                rowList.add("充值失败");
            }else{
                rowList.add("");
            }
            
            rowList.add(DateUtil.dateToString(module.getRefundApprovalTime(), "yyyy-MM-dd HH:mm:ss"));
            
            if (module.getRefundStatus().intValue() == 1) {
                rowList.add("受理中");
            }else if(module.getRefundStatus().intValue() == 2){
                rowList.add("退款中");
            }else if (module.getRefundStatus().intValue() == 3) {
                rowList.add("退款成功");
            }else if (module.getRefundStatus().intValue() == 4) {
                rowList.add("退款失败");
            }else {
                rowList.add("");
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "refundlist.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /** 
     * 下载重复支付记录列表
     * @Title: downloadRepeatPayList 
     */
    @RequestMapping("/downloadRepeatPayList")
    public void downloadRepeatPayList(HttpServletRequest request, HttpServletResponse response, String orderSerialNum, String searchTime) {

        Map map = new HashMap();
        map.put("orderSerialNum", orderSerialNum);
        map.put("searchTime", searchTime);

        List<YqxPayRecord> list = yqxPayRecordService.selectRepeatPayByMap(map);

        List<String> title = new ArrayList<String>();
        title.add("订单号");
        title.add("支付流水号");
        title.add("支付完成时间");
        title.add("支付状态");
        title.add("充值时间");
        title.add("充值状态");

        List<String> rowList = new ArrayList<String>();

        for (YqxPayRecord module : list) {
            rowList.add("'" + module.getOrderSerialNum());
            rowList.add(module.getDoneCode());
            rowList.add(DateUtil.dateToString(module.getResultReturnTime(), "yyyy-MM-dd HH:mm:ss"));
          
            if (module.getStatus().intValue() == 0) {
                rowList.add("支付成功");
            }else if (module.getStatus().intValue() == 1) {
                rowList.add("支付失败");
            }else if (module.getStatus().intValue() == 2) {
                rowList.add("等待支付(支付平台返回)");
            }else if (module.getStatus().intValue() == 3) {
                rowList.add("等待支付平台返回");
            }else{
                rowList.add("");
            }
            
            rowList.add(DateUtil.dateToString(module.getChargeTime(), "yyyy-MM-dd HH:mm:ss"));

            if (module.getChargeStatus() == null){
                rowList.add("未充值");
            }else if (module.getChargeStatus().intValue() == 1) {
                rowList.add("未充值");
            }else if (module.getChargeStatus().intValue() == 2){
                rowList.add("已发送充值请求");
            }else if (module.getChargeStatus().intValue() == 3){
                rowList.add("充值成功");
            }else if (module.getChargeStatus().intValue() == 4){
                rowList.add("充值失败");
            }else{
                rowList.add("");
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "repeatPayList.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    
    /**
     * 对账前端
     */
    @RequestMapping(value = "/payReconcileIndex")
    public String payReconcileIndex(ModelMap modelMap, QueryObject queryObject) {

        return "yunqixin/payReconcileIndex.ftl";
    }
    
    /**
     * 对账前端
     */
    @RequestMapping(value = "/payReconcileSearch")
    public void payReconcileSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("orderSerialNum", queryObject);
        setQueryParameter("mobile", queryObject);
        setQueryParameter("reconcileStatus", queryObject);
        setQueryParameter("searchTime", queryObject);
        
        int count = yqxPayRecordService.reconcileCountByMap(queryObject.getQueryCriterias());
        List<YqxPayReconcileRecord> list = yqxPayRecordService.reconcileSelectByMap(queryObject.toMap());
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 对账前端
     */
    @RequestMapping(value = "/payBillIndex")
    public String payBillIndex(ModelMap modelMap, QueryObject queryObject) {

        return "yunqixin/payBillIndex.ftl";
    }
    
    /**
     * 对账前端
     */
    @RequestMapping(value = "/payBillSearch")
    public void payBillSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("orderSerialNum", queryObject);
        setQueryParameter("mobile", queryObject);
        setQueryParameter("searchTime", queryObject);
        
        int count = yqxPayRecordService.payBillCountByMap(queryObject.getQueryCriterias());
        List<YqxPayReconcileRecord> list = yqxPayRecordService.payBillSelectByMap(queryObject.toMap());
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 下载
     * */
    @RequestMapping("downloadReconcileData")
    public void downloadReconcileData(HttpServletResponse response, String orderSerialNum,
                                         String mobile, String reconcileStatus,String searchTime){
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        if(!StringUtils.isEmpty(orderSerialNum)){
            paramsMap.put("orderSerialNum", orderSerialNum);
        }
        if(!StringUtils.isEmpty(mobile)){
            paramsMap.put("mobile", mobile);
        }
        if(!StringUtils.isEmpty(reconcileStatus)){
            paramsMap.put("reconcileStatus", reconcileStatus);
        }
        
        if(!StringUtils.isEmpty(searchTime)){
            String[] times = searchTime.split("~");
            String startTime = times[0];
            String endTime = times[1];
            startTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", startTime), "yyyy-MM-dd HH:mm");
            endTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", endTime), "yyyy-MM-dd HH:mm");
            paramsMap.put("startTime", startTime);
            paramsMap.put("endTime", endTime);
        }
        
        List<YqxPayReconcileRecord> recordList = yqxPayRecordService.reconcileSelectByMap(paramsMap);
        List<String> titles = new ArrayList<String>();
        titles.add("序号");
        titles.add("订单号");
        titles.add("订单创建时间");
        titles.add("支付流水号");
        titles.add("第三方支付流水号");
        titles.add("手机号码");
        titles.add("购买产品");
        titles.add("实付金额");
        titles.add("对账状态");
        List<String> rowList = new ArrayList<String>();
        
        for(int i=1;i<=recordList.size();i++){
            YqxPayReconcileRecord record = recordList.get(i-1);
            rowList.add(""+i);
            rowList.add("'" + record.getOrderSerialNum());
            rowList.add(new DateTime(record.getPayCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            rowList.add("'" + record.getPayTransactionId());
            if(!org.apache.commons.lang.StringUtils.isBlank(record.getDoneCode())){
                rowList.add("'" + record.getDoneCode());
            }else{
                rowList.add("");
            }
            rowList.add("'" + record.getMobile());
            rowList.add(record.getPrdName());
            if(record.getPrice() != null){
                rowList.add(""+record.getPrice().intValue()/100.0);
            }else{
                rowList.add("");
            }
            if(record.getReconcileStatus() ==null){
                rowList.add("");
            }else if(YqxReconcileStatus.NOSTART.getCode().equals(record.getReconcileStatus())){
                rowList.add("未对账");
            }else if(YqxReconcileStatus.SUCCESS.getCode().equals(record.getReconcileStatus())){
                rowList.add("成功");
            }else if(YqxReconcileStatus.FAILED.getCode().equals(record.getReconcileStatus())){
                rowList.add("失败");
            }
        }
        
        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, titles));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "platform_products.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * downloadBillData
     */
    @RequestMapping("downloadBillData")
    public void downloadBillData(HttpServletResponse response, String orderSerialNum,
                                         String mobile, String reconcileStatus,String searchTime){
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        if(!StringUtils.isEmpty(orderSerialNum)){
            paramsMap.put("orderSerialNum", orderSerialNum);
        }
        if(!StringUtils.isEmpty(mobile)){
            paramsMap.put("mobile", mobile);
        }
        
        if(!StringUtils.isEmpty(searchTime)){
            String[] times = searchTime.split("~");
            String startTime = times[0];
            String endTime = times[1];
            startTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", startTime), "yyyy-MM-dd HH:mm");
            endTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", endTime), "yyyy-MM-dd HH:mm");
            paramsMap.put("startTime", startTime);
            paramsMap.put("endTime", endTime);
        }
        
        List<YqxPayReconcileRecord> recordList = yqxPayRecordService.payBillSelectByMap(paramsMap);
        List<String> titles = new ArrayList<String>();
        titles.add("序号");
        titles.add("订单号");
        titles.add("订购创建时间");
        titles.add("第三方支付流水号");
        titles.add("支付金额");
        titles.add("支付时间");
        titles.add("手机号码");
        titles.add("购买产品");
        List<String> rowList = new ArrayList<String>();

        for(int i=1;i<=recordList.size();i++){
            YqxPayReconcileRecord record = recordList.get(i-1);
            rowList.add(""+i);
            if(org.apache.commons.lang.StringUtils.isBlank(record.getOrderSerialNum())){
                rowList.add("");
            }else{
                rowList.add("'" + record.getOrderSerialNum());
            }
            
            if(record.getPayCreateTime() == null){
                rowList.add("");
            }else{
                rowList.add(new DateTime(record.getPayCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            }
            
            rowList.add("'" + record.getDoneCode());
            if(record.getPrice() != null){
                rowList.add(""+record.getPrice().intValue()/100.0);
            }else{
                rowList.add("");
            }
            
            rowList.add(record.getPayTime());
            if(!org.apache.commons.lang.StringUtils.isBlank(record.getMobile())){
                rowList.add("'" + record.getMobile());
            }else{
                rowList.add("");
            }
            
            if(org.apache.commons.lang.StringUtils.isBlank(record.getPrdName())){
                rowList.add("");
            }else{
                rowList.add(record.getPrdName());
            } 
        }
        
        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, titles));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "platform_products.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    
    /**
     * 搜索退款记录（订单管理中的退款列表）
     * @param queryObject
     * @param res
     */
    @RequestMapping(value = "/searchRefundRecord")
    public void searchRefundRecord(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("orderSerialNum", queryObject);
        setQueryParameter("mobile", queryObject);
        setQueryParameter("searchTime", queryObject);
        
        Integer count = yqxRefundRecordService.queryPaginationRefundCount(queryObject);
        List<YqxRefundRecord> list = yqxRefundRecordService.queryPaginationRefundList(queryObject);
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    
    /** 
     * 退款列表-详情
     * @Title: showOrderDetail 
     */
    @RequestMapping("showRefundRecordDetail")
    public String showRefundRecordDetail(String refundSerialNum, ModelMap map){
        YqxRefundRecord refundRecord = yqxRefundRecordService.selectByRefundSerialNum(refundSerialNum);
        if(refundRecord == null){
            LOGGER.info("无法找到退款记录，refundSerialNum={}", refundSerialNum);
            map.put("errorMsg", "无法找到退款记录");
            return "error.ftl";
        }
        map.put("refundRecord", refundRecord);
        
        YqxOrderRecord orderRecord = yqxOrderRecordService.selectBySerialNum(refundRecord.getOrderSerialNum());
        if(orderRecord == null){
            LOGGER.info("无法找到订购记录，serialNum={}", refundRecord.getOrderSerialNum());
            map.put("errorMsg", "无法找到订购记录");
            return "error.ftl";
        }
        map.put("orderRecord", orderRecord);
        
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,###.##");

        IndividualProduct product = individualProductService.selectByPrimaryId(orderRecord.getIndividualProductId());
        if(product.getProductSize()>=1024*1024){
            map.put("productName", myformat.format(product.getProductSize()/1024.0/1024.0) + "G");
        }else if(product.getProductSize()>=1024){
            map.put("productName", myformat.format(product.getProductSize()/1024.0) + "M");
        }else{
            map.put("productName", myformat.format(product.getProductSize()) + "K");
        }

        YqxPayRecord payRecord = yqxPayRecordService.selectByDoneCode(refundRecord.getDoneCode());
        if(payRecord == null){
            LOGGER.info("无法找到支付记录，doneCode={}", refundRecord.getDoneCode());
            map.put("errorMsg", "无法找到支付记录");
            return "error.ftl";
        }
        map.put("payRecord", payRecord);
        
        return "yunqixin/manage/refund_detail.ftl";
    }


    @RequestMapping("orderService")
    public String orderService(String doneCode, ModelMap map){
        map.put("doneCode", doneCode);
        if(StringUtils.isEmpty(doneCode)){
            map.put("errorMsg", "支付流水号为空");
            return "error.ftl";
        }
        Administer admin = getCurrentUser();
        if(admin == null){
            map.put("errorMsg", "当前用户不存在");
            return "error.ftl";
        }
        map.put("administer", admin);
        return "yunqixin/manage/order_service.ftl";        
    }
    
    @RequestMapping("submitRefundProcessing")
    public String submitRefundProcessing(String doneCode, String reasonContent, ModelMap map){
        YqxPayRecord payRecord = yqxPayRecordService.selectByDoneCode(doneCode);
        if(payRecord == null || StringUtils.isEmpty(payRecord.getOrderSerialNum())){
            LOGGER.info("根据doneCode无法找到支付记录，doneCode={}", doneCode);
            map.put("errorMsg", "提交失败");
            return "redirect:orderService.html?doneCode=" + doneCode;
        }
        Result result = payPlatformService.yqxRefundProcess(doneCode, reasonContent, null, RefundType.BUSINESS, getCurrentAdminID());
        //Result result = new Result(YqxRefundReturnType.GENEURLFAILED);
        if(YqxRefundReturnType.ACCEPTED.getCode().equals(result.getCode())){
            LOGGER.info("退款操作http直接返回受理成功，等待退款回调：doneCode={}", doneCode);
        }else{
            LOGGER.info("退款操作http直接返回受理失败：doneCode={}", doneCode);
        } 
        return "redirect:list.html";//提交退款申请，返回订单列表页
    }
    
}

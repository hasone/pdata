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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.IndividualFlowOrder;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;

/**
 * 四川红包管理平台列表功能
 * ScredpacketManageController.java
 * @author wujiamin
 * @date 2017年4月20日
 */
@Controller
@RequestMapping("/manage/scredpacket")
public class ScredpacketManageController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScredpacketManageController.class);
    
    @Autowired
    IndividualFlowOrderService individualFlowOrderService;
    
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    
    /** 
     * 订购报表
     * @Title: orderlist 
     */
    @RequestMapping("orderlist")
    public String orderlist(){
        return "individual/flowRedpacket/manage/orderlist.ftl";        
    }
    
    /**
     * @param queryObject
     * @param res
     */
    @RequestMapping(value = "/searchOrderlist")
    public void searchOrderlist(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("mobile", queryObject); 
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        
        Integer count = individualFlowOrderService.countByMap(queryObject.toMap());
        List<IndividualFlowOrder> list = individualFlowOrderService.selectByMap(queryObject.toMap());
        
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
     * 下载订购报表
     * @Title: createOrderlistFile 
     */
    @RequestMapping("/createOrderlistFile")
    public void createOrderlistFile(HttpServletRequest request, HttpServletResponse response, String mobile, String startTime, String endTime) {
        Map map = new HashMap();

        map.put("mobile", mobile);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        List<IndividualFlowOrder> list = individualFlowOrderService.selectByMap(map);

        List<String> title = new ArrayList<String>();
        title.add("订购时间");
        title.add("到期时间");
        title.add("手机号");
        title.add("产品名称");
        title.add("产品大小");
        title.add("价格");
        title.add("状态");

        List<String> rowList = new ArrayList<String>();

        for (IndividualFlowOrder module : list) {
            rowList.add(DateUtil.dateToString(module.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            rowList.add(DateUtil.dateToString(module.getExpireTime(), "yyyy-MM-dd HH:mm:ss"));
            rowList.add(module.getMobile());
            rowList.add(module.getProductName());
            DecimalFormat format = new DecimalFormat("#.##");
            rowList.add(format.format(new Double(module.getProductSize()/1024.0)));            
            rowList.add(format.format(new Double(module.getPrice().doubleValue()/100.0)));            
            rowList.add(module.getErrorMsg());

        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "orderlist.csv");
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
     * 跳转到某个订购的详情页
     * @Title: showOrderDetail 
     */
    @RequestMapping(value = "/showOrderDetail")
    public String showOrderDetail(String orderSystemNum, ModelMap map) {
        map.put("orderSystemNum", orderSystemNum);
        return "individual/flowRedpacket/manage/orderDetailList.ftl";
    }
    
    /** 
     * 搜索订购详情列表
     * @Title: searchOrderDetail 
     */
    @RequestMapping(value = "/searchOrderDetail")
    public void searchOrderDetail(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("chargeMobile", queryObject);        
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        setQueryParameter("orderSystemNum", queryObject);
        
        //流量币账户详情
        Integer count = activityWinRecordService.countIndividualFlowRedpacketList(queryObject.toMap());
        List<ActivityWinRecord> list = activityWinRecordService.selectIndividualFlowRedpacketList(queryObject.toMap());
        
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
     * 下载订购详情列表
     * @Title: createOrderDetailListFile 
     */
    @RequestMapping("/createOrderDetailListFile")
    public void createOrderDetailListFile(HttpServletRequest request, HttpServletResponse response, String chargeMobile, String orderSystemNum,
            String startTime, String endTime) {
        Map map = new HashMap();

        map.put("chargeMobile", chargeMobile);
        map.put("orderSystemNum", orderSystemNum);        
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        List<ActivityWinRecord> list = activityWinRecordService.selectIndividualFlowRedpacketList(map);

        List<String> title = new ArrayList<String>();
        title.add("抢红包时间");
        title.add("手机号");
        title.add("产品大小");
        title.add("状态");

        List<String> rowList = new ArrayList<String>();

        for (ActivityWinRecord module : list) {
            rowList.add(DateUtil.dateToString(module.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            rowList.add(module.getChargeMobile());
            rowList.add(module.getSize());
            if (module.getStatus().equals(1)) {
                rowList.add("待充值");
            }else if (module.getStatus().equals(2)) {
                rowList.add("已发送充值请求");
            }else{
                rowList.add(module.getReason());
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "orderDetailList.csv");
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
     * 抢红包列表
     * @Title: grablist 
     */
    @RequestMapping("grablist")
    public String grablist(){
        return "individual/flowRedpacket/manage/grablist.ftl";        
    }

    /** 
     * 搜索抢红包列表
     * @Title: searchGrabList 
     */
    @RequestMapping(value = "/searchGrabList")
    public void searchGrabList(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("chargeMobile", queryObject);        
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        
        //流量币账户详情
        Integer count = activityWinRecordService.countIndividualFlowRedpacketList(queryObject.toMap());
        List<ActivityWinRecord> list = activityWinRecordService.selectIndividualFlowRedpacketList(queryObject.toMap());
        
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
     * 下载抢红包列表
     * @Title: createGrabListFile 
     */
    @RequestMapping("/createGrabListFile")
    public void createGrabListFile(HttpServletRequest request, HttpServletResponse response, String mobile, String startTime, String endTime) {
        Map map = new HashMap();

        map.put("chargeMobile", mobile);      
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        List<ActivityWinRecord> list = activityWinRecordService.selectIndividualFlowRedpacketList(map);

        List<String> title = new ArrayList<String>();
        title.add("抢红包时间");
        title.add("发红包手机号");
        title.add("抢红包手机号");
        title.add("产品大小");
        title.add("状态");

        List<String> rowList = new ArrayList<String>();

        for (ActivityWinRecord module : list) {
            rowList.add(DateUtil.dateToString(module.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            rowList.add(module.getOrderMobile());
            rowList.add(module.getChargeMobile());
            rowList.add(module.getSize());                       
            if (module.getStatus().equals(1)) {
                rowList.add("待充值");
            }else if (module.getStatus().equals(2)) {
                rowList.add("已发送充值请求");
            }else{
                rowList.add(module.getReason());
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "orderDetailList.csv");
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


}

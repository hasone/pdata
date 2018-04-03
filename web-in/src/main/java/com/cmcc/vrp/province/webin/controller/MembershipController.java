package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.module.Membership;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.wx.WxExchangeRecordService;
import com.cmcc.vrp.wx.model.WxExchangeRecord;

/**
 * 广东众筹平台，会员列表功能
 * MembershipController.java
 * @author wujiamin
 * @date 2017年4月11日
 */
@Controller
@RequestMapping("/manage/membership")
public class MembershipController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipController.class);
    
    @Autowired
    IndividualAccountService individualAccountService;
    
    @Autowired
    WxExchangeRecordService wxExchangeRecordService;
    
    @Autowired
    IndividualAccountRecordService individualAccountRecordService;
    
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    
    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    
    @Autowired
    ActivitiesService activitiesService;
    
    /** 
     * 会员列表首页
     * @Title: index 
     */
    @RequestMapping("index")
    public String index(QueryObject queryObject, ModelMap modelMap){
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "membership/index.ftl";        
    }
    
    /**
     * @param queryObject
     * @param res
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("mobile", queryObject);
        setQueryParameter("openid", queryObject);
        setQueryParameter("nickname", queryObject);
        
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        
        Integer count = individualAccountService.countMembershipList(queryObject.toMap());
        List<Membership> list = individualAccountService.getMembershipList(queryObject.toMap());
        
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
     * 下载会员列表
     * @Title: creatMembershipListFile 
     */
    @RequestMapping("/createMembershipListFile")
    public void createMembershipListFile(HttpServletRequest request, HttpServletResponse response, String mobile, String openid,
                                    String nickname, String startTime, String endTime) {
        Map map = new HashMap();
        map.put("mobile", mobile);
        map.put("openid", openid);
        map.put("nickname", nickname);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        List<Membership> list = individualAccountService.getMembershipList(map);

        List<String> title = new ArrayList<String>();
        title.add("手机号");
        title.add("流量币");
        title.add("累积流量币");
        title.add("等级");
        title.add("创建时间");

        List<String> rowList = new ArrayList<String>();

        for (Membership module : list) {
            rowList.add(module.getMobile());

            if (module.getCount() != null) {
                rowList.add(module.getCount().toString());
            } else {
                rowList.add("0");
            }

            if (module.getAccumulateCount() != null) {
                rowList.add(module.getAccumulateCount().toString());
            } else {
                rowList.add("0");
            }
            
            rowList.add(module.getGrade());
            
            rowList.add(DateUtil.dateToString(module.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));

        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "membership.csv");
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
     * 跳转到某个会员的详情页
     * @Title: detail 
     */
    @RequestMapping(value = "/detail")
    public String detail(String mobile, ModelMap map) {
        map.put("ownerMobile", mobile);
        return "membership/detail.ftl";
    }
    
    /** 
     * 搜索流量币详情列表
     * @Title: searchFlowcoinDetail 
     */
    @RequestMapping(value = "/searchFlowcoinDetail")
    public void searchFlowcoinDetail(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("mobile", queryObject);
        setQueryParameter("openid", queryObject);
        setQueryParameter("nickname", queryObject);
        
        setQueryParameter("flowcoinStartTime", queryObject);
        setQueryParameter("flowcoinEndTime", queryObject);
        String flowcoinEndTime = (String) queryObject.getQueryCriterias().get("flowcoinEndTime");
        if(!StringUtils.isEmpty(flowcoinEndTime)){
            queryObject.getQueryCriterias().put("flowcoinEndTime", flowcoinEndTime + " 23:59:59");
        }
        
        //流量币账户详情
        Integer count = individualAccountRecordService.countDetailRecordByMap(queryObject.toMap());
        List<IndividualAccountRecord> list = individualAccountRecordService.selectDetailRecordByMap(queryObject.toMap());
        
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
     * 搜索众筹详情列表
     * @Title: searchCrowfundingDetail 
     */
    @RequestMapping(value = "/searchCrowdfundingDetail")
    public void searchCrowfundingDetail(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("mobile", queryObject);
        setQueryParameter("openid", queryObject);
        setQueryParameter("nickname", queryObject);
        
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        String endTime = (String) queryObject.getQueryCriterias().get("endTime");
        if(!StringUtils.isEmpty(endTime)){
            queryObject.getQueryCriterias().put("endTime", endTime + " 23:59:59");
        }
        setQueryParameter("payStartTime", queryObject);        
        setQueryParameter("payEndTime", queryObject);
        String payEndTime = (String) queryObject.getQueryCriterias().get("payEndTime");
        if(!StringUtils.isEmpty(payEndTime)){
            queryObject.getQueryCriterias().put("payEndTime", payEndTime + " 23:59:59");
        }
        queryObject.getQueryCriterias().put("activityType", ActivityType.CROWD_FUNDING.getCode());
        
        //众筹列表
        Integer count = activityWinRecordService.countWinRecords(queryObject.toMap());
        List<ActivityWinRecord> list = activityWinRecordService.showWinRecords(queryObject.toMap());
        for(ActivityWinRecord record: list){
            CrowdfundingActivityDetail detail = crowdfundingActivityDetailService.selectByActivityId(record.getActivityId());            
            if(detail.getResult().equals(1)){
                if(ChargeRecordStatus.COMPLETE.getCode().equals(record.getStatus())){
                    record.setCrowdfundingStatus("充值成功");
                }else if(ChargeRecordStatus.FAILED.getCode().equals(record.getStatus())){
                    record.setCrowdfundingStatus("充值失败");
                }else if(ChargeRecordStatus.PROCESSING.getCode().equals(record.getStatus())){
                    record.setCrowdfundingStatus("已发送充值请求");
                }else{
                    record.setCrowdfundingStatus("待充值");
                }                
            }else{//大企业版，众筹没成功，如果活动还在进行中状态，则为已支付状态
                Activities activity = activitiesService.selectByActivityId(record.getActivityId());
                if(ActivityStatus.PROCESSING.getCode().equals(activity.getStatus())){
                    record.setCrowdfundingStatus("已支付");
                }else{
                    record.setCrowdfundingStatus("众筹失败");
                }
            }            
        }
        
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
     * 下载详情列表
     * @Title: createDetailFile 
     */
    @RequestMapping("/createDetailFile")
    public void createDetailFile(HttpServletRequest request, HttpServletResponse response, String mobile, String payStartTime, String payEndTime,
            String startTime, String endTime, String flowcoinStartTime, String flowcoinEndTime,  String activityType) {
       
        Map map = new HashMap();
        map.put("mobile", mobile);
        map.put("payStartTime", payStartTime);
        if(!StringUtils.isEmpty(payEndTime)){
            map.put("payEndTime", payEndTime + " 23:59:59");
        }        
        map.put("flowcoinStartTime", flowcoinStartTime);
        if(!StringUtils.isEmpty(flowcoinEndTime)){
            map.put("flowcoinEndTime", flowcoinEndTime + " 23:59:59");
        }
        map.put("startTime", startTime);
        if(!StringUtils.isEmpty(endTime)){
            map.put("endTime", endTime + " 23:59:59");
        }
        map.put("activityType", ActivityType.CROWD_FUNDING.getCode().toString());
        
        List<String> title = new ArrayList<String>();
        List<String> rowList = new ArrayList<String>();
        
        String fileName="";
        if("1".equals(activityType)){//前端页面定义：1-众筹；2-流量币
            createCrowdfundingFile(map, response, title, rowList);
            fileName="crowdfunding.csv";
        }
        if("2".equals(activityType)){
            createFlowcoinFile(map, response, title, rowList);
            fileName="flowcoin.csv";
        }
        
        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
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
     * 创建流量币列表文件
     * @Title: createFlowcoinFile 
     */
    private void createFlowcoinFile(Map map, HttpServletResponse response, List<String> title, List<String> rowList) {
        List<IndividualAccountRecord> list = individualAccountRecordService.selectDetailRecordByMap(map);

        title.add("时间");
        title.add("详情");
        title.add("收入");
        title.add("支出");

        for (IndividualAccountRecord module : list) {
            rowList.add(DateUtil.dateToString(module.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            
            rowList.add(module.getDescription());
            
            DecimalFormat myformat = new DecimalFormat();
            myformat.applyPattern("##,###.##");
            if(new Integer((int)AccountRecordType.INCOME.getValue()).equals(module.getType())){
                NumberFormat nf = java.text.NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                rowList.add(nf.format(module.getCount()));
                rowList.add("");
            }else{
                rowList.add("");
                NumberFormat nf = java.text.NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                rowList.add(nf.format(module.getCount()));
            }
        }

    }

    /** 
     * 创建众筹列表文件
     * @Title: createCrowdfundingFile 
     */
    private void createCrowdfundingFile(Map map, HttpServletResponse response, List<String> title, List<String> rowList) {
        List<ActivityWinRecord> list = activityWinRecordService.showWinRecords(map);

        title.add("参加时间");
        title.add("支付时间");
        title.add("企业名称");
        title.add("活动名称");
        title.add("产品名称");
        title.add("产品价格");
        title.add("状态");
        
        for (ActivityWinRecord module : list) {
            rowList.add(DateUtil.dateToString(module.getWinTime(), "yyyy-MM-dd HH:mm:ss"));
            
            if(module.getPaymentTime()!=null){
                rowList.add(DateUtil.dateToString(module.getPaymentTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                rowList.add("");
            }
            
            rowList.add(module.getEntName());
            
            rowList.add(module.getActivityName());
            
            rowList.add(module.getProductName());
            
            Double price =  (module.getPrice()*module.getDiscount()/(double)10000);
            DecimalFormat myformat = new DecimalFormat();
            myformat.applyPattern("##,###.##");
            rowList.add(myformat.format(price) + "元");

            ChargeRecordStatus status = null;

            CrowdfundingActivityDetail detail = crowdfundingActivityDetailService.selectByActivityId(module.getActivityId());            
            if(detail.getResult().equals(1)){
                if(ChargeRecordStatus.COMPLETE.getCode().equals(module.getStatus())){
                    rowList.add("充值成功");
                }else if(ChargeRecordStatus.FAILED.getCode().equals(module.getStatus())){
                    rowList.add("充值失败");
                }else if(ChargeRecordStatus.PROCESSING.getCode().equals(module.getStatus())){
                    rowList.add("已发送充值请求");
                }else{
                    rowList.add("待充值");
                }                
            }else{//大企业版，众筹没成功，如果活动还在进行中状态，则为已支付状态
                Activities activity = activitiesService.selectByActivityId(module.getActivityId());
                if(ActivityStatus.PROCESSING.getCode().equals(activity.getStatus())){
                    rowList.add("已支付");
                }else{
                    rowList.add("众筹失败");
                }
            }       

            /*if (module.getStatus() != null && (status = ChargeRecordStatus.fromValue(module.getStatus())) != null) {
                rowList.add(status.getMessage());
            } else {
                rowList.add("");
            }*/
        }

    }

    /** 
     * 兑换报表
     * @Title: exchangeList 
     */
    @RequestMapping(value = "/exchangelist")
    public String exchangeList(String mobile, ModelMap map) {
        return "membership/exchangelist.ftl";
    }
    
    /** 
     * 搜索兑换报表的详细内容
     * @Title: searchExchangeList 
     */
    @RequestMapping(value = "/searchExchangeList")
    public void searchExchangeList(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        setQueryParameter("mobile", queryObject);
        setQueryParameter("openid", queryObject);
        setQueryParameter("nickname", queryObject);
        
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        
        Integer count = wxExchangeRecordService.countByMap(queryObject.toMap());
        List<WxExchangeRecord> list = wxExchangeRecordService.selectByMap(queryObject.toMap());
        
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
     * 下载兑换列表
     * @Title: createExchangeListFile 
     */
    @RequestMapping("/createExchangeListFile")
    public void createExchangeListFile(HttpServletRequest request, HttpServletResponse response, String mobile, String openid,
                                    String nickname, String startTime, String endTime) {

        Map map = new HashMap();
        map.put("mobile", mobile);
        map.put("openid", openid);
        map.put("nickname", nickname);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        List<WxExchangeRecord> list = wxExchangeRecordService.selectByMap(map);
        
        List<String> title = new ArrayList<String>();
        title.add("手机号");
        title.add("充值手机号");
        title.add("产品名称");
        title.add("产品大小");
        title.add("消耗流量币");
        title.add("兑换时间");
        title.add("状态");

        List<String> rowList = new ArrayList<String>();

        for (WxExchangeRecord module : list) {
            rowList.add(module.getOwnerMobile());
            
            rowList.add(module.getMobile());
            
            rowList.add(module.getProductName());           
            
            if(module.getProductSize()!=null){
                rowList.add(module.getProductSize().toString());
            }else{
                rowList.add("0");
            }            
           
            if(module.getCount()!=null){
                rowList.add(module.getCount().toString());
            }else{
                rowList.add("0");
            }
            
            rowList.add(DateUtil.dateToString(module.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            
            ChargeRecordStatus status = null;
            if (module.getStatus() != null && (status = ChargeRecordStatus.fromValue(module.getStatus())) != null) {
                rowList.add(status.getMessage());
            } else {
                rowList.add("");
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "exchange.csv");
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

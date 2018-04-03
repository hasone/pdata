package com.cmcc.vrp.weixin.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.CrowdFundingService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.CrowdfundingQueryUrlService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.EntUserQueryService;
import com.cmcc.vrp.wx.PaymentService;
import com.cmcc.vrp.wx.UserCheckService;
import com.cmcc.vrp.wx.beans.PayParameter;
import com.cmcc.vrp.wx.enums.District;
import com.cmcc.vrp.wx.impl.PaymentServiceImpl;

/**
 * ZhongchouActivityController.java
 * 
 * @author wujiamin
 * @date 2017年1月6日
 */
@Controller
@RequestMapping("/wx/zhongchou")
public class ZhongchouActivityController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ZhongchouActivityController.class);
    @Autowired
    ActivitiesService activitiesService;
    
    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    
    @Autowired
    ActivityPrizeService activityPrizeService;
    
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    
    @Autowired
    ProductService productService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    ActivityPaymentInfoService activityPaymentInfoService;
    
    @Autowired
    UserCheckService userCheckService;
    
    @Autowired
    WxAdministerService wxAdministerService;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    PaymentService paymentService;

    @Autowired
    S3Service s3Service;
    
    @Autowired
    ManagerService managerService;

    @Autowired
    CrowdfundingQueryUrlService crowdfundingQueryUrlService;
    
    @Autowired
    EntUserQueryService entUserQueryService;
    
    @Autowired
    CrowdFundingService crowdFundingService;
    
    /**
     * 进入众筹活动，活动列表页面
     * 
     * @Title: playList
     */
    @RequestMapping("/list")
    public String playList(HttpServletRequest request, ModelMap map) {
        String wxAspect = (String) request.getAttribute("wxAspect");
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            WxAdminister admin = getWxCurrentUser();
            
            if(admin == null){
                logger.info("session中adminId为空");
                return "gdzc/404.ftl";
            }else{
                return "gdzc/activityList.ftl";
            }
        }else{
            return wxAspect;
        }
        
    }
    
    /** 
     * 进入众筹活动列表，查询当前用户的支付状态
     * @Title: queryPayResult 
     */
    @RequestMapping("/queryPayResult")
    public void queryPayResult(HttpServletResponse res) {
        String currentMobile = getMobile();
        Map result = new HashMap();
        String activityId = crowdFundingService.queryPayResult(currentMobile);

        if(activityId != null){
            result.put("payActivityId", activityId);
        }else{
            result.put("payActivityId", "null");
        }
        try {            
            res.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 再次支付及取消订单使用，将所有支付中状态的支付记录设置为关闭状态
     * @Title: queryPayResult 
     */
    @RequestMapping("/processPayingRecord")
    public void processPayingRecord(String payActivityId, String type, HttpServletResponse res) {
        String currentMobile = getMobile();
        logger.info("用户mobile={}对活动activityId={}进行支付{}操作。", currentMobile, payActivityId, type);
        Map result = new HashMap();
        result.put("result", false);
        if(!StringUtils.isEmpty(payActivityId)){
            result.put("result", crowdFundingService.processPayingRecord(payActivityId, currentMobile));
        }

        try {            
            res.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 进入众筹活动，获取企业logo或活动banner
     * @throws IOException 
     */
    @RequestMapping("/{type}/{activityId}")
    public void getLogo(HttpServletRequest request, HttpServletResponse response, 
            @PathVariable String type, @PathVariable String activityId) throws IOException {
        //设置不缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);      
        CrowdfundingActivityDetail detail = crowdfundingActivityDetailService.selectByActivityId(activityId);

        if("logo".equals(type)){
            s3Service.downloadFromS3(response, detail.getLogoKey(), detail.getLogo(), request);
        }else if("banner".equals(type)){
            s3Service.downloadFromS3(response, detail.getBannerKey(), detail.getBanner(), request);
        }     
    }
   
   
    /**
     * 进入众筹活动，下拉及搜索
     * 
     * @Title: playDownSearch
     */
    @RequestMapping("/allSearch")
    public void playAllSearch(QueryObject queryObject, int pageSize, int pageNo, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 分页转换
         */
        queryObject.setPageNum(pageNo);
        queryObject.setPageSize(pageSize);
        
        /**
         * 查询参数: 活动名称
         */
        setQueryParameter("activityName", queryObject);
        
        String status = ActivityStatus.PROCESSING.getCode() + "," + ActivityStatus.ON.getCode();
        queryObject.getQueryCriterias().put("status", status);
        
        queryObject.getQueryCriterias().put("type", ActivityType.CROWD_FUNDING.getCode());
        
        queryObject.getQueryCriterias().put("mobile", getMobile());
        
        queryObject.getQueryCriterias().put("joinType", 2);
        
        List<Activities> activities = activitiesService.wxCrowdfundingActivityList(queryObject.toMap());
       
        try {
            Map result = new HashMap();
            result.put("data", activities);
            res.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    /**
     * 进入众筹活动，下拉及搜索，我的众筹
     * 
     * @Title: playDownSearch
     */
    @RequestMapping("/mySearch")
    public void playMySearch(QueryObject queryObject,  int pageSize, int pageNo, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        /**
         * 分页转换
         */
        queryObject.setPageNum(pageNo);
        queryObject.setPageSize(pageSize);
        
        /**
         * 查询参数: 活动名称
         */
        setQueryParameter("activityName", queryObject);
        queryObject.getQueryCriterias().put("type", ActivityType.CROWD_FUNDING.getCode());
        queryObject.getQueryCriterias().put("ownMobile", getMobile());
        queryObject.getQueryCriterias().put("joinType", 2);

        List<Activities> activities = activitiesService.myCrowdfundingActivityList(queryObject.toMap());

        try {
            Map result = new HashMap();
            result.put("data", activities);
            res.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入众筹活动，活动详情
     * 
     * @Title: playDownSearch
     */
    @RequestMapping("/activityDetail")
    public String playActivityDetail(String activityId, ModelMap map) {
        List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityId(activityId);
        CrowdfundingActivityDetail detail = crowdfundingActivityDetailService.selectByActivityId(activityId);
        Activities activity = activitiesService.selectByActivityId(activityId);
        if(activityPrizes==null || activityPrizes.size()<=0 || detail == null || activity == null){
            return "gdzc/404.ftl";
        }
        map.put("activityPrizes", activityPrizes);
        map.put("activityDetail", detail);
        map.put("activity", activity);
        Double percent = new Double(detail.getCurrentCount())/new Double(detail.getTargetCount());
        if(percent.doubleValue()>1d){
            percent = 1d;
        }
        DecimalFormat df = new DecimalFormat("#.##%");  
        map.put("percent", df.format(percent));

        map.put("prizeId", activityPrizes.get(0).getId());
        
        //活动对于当前用户的状态（已参加未支付，已支付，已充值。。。。返回相应的按钮状态和生效状态）立即参加、立即支付、、正在支付、已支付、已结束、未开始、已参加
        //1、判断该用户是否已参加该活动
        Map searchMap = new HashMap();
        searchMap.put("ownMobile", getMobile());
        searchMap.put("activityId", activityId);
        List<ActivityWinRecord> activityWinRecord = activityWinRecordService.selectByMap(searchMap);
        //未支付用户显示当月生效和次月生效
        if(detail.getChargeType().equals(1)){
            map.put("chargeType", "当月生效");
        }
        if(detail.getChargeType().equals(2)){
            map.put("chargeType", "次月生效");
        }
        if(activityWinRecord==null || activityWinRecord.size()==0){
            if(ActivityStatus.DOWN.getCode().equals(activity.getStatus()) || ActivityStatus.END.getCode().equals(activity.getStatus())){
                map.put("buttonStatus", "ended");
            }else if(!ActivityStatus.PROCESSING.getCode().equals(activity.getStatus())){
                map.put("buttonStatus", "notBegin");
            }else if(detail.getCurrentCount()>=detail.getTargetCount()){
                map.put("buttonStatus", "pay");
            }else {
                map.put("buttonStatus", "join");
            }
        }else{
            if(activityWinRecord.get(0).getPayResult().equals(2)){
                map.put("buttonStatus", "payed");
                //已支付的用户显示充值时间
                map.put("chargeType", DateUtil.dateToString(activityWinRecord.get(0).getChargeTime(), "yyyy-MM-dd HH:mm:ss"));
            }else if(activityWinRecord.get(0).getPayResult().equals(1)){
                map.put("buttonStatus", "paying");
            }else{
                if(ActivityStatus.DOWN.getCode().equals(activity.getStatus()) || ActivityStatus.END.getCode().equals(activity.getStatus())){
                    map.put("buttonStatus", "ended");
                }else if((detail.getCurrentCount()>=detail.getTargetCount()) && (activityWinRecord.get(0).getPayResult().equals(4)
                        || activityWinRecord.get(0).getPayResult().equals(3) || activityWinRecord.get(0).getPayResult().equals(0))){
                    //已到达目标人数且中奖记录中的支付状态为退款成功、支付失败、未支付，都可以进行支付
                    map.put("buttonStatus", "pay");
                }else if(activityWinRecord.get(0).getPayResult().equals(6)){//中奖纪录中支付状态为“未知异常”，页面按钮文案为“未知异常”
                    map.put("buttonStatus", "error");
                }else{
                    map.put("buttonStatus", "joined");
                }               
            }
            //已参加过的用户
            map.put("prizeId", activityWinRecord.get(0).getPrizeId());
            map.put("activityWinRecordId", activityWinRecord.get(0).getRecordId());
        }

        Date now = new Date();
        long l = activity.getEndTime().getTime() - now.getTime();
        double day = Math.ceil(new Double(l/(double)(24*60*60*1000)));
        if(day < 0){
            day = 0;
        }
        map.put("leftDays", day);
        
        return "gdzc/activityDetail.ftl";
    }
    
    /** 
     * 参加众筹活动
     * @throws IOException 
     * @Title: join 
     */
    @RequestMapping("/join")
    public void join(String activityId, Long prizeId, HttpServletResponse res) throws IOException{
        Map result = new HashMap();
        //1、校验activityId，prizeId
        if(StringUtils.isEmpty(activityId) || prizeId == null){
            logger.info("参数异常,activityId={}, prizeId={}", activityId, prizeId);
            result.put("result", "fail");
            res.getWriter().write(JSONObject.toJSONString(result));
            return;
        }

        //校验用户是否可以参加该活动
        if(!checkUser(activityId)){
            result.put("result", "fail");
            result.put("blackAndWhite", "fail");
            res.getWriter().write(JSONObject.toJSONString(result));
            return;
        }
        
        //2、判断该用户是否已参加该活动
        Map map = new HashMap();
        map.put("ownMobile", getMobile());
        map.put("activityId", activityId);
        List<ActivityWinRecord> activityWinRecord = activityWinRecordService.selectByMap(map);
        if(activityWinRecord!=null && activityWinRecord.size()>0){
            logger.info("用户已经参加该活动,mobile={}, activityId={}", getMobile(), activityId);
            result.put("result", "fail");
            res.getWriter().write(JSONObject.toJSONString(result));
            return;
        }
       
        //3、参加活动（插入activityWinRecord表，增加当前活动参与人数值）
        try{
            if(activitiesService.joinCrowdfundingActivity(activityId, getMobile(), prizeId, getWxOpenid())){                
                activityWinRecord = activityWinRecordService.selectByMap(map);
                if(activityWinRecord!=null && activityWinRecord.size() == 1){
                    result.put("result", "success");
                    result.put("activityWinRecordId", activityWinRecord.get(0).getRecordId());
                    res.getWriter().write(JSONObject.toJSONString(result));
                    return;
                }                
            }
        }catch(Exception e){
            logger.info("用户参加众筹活动失败，异常原因：{}", e.getMessage());
            result.put("result", "fail");
            res.getWriter().write(JSONObject.toJSONString(result));
            return;
        }
        
        result.put("result", "fail");
        res.getWriter().write(JSONObject.toJSONString(result));
        return;
    }
    
    /** 
     * 支付众筹活动，跳转到支付页面
     * @Title: pay 
     */
    @RequestMapping("/pay")
    public String pay(String activityWinRecordId, ModelMap map){
        ActivityWinRecord record = activityWinRecordService.selectByRecordId(activityWinRecordId);        
        /**
         * 获取pay页面详情
         */                        
        if(record == null){
            logger.info("activityWinRecord为空,recordId={}", activityWinRecordId);
            return "gdzc/404.ftl";
        }
        
        //检查当前用户是否是活动中奖用户，避免分享操作引起问题
        if(!record.getOwnMobile().equals(getMobile())){
            logger.info("activityWinRecord的ownMobile={}不等于当前用户mobile={}", record.getOwnMobile(), getMobile());
            return "redirect:list.html";
        }        
        

        //校验用户是否可以参加该活动
        if(!checkUser(record.getActivityId())){
            logger.info("校验用户是否可以参加该活动时返回false，mobile={}, activityId={}", getMobile(), record.getActivityId());           
            return "gdzc/404.ftl";
        }
        
        map.put("activityWinRecordId", activityWinRecordId);
        
        ActivityPrize prize = activityPrizeService.selectByPrimaryKey(record.getPrizeId());
        if(prize == null){
            logger.info("prize为空,recordId={},prizeId={}", activityWinRecordId, record.getPrizeId());
            return "gdzc/404.ftl";
        }
        map.put("prize", prize);
        
        Product product = productService.selectProductById(prize.getProductId());
        if(product == null){
            logger.info("product为空,recordId={},productId={}", activityWinRecordId, prize.getProductId());
            return "gdzc/404.ftl";
        }
        
        double price = product.getPrice()/100d * prize.getDiscount()/100d;
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        map.put("price", nf.format(price));       
        
        return "gdzc/pay.ftl";        
    }
    
   
    /** 
     * 支付页面，点击支付按钮
     * @Title: submitPay 
     */
    @RequestMapping("/submitPay")
    public void join(String activityWinRecordId, HttpServletResponse res){
        ActivityWinRecord record = activityWinRecordService.selectByRecordId(activityWinRecordId); 
        Map result = new HashMap();
        result.put("repeat", true);                        
        if(record!=null){
            logger.info("activityWinRecord{}当前的支付状态为{}", activityWinRecordId, record.getPayResult());
            //支付状态为：失败、未支付、退款成功
            if(record.getPayResult().equals(4) || record.getPayResult().equals(3) || record.getPayResult().equals(0)){
                result.put("repeat", false);
            }
        }
        try {
            res.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;
    }
    
    private Boolean checkUser(String activityId){
        CrowdfundingActivityDetail crowdfundingActivityDetail = crowdfundingActivityDetailService.selectByActivityId(activityId);
        Activities activities = activitiesService.selectByActivityId(activityId);
        if (crowdfundingActivityDetail == null) {
            logger.info("参数异常,crowdfundingActivityDetail为空,activityId={}", activityId);           
            return false;
        }
        //校验用户是否可以参加该活动
        if (crowdfundingActivityDetail.getUserList() == 2) {
            if (!userCheckService.checkMobile(getMobile(), activities.getEntId())) {
                logger.info("用户不满足ADC查询，mobile={}, activityId={}", getMobile(), activityId);               
                return false;
            }
        } else if (crowdfundingActivityDetail.getUserList() == 3) {
            if (!entUserQueryService.checkMobile(getMobile(), crowdfundingActivityDetail.getId())) {
                logger.info("用户不满足企业接口查询，mobile={}, activityId={}", getMobile(), activityId);
                return false;
            }
        } else {          
            if(!activitiesService.checkUser(getMobile(), activityId)){
                logger.info("用户不满足黑白名单，mobile={}, activityId={}", getMobile(), activityId);
                return false;
            }
        }
        return true;
    }
    
    /** 
     * 支付众筹活动
     * @Title: pay 
     */
    @RequestMapping("/callForPay")
    public String callForPay(String activityWinRecordId, ModelMap map){
        if(StringUtils.isEmpty(activityWinRecordId)){
            logger.info("activityWinRecordId为空");
            return "gdzc/404.ftl";
        }
        //1、插入支付记录activity_payment_info
        ActivityWinRecord record = activityWinRecordService.selectByRecordId(activityWinRecordId);
        ActivityPaymentInfo info = initActivityPaymentInfo(record);
        if(!activityPaymentInfoService.insertSelective(info)){
            logger.info("插入支付信息失败activityWinRecordId{}", activityWinRecordId);
            return "gdzc/404.ftl";
        }
        
        Activities activities = activitiesService.selectByActivityId(record.getActivityId());
        Manager entManager = null;
        if (activities != null) {
            entManager = entManagerService.getManagerForEnter(activities.getEntId());
        }

        String fullname = "";
        if (entManager != null) {
            fullname = managerService.getFullNameByCurrentManagerId(fullname, entManager.getParentId());
        }
        
        //2、发起支付请求
        PayParameter parameter = new PayParameter();
        parameter.setoId(info.getSysSerialNum());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        parameter.setoTime(sdf.format(new Date()));
        parameter.setUser(record.getOwnMobile());
        parameter.setAmount(info.getPayAmount().toString());
        parameter.setCity(District.getCode(fullname));
        Activities activity = activitiesService.selectByActivityId(record.getActivityId());
        parameter.setTitle(activity.getName());
        ActivityPrize activityPrize = activityPrizeService.selectByPrimaryKey(record.getPrizeId());
        parameter.setDesc(activityPrize.getPrizeName());    
               
        String param = paymentService.combinePayPara(parameter);
        if (param == null) {
            logger.info("构造支付订单url失败，activityWinRecordId{}", activityWinRecordId);
            return "gdzc/404.ftl";
        }
        String url = getUrl();
        String totalUrl = url + "?" + param;
        logger.info("支付地址:" + totalUrl);
        info.setStatus(1);//修改支付状态为支付中
        if(!activityPaymentInfoService.updateBySysSerialNumSelective(info)){
            logger.error("更新activity_payment_info失败，activity_payment_info={}", JSONObject.toJSONString(info));
        }
        
        record.setPayResult(1);
        if(!activityWinRecordService.updateByPrimaryKeySelective(record)){
            logger.error("更新activity_win_record失败，activity_win_record={}", JSONObject.toJSONString(record));
        }
//        
//        
//        /**
//         * 发起支付
//         */
//        if(!activityPaymentInfoService.callForPay(activityWinRecordId)){
//            logger.info("发起支付请求失败！activityWinRecordId={}", activityWinRecordId);
//            ActivityWinRecord record = activityWinRecordService.selectByRecordId(activityWinRecordId);
//            return "redirect:activityDetail.html?activityId=" + record.getActivityId();
//        }
        
        return "redirect:" + totalUrl;
        
    }    
    
    /** 
     * @Title: getCurrentPhone 
     * 获取当前用户
     */
    /*private String getCurrentPhone(){
        return "18827103708";
    }
    
    private String getWxOpenid(){
        return "wxOpenid";
    }*/
    
    /** 
     * @Title: initActivityPaymentInfo 
     */
    private ActivityPaymentInfo initActivityPaymentInfo(ActivityWinRecord record){
        ActivityPaymentInfo info = new ActivityPaymentInfo();
        info.setWinRecordId(record.getRecordId());
        info.setSysSerialNum(payUuid(record.getActivityId()));
        info.setChargeType("wx");//支付类型需确认
        info.setChargeTime(new Date());
        info.setStatus(0);//未支付
        info.setDeleteFlag(0);
        
        ActivityPrize prize = activityPrizeService.selectByPrimaryKey(record.getPrizeId());
        Product product = productService.selectProductById(prize.getProductId());       
        Long price = (long) (product.getPrice() * prize.getDiscount()/100);
        info.setPayAmount(price);
        
        return info;
    }
    private String payUuid(String activityId){
        Activities activity = activitiesService.selectByActivityId(activityId);
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(activity.getEntId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = sdf.format(new Date());
        int[] nums = new int[6];
        String random = "";
        for(int i = 0;i < nums.length;i++){
            nums[i] = (int)(Math.random()*10);
            random = random + nums[i];
        }
        String uuid = "GZH" + enterprise.getCode() + time + random;
        return uuid;
    }
    private String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_PAY_URL.getKey());
    }

    public static void main(String[] args) {
        PayParameter parameter = new PayParameter();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = sdf.format(new Date());
        int[] nums = new int[6];
        String random = "";
        for(int i = 0;i < nums.length;i++){
            nums[i] = (int)(Math.random()*10);
            random = random + nums[i];
        }
        String uuid = "GZH" + "2000456004" + time + random;
        parameter.setoId(uuid);
        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        time = sdf.format(new Date());
        parameter.setoTime(time);
        parameter.setUser("18867105766");

        parameter.setAmount("10");
       // Activities activity = activitiesService.selectByActivityId(record.getActivityId());
        parameter.setTitle("test");
        PaymentServiceImpl paymentService = new PaymentServiceImpl();
        String param = paymentService.combinePayPara(parameter);
        System.out.println(param);
        String url = "http://adc.ewaytec2000.cn/NGADCABInterface/TrafficZC/PmpOnePhaseSubmit.aspx";
        String totalUrl = url + "?" + param;
        System.out.println(totalUrl);
    }
}

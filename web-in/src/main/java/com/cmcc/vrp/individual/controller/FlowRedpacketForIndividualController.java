package com.cmcc.vrp.individual.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.service.ScQryRandPassService;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketActivityParam;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualFlowOrder;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.module.ScUserInfoRespData;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualActivitiesService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;


/**
 * FlowRedpacketController.java
 * 四川流量个人红包
 * @author wujiamin
 * @date 2017年3月6日
 */
@Controller
@RequestMapping("/individual/flowredpacket")
public class FlowRedpacketForIndividualController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(FlowRedpacketForIndividualController.class);
    @Autowired
    IndividualActivitiesService individualActivitiesService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    IndividualAccountService individualAccountService;
    
    @Autowired
    IndividualFlowOrderService individualFlowOrderService;
    
    @Autowired
    IndividualProductService individualProductService;
    
    @Autowired
    ActivitiesService activitiesService;
    
    @Autowired
    AdministerService administerService;
    
    @Autowired
    SmsRedisListener smsRedisListener;
    
    @Autowired
    PhoneRegionService phoneRegionService;
    
    @Autowired
    ActivityInfoService activityInfoService;
    
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    
    @Autowired
    ScQryRandPassService scQryRandPassService;
    
    @Value("#{settings['redis.sessiontimeout']}")  
    private Integer sessionTimeOut;
 
    /** 
     * 打开登录页
     * @Title: index 
     */
    @RequestMapping(value = "index")
    public String index(final HttpServletRequest request, final HttpServletResponse response) {
        //避免长时间停留在改页面，session过期导致验证码获取失败
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(86400);
        
        return "individual/flowRedpacket/login.ftl";
    }
    
    /** 
     * 登录
     * @throws IOException 
     * @Title: login 
     */
    @RequestMapping(value = "login")
    public void login(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        //避免因图片验证码session长期有效，先清空session
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(sessionTimeOut);
        String code = request.getParameter("code");
        String tel = request.getParameter("tel");
        JSONObject json = new JSONObject();
        if(!checkScPhone(tel)){
            json.put("sc", false);
        }
        
        if(!checkRandomMobileCode(code, tel)){            
            json.put("success", false);            
            response.getWriter().write(json.toJSONString());
            return;
        }

        setScRedpacketMobile(tel);
        
        //新用户，需要插入 administer
        Administer admin = administerService.selectByMobilePhone(tel);
        if(admin == null){
            if(!administerService.insertForScJizhong(tel)){
                logger.info("四川集中化平台创建新用户失败，mobile={}", tel);
                return;
            }
        }else{//如果该手机号之前已经是平台的管理员用户，在administer表中会存在该用户，但是该用户并没有个人账户信息，需要检查是否存在账户并插入账户         
            if(!individualAccountService.insertAccountForScJizhong(admin.getId())){
                logger.error("平台已存在该用户，但是用户没有个人账户，创建个人账户时失败，mobile={}，adminId={}", tel, admin.getId());
                return;
            }
        }
        
        json.put("success", true);            
        response.getWriter().write(json.toJSONString());
        return;
 
    }

    /** 
     * 校验四川手机号
     * @Title: checkScPhone 
     */
    private boolean checkScPhone(String mobile){
        //校验四川手机号
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        if(phoneRegion!=null && "四川".equals(phoneRegion.getProvince())){
            return true;
        }
        return false;
    }

    /** 
     * 发送短信随机验证码
     * @Title: sendRandPass 
     */
    @RequestMapping(value = "sendRandPass")
    public void sendRandPass(final HttpServletRequest request, final HttpServletResponse response) throws IOException {        
        String mobile = request.getParameter("mobile");
        String msgType = request.getParameter("msgType");
        JSONObject json = new JSONObject();
        if(!checkScPhone(mobile)){
            json.put("sc", false);
        }else{
            //发送短信
            json.put("result", scQryRandPassService.sendQryRandPass(mobile, "0", null, msgType));//短信发送结果     
        }
        
        response.getWriter().write(json.toJSONString());
        return;
 
    }

    /** 
     * 校验随机码是否一致
     * @Title: checkRandomMobileCode 
     */
    private boolean checkRandomMobileCode(String randomPassword, String mobile) {   
        return scQryRandPassService.sendQryRandPass(mobile, "1", randomPassword, null);
    }
    
    
    /** 
     * 打开订购列表页
     * @Title: orderListPage 
     */
    @RequestMapping(value = "list")
    public String orderListPage(final HttpServletRequest request, final HttpServletResponse response, ModelMap model) {
        String currentMobile = getScRedpacketMobile();        
        /*if(StringUtils.isEmpty(currentMobile)){
            logger.info("session中不存在用户手机号");
            return "redirect:index.html";
        }*/
        model.put("dailyCount", globalConfigService.get(GlobalConfigKeyEnum.SC_FLOW_ORDER_DAILY_LIMIT.getKey()));
        model.put("monthlyCount", globalConfigService.get(GlobalConfigKeyEnum.SC_FLOW_ORDER_MONTHLY_LIMIT.getKey()));
        
        List<IndividualProduct> products = individualProductService.selectByType(IndividualProductType.FLOW_PACKAGE.getValue());
        model.put("products", products);
        
        model.put("currentMobile", currentMobile);
        
        return "individual/flowRedpacket/redPacketList.ftl";
    }

    /** 
     * 打开创建红包活动页
     * @Title: createPage 
     */
    @RequestMapping(value = "create")
    public String createPage(ModelMap model, final HttpServletRequest request, final HttpServletResponse response) {
        String currentMobile = getScRedpacketMobile(); 
        Administer admin = administerService.selectByMobilePhone(currentMobile);
        if(admin == null){
            logger.info("admin不存在，mobile={}", currentMobile);
            return "redirect:index.html";
        }
        
        IndividualProduct product = individualProductService.getDefaultFlowProduct();
        if(product == null){
            logger.info("平台未默认的配置流量产品");
            return "404.ftl";
        }

        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(admin.getId(), 
                product.getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        model.put("count", account.getCount());
        return "individual/flowRedpacket/create.ftl";
    }
    
    /** 
     * 订购红包
     * @Title: order 
     */
    @RequestMapping(value = "order")
    public void order(final HttpServletRequest request, final HttpServletResponse response) {
        String currentMobile = getScRedpacketMobile();
        String prdCode = request.getParameter("prdCode");
        String verifyCode = request.getParameter("verifyCode");
        if(StringUtils.isEmpty(currentMobile) || StringUtils.isEmpty(prdCode) ){
            logger.info("session中不存在用户手机号，或传入的产品资费代码为空");
            return;
        }
        
        //红包订购
        JSONObject json = new JSONObject();
        
        //验证随机密码
        if(!checkRandomMobileCode(verifyCode, currentMobile)){
            json.put("result", "短信验证码错误");
            try {
                response.getWriter().write(json.toJSONString());
            } catch (IOException e) {                
                e.printStackTrace();
            }
            return;
        }        
        
        //检查红包订购是否超过限制
        Map<String, String> map = canOrder(currentMobile);
        if("success".equals(map.get("result"))){
            if(individualFlowOrderService.orderFlowForPage(currentMobile, prdCode)){            
                json.put("result", "success");
            }else{
                json.put("result", "订购失败");
            }
        }else{
            json.put("result", map.get("result"));
        }
        try {
            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {                
            e.printStackTrace();
        }
    }
    
    private Map<String, String> canOrder(String currentMobile){
        Map<String, String> map = new HashMap<String, String>();
        Administer admin = administerService.selectByMobilePhone(currentMobile);
        if(admin == null){
            logger.info("用户不存在，mobile={}", currentMobile);
            map.put("result", "订购失败");
            return map;
        }
        IndividualProduct product = individualProductService.getDefaultFlowProduct();
        if(product == null){
            logger.info("平台未默认的配置流量产品");
            map.put("result", "订购失败");
            return map;
        }
        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());;
        if(account != null && account.getCount().intValue() > 0){
            logger.info("个人流量账户还有余额，无法再次订购流量，accountId={}，count={}", account.getId(), account.getCount());
            map.put("result", "您有红包未发完，只有发完当前的红包才能重新订购");
            return map;
        }  
        
        map.put("result", "success");
        individualFlowOrderService.validateLimit(admin.getId(), map);
        //判断当天的次数
        /*Integer dateCount = individualFlowOrderService.countByDate(DateUtil.getBeginOfDay(now),DateUtil.getEndTimeOfDate(now));
        if(dateCount >= Integer.parseInt(globalConfigService.get(GlobalConfigKeyEnum.SC_FLOW_ORDER_DAILY_LIMIT.getKey()))){
            map.put("result", "您已达到当天购买上限");
            return map;
        }
        //判断当月的订购次数
        Calendar n = Calendar.getInstance( );
        int year = n.get(Calendar.YEAR);
        int month = n.get(Calendar.MONTH)+1;
        Integer monthCount = individualFlowOrderService.countByDate(DateUtil.getBeginMonthOfDate(now),DateUtil.getEndTimeOfMonth(year, month));
        if(monthCount >= Integer.parseInt(globalConfigService.get(GlobalConfigKeyEnum.SC_FLOW_ORDER_MONTHLY_LIMIT.getKey()))){
            map.put("result", "您已达到当月购买上限");
            return map;
        }*/ 
        
        return map;
    }

    /** 
     * 显示订购记录
     * @Title: showOrderList 
     */
    @RequestMapping("/showlist")
    public void showOrderList(int pageSize, int pageNo, HttpServletResponse res) {
        String currentMobile = getScRedpacketMobile();
        if(StringUtils.isEmpty(currentMobile)){
            logger.info("session中不存在用户手机号");
            return;
        }
        Administer admin = administerService.selectByMobilePhone(currentMobile);
        if(admin == null){
            logger.info("用户不存在，mobile={}", currentMobile);
            return;
        }
        
        QueryObject queryObject = new QueryObject();
        queryObject.setPageNum(pageNo);
        queryObject.setPageSize(pageSize);
        queryObject.getQueryCriterias().put("mobile", currentMobile);
        queryObject.getQueryCriterias().put("status", ChargeRecordStatus.COMPLETE.getCode());
        List<IndividualFlowOrder> records = individualFlowOrderService.selectByMap(queryObject.toMap());
        
        IndividualProduct product = individualProductService.getDefaultFlowProduct();
        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if(account==null){
            logger.info("账户不存在，adminId={}", admin.getId());
            return;
        }
        for(IndividualFlowOrder record : records){
            if(record.getId().equals(account.getCurrentOrderId())){
                if(account.getCount()!=null && account.getCount().intValue()>0){                               
                    List<Activities> activities = activitiesService.selectForOrder(admin.getId(), record.getSystemNum(), ActivityType.LUCKY_REDPACKET.getCode(),
                            ActivityStatus.PROCESSING.getCode());
                    
                    record.setCanCreateRedpacket(1);
                    
                    if(activities != null && activities.size() > 0){
                        //检查该活动是否已经抢完
                        for(Activities act : activities){
                            //校验活动奖品数量和当前中奖用户数量，活动奖品总数需要大于当前中奖用户数量
                            int count = activityWinRecordService.countChargeMobileByActivityId(act.getActivityId());
                            ActivityInfo activityInfo = activityInfoService.selectByActivityId(act.getActivityId());
                            if(activityInfo != null && activityInfo.getPrizeCount()>count){
                                record.setCanCreateRedpacket(0);//该订购不能新生成活动
                                break;
                            }
                        }                    
                    }
                }            
                break;
            }
        }

        try {
            Map result = new HashMap();
            result.put("data", records);
            res.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 跳转到中奖记录页面
     * @Title: showOrderList 
     */
    @RequestMapping("/winningList")
    public String winningList(String orderSystemNum, ModelMap model) {
        String currentMobile = getScRedpacketMobile();
        if(StringUtils.isEmpty(currentMobile)){
            logger.info("session中不存在用户手机号");
            return "redirect:index.html";
        }
        model.put("orderSystemNum", orderSystemNum);
        return "individual/flowRedpacket/winningList.ftl";
    }
    
    /** 
     * 显示中奖记录
     * @Title: showOrderList 
     */
    @RequestMapping("/showWinningList")
    public void showWinning(String orderSystemNum, int pageSize, int pageNo, HttpServletResponse res) {
        String currentMobile = getScRedpacketMobile();
        if(StringUtils.isEmpty(currentMobile)){
            logger.info("session中不存在用户手机号");
            return;
        }
        Administer admin = administerService.selectByMobilePhone(currentMobile);
        
        if(admin == null){
            logger.info("用户不存在，mobile={}", currentMobile);
            return;
        }
        
        QueryObject queryObject = new QueryObject();
        queryObject.setPageNum(pageNo);
        queryObject.setPageSize(pageSize);
        queryObject.getQueryCriterias().put("mobile", currentMobile);
        setQueryParameter("orderSystemNum", queryObject);

        List<ActivityWinRecord> records = activityWinRecordService.selectIndividualFlowRedpacketList(queryObject.toMap());

        try {
            Map result = new HashMap();
            result.put("data", records);
            res.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 生成红包
     * @Title: generateRedpacket 
     */
    @RequestMapping(value = "generate")    
    public void generateRedpacket(final HttpServletRequest request, final HttpServletResponse response) {   
        IndividualRedpacketActivityParam param = initParam(request);
        JSONObject json = new JSONObject();
        if(param !=null){ 
            if(param.getCount()<=1L){
                json.put("error", "红包每次至少发送2人");
            }else{
                //创建红包
                Map resultMap = individualActivitiesService.generateFlowRedpacketForPage(param);
                if(resultMap != null){
                    String url = (String) resultMap.get("url");
                    if(!"processing".equals(url)){                          
                        json.put("url", url); 
                        json.put("activityId", (String) resultMap.get("activityId")); 
                    }else{
                        json.put("error", "您有红包未发完，只有发完当前的红包才能重新生成红包");
                    }
                }else{
                    json.put("error", "生成红包失败！");
                }
            }
        }
        try {
            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {                
            e.printStackTrace();
        }
        return;
    }
    
    /** 
     * 获取红包url
     * @Title: getRedpacketUrl 
     */
    @RequestMapping(value = "getRedpacketUrl")
    public String getRedpacketUrl(String orderSystemNum){
        String currentMobile = getScRedpacketMobile();
        if(StringUtils.isEmpty(currentMobile)){
            logger.info("session中不存在用户手机号");
            return "redirect:index.html";
        }
        Administer admin = administerService.selectByMobilePhone(currentMobile);
        if(admin == null){
            logger.info("用户不存在，mobile={}", currentMobile);
            return "redirect:index.html";
        }
        List<Activities> actList = activitiesService.selectForOrder(admin.getId(), orderSystemNum, ActivityType.LUCKY_REDPACKET.getCode(), 
                ActivityStatus.PROCESSING.getCode());
        if(actList!=null && actList.size()==1){
            ActivityInfo activityInfo = activityInfoService.selectByActivityId(actList.get(0).getActivityId());
            if(activityInfo != null && !StringUtils.isEmpty(activityInfo.getUrl())){
                return "redirect:share.html?activityId=" + actList.get(0).getActivityId();
                //return "redirect:" + activityInfo.getUrl();
            }
        }
        return "404.ftl";
    }

    private IndividualRedpacketActivityParam initParam(HttpServletRequest request) {
        IndividualRedpacketActivityParam param = new IndividualRedpacketActivityParam();
        String currentMobile = getScRedpacketMobile();
        String size = request.getParameter("size");
        String count = request.getParameter("count");
        if(StringUtils.isEmpty(currentMobile) || StringUtils.isEmpty(size) || StringUtils.isEmpty(count)){
            logger.info("session中不存在用户手机号，或传入的流量大小和个数为空");
            return null;
        }
        param.setMobile(currentMobile);
        param.setStartTime(DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
        param.setType(ActivityType.LUCKY_REDPACKET.getCode());
        param.setSize(Long.parseLong(size));
        param.setCount(Long.parseLong(count));        
        String activityName = globalConfigService.get(GlobalConfigKeyEnum.SC_REDPACKET_NAME.getKey());
        param.setActivityName(activityName);        
        String object = globalConfigService.get(GlobalConfigKeyEnum.SC_REDPACKET_OBJECT.getKey());
        param.setObject(object);
        String rules = globalConfigService.get(GlobalConfigKeyEnum.SC_REDPACKET_RULE.getKey());
        param.setRules(rules);
        return param;
    }
    
    /** 
     * 和生活单点登录
     * @Title: getUserInfo 
     */
    @RequestMapping(value = "getUserInfo")    
    public void getUserInfo(final HttpServletRequest request, final HttpServletResponse response) { 
        String tokenJson = request.getParameter("token");
        logger.info("单点登录获取token={}", tokenJson);
        JSONObject json = new JSONObject();
        json.put("success", false);
        json.put("isScMobile", true);
        ScUserInfoRespData data = individualActivitiesService.getScUserInfo(tokenJson);        
        if(data!=null){            
            if("1".equals(data.getIsScMobile())){//"0"否，不是四川移动手机号，"1"是四川移动手机号
                json.put("success", true);
                setScRedpacketMobile(data.getMobile());
                json.put("mobile", data.getMobile());
            }else{
                json.put("isScMobile", false);
            }            
        }
        try {
            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {                
            e.printStackTrace();
        }
        return;
    }
    
    
    /** 
     * 跳转到分享中间页
     * @Title: share 
     */
    @RequestMapping(value = "share")
    public String share(String activityId, ModelMap map){
        Activities activity = activitiesService.selectByActivityId(activityId);
        ActivityInfo info = activityInfoService.selectByActivityId(activityId);
        if(activity==null || info == null || StringUtils.isEmpty(info.getUrl())){
            logger.info("活动不存在，活动信息不存在，或活动url不存在，activityId={}", activityId);
            return "404.ftl";
        }
        
        map.put("url", info.getUrl());
        return "individual/flowRedpacket/share.ftl";
    }
}

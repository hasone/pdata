package com.cmcc.vrp.weixin.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.SignAchieveCoinType;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.model.WxSignDetailRecord;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.SignInService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.service.WxSerialSignRecordService;
import com.cmcc.vrp.province.service.WxSignDetailRecordService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.beans.WxUserInfo;
import com.google.gson.Gson;

/**
 * 个人签到服务
 *
 * Created by sunyiwei on 2017/2/23.
 */
@RequestMapping("wx/sign")
@Controller
public class SignInController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignInController.class);

    @Autowired
    private SignInService signInService;
    
    @Autowired
    WxAdministerService wxAdministerService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    WxSerialSignRecordService wxSerialSignRecordService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualAccountService accountService;
    @Autowired
    WxSignDetailRecordService wxSignDetailRecordService;
    @Autowired
    WetchatService wetchatService;
    @Autowired
    CacheService cacheService;

    /**
     * 获取签到页面
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap map) {
        String wxAspect = (String) request.getAttribute("wxAspect");
        //test
        //String wxAspect = "done";
        
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            WxAdminister admin = getWxCurrentUser();
            
            //test
            //WxAdminister admin = wxAdministerService.selectByMobilePhone("13597092654");
            
            map.addAttribute("limitStartTime", globalConfigService.get(GlobalConfigKeyEnum.START_TIME_OF_FORBID_TO_SIGN_IN.getKey()));
            map.addAttribute("limitEndTime", globalConfigService.get(GlobalConfigKeyEnum.END_TIME_OF_FORBID_TO_SIGN_IN.getKey()));
            
            map.addAttribute("maxSerialSignDay", globalConfigService.get(GlobalConfigKeyEnum.MAX_SERIAL_SIGN_DAY.getKey()));
            map.addAttribute("giveCountPerDay", globalConfigService.get(GlobalConfigKeyEnum.SIGN_GIVE_COUNT_PER_DAY.getKey()));
            String awardCount = globalConfigService.get(GlobalConfigKeyEnum.SIGN_AWARD_COUNT.getKey());
            //String giveCountPerDay = globalConfigService.get(GlobalConfigKeyEnum.SIGN_GIVE_COUNT_PER_DAY.getKey());
            map.addAttribute("awardCount", awardCount);
            
            if(admin == null){
                LOGGER.info("session中adminId为空");
                return "gdzc/404.ftl";
            }
            
            map.addAttribute("today", DateUtil.dateToString(new Date(), "yyyy-MM-dd"));

            return "weixin/sign/newSign.ftl";
        }else{
            return wxAspect;
        }
    }

    
    /**
     * 获取当前登录用户所有的签到记录
     */
    @RequestMapping(value = "records", method = RequestMethod.GET)
    @ResponseBody
    public String signRecords(HttpServletRequest request) {
        
        WxAdminister user = getWxCurrentUser();
        //test
        //WxAdminister user = wxAdministerService.selectByMobilePhone("13597092654");
        
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        
        Map map = new HashMap<String, String>();
        map.put("startTime", startDate+" 00:00:00");
        map.put("endTime", endDate+" 23:59:59");
        map.put("type", SignAchieveCoinType.SIGN_COIN.getCode());
        map.put("adminId", user.getId());
        List<WxSignDetailRecord> list = wxSignDetailRecordService.selectByMap(map);
        
        //获取指定用户所有的签到记录, 每个字符串代表一个日期, 格式为yyyy-MM-dd
        Set<String> records = new HashSet<String>();
        if(list!=null && list.size()>0){
            for(WxSignDetailRecord item : list){
                String time = DateUtil.dateToString(item.getSignTime(), "yyyy-MM-dd"); 
                records.add(time);
            }
        }
        //Set<String> records = signInService.records(user.getMobilePhone());

        //转换成json字符串
        Map<String, Set<String>> result = new LinkedHashMap<String, Set<String>>();
        result.put("records", records);

        return new Gson().toJson(result);
    }
    
    /**
     * 前端需要后台传服务器时间
     * @author qinqinyan
     * */
    @RequestMapping(value = "getTime")
    public void getTime(HttpServletResponse response) throws IOException{
        Map map = new HashMap();
        
        String result = "true";
        Date timeDate = new Date();
        
        String time1 = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String limitEndTime = globalConfigService.get(GlobalConfigKeyEnum.END_TIME_OF_FORBID_TO_SIGN_IN.getKey());
        
        String startTimeStr = time1 + " "+ limitEndTime;
        LOGGER.info("startTimeStr = "+startTimeStr);
        
        if(timeDate.getTime() <= DateUtil.parse("yyyy-MM-dd HH:mm:ss", startTimeStr).getTime()){
            LOGGER.info("还未到签到时间");
            result = "false";
        }
        map.put("time", DateUtil.dateToString(timeDate, "yyyy-MM-dd HH:mm:ss"));
        map.put("startTime", startTimeStr);
        map.put("result", result);
        
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 用户执行签到操作
     * @throws IOException 
     */
    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public void sign(HttpServletResponse response) throws IOException{
        //1. 获取用户手机号
        String mobile = getMobile();
        
        //test
        //String mobile = "13597092654";
        
        //校验是否符合签到规定时间范围
        LOGGER.info("mobile = {}", mobile);
        Date startTime = getStartTimeOfForbitToSignIn();
        Date endTime = getEndTimeOfForbitToSignIn();
        Date signTime = new Date();
        if(!StringUtils.isValidMobile(mobile) || startTime == null || endTime == null){
            LOGGER.info("参数异常，签到失败！");
            Map map = new HashMap();
            map.put("result", "false");
            map.put("msg", "参数异常，签到失败！");
            map.put("time", DateUtil.dateToString(signTime, "yyyy-MM-dd HH:mm:ss"));
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }else{
            //检验签到时间是否在规定的00:00:00-07:00:00之外
            if(signTime.getTime()>=startTime.getTime() && signTime.getTime()<=endTime.getTime()){
                LOGGER.info("mobile = {} 签到时间不在规定时间范围内，签到失败", mobile);
                Map map = new HashMap();
                map.put("result", "false");
                map.put("msg", "签到时间不在规定时间范围内，签到失败！");
                map.put("time", DateUtil.dateToString(signTime, "yyyy-MM-dd HH:mm:ss"));
                response.getWriter().write(JSON.toJSONString(map)); 
                return;
            }else{
                Map map = signInService.newSign(mobile, signTime);
                response.getWriter().write(JSON.toJSONString(map)); 
                //response.getWriter().write(JSONObject.toJSONString(map));
                return; 
            }
        }
    }
    
    /**
     * 获取禁止签到起始时间段
     * */
    private Date getEndTimeOfForbitToSignIn(){
        String endTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd")+" "
                +globalConfigService.get(GlobalConfigKeyEnum.END_TIME_OF_FORBID_TO_SIGN_IN.getKey());
        try{
            return DateUtil.parse("yyyy-MM-dd HH:mm:ss", endTime);
        }catch(Exception e){
            LOGGER.info(e.getMessage());
            return null;
        }
    }
    
    /**
     * 获取禁止签到起始时间段
     * */
    private Date getStartTimeOfForbitToSignIn(){
        String startTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd")+" "
                +globalConfigService.get(GlobalConfigKeyEnum.START_TIME_OF_FORBID_TO_SIGN_IN.getKey());
        try{
            return DateUtil.parse("yyyy-MM-dd HH:mm:ss", startTime);
        }catch(Exception e){
            LOGGER.info(e.getMessage());
            return null;
        }
    }
    
    /**
     * 我的签到
     * @author qinqinyan
     * */
    @RequestMapping(value="mySign")
    public String mySign(ModelMap modelMap, HttpServletRequest request){
        
        //test
        //String wxAspect = "done";
        //String mobile = "13597092654";

        //1. 获取用户手机号
        String wxAspect = (String) request.getAttribute("wxAspect");
        String mobile = getMobile();
        modelMap.addAttribute("currentMobile", mobile);
        
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            WxAdminister admin = getWxCurrentUser();
            //test
            //WxAdminister admin = wxAdministerService.selectByMobilePhone("13597092654");
            if(admin == null){
                LOGGER.info("session中adminId为空");
                return "gdzc/404.ftl";
            }
            LOGGER.info("我的签到 adminId = {}, mobile = {}", admin.getId(), admin.getMobilePhone());
            
            
            //连续签到次数
            /*int serialSignCount = 0;
            Map map = new HashMap<String, String>();
            map.put("adminId", admin.getId());
            map.put("serialFlag", SerialFlagStatus.SERIAL.getValue());
            List<WxSerialSignRecord> list = wxSerialSignRecordService.selectByMap(map);
            if(list!=null && list.size()>0){
                serialSignCount = list.get(0).getCount();
            }
            modelMap.addAttribute("serialSignCount", serialSignCount);*/
            int signCount = wxSerialSignRecordService.getTotalCountByAdminIdAndMonth(admin.getId(), 
                    new Date());
            modelMap.addAttribute("serialSignCount", signCount);
            
            //今天获得的流量币
            String coinCount = signInService.getAchieveCoinCountToday(admin.getId());
            modelMap.addAttribute("coinCount", coinCount);
            
            //签到时间
            String signTime = signInService.getSignTime(admin.getId());
            modelMap.put("signTime", signTime);
            
            //总流量币
            IndividualProduct product = individualProductService.getIndivialPointProduct();
            if(product == null || product.getId() == null){
                LOGGER.info("平台未配置个人积分产品");
                return "gdzc/404.ftl";
            }
            IndividualAccount account = accountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), 
                    IndividualAccountType.INDIVIDUAL_BOSS.getValue());
            if(account!=null){
                modelMap.put("score", account.getCount());
            }else{
                modelMap.put("score", 0);
            }      
            
            WxUserInfo userInfo = wetchatService.getWxUserInfo(admin.getMobilePhone());
            if(userInfo!=null){
                modelMap.put("headImgurl", userInfo.getHeadimgurl());
            }
            
            Long count = signInService.getTotalSignCount();
            modelMap.put("totalSignCount", count.toString());
            
            return "weixin/sign/mySign.ftl";
        }else{
            return wxAspect;
        }
    }
    
    /**
     * 签到规则
     * @author qinqinyan 
     * */
    @RequestMapping(value = "signRule", method = RequestMethod.GET)
    public String signRule(HttpServletRequest request, ModelMap map){
        String wxAspect = (String) request.getAttribute("wxAspect");
        //test
        //String wxAspect = "done";
        
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            WxAdminister admin = getWxCurrentUser();
            
            //test
            //WxAdminister admin = wxAdministerService.selectByMobilePhone("13597092654");
            
            if(admin == null){
                LOGGER.info("session中adminId为空");
                return "gdzc/404.ftl";
            }
            map.addAttribute("maxAwardUserCount", globalConfigService.get(GlobalConfigKeyEnum.SIGN_MAX_COUNT_PER_DAY.getKey()));
            map.addAttribute("signAwardCount", globalConfigService.get(GlobalConfigKeyEnum.SIGN_AWARD_COUNT.getKey()));
            map.addAttribute("signGiveCount", globalConfigService.get(GlobalConfigKeyEnum.SIGN_GIVE_COUNT_PER_DAY.getKey()));
            map.addAttribute("maxSerialSign", globalConfigService.get(GlobalConfigKeyEnum.MAX_SERIAL_SIGN_DAY.getKey()));
            return "weixin/sign/signRule.ftl";

        }else{
            return wxAspect;
        }
    }
}

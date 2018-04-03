package com.cmcc.vrp.weixin.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.WxGradeService;
import com.cmcc.vrp.wx.WxInviteRecordService;
import com.cmcc.vrp.wx.beans.WxUserInfo;
import com.cmcc.vrp.wx.model.WxGrade;
import com.cmcc.vrp.wx.model.WxInviteRecord;

@Controller
@RequestMapping("/wx/score")
public class ScoreController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreController.class);
    
    private static final String OPEN_MYSCORE_TIME = "openMyscoreTime";//打开我的积分的时间
    
    private static final String CURRENT_GRADE = "currentGrade";//当前用户的等级
    
    @Autowired
    RedisUtilService redisUtilService;
    
    @Autowired
    IndividualAccountService accountService;
    
    @Autowired
    IndividualProductService individualProductService;
    
    @Autowired
    WxInviteRecordService inviteRecordService;
    
    @Autowired
    WxAdministerService wxAdministerService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    WxGradeService wxGradeService;
    
    @Autowired
    IndividualAccountRecordService individualAccountRecordService;
    
    @Autowired
    WetchatService wetchatService;
    
    /** 
     * 跳转到我的积分页面
     * @Title: myScore 
     */
    @RequestMapping("myScore")
    public String myScore(ModelMap model, HttpServletRequest request){
        String wxAspect = (String) request.getAttribute("wxAspect");
        
        //String wxAspect =  "done";
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            WxAdminister admin = getWxCurrentUser(); 
            
            //Administer admin = administerService.selectByMobilePhone("13597098090");
            
            if(admin == null){
                LOGGER.info("session中adminId为空");
                return "gdzc/404.ftl";
            }else{
                LOGGER.info("我的积分页面 adminId = {}, mobile = {}", admin.getId(), admin.getMobilePhone());
            	model.put("mobile", getMobile());            	
                return "weixin/score/myScore.ftl";
            }

        }else{
            return wxAspect;
        }
        
    }
    
    /**
     * 获取页面展现的信息
     * @param res
     */
    @RequestMapping("/getInfo")
    public void getInfo(HttpServletResponse res) {
    	Map result = new HashMap();//要返回到页面的信息
    	
    	WxAdminister admin = getWxCurrentUser(); 
    	//Administer admin = administerService.selectByMobilePhone("13597098090");
    	
        if(admin == null){
            LOGGER.info("session中adminId为空");
            return;
        }
        IndividualProduct product = individualProductService.getIndivialPointProduct();
        if(product == null || product.getId() == null){
            LOGGER.info("平台未配置个人积分产品");
            return;
        }
        IndividualAccount account = accountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), 
        		IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        Long myScore = 0L;
        Long accumulateScore = 0L;
        if(account != null){
            myScore = account.getCount().longValue();
            //累积的积分
            BigDecimal score = individualAccountRecordService.selectAccumulateAccount(account.getId());
            if(score != null){
                accumulateScore = score.longValue();
            }
            
            LOGGER.info("用户当前累积积分为：{}", accumulateScore);
        }
        result.put("myScore", myScore);
        
        Long adminId = admin.getId();
    	//当用户有多个弹窗要显示时按成功邀请、接受邀请、升级
        Map inviteMap = new HashMap();
        Map invitedMap = new HashMap();
        inviteMap.put("inviteAdminId", adminId);
        invitedMap.put("invitedAdminId", adminId);
       
        //更新缓存中的时间       
        String startTime = redisUtilService.get(OPEN_MYSCORE_TIME + adminId.toString());
        String endTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        inviteMap.put("startTime", startTime);
        inviteMap.put("endTime", endTime);
        invitedMap.put("startTime", startTime);
        invitedMap.put("endTime", endTime);
        
        List<String> prompt = new ArrayList<String>();
        //获取邀请积分
        String point = globalConfigService.get(GlobalConfigKeyEnum.INVITE_POINT.getKey());
        if(StringUtils.isEmpty(point)){
            point = "10";
        }
        //1-成功邀请
        List<WxInviteRecord> records = inviteRecordService.selectByMap(inviteMap);
        if(records!=null && records.size()>0){
            if(records.size()==1){
                WxAdminister invitedAdmin = wxAdministerService.selectWxAdministerById(records.get(0).getInvitedAdminId());
                String msg = "恭喜您成功邀请好友" + invitedAdmin.getMobilePhone() + "获得" + point + "流量币！";
                prompt.add(msg);
            }else{
                Integer points = Integer.parseInt(point) * records.size();
                String msg = "恭喜您成功邀请" + records.size() + "个好友获得" + points + "流量币！";
                prompt.add(msg);
            }
        }
        
        //2-接受邀请
        List<WxInviteRecord> invitedRecords = inviteRecordService.selectByMap(invitedMap);
        if(invitedRecords!=null && invitedRecords.size()==1){                     
            WxAdminister inviteAdmin = wxAdministerService.selectWxAdministerById(invitedRecords.get(0).getInviteAdminId());
            String msg = "恭喜您接受" + inviteAdmin.getMobilePhone() + "邀请绑定手机获得" + point + "流量币！";
            prompt.add(msg);            
        }
                
        //3-升级提醒
        //获取当前用户等级
        String currentGradeStr = redisUtilService.get(CURRENT_GRADE + adminId.toString());
        Integer currentGrade = 1;
        if(StringUtils.isEmpty(currentGradeStr)){
            redisUtilService.set(CURRENT_GRADE + adminId.toString(), currentGrade.toString(), null);
            currentGradeStr = "1";
        }
        
        if(!StringUtils.isEmpty(currentGradeStr)){
            //当前的级数
            currentGrade = Integer.parseInt(currentGradeStr);
            //获取当前积分
            Long currentPoints = accumulateScore.longValue();
            List<WxGrade> grades = wxGradeService.selectAllGrade();
            //当前积分应该位于的等级对象
            WxGrade currentGradeObj = null;
            for(WxGrade grade : grades){
                //当前积分大于该级需要的积分
                if(currentPoints >= grade.getPoints() && grade.getGrade() > currentGrade){
                    currentGradeObj = grade;
                    String msg = "恭喜您累积流量币达到" + grade.getPoints() + "升级为" + currentGradeObj.getName() + "！";
                    prompt.add(msg);                                        
                }
            }
            
            //有新的当前级别对象，则需要更新缓存中的级别级数
            if(currentGradeObj != null){
                currentGrade = currentGradeObj.getGrade();
                redisUtilService.set(CURRENT_GRADE + adminId.toString(), currentGrade.toString(), null);
            }
            
            //累积积分弹窗
            //获取下一级
            WxGrade nextGrade = wxGradeService.selectByGrade(currentGrade + 1);
            if(nextGrade!=null){//已经是顶级了，没有下一级升级了！
            	result.put("gradeMsg", "您已经累积了" + currentPoints + "流量币，再累积" +  (nextGrade.getPoints()-currentPoints) 
                    + "流量币即可升级为" + nextGrade.getName() + "！");
            }else{
            	result.put("gradeMsg", "您已经累积了" + currentPoints + "流量币！");
            }           
        }
        
        //成功邀请、接受邀请、升级弹窗的提示文案
        result.put("prompt", prompt);
        result.put("pic", "grade-" + currentGrade + ".png");
        
        //记录下最新打开页面的时间
        redisUtilService.set(OPEN_MYSCORE_TIME + adminId.toString(), endTime, null);
        
        try {
            res.getWriter().write(JSONObject.toJSONString(result)); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @RequestMapping("detail")
    public String detail(ModelMap model, HttpServletRequest request){
        String wxAspect = (String) request.getAttribute("wxAspect");
        
        //String wxAspect =  "done";
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            WxAdminister admin = getWxCurrentUser(); 
            
            //Administer admin = administerService.selectByMobilePhone("13597098090");
            
            if(admin == null){
                LOGGER.info("session中adminId为空");
                return "gdzc/404.ftl";
            }else{
                LOGGER.info("积分明细：adminId = {}, mobile = {}", admin.getId(), admin.getMobilePhone());
                
                IndividualProduct product = individualProductService.getIndivialPointProduct();
                if(product == null || product.getId() == null){
                    LOGGER.info("平台未配置个人积分产品");
                    return "gdzc/404.ftl";
                }
                IndividualAccount account = accountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), 
                		IndividualAccountType.INDIVIDUAL_BOSS.getValue());
                Long myScore = 0L;
                if(account != null){
                    myScore = account.getCount().longValue();
                }
                model.put("myScore", myScore);
                                
                WxUserInfo userInfo = wetchatService.getWxUserInfo(admin.getMobilePhone());
                if(userInfo!=null){
                    model.put("headImgurl", userInfo.getHeadimgurl());
                }
                
                return "weixin/score/scoreDetail.ftl";
            }

        }else{
            return wxAspect;
        }        
    }
    
}

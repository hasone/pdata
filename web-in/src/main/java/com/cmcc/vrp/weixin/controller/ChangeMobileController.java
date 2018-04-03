package com.cmcc.vrp.weixin.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.sms.login.LoginSmsPojo;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.WxGradeService;
import com.cmcc.vrp.wx.WxInviteRecordService;

@Controller
@RequestMapping("/wx/changeMobile")
public class ChangeMobileController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeMobileController.class);
    
    
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
    
    @Autowired
    SmsRedisListener smsRedisListener;
    
    /**
     * 跳转到解绑
     * @title:changeMobile
     * */
    @RequestMapping("changeMobile")
    public String changeMobile(ModelMap model, HttpServletRequest request){
        String wxAspect = (String) request.getAttribute("wxAspect");
        
        //String wxAspect = "done";
        
        LOGGER.info("wxAspect:"+wxAspect);
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            //test 暂时注释
            WxAdminister admin = getWxCurrentUser();
            //WxAdminister admin = wxAdministerService.selectByMobilePhone("18712345678");
                    
            if(admin == null){
                LOGGER.info("session中adminId为空");
                return "gdzc/404.ftl";
            }else{
                //test 暂时注释
                LOGGER.info("获取session里的手机号  = {}", getMobile());
                model.put("oldMobile", getMobile());

                //model.put("oldMobile", "18712345678");
                return "weixin/changeMobile/oldMobile.ftl";
            }
        }else{
            return wxAspect;
        }
    }
    
    /**
     * 检验短信验证码
     * @author qinqinyan
     * */
    @RequestMapping(value = "checkMsgCode")
    public void checkMsgCode(String code, String phone, HttpServletResponse res, HttpServletRequest request)
        throws IOException {
        LOGGER.info("短信验证码：code = {}, 手机号：phone = {}", code, phone);
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        try {
            if (checkRandomCode(code, phone)) {
                LOGGER.info("校验通过");
                map.put("result", "true");
                map.put("message", "success");
                res.getWriter().write(JSON.toJSONString(map));
                return;
            }
        } catch (RuntimeException e) {
            LOGGER.info("校验不通过,原因:"+e.getMessage());
            map.put("result", "false");
            map.put("message", e.getMessage());
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }
    }


    /**
     * 检验短信验证码
     * @author qinqinyan
     * */
    @RequestMapping(value = "checkMsgCodeForNewMobile")
    public void checkMsgCode(String code, String phone, String oldMobile, HttpServletResponse res, HttpServletRequest request)
            throws IOException {
        LOGGER.info("短信验证码：code = {}, 旧手机号：oldMobile = {}， 新手机号：phone = {}", code, oldMobile, phone);
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        try {
            String key = oldMobile + "_change";
            if (checkRandomCode(code, key)) {
                LOGGER.info("校验通过");
                map.put("result", "true");
                map.put("message", "success");
                res.getWriter().write(JSON.toJSONString(map));
                return;
            }
        } catch (RuntimeException e) {
            LOGGER.info("校验不通过,原因:"+e.getMessage());
            map.put("result", "false");
            map.put("message", e.getMessage());
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }
    }
    
    private String getModifyMobileURLForWinxin(){
        return globalConfigService.get("MODIFY_MOBILE_URL_FOR_WEIXIN");
    }
    
    /**
     * 跳转到绑定手机号页面
     * @author qinqinyan
     * @date 2017/09/19
     * */
    @RequestMapping("bindTelPage")
    public String bindTelPage(ModelMap map, HttpServletRequest request){
        String wxAspect = (String) request.getAttribute("wxAspect");
        //String wxAspect = "done";
        
        LOGGER.info("wxAspect:"+wxAspect);
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            //test 暂时注释
            WxAdminister admin = getWxCurrentUser();
            //WxAdminister admin = wxAdministerService.selectByMobilePhone("18712345678");
                    
            if(admin == null){
                LOGGER.info("session中adminId为空");
                return "gdzc/404.ftl";
            }else{
                //test 暂时注释
                LOGGER.info("获取session里的手机号  = {}", getMobile());
                map.put("oldMobile", getMobile());

                //map.put("oldMobile", "18712345678");
                return "weixin/changeMobile/newMobile.ftl";
            }
        }else{
            return wxAspect;
        }
    }
    
    /*@RequestMapping("testSession")
    public void testSession(){
        String openid = "openid-qqy";
        String oldMobile = "18867103717";
        String newMobile = "15116331234";
        String mobileFromSession1 = getMobile();
        LOGGER.info("mobileFromSession1 = " + mobileFromSession1);
        setMobile(oldMobile, openid);
        String mobileFromSession2 = getMobile();
        LOGGER.info("mobileFromSession2 = " + mobileFromSession2);
        setMobile(newMobile, openid);
        String mobileFromSession3 = getMobile();
        LOGGER.info("mobileFromSession3 = " + mobileFromSession3);
    }*/
    
    @RequestMapping("submitAjax")
    public void submitAjax(ModelMap modelMap, HttpServletRequest req, HttpServletResponse resp,
            String oldMobile, String newMobile) throws IOException{
        LOGGER.info("开始绑定:oldMobile = {}, newMobile = {}", oldMobile, newMobile);
        Map map = new HashMap<String, String>();
        String flag = "false";
        String message = "保存失败!";
        
        //先开始绑定微信侧的
        resp.reset();
        resp.setContentType("text/plain;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");

        String url = getModifyMobileURLForWinxin() + "?oldMobile=" 
        + oldMobile+"&newMobile=" + newMobile;
        LOGGER.info("修改微信侧手机号url:" + url);
        long startTime = System.currentTimeMillis();
        
        //test 暂时注释
        String result = HttpUtils.get(url);
        //String result = "true";
                
        long endTime = System.currentTimeMillis();
        LOGGER.info("修改微信侧手机号花费时间:"+(endTime-startTime)/1000);
        LOGGER.info("修改微信侧手机号结果:" + result);
        
        if(StringUtils.isEmpty(result) || "false".equals(result)){
            LOGGER.info("修改微信侧手机号失败");
        }else{
            LOGGER.info("修改微信侧手机号成功");
            try{
                if(wxAdministerService.updateWxAdminster(oldMobile, newMobile)){
                    LOGGER.info("修改手机号成功!oldMobile = {}, newMobile = {}", oldMobile, newMobile);
                    flag = "true";
                    //修改session中手机号
                    String openId = getWxOpenid();
                    LOGGER.info("openId = {}, newMobile = {}", openId, newMobile);
                    LOGGER.info("更改session之前的手机号：{}", getMobile());
                    setMobile(newMobile, openId);
                    LOGGER.info("更改session之后的手机号：{}", getMobile());
                    //req.getSession().setAttribute("mobile", newMobile);
                }else{
                    LOGGER.info("流量平台修改手机号失败!oldMobile = {}, newMobile = {}", oldMobile, newMobile);
                    LOGGER.info("将微信侧活动更改的手机号还原回来");
                    
                    String url1 = getModifyMobileURLForWinxin() + "?oldMobile=" 
                            + newMobile+"&newMobile=" + oldMobile;
                    LOGGER.info("还原微信侧手机号url1:" + url1);
                    startTime = System.currentTimeMillis();
                    
                    String result1 = HttpUtils.get(url1);
                            
                    endTime = System.currentTimeMillis();
                    LOGGER.info("还原微信侧手机花费时间:"+(endTime-startTime)/1000);
                    LOGGER.info("还原微信侧手机号结果:" + result1);
                    
                    if(StringUtils.isEmpty(result1) || "false".equals(result1)){
                        LOGGER.info("将手机号  = {} 还远成 = {} 失败。", newMobile, oldMobile);
                    }
                }
            }catch(RuntimeException e){
                LOGGER.info("流量平台修改手机号失败!oldMobile = {}, newMobile = {}, 失败原因: {}", 
                        oldMobile, newMobile, e.getMessage());
                LOGGER.info("将微信侧活动更改的手机号还原回来");
                
                String url2 = getModifyMobileURLForWinxin() + "?oldMobile=" 
                        + newMobile+"&newMobile=" + oldMobile;
                LOGGER.info("还原微信侧手机号url2:" + url2);
                startTime = System.currentTimeMillis();
                
                String result2 = HttpUtils.get(url2);
                        
                endTime = System.currentTimeMillis();
                LOGGER.info("还原微信侧手机花费时间:"+(endTime-startTime)/1000);
                LOGGER.info("还原微信侧手机号结果:" + result2);
                
                if(StringUtils.isEmpty(result2) || "false".equals(result2)){
                    LOGGER.info("将手机号  = {} 还远成 = {} 失败。", newMobile, oldMobile);
                }
            }
        }
        LOGGER.info("更改手机号结果flag = {}", flag);
        map.put("result", flag);
        map.put("message", message);
        resp.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 校验验证码
     */
    private boolean checkRandomCode(String code, String phone) throws RuntimeException {
        LoginSmsPojo pojo = smsRedisListener.getLoginInfo(phone, SmsType.UPDATEPHON_FOR_WX_SMS);
        if (pojo == null) {
            throw new RuntimeException("请先获取短信验证码!");
        }

        //判断是否已超过5分钟
        Date after = DateUtil.getDateAfterMinutes(pojo.getLastTime(), 5);
        if (after.getTime() < (new Date()).getTime()) {
            throw new RuntimeException("短信验证码已超时!");
        }

        if (!code.equals(pojo.getPassword())) {
            throw new RuntimeException("请输入正确短信验证码!");
        }
        return true;
    }
    
    
    
    
    
}

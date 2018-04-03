package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.AES;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.util.VerifycodeUtil;

/**
 * SendMessageController.java
 */
@Controller
@RequestMapping("/manage/message")
public class SendMessageController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(SendMessageController.class);
    //发短信使用的globalConfig标志的config——key
    private final String sendMsgKey = "SENDMESSAGE_CHECK";

    //SENDMESSAGE_CHECK发送短信时数据库中的值
    private final String sendMsgValue = "OK";


    @Autowired
    SmsRedisListener smsRedisListener;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    TaskProducer taskProducer;

    @Autowired
    private JedisPool jedisPool;

    private final String configKey = "RANDOMPASS_CHECK";//数据库中globalconfig的随机验证码验证key值
    
    private final String checkPassKey = "OK";//globalconfig需要检验随机密码的value值
    
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    WxAdministerService wxAdministerService;

    /**
     * ajax发送用户登陆使用短信
     *
     * @param req
     * @param res
     * @throws IOException
     */
    @RequestMapping(value = "sendOutLoginMessage")
    public void sendOutLoginMessage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        send(req, res, SmsType.WEBINLOGIN_SMS);
    }
 
    /**
     * ajax发送四川集中化平台用户登陆使用短信
     *
     * @param req
     * @param res
     * @throws IOException
     */
    @RequestMapping(value = "sendIndividualLoginMessage")
    public void sendIndividualLoginMessage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/text;charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值

        //从前台得到用户的登陆手机号码
        String imgVerifyCode = req.getParameter("imgVerifyCode");

        logger.info("用户输入的图形验证码为：{}", imgVerifyCode);
        String configValue = globalConfigService.get(configKey);
        if (configValue != null && !checkPassKey.equals(configValue)) {
            send(req, res, SmsType.WEBINLOGIN_SMS);
            return;
        } else {
            String sessionCode = (String) req.getSession(true).getAttribute("virifyCode");//得到session中的验证码
            logger.info("session中的图形验证码为：{}", sessionCode);
            if (imgVerifyCode.equalsIgnoreCase(sessionCode)) {
                send(req, res, SmsType.WEBINLOGIN_SMS);
                return;
            } else {
                map.put("message", "请输入正确的图形验证码!");
                res.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
        
    }

    /**
     * ajax发送用户更改密码使用短信
     *
     * @param req
     * @param res
     * @throws IOException
     */
    @RequestMapping(value = "sendChangePassMessage")
    public void sendChangePassMessage(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        send(req, res, SmsType.UPDATEPASS_SMS);
    }
    
    private void send(HttpServletRequest req, HttpServletResponse res, SmsType smsType) throws IOException {
        res.setContentType("application/text;charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值

        //从前台得到用户的登陆手机号码
        String mobilePhone = req.getParameter("mobilePhone");

        //检测手机是否合法
        if (!StringUtils.isValidMobile(mobilePhone)) {
            map.put("message", "对不起，您填写的手机不符合格式规范!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //尝试发送短信
        String sendResult = sendRandomCode(mobilePhone, smsType);
        if (sendResult == null) {
            map.put("message", "短信发送成功!");
            map.put("success", "true");
            res.getWriter().write(JSON.toJSONString(map));
        } else {
            map.put("message", sendResult);
            res.getWriter().write(JSON.toJSONString(map));
        }
    }

    private boolean validateLimit(SmsType smsType) {
        //只限制登录频率
        if (smsType != SmsType.WEBINLOGIN_SMS) {
            return true;
        }

        final String LIMIT_KEY = "sms_login_limit";
        final int LIMIT_COUNT = getLimitCount();
        final int EXPIRE_PERIOD = 3600;

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(LIMIT_KEY)) { //不存在这个KEY，第一次发送
                jedis.setex(LIMIT_KEY, EXPIRE_PERIOD, "1");
                return true;
            } else {
                return jedis.incr(LIMIT_KEY) < LIMIT_COUNT;
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    /**
     * 获取判读手机号是否已经被绑定url
     * */
    private String getIsBindedUrl(){
        return globalConfigService.get("IS_BINDED_URL_IN_WEIXIN");
    } 
    
    /**
     * 用于广东众筹更换手机号
     * 这里用于验证旧手机号
     * @throws IOException 
     * */
    @RequestMapping("sendRondomCodeToOldmobile")
    public void sendRondomCodeToOldmobile(HttpServletRequest req, 
            HttpServletResponse res) throws IOException{
        res.reset();
        res.setContentType("application/text;charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        
        //从前台得到用户的登陆手机号码
        String oleMobile = req.getParameter("oleMobile");
        logger.info("获取到的旧手机号 oleMobile = " + oleMobile);
        if(!StringUtils.isValidMobile(oleMobile)){
            map.put("message", "用户信息异常,发送失败!");
            res.getWriter().write(JSON.toJSONString(map));
        }
        
        //尝试发送短信
        String sendResult = sendRandomCode(oleMobile, SmsType.UPDATEPHON_FOR_WX_SMS);
        logger.info("发送短信结果 sendResult = " + sendResult);
        if (sendResult == null) {
            map.put("message", "短信发送成功!");
            res.getWriter().write(JSON.toJSONString(map));
        } else {
            map.put("message", sendResult);
            res.getWriter().write(JSON.toJSONString(map));
        }
    }
    
    /**
     * 用于广东众筹更换手机号
     * @throws IOException 
     * */
    @RequestMapping(value = "sendRondomCodeToNewmobile")
    public void sendRondomCodeForChangeMobile(HttpServletRequest req, 
            HttpServletResponse res) throws IOException{
        res.reset();
        res.setContentType("application/text;charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        
        //从前台得到用户的登陆手机号码
        String newMobile = req.getParameter("newMobile");
        String oldMobile = req.getParameter("oldMobile");
        
        //检测手机是否合法
        if (!StringUtils.isValidMobile(newMobile) || !StringUtils.isValidMobile(oldMobile)) {
            map.put("message", "对不起，您填写的手机不符合格式规范!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }
        
        //校验新手机号在平台侧是否已经被使用
        WxAdminister administer = wxAdministerService.selectByMobilePhone(newMobile);
        if(administer!=null){
            map.put("message", "对不起，该手机号已被绑定，请重新输入!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }
        
        //校验微信侧新手机号是否已经被使用
        res.setHeader("Cache-Control", "no-cache");
        String url = getIsBindedUrl() + newMobile;
        logger.info("判断新手机号是否已经被绑定url:" + url);
        long startTime = System.currentTimeMillis();
        
        //test 为了测试暂时注释
        String result = HttpUtils.get(url);
        //String result = "true";
        
        long endTime = System.currentTimeMillis();
        logger.info("判断新手机号绑定请求话费时间:" + (endTime - startTime)/1000 + "秒");
        logger.info("判断新手机号绑定结果:" + result);
        if(StringUtils.isEmpty(result) || "false".equals(result)){
            map.put("message", "对不起，该手机号已被绑定，请重新输入!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //尝试发送短信
        String sendResult = sendRandomCodeForGDWx(oldMobile, newMobile, SmsType.UPDATEPHON_FOR_WX_SMS);
        logger.info("发送短信结果:" + sendResult);
        if (sendResult == null) {
            map.put("message", "短信发送成功!");
            res.getWriter().write(JSON.toJSONString(map));

        } else {
            map.put("message", sendResult);
            res.getWriter().write(JSON.toJSONString(map));
        }
    }

    /**
     * 发送更改手机号码的验证码
     *
     * @param req
     * @param res
     * @throws IOException
     */
    @RequestMapping(value = "sendChangeMobilePhone")
    public void sendChangeMobilePhone(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        res.setContentType("application/text;charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        //从前台得到用户的登陆手机号码
        String mobilePhone = req.getParameter("mobilePhone");
        
        //检测手机是否合法
        if (!StringUtils.isValidMobile(mobilePhone)) {
            map.put("message", "对不起，您填写的手机不符合格式规范!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //尝试发送短信
        String sendResult = sendRandomCode(mobilePhone, SmsType.UPDATEPHONE_SMS);
        if (sendResult == null) {
            map.put("message", "短信发送成功!");
            res.getWriter().write(JSON.toJSONString(map));

        } else {
            map.put("message", sendResult);
            res.getWriter().write(JSON.toJSONString(map));
        }

    }
    
    /**
     * 发送重置密码的验证码
     *
     * @param req
     * @param res
     * @throws IOException
     */
    @RequestMapping(value = "sendResetPassword")
    public void sendResetPassword(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        res.setContentType("application/text;charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        map.put("success", false);
        //从session获得要发送验证码的手机号
        Administer administer = getCurrentUser();
        if (administer == null) {
            logger.error("session中不存在用户！");
            map.put("message", "短信发送失败!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }
        String mobilePhone = administer.getMobilePhone();
        
        //检测手机是否合法
        if (!StringUtils.isValidMobile(mobilePhone)) {
            map.put("message", "对不起，您填写的手机不符合格式规范!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //尝试发送短信
        String sendResult = sendRandomCode(mobilePhone, SmsType.RESET_PASSWORD);
        if (sendResult == null) {
            map.put("message", "已向" + administer.getMobilePhone().substring(0, 3) + "****" 
                    + administer.getMobilePhone().substring(7, 11) + "手机号码发送验证码，请查收！");
            map.put("success", true);
            res.getWriter().write(JSON.toJSONString(map));
            return;
        } else {
            map.put("message", sendResult);
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }

    }

    /**
     * 发送随机验证码，1分钟之内不允许重复发送
     *
     * @param oldMobile 手机号码
     * @param newMobile 手机号码
     * @param type   短信类型
     * @return
     */
    private String sendRandomCodeForGDWx(String oldMobile, String newMobile, SmsType type) {
        //随机验证码
        String randomCode = VerifycodeUtil.getRandomNum();

        String rediskey = oldMobile + "_change";

        //尝试在redis中设置新的手机验证码
        if (!smsRedisListener.setNewPassForGD(newMobile, rediskey, randomCode, type)) {
            return "请勿在1分钟之内重复发送";
        }

        if (!validateLimit(type)) {
            return "对不起，当前已超过登录频率限制!";
        }

        //如果是重置密码时重新获取验证码，要先删除缓存中验证码验证次数
        if(SmsType.RESET_PASSWORD.getSuffix().equals(type.getSuffix())){
            //删除验证码次数的缓存
            if(!smsRedisListener.delete(rediskey + type.getSuffix() + "Times")){
                logger.error("redis中删除验证码验证次数失败，redis-key={}", rediskey + type.getSuffix() + "Times");
            }
        }

        logger.info("手机 " + newMobile + type.getType() + "的最新动态密码是" + randomCode);

        String sendMsg = buildContent(type, randomCode);
        if (isSendMsg()) {//连接短信网关
            SmsPojo pojo = new SmsPojo(newMobile, sendMsg, null, null, MessageType.RANDOM_CODE.getCode());
            taskProducer.produceDeliverVerifySmsMsg(pojo);

            return null;
        }

        return null;
    }

    /**
     * 发送随机验证码，1分钟之内不允许重复发送
     *
     * @param mobile 手机号码
     * @param type   短信类型
     * @return
     */
    private String sendRandomCode(String mobile, SmsType type) {
        //随机验证码
        String randomCode = VerifycodeUtil.getRandomNum();

        //尝试在redis中设置新的手机验证码
        if (!smsRedisListener.setNewPass(mobile, randomCode, type)) {
            return "请勿在1分钟之内重复发送";
        }

        if (!validateLimit(type)) {
            return "对不起，当前已超过登录频率限制!";
        }

        //如果是重置密码时重新获取验证码，要先删除缓存中验证码验证次数
        if(SmsType.RESET_PASSWORD.getSuffix().equals(type.getSuffix())){
            //删除验证码次数的缓存        
            if(!smsRedisListener.delete(mobile + type.getSuffix() + "Times")){
                logger.error("redis中删除验证码验证次数失败，redis-key={}", mobile + type.getSuffix() + "Times");
            }
        }
        
        logger.info("手机 " + mobile + type.getType() + "的最新动态密码是" + randomCode);

        String sendMsg = buildContent(type, randomCode);
        if (isSendMsg()) {//连接短信网关
            SmsPojo pojo = new SmsPojo(mobile, sendMsg, null, null, MessageType.RANDOM_CODE.getCode());
            taskProducer.produceDeliverVerifySmsMsg(pojo);

            return null;
        }

        return null;
    }

    //根据短信类型可能有不同的短信内容
    private String buildContent(SmsType type, String randomCode) {
        if(SmsType.RESET_PASSWORD.getSuffix().equals(type.getSuffix())){
            String logoName = globalConfigService.get(GlobalConfigKeyEnum.LOGO_NAME.getKey());
            return "【" + logoName + "】您正在重置密码，验证码" + randomCode + "，5分钟内有效";
        }
        
        return "您的验证码是 " + randomCode + "，有效期5分钟。";
    }

    private boolean isSendMsg() {
        //从数据库的global_config表中得到config_key对应的value值，值不为"OK"则不进行检测验证码，返回
        String configValue = globalConfigService.get(sendMsgKey);

        //为了防止误删，当数据库不存在key时，一样需要发送短信
        return configValue == null || sendMsgValue.equals(configValue);
    }

    /**
     * 发送流量券登录验证码短信
     *
     * @param secretPhone
     * @param res
     * @throws IOException
     * @Title: sendChangeMobilePhone
     * @Author: wujiamin
     * @date 2016年8月19日下午1:57:07
     */
    @RequestMapping(value = "sendFlowcardLogin")
    public void sendFlowcardLogin(String secretPhone, HttpServletResponse res)
        throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值

        String mobilePhone = "";
        if("shanghai".equals(globalConfigService.get("PROVINCE_FLAG")) || secretPhone.length()==11){
            mobilePhone = secretPhone;
        }else{
            //解密
            mobilePhone = decryptOwnerPhone(secretPhone);
        }
        //检测手机是否合法
        if (!StringUtils.isValidMobile(mobilePhone)) {
            map.put("message", "对不起，您填写的手机不符合格式规范!");
            res.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //尝试发送短信
        String sendResult = sendRandomCode(mobilePhone, SmsType.FLOWCARD_SMS);
        if (sendResult == null) {
            map.put("message", "短信发送成功!");
            res.getWriter().write(JSON.toJSONString(map));

        } else {
            map.put("message", sendResult);
            res.getWriter().write(JSON.toJSONString(map));
        }
    }

    /**
     * 解密电话号码
     *
     * @param secretPhone
     * @return
     * @Title: decryptOwnerPhone
     * @Author: wujiamin
     * @date 2016年8月24日下午7:19:57
     */
    private String decryptOwnerPhone(String secretPhone) {
        String key = globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_URL_KEY.getKey());
        String phone = AES.decryptString(secretPhone, key);
        logger.info("解密后的电话号码为:" + phone);
        return phone;
    }

    public int getLimitCount() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.LOGIN_SMS_LIMIT.getKey()), 60);
    }


    @RequestMapping("getRemainedTimeAjax")
    public void getRemainedTimeAjax(HttpServletResponse response, String mobile){
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        logger.info("mobile = " + mobile);
        Long remainedTime = 60L;
        if(!StringUtils.isEmpty(mobile)){
            remainedTime = smsRedisListener.getRemainTime(mobile, SmsType.UPDATEPHON_FOR_WX_SMS);
        }

        map.put("remainedTime", remainedTime.toString());
        try{
            response.getWriter().write(JSON.toJSONString(map));
        }catch (IOException e){
            logger.info("返回失败，失败原因："+e.getMessage());
        }
    }


    @RequestMapping("getRemainedTimeForNewMobileAjax")
    public void getNewRemainedTimeAjax(HttpServletResponse response, String newMobile, String oldMobile){
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        logger.info("newMobile = " + newMobile +" ; oldMobile = "+oldMobile);
        Long remainedTime = 60L;
        if(!StringUtils.isEmpty(oldMobile)){
            remainedTime = smsRedisListener.getRemainTime(oldMobile+"_change", SmsType.UPDATEPHON_FOR_WX_SMS);
        }

        map.put("remainedTime", remainedTime.toString());
        try{
            response.getWriter().write(JSON.toJSONString(map));
        }catch (IOException e){
            logger.info("返回失败，失败原因："+e.getMessage());
        }
    }
}


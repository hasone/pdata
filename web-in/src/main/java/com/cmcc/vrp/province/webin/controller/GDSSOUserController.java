package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.RespModel;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.AESdecry;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;


/**
 * 
 * 广东单点登录相关controller
 *
 */
@Controller
@RequestMapping("/gd/sso")
public class GDSSOUserController {
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    AdministerService administerService;
    
    private static final Logger logger = LoggerFactory.getLogger(GDSSOUserController.class);
    
    /**
     * 收到登录请求，验证类
     */
    @RequestMapping(value = "loginReq")
    public void loginReq(HttpServletRequest req,HttpServletResponse res,ModelMap modelMap) throws IOException{
        
        //非广东环境，直接返回400
        if(!isGDUseSSO()){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println(getErrRespData("is not gd sso environment."));
            logger.error("非广东单点登录环境");
            return;
        }
        
        //从header中获取token
        String token = req.getHeader("token");
        if(StringUtils.isBlank(token)){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println(getErrRespData("token not exist."));
            logger.error("token not exist.");
            return;
        }
        logger.info("获取token:" + token);
        
        //验证token，获取到手机号
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("token", token);
        
        String ssoCheckUrl = ssoCheckUserUrl();
        String response = 
                HttpUtils.post(ssoCheckUserUrl(), "", ContentType.TEXT_PLAIN.getMimeType(), headerMap);

        //解析返回数据，获取手机号
        String mobilePhone = analyseResp(response);
        if(!com.cmcc.vrp.util.StringUtils.isValidMobile(mobilePhone)){
            logger.error("验证token获取手机号失败,ssoCheckUrl={},token={},response={},mobile={}" ,ssoCheckUrl,token,response,mobilePhone);
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println(getErrRespData("fail to get phone."));
            return;
        }
        
        //校验手机
        if(administerService.selectByMobilePhone(mobilePhone) == null){
            logger.error("手机号在平台不存在,mobilePhone={}",mobilePhone);
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().println(getErrRespData("mobile not exist."));
            return;
        }
        
        //生成url，加密放在json中返回
        String url = generateLoginUrl(mobilePhone);        
        String respData = getCorrectRespData(url);
        res.getWriter().println(respData);
        res.setStatus(HttpServletResponse.SC_OK);
        logger.info("返回数据:{},token:{}",respData,token);
    }
    
    /**
     * 生成正确时数据，已将登录地址加密
     */
    public String getCorrectRespData(String url){
        String decyStr = AESdecry.encrypt(url,getAESPublicKey());  
        return new Gson().toJson(new RespModel(200, decyStr, null));
    }
    
    /**
     * 生成错误时数据
     */
    public String getErrRespData(String errReason){
        return new Gson().toJson(new RespModel(400, null, errReason));
    }
    
    /**
     * 解析数据，得到手机号
     * 解密后的格式为  123456&18867102087  前面为时间戳，后面为手机号
     */
    public String analyseResp(String resp){
        try{
            JSONObject jsonObject = JSONObject.parseObject(resp);
            String encryData;//加密后数据
            if(jsonObject == null || StringUtils.isBlank(encryData = jsonObject.getString("src"))){
                return "";
            }
            
            String decryData = AESdecry.decrypt(encryData, getAESPublicKey());
            if(StringUtils.isBlank(decryData)){
                return "";
            }
            
            String[] parties = decryData.split("&");
            
            String mobile = parties.length<2 ? "":parties[1];
            return mobile;         
        }catch(JSONException e){     
            return "";
        } 
    }
    
    /**
     * 生成登录用地址
     */
    private String generateLoginUrl(String mobile){
        String ssoToken = String.valueOf(new DateTime().getMillis()) + "&" + mobile;

        try {
            return getLocalLoginUrl() + "?ssoToken=" +
                URLEncoder.encode(AESdecry.encrypt(ssoToken, getAESPrivateKey()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    
    /**
     * 检测是广东环境,且使用sso登录
     */
    private boolean isGDUseSSO(){
        return "gd_mdrc".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())) &&
                "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_IS_USE.getKey()));
    }
    
    
    /**
     * sso验证token地址
     */
    private String ssoCheckUserUrl(){
        return globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_CHECKTOKEN_URL.getKey());
    }
    
    /**
     * 获取本平台登录地址
     */
    private String getLocalLoginUrl(){
        return globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_LOGAL_LOGIN_URL.getKey());
    }
    
    /**
     * 广东单点登录与登录平台共用加密key
     */
    private String getAESPublicKey(){
        return globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_PUBLIC_KEY.getKey());
    }
    
    /**
     * 广东单点登录加密登录地址使用私钥
     */
    private String getAESPrivateKey(){
        return globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_PRIVATE_KEY.getKey());
    }
}

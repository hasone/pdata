package com.cmcc.vrp.boss.sichuan.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.ScQryRandPassRequest;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.service.ScQryRandPassService;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;

/**
 * ScQryRandPassServiceImpl.java
 * @author wujiamin
 * @date 2017年3月17日
 */
@Service("scQryRandPassService")
public class ScQryRandPassServiceImpl extends AbstractCacheSupport implements ScQryRandPassService{
    
    private static Logger logger =  LoggerFactory.getLogger(ScQryRandPassServiceImpl.class);
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Override
    public boolean sendQryRandPass(String mobile, String type, String randPass, String msgType) {
        //判断是不是测试环境（DYNAMIC_PROXY_BOSS_FLAG）
        if("true".equals(globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey()))){
            logger.info("短信验证码测试发送和验证");
            return true;
        }
        //随机验证码发送，需要校验频率
        if("0".equals(type)){
            if(StringUtils.isEmpty(cacheService.get(msgType+mobile))){
                cacheService.getIncrOrUpdate(msgType+mobile, 60);
            }else{
                logger.info("mobile={}在60s内已发送过{}短信验证码，请60s后重试！", mobile, msgType);
                return false;
            }
        }
        
        String request = generateRequest(mobile, type, randPass);
        if(request == null){
            return false;            
        }
        
        String url = globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_QRY_RANDPASS_URL.getKey());
        logger.info("发生短信验证码请求,请求地址：{},请求参数:{}" , url ,request);
        String result;
        try {
            result = HttpConnection.sendGetRequest(url,request);
        } catch (IllegalStateException e) {            
            e.printStackTrace();
            return false;
        } catch (IOException e) {            
            e.printStackTrace();
            return false;
        }        
        logger.info("发生短信验证码返回:{},请求参数:{}" , result, request);
        
        JSONObject json = (JSONObject) JSONObject.parse(result);
        String resCode = json.getString("resCode");
        
        //发送短信没有成功，则在缓存中删除标识，让用户可以再次发送
        if(!"0000000".equals(resCode)&&"0".equals(type)){
            cacheService.delete(msgType+mobile);
        }
        
        return "0000000".equals(resCode);
           
    }
    
    /** 
     * 获取请求参数
     * @Title: generateRequest 
     */
    private String generateRequest(String mobile, String type, String randPass){
        if(StringUtils.isEmpty(mobile)){
            return null;
        }
        ScQryRandPassRequest request = new ScQryRandPassRequest();
        String appKey = globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_APP_KEY.getKey());
        request.setAppKey(appKey);        
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));
        String userName = globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_USER_NAME.getKey());
        request.setUserName(userName);
        request.setPhoneNo(mobile);
        request.setPassMode(type);//0-生成随机码，1-验证随机码
        if("1".equals(type)){
            request.setSmsPwd(randPass);
        }
        request.setPassLength("6");
        String loginNo = globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_LOGIN_NO.getKey());
        request.setLoginNo(loginNo);
       
        String params = request.getReqParams();
        String sign = null;
        try {
            sign = Sign.sign(params, globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_PRIVATE_KEY_PATH.getKey()));
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }

        params = params + "&sign=" + sign;
        return params;        
    }

    @Override
    protected String getPrefix() {
        return "sc.sendRandPass.";
    }

}

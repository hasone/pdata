package com.cmcc.vrp.sms.openplatform;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * 开放平台发送短信服务类,需特别注意短信模板部分,qh
 *
 *
 */
public class OPSendMessageServiceImpl implements SendMessageService {
    
    @Autowired
    private GlobalConfigService globalConfigService;
    
    private final Gson gson = new GsonBuilder().create();
    
    private static final Log logger = LogFactory.getLog(OPSendMessageServiceImpl.class);
    
    /**
     * apiKey
     */
    private String getApikey(){
        //return "9b3a7827ece947238efdf1f08d98c4dd";
        return globalConfigService.get(GlobalConfigKeyEnum.OPEN_PLATFORM_SMS_APIKEY.getKey());
    }
    
    /**
     * getAppSecret
     */
    private String getAppSecret(){
        //return "2124e16970354338a94c4492d4ecf534";
        return globalConfigService.get(GlobalConfigKeyEnum.OPEN_PLATFORM_SMS_APPSECRET.getKey());
    }
    
    /**
     * getMessageSign
     */
    private String getMessageSign(){
        //return "和力云平台";
        return globalConfigService.get(GlobalConfigKeyEnum.OPEN_PLATFORM_SMS_MESSAGESIGN.getKey());
    }
    
    /**
     * getSendUrl
     */
    private String getSendUrl(){
        //return "http://172.20.11.200/api/v1/sms/send";
        //return "http://www.cmccheli.com/api/v1/sms/send";
        return globalConfigService.get(GlobalConfigKeyEnum.OPEN_PLATFORM_SMS_SENDURL.getKey());
    }
    
    /**
     * 开放平台模板编号,需要联系对方进行设置
     */
    private int getTemplateId(){
        //return 29;
        String idStr = globalConfigService.get(GlobalConfigKeyEnum.OPEN_PLATFORM_SMS_TEMPLATEID.getKey());
        return NumberUtils.toInt(idStr);
    }
    
    @Override
    public boolean send(SmsPojo smsPojo) {
        SmsBody smsBody = generateSmsbody(smsPojo);
     
        Map<String, String> map =generateHeader(smsBody);
        String result = HttpUtils.post(getSendUrl(), gson.toJson(smsBody), "application/json", map);
        
        //解析返回对象,判断是否正确
        try{
            JSONObject resultObject = JSONObject.parseObject(result);  
            return resultObject != null && 
                    "200".equals(resultObject.getString("resultCode"));
           
        }catch(JSONException e){
            logger.error("解析json失败,返回数据为"+result);
            return false;
        }
        
    }

    /**
     * 生成发送json对象
     */
    private SmsBody generateSmsbody(SmsPojo smsPojo){
        SmsBody smsBody = new SmsBody();
        List<String> mobiles = new ArrayList<String>();
        mobiles.add(smsPojo.getMobile());
                
        smsBody.setMessageSign(getMessageSign());
        smsBody.setMobiles(mobiles);

        //构建短信模板
        SmsBasicTemplate smsTemplate = SmsTempFactory.getTemplate(getTemplateId(),smsPojo.getContent());
        if(smsTemplate != null){
            smsTemplate.setTemplateProperties(smsBody);
        }
        
        return smsBody;    
    }
    
    /**
     * 生成Header
     */
    private Map<String, String> generateHeader(SmsBody smsBody){
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", getAuthorization(smsBody));
        return headerMap;       
    }
    
    /**
     * 加密数据
     * 
     * 生成方法为：
            1、  MD5编码apiKey+secretKey+time所拼接的字符串，亦即signStr= MD5(${apiKey} + ${secretKey} +${ time})；
            2、  将MD5编码之后的字符串和apiKey，time一并处理为JSON字符串，亦即
                jsonStr = {
                "apiKey": "b03596215489417089131859ca769718",
                "time": "1459217778516",
                "sign": ${signStr}
                }
            3、  将JSON串进行Base64编码然后填充到头部，Authorization=Base64(jsonStr)
                                     其中time为时间戳。Base64的作用是将json字符串编码，采用的apache.commons.codec提供的编码方法（建议采用该包做base64处理）。

     */
    private String getAuthorization(SmsBody smsBody){
        long timeStamp = new Date().getTime();
       
        String md5Sign = DigestUtils.md5Hex(getApikey()+getAppSecret() + String.valueOf(timeStamp));

        Authorization auth = new Authorization(getApikey(), String.valueOf(timeStamp), md5Sign);
        
        return Base64.encodeBase64String(gson.toJson(auth).getBytes());

    }
  
    /**
     * 内部类加密数据类
     *
     */
    private class Authorization{
        @SerializedName(value = "apiKey")
        private String apiKey;
        
        @SerializedName(value = "time")
        private String time;
        
        @SerializedName(value = "sign")
        private String sign;

        public Authorization(String apiKey, String time, String sign) {
            this.apiKey = apiKey;
            this.time = time;
            this.sign = sign;
        }    
    }
   
    
    /*public static void main(String[] args){
        OPSendMessageServiceImpl service = new OPSendMessageServiceImpl();
        SmsPojo smsPojo = new SmsPojo();
        smsPojo.setContent("hello");
        smsPojo.setMobile("18867102087");
        service.send(smsPojo);
    }*/
    
    
}

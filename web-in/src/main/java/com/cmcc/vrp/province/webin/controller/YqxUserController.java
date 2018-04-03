package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.gansu.HttpRespModel;
import com.cmcc.vrp.boss.gansu.HttpUtil;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.AESdecry;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.yqx.model.YqxRespFailModel;
import com.cmcc.vrp.yqx.model.YqxRespSuccModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 云企信登录controller
 *
 */
@Controller
@RequestMapping("/yqx/user")
public class YqxUserController extends BaseController{
    private final static Logger LOGGER = LoggerFactory.getLogger(YqxUserController.class);
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    /**
     * VALID_ORIGINALID
     */
    private static final String VALID_ORIGINALID_SC = "zyscyqx";
    private static final String VALID_ORIGINALID_CQ = "zycqyqx";
    
    /**
     * ASPIRE_SHARE_KEY
     */
    private static final String ASPIRE_SHARE_KEY = "aspire_share_aes";
    
    /**
     * LLPT_SHARE_KEY_SC
     */
    private static final String LLPT_SHARE_KEY_SC = "llpt_share_scyqx";
    /**
     * LLPT_SHARE_KEY_CQ
     */
    private static final String LLPT_SHARE_KEY_CQ = "llpt_share_cqyqx";
    
   
    

    /**
     * 云企信登录鉴权使用
     */
    @RequestMapping(value = "loginReq")
    public void loginReq(HttpServletRequest req,HttpServletResponse res,ModelMap modelMap) throws IOException{

        //1.接收云企信的数据，解密得到Authorization
        String decryMsg = analyseYqxLoginReq(req);
        if(StringUtils.isBlank(decryMsg)){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);           
            String json = getErrMsg("message decry error.");
            res.getWriter().println(json);
            LOGGER.info("返回给云企信数据:" + json);
            return;
        }
        LOGGER.info("解密后数据为:" + decryMsg);
         
        String authorization = "";
        String originId = "";//zyscyqx 或者是 zycqyqx
        String userId = "";
        
        try{
            JSONObject jsonObject = JSONObject.parseObject(decryMsg);
            if(jsonObject == null){
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String json = getErrMsg("recv message empty.");
                res.getWriter().println(json);
                LOGGER.info("返回给云企信数据:" + json);
                return;
            }
            
            originId = jsonObject.getString("originId");
            authorization = jsonObject.getString("authorization");
            userId = jsonObject.getString("userId");
            
            String checkResult = checkReqValid(originId,authorization,userId);
            if(StringUtils.isNotBlank(checkResult)){
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);               
                String json = getErrMsg(checkResult);
                res.getWriter().println(json);
                LOGGER.info("返回给云企信数据:" + json);  
                return;
            }
    
        }catch(JSONException e){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String json = getErrMsg("recv json error.");
            res.getWriter().println(json);
            LOGGER.info("返回给云企信数据:" + json);  
            return;
        }   


        //向云企信发送请求,获取手机号,
        String url = "";
        String llptKey = "";
        String address = "";
        if(VALID_ORIGINALID_SC.equals(originId)){//之前已经做过VALID_ORIGINALID_SC和CQ判断
            url = getYqxUrlSC();
            llptKey = LLPT_SHARE_KEY_SC;
            address = getScReturnAddress();
        }else if(VALID_ORIGINALID_CQ.equals(originId)){
            url = getYqxUrlCQ();
            llptKey = LLPT_SHARE_KEY_CQ;
            address = getCqReturnAddress();
        }
        
        
        //向云企信发送请求,获取手机号,
        /*String mobile = getPhoneFromYqx(originId,authorization,userId,url,llptKey);
        if(StringUtils.isBlank(mobile)){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);           
            String json = new Gson().toJson(getErrMsg("get phoneNum from aspire failed."));
            res.getWriter().println(json);
            LOGGER.info("返回给云企信数据:" + json);  
            return;
        }*/
        YqxResultModel model = getPhoneFromYqx(originId,authorization,userId,url,llptKey);
        String mobile = "";
        if(!model.isSuccess()){
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);           
            String json = getErrMsg(model.getResult());
            res.getWriter().println(json);
            LOGGER.info("返回给云企信数据:" + json);  
            return;
        }else{
            mobile = model.getResult();
        }
        
        //获取手机号后，1.返回加密后的地址给云企信
        LOGGER.info("得到手机号:"+mobile);


        //返回正确信息和地址给云企信，地址并带上当前时间,进行加密
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());        
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create(); //gson不自动转换特殊字符
        
        String data = currentTime +"&" + mobile +"&" +originId;
        
        /*address = address +  "?time=" + currentTime 
                + "&mobile=" + mobile + "&originalId=" + originId;*/
        
        address = address + "?key=" +
                URLEncoder.encode(AESdecry.encrypt(data, getYqxEncryKey()), "utf-8");
        
        String encryAddress = AESdecry.encrypt(address , llptKey);
        String json = gson.toJson(new YqxRespSuccModel(0,
                encryAddress));
               
        res.getWriter().println(json);
        res.setStatus(HttpServletResponse.SC_OK);  
        LOGGER.info("返回给云企信数据:" + json);  
        LOGGER.info("解密数据src:{},结果为:{}",encryAddress,AESdecry.decrypt(encryAddress, llptKey));
        return;
    }
    
    /**
     * 获取和解码云企信发送的登录数据
     */
    private String analyseYqxLoginReq(HttpServletRequest req){
        String encryMsg = req.getHeader("key");
        LOGGER.info("从云企信用户接口Header中收到加密数据:" + encryMsg);
        
        String decryMsg = AESdecry.decrypt(encryMsg, ASPIRE_SHARE_KEY);
        if(StringUtils.isNotBlank(decryMsg)){
            return decryMsg;
        }
        
        encryMsg = req.getParameter("key");
        LOGGER.info("从云企信用户接口Param中收到加密数据:" + encryMsg);
        return AESdecry.decrypt(encryMsg, ASPIRE_SHARE_KEY);
    }

  
    
    /**
     * 
     * @param errMsg
     * @return
     */
    private String getErrMsg(String errMsg){
        return new Gson().toJson(
                new YqxRespFailModel("invalid_req",errMsg));
    }
    
    /**
     * 
     * @param originId
     * @param authorization
     * @return
     */
    private String checkReqValid(String originId,String authorization,String userId){
        if(!VALID_ORIGINALID_SC.equals(originId) && !VALID_ORIGINALID_CQ.equals(originId)){
            return "originId invalid";
        }
        
        if(VALID_ORIGINALID_CQ.equals(originId) && StringUtils.isBlank(userId)){
            return "userId missed";
        }
        
        if(StringUtils.isBlank(authorization)){
            return "authorization error";
        }
        
        return "";
    }
    
    /**
     * 如果正确，返回云企信解密密的数据，手机号
     * 如果错误，返回空
     */
    private YqxResultModel getPhoneFromYqx(String originId,String authorization,
            String userId,String url,String llptKey){
        Map<String, String> headerMap = new HashMap<String, String>();
        if(VALID_ORIGINALID_SC.equals(originId)){
            headerMap.put("Authorization", "Bearer " + authorization);
        }else if(VALID_ORIGINALID_CQ.equals(originId)){
            headerMap.put("Authorization", authorization);
            headerMap.put("userId", userId);
        }else{
            return new YqxResultModel(false, "originId错误");
        }
             
        LOGGER.info("向云企信获取手机号,请求参数：" + headerMap.toString() + ",发送时间：" + new Date());
                
        HttpRespModel respModel = HttpUtil.doPostRespCode(url, "", "utf-8", false, headerMap);
        LOGGER.info("云企信返回状态码:{},信息:{}",respModel.getCode(),respModel.getMsg());
        
        if(HttpStatus.SC_OK != respModel.getCode() && HttpStatus.SC_BAD_REQUEST != respModel.getCode()
                && HttpStatus.SC_UNAUTHORIZED != respModel.getCode()){
            return new YqxResultModel(false, "网络错误，无法连接到云企信获取手机号");
        }
        
        //从json得到src字段
        String jsonSrc ="";
        try{
            JSONObject jsonObject = JSONObject.parseObject(respModel.getMsg());
            if(jsonObject == null){
                return new YqxResultModel(false, "云企信返回字符串为空");
            } 
            
            jsonSrc = jsonObject.getString("src");
            
            if(StringUtils.isBlank(jsonSrc)){
                String errorMsg ="";
                if(VALID_ORIGINALID_CQ.equals(originId)){
                    //{"error":510,"msg":"USER_TOKEN_OUTOFDATE"}
                    errorMsg = jsonObject.getString("msg");
                }else{
                    //{"error": "invalid_token","error_description": "Invalid access token: 0f105fd8-ee7b-4440-9089-44400e19c6d"
                    errorMsg = jsonObject.getString("error_description");
                 }
                if(StringUtils.isBlank(errorMsg)){
                    return new YqxResultModel(false, "云企信返回json,不存在src字段");
                }else{
                    return new YqxResultModel(false, errorMsg);
                }
            }
            
        }catch(JSONException e){
            return new YqxResultModel(false, "云企信返回json,无法解析");
        } 
        
        //开始解码src字段
        String decryptCode = AESdecry.decrypt(jsonSrc, llptKey);
        if(StringUtils.isBlank(decryptCode)){
            LOGGER.info("解码后失败,源数据为:" + jsonSrc);
            return new YqxResultModel(false, "云企信返回json,src字段解码失败");
        }
        
        LOGGER.info("解码后的数据为:" + decryptCode);//1674972242512896&18867105325          
        String[] parties = decryptCode.split("&");
        
        String mobile = parties.length<2 ? "":parties[1];
        if(StringUtils.isBlank(mobile)){
            return new YqxResultModel(false, "云企信返回json,src字段解码无法获取手机号");
        }else{
            return new YqxResultModel(true, mobile);
        }
        
    }
    
    
    /**
     * 
     * 四川云企信地址
     */
    private String getYqxUrlSC(){
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_SC_CONNECT_REQUEST_URL.getKey());
    }
    
    /**
     * 
     * 重庆云企信地址
     */
    private String getYqxUrlCQ(){
        //return "http://172.23.28.70:9109/front/users/checkToken";
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_CONNECT_REQUEST_URL.getKey());
    }
 
    /**
     * 我们返回给四川云企信的地址
     */
    private String getScReturnAddress(){
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_SC_CONNECT_RESPONSE_URL.getKey());
    }
    
    /**
     * 我们返回给重庆云企信的地址
     */
    private String getCqReturnAddress(){
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_CONNECT_RESPONSE_URL.getKey());
        //return "http://xj-qa.4ggogo.com/web-in/yqx/order/cqYqxEntry.html";
    }
    
    /**
     * 我们给云企信平台返回数据加密使用的key
     */
    private String getYqxEncryKey(){
        return "llpt_private_yqx";
    }
    
    /**
     * 云企信返回结果组装类
     *
     */
    private class YqxResultModel{
        private boolean isSuccess = false;
        private String result = "";
        
        public YqxResultModel(boolean isSuccess, String result) {
            this.isSuccess = isSuccess;
            this.result = result;
        }
        public boolean isSuccess() {
            return isSuccess;
        }

        public String getResult() {
            return result;
        }   
    }
}

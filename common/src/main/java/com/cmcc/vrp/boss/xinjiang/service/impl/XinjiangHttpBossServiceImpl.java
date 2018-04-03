package com.cmcc.vrp.boss.xinjiang.service.impl;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.xinjiang.enums.TransCode;
import com.cmcc.vrp.boss.xinjiang.request.json.model.NewSend;
import com.cmcc.vrp.boss.xinjiang.request.json.model.OldSend;
import com.cmcc.vrp.boss.xinjiang.request.json.model.ResourcePool;
import com.cmcc.vrp.boss.xinjiang.response.GroupInfoResp;
import com.cmcc.vrp.boss.xinjiang.response.NewResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.ResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.SendResp;
import com.cmcc.vrp.boss.xinjiang.service.XinjiangBossService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
 * XinjiangHttpBossServiceImpl ,http版新版接口
 *
 */
@Service("xinjiangHttpBossService")
public class XinjiangHttpBossServiceImpl implements XinjiangBossService {
    
    private static final Logger logger = LoggerFactory.getLogger(XinjiangHttpBossServiceImpl.class);
    
    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 平台不使用
     */
    public GroupInfoResp getGetGroupInfo(String groupId) {
        String postUrl = generateurl(TransCode.GetGroupInfo.getHttpCode(),getAppId(),getAppSecret(),"1");
        String postData = new Gson().toJson(new ResourcePool(groupId));
        String str = HttpUtils.post(postUrl, postData, ContentType.APPLICATION_JSON.getMimeType());
        System.out.println("返回数据"+str);
        return null;
    }

    /**
     * 平台不使用
     */
    public ResourcePoolResp getResourcePoolResp(String groupId) {
        /**
        {"OUTDATA":[{"ALL_INIT_VALUE":"3840000","ALL_VALUE":"150532","END_DATE":"20170731235959","RES_ID":"210000157487597","START_DATE":"20160701000000","USER_ID":"1616070114209535"}],"X_NODE_NAME":"app-node02-srv01:2c01a3acbc3345ffbf21d2833a89f24a:1497267241191","X_RECORDNUM":"1","X_RESULTCODE":"0","X_RESULTINFO":"ok"}}
        */
        /**
        {"OUTDATA":[{"ALL_INIT_VALUE":"3840000","ALL_VALUE":"150532","END_DATE":"20170731235959","RES_ID":"210000157487597","START_DATE":"20160701000000","USER_ID":"1616070114209535"}],"X_NODE_NAME":"app-node02-srv01:2c01a3acbc3345ffbf21d2833a89f24a:1497267241191","X_RECORDNUM":"1","X_RESULTCODE":"0","X_RESULTINFO":"ok"}}
        */
        String postUrl = generateurl(TransCode.QueryResourcePool.getHttpCode(),getAppId(),getAppSecret(),"1");
        String postData = new Gson().toJson(new ResourcePool(groupId));
        String str = HttpUtils.post(postUrl, postData, ContentType.APPLICATION_JSON.getMimeType());
        System.out.println("返回数据"+str);
        return null;
    }

    /**
     * 5.6. 集团用户产品流量池信息查询 参数： group_id：集团ID
     * 
     * 返回： ResourcePoolResp 集团的基本信息，详情请参考该类
     * 
     * 新接口：XINJ_UNHQ_queryResourcePool
                    原接口：ITF_FLHQ_QueryResourcePool

     */
    public NewResourcePoolResp getResourcePoolRespNew(String groupId) {
        // 设定访问的类型
        String postUrl = generateurl(TransCode.QueryResourcePoolNew.getHttpCode(),getAppId(),getAppSecret(),"1");
        String postData = new Gson().toJson(new ResourcePool(groupId));
        
        String str = HttpUtils.post(postUrl, postData, ContentType.APPLICATION_JSON.getMimeType());
        logger.info("groupId:"+groupId+",查询余额返回数据"+str);
        return analyseResultNRPR(str);
        
        /**
         {"respCode":"0","respDesc":"ok","result":{"OUTDATA":{"ALL_VALUE":"288039.0","END_DATE":"20180228235959","START_DATE":"20170216000000"},"X_NODE_NAME":"app-node02-srv01:227496d9bb1a44fa9d2c53edd73ce008:1497333052962","X_RECORDNUM":"1","X_RESULTCODE":"0","X_RESULTINFO":"ok"}}
         */
    }
    
    /**
     * getResourcePoolRespNew的分析json数据
     */
    public NewResourcePoolResp analyseResultNRPR(String result){
        NewResourcePoolResp resp = new NewResourcePoolResp();
        try{
            JSONObject jsonObject = JSONObject.parseObject(result);
            if(jsonObject == null){
                resp.setResultCode("-1");
                resp.setResultInfo("boss无响应数据");
                return resp;
            }
            JSONObject resultObject = jsonObject.getJSONObject("result");
            if(resultObject == null){
                resp.setResultCode("-1");
                resp.setResultInfo("boss返回数据解析错误");
                return resp;
            }
            String resultCode = resultObject.getString("X_RESULTCODE");
            String resultInfo = resultObject.getString("X_RESULTINFO");
            if(StringUtils.isBlank(resultCode) || StringUtils.isBlank(resultInfo)){
                resp.setResultCode("-1");
                resp.setResultInfo("boss返回数据解析错误");
                return resp;
            }

            resp.setResultCode(resultCode);
            resp.setResultInfo(resultInfo);
            if (!"0".equals(resultCode)){
                return resp;
            }else{
                JSONObject outDataObject = resultObject.getJSONObject("OUTDATA");
                if(outDataObject == null){
                    resp.setResultCode("-1");
                    resp.setResultInfo("boss返回数据解析错误");
                    return resp;
                }
                
                String addValue = outDataObject.getString("ALL_VALUE");
                if(StringUtils.isBlank(addValue)){
                    resp.setResultCode("-1");
                    resp.setResultInfo("boss返回数据解析错误");
                }else{
                    resp.setAddValue(addValue);
                    resp.setStartDate(outDataObject.getString("START_DATE"));
                    resp.setEndDate(outDataObject.getString("END_DATE"));
                }
                return resp;                
            }   
            
        }catch(JSONException e){
            resp.setResultCode("-1");
            resp.setResultInfo("boss返回数据解析错误");
            return resp;
        }
        
    }

    /**
     * 老版的赠送
     */
    public SendResp getSendResp(String groupId, String userId, String phone,
            String flowNum, String serialNum) {
        // 设定访问的类型
        String postUrl = generateurl(TransCode.TcsGrpIntf.getHttpCode(),getAppId(),getAppSecret(),"1");
        String postData = new Gson().toJson(new OldSend(groupId, userId, phone, flowNum, getRandomString(24)));
        String str = HttpUtils.post(postUrl, postData, ContentType.APPLICATION_JSON.getMimeType());
        logger.info("groupId:{},userId:{},phone:{},flowNum:{},充值返回数据{}",groupId,userId,phone,flowNum,str);
        
        /**
        {"respCode":"0","respDesc":"ok","result":{"OUTDATA":{"ACCESS_NUM":"13999867721","ORDER_ID":"9117061351908966","ORDER_LINE_ID":"9117061352157885","ORDER_TYPE_CODE":"5092","ROUTE_CODE":"0991","printFlag":"false"},"X_NODE_NAME":"app-node02-srv01:265fa7ac876949df8f9891f9231faaa0:1497333334744","X_RECORDNUM":"1","X_RESULTCODE":"0","X_RESULTINFO":"ok"}}
         */
        
        return analyseResultSendResp(str);
    }
    
    

    /**
     * 新版的赠送
     */
    public SendResp getNewSendResp(String groupId, String phone,
            String flowNum, String serialNum) {
        // 设定访问的类型
        String postUrl = generateurl(TransCode.TcsGrpIntfNew.getHttpCode(),getAppId(),getAppSecret(),"1");
        String postData = new Gson().toJson(new NewSend(phone, groupId, flowNum, getRandomString(24)));
        String str = HttpUtils.post(postUrl, postData, ContentType.APPLICATION_JSON.getMimeType());
        logger.info("groupId:{},phone:{},flowNum:{},充值返回数据{}",groupId,phone,flowNum,str);
        return analyseResultSendResp(str);
        
        /**
       {"respCode":"0","respDesc":"ok","result":{"OUTDATA":{"ACCESS_NUM":"13999867721","ORDER_ID":"9117061251881871","ORDER_LINE_ID":"9117061252126679","ORDER_TYPE_CODE":"5092","ROUTE_CODE":"0991","printFlag":"false"},"X_NODE_NAME":"app-node02-srv01:515766934845481a9926e32a1aab4be8:1497267474779","X_RECORDNUM":"1","X_RESULTCODE":"0","X_RESULTINFO":"ok"}}
         */
    }
    
    
    /**
     * 分析充值json返回结果
     */
    private SendResp analyseResultSendResp(String result){
        SendResp resp =new SendResp();
        try{
            JSONObject jsonObject = JSONObject.parseObject(result);
            if(jsonObject == null){
                resp.setResultCode("-1");
                resp.setResultInfo("boss无响应数据");
                return resp;
            }
            JSONObject resultObject = jsonObject.getJSONObject("result");
            if(resultObject == null){
                resp.setResultCode("-1");
                resp.setResultInfo("boss返回数据解析错误");
                return resp;
            }
            String resultCode = resultObject.getString("X_RESULTCODE");
            String resultInfo = resultObject.getString("X_RESULTINFO");

            if(StringUtils.isBlank(resultCode) || StringUtils.isBlank(resultInfo)){
                resp.setResultCode("-1");
                resp.setResultInfo("boss返回数据解析错误");
                return resp;
            }else{
                resp.setResultCode(resultCode);
                resp.setResultInfo(resultInfo);
                return resp;
            }
            
            
        }catch(JSONException e){
            resp.setResultCode("-1");
            resp.setResultInfo("boss返回数据解析错误");
            return resp;
        }
        
    }
    
    /**
     * 生成完整的访问地址
     */
    private String generateurl(String abilityCode,String appId,String appKey,String status){
        
        String publicParam = "method="+abilityCode+"&format=json&appId=" +appId+
                "&status=1&timestamp=20141225144312&"
                + "provinceCode="+getProvinceCode()+"&tradeStaffId="+getTradeStaffId()+"&"
                + "tradeEparchyCode="+getTradeEparchyCode()+"&tradeCityCode="+getTradeCityCode()+"&"
                + "tradeDepartId="+getTradeDepartId()+"&channelTypeId="+getChannelTypeId()+"&flowId="+
                getRandomString(16)+"&tradeDepartName="+getTradeDepartName()+"&"
                        + "tradeStaffName="+getTradeStaffName()+"&tradeDepartPassWd="+getTradeDepartPassWd()+"&"
                        + "appKey="+appKey;
        return getUrl() + publicParam;
    }
    
    /**
     * getUrl
     */
    private String getUrl(){
        //return "http://10.238.98.31:7001/oppf?";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_URL.getKey());
    }
    
    /**
     * getAppId
     */
    private String getAppId(){
        //return "510607";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_APPID.getKey());
    }
    
    /**
     * getAppSecret
     */
    private String getAppSecret(){
        //return "61b8a845738bc9690910bb7e787f21b2";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_APPSECRET.getKey());
    }
    
    /**
     * getProvinceCode
     */
    private String getProvinceCode(){
        //return "XINJ";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_PROVINCECODE.getKey());
    }
    
    /**
     * getTradeEparchyCode
     */
    private String getTradeEparchyCode(){
        //return "0991";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_TRADEEPARCHYCODE.getKey());
    }
    
    /**
     * getTradeStaffId
     */
    private String getTradeStaffId(){
        //return "LLPT0000";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_TRADESTAFFID.getKey());
    }
    
    /**
     * getTradeCityCode
     */
    private String getTradeCityCode(){
        //return "A91R";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_TRADECITYCODE.getKey());
    }
    
    /**
     * getTradeDepartId
     */
    private String getTradeDepartId(){
        //return "00000";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_TRADEDEPARTID.getKey());
    }
    
    /**
     * getChannelTypeId
     */
    private String getChannelTypeId(){
        //return "0";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_CHANNELTYPEID.getKey());
    }
    

    /**
     * getTradeDepartName
     */
    private String getTradeDepartName(){
        //return "1";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_TRADEDEPARTNAME.getKey());
    }
    
    /**
     * getTradeStaffName
     */
    private String getTradeStaffName(){
        //return "ITF";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_TRADESTAFFNAME.getKey());
    }
    
    /**
     * getTradeDepartPassWd
     */
    private String getTradeDepartPassWd(){
        //return "LPT0000";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_TRADEDEPARTPASSWD.getKey());
    }
    
    /**
     * 得到新疆的随机数
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}

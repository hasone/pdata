package com.cmcc.vrp.boss.shangdong.boss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.shangdong.boss.model.DynamicToken;
import com.cmcc.vrp.boss.shangdong.boss.model.ProductMemberFlowSystemPay;
import com.cmcc.vrp.boss.shangdong.boss.model.SmsIctParam;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * 山东对接云平台实现类
 *
 * @author Administrator
 */
@Service
public class SdCloudWebserviceImpl {

    /**
     * getCurrentUserInfo ,getEnterpriseInfo ,dynamicToken 使用该地址
     */
    public static final String PLAT_COMMONSERVICE_URL = "/sdkService/restWs/commonService";
    /**
     * 3、updateProductMemberFlowSystemPay 流量统付产品成员反向同步接口 4、getCurrentMgrInfo 查询客户经理信息接口
     * 5、getEnterprisesInfo  查询客户经理管理企业信息列表接口 6、getMemCurrentInstance 查询成员boss订购信息接口—需要boss和云平台开发
     * 7、getEntCurrentInstance 查询集团业务订购信息接口—需要boss和云平台开发 以上接口使用该地址
     */
    public static final String PLAT_LLPT_URL = "/sdkService/restWs/llpt";
    /**
     * 4.2.2.10 流量统付产品成员反向同步接口
     */
    public static final String UPDATE_PRODUCTMEMBER_URL = "/updateProductMemberFlowSystemPay";
    /**
     * 4.1.3.1	token获取接口
     */
    public static final String DYNAMICTOKEN_URL = "/dynamicToken";
    /**
     * 4.2.2.1 查询用户信息接口
     */
    public static final String QUERY_USER_URL = "/getCurrentUserInfo";
    /**
     * 4.2.2.2 查询企业信息接口
     */
    public static final String QUERY_ENTERPRISE_URL = "/getEnterpriseInfo";
    /**
     * 4.2.2.11 查询客户经理信息接口
     */
    public static final String CURRENTMGRINFO_URL = "/getCurrentMgrInfo";
    /**
     * 4.2.2.12 查询客户经理管理所以企业信息列表接口
     */
    public static final String MGRENTERPRISESINFO_URL = "/getMgrEnterprisesInfo";
    /**
     * 4.2.2.10 查询成员boss订购信息接口—需要boss和云平台开发
     */
    public static final String MEMCURRENTINSTANCE_URL = "/getMemCurrentInstance";
    /**
     * 发送短信接口
     */
    public static final String MESS_URL = "/sendSmsNotice";
    private static final Logger LOGGER = LoggerFactory.getLogger(SdCloudWebserviceImpl.class);
    /**
     * 当前云平台拥有的所有appId
     */
    private static final List<String> APPID_LIST = new ArrayList<String>() {{
        add("1087");
        add("1092");
        add("1099");
    }};
    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 4.2.2.10 流量统付产品成员反向同步接口
     *
     * 流水号编码规则－ 4位平台机构编码+8位业务编码（BIPCode）+14位组包时间YYYYMMDDHH24MISS+6位流水号（定长），序号从000001开始，增量步长为1。 如：
     * LLPTBIPCP00320160929112335000001 LLPT           -4位平台机构编码 云平台为（YPT0)此处需要boss侧确认是否可用 BIPCP003
     * -8位业务编码（BIPCode 20160929112335 -14位组包时间YYYYMMDDHH24MISS 000001         -6位流水号（定长）
     *
     * appToken 则该实现类中传入的值不会被使用，只是为了兼容接口而存在
     *
     *
     * isAsyc 结果是同步还是异步
     */
    public BossOperationResult updateProductMemberFlowSystemPay(ProductMemberFlowSystemPay pm, boolean isAsyc) {

        String dynamicToken = getAppToken(pm.getProductID(), pm.getEnterpriseID());

        if (StringUtils.isEmpty(dynamicToken)) {
            String errMsg = "{\"code\":500,\"msg\":\"充值获取dynamicToken失败\"}";
            LOGGER.error(errMsg);
            return analyseChargeResult(errMsg, isAsyc);
        }

        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.create();
        String jsonString = gson.toJson(pm);

        //String chargeUrl = getPlatUrl() + PLAT_LLPT_URL + UPDATE_PRODUCTMEMBER_URL;
        String chargeUrl = getChargeUrl();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("platformId", getPlatFormId());
        headerMap.put("appToken", dynamicToken);
        headerMap.put("skyform_param_json_ProductMember", jsonString);

        LOGGER.info("向云平台发起充值请求,请求参数：" + headerMap.toString() + ",发送时间：" + new Date());
        String recvmsg = HttpUtils.post(chargeUrl, "", "application/x-www-form-urlencoded", headerMap);
        LOGGER.info("收到云平台响应报文：" + recvmsg + ",接收时间为：" + new Date());

        return analyseChargeResult(recvmsg, isAsyc);
    }

    /**
     * @param responseString http返回结果
     */
    private BossOperationResult analyseChargeResult(String responseString, boolean isAsyc) {
        try {
            if (responseString != null) {
                JSONObject jo = JSONObject.parseObject(responseString);

                String code = jo.getString("code");
                String msg = jo.getString("msg");

                /**
                 * 缺少错误信息，直接返回默认错误。
                 */
                if (StringUtils.isEmpty(code) ||
                        StringUtils.isEmpty(msg)) {
                    return SdCloudOperationResultImpl.initHttpErrResult(isAsyc);
                }


                SdCloudOperationResultImpl result =
                        new SdCloudOperationResultImpl(code, msg, isAsyc);

                return result;
            } else {
                return SdCloudOperationResultImpl.initHttpErrResult(isAsyc);
            }
        } catch (JSONException e) {
            LOGGER.error(e.getMessage());
            return SdCloudOperationResultImpl.initHttpErrResult(isAsyc);
        }

    }

    /**
     * 4.1.3	无登录获取Token
     *
     * @param appId        1087 or 1092
     * @param enterpriseId boss端的企业编码
     * @return 得到则返回具体Token值，没有得到直接返回null
     */

    public String getAppToken(String appId, String enterpriseId) {
        String url = getDynamicTokenTokenUrl();

        //设置header
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("dy_platformId", getPlatFormId());
        headerMap.put("enterpriseId", enterpriseId);
        headerMap.put("dy_appId", appId);

        LOGGER.info("向云平台获取token,请求参数：" + headerMap.toString() + ",发送时间：" + new Date());
        String result = HttpUtils.post(url, "", "application/x-www-form-urlencoded", headerMap);
        LOGGER.info("向云平台获取token,返回结果：" + result);

        if (StringUtils.isEmpty(result)) {
            return null;
        }

        //返回范例 {"code":200,"msg":"ok","token":"cc99cfc7-05a2-43b9-b1e9-dde00d59d591"}
        try {
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            DynamicToken token = gson.fromJson(result, DynamicToken.class);

            return token.getToken();
        } catch (JsonSyntaxException e) {
            LOGGER.info(e.getMessage());
            LOGGER.info("json 解析失败，字符串为:" + result);
            return null;
        }
    }

    /**
     * 4.2.2.1  企业关键人查询用户信息接口
     */
    public String getCurrentUserInfo(String userToken) {
        String url = getPlatUrl() + PLAT_COMMONSERVICE_URL + QUERY_USER_URL;

        Map<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("platformId", getPlatFormId());
        headerMap.put("userToken", userToken);

        LOGGER.info("向云平台获取查询企业关键人信息接口,请求参数：" + headerMap.toString() + ",发送时间：" + new Date());

        String result = HttpUtils.post(url, "", "application/x-www-form-urlencoded", headerMap);

        LOGGER.info("getCurrentUserInfo url:" + url);
        LOGGER.info("getCurrentUserInfo result:" + result);

        return result;
    }


    /**
     * 4.2.2.2 查询企业信息接口,，与boss对接，返回List<>,内容是enterpriseId,如错误或空
     */
    public List<String> getEnterpriseInfoList(String userToken) {
        List<String> resultList = new ArrayList<String>();

        //遍历APPID_LIST,调用boss，将结果添加到resultList
        for (String appId : APPID_LIST) {
            getEnterpriseInfoByAppId(userToken, appId, resultList);
        }
        return resultList;
    }

    //调用云平台接口4.2.2.2，将结果中的id添加到enterpriseIds中

    /**
     * 调用云平台接口4.2.2.2，将结果中的id添加到enterpriseIds中
     */
    private void getEnterpriseInfoByAppId(String userToken, String appId, List<String> enterpriseIds) {
        String url = getPlatUrl() + PLAT_COMMONSERVICE_URL + QUERY_ENTERPRISE_URL;

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("platformId", getPlatFormId());
        headerMap.put("appToken", userToken);
        headerMap.put("appId", appId);

        LOGGER.info("向云平台获取查询企业信息接口,请求参数：" + headerMap.toString() + ",发送时间：" + new Date());

        String result = HttpUtils.post(url, "", "application/x-www-form-urlencoded", headerMap);

        LOGGER.info("getEnterpriseInfo url:" + url);
        LOGGER.info("getEnterpriseInfo result:" + result + " appId:" + appId);

        try {
            JSONObject jo = JSONObject.parseObject(result);
            String code = jo.getString("code");
            if (code != null && NumberUtils.toInt(code) == 200) {
                JSONArray info = (JSONArray) jo.get("enterprises");
                for (int i = 0; i < info.size(); i++) {
                    JSONObject j = (JSONObject) info.get(i);

                    String eid = j.getString("id");
                    if (!enterpriseIds.contains(eid)) {
                        enterpriseIds.add(j.getString("id"));
                    }
                }

            }
        } catch (JSONException e) {
            LOGGER.error(e.getMessage());
            return;
        }
    }

    /**
     * 4.2.2.11 查询客户经理信息接口
     */
    public String getCurrentMgrInfo(String userToken) {
        String url = getPlatUrl() + PLAT_LLPT_URL + CURRENTMGRINFO_URL;

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("platformId", getPlatFormId());
        //headerMap.put("userToken", userToken);
        headerMap.put("appToken", userToken);

        LOGGER.info("向云平台获取客户经理信息接口,请求参数：" + headerMap.toString() + ",发送时间：" + new Date());

        String result = HttpUtils.post(url, "", "application/x-www-form-urlencoded", headerMap);
     
        LOGGER.info("getCurrentMgrInfo url:" + url);

        LOGGER.info("getCurrentMgrInfo result:" + result);
        return result;
    }

    /**
     * 4.2.2.12 查询客户经理管理所以企业信息列表接口,返回解析后的List(enterpriseId)
     */
    public List<String> getEnterprisesInfoList(String userToken) {
        List<String> resultList = new ArrayList<String>();

        //遍历APPID_LIST,调用boss，将结果添加到resultList
        for (String appId : APPID_LIST) {
            getEnterprisesInfoByAppId(userToken, appId, resultList);
        }
        return resultList;
    }

    //调用云平台接口4.2.2.12，将结果中的id添加到enterpriseIds中
    private void getEnterprisesInfoByAppId(String userToken, String appId, List<String> enterpriseIds) {
        String url = getPlatUrl() + PLAT_LLPT_URL + MGRENTERPRISESINFO_URL;

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("platformId", getPlatFormId());
        headerMap.put("appToken", userToken);
        headerMap.put("appId", appId);

        LOGGER.info("向云平台获取客户经理相关企业信息接口,请求参数：" + headerMap.toString() + ",发送时间：" + new Date());

        String result = HttpUtils.post(url, "", "application/x-www-form-urlencoded", headerMap);

        LOGGER.info("getCurrentMgrInfo url:" + url);

        LOGGER.info("getCurrentMgrInfo result:" + result + " appId:" + appId);

        try {
            JSONObject jo = JSONObject.parseObject(result);
            String code = jo.getString("code");
            if (code != null && NumberUtils.toInt(code) == 200) {
                JSONArray info = (JSONArray) jo.get("enterprises");
                for (int i = 0; i < info.size(); i++) {
                    JSONObject j = (JSONObject) info.get(i);

                    String eid = j.getString("id");
                    if (!enterpriseIds.contains(eid)) {
                        enterpriseIds.add(j.getString("id"));
                    }
                }

            }
        } catch (JSONException e) {
            LOGGER.error(e.getMessage());
            return;
        } catch (NullPointerException e) {
            LOGGER.error(e.getMessage());
            return;
        }
    }

    /**
     * 4.1.2.10 查询成员boss订购信息接口—需要boss和云平台开发
     *
     * 接口详情：/getMemCurrentInstance 入参String appToken为dynamicToken 入参 pkgSeq 交易流水号（可选，与成员号码2选1）
     * 入参userId 集团用户id（必填，根据该集团用户编码查询用户是否订购当前集团产品业务） 入参 eCMemberTel 成员号码（可选，与交易流水号2选1）
     *
     * 入参platformId 为云平台分配的第三方id
     */
    public BossOperationResult getMemCurrentInstance(String appId, String enterpriseId, String pkgSeq, String userId) {
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(enterpriseId) || StringUtils.isEmpty(pkgSeq) || StringUtils.isEmpty(userId)) {
            return null;
        }

        String dyToken = getAppToken(appId, enterpriseId);

        if (StringUtils.isEmpty(dyToken)) {
            LOGGER.info("反向查询获取dyToken为空");
            return null;
        }

        String url = getPlatUrl() + PLAT_LLPT_URL + MEMCURRENTINSTANCE_URL;


        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("platformId", getPlatFormId());
        headerMap.put("dy_token", dyToken);
        headerMap.put("userId", userId);
        headerMap.put("pkgSeq", pkgSeq);

        LOGGER.info("反向查询url:" + url);
        LOGGER.info("反向查询pkgSeq:" + pkgSeq + " userId:" + userId);

        String result = HttpUtils.post(url, "", "application/x-www-form-urlencoded", headerMap);

        LOGGER.info("反向查询结果:" + result);

        try {
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jo = JSONObject.parseObject(result);

                String code = jo.getString("code");
                String msg = jo.getString("msg");

                /**
                 * 缺少错误信息，直接返回默认错误。
                 */
                if (StringUtils.isEmpty(code) ||
                        StringUtils.isEmpty(msg)) {
                    return null;
                }

                SdCloudOperationResultImpl queryResult =
                        new SdCloudOperationResultImpl(code, msg, true);

                return queryResult;
            } else {
                return null;
            }

        } catch (JSONException e) {
            LOGGER.error(e.getMessage());
            return null;
        }

    }
    
    /**
     * 4.1.2.10 查询成员boss订购信息接口—需要boss和云平台开发
     *
     * 接口详情：/getMemCurrentInstance 入参String appToken为dynamicToken 入参 pkgSeq 交易流水号（可选，与成员号码2选1）
     * 入参userId 集团用户id（必填，根据该集团用户编码查询用户是否订购当前集团产品业务） 入参 eCMemberTel 成员号码（可选，与交易流水号2选1）
     *
     * 入参platformId 为云平台分配的第三方id
     * 
     * 注：本版本只查询，并返回充值为成功的状态为true，其余情况都是false
     * 
     * 
     * 
     */
    public boolean getQueryChargeSuccess(String appId, String enterpriseId, String pkgSeq, String userId) {
        //以下为充值成功示例
        /*{"manager":{"eCMember":{"bizReq":{
            "bizId":"109201","bizOprCode":"","endDate":"20170524000000","limitFlow":"1","oLDBizId":"","oLDSPID":"","oprEffTime":"","sPID":"472528","startDate":"20170520012305"},
            "eCMemberID":"5318067942708","eCMemberTel":"","memberAttr":[],"oprCode":"","productID":"1092"},"pkgSeq":"LLPTBIPCP00320170520012304000001","userID":"5318068909719"},
        "code":200,"msg":"ok"}*/
        
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(enterpriseId) || 
                StringUtils.isEmpty(pkgSeq) || StringUtils.isEmpty(userId)) {
            return false;
        }

        String dyToken = getAppToken(appId, enterpriseId);

        if (StringUtils.isEmpty(dyToken)) {
            LOGGER.info("反向查询获取dyToken为空");
            return false;
        }

        String url = getPlatUrl() + PLAT_LLPT_URL + MEMCURRENTINSTANCE_URL;

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("platformId", getPlatFormId());
        headerMap.put("dy_token", dyToken);
        headerMap.put("userId", userId);
        headerMap.put("pkgSeq", pkgSeq);

        LOGGER.info("反向查询url:" + url);
        LOGGER.info("反向查询pkgSeq:" + pkgSeq + " userId:" + userId);

        String result = HttpUtils.post(url, "", "application/x-www-form-urlencoded", headerMap);

        LOGGER.info("反向查询结果:" + result);

        try {
            JSONObject jo = JSONObject.parseObject(result);
            if(jo==null){
                return false;
            }

            //判断是否code为200
            String code = jo.getString("code"); 
            if (StringUtils.isBlank(code) ||
                    !"200".equals(code)) {
                return false;
            }

            //判断manager节点中，pkgSeq和userID是否和发送时一致
            JSONObject managerJo = (JSONObject)jo.get("manager");
            if(managerJo == null){
                return false;
            }
            String pkgSeqJo = managerJo.getString("pkgSeq");
            String userIdJo = managerJo.getString("userID");
            
            if(!pkgSeq.equals(pkgSeqJo) || !userId.equals(userIdJo)){
                return false;
            }
            
            return true;
            
       
        } catch (JSONException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * 4.2.2.12 通知类短信发送接口
     *
     * 注，返回信息为 {"code": "200","msg": "[{"code":200,"msg":"success","msgId":164883,
     * "msgSize":1,"recvObject":"15169097775","servicePort":"1065701888123"}]"}
     *
     * 不是标准的json，无法解析，因此根据是否包含success判断是否成功
     */
    public boolean sendMessage(SmsIctParam param) {
        String url = getPlatUrl() + PLAT_COMMONSERVICE_URL + MESS_URL;

        String jsonString = smsIctParamToJson(param);

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("platformId", getPlatFormId());
        headerMap.put("skyform_param_json_param", jsonString);

        LOGGER.info("短信 url:" + url);
        LOGGER.info("短信 param:" + jsonString);

        String result = HttpUtils.post(url, "", "application/x-www-form-urlencoded", headerMap);

        LOGGER.info("短信结果:" + result);

        if (StringUtils.isNotEmpty(result) && result.contains("success")) {
            return true;
        }

        return false;
    }

    private String smsIctParamToJson(SmsIctParam param) {
        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("yyyy-MM-dd HH:mm:ss");
        com.google.gson.Gson gson = gb.create();

        String jsonString = gson.toJson(param);
        return jsonString;
    }

    /**
     * 云平台IP地址
     */
    public String getPlatUrl() {
        //return "http://211.137.190.207:8089";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_CLOUD_URL.getKey());
    }

    /**
     * 云平台设置的PLATFORM_ID
     */
    public String getPlatFormId() {
        //return "SDLLPT_20161018";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_CLOUD_PLATFORMID.getKey());
    }

    /**
     * 山东云平台充值URL地址
     */
    public String getChargeUrl() {
        //return "http://211.137.190.207:8089/sdkService/restWs/llpt/updateProductMemberFlowSystemPay";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_CLOUD_CHARGE_URL.getKey());
    }

    /**
     * 山东云平台充值URL地址
     */
    public String getDynamicTokenTokenUrl() {
        //return "http://211.137.190.207:8089/sdkService/restWs/commonService/dynamicToken";
        return globalConfigService.get(GlobalConfigKeyEnum.SD_DYNAMICTOKEN_UTL.getKey());
    }
}

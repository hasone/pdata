package com.cmcc.vrp.boss.tianjin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.tianjin.model.ChargeResponse;
import com.cmcc.vrp.boss.tianjin.model.TjBossOperationResultImpl;
import com.cmcc.vrp.boss.tianjin.util.GlobalConfig;
import com.cmcc.vrp.boss.tianjin.util.Sign;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月11日 上午10:31:55
*/
@Service("TjBossService")
public class TjBossServiceImpl implements BossService{
    private final static Logger LOGGER = LoggerFactory.getLogger(TjBossServiceImpl.class);

    @Autowired
    SupplierProductService supplierProductService;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    Gson gson;
    
    @Autowired
    SerialNumService serialNumService;
    
    @Autowired
    GlobalConfig globalConfig;
    
    @Autowired
    GetProductInfo getProductInfo;

//    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");
//        TjBossServiceImpl bossService = (TjBossServiceImpl)context.getBean("TjBossService");
//        
//        TjBossServiceImpl tjBossServiceImpl = new TjBossServiceImpl();
//        String sysNum = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//        bossService.charge(null, null, null, sysNum, null);
//        BossOperationResult result = tjBossServiceImpl.charge(null, null, "18222016575", sysNum, null);
//        System.out.println(result.getResultCode());
//        System.out.println(result.getResultDesc());
//        System.out.println(result.isSuccess());
//        result = tjBossServiceImpl.charge(null, null, "15002222539", sysNum, null);
//        System.out.println(result.getResultCode());
//        System.out.println(result.getResultDesc());
//        System.out.println(result.isSuccess());
//        result = tjBossServiceImpl.charge(null, null, "15222359828", sysNum, null);
//        System.out.println(result.getResultCode());
//        System.out.println(result.getResultDesc());
//        System.out.println(result.isSuccess());
//    }
    
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("天津充值开始");
        String requestTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        SupplierProduct supplierProduct = null;
        Enterprise enterprise = null;
        if (splPid == null
               || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
               || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null
               || StringUtils.isBlank(mobile)
               || StringUtils.isBlank(serialNum)) {
            LOGGER.info("天津充值失败：参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return null;
        }
        String entCode = enterprise.getCode();
        String productCode = supplierProduct.getCode();
        String productId = JSONObject.fromObject(supplierProduct.getFeature()).getString("productId");
//        String entCode = "2206409888";
//        String productCode = "16112801";
//        String productId = "7178";
        String userId = getProductInfo.getUrlID(entCode, productId);
        if(StringUtils.isBlank(userId)) {
            LOGGER.error("获取userid失败");
            return new TjBossOperationResultImpl("1001", "获取userid失败");
        }
        Map<String, String> sysParams = buildSysParam(serialNum, requestTime);
        String busiParams = buildBusiParam(entCode, productCode, mobile, serialNum, userId, supplierProduct);
        String sign = Sign.generateSign(sysParams, busiParams, globalConfig.getPublicKey());
        String url = buildUrl(serialNum, requestTime) + "&sign=" + sign;
        LOGGER.info("天津BOSS渠道充值url:{}", url);
        LOGGER.info("天津BOSS渠道充值请求:{}", busiParams);
        LOGGER.info("天津BOSS渠道充值请求：{}", sysParams);
//        System.out.println(sysParams);
//        System.out.println(busiParams);
//        System.out.println(sign);
//        System.out.println(url);
        String response = HttpUtils.post(url, busiParams, "text/plain");
        ChargeResponse chargeResponse = null;
        if (StringUtils.isNotBlank(response)
                && (chargeResponse = gson.fromJson(response, ChargeResponse.class)) != null) {
            LOGGER.info("天津BOSS充值响应,返回包体:{}", chargeResponse);
            String respSerial = null;
            if ((respSerial = chargeResponse.getResult().getSVC_CONTENT().get(0).getTRADE_ID()) != null) {
                if (!updateRecord(serialNum, respSerial, serialNum)) {
                    LOGGER.error("天津充值更新流水号失败,serialNum:{}.bossRespNum:{}", serialNum, respSerial);
                }
            }
            
            return new TjBossOperationResultImpl(chargeResponse.getRespCode(), chargeResponse.getRespDesc());
        }
        return new TjBossOperationResultImpl("1000", "充值失败");
    }
    private boolean updateRecord(String systemNum, String bossRespNum, String bossReqNum) {
        if (org.apache.commons.lang.StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setBossRespSerialNum(bossRespNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }
    @Override
    public String getFingerPrint() {
        // TODO Auto-generated method stub
        return "tianjin";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        // TODO Auto-generated method stub
        return false;
    }
    
    private Map<String, String> buildSysParam(String bossReqNum, String requestTime) {
        
        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("method", "TJ_UNHT_netWorkGiftPurchase");
        sysParam.put("timestamp", requestTime);
        sysParam.put("format", "json");
        sysParam.put("appId", globalConfig.getAppId());
        sysParam.put("version", "1.0");
        sysParam.put("status", globalConfig.getStatus());
        sysParam.put("flowId", bossReqNum);
        sysParam.put("appKey", globalConfig.getAppKey());
        return sysParam;
    }
    
    private String buildUrl(String flowId, String requestTime) {
        
        StringBuffer urlSb = new StringBuffer();
        urlSb.append(globalConfig.getUrl());
        urlSb.append("?");
        
        urlSb.append("method=" + "TJ_UNHT_netWorkGiftPurchase");
        urlSb.append("&");
        
        urlSb.append("timestamp=" + requestTime);
        urlSb.append("&");
        
        urlSb.append("format=" + "json");
        urlSb.append("&");
        
        urlSb.append("appId=" + globalConfig.getAppId());
        urlSb.append("&");     
        
        urlSb.append("version=" + "1.0");
        urlSb.append("&");
        
        urlSb.append("status=" + globalConfig.getStatus());
        urlSb.append("&");
        
        urlSb.append("flowId=" + flowId);
        urlSb.append("&");
        
        urlSb.append("appKey=" + globalConfig.getAppKey());
        
        return urlSb.toString();
    }
    
    private String buildBusiParam(String groupId, String productCode, String mobile, String serialBill, String userId, SupplierProduct supplierProduct) {
        JSONObject feature = JSONObject.fromObject(supplierProduct.getFeature());   
        //JSONObject feature = JSONObject.fromObject("{\"elementId\":\"20161128\", \"packageId\":\"22782802\", \"productId\":\"7178\"}"); 
        String discntCode = feature.getString("elementId") + "," + feature.getString("packageId") + "," + productCode;
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("PROVINCE_CODE", "TJIN");
        jsonObject.put("ROUTE_EPARCHY_CODE", "0022");
        jsonObject.put("TRADE_EPARCHY_CODE", "0022");
        jsonObject.put("EPARCHY_CODE", "0022");
        jsonObject.put("TRADE_CITY_CODE", "0022");
        jsonObject.put("TRADE_DEPART_ID", globalConfig.getTradeDepartId());
        jsonObject.put("TRADE_STAFF_ID", globalConfig.getTradeStaffId());
        jsonObject.put("TRADE_DEPART_PASSWD", globalConfig.getTradeDepartPasswd());
        jsonObject.put("IN_MODE_CODE", "Z"); 
        jsonObject.put("TRADEDEPARTPASSWD", globalConfig.getTradeDepartPasswd());
        jsonObject.put("DEPART_ID", globalConfig.getDepartId());
        jsonObject.put("CITY_CODE", "0022");
        jsonObject.put("LOGIN_EPARCHY_CODE", "0022");
        jsonObject.put("STAFF_EPARCHY_CODE", "0022"); 
        
        jsonObject.put("GROUP_ID", groupId);
        jsonObject.put("DISCNT_CODE", discntCode);
        jsonObject.put("USER_ID", userId);
        jsonObject.put("PRODUCT_ID", feature.getString("productId"));
        jsonObject.put("MODIFY_TAG", "0");
        jsonObject.put("SERIAL_NUMBER", mobile);
        jsonObject.put("EPARCHY_CODE", "0022");
        jsonObject.put("EFFECT_NOW", "true");
        jsonObject.put("SERIAL_BILL", serialBill);
        
        return jsonObject.toString();
    }
}

package com.cmcc.vrp.boss.shanghai;

import com.alibaba.fastjson.JSON;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.shanghai.model.Feature;
import com.cmcc.vrp.boss.shanghai.model.ShReturnCode;
import com.cmcc.vrp.boss.shanghai.model.common.BusiParams;
import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.ProdAttrMap;
import com.cmcc.vrp.boss.shanghai.model.common.PubInfo;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.paymember.PayMemberInMap;
import com.cmcc.vrp.boss.shanghai.model.paymember.PayMemberProdAttrMap;
import com.cmcc.vrp.boss.shanghai.model.paymember.PayMemberReq;
import com.cmcc.vrp.boss.shanghai.model.paymember.PmAsiaResult;
import com.cmcc.vrp.boss.shanghai.model.paymember.PmRetInfo;
import com.cmcc.vrp.boss.shanghai.service.ShBossService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/7/15.
 */
@Service
public class ShNationalBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShNationalBossServiceImpl.class);

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    @Qualifier("QueryAllGroupServiceImpl")
    private ShBossService QueryAllGroupService;

    @Autowired
    @Qualifier("QueryMemberRoleImpl")
    private ShBossService QueryMemberRoleService;

    @Autowired
    @Qualifier("QueryProductServiceIml")
    private ShBossService QueryProductService;

    @Autowired
    private Gson gson;

    @Autowired
    private SerialNumService serialNumService;

    @Override
    public synchronized BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("上海渠道充值开始");
        SupplierProduct product;
        String pCode;
        String bbossInsOfferId;
        if (entId == null
                || splPid == null
                || !StringUtils.isValidMobile(mobile)
                || org.apache.commons.lang.StringUtils.isBlank(serialNum)
                || (product = productService.selectByPrimaryKey(splPid)) == null
                || org.apache.commons.lang.StringUtils.isBlank(pCode = product.getCode())) {
            return new ShBossOperationResultImpl(ShReturnCode.PARA_ILLEGALITY);
        }
        Feature feature = JSON.parseObject(product.getFeature(), Feature.class);
        bbossInsOfferId = feature.getBbossInsOfferId(); 
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("shanghai", 25);
        String chargeReq = gson.toJson(buildCR(mobile, bossReqNum, bbossInsOfferId, pCode));
        LOGGER.info("上海渠道充值请求包体:{}", chargeReq);
        PmRetInfo pmRetInfo = null;
        try {
//            OpenapiHttpCilent cilent = new OpenapiHttpCilent("A0002019", "hgH7terOSW5uYQvHUY/jrXZKcAjsyF3q"); //测试
            //OpenapiHttpCilent cilent = new OpenapiHttpCilent(getAppCode(), getApk()); //生产
            com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent cilent = 
                    new com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent(getAppCode(), getApk(), getSecurityUrl(), getOpenapiUrl());
            String resp = cilent.call("CRM4186", bossReqNum, chargeReq);
            LOGGER.info("上海渠道充值返回包体:{}", resp);
            JSONObject jsonObj;
            String status;
            String response;
            PmAsiaResult result;
            ErrorInfo errorInfo;
            if (org.apache.commons.lang.StringUtils.isNotBlank(resp)
                    && (jsonObj = new JSONObject(resp)) != null
                    && org.apache.commons.lang.StringUtils.isNotBlank(status = jsonObj.get("status").toString())
                    && "SUCCESS".equals(status)
                    && org.apache.commons.lang.StringUtils.isNotBlank(response = jsonObj.get("result").toString())
                    && (result = gson.fromJson(response, PmAsiaResult.class)) != null
                    && (errorInfo = result.getResponse().getErrorInfo()) != null
                    && (errorInfo.getCode().equals("0000"))
                    && (pmRetInfo = result.getResponse().getRetInfo()) != null
                    && (pmRetInfo.getReturnCode().equals(ShReturnCode.SUCCESS.getCode()))) {
                if (!updateRecord(serialNum, pmRetInfo.getReturnContent().getSerialNum(), bossReqNum)) {
                    LOGGER.error("上海充值更新流水号失败,serialNum:{}.bossRespNum:{}.bossReqNum:{}", serialNum, pmRetInfo.getReturnContent().getSerialNum(), bossReqNum);
                }
                ShBossOperationResultImpl shBossOperationResult = new ShBossOperationResultImpl(ShReturnCode.SUCCESS);
                shBossOperationResult.setEntId(entId);
                shBossOperationResult.setSystemNum(serialNum);
                shBossOperationResult.setFingerPrint(getFingerPrint());
                return shBossOperationResult;
            }
        } catch (Exception e) {
            LOGGER.error("上海渠道充值抛出异常:{}", e.getMessage());
            if (!updateRecord(serialNum, SerialNumGenerator.buildNullBossRespNum("shanghai"), bossReqNum)) {
                LOGGER.error("上海充值更新流水号失败,serialNum:{}.bossRespNum:{}.bossReqNum:{}", serialNum, pmRetInfo.getReturnContent().getSerialNum(), bossReqNum);
            }
            return new ShBossOperationResultImpl(ShReturnCode.FAILD);
        }
        if (!updateRecord(serialNum, pmRetInfo == null ? SerialNumGenerator.buildNullBossRespNum("shanghai")
                : pmRetInfo.getReturnContent().getSerialNum(), bossReqNum)) {
            LOGGER.error("上海充值更新流水号失败,serialNum:{}.bossRespNum:{}.bossReqNum:{}", serialNum, pmRetInfo.getReturnContent().getSerialNum(), bossReqNum);
        }
        return new ShBossOperationResultImpl(ShReturnCode.FAILD);
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
        return "shanghainational123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    /**
     * 构建请求包体
     *
     * @return
     */
    private PayMemberReq buildCR(String mobile, String serialNum, String bbossInsOfferId, String productId) {
        PayMemberReq req = new PayMemberReq();
        req.setPubInfo(buildPubInfo(serialNum));
        req.setRequest(buildRequest(mobile, bbossInsOfferId, productId, serialNum));
        return req;
    }

    private PubInfo buildPubInfo(String serialNum) {
        PubInfo pubInfo = new PubInfo();
        pubInfo.setClientIP("10.10.141.98");
        pubInfo.setCountyCode("021");
        pubInfo.setInterfaceId("79");
        pubInfo.setOpId("999990077");
        pubInfo.setRegionCode("021");
        pubInfo.setTransactionId(serialNum);
        pubInfo.setTransactionTime(DateUtil.getShBossTime());
        pubInfo.setOrgId("1");
        pubInfo.setInterfaceType("06");
        return pubInfo;
    }

    private Request buildRequest(String mobile, String bbossInsOfferId, String productId, String bossReqNum) {
        Request request = new Request();
        request.setBusiCode("PT-SH-FS-OI4186");
        request.setBusiParams(buildBP(mobile, bbossInsOfferId, productId, bossReqNum));
        return request;
    }

    private BusiParams buildBP(String mobile, String bbossInsOfferId, String productId, String bossReqNum) {
        BusiParams busiParams = new BusiParams();
        busiParams.setFlag("5");
        busiParams.setInMap(buildMap(mobile, bbossInsOfferId, productId, bossReqNum));
        return busiParams;
    }

    private InMap buildMap(String mobile, String bbossInsOfferId, String productId, String bossReqNum) {
        PayMemberInMap inMap = new PayMemberInMap();
        inMap.setProdSpecId("99905");
        inMap.setProdAttrMap(buildPAM(productId));
        inMap.setPoSpecId("010190004");//010190004
        inMap.setMemBillId(mobile);
        inMap.setBossInsOfferId(bbossInsOfferId);//"818935"
        inMap.setChannelType(getChannelType());
        inMap.setTransIDO(bossReqNum);
        return inMap;
    }

    private ProdAttrMap buildPAM(String productId) {
        PayMemberProdAttrMap map = new PayMemberProdAttrMap();
        map.setAccPeriodEffRule(getAccPeriodEffRule());
        map.setGiveValid(getGiveValid());
        map.setLimitCharge("");//100
        map.setMemberEffDate("");
        map.setMemberSps(productId);
        map.setSrvPackage("");
        return map;
    }

    private String format(String input) {
        String result;
        try {
            JSONObject jsonObj = new JSONObject(input);
            result = jsonObj.get("result").toString();
            return result;
        } catch (JSONException e) {
            LOGGER.error("格式化上海能力平台开发平台返回包体异常:{}", e.getMessage());
        }
        return null;
    }

    private String getGiveValid() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_GIVEVALID.getKey());
    }

    private String getAccPeriodEffRule() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_ACCPERIODEFFRULE.getKey());
    }

    private String getChannelType() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_CHANNELTYPE.getKey());
    }

    private String getAppCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_APPCODE.getKey());
    }

    private String getApk() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_APK.getKey());
    }
    
    private String getSecurityUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_OLD_SECURITY_URL.getKey());
        //return "http://211.136.164.123/open/security";
    }
    
    private String getOpenapiUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_OLD_OPENAPI_URL.getKey());
        //return "http://211.136.164.123/open/service";
    }
}

package com.cmcc.vrp.boss.shanghai;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.amazonaws.util.json.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.shanghai.model.Feature;
import com.cmcc.vrp.boss.shanghai.model.ShReturnCode;
import com.cmcc.vrp.boss.shanghai.model.charge.ChargeInMap;
import com.cmcc.vrp.boss.shanghai.model.charge.ChargeReq;
import com.cmcc.vrp.boss.shanghai.model.charge.ChargeResult;
import com.cmcc.vrp.boss.shanghai.model.charge.ChargeRetInfo;
import com.cmcc.vrp.boss.shanghai.model.charge.ChargeReturnContent;
import com.cmcc.vrp.boss.shanghai.model.common.BusiParams;
import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.PubInfo;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.querycount.InfoList;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlow;
import com.cmcc.vrp.boss.shanghai.service.ShBossService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.ShOrderListService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/7/15.
 */
@Service
public class ShNewBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShNewBossServiceImpl.class);

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private Gson gson = new Gson();

    @Autowired
    private SerialNumService serialNumService;
    
    @Autowired
    private ShOrderListService shOrderListService;
    
    @Autowired
    @Qualifier("QueryUsableBalanceOfFlowServiceImpl")
    private ShBossService queryUsableBalanceOfFlowService;
    
    @Autowired
    private AdministerService administerService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    EnterprisesService enterprisesService;
    
    public static void main(String[] args) {
        ShNewBossServiceImpl bossService = new ShNewBossServiceImpl();
        BossOperationResult bossOperationResult = bossService.charge(null, null, "13671746998", "1234567890", null);
        System.out.println(bossOperationResult.getResultCode());
        System.out.println(bossOperationResult.getResultDesc());
    }
    
    @Override
    public synchronized BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("上海渠道充值开始");
        SupplierProduct product;
        Enterprise enterprise;
        String pCode;
        String mainBillId;
        String offerId;
        String acctId;
        if (entId == null
                || splPid == null
                || !StringUtils.isValidMobile(mobile)
                || org.apache.commons.lang.StringUtils.isBlank(serialNum)
                || (product = productService.selectByPrimaryKey(splPid)) == null
                || org.apache.commons.lang.StringUtils.isBlank(pCode = product.getCode())
                || (enterprise = enterprisesService.selectById(entId)) == null) {
            return new ShNewBossOperationResultImpl(ShReturnCode.PARA_ILLEGALITY);
        }
        //String pCode = "1933001";
        Feature feature = JSON.parseObject(product.getFeature(), Feature.class);
//        Feature feature = new Feature();
//        feature.setOfferId("390000023260");
//        feature.setMainBillId("30000045695506");
        mainBillId = feature.getMainBillId();
        offerId = feature.getOfferId();
        acctId = feature.getAcctId();
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("shanghai", 25);
        ShOrderList orderList  = shOrderListService.getByMainBillId(mainBillId);
        if ("02".equals(orderList.getOrderType())) {
            QueryUsableBalanceOfFlow queryUsableBalanceOfFlow = new QueryUsableBalanceOfFlow();
            queryUsableBalanceOfFlow.setAccId(acctId);
            queryUsableBalanceOfFlow.setMainBillId(mainBillId);
            queryUsableBalanceOfFlow.setOfferId(offerId);
            List<InfoList> infoLists = queryUsableBalanceOfFlowService.queryUsableBalanceOfFlow(queryUsableBalanceOfFlow);
            Double totalFee = 0.0;
            if (infoLists != null) {
                for(InfoList infoList:infoLists) {
                    totalFee = totalFee + NumberUtils.toDouble(infoList.getTotalFee()) * 100;
                }
            }
            if (totalFee < orderList.getStopCount()) {
                List<Administer> administers = administerService.selectEMByEnterpriseId(entId);
                if (!administers.isEmpty()) {
                    String entManPhone = administers.get(0).getMobilePhone();
                    String content = "你好！企业" + enterprise.getName() + "，企业编码" + enterprise.getCode() + "订购组名称" + orderList.getOrderName() + "已达到暂停值，请及时充值，谢谢！";
                // 将短信扔到相应的队列中
                    if (!taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(entManPhone,
                        content, null, enterprise.getEntName(), MessageType.CHARGE_NOTICE.getCode(),
                        null))) {
                        LOGGER.info("入短信提醒队列失败");
                    }
                }
                if (!updateRecord(serialNum, bossReqNum)) {
                    LOGGER.error("上海充值更新流水号失败,serialNum:{}.bossReqNum:{}", serialNum, bossReqNum);
                }
                ShNewBossOperationResultImpl shBossOperationResult = new ShNewBossOperationResultImpl(ShReturnCode.STOP_COUNT);
                shBossOperationResult.setEntId(entId);
                shBossOperationResult.setSystemNum(serialNum);
                shBossOperationResult.setFingerPrint(getFingerPrint());
                return shBossOperationResult;
            }
            if (totalFee < orderList.getAlertCount()) {
                List<Administer> administers = administerService.selectEMByEnterpriseId(entId);
                if (!administers.isEmpty()) {
                    String entManPhone = administers.get(0).getMobilePhone();
                    String content = "你好！企业" + enterprise.getName() + "，企业编码" + enterprise.getCode() + "订购组名称" + orderList.getOrderName() + "已达到预警值，请及时充值，谢谢！";
                // 将短信扔到相应的队列中
                    if (!taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(entManPhone,
                        content, null, enterprise.getEntName(), MessageType.CHARGE_NOTICE.getCode(),
                        null))) {
                        LOGGER.info("入短信提醒队列失败");
                    }
                }
            }
            
        }
        String chargeReq = gson.toJson(buildCR(mobile, bossReqNum, mainBillId, pCode, offerId));
        LOGGER.info("上海渠道充值请求包体:{}", chargeReq);
        ChargeRetInfo retInfo = null;
        try {
            com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent cilent = 
                    new com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent(getAppCode(), getApk(), getSecurityUrl(), getOpenapiUrl());
            String resp = cilent.call("CRM4186", bossReqNum, chargeReq);
            LOGGER.info("上海渠道充值返回包体:{}", resp);
            JSONObject jsonObj;
            String status;
            String response;
            ChargeResult result;
            ErrorInfo errorInfo;
            ChargeReturnContent returnMap;
            if (org.apache.commons.lang.StringUtils.isNotBlank(resp)
                    && (jsonObj = new JSONObject(resp)) != null
                    && org.apache.commons.lang.StringUtils.isNotBlank(status = jsonObj.get("status").toString())
                    && "SUCCESS".equals(status)
                    && org.apache.commons.lang.StringUtils.isNotBlank(response = jsonObj.get("result").toString())
                    && (result = gson.fromJson(response, ChargeResult.class)) != null
                    && (errorInfo = result.getResponse().getErrorInfo()) != null){
                if (!errorInfo.getCode().equals("0000")) {
                    if (!updateRecord(serialNum, bossReqNum)) {
                        LOGGER.error("上海充值更新流水号失败,serialNum:{}.bossReqNum:{}", serialNum, bossReqNum);
                    }
                    ShNewBossOperationResultImpl shBossOperationResult = new ShNewBossOperationResultImpl(errorInfo.getCode(), errorInfo.getMessage());
                    shBossOperationResult.setEntId(entId);
                    shBossOperationResult.setSystemNum(serialNum);
                    shBossOperationResult.setFingerPrint(getFingerPrint());
                    return shBossOperationResult;
                } else if ((retInfo = result.getResponse().getRetInfo()) != null
                        && (returnMap = retInfo.getReturnMap()) != null) {
                    if (!updateRecord(serialNum, bossReqNum)) {
                        LOGGER.error("上海充值更新流水号失败,serialNum:{}.bossReqNum:{}", serialNum, bossReqNum);
                    }
                    ShNewBossOperationResultImpl shBossOperationResult = new ShNewBossOperationResultImpl(returnMap.getReturnCode(), returnMap.getReturnMsg());
                    shBossOperationResult.setEntId(entId);
                    shBossOperationResult.setSystemNum(serialNum);
                    shBossOperationResult.setFingerPrint(getFingerPrint());
                    return shBossOperationResult;
                }
            }
        } catch (Exception e) {
            LOGGER.info("上海渠道充值抛出异常:{}", e.getMessage());
            if (!updateRecord(serialNum, bossReqNum)) {
                LOGGER.error("上海充值更新流水号失败,serialNum:{}.bossReqNum:{}", serialNum, bossReqNum);
            }
            return new ShNewBossOperationResultImpl(ShReturnCode.FAILD);
        }
        if (!updateRecord(serialNum, bossReqNum)) {
            LOGGER.error("上海充值更新流水号失败,serialNum:{}.bossReqNum:{}", serialNum, bossReqNum);
        }
        return new ShNewBossOperationResultImpl(ShReturnCode.FAILD);
    }

    private boolean updateRecord(String systemNum, String bossReqNum) {
        if (org.apache.commons.lang.StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }

    @Override
    public String getFingerPrint() {
        return "shanghainew";
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
     * @param offerId 
     *
     * @return
     */
    private ChargeReq buildCR(String mobile, String serialNum, String mainBillId, String productId, String offerId) {
        ChargeReq req = new ChargeReq();
        req.setPubInfo(buildPubInfo(serialNum));
        req.setRequest(buildRequest(mobile, mainBillId, productId, serialNum, offerId));
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

    private Request buildRequest(String mobile, String mainBillId, String productId, String bossReqNum, String offerId) {
        Request request = new Request();
        request.setBusiCode("PT-SH-FS-OI4186");
        request.setBusiParams(buildBP(mobile, mainBillId, productId, bossReqNum, offerId));
        return request;
    }

    private BusiParams buildBP(String mobile, String mainBillId, String productId, String bossReqNum, String offerId) {
        BusiParams busiParams = new BusiParams();
        busiParams.setFlag("21");
        busiParams.setInMap(buildMap(mobile, mainBillId, productId, bossReqNum, offerId));
        return busiParams;
    }

    private InMap buildMap(String mobile, String mainBillId, String productId, String bossReqNum, String offerId) {
        ChargeInMap inMap = new ChargeInMap();
        inMap.setMainBillId(mainBillId);
        inMap.setMemBillId(mobile);
        inMap.setMemOrdProds(productId);
        inMap.setOfferId(offerId);
        inMap.setSerialNum(bossReqNum);
        return inMap;
    }

    private String getAppCode() {
        //return "A0002019";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEW_APPCODE.getKey());
    }

    private String getApk() {
        //return "hgH7terOSW5uYQvHUY/jrXZKcAjsyF3q";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEW_APK.getKey());
    }
    
    
    private String getSecurityUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEW_SECURITY_URL.getKey());
        //return "http://211.136.164.123/open/security";
    }
    
    private String getOpenapiUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_NEW_OPENAPI_URL.getKey());
        //return "http://211.136.164.123/open/service";
    }
}

package com.cmcc.vrp.boss.shanxi;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.shanxi.model.SxBossOperationResultImpl;
import com.cmcc.vrp.boss.shanxi.model.SxChargeResp;
import com.cmcc.vrp.boss.shanxi.model.SxReturnCode;
import com.cmcc.vrp.boss.shanxi.model.SxSaleActiveTradeRegReturn;
import com.cmcc.vrp.boss.shanxi.util.SxEncrypt;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2016年12月2日 上午11:50:57
 */
@Service
public class SxFeeBossServiceImpl implements BossService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SxFeeBossServiceImpl.class);
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    private SerialNumService serialNumService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private Gson gson;

//    public static void main(String[] args) {
//        Gson gson = new Gson();
//        String json = "{\"X_RESULTSIZE\":\"0\",\"CITY_CODE\":\"\",\"X_EXCEPTION\":\"Exception\",\"X_IBOSSMODE\":\"0\",\"IN_MODE_CODE\":\"Y\","
//                + "\"X_RSPDESC\":\"根据[69900043|99412363|0029]获取活动产品信息无数据！\",\"X_RSPTYPE\":\"2\",\"X_NODENAME\":\"app-node26-"
//                + "srv08:be04023ef4624eb591d40be479d4ee06:1484917673198\",\"EPARCHY_CODE\":\"\",\"X_RESULTCOUNT\":\"0\",\"DEPART_NAME\":\"\","
//                + "\"DEPART_ID\":\"\",\"X_RSPCODE\":\"-1\",\"X_RESULTINFO\":\"根据[69900043|99412363|0029]获取活动产品"
//                + "信息无数据！\",\"CONTACTID\":\"003200903120000\",\"X_TRANSMODE\":\"0\",\"STAFF_NAME\":\"\",\"X_RECORDNUM\":\"0\",\"X_RESULTCODE\":\"-1\"}";
//        SxChargeResp resp = gson.fromJson(json, SxChargeResp.class);
//        System.out.println(resp.getX_RESULTINFO());
//    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("陕西营销活动开始, entId={}, splPid={}, mobile={}, serialNum={}", entId, splPid, mobile, serialNum);
        SupplierProduct supplierProduct;
        String pCode;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = supplierProduct.getCode())
                || StringUtils.isBlank(serialNum)) {
            return new SxBossOperationResultImpl(SxReturnCode.PARA_ILLEGALITY);
        }

        String saleActiveTradeRegReqStr = saleActiveTradeRegReqStr(mobile, "69900043", pCode);
        SxChargeResp resp = null;
        try {
            String saleActiveRuleCheckRespStr = HttpUtils.post(getChargeUrl(), SxEncrypt.encrypt(saleActiveTradeRegReqStr),
                    buildRuleCheckHeaders(mobile, getTradeStaffId(), getInModeCode()), "application/x-www-form-urlencoded", new BasicResponseHandler());
            if (StringUtils.isBlank(saleActiveRuleCheckRespStr)
                    || StringUtils.isBlank(saleActiveRuleCheckRespStr = SxEncrypt.decrypt(saleActiveRuleCheckRespStr))
                    || (resp = gson.fromJson(saleActiveRuleCheckRespStr, SxChargeResp.class)) == null) {
                LOGGER.error("陕西营销活动校验失败,返回包体:{}", saleActiveRuleCheckRespStr);
                return new SxBossOperationResultImpl(SxReturnCode.CODE_PARAM_JSON_ERROR);
            }

            if (!resp.getX_RESULTCODE().equals(SxSaleActiveTradeRegReturn.SUCCESS.getCode())) {
                LOGGER.error("陕西营销活动校验失败,返回包体:{}", saleActiveRuleCheckRespStr);
                return new SxBossOperationResultImpl(resp.getX_RESULTCODE(), resp.getX_RESULTINFO());
            }

            String saleActiveTradeRegRespStr = HttpUtils.post(getChargeUrl(), SxEncrypt.encrypt(saleActiveTradeRegReqStr),
                    buildTradeRegHeaders(mobile, getTradeStaffId(), getInModeCode()), "application/x-www-form-urlencoded", new BasicResponseHandler());
            if (StringUtils.isNotBlank(saleActiveTradeRegRespStr)
                    && StringUtils.isNotBlank(saleActiveTradeRegRespStr = SxEncrypt.decrypt(saleActiveTradeRegRespStr))
                    && (resp = gson.fromJson(saleActiveTradeRegRespStr, SxChargeResp.class)) != null) {
                LOGGER.error("陕西营销活动办理返回包体:{}", saleActiveTradeRegRespStr);
                if (!updateRecord(serialNum, resp.getORDER_ID(), SerialNumGenerator.buildNullBossReqNum("shanxi"))) {
                    LOGGER.error("陕西营销活动更新流水号失败,serialNum:{}.bossRespNum:{}", serialNum, resp.getORDER_ID());
                }
                return new SxBossOperationResultImpl(resp.getX_RESULTCODE(), resp.getX_RESULTINFO());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SxBossOperationResultImpl(SxReturnCode.CODE_PARAM_JSON_ERROR);
        }

        return new SxBossOperationResultImpl(SxReturnCode.FAILD);
    }

    private Map<String, String> buildTradeRegHeaders(String phone, String tradeStaffId, String inModeCode) {
        Map<String, String> headers = new LinkedHashMap<String, String>();

        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //设置报文头
        headers.put("DESFLAG", "true");    //是否加密
        headers.put("TRADE_ROUTE_TYPE", "01");   //路由类型
        headers.put("TRADE_ROUTE_VALUE", phone);   //路由值
        headers.put("TRADE_STAFF_ID", tradeStaffId);    //工号
        headers.put("TRADE_STAFF_PASSWD", "3211");  //密码
        headers.put("ORGCHANNELID", "E029");    //渠道ID
        headers.put("IN_MODE_CODE", inModeCode);    //接入方式
        headers.put("BIZ_CODE", "BOP3163");  //业务编码
        headers.put("TRANS_CODE", "THT1039");    //交易编码

        return headers;
    }

    private Map<String, String> buildRuleCheckHeaders(String phone, String tradeStaffId, String inModeCode) {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        //设置报文头
        headers.put("DESFLAG", "true");    //是否加密
        headers.put("TRADE_ROUTE_TYPE", "01");   //路由类型
        headers.put("TRADE_ROUTE_VALUE", phone);   //路由值
        headers.put("TRADE_STAFF_ID", tradeStaffId);    //工号
        headers.put("TRADE_STAFF_PASSWD", "3211");  //密码
        headers.put("ORGCHANNELID", "E029");    //渠道ID
        headers.put("IN_MODE_CODE", inModeCode);    //接入方式
        headers.put("BIZ_CODE", "BOP3163");  //业务编码
        headers.put("TRANS_CODE", "THT1040");    //交易编码

        return headers;
    }

    private String saleActiveTradeRegReqStr(String mobile, String productId, String packageId) {
        Map<String, String> data = new HashMap<String, String>();
        String activeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        data.put("SERIAL_NUMBER", mobile);
        data.put("PRODUCT_ID", productId);
        data.put("PACKAGE_ID", packageId);
        data.put("ACTIVE_DATE", "'" + activeDate + "'");
        data.put("ONNET_ACTIVE_TAG", "1");
        Map paramData = new HashMap();
        paramData.put("SVC_CONTENT", data);
        return paramData.toString();
    }

    private String saleActiveRuleCheckReqStr(String mobile, String productId, String packageId) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("SERIAL_NUMBER", mobile);
        data.put("PRODUCT_ID", productId);
        data.put("PACKAGE_ID", packageId);
        data.put("MIFI_ORDER", "0");
        Map paramData = new HashMap();
        paramData.put("SVC_CONTENT", data);
        return paramData.toString();
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
        return "sx_fee";
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

    private String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_CHARGE_URL.getKey());
    }

    private String getTradeStaffId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_ACTIVITY_TRADESTAFFID.getKey());
    }

    private String getInModeCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANX_ACTIVITY_INMODECODE.getKey());
    }
}

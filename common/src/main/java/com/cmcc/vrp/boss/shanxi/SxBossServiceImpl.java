package com.cmcc.vrp.boss.shanxi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.shanxi.model.SxBossOperationResultImpl;
import com.cmcc.vrp.boss.shanxi.model.SxChargeResp;
import com.cmcc.vrp.boss.shanxi.model.SxReturnCode;
import com.cmcc.vrp.boss.shanxi.util.SxEncrypt;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/8/28.
 */
@Service("sxBossService")
public class SxBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SxBossServiceImpl.class);
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    private SupplierProductService productService;
    @Autowired
    private Gson gson;
    @Autowired
    private SerialNumService serialNumService;
    @Autowired
    private GlobalConfigService globalConfigService;

//    public static void main(String[] args) {
////        SxBossServiceImpl sxBossService = new SxBossServiceImpl();
////        String result = sxBossService.buildReqStr("123", "18867101129");
////        System.out.println(result);
//
////        Map data = new HashMap();
////        data.put("SERIAL_NUMBER", "15291483570");
////        data.put("MODIFY_TAG", "0");
////        data.put("ELEMENT_ID", "99412321");
////        data.put("ELEMENT_TYPE_CODE", "'D'");
////        Map paramData = new HashMap();
////        paramData.put("SVC_CONTENT", data);
////        String result2 = paramData.toString();
////        System.out.println(result2);
//
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");
//        SxBossServiceImpl bossService = (SxBossServiceImpl) context.getBean("sxBossService");
//
//        String sysNum = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//        bossService.charge(null, null, "18867105766", sysNum, null);
//    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("陕西BOSS渠道充值开始了");
        SupplierProduct product;
        String pCode;
        Enterprise enterprise;
        String result;
        if (entId == null
                || (enterprise = enterprisesService.selectById(entId)) == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || (product = productService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = product.getCode())
                || StringUtils.isBlank(serialNum)) {
            return new SxBossOperationResultImpl(SxReturnCode.PARA_ILLEGALITY);
        }
        String reqStr = buildReqStr(pCode, mobile);
        LOGGER.info("陕西BOSS渠道充值请求:{}", reqStr);
        String entCode = enterprise.getCode();
        if (entCode.equals(getEntCode())) {
            result = HttpUtils.post(getChargeUrl(), SxEncrypt.encrypt(reqStr),
                    buildHeaders(mobile, getXqjTradeStaffId(), getXqjPassWord(), getXqjInModeCode()),
                    "application/x-www-form-urlencoded",
                    new BasicResponseHandler());
        } else {
            result = HttpUtils.post(getChargeUrl(), SxEncrypt.encrypt(reqStr),
                    buildHeaders(mobile, getTradeStaffId(), getPassWord(), getInModeCode()),
                    "application/x-www-form-urlencoded",
                    new BasicResponseHandler());
        }

        LOGGER.info("陕西BOSS渠道充值响应:{}", result);
        SxChargeResp resp = null;
        if (StringUtils.isNotBlank(result)
                && StringUtils.isNotBlank(result = SxEncrypt.decrypt(result))
                && (resp = gson.fromJson(result, SxChargeResp.class)) != null) {
            LOGGER.info("陕西BOSS充值返回包体:{}", result);
            if (!updateRecord(serialNum, resp.getORDER_ID(), SerialNumGenerator.buildNullBossReqNum("shanxi"))) {
                LOGGER.error("陕西充值更新流水号失败,serialNum:{}.bossRespNum:{}", serialNum, resp.getORDER_ID());
            }
            return new SxBossOperationResultImpl(resp.getX_RESULTCODE(), resp.getX_RESULTINFO());
        }
        return new SxBossOperationResultImpl(SxReturnCode.FAILD);
    }

    private Map<String, String> buildHeaders(String phone, String tradeStaffId, String passWd, String inModeCode) {
        Map<String, String> headers = new LinkedHashMap<String, String>();

        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=GB18030");
        headers.put("DESFLAG", "true");
        headers.put("TRADE_ROUTE_TYPE", "01");
        headers.put("TRADE_ROUTE_VALUE", phone);
        headers.put("TRADE_STAFF_ID", tradeStaffId);
        headers.put("TRADE_STAFF_PASSWD", passWd);
        headers.put("ORGCHANNELID", "E029");
        headers.put("IN_MODE_CODE", inModeCode);
        headers.put("BIZ_CODE", "WYX1003");
        headers.put("TRANS_CODE", "UYX1003");
        headers.put("SEQID", "E001_1234567890");
        headers.put("Connection", "close");

        return headers;
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

    private String buildReqStr(String pCode, String mobile) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("SERIAL_NUMBER", mobile);
        data.put("ELEMENT_ID", pCode);
        data.put("ELEMENT_TYPE_CODE", getSxFormat("D"));
        data.put("MODIFY_TAG", "0");
        Map paramData = new HashMap();
        paramData.put("SVC_CONTENT", data);
        return paramData.toString();
    }

    private String getSxFormat(String string) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(string);
        sb.append("'");
        return sb.toString();
    }

    @Override
    public String getFingerPrint() {
        return "shanxi123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
    }

    private String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_CHARGE_URL.getKey());
    }

    private String getTradeStaffId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_TRADESTAFFID.getKey());
    }

    private String getInModeCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_INMODECODE.getKey());
    }

    private String getPassWord() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_PASSWD.getKey());
    }

    private String getXqjTradeStaffId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_XQJ_TRADESTAFFID.getKey());
    }

    private String getXqjInModeCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_XQJ_INMODECODE.getKey());
    }

    private String getXqjPassWord() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_XQJ_PASSWD.getKey());
    }

    private String getEntCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_XQJ_CODE.getKey());
    }
}

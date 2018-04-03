package com.cmcc.vrp.boss.guangxi;

import com.google.common.base.Charsets;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.guangxi.model.AdditionRsp;
import com.cmcc.vrp.boss.guangxi.model.GxChargeResp;
import com.cmcc.vrp.boss.guangxi.model.GxReturnCode;
import com.cmcc.vrp.boss.guangxi.model.InitDataDTO;
import com.cmcc.vrp.boss.guangxi.model.Response;
import com.cmcc.vrp.boss.guangxi.util.DigestUtil;
import com.cmcc.vrp.boss.guangxi.util.GxDataGenerater;
import com.cmcc.vrp.boss.guangxi.util.MD5Utils;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.StringUtils;
import com.thoughtworks.xstream.XStream;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leelyn on 2016/9/12.
 */
@Service
public class GxBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GxBossServiceImpl.class);

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private SerialNumService serialNumService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("广西渠道开始充值了");
        SupplierProduct supplierProduct;
        String pCode;
        if (entId == null
                || splPid == null
                || !StringUtils.isValidMobile(mobile)
                || org.apache.commons.lang.StringUtils.isBlank(serialNum)
                || (supplierProduct = productService.selectByPrimaryKey(splPid)) == null
                || org.apache.commons.lang.StringUtils.isBlank(pCode = supplierProduct.getCode())) {
            return new GxBossOperationResultImpl(GxReturnCode.FAILD);
        }

        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("ZYXX", 25);

        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        InitDataDTO initData = initData(mobile, pCode, bossReqNum);
        HttpEntity entity = GxDataGenerater.buildData(initData);
        String respStr = null;

        try {
            respStr = HttpUtils.post(getUrl(), StreamUtils.copyToString(entity.getContent(), Charsets.UTF_8),
                    buildHeaders("T4011137", getAppkey(), getAppSecret(), getProductId()),
                    "application/xml", new BasicResponseHandler());
        } catch (IOException e) {
            LOGGER.error("向广西渠道发起充值请求时抛出异常,异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
            return new GxBossOperationResultImpl(GxReturnCode.FAILD);
        }

        LOGGER.info("广西渠道充值响应:{}", respStr);
        xStream.alias("InterBOSS", GxChargeResp.class);
        GxChargeResp gxChargeResp;
        Response response;
        String additionRsp;
        AdditionRsp rsp = null;
        try {
            if (org.apache.commons.lang.StringUtils.isNotBlank(respStr)
                    && (gxChargeResp = (GxChargeResp) xStream.fromXML(respStr)) != null
                    && (response = gxChargeResp.getResponse()) != null
                    && response.getRspCode().equals("0000")
                    && org.apache.commons.lang.StringUtils.isNotBlank(additionRsp = gxChargeResp.getSvcCont())) {
                xStream.alias("AdditionRsp", AdditionRsp.class);
                rsp = (AdditionRsp) xStream.fromXML(additionRsp);
                String status;
                if (rsp != null
                        && org.apache.commons.lang.StringUtils.isNotBlank(status = rsp.getStatus())
                        && status.equals(GxReturnCode.SUCCESS.getCode())) {
                    if (!updateRecord(serialNum, bossReqNum, bossReqNum)) {
                        LOGGER.error("广西充值成功后更新流水号失败");
                    }
                    return new GxBossOperationResultImpl(GxReturnCode.SUCCESS);
                }
                if (rsp != null) {
                    LOGGER.info("广西渠道充值失败,status：{},ErrDesc:{}", rsp.getStatus(), rsp.getErrDesc());
                }
                if (!updateRecord(serialNum, bossReqNum, bossReqNum)) {
                    LOGGER.error("广西失败后更新流水号失败");
                }
                return new GxBossOperationResultImpl(GxReturnCode.FAILD);
            }
        } catch (Exception e) {
            LOGGER.error("广西渠道充值抛出异常:{}", e);
            if (!updateRecord(serialNum, bossReqNum, bossReqNum)) {
                LOGGER.error("广西抛出异常后更新流水号失败");
            }
            return new GxBossOperationResultImpl(GxReturnCode.FAILD);
        }
        LOGGER.info("广西渠道充值失败,地址:{}", getUrl());
        if (!updateRecord(serialNum, bossReqNum, bossReqNum)) {
            LOGGER.error("广西充值失败后更新流水号失败");
        }
        return new GxBossOperationResultImpl(GxReturnCode.FAILD);
    }

    private Map<String, String> buildHeaders(String actCode, String appKey, String appSecret, String productId) {
        Map<String, String> headers = new LinkedHashMap<String, String>();

        headers.put("Content-Type", "multipart/form-data");
        headers.put("Authorization", getAuthorization());
        headers.put("X-WSSE", getXWSSE(appKey, appSecret));
        headers.put("PRODID", getPRODID(productId));
        headers.put("ACTCODE", actCode);

        return headers;
    }

    private String getAuthorization() {
        return "WSSE realm=\"DOMS\", profile=\"UsernameToken\", type=\"AppKey\"";
    }

    private String getXWSSE(String appKey, String appSecret) {
        String created = DateUtil.getGxBossATime();
        String nonce = DigestUtil.genNonce();
        String pd = DigestUtil.digistAppSecret(nonce, created, appSecret);

        String wsse = "UsernameToken Username=\"" + appKey
                + "\", PasswordDigest=\"" + pd + "\", Nonce=\"" + nonce
                + "\", Created=\"" + created + "\"";
        return wsse;
    }

    private String getPRODID(String productId) {
        MD5Utils md = new MD5Utils();
        return md.md5(productId);
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

    private InitDataDTO initData(String mobile, String pCode, String bossReqNum) {
        InitDataDTO data = new InitDataDTO();
        data.setActionCode(getActionCode());
        data.setActivityCode(getActivityCode());
        data.setBipCode(getBipCode());
        data.setBossReqNum(bossReqNum);
        data.setFlag(getFlag());
        data.setMobile(mobile);
        data.setpCode(pCode);
        data.setPrdouctId(getProductId());
        return data;
    }

    private String getActionCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGXI_ACTIONCODE.getKey());
    }

    private String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGXI_URL.getKey());
    }


    private String getAppkey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGXI_APPKEY.getKey());
    }

    private String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGXI_APPSECRET.getKey());
    }

    private String getProductId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGXI_PRODUCTID.getKey());
    }

    private String getFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGXI_FLAG.getKey());
    }

    private String getActivityCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGXI_ACTIVITYCODE.getKey());
    }

    private String getBipCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGXI_BIPCODE.getKey());
    }

    @Override
    public String getFingerPrint() {
        return "guangxi123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
    }
}

package com.cmcc.vrp.boss.ecinvoker.impl;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.ecinvoker.AbstractEcBossServiceImpl;
import com.cmcc.vrp.boss.ecinvoker.EcBossOperationResultImpl;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcAuthService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcCacheService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcChargeService;
import com.cmcc.vrp.boss.ecinvoker.utils.DataEcUtil;
import com.cmcc.vrp.boss.ecinvoker.utils.DateEcUitl;
import com.cmcc.vrp.boss.ecinvoker.utils.SignatureEcUtil;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.bean.AuthRespData;
import com.cmcc.vrp.ec.bean.ChargeReq;
import com.cmcc.vrp.ec.bean.ChargeResp;
import com.cmcc.vrp.ec.bean.ChargeRespData;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.YqxChargeInfo;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.YqxChargeInfoService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.thoughtworks.xstream.XStream;

/**
 * YqxEcBossServiceImpl.java
 * @author wujiamin
 * @date 2017年5月9日
 */
@Service
public class YqxEcBossServiceImpl extends AbstractEcBossServiceImpl{
    private static final Logger LOGGER = LoggerFactory.getLogger(YqxEcBossServiceImpl.class);
    
    @Autowired
    private ProductService productService;

    @Autowired
    private EcCacheService cacheService;

    @Autowired
    private EcAuthService authService;

    @Autowired
    private EcChargeService chargeService;

    @Autowired
    private YqxChargeInfoService yqxChargeInfoService;
    
    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public String getFingerPrint() {
        return null;
    }

    @Override
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_EC_APP_KEY.getKey());
    }

    @Override
    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_EC_APP_SECRET.getKey());
    }

    @Override
    protected String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_EC_CHARGE_URL.getKey());
    }

    @Override
    protected String getAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_EC_AUTH_URL.getKey());
    }

    @Override
    protected String getKeyPrefix() {
        return "yunqixin";
    }
    
    @Override
    public BossOperationResult charge(Long entId, Long platformProductId, String mobile, String serialNum, Long prdSize) {
        Product product;
        if (platformProductId == null
            || !StringUtils.isValidMobile(mobile)
            || org.apache.commons.lang.StringUtils.isBlank(serialNum)
            || (product = productService.selectProductById(platformProductId))== null) {
            LOGGER.info("参数校验未通过");
            return EcBossOperationResultImpl.FAIL;
        }

        String token;
        AuthResp response;
        AuthRespData authorizationResp;

        String pltPrdCode = product.getProductCode();
        String appKey = getAppKey();
        String appSecrect = getAppSecret();
        String authUrl = getAuthUrl();
        String chargeUrl = getChargeUrl();

        // 获取token
        if (org.apache.commons.lang.StringUtils.isBlank(token = cacheService.getAccessToken(getKey()))
            && (response = authService.auth(appKey, appSecrect, authUrl)) != null //当缓存没有去调用认证接口
            && (authorizationResp = response.getAuthRespData()) != null) {
            token = authorizationResp.getToken();
            DateTime deadTime = ISODateTimeFormat.dateTimeParser().parseDateTime(authorizationResp.getExpiredTime());
            String interval = DateEcUitl.minutesBetween(new DateTime(), deadTime);
            cacheService.saveAccessToken(token, interval, getKey());
        }

        //构建请求数据
        ChargeReq reqBody = DataEcUtil.buildCR(new DateTime().toString(), pltPrdCode, mobile, serialNum);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("Request", ChargeReq.class);

        // 发起充值请求
        String body = xStream.toXML(reqBody);
        ChargeResp resp;
        String signature = SignatureEcUtil.signatrue2DoPost(body, appSecrect);
        try{
            resp = chargeService.charge(reqBody, token, signature, chargeUrl);
        }catch(Exception e){
            LOGGER.info("充值时抛出异常.{}",e.getMessage());
            return com.cmcc.vrp.boss.ecinvoker.EcBossOperationResultImpl.FAIL;
        }
        ChargeRespData dataResp = null;

        if (resp != null
            && (dataResp = resp.getRespData()) != null
            && org.apache.commons.lang.StringUtils.isNotBlank(dataResp.getSystemNum())) {
            if (!updateRecord(serialNum, dataResp.getSystemNum())) {
                LOGGER.error("更新流水失败");
            }
            return com.cmcc.vrp.boss.ecinvoker.EcBossOperationResultImpl.SUCCESS;
        }

        if (!updateRecord(serialNum, dataResp == null ? SerialNumGenerator.buildNullBossRespNum("sichuan") : dataResp.getSystemNum())) {
            LOGGER.error("更新流水失败");
        }

        LOGGER.error("invoker {} ec charge faild.reqStr = {}", getFingerPrint(), body);
        return com.cmcc.vrp.boss.ecinvoker.EcBossOperationResultImpl.FAIL;
    }
    
    private String getKey() {
        return getKeyPrefix() + ".token";
    }
    
    //更新流水号
    private boolean updateRecord(String serialNum, String returnSystemNum) {
        YqxChargeInfo chargeInfo = null;
        if (org.apache.commons.lang.StringUtils.isBlank(serialNum)
            || (chargeInfo = yqxChargeInfoService.selectBySerialNum(serialNum)) == null) {
            return false;
        }
        chargeInfo.setReturnSystemNum(returnSystemNum);
        return yqxChargeInfoService.updateReturnSystemNum(chargeInfo);
    }
}

/**
 *
 */
package com.cmcc.vrp.boss.gansu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcCacheService;
import com.cmcc.vrp.boss.ecinvoker.utils.DateEcUitl;
import com.cmcc.vrp.boss.gansu.model.GSChargeRequest;
import com.cmcc.vrp.boss.gansu.model.GSChargeRequestAuthorization;
import com.cmcc.vrp.boss.gansu.model.GSChargeRequestServiceContent;
import com.cmcc.vrp.boss.gansu.model.GSChargeRequestTransInfo;
import com.cmcc.vrp.boss.gansu.model.GSDynamicTokenRequest;
import com.cmcc.vrp.boss.gansu.model.GSDynamicTokenResponse;
import com.cmcc.vrp.boss.gansu.model.GSInterBossRequest;
import com.cmcc.vrp.boss.gansu.model.GSInterBossResponse;
import com.cmcc.vrp.boss.gansu.model.ProductInfo;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年5月16日
 */
@Component("GSBossService")
public class GSBossServiceImpl implements BossService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GSBossServiceImpl.class);

    @Autowired
    EntProductService entProductService;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    private EcCacheService cacheService;

    public static void main(String[] args) {
        String interval = DateEcUitl.minutesBetween(new DateTime(), new DateTime(DateUtil.getDateAfterMinutes(new Date(), 5).getTime()));
        System.out.println(interval);
    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("甘肃charge method start!");

        //检查参数
        SupplierProduct supplierProduct = null;
        if (splPid == null
                || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(mobile)
                || StringUtils.isBlank(serialNum)) {
            LOGGER.info("调用甘肃省BOSS充值接口失败：参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return null;
        }

        String originalId = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDHHMMSS");
        String originalTime = dateFormat.format(new Date());
        String productCode = supplierProduct.getCode();

        //获取动态验证码
        String appId = getAppId();
        String accessToken = getAccessToken();

        String dynamicToken = null;
        // 获取token
        if (org.apache.commons.lang.StringUtils.isBlank(dynamicToken = cacheService.getAccessToken(getKey()))
                && (dynamicToken = authentication(appId, accessToken)) != null) { //当缓存没有去调用认证接口
            LOGGER.info("开始向BOSS侧发送认证请求，获取动态验证码. AppId = {}, accessToken = {}.", appId, accessToken);
            LOGGER.info("BOSS侧认证请求返回{}.", dynamicToken);
            cacheService.saveAccessToken(dynamicToken, getExpiredTime(), getKey());
        }
        LOGGER.info("开始向BOSS侧发送充值请求, dynamicToken = {}, originalId = {}, originalTime = {}, sessionId = {}, mobile = {}, productCode = {}",
                dynamicToken, originalId, originalTime, sessionId, mobile, productCode);
        GSInterBossResponse data = getRecvMsg(dynamicToken, originalId, originalTime, sessionId, mobile, productCode);
        LOGGER.info("BOSS侧充值请求返回{}.", JSONObject.toJSONString(data));

        return new GSBossOperationResultImpl(data);
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    @Override
    public String getFingerPrint() {
        //md5hex("gansubossservice")
        return "2052bec3713769532a0765430fd8e7ca";
    }

    private String getKey() {
        return getKeyPrefix() + ".token";
    }

    protected String getKeyPrefix() {
        return "gansuaccesstoken";
    }

    /**
     * 鉴权
     *
     * @param appId       appid
     * @param accessToken accessToken
     * @return 结果
     */
    public String authentication(String appId, String accessToken) {
        GSDynamicTokenRequest gsDynamicTokenRequest = new GSDynamicTokenRequest();
        gsDynamicTokenRequest.setAccessToken(accessToken);
        gsDynamicTokenRequest.setAppId(appId);

        String request = JSON.toJSONString(gsDynamicTokenRequest);

        LOGGER.info("认证请求发送参数为{}.", request);
        String response = HttpUtils.post(getDynamicTokenUrl(), request, "application/json");
        LOGGER.info("认证请求返回参数为{}.", response);

        GSDynamicTokenResponse gsDynamicTokenResponse = JSON.parseObject(response, GSDynamicTokenResponse.class);

        return gsDynamicTokenResponse != null ? gsDynamicTokenResponse.getDynamicToken() : null;
    }

    private GSInterBossResponse getRecvMsg(String dynamicToken, String originalId, String originalTime,
                                           String sessionId, String phoneId, String prodId) {

        //得到Http返回的信息
        String recvMsg = getRecvData(dynamicToken, originalId, originalTime, sessionId,
                phoneId, prodId);

        // 将得到的数据，转化为Response对象
        GSInterBossResponse datas = JSON.parseObject(recvMsg, GSInterBossResponse.class);

        return datas;
    }

    private String getRecvData(String dynamicToken, String originalId, String originalTime,
                               String sessionId, String phoneId, String prodId) {

        String json = getSendJson(dynamicToken, originalId, originalTime, sessionId,
                phoneId, prodId);

        LOGGER.info("充值请求参数为{}", json);
        String response = HttpUtils.post(getProductUrl(), json, "application/json");
        LOGGER.info("充值请求返回参数为{}.", response);

        return response;
    }

    /**
     * 得到发送到boss的String对象
     */
    private String getSendJson(String dynamicToken, String originalId, String originalTime,
                               String sessionId, String phoneId, String prodId) {
        GSInterBossRequest requestObject = constructRequestObject(dynamicToken, originalId,
                originalTime, sessionId, phoneId, prodId);

        return JSON.toJSONString(requestObject);
    }


    private GSInterBossRequest constructRequestObject(String dynamicToken, String originalId, String originalTime,
                                                      String sessionId, String phoneId, String prodId) {
        GSInterBossRequest interBoss = new GSInterBossRequest();
        GSChargeRequest request = new GSChargeRequest();
        GSChargeRequestTransInfo transInfo = new GSChargeRequestTransInfo();
        GSChargeRequestAuthorization authorization = new GSChargeRequestAuthorization();
        GSChargeRequestServiceContent serviceContent = new GSChargeRequestServiceContent();
        ProductInfo prodInfo = new ProductInfo();
        List<ProductInfo> prodInfoList = new LinkedList<ProductInfo>();

        request.setVersion(getVersion());
        request.setTestFlag(getTestFlag());
        request.setAppId(getAppId());
        request.setDynamicToken(dynamicToken);
        request.setUserPhoneNumber(phoneId);

        transInfo.setSessionId(sessionId);
        transInfo.setOriginalId(originalId);
        transInfo.setOriginalTime(originalTime);

        authorization.setUserAuthorizationCode(getUserAuthorizationCode());

        serviceContent.setNewBrand(getNewBrand());
        serviceContent.setNewPlanId(getNewPlanId());
        serviceContent.setValidType(getValidType());

        prodInfo.setActionType(getActionType());
        prodInfo.setBusType(getBusType());
        prodInfo.setPreProdId(getPreProdId());
        prodInfo.setProdId(prodId);
        prodInfo.setProdType(getProdType());
        prodInfo.setServiceId(getServiceId());
        prodInfo.setVeType(getVeType());

        prodInfoList.add(prodInfo);
        serviceContent.setProdInfoList(prodInfoList);
        request.setAuthorization(authorization);
        request.setServiceContent(serviceContent);
        request.setTransInfo(transInfo);
        interBoss.setInterBOSS(request);

        return interBoss;
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    public String getVersion() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_VERSION.getKey());
    }

    public int getTestFlag() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_TEST_FLAG.getKey()));
    }

    public String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_APPID.getKey());
    }

    public String getAccessToken() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_ACCESS_TOKEN.getKey());
    }

    public String getUserAuthorizationCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_USER_AUTHORIZATION_CODE.getKey());
    }

    public String getNewBrand() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_NEW_BRAND.getKey());
    }

    public String getNewPlanId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_NEW_PLAN_ID.getKey());
    }

    public int getValidType() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_VALID_TYPE.getKey()));
    }

    public int getActionType() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_ACTION_TYPE.getKey()));
    }

    public String getBusType() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_BUS_TYPE.getKey());
    }

    public String getPreProdId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_PRE_PRODID.getKey());
    }

    public int getProdType() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_PROD_TYPE.getKey()));
    }

    public String getProductUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_PRODUCT_URL.getKey());
    }

    public String getDynamicTokenUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_DYNAMIC_TOKEN_URL.getKey());
    }

    public String getServiceId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_SERVICE_ID.getKey());
    }

    public String getVeType() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_VETYPE.getKey());
    }

    public String getExpiredTime() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_EXPIRED_TIME.getKey());
    }
}

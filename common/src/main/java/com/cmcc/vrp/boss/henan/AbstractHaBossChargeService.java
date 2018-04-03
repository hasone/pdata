package com.cmcc.vrp.boss.henan;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.henan.Util.HaAES256;
import com.cmcc.vrp.boss.henan.Util.HaSign;
import com.cmcc.vrp.boss.henan.model.HaChargeReq;
import com.cmcc.vrp.boss.henan.model.HaChargeResp;
import com.cmcc.vrp.boss.henan.model.HaErrorCode;
import com.cmcc.vrp.boss.henan.model.MemberDeal;
import com.cmcc.vrp.boss.henan.model.HaQueryResult;
import com.cmcc.vrp.boss.henan.model.HaAuthResp;
import com.cmcc.vrp.boss.henan.model.HaAuthReq;
import com.cmcc.vrp.boss.henan.service.HaAuthService;
import com.cmcc.vrp.boss.henan.service.HaCacheService;
import com.cmcc.vrp.boss.henan.service.HaQueryBossService;
import com.cmcc.vrp.boss.henan.service.Impl.HaBossOperationResutlImpl;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by leelyn on 2016/8/17.
 */
public abstract class AbstractHaBossChargeService implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHaBossChargeService.class);

    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private SupplierProductService productService;
    @Autowired
    private HaAuthService authService;
    @Autowired
    private HaCacheService cacheService;
    @Autowired
    private SerialNumService serialNumService;
    @Autowired
    private Gson gson;
    private String appkey;
    private String appId;
    private String domain;
    private String SMS_TEMPLATE;
    private String VALID_TYPE;
    private String VALID_MONTH;

    protected abstract String getAppId();

    protected abstract String getAppkey();

    protected abstract HaQueryBossService getQueryService();

    protected abstract String getSMS_TEMPLATE();

    protected abstract String getVALID_TYPE();

    protected abstract String getVALID_MONTH();

    protected abstract Enterprise getEnterprise(Long entId);


    protected void init() {
        appId = getAppId();
        appkey = getAppkey();
        domain = getDomain();
        SMS_TEMPLATE = getSMS_TEMPLATE();
        VALID_TYPE = getVALID_TYPE();
        VALID_MONTH = getVALID_MONTH();
    }


    @Override
    public final BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("河南渠道充值开始");
        SupplierProduct sPrdouct;
        String pCode = null;
        String entName;
        String entCode;
        Enterprise enterprise;
        if (entId == null
                || splPid == null
                || (enterprise = getEnterprise(entId)) == null
                || StringUtils.isBlank(entName = enterprise.getName())
                || StringUtils.isBlank(entCode = enterprise.getCode())
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = sPrdouct.getCode())) {
            LOGGER.error("HeNan charge requset Para is null");
            return new HaBossOperationResutlImpl(HaErrorCode.parameter_error);
        }
        //初始化
        init();
        String bossReqNum = SerialNumGenerator.buildBossReqSerialNum(25);
        String accessToken = getAccessToken();

        //叠加包的标志位
        boolean flag = isSuperposition(entName, mobile);
        LOGGER.info("叠加包的标志位:{}", flag);
        String busiPara = gson.toJson(buildCR(pCode, mobile, bossReqNum, flag, entCode));
        // 更新序列号表
        if (updateRecord(serialNum, bossReqNum)) {
            LOGGER.info("记录平台流水和BOSS请求流水号的关系成功");
        } else {
            LOGGER.error("记录平台流水和BOSS请求流水号的关系失败");
        }
        String timestamp = DateUtil.getHenanBossTime();
        String sign = HaSign.sign("SO_MEMBER_DEAL_OPER", appId, appkey, accessToken, busiPara, timestamp);
        String url = domain + "?" + combineUrl(accessToken, sign, timestamp, appId);

        //4 加密包体并发送请求
        LOGGER.info("河南渠道的充值请求地址:{},请求包体:{}", url, busiPara);
        String result = HttpUtils.post(url, HaAES256.encryption(busiPara, appkey), "application/json");
        LOGGER.info("河南渠道的充值响应:{}", result);
        HaChargeResp resp = gson.fromJson(result, HaChargeResp.class);
        if (resp != null
                && resp.getRespCode().equals(HaErrorCode.send.getCode())) {
            HaBossOperationResutlImpl operation = new HaBossOperationResutlImpl(HaErrorCode.send);
            operation.setQueryFlag(Boolean.parseBoolean(getQueryFlag()));
            operation.setFingerPrint(getFingerPrint());
            operation.setSystemNum(serialNum);
            operation.setEntId(entId);
            LOGGER.info("operation:{}", gson.toJson(operation));
            return operation;
        }
        return new HaBossOperationResutlImpl(HaErrorCode.faild);
    }


    /**
     * 组合URL参数
     *
     * @param accessToken
     * @param sign
     * @param timestamp
     * @return
     */
    private String combineUrl(String accessToken, String sign, String timestamp, String appId) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("method=SO_MEMBER_DEAL_OPER");
        buffer.append("&format=json");
        buffer.append("&appId=" + appId);
        buffer.append("&busiSerial=1");
        buffer.append("&version=1.0");
        buffer.append("&accessToken=" + accessToken);
        buffer.append("&timestamp=" + timestamp);
        buffer.append("&sign=" + sign);
        return buffer.toString();
    }


    /**
     * 构建充值请求包体
     *
     * @param pCode
     * @param mobile
     * @param serialNum
     * @return
     */
    private HaChargeReq buildCR(String pCode, String mobile, String serialNum, boolean flag, String entCode) {
        HaChargeReq req = new HaChargeReq();
        req.setBILL_ID(mobile);
        req.setCUST_ORDER_ID(serialNum);
        if (flag) {        // 1：添加；（2：修改；3：删除，这两个基本不用）4：叠加包订购
            req.setFLAG("4");
        } else {
            req.setFLAG("1");
        }
        req.setGBILL_ID(entCode);
        req.setMEM_SRVPKG(pCode);
        req.setSMS_TEMPLATE(getSMS_TEMPLATE());
        req.setVALID_MONTH(getVALID_MONTH());
        req.setVALID_TYPE(getVALID_TYPE());
        return req;
    }

    private boolean updateRecord(String systemNum, String bossReqNum) {
        if (StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }


    /**
     * 判断是否需要去调用叠加包
     *
     * @param entName
     * @param phone
     * @return
     */
    protected boolean isSuperposition(String entName, String phone) {
        MemberDeal memberDeal = getQueryService().queryMemberDeal(phone);
        LOGGER.info("查询企业:{}为号码:{}订购记录:{}", entName, phone, new Gson().toJson(memberDeal));
        List<HaQueryResult> list;
        try {
            if (memberDeal != null
                    && (list = memberDeal.getSO_MEMBER_DEAL()) != null) {
                for (HaQueryResult result : list) {
                    if (result.getGROUP_NAME().equals(entName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("判断是否去叠加抛出异常:{}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取AccessToken
     *
     * @return
     */
    protected String getAccessToken() {
        String accessToken = cacheService.getAccessToken();
        HaAuthResp authResp;
        if (StringUtils.isBlank(accessToken)
                && (authResp = authService.auth(buildHAR())) != null) {
            //2 通过调接口获取accesstoken，并缓存
            accessToken = authResp.getAccess_token();
            Integer minus = (Integer) (Integer.parseInt(authResp.getExpires_in()) / 60); // 缓存中时间以分钟为单位
            cacheService.saveAccessToken(accessToken, String.valueOf(minus));
        }
        return accessToken;
    }

    /**
     * 构建授权请求包体
     *
     * @return
     */
    private HaAuthReq buildHAR() {
        HaAuthReq authReq = new HaAuthReq();
        authReq.setAppId(getAppId());
        authReq.setAppKey(getAppkey());
        authReq.setGrantType("client_credentials");
        return authReq;
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    private String getDomain() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_CHARGE_DOMAIN.getKey());
    }

    private String getQueryFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_QUERY_FLAG.getKey());
    }

}


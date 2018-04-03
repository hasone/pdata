package com.cmcc.vrp.boss.ecinvoker;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
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
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.StringUtils;
import com.thoughtworks.xstream.XStream;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月7日 下午3:03:40
*/
public abstract class AbstractNmEcBossServiceImpl implements BossService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNmEcBossServiceImpl.class);

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private EcCacheService cacheService;

    @Autowired
    private EcAuthService authService;

    @Autowired
    private EcChargeService chargeService;

    @Autowired
    private SerialNumService serialNumService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        SupplierProduct product;
        if (entId == null
            || splPid == null
            || !StringUtils.isValidMobile(mobile)
            || org.apache.commons.lang.StringUtils.isBlank(serialNum)
            || (product = productService.selectByPrimaryKey(splPid)) == null) {
            return NmEcBossOperationResultImpl.FAIL;
        }

        String token;
        AuthResp response;
        AuthRespData authorizationResp;

        String pltPrdCode = product.getCode();
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
        String signature = SignatureEcUtil.signatrue2DoPost(body, appSecrect);
        ChargeResp resp = chargeService.charge(reqBody, token, signature, chargeUrl);
        ChargeRespData dataResp = null;

        if (resp != null
            && (dataResp = resp.getRespData()) != null
            && org.apache.commons.lang.StringUtils.isNotBlank(dataResp.getSystemNum())) {
            if (!updateRecord(serialNum, dataResp.getSystemNum(), serialNum)) {
                LOGGER.error("更新流水失败");
            }
            NmEcBossOperationResultImpl result = NmEcBossOperationResultImpl.SUCCESS;
            result.setSystemNum(serialNum);
            result.setFingerPrint(getFingerPrint());
            result.setEntId(entId);
            return result;
        }

        if (!updateRecord(serialNum, dataResp == null ? SerialNumGenerator.buildNullBossRespNum("neimenggu") : dataResp.getSystemNum(), serialNum)) {
            LOGGER.error("更新流水失败");
        }

        LOGGER.error("invoker {} ec charge faild.reqStr = {}", getFingerPrint(), body);
        return com.cmcc.vrp.boss.ecinvoker.NmEcBossOperationResultImpl.FAIL;
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    protected abstract String getAppKey();

    protected abstract String getAppSecret();

    protected abstract String getChargeUrl();

    protected abstract String getAuthUrl();

    protected abstract String getKeyPrefix();

    private String getKey() {
        return getKeyPrefix() + ".token";
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
}

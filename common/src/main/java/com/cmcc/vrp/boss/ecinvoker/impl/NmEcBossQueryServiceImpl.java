package com.cmcc.vrp.boss.ecinvoker.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.beijing.model.OrderReqHeader;
import com.cmcc.vrp.boss.beijing.model.QueryReqBody;
import com.cmcc.vrp.boss.beijing.model.QueryReqPara;
import com.cmcc.vrp.boss.beijing.model.QueryWebRequest;
import com.cmcc.vrp.boss.beijing.util.TeaUtil;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcAuthService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcCacheService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcChargeService;
import com.cmcc.vrp.boss.ecinvoker.utils.DateEcUitl;
import com.cmcc.vrp.boss.ecinvoker.utils.SignatureEcUtil;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.bean.AuthRespData;
import com.cmcc.vrp.ec.bean.QueryItem;
import com.cmcc.vrp.ec.bean.QueryResp;
import com.cmcc.vrp.exception.HttpBadRequestException;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.NmHttpUtils;
import com.thoughtworks.xstream.XStream;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月7日 下午3:56:54
*/
@Service
public class NmEcBossQueryServiceImpl implements BaseBossQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(NmEcBossQueryServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private ChargeRecordService recordService;

    @Autowired
    private SerialNumService serialNumService;
    
    @Autowired
    private EcCacheService cacheService;

    @Autowired
    private EcAuthService authService;
    
    @Autowired
    private EcChargeService chargeService;

    @Override
    public BossQueryResult queryStatus(String systemNum) {
        LOGGER.info("内蒙古EC渠道查询充值状态开始了,systemNum:{}", systemNum);
        String respStr;
        String orderId;
        SerialNum serialNum;
        if (StringUtils.isBlank(systemNum)
            || (recordService.getRecordBySN(systemNum)) == null
            || (serialNum = serialNumService.getByPltSerialNum(systemNum)) == null
            || StringUtils.isBlank(orderId = serialNum.getBossRespSerialNum())) {
            LOGGER.error("内蒙古EC渠道查询充值状态失败，参数缺失");
            return BossQueryResult.FAILD;
        }
        
        String token;
        AuthResp response;
        AuthRespData authorizationResp;
        
        String appKey = getAppKey();
        String appSecrect = getAppSecret();
        String authUrl = getAuthUrl();
        String queryUrl = getQueryUrl() + "/" + orderId + ".html";
        
        // 获取token
        if (org.apache.commons.lang.StringUtils.isBlank(token = cacheService.getAccessToken(getKey()))
            && (response = authService.auth(appKey, appSecrect, authUrl)) != null //当缓存没有去调用认证接口
            && (authorizationResp = response.getAuthRespData()) != null) {
            token = authorizationResp.getToken();
            DateTime deadTime = ISODateTimeFormat.dateTimeParser().parseDateTime(authorizationResp.getExpiredTime());
            String interval = DateEcUitl.minutesBetween(new DateTime(), deadTime);
            cacheService.saveAccessToken(token, interval, getKey());
        }
        
        String signature = SignatureEcUtil.signatrue2DoGet(appSecrect);
        QueryResp resp;
        try {
            resp = qureyChargeResult(token, signature, queryUrl);
            LOGGER.info("内蒙古EC渠道渠道查询充值状态的返回包体:{}", resp);
            if (resp != null
                    && resp.getItems() != null
                    && !resp.getItems().isEmpty()) {
                QueryItem queryItem = resp.getItems().get(0);
                if(queryItem.getStatus() == 1
                        || queryItem.getStatus() == 2) {
                    return BossQueryResult.PROCESSING;
                } else if(queryItem.getStatus() == 4){
                    BossQueryResult result = BossQueryResult.FAILD;
                    result.setMsg(queryItem.getDescription());
                    return result;
                } else if (queryItem.getStatus() == 3) {
                    return BossQueryResult.SUCCESS;
                } 
            } 
        } catch (HttpBadRequestException e) {
            LOGGER.info("内蒙古EC渠道渠道查询充值状态的返回包体:{}", 400);
            return BossQueryResult.PROCESSING;
        }
        

        return BossQueryResult.FAILD; 
    }
    
    
    /**
     * @param token
     * @param signature
     * @param url
     * @return
     * @throws HttpBadRequestException 
     */
    public QueryResp qureyChargeResult(String token, String signature, String url) throws HttpBadRequestException {
        if (StringUtils.isBlank(token)
                || StringUtils.isBlank(signature)) {
            return null;
        }
        String resp = NmHttpUtils.nmGet(url, null, buildHeaders(token, signature));
        XStream xStream = new XStream();
        xStream.alias("Response", QueryResp.class);
        xStream.autodetectAnnotations(true);

        return (QueryResp) xStream.fromXML(resp);
    }
    private static Map<String, String> buildHeaders(String token, String signature) {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        if (StringUtils.isNotBlank(token)) {
            headers.put("4GGOGO-Auth-Token", token);
        }

        if (StringUtils.isNotBlank(signature)) {
            headers.put("HTTP-X-4GGOGO-Signature", signature);
        }

        headers.put("Content-Type", "application/xml");
        return headers;
    }
    
    @Override
    public BossOperationResult queryStatusAndMsg(final String systemNum) {
        final BossQueryResult queryResult = queryStatus(systemNum);
               
        return new BossOperationResult(){

            @Override
            public String getResultCode() {        
                return queryResult.getCode();
            }

            @Override
            public boolean isSuccess() {
                return queryResult.getCode().equals(BossQueryResult.SUCCESS.getCode());
            }

            @Override
            public boolean isAsync() {
                return false;
            }

            @Override
            public String getResultDesc() {
                return queryResult.getMsg();
            }

            @Override
            public Object getOperationResult() {
                return null;
            }

            @Override
            public boolean isNeedQuery() {
                return false;
            }

            @Override
            public String getFingerPrint() {
                return null;
            }

            @Override
            public String getSystemNum() {
                return systemNum;
            }

            @Override
            public Long getEntId() {
                return null;
            }
            
        };
    }

    @Override
    public String getFingerPrint() {
        return "neimenggu123456789";
    }

    private QueryWebRequest buildRequest(String orderId) {
        QueryWebRequest webRequest = new QueryWebRequest();
        webRequest.setHeader(buildHeader());
        webRequest.setWebBody(buildBody(orderId));
        return webRequest;
    }

    private OrderReqHeader buildHeader() {
        OrderReqHeader reqHeader = new OrderReqHeader();
        reqHeader.setChannelId(TeaUtil.encrypt("EDSMP"));
        reqHeader.setRequstTime(TeaUtil.encrypt(DateUtil.getBjBossTime()));
        return reqHeader;
    }

    private QueryReqBody buildBody(String orderId) {
        QueryReqBody reqBody = new QueryReqBody();
        reqBody.setPara(buildPara(orderId));
        return reqBody;
    }

    private QueryReqPara buildPara(String orderId) {
        QueryReqPara param = new QueryReqPara();
        param.setPassword(TeaUtil.encrypt(getPassWord()));
        param.setCa(TeaUtil.encrypt(getCa()));
        param.setCurrentPage(TeaUtil.encrypt("1"));
        param.setAdminName(TeaUtil.encrypt(getAdminName()));
        param.setNumber(TeaUtil.encrypt("5"));
        param.setOrderId(TeaUtil.encrypt(orderId));
        return param;
    }

    private String getAdminName() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_ADMIN_NAME.getKey());
    }

    private String getCa() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_CA.getKey());
    }

    private String getPassWord() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_PASSWORD.getKey());
    }
    
    
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_EC_APPKEY.getKey());
    }

    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_EC_APPSECRET.getKey());
    }

    protected String getQueryUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_EC_QUERY_URL.getKey());
    }

    protected String getAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_EC_AUTH_URL.getKey());
    }
    
    private String getKey() {
        return getKeyPrefix() + ".token";
    }
    
    protected String getKeyPrefix() {
        return "neimenggu";
    }
}


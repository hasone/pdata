package com.cmcc.vrp.boss.henan.service;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.openplatform.common.util.SecurityUtils;
import com.cmcc.vrp.boss.henan.Util.HaAES256;
import com.cmcc.vrp.boss.henan.Util.HaSign;
import com.cmcc.vrp.boss.henan.model.HaAuthReq;
import com.cmcc.vrp.boss.henan.model.HaAuthResp;
import com.cmcc.vrp.boss.henan.model.HaConstats;
import com.cmcc.vrp.boss.henan.model.HaQueryBalanceResp;
import com.cmcc.vrp.boss.henan.model.HaQueryResp;
import com.cmcc.vrp.boss.henan.model.HaQueryStatusResp;
import com.cmcc.vrp.boss.henan.model.MemberDeal;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by leelyn on 2016/8/17.
 */
public abstract class AbstractHaBossService implements HaQueryBossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHaBossService.class);

    @Autowired
    private HaCacheService cacheService;

    @Autowired
    private HaAuthService authService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private Gson gson;

    private String appId;

    private String appkey;

    private String domain;

    private String timestamp;

    private String accessToken;

    protected abstract String getAPPID();

    protected abstract String getAPPKEY();

    protected void initPara() {
        appId = getAPPID();
        appkey = getAPPKEY();
        domain = getDomain();
        timestamp = DateUtil.getHenanBossTime();
        accessToken = getAccessToken();
    }

    @Override
    public final MemberDeal queryMemberDeal(String phone) {
        String exception;
        LOGGER.info("查询河南BOSS订购记录开始");
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        //初始化
        initPara();

        JSONObject object = new JSONObject();
        object.put("BILL_ID", phone);
        String queryStr = object.toString();
        String respStr = doRequest(queryStr, "SO_MEMBER_DEAL_QUERY");
        HaQueryResp resp = gson.fromJson(respStr, HaQueryResp.class);
        String result;
        try {
            if (resp == null
                    || !resp.getRespCode().equals(HaConstats.QueryOrderResp.success.getCode())
                    || StringUtils.isBlank(result = resp.getResult())) {
                LOGGER.error("没有查询到该号码:{}的在河南BOSS侧的添加纪录", phone);
                return null;
            }
            String decode = SecurityUtils.decodeAES256HexUpper(result, SecurityUtils.decodeHexUpper(appkey));
            return gson.fromJson(decode, MemberDeal.class);
        } catch (IllegalBlockSizeException e) {
            exception = e.getMessage();
        } catch (InvalidKeyException e) {
            exception = e.getMessage();
        } catch (BadPaddingException e) {
            exception = e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            exception = e.getMessage();
        } catch (NoSuchPaddingException e) {
            exception = e.getMessage();
        } catch (UnsupportedEncodingException e) {
            exception = e.getMessage();
        } catch (Exception e) {
            exception = e.getMessage();
        }
        LOGGER.error("查询河南BOSS订购记录抛出异常:{}", exception);
        return null;
    }


    @Override
    public final HaQueryStatusResp queryMemberStatus(String phone, String bossReqNum) {
        LOGGER.info("查询河南BOSS成员处理情况开始");
        if (StringUtils.isBlank(phone)
                || StringUtils.isBlank(bossReqNum)) {
            return null;
        }
        //初始化
        initPara();

        JSONObject object = new JSONObject();
        object.put("BILL_ID", phone);
        object.put("CUST_ORDER_ID", bossReqNum);
        String queryStr = object.toString();

        String result = doRequest(queryStr, "SO_MEMBER_RETURN_QUERY");
        return gson.fromJson(result, HaQueryStatusResp.class);
    }

    @Override
    public final HaQueryBalanceResp queryGrpBalance(String billId) {
        LOGGER.info("查询河南BOSS集团账户余额开始");
        if (StringUtils.isBlank(billId)) {
            return null;
        }

        initPara();

        JSONObject object = new JSONObject();
        object.put("BILL_ID", billId);
        String queryStr = object.toString();

        String result = doRequest(queryStr, "QRY_GRP_BALANCE");
        return gson.fromJson(result, HaQueryBalanceResp.class);
    }

    /**
     * 请求BOSS侧
     *
     * @param queryStr
     * @return
     */
    protected String doRequest(String queryStr, String method) {
        String sign = HaSign.sign(method, appId, appkey, accessToken, queryStr, timestamp);
        String url = domain + "?" + combineUrl(accessToken, sign, timestamp, appId, method);
        LOGGER.info(method + "请求包体:{},URL:{}", queryStr, url);
        String result = HttpUtils.post(url, HaAES256.encryption(queryStr, appkey), "application/json");
        LOGGER.info(method + "返回包体:{}", result);
        return result;
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
    protected HaAuthReq buildHAR() {
        HaAuthReq authReq = new HaAuthReq();
        authReq.setAppId(getAPPID());
        authReq.setAppKey(getAPPKEY());
        authReq.setGrantType("client_credentials");
        return authReq;
    }

    /**
     * 获取域名
     *
     * @return
     */
    protected String getDomain() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_CHARGE_DOMAIN.getKey());
    }

    /**
     * 组合URL参数
     *
     * @param accessToken
     * @param sign
     * @param timestamp
     * @return
     */
    protected String combineUrl(String accessToken, String sign, String timestamp, String appId, String method) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("method=" + method);
        buffer.append("&format=json");
        buffer.append("&appId=" + appId);
        buffer.append("&busiSerial=1");
        buffer.append("&version=1.0");
        buffer.append("&accessToken=" + accessToken);
        buffer.append("&timestamp=" + timestamp);
        buffer.append("&sign=" + sign);
        return buffer.toString();
    }
}

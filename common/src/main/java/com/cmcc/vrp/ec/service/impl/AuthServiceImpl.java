package com.cmcc.vrp.ec.service.impl;

import com.google.gson.Gson;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.bean.AuthRespData;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.NetTrafficAuthReq;
import com.cmcc.vrp.ec.bean.NetTrafficAuthResp;
import com.cmcc.vrp.ec.exception.ForbiddenException;
import com.cmcc.vrp.ec.exception.ParaErrorException;
import com.cmcc.vrp.ec.model.SignKey;
import com.cmcc.vrp.ec.service.AuthPojo;
import com.cmcc.vrp.ec.service.AuthService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EntWhiteListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import redis.clients.jedis.JedisPool;

/**
 * Created by leelyn on 2016/5/18.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    SignKey signKey;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    EntWhiteListService entWhiteListService;
    @Autowired
    private GlobalConfigService globalConfigService;

    public static void main(String[] args) {
        AuthServiceImpl authService = new AuthServiceImpl();
        String time = authService.getDeadTime();
        System.out.println(time);
    }

    @Override
    public AuthResp getToken(AuthReq authReq, String ipAddress) throws ParaErrorException, ForbiddenException {
        String appKey = null;
        String sign = null;
        String token = null;
        String requestTime = null;
        AuthReqData author = null;

        if (authReq == null
                || (author = authReq.getAuthorization()) == null
                || StringUtils.isBlank(appKey = author.getAppKey())
                || StringUtils.isBlank(sign = author.getSign())
                || StringUtils.isBlank(requestTime = authReq.getRequestTime())
                || (StringUtils.isNotBlank(author.getSecInterface()) 
                        && !(Constants.SEC_INTERFACE.equals(author.getSecInterface())
                                || Constants.JIAKAI_INTERFACE.equals(author.getSecInterface())))) {

            logger.info("认证失败：报文格式错误。请求报文：" + new Gson().toJson(authReq) + ",请求IP：" + ipAddress);
            throw new ForbiddenException(MessageFormat.format(
                    "Auth request para is error.author={0},appKey={1},sign={2},requestTime={3}", author, appKey, sign,
                    requestTime));
        }

        //请求超时
        if (isOutOfDate(requestTime)) {
            logger.info("认证失败：请求超时。请求报文：" + new Gson().toJson(authReq) + ",请求IP：" + ipAddress);
            throw new ForbiddenException("Request out of date.requestTime=" + requestTime);
        }

        //判断是否为双创接口
        AuthPojo authPojo = isShuanChuanInterface(author) ? authShuanChuang(author.getAppKey(), author.getAppSecret()) //双创企业交给双创平台校验
                : isJiaKaiInterface(author) ? authJiaKai(author.getAppKey(), author.getAppSecret()) 
                        : authCommon(appKey, sign, requestTime, ipAddress); //平台企业

        //校验不通过，拜拜
        if (authPojo == null) {
            throw new ForbiddenException("AuthPojo returns null. Check Auth sign faild");
        }

        //校验企业appkey是否过期
        Enterprise enterprise = enterprisesService.selectByAppKey(authPojo.getAppKey());
        String interfaceExpireFlag = globalConfigService.get(GlobalConfigKeyEnum.INTERFACE_EXPIRE_TIME.getKey());
        if (!StringUtils.isEmpty(interfaceExpireFlag) && !("-1").equals(interfaceExpireFlag)) {
            if (enterprise.getInterfaceExpireTime() != null && enterprise.getInterfaceExpireTime().before(new Date())) {
                logger.info("认证失败：appKey过期。请求报文：" + new Gson().toJson(authReq) + ",请求IP：" + ipAddress);
                throw new ForbiddenException("enterprise appkey is out of date.appKey=" + appKey);
            }
        }

        if (StringUtils.isNotBlank(token = buildToken(authPojo))) {
            AuthResp authResp = new AuthResp();

            AuthRespData respData = new AuthRespData();
            respData.setToken(token);
            respData.setExpiredTime(new DateTime().plusMinutes(30).toString()); //这里扣减5分钟是为了防止生成token的时间和发布的时间之间有时延
            respData.setCreatedTime(new DateTime().toString());

            authResp.setAuthRespData(respData);
            authResp.setResponseTime(new DateTime().toString());

            return authResp;
        }

        return null;
    }

    private boolean isShuanChuanInterface(AuthReqData ard) {
        return StringUtils.isNotBlank(ard.getSecInterface()) && ard.getSecInterface().equals(Constants.SEC_INTERFACE);
    }
    private boolean isJiaKaiInterface(AuthReqData ard) {
        return StringUtils.isNotBlank(ard.getSecInterface()) && ard.getSecInterface().equals(Constants.JIAKAI_INTERFACE);
    }

    private AuthPojo authCommon(String appKey, String sign, String requestTime, String ipAddress) {
        Enterprise enterprise = enterprisesService.selectByAppKey(appKey);
        if (enterprise != null) {
            //EC接口ip白名单开关，1-打开，需皮校验；0-关闭，不检查ip
            String ipFlag = globalConfigService.get(GlobalConfigKeyEnum.EC_IP_WHITELIST.getKey());
            if (!StringUtils.isEmpty(ipFlag) && "1".equals(ipFlag)
                    && !entWhiteListService.isIpInEntWhiteList(ipAddress, enterprise.getId())) {
                logger.error("认证失败：请求的IP不在企业IP白名单中。 请求IP:{}, appKey:{}", ipAddress, appKey);
                return null;
            }
            if (enterprise.getDeleteFlag() != 0 || enterprise.getInterfaceFlag() != 1) {
                logger.error("认证失败：企业暂停或接口关闭. 请求IP = {}, 企业appKey = {}", ipAddress, appKey);
                return null;
            }

            String appSecret = enterprise.getAppSecret();
            if (checkSign(appKey, appSecret, requestTime, sign)) {
                return build(appKey, null, null, null);
            }
        } else {
            logger.error("认证失败：企业不存在. 请求IP = {}, 企业appKey = {}", ipAddress, appKey);
        }

        return null;
    }

    private AuthPojo authShuanChuang(String appKey, String appSecret) {
        NetTrafficAuthResp netTrafficAuthResp = null;
        NetTrafficAuthReq netTrafficAuthReq = new NetTrafficAuthReq();

        netTrafficAuthReq.setApiKey(appKey);
        netTrafficAuthReq.setSecretKey(appSecret);

        String request = JSON.toJSONString(netTrafficAuthReq);
        String response = HttpUtils.post(getNetTrafficAuthUrl(), request, "application/json");
        if (StringUtils.isNotBlank(response)) {
            netTrafficAuthResp = JSON.parseObject(response, NetTrafficAuthResp.class);
            if (NumberUtils.toInt(netTrafficAuthResp.getResultCode()) == HttpServletResponse.SC_OK) {
                //从这里开始，双创企业的appKey就被替换为双创平台的appKey+fingerprint的表达方式
                String entAppKey = enterprisesService.selectByCode(getShuangchuangCode()).getAppKey();
                String fingerprint = netTrafficAuthResp.getCorporateIdentify();
                return build(entAppKey, fingerprint, appKey, appSecret);
            }
        }

        return null;
    }
    private AuthPojo authJiaKai(String appKey, String appSecret) {
        NetTrafficAuthResp netTrafficAuthResp = null;
        NetTrafficAuthReq netTrafficAuthReq = new NetTrafficAuthReq();

        netTrafficAuthReq.setApiKey(appKey);
        netTrafficAuthReq.setSecretKey(appSecret);

        String request = JSON.toJSONString(netTrafficAuthReq);
        logger.info("jiakai请求报文" + request);
        String response = HttpUtils.post(getJiaKaiAuthUrl(), request, "application/json");
        logger.info("jiakai响应报文" + response);
        if (StringUtils.isNotBlank(response)) {
            netTrafficAuthResp = JSON.parseObject(response, NetTrafficAuthResp.class);
            if (NumberUtils.toInt(netTrafficAuthResp.getResultCode()) == HttpServletResponse.SC_OK) {
                //从这里开始，双创企业的appKey就被替换为双创平台的appKey+fingerprint的表达方式
                String entAppKey = enterprisesService.selectByCode(getJiaKaiCode()).getAppKey();
                String fingerprint = netTrafficAuthResp.getCorporateIdentify();
                return build(entAppKey, fingerprint, appKey, appSecret);
            }
        }

        return null;
    }

    private boolean checkSign(String appKey, String appSecret, String requestTime, String sign) {
        String expectedSign = DigestUtils.sha256Hex(appKey + requestTime + appSecret);
        if (!expectedSign.equals(sign)) {
            logger.error("认证失败：签名错误。appKey:{}, sign:{},expectedSign:{}", appKey, sign, expectedSign);
        }

        return expectedSign.equals(sign);
    }

    private boolean isOutOfDate(String requestTime) {
        final int nSecs = 30;
        try {
            boolean result = ISODateTimeFormat.dateTimeParser().parseDateTime(requestTime).plusSeconds(nSecs)
                    .isBeforeNow();
            if (result) {
                logger.info("请求超时，报文请求时间：" + requestTime + ",系统时间：" + new Date() + ",约定请求时间与系统时间相差不超过" + nSecs + "秒！");
            }
            return result;
        } catch (Exception e) {
            logger.error("Out of date check failed.", e);
            return true;
        }
    }

    private String buildToken(AuthPojo pojo) {
        if (pojo == null) {
            return null;
        }

        return Jwts.builder().setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE) //set tpy
                .setIssuer(Constants.ISSUER) //set iss
                .setAudience(concat(pojo)) //set aud
                .setExpiration(new DateTime().plusMinutes(getValidateMinutes()).toDate()) //set exp
                .signWith(randomHSAlg(), signKey.buildKey(pojo.getAppKey())).compact();
    }

    private AuthPojo build(String appKey, String fingerPrint, String subAppKey, String subAppSecret) {
        AuthPojo authPojo = new AuthPojo();
        authPojo.setAppKey(appKey);
        authPojo.setFingerprint(fingerPrint);
        authPojo.setSubAppKey(subAppKey);
        authPojo.setSubAppSecret(subAppSecret);

        return authPojo;
    }

    private String concat(AuthPojo authPojo) {
        if (authPojo == null) {
            return null;
        }

        return authPojo.getAppKey() + ":" + convert(authPojo.getFingerprint()) + ":" + convert(authPojo.getSubAppKey())
                + ":" + convert(authPojo.getSubAppSecret());
    }

    private String convert(String content) {
        return StringUtils.isBlank(content) ? "" : content;
    }

    private SignatureAlgorithm randomHSAlg() {

        SignatureAlgorithm[] algs = new SignatureAlgorithm[]{SignatureAlgorithm.HS256, SignatureAlgorithm.HS384,
                SignatureAlgorithm.HS512};
        return algs[RandomUtils.nextInt(algs.length)];
    }

    private String getDeadTime() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
        return DateFormatUtils.format(DateUtil.addMins(new Date(), getValidateMinutes() - 5), pattern);//这里扣减5分钟是为了防止生成token的时间和发布的时间之间有时延
    }

    public String getNetTrafficAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.SHUANCHANG_URL.getKey());
    }

    public String getShuangchuangCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.SHUANCHANG_CODE.getKey());
    }
    
    public String getJiaKaiAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.JIAKAI_URL.getKey());
    }

    public String getJiaKaiCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.JIAKAI_CODE.getKey());
    }

    public int getValidateMinutes() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.TOKEN_VALID_PERIOD.getKey()));
    }
}

package com.cmcc.vrp.aop;

import java.util.Date;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.model.SignKey;
import com.cmcc.vrp.ec.service.AuthPojo;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EntWhiteListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.IpUtils;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by leelyn on 2016/5/18.
 */
@Aspect
@Component
public class AuthAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuthAspect.class);

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    SignKey signKey;

    @Autowired
    JedisPool jedisPool;

    @Autowired
    EntWhiteListService entWhiteListService;

    @Autowired
    GlobalConfigService globalConfigService;

    @Pointcut("execution(public * com.cmcc.vrp.async.controller..*(..))")
    private void allAsyncMethods() {
    }

    @Pointcut("execution(* com.cmcc.vrp.mdrc.controller.MdrcInterfaceController.operate(..))")
    private void allMdrcMethods() {
    }

    @Pointcut("execution(* com.cmcc.vrp.mdrc.controller.MdrcInterfaceController.use(..))")
    private void useMdrcMethod() {
    }

    @Pointcut("execution(* com.cmcc.vrp.province.webin.controller.OrderFlowController.order(..))")
    private void hainanBossOrder() {
    }

    @Pointcut("execution(* com.cmcc.vrp.async.controller.AuthController.auth(..))")
    private void authMenthod() {
    }
    
    @Pointcut("execution(* com.cmcc.vrp.async.controller.UploadFileController.uploadFile(..))")
    private void uploadFile() {
    }
    
    @Pointcut("execution(* com.cmcc.vrp.province.AsynCallbackController.sdCallback(..))")
    private void sdCloudCallBack() {
    }
    
    @Pointcut("execution(* com.cmcc.vrp.async.controller.GDCheckUserExistController.gdCheckUser(..))")
    private void gdCheckUser() {
    }
    
    @Pointcut("execution(* com.cmcc.vrp.async.controller.GDSessionInvalidController.gdInvalidUser(..))")
    private void gdInvalidUser() {
    }

    /**
     * @param joinPoint
     * @return
     */
    @Around("(allAsyncMethods() || allMdrcMethods() || useMdrcMethod() || hainanBossOrder()) && !authMenthod() "
            + "&& !uploadFile() || sdCloudCallBack() || gdCheckUser() || gdInvalidUser()")
    public Object around(ProceedingJoinPoint joinPoint) {
        // get req and resp
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        logger.debug("Request from {}:{}", IpUtils.getRemoteAddr(req), req.getRemotePort());

        // get param
        String token = req.getHeader(Constants.TOKEN_HEADER);
        String signature = req.getHeader(Constants.SIGNATURE_HEADER);
        String bodyXml = retrieveBodyXml(req);
        String ipAddress = IpUtils.getRemoteAddr(req);

        logger.debug("Receive request with token = {}.", token);

        // auth
        AuthResult authResult = auth(token, signature, bodyXml, resp, ipAddress);
        if (authResult == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        // add serialNum and appKey attr
        String systemNum = SerialNumGenerator.buildSerialNum();
        req.setAttribute(Constants.SYSTEM_NUM_ATTR, systemNum);
        req.setAttribute(Constants.APP_KEY_ATTR, authResult.getAppKey());
        req.setAttribute(Constants.FINGERPRINT, authResult.getFingerprint());
        req.setAttribute(Constants.BODY_XML_ATTR, bodyXml);

        // everything is ok, insert record
        logger.debug("Authentication success, now ready to insert appInvoke record.");
        try {
            logger.debug("Invoke {}:{} with serialNum {}.", joinPoint
                            .getTarget().getClass(), joinPoint.getSignature(),
                    systemNum);
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("invoke interface failed...", throwable);
        }

        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return null;
    }

    private boolean checkSignature(String appSecret, String bodyXml,
                                   String signature) {
        return DigestUtils.sha256Hex((StringUtils.isBlank(bodyXml) ? "" : bodyXml) + appSecret).equals(signature);
    }

    private String parse(String audience) {
        if (StringUtils.isBlank(audience) || !audience.contains(":")) {
            return null;
        }

        String[] strs = audience.split(":");
        return strs[0];
    }

    private AuthPojo build(String content) {
        String[] strs = null;
        if (StringUtils.isBlank(content)
                || !content.contains(":")
                || (strs = content.split(":", -1)).length != 4) {
            return null;
        }

        AuthPojo authPojo = new AuthPojo();

        authPojo.setAppKey(strs[0]);
        authPojo.setFingerprint(strs[1]);
        authPojo.setSubAppKey(strs[2]);
        authPojo.setSubAppSecret(strs[3]);

        return authPojo;
    }

    private String auth(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public byte[] resolveSigningKeyBytes(JwsHeader header,
                                                             Claims claims) {
                            String audience = claims.getAudience();
                            return signKey.buildKey(parse(audience));
                        }
                    }).requireIssuer(Constants.ISSUER).parseClaimsJws(token)
                    .getBody().getAudience();
        } catch (Exception e) {
            logger.error("Authentication to {} fail.", token);
            return null;
        }
    }

    private String retrieveBodyXml(HttpServletRequest request) {
        try {
            return StreamUtils.copyToString(request.getInputStream(),
                    Charsets.UTF_8);
        } catch (Exception e) {
            logger.error("获取请求内容时出错，错误信息为{}.", e.toString());
            return null;
        }
    }

    private AuthResult auth(String token, String signature, String bodyXml, HttpServletResponse resp, String ipAddress) {
        try {
            //validate
            if (StringUtils.isBlank(token)
                    || StringUtils.isBlank(signature)) {
                logger.error("无效的认证参数. Token = {}，Signature = {}.", token, signature);
            }

            String audience = auth(token);
            if (audience == null) {
                logger.error("解析token结果为空，认证失败.Token = {}.", token);
                return null;
            }

            AuthPojo authPojo = build(audience);
            if (authPojo == null) {
                logger.error("解析AuthPojo结果为空，认证失败.Authdience = {}.", audience);
                return null;
            }

            //检查企业appkey是否过期
            Enterprise enterprise = enterprisesService.selectByAppKey(authPojo.getAppKey());
            String interfaceExpireFlag = globalConfigService.get(GlobalConfigKeyEnum.INTERFACE_EXPIRE_TIME.getKey());
            if(!StringUtils.isEmpty(interfaceExpireFlag) && !("-1").equals(interfaceExpireFlag)){
                if (enterprise != null && enterprise.getInterfaceExpireTime() != null
                        && enterprise.getInterfaceExpireTime().before(new Date())) {
                    logger.error("该企业appkey已过期，appkey = {}", enterprise.getAppKey());
                    return null;
                }
            }

            //检查发送请求ip是否在企业ip白名单中
            //双创企业不做校验 
            //EC接口ip白名单开关，1-打开，需皮校验；0-关闭，不检查ip
            String ipFlag = globalConfigService.get(GlobalConfigKeyEnum.EC_IP_WHITELIST.getKey());
            if (!StringUtils.isEmpty(ipFlag) && "1".equals(ipFlag) && StringUtils.isBlank(authPojo.getFingerprint())) {
                if (enterprise == null || !entWhiteListService.isIpInEntWhiteList(ipAddress, enterprise.getId())) {
                    logger.error("请求的IP不在企业IP白名单中，请求IP = {}", ipAddress);
                    return null;
                }
            }

            if (!auth(authPojo, bodyXml, signature)) {
                logger.error("认证时出错, AuthPojo = {}, BodyXML = {}, Signature = {}.", new Gson().toJson(authPojo), bodyXml, signature);
                throw new IllegalArgumentException("Authentication fail.");
            }

            return build(authPojo.getAppKey(), authPojo.getFingerprint());
        } catch (Exception e) {
            logger.error("认证时出错, 错误信息为{}. Signature = {}, BodyXML = {}.",
                    e.toString(), signature, bodyXml);
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        return null;
    }

    private AuthResult build(String appKey, String fingerPrint) {
        AuthResult authResult = new AuthResult();
        authResult.setAppKey(appKey);
        authResult.setFingerprint(fingerPrint);

        return authResult;
    }

    //如果authPojo的fingerprint不为空，那么认为是平台中的企业，用平台的appkey/appsecret进行认证
    //否则，直接使用企业的信息进行认证
    private boolean auth(AuthPojo authPojo, String bodyXml, String signature) {
        String appSecret = null;

        if (StringUtils.isBlank(authPojo.getFingerprint())) {  //直接通过企业信息认证
            logger.debug("FingerPrint为空，通过平台企业信息进行认证.");

            Enterprise enterprise = enterprisesService.selectByAppKey(authPojo.getAppKey());
            if (enterprise == null || enterprise.getDeleteFlag() == Constants.DELETED_FLAG) {
                return false;
            }

            appSecret = enterprise.getAppSecret();
        } else { //通过平台企业的信息认证
            logger.debug("FingerPrint不为空，通过双创平台企业信息进行认证.");

            appSecret = authPojo.getSubAppSecret();
        }

        return checkSignature(appSecret, bodyXml, signature);
    }

    private class AuthResult {
        private String appKey;
        private String fingerprint;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }
    }
}

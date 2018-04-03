package com.cmcc.vrp.boss.chongqing.service.impl;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.chongqing.pojo.model.QryLeftTraffic;
import com.cmcc.vrp.boss.chongqing.pojo.model.QryLeftTrafficResp;
import com.cmcc.vrp.boss.chongqing.pojo.model.Token;
import com.cmcc.vrp.boss.chongqing.service.UserQryService;
import com.cmcc.vrp.boss.chongqing.web.HttpsUtil;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;

/**
 * @author lgk8023
 *
 */
@Service
public class UserQryServiceImpl implements UserQryService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserQryServiceImpl.class);
    
    public static void main(String[] args) {
        UserQryServiceImpl userQryServiceImpl = new UserQryServiceImpl();
        userQryServiceImpl.qryLeftTraffic("15902305318", "0", "201708");
    }
    @Autowired
    private GlobalConfigService globalConfigService;
    @Override
    public String qryLeftTraffic(String mobile, String type, String cycle) {
        LOGGER.info("用户套餐量剩余信息查询开始，mobile-{}, type-{}, cycle-{}", mobile, type, cycle);
        if (StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(type)
                || StringUtils.isEmpty(cycle)
                || !StringUtils.isValidMobile(mobile)
                || !validate(type, cycle)) {
            LOGGER.error("请求参数错误，mobile-{}", mobile);
            return null;
        }
        String accessToken = getToken();
        if (StringUtils.isEmpty(accessToken)) {
            LOGGER.error("获取accessToken失败");
            return null;
        }
        
        String url = getUrl() + "?access_token=" + accessToken + "&method=qryLeftTraffic&format=json";
        String params = buildParam(mobile, type, cycle);
        LOGGER.info("查询请求参数url-{}, params-{}", url, params);
        String response = null;
        QryLeftTrafficResp qryLeftTrafficResp = null;
        Gson gson = new Gson();
        try {
            response = HttpsUtil.post(url, params);
            LOGGER.info("查询响应报文-{}", response);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            qryLeftTrafficResp = gson.fromJson(response, QryLeftTrafficResp.class);
        } catch (KeyManagementException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (NoSuchProviderException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        return gson.toJson(qryLeftTrafficResp);
    }
    private String buildParam(String mobile, String type, String cycle) {
        QryLeftTraffic qryLeftTraffic = new QryLeftTraffic();
        qryLeftTraffic.setTelnum(mobile);
        qryLeftTraffic.setType(type);
        qryLeftTraffic.setCycle(cycle);
        Gson gson = new Gson();
        
        return gson.toJson(qryLeftTraffic);
    }

    private String getToken() {
        String clientId = getClientId();
        String clientSecret = getClientSecret();
        String url = getTokenUrl();
        if (StringUtils.isEmpty(clientId)
                || StringUtils.isEmpty(clientSecret)
                || StringUtils.isEmpty(url)) {
            LOGGER.error("获取token请求参数错误，clientid-{}, clientsecret-{}, url-{}", clientId, clientSecret, url);
        }
        String  param = "grant_type=client_credentials&client_id=" + clientId +"&client_secret=" + clientSecret;
        LOGGER.info("获取token请求地址-{}", url);
        LOGGER.info("获取token报文-{}", param);
        String response;
        Gson gson = new Gson();
        Token accessTokenResp = null;
        try {
            response = HttpsUtil.getToken(url, param);
            LOGGER.info("获取token响应结果-{}", response);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            accessTokenResp = gson.fromJson(response, Token.class);
        } catch (KeyManagementException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (NoSuchProviderException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        if (accessTokenResp != null) {
            return accessTokenResp.getToken();
        }
        return null;
    }
    private boolean validate(String type, String cycle) {
        if (!("0".equals(type)
                || "4a".equals(type)
                || "4b".equals(type))) {
            LOGGER.error("查询类型错误-" + type);
            return false;
        }
        Date date = DateUtil.parse("yyyyMM", cycle);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        int rang = DateUtil.getMonthsOfAge(calendar1, calendar2);
        if (rang < 0
                || rang > 1) {
            LOGGER.error("日期超过可查区间-" + cycle);
            return false;
        }
        
        return true;
    }
    
    private String getUrl() {
        //return "https://183.230.30.244:7102/openapi/httpService/UserQryService";
        return globalConfigService.get(GlobalConfigKeyEnum.QRY_LEFT_TRAFFIC_URL.getKey());
    }
    
    private String getClientId() {
        return globalConfigService.get(GlobalConfigKeyEnum.QRY_LEFT_TRAFFIC_CLIENT_ID.getKey());
        //return "20170726000098025";
    }
    
    private String getClientSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.QRY_LEFT_TRAFFIC_CLIENT_SECRET.getKey());
        //return "5ce4bb6ba1c8f3c1d3b304656cacbcb8";
    }
    
    private String getTokenUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.QRY_LEFT_TRAFFIC_TOKEN_URL.getKey());
        //return "https://183.230.30.244:7102/OAuth/restOauth2Server/access_token";
    }
}

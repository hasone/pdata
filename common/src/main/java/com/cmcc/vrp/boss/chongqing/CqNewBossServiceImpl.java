package com.cmcc.vrp.boss.chongqing;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.chongqing.pojo.model.NewChargeReq;
import com.cmcc.vrp.boss.chongqing.pojo.model.NewChargeResp;
import com.cmcc.vrp.boss.chongqing.pojo.model.Token;
import com.cmcc.vrp.boss.chongqing.service.response.impl.CQBOSSChargeResponse;
import com.cmcc.vrp.boss.chongqing.web.HttpsUtil;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * @author lgk8023
 * 闲时流量包
 */
@Service
public class CqNewBossServiceImpl implements BossService {

    private static final Logger logger = LoggerFactory.getLogger(CqNewBossServiceImpl.class);
    
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        logger.info("重庆闲时流量包充值start-{}", serialNum);
        SupplierProduct supplierProduct = null;
        if (splPid == null
            || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
            || (enterprisesService.selectByPrimaryKey(entId)) == null
            || StringUtils.isBlank(mobile)
            || StringUtils.isBlank(serialNum)) {
            logger.info("调重庆BOSS充值接口失败：参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return new CQBOSSChargeResponse("102", "参数错误");
        }
        Date date = DateUtil.getBeginMonthOfDate(new Date());
        List<ChargeRecord> chargeRecords = chargeRecordService.getRecordsByMobileAndPrd(mobile, splPid, date);
        if (chargeRecords != null
                && chargeRecords.size() > 0) {
            logger.error("当月不可以重复订购. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return new CQBOSSChargeResponse("103", "当月不可以重复订购");
        }
        String accessToken = getToken();
        if (StringUtils.isEmpty(accessToken)) {
            logger.error("获取accessToken失败");
            return new CQBOSSChargeResponse("102", "获取token失败");
        }
        
        String url = getUrl() + "?access_token=" + accessToken + "&method=IncProductSrv2&format=json";
        String prdCode = supplierProduct.getCode();
        String param = build(prdCode, mobile);
        CqNewBossOperationResultImpl response = null;
        try {
            logger.info("充值请求地址-{}", url);
            logger.info("充值请求报文-{}", param);
            String result = HttpsUtil.post(url, param);
            logger.info("充值响应报文-{}", result);
            Gson gson = new Gson();
            NewChargeResp newChargeResp = gson.fromJson(result, NewChargeResp.class);
            response = new CqNewBossOperationResultImpl(newChargeResp.getResCode(), newChargeResp.getResDesc());
        } catch (KeyManagementException e) {
            logger.error(e.getMessage());
            response = new CqNewBossOperationResultImpl("001", "boss连接异常");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            response = new CqNewBossOperationResultImpl("001", "boss连接异常");
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage());
            response = new CqNewBossOperationResultImpl("001", "boss连接异常");
        } catch (IOException e) {
            logger.error(e.getMessage());
            response = new CqNewBossOperationResultImpl("001", "boss连接异常");
        }
        // TODO Auto-generated method stub
        return response;
    }

    private String build(String prdCode, String mobile) {
        NewChargeReq newChargeReq = new NewChargeReq();
        Gson gson = new Gson();
        newChargeReq.setTelnum(mobile);
        newChargeReq.setNcode(prdCode);
        newChargeReq.setEffType("0");
        newChargeReq.setParm("8");
        newChargeReq.setOperType("ADD");
        return gson.toJson(newChargeReq);
    }

    @Override
    public String getFingerPrint() {
        // TODO Auto-generated method stub
        return "chongqingnew";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        // TODO Auto-generated method stub
        return false;
    }
    private String getToken() {
        String clientId = getClientId();
        String clientSecret = getClientSecret();
        String url = getTokenUrl();
        if (StringUtils.isEmpty(clientId)
                || StringUtils.isEmpty(clientSecret)
                || StringUtils.isEmpty(url)) {
            logger.error("获取token请求参数错误，clientid-{}, clientsecret-{}, url-{}", clientId, clientSecret, url);
            return null;
        }
        String  param = "grant_type=client_credentials&client_id=" + clientId +"&client_secret=" + clientSecret;
        logger.info("获取token请求地址-{}", url);
        logger.info("获取token报文-{}", param);
        String response;
        Gson gson = new Gson();
        Token accessTokenResp = null;
        try {
            response = HttpsUtil.getToken(url, param);
            logger.info("获取token响应结果-{}", response);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            accessTokenResp = gson.fromJson(response, Token.class);
        } catch (KeyManagementException e) {
            logger.error(e.getMessage());
            return null;
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            return null;
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage());
            return null;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
        if (accessTokenResp != null) {
            return accessTokenResp.getToken();
        }
        return null;
    }
    private String getUrl() {
        //return "https://183.230.30.244:7101/openapi/httpService/OrderService";
        return globalConfigService.get(GlobalConfigKeyEnum.CQ_NEW_BOSS_URL.getKey());
    }
    
    private String getClientId() {
        return globalConfigService.get(GlobalConfigKeyEnum.CQ_NEW_BOSS_CLIENT_ID.getKey());
        //return "20170726000098025";
    }
    
    private String getClientSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.CQ_NEW_BOSS_CLIENT_SECRET.getKey());
        //return "5ce4bb6ba1c8f3c1d3b304656cacbcb8";
    }
    
    private String getTokenUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.CQ_NEW_BOSS_TOKEN_URL.getKey());
        //return "https://183.230.30.244:7101/OAuth/restOauth2Server/access_token";
    }
}

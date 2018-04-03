package com.cmcc.vrp.boss.shanghai.service.impl;

import com.amazonaws.util.json.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.shanghai.model.common.BusiParams;
import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.PubInfo;
import com.cmcc.vrp.boss.shanghai.model.common.QueryStatus;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.queryorderstate.QosAsiaResult;
import com.cmcc.vrp.boss.shanghai.model.queryorderstate.QosRetInfo;
import com.cmcc.vrp.boss.shanghai.model.queryorderstate.QosReturnContent;
import com.cmcc.vrp.boss.shanghai.model.queryorderstate.QueryOrderStateInMap;
import com.cmcc.vrp.boss.shanghai.model.queryorderstate.QueryOrderStateReq;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lilin on 2016/9/14.
 */
@Service
public class ShBossQueryServiceImpl implements BaseBossQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShBossQueryServiceImpl.class);

    @Autowired
    private SerialNumService serialNumService;

    @Autowired
    private ChargeRecordService recordService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private Gson gson;

    public static void main(String[] args) {
        ShBossQueryServiceImpl shBossQueryService = new ShBossQueryServiceImpl();
        InMap map = shBossQueryService.getMap("123456789");
        QueryOrderStateReq stateReq = shBossQueryService.buildQOSR(map);
        String reqStr = new Gson().toJson(stateReq);
        System.out.println(reqStr);
    }

    @Override
    public BossQueryResult queryStatus(String systemNum) {
        String bossReqNum;
        SerialNum serialNum;
        if (StringUtils.isBlank(systemNum)
                || (recordService.getRecordBySN(systemNum)) == null
                || (serialNum = serialNumService.getByPltSerialNum(systemNum)) == null
                || StringUtils.isBlank(bossReqNum = serialNum.getBossReqSerialNum())) {
            LOGGER.error("查询上海充值状态，找不到相应的订单");
            return BossQueryResult.FAILD;
        }
        String reqStr;
        String interfaceSerialNum;
        InMap inMap = getMap(bossReqNum);
        QueryOrderStateReq stateReq = buildQOSR(inMap);
        try {
            com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent cilent = 
                    new com.cmcc.vrp.boss.shanghai.openapi.client.OpenapiHttpCilent(getAppCode(), getApk(), getSecurityUrl(), getOpenapiUrl());
            reqStr = new Gson().toJson(stateReq);
            interfaceSerialNum = SerialNumGenerator.buildSerialNum();
            LOGGER.info("订单状态查询请求包体:{},接口请求流水:{}", reqStr, interfaceSerialNum);
            String resp = cilent.call(getApiCode(), interfaceSerialNum, reqStr);
            LOGGER.info("订单状态查询响应包体:{}", resp);
            JSONObject jsonObj;
            String status;
            String response;
            QosAsiaResult result;
            ErrorInfo errorInfo;
            QosRetInfo retInfo;
            QosReturnContent returnContent;
            String state;
            if (org.apache.commons.lang.StringUtils.isNotBlank(resp)
                    && (jsonObj = new JSONObject(resp)) != null
                    && org.apache.commons.lang.StringUtils.isNotBlank(status = jsonObj.get("status").toString())
                    && "SUCCESS".equals(status)
                    && org.apache.commons.lang.StringUtils.isNotBlank(response = jsonObj.get("result").toString())
                    && (result = gson.fromJson(response, QosAsiaResult.class)) != null
                    && (errorInfo = result.getResponse().getErrorInfo()) != null
                    && (errorInfo.getCode().equals("0000"))
                    && (retInfo = result.getResponse().getRetInfo()) != null
                    && retInfo.getReturnCode().equals("0000")
                    && (returnContent = retInfo.getReturnContent()) != null
                    && StringUtils.isNotBlank(state = returnContent.getState())) {
                if (state.equals(QueryStatus.SUCCESS_DEAL.getCode())) {
                    return BossQueryResult.SUCCESS;
                } else if (state.equals(QueryStatus.FAILD_DEAL.getCode())) {
                    LOGGER.error("查询上海充值渠道返回失败,原因:{},平台流水:{},BOSS请求流水:{}", returnContent.getMsg(), systemNum, bossReqNum);
                    return BossQueryResult.FAILD;
                } else if (state.equals(QueryStatus.HAS_SUBMIT.getCode())) {
                    return BossQueryResult.PROCESSING;
                } else if (state.equals(QueryStatus.NO_DEAL.getCode())) {
                    return BossQueryResult.PROCESSING;
                }
            }
        } catch (Exception e) {
            LOGGER.error("订单状态查询抛出异常:{}", e.getMessage());
            return BossQueryResult.EXCEPTION;
        }
        return BossQueryResult.FAILD;
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
        return "shanghainational123456789";
    }

    protected InMap getMap(String bossReqNum) {
        QueryOrderStateInMap inMap = new QueryOrderStateInMap();
        inMap.setTransIDO(bossReqNum);
        return inMap;
    }

    protected Request getRequest(InMap inMap) {
        Request request = new Request();
        request.setBusiCode("PT-SH-FS-OI4186");
        BusiParams busiParams = new BusiParams();
        busiParams.setFlag("11");
        busiParams.setInMap(inMap);
        request.setBusiParams(busiParams);
        return request;
    }

    private QueryOrderStateReq buildQOSR(InMap inMap) {
        QueryOrderStateReq stateReq = new QueryOrderStateReq();
        stateReq.setPubInfo(getPubInfo());
        stateReq.setRequest(getRequest(inMap));
        return stateReq;
    }

    private PubInfo getPubInfo() {
        PubInfo pubInfo = new PubInfo();
        pubInfo.setClientIP("10.10.141.98");
        pubInfo.setCountyCode("021");
        pubInfo.setInterfaceId("79");
        pubInfo.setOpId("999990077");
        pubInfo.setRegionCode("021");
        pubInfo.setTransactionId(SerialNumGenerator.buildSerialNum());
        pubInfo.setTransactionTime(DateUtil.getShBossTime());
        pubInfo.setOrgId("1");
        pubInfo.setInterfaceType("06");
        return pubInfo;
    }

    private String getAppCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_APPCODE.getKey());
    }

    private String getApk() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_APK.getKey());
    }

    private String getApiCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_APICODE.getKey());
    }
    private String getSecurityUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_OLD_SECURITY_URL.getKey());
        //return "http://211.136.164.123/open/security";
    }
    
    private String getOpenapiUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_OLD_OPENAPI_URL.getKey());
        //return "http://211.136.164.123/open/service";
    }
}

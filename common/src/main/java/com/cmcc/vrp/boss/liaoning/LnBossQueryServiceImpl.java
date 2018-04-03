package com.cmcc.vrp.boss.liaoning;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.openplatform.utils.AIESBConstants;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.liaoning.model.Detail;
import com.cmcc.vrp.boss.liaoning.model.LnChargeResp;
import com.cmcc.vrp.boss.liaoning.util.LnGlobalConfig;
import com.cmcc.vrp.boss.liaoning.util.SignUtil;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
* <p>Title: LnBossQueryServiceImpl</p>
* <p>Description: 辽宁充值结果查询</p>
* @author lgk8023
* @date 2017年2月9日 下午3:07:28
*/
@Service
public class LnBossQueryServiceImpl implements BaseBossQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(LnBossQueryServiceImpl.class);
    private static String TIME_FOMMAT = "yyyyMMddHHmmss";

    @Autowired
    private ChargeRecordService recordService;

    @Autowired
    private SerialNumService serialNumService;
    
    @Autowired
    LnGlobalConfig lnGlobalConfig;
    
    @Autowired
    Gson gson;
//    
//    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");
//        LnBossQueryServiceImpl bossService = (LnBossQueryServiceImpl)context.getBean("lnBossQueryServiceImpl");
//        System.out.println(bossService.queryStatus("6247321037451800576"));
//    }

    @Override
    public BossQueryResult queryStatus(String systemNum) {
        LOGGER.info("辽宁渠道查询充值状态开始了,systemNum:{}", systemNum);
        String orderId = null;
        SerialNum serialNum;
        ChargeRecord chargeRecord = null;
        if (StringUtils.isBlank(systemNum)
            || (chargeRecord = recordService.getRecordBySN(systemNum)) == null
            || (serialNum = serialNumService.getByPltSerialNum(systemNum)) == null
            || StringUtils.isBlank(orderId = serialNum.getBossReqSerialNum())) {
            LOGGER.error("辽宁渠道查询充值状态失败，参数缺失");
            return BossQueryResult.FAILD;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        String mobile = chargeRecord.getPhone();
        sdf = new SimpleDateFormat("yyyyMMdd");
        String begin = sdf.format(chargeRecord.getChargeTime());
        String end = sdf.format(new Date());
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("ln", 10);
        Map<String, String> sysParams = buildSysParam(bossReqNum, requestTime);
        String busiParams = buildBusiParam(bossReqNum, requestTime, orderId, mobile, begin, end).toString();
        LOGGER.info("辽宁充值结果查询请求系统参数" + sysParams);
        LOGGER.info("辽宁充值结果查询请求业务参数" + busiParams);
        LnChargeResp resp;
        try {
            String response = SignUtil.execute(sysParams, busiParams, AIESBConstants.PROTOCOL.HTTP, lnGlobalConfig.getAppKey(), lnGlobalConfig.getUrl());
            LOGGER.info("辽宁boss充值结果返回：" + response);
            if (StringUtils.isNotBlank(response)
                    && (resp = gson.fromJson(response, LnChargeResp.class)) != null) {
                String respCode = resp.getResponse().getRetInfo().getErrorInfo().getCode();
                String respDes = resp.getResponse().getRetInfo().getErrorInfo().getMessage();
                List<Detail> details = resp.getResponse().getRetInfo().getDetail();
                if ("0000".equals(respCode)) {
                    if (details.isEmpty()) {
                        BossQueryResult queryResult = BossQueryResult.FAILD;
                        queryResult.setMsg(respDes);
                        return queryResult;
                    } else {
                        Detail detail = details.get(0);
                        if ("2".equals(detail.getSATE())) {
                            return BossQueryResult.SUCCESS;
                        } else {
                            BossQueryResult queryResult = BossQueryResult.FAILD;
                            queryResult.setMsg(respDes);
                            return queryResult;
                        }
                    }
                    
                }
                BossQueryResult queryResult = BossQueryResult.FAILD;
                queryResult.setMsg(respDes);
                return queryResult;
            }
            return BossQueryResult.FAILD;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            return BossQueryResult.EXCEPTION;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return BossQueryResult.EXCEPTION;
        }
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
        return "liaoning";
    }


    /**
     * @param bossReqNum
     * @param requestTime
     * @return
     */
    private Map<String, String> buildSysParam(String bossReqNum, String requestTime) {
        
        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("method", "OI_QueryDetail");
        sysParam.put("format", "json");
        sysParam.put("timestamp", requestTime);
        sysParam.put("appId", lnGlobalConfig.getAppId());
        sysParam.put("version", "1.0");
        sysParam.put("operId", lnGlobalConfig.getOperId());
        sysParam.put("accessToken", lnGlobalConfig.getAccessToken());
        sysParam.put("openId", lnGlobalConfig.getOpenId());
        sysParam.put("busiSerial", bossReqNum);
        return sysParam;
    }
    

    /**
     * @param bossReqNum
     * @param requestTime
     * @param batchNO
     * @param mobile
     * @param end 
     * @param begin 
     * @return
     */
    private JSONObject buildBusiParam(String bossReqNum, String requestTime, String batchNO, String mobile, String begin, String end) {
        JSONObject busiParam = new JSONObject();    
        busiParam.put("Request", buildRequest(batchNO, mobile, begin, end));
        busiParam.put("PubInfo", buildPubInfo(bossReqNum, requestTime));
        
        return busiParam;
    }
    
    private JSONObject buildRequest(String batchNO, String mobile, String begin, String end) {
        JSONObject request = new JSONObject();
        request.put("BusiParams", buildBusiParams(batchNO, mobile, begin, end));
        request.put("BusiCode", "OI_QueryDetail"); 
        return request;
    }
    
    private JSONObject buildBusiParams(String batchNO, String mobile, String begin, String end) {
        JSONObject busiParam = new JSONObject();
        busiParam.put("BatchNO", batchNO);
        busiParam.put("BillId", mobile);
        busiParam.put("STATE", "-1");
        busiParam.put("Begin", "1");
        busiParam.put("End", "1");
        return busiParam;
    }
    
    /**
     * @return
     */
    private JSONObject buildPubInfo(String bossReqNum, String requestTime) {
        JSONObject pubInfo = new JSONObject();
        pubInfo.put("TransactionTime", requestTime);
        pubInfo.put("OrgId", "40001000");
        pubInfo.put("ClientIP", "");
        pubInfo.put("RegionCode", "400");
        pubInfo.put("CountyCode", "4000");
        pubInfo.put("InterfaceType", "88");
        pubInfo.put("TransactionId", bossReqNum);
        pubInfo.put("OpId", "40051860");
        pubInfo.put("InterfaceId", "81");
        return pubInfo;
    }
}


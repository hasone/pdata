package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmcc.vrp.boss.guangxi.enums.CallbackResult;
import com.cmcc.vrp.boss.guangxi.enums.CallbackStatus;
import com.cmcc.vrp.boss.guangxi.model.AdditionResult;
import com.cmcc.vrp.boss.guangxi.model.BIPType;
import com.cmcc.vrp.boss.guangxi.model.GxCallbackBody;
import com.cmcc.vrp.boss.guangxi.model.GxCallbackHeader;
import com.cmcc.vrp.boss.guangxi.model.GxCallbackResp;
import com.cmcc.vrp.boss.guangxi.model.Response;
import com.cmcc.vrp.boss.guangxi.model.Routing;
import com.cmcc.vrp.boss.guangxi.model.RoutingInfo;
import com.cmcc.vrp.boss.guangxi.model.TransInfoResp;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.util.DateUtil;
import com.thoughtworks.xstream.XStream;

/**
 * Created by leelyn on 2016/9/18.
 */
@Controller
@RequestMapping(value = "/guangxi/charge")
public class GxCallBackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GxCallBackController.class);

    private static final String XMLHEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    @Autowired
    private ChargeRecordService recordService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TaskProducer taskProducer;

    @Autowired
    private SerialNumService serialNumService;

    @RequestMapping(value = "callback", method = RequestMethod.POST)
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("广西回调来了");
        String xmlhead = request.getParameter("xmlhead");
        String xmlbody = request.getParameter("xmlbody");
        LOGGER.info("广西回调xmlhead:{},广西回调xmlbody:{}", xmlhead, xmlbody);
        GxCallbackHeader cbHeader;
        GxCallbackBody cbBody;
        String svcCont;
        AdditionResult result;
        try {
            if ((cbHeader = xml2Obj(GxCallbackHeader.class, "InterBOSS", xmlhead)) != null
                    && (cbBody = xml2Obj(GxCallbackBody.class, "InterBOSS", xmlbody)) != null
                    && StringUtils.isNotBlank(svcCont = cbBody.getSvcCont())
                    && (result = xml2Obj(AdditionResult.class, "AdditionResult", svcCont)) != null) {
                if (updateRecordStatus(result)) {
                    LOGGER.info("广西回调后更新充值记录成功");
                } else {
                    LOGGER.info("广西回调后更新充值记录失败");
                }
                GxCallbackResp resp = buildCbResp(CallbackResult.SUCCESS.getRspCode(), CallbackResult.SUCCESS.getRspDsc(), cbHeader);
                XStream xStream = new XStream();
                xStream.autodetectAnnotations(true);
                xStream.alias("InterBOSS", GxCallbackResp.class);
                String respStr = composeXML(xStream.toXML(resp));
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(respStr);
                LOGGER.info("响应广西回调成功");
                return;
            }
        } catch (Exception e) {
            LOGGER.error("广西回调解析包体异常:{}", e);
        }

    }

    private <T> T xml2Obj(Class<T> cls, String rootTag, String data) {
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias(rootTag, cls);
        return (T) xStream.fromXML(data);
    }

    private String composeXML(String body) {
        StringBuffer SB = new StringBuffer();
        SB.append(XMLHEADER);
        SB.append(body);
        return SB.toString();
    }

    private GxCallbackResp buildCbResp(String rspCode, String rspDesc, GxCallbackHeader cbHeader) {
        GxCallbackResp resp = new GxCallbackResp();
        resp.setVersion("0100");
        resp.setTestFlag("1");
        resp.setbIPType(buildBIPType());
        resp.setRoutingInfo(buildRoutInfo());
        resp.setTransInfo(buildTransInfo(cbHeader));
        resp.setResponse(buidlResp(rspCode, rspDesc));
        return resp;
    }

    private Response buidlResp(String rspCode, String rspDesc) {
        Response response = new Response();
        response.setRspCode(rspCode);
        response.setRspDesc(rspDesc);
        response.setRspType("0");
        return response;
    }

    private BIPType buildBIPType() {
        BIPType type = new BIPType();
        type.setbIPCode("BIP4B877");
        type.setActivityCode("T4011138");
        type.setActionCode("1");
        return type;
    }

    private RoutingInfo buildRoutInfo() {
        RoutingInfo routingInfo = new RoutingInfo();
        routingInfo.setOrigDomain("BBSS");
        routingInfo.setRouteType("00");
        routingInfo.setRouting(buildRouting());
        return routingInfo;
    }

    private Routing buildRouting() {
        Routing routing = new Routing();
        routing.setHomeDomain("STKP");
        routing.setRouteValue("998");
        return routing;
    }

    private TransInfoResp buildTransInfo(GxCallbackHeader cbHeader) {
        TransInfoResp transInfoResp = new TransInfoResp();
        transInfoResp.setSessionID(cbHeader.getTransInfo().getSessionID());
        transInfoResp.setTransIDO(cbHeader.getTransInfo().getTransIDO());
        transInfoResp.setTransIDOTime(cbHeader.getTransInfo().getTransIDOTime());
        transInfoResp.setTransIDH(SerialNumGenerator.buildNormalBossReqNum("ZYXX", 25));
        transInfoResp.setTransIDHTime(DateUtil.getGxBossTime());
        return transInfoResp;
    }

    //获取到充值结果后更新记录状态
    private boolean updateRecordStatus(AdditionResult result) {
        //String bossRespNum = result.getOperSeq();
        String bossRespNum = result.getTransIDO();
        String successNum = result.getSuccNum();
        String status = result.getStatus();
        SerialNum serialNum;
        String systemNum;
        ChargeRecord record;
        if ((serialNum = serialNumService.getByBossRespSerialNum(bossRespNum)) == null
                || StringUtils.isBlank(systemNum = serialNum.getPlatformSerialNum())
                || (record = recordService.getRecordBySN(systemNum)) == null) {
            LOGGER.error("未找到相应的订单,更新失败");
            return false;
        }
        Long entId = record.getEnterId();
        ChargeResult chargeResult;
        Date updateChargeTime = new Date();
        Integer financeStatus = null;
        if (!"00".equals(status)) {
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);
            if (result.getFailInfo() != null
                    && !StringUtils.isEmpty(result.getFailInfo().getRspDesc())) {
                chargeResult.setFailureReason(result.getFailInfo().getRspDesc());
            } else {
                chargeResult.setFailureReason(CallbackStatus.getRspDsc(status));
            }
            if (!accountService.returnFunds(systemNum)) {
                LOGGER.error("充值serialNum{},entId{}失败时账户返还失败", systemNum, entId);
            }else{
                financeStatus = FinanceStatus.IN.getCode();
            } 
        } else if (StringUtils.isNotBlank(successNum)
                && !"0".equals(successNum)) {
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.SUCCESS);
        } else {
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);
            if (result.getFailInfo() != null
                    && !StringUtils.isEmpty(result.getFailInfo().getRspDesc())) {
                chargeResult.setFailureReason(result.getFailInfo().getRspDesc());
            } else {
                chargeResult.setFailureReason("充值失败");
            }
            if (!accountService.returnFunds(systemNum)) {
                LOGGER.error("充值serialNum{},entId{}失败时账户返还失败", systemNum, entId);
            }else{
                financeStatus = FinanceStatus.IN.getCode();
            }
        }
        
        chargeResult.setFinanceStatus(financeStatus);
        chargeResult.setUpdateChargeTime(updateChargeTime);
        
        boolean isUpdate = updateRecordStatus(record, chargeResult);

        // 将回调EC消息放入消息队列中
        callbackEC(entId, systemNum);

        return isUpdate;
    }

    /**
     * 回掉EC平台
     *
     * @param entId
     * @param systemNum
     */
    private void callbackEC(Long entId, String systemNum) {
        // 将消息入列，实现回调EC平台
        CallbackPojo callbackPojo = new CallbackPojo();
        callbackPojo.setEntId(entId);
        callbackPojo.setSerialNum(systemNum);
        taskProducer.productPlatformCallbackMsg(callbackPojo);
    }

    //获取到充值结果后更新记录状态
    private boolean updateRecordStatus(ChargeRecord chargeRecord, ChargeResult chargeResult) {
        ChargeRecordStatus chargeStatus = null;
        String errorMsg = null;

        if (chargeResult.isSuccess()) {
            if (chargeResult.getCode().equals(ChargeResult.ChargeResultCode.SUCCESS)) {
                chargeStatus = ChargeRecordStatus.COMPLETE;
                errorMsg = ChargeRecordStatus.COMPLETE.getMessage();
            } else {
                chargeStatus = ChargeRecordStatus.PROCESSING;
                errorMsg = ChargeRecordStatus.PROCESSING.getMessage();
            }
        } else {
            chargeStatus = ChargeRecordStatus.FAILED;
            errorMsg = chargeResult.getFailureReason();
        }
        
        chargeStatus.setFinanceStatus(chargeResult.getFinanceStatus());
        chargeStatus.setUpdateChargeTime(chargeResult.getUpdateChargeTime());

        return recordService.updateStatus(chargeRecord.getId(), chargeStatus, errorMsg);
    }
}

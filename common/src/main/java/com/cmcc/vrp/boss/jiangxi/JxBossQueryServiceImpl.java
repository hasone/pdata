package com.cmcc.vrp.boss.jiangxi;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.jiangxi.model.JxQueryBodyRep;
import com.cmcc.vrp.boss.jiangxi.model.JxQueryBodyReq;
import com.cmcc.vrp.boss.jiangxi.model.JxQueryHead;
import com.cmcc.vrp.boss.jiangxi.model.QueryOrderInfoReq;
import com.cmcc.vrp.boss.jiangxi.model.QueryOrderInfoRsp;
import com.cmcc.vrp.boss.jiangxi.util.SecurityAESTool;
import com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType;
import com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortTypeProxy;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * @author lgk8023
 *
 */
@Service
public class JxBossQueryServiceImpl implements BaseBossQuery {
    private static final Logger LOGGER = LoggerFactory.getLogger(JxBossQueryServiceImpl.class);
    private static String TIME_FOMMAT = "yyyyMMddHHmmss";
    @Autowired
    private ChargeRecordService chargeRecordService;
    
    @Autowired
    private SerialNumService serialNumService;
    
    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public BossQueryResult queryStatus(String systemNum) {
        LOGGER.info("江西渠道查询充值状态开始了,systemNum:{}", systemNum);
        String orderId = null;
        String time = null;
        SerialNum serialNum;
        ChargeRecord chargeRecord = null;
        if (StringUtils.isBlank(systemNum)
            || (chargeRecord = chargeRecordService.getRecordBySN(systemNum)) == null
            || (serialNum = serialNumService.getByPltSerialNum(systemNum)) == null
            || StringUtils.isBlank(orderId = serialNum.getBossReqSerialNum())
            || StringUtils.isBlank(time = serialNum.getBossRespSerialNum())) {
            LOGGER.error("江西渠道查询充值状态失败，参数缺失");
            return BossQueryResult.FAILD;
        }
        String mobile = chargeRecord.getPhone();
        
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("jiangxi", 8);
        String reqStr = buildReqStr(buildHead(bossReqNum), encryptBody(buildBody(mobile, orderId, time), getAppSecret()));
        LOGGER.info("江西渠道充值结果查询请求包体:{}", reqStr);
        IfAPServicePortType ifAPService = new IfAPServicePortTypeProxy();
        JxQueryBodyRep bodyResp = null;
        try {
            String recMsg = ifAPService.queryOrderInfo(reqStr);
            LOGGER.info("江西渠道充值结果查询响应包体:{}", recMsg);
            QueryOrderInfoRsp rsp;
            String body;
            if ((rsp = xml2Obj(QueryOrderInfoRsp.class, "QueryOrderInfoRsp", recMsg)) != null
                    && StringUtils.isNotBlank(body = rsp.getBody())
                    && StringUtils.isNotBlank(body = decryptBody(body, getAppSecret()))
                    && (bodyResp = xml2Obj(JxQueryBodyRep.class, "BODY", body)) != null) {
                String resultCode = bodyResp.getResultCode();
                if (!"00".equals(resultCode)) {
                    BossQueryResult queryResult = BossQueryResult.FAILD;
                    queryResult.setMsg(bodyResp.getResultMsg());
                    return queryResult;
                }
                if (!"0".equals(bodyResp.getDealCode())) {
                    BossQueryResult queryResult = BossQueryResult.FAILD;
                    queryResult.setMsg(bodyResp.getDealMsg());
                    return queryResult;
                }
                if ("1".equals(bodyResp.getStatus())) {
                    BossQueryResult queryResult = BossQueryResult.FAILD;
                    queryResult.setMsg("流量办理失败");
                    return queryResult;
                }
                return BossQueryResult.SUCCESS;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return BossQueryResult.EXCEPTION;
        }
        return BossQueryResult.FAILD;
    }

    @Override
    public BossOperationResult queryStatusAndMsg(String systemNum) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFingerPrint() {
        // TODO Auto-generated method stub
        return "jiangxiquery";
    }
    private <T> T xml2Obj(Class<T> cls, String rootTag, String data) {
        LOGGER.info("Xml转对象之前String:{}", data);
        XStream xStream = new XStream(new DomDriver("utf-8", new XmlFriendlyNameCoder("-_", "_")));
        xStream.autodetectAnnotations(true);
        xStream.alias(rootTag, cls);
        return (T) xStream.fromXML(data);
    }
    private JxQueryHead buildHead(String bossReqNum) {
        JxQueryHead head = new JxQueryHead();
        head.setCode("QueryOrderInfo");
        head.setSid(bossReqNum);
        head.setTimeStmp(DateUtil.getJiangXiBossTime());
        head.setServiceId(getServiceId());
        return head;
    }

    private JxQueryBodyReq buildBody(String phone, String orderSid, String time) {
        JxQueryBodyReq body = new JxQueryBodyReq();
        body.setEcCode(getEcCode());
        body.setPhone(phone);
        body.setOrderSid(orderSid);
        body.setOrderTimestamp(time);
        return body;
    }

    private String buildReqStr(JxQueryHead head, String body) {
        QueryOrderInfoReq queryOrderInfoReq = new QueryOrderInfoReq();
        queryOrderInfoReq.setHead(head);
        queryOrderInfoReq.setBody(body);
        try {
            XStream xStream = new XStream(new DomDriver("utf-8", new XmlFriendlyNameCoder("-_", "_")));
            xStream.alias("QueryOrderInfoReq", QueryOrderInfoReq.class);
            xStream.autodetectAnnotations(true);
            return xStream.toXML(queryOrderInfoReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String encryptBody(JxQueryBodyReq body, String key) {
        XStream xStream = new XStream(new DomDriver("utf-8", new XmlFriendlyNameCoder("-_", "_")));
        xStream.alias("BODY", JxQueryBodyReq.class);
        xStream.autodetectAnnotations(true);
        try {
            return SecurityAESTool.encrypt(xStream.toXML(body), key);
        } catch (Exception e) {
            LOGGER.error("江西渠道加密抛出异常:{}", e.getMessage());
        }
        return new String();
    }

    private String decryptBody(String data, String key) {
        return SecurityAESTool.decrypt(data, key);
    }

    private String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_APPSECRET.getKey());
    }

    private String getServiceId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_SERVICEID.getKey());
    }

    private String getEcCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_ECCODE.getKey());
    }
}

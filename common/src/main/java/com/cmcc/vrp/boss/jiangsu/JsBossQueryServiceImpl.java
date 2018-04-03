package com.cmcc.vrp.boss.jiangsu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.jiangsu.model.Item;
import com.cmcc.vrp.boss.jiangsu.model.Perfcol;
import com.cmcc.vrp.boss.jiangsu.model.QryOrderContent;
import com.cmcc.vrp.boss.jiangsu.model.QryOrderOperationIn;
import com.cmcc.vrp.boss.jiangsu.model.QryOrderOperationOut;
import com.cmcc.vrp.boss.jiangsu.model.QryOrderResponseContent;
import com.cmcc.vrp.boss.jiangsu.model.Qrylist;
import com.cmcc.vrp.boss.jiangsu.model.UserPasswd;
import com.cmcc.vrp.boss.jiangsu.util.GbkBasicResponseHandler;
import com.cmcc.vrp.boss.jiangsu.util.JsHttpUtils;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * 江苏超时调用充值结果查询接口
 * @author lgk8023
 *
 */
@Service
public class JsBossQueryServiceImpl implements BaseBossQuery{
    private static final Logger LOGGER = LoggerFactory.getLogger(JsBossQueryServiceImpl.class);
    private static XStream xStream = null;
    static {
        xStream = new XStream(new DomDriver("GBK", new XmlFriendlyNameCoder("-_", "_")));
        xStream.alias("operation_in", QryOrderOperationIn.class);
        xStream.alias("operation_out", QryOrderOperationOut.class);
        xStream.autodetectAnnotations(true);
    }
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    SerialNumService serialNumService;
    @Override
    public BossQueryResult queryStatus(String systemNum) {
        LOGGER.info("江苏渠道查询充值状态开始了,systemNum:{}", systemNum);
        ChargeRecord chargeRecord = null;
        SupplierProduct supplierProduct = null;
        if (StringUtils.isEmpty(systemNum)
                || (chargeRecord = chargeRecordService.getRecordBySN(systemNum)) == null
                || (supplierProduct = supplierProductService.selectByPrimaryKey(chargeRecord.getSupplierProductId())) == null) {
            LOGGER.error("江苏渠道查询充值状态失败，参数缺失");
            return BossQueryResult.FAILD;
        }
        QryOrderOperationIn operationIn = buildOperationIn(chargeRecord, supplierProduct, systemNum);
        try {
            String req = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xStream.toXML(operationIn);
            LOGGER.info("查询请求报文{}", req);
            String resp = JsHttpUtils.post(getChargeUrl(), req, null, "application/xml", new GbkBasicResponseHandler());
            LOGGER.info("查询响应报文{}", resp);
            if (StringUtils.isEmpty(resp)) {
                return BossQueryResult.FAILD;
            }
            QryOrderOperationOut operationOut = (QryOrderOperationOut) xStream.fromXML(resp);
            if (operationOut == null) {
                LOGGER.error("解析响应报文为空");
                return BossQueryResult.FAILD;
            }
            if (!"0000".equals(operationOut.getResponse().getRespCode())) {
                BossQueryResult result = BossQueryResult.FAILD;
                result.setMsg(operationOut.getResponse().getRespDesc());
                return result;
            }
            QryOrderResponseContent content = operationOut.getContent();
            if (content == null
                    || !"0".equals(content.getRetCode())) {
                return BossQueryResult.FAILD;
            }
            List<Qrylist> qrylists = content.getQrylist();
            if (qrylists == null
                    || qrylists.size() == 0) {
                return BossQueryResult.FAILD;
            }
            for(Qrylist qrylist:qrylists) {
                Item item = null;
                if (qrylist != null
                        && (item = qrylist.getItem()) != null) {
                    if ("1".equals(item.getOrderstatus())) {
                        if (!updateRecord(systemNum, item.getOrderid(), systemNum)) {
                            LOGGER.error("江苏充值更新流水号失败,serialNum:{}.bossRespNum:{}", systemNum, item.getOrderid());
                        }
                        return BossQueryResult.SUCCESS;
                    } else {
                        if (!updateRecord(systemNum, item.getOrderid(), systemNum)) {
                            LOGGER.error("江苏充值更新流水号失败,serialNum:{}.bossRespNum:{}", systemNum, item.getOrderid());
                        }
                        return BossQueryResult.FAILD;
                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error("充值异常：" + e.getMessage());
            return BossQueryResult.EXCEPTION;
        }
        
        return BossQueryResult.FAILD;
    }
    private boolean updateRecord(String systemNum, String bossRespNum, String bossReqNum) {
        if (org.apache.commons.lang.StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setBossRespSerialNum(bossRespNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }

    private QryOrderOperationIn buildOperationIn(ChargeRecord chargeRecord, SupplierProduct supplierProduct, String systemNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String endTime = df.format(new Date());
        String beginTime = df.format(chargeRecord.getChargeTime());
        
        QryOrderOperationIn qryOrderOperationIn = new QryOrderOperationIn();
        qryOrderOperationIn.setProcessCode("cc_qryofferorders");
        qryOrderOperationIn.setAppId(getAppId());
        qryOrderOperationIn.setAccessToken(getAccessToken());
        qryOrderOperationIn.setVerifyCode("");
        qryOrderOperationIn.setRequestType("");
        qryOrderOperationIn.setSysfuncId("");
        qryOrderOperationIn.setOrganId("");
        qryOrderOperationIn.setRouteType("2");
        qryOrderOperationIn.setRouteValue(chargeRecord.getPhone());
        qryOrderOperationIn.setOperatorId(getOperatorId());
        qryOrderOperationIn.setChannelid("");
        qryOrderOperationIn.setRequestTime(endTime);
        qryOrderOperationIn.setRequestSeq(systemNum);
        qryOrderOperationIn.setRequestSource("");
        qryOrderOperationIn.setRequestTarget("");
        qryOrderOperationIn.setMsgVersion("");
        qryOrderOperationIn.setContVersion("");
        
        UserPasswd userPasswd = new UserPasswd();
        userPasswd.setNeed("0");
        qryOrderOperationIn.setUserPasswd(userPasswd);
        
        Perfcol perfcol = new Perfcol();
        perfcol.setNeed("0");
        qryOrderOperationIn.setPerfcol(perfcol);
                
        QryOrderContent content = new QryOrderContent();
        content.setMsisdn(chargeRecord.getPhone());
        content.setOfferCode(supplierProduct.getCode());
        content.setBegintime(beginTime);
        content.setEndtime(endTime);
        content.setChannelid("");
        qryOrderOperationIn.setQryOrderContent(content);
        return qryOrderOperationIn;
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
        // TODO Auto-generated method stub
        return "jiangsu";
    }
    private String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_APPID.getKey());
    }
    private String getAccessToken() {
        return globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_ACCESS_TOKEN.getKey());
    }
    private String getOperatorId() {
        return globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_OPERATOR_ID.getKey());
    }
    
    private String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_CHARGE_URL.getKey());
    }
}

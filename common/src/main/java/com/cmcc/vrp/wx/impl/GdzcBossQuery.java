package com.cmcc.vrp.wx.impl;

import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.wx.enums.BossQueryResult;
import com.cmcc.vrp.wx.model.GetOrderInfoReq;
import com.cmcc.vrp.wx.model.GetOrderInfoResp;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2017年3月9日 下午2:46:27
 */
@Component
public class GdzcBossQuery {
    private static final Logger logger = LoggerFactory.getLogger(GdzcBossQuery.class);
    private static XStream xStream = null;

    static {
        xStream = new XStream();
        xStream.alias("GetOrderInfoReq", GetOrderInfoReq.class);
        xStream.alias("GetOrderInfoResp", GetOrderInfoResp.class);
        xStream.autodetectAnnotations(true);
    }

    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    private ChargeRecordService chargeRecordService;
    @Autowired
    private SerialNumService serialNumService;

    /**
     * @param systemNum
     * @return
     */
    public BossQueryResult queryStatus(String systemNum) {
        logger.info("广东众筹查询充值状态开始了,systemNum:{}", systemNum);
        String orderId = null;
        SerialNum serialNum;
        ChargeRecord chargeRecord = null;
        if (StringUtils.isBlank(systemNum)
                || (chargeRecord = chargeRecordService.getRecordBySN(systemNum)) == null
                || (serialNum = serialNumService.getByPltSerialNum(systemNum)) == null
                || StringUtils.isBlank(orderId = serialNum.getBossReqSerialNum())) {
            logger.error("广东众筹查询充值状态失败，参数缺失");
            return BossQueryResult.FAILD;
        }
        GetOrderInfoReq getOrderInfoReq = buildGetOrderInfoReq(orderId, serialNum.getBossRespSerialNum(), chargeRecord.getPhone());
        String req = xStream.toXML(getOrderInfoReq);
        String url = getOrderInfoUrl();
        String response = HttpUtils.post(url, req, "application/xml");
        logger.info("广东众筹查询充值状态返回：{}", response);
        GetOrderInfoResp getOrderInfoResp = null;
        if (response != null
                && (getOrderInfoResp = (GetOrderInfoResp) xStream.fromXML(response)) != null
                && "0".equals(getOrderInfoResp.getResult())
                && getOrderInfoResp.getMember() != null) {
            if ("0".equals(getOrderInfoResp.getMember().getResultCode())) {
                return BossQueryResult.SUCCESS;
            } else {
                BossQueryResult bossQueryResult = BossQueryResult.FAILD;
                bossQueryResult.setCode(getOrderInfoResp.getMember().getResultCode());
                bossQueryResult.setMsg(getOrderInfoResp.getMember().getResultMsg());
                return bossQueryResult;
            }
        }
        return null;
    }

    private GetOrderInfoReq buildGetOrderInfoReq(String orderId, String bossNum, String phone) {
        GetOrderInfoReq getOrderInfoReq = new GetOrderInfoReq();
        getOrderInfoReq.setMobile(phone);
        getOrderInfoReq.setSerialNum(orderId);
        getOrderInfoReq.setOrderId(bossNum);
        return getOrderInfoReq;
    }

    private String getOrderInfoUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_ORDERINFO_URL.getKey());
        //return "http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=GetTx&svc_code=GetOrderInfo";
    }
}

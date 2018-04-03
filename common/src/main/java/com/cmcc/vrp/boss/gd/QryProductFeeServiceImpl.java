package com.cmcc.vrp.boss.gd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.gd.model.GetFeeResponse;
import com.cmcc.vrp.boss.gd.model.QryProductFeeReq;
import com.cmcc.vrp.boss.gd.model.QryProductFeeResp;
import com.cmcc.vrp.boss.gd.service.QryProductFeeService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * @author lgk8023
 *
 */
public class QryProductFeeServiceImpl implements QryProductFeeService {
    private static final Logger logger = LoggerFactory.getLogger(QryProductFeeServiceImpl.class);
    private static XStream xStream = null;
    
    static{
        xStream = new XStream();
        xStream.alias("QryProductFeeReq", QryProductFeeReq.class);
        xStream.alias("QryProductFeeResp", QryProductFeeResp.class);
        xStream.alias("Response", GetFeeResponse.class);
        xStream.ignoreUnknownElements();
        xStream.autodetectAnnotations(true);
    }
 
    @Autowired
    GlobalConfigService globalConfigService;
    public static void main(String[] args) {
        QryProductFeeServiceImpl qryProductFeeServiceImpl = new QryProductFeeServiceImpl();
        qryProductFeeServiceImpl.qryProductFeeResp("51964000010");
    }
    @Override
    public QryProductFeeResp qryProductFeeResp(String ecPrdCode) {
        QryProductFeeResp qryProductFeeResp = null;
        logger.info("查询产品账户余额start-{}", ecPrdCode);
        if (StringUtils.isEmpty(ecPrdCode)) {
            logger.error("请求参数错误");
            return null;
        }
        QryProductFeeReq qryProductFeeReq = build(ecPrdCode);
        logger.info("查询请求报文-{}", new Gson().toJson(qryProductFeeReq));
        String response = HttpUtils.post(geturl(), xStream.toXML(qryProductFeeReq) , "application/xml");

        logger.info("查询响应报文-{}", response);
        if (StringUtils.isEmpty(response)) {
            logger.error("响应为空，返回null");
            return null;
        }
        qryProductFeeResp = (QryProductFeeResp) xStream.fromXML(response);
        return qryProductFeeResp;
    }


    @Override
    public String getFee(String ecPrdCode) {
        GetFeeResponse response = new GetFeeResponse();
        QryProductFeeResp qryProductFeeResp = qryProductFeeResp(ecPrdCode);
        if (qryProductFeeResp == null) {
            response.setResultCode("1");
            response.setResultMsg("查询结果为空");
            return xStream.toXML(response);
        }
        if (!"0".equals(qryProductFeeResp.getResult())) {
            response.setResultCode(qryProductFeeResp.getResult());
            response.setResultMsg(qryProductFeeResp.getResultMsg());
            return xStream.toXML(response);
        }
        response.setResultCode("0");
        response.setResultMsg(qryProductFeeResp.getReCharge());
        return xStream.toXML(response);
    }
    private QryProductFeeReq build(String ecPrdCode) {
        QryProductFeeReq qryProductFeeReq = new QryProductFeeReq();
        qryProductFeeReq.setEcPrdCode(ecPrdCode);
        return qryProductFeeReq;
    }
    private String geturl() {
        //return "http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=GetTx&svc_code=QryProductFee";
        return globalConfigService.get(GlobalConfigKeyEnum.GD_QRY_PRODUCT_FEE_URL.getKey());
    }
}

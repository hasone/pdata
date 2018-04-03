package com.cmcc.vrp.boss.gd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.gd.model.QryEntSrvRegReq;
import com.cmcc.vrp.boss.gd.model.QryEntSrvRegResp;
import com.cmcc.vrp.boss.gd.service.QryEntSrvRegService;
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
@Service
public class QryEntSrvRegServiceImpl implements QryEntSrvRegService{
    private static final Logger logger = LoggerFactory.getLogger(QryEntSrvRegServiceImpl.class);
    private static XStream xStream = null;
    
    static {
        xStream = new XStream();
        xStream.alias("QryEntSrvRegReq", QryEntSrvRegReq.class);
        xStream.alias("QryEntSrvRegResp", QryEntSrvRegResp.class);
        xStream.ignoreUnknownElements();
        xStream.autodetectAnnotations(true);
    }

    @Autowired
    private GlobalConfigService globalConfigService;
    public static void main(String[] args) {
        QryEntSrvRegServiceImpl qryEntSrvRegServiceImpl = new QryEntSrvRegServiceImpl();
        qryEntSrvRegServiceImpl.qryEntSrvReg("51964000010");
    }
    @Override
    public QryEntSrvRegResp qryEntSrvReg(String ecPrdCode) {
        QryEntSrvRegResp qryEntSrvRegResp = null;
        logger.info("查询产品订购关系start-{}", ecPrdCode);
        if (StringUtils.isEmpty(ecPrdCode)) {
            logger.error("集团产品编码为空");
            return null;
        }
        QryEntSrvRegReq qryEntSrvRegReq = new QryEntSrvRegReq();
        qryEntSrvRegReq.setEcPrdCode(ecPrdCode);
        logger.info("查询请求报文-{}", new Gson().toJson(qryEntSrvRegReq));
        String response = HttpUtils.post(geturl(), xStream.toXML(qryEntSrvRegReq) , "application/xml");

        logger.info("查询响应报文-{}", response);
        if (StringUtils.isEmpty(response)) {
            logger.error("响应为空，返回null");
            return null;
        }
        qryEntSrvRegResp = (QryEntSrvRegResp) xStream.fromXML(response);
        return qryEntSrvRegResp;
    }
    private String geturl() {
        return globalConfigService.get(GlobalConfigKeyEnum.GD_QRY_ENT_SRV_REG_URL.getKey());
        //return "http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=GetTx&svc_code=QryEntSrvReg";
    }

}

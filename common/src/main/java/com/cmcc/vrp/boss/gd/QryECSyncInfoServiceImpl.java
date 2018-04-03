package com.cmcc.vrp.boss.gd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.gd.model.ContactInfo;
import com.cmcc.vrp.boss.gd.model.ECSyncInfo;
import com.cmcc.vrp.boss.gd.model.QryECSyncInfoReq;
import com.cmcc.vrp.boss.gd.model.QryECSyncInfoResp;
import com.cmcc.vrp.boss.gd.model.Response;
import com.cmcc.vrp.boss.gd.service.QryECSyncInfoService;

import com.cmcc.vrp.province.service.ECSyncInfoService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GDRegion;
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
public class QryECSyncInfoServiceImpl implements QryECSyncInfoService{

    private static final Logger logger = LoggerFactory.getLogger(QryECSyncInfoServiceImpl.class);
    private static XStream xStream = null;
    static {
        xStream = new XStream();
        xStream.alias("QryECSyncInfoReq", QryECSyncInfoReq.class);
        xStream.alias("QryECSyncInfoResp", QryECSyncInfoResp.class);
        xStream.alias("Response", Response.class);
        xStream.ignoreUnknownElements();
        xStream.autodetectAnnotations(true);
    }
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ECSyncInfoService ecSyncInfoService;
    public static void main(String[] args) {
        QryECSyncInfoServiceImpl qryECSyncInfoServiceImpl = new QryECSyncInfoServiceImpl();
        qryECSyncInfoServiceImpl.qryECSyncInfo("2000821305");
    }
    @Override
    public QryECSyncInfoResp qryECSyncInfo(String ecCode) {

        logger.info("查询集团信息开始-{}", ecCode);
        QryECSyncInfoResp qryECSyncInfoResp = null;
        if (StringUtils.isEmpty(ecCode)) {
            logger.error("集团编码为空");
            return null;
        }
        QryECSyncInfoReq qryECSyncInfoReq = new QryECSyncInfoReq();
        qryECSyncInfoReq.setEcCode(ecCode);
        logger.info("查询请求报文-{}", new Gson().toJson(qryECSyncInfoReq));
        String response = HttpUtils.post(geturl(), xStream.toXML(qryECSyncInfoReq), "application/xml");
        logger.info("查询响应报文-{}", response);
        if (StringUtils.isEmpty(response)) {
            logger.error("响应为空，返回null");
            return null;
        }
        qryECSyncInfoResp = (QryECSyncInfoResp) xStream.fromXML(response);
        return qryECSyncInfoResp;
    }

    private String geturl() {
        //return "http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=GetTx&svc_code=QryECSyncInfo";
        return globalConfigService.get(GlobalConfigKeyEnum.GD_QRY_EC_SYNC_INFO_URL.getKey());
    }
    @Override
    public String updateECInfo(String ecCode) {
        logger.info("同步信息请求开始-{}", ecCode);
        Response response = new Response();
        if (StringUtils.isEmpty(ecCode)) {
            logger.error("请求参数为空");
            response.setResultCode("1");
            response.setResultMsg("请求参数为空");
            return xStream.toXML(response);
        }
        QryECSyncInfoResp qryECSyncInfoResp = qryECSyncInfo(ecCode);
        if (qryECSyncInfoResp == null) {
            logger.error("查询信息为空");
            response.setResultCode("2");
            response.setResultMsg("查询信息失败");
            return xStream.toXML(response);
        }
        if (!"0".equals(qryECSyncInfoResp.getResult())) {
            logger.error("ADC处理失败-{},失败原因-{}", qryECSyncInfoResp.getResult(), qryECSyncInfoResp.getResultMsg());
            response.setResultCode(qryECSyncInfoResp.getResult());
            response.setResultMsg(qryECSyncInfoResp.getResultMsg());
            return xStream.toXML(response);
        }
        ECSyncInfo ecSyncInfo = qryECSyncInfoResp.getEcSyncInfo();
        com.cmcc.vrp.province.model.ECSyncInfo newEcSyncInfo = initECSyncInfo(ecSyncInfo);
        if (!ecSyncInfoService.updateOrInsert(newEcSyncInfo)) {
            logger.error("更新数据库失败!");
            response.setResultCode("3");
            response.setResultMsg("更新数据库失败!");
            return xStream.toXML(response);
        }
        response.setResultCode("0");
        response.setResultMsg("同步成功");
        return xStream.toXML(response);
    }
    private com.cmcc.vrp.province.model.ECSyncInfo initECSyncInfo(ECSyncInfo ecSyncInfo) {
        com.cmcc.vrp.province.model.ECSyncInfo newEcSyncInfo = new com.cmcc.vrp.province.model.ECSyncInfo();
        
        List<ContactInfo> contactInfos = ecSyncInfo.getContactInfo();
        ContactInfo contactInfo = getContactInfo(contactInfos);
        
        newEcSyncInfo.setEcCode(ecSyncInfo.getEcCode());
        newEcSyncInfo.setEcName(ecSyncInfo.getEcName());
        newEcSyncInfo.setRegion(GDRegion.getAreaName(ecSyncInfo.getRegion()));
        newEcSyncInfo.setLegalPerson(ecSyncInfo.getLegalPerson());
        newEcSyncInfo.setEntPermit(ecSyncInfo.getEntPermit());
        newEcSyncInfo.setUserName(contactInfo.getUserName());
        newEcSyncInfo.setMobile(contactInfo.getMobile());
        newEcSyncInfo.setEmail(contactInfo.getEmail());
        newEcSyncInfo.setMainContact(Integer.valueOf(contactInfo.getMainContact()));
        newEcSyncInfo.setEcLevel(ecSyncInfo.getEcLevel());
        newEcSyncInfo.setUnitKind(ecSyncInfo.getUnitKind());
        newEcSyncInfo.setDistrict(GDRegion.getAreaName(ecSyncInfo.getDistrict()));
        newEcSyncInfo.setInnetDate(DateUtil.parse("yyyy-MM-dd", ecSyncInfo.getInnetDate()));
        newEcSyncInfo.setVipType(ecSyncInfo.getVipType());
        newEcSyncInfo.setVipTypeStateDate(DateUtil.parse("yyyy-MM-dd", ecSyncInfo.getVipTypeStateDate()));
        newEcSyncInfo.setCreditLevel(ecSyncInfo.getCreditLevel());
        newEcSyncInfo.setDevChannel(ecSyncInfo.getDevChannel());
        newEcSyncInfo.setDevUserId(ecSyncInfo.getDevUserId());
        //newEcSyncInfo.setDeleteFlag(0);
        return newEcSyncInfo;
    }
    private ContactInfo getContactInfo(List<ContactInfo> contactInfos) {
        if (contactInfos == null
                || contactInfos.size() == 0) {
            return null;
        }
        for(ContactInfo contactInfo:contactInfos) {
            if ("1".equals(contactInfo.getMainContact())) {
                return contactInfo;
            }
        }
        
        return contactInfos.get(0);
    }
}

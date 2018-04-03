package com.cmcc.vrp.boss.xinjiang.service.impl;

import java.rmi.RemoteException;
import java.util.Random;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.xinjiang.enums.TransCode;
import com.cmcc.vrp.boss.xinjiang.request.BasicParam;
import com.cmcc.vrp.boss.xinjiang.request.CommuneParam;
import com.cmcc.vrp.boss.xinjiang.request.GroupInfoParam;
import com.cmcc.vrp.boss.xinjiang.request.NewSendParam;
import com.cmcc.vrp.boss.xinjiang.request.QueryResourcePoolParam;
import com.cmcc.vrp.boss.xinjiang.request.SendParam;
import com.cmcc.vrp.boss.xinjiang.request.ServiceBasicParam;
import com.cmcc.vrp.boss.xinjiang.response.GroupInfoResp;
import com.cmcc.vrp.boss.xinjiang.response.NewResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.ResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.SendResp;
import com.cmcc.vrp.boss.xinjiang.service.XinjiangBossService;
import com.cmcc.vrp.boss.xinjiang.wsdl.WebserviceClient;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 新疆bossService,webservice版，http版存在后将不再使用
 * @author qihang
 *
 */
//@Service("XinjiangBossService")
public class XinjiangBossServiceImpl implements XinjiangBossService {

    private static final Logger logger = Logger
            .getLogger(XinjiangBossServiceImpl.class);
    
    @Autowired
    private GlobalConfigService globalConfigService;


    /**
     * 5.1. 集团用户信息查询 参数： group_id：集团ID
     * 
     * 返回： GroupInfoResp 集团的基本信息，详情请参考该类
     */
    @Override
    public GroupInfoResp getGetGroupInfo(String groupId) {
        // 随机生成字符串，放在boss报文中
        String seqId = getRandomString(24);
        // 设定访问的类型
        CommuneParam commune = new CommuneParam(seqId,
                TransCode.GetGroupInfo.getCode());
        // 生成初始化类
        ServiceBasicParam groupInfoParam = new GroupInfoParam(groupId);
        // 得到发送报文
        String sendPacket = getSendPacket(commune, groupInfoParam);

        // 发送，并得到返回的报文，报文为xml格式
        String responsePacket = "";
        String svcContent = "";
        try {
            // 发送请求并得到相应
            logger.info("发送url:" + getUrl());
            logger.info("发送报文:" + sendPacket);
            responsePacket = WebserviceClient.sendPacket(sendPacket,
                    getUrl());

            logger.info("收到报文:" + responsePacket);
            // 得到报文中的body元素部分
            svcContent = getContent(responsePacket);
        } catch (RemoteException e) {
            logger.error("访问地址失败");
            GroupInfoResp resp = new GroupInfoResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问地址失败");
            return resp;

        } catch (ServiceException e) {
            logger.error("访问webservice服务失败");
            GroupInfoResp resp = new GroupInfoResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问webservice服务失败");
            return resp;

        } catch (DocumentException e) {
            logger.error("xml解析失败，xml=" + svcContent);
            GroupInfoResp resp = new GroupInfoResp();
            resp.setResultCode("-1");
            resp.setResultInfo("xml解析失败");
            return resp;
        }

        // 分析报文，得到相关对象
        return GroupInfoResp.analyseRespBodyPacket(svcContent);
    }

    /**
     * 5.6. 集团用户产品流量池信息查询 参数： group_id：集团ID
     * 
     * 返回： ResourcePoolResp 集团的基本信息，详情请参考该类
     */
    @Override
    public ResourcePoolResp getResourcePoolResp(String groupId) {
        // 随机生成字符串，放在boss报文中
        String seqId = getRandomString(24);
        // 设定访问的类型
        CommuneParam commune = new CommuneParam(seqId,
                TransCode.QueryResourcePool.getCode());
        // 生成初始化类
        ServiceBasicParam groupInfoParam = new QueryResourcePoolParam(groupId);
        // 发送，并得到返回的报文，报文为xml格式
        String sendPacket = getSendPacket(commune, groupInfoParam);
        String responsePacket = "";
        String svcContent = "";
        try {
            // 发送请求并得到相应
            logger.info("发送url:" + getUrl());
            logger.info("发送报文:" + sendPacket);
            responsePacket = WebserviceClient.sendPacket(sendPacket,
                    getUrl());

            logger.info("收到报文:" + responsePacket);
            // 得到报文中的body元素部分
            svcContent = getContent(responsePacket);
        } catch (RemoteException e) {
            logger.error("访问地址失败");
            ResourcePoolResp resp = new ResourcePoolResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问地址失败");
            return resp;

        } catch (ServiceException e) {
            logger.error("访问webservice服务失败");
            ResourcePoolResp resp = new ResourcePoolResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问webservice服务失败");
            return resp;

        } catch (DocumentException e) {
            logger.error("xml解析失败，xml=" + svcContent);
            ResourcePoolResp resp = new ResourcePoolResp();
            resp.setResultCode("-1");
            resp.setResultInfo("xml解析失败");
            return resp;

        }
        // 分析报文，得到相关对象
        return ResourcePoolResp.analyseRespBodyPacket(svcContent);
    }

    /**
     * 5.2. 集团用户产品流量池信息查询
     * 参数：
     * group_id：集团ID
     * 
     * 返回：
     * ResourcePoolResp 集团用户产品流量池的信息，详情请参考该类
     */
    @Override
    public NewResourcePoolResp getResourcePoolRespNew(String groupId) {
        // 随机生成字符串，放在boss报文中
        String seqId = getRandomString(24);
        // 设定访问的类型
        CommuneParam commune = new CommuneParam(seqId,
                TransCode.QueryResourcePoolNew.getCode());
        // 生成初始化类
        ServiceBasicParam groupInfoParam = new QueryResourcePoolParam(groupId);
        // 发送，并得到返回的报文，报文为xml格式
        String sendPacket = getSendPacket(commune, groupInfoParam);
        String responsePacket = "";
        String svcContent = "";
        try {
            // 发送请求并得到相应
            logger.info("发送url:" + getUrl());
            logger.info("发送报文:" + sendPacket);
            responsePacket = WebserviceClient.sendPacket(sendPacket,
                    getUrl());

            logger.info("收到报文:" + responsePacket);
            // 得到报文中的body元素部分
            svcContent = getContent(responsePacket);
        } catch (RemoteException e) {
            logger.error("访问地址失败");
            NewResourcePoolResp resp = new NewResourcePoolResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问地址失败");
            return resp;

        } catch (ServiceException e) {
            logger.error("访问webservice服务失败");
            NewResourcePoolResp resp = new NewResourcePoolResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问webservice服务失败");
            return resp;

        } catch (DocumentException e) {
            logger.error("xml解析失败，xml=" + svcContent);
            NewResourcePoolResp resp = new NewResourcePoolResp();
            resp.setResultCode("-1");
            resp.setResultInfo("xml解析失败");
            return resp;

        }
        // 分析报文，得到相关对象
        return NewResourcePoolResp.analyseRespBodyPacket(svcContent);
    }

    /**
     * 5.4. 集团流量转赠接口
     * 参数：
     * group_id：集团ID
     * user_id：集团产品USER_ID，由5.6集团用户产品流量池信息查询中返回。
     * phone：手机号
     * flowNum： 转赠流量值（整数）
     * serialNum： 使用流水号（注意唯一），格式长度现在还没有限制
     * 
     * 返回：
     * SendResp 转增的基本信息，详情请参考该类
     */
    @Override
    public SendResp getSendResp(String groupId, String userId, String phone,
            String flowNum, String serialNum) {
        // 随机生成字符串，放在boss报文中
        String seqId = getRandomString(24);
        // 设定访问的类型
        CommuneParam commune = new CommuneParam(seqId,
                TransCode.TcsGrpIntf.getCode());
        // 生成初始化类
        ServiceBasicParam groupInfoParam = new SendParam(groupId, userId,
                serialNum, phone, flowNum);
        // 发送，并得到返回的报文，报文为xml格式
        String sendPacket = getSendPacket(commune, groupInfoParam);
        logger.info("发送报文：" + sendPacket);
        String responsePacket = "";
        String svcContent = "";
        try {
            // 发送请求并得到相应
            responsePacket = WebserviceClient.sendPacket(sendPacket,
                    getUrl());
            logger.info("收到报文：" + responsePacket);
            // 得到报文中的body元素部分
            svcContent = getContent(responsePacket);
        } catch (RemoteException e) {
            logger.error("访问地址失败");
            SendResp resp = new SendResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问地址失败");
            return resp;
        } catch (ServiceException e) {
            logger.error("访问webservice服务失败");
            SendResp resp = new SendResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问webservice服务失败");
            return resp;

        } catch (DocumentException e) {
            logger.error("xml解析失败，xml=" + svcContent);
            SendResp resp = new SendResp();
            resp.setResultCode("-1");
            resp.setResultInfo("xml解析失败");
            return resp;
        }

        // 分析报文，得到相关对象
        return SendResp.analyseRespBodyPacket(svcContent);
    }

    /**
     * 5.1. 集团流量转赠接口(新版)
     * 参数：
     * group_id：集团ID
     * user_id：集团产品USER_ID，由5.6集团用户产品流量池信息查询中返回。
     * phone：手机号
     * flowNum： 转赠流量值（整数）
     * serialNum： 使用流水号（注意唯一），格式长度现在还没有限制
     * 
     * 返回：
     * SendResp 转增的基本信息，详情请参考该类
     */
    @Override
    public SendResp getNewSendResp(String groupId, String phone,
            String flowNum, String serialNum) {
        // 随机生成字符串，放在boss报文中
        String seqId = getRandomString(24);
        // 设定访问的类型
        CommuneParam commune = new CommuneParam(seqId,
                TransCode.TcsGrpIntfNew.getCode());
        // 生成初始化类
        ServiceBasicParam groupInfoParam = new NewSendParam(groupId, phone,
                flowNum, serialNum);
        // 发送，并得到返回的报文，报文为xml格式

        String sendPacket = getSendPacket(commune, groupInfoParam);
        logger.info("发送报文:" + sendPacket);
        String responsePacket = "";
        String svcContent = "";
        try {
            // 发送请求并得到相应
            responsePacket = WebserviceClient.sendPacket(sendPacket,
                    getUrl());
            logger.info("收到报文:" + responsePacket);
            // 得到报文中的body元素部分
            svcContent = getContent(responsePacket);

        } catch (RemoteException e) {
            logger.error("访问地址失败");
            SendResp resp = new SendResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问地址失败");
            return resp;
        } catch (ServiceException e) {
            logger.error("访问webservice服务失败");
            SendResp resp = new SendResp();
            resp.setResultCode("-1");
            resp.setResultInfo("访问webservice服务失败");
            return resp;

        } catch (DocumentException e) {
            logger.error("xml解析失败，xml=" + svcContent);
            SendResp resp = new SendResp();
            resp.setResultCode("-1");
            resp.setResultInfo("xml解析失败");
            return resp;
        }

        // 分析报文，得到相关对象
        return SendResp.analyseRespBodyPacket(svcContent);
    }
    
    /**
     * getUrl
     */
    private String getUrl(){
        //return "127.0.0.1";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XJ_URL.getKey());
    }

    /**
     * 生成报文中的xml
     */
    private String getSendPacket(CommuneParam commune,
            ServiceBasicParam basicParam) {
        Document document = DocumentHelper.createDocument();// 创建根节点

        // 生成HEAD元素
        Element root = document.addElement("SvcInfo");
        Element headElement = root.addElement("Header");
        Element bodyElement = root.addElement("Body");

        generateHeadElement(headElement, commune.getSeqId(), commune);
        Element svcContentElement = bodyElement.addElement("SVC_CONTENT");
        svcContentElement.setText(basicParam.toPacket());
        return root.asXML();

    }

    /**
     * getContent
     */
    private String getContent(String xml) throws DocumentException {

        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();

        Element bodyElement = root.element("Body");
        Element svcContentElement = bodyElement.element("SVC_CONTENT");

        return svcContentElement.getText();
    }

    /**
     * generateHeadElement
     */
    private void generateHeadElement(Element headElement, String seqId,
            CommuneParam commune) {
        Element securityElement = headElement.addElement("Security");
        Element seqidElement = securityElement.addElement("SEQID");
        seqidElement.setAttributeValue("value", seqId);
        seqidElement.setAttributeValue("index", "0");

        Element despwdElement = securityElement.addElement("DESPWD");
        despwdElement.setAttributeValue("value", BasicParam.DESPWD);
        despwdElement.setAttributeValue("index", "0");

        Element systemElement = headElement.addElement("System");
        Element communicateElement = systemElement.addElement("COMMUNICATE");
        communicateElement.setAttributeValue("value", BasicParam.COMMUNICATE);
        communicateElement.setAttributeValue("index", "0");

        Element transferElement = systemElement.addElement("TRANSFER");
        transferElement.setAttributeValue("value", BasicParam.TRANSFER);
        transferElement.setAttributeValue("index", "0");

        Element contactidElement = systemElement.addElement("CONTACTID");
        contactidElement.setAttributeValue("value", BasicParam.CONTACTID);
        contactidElement.setAttributeValue("index", "0");

        Element bussidElement = systemElement.addElement("BUSSID");
        bussidElement.setAttributeValue("value", BasicParam.BUSSID);
        bussidElement.setAttributeValue("index", "0");

        Element orgchannelidElement = systemElement.addElement("ORGCHANNELID");
        orgchannelidElement.setAttributeValue("value", BasicParam.ORGCHANNELID);
        orgchannelidElement.setAttributeValue("index", "0");

        Element homechannelidElement = systemElement
                .addElement("HOMECHANNELID");
        homechannelidElement.setAttributeValue("value",
                BasicParam.HOMECHANNELID);
        homechannelidElement.setAttributeValue("index", "0");

        Element actioncodeElement = headElement.addElement("ACTIONCODE");
        actioncodeElement.setAttributeValue("value", BasicParam.ACTIONCODE);
        actioncodeElement.setAttributeValue("index", "0");

        Element testflagElement = headElement.addElement("TESTFLAG");
        testflagElement.setAttributeValue("value", BasicParam.TESTFLAG);
        testflagElement.setAttributeValue("index", "0");

        Element inparamElement = headElement.addElement("Inparam");

        Element tradeDepartIdElement = inparamElement
                .addElement("TRADE_DEPART_ID");
        tradeDepartIdElement.setAttributeValue("value",
                BasicParam.TRADEDEPARTID);
        tradeDepartIdElement.setAttributeValue("index", "0");

        Element tradeCityCodeElement = inparamElement
                .addElement("TRADE_CITY_CODE");
        tradeCityCodeElement.setAttributeValue("value",
                BasicParam.TRADECITYCODE);
        tradeCityCodeElement.setAttributeValue("index", "0");

        Element tradeEparchyCodeElement = inparamElement
                .addElement("TRADE_EPARCHY_CODE");
        tradeEparchyCodeElement.setAttributeValue("value",
                BasicParam.TRADEEPARCHYCODE);
        tradeEparchyCodeElement.setAttributeValue("index", "0");

        Element routeEparchyCodeElement = inparamElement
                .addElement("ROUTE_EPARCHY_CODE");
        routeEparchyCodeElement.setAttributeValue("value",
                BasicParam.ROUTEEPARCHYCODE);
        routeEparchyCodeElement.setAttributeValue("index", "0");

        Element provinceCodeElement = inparamElement
                .addElement("PROVINCE_CODE");
        provinceCodeElement.setAttributeValue("value", BasicParam.PROVINCECODE);
        provinceCodeElement.setAttributeValue("index", "0");

        Element tradeRouteTypeElement = inparamElement
                .addElement("TRADE_ROUTE_TYPE");
        tradeRouteTypeElement.setAttributeValue("value",
                BasicParam.TRADEROUTETYPE);
        tradeRouteTypeElement.setAttributeValue("index", "0");

        Element tradeRouteValueElement = inparamElement
                .addElement("TRADE_ROUTE_VALUE");
        tradeRouteValueElement.setAttributeValue("value",
                BasicParam.TRADEROUTEVALUE);
        tradeRouteValueElement.setAttributeValue("index", "0");

        Element tradeStaffIdElement = inparamElement
                .addElement("TRADE_STAFF_ID");
        tradeStaffIdElement.setAttributeValue("value", BasicParam.TRADESTAFFID);
        tradeStaffIdElement.setAttributeValue("index", "0");

        Element inModeCodeElement = inparamElement.addElement("IN_MODE_CODE");
        inModeCodeElement.setAttributeValue("value", BasicParam.INMODECODE);
        inModeCodeElement.setAttributeValue("index", "0");

        Element tradeDepartPasswdElement = inparamElement
                .addElement("TRADE_DEPART_PASSWD");
        tradeDepartPasswdElement.setAttributeValue("value",
                BasicParam.TRADEDEPARTPASSWD);
        tradeDepartPasswdElement.setAttributeValue("index", "0");

        Element channelTradeIdElement = inparamElement
                .addElement("CHANNEL_TRADE_ID");
        channelTradeIdElement.setAttributeValue("value",
                BasicParam.CHANNELTRADEID);
        channelTradeIdElement.setAttributeValue("index", "0");

        Element tradeTerminalIdElement = inparamElement
                .addElement("TRADE_TERMINAL_ID");
        tradeTerminalIdElement.setAttributeValue("value",
                BasicParam.TRADETERMINALID);
        tradeTerminalIdElement.setAttributeValue("index", "0");

        Element bizCodeElement = inparamElement.addElement("BIZ_CODE");
        bizCodeElement.setAttributeValue("value", BasicParam.BIZCODE);
        bizCodeElement.setAttributeValue("index", "0");

        Element transCodeElement = inparamElement.addElement("TRANS_CODE");
        transCodeElement.setAttributeValue("value", commune.getTransCode());
        transCodeElement.setAttributeValue("index", "0");

        Element infieldElement = headElement.addElement("InField");
        infieldElement.addElement("INFO_CODE");
        infieldElement.addElement("INFO_VALUE");

    }

    /**
     * 得到新疆的随机数
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}

package com.cmcc.vrp.boss.guangxi.util;

import com.google.common.base.Charsets;

import com.cmcc.vrp.boss.guangxi.model.InitDataDTO;
import com.cmcc.vrp.util.DateUtil;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.IOException;

/**
 * 第一次看到接口的XML请求包体如此写的,无力吐槽 Created by leelyn on 2016/9/22.
 */
public class GxDataGenerater {

    private static final Logger LOGGER = LoggerFactory.getLogger(GxDataGenerater.class);

    /**
     * @param data
     * @return
     */
    public static HttpEntity buildData(InitDataDTO data) {
        String msgHeaer = createReqHeader(data);
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<AdditionInfo>");
        sb.append("<ProductID>");
        sb.append(data.getPrdouctId());
        sb.append("</ProductID>\n");
        sb.append("<UserData>");
        sb.append("<MobNum>");
        sb.append(data.getMobile());
        sb.append("</MobNum>");
        sb.append("<UserPackage>");
        sb.append(data.getpCode());
        sb.append("</UserPackage>");
        sb.append("</UserData>\n");
        sb.append("</AdditionInfo>");
        String svcCont = sb.toString();

        sb.setLength(0);
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<InterBOSS>\n");
        sb.append(msgHeaer);
        sb.append("</InterBOSS>");
        String xmlhead = sb.toString();

        sb.setLength(0);
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<InterBOSS>\n");
        sb.append("<SvcCont><![CDATA[");
        sb.append(svcCont);
        sb.append("]]></SvcCont>\n</InterBOSS>");
        String xmlbody = sb.toString();
        sb.insert(0, '\n');
        sb.insert(0, xmlhead);
        StringBody body = new StringBody(xmlbody, ContentType.create("application/xml", Consts.UTF_8));
        StringBody head = new StringBody(xmlhead, ContentType.create("application/xml", Consts.UTF_8));
        LOGGER.info("广西充值请求头:{}", xmlhead);
        LOGGER.info("广西充值请求体：{}", xmlbody);
        return MultipartEntityBuilder.create().addPart("xmlhead", head).addPart("xmlbody", body).build();
    }

    public static void main(String[] args) {
        try {
            System.out.println(StreamUtils.copyToString(buildData(new InitDataDTO()).getContent(), Charsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createReqHeader(InitDataDTO data) {
        StringBuilder msg = createMsgHeader(data);
        msg.append("<TransInfo>");
        msg.append("<SessionID>");
        msg.append(data.getBossReqNum());
        msg.append("</SessionID>");
        msg.append("<TransIDO>");
        msg.append(data.getBossReqNum());
        msg.append("</TransIDO>");
        msg.append("<TransIDOTime>");
        msg.append(DateUtil.getGxBossTime());
        msg.append("</TransIDOTime>");
        msg.append("</TransInfo>\n");
        return msg.toString();
    }


    private static StringBuilder createMsgHeader(InitDataDTO data) {
        StringBuilder msg = new StringBuilder();
        msg.append("<Version>0100</Version>\n");
        msg.append("<TestFlag>");
        msg.append(data.getFlag());
        msg.append("</TestFlag>\n");
        msg.append("<BIPType>");
        msg.append("<BIPCode>");
        msg.append(data.getBipCode());
        msg.append("</BIPCode>");
        msg.append("<ActivityCode>");
        msg.append(data.getActivityCode());
        msg.append("</ActivityCode>");
        msg.append("<ActionCode>");
        msg.append(data.getActionCode());
        msg.append("</ActionCode>");
        msg.append("</BIPType>\n");
        msg.append("<RoutingInfo>");
        msg.append("<OrigDomain>DOMS</OrigDomain><RouteType>00</RouteType><Routing><HomeDomain>BBSS</HomeDomain><RouteValue>998</RouteValue></Routing>");
        msg.append("</RoutingInfo>\n");
        return msg;
    }
}

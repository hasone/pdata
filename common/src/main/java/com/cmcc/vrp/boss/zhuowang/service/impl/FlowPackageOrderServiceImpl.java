package com.cmcc.vrp.boss.zhuowang.service.impl;

import com.cmcc.vrp.boss.zhuowang.bean.UserData;
import com.cmcc.vrp.boss.zhuowang.service.AbstractFlowService;
import com.cmcc.vrp.boss.zhuowang.service.FlowPackageOrderService;
import com.cmcc.vrp.boss.zhuowang.utils.ToolsUtils;
import com.cmcc.vrp.province.service.GlobalConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 流量叠加包订购
 *
 * @author qinpo
 */
@Service
public class FlowPackageOrderServiceImpl extends AbstractFlowService<UserData>
        implements FlowPackageOrderService<UserData> {
    @Autowired
    private GlobalConfigService globalConfigService;

    public static void main(String[] args) {
        FlowPackageOrderService s = new FlowPackageOrderServiceImpl();

        List<UserData> userDatas = new ArrayList<UserData>();
        UserData u = new UserData();
        u.setMobNum("18867102100");
        u.setUserPackage("10");
        userDatas.add(u);

        s.sendRequest(userDatas, "sfasfda");
    }

    @Override
    protected String getXmlHead(String serialNum) {
        StringBuffer xmlhead = new StringBuffer();

        xmlhead.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xmlhead.append("<InterBOSS>");
        xmlhead.append("<Version>0100</Version>");
        xmlhead.append("<TestFlag>").append(getTestFlag()).append("</TestFlag>");
        xmlhead.append("<BIPType>");
        xmlhead.append("<BIPCode>BIP4B876</BIPCode>");
        xmlhead.append("<ActivityCode>T4011137</ActivityCode>");
        xmlhead.append("<ActionCode>0</ActionCode>");
        xmlhead.append("</BIPType>");
        xmlhead.append("<RoutingInfo>");
        xmlhead.append("<OrigDomain>STKP</OrigDomain>");
        xmlhead.append("<RouteType>00</RouteType>");
        xmlhead.append("<Routing>");
        xmlhead.append("<HomeDomain>BBSS</HomeDomain>");
        xmlhead.append("<RouteValue>998</RouteValue>");
        xmlhead.append("</Routing>");
        xmlhead.append("</RoutingInfo>");
        xmlhead.append("<TransInfo>");
        xmlhead.append("<SessionID>").append(serialNum).append("</SessionID>");
        xmlhead.append("<TransIDO>").append(serialNum).append("</TransIDO>");
        xmlhead.append("<TransIDOTime>")
                .append(ToolsUtils.getNoLineYYYYMMDDHHMMSS(new Date()))
                .append("</TransIDOTime>");
        xmlhead.append("</TransInfo>");
        xmlhead.append("</InterBOSS>");

        return xmlhead.toString();
    }

    @Override
    protected String getXmlBody(List<UserData> userDatas) {
        StringBuffer xmlbodySb = new StringBuffer();
        xmlbodySb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xmlbodySb.append("<InterBOSS>");
        xmlbodySb.append("<SvcCont>");
        xmlbodySb.append("<![CDATA[");
        xmlbodySb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xmlbodySb.append("<AdditionInfo>");

        xmlbodySb.append("<ProductID>").append(getProductId()).append("</ProductID>");
        for (UserData userData : userDatas) {
            xmlbodySb.append("<UserData>");
            xmlbodySb.append("<MobNum>").append(userData.getMobNum())
                    .append("</MobNum>");
            xmlbodySb.append("<UserPackage>").append(userData.getUserPackage())
                    .append("</UserPackage>");
            xmlbodySb.append("</UserData>");
        }
        xmlbodySb.append("</AdditionInfo>");
        xmlbodySb.append("]]>");
        xmlbodySb.append("</SvcCont>");
        xmlbodySb.append("</InterBOSS>");

        return xmlbodySb.toString();
    }

    @Override
    protected boolean verify(List<UserData> list) {
        // TODO:对传入参数进行校验，检验手机号码等信息
        return true;
    }


}

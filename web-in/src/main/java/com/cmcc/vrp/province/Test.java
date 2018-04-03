package com.cmcc.vrp.province;

import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author lgk8023
 * @date 2017年1月11日 下午2:34:40
 */
public class Test {

    public static void main(String[] args) {

        Gson gson = new Gson();
        //String body = ecSyncInfo();
        String url = "https://pdataqa.4ggogo.com/gdsso/gd/boss/recv.html?svc_code=EntSrvReg";
        //String url = "http://localhost:8080/web-in/gd/ecSyncInfo.html";
        //String body = entSrvState();
//        String url = "http://localhost:8080/web-in/gd/entSrvState.html";
        
        String body = entSrvReg();
//        String url = "http://localhost:8080/web-in/gd/entSrvReg.html";
        String response = HttpUtils.post(url, body, "text/plain");
//        Response result = gson.fromJson(response, Response.class);
//        System.out.println(result.getMsg());
        System.out.println(response);
    }

    /**
     * @return
     */
    public static String ecSyncInfo() {

        return "<ECSyncInfoReq>" + "<ECCode>123456</ECCode>" + "<OprCode>01</OprCode>" + "<ECName>鹤山北丰家用纺织品有限公司</ECName>" + "<Region>200</Region>"
                + "<LegalPerson>范思浩</LegalPerson>" + "<EntPermit>企独粤江总字第008339</EntPermit>" + "<ContactInfo>" + "<UserName>陈国能</UserName>"
                + "<Mobile>18819270865</Mobile>" + "<EMail>adctestmail@ewaytec.cn</EMail>" + "<MainContact>1</MainContact>" + "</ContactInfo>" + "<ContactInfo>"
                + "<UserName>陈国能</UserName>" + "<Mobile>18819270865</Mobile>" + "<EMail>adctestmail@ewaytec.cn</EMail>" + "<MainContact>1</MainContact>"
                + "</ContactInfo>" + "<ContactInfo>" + "<UserName>黄雪芬</UserName>" + "<Mobile>18819270865</Mobile>" + "<EMail>adctestmail@ewaytec.cn</EMail>"
                + "<MainContact>1</MainContact>" + "</ContactInfo>" + "<ContactInfo>" + "<UserName>张洁华</UserName>" + "<Mobile>18819270866</Mobile>"
                + "<EMail>adctestmail@ewaytec.cn</EMail>" + "<MainContact>1</MainContact>" + "</ContactInfo>" + "<ECLevel>grouptypeA2</ECLevel>"
                + "<UnitKind>corporation12</UnitKind>" + "<District>200</District>" + "<InnetDate>2006-03-14</InnetDate>" + "<VipType>Group</VipType>"
                + "<VipTypeStateDate>2013-04-15</VipTypeStateDate>" + "<CreditLevel>crdtMid</CreditLevel>" + "<DevChannel>DevOrgCustMgr</DevChannel>"
                + "<DevUserId>JMFSC00024</DevUserId>" + "</ECSyncInfoReq>";
        // ECSyncInfoReq ecSyncInfoReq = new ECSyncInfoReq();
        //
        // ecSyncInfoReq.setEcCode("12346");
        // ecSyncInfoReq.setEcName("测试企业");
        // ecSyncInfoReq.setOprCode("01");
        // ecSyncInfoReq.setRegion("广东");
        // ContactInfo contactInfo = new ContactInfo();
        // contactInfo.setUserName("李四");
        // contactInfo.setMobile("15500000000");
        // contactInfo.setEmail("112@qq.com");
        // contactInfo.setMainContact("1");
        // ecSyncInfoReq.setContactInfo(contactInfo);
        // ecSyncInfoReq.setEcLevel("2");
        // ecSyncInfoReq.setUnitKind("3");
        // ecSyncInfoReq.setDistrict("广东");
        // ecSyncInfoReq.setInnetDate("2016-07-08");
        // ecSyncInfoReq.setVipType("4");
        // ecSyncInfoReq.setVipTypeStateDate("2016-07-09");
        // ecSyncInfoReq.setCreditLevel("5");
        // ecSyncInfoReq.setDevChannel("6");
        // ecSyncInfoReq.setDevUserId("7");
        // XStream xStream = new XStream();
        // xStream.alias("ECSyncInfoReq", ECSyncInfoReq.class);
        // xStream.alias("ECSyncInfoResp", ECSyncInfoResp.class);
        // xStream.autodetectAnnotations(true);
        //
        // return xStream.toXML(ecSyncInfoReq);
    }

    /**
     * @return
     */
    public static String entSrvState() {
        return "<EntSrvStateReq>" + "<SICode>200000426</SICode>" + "<OptType>4</OptType>" + "<EntCode>20008</EntCode>"
                + "<prdOrdCode>11000007</prdOrdCode>" + "<ECAccessPort>11</ECAccessPort>" + "<OptTime>2017-08-07 10:10:10</OptTime>"
                + "<ExecDate>2017-08-07 10:10:10</ExecDate>" + "<ModiReason>随便</ModiReason>" + "<Memo>3333</Memo>" + "</EntSrvStateReq>";
    }

    /**
     * @return
     */
    public static String entSrvReg() {

        return "<EntSrvRegReq>" + "<OptCode>0</OptCode>" + "<EntCode>1234567890</EntCode>" + "<EntName>覃测试企业</EntName>" + "<AreaCode>200</AreaCode>"
                + "<SICode>200000426</SICode>" + "<SIName>广东移动</SIName>" + "<PrdCode>8588</PrdCode>" + "<PrdOrdCode>1234567890</PrdOrdCode>" + "<PrdPkgInfo />"
                + "<AccessNo>3557</AccessNo>" + "<ECAccessPort>555555</ECAccessPort>" + "<OrdRela>1</OrdRela>" + "<Service>"
                + "<ServiceId>MService.8588</ServiceId>" + "<ServiceCode>200000426158</ServiceCode>" + "<USERINFOMAP>" + "<ItemName>discount8588M</ItemName>"
                + "<ItemValue>95</ItemValue>" + "</USERINFOMAP>" + "</Service>" + "<StartEfft>2016-05-16 09:37:00</StartEfft>"
                + "<EndEfft>2020-07-25 10:00:00</EndEfft>" + "<PrdAdminUser>adcadministrator5</PrdAdminUser>" + "<PrdAdminName>梅小姐4</PrdAdminName>"
                + "<PrdAdminMobile>18900000022</PrdAdminMobile>" + "<PrdAdminMail>adctestmail@ewaytec.cn</PrdAdminMail>" + "<ISTextSign>1</ISTextSign>"
                + "<DefaultSignLang>1</DefaultSignLang>" + "<TextSignEn>oop</TextSignEn>" + "<TextSignZh>哈</TextSignZh>"
                + "<PrdChannel>IniOrgCustMgr</PrdChannel>" + "<PrdDevUserId>PrdDevUserId1</PrdDevUserId>" + "<SubPrdInfo>"
                + "<PrdCode>prod.10086000005101</PrdCode>" + "<OptCode>0</OptCode>" + "<StartEfft>2016-06-16 09:37:00</StartEfft>"
                + "<EndEfft>2018-06-02 14:00:00</EndEfft>" + "<pkgCode />" + "<Service>" + "<ServiceId>Service8588.Mem</ServiceId>"
                + "<ServiceCode></ServiceCode>" + "</Service>" + "</SubPrdInfo>" + "</EntSrvRegReq>";
    }
}

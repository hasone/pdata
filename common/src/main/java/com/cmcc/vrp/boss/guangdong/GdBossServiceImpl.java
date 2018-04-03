package com.cmcc.vrp.boss.guangdong;

import java.net.MalformedURLException;
import java.net.URL;

import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.NativeFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.guangdong.model.GdBody;
import com.cmcc.vrp.boss.guangdong.model.GdFeatrue;
import com.cmcc.vrp.boss.guangdong.model.GdMember;
import com.cmcc.vrp.boss.guangdong.model.GdRespBody;
import com.cmcc.vrp.boss.guangdong.model.GdRespMember;
import com.cmcc.vrp.boss.guangdong.model.GdReturnCode;
import com.cmcc.vrp.boss.guangdong.model.GdService1;
import com.cmcc.vrp.boss.guangdong.model.GdService2;
import com.cmcc.vrp.boss.guangdong.model.MemberShipRequest;
import com.cmcc.vrp.boss.guangdong.model.MemberShipResponse;
import com.cmcc.vrp.boss.guangdong.model.PrdList1;
import com.cmcc.vrp.boss.guangdong.model.PrdList2;
import com.cmcc.vrp.boss.guangdong.model.UserInfoMap;
import com.cmcc.vrp.boss.guangdong.webservice.NGADCServicesForECStub;
import com.cmcc.vrp.boss.guangdong.webservice.NGEC;
import com.cmcc.vrp.boss.guangdong.webservice.Response;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * Created by leelyn on 2016/7/8.
 */
@org.springframework.stereotype.Service
public class GdBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GdBossServiceImpl.class);

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private Gson gson;

    @Autowired
    private SerialNumService serialNumService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("广东渠道开始充值");
        SupplierProduct sPrdouct;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null) {
            return new GdBossOperationResultImpl(GdReturnCode.PARA_ILLEGALITY);
        }
        String bossReqNum = null;
        try {
            Service service = new org.apache.axis.client.Service();
            URL endpointURL = new URL(getUrl());
            NGADCServicesForECStub locator = new NGADCServicesForECStub(endpointURL, service);

            // If authorization is required
            //((NGADCServicesForECStub)service).setUsername("user3");
            //((NGADCServicesForECStub)service).setPassword("pass3");
            // invoke business method
            bossReqNum = SerialNumGenerator.buildNormalBossReqNum("guangdong", 25);
            NGEC newRequest = buildNGEC(mobile, sPrdouct, bossReqNum, true);
            if (newRequest != null) {
                LOGGER.info("广东渠道请求包体:{}", new Gson().toJson(newRequest));
            }
            NGEC ngec = locator.adcServices(newRequest);
            Response response = null;
            if (ngec != null) {
                LOGGER.info("广东渠道响应包体:{}", new Gson().toJson(ngec));
            }
            String svcCont;
            if (ngec == null
                    || (response = ngec.getResponse()) == null
                    || StringUtils.isBlank(svcCont = ngec.getSvcCont())) {
                //充值失败更新流水号
                if (!updateRecord(serialNum, SerialNumGenerator.buildNullBossRespNum("guangdong"), bossReqNum)) {
                    LOGGER.error("更新流水号失败");
                }
                return new GdBossOperationResultImpl(GdReturnCode.RESP_ILLEGALITY);
            }
            XStream xStream = new XStream();
            xStream.autodetectAnnotations(true);
            xStream.alias("MemberShipResponse", MemberShipResponse.class);
            MemberShipResponse shipResponse = (MemberShipResponse) xStream.fromXML(svcCont);
            String respCode = response.getRspCode();
            GdRespBody respBody;
            GdRespMember respMember = null;
            if (respCode.equals(GdReturnCode.SUCCESS.getCode())
                    && shipResponse != null
                    && (respBody = shipResponse.getBody()) != null
                    && (respMember = respBody.getGdRespMember()) != null
                    && StringUtils.isNotBlank(respMember.getCrmApplyCode())) {
                //充值成功更新流水号
                if (!updateRecord(serialNum, SerialNumGenerator.buildNullBossRespNum("guangdong"), bossReqNum)) {
                    LOGGER.error("更新流水号失败");
                }
                return new GdBossOperationResultImpl(GdReturnCode.SUCCESS);
            } else if (respCode.equals(GdReturnCode.ERR_940003.getCode())  //根据广东文档的要求,当新增接口失败时候，需要调用叠加接口
                    || respCode.equals(GdReturnCode.ERR_940037.getCode())
                    || respCode.equals(GdReturnCode.ERR_983304.getCode())) {
                NGEC againRequst = buildNGEC(mobile, sPrdouct, bossReqNum, false);
                NGEC againNgec = locator.adcServices(againRequst);
                Response againResponse = null;
                String againSvcCont;
                if (againNgec == null
                        || (againResponse = againNgec.getResponse()) == null
                        || StringUtils.isBlank(againSvcCont = againNgec.getSvcCont())) {
                    //充值失败更新流水号
                    if (!updateRecord(serialNum, SerialNumGenerator.buildNullBossRespNum("guangdong"), bossReqNum)) {
                        LOGGER.error("更新流水号失败");
                    }
                    return new GdBossOperationResultImpl(GdReturnCode.RESP_ILLEGALITY);
                }
                String againRespCode = againResponse.getRspCode();
                MemberShipResponse againShipResponse = (MemberShipResponse) xStream.fromXML(againSvcCont);
                GdRespBody againRespBody;
                GdRespMember againRespMember = null;
                if (againRespCode.equals(GdReturnCode.SUCCESS.getCode())
                        && againShipResponse != null
                        && (againRespBody = shipResponse.getBody()) != null
                        && (againRespMember = againRespBody.getGdRespMember()) != null
                        && StringUtils.isNotBlank(againRespMember.getCrmApplyCode())) {
                    //充值成功更新流水号
                    if (!updateRecord(serialNum, SerialNumGenerator.buildNullBossRespNum("guangdong"), bossReqNum)) {
                        LOGGER.error("更新流水号失败");
                    }
                    return new GdBossOperationResultImpl(GdReturnCode.SUCCESS);
                }
                if (againRespMember != null) {
                    LOGGER.error("Guangdong again charge faild.ResultCode:{},ResultMsg:{}", againRespMember.getResultCode(), againRespMember.getResultMsg());
                }
            }
            if (respMember != null) {
                LOGGER.error("Guangdong new charge faild.ResultCode:{},ResultMsg:{}", respMember.getResultCode(), respMember.getResultMsg());
            }
        } catch (java.rmi.RemoteException e) {
            LOGGER.error("广东渠道充值抛出异常:{}", e);
        } catch (MalformedURLException e) {
            LOGGER.error("广东渠道充值抛出异常:{}", e);
            //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            LOGGER.error("广东渠道充值抛出异常:{}", e);
        }
        //充值失败更新流水号
        if (!updateRecord(serialNum, SerialNumGenerator.buildNullBossRespNum("guangdong"), bossReqNum)) {
            LOGGER.error("更新流水号失败");
        }
        return new GdBossOperationResultImpl(GdReturnCode.FAILD);
    }

    private boolean updateRecord(String systemNum, String bossRespNum, String bossReqNum) {
        if (StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setBossRespSerialNum(bossRespNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }

    @Override
    public String getFingerPrint() {
        return "guangdong123456789";
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }


    private String getEccode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_ECCODE.getKey());
    }

    private String getEcusername() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_ECUSERNAME.getKey());
    }

    private String getEcpassword() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_ECPASSWORD.getKey());
    }

    private String getPrdOrdNum() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_PRDORDNUM.getKey());
    }

    private String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_CHARGE_URL.getKey());
    }


    /**
     * 构建请求包体
     *
     * @param mobile
     * @param sPrdouct
     * @param bossReqNum
     * @param newAdd     true:新添加  false:叠加
     * @return
     */
    private NGEC buildNGEC(String mobile, SupplierProduct sPrdouct, String bossReqNum, boolean newAdd) {
        NGEC request = new NGEC();
        Response response = new Response();
        request.setAreacode("MM");
        request.setBIPCode("EC0001");
        request.setBIPVer("V1.0");
        request.setECCode(getEccode());
        request.setECUserName(getEcusername());
        request.setECUserPwd(getEcpassword());
        request.setOrigDomain("NGEC");
        request.setProcessTime(String.valueOf(System.currentTimeMillis()));
        request.setResponse(response);
        request.setSvcCont(buildSvcCont(mobile, sPrdouct, newAdd));
        request.setTransIDO(bossReqNum);
        return request;
    }

    private GdBody buildBody(String mobile, SupplierProduct sPrdouct, boolean newAdd) {
        GdBody body = new GdBody();
        body.seteCCode(getEccode());
        body.setPrdOrdNum(getPrdOrdNum());
        body.setMember(buildMemer(mobile, sPrdouct, newAdd));
        return body;
    }

    private GdMember buildMemer(String mobile, SupplierProduct sPrdouct, boolean newAdd) {
        GdMember member = new GdMember();
        member.setEffType("2");
        member.setMobile(mobile);
        if (newAdd) {
            member.setOptType("0");
        } else {
            member.setOptType("4");
        }
        member.setPayFlag("0");
        member.setPrdList1(buildList1(newAdd));
        member.setPrdList2(buildList2(sPrdouct));
        member.setUsecyCle("1");
        member.setUserName("13570293580");
        return member;
    }

    /**
     * list1
     *
     * @param newAdd
     * @return
     */
    private PrdList1 buildList1(boolean newAdd) {
        PrdList1 list1 = new PrdList1();
        if (newAdd) {
            list1.setOptType("0");
        } else {
            list1.setOptType("2");
        }
        list1.setPrdCode("AppendAttr.8585");
        list1.setService(buildService1(newAdd));
        return list1;
    }

    /**
     * list2
     *
     * @param sPrdouct
     * @return
     */
    private PrdList2 buildList2(SupplierProduct sPrdouct) {
        PrdList2 list2 = new PrdList2();
        String featrue = sPrdouct.getFeature();
        GdFeatrue gdFeatrue = gson.fromJson(featrue, GdFeatrue.class);
        list2.setOptType("0");
        list2.setPrdCode(sPrdouct.getCode());
        list2.setService(buildService2(gdFeatrue.getServiceCode()));
        return list2;
    }

    private GdService2 buildService2(String serviceCode) {
        GdService2 service2 = new GdService2();
        service2.setServiceCode(serviceCode);
        return service2;
    }

    private GdService1 buildService1(boolean newAdd) {
        GdService1 service1 = new GdService1();
        service1.setServiceCode("Service8585.Mem");
        service1.setUserInfoMap(buildUserInfoMap(newAdd));
        return service1;
    }

    private UserInfoMap buildUserInfoMap(boolean newAdd) {
        UserInfoMap infoMap = new UserInfoMap();
        if (newAdd) {
            infoMap.setOptType("0");
        } else {
            infoMap.setOptType("2");
        }
        infoMap.setItemName("IFPersonPay");
        infoMap.setItemValue("0");
        return infoMap;
    }


    private String buildSvcCont(String mobile, SupplierProduct sPrdouct, boolean newAdd) {
        XStream xStream = new XStream(new PureJavaReflectionProvider(new FieldDictionary(new NativeFieldKeySorter())));
        xStream.autodetectAnnotations(true);
        MemberShipRequest shipRequest = buildRequst(mobile, sPrdouct, newAdd);
        xStream.alias("MemberShipRequest", MemberShipRequest.class);
        return xStream.toXML(shipRequest).replace("\n", "").replace(" ", "");
    }

    private MemberShipRequest buildRequst(String mobile, SupplierProduct sPrdouct, boolean newAdd) {
        MemberShipRequest request = new MemberShipRequest();
        request.setBody(buildBody(mobile, sPrdouct, newAdd));
        return request;
    }

    public static void main(String[] args) {
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<MemberShipResponse>\n" +
                "  <BODY>\n" +
                "    <ECCode>2000188888</ECCode>\n" +
                "    <PrdOrdNum>50115100100</PrdOrdNum>\n" +
                "    <Member>\n" +
                "      <Mobile>18867101917</Mobile>\n" +
                "      <ResultCode>983300</ResultCode>\n" +
                "      <ResultMsg>成员订购的产品,不是对应集团用户的成员可选产品。</ResultMsg>\n" +
                "    </Member>\n" +
                "  </BODY>\n" +
                "</MemberShipResponse>";
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("MemberShipResponse", MemberShipResponse.class);
        MemberShipResponse response = (MemberShipResponse) xStream.fromXML(data);
        System.out.println(response);
    }
}

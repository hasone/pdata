package com.cmcc.vrp.wx.flowcoin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Random;

import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.vrp.wx.WxExchangeRecordService;
import com.cmcc.vrp.wx.flowcoin.model.GdBody;
import com.cmcc.vrp.wx.flowcoin.model.GdFeatrue;
import com.cmcc.vrp.wx.flowcoin.model.GdMember;
import com.cmcc.vrp.wx.flowcoin.model.GdRespBody;
import com.cmcc.vrp.wx.flowcoin.model.GdRespMember;
import com.cmcc.vrp.wx.flowcoin.model.GdReturnCode;
import com.cmcc.vrp.wx.flowcoin.model.GdService1;
import com.cmcc.vrp.wx.flowcoin.model.GdService2;
import com.cmcc.vrp.wx.flowcoin.model.MemberShipRequest;
import com.cmcc.vrp.wx.flowcoin.model.MemberShipResponse;
import com.cmcc.vrp.wx.flowcoin.model.PrdList1;
import com.cmcc.vrp.wx.flowcoin.model.PrdList2;
import com.cmcc.vrp.wx.flowcoin.model.UserInfoMap;
import com.cmcc.vrp.wx.flowcoin.webservice.NGADCServicesForECStub;
import com.cmcc.vrp.wx.flowcoin.webservice.NGEC;
import com.cmcc.vrp.wx.flowcoin.webservice.Response;
import com.cmcc.vrp.wx.model.WxExchangeRecord;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.NativeFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

/**
 * Created by leelyn on 2016/7/8.
 */
@org.springframework.stereotype.Service
public class GdFlowCoinCharge{

    private static final Logger LOGGER = LoggerFactory.getLogger(GdFlowCoinCharge.class);

    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Autowired
    private IndividualProductService individualProductService;
    
    @Autowired
    WxExchangeRecordService wxExchangeRecordService;
    
    public static void main(String[] args) {
//        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//                "<MemberShipResponse>\n" +
//                "  <BODY>\n" +
//                "    <ECCode>2000188888</ECCode>\n" +
//                "    <PrdOrdNum>50115100100</PrdOrdNum>\n" +
//                "    <Member>\n" +
//                "      <Mobile>18867101917</Mobile>\n" +
//                "      <ResultCode>983300</ResultCode>\n" +
//                "      <ResultMsg>成员订购的产品,不是对应集团用户的成员可选产品。</ResultMsg>\n" +
//                "    </Member>\n" +
//                "  </BODY>\n" +
//                "</MemberShipResponse>";
//        XStream xStream = new XStream();
//        xStream.autodetectAnnotations(true);
//        xStream.alias("MemberShipResponse", MemberShipResponse.class);
//        MemberShipResponse response = (MemberShipResponse) xStream.fromXML(data);
//        System.out.println(response);
        GdFlowCoinCharge gdFlowCoinCharge = new GdFlowCoinCharge();
        System.out.println(gdFlowCoinCharge.chargeFlow("13533105109", null, null).getResultDesc());
    }

    /**
     * @param mobile
     * @param individualProductCode
     * @param wxExchangeRecord
     * @return
     */
    public BossOperationResult chargeFlow(String mobile, String individualProductCode, WxExchangeRecord wxExchangeRecord) {
        LOGGER.info("广东流量币兑换请求开始");
        IndividualProduct individualProduct;
        if (individualProductCode == null
                || (individualProduct = individualProductService.selectByProductCode(individualProductCode)) == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || wxExchangeRecord==null) {
            LOGGER.error("无效的请求参数mobile{}, individualProductCode{}, wxExchangeRecord{}", mobile, individualProductCode, 
                    new Gson().toJson(WxExchangeRecord.class));
            return new GdBossOperationResultImpl(GdReturnCode.PARA_ILLEGALITY); 
        }
//        IndividualProduct individualProduct = new IndividualProduct();
//        individualProduct.setProductCode("prod.10086000001992");
//        individualProduct.setFeature("{\"serviceCode\":\"8585.memserv3\"}");
//        individualProduct.setProductSize(10l);
        String bossReqNum = null;
        try {
            Service service = new org.apache.axis.client.Service();
            URL endpointURL = new URL(getUrl());
            NGADCServicesForECStub locator = new NGADCServicesForECStub(endpointURL, service);

//            bossReqNum = SerialNumGenerator.buildNormalBossReqNum("GD", 25);
            bossReqNum = "hy" + String.valueOf(System.currentTimeMillis()) + SerialNumGenerator.genRandomNum(4);
            NGEC newRequest = buildNGEC(mobile, individualProduct, bossReqNum, true);
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
                if (!updateRecord(wxExchangeRecord, SerialNumGenerator.buildNullBossRespNum("guangdong"), bossReqNum, 
                        ChargeRecordStatus.FAILED.getCode(), GdReturnCode.RESP_ILLEGALITY.getMsg())) {
                    LOGGER.error("更新流水号失败");
                }
                return new GdBossOperationResultImpl(GdReturnCode.RESP_ILLEGALITY);
            }
            LOGGER.info(svcCont);
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
                    && (respMember = respBody.getGdRespMember()) != null) {
                Integer status = null;
                String message = null;
                GdBossOperationResultImpl result = null;
                //充值成功
                if(StringUtils.isNotBlank(respMember.getCrmApplyCode())){
                    result = new GdBossOperationResultImpl(GdReturnCode.SUCCESS);
                    status = ChargeRecordStatus.COMPLETE.getCode();
                    message = ChargeRecordStatus.COMPLETE.getMessage();
                }else{//充值失败
                    result = new GdBossOperationResultImpl(GdReturnCode.FAILD);
                    status = ChargeRecordStatus.FAILED.getCode();
                    message = respMember.getResultMsg();
                }
                //充值成功更新流水号
                if (!updateRecord(wxExchangeRecord, respMember.getCrmApplyCode(), bossReqNum, status, message)) {
                    LOGGER.error("更新流水号失败");
                }
                return result;     
                
            } else if (respCode.equals(GdReturnCode.ERR_940003.getCode())  //根据广东文档的要求,当新增接口失败时候，需要调用叠加接口
                    || respCode.equals(GdReturnCode.ERR_940037.getCode())
                    || respCode.equals(GdReturnCode.ERR_983304.getCode())) {
                NGEC againRequst = buildNGEC(mobile, individualProduct, bossReqNum, false);
                NGEC againNgec = locator.adcServices(againRequst);
                Response againResponse = null;
                String againSvcCont;
                if (againNgec == null
                        || (againResponse = againNgec.getResponse()) == null
                        || StringUtils.isBlank(againSvcCont = againNgec.getSvcCont())) {
                    //充值失败更新流水号
                    if (!updateRecord(wxExchangeRecord, SerialNumGenerator.buildNullBossRespNum("guangdong"), bossReqNum, 
                            ChargeRecordStatus.FAILED.getCode(), GdReturnCode.RESP_ILLEGALITY.getMsg())) {
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
                        && (againRespMember = againRespBody.getGdRespMember()) != null) {
                    
                    Integer status = null;
                    String message = null;
                    GdBossOperationResultImpl result = null;
                    //充值成功
                    if(StringUtils.isNotBlank(againRespMember.getCrmApplyCode())){
                        result = new GdBossOperationResultImpl(GdReturnCode.SUCCESS);
                        status = ChargeRecordStatus.COMPLETE.getCode();
                        message = ChargeRecordStatus.COMPLETE.getMessage();
                    }else{//充值失败
                        result = new GdBossOperationResultImpl(GdReturnCode.FAILD);
                        status = ChargeRecordStatus.FAILED.getCode();
                        message = againRespMember.getResultMsg();
                    }
                    //充值成功更新流水号
                    if (!updateRecord(wxExchangeRecord, againRespMember.getCrmApplyCode(), bossReqNum, status, message)) {
                        LOGGER.error("更新流水号失败");
                    }
                    return result;
                }
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
        if (!updateRecord(wxExchangeRecord, SerialNumGenerator.buildNullBossRespNum("guangdong"), bossReqNum, 
                ChargeRecordStatus.FAILED.getCode(), ChargeRecordStatus.FAILED.getMessage())) {
            LOGGER.error("更新流水号失败");
        }
        return new GdBossOperationResultImpl(GdReturnCode.FAILD);
        
    }
    

    private boolean updateRecord(WxExchangeRecord wxExchangeRecord, String bossRespNum, String bossReqNum, Integer status, String message) {
        wxExchangeRecord.setBossReqSerialNum(bossReqNum);
        wxExchangeRecord.setBossRespSerialNum(bossRespNum);
        wxExchangeRecord.setStatus(status);
        wxExchangeRecord.setUpdateTime(new Date());
        wxExchangeRecord.setMessage(message);
        return wxExchangeRecordService.updateByPrimaryKeySelective(wxExchangeRecord);
    }

    private String getEccode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_ECCODE.getKey());
        //return "2000188888";
    }

    private String getEcusername() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_ECUSERNAME.getKey());
        //return "admin";
    }

    private String getEcpassword() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_ECPASSWORD.getKey());
        //return "TneHRkPUuqKvpxEJNUSCguMaIoR413Jf";
    }

    private String getPrdOrdNum() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_PRDORDNUM.getKey());
        //return "50115100100";
    }

    private String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_CHARGE_URL.getKey());
        //return "http://221.179.7.247:8201/NGADCInterface/NGADCServicesForEC.svc/NGADCServicesForEC";
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
    private NGEC buildNGEC(String mobile, IndividualProduct individualProduct, String bossReqNum, boolean newAdd) {
        NGEC request = new NGEC();
        //Response response = new Response();
        request.setAreacode("GD");
        request.setBIPCode("EC0001");
        request.setBIPVer("V1.0");
        request.setECCode(getEccode());
        request.setECUserName(getEcusername());
        request.setECUserPwd(getEcpassword());
        request.setOrigDomain("NGEC");
        request.setProcessTime(String.valueOf(System.currentTimeMillis()));
        request.setSvcCont(buildSvcCont(mobile, individualProduct, newAdd));
        //request.setResponse(response);
        request.setTransIDO(bossReqNum);
        return request;
    }

    private GdBody buildBody(String mobile, IndividualProduct individualProduct, boolean newAdd) {
        GdBody body = new GdBody();
        body.seteCCode(getEccode());
        body.setPrdOrdNum(getPrdOrdNum());
        body.setMember(buildMemer(mobile, individualProduct, newAdd));
        return body;
    }

    private GdMember buildMemer(String mobile, IndividualProduct individualProduct, boolean newAdd) {
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
        member.setPrdList2(buildList2(individualProduct, newAdd));
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
     * @param newAdd 
     *
     * @param sPrdouct
     * @return
     */
    private PrdList2 buildList2(IndividualProduct individualProduct, boolean newAdd) {
        PrdList2 list2 = new PrdList2();
        String featrue = individualProduct.getFeature();
        String size = String.valueOf(SizeUnits.KB.toMB(individualProduct.getProductSize()));
        Gson gson = new Gson();
        GdFeatrue gdFeatrue = gson.fromJson(featrue, GdFeatrue.class);
        list2.setOptType("0");
        list2.setPrdCode("prod.10086000002507");
        list2.setService(buildService2(gdFeatrue.getServiceCode(), newAdd, size));
        return list2;
    }

    private GdService2 buildService2(String serviceCode, boolean newAdd, String size) {
        GdService2 service2 = new GdService2();
        service2.setServiceCode("8585.MemAllot");
        service2.setUserInfoMap(buildUserInfoMap2(newAdd, size));
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
    
    private UserInfoMap buildUserInfoMap2(boolean newAdd, String size) {
        UserInfoMap infoMap = new UserInfoMap();
        if (newAdd) {
            infoMap.setOptType("0");
        } else {
            infoMap.setOptType("2");
        }
        infoMap.setItemName("GroupGPRSAllot");
        infoMap.setItemValue(size);
        return infoMap;
    }


    private String buildSvcCont(String mobile, IndividualProduct individualProduct, boolean newAdd) {
        XStream xStream = new XStream(new PureJavaReflectionProvider(new FieldDictionary(new NativeFieldKeySorter())));
        xStream.autodetectAnnotations(true);
        MemberShipRequest shipRequest = buildRequst(mobile, individualProduct, newAdd);
        xStream.alias("MemberShipRequest", MemberShipRequest.class);
        return xStream.toXML(shipRequest).replace("\n", "").replace(" ", "");
    }

    private MemberShipRequest buildRequst(String mobile, IndividualProduct individualProduct, boolean newAdd) {
        MemberShipRequest request = new MemberShipRequest();
        request.setBody(buildBody(mobile, individualProduct, newAdd));
        return request;
    }
    private Boolean getDynamicProxyFlag() {
        String dynamicFlag = globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey());
        String finalFlag = StringUtils.isBlank(dynamicFlag) ? "false" : dynamicFlag;
        return Boolean.parseBoolean(finalFlag);
    }
    private class ProxyOperationResult extends AbstractBossOperationResultImpl {
        Random random = new Random();

        @Override
        public boolean isSuccess() {
            if ("true".equals(globalConfigService.get(GlobalConfigKeyEnum.TEST_CHARGE_RESULT.getKey()))) {
                return true;
            } else {
                return random.nextBoolean();
            }
        }

        @Override
        public boolean isAsync() {
            return false;
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
        public void setResultDesc(String resultDesc) {
            if (globalConfigService.get(GlobalConfigKeyEnum.TEST_RESULT_DESC.getKey()).equals("true")) {
                this.resultDesc = "测试自定义充值失败";
            } else {
                this.resultDesc = "充值失败";
            }
            
        }
    }

}

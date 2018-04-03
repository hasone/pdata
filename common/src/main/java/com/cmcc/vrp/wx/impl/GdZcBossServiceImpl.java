package com.cmcc.vrp.wx.impl;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.PaymentStatus;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.wx.model.Feature;
import com.cmcc.vrp.wx.model.LECOrderRelationNotifyReq;
import com.cmcc.vrp.wx.model.LECOrderRelationNotifyResp;
import com.cmcc.vrp.wx.model.LECSubmitOrderReq;
import com.cmcc.vrp.wx.model.Member;
import com.cmcc.vrp.wx.model.PrdList;
import com.cmcc.vrp.wx.model.SubmitOrderReq;
import com.cmcc.vrp.wx.model.SubmitOrderResp;
import com.cmcc.vrp.wx.model.UserInfoMap;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by leelyn on 2017/1/3.
 */
@Service("gdZcBossService")
public class GdZcBossServiceImpl implements BossService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GdZcBossServiceImpl.class);
    private static XStream xStream = null;

    static {
        xStream = new XStream();
        xStream.alias("SubmitOrderReq", SubmitOrderReq.class);
        xStream.alias("LECSubmitOrderReq", LECSubmitOrderReq.class);
        xStream.alias("SubmitOrderResp", SubmitOrderResp.class);
        xStream.alias("LECOrderRelationNotifyResp", LECOrderRelationNotifyResp.class);
        xStream.autodetectAnnotations(true);
    }

    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private ActivityPaymentInfoService activityPaymentInfoService;
    @Autowired
    private ActivityWinRecordService activityWinRecordService;
    @Autowired
    private CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    @Autowired
    private SerialNumService serialNumService;

    public static void main(String[] args) {
//        GdZcBossServiceImpl gdZcBossServiceImpl = new GdZcBossServiceImpl();
//        System.out.println(gdZcBossServiceImpl.charge(null, null, "13590579551", null, null).isSuccess());
        Gson gson = new Gson();
        Feature feature = gson.fromJson("{\"serviceCode\":\"8585.memserv3\"}", Feature.class);
        System.out.println(feature.getExtendPrd());
    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {

        LOGGER.info("广东众筹订购业务开始！");
        //检查参数
        SupplierProduct supplierProduct = null;
        Enterprise enterprise = null;
        EnterprisesExtInfo enterprisesExtInfo = null;
        ActivityPaymentInfo activityPaymentInfo = null;
        ActivityWinRecord activityWinRecord = null;
        CrowdfundingActivityDetail crowdfundingActivityDetail = null;
        String amount = null;
        String poid = null;
        if (splPid == null
                || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
                || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null
                || (enterprisesExtInfo = enterprisesExtInfoService.get(entId)) == null
                || StringUtils.isBlank(mobile)
                || StringUtils.isBlank(serialNum)) {
            LOGGER.info("广东众筹订购业务失败：参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid, mobile, serialNum);
            return null;
        }

        activityWinRecord = activityWinRecordService.selectByRecordId(serialNum);
        String effType = "0";
        String payFlag = "0";
        if (activityWinRecord != null) {
            crowdfundingActivityDetail = crowdfundingActivityDetailService.selectByActivityId(activityWinRecord.getActivityId());
            if (crowdfundingActivityDetail != null) {
                String chargeType = crowdfundingActivityDetail.getChargeType().toString();
                String joinType = crowdfundingActivityDetail.getJoinType().toString();
                if ("1".equals(chargeType)) {
                    effType = "2";
                } else {
                    effType = "3";
                }

                if ("1".equals(joinType)) {
                    payFlag = "0";
                } else {
                    payFlag = "1";
                }
            }
        }

        String eCCode = enterprise.getCode();  //集团编码
        String prdCode = supplierProduct.getCode();  //产品编码
        String prdOrdNum = enterprisesExtInfo.getEcPrdCode();  //集团产品号码
        Gson gson = new Gson();
        Feature feature = gson.fromJson(supplierProduct.getFeature(), Feature.class);
        //String serviceCode = feature.getServiceCode();

        SubmitOrderResp submitOrderResp = null;
        try {
            String response = null;
            String url = getUrl("PayTx", "SubmitOrder");
            if ("0".equals(payFlag)) {
                url = getUrl("PayTx", "LECSubmitOrder");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                poid = "LEC" + eCCode + sdf.format(new Date()) + SerialNumGenerator.genRandomNum(6);
                amount = "-1";
            } else {
                List<ActivityPaymentInfo> infos = activityPaymentInfoService.selectByWinRecordIdAndStatus(serialNum, PaymentStatus.Pay_Success.getStatus());
                if (infos == null || infos.size() != 1) {
                    LOGGER.info("广东众筹订购业务失败：根据activityWinRecordId没有找到唯一的活动支付成功记录ActivityPaymentInfo，activityWinRecordId={}", serialNum);
                    return null;
                }
                activityPaymentInfo = infos.get(0);
                amount = String.valueOf(activityPaymentInfo.getReturnPayAmount());
                poid = activityPaymentInfo.getReturnPayNum();
            }
            SubmitOrderReq submitOrderReq = getSubmitOrderReq(eCCode, mobile, prdOrdNum, prdCode, poid, amount, feature, effType, payFlag);
            String req = xStream.toXML(submitOrderReq);
            LOGGER.info("广东众筹订购请求地址：" + url);
            LOGGER.info("广东众筹订购请求报文:" + req);
            response = HttpUtils.post(url, req, "application/xml");
            LOGGER.info("广东众筹订购响应：" + response);

            if (response != null) {
                submitOrderResp = (SubmitOrderResp) xStream.fromXML(response);
                if (submitOrderResp != null
                        && submitOrderResp.getOrderId() != null) {
                    if (!updateRecord(serialNum, submitOrderResp.getOrderId(), poid)) {
                        LOGGER.error("广东众筹订购更新流水号失败,serialNum:{}.bossRespNum:{}", serialNum, submitOrderResp.getOrderId());
                    }
                }
            } else {
                submitOrderResp = new SubmitOrderResp("001", "BOSS连接失败");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            submitOrderResp = new SubmitOrderResp("001", "BOSS连接异常");
        }

        return submitOrderResp;
    }

    /**
     * 大企业版消息通知
     */
    public boolean orderRelation(Long entId, String status) {
        if (getDynamicProxyFlag()) {
            return true;
        }
        EnterprisesExtInfo enterprisesExtInfo;
        if (entId == null
                || status == null
                || (enterprisesExtInfo = enterprisesExtInfoService.get(entId)) == null) {
            LOGGER.error("广东众筹大企业版订购关系请求失败");
            return false;
        }
        String entCode = enterprisesExtInfo.getEcCode();
        String entPrdCode = enterprisesExtInfo.getEcPrdCode();
        LECOrderRelationNotifyReq lecOrderRelationNotifyReq = buildOrderRelation(entCode, entPrdCode, status);
        LOGGER.info("广东众筹大企业版订购关系请求：{}", lecOrderRelationNotifyReq);
        LECOrderRelationNotifyResp lecOrderRelationNotifyResp = null;
        try {
            String req = xStream.toXML(lecOrderRelationNotifyReq);
            String url = getOrderRelationUrl("PayTx", "LECOrderRelationNotify");
            String response = HttpUtils.post(url, req, "application/xml");
            LOGGER.info("广东众筹大企业版订购关系返回：{}", response);
            if (response != null
                    && (lecOrderRelationNotifyResp = (LECOrderRelationNotifyResp) xStream.fromXML(response)) != null
                    && "0".equals(lecOrderRelationNotifyResp.getResult())) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return false;
    }

    private LECOrderRelationNotifyReq buildOrderRelation(String entCode, String entPrdCode, String status) {
        LECOrderRelationNotifyReq lecOrderRelationNotifyReq = new LECOrderRelationNotifyReq();
        lecOrderRelationNotifyReq.setEntCode(entCode);
        lecOrderRelationNotifyReq.setEntPrdCode(entPrdCode);
        lecOrderRelationNotifyReq.setStatus(status);
        return lecOrderRelationNotifyReq;
    }

    @Override
    public String getFingerPrint() {
        return "gd_crowdfunding";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
    }

    private SubmitOrderReq getSubmitOrderReq(String eCCode, String mobile, String prdOrdNum,
                                             String prdCode, String poid, String amount, Feature feature, String effType, String payFlag) {
        SubmitOrderReq submitPaymentReq = new SubmitOrderReq();
        submitPaymentReq.seteCCode(eCCode);
        submitPaymentReq.setPrdOrdNum(prdOrdNum);
        submitPaymentReq.setPoId(poid);
        Member member = getMember(mobile, prdCode, amount, feature, effType, payFlag);
        submitPaymentReq.setMember(member);
        return submitPaymentReq;

    }


    private Member getMember(String mobile, String prdCode, String amount, Feature feature, String effType, String payFlag) {
        Member member = new Member();
        member.setOptType(getOptType());
        member.setPayFlag(payFlag);    //0＝集团代 付，1＝个人支付
        member.setUsecyCle(feature.getMonths());
        member.setMobile(mobile);
        member.setUserName(mobile);
        member.setEffType(effType);      //生效方式：取值：默认(0),立即(2),下月(3)

        String serviceCode = feature.getServiceCode();
        String monthFlag = feature.getMonthFlag();
        String mainProCode = feature.getMainPrdCode();
        String mainServiceCode = feature.getMainServiceCode();
        PrdList prdList = getPrdList2(amount, mainProCode, mainServiceCode);
        member.setPrdList2(prdList);
        prdList = getPrdList1(prdCode, amount, serviceCode);
        member.setPrdList1(prdList);
        if ("1".equals(monthFlag)) {
            String extendPrd = feature.getExtendPrd();
            prdList = getPrdList3(extendPrd, amount);
            member.setPrdList3(prdList);
        }
        return member;
    }

    private PrdList getPrdList1(String prdCode, String amount, String serviceCode) {
        PrdList prdList = new PrdList();
        prdList.setPrdCode(prdCode);
        prdList.setOptType(getOptType());
        prdList.setAmount(amount);
        if (!com.cmcc.vrp.util.StringUtils.isEmpty(serviceCode)) {
            com.cmcc.vrp.wx.model.Service service = getService1(serviceCode);
            prdList.setService(service);
        }
        return prdList;
    }

    private PrdList getPrdList2(String amount, String mainProCode, String mainServiceCode) {
        PrdList prdList = new PrdList();
        //prdList.setPrdCode("AppendAttr.8585");
        prdList.setPrdCode(mainProCode);
        prdList.setOptType(getOptType());
        prdList.setAmount(amount);
        com.cmcc.vrp.wx.model.Service service = getService2(mainServiceCode);
        prdList.setService(service);
        return prdList;
    }

    private PrdList getPrdList3(String extendPrd, String amount) {
        PrdList prdList = new PrdList();
        prdList.setPrdCode(extendPrd);
        prdList.setOptType(getOptType());
        prdList.setAmount(amount);
        return prdList;
    }

    private com.cmcc.vrp.wx.model.Service getService1(String serviceCode) {
        com.cmcc.vrp.wx.model.Service service = new com.cmcc.vrp.wx.model.Service();
        service.setServiceCode(serviceCode);
        UserInfoMap userInfoMap = getUserInfoMap1();
        service.setUserInfoMap(userInfoMap);
        return service;
    }

    private com.cmcc.vrp.wx.model.Service getService2(String mainServiceCode) {
        com.cmcc.vrp.wx.model.Service service = new com.cmcc.vrp.wx.model.Service();
        //service.setServiceCode("Service8585.Mem");
        service.setServiceCode(mainServiceCode);
        UserInfoMap userInfoMap = getUserInfoMap2();
        service.setUserInfoMap(userInfoMap);
        return service;
    }

    private UserInfoMap getUserInfoMap1() {
        UserInfoMap userInfoMap = new UserInfoMap();
        userInfoMap.setOptType(getOptType());
        userInfoMap.setItemName("IFPersonPay");
        userInfoMap.setItemValue("0");
        return userInfoMap;
    }

    private UserInfoMap getUserInfoMap2() {
        UserInfoMap userInfoMap = new UserInfoMap();
        userInfoMap.setOptType(getOptType());
        userInfoMap.setItemName("IFPersonPay");
        userInfoMap.setItemValue("0");
        return userInfoMap;
    }

    private boolean updateRecord(String systemNum, String bossRespNum, String bossReqNum) {
        if (org.apache.commons.lang.StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setBossRespSerialNum(bossRespNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }

    private String getOptType() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_CHARGE_OPTTYPE.getKey());
        //return "0";
    }

    private String getUrl(String svcCat, String svcCode) {
        //return "http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=" + svcCat + "&svc_code=" + svcCode;
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_CHARGE_URL.getKey()) + "?svc_cat=" + svcCat + "&svc_code=" + svcCode;
    }

    private String getOrderRelationUrl(String svcCat, String svcCode) {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_LEC_ORDERRELATION_URL.getKey()) + "?svc_cat=" + svcCat + "&svc_code=" + svcCode;
        //return "http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=" + svcCat + "&svc_code=" + svcCode;
    }

    private Boolean getDynamicProxyFlag() {
        String dynamicFlag = globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey());
        String finalFlag = StringUtils.isBlank(dynamicFlag) ? "false" : dynamicFlag;
        return Boolean.parseBoolean(finalFlag);
    }
}

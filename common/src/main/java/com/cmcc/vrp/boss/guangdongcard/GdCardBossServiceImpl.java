package com.cmcc.vrp.boss.guangdongcard;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.GdCardBossOperationResultImpl;
import com.cmcc.vrp.boss.guangdongcard.enums.ReturnInfo;
import com.cmcc.vrp.boss.guangdongcard.model.MemberOrderResponse;
import com.cmcc.vrp.boss.guangdongcard.wsdl.ADCABServiceForOpen;
import com.cmcc.vrp.boss.guangdongcard.wsdl.AdcSiRequest;
import com.cmcc.vrp.boss.guangdongcard.wsdl.AdcSiResponse;
import com.cmcc.vrp.boss.guangdongcard.wsdl.NGADCABServiceForOpen;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.GdPrdInfo;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 广东流量卡BOSS实现
 * <p>
 * Created by sunyiwei on 2016/11/22.
 */
@Service
public class GdCardBossServiceImpl implements BossService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdCardBossServiceImpl.class);
    private static XStream xStream;

    static {
        xStream = new XStream();
        xStream.alias("MemberOrderResponse", MemberOrderResponse.class);
        xStream.autodetectAnnotations(true);
    }

    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    AdcSiRequestFactory adcSiRequestFactory;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("start to charge ...");
        if (entId == null
            || splPid == null
            || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
            || StringUtils.isBlank(serialNum)) {
            LOGGER.error("充值请求参数错误, EntId = {}, SplPid = {}, Mobile = {}, SerialNum = {}.", entId, splPid, mobile, serialNum);
            return new GdCardBossOperationResultImpl(ReturnInfo.INVALID_PARAM);
        }
        SupplierProduct supplierProduct = supplierProductService.selectByPrimaryKey(splPid);

        EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(entId);

        String feature = supplierProduct.getFeature();
        GdPrdInfo gdPrdInfo = JSON.parseObject(feature, GdPrdInfo.class);//这里GdPrdInfo就是转化SupplierProduct中的feature
        LOGGER.info("gdPrdInfo={}", JSON.toJSONString(gdPrdInfo));

        if (enterprisesExtInfo == null || gdPrdInfo == null) {
            LOGGER.error("无效的供应商产品ID或企业ID， SupplierPrdId = {}， EnterId = {}.", splPid, entId);
            return new GdCardBossOperationResultImpl(ReturnInfo.INVALID_PARAM);
        }

        LOGGER.info("调用build函数,ecCode={},ecPrdCode={},serialNum={},mobile={}, userProductCode={}, subProductCode={},useCycle ={}," + "isNeedServicesNode ={} ",
                enterprisesExtInfo.getEcCode(), enterprisesExtInfo.getEcPrdCode(), serialNum, mobile, gdPrdInfo.getUserProductCode(),
                gdPrdInfo.getSubProductCode(), gdPrdInfo.getUseCycle(), gdPrdInfo.getIsNeedServicesNode());
       
        AdcSiRequest adcSiRequest = adcSiRequestFactory.build(null, enterprisesExtInfo.getEcCode(), enterprisesExtInfo.getEcPrdCode(),
                serialNum, mobile, gdPrdInfo, false);
        
        LOGGER.info("开始往广东营销卡BOSS接口发送请求，请求内容为{}.", JSON.toJSONString(adcSiRequest));

        AdcSiResponse adcSiResponse = null;
        MemberOrderResponse mor = null;
        try {
            ADCABServiceForOpen adcabServiceForOpen = new ADCABServiceForOpen();
            NGADCABServiceForOpen ngadcabServiceForOpen = adcabServiceForOpen.getNGADCABServicesForOpen();

            LOGGER.info("start to send request ...");
            adcSiResponse = ngadcabServiceForOpen.adcSiInterface(adcSiRequest);
            LOGGER.info("广东营销卡BOSS接口返回响应， 响应内容为{}.", JSON.toJSONString(adcSiResponse));
            //new Gson().toJson(adcSiResponse)
            mor = parse(adcSiResponse);
        } catch (Exception e) {
            LOGGER.error("向广东营销卡BOSS接口发送请求时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
            mor = null;
        } finally {
            //回调流水号
            String bossRespSn = (mor == null) ? null : mor.getCrmApplyCode();
            if (!serialNumService.updateSerial(buildSn(serialNum, bossRespSn))) {
                LOGGER.error("更新流水号对应关系时出错，PltSn = {}, BossRespSn = {}.", serialNum, bossRespSn);
            }
        }

        //根据BOSS侧的响应结果构建相应的充值结果
        return new GdCardBossOperationResultImpl(mor);
    }

    @Override
    public String getFingerPrint() {
        return "guangdongcard";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    private SerialNum buildSn(String pltSn, String bossRespSn) {
        SerialNum sn = new SerialNum();

        sn.setPlatformSerialNum(pltSn);
        sn.setBossReqSerialNum(pltSn);
        sn.setBossRespSerialNum(bossRespSn);

        return sn;
    }

    //解析响应内容
    private MemberOrderResponse parse(AdcSiResponse adcSiResponse) {
        if (adcSiResponse == null) {
            return null;
        }

        try {
            LOGGER.info("svcCont========={}",adcSiResponse.getSvcCont().getValue());
            return (MemberOrderResponse) xStream.fromXML(adcSiResponse.getSvcCont().getValue());
        } catch (Exception e) {
            LOGGER.error("解析广东营销卡BOSS返回的响应内容时出错，错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

}

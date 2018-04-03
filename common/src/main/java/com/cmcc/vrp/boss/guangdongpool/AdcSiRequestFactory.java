package com.cmcc.vrp.boss.guangdongpool;

import com.cmcc.vrp.boss.guangdongpool.enums.ActionCode;
import com.cmcc.vrp.boss.guangdongpool.enums.CustType;
import com.cmcc.vrp.boss.guangdongpool.enums.DealKind;
import com.cmcc.vrp.boss.guangdongpool.enums.EffType;
import com.cmcc.vrp.boss.guangdongpool.enums.MemberOptType;
import com.cmcc.vrp.boss.guangdongpool.enums.PayFlag;
import com.cmcc.vrp.boss.guangdongpool.enums.ProductOptType;
import com.cmcc.vrp.boss.guangdongpool.model.CustInfo;
import com.cmcc.vrp.boss.guangdongpool.model.Member;
import com.cmcc.vrp.boss.guangdongpool.model.MemberOrderRequest;
import com.cmcc.vrp.boss.guangdongpool.model.Param;
import com.cmcc.vrp.boss.guangdongpool.model.Product;
import com.cmcc.vrp.boss.guangdongpool.model.Service;
import com.cmcc.vrp.boss.guangdongpool.model.User;
import com.cmcc.vrp.boss.guangdongpool.wsdl.AdcSiRequest;
import com.cmcc.vrp.boss.guangdongpool.wsdl.ObjectFactory;
import com.cmcc.vrp.province.model.GdPrdInfo;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 构建AdcSiRequest的工厂类
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
@Component("aDCSiRequestFactory")
public class AdcSiRequestFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdcSiRequestFactory.class);
    private static XStream xStream = null;
    /**
     * 这个是在充值时，构建父产品识别，理论上是不会变动的
     */
    private static final String _Parent_Crm_PRODUCTCOD = "AppendAttr.8588";
    private static final String _Parent_SERVICECODE = "Service8588.Mem";
    private static final String _Param_CODE = "GroupGPRSAllot";
    
    //isNeedServicesNode==1 非体验包（需要ServicesNode）
    private static final String _Default_ISNEEDSERVICESNODE ="1";

    static {
        xStream = new XStream();
        xStream.autodetectAnnotations(true);
    }

    @Autowired
    private GlobalConfigService globalConfigService;

    //创建对象工厂
    private ObjectFactory objectFactory = new ObjectFactory();



    /**
     * 构建AdcSiRequest请求
     * title: build
     * desc: 
     * @param custId          集团客户编码
     * @param ecCode          集团编码
     * @param ecPrdCode       集团产品号码
     * @param syAppCode       外围申请单编码，即流水号
     * @param mobile          手机号码
     * @param gdPrdInfo       feature字段对应的POJO
     * @param testFlag        测试标识
     * @return
     * wuguoping
     * 2017年6月8日
     */
    public AdcSiRequest build(String custId, String ecCode, String ecPrdCode,
            String syAppCode, String mobile, GdPrdInfo gdPrdInfo, boolean testFlag, String size) {
        LOGGER.info("start to build ...");
        String timestamp = buildTimestamp();

        //初始化头信息
        AdcSiRequest adcSiRequest = new AdcSiRequest();
        initHead(adcSiRequest, timestamp, testFlag);

        //设置内容
        LOGGER.info("start to set content ...");

        adcSiRequest.setSvcCont(objectFactory.createAdcSiRequestSvcCont(buildSvcCont(mobile, custId, ecCode, ecPrdCode,
                gdPrdInfo, syAppCode, size)));
        
        LOGGER.info("set content succeed...");
        //设置签名
        LOGGER.info("start to set signature ...");
        adcSiRequest.setSign(objectFactory.createAdcSiRequestSign(buildSign(timestamp)));
        LOGGER.info("set signature succeed...");

        return adcSiRequest;
    }
    
    private String buildTimestamp() {
        final String format = "yyyy-MM-dd HH:mm:ss SSSS";
        return new SimpleDateFormat(format).format(new Date());
    }

    private void initHead(AdcSiRequest adcSiRequest, String timestamp, boolean testFlag) {
        adcSiRequest.setBizCode(objectFactory.createAdcSiRequestBizCode(getBizCode()));
        adcSiRequest.setTransID(objectFactory.createAdcSiRequestTransID(TransIdService.buildTransId()));
        adcSiRequest.setActionCode(ActionCode.REQUEST.getValue());
        adcSiRequest.setTimeStamp(objectFactory.createAdcSiRequestTimeStamp(timestamp));
        adcSiRequest.setSIAppID(objectFactory.createAdcSiRequestSIAppID(getSiAppId()));
        adcSiRequest.setTestFlag(testFlag ? 1 : 0);
        adcSiRequest.setDealkind(DealKind.SYNC.getValue());
        adcSiRequest.setVersion(objectFactory.createAdcSiRequestVersion("0"));
        adcSiRequest.setPriority(0); //reserved
    }

    /**
     * 创建签名
     *
     * @param timestamp 时间戳
     * @return 签名内容
     */
    private String buildSign(String timestamp) {
        return SignService.sign(getSiAppId(), getAppSysPUkEY(), timestamp);
    }

    /**
     * 创建请求的svcCont
     * title: buildSvcCont
     * desc: 
     * @param mobile        手机号码
     * @param custId
     * @param ecCode
     * @param ecPrdCode
     * @param gdPrdInfo
     * @param syAppCode     外围申请单编码，即流水号
     * @return
     * wuguoping
     * 2017年6月8日
     */
    private String buildSvcCont(String mobile, String custId, String ecCode, String ecPrdCode,
            GdPrdInfo gdPrdInfo, String syAppCode, String size) {
        MemberOrderRequest mor = new MemberOrderRequest();
        mor.setCustInfo(buildCustInfo(mobile, custId, ecCode, ecPrdCode, gdPrdInfo, syAppCode, size));

        LOGGER.info(xStream.toXML(mor));
        return xStream.toXML(mor);
    }
    
    private CustInfo buildCustInfo(String mobile, String custId, String ecCode, String ecPrdCode,
            GdPrdInfo gdPrdInfo, String syAppCode, String size) {
        CustInfo custInfo = new CustInfo();
        custInfo.setCustId(custId);
        custInfo.setCustType(String.valueOf(CustType.ENTERPRISE.getValue()));
        custInfo.setEcCode(ecCode);
        custInfo.setUser(buildUser(mobile, ecPrdCode,  gdPrdInfo, syAppCode, size));

        return custInfo;
    }

    private User buildUser(String mobile, String ecPrdCode, GdPrdInfo gdPrdInfo, String syAppCode, String size) {
        User user = new User();
        user.setProductCode(gdPrdInfo.getUserProductCode());
        user.setEcPrdCode(ecPrdCode);
        user.setSyAppCode(syAppCode);
        user.setMember(buildMember(mobile, gdPrdInfo, size));

        return user;
    }

    private Member buildMember(String mobile, GdPrdInfo gdPrdInfo, final String size) {
        Member member = new Member();
        member.setOptType(String.valueOf(MemberOptType.ORDER.getValue()));
        member.setMobile(mobile);

        LinkedList<Product> prds = new LinkedList<Product>();
        
        //具体子产品
        prds.add(buildProduct(gdPrdInfo, gdPrdInfo.getSubProductCode(),new ServiceCreator() {
            @Override
            public LinkedList<Service> buildService(GdPrdInfo gdPrdInfo) {
                LinkedList<Service> services = new LinkedList<Service>();
                Service service = new Service();
                service.setServiceCode(gdPrdInfo.getServiceCode());

                List<Param> params = new ArrayList<Param>();
                Param param = new Param();
                param.setOptType(String.valueOf(MemberOptType.ORDER.getValue()));
                param.setParamCode(_Param_CODE);
                param.setParamValue(size);
                params.add(param);
                service.setParams(params);
                services.add(service);
                return services;
            }
        }));


        //父产品，用于识别子产品(这里是个坑。。。。。。。。)
        prds.add(buildProduct(gdPrdInfo, _Parent_Crm_PRODUCTCOD, new ServiceCreator() {
            @Override
            public LinkedList<Service> buildService(GdPrdInfo gdPrdInfo) {
                LinkedList<Service> services = new LinkedList<Service>();
                Service service = new Service();
                service.setServiceCode(_Parent_SERVICECODE);

                services.add(service);
                return services;
            }
        }));

        member.setProducts(prds);

        return member;
    }

    private Product buildProduct(GdPrdInfo gdPrdInfo, String productCode, ServiceCreator serviceCreator) {
        Product product = new Product();
        product.setOptType(String.valueOf(ProductOptType.ORDER.getValue()));
        product.setProductCode(productCode);
        product.setPayFlag(String.valueOf(PayFlag.ENTERPRISE.getValue()));
        product.setEffType(String.valueOf(EffType.RIGHT_NOW.getValue()));
        product.setUseCycle(gdPrdInfo.getUseCycle());//这里才是设置年包、季包等的地方

        String isNeedServicesNode = gdPrdInfo.getIsNeedServicesNode();
        if(!StringUtils.isNotBlank(isNeedServicesNode)){
            LOGGER.error("isNeedServicesNode is null");
            return null;
        }
        //isNeedServicesNode==1 非体验包（需要ServicesNode）
        if(_Default_ISNEEDSERVICESNODE.equals(isNeedServicesNode) || productCode.equals(_Parent_Crm_PRODUCTCOD)){
            product.setServices(serviceCreator.buildService(gdPrdInfo));
        }

        return product;
    }

    public String getBizCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.GD_CARD_BIZ_CODE.getKey());
    }

    public String getSiAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.GD_CARD_SI_APPID.getKey());
    }

    public String getAppSysPUkEY() {
        return globalConfigService.get(GlobalConfigKeyEnum.GD_CARD_APP_SYS_PUKEY.getKey());
    }

    public String getSubProductCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.GD_CARD_SUB_PRODUCT_CODE.getKey());
    }

    public String getServiceCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.GD_CARD_SERVICE_CODE.getKey());
    }

    private interface ServiceCreator {
        LinkedList<Service> buildService(GdPrdInfo gdPrdInfo);
    }
}

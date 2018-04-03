package com.cmcc.vrp.boss.shangdong;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.shangdong.model.OrderReqAdvPay;
import com.cmcc.vrp.boss.shangdong.model.OrderReqBusiData;
import com.cmcc.vrp.boss.shangdong.model.OrderReqPubInfo;
import com.cmcc.vrp.boss.shangdong.model.OrderRespAdvpay;
import com.cmcc.vrp.boss.shangdong.model.OrderRespPubInfo;
import com.cmcc.vrp.boss.shangdong.model.SDReturnCode;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.MD5;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import javax.annotation.PostConstruct;

/**
 * Created by leelyn on 2016/6/28.
 */
@Service
public class SdBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SdBossServiceImpl.class);
    @Value("#{settings['sd.orderUrl']}")
    String orderUrl;
    @Autowired
    private SupplierProductService productService;
    @Value("#{settings['shandong.appkey']}")
    private String appkey;
    @Value("#{settings['shandong.appsecret']}")
    private String appSercret;
    @Value("#{settings['shandong.userid']}")
    private String userId;
    private XStream xStream = new XStream();

    public SdBossServiceImpl() {
        super();
    }

    /**
     * 初始化Xstream
     */
    @PostConstruct
    public void init() {
        xStream.autodetectAnnotations(true);
    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        SupplierProduct sPrdouct;
        String busiDataStr = null;
        String verifyCode = null;
        String advPayStr = null;
        String pCode = null;
        OrderReqBusiData busiData;
        OrderReqPubInfo pubInfo;
        OrderRespPubInfo resp = null;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = sPrdouct.getCode())) {
            LOGGER.error("Shandong charge requset Para is null");
            return new SdBossOperationResultImpl(SDReturnCode.PARA_ILLEGALITY);
        }
        if ((busiData = buildOrderReqBD(mobile, pCode, userId)) != null
                && StringUtils.isNotBlank(busiDataStr = (xStream.toXML(busiData)).replace("\n", "").replace(" ", ""))
                && StringUtils.isNotBlank(verifyCode = MD5.sign(busiDataStr, appSercret, "UTF-8"))
                && (pubInfo = buildOrderReqPubInfo(appkey, verifyCode)) != null
                && StringUtils.isNotBlank(advPayStr = xStream.toXML(buildOrderReqAdv(pubInfo, busiData)))) {
            advPayStr = (advPayStr.replace("\n", "")).replace(" ", "");
            OrderRespAdvpay orderResp = HttpUtils.post(orderUrl, advPayStr, "text/plain", new ResponseHandler<OrderRespAdvpay>() {
                @Override
                public OrderRespAdvpay handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    return null;
                }
            });

            if (orderResp != null
                    && (resp = orderResp.getRespPubInfo()) != null
                    && (resp.getReturnCode()).equals(SDReturnCode.SUCCESS.getCode())) {
                return new SdBossOperationResultImpl(SDReturnCode.SUCCESS);
            }
        }
        LOGGER.error("Shandong Charge Faild code:{},msg:{}", resp == null ? "" : resp.getReturnCode(), resp == null ? "" : resp.getReturnMsg());
        return new SdBossOperationResultImpl(SDReturnCode.FAILD);
    }

    @Override
    public String getFingerPrint() {
        return "abandon_shangdong123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    private OrderReqBusiData buildOrderReqBD(String chargePhoneNum, String productCode, String userID) {
        if (StringUtils.isBlank(chargePhoneNum)
                || StringUtils.isBlank(productCode)
                || StringUtils.isBlank(userID)) {
            return null;
        }
        OrderReqBusiData orderReqBD = new OrderReqBusiData();
        orderReqBD.setChargePhoneNum(chargePhoneNum);
        orderReqBD.setProductCode(productCode);
        orderReqBD.setUserID(userID);
        orderReqBD.setChargeNum("1");
        orderReqBD.setCreateTime(String.valueOf(System.currentTimeMillis()));
        return orderReqBD;
    }

    private OrderReqPubInfo buildOrderReqPubInfo(String enterpriseBossId, String verifyCode) {
        if (StringUtils.isBlank(enterpriseBossId) || StringUtils.isBlank(verifyCode)) {
            return null;
        }
        OrderReqPubInfo orderReqPubInfo = new OrderReqPubInfo();
        orderReqPubInfo.setEnterpriseBossId(enterpriseBossId);
        orderReqPubInfo.setVerifyCode(verifyCode);
        orderReqPubInfo.setVersion("1");

        return orderReqPubInfo;
    }

    private OrderReqAdvPay buildOrderReqAdv(OrderReqPubInfo pubInfo, OrderReqBusiData busiData) {
        if (pubInfo == null || busiData == null) {
            return null;
        }
        OrderReqAdvPay advPay = new OrderReqAdvPay();
        advPay.setOrderReqPubInfo(pubInfo);
        advPay.setOrderReqBusiData(busiData);
        return advPay;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }
}

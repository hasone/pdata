package com.cmcc.vrp.boss.jiangxi;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.jiangxi.model.JXChargeBodyReq;
import com.cmcc.vrp.boss.jiangxi.model.JXChargeBodyResp;
import com.cmcc.vrp.boss.jiangxi.model.JXChargeHead;
import com.cmcc.vrp.boss.jiangxi.model.JXReturnCode;
import com.cmcc.vrp.boss.jiangxi.model.PresentPhoneVolumeReq;
import com.cmcc.vrp.boss.jiangxi.model.PresentPhoneVolumeRsp;
import com.cmcc.vrp.boss.jiangxi.util.CommonMessageTool;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by leelyn on 2016/7/7.
 */
@Service
public class JxBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JxBossServiceImpl.class);

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private SerialNumService serialNumService;


    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("江西渠道开始充值了");
        SupplierProduct sPrdouct;
        String pCode;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = sPrdouct.getCode())) {
            return new JxBossOperationResultImpl(JXReturnCode.PARA_ILLEGALITY);
        }
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("jiangxi", 25);
        String reqStr = buildReqStr(buildHead(bossReqNum), encryptBody(buildBody(mobile, pCode), getAppSecret()));
        LOGGER.info("江西渠道请求包体:{}", reqStr);
        String endpoint = "http://hongbao.jxict.cn:6060/jiekou/services/JxydHongbaoVolume";

        org.apache.axis.client.Service service = new org.apache.axis.client.Service();
        org.apache.axis.client.Call call = null;
        JXChargeBodyResp bodyResp = null;
        try {
            call = (org.apache.axis.client.Call) service.createCall();
            call.setOperationName("presentPhoneVolume");
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.addParameter("in0", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType(XMLType.XSD_STRING);
            call.setTimeout(60 * 1000);
            String rec_msg = (String) call.invoke(new Object[]{reqStr});
            LOGGER.info("江西渠道充值响应包体:{}", rec_msg);
            PresentPhoneVolumeRsp rsp;
            String body;
            if ((rsp = xml2Obj(PresentPhoneVolumeRsp.class, "PresentPhoneVolumeRsp", rec_msg)) != null
                    && StringUtils.isNotBlank(body = rsp.getChargeBody())
                    && StringUtils.isNotBlank(body = decryptBody(body, getAppSecret()))
                    && (bodyResp = xml2Obj(JXChargeBodyResp.class, "BODY", body)) != null) {
                String resultCode = bodyResp.getResultCode();
                if (resultCode.equals(JXReturnCode.SUCCESS.getCode())) {
                    LOGGER.info("江西渠道充值成功");
                    updateRecord(serialNum, bossReqNum);
                    return new JxBossOperationResultImpl(JXReturnCode.SUCCESS);
                } else if (resultCode.equals(JXReturnCode.SYSTEM_EXCP.getCode())) {
                    LOGGER.info("江西渠道充值系统异常，处理失败");
                    updateRecord(serialNum, bossReqNum);
                    return new JxBossOperationResultImpl(JXReturnCode.SYSTEM_EXCP);
                } else if (resultCode.equals(JXReturnCode.AUTH_FAILD.getCode())) {
                    LOGGER.info("江西渠道充值IP鉴权不通过");
                    updateRecord(serialNum, bossReqNum);
                    return new JxBossOperationResultImpl(JXReturnCode.AUTH_FAILD);
                } else if (resultCode.equals(JXReturnCode.DATA_ILLEGALITY.getCode())) {
                    LOGGER.info("江西渠道充值数据非法加密");
                    updateRecord(serialNum, bossReqNum);
                    return new JxBossOperationResultImpl(JXReturnCode.DATA_ILLEGALITY);
                } else if (resultCode.equals(JXReturnCode.PARA_ILLEGALITY.getCode())) {
                    LOGGER.info("江西渠道充值ECCODE参数非法");
                    updateRecord(serialNum, bossReqNum);
                    return new JxBossOperationResultImpl(JXReturnCode.PARA_ILLEGALITY);
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if (bodyResp != null) {
            LOGGER.info("江西充值失败,resultCode:{},resultMsg:{}", bodyResp.getResultCode(), bodyResp.getResultMsg());
        }
        JXReturnCode code = JXReturnCode.FAILD;
        code.setMsg(bodyResp == null ? code.getMsg() : bodyResp.getResultMsg());
        updateRecord(serialNum, bossReqNum);
        return new JxBossOperationResultImpl(code);
    }

    private <T> T xml2Obj(Class<T> cls, String rootTag, String data) {
        LOGGER.info("Xml转对象之前String:{}", data);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias(rootTag, cls);
        return (T) xStream.fromXML(data);
    }

    private boolean updateRecord(String systemNum, String bossReqNum) {
        if (StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }

    @Override
    public String getFingerPrint() {
        return "jiangxi123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }


    private JXChargeHead buildHead(String bossReqNum) {
        JXChargeHead head = new JXChargeHead();
        head.setCode("PresentPhoneVolume");
        head.setSid(bossReqNum);
        head.setServiceId(getServiceId());
        head.setTimeStmp(DateUtil.getJiangXiBossTime());
        return head;
    }

    private JXChargeBodyReq buildBody(String phone, String pCode) {
        JXChargeBodyReq body = new JXChargeBodyReq();
        body.setEcCode(getEcCode());
        body.setPhone(phone);
        body.setVolume(pCode);
        return body;
    }

    private String buildReqStr(JXChargeHead head, String body) {
        PresentPhoneVolumeReq volumeReq = new PresentPhoneVolumeReq();
        volumeReq.setChargeHead(head);
        volumeReq.setChargeBody(body);
        try {
            XStream xStream = new XStream();
            xStream.alias("PresentPhoneVolumeReq", PresentPhoneVolumeReq.class);
            xStream.autodetectAnnotations(true);
            return xStream.toXML(volumeReq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String encryptBody(JXChargeBodyReq body, String key) {
        XStream xStream = new XStream();
        xStream.alias("BODY", JXChargeBodyReq.class);
        xStream.autodetectAnnotations(true);
        try {
            return CommonMessageTool.encrypt(xStream.toXML(body), key);
        } catch (Exception e) {
            LOGGER.error("江西渠道加密抛出异常:{}", e.getMessage());
        }
        return new String();
    }

    private String decryptBody(String data, String key) {
        return CommonMessageTool.decrypt(data, key);
    }

    private String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_APPSECRET.getKey());
    }

    private String getServiceId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_SERVICEID.getKey());
    }

    private String getEcCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_ECCODE.getKey());
    }
}

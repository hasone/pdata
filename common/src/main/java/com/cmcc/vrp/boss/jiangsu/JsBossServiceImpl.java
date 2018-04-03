package com.cmcc.vrp.boss.jiangsu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.jiangsu.model.Content;
import com.cmcc.vrp.boss.jiangsu.model.Cplanpackagedt;
import com.cmcc.vrp.boss.jiangsu.model.OperationIn;
import com.cmcc.vrp.boss.jiangsu.model.OperationOut;
import com.cmcc.vrp.boss.jiangsu.model.PackageUserId;
import com.cmcc.vrp.boss.jiangsu.util.GbkBasicResponseHandler;
import com.cmcc.vrp.boss.jiangsu.util.JsHttpUtils;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;


/**
 * @author lgk8023
 *
 */
@Service("JsBossService")
public class JsBossServiceImpl implements BossService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsBossServiceImpl.class);
    private static XStream xStream = null;

    static {
        xStream = new XStream(new DomDriver("GBK", new XmlFriendlyNameCoder("-_", "_")));
        xStream.alias("operation_in", OperationIn.class);
        xStream.alias("operation_out", OperationOut.class);
        xStream.autodetectAnnotations(true);
    }
    
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    SerialNumService serialNumService;
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("江苏充值start,mobile{},serialNum{}", mobile, serialNum);
        SupplierProduct supplierProduct = null;
        if (entId == null
                || splPid == null
                || StringUtils.isEmpty(serialNum)
                || !StringUtils.isValidMobile(mobile)
                || (enterprisesService.selectByPrimaryKey(entId)) == null
                || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null) {
            LOGGER.error("无效的充值请求参数entId{},splpid{},mobile{},serialnum{}", entId, splPid, mobile, serialNum);
            return null;
        }
        String prdcode = supplierProduct.getCode();
        OperationIn operationIn = buildOperationIn(prdcode, mobile, serialNum);
        try {
            String req = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xStream.toXML(operationIn);
            LOGGER.info("充值请求报文{}", req);
            String resp = JsHttpUtils.post(getChargeUrl(), req, null, "application/xml", new GbkBasicResponseHandler());

            LOGGER.info("充值响应报文{}", resp);
            if (StringUtils.isEmpty(resp)) {
                return new JsBossOperationResultImpl("-100", "boss响应为空");
            }
            OperationOut operationOut = (OperationOut) xStream.fromXML(resp);
            if (operationOut == null) {
                return new JsBossOperationResultImpl("-103", "解析报文为空");
            }
            if (operationOut.getResponse() != null
                    && !"0000".equals(operationOut.getResponse().getRespCode())) {
                JsBossOperationResultImpl bossOperationResultImpl = 
                        new JsBossOperationResultImpl(operationOut.getResponse().getRespCode(), operationOut.getResponse().getRespDesc());
                bossOperationResultImpl.setEntId(entId);
                bossOperationResultImpl.setSystemNum(serialNum);
                bossOperationResultImpl.setFingerPrint(getFingerPrint());
                return bossOperationResultImpl;
            }
            if (!"0".equals(operationOut.getContent().getRetCode())) {
                return new JsBossOperationResultImpl("-101", "充值失败");
            }
            String respSerial = operationOut.getContent().getCcOperatingSrl();
            if (!updateRecord(serialNum, respSerial, serialNum)) {
                LOGGER.error("江苏充值更新流水号失败,serialNum:{}.bossRespNum:{}", serialNum, respSerial);
            }
            return new JsBossOperationResultImpl("0", "充值成功");
        } catch (Exception e) {
            LOGGER.error("充值异常：" + e.getMessage());
            return new JsBossOperationResultImpl("-102", "充值失败");
        }
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
    private OperationIn buildOperationIn(String prdcode, String mobile, String serialNum) {
        Cplanpackagedt cplanpackagedt = new Cplanpackagedt();
        cplanpackagedt.setPackageType("");
        cplanpackagedt.setPackageCode(prdcode);
        cplanpackagedt.setPackageInureMode("1");
        cplanpackagedt.setPackageCmdCode("1");
        cplanpackagedt.setPackageOldCode("");
        cplanpackagedt.setPackageAddAttr("");
        
        PackageUserId packageUserId = new PackageUserId();
        packageUserId.setCplanpackagedt(cplanpackagedt);
        
        Content content = new Content();
        content.setUserId("");
        content.setMsisdn(mobile);
        content.setPackageUserId(packageUserId);
        
        OperationIn operationIn = new OperationIn();
        operationIn.setProcessCode("cc_cchgpkgforpro");
        operationIn.setAppId(getAppId());
        operationIn.setAccessToken(getAccessToken());
        operationIn.setSign("");
        operationIn.setVerifyCode("");
        operationIn.setRequestType("");
        operationIn.setSysfuncId("");
        operationIn.setOperatorId(getOperatorId());
        operationIn.setOrganId("");
        operationIn.setRouteType("2");
        operationIn.setRouteValue(mobile);
        operationIn.setRequestTime("");
        operationIn.setRequestSeq(serialNum);
        operationIn.setRequestSource("");
        operationIn.setRequestTarget("");
        operationIn.setMsgVersion("");
        operationIn.setContVersion("");
        operationIn.setUserPasswd("");
        operationIn.setOperatorIp("");
        operationIn.setLoginMsisdn("");
        operationIn.setContent(content);
        return operationIn;
    }

    @Override
    public String getFingerPrint() {
        // TODO Auto-generated method stub
        return "jiangsu";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        // TODO Auto-generated method stub
        return false;
    }
    private String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_APPID.getKey());
    }
    private String getAccessToken() {
        return globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_ACCESS_TOKEN.getKey());
    }
    private String getOperatorId() {
        return globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_OPERATOR_ID.getKey());
    }
    
    private String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_CHARGE_URL.getKey());
    }
}

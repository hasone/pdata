package com.cmcc.vrp.boss.heilongjiang;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.heilongjiang.model.HLJBossEntAccountResult;
import com.cmcc.vrp.boss.heilongjiang.model.HLJChargeResult;
import com.cmcc.vrp.boss.heilongjiang.model.HLJChargeResultPojo;
import com.cmcc.vrp.boss.heilongjiang.model.HLJSupplementModel;
import com.cmcc.vrp.boss.heilongjiang.model.HlJBossOperationResultImpl;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.GsonBuilder;

/**
 * 黑龙江boss实现类，参考原一期的代码
 * <p>
 * 原实现请看OpMsg.java
 *
 * @author qihang
 */
@Component("HLJBossService")
public class HLJBossServiceImpl implements BossService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HLJBossServiceImpl.class);

    @Autowired
    EntProductService entProductService;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    SerialNumService serialNumService;
    /**
     * 
     */
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("黑龙江charge method start!");
        //检查参数
        SupplierProduct supplierProduct = null;
        Enterprise enterprise = null;
        if (splPid == null || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
                || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null || StringUtils.isBlank(mobile)
                || StringUtils.isBlank(serialNum)) {
            LOGGER.info("调用黑龙江BOSS充值接口失败：参数错误. EntId = {}, SplPid = {}, mobile = {}, serialNum = {}", entId, splPid,
                    mobile, serialNum);
            return null;
        }

        String productCode = supplierProduct.getCode();
        String enterCode = enterprise.getCode();

        if (useBossCharge() && !queryGroupAccount(enterCode, SerialNumGenerator.buildSerialNum()).isSuccess()) {//直接使用BOSS提供的充值接口
            LOGGER.info("路由到BOSS接口，moible = " + mobile);
            GsonBuilder gb = new GsonBuilder();
            com.google.gson.Gson gson = gb.create();
            gb.setDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
            HLJSupplementModel model = gson.fromJson(supplierProduct.getFeature(), HLJSupplementModel.class);

            //填充BOSS请求序列号
            String bossReqSerialNum = "0";//黑龙江BOSS充值请求序列号约定都为0
            SerialNum serialNumObj = new SerialNum();
            serialNumObj.setBossReqSerialNum(bossReqSerialNum);
            serialNumObj.setPlatformSerialNum(serialNum);
            if (!serialNumService.updateSerial(serialNumObj)) {
                LOGGER.info("填充BOSS请求序列号失败：platformSerialNum = " + serialNum + ",bossReqSerialNum = "
                        + bossReqSerialNum);
            }
            LOGGER.info("平台系统序列号platformSerialNum：" + serialNum + ",BOSS请求序列号bossReqSerialNum：" + bossReqSerialNum);

            List<String> result = null;
            if (useBossBatchCharge()) {//使用BOSS提供批量赠送接口
                LOGGER.info("使用BOSS提供的批量赠送接口，moible = " + mobile);
                List<String> mobiles = new ArrayList<String>();
                mobiles.add(mobile);
                result = batchCharge(enterCode, productCode, mobiles, model, bossReqSerialNum);

                //获取响应流水号好，更新serialNum：0 esbRetCode; 1 retCode(状态码); 2: buffer(BOSS响应序列号) 3: retMsg(操作说明)
                if (result.size() == 4) {
                    SerialNum serialNumReturn = new SerialNum();
                    serialNumReturn.setPlatformSerialNum(serialNum);
                    serialNumReturn.setBossRespSerialNum(result.get(2));
                    if (!serialNumService.updateSerial(serialNumReturn)) {
                        LOGGER.info("更新BOSS响应序列号失败：platformSerialNum = " + serialNum + ",bossRespSerialNum = "
                                + serialNumReturn.getBossRespSerialNum());
                    }
                } else {
                    LOGGER.info("BOSS返回的报文参数缺失，无法获取响应序列号:" + result.toString());
                }
                return new HlJBossOperationResultImpl(result);
            } else {//使用BOSS提供单独赠送接口
                LOGGER.info("使用BOSS提供的单个赠送接口，moible = " + mobile);
                result = charge(enterCode, productCode, mobile, model);
                return new HlJBossOperationResultImpl(result);
            }

        } else {//思特奇流量充值接口,第三方能力平台接口           
            //填充BOSS请求序列号
            LOGGER.info("路由到思特奇提供的流量包充值接口，moible = " + mobile);
            String bossReqSerialNum = SerialNumGenerator.buildSerialNum();
            SerialNum serialNumObj = new SerialNum();
            serialNumObj.setBossReqSerialNum(bossReqSerialNum);
            serialNumObj.setPlatformSerialNum(serialNum);
            if (!serialNumService.updateSerial(serialNumObj)) {
                LOGGER.info("填充BOSS请求序列号失败：platformSerialNum = " + serialNum + ",bossReqSerialNum = "
                        + bossReqSerialNum);
            }
            LOGGER.info("平台系统序列号platformSerialNum：" + serialNum + ",BOSS请求序列号bossReqSerialNum：" + bossReqSerialNum);
            GsonBuilder gb = new GsonBuilder();
            com.google.gson.Gson gson = gb.create();
            gb.setDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
            HLJSupplementModel model = gson.fromJson(supplierProduct.getFeature(), HLJSupplementModel.class);
            return charge(enterCode, model.getiOfferId(), mobile, "1", "1", bossReqSerialNum);
        }
    }

    /**
     * 
     */
    @Override
    public String getFingerPrint() {
        return "hlj";
    }
    /**
     * 
     */
    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }
    /**
     * 
     */
    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }
    /**
     * 
     */
    private List<String> charge(String enterCode, String productCode, String mobile, HLJSupplementModel model) {
        String[] s = new String[14];
        s[0] = "0";
        s[1] = getiChnSource();
        s[2] = getiOpCode();
        s[3] = getiLoginNo();
        s[4] = getiLoginPwd();
        s[5] = getiPhoneNo();
        s[6] = "";
        s[7] = enterCode;
        s[8] = mobile;
        s[9] = productCode;
        s[10] = model.getiOfferId();
        s[11] = model.getiEffLength();
        s[12] = "1";
        s[13] = model.getiEffType();

        LOGGER.info("调用黑龙江BOSS充值接口：参数为. 业务流水 = {}, 渠道标识 = {}, 操作代码 = {}, 工号 = {},"
                + "工号密码 = {}, 号码 = {}, 号码密码 = {}, 集团编码 = {},被赠送的手机号码={},赠送流量包编码={},"
                + "赠送资费代码 = {}, 赠送月份数 = {}, 赠送流量包个数 = {}, 赠送生效方式 = {}", s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7],
                s[8], s[9], s[10], s[11], s[12], s[13]);

        List<String> list = getXml(getWsdlAddress(), "callService", s, "http://txdoWS.esb.sitech.com");
        return list;
    }
    /**
     * 
     */
    private List<String> getXml(String endpoint, String localPart, Object[] opArgs, String namespaceURI) {
        try {
            ServiceClient sender = new ServiceClient();
            EndpointReference targetEPR = new EndpointReference(endpoint);

            String action = getChargeAddress();
            Options options = new Options();
            options.setAction(action);
            options.setTo(targetEPR);
            sender.setOptions(options);

            options.setTo(targetEPR);
            OMFactory fac = OMAbstractFactory.getOMFactory();
            OMNamespace omNs = fac.createOMNamespace(namespaceURI, localPart);
            OMElement data = fac.createOMElement("callService", omNs);

            for (int i = 0; i < 14; i++) {
                OMElement inner = fac.createOMElement("params", omNs);
                inner.setText((String) opArgs[i]);
                data.addChild(inner);
            }
            List<String> resultRecv = new ArrayList<String>();
            LOGGER.info("请求WDSL:{},请求地址:{},请求报文:{}", endpoint, action, data.toString());
            OMElement result = sender.sendReceive(data);
            if (result != null) {
                LOGGER.info("响应报文：" + result.toString());
            } else {
                LOGGER.info("响应报文为NULL");
                return resultRecv;
            }

            OMElement firstElement = result.getFirstElement();
            Iterator it = firstElement.getChildElements();
            while (it.hasNext()) {
                OMElement childs = (OMElement) it.next();
                if (childs.getLocalName().equals("esbRetCode")) {
                    resultRecv.add(childs.getText());
                } else if (childs.getLocalName().equals("retCode")) {
                    resultRecv.add(childs.getText());
                } else if (childs.getLocalName().equals("retMsg")) {
                    resultRecv.add(childs.getText());
                }
            }
            return resultRecv;

        } catch (org.apache.axis2.AxisFault e) {
            LOGGER.error(e.getMessage());
            return new ArrayList<String>();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ArrayList<String>();
        }
    }
    /**
     * 
     */
    public String getWsdlAddress() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_WSDL_ADDRESS.getKey());
    }
    /**
     * 
     */
    public String getChargeAddress() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_CHARGE_ADDRESS.getKey());
    }
    /**
     * 
     */
    public String getiChnSource() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_CHANNEL_SOURCE.getKey());
    }

    public String getiOpCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_OP_CODE.getKey());
    }
    /**
     * 
     */
    public String getiLoginNo() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_LOGIN_NO.getKey());
    }
    /**
     * 
     */
    public String getiLoginPwd() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_LOGIN_PWD.getKey());
    }
    /**
     * 
     */
    public String getiPhoneNo() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_PHONE_NO.getKey());
    }

    /**
     * 
     * @Title: getiRowCount 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getiRowCount() {
        String iRowCount = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_QUERY_CHARGE_RESULT_ROWCOUNT.getKey());
        return StringUtils.isNotBlank(iRowCount) ? iRowCount : "5";
    }

    /**
     * 
     * @Title: getiOpPhone 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getiOpPhone() {
        String iOpPhone = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_QUERY_CHARGE_RESULT_IOPPHONE.getKey());
        return StringUtils.isNotBlank(iOpPhone) ? iOpPhone : "";
    }

    /**
     * 
     * @Title: getiUserPwd 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getiUserPwd() {
        String iUserPwd = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_QUERY_CHARGE_RESULT_IUSERPWD.getKey());
        return StringUtils.isNotBlank(iUserPwd) ? iUserPwd : "";
    }

    /**
     * 
     * @Title: getiRowNum 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getiRowNum() {
        String iRowNum = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_QUERY_CHARGE_RESULT_ROWNUM.getKey());
        return StringUtils.isNotBlank(iRowNum) ? iRowNum : "1";
    }

    /**
     * 
     * @Title: getiRepeatFlag 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getiRepeatFlag() {
        String iRepeatFlag = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_IREPEAT_FLAG.getKey());
        return StringUtils.isNotBlank(iRepeatFlag) ? iRepeatFlag : "0";//默认不允许重复
    }

    /**
     * 
     * @Title: getQueryiOpCode 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getQueryiOpCode() {
        String iOpCode = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_QUERY_CHARGE_RESULT_IOPCODE.getKey());
        return StringUtils.isNotBlank(iOpCode) ? iOpCode : "g686";//默认值
    }

    /**
     * 
     * @Title: useBossCharge 
     * @Description: 是否使用BOSS接口
     * @return
     * @return: boolean
     */
    private boolean useBossCharge() {
        //开关打开：使用BOSS充值接口
        String chargeMethod = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_USE_BOSS_INTERFACE.getKey());
        return (StringUtils.isNotBlank(chargeMethod) && "YES".equalsIgnoreCase(chargeMethod));
    }

    /**
     * 
     * @Title: getQueryWSDL 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getQueryWSDL() {
        String queryWSDL = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_QUERY_CHARGE_RESULT_WSDL.getKey());
        return StringUtils.isNotBlank(queryWSDL) ? queryWSDL
                : "http://10.110.26.27:51000/esbWS/services/sHyLLPhoneQryWS?wsdl";
    }

    /**
     * 
     * @Title: getQueryAddress 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getQueryAddress() {
        String queryAddress = globalConfigService
                .get(GlobalConfigKeyEnum.BOSS_HLJ_QUERY_CHARGE_RESULT_ADDRESS.getKey());
        return StringUtils.isNotBlank(queryAddress) ? queryAddress
                : "http://10.110.26.27:51000/esbWS/services/sHyLLPhoneQryWS/callService";
    }

    /**
     * 
     * @Title: queryChargeResult 
     * @Description: 黑龙江充值结果查询接口
     * @param bossReqNum 操作流水
     * @param entCode 集团编码
     * @return
     * @return: List<HLJChargeResultPojo>
     */
    public List<HLJChargeResultPojo> queryChargeResult(String bossReqNum, String entCode) {
        //约定的报文格式
        //        <?xml version="1.0" encoding="utf-8"?>
        //        <soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope">
        //          <soapenv:Body>
        //            <axis2ns294:callService xmlns:axis2ns294="http://txdoWS.esb.sitech.com">
        //              <params>118739297234</params>
        //              <params>01</params>
        //              <params>g686</params>
        //              <params>newweb</params>
        //              <params>EIGBDHBHPHHPMJCE</params>
        //              <params/>
        //              <params/>
        //              <params>4512418076</params>
        //              <params/>
        //              <params/>
        //              <params>1</params>
        //              <params>5</params>
        //              <params/>
        //            </axis2ns294:callService>
        //          </soapenv:Body>
        //        </soapenv:Envelope>

        String[] s = new String[13];
        s[0] = bossReqNum;//iLoginAccept 操作流水,如1234558468
        s[1] = getiChnSource();//iChnSource,渠道标识，如01
        s[2] = getQueryiOpCode();//iOpCode,操作代码，如g686
        s[3] = getiLoginNo();//iLoginNo,操作工号，如newweb
        s[4] = getiLoginPwd();//iLoginPwd,工号密码，如EIGBDHBHPHHPMJCE
        s[5] = getiPhoneNo();//iPhoneNo,手机号码
        s[6] = getiUserPwd();//iUserPwd,用户密码

        s[7] = entCode;//iUnitId,集团编码，如4510993020
        s[8] = "";//iFieldCode,流量包编码
        s[9] = "";//iOfferId,资费代码
        s[10] = getiRowNum();//iRowNum,查询行数，如1
        s[11] = getiRowCount();//iRowCount,每行展示的个数,如5
        s[12] = getiOpPhone();//iOpPhone,操作员手机号
        LOGGER.info(
                "黑龙江充值结果查询接口： 操作流水iLoginAccept = {}, 渠道标识iChnSource = {}, 操作代码iOpCode = {}, 操作工号iLoginNo = {},"
                        + "工号密码iLoginPwd = {}, 手机号码iPhoneNo = {}, 用户密码iUserPwd = {}, 集团编码iUnitId= {},流量包编码iFieldCode ={},资费代码iOfferId={}, 查询行数iRowNum={},"
                        + "每行展示的个数iRowCount= {}, 操作员手机号iOpPhone= {}.", s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7],
                s[8], s[9], s[10], s[11], s[12]);
        String xml = sendRequest(getQueryWSDL(), getQueryAddress(), s);
        //解析报文
        return parseQueryChargeResult(xml);

    }

    //批量赠送
    /**
     * 
     */
    public List<String> batchCharge(String entCode, String productCode, List<String> mobiles, HLJSupplementModel model,
            String bossReqNum) {

        //约定的报文格式
        //        <soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope">
        //          <soapenv:Body>
        //            <axis2ns286:callService xmlns:axis2ns286="http://txdoWS.esb.sitech.com">
        //              <params>1234558468</params>
        //              <params>01</params>
        //              <params>g685</params>
        //              <params>newweb</params>
        //              <params>EIGBDHBHPHHPMJCE</params>
        //              <params/>
        //              <params/>
        //              <params>4510993020</params>
        //              <params>13904516568|13904516568|</params>
        //              <params>10672|10672|</params>
        //              <params>38364|38364|</params>
        //              <params>1|1|</params>
        //              <params>1|1|</params>
        //              <params>1|1|</params>
        //              <params>0</params>
        //              <params/>
        //            </axis2ns286:callService>
        //          </soapenv:Body>
        //        </soapenv:Envelope>

        String[] s = new String[16];
        s[0] = bossReqNum;//iLoginAccept 操作流水,如1234558468
        s[1] = getiChnSource();//iChnSource,渠道标识，如01
        s[2] = getiOpCode();//iOpCode,操作代码，如g685
        s[3] = getiLoginNo();//iLoginNo,操作工号，如newweb
        s[4] = getiLoginPwd();//iLoginPwd,工号密码，如EIGBDHBHPHHPMJCE
        s[5] = getiPhoneNo();//iPhoneNo,手机号码
        s[6] = "";//iUserPwd,用户密码

        s[7] = entCode;//iUnitId,集团编码，如4510993020
        s[8] = "";//iPhoneNoList,被赠送的手机串，如13904516568|13904516568|
        s[9] = "";//iFieldCodeList,被赠送的流量编码串，如10672|10672|
        s[10] = "";//iOfferIdList,被赠送的资费代码串，如38364|38364|
        s[11] = "";//iEffLengthList,赠送月份数量串，如1|1|
        s[12] = "";//iOfferNumList,赠送流量包数量串，如1|1|
        s[13] = "";//iEffTypeList,生效规则串，如1|1|
        s[14] = getiRepeatFlag();//iRepeatFlag,号码重复标志(0-允许重复；1-不允许重复)，如0
        s[15] = "";//iLLType,流水包类型

        for (String mobile : mobiles) {
            s[8] += mobile + "|";//拼接被赠送的手机串
            s[9] += productCode + "|";//拼接被赠送的流量编码串
            s[10] += model.getiOfferId() + "|";//拼接被赠送的资费代码串
            s[11] += model.getiEffLength() + "|";//拼接赠送月份数量串
            s[12] += "1|";//拼接赠送流量包数量串,默认赠送每个号码赠送1个
            s[13] += model.getiEffType() + "|";//拼接生效规则串
        }
        LOGGER.info(
                "黑龙集批量赠送接口： 操作流水iLoginAccept={}, 渠道标识iChnSource={}, 操作代码iOpCode={}, 操作工号iLoginNo={},"
                        + "工号密码iLoginPwd={}, 手机号码iPhoneNo={}, 用户密码iUserPwd={}, 集团编码iUnitId={}, 被赠送的手机串iPhoneNoList={}, 被赠送的流量编码串iFieldCodeList={}, 被赠送的资费代码串iOfferIdList={},"
                        + "赠送月份数量串iEffLengthList={}, 赠送流量包数量串iOfferNumList={}, 生效规则串iEffTypeList={}, 号码重复标志iRepeatFlag={}, 流水包类型iLLType={}.",
                s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], s[9], s[10], s[11], s[12], s[13], s[14], s[15]);
        String xml = sendRequest(getWsdlAddress(), getChargeAddress(), s);
        return parseBatchChargeResp(xml);
    }

    //发送充值请求
    /**
     * 
     */
    private String sendRequest(String endpoint, String action, String[] params) {
        String response = "";
        try {
            ServiceClient sender = new ServiceClient();
            EndpointReference targetEPR = new EndpointReference(endpoint);

            Options options = new Options();
            options.setAction(action);
            options.setTo(targetEPR);
            sender.setOptions(options);
            options.setTo(targetEPR);
            OMFactory fac = OMAbstractFactory.getOMFactory();

            //构建txd:callService子节点
            OMNamespace omNsCallService = fac.createOMNamespace("http://txdoWS.esb.sitech.com", "txd");
            OMElement eleCallService = fac.createOMElement("callService", omNsCallService);
            for (int i = 0; i < params.length; i++) {
                //构建params子节点
                OMElement eleParams = fac.createOMElement("params", null);
                eleParams.setText((String) params[i]);
                //添加params子节点
                eleCallService.addChild(eleParams);
            }

            LOGGER.info("请求WDSL:{},请求地址:{},请求报文:{}", endpoint, action, eleCallService.toString());
            OMElement result = sender.sendReceive(eleCallService);
            if (result != null) {
                response = result.toString();
                LOGGER.info("响应报文：" + response);
            } else {
                LOGGER.info("响应报文为Null,使用空串替换");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

    /**
     * 
     * @Title: parseBatchChargeResp 
     * @Description: 批量赠送报文解析
     * @param xml
     * @return
     * @return: List<String>
     */
    public List<String> parseBatchChargeResp(String xml) {
        //约定的报文格式      
        String defaultXmlFormat = "<ns:callServiceResponse xmlns:ns=\"http://txdoWS.esb.sitech.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<ns:return xmlns:ax22007=\"http://ws.esb.sitech.com/xsd\" type=\"com.sitech.esb.ws.SrvReturnBean\">"
                + "<ax22007:esbRetCode>0</ax22007:esbRetCode>"
                + "<ax22007:retCode>000000</ax22007:retCode>"
                + "<ax22007:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax22007:buffer>1234558468</ax22007:buffer>"
                + "</ax22007:retMatrix>"
                + "<ax22007:retMsg>操作成功</ax22007:retMsg>" + "</ns:return>" + "</ns:callServiceResponse>";
        List<String> resultRecv = new ArrayList<String>();
        try {
            OMElement result = OMXMLBuilderFactory.createOMBuilder(new ByteArrayInputStream(xml.getBytes("UTF-8")),
                    "UTF-8").getDocumentElement();
            OMElement firstElement = result.getFirstElement();
            Iterator it = firstElement.getChildElements();
            while (it.hasNext()) {
                OMElement childs = (OMElement) it.next();
                if (childs.getLocalName().equals("esbRetCode")) {
                    resultRecv.add(childs.getText());
                } else if (childs.getLocalName().equals("retCode")) {
                    resultRecv.add(childs.getText());
                } else if (childs.getLocalName().equals("retMatrix")) {
                    OMElement bufferEle = childs.getFirstElement();
                    resultRecv.add(bufferEle.getText());
                } else if (childs.getLocalName().equals("retMsg")) {
                    resultRecv.add(childs.getText());
                }
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("黑龙江流量包批量充值接口响应报文格式错误，约定的报文格式为：" + defaultXmlFormat + ",实际报文格式：" + xml);
        } catch (Exception e) {
            LOGGER.error("黑龙江流量包批量充值接口响应报文格式错误，约定的报文格式为：" + defaultXmlFormat + ",实际报文格式：" + xml);
        }
        return resultRecv;
    }

    /**
     * 
     * @Title: parseQueryChargeResult 
     * @Description: 黑龙江充值结果查询接口响应报文解析
     * @param xml
     * @return
     * @return: List<HLJChargeResultPojo>
     */
    private List<HLJChargeResultPojo> parseQueryChargeResult(String xml) {
        List<HLJChargeResultPojo> reslutList = new LinkedList<HLJChargeResultPojo>();
        //约定的报文格式
        //        <ns:callServiceResponse xmlns:ns="http://txdoWS.esb.sitech.com" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        //          <ns:return xmlns:ax21031="http://ws.esb.sitech.com/xsd" type="com.sitech.esb.ws.SrvReturnBean">
        //            <ax21031:esbRetCode>0</ax21031:esbRetCode>
        //            <ax21031:retCode>000000</ax21031:retCode>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>3</ax21031:buffer>
        //              <ax21031:buffer>3</ax21031:buffer>
        //              <ax21031:buffer>3</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>0</ax21031:buffer>
        //              <ax21031:buffer>0</ax21031:buffer>
        //              <ax21031:buffer>0</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>0</ax21031:buffer>
        //              <ax21031:buffer>0</ax21031:buffer>
        //              <ax21031:buffer>0</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>3</ax21031:buffer>
        //              <ax21031:buffer>3</ax21031:buffer>
        //              <ax21031:buffer>3</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>2017-07-25 16:31:42</ax21031:buffer>
        //              <ax21031:buffer>2017-07-25 16:31:42</ax21031:buffer>
        //              <ax21031:buffer>2017-07-25 16:31:42</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>2017-07-25 16:31:42</ax21031:buffer>
        //              <ax21031:buffer>2017-07-25 16:31:42</ax21031:buffer>
        //              <ax21031:buffer>2017-07-25 16:31:42</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>13904516567</ax21031:buffer>
        //              <ax21031:buffer>13904516568</ax21031:buffer>
        //              <ax21031:buffer>13904516569</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>5元含30M流量包</ax21031:buffer>
        //              <ax21031:buffer>5元含30M流量包</ax21031:buffer>
        //              <ax21031:buffer>5元含30M流量包</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>1</ax21031:buffer>
        //              <ax21031:buffer>1</ax21031:buffer>
        //              <ax21031:buffer>1</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>1</ax21031:buffer>
        //              <ax21031:buffer>1</ax21031:buffer>
        //              <ax21031:buffer>1</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>1</ax21031:buffer>
        //              <ax21031:buffer>1</ax21031:buffer>
        //              <ax21031:buffer>1</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer>D</ax21031:buffer>
        //              <ax21031:buffer>D</ax21031:buffer>
        //              <ax21031:buffer>D</ax21031:buffer>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer/>
        //              <ax21031:buffer/>
        //              <ax21031:buffer/>
        //            </ax21031:retMatrix>
        //            <ax21031:retMatrix type="com.sitech.esb.ws.TxdoBuf">
        //              <ax21031:buffer/>
        //              <ax21031:buffer/>
        //              <ax21031:buffer/>
        //            </ax21031:retMatrix>
        //            <ax21031:retMsg>操作成功</ax21031:retMsg>
        //          </ns:return>
        //        </ns:callServiceResponse>

        String defaultXmlFormat = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<ns:callServiceResponse xmlns:ns=\"http://txdoWS.esb.sitech.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<ns:return xmlns:ax21999=\"http://ws.esb.sitech.com/xsd\" type=\"com.sitech.esb.ws.SrvReturnBean\">"
                + "<ax21999:esbRetCode>0</ax21999:esbRetCode>" + "<ax21999:retCode>000000</ax21999:retCode>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">" + "<ax21999:buffer>1</ax21999:buffer>"
                + "</ax21999:retMatrix>" + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>1</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">" + "<ax21999:buffer>0</ax21999:buffer>"
                + "</ax21999:retMatrix>" + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>0</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>2015-04-29 21:50:53</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>2015-04-29 21:51:17</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>13836874845</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>10元含100M流量包</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">" + "<ax21999:buffer>1</ax21999:buffer>"
                + "</ax21999:retMatrix>" + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>1</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">" + "<ax21999:buffer>1</ax21999:buffer>"
                + "</ax21999:retMatrix>" + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>Y</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">" + "<ax21999:buffer/>"
                + "</ax21999:retMatrix>" + "<ax21999:retMatrix type=\"com.sitech.esb.ws.TxdoBuf\">"
                + "<ax21999:buffer>13555225521</ax21999:buffer>" + "</ax21999:retMatrix>"
                + "<ax21999:retMsg>操作成功</ax21999:retMsg>" + "</ns:return>" + "</ns:callServiceResponse>";
        try {
            OMElement result = OMXMLBuilderFactory.createOMBuilder(new ByteArrayInputStream(xml.getBytes("UTF-8")),
                    "UTF-8").getDocumentElement();
            OMElement firstElement = result.getFirstElement();
            Iterator it = firstElement.getChildElements();
            List<OMElement> elements = new LinkedList<OMElement>();
            while (it.hasNext()) {
                OMElement childs = (OMElement) it.next();
                if (childs.getLocalName().equals("retMatrix")) {
                    elements.add(childs);
                }
            }
            if (elements.size() != 14) {
                LOGGER.error("黑龙江流量包充值结果查询接口响应报文格式错误，约定的报文格式为：" + defaultXmlFormat);
                return reslutList;
            }

            //计算记录条数
            it = elements.get(0).getChildElements();
            while (it.hasNext()) {
                HLJChargeResultPojo pojo = new HLJChargeResultPojo();
                reslutList.add(pojo);
                it.next();
            }
            int i = 0;

            //填充：总数（当操作代码为g687，为文件名称）
            it = elements.get(0).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoCountNum(buffer.getText());
                i++;
            }
            //成功总数
            it = elements.get(1).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoCountY(buffer.getText());
                i++;
            }
            //失败总数
            it = elements.get(2).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoCountN(buffer.getText());
                i++;
            }
            //待处理总数
            it = elements.get(3).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoCountD(buffer.getText());
                i++;
            }
            //接收时间
            it = elements.get(4).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoReceiveTime(buffer.getText());
                i++;
            }

            //处理时间
            it = elements.get(5).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoDealTime(buffer.getText());
                i++;
            }
            //手机号码
            it = elements.get(6).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoPhoneNo(buffer.getText());
                i++;
            }

            //资费名称
            it = elements.get(7).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoOfferName(buffer.getText());
                i++;
            }
            //赠送月份数量
            it = elements.get(8).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoEffLength(buffer.getText());
                i++;
            }

            //赠送流量包数量
            it = elements.get(9).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoOfferNum(buffer.getText());
                i++;
            }

            //生效规则
            it = elements.get(10).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoEffType(buffer.getText());
                i++;
            }

            //处理结果
            it = elements.get(11).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoDealState(buffer.getText());
                i++;
            }

            //失败原因
            it = elements.get(12).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setoFailMsg(buffer.getText());
                i++;
            }

            //操作员手机号
            it = elements.get(13).getChildElements();
            i = 0;
            while (it.hasNext()) {
                OMElement buffer = (OMElement) it.next();
                reslutList.get(i).setiOpPhone(buffer.getText());
                i++;
            }

            LOGGER.info("充值结果参数列表：" + JSONArray.fromObject(reslutList).toString());
            return reslutList;

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("黑龙江流量包充值结果查询接口响应报文格式错误，约定的报文格式为：" + defaultXmlFormat + ",实际报文格式：" + xml);
        } catch (Exception e) {
            LOGGER.error("黑龙江流量包充值结果查询接口响应报文格式错误，约定的报文格式为：" + defaultXmlFormat + ",实际报文格式：" + xml);
        }
        return reslutList;
    }

    /**
     * 
     * @Title: assembleParaForCharge 
     * @Description: 行业应用流量包实时赠送接口构造报文
     * @param entCode 集团编码
     * @param prdCode 产品编码
     * @param mobile 手机号码
     * @param presentMonth 赠送月份
     * @param transId 流水号
     * @return
     * @return: String
     */
    private String assembleParaForCharge(String entCode, String prdCode, String mobile, String presentNum,
            String presentMonth, String transId) {
        //约定的报文格式
        //        {
        //            "ROOT": {
        //                "BODY": {
        //                    "BUSI_INFO": {
        //                        "ACCESS_TYPE": "11",
        //                        "AGENCY_ID": "4510208711",
        //                        "BUSI_INFO_LIST": [
        //                            {
        //                                "EFF_RULE": "1002",
        //                                "EFF_TIME": "",
        //                                "EXP_RULE": "2001",
        //                                "EXP_TIME": "",
        //                                "GOODS_ID": "201705170001",
        //                                "PAY_MODE": "0",
        //                                "PRESENT_MONTH": "1",
        //                                "SALE_NUM": "2",
        //                                "SALE_PRICE": "0",
        //                                "SALE_SIZE": "30",
        //                                "SERVICE_NUM": "15246337626"
        //                            }
        //                        ],
        //                        "OPER_CODE": "1",
        //                        "OPR_ID": "newweb",
        //                        "OPR_ROLE": "1",
        //                        "PASS_WORD": "BYVJFNRZMCEFBREK",
        //                        "PHONE_NUM": "13605310531",
        //                        "TRANS_ID": "201707090001",
        //                        "UNIT_ID_NO": ""
        //                    },
        //                    "OPR_INFO": {
        //                        "GROUP_ID": "4510208711",
        //                        "LOGIN_NO": "newweb",
        //                        "OP_CODE": "10001",
        //                        "PROVINCE_ID": "230000"
        //                    }
        //                },
        //                "HEADER": {
        //                    "CHANNEL_ID": "11",
        //                    "ROUTING": {
        //                        "ROUTE_KEY": "11",
        //                        "ROUTE_VALUE": "4510208711"
        //                    },
        //                    "TRACE_ID": "11*20170228154105989*0000*hljtest*184505"
        //                }
        //            }
        //        }
        //BUSI_INFO参数信息
        String accessType = getChargeAccessType();//网站赠送,默认37
        String agencyId = entCode;//集团编码,4510208711

        //BUSI_INFO_LIST参数信息
        String effRule = getChargeEffRule();//生效规则（1001 立即生效  1003 1自然日，1002 1自然月，1010 2自然月  1011 3 自然月 1000自定义）只有生效时间有自定义，自定义时，生失效时间必传
        String effTime = getChargeEffTime();//生效时间（YYYY_MM_DD HH24:mi:ss）自定义生效规则，生失效时间必传。
        String expRule = getChargeExpRule();//赠送流量类型( 2000 日包 2001 月包 2002 半年包 2003 年包）
        String expTime = getChargeExpTime();//失效时间（YYYY_MM_DD HH24:mi:ss）自定义生效规则，生失效时间必传。
        String goodsId = prdCode;//商品标识,201705170001
        String payMode = getChargePayMode();//付费类型（0:不收费），赠送送0
        String saleNum = presentNum;//赠送/零售数量,默认1个
        String salePrice = getChargeSalePrice();//赠送/零售价格 （单位li，赠送时传 0）
        String saleSize = getChargeSaleSize();//赠送流量大小（散装存实际大小，标准存商品大小，单位M），可赠送流量查询返回的GOODS_SIZE
        String serviceNum = mobile;//赠送/零售手机号15246337626

        String operCode = getChargeOperCode();//操作类型 传4  （1集团行业部流量赠送2 代理商批量赠送 5行业应用流量包实名制赠送 4 实时赠送 
        String oprId = getChargeOprId();//操作工号 实时赠送 传 llzcpt
        String oprRole = getChargeOprRole();//工号角色（3 网站实时赠送）
        String passWord = getChargePassword();//工号密码
        String phoneNum = getChargePhoneNum();//集团联系人/流量包操作员手机号码
        String unitIdNo = getChargeUnitIdNo();//OPER_CODE 为1时，此值传集团用户id
        String repeatFlag = getChargeRepeateFlag();//是否允许重复赠送标识0-允许重复；1-不允许重复

        //OPR_INFO参数信息
        String groupId = entCode;//工号归属
        String loginNo = getChargeLoginNo();//工号 llzcpt
        String opCode = getChargeOpCode();//操作类型 传4  （1集团行业部流量赠送2 代理商批量赠送 5行业应用流量包实名制赠送 4 实时赠送 
        String provinceId = getChargeProviceId();//省份代码 黑龙江填写：230000       
        String provinceGroup = "HLJ";//默认值 写 HLJ

        //HEADER参数信息
        String channelId = getChargeChannelId();//渠道标识
        String routeKey = getChargeRouteKey();//路由类型，默认11
        String routeValue = entCode;//路由值使用集团编码       
        String traceId = getTraceID(channelId, opCode, loginNo);//按照思特奇的要求生产请求流水号，奇葩规定，无力吐槽

        //构建BUSI_INFO_LIST
        Map<String, Object> busiInfoListMap = new HashMap<String, Object>();
        busiInfoListMap.put("EFF_RULE", effRule);
        busiInfoListMap.put("EFF_TIME", effTime);
        busiInfoListMap.put("EXP_RULE", expRule);
        busiInfoListMap.put("EXP_TIME", expTime);
        busiInfoListMap.put("GOODS_ID", goodsId);
        busiInfoListMap.put("PAY_MODE", payMode);
        busiInfoListMap.put("PRESENT_MONTH", presentMonth);//月包赠送月份，如果失效规则为2001 则这个值为必填
        busiInfoListMap.put("SALE_NUM", saleNum);
        busiInfoListMap.put("SALE_PRICE", salePrice);
        busiInfoListMap.put("SALE_SIZE", saleSize);
        busiInfoListMap.put("SERVICE_NUM", serviceNum);

        //构建BUSI_INFO
        Map<String, Object> busiMap = new HashMap<String, Object>();
        busiMap.put("ACCESS_TYPE", accessType);
        busiMap.put("AGENCY_ID", agencyId);
        busiMap.put("BUSI_INFO_LIST", busiInfoListMap);
        busiMap.put("OPER_CODE", operCode);
        busiMap.put("OPR_ID", oprId);
        busiMap.put("OPR_ROLE", oprRole);
        busiMap.put("PASS_WORD", passWord);
        busiMap.put("PHONE_NUM", phoneNum);
        busiMap.put("TRANS_ID", transId);
        busiMap.put("UNIT_ID_NO", unitIdNo);
        busiMap.put("REPEAT_FLAG", repeatFlag);

        //构建OPR_INFO
        Map<String, Object> oprInfoMap = new HashMap<String, Object>();
        oprInfoMap.put("GROUP_ID", groupId);
        oprInfoMap.put("LOGIN_NO", loginNo);
        oprInfoMap.put("OP_CODE", opCode);
        oprInfoMap.put("PROVINCE_ID", provinceId);

        //构建BODY
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        bodyMap.put("BUSI_INFO", busiMap);
        bodyMap.put("OPR_INFO", oprInfoMap);

        //构建ROUTING
        Map<String, Object> routingMap = new HashMap<String, Object>();
        routingMap.put("ROUTE_KEY", routeKey);
        routingMap.put("ROUTE_VALUE", routeValue);

        //构建HEADER
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("CHANNEL_ID", channelId);
        headerMap.put("ROUTING", routingMap);
        headerMap.put("TRACE_ID", traceId);

        //
        Map<String, Object> allMap = new HashMap<String, Object>();
        allMap.put("BODY", bodyMap);
        allMap.put("HEADER", headerMap);

        //构建ROOT
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("ROOT", allMap);

        //去掉外层的[]，黑龙江BOSS约定，奇葩
        String result = JSONArray.fromObject(rootMap).toString();
        return result.substring(1, result.length() - 1);
    }

    /**
     * 
     * @Title: charge 
     * @Description: 行业应用流量包实时赠送接口
     * @param entCode 集团编码
     * @param prdCode 产品编码
     * @param mobile 手机号码
     * @param presentNum 赠送数量
     * @param presentMonth 赠送月份
     * @param transId 流水号
     * @return
     */
    public HLJChargeResult charge(String entCode, String prdCode, String mobile, String presentNum,
            String presentMonth, String transId) {
        //获取请求URL
        String url = getChargeUrl();
        //请求报文
        String requestParam = assembleParaForCharge(entCode, prdCode, mobile, presentNum, presentMonth, transId);
        LOGGER.info("行业应用流量包实时赠送接口，请求url = {}, 请求参数 = {}.", url, requestParam);
        //发送请求
        String repsone = HttpUtil.invoke(url, requestParam);
        LOGGER.info("行业应用流量包实时赠送接口， 应答报文 = {}.", repsone);

        //解析报文
        return parseChargeResp(repsone);
    }

    /**
     * 
     * @Title: parseChargeResp 
     * @Description: 行业应用流量包实时赠送接口响应报文解析
     * @param response
     * @return
     * @return: HLJChargeResult
     */
    private HLJChargeResult parseChargeResp(String response) {
        //约定的报文格式示例
        String defaultResponse = "{\"code\":\"0000000\",\"message\":\"Rest Request Success\",\"promptMsg\":\"User Operate Success\",\"data\":{\"TRANS_ID\":\"20170828165500520\"}}";
        HLJChargeResult resp = new HLJChargeResult();
        try {
            resp = JSON.parseObject(response, HLJChargeResult.class);
        } catch (Exception e) {
            LOGGER.info("行业应用流量包实时赠送接口报文格式错误，约定的报文格式为：" + defaultResponse + ",实际报文格格式：" + response);
        }
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_IS_ASYC.getKey());
        if (!resp.isSuccess() || (StringUtils.isNotBlank(value) && !"true".equalsIgnoreCase(value))) {//同步接口
            resp.setAsync(false);
        } else {//异步接口
            resp.setAsync(true);
        }
        return resp;
    }

    /**
     * 
     * @Title: assembleParaForGroup 
     * @Description: 黑龙江集团可赠送流量查询接口构造请求报文
     * @param entCode 集团编码
     * @param transId 流水号
     * @return
     * @return: String
     */
    private String assembleParaForGroup(String entCode, String transId) {
        //报文示例
        //        {
        //            "ROOT": {
        //                "BODY": {
        //                    "BUSI_INFO": {
        //                        "OPR_ID": "newweb",
        //                        "OPR_ROLE": "1",
        //                        "TRANS_ID": "201703301001",
        //                        "UNIT_ID": "4510208711",
        //                         "PASS_WORD": "BYVJFNRZMCEFBREK",
        //                         "PHONE_NUM":"13605310531"
        //                    },
        //                    "OPR_INFO": {
        //                        "GROUP_ID": "11",
        //                        "LOGIN_NO": "hljtest",
        //                        "OP_CODE": "0000",
        //                        "PROVINCE_ID": "230000"
        //                    }
        //                },
        //                "HEADER": {
        //                    "CHANNEL_ID": "11",
        //                    "PROVINCE_GROUP": "HLJ",
        //                    "ROUTING": {
        //                        "ROUTE_KEY": "11",
        //                        "ROUTE_VALUE": "4510208711"
        //                    },
        //                    "TRACE_ID": "13248732456617832402"
        //                }
        //            }
        //        }

        //BUSI_INFO参数信息       
        String oprId = getQueryGroupAccountOprId();//操作工号,"newweb";
        String oprRole = getQueryGroupAccountOprRole();//"3";//工号角色（1集团联系人，2流量包操作员 3 网站实时赠送）
        String unitId = entCode;//集团编码,4510208711
        String passWord = getQueryGroupAccountPassword();//工号密码
        String phoneNum = getQueryGroupAccountPhoneNum();//集团联系人/流量包操作员联系电话

        //OPR_INFO参数信息
        String groupId = getQueryGroupAccountGroupId();//工号归属
        String loginNo = getQueryGroupAccountLoginNo();//工号
        String opCode = getQueryGroupAccountOpCode();//操作代码
        String provinceId = getQueryGroupAccountProvinceId();//省份代码

        //HEADER参数信息
        String channelId = getQueryGroupAccountChannelId();//渠道标识
        String provinceGroup = getQueryGroupAccountProvinceGroup();//默认值 写 HLJ
        String routeKey = getQueryGroupAccountRouteKey();//路由类型
        String routeValue = entCode;//路由值,集团客户编码

        //构建BUSI_INFO
        Map<String, Object> busiMap = new HashMap<String, Object>();
        busiMap.put("OPR_ID", oprId);//操作工号
        busiMap.put("OPR_ROLE", oprRole);//工号角色（1集团联系人，2流量包操作员 3 网站实时赠送）
        busiMap.put("TRANS_ID", transId);//交易流水
        busiMap.put("UNIT_ID", unitId);//集团编码
        busiMap.put("PASS_WORD", passWord);//工号密码
        busiMap.put("PHONE_NUM", phoneNum);//集团联系人/流量包操作员联系电话 opr_role为 3 时 非必填

        //构建OPR_INFO
        Map<String, Object> oprInfoMap = new HashMap<String, Object>();
        oprInfoMap.put("GROUP_ID", groupId);
        oprInfoMap.put("LOGIN_NO", loginNo);
        oprInfoMap.put("OP_CODE", opCode);
        oprInfoMap.put("PROVINCE_ID", provinceId);

        //构建BODY
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        bodyMap.put("BUSI_INFO", busiMap);
        bodyMap.put("OPR_INFO", oprInfoMap);

        //构建ROUTING
        Map<String, Object> routingMap = new HashMap<String, Object>();
        routingMap.put("ROUTE_KEY", routeKey);
        routingMap.put("ROUTE_VALUE", routeValue);

        //构建HEADER
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("CHANNEL_ID", channelId);
        headerMap.put("PROVINCE_GROUP", provinceGroup);
        headerMap.put("ROUTING", routingMap);
        headerMap.put("TRACE_ID", getTraceID(channelId, opCode, loginNo));//交易流水

        Map<String, Object> allMap = new HashMap<String, Object>();
        allMap.put("BODY", bodyMap);
        allMap.put("HEADER", headerMap);

        //构建ROOT
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("ROOT", allMap);

        //去掉外层的[]，黑龙江BOSS约定，奇葩
        String result = JSONArray.fromObject(rootMap).toString();
        return result.substring(1, result.length() - 1);
    }

    /**
     * 
     * @Title: queryGroupAccount 
     * @Description: 黑龙江集团可赠送流量查询接口
     * @param entCode
     * @param transId
     * @return
     * @return: HLJBossEntAccountResult
     */
    public HLJBossEntAccountResult queryGroupAccount(String entCode, String transId) {
        //获取请求URL
        String url = getQueryGroupAccountURL();
        //组装请求报文
        String requestParam = assembleParaForGroup(entCode, transId);
        LOGGER.info("集团可赠送流量查询，请求url = {}, 请求参数 = {}.", url, requestParam);
        //发送充值请求
        String repsone = HttpUtil.invoke(url, requestParam);
        LOGGER.info("集团可赠送流量查询， 应答报文 = {}.", repsone);
        //解析响应报文
        return parseGroupAccountRes(repsone);
    }

    /**
     * 
     * @Title: parseGroupAccountRes 
     * @Description: 黑龙江集团可赠送流量查询接口报文解析
     * @param response
     * @return
     * @return: HLJBossEntAccountResult
     */
    public static HLJBossEntAccountResult parseGroupAccountRes(String response) {
        //约定的报文格式示例
        String defaultResponse = "{\"code\":\"0000000\",\"message\":\"Rest Request Success\",\"promptMsg\":\"User Operate Success\",\"data\":{\"GOODS_INFO_LIST\":[{\"GOODS_UNIT\":\"M\",\"COUNT_NUM\":\"42\",\"EFF_TIME\":\"2017-08-28 11:27:08\",\"EXP_TIME\":\"2017-08-29 00:00:00\",\"TOTAL_QUANTITY\":\"42\",\"GOODS_NAME\":\"1G省内流量包-日包\",\"RETAIL_PRICE\":\"7000\",\"GOODS_ID\":\"201705170002\",\"GOODS_SIZE\":\"1024\",\"EXP_TIME_RULE\":\"2000\",\"COST_PRICE\":\"7000\"},{\"GOODS_UNIT\":\"M\",\"COUNT_NUM\":\"81\",\"EFF_TIME\":\"2017-08-29 00:00:00\",\"EXP_TIME\":\"2017-09-01 00:00:00\",\"TOTAL_QUANTITY\":\"81\",\"GOODS_NAME\":\"30M国内流量包\",\"RETAIL_PRICE\":\"5000\",\"GOODS_ID\":\"201705170001\",\"GOODS_SIZE\":\"30\",\"EXP_TIME_RULE\":\"2001\",\"COST_PRICE\":\"5000\"},{\"GOODS_UNIT\":\"M\",\"COUNT_NUM\":\"26\",\"EFF_TIME\":\"2017-08-29 00:00:00\",\"EXP_TIME\":\"2018-01-01 00:00:00\",\"TOTAL_QUANTITY\":\"26\",\"GOODS_NAME\":\"12G包年\",\"RETAIL_PRICE\":\"600000\",\"GOODS_ID\":\"201705170004\",\"GOODS_SIZE\":\"12288\",\"EXP_TIME_RULE\":\"2003\",\"COST_PRICE\":\"600000\"},{\"GOODS_UNIT\":\"M\",\"COUNT_NUM\":\"45\",\"EFF_TIME\":\"2017-08-29 00:00:00\",\"EXP_TIME\":\"2018-02-01 00:00:00\",\"TOTAL_QUANTITY\":\"45\",\"GOODS_NAME\":\"6G半年包\",\"RETAIL_PRICE\":\"300000\",\"GOODS_ID\":\"201705170003\",\"GOODS_SIZE\":\"6144\",\"EXP_TIME_RULE\":\"2002\",\"COST_PRICE\":\"300000\"}]}}";
        HLJBossEntAccountResult resp = new HLJBossEntAccountResult();
        try {
            //解析外层信息
            resp = JSON.parseObject(response, HLJBossEntAccountResult.class);
            JSONObject jo = JSONObject.parseObject(response);

            //解析里层数组信息
            HLJBossEntAccountResult resp2 = JSON.parseObject(jo.getString("data"), HLJBossEntAccountResult.class);
            if (resp2 != null) {
                resp.setGoodsInfoList(resp2.getGoodsInfoList());
            }
        } catch (Exception e) {
            LOGGER.info("集团可赠送流量查询接口报文格式错误，约定的报文格式为：" + defaultResponse + "，实际报文格式：" + response);
        }
        return resp;
    }

    /**
     * 
     * @Title: getQueryGroupAccountURL 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口URL
     * @return
     * @return: String
     */
    private String getQueryGroupAccountURL() {
        String queryAddress = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_URL
                .getKey());
        return StringUtils.isNotBlank(queryAddress) ? queryAddress
                : "http://10.149.85.32:51000/esbWS/rest/bsp_accept_v1_batch_queryAvailableRev";
    }

    /**
     * 
     * @Title: getQueryGroupAccountOprId 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口之操作工号
     * @return
     * @return: String
     */
    private String getQueryGroupAccountOprId() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_OPRID.getKey());
        return StringUtils.isNotBlank(value) ? value : "newweb";
    }

    /**
     * 
     * @Title: getQueryGroupAccountOprId 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口之工号角色
     * @return
     * @return: String
     */
    private String getQueryGroupAccountOprRole() {
        String value = globalConfigService
                .get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_OPRROLE.getKey());
        return StringUtils.isNotBlank(value) ? value : "3";
    }

    /**
     * 
     * @Title: getQueryGroupAccountOprId 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口之工号密码
     * @return
     * @return: String
     */
    private String getQueryGroupAccountPassword() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_PASSWORD
                .getKey());
        return StringUtils.isNotBlank(value) ? value : "BYVJFNRZMCEFBREK";
    }

    /**
     * 
     * @Title: getQueryGroupAccountPhoneNum 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:集团联系人/流量包操作员联系电话
     * @return
     * @return: String
     */
    private String getQueryGroupAccountPhoneNum() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_PHONENUM
                .getKey());
        return StringUtils.isNotBlank(value) ? value : "13605310531";
    }

    /**
     * 
     * @Title: getQueryGroupAccountGroupId 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:工号归属
     * @return
     * @return: String
     */
    private String getQueryGroupAccountGroupId() {
        String value = globalConfigService
                .get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_GROUPID.getKey());
        return StringUtils.isNotBlank(value) ? value : "11";
    }

    /**
     * 
     * @Title: getQueryGroupAccountLoginNo 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:工号
     * @return
     * @return: String
     */
    private String getQueryGroupAccountLoginNo() {
        String value = globalConfigService
                .get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_LOGINNO.getKey());
        return StringUtils.isNotBlank(value) ? value : "hljtest";
    }

    /**
     * 
     * @Title: getQueryGroupAccountOpCode 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:操作代码
     * @return
     * @return: String
     */
    private String getQueryGroupAccountOpCode() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_OPCODE.getKey());
        return StringUtils.isNotBlank(value) ? value : "0000";
    }

    /**
     * 
     * @Title: getQueryGroupAccountProvinceId 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:省份代码
     * @return
     * @return: String
     */
    private String getQueryGroupAccountProvinceId() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_PROVINCEID
                .getKey());
        return StringUtils.isNotBlank(value) ? value : "230000";
    }

    /**
     * 
     * @Title: getQueryGroupAccountChannelId 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:渠道标识
     * @return
     * @return: String
     */
    private String getQueryGroupAccountChannelId() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_CHANNELID
                .getKey());
        return StringUtils.isNotBlank(value) ? value : "11";
    }

    /**
     * 
     * @Title: getQueryGroupAccountProvinceGroup 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:默认值 写 HLJ
     * @return
     * @return: String
     */
    private String getQueryGroupAccountProvinceGroup() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_PROVINCEGROUP
                .getKey());
        return StringUtils.isNotBlank(value) ? value : "HLJ";
    }

    /**
     * 
     * @Title: getQueryGroupAccountRouteKey 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:路由类型
     * @return
     * @return: String
     */
    private String getQueryGroupAccountRouteKey() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_ROUTEKEY
                .getKey());
        return StringUtils.isNotBlank(value) ? value : "11";
    }

    /**
     * 
     * @Title: getQueryGroupAccountRouteValue 
     * @Description: 黑龙江斯特奇集团可赠送流量查询接口-第三代BOSS接口参数:路由值
     * @return
     * @return: String
     */
    private String getQueryGroupAccountRouteValue() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_ROUTEVALUE
                .getKey());
        return StringUtils.isNotBlank(value) ? value : "4510208711";
    }

    /**
     * 
     * @Title: getChargeUrl 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数
     * @return
     * @return: String
     */
    private String getChargeUrl() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_URL.getKey());
        return StringUtils.isNotBlank(value) ? value
                : "http://10.149.85.32:51000/esbWS/rest/bsp_accept_v1_batch_realPresentlRev";//默认使用思特奇测试环境
    }

    /**
     * 
     * @Title: getChargeAccessType 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数：网站赠送
     * @return
     * @return: String
     */
    private String getChargeAccessType() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_ACCESSTYPE.getKey());
        return StringUtils.isNotBlank(value) ? value : "37";
    }

    /**
     * 
     * @Title: getChargeEffRule 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:生效规则（1001 立即生效  1003 1自然日，1002 1自然月，1010 2自然月  1011 3 自然月 1000自定义）只有生效时间有自定义，自定义时，生失效时间必传
     * @return
     * @return: String
     */
    private String getChargeEffRule() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_EFFRULE.getKey());
        return StringUtils.isNotBlank(value) ? value : "1001";//默认立即生效
    }

    /**
     * 
     * @Title: getChargeEffTime 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:生效时间（YYYY_MM_DD HH24:mi:ss）自定义生效规则，生失效时间必传。
     * @return
     * @return: String
     */
    private String getChargeEffTime() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_EFFTIME.getKey());
        return StringUtils.isNotBlank(value) ? value : "";//默认不传
    }

    /**
     * 
     * @Title: getChargeExpRule 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:失效规则( 2000 日包 2001 月包 2002 半年包 2003 年包） 可赠送流量查询返回的EXP_TIME_RULE
     * @return
     * @return: String
     */
    private String getChargeExpRule() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_EXPRULE.getKey());
        return StringUtils.isNotBlank(value) ? value : "2001";//默认月包
    }

    /**
     * 
     * @Title: getChargeExpTime 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:失效时间（YYYY_MM_DD HH24:mi:ss）自定义生效规则，生失效时间必传。
     * @return
     * @return: String
     */
    private String getChargeExpTime() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_EXPTIME.getKey());
        return StringUtils.isNotBlank(value) ? value : "";//默认为空
    }

    /**
     * 
     * @Title: getChargePayMode 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:付费类型（0:不收费），赠送送0
     * @return
     * @return: String
     */
    private String getChargePayMode() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_PAYMODE.getKey());
        return StringUtils.isNotBlank(value) ? value : "0";
    }

    /**
     * 
     * @Title: getChargeSaleNum 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数
     * @return
     * @return: String
     */
    private String getChargeSaleNum() {//赠送/零售数量
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_SALENUM.getKey());
        return StringUtils.isNotBlank(value) ? value : "1";//默认赠送一个
    }

    /**
     * 
     * @Title: getChargeSalePrice 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:赠送/零售价格 （单位li，赠送时传 0）
     * @return
     * @return: String
     */
    private String getChargeSalePrice() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_SALEPRICE.getKey());
        return StringUtils.isNotBlank(value) ? value : "0";
    }

    /**
     * 
     * @Title: getChargeSaleSize 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:赠送流量大小（散装存实际大小，标准存商品大小，单位M），可赠送流量查询返回的GOODS_SIZE
     * @return
     * @return: String
     */
    private String getChargeSaleSize() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_SALESIZE.getKey());
        return StringUtils.isNotBlank(value) ? value : "";//默认为空
    }

    /**
     * 
     * @Title: getChargeOperCode 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:操作类型 传4  （1集团行业部流量赠送2 代理商批量赠送 5行业应用流量包实名制赠送 4 实时赠送 
     * @return
     * @return: String
     */
    private String getChargeOperCode() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_OPERCODE.getKey());
        return StringUtils.isNotBlank(value) ? value : "4";
    }

    /**
     * 
     * @Title: getChargeOprId 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数：操作工号 实时赠送 传 llzcpt
     * @return
     * @return: String
     */
    private String getChargeOprId() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_OPRID.getKey());
        return StringUtils.isNotBlank(value) ? value : "llzcpt";
    }

    /**
     * 
     * @Title: getChargeOprRole 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:工号角色（3 网站实时赠送 ）
     * @return
     * @return: String
     */
    private String getChargeOprRole() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_OPRROLE.getKey());
        return StringUtils.isNotBlank(value) ? value : "3";//默认网站实时赠送
    }

    /**
     * 
     * @Title: getChargePassword 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数：工号密码 BYVJFNRZMCEFBREK
     * @return
     * @return: String
     */
    private String getChargePassword() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_PASSWORD.getKey());
        return StringUtils.isNotBlank(value) ? value : "BYVJFNRZMCEFBREK";
    }

    /**
     * 
     * @Title: getChargePhoneNum 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数，集团联系人/流量包操作员手机号码
     * @return
     * @return: String
     */
    private String getChargePhoneNum() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_PHONENUM.getKey());
        return StringUtils.isNotBlank(value) ? value : "13504831117";
    }

    /**
     * 
     * @Title: getChargeUnitIdNo 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:OPER_CODE 为1时，此值传集团用户id
     * @return
     * @return: String
     */
    private String getChargeUnitIdNo() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_UNITIDNO.getKey());
        return StringUtils.isNotBlank(value) ? value : "";
    }

    /**
     * 
     * @Title: getChargeRepeateFlag 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数：是否允许重复赠送标识0-允许重复；1-不允许重复
     * @return
     * @return: String
     */
    private String getChargeRepeateFlag() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_REPEATEFLAG.getKey());
        return StringUtils.isNotBlank(value) ? value : "0";//默认运行重复赠送
    }

    /**
     * 
     * @Title: getChargeGroupId 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数：工号归属
     * @return
     * @return: String
     */
    private String getChargeGroupId() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_GROUPID.getKey());
        return StringUtils.isNotBlank(value) ? value : "4510208711";
    }

    /**
     * 
     * @Title: getChargeLoginNo 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数：工号 llzcpt
     * @return
     * @return: String
     */
    private String getChargeLoginNo() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_LOGINNO.getKey());
        return StringUtils.isNotBlank(value) ? value : "llzcpt";
    }

    /**
     * 
     * @Title: getChargeOpCode 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数：操作代码
     * @return
     * @return: String
     */
    private String getChargeOpCode() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_OPCODE.getKey());
        return StringUtils.isNotBlank(value) ? value : "10001";
    }

    /**
     * 
     * @Title: getChargeProviceId 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数：省份代码 黑龙江填写：230000
     * @return
     * @return: String
     */
    private String getChargeProviceId() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_PROVINCEID.getKey());
        return StringUtils.isNotBlank(value) ? value : "230000";
    }

    /**
     * 
     * @Title: getChargeProviceGroup 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数
     * @return
     * @return: String
     */
    private String getChargeProviceGroup() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_PROVICEGROUP.getKey());
        return StringUtils.isNotBlank(value) ? value : "HLJ";
    }

    /**
     * 
     * @Title: getChargeChannelId 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数:渠道标识，默认使用37
     * @return
     * @return: String
     */
    private String getChargeChannelId() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_CHANNELID.getKey());
        return StringUtils.isNotBlank(value) ? value : "37";//渠道标识 默认使用37
    }

    /**
     * 
     * @Title: getChargeRouteKey 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数： 路由类型 默认使用11
     * @return
     * @return: String
     */
    private String getChargeRouteKey() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_ROUTEKEY.getKey());
        return StringUtils.isNotBlank(value) ? value : "11";// 路由类型 默认使用11
    }

    /**
     * 
     * @Title: getChargeRouteValue 
     * @Description: 黑龙江思特奇行业应用流量包实时赠送接口-第三代BOSS接口参数
     * @return
     * @return: String
     */
    private String getChargeRouteValue() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_SITECH_CHARGE_ROUTEVALUE.getKey());
        return StringUtils.isNotBlank(value) ? value : "4510208711";
    }

    /**
     * 
     * @Title: useBossBatchCharge 
     * @Description: 是否使用BOSS提供的批量充值接口
     * @return
     * @return: boolean
     */
    private boolean useBossBatchCharge() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_USE_BOSS_CHARGE_SINGLE_OR_BATCH.getKey());
        return !(StringUtils.isNotBlank(value) && "SINGLE".equalsIgnoreCase(value));
    }

    /**
     * 获取统一流水号
     *
     * @param channelId
     *            渠道编码
     * @param opCode
     *            操作编码
     * @param loginNo
     *            工号
     * @return 统一流水号(Trace_id)
     */
    public static String getTraceID(String channelId, String opCode, String loginNo) {

        String SpliteString = "*";
        String result = "";
        if (channelId.isEmpty()) {
            result = "channelCode is empty";
            return result;
        }

        if (opCode.isEmpty()) {
            result = "opCode is empty";
            return result;
        }

        if (loginNo.isEmpty()) {
            result = "jobCode is empty";
            return result;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String currTime = simpleDateFormat.format(new Date());
        String randomString = String.valueOf((int) (Math.random() * 1000000));
        result = channelId + SpliteString + currTime + SpliteString + opCode + SpliteString + loginNo + SpliteString
                + randomString;
        return result;
    }

}

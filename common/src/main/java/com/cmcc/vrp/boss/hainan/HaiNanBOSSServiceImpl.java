package com.cmcc.vrp.boss.hainan;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.rpc.ServiceException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.hainan.model.HNBOSSCharge;
import com.cmcc.vrp.boss.hainan.model.HNBOSSChargeResponse;
import com.cmcc.vrp.boss.hainan.model.HNBOSSMDRCCharge;
import com.cmcc.vrp.boss.hainan.model.HNBOSSUserInfo;
import com.cmcc.vrp.boss.hainan.model.HNBOSSUserInfoCheckReponse;
import com.cmcc.vrp.boss.hainan.model.HNMDRCChargeReponse;
import com.cmcc.vrp.boss.hainan.uipsoap.client.BusinessCallRequest;
import com.cmcc.vrp.boss.hainan.uipsoap.client.UIP_PortType;
import com.cmcc.vrp.boss.hainan.uipsoap.client.UIP_ServiceLocator;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;

/**
 * @ClassName: HNBOSSServiceImpl
 * @Description: 海南BOSS接口实现类
 * @author: Rowe
 * @date: 2016年5月4日 上午11:19:21
 */
@Component("haiNanBossService")
public class HaiNanBOSSServiceImpl implements BossService {

    private static final Logger logger = LoggerFactory.getLogger(HaiNanBOSSServiceImpl.class);

    //md5sum("hainanbossservice")    
    private final static String HAINAN_BOSS_FINGERPRINT = "ebb22fbcb9928f128fa480c3d90f7458";
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    private SerialNumService serialNumService;
    @Value("#{settings['boss.seqid']}")
    private String SEQID;
    @Value("#{settings['boss.despwd']}")
    private String DESPWD;//seqid + srcpwd MD5后结果
    @Value("#{settings['boss.communicate']}")
    private String COMMUNICATE; //协议方式
    @Value("#{settings['boss.transfer']}")
    private String TRANSFER; //数据包格式
    @Value("#{settings['boss.contactid']}")
    private String CONTACTID; //接触流水
    @Value("#{settings['boss.bussid']}")
    private String BUSSID; //业务流水
    @Value("#{settings['boss.orgchannelid']}")
    private String ORGCHANNELID; //发起方渠道标识
    @Value("#{settings['boss.homechannelid']}")
    private String HOMECHANNELID; //落地方渠道标识
    @Value("#{settings['boss.actioncode']}")
    private String ACTIONCODE; //交易动作代码0：请求，1：应答
    @Value("#{settings['boss.testflag']}")
    private String TESTFLAG; //测试标记 0-测试交易 1-正式交易
    @Value("#{settings['boss.province.code']}")
    private String PROVINCE_CODE; //省别编码
    @Value("#{settings['boss.in.mode.code']}")
    private String IN_MODE_CODE; //接入方式编码
    @Value("#{settings['boss.trade.eparchy.code']}")
    private String TRADE_EPARCHY_CODE; //员工所在地州编码，对于省集中的省份，填写“INTF”
    @Value("#{settings['boss.route.eparchy.code']}")
    private String ROUTE_EPARCHY_CODE; //路由地州,可不填
    @Value("#{settings['boss.trade.city.code']}")
    private String TRADE_CITY_CODE; //员工所在的业务区编码
    @Value("#{settings['boss.trade.depart.id']}")
    private String TRADE_DEPART_ID; //员工所在的部门
    @Value("#{settings['boss.trade.staff.id']}")
    private String TRADE_STAFF_ID; //员工标识
    @Value("#{settings['boss.staff.passwd']}")
    private String STAFF_PASSWD; //员工密码
    @Value("#{settings['boss.trade.depart.passwd']}")
    private String TRADE_DEPART_PASSWD; //渠道接入密码 ，此密码由BOSS制定，外围接入渠道必填
    @Value("#{settings['boss.channel.trade.id']}")
    private String CHANNEL_TRADE_ID; //流水号（唯一标识本次请求)
    @Value("#{settings['TRADE_TERMINAL_ID']}")
    private String TRADE_TERMINAL_ID; //终端接入IP
    @Value("#{settings['boss.trade.route.type']}")
    private String TRADE_ROUTE_TYPE; //交易路由类型，必须填写
    @Value("#{settings['boss.trade.route.value']}")
    private String TRADE_ROUTE_VALUE; //交易路由值，必须填写
    @Value("#{settings['boss.biz.code']}")
    private String BIZ_CODE; //业务代码，UIP接入必填
    @Value("#{settings['boss.trans.code']}")
    private String TRANS_CODE; //交易代码，UIP接入必填
    @Value("#{settings['boss.biz.code.mdrc']}")
    private String BOSS_BIZ_CODE_MDRC; //业务代码，UIP接入必填
    @Value("#{settings['boss.trans.code.mdrc']}")
    private String BOSS_TRANS_CODE_MDRC; //交易代码，UIP接入必填
    @Value("#{settings['boss.trade.brand.code']}")
    private String TRADE_BRAND_CODE;
    @Value("#{settings['boss.url']}")
    private String BOSS_URL; //BOSS接口地址

    /**
     * @param xml
     * @return
     * @Title: parseMdrcChargeResponse
     * @Description: 解析流量卡充值返回的报文
     * @return: HNBOSSChargeResponse
     */
    public static HNMDRCChargeReponse parseMdrcChargeResponse(String xml) {
        HNMDRCChargeReponse mdrcChargeResponse = new HNMDRCChargeReponse();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement().element("Body");
            String content = root.element("SVC_CONTENT").getStringValue();
            String X_RESULTCODE = StringUtils.substringBetween(content, "X_RESULTCODE=[\"", "\"]");//充值结果代码
            String X_RESULTINFO = StringUtils.substringBetween(content, "X_RESULTINFO=[\"", "\"]");//充值结果代码说明
            mdrcChargeResponse.setRes_code(X_RESULTCODE);
            mdrcChargeResponse.setRes_desc(X_RESULTINFO);

            String X_RECORDNUM = StringUtils.substringBetween(content, "X_RECORDNUM=[\"", "\"]");//操作数量
            String TRADE_ID = StringUtils.substringBetween(content, "TRADE_ID=[\"", "\"]");//响应序列号

            //封装充值结果
            HNBOSSMDRCCharge mdrcCharge = new HNBOSSMDRCCharge();
            mdrcCharge.setRecordNum(X_RECORDNUM);
            mdrcCharge.setTradeId(TRADE_ID);

            mdrcChargeResponse.setMdrcCharge(mdrcCharge);

            return mdrcChargeResponse;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mdrcChargeResponse;
    }

    public static void main(String[] args) throws XMLStreamException, IOException, ServiceException {
//		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");
//		HaiNanBOSSServiceImpl bossService = (HaiNanBOSSServiceImpl)context.getBean("bossService");
//		BossOperationResult chargeResult = bossService.charge(95L,366L,"14708939563",new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
//		System.out.println("充值结果：" + chargeResult.isSuccess());
//		BossOperationResult userInfoCheckResult = bossService.userInfoCheck("13807657474");
//		System.out.println("用户资料校验结果：" + userInfoCheckResult.isSuccess());
//		new HaiNanBOSSServiceImpl().chargeTest();
//		new HaiNanBOSSServiceImpl().userInfoCheck("14708939563");


        //        String response = "<?xml version=\"1.0\"  encoding=\"UTF-8\"?><SvcInfo><Header><System><COMMUNICATE value=\"02\" index=\"0\" /><TRANSFER value=\"01\" index=\"0\" /><ORGCHANNELID value=\"A002\" index=\"0\" /><HOMECHANNELID /><CONTACTID  value=\"003200903120000\" index=\"0\" /></System><Inparam><CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\" /></Inparam><Outparam><ChargeResultCode value=\"0\" /><RESULT_INFO value=\"OK!\" /></Outparam><TESTFLAG value=\"0\" index=\"0\" /><ACTIONCODE value=\"1\" index=\"0\" /></Header><Body><SVC_CONTENT>{X_RESULTINFO=[\"接 口参数检查: 输入参数[GRP_ID]不存在或者参数值为空\"], X_RESULTSIZE=[\"0\"], X_RSPCOD E=[\"-1\"], IN_MODE_CODE=[\"5\"], X_RSPDESC=[\"接口参数检查: 输入参数[GRP_ID]不存在或 者参数值为空\"], X_RSPTYPE=[\"2\"], X_NODENAME=[\"app-node01-srv02\"], X_RESULTCOUNT= [\"0\"], X_RESULTCODE=[\"-1\"], X_RECORDNUM=[\"0\"]}</SVC_CONTENT></Body></SvcInfo>";
        //        HNMDRCChargeReponse hnBossChargeResponse = parseMdrcChargeResponse(response);
//        System.out.println("ok");
    }

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {

        HNBOSSChargeResponse hnBossChargeResponse = new HNBOSSChargeResponse();
        //1.参数校验
        if (entId == null || splPid == null || StringUtils.isBlank(mobile) || StringUtils.isBlank(serialNum)) {
            logger.info("海南BOSS端集团订购流量包接口调用失败：参数不完整！");
            hnBossChargeResponse.setDesc("参数不完整");
            return hnBossChargeResponse;

        }

        //2.获取参数
        //获取企业信息
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null || StringUtils.isBlank(enterprise.getCode())) {
            logger.info("海南BOSS端集团订购流量包接口调用失败：企业Id =" + entId + "不存在！");
            hnBossChargeResponse.setDesc("企业Id =" + entId + "不存在！");
            return hnBossChargeResponse;

        }

        String grpId = enterprise.getCode();//集团编码

        //校验手机号码
        BossOperationResult result = userInfoCheck(mobile);
        if (!result.isSuccess()) {
            logger.info("海南BOSS端集团订购流量包接口调用失败：手机号码校验失败【" + result.getResultDesc() + "】");
            hnBossChargeResponse.setDesc(result.getResultDesc());
            return hnBossChargeResponse;
        }

        //获取产品信息
        SupplierProduct supplierProduct = supplierProductService.selectByPrimaryKey(splPid);
        if (supplierProduct == null || StringUtils.isBlank(supplierProduct.getFeature())) {
            logger.info("海南BOSS端集团订购流量包接口调用失败：平台尚未存储BOSS端产品信息！");
            hnBossChargeResponse.setDesc("平台尚未存储BOSS端产品信息！");
            return hnBossChargeResponse;
        }
        JSONObject proJsonObject = (JSONObject) JSONObject.parse(supplierProduct.getFeature());
        String pakMoney = (String) proJsonObject.get("PAK_MONEY");
        String pakGPRS = (String) proJsonObject.get("PAK_GPRS");
        if (StringUtils.isBlank(pakMoney) || StringUtils.isBlank(pakGPRS)) {
            logger.info("海南BOSS端集团订购流量包接口调用失败：平台存储的BOSS端产品信息格式不正确！");
            hnBossChargeResponse.setDesc("平台存储的BOSS端产品信息格式不正确");
            return hnBossChargeResponse;

        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = new Date();//生效日期

        //计算失效期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Date entDate = DateUtil.getEndTimeOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);//失效日期，当月月底
        String pakStartDate = sdf1.format(startDate);//生效日期,格式：yyyy-MM-dd
        String pakEndDate = sdf2.format(entDate);//失效日期,格式:yyyy-MM-dd HH:mm:ss

        //3.发送请求
        //此处订单号orderId和流水号serialNum相同
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("hainan", 25);
        String response = charge(grpId, bossReqNum, bossReqNum, mobile, "1", pakMoney, pakGPRS, pakStartDate, pakEndDate);

        //4.解析报文
        HNBOSSChargeResponse chargeResponse = parseChargeResponse(response);
        String bossRespNum = chargeResponse.getCharge() == null ? SerialNumGenerator.buildNullBossRespNum("hainan") : chargeResponse.getCharge().getTradeId();
        if (!updateRecord(serialNum, bossRespNum, bossReqNum)) {
            logger.info("海南充值更新流水失败,serialNum:{}.bossRespNum:{}.bossReqNum:{}", serialNum, bossRespNum, bossReqNum);
        }
        return chargeResponse;
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
        //md5sum("hainanbossservice")
        return HAINAN_BOSS_FINGERPRINT;
    }

    /**
     * @param grpId：集团编码
     * @param orderId：订单编码
     * @param serialNumber：平台流水号
     * @param mobile：手机号码
     * @param pakNum：流量包个数
     * @param pakMoney：每个流量包的费用
     * @param pakGPRS：每个流量包的流量
     * @param pakStartDate：流量包生效时间
     * @param pakEndDate：流量包失效时间
     * @return
     * @Title: charge
     * @Description: 集团流量包赠送
     * @return: String
     */
    private String charge(String grpId, String orderId, String serialNumber, String mobile, 
            String pakNum, String pakMoney, String pakGPRS, String pakStartDate, String pakEndDate) {
        logger.info("开始调用海南BOSS端集团流量包赠送接口");
        //1.校验参数
        if (StringUtils.isBlank(grpId)
                || StringUtils.isBlank(orderId)
                || StringUtils.isBlank(serialNumber)
                || StringUtils.isBlank(mobile)
                || StringUtils.isBlank(pakNum)
                || StringUtils.isBlank(pakMoney)
                || StringUtils.isBlank(pakGPRS)
                || StringUtils.isBlank(pakStartDate)
                || StringUtils.isBlank(pakEndDate)) {
            logger.info("海南BOSS端集团流量包赠送接口调用失败：参数不完整！");
            return null;
        }

        //2.组织参数
        String xml = assembleChargeRequestParam(grpId, orderId, serialNumber, mobile, pakNum, pakMoney, pakGPRS, pakStartDate, pakEndDate);

        //3.发送请求
        return sendRequest(BOSS_URL, xml);
    }

    /**
     * @param mobile
     * @return
     * @Title: assembleCheckUserInfoParam
     * @Description: 组织用户资料校验接口参数
     * @return: String
     */
    private String assembleCheckUserInfoParam(String mobile) {

        //配置参数
        String bizCode = "BCS4165";
        String transCode = "THQ3083";

        //组织参数
        Map<String, String> data = Collections.synchronizedMap(new HashMap<String, String>());
        data.put("SERIAL_NUMBER", "[\"" + mobile + "\"]");
        String xml = "<SvcInfo>"
                + "<Header>"
                + "<Security>"
                + "<SEQID value=\"E0022014110411444100000000\" index=\"0\"/>"
                + "<DESPWD value=\"7d05bd542beaaecf0eb40cbf15d6bdcf\" index=\"0\"/>"
                + "</Security>"
                + "<System>"
                + "<COMMUNICATE value=\"02\" index=\"0\"/>"
                + "<TRANSFER value=\"01\" index=\"0\"/>"
                + "<CONTACTID value=\"003200903120000\" index=\"0\"/>"
                + "<BUSSID value=\"E002A002201411041144410000000000\" index=\"0\"/>"
                + "<ORGCHANNELID value=\"E002\" index=\"0\"/>"
                + "<HOMECHANNELID value=\"A002\" index=\"0\"/>"
                + "</System>"
                + "<ACTIONCODE value=\"0\" index=\"0\"/>"
                + "<TESTFLAG value=\"0\" index=\"0\"/>"
                + "<Inparam>"
                + "<PROVINCE_CODE value=\"HAIN\" index=\"0\"/>"
                + "<IN_MODE_CODE value=\"5\" index=\"0\"/>"
                + "<TRADE_EPARCHY_CODE value=\"0898\" index=\"0\"/>"
                + "<ROUTE_EPARCHY_CODE value=\"0898\" index=\"0\"/>"
                + "<TRADE_CITY_CODE value=\"HNSJ\" index=\"0\"/>"
                + "<TRADE_DEPART_ID value=\"00316\" index=\"0\"/>"
                + "<TRADE_STAFF_ID value=\"ITFSM000\" index=\"0\"/>"
                + "<STAFF_PASSWD value=\"123456\" index=\"0\"/>"
                + "<TRADE_DEPART_PASSWD index=\"0\" value=\"0123456789\" />"
                + "<CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\"/>"
                + "<TRADE_TERMINAL_ID value=\"192.168.1.163\" index=\"0\"/>"
                + "<TRADE_ROUTE_TYPE value=\"00\" index=\"0\"/>"
                + "<TRADE_ROUTE_VALUE value=\"0898\" index=\"0\"/>"
                + "<BIZ_CODE value=\"" + bizCode + "\" index=\"0\"/>"
                + "<TRANS_CODE value=\"" + transCode + "\" index=\"0\"/>"
                + "<TRADE_BRAND_CODE index=\"0\" value=\"G001\" />"
                + "</Inparam>"
                + "<InFields>"
                + "<InField>"
                + "<INFO_CODE/>"
                + "<INFO_VALUE/>"
                + "</InField>"
                + "</InFields>"
                + "</Header>"
                + "<Body>"
                + "<SVC_CONTENT>"
                + data
                + "</SVC_CONTENT>"
                + "</Body>"
                + "</SvcInfo>";
        return xml;
    }

    /**
     * @param mobile
     * @return
     * @Title: userInfoCheck
     * @Description: 用户资料校验接口
     * @return: BossOperationResult
     */
    public BossOperationResult userInfoCheck(String mobile) {
        logger.info("开始调用海南BOSS端用户资料校验接口，参数为：" + mobile);
        //1.校验参数
        if (StringUtils.isBlank(mobile)) {
            logger.info("海南BOSS端用户资料校验接口调用失败：参数不完整！");
            return null;
        }
        //2.组织参数
        String xml = assembleCheckUserInfoParam(mobile);

        //3.发送请求
        String response = sendRequest(BOSS_URL, xml);

        //4解析报文
        return parseCheckUserInfoResponse(response);
    }

    private HNBOSSUserInfoCheckReponse parseCheckUserInfoResponse(String response) {
        HNBOSSUserInfoCheckReponse userInfoCheckReponse = new HNBOSSUserInfoCheckReponse();
        try {

            Document document = DocumentHelper.parseText(response);
            Element root = document.getRootElement().element("Body");
            String content = root.element("SVC_CONTENT").getStringValue();
            String SERIAL_NUMBER = StringUtils.substringBetween(content, "SERIAL_NUMBER=[\"", "\"]");//手机号码
            String USER_STATUS = StringUtils.substringBetween(content, "USER_STATUS=[\"", "\"]");//状态
            userInfoCheckReponse.setCode(USER_STATUS);
            userInfoCheckReponse.setDesc(HNBOSSUserInfoCheckReponse.UserStatus.toMap().get(USER_STATUS));

            HNBOSSUserInfo userInfo = new HNBOSSUserInfo();
            userInfo.setMobile(SERIAL_NUMBER);
            userInfo.setStatus(USER_STATUS);

            userInfoCheckReponse.setUserInfo(userInfo);

            return userInfoCheckReponse;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return userInfoCheckReponse;
    }

    /**
     * @return
     * @Title: assembleChargeHeader
     * @Description: 组织Header部分数据
     * @return: String
     */
    private String assembleChargeHeader(String bizCode, String transCode) {
        //1.构建Security节点
        String seqid = "<SEQID index=\"0\" value=\"" + SEQID + "\"/>";
        String despwd = "<DESPWD index=\"0\" value=\"" + DESPWD + "\"/>";
        String security = "<Security>" + seqid + despwd + "</Security>";

        //2.构建System节点
        String communicate = "<COMMUNICATE index=\"0\" value=\"" + COMMUNICATE + "\"/>";
        String transfer = "<TRANSFER index=\"0\" value=\"" + TRANSFER + "\"/>";
        String contactid = "<CONTACTID index=\"0\" value=\"" + CONTACTID + "\"/>";
        String bussid = "<BUSSID index=\"0\" value=\"" + BUSSID + "\"/>";
        String orgchannelid = "<ORGCHANNELID index=\"0\" value=\"" + ORGCHANNELID + "\"/>";
        String homechannelid = "<HOMECHANNELID index=\"0\" value=\"" + HOMECHANNELID + "\"/>";
        String system = "<System>" + communicate + transfer + contactid + bussid + orgchannelid + homechannelid + "</System>";

        //3.构建ACTIONCODE节点
        String actioncode = "<ACTIONCODE index=\"0\" value=\"" + ACTIONCODE + "\"/>";

        //4.构建TESTFLAG节点
        String testflag = "<TESTFLAG index=\"0\" value=\"" + TESTFLAG + "\"/>";

        //5.构建Inparam节点
        String provinceCode = "<PROVINCE_CODE index=\"0\" value=\"" + PROVINCE_CODE + "\"/>";
        String inModeCode = "<IN_MODE_CODE index=\"0\" value=\"" + IN_MODE_CODE + "\"/>";
        String tradeEparchyCode = "<TRADE_EPARCHY_CODE index=\"0\" value=\"" + TRADE_EPARCHY_CODE + "\"/>";
        String routeEparchyCode = "<ROUTE_EPARCHY_CODE index=\"0\" value=\"" + ROUTE_EPARCHY_CODE + "\"/>";
        String tradeCityCode = "<TRADE_CITY_CODE index=\"0\" value=\"" + TRADE_CITY_CODE + "\"/>";
        String tradeDepartId = "<TRADE_DEPART_ID index=\"0\" value=\"" + TRADE_DEPART_ID + "\"/>";
        String tradeStaffId = "<TRADE_STAFF_ID index=\"0\" value=\"" + TRADE_STAFF_ID + "\"/>";
        String staffPasswd = "<STAFF_PASSWD index=\"0\" value=\"" + STAFF_PASSWD + "\"/>";
        String tradeDepartPasswd = "<TRADE_DEPART_PASSWD index=\"0\" value=\"" + TRADE_DEPART_PASSWD + "\"/>";
        String channelTradeId = "<CHANNEL_TRADE_ID index=\"0\" value=\"" + CHANNEL_TRADE_ID + "\"/>";
        String tradeTerminalId = "<TRADE_TERMINAL_ID index=\"0\" value=\"" + TRADE_TERMINAL_ID + "\"/>";
        String tradeRouteType = "<TRADE_ROUTE_TYPE index=\"0\" value=\"" + TRADE_ROUTE_TYPE + "\"/>";
        String tradeRouteValue = "<TRADE_ROUTE_VALUE index=\"0\" value=\"" + TRADE_ROUTE_VALUE + "\"/>";
        String bizCodeEle = "<BIZ_CODE index=\"0\" value=\"" + bizCode + "\"/>";
        String transCodeEle = "<TRANS_CODE index=\"0\" value=\"" + transCode + "\"/>";
        String tradeBrandCode = "<TRADE_BRAND_CODE index=\"0\" value=\"" + TRADE_BRAND_CODE + "\"/>";
        String inparam = "<Inparam>" + provinceCode + inModeCode + tradeEparchyCode + routeEparchyCode +
                tradeCityCode + tradeDepartId + tradeStaffId + staffPasswd + tradeDepartPasswd + channelTradeId +
                tradeTerminalId + tradeRouteType + tradeRouteValue + bizCodeEle + transCodeEle + tradeBrandCode + "</Inparam>";

        //6.构建InFields节点
        String infoCode = "<INFO_CODE/>";
        String infoValue = "<INFO_VALUE/>";
        String inFields = "<InFields><InField>" + infoCode + infoValue + "</InField></InFields>";

        //7.构建Header节点
        String header = "<Header>" + security + system + actioncode + testflag + inparam + inFields + "</Header>";

        return header;
    }

    /**
     * @param grpId:集团编码
     * @param orderId：订单编码
     * @param transId：平台流水号
     * @param mobile：手机号码
     * @param pakNum：流量包个数
     * @param pakMoney：每个流量包的费用，单位分，无汉字
     * @param pakGPRS：每个流量包的流量，单位M，无汉字
     * @param pakStartDate：流量包生效时间
     * @param pakEndDate：流量包失效时间
     * @return
     * @Title: assembleChargeBody
     * @Description: 组织BODY部分数据
     * @return: String
     */
    private String assembleChargeBody(String grpId, String orderId, String transId, 
            String mobile, String pakNum, String pakMoney, String pakGPRS, String pakStartDate, String pakEndDate) {
        return "<Body><SVC_CONTENT>{GRP_ID=[\"" + grpId
                + "\"], ORDER_ID=[\"" + orderId + "\"], ITEM=[{TRANS_ID=[\"" + transId
                + "\"], SERIAL_NUMBER=[\"" + mobile + "\"], PAK_NUM=[\"" + pakNum + "\"], PAK_MONEY=[\"" + pakMoney + "\"], PAK_GPRS=[\"" + pakGPRS
                + "\"], PAK_START_DTAE=[\"" + pakStartDate + "\"], PAK_END_DTAE=[\"" + pakEndDate
                + "\"]}]}</SVC_CONTENT></Body>";
    }

    /**
     * @param grpId：集团编码
     * @param orderId：订单编码
     * @param serialNumber：平台流水号
     * @param mobile:手机号码
     * @param pakNum：流量包个数
     * @param pakMoney：每个流量包的费用
     * @param pakGPRS：每个流量包的流量
     * @param pakStartDate：流量包生效时间
     * @param pakEndDate：流量包失效时间
     * @return
     * @Title: assembleRequestParam
     * @Description: 组织请求充值请求参数
     * @return: String
     */
    private String assembleChargeRequestParam(String grpId, String orderId, 
            String serialNumber, String mobile, String pakNum, String pakMoney, String pakGPRS, String pakStartDate, String pakEndDate) {
        String header = assembleChargeHeader(BIZ_CODE, TRANS_CODE);
        String body = assembleChargeBody(grpId, orderId, serialNumber, mobile, pakNum, pakMoney, pakGPRS, pakStartDate, pakEndDate);
        return "<SvcInfo>" + header + body + "</SvcInfo>";
    }

    /**
     * @param cardNumber
     * @param mobile
     * @return
     * @Title: assembleMdrcChargeRequestParm
     * @Description: 封装流量卡充值请求报文体
     * @return: String
     */
    private String assembleMdrcChargeRequestParm(String cardNumber, String mobile) {
        String header = assembleChargeHeader(BOSS_BIZ_CODE_MDRC, BOSS_TRANS_CODE_MDRC);
        String body = "<Body><SVC_CONTENT>{SERIAL_NUMBER=[\"" + mobile + "\"], FLOW_CARD=[\"" + cardNumber + "\"]}</SVC_CONTENT></Body>";
        return "<SvcInfo>" + header + body + "</SvcInfo>";
    }

    /**
     * @param url：请求地址
     * @param xml：请求参数
     * @return
     * @throws ServiceException
     * @throws MalformedURLException
     * @throws RemoteException
     * @Title: sendRequest
     * @Description: TODO
     * @return: String
     */
    private String sendRequest(String url, String xml) {
        try {
            UIP_PortType service = new UIP_ServiceLocator().getUIPSOAP(new java.net.URL(url));
            BusinessCallRequest request = new BusinessCallRequest(xml);
            String repsone = service.businessCall(request);
            logger.info("BOSS请求地址url：" + url + "\n请求参数xml:" + xml + "\n返回报文:" + repsone);
            return repsone;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     */
    public String chargeTest() {
        String url = "http://hiboss3.4ggogo.com/uip_inws/services/UIPSOAP";
//		String url = "http://hiboss3.4ggogo.com/uip_inws/services/UIPSOAP";
        String bizCode = "BCS4169";
        String transCode = "THT3087";
//		String data = "{SERIAL_NUMBER=[\"13807657474\"], GRP_ID=[\"8989859421\"], PAK_NUM=[\"1\"], ORDER_ID=[\"1116042608492397\"], " +
//        				"ITEM=[{TRADE_ID=[\"1116042644181421\"], SERIAL_NUMBER=[\"13807657474\"], PAK_MONEY=[\"50000\"], PAK_GPRS=[\"10240\"], " +
//        						"PAK_OUT=[\"1\"], PAK_START_DTAE=[\"2016-04-20\"], PAK_END_DTAE=[\"2016-06-19 23:59:59\"]}]}";

        String data = "{VALUE_CARD_NO=[\"14708939563\"], SERIAL_NUMBER=[\"8989859421\"]}";

        String xml = "<![CDATA[<SvcInfo>"
                + "<Header>"
                + "<Security>"
                + "<SEQID value=\"E0022014110411444100000000\" index=\"0\"/>"
                + "<DESPWD value=\"7d05bd542beaaecf0eb40cbf15d6bdcf\" index=\"0\"/>"
                + "</Security>"
                + "<System>"
                + "<COMMUNICATE value=\"02\" index=\"0\"/>"
                + "<TRANSFER value=\"01\" index=\"0\"/>"
                + "<CONTACTID value=\"003200903120000\" index=\"0\"/>"
                + "<BUSSID value=\"E002A002201411041144410000000000\" index=\"0\"/>"
                + "<ORGCHANNELID value=\"E002\" index=\"0\"/>"
                + "<HOMECHANNELID value=\"A002\" index=\"0\"/>"
                + "</System>"
                + "<ACTIONCODE value=\"0\" index=\"0\"/>"
                + "<TESTFLAG value=\"0\" index=\"0\"/>"
                + "<Inparam>"
                + "<PROVINCE_CODE value=\"HAIN\" index=\"0\"/>"
                + "<IN_MODE_CODE value=\"5\" index=\"0\"/>"
                + "<TRADE_EPARCHY_CODE value=\"0898\" index=\"0\"/>"
                + "<ROUTE_EPARCHY_CODE value=\"0898\" index=\"0\"/>"
                + "<TRADE_CITY_CODE value=\"HNSJ\" index=\"0\"/>"
                + "<TRADE_DEPART_ID value=\"00316\" index=\"0\"/>"
                + "<TRADE_STAFF_ID value=\"ITFSM000\" index=\"0\"/>"
                + "<STAFF_PASSWD value=\"123456\" index=\"0\"/>"
                + "<TRADE_DEPART_PASSWD index=\"0\" value=\"0123456789\" />"
                + "<CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\"/>"
                + "<TRADE_TERMINAL_ID value=\"192.168.1.163\" index=\"0\"/>"
                + "<TRADE_ROUTE_TYPE value=\"00\" index=\"0\"/>"
                + "<TRADE_ROUTE_VALUE value=\"0898\" index=\"0\"/>"
                + "<BIZ_CODE value=\"" + bizCode + "\" index=\"0\"/>"
                + "<TRANS_CODE value=\"" + transCode + "\" index=\"0\"/>"
                + "<TRADE_BRAND_CODE index=\"0\" value=\"G001\" />"
                + "</Inparam>"
                + "<InFields>"
                + "<InField>"
                + "<INFO_CODE/>"
                + "<INFO_VALUE/>"
                + "</InField>"
                + "</InFields>"
                + "</Header>"
                + "<Body>"
                + "<SVC_CONTENT>"
                + data
                + "</SVC_CONTENT>"
                + "</Body>"
                + "</SvcInfo>]]>";
        String xml2 = "<SvcInfo><Header><Security><SEQID index=\"0\" value=\"E0022014110411444100000000\"/>" 
                    + "<DESPWD index=\"0\" value=\"7d05bd542beaaecf0eb40cbf15d6bdcf\"/></Security><System><COMMUNICATE index=\"0\" value=\"02\"/>" 
                    +"<TRANSFER index=\"0\" value=\"01\"/>" 
                    + "<CONTACTID index=\"0\" value=\"003200903120000\"/>" 
                    + "<BUSSID index=\"0\" value=\"E002A002201411041144410000000000\"/><ORGCHANNELID index=\"0\" value=\"E002\"/>" 
                    + "<HOMECHANNELID index=\"0\" value=\"A002\"/>" 
                    + "</System><ACTIONCODE index=\"0\" value=\"0\"/><TESTFLAG index=\"0\" value=\"0\"/><Inparam><PROVINCE_CODE index=\"0\" value=\"HAIN\"/>" 
                    + "<IN_MODE_CODE index=\"0\" value=\"5\"/><TRADE_EPARCHY_CODE index=\"0\" value=\"0898\"/>" 
                    + "<ROUTE_EPARCHY_CODE index=\"0\" value=\"0898\"/><TRADE_CITY_CODE index=\"0\" value=\"HNSJ\"/>" 
                    + "<TRADE_DEPART_ID index=\"0\" value=\"00316\"/><TRADE_STAFF_ID index=\"0\" value=\"ITFSM000\"/>" 
                    + "<STAFF_PASSWD index=\"0\" value=\"123456\"/><TRADE_DEPART_PASSWD index=\"0\" value=\"0123456789\"/>" 
                    + "<CHANNEL_TRADE_ID index=\"0\" value=\"E00320090401165020000000000000\"/><TRADE_TERMINAL_ID index=\"0\" value=\"null\"/>" 
                    + "<TRADE_ROUTE_TYPE index=\"0\" value=\"00\"/><TRADE_ROUTE_VALUE index=\"0\" value=\"0898\"/><BIZ_CODE index=\"0\" value=\"BCS4166\"/>" 
                    + "<TRANS_CODE index=\"0\" value=\"THT3084\"/><TRADE_BRAND_CODE index=\"0\" value=\"G001\"/></Inparam><InFields><InField><INFO_CODE/>" 
                    + "<INFO_VALUE/></InField></InFields></Header><Body>" 
                    + "<SVC_CONTENT>{SERIAL_NUMBER=[\"18289546882\"], GRP_ID=[\"NTF8006586\"], PAK_NUM=[\"1\"], ORDER_ID=[\"1116042608492397\"], " 
                    + "ITEM=[{TRADE_ID=[\"1116042644181421\"], SERIAL_NUMBER=[\"18289546882\"], PAK_MONEY=[\"50000\"], PAK_GPRS=[\"10240\"], " 
                    + "PAK_OUT=[\"1\"], PAK_START_DTAE=[\"2016-04-20\"], PAK_END_DTAE=[\"2016-06-19 23:59:59\"]}]}</SVC_CONTENT></Body></SvcInfo>";
        return sendRequest(url, xml);
    }

    /**
     * @param xml
     * @return
     * @Title: parseChargeResponse
     * @Description: 解析充值返回的报文
     * @return: HNBOSSChargeResponse
     */
    private HNBOSSChargeResponse parseChargeResponse(String xml) {
        HNBOSSChargeResponse hnBossChargeResponse = new HNBOSSChargeResponse();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement().element("Body");
            String content = root.element("SVC_CONTENT").getStringValue();
            String ITEM = StringUtils.substringBetween(content, "ITEM=[[", "]]");
            String X_RESULTCODE = StringUtils.substringBetween(ITEM, "X_RESULTCODE=[\"", "\"]");//充值结果代码
            String X_RESULTINFO = StringUtils.substringBetween(ITEM, "X_RESULTINFO=[\"", "\"]");//充值结果代码说明
            hnBossChargeResponse.setCode(X_RESULTCODE);
            hnBossChargeResponse.setDesc(X_RESULTINFO);

            String ORDER_ID = StringUtils.substringBetween(content, "ORDER_ID=[\"", "\"]");//订单编码
            String GRP_ID = StringUtils.substringBetween(content, "GRP_ID=[\"", "\"]");//集团编码
            String TRADE_ID = StringUtils.substringBetween(ITEM, "TRADE_ID=[\"", "\"]");//CRM流水编码，响应序列号
            String TRANS_ID = StringUtils.substringBetween(ITEM, "TRANS_ID=[\"", "\"]");//平台流水号，充值请求序列号

            //封装充值结果
            HNBOSSCharge hnbossCharge = new HNBOSSCharge();
            hnbossCharge.setGroupId(GRP_ID);
            hnbossCharge.setOrderId(ORDER_ID);
            hnbossCharge.setTradeId(TRADE_ID);
            hnbossCharge.setTransId(TRANS_ID);
            hnBossChargeResponse.setCharge(hnbossCharge);

            return hnBossChargeResponse;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hnBossChargeResponse;
    }

    /**
     * 流量卡充值接口
     */
    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        //1、参数校验
        if (StringUtils.isBlank(cardNumber) || StringUtils.isBlank(mobile)) {
            logger.info("流量卡充值接口调用失败：请求参数不完整！");
            return null;
        }

        //2、组织请求报文
        String xml = assembleMdrcChargeRequestParm(cardNumber, mobile);

        //3、发送充值请求
        String response = sendRequest(BOSS_URL, xml);

        logger.info("开始调用流量卡充值接口：请求参数xml=" + xml + ",响应报文=" + response);

        //4、报文解析
        return parseMdrcChargeResponse(response);
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

}

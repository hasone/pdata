package com.cmcc.vrp.async.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossMatchResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.BossServiceProxyService;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.ec.bean.ChargeReq;
import com.cmcc.vrp.ec.bean.ChargeReqData;
import com.cmcc.vrp.ec.bean.ChargeResp;
import com.cmcc.vrp.ec.bean.ChargeRespData;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.InterfaceRecord;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargePojo;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.IpUtils;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * Created by leelyn on 2016/5/17.
 */
@Controller
@RequestMapping(value = "boss")
public class ChargeController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);
    public static final String MOBILE_REGEX = "^1[3-8][0-9]{9}$";

    @Autowired
    TaskProducer taskProducer;

    @Autowired
    InterfaceRecordService interfaceRecordService;

    @Autowired
    ProductService productService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    AccountService accountService;

    @Autowired
    BossServiceProxyService bossServiceProxyService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    SerialNumService serialNumService;

    @Autowired
    VirtualProductService virtualProductService;

    @Autowired
    GlobalConfigService globalConfigService;

    private XStream xStream;

    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", ChargeReq.class);
        xStream.alias("Response", ChargeResp.class);
        xStream.autodetectAnnotations(true);
    }

    /**
     * @Title: charge
     * @Author: wujiamin
     * @date 2016年10月17日下午4:56:01
     */
    @RequestMapping(value = "charge", method = RequestMethod.POST)
    @ResponseBody
    public void charge(final HttpServletRequest request, final HttpServletResponse response) {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {

                // 1. 校验参数
                String appKey = null;
                String systemSerialNum = null;
                ChargeReq req = null;
                Enterprise enterprise = null;
                Product product = null;
                if (needCheck()) {
                    String type = request.getContentType();
                    if (StringUtils.isBlank(type)) {
                        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                        logger.error("ContentType is null.");
                        return null;
                    }

                    String contentType = request.getContentType();
                    if (!(contentType.indexOf("application/xml") != -1 || contentType.indexOf("text/xml") != -1)) {
                        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                        logger.error("ContentType is not up to standard. {}", contentType);
                        return null;
                    }
                }

                if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
                        || StringUtils.isBlank(systemSerialNum = (String) request.getAttribute(Constants.SYSTEM_NUM_ATTR))
                        || (req = retrieveChargeReq(request)) == null || !validate(req)) {
                    logger.error("无效的充值请求参数, AppKey = {}, SystemNum = {}, ChargeReq = {}.", appKey, systemSerialNum, JSONObject.toJSONString(req));

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }

                logger.info("收到用户充值请求, AppKey = {}, SystemNum = {}, ChargeReq = {}.", appKey, systemSerialNum, JSONObject.toJSONString(req));

                if (req.getChargeReqData().getSerialNum().length() > 50) {
                    response.setStatus(422);
                    return null;
                }
                if ((enterprise = enterprisesService.selectByAppKey(appKey)) == null || enterprise.getDeleteFlag() != 0 || enterprise.getInterfaceFlag() != 1) {
                    logger.error("企业不存在、企业暂停或接口关闭, AppKey = {}, SystemNum = {}, ChargeReq = {}, Enterprise = {}.", appKey, systemSerialNum,
                            JSONObject.toJSONString(req), enterprise == null ? "" : JSONObject.toJSONString(enterprise));
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return null;
                }

                if ((product = productService.selectByProductCode(req.getChargeReqData().getProductCode())) == null
                        || !Pattern.compile(MOBILE_REGEX).matcher(req.getChargeReqData().getMobile()).matches()) {
                    logger.error("产品编码错误或号码格式错误. AppKey = {}, SystemNum = {}, ChargeReq = {}", appKey, systemSerialNum, JSONObject.toJSONString(req));
                    response.setStatus(422);
                    return null;
                }

                Long prdId = product.getId();
                EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(prdId, enterprise.getId());
                if (entProduct == null) {
                    logger.error("该产品与企业没有关联关系, Product = {}, Enterprise = {}.", JSONObject.toJSONString(product), JSONObject.toJSONString(enterprise));
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }

                // 1、流量池、话费产品转化 2、流量包不转化
                try {
                    Product p = null;
                    if (product.getType() == 3) {
                        p = virtualProductService.initProcess(prdId, req.getChargeReqData().getFlowSize());
                    } else {
                        String flowSize = req.getChargeReqData().getFlowSize();
                        if (flowSize != null) {
                            Long sizeKB = Long.valueOf(req.getChargeReqData().getFlowSize()) * 1024;
                            flowSize = sizeKB.toString();
                        }
                        p = virtualProductService.initProcess(prdId, flowSize);
                    }
                    if (p == null) {
                        logger.error("产品不存在");
                        response.setStatus(422);
                        return null;
                    }
                    prdId = p.getId();
                } catch (ProductInitException e) {
                    logger.info("ProductInitException {}"+ e.getMessage());
                    response.setStatus(422);
                    return null;
                } catch (NumberFormatException e) {
                    logger.info("NumberFormatException {}"+ e.getMessage());
                    response.setStatus(422);
                    return null;
                }

                ChargeReqData crd = req.getChargeReqData();

                Integer effectType = null;
                if (StringUtils.isBlank(crd.getEffectType())) {
                    effectType = 1;
                } else if (!"1".equals(crd.getEffectType()) && !"2".equals(crd.getEffectType())) {
                    logger.error("用户传入无效的生效方式, AppKey = {}, SystemNum = {}, ChargeReq = {}", appKey, systemSerialNum, JSONObject.toJSONString(req));
                    response.setStatus(422);
                    return null;
                } else {
                    effectType = NumberUtils.toInt(crd.getEffectType());
                }

                // 1. 扣钱
                Long entId = enterprise.getId();
                String mobile = crd.getMobile();
                String ecSerialNum = crd.getSerialNum();

                if (!minusCount(entId, prdId, mobile, systemSerialNum)) {
                    logger.error("扣减账户余额失败. EntId = {}, PrdId = {}, Mobile = {}, EcSerialNum = {}.", entId, prdId, mobile, ecSerialNum);
                    response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);// 0411更新，扣款失败返回状态码402
                    return null;
                }

                // 2. 插入记录
                String remoteIpAddr = IpUtils.getRemoteAddr(request);
                String fingerprint = (String) request.getAttribute(Constants.FINGERPRINT);
                ChargePojo chargePojo = buildChargePojo(entId, prdId, mobile, ecSerialNum, appKey, systemSerialNum, remoteIpAddr, fingerprint, effectType);
                ChargeRecord chargeRecord = null;
                if ((chargeRecord = insertRecord(chargePojo)) == null) {
                    logger.error("插入记录失败，ChargePojo = {}.", new Gson().toJson(chargePojo));

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }

                // 3. 校验通过, 扔到队列中
                chargePojo.setChargeRecordId(chargeRecord.getRecordId());
                if (!taskProducer.produceChargeMsg(chargePojo)) {
                    logger.error("无法将充值请求加入队列. ChargeReqData = {}, AppKey = {}, SysSerialNum = {}.", JSONObject.toJSONString(crd), appKey, systemSerialNum);
                    Date updateChargeTime = new Date();
                    Integer financeStatus = null;

                    // 进入队列失败，退款
                    if (!accountService.returnFunds(systemSerialNum)) {
                        logger.error("退款时出错. pltSerailNum = {}.", systemSerialNum);
                    } else {
                        financeStatus = FinanceStatus.IN.getCode();
                    }

                    // 入业务队列失败， 更新充值总表和EC分表的充值状态
                    if (!chargeRecordService.updateStatusAndStatusCode(chargeRecord.getId(), ChargeResult.ChargeMsgCode.refuseToEnterQueue.getCode(),
                            ChargeRecordStatus.FAILED.getCode(), ChargeRecordStatus.FAILED.getMessage(), financeStatus, updateChargeTime)) {
                        logger.error("更新充值记录失败, id = {}.", chargeRecord.getId());
                    }

                    ChargeRecordStatus chargeRecordStatus = ChargeRecordStatus.FAILED;
                    chargeRecordStatus.setStatusCode(ChargeResult.ChargeMsgCode.refuseToEnterQueue.getCode());
                    if (!interfaceRecordService.updateChargeStatus(chargeRecord.getRecordId(), ChargeRecordStatus.FAILED,
                            ChargeRecordStatus.FAILED.getMessage())) {
                        logger.error("更新ec记录失败, id = {}.", chargeRecord.getId());
                    }

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return null;

                } else {
                    if (!interfaceRecordService.updateStatusCode(chargeRecord.getRecordId(), ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                        logger.error("入业务队列成功，更新ec记录状态码失败");
                    } else {
                        logger.info("入业务队列成功，更新ec记录状态码成功，状态码={}.", ChargeResult.ChargeMsgCode.businessQueue.getCode());
                    }
                    if (!chargeRecordService.updateStatusCode(chargeRecord.getId(), ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                        logger.error("入业务队列成功，更新充值记录状态码失败");
                    } else {
                        logger.info("入业务队列成功，更新充值记录状态码成功，状态码={}.", ChargeResult.ChargeMsgCode.businessQueue.getCode());
                    }
                }

                // 4. 返回
                return buildResp(crd.getSerialNum(), systemSerialNum);
            }
        };

        try {
            String rep = callable.call();
            response.getWriter().write(rep);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private ChargeRecord buildChargeRecord(String serialNum, long entId, String mobile, long productId, long recordId, String systemNum, String fingerprint,
            Integer effectType) {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setEnterId(entId);
        chargeRecord.setPrdId(productId);
        chargeRecord.setStatus(com.cmcc.vrp.enums.ChargeRecordStatus.WAIT.getCode());
        chargeRecord.setType(ActivityType.INTERFACE.getname());
        chargeRecord.setaName("EC");
        chargeRecord.setTypeCode(ActivityType.INTERFACE.getCode());
        chargeRecord.setPhone(mobile);
        chargeRecord.setRecordId(recordId);
        chargeRecord.setSerialNum(serialNum);
        chargeRecord.setChargeTime(new Date());
        chargeRecord.setSystemNum(systemNum);
        chargeRecord.setFingerprint(fingerprint);
        chargeRecord.setEffectType(effectType);
        return chargeRecord;
    }

    private InterfaceRecord buildInterfaceRecord(String mobile, String productCode, String enterpriseCode, String ecSerialNum, String systenSerialNum,
            String ipAddress, String fingerprint) {
        InterfaceRecord record = new InterfaceRecord();
        record.setEnterpriseCode(enterpriseCode);
        record.setProductCode(productCode);
        record.setPhoneNum(mobile);
        record.setSerialNum(ecSerialNum);
        record.setSysSerialNum(systenSerialNum);
        record.setIpAddress(ipAddress);
        record.setStatus(ChargeRecordStatus.PROCESSING.getCode());
        record.setCreateTime(new Date());
        record.setDeleteFlag(com.cmcc.vrp.util.Constants.DELETE_FLAG.UNDELETED.getValue());
        record.setFingerprint(fingerprint);

        return record;
    }

    private SerialNum buildSerialNum(ChargePojo chargePojo) {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum(chargePojo.getSystemNum());
        serialNum.setEcSerialNum(chargePojo.getEcSerialNum());
        serialNum.setCreateTime(new Date());
        serialNum.setUpdateTime(new Date());
        serialNum.setDeleteFlag(0);
        return serialNum;
    }

    private ChargeRecord insertRecord(ChargePojo chargePojo) {
        String mobile = chargePojo.getMobile();
        Product product = productService.get(chargePojo.getProductId());
        String ecSerialNum = chargePojo.getEcSerialNum();
        String systemSerialNum = chargePojo.getSystemNum();
        String ipAddress = chargePojo.getRemoteIpAddr();
        String fingerprint = chargePojo.getFingerprint();
        String appKey = chargePojo.getAppKey();
        Integer effectType = chargePojo.getEffectType();
        Enterprise enterprise = enterprisesService.selectByAppKey(appKey);

        if (product == null || enterprise == null) {
            logger.error("找不到相应的产品或企业. 产品ID为{}, AppKey为{}.", chargePojo.getProductId(), appKey);
            return null;
        }

        // 判断企业的状态是否正常
        if (!enterprise.getDeleteFlag().equals(EnterpriseStatus.NORMAL.getCode()) || enterprise.getInterfaceFlag() != InterfaceStatus.OPEN.getCode()) {
            logger.error("企业的状态异常. Enterprise = {}.", new Gson().toJson(enterprise));
            return null;
        }

        // 插入调用记录
        InterfaceRecord interfaceRecord = buildInterfaceRecord(mobile, product.getProductCode(), enterprise.getCode(), ecSerialNum, systemSerialNum, ipAddress,
                fingerprint);
        if (!interfaceRecordService.insert(interfaceRecord)) {
            logger.error("插入调用记录时失败.");
            return null;
        }

        // 插入充值记录
        ChargeRecord chargeRecord = buildChargeRecord(ecSerialNum, enterprise.getId(), mobile, product.getId(), interfaceRecord.getId(), systemSerialNum,
                fingerprint, effectType);
        if (!chargeRecordService.create(chargeRecord)) {
            logger.error("插入充值记录时失败.");
            return null;
        }

        // 插入各方流水记录
        SerialNum serialNum = buildSerialNum(chargePojo);
        if (!serialNumService.insert(serialNum)) {
            logger.error("插入流水记录表时失败.");
            return null;
        }

        return chargeRecord;
    }

    private boolean minusCount(Long entId, Long prdId, String mobile, String serialNum) {
        try {
            AccountType accountType = AccountType.ENTERPRISE;

            Product product = productService.get(prdId);
            if (product == null) {
                logger.error("无法根据产品ID获取产品信息, PrdId = {}.", prdId);
                return false;
            }

            // 账户余额扣减,扣减的都是企业账户。现在没有活动账户了
            if (!tryMinusCount(entId, prdId, accountType, 1, serialNum, "充值")) {
                // 余额扣减失败了，从BOSS侧同步相应的余额并再次尝试扣减余额
                if (!syncFromBoss(mobile, entId, prdId) || !tryMinusCount(entId, prdId, accountType, 1, serialNum, "充值")) {
                    logger.error("账户余额扣减失败，原因是余额不足, SerialNum = {}, Mobile = {}.", serialNum, mobile);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("充值失败, SerialNum = {}, Mobile = {}.", serialNum, mobile);
        }

        return false;
    }

    private boolean syncFromBoss(String mobile, Long entId, Long prdId) {
        if (bossServiceProxyService.needSyncFromBoss()) { // 只有当proxy需要同步时才真正进行余额同步
            BossMatchResult bmr = bossServiceProxyService.chooseBossService(mobile, entId, prdId);

            BossService bossService = null;
            return bmr != null // 匹配结果不为空
                    && (bossService = bmr.getBossService()) != null // 选择的BOSS渠道不为空
                    && bossService.syncFromBoss(entId, prdId); // 同步余额成功
        }

        return false;
    }

    private boolean tryMinusCount(Long activityId, Long prdId, AccountType accountType, double delta, String serialNum, String desc) {
        try {
            if (accountService.minusCount(activityId, prdId, accountType, delta, serialNum, desc)) {
                logger.info(buildMsg("成功", activityId, prdId, delta, serialNum));
                return true;
            } else {
                logger.error(buildMsg("失败", activityId, prdId, delta, serialNum));
            }
        } catch (RuntimeException e) {
            logger.error(buildMsg("失败", activityId, prdId, delta, serialNum));
        }

        logger.error("扣减帐户失败，返回false");
        return false;
    }

    private String buildMsg(String statusMessage, Long activityId, Long prdId, double delta, String serialNum) {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder);
        formatter.format("扣减帐户余额时%s，充值失败. ActivityId = %s, PrdId = %s, Delta = %f, SerialNum = %s", statusMessage, activityId, prdId, delta, serialNum);

        return stringBuilder.toString();
    }

    private String buildResp(String ecSerialNum, String sysSerialNum) {
        ChargeResp chargeResp = buildChargeResp(ecSerialNum, sysSerialNum);

        OutputStream os = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(os, Charsets.UTF_8);
        xStream.toXML(chargeResp, writer);

        return os.toString();
    }

    // 获取充值请求参数
    private ChargeReq retrieveChargeReq(HttpServletRequest request) {
        try {
            String chargeReqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            return (ChargeReq) xStream.fromXML(chargeReqStr);
        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            return null;
        }
    }

    // 校验充值请求参数
    private boolean validate(ChargeReq req) {
        ChargeReqData reqData;

        return req != null && (reqData = req.getChargeReqData()) != null && StringUtils.isNotBlank(reqData.getSerialNum())
                && StringUtils.isNotBlank(reqData.getMobile()) && StringUtils.isNotBlank(reqData.getProductCode());
    }

    private ChargePojo buildChargePojo(Long entId, Long prdId, String mobile, String ecSerialNum, String appKey, String sysSerialNum, String remoteIpAddr,
            String fingerprint, Integer effectType) {
        ChargePojo pojo = new ChargePojo();
        pojo.setEnterpriseId(entId);
        pojo.setProductId(prdId);
        pojo.setMobile(mobile);
        pojo.setSystemNum(sysSerialNum);
        pojo.setEcSerialNum(ecSerialNum);

        pojo.setAppKey(appKey);
        pojo.setRemoteIpAddr(remoteIpAddr);
        pojo.setFingerprint(fingerprint);

        pojo.setEffectType(effectType);

        return pojo;
    }

    private ChargeResp buildChargeResp(String ecSerialNum, String systemSerialNum) {
        ChargeResp resp = new ChargeResp();
        resp.setRespData(buildRespData(ecSerialNum, systemSerialNum));
        resp.setResponseTime(DateUtil.getRespTime());

        return resp;
    }

    private ChargeRespData buildRespData(String ecSerialNum, String systemSerialNum) {
        ChargeRespData respData = new ChargeRespData();
        respData.setSerialNum(ecSerialNum);
        respData.setSystemNum(systemSerialNum);

        return respData;
    }

    private boolean needCheck() {

        String checkFlag = globalConfigService.get(GlobalConfigKeyEnum.EC_NEED_CHECK.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(checkFlag) ? "false" : checkFlag;
        return Boolean.parseBoolean(finalFlag);
    }
}

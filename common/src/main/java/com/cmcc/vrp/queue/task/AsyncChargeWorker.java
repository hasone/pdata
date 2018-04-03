package com.cmcc.vrp.queue.task;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.ec.bean.CallBackReq;
import com.cmcc.vrp.ec.bean.CallbackResp;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.ChargePojo;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/5/17.
 * edited by sunyiwei on 2016/5/26
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AsyncChargeWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(AsyncChargeWorker.class);
    private static XStream xStream;

    static {
        xStream = new XStream();
        xStream.alias("Request", CallBackReq.class);
        xStream.alias("Response", CallbackResp.class);

        xStream.autodetectAnnotations(true);
    }

    @Autowired
    ProductService productService;

    @Autowired
    ChargeService chargeService;

    @Autowired
    InterfaceRecordService interfaceRecordService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    EntCallbackAddrService entCallbackAddrService;

    @Autowired
    SerialNumService serialNumService;

    @Autowired
    AccountService accountService;

    @Autowired
    TaskProducer taskProducer;

    @Autowired
    ApplicationContext applicationContext;


    //1. 解析充值参数
    //2. 更新记录状态为已发送充值请求
    //3. 发送充值请求
    //4. 更新记录状态为充值结果
    @Override
    public void exec() {
        //0. 获取队列消息
        Gson gson = new Gson();
        String taskStr = getTaskString();
        logger.info("从充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        ChargePojo chargePojo;
        if (!validate(chargePojo = parse(gson, taskStr))) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        //impossible to be true, just to escape findbugs
        if (chargePojo == null) {
            return;
        }

        //获取参数
//        String mobile = chargePojo.getMobile();
        String systemSerialNum = chargePojo.getSystemNum();
//        Long prdId = chargePojo.getProductId();
        Long entId = chargePojo.getEnterpriseId();
        Long chargeRecordId = chargePojo.getChargeRecordId();

        //2. 开始处理，更新记录状态为已发送充值请求
//        if (!chargeRecordService.updateStatus(chargeRecordId, ChargeRecordStatus.PROCESSING, ChargeRecordStatus.PROCESSING.getMessage())) {
//            logger.error("更新充值状态为已发送充值请求时失败. ChargePojo = {}.", new Gson().toJson(chargePojo));
//            return;
//        }


        ChargeDeliverPojo chargeDeliverPojo = buildChargeDeliverPojo(chargePojo);
        DeliverByBossQueue adw = applicationContext.getBean(DeliverByBossQueue.class);

        ChargeRecord cr = chargeRecordService.getRecordBySN(chargeDeliverPojo.getSerialNum());
        if (!adw.publish(chargeDeliverPojo)) {

            logger.error("塞入分发队列失败");
            ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, ChargeRecordStatus.FAILED.getMessage());

            Date updateChargeTime = new Date();
            Integer financeStatus = null;
            
            //退款
            if (!chargeResult.isSuccess() && !accountService.returnFunds(systemSerialNum)) {
                logger.error("充值serialNum{},entId{}失败时账户返还失败", systemSerialNum, entId);
            }else{
                financeStatus = FinanceStatus.IN.getCode();
            }

            if (!interfaceRecordService.updateChargeStatus(chargeRecordId, ChargeRecordStatus.FAILED, ChargeRecordStatus.FAILED.getMessage())) {
                logger.error("更新ec状态时失败, recordId: " + chargeRecordId);
            }

            chargeResult.setFinanceStatus(financeStatus);
            chargeResult.setUpdateChargeTime(updateChargeTime);
            
            if (!chargeRecordService.updateStatus(cr.getId(), chargeResult)) {
                logger.error("更新chargeRecord状态时失败, recordId: " + chargeRecordId);
            }
            return;

        } else {

            if (!interfaceRecordService.updateStatusCode(chargeRecordId, ChargeResult.ChargeMsgCode.deliverQueue.getCode())) {
                logger.error("入分发队列成功，更新ec记录状态码失败, recordId: " + chargeRecordId);
            } else {
                logger.info("入分发队列成功，更新ec记录状态码成功, 状态码={}.", ChargeResult.ChargeMsgCode.deliverQueue.getCode());
            }

            if (!chargeRecordService.updateStatusCode(cr.getId(), ChargeResult.ChargeMsgCode.deliverQueue.getCode())) {
                logger.error("入分发队列成功，更新chargeRecord状态码失败, recordId: " + chargeRecordId);
            } else {
                logger.info("入分发队列成功，更新chargeRecord记录状态码成功, 状态码={}.", ChargeResult.ChargeMsgCode.deliverQueue.getCode());
            }
        }


        //3. 发送充值请求
//        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);
//        try {
//            chargeResult = chargeService.charge(chargeRecordId, entId, prdId, mobile, systemSerialNum);
//        } catch (Exception e) {
//            logger.error("充值时抛出异常，异常信息为{}.", e);
//        }finally {
//            //失败了要进行退款操作
//            if (!chargeResult.isSuccess() && !accountService.returnFunds(systemSerialNum, entId)) {
//                logger.error("充值serialNum{},entId{}失败时账户返还失败", systemSerialNum, entId);
//            }
//        }

        //4. 更新记录状态
//        if (chargeRecordService.updateStatus(chargeRecordId, chargeResult)) {
//            logger.info("充值结果为{}，更新记录成功, systemSerialNum = {}.", chargeResult.isSuccess(), systemSerialNum);
//        } else {
//            logger.error("充值结果为{}，更新记录失败, systemSerialNum = {}.", chargeResult.isSuccess(), systemSerialNum);
//        }

        //非异步情况且需要回调的一律回调
//        if (!chargeResult.getCode().equals(ChargeResult.ChargeResultCode.PROCESSING)) {
//            com.cmcc.vrp.queue.pojo.CallbackPojo pojo = new com.cmcc.vrp.queue.pojo.CallbackPojo();
//            pojo.setEntId(entId);
//            pojo.setSerialNum(systemSerialNum);
//            taskProducer.productPlatformCallbackMsg(pojo);
//        }
    }

    //解析充值对象
    private ChargePojo parse(Gson gson, String taskString) {
        try {
            return gson.fromJson(taskString, ChargePojo.class);
        } catch (Exception e) {
            logger.info("充值参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }

    //校验充值对象的有效性
    private boolean validate(ChargePojo taskPojo) {
        if (taskPojo == null
            || StringUtils.isBlank(taskPojo.getSystemNum())
            || StringUtils.isBlank(taskPojo.getEcSerialNum())
            || StringUtils.isBlank(taskPojo.getMobile())
            || taskPojo.getProductId() == null
            || taskPojo.getEnterpriseId() == null
            || taskPojo.getChargeRecordId() == null) {
            logger.error("无效的充值请求参数，ChargePojo = {}.", JSONObject.toJSONString(taskPojo));
            return false;
        }

        return true;
    }

    private ChargeDeliverPojo buildChargeDeliverPojo(ChargePojo chargePojo) {
        ChargeDeliverPojo cdp = new ChargeDeliverPojo();
        cdp.setEntId(chargePojo.getEnterpriseId());
        cdp.setPrdId(chargePojo.getProductId());
        cdp.setMobile(chargePojo.getMobile());
        cdp.setType(ChargeType.EC_TASK.getCode());
        cdp.setActivityName("EC");
        cdp.setActivityType(ActivityType.INTERFACE);
        cdp.setRecordId(chargePojo.getChargeRecordId());
        cdp.setSerialNum(chargePojo.getSystemNum());
        return cdp;
    }
}

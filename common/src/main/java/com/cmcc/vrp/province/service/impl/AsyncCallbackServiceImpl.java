package com.cmcc.vrp.province.service.impl;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.model.AsyncCallbackReq;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AsyncCallbackService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;
import com.google.gson.Gson;

/**
 * 处理回调的服务
 * <p>
 * Created by sunyiwei on 2016/10/11.
 */
@Service
public class AsyncCallbackServiceImpl implements AsyncCallbackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCallbackServiceImpl.class);

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    AccountService accountService;

    @Autowired
    TaskProducer taskProducer;
    
    
    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    ProductService productService;
    
    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;

    /**
     * 处理BOSS侧的回调， 包括但不限于以下操作
     * <p>
     * 1. 更新充值记录
     * 2. 更新流水号表
     * 3. 更新活动记录或接口记录
     * 4. 如果失败了， 要进行退钱操作
     * 5. 回调EC
     *
     * @param acr
     * @return
     */
    @Override
    public boolean process(AsyncCallbackReq acr) {
        //1. 校验参数
        if (!validate(acr)) {
            LOGGER.error("无效的回调参数，参数信息为{}.", acr == null ? null : new Gson().toJson(acr));
            return false;
        }

        String systemSerialNum = acr.getSystemSerialNum();
        ChargeRecord chargeRecord = chargeRecordService.getRecordBySN(systemSerialNum);
        if (chargeRecord == null) {
            LOGGER.error("根据系统流水号没有找到相应的充值记录. SystemSerialNum = {}.", systemSerialNum);
            return false;
        }

        ChargeRecordStatus chargeRecordStatus = ChargeRecordStatus.fromValue(acr.getChargeRecordStatus());

        //2只有在当前的充值状态不为失败, 而回调状态为充值失败时，才需要进行退款！
        boolean needRefund = !chargeRecord.getStatus().equals(ChargeRecordStatus.FAILED.getCode())
            && chargeRecordStatus == ChargeRecordStatus.FAILED;
        if (chargeRecordStatus == ChargeRecordStatus.FAILED && !needRefund) {
            LOGGER.info("当前的充值状态已经为充值失败，不会再次退款！SystemNum = {}.", systemSerialNum);
        }

        //需要退款，财务状态为未出账
        if(needRefund){
            chargeRecordStatus.setFinanceStatus(FinanceStatus.IN.getCode());
        }
        chargeRecordStatus.setUpdateChargeTime(new Date());

        //3如果充值失败了,要给企业退钱
        if (needRefund
            && !accountService.returnFunds(systemSerialNum)) {
            LOGGER.error("充值失败给企业退款时失败. SystemNum = {}，EntId = {}.", systemSerialNum, chargeRecord.getEnterId());
            return false;
        }

        //4 更新充值记录与活动记录
        if (!chargeRecordService.updateStatus(chargeRecord.getId(), chargeRecordStatus, acr.getErrorMsg())) {
            LOGGER.error("更新充值记录与活动记录时失败. 具体信息为{}.", new Gson().toJson(acr));
            return false;
        }

        //6发送短信:充值成功且开启发送短信开关
        if (ChargeRecordStatus.COMPLETE.getCode().intValue() == acr.getChargeRecordStatus() && needNotice()) {
            LOGGER.info("充值成功，发送短信提醒{}", systemSerialNum);
            String mobile = chargeRecord.getPhone();
            Integer effectType = chargeRecord.getEffectType();
            String type = "本流量包含2G/3G/4G流量";
            if ("2".equals(String.valueOf(effectType))) {
                type = "本流量将于下月1日生效，包含2G/3G/4G流量";
            }
            Enterprise enterprise = enterprisesService.selectById(chargeRecord.getEnterId());
            Product product = productService.selectProductById(chargeRecord.getPrdId());
            String productSize = String.valueOf(SizeUnits.KB.toMB(product.getProductSize()));
            EnterpriseSmsTemplate smsmTemplate = enterpriseSmsTemplateService.getChoosedSmsTemplate(chargeRecord
                    .getEnterId());
            if (smsmTemplate != null) {
                String template = smsmTemplate.getContent();
                //String template = "尊敬的用户，{1}向您赠送了{0}MB国内流量，{2}，感谢您的支持！";
                String content = MessageFormat.format(template, productSize, enterprise.getName(), type);
                // 将短信扔到相应的队列中
                if (!taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(mobile, content, productSize, enterprise
                        .getEntName(), MessageType.CHARGE_NOTICE.getCode(), smsmTemplate.getName()))) {
                    LOGGER.info("充值成功，入短信提醒队列失败{}", systemSerialNum);
                }
            }

        }
        
        //5. 回调EC, 但正在处理中的订单不能回调！
        //20170105修改，增加类型的判断，非ec充值不用回调
        return ChargeRecordStatus.PROCESSING == chargeRecordStatus
            || !chargeRecord.getTypeCode().equals(ActivityType.INTERFACE.getCode())
            || callbackEC(chargeRecord.getEnterId(), systemSerialNum);
    }

    private boolean callbackEC(Long entId, String systemNum) {
        // 将消息入列，实现回调EC平台
        CallbackPojo callbackPojo = new CallbackPojo();
        callbackPojo.setEntId(entId);
        callbackPojo.setSerialNum(systemNum);
        return taskProducer.productPlatformCallbackMsg(callbackPojo);
    }

    private boolean validate(AsyncCallbackReq acr) {
        return (acr != null
            && ChargeRecordStatus.fromValue(acr.getChargeRecordStatus()) != null
            && StringUtils.isNotBlank(acr.getMobile())
            && StringUtils.isNotBlank(acr.getSystemSerialNum()));
    }
    
    /**
     * @title: needNotice
     * */
    private boolean needNotice() {
        String noticeFlag = globalConfigService.get(GlobalConfigKeyEnum.CHARGE_SUCCESS_NOTICE.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(noticeFlag) ? "false" : noticeFlag;
        return Boolean.parseBoolean(finalFlag);
    }
}

package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.FlowCoinExchangeStatus;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.FlowcoinPresentPojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title:FlowcoinPresentWorker </p>
 * <p>Description: 流量币赠送worker</p>
 *
 * @author xujue
 * @date 2016年9月30日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowcoinPresentWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(FlowcoinPresentWorker.class);

    @Autowired
    IndividualAccountService accountService;

    @Autowired
    ActivityWinRecordService activityWinRecordService;

    @Autowired
    TaskProducer taskProducer;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    AdministerService administerService;

    @Override
    public void exec() {
        // 0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从赠送队列中消费消息，消息内容为{}.", taskStr);

        // 1. 解析充值参数
        FlowcoinPresentPojo pojo;
        if ((pojo = parse(taskStr)) == null) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        // 获取参数
        IndividualAccount account = pojo.getIndividualAccount();
        ActivityWinRecord record = pojo.getActivityWinRecord();

        // 检查被赠送用户是否是平台用户
        Administer chargeAdminister = administerService
            .selectByMobilePhone(record.getChargeMobile());

        if (chargeAdminister == null || chargeAdminister.getId() == null) {
            logger.info("被赠送号码不是平台用户，创建用户");
            if (!administerService.insertForScJizhong(record.getChargeMobile())) {
                logger.error("创建被赠送号码用户失败");
                return;
            }
            //新用户需要重新注入chargeAdminister
            chargeAdminister = administerService.selectByMobilePhone(record
                .getChargeMobile());
        }else{//如果该手机号之前已经是平台的管理员用户，在administer表中会存在该用户，但是该用户并没有个人账户信息，需要检查是否存在账户并插入账户             
            if(!accountService.insertAccountForScJizhong(chargeAdminister.getId())){
                logger.error("平台已存在该用户，但是用户没有个人账户，创建个人账户时失败，mobile={}，adminId={}", record.getChargeMobile(), chargeAdminister.getId());
                return;
            }
        }

        // 1、发起向boss增加流量币的请求
        if (accountService.changeBossAccount(chargeAdminister.getId(), new BigDecimal(record.getProductSize()),
            account.getIndividualProductId(), record.getRecordId(), (int) AccountRecordType.INCOME.getValue(),
            "流量币赠送,boss流量币增加", ActivityType.FLOWCOIN_PRESENT.getCode(), 0)) {
            // 扣除冻结账户
            if (!accountService.changeFrozenAccount(account.getAdminId(), account.getOwnerId(), account.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(), record.getRecordId(),
                (int) AccountRecordType.OUTGO.getValue(), "流量币赠送,扣除冻结账户", ActivityType.FLOWCOIN_PRESENT.getCode(),
                0)) {
                logger.error("changeFrozenAccount失败，account={}" + JSONObject.toJSONString(account));
            } else {

                String content = "您的好友" + record.getOwnMobile() + "赠送您" + record.getProductSize()
                    + "个流量币，流量币已放入您的账户中，请登录四川移动官方平台" + getFlowCoinPresentPath() + "进行兑换。";
                logger.info("准备发送赠送成功短信,短信内容：" + content);
                taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(record.getChargeMobile(), content,
                    record.getProductSize().toString(), null, null));
            }
            // 更新记录状态
            List<String> recordIds = new ArrayList<String>();
            recordIds.add(record.getRecordId());
            if (!activityWinRecordService.batchUpdateStatus(recordIds, record.getChargeMobile(),
                FlowCoinExchangeStatus.SUCCESS.getCode(), FlowCoinExchangeStatus.PROCESSING.getCode(), new Date())) {
                logger.error("更新赠送记录状态失败，记录recordId={}", record.getId());
            }
        } else {
            // 充值结果失败
            // 更新记录状态
            List<String> recordIds = new ArrayList<String>();
            recordIds.add(record.getRecordId());
            if (!activityWinRecordService.batchUpdateStatus(recordIds, record.getChargeMobile(),
                FlowCoinExchangeStatus.FAIL_BACK_PROC.getCode(), FlowCoinExchangeStatus.PROCESSING.getCode(), new Date())) {
                logger.error("更新赠送记录状态失败记录recordId={}", record.getId());
            }

            // 向boss发送流量币增加请求
            // 增加成功
            if (accountService.changeBossAccount(account.getAdminId(), new BigDecimal(record.getProductSize()),
                account.getIndividualProductId(), record.getRecordId(), (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，boss流量币增加", ActivityType.FLOWCOIN_PRESENT.getCode(), 1)) {
                if (accountService.changeFrozenAccount(account.getAdminId(), account.getOwnerId(), account.getId(),
                    new BigDecimal(record.getProductSize()), account.getIndividualProductId(), record.getRecordId(),
                    (int) AccountRecordType.INCOME.getValue(), "流量币赠送失败，扣除冻结账户", ActivityType.FLOWCOIN_PRESENT.getCode(),
                    1)) {
                    if (!activityWinRecordService.batchUpdateStatus(recordIds, record.getChargeMobile(),
                        FlowCoinExchangeStatus.FAIL_BACK_SUCC.getCode(), FlowCoinExchangeStatus.FAIL_BACK_PROC.getCode(),
                        new Date())) {
                        logger.error("更新赠送记录状态失败记录recordId={}", record.getId());
                    }
                } else {
                    if (!activityWinRecordService.batchUpdateStatus(recordIds, record.getChargeMobile(),
                        FlowCoinExchangeStatus.FAIL_BACK_FAIL.getCode(), FlowCoinExchangeStatus.FAIL_BACK_PROC.getCode(),
                        new Date())) {
                        logger.error("更新赠送记录状态失败记录recordId={}", record.getId());
                    }
                    logger.error("扣除冻结账户失败recordId={}", record.getRecordId());
                }
            } else {
                if (!activityWinRecordService.batchUpdateStatus(recordIds, record.getChargeMobile(),
                    FlowCoinExchangeStatus.FAIL_BACK_FAIL.getCode(), FlowCoinExchangeStatus.FAIL_BACK_PROC.getCode(),
                    new Date())) {
                    logger.error("更新赠送记录状态失败记录recordId={}", record.getId());
                }
                logger.error("boss侧增加流量币失败recordId={}", record.getRecordId());
            }
        }
    }

    // 解析充值对象
    private FlowcoinPresentPojo parse(String taskString) {
        try {
            return JSONObject.parseObject(taskString, FlowcoinPresentPojo.class);
        } catch (Exception e) {
            logger.info("参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }

    public String getFlowCoinPresentPath() {
        return globalConfigService.get(GlobalConfigKeyEnum.FLOWCOIN_PRESENT_PATH.getKey());
    }

}

package com.cmcc.vrp.queue.task;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/8/10.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AsynChargeQueryWorker extends Worker implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsynChargeQueryWorker.class);
    

    @Autowired
    private AccountService accountService;

    @Autowired
    private TaskProducer taskProducer;

    @Autowired
    private ChargeRecordService chargeRecordService;

    private ApplicationContext applicationContext;
    
    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void exec() {
        //获取消息
        String msg = getTaskString();
        LOGGER.info("从查询充值状态消息队列中获取消息:{}", msg);

        ChargeQueryPojo pojo = new Gson().fromJson(msg, ChargeQueryPojo.class);

        String fingerPrint = pojo.getFingerPrint();
        String systemNum = pojo.getSystemNum();
        Long entId = pojo.getEntId();

        if (StringUtils.isBlank(fingerPrint)
            || StringUtils.isBlank(systemNum)
            || entId == null) {
            LOGGER.info("从查询充值状态消息队列为空");
            return;
        }

        BaseBossQuery bossQuery = chooseBossQuery(fingerPrint);
        if (bossQuery == null) {
            LOGGER.info("没有对应的查询服务");
            return;
        }

        ChargeRecord chargeRecord = chargeRecordService.getRecordBySN(systemNum);
        if (chargeRecord == null) {
            LOGGER.error("无法根据系统序列号获取相应的充值记录！");
            return;
        }

        //选择查询BOSS服务
        LOGGER.info("选择的BOSS查询渠道为{}.", bossQuery.getFingerPrint());
        BossQueryResult result = bossQuery.queryStatus(systemNum);

        //处理1：失败了要退钱，没查到要继续查;退钱了就返回财务状态为未出账
        Integer financeStatus = preProcess(result, pojo);
                
        //处理2：更新充值记录（如果有必要的话）
        ChargeResult chargeResult = convert(result, chargeRecord.getStatus());
        if (chargeResult != null) {
            chargeResult.setFinanceStatus(financeStatus);
            chargeResult.setUpdateChargeTime(new Date());
            
            if (updateRecord(systemNum, chargeResult)) {
                LOGGER.info("充值结果为{}，更新记录成功, systemSerialNum = {}.", chargeResult.isSuccess(), systemNum);
            } else {
                LOGGER.error("充值结果为{}，更新记录失败, systemSerialNum = {}.", chargeResult.isSuccess(), systemNum);
            }

            if (!chargeResult.getCode().equals(ChargeResult.ChargeResultCode.PROCESSING)) {
                callbackEC(entId, systemNum);
            }
        }
    }

    private Integer preProcess(BossQueryResult bqr, ChargeQueryPojo pojo) {
        //失败了准备退钱
        Integer financeStatus = null;
        if (bqr.equals(BossQueryResult.FAILD)) {
            String systemNum = pojo.getSystemNum();
            if (!accountService.returnFunds(systemNum)) {//退款
                LOGGER.info("查询BOSS侧充值未成功后，平台侧退款失败");
            }else{
                financeStatus = FinanceStatus.IN.getCode();
            }
            return financeStatus;
        }
        Integer times = pojo.getTimes();
        // 如果在处理中或者由于异常出现的查询失败,将消息扔回队列继续查询
        if ((bqr.equals(BossQueryResult.PROCESSING) || bqr.equals(BossQueryResult.EXCEPTION))
                && times <= getQueryTimes()) {
            String beginTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            pojo.setBeginTime(beginTime);
            times = times + 1;
            pojo.setTimes(times);
            LOGGER.info("入队时间{},入队次数{}", beginTime,times);
            taskProducer.produceAsynDeliverQueryMsg(pojo);
        }
        return financeStatus;
    }

    private ChargeResult convert(BossQueryResult bqr, int currentStatus) {
        ChargeResult chargeResult = null;        
        if (bqr.equals(BossQueryResult.FAILD)) {
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);
            chargeResult.setFailureReason(bqr.getMsg());
        } else if (bqr.equals(BossQueryResult.SUCCESS)) {
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.SUCCESS);
        } else if (bqr.equals(BossQueryResult.PROCESSING) || bqr.equals(BossQueryResult.EXCEPTION)) {
            //由于充值中不是终止状态，因此只有在当前状态不为“充值中”时，才去更新！
            if (currentStatus != ChargeRecordStatus.PROCESSING.getCode()) {
                chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.PROCESSING);
            } else {
                chargeResult = null;
            }
        } else {
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.UN_ACCEPT);
            chargeResult.setFailureReason(bqr.getMsg());
        }

        return chargeResult;
    }

    /**
     * 更新充值状态
     *
     * @param systemNum
     * @param chargeResult
     * @return
     */
    private boolean updateRecord(String systemNum, ChargeResult chargeResult) {
        ChargeRecord record = chargeRecordService.getRecordBySN(systemNum);
        return chargeRecordService.updateStatus(record.getId(), chargeResult);
    }

    /**
     * 回掉EC平台
     *
     * @param entId
     * @param systemNum
     */
    private void callbackEC(Long entId, String systemNum) {
        // 将消息入列，实现回调EC平台
        CallbackPojo callbackPojo = new CallbackPojo();
        callbackPojo.setEntId(entId);
        callbackPojo.setSerialNum(systemNum);
        taskProducer.productPlatformCallbackMsg(callbackPojo);
    }

    /**
     * 根据指纹来查询相应的查询服务
     *
     * @param fingerPrint
     * @return
     */
    private BaseBossQuery chooseBossQuery(String fingerPrint) {
        List<BaseBossQuery> bossQueries = new LinkedList<BaseBossQuery>(applicationContext.getBeansOfType(BaseBossQuery.class).values());
        for (BaseBossQuery bossQuery : bossQueries) {
            if (fingerPrint.equals(bossQuery.getFingerPrint())) {
                return bossQuery;
            }
        }
        return null;
    }
    
    private Integer getQueryTimes() {
        String times = globalConfigService.get(GlobalConfigKeyEnum.BOSS_QUERY_TIME.getKey());
        return StringUtils.isBlank(times)? 10 : NumberUtils.toInt(times);
    }
}

package com.cmcc.vrp.queue.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.MdrcChargePojo;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * Created by sunyiwei on 2016/6/21.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MdrcAsyncChargeWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(MdrcAsyncChargeWorker.class);

    private static String chargeTimeLimitPerMonthPrefix = "MDRC_MAX_SIZE_PER_MONTH_";
    
    private static String mdrcChargePrefix = "MDRC_CHARGE_";
    
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    
    @Autowired
    RedisUtilService redisUtilService;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    AccountService accountService;

    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        MdrcChargePojo chargePojo;
        if (!validate(chargePojo = parse(taskStr))) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        //2. 发起充值
        String cardNum = chargePojo.getCardNum();
        String password = chargePojo.getPassword();
        String mobile = chargePojo.getMobile();
        String ip = chargePojo.getIp();
        String serialNum = chargePojo.getSerialNum();

        //3.校验
        String errMsg = mdrcCardInfoService.validate(cardNum, password);
        if (StringUtils.isNotBlank(errMsg)) {
            logger.info("流量卡充值失败，校验不通过：cardNum = {}, password = {}, errMsg = {}", cardNum, password, errMsg);
            return;
        }
        MdrcCardInfo mdrcCardInfo = mdrcCardInfoService.get(cardNum);

        //校验账户
        //        MinusCountReturnType checkType = accountService.minusCount(mdrcCardInfo.getEnterId(),
        //                mdrcCardInfo.getProductId(), AccountType.ENTERPRISE, 1.0, serialNum,
        //                "流量卡", false);
        //        if (!checkType.equals(MinusCountReturnType.OK)) {
        //            logger.info("流量卡充值失败，账户余额不足：cardNum = {}, password = {}, errMsg = {}" , cardNum, password, checkType.getMsg());
        //            return;
        //        }

        //4.已发送充值请求       
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("systemNum", serialNum);
        List<ChargeRecord> records = chargeRecordService.getMdrcChargeRecords(String.valueOf(mdrcCardInfo.getYear()),
                map);
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setId(records.get(0).getId());
        chargeRecord.setChargeTime(new Date());
        chargeRecord.setStatus(ChargeRecordStatus.PROCESSING.getCode());
        if (!chargeRecordService.updateByPrimaryKeySelective(chargeRecord)) {
            logger.info("流量卡充值失败，更新充值记录为已发送充值请求失败：chargeRecord = " + new Gson().toJson(chargeRecord));
            return;
        }
        
        //更新请求序列号
        MdrcCardInfo cardUpdate = new MdrcCardInfo();
        cardUpdate.setId(mdrcCardInfo.getId());
        cardUpdate.setReponseSerialNumber("");
        cardUpdate.setRequestSerialNumber(serialNum);
        if(!mdrcCardInfoService.updateByPrimaryKeySelective(mdrcCardInfo.getYear(), cardUpdate)){
            logger.info("流量卡重新充值更新卡充值序列号失败，chargeRecord = {}", new Gson().toJson(cardUpdate));
            return;
        }

        //5.返回充值结果:充值结果在use中更新
        if (mdrcCardInfoService.use(cardNum, password, mobile, ip, serialNum)) {
            logger.info("流量卡充值成功, 具体信息为{}.", taskStr);

            //是否限制每个用户充值成功次数
            String maxTimesPerMonthStr = globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_CHARGE_SUCCESS_PER_MONTH
                    .getKey());
            if (StringUtils.isBlank(maxTimesPerMonthStr)) {
                return;
            }

            //每月充值成功次数限制
            Calendar calendar = Calendar.getInstance();
            Date endTimeOfMonth = DateUtil.getEndTimeOfMonth(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1);
            Long minutes = (endTimeOfMonth.getTime() - calendar.getTimeInMillis()) / (1000 * 60);
            String key = chargeTimeLimitPerMonthPrefix + mobile;

            //已充值的次数
            String chargedSuccessTimes = redisUtilService.get(key);
            Long times = null;
            if (StringUtils.isNotBlank(chargedSuccessTimes)) {//当月已经发生过充值
                try {
                    times = Long.valueOf(chargedSuccessTimes);
                    times++;
                } catch (Exception e) {
                    // TODO: handle exception
                    times = 1L;
                }
                String result = redisUtilService.set(key, times.toString(), minutes.intValue());
                logger.info("更新手机号码：{}，流量卡充值成功次数：{}，该限制{}分钟后失效， 操作结果：{}。", mobile, times.toString(),
                        minutes.intValue(), result);

            } else {//当前没有发送过充值,充值成功次数默认为1
                redisUtilService.set(key, "1", minutes.intValue());
            }
        } else {
            logger.error("流量卡充值失败, 具体信息为{}.", taskStr);
            //退款
            //            if (!accountService.returnFunds(serialNum, ActivityType.MDRC_CHARGE, mdrcCardInfo.getProductId(), 1)) {
            //                logger.error("流量卡充值失败, 退款失败, 具体信息为{}.", taskStr);
            //            }  
        }
        
        //充值结束，释放锁
        String mdrcChargeKey = mdrcChargePrefix + cardNum;
        if(StringUtils.isBlank(redisUtilService.get(mdrcChargeKey))){//该卡号已发起过充值
            logger.info("营销卡充值结束, redis中, key = {} 已自动失效！", mdrcChargeKey);
        }else {
            if(redisUtilService.del(mdrcChargeKey)){
                logger.info("营销卡充值结束, redis中，key = {} 手动删除成功！", mdrcChargeKey);
            }else {
                logger.info("营销卡充值结束, redis中，key = {} 手动删除失败！", mdrcChargeKey);
            }
        }
    }
    //解析充值对象
    private MdrcChargePojo parse(String taskString) {
        try {
            return JSON.parseObject(taskString, MdrcChargePojo.class);
        } catch (Exception e) {
            logger.info("充值参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.toString(), taskString);
            return null;
        }
    }

    //校验充值对象的有效性
    private boolean validate(MdrcChargePojo taskPojo) {
        if (taskPojo == null || StringUtils.isBlank(taskPojo.getCardNum())
                || StringUtils.isBlank(taskPojo.getPassword()) || StringUtils.isBlank(taskPojo.getMobile())
                || StringUtils.isBlank(taskPojo.getIp()) || StringUtils.isBlank(taskPojo.getSerialNum())) {
            logger.error("无效的充值请求参数，MdrcChargePojo = {}.", JSONObject.toJSONString(taskPojo));
            return false;
        }

        return true;
    }
}

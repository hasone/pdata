package com.cmcc.vrp.queue.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.PresentRuleService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.BlockPresentPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;
import com.cmcc.vrp.queue.task.strategy.ChargeRecordSerialNum;
import com.cmcc.vrp.queue.task.strategy.Info;

/**
 * 批量赠送和包月赠送的公用策略，从原BatchPresentWorker类提取
 *
 */
@Component("presentWorkerStrategy")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PresentWorkerStrategy {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PresentWorkerStrategy.class);
    
    @Autowired
    PresentRecordService presentRecordService;
    
    @Autowired
    MonthlyPresentRecordService monthlyPresentRecordService;

    @Autowired
    PresentRuleService presentRuleService;

    @Autowired
    ChargeService chargeService;

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    SerialNumService serialNumService;

    @Autowired
    AccountService accountService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    ProductService productService;

    @Autowired
    PresentSerialNumService presentSerialNumService;

    @Autowired
    ApplicationContext applicationContext;
    
    private ChargeType chargeType = ChargeType.PRESENT_TASK;
    
    private ActivityType activityType = ActivityType.GIVE;
    
    /**
     * 解析json，得到pojo
     */
    public List<PresentPojo> getPresentPojos(String taskString){
        BlockPresentPojo taskPojo = null;
        try {
            taskPojo = JSON.parseObject(taskString, BlockPresentPojo.class);
            LOGGER.error("开始处理批量赠送, 赠送参数为{}" + taskString);
        } catch (JSONException e) {
            LOGGER.error("批量赠送参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.toString(), taskString);
            return new ArrayList<PresentPojo>();
        }

        List<PresentPojo> pojos = taskPojo.getPojos();
        return pojos;
    }
    
    /**
     * 构造充值记录列表
     */
    public List<ChargeRecord> buildChargeRecords(List<PresentPojo> pojos, String activityName){
        return buildChargeRecords(pojos, ChargeRecordStatus.WAIT, ChargeRecordStatus.WAIT.getMessage(), activityName);
    }
    
 
    /**
     * 构造充值记录列表
     */
    private List<ChargeRecord> buildChargeRecords(List<PresentPojo> pojos, ChargeRecordStatus chargeRecordStatus, String errorMsg, String activityName) {
        List<ChargeRecord> crs = new LinkedList<ChargeRecord>();
        for (PresentPojo pojo : pojos) {
            ChargeRecord chargeRecord = new ChargeRecord();
            chargeRecord.setEnterId(pojo.getEnterpriseId());
            chargeRecord.setPrdId(pojo.getProductId());
            chargeRecord.setStatus(chargeRecordStatus.getCode());
            chargeRecord.setErrorMessage(errorMsg);
            chargeRecord.setType(getActivityType().getname());
            chargeRecord.setaName(activityName);
            chargeRecord.setTypeCode(getActivityType().getCode());
            chargeRecord.setPhone(pojo.getMobile());
            chargeRecord.setRecordId(pojo.getRecordId());
            chargeRecord.setChargeTime(new Date());
            chargeRecord.setSystemNum(pojo.getRequestSerialNum());
            chargeRecord.setCount(pojo.getCount());
            crs.add(chargeRecord);
        }

        return crs;
    }

    /**
     * buildSerialNums
     */
    public List<ChargeRecordSerialNum> buildSerialNums(List<ChargeRecord> chargeRecords){
        List<ChargeRecordSerialNum> crsns = new LinkedList<ChargeRecordSerialNum>();
        for (ChargeRecord chargeRecord : chargeRecords) {
            crsns.add(new ChargeRecordSerialNum(chargeRecord, initSerailNum(chargeRecord.getSystemNum())));
        }

        return crsns;
    }
    
    /**
     * buildInfos
     */
    public List<Info> buildInfos(List<ChargeRecord> crs){
        List<Info> infos = new LinkedList<Info>();
        for (ChargeRecord cr : crs) {
            infos.add(new Info(cr, new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, ChargeRecordStatus.FAILED.getMessage())));
        }

        return infos;
    }
    
    /**
     * batchCharge
     */
    public List<Info> batchCharge(final List<ChargeRecordSerialNum> crsns, String activityName) {
        final List<Info> infos = new LinkedList<Info>();

        final int size = crsns.size();

        List<Long> ids = new ArrayList<Long>();
        List<String> sns = new ArrayList<String>();

        for (int i = 0; i < size; i++) {
            ChargeRecordSerialNum crsn = crsns.get(i);
            ChargeRecord cr = crsn.getChargeRecord();

            //初始化充值结果
            ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "充值失败");
            try {
                ChargeDeliverPojo chargeDeliverPojo = buildChargeDeliverPojo(cr, activityName);

                DeliverByBossQueue adw = applicationContext.getBean(DeliverByBossQueue.class);
                if (!adw.publish(chargeDeliverPojo)) {
                    infos.add(new Info(cr, chargeResult));
                } else {
                    ids.add(cr.getRecordId());
                    sns.add(cr.getSystemNum());
                }
                /*chargeResult = chargeService.charge(cr.getId(), cr.getEnterId(),
                    cr.getPrdId(), cr.getPhone(), crsn.getSerialNum().getPlatformSerialNum());*/
            } catch (Exception e) {
                LOGGER.error("将充值请求塞入分发队列出错，异常信息为{}.", e);
                infos.add(new Info(cr, chargeResult));
            }
        }

        if (!chargeRecordService.batchUpdateStatusCode(ChargeResult.ChargeMsgCode.deliverQueue.getCode(), sns)) {
            LOGGER.error("更新充值记录状态码出错.");
        }

        if (ids != null && ids.size() > 0 && !updateStatusCode(ids)) {
            LOGGER.error("更新赠送记录状态码出错.");
        } else {
            LOGGER.info("入分发队列成功，赠送记录id = {}， 状态码={}", ids, ChargeResult.ChargeMsgCode.deliverQueue.getCode());
        }

        return infos;

    }
    
    /**
     * 更新批量总送的状态码
     */
    protected boolean updateStatusCode(List<Long> ids){
        //1.
        if(ChargeType.PRESENT_TASK.equals(getChargeType())){
            return presentRecordService.batchUpdateStatusCode(ids, ChargeResult.ChargeMsgCode.deliverQueue.getCode());
        }else if(ChargeType.MONTHLY_TASK.equals(getChargeType())){
            return monthlyPresentRecordService.batchUpdateStatusCode(ids, ChargeResult.ChargeMsgCode.deliverQueue.getCode());
        }
        
        return false;
    }
    
    /**
     * initSerailNum
     */
    private SerialNum initSerailNum(String platformSerialNum) {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum(platformSerialNum);
        serialNum.setUpdateTime(new Date());
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(0);
        return serialNum;
    }

    /**
     * 根据错误的充值记录，每条每条退款
     */
    public boolean refund(List<Info> failInfos) {
        if (failInfos.size() == 0) {
            return true;
        }

        try {
            for (Info info : failInfos) {
                Integer financeStatus = null;
                Date updateChargeTime = new Date();
                //退钱
                if (!accountService.returnFunds(info.getChargeRecord().getSystemNum(), getActivityType(), info.getChargeRecord().getPrdId(), 
                        info.getChargeRecord().getCount())) {
                    LOGGER.error("退还批量赠送失败金额时失败. 退款失败,chargeRecord={}", JSON.toJSONString(info.getChargeRecord()));
                }else{
                    financeStatus = FinanceStatus.IN.getCode();
                }
                //更新charge_record的充值状态
                if(!chargeRecordService.updateBySystemNum(info.getChargeRecord().getSerialNum(), ChargeRecordStatus.FAILED.getCode(), 
                        ChargeRecordStatus.FAILED.getMessage(), financeStatus, updateChargeTime)){
                    LOGGER.error("批量赠送更新charge_record失败,chargeRecord={}", JSON.toJSONString(info.getChargeRecord()));
                }
            }
        } catch (RuntimeException e) {
            LOGGER.error("退还批量赠送失败金额时失败. 退款失败." + e.getMessage());
        }

        return true;
    }
    
    //获取所有充值失败的记录
    public List<Info> getFailInfo(List<Info> infos) {
        List<Info> failInfos = new ArrayList<Info>();
        for (Info info : infos) {
            ChargeResult cr = info.getChargeResult();
            if (!cr.isSuccess()) {
                failInfos.add(info);
            }
        }
        return failInfos;
    }
    
    /**
     * buildChargeDeliverPojo
     */
    protected ChargeDeliverPojo buildChargeDeliverPojo(ChargeRecord cr, String activityName) {
        ChargeDeliverPojo cdp = new ChargeDeliverPojo();
        cdp.setEntId(cr.getEnterId());
        cdp.setPrdId(cr.getPrdId());
        cdp.setMobile(cr.getPhone());
        cdp.setType(getChargeType().getCode());
        cdp.setActivityName(activityName);
        cdp.setActivityType(getActivityType());
        cdp.setRecordId(cr.getRecordId());
        cdp.setSerialNum(cr.getSystemNum());
        cdp.setCount(cr.getCount());
        return cdp;
    }
    
    /**
     * 得到ChargeType
     */
    public ChargeType getChargeType(){
        return chargeType;
    }
    
    /**
     * 得到ActivityType
     */
    public ActivityType getActivityType(){
        return activityType;
    }
    
    /**
     * setChargeType
     */
    public void setChargeType(ChargeType chargeType){
        this.chargeType = chargeType;
    }
    
    /**
     * setActivityType
     */
    public void setActivityType(ActivityType activityType){
        this.activityType = activityType;
    }
}

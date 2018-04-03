package com.cmcc.vrp.queue.task.provinces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.ZwBossPojo;
import com.cmcc.vrp.queue.task.Worker;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/15.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PackageWorker extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageWorker.class);

    @Autowired
    TaskProducer producer;

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    Gson gson;
    
    @Autowired
    InterfaceRecordService interfaceRecordService;
    
    @Autowired
    PresentRecordService presentRecordService;
    
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    
    @Autowired
    TaskProducer taskProducer;
    
    @Autowired
    AccountService accountService;

    @Override
    public void exec() {
        // 获取消息
        String msg = getTaskString();

        ZwBossPojo pojo = gson.fromJson(msg, ZwBossPojo.class);

        if (pojo == null) {
            LOGGER.error("没有获取消息");
            return;
        }
        
        //打包后分发回卓望队列
        if(!producer.produceZwPackage(pojo)){
            //失败需要退款及更新充值状态操作
            dealWhenFailure(pojo.getPojos(), "打包后分发回卓望队列出错", ChargeResult.ChargeMsgCode.zwProviceQueue.getCode());   
        }

        if (!updateStatusCode(pojo, ChargeResult.ChargeMsgCode.zwBossQueue.getCode())) {
            LOGGER.error("更新充值业务流状态失败");
        }
        LOGGER.info("已发送卓望BOSS队列,更新StatusCode状态:{}", ChargeResult.ChargeMsgCode.zwBossQueue.getCode());
    }

    private boolean updateStatusCode(ZwBossPojo pojo, String statusCode) {
        List<ChargeDeliverPojo> pojos = pojo.getPojos();

        List<ChargeRecord> chargeRecords = chargeRecordService.batchSelectBySystemNum(buildSN(pojos));

        for (ChargeRecord chargeRecord : chargeRecords) {            
            //更新charge_record的status_code
            if(!chargeRecordService.updateStatusCode(chargeRecord.getId(), statusCode)){
                LOGGER.error("charge_record更新statusCode失败,id={},statusCode={}", chargeRecord.getId(), statusCode);
            }
            //更新分表的status_code
            if (chargeRecord.getTypeCode().equals(ActivityType.GIVE.getCode())) {
                if (!presentRecordService.updateStatusCode(chargeRecord.getRecordId(), statusCode)) {
                    LOGGER.error("更新赠送记录状态码出错,recordId={}, statusCode={}", chargeRecord.getRecordId(), statusCode);
                } 
            } else if (chargeRecord.getTypeCode().equals(ActivityType.INTERFACE.getCode())) {
                if (!interfaceRecordService.updateStatusCode(chargeRecord.getRecordId(), statusCode)) {
                    LOGGER.error("更新ec记录状态码出错,recordId={}, statusCode={}", chargeRecord.getRecordId(), statusCode);
                }
            } else {
                //营销活动
                if (!activityWinRecordService.updateStatusCodeByRecordId(chargeRecord.getSerialNum(), statusCode)) {
                    LOGGER.error("更新活动记录状态码出错,recordId={}, statusCode={}", chargeRecord.getSerialNum(), statusCode);
                }
            }
           
        }
        return true;
    }

    private List<String> buildSN(List<ChargeDeliverPojo> pojos) {
        List<String> systemNums = new ArrayList<String>();
        for (ChargeDeliverPojo chargeDeliverPojo : pojos) {
            systemNums.add(chargeDeliverPojo.getSerialNum());
        }
        return systemNums;
    }
    
    protected void dealWhenFailure(List<ChargeDeliverPojo> pojos, String failureMsg, String statusCode) {
        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);
        chargeResult.setFailureReason(failureMsg);
        chargeResult.setStatusCode(statusCode);
        
        //boss为同步接口且充值失败，需要更新charge_record的财务状态和充值时间
        chargeResult.setUpdateChargeTime(new Date());
        
        if(pojos !=null && pojos.size()>0){
            for(ChargeDeliverPojo pojo : pojos){
              //退款
                if (!accountService.returnFunds(pojo.getSerialNum())) {
                    LOGGER.error("退款失败");
                }else{
                    chargeResult.setFinanceStatus(FinanceStatus.IN.getCode());
                }
                
                //更新充值状态为充值失败
                if (!updateChargeRecord(pojo.getSerialNum(), chargeResult)) {
                    LOGGER.error("更新充值记录失败");
                }
                //从EC侧来的业务请求需要进行回调
                if (pojo.getType().equals(ChargeType.EC_TASK.getCode())) {
                    callBackEC(pojo.getEntId(), pojo.getSerialNum());
                }
            }
        } 
    }
    
    /**
     * 更新充值状态
     *
     * @param systemNum
     * @param chargeResult
     * @return
     */
    protected boolean updateChargeRecord(String systemNum, ChargeResult chargeResult) {
        ChargeRecord record;
        if (StringUtils.isBlank(systemNum)
                || chargeResult == null
                || (record = chargeRecordService.getRecordBySN(systemNum)) == null) {
            return false;
        }
        String statusCode = null;
        if(StringUtils.isEmpty(chargeResult.getStatusCode())){
            statusCode = ChargeResult.ChargeMsgCode.completed.getCode();
        }
        LOGGER.info("更新StatusCode为:{}", statusCode);
        chargeResult.setStatusCode(statusCode);
        return chargeRecordService.updateStatus(record.getId(), chargeResult);
    }
    
    /**
     * 回调EC
     *
     * @param entId
     * @param systemSerialNum
     */
    protected void callBackEC(Long entId, String systemSerialNum) {
        CallbackPojo pojo = new CallbackPojo();
        pojo.setEntId(entId);
        pojo.setSerialNum(systemSerialNum);
        taskProducer.productPlatformCallbackMsg(pojo);
    }
}

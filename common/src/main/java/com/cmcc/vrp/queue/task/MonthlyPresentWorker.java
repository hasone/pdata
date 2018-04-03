package com.cmcc.vrp.queue.task;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.province.dao.MonthlyPresentRecordMapper;
import com.cmcc.vrp.province.dao.MonthlyPresentRuleMapper;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.MonthlyPresentRecord;
import com.cmcc.vrp.province.model.MonthlyPresentRule;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.MonthlyPresentRuleService;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.queue.task.strategy.ChargeRecordSerialNum;
import com.cmcc.vrp.queue.task.strategy.Info;

/**
 * 月赠送任务类，支持单条月赠送记录的赠送 赠送包括前置检查 与boss系统发送请求 更新赠送记录状态
 *
 * @author qh
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MonthlyPresentWorker extends Worker {

    protected final Logger LOGGER = LoggerFactory.getLogger(MonthlyPresentWorker.class);
    
    @Autowired
    MonthlyPresentRecordMapper monthlyPresentRecordMapper;

    @Autowired
    MonthlyPresentRuleMapper monthlyPresentRuleMapper;
    
    @Autowired
    MonthlyPresentRecordService monthlyPresentRecordService;
    
    @Autowired
    MonthlyPresentRuleService monthlyPresentRuleService;
    
    @Autowired
    ApplicationContext applicationContext;
    
    @Autowired
    EnterprisesService enterprisesService;
    
    PresentWorkerStrategy presentWorkerStrategy;
    
    @PostConstruct
    public void init(){
        presentWorkerStrategy = applicationContext.getBean(PresentWorkerStrategy.class);
        presentWorkerStrategy.setActivityType(ActivityType.MONTHLY_PRESENT);
        presentWorkerStrategy.setChargeType(ChargeType.MONTHLY_TASK);
    }
    
    @Override
    public void exec() {
        //块赠送， 批量更新、批量扣钱、依次向BOSS发起请求、批量更新
        String taskString = getTaskString();
        List<PresentPojo> pojos = presentWorkerStrategy.getPresentPojos(taskString);
        MonthlyPresentRule presentRule = null;
        Enterprise enterprise = null;
        if (pojos == null || pojos.isEmpty()
                || (presentRule = monthlyPresentRuleMapper.selectByPrimaryKey(pojos.get(0).getRuleId())) == null
                || presentRule.getMonthCount() == null) {
            LOGGER.error("无效的批量赠送参数或赠送规则, 赠送参数为{}.", taskString);
            return;
        }
        
        //包月赠送需要自行填充充值的个数 @luozuwu
        addCountToPojos(pojos,presentRule);
        
        //构建充值记录
        String activityName = "default" + presentRule.getId();//默认名称，合并代码之后以后进行修改
        List<ChargeRecord> crs = presentWorkerStrategy.buildChargeRecords(pojos, activityName);
        List<ChargeRecordSerialNum> crsns = presentWorkerStrategy.buildSerialNums(crs);
        List<Info> infos = presentWorkerStrategy.buildInfos(crs);
        
        if (presentRule.getEntId() == null
                || (enterprise = enterprisesService.selectById(presentRule.getEntId())) == null
                || enterprise.getDeleteFlag() != 0
                || (enterprise.getStartTime() != null && enterprise.getStartTime().after(new Date()))
                || (enterprise.getEndTime() != null && (new Date()).after(enterprise.getEndTime()))) {
            
            LOGGER.error("企业状态异常或企业合作时间已过期，赠送参数为{}", taskString);
            
            //批量赠送更新状态，等待service代码完成后更新
            if (!monthlyPresentRecordService.batchUpdateChargeResult(buildPrs(infos))) {
                LOGGER.error("批量更新赠送记录和充值记录时失败. 赠送参数为{}.", taskString);
            } else {
                //现在要给企业退钱了（充值失败的那部分）！在退钱的同时更新charge_record记录
                presentWorkerStrategy.refund(presentWorkerStrategy.getFailInfo(infos));
            }
            
            monthlyPresentRuleService.updateRuleStatusFini();
            return;
        }

        infos = presentWorkerStrategy.batchCharge(crsns, activityName);

        //如果入队列失败则更新赠送记录
        if (infos != null && infos.size() > 0) {
            LOGGER.error("批量赠送入队没有全部成功, 开始更新赠送记录并退钱. 赠送参数为{}.", taskString);
                        
            //批量赠送更新状态，等待service代码完成后更新
            if (!monthlyPresentRecordService.batchUpdateChargeResult(buildPrs(infos))) {
                LOGGER.error("批量更新赠送记录和充值记录时失败. 赠送参数为{}.", taskString);
            } else {
                //现在要给企业退钱了（充值失败的那部分）！在退钱的同时更新charge_record记录
                presentWorkerStrategy.refund(presentWorkerStrategy.getFailInfo(infos));
            }
            
            monthlyPresentRuleService.updateRuleStatusFini();
        } 
    }
    
    /**
     * addCountToPojos
     */
    private void addCountToPojos(List<PresentPojo> pojos,MonthlyPresentRule presentRule){
        for(PresentPojo pojo : pojos){
            pojo.setCount(presentRule.getMonthCount());
        }
    }
    
    /**
     * 根据充值结果构造批量赠送的更新状态对象列表
     */
    protected List<MonthlyPresentRecord> buildPrs(List<Info> infos) {
        List<MonthlyPresentRecord> prs = new LinkedList<MonthlyPresentRecord>();

        for (Info info : infos) {
            MonthlyPresentRecord pr = new MonthlyPresentRecord();

            pr.setId(info.getChargeRecord().getRecordId());
            pr.setOperateTime(new Date());

            ChargeResult chargeResult = info.getChargeResult();
            if (chargeResult.isSuccess()) {
                if (chargeResult.getCode() == ChargeResult.ChargeResultCode.PROCESSING) {
                    pr.setStatus(ChargeRecordStatus.PROCESSING.getCode().byteValue());
                    pr.setErrorMessage(ChargeRecordStatus.PROCESSING.getMessage());
                } else {
                    pr.setStatus(ChargeRecordStatus.COMPLETE.getCode().byteValue());
                    pr.setErrorMessage(ChargeRecordStatus.COMPLETE.getMessage());
                }
            } else {
                pr.setStatus(ChargeRecordStatus.FAILED.getCode().byteValue());
                pr.setErrorMessage(chargeResult.getFailureReason());
            }

            prs.add(pr);
        }

        return prs;
    }

}
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
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.PresentRuleService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.queue.task.strategy.ChargeRecordSerialNum;
import com.cmcc.vrp.queue.task.strategy.Info;

/**
 * 执行批量赠送（包括单个赠送）的worker类
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BatchPresentWorker extends Worker {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchPresentWorker.class);
    
    PresentWorkerStrategy presentWorkerStrategy;

    @Autowired
    PresentRecordService presentRecordService;

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

    @PostConstruct
    public void init(){
        presentWorkerStrategy = applicationContext.getBean(PresentWorkerStrategy.class);
        presentWorkerStrategy.setActivityType(ActivityType.GIVE);
        presentWorkerStrategy.setChargeType(ChargeType.PRESENT_TASK);
    }
    
    @Override
    public void exec() {
        //块赠送， 批量更新、批量扣钱、依次向BOSS发起请求、批量更新
        String taskString = getTaskString();
        List<PresentPojo> pojos = presentWorkerStrategy.getPresentPojos(taskString);
        PresentRule presentRule = null;
        if (pojos == null || pojos.isEmpty()
                || (presentRule = presentRuleService.selectByPrimaryKey(pojos.get(0).getRuleId())) == null) {
                LOGGER.error("无效的批量赠送参数或赠送规则, 赠送参数为{}.", taskString);
                return;
        }
        
        //构建充值记录
        String activityName = presentRule.getaName();
        //String activityName = "default" + presentRule.getId();//默认名称，合并代码之后以后进行修改
        List<ChargeRecord> crs = presentWorkerStrategy.buildChargeRecords(pojos, activityName);
        List<ChargeRecordSerialNum> crsns = presentWorkerStrategy.buildSerialNums(crs);
        List<Info> infos = presentWorkerStrategy.buildInfos(crs);

        infos = presentWorkerStrategy.batchCharge(crsns, activityName);

        //如果入队列失败则更新赠送记录
        if (infos != null && infos.size() > 0) {
            LOGGER.error("批量赠送入队没有全部成功, 开始更新赠送记录并退钱. 赠送参数为{}.", taskString);
                        
            //批量赠送更新状态，等待service代码完成后更新
            if (!presentRecordService.batchUpdateChargeResult(buildPrs(infos))) {
                LOGGER.error("批量更新赠送记录和充值记录时失败. 赠送参数为{}.", taskString);
            } else {
                //现在要给企业退钱了（充值失败的那部分）！在退钱的同时更新charge_record记录
                presentWorkerStrategy.refund(presentWorkerStrategy.getFailInfo(infos));
            }
        }
        
     /*   //块赠送， 批量更新、批量扣钱、依次向BOSS发起请求、批量更新
        String taskString = getTaskString();
        List<PresentPojo> pojos = getPresentPojos(taskString);
        PresentRule presentRule = null;
        if (pojos == null || pojos.isEmpty()
            || (presentRule = presentRuleService.selectByPrimaryKey(pojos.get(0).getRuleId())) == null) {
            LOGGER.error("无效的批量赠送参数或赠送规则, 赠送参数为{}.", taskString);
            return;
        }

        //构建充值记录
        String activityName = presentRule.getaName();
        List<ChargeRecord> crs = buildChargeRecords(pojos, activityName);
        List<ChargeRecordSerialNum> crsns = buildSerialNums(crs);
        List<Info> infos = buildInfos(crs);

        infos = batchCharge(crsns, activityName);

        //如果入队列失败则更新赠送记录
        if (infos != null && infos.size() > 0) {
            LOGGER.error("批量赠送入队没有全部成功, 开始更新赠送记录并退钱. 赠送参数为{}.", taskString);
            if (!presentRecordService.batchUpdateChargeResult(buildPrs(infos))) {
                LOGGER.error("批量更新赠送记录和充值记录时失败. 赠送参数为{}.", taskString);
            } else {
                //现在要给企业退钱了（充值失败的那部分）！在退钱的同时更新charge_record记录
                refund(getFailInfo(infos));
            }
        }*/
    }
    

    /**
     * 根据充值结果构造批量赠送的更新状态对象列表
     */
    protected List<PresentRecord> buildPrs(List<Info> infos) {
        List<PresentRecord> prs = new LinkedList<PresentRecord>();

        for (Info info : infos) {
            PresentRecord pr = new PresentRecord();

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
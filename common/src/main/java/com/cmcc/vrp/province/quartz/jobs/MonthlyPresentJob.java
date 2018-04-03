/**
 *
 */
package com.cmcc.vrp.province.quartz.jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.MinusCountReturnType;
import com.cmcc.vrp.enums.SchedulerType;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.MonthlyPresentRecord;
import com.cmcc.vrp.province.model.MonthlyPresentRecordCopy;
import com.cmcc.vrp.province.model.MonthlyPresentRule;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordCopyService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.MonthlyPresentRuleService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.BlockPresentPojo;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;


/**
 * 包月赠送定时任务
 * @author lgk8023
 *
 */
public class MonthlyPresentJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(MonthlyPresentJob.class);

    @Autowired
    AccountService accountService;
    @Autowired
    MonthlyPresentRecordCopyService monthlyPresentRecordCopyService;
    @Autowired
    MonthlyPresentRuleService monthlyPresentRuleService;
    @Autowired
    MonthlyPresentRecordService monthlyPresentRecordService;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    RedisUtilService redisService;
    @Autowired
    CacheService cacheService;
    @Autowired
    PresentSerialNumService presentSerialNumService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("进入包月赠送定时任务");

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jsonStr = (String) jobDataMap.get("param");
        logger.info("请求参数：" + jsonStr);
        
        //定时任务是否已经被读取
        MonthlyPresentJobPojo pojo = check(jsonStr);//检查参数
        String key = pojo.getRuleId().toString() + "." + pojo.getCount().toString() + getKeyPrefix();
        if (!StringUtils.isEmpty(cacheService.get(key))) {
            logger.error("redis存在{}，说明该定时任务已被读取", key);
            return;
        } else {
            cacheService.getIncrOrUpdate(key, 5 * 60);
            logger.info(cacheService.get(key));
        }
        
        MonthlyPresentRule rule = monthlyPresentRuleService.getDetailByRuleId(pojo.getRuleId());
        if (rule == null) {
            logger.error("根据ruleId获取赠送信息失败-{}", pojo.getRuleId());
            return;
        }
        Integer count = pojo.getCount();
        if (rule.getGivenCount() < count
                && count <= rule.getMonthCount()
                && rule.getGivenCount() < rule.getMonthCount()) {
            
            //创建下一次定时任务
            if (count < rule.getMonthCount()) {
                Date beginTime = DateUtil.getAfterFewMonths(new Date(), 1);
                beginTime = DateUtil.addMins(beginTime, 2 * 60);
                if ("ON".equals(globalConfigService.get(GlobalConfigKeyEnum.SD_RECONCILE_FOR_TEST.getKey()))) {
                    beginTime = DateUtil.addMins(new Date(), 5);
                }
                Integer nextCount = count + 1;
                if (!createMonthlyPresentSchedule(rule, beginTime, nextCount)) {
                    logger.info("生成定时任务失败" + beginTime);
                }   
            }
            MonthlyPresentRule monthlyPresentRule = new MonthlyPresentRule();
            monthlyPresentRule.setId(rule.getId());
            monthlyPresentRule.setGivenCount(count);
            monthlyPresentRule.setUpdateTime(new Date());
            if (!monthlyPresentRuleService.updateByPrimaryKeySelective(monthlyPresentRule)) {
                String errMsg = "包月赠送活动失败，更新活动记录失败:" + new Gson().toJson(monthlyPresentRule);
                logger.info(errMsg);
                return;
            }
            List<MonthlyPresentRecordCopy> monthlyPresentRecordCopyList = 
                    monthlyPresentRecordCopyService.getByRuleId(rule.getId());
            //构造当前赠送记录
            List<MonthlyPresentRecord> monthlyPresentRecordList = buildMonthlyPresentRecords(rule, monthlyPresentRecordCopyList, count);
            
            //生成赠送记录
            if (!monthlyPresentRecordService.batchInsert(monthlyPresentRecordList)) {
                String errMsg = "生成包月赠送充值记录失败:" + new Gson().toJson(monthlyPresentRecordList);
                logger.info(errMsg);
                return;
            }
            
            //校验账户信息
            String serialNum = SerialNumGenerator.buildSerialNum();
            MinusCountReturnType checkType = accountService.minusCount(rule.getEntId(), rule.getPrdId(),
                    AccountType.ENTERPRISE, (double) rule.getTotal(), serialNum, "包月赠送", false);
            if (!checkType.equals(MinusCountReturnType.OK)) {
                logger.info("扣减账户余额失败");
                if (!monthlyPresentRecordService.batchUpdateChargeResult(buildPrs(monthlyPresentRecordList))) {
                    logger.error("批量更新赠送记录时失败. 赠送参数为{}.", new Gson().toJson(monthlyPresentRecordList));
                }
                monthlyPresentRuleService.updateRuleStatusFini();
                return;
            }
            List<SerialNum> serialNums = buildSerialNums(monthlyPresentRecordList);
            if (!serialNumService.batchInsert(serialNums)) {
                String errMsg = "包月赠送生成serialNum记录失败:" + new Gson().toJson(monthlyPresentRecordList);
                logger.info(errMsg);
                return;
            }
            /**
             * 插入批量更新记录
             */
            List<ChargeRecord> crList = buildChargeRecords(monthlyPresentRecordList, rule);
            if (!chargeRecordService.batchInsert(crList)) {
                String errMsg = "插入充值记录时出错." + new Gson().toJson(crList);
                logger.info(errMsg);
                return;
            }
            //将充值对象放入队列中
            List<BlockPresentPojo> pojos = buildPojos(rule.getEntId(), monthlyPresentRecordList,
                    serialNum);

            if (!taskProducer.produceMonthPresentMsg(pojos)) {
                String errMsg = "包月赠送活动失败，进队列失败:" + new Gson().toJson(pojos);
                logger.info(errMsg);
                return;
            }

            List<Long> ids = new ArrayList<Long>();
            for (MonthlyPresentRecord record : monthlyPresentRecordList) {
                ids.add(record.getId());
            }

            /**
             * 入业务队列及更新
             */
            List<String> pltSns = parseSns(pojos);
            if (!presentSerialNumService.batchInsert(serialNum, pltSns)) {
                logger.error("批量插入流水号关联关系时失败，流水号为{}, 赠送参数为{}.", serialNum, new Gson().toJson(pojos));
                return;
            }
            if (!monthlyPresentRecordService.batchUpdateStatusCode(ids, ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                String errMsg = "包月赠送活动失败，更新赠送记录状态码失败:" + new Gson().toJson(ids);
                logger.info(errMsg);
                return;
            }

            if (!chargeRecordService.batchUpdateStatusCode(ChargeResult.ChargeMsgCode.businessQueue.getCode(),
                    pltSns)) {
                String errMsg = "包月赠送活动失败，更新充值记录失败:" + new Gson().toJson(pojos);
                logger.info(errMsg);
                return;
            }
            //校验通过，没有错误信息
            return;
        }
    }

    private List<MonthlyPresentRecord> buildPrs(List<MonthlyPresentRecord> monthlyPresentRecordList) {
        List<MonthlyPresentRecord> prs = new LinkedList<MonthlyPresentRecord>();

        for (MonthlyPresentRecord monthlyPresentRecord : monthlyPresentRecordList) {
            MonthlyPresentRecord pr = new MonthlyPresentRecord();
            pr.setId(monthlyPresentRecord.getId());
            pr.setOperateTime(new Date());
            pr.setStatus(ChargeRecordStatus.FAILED.getCode().byteValue());
            pr.setErrorMessage("余额不足");
            prs.add(pr);
        }

        return prs;
    }

    /**
     * 检查传入参数
     *
     * @return
     */
    private MonthlyPresentJobPojo check(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            logger.error("参数为空");
            return null;
        }
        MonthlyPresentJobPojo pojo = JSON.parseObject(jsonStr, MonthlyPresentJobPojo.class);
        if (pojo == null || pojo.getRuleId() == null) {
            logger.error("参数为空");
            return null;
        }
        return pojo;
    }
    private List<SerialNum> buildSerialNums(List<MonthlyPresentRecord> records) {
        List<SerialNum> serialNums = new ArrayList();
        for (MonthlyPresentRecord record : records) {
            SerialNum serialNum = new SerialNum();
            serialNum.setPlatformSerialNum(record.getSysSerialNum());
            serialNum.setCreateTime(new Date());
            serialNum.setUpdateTime(new Date());
            serialNum.setDeleteFlag(0);
            serialNums.add(serialNum);
        }
        return serialNums;
    }
    private List<MonthlyPresentRecord> buildMonthlyPresentRecords(MonthlyPresentRule rule, 
            List<MonthlyPresentRecordCopy> monthlyPresentRecordCopyList, Integer count) {
        List<MonthlyPresentRecord> monthlyPresentRecordList = new LinkedList<MonthlyPresentRecord>();
        for (MonthlyPresentRecordCopy record:monthlyPresentRecordCopyList) {
            //赠送记录
            MonthlyPresentRecord monthlyPresentRecord = new MonthlyPresentRecord();
            monthlyPresentRecord.setRuleId(rule.getId());
            monthlyPresentRecord.setPrdId(record.getPrdId());
            monthlyPresentRecord.setMobile(record.getMobile());
            monthlyPresentRecord.setStatus(ChargeRecordStatus.WAIT.getCode().byteValue());
            monthlyPresentRecord.setEffectType(0);
            //monthlyPresentRecord.setStatusCode("");
            //monthlyPresentRecord.setErrorMessage("");
            monthlyPresentRecord.setCreateTime(new Date());
            monthlyPresentRecord.setOperateTime(new Date());
            monthlyPresentRecord.setSysSerialNum(SerialNumGenerator.buildSerialNum());
            monthlyPresentRecord.setBossSerialNum("");
            monthlyPresentRecord.setGiveMonth(count);
            monthlyPresentRecordList.add(monthlyPresentRecord);
        }
        return monthlyPresentRecordList;
    }
    private List<ChargeRecord> buildChargeRecords(List<MonthlyPresentRecord> prList, MonthlyPresentRule presentRule) {
        List<ChargeRecord> crList = new ArrayList<ChargeRecord>();

        for (MonthlyPresentRecord pr : prList) {
            ChargeRecord cr = new ChargeRecord();
            cr.setEnterId(presentRule.getEntId());
            cr.setPrdId(pr.getPrdId());
            cr.setStatus(ChargeRecordStatus.WAIT.getCode());
            cr.setType(ActivityType.MONTHLY_PRESENT.getname());
            cr.setaName(presentRule.getActivityName());
            cr.setTypeCode(ActivityType.MONTHLY_PRESENT.getCode());
            cr.setPhone(pr.getMobile());
            cr.setRecordId(pr.getId());
            cr.setSystemNum(pr.getSysSerialNum());
            cr.setChargeTime(new Date());
            cr.setEffectType(pr.getEffectType());
            cr.setCount(presentRule.getMonthCount());
            crList.add(cr);
        }
        return crList;
    }
    private List<BlockPresentPojo> buildPojos(Long entId, List<MonthlyPresentRecord> records, String serialNum) {
        List<BlockPresentPojo> pojos = new LinkedList<BlockPresentPojo>();

        int blockSize = getBatchBlockSize();
        int size = records.size();
        int index = 0;

        //分块构建
        while (index + blockSize <= size) {
            pojos.add(buildPojo(records.subList(index, index + blockSize), entId, serialNum));
            index += blockSize;
        }

        //最后那一点点
        pojos.add(buildPojo(records.subList(index, size), entId, serialNum));
        return pojos;
    }
    public int getBatchBlockSize() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BATCH_PRESENT_BLOCK_SIZE.getKey()), 50);
    }
    private BlockPresentPojo buildPojo(List<MonthlyPresentRecord> records, Long entId, String serialNum) {
        List<PresentPojo> pojos = new LinkedList<PresentPojo>();
        for (MonthlyPresentRecord record : records) {
            PresentPojo pojo = new PresentPojo();
            pojo.setMobile(record.getMobile());
            pojo.setRecordId(record.getId());
            pojo.setEnterpriseId(entId);
            pojo.setProductId(record.getPrdId());
            pojo.setRuleId(record.getRuleId());
            pojo.setRequestSerialNum(record.getSysSerialNum());

            pojos.add(pojo);
        }

        return buildBlockPresentPojo(pojos, serialNum);
    }
    private BlockPresentPojo buildBlockPresentPojo(List<PresentPojo> pojos, String serialNum) {
        BlockPresentPojo blockPresentPojo = new BlockPresentPojo();
        blockPresentPojo.setSerialNum(serialNum);
        blockPresentPojo.setPojos(pojos);

        return blockPresentPojo;
    }
    private List<String> parseSns(List<BlockPresentPojo> pojos) {
        List<String> sns = new LinkedList<String>();
        for (BlockPresentPojo blockPresentPojo : pojos) {
            for (PresentPojo presentPojo : blockPresentPojo.getPojos()) {
                sns.add(presentPojo.getRequestSerialNum());
            }
        }

        return sns;
    }
    private boolean createMonthlyPresentSchedule(MonthlyPresentRule rule, Date beginTime, Integer count) {
        MonthlyPresentJobPojo pojo = new MonthlyPresentJobPojo(rule.getId(), count);
        String jsonStr = JSON.toJSONString(pojo);

        // 创建包月赠送定时任务
        String msg = scheduleService.createScheduleJob(MonthlyPresentJob.class,
                SchedulerType.MONTHLY_PRESENT.getCode(), jsonStr, rule.getId().toString(),
                beginTime);
        logger.info("创建定时任务-{}", msg);
        if ("success".equals(msg)) {
            return true;
        }

        return false;
    }
    private String getKeyPrefix() {
        return".monthlypPresent";
    }
}

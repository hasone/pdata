//package com.cmcc.vrp.province.quartz.jobs;
//
//import com.alibaba.fastjson.JSON;
//import com.cmcc.vrp.ec.utils.SerialNumGenerator;
//import com.cmcc.vrp.enums.MonthRuleStatus;
//import com.cmcc.vrp.province.dao.MonthlyPresentRecordMapper;
//import com.cmcc.vrp.province.dao.MonthlyPresentRuleMapper;
//import com.cmcc.vrp.province.model.MonthlyPresentRecord;
//import com.cmcc.vrp.province.model.MonthlyPresentRule;
//import com.cmcc.vrp.queue.TaskProducer;
//import com.cmcc.vrp.queue.pojo.PresentPojo;
//import org.apache.commons.lang.StringUtils;
//import org.quartz.Job;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 月赠送规则定时任务
// *
// * @author zhoujianbin
// */
//public class MonthlyJobs implements Job {
//
//    private static Logger logger = LoggerFactory.getLogger(MonthlyJobs.class);
//
//    @Autowired
//    TaskProducer producer;
//    @Autowired
//    private MonthlyPresentRecordMapper monthlyPresentRecordMapper;
//    @Autowired
//    private MonthlyPresentRuleMapper monthlyPresentRuleMapper;
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//
//        logger.info("进入月赠送定时任务");
//
//        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
//        String jsonStr = (String) jobDataMap.get("param");
//
//        logger.info("请求参数：" + jsonStr);
//        JobPojo pojo = check(jsonStr);//检查参数
//
//        MonthlyPresentRule monthlyPresentRule = monthlyPresentRuleMapper.selectByPrimaryKey(pojo.getRuleId());
//        if (monthlyPresentRule == null || MonthRuleStatus.DELETED.getCode().equals(monthlyPresentRule.getStatus())) {
//            logger.info("月赠送记录缺失或已删除");
//            return;
//        }
//
//        List<MonthlyPresentRecord> records = record(pojo);//获取记录
//        if (records == null) {
//            logger.error("月赠送记录缺失[" + jsonStr + "]");
//            return;
//        }
//
//        produce(records, pojo);//放入队列
//    }
//
//    /**
//     * 检查传入参数
//     *
//     * @return
//     */
//    private JobPojo check(String jsonStr) {
//        if (StringUtils.isBlank(jsonStr)) {
//            logger.error("参数为空");
//            return null;
//        }
//        JobPojo pojo = JSON.parseObject(jsonStr, JobPojo.class);
//        if (pojo == null || pojo.getRuleId() == null || pojo.getRuleId().compareTo(0L) <= 0
//            || pojo.getSerialNumber() == null || pojo.getSerialNumber().compareTo(0) <= 0) {
//            logger.error("参数为空");
//            return null;
//        }
//        return pojo;
//    }
//
//    /**
//     * 获取记录
//     *
//     * @param pojo
//     * @return
//     */
//    private List<MonthlyPresentRecord> record(JobPojo pojo) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("ruleId", pojo.getRuleId());
//        map.put("serialNumber", pojo.getSerialNumber());
//
//        return monthlyPresentRecordMapper.queryList(map);
//    }
//
//    /**
//     * 封装任务并放入队列
//     *
//     * @param records
//     * @return
//     */
//    private String produce(List<MonthlyPresentRecord> records, JobPojo pojo) {
//        PresentPojo tempPojo = null;
//        MonthlyPresentRecord temp = null;
//
//        List<PresentPojo> pojoList = new ArrayList<PresentPojo>(records.size());
//
//        for (int i = 0; i < records.size(); i++) {
//            temp = records.get(i);
//            tempPojo = new PresentPojo();
//            tempPojo.setRuleId(temp.getRuleId());
//            tempPojo.setMobile(temp.getMobile());
//            tempPojo.setRecordId(temp.getId());
//            tempPojo.setRequestSerialNum(SerialNumGenerator.buildSerialNum());
//            pojoList.add(tempPojo);
//        }
//
//        if (producer.produceMonthlyPresentMsg(pojoList)) {//放入队列中去
//            logger.info("包月赠送任务已成功放入队列中.");
//        } else {
//            logger.error("包月赠送任务放入队列中时出错.");
//        }
//
//        if (pojo.getIsLastGroup()) {//最后一批发送完成后更新rule状态
//            MonthlyPresentRule rule = new MonthlyPresentRule();
//            rule.setStatus(MonthRuleStatus.COMPLETE.getCode());
//            rule.setId(pojo.getRuleId());
//            monthlyPresentRuleMapper.updateByPrimaryKeySelective(rule);
//        }
//        return null;
//    }
//}
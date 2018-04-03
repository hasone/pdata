package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.MinusCountReturnType;
import com.cmcc.vrp.enums.SchedulerType;
import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.dao.MonthlyPresentRuleMapper;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.MonthlyPresentRecord;
import com.cmcc.vrp.province.model.MonthlyPresentRecordCopy;
import com.cmcc.vrp.province.model.MonthlyPresentRule;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.quartz.jobs.MonthlyPresentJob;
import com.cmcc.vrp.province.quartz.jobs.MonthlyPresentJobPojo;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordCopyService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.MonthlyPresentRuleService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.BlockPresentPojo;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;
import com.google.gson.Gson;

/**
 * 
 * @ClassName: MonthlyPresentRuleServiceImpl 
 * @Description: 包月赠送活动服务类
 * @author: Rowe
 * @date: 2017年7月5日 上午9:18:55
 */
@Service
public class MonthlyPresentRuleServiceImpl implements MonthlyPresentRuleService {

    private static Logger logger = LoggerFactory.getLogger(MonthlyPresentRuleServiceImpl.class);

    @Autowired
    MonthlyPresentRuleMapper monthlyPresentRuleMapper;

    @Autowired
    MonthlyPresentRecordService monthlyPresentRecordService;

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    ProductService productService;

    @Autowired
    AccountService accountService;

    @Autowired
    TaskProducer taskProducer;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    VirtualProductService virtualProductService;

    @Autowired
    SerialNumService serialNumService;
    
    @Autowired
    ActivityCreatorService activityCreatorService;
    @Autowired
    MonthlyPresentRecordCopyService monthlyPresentRecordCopyService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PresentSerialNumService presentSerialNumService;
    @Override
    public MonthlyPresentRule selectByPrimaryKey(Long id) {
        return monthlyPresentRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean insert(MonthlyPresentRule rule) {
        if (rule == null) {
            return false;
        }
        return monthlyPresentRuleMapper.insertSelective(rule) == 1;
    }

    @Override
    public List<MonthlyPresentRule> getRules(QueryObject queryObject) {
        return monthlyPresentRuleMapper.getRules(queryObject.toMap());
    }

    @Override
    public Long countRules(QueryObject queryObject) {
        return monthlyPresentRuleMapper.countRules(queryObject.toMap());
    }

    @Override
    public boolean updateByPrimaryKeySelective(MonthlyPresentRule rule) {
        return monthlyPresentRuleMapper.updateByPrimaryKeySelective(rule) == 1;
    }

    @Override
    public String create(MonthlyPresentRule rule) {
        if (rule == null) {
            return "参数错误";
        }
        //所有参数校验通过，没有错误信息返回
        Product product = productService.selectProductById(rule.getPrdId());
        Enterprise enterprise = null;
        //校验企业和产品
        if ((enterprise = enterprisesService.selectById(rule.getEntId())) == null) {
            logger.info("企业 ID :" + rule.getEntId() + "不存在！");
            return "参数错误：企业不存在";
        } else if (enterprise.getDeleteFlag() != 0
                || (enterprise.getStartTime() != null && enterprise.getStartTime().after(new Date()))
                || (enterprise.getEndTime() != null && (new Date()).after(enterprise.getEndTime()))) {
            logger.info("企业状态异常或企业合作时间已过期，企业对象：" + JSON.toJSONString(enterprise));
            return "参数错误：企业状态异常";
        } else if (entProductService.selectByProductIDAndEnterprizeID(rule.getPrdId(), rule.getEntId()) == null) {
            logger.info("企业 ID :" + rule.getEntId() + " 尚未订购产品ID: " + rule.getPrdId());
            return "参数错误:企业未订购该产品";
        }
        if (product == null) {
            logger.info("产品ID: " + rule.getPrdId() + "不存在");
            return "参数错误：产品不存在";
        }

        //校验产品类型，衍生出虚拟产品
        try {
            product = virtualProductService.initProcess(rule.getPrdId(),
                    transferMiniUnit(product.getType().byteValue(), rule.getSize()));
        } catch (ProductInitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.getMessage();
        }

        //校验账户信息
        String serialNum = SerialNumGenerator.buildSerialNum();
        MinusCountReturnType checkType = accountService.minusCount(rule.getEntId(), product.getId(),
                AccountType.ENTERPRISE, (double) rule.getMonthCount() * rule.getTotal(), serialNum, "包月赠送", false);
        if (!checkType.equals(MinusCountReturnType.OK)) {
            return checkType.getMsg();
        }

        //更新产品信息
        rule.setPrdId(product.getId());
        //更新赠送月数
        rule.setGivenCount(rule.getMonthCount());

        //生成活动记录
        if (monthlyPresentRuleMapper.insertSelective(rule) != 1) {
            String errMsg = "生成包月赠送活动失败:" + new Gson().toJson(rule);
            logger.info(errMsg);
            return "生成包月赠送活动失败";
        }

        //构造赠送记录
        List<MonthlyPresentRecord> monthlyPresentRecordList = buildMonthlyPresentRecords(rule);

        //生成赠送记录
        if (!monthlyPresentRecordService.batchInsert(monthlyPresentRecordList)) {
            String errMsg = "生成包月赠送充值记录失败:" + new Gson().toJson(monthlyPresentRecordList);
            logger.info(errMsg);
            return "生成包月赠送充值记录失败";
        }

        //批量生成serialNum
        List<SerialNum> serialNums = buildSerialNums(monthlyPresentRecordList);
        if (!serialNumService.batchInsert(serialNums)) {
            String errMsg = "包月赠送生成serialNum记录失败:" + new Gson().toJson(monthlyPresentRecordList);
            logger.info(errMsg);
            return "包月赠送生成serialNum记录失败";
        }

        /**
         * 插入批量更新记录
         */
        List<ChargeRecord> crList = buildChargeRecords(monthlyPresentRecordList, rule);
        if (!chargeRecordService.batchInsert(crList)) {
            String errMsg = "插入充值记录时出错." + new Gson().toJson(crList);
            logger.info(errMsg);
            return "插入充值记录时出错";
        }

        //将充值对象放入队列中
        List<BlockPresentPojo> pojos = buildPojos(rule.getEntId(), monthlyPresentRecordList,
                serialNum);

        if (!taskProducer.produceMonthPresentMsg(pojos)) {
            String errMsg = "包月赠送活动失败，进队列失败:" + new Gson().toJson(pojos);
            logger.info(errMsg);
            return "包月赠送活动失败，进队列失败";
        }

        List<Long> ids = new ArrayList<Long>();
        for (MonthlyPresentRecord pojo : monthlyPresentRecordList) {
            ids.add(pojo.getId());
        }

        /**
         * 入业务队列及更新
         */
        List<String> pltSns = parseSns(pojos);
        if (!presentSerialNumService.batchInsert(serialNum, pltSns)) {
            logger.error("批量插入流水号关联关系时失败，流水号为{}, 赠送参数为{}.", serialNum, new Gson().toJson(pojos));
            return "包月赠送活动失败，更新赠送流水号失败";
        }
        
        if (!monthlyPresentRecordService.batchUpdateStatusCode(ids, ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
            String errMsg = "包月赠送活动失败，更新赠送记录状态码失败:" + new Gson().toJson(ids);
            logger.info(errMsg);
            return "包月赠送活动失败，更新赠送记录状态码失败";
        }

        if (!chargeRecordService.batchUpdateStatusCode(ChargeResult.ChargeMsgCode.businessQueue.getCode(),
                pltSns)) {
            String errMsg = "包月赠送活动失败，更新充值记录失败:" + new Gson().toJson(pojos);
            logger.info(errMsg);
            return "包月赠送活动失败，更新充值记录失败";
        }
        
        if (!activityCreatorService.insert(ActivityType.MONTHLY_PRESENT, rule.getId(), rule.getCreatorId())){
            logger.info("包月赠送活动失败，插入活动创建者表失败,id="+rule.getId());
            return "包月赠送活动失败，插入活动创建者表失败";
        }

        //校验通过，没有错误信息
        return null;
    }

    /**
     * 
     * @param pojos
     * @return
     */
    private List<String> parseSns(List<BlockPresentPojo> pojos) {
        List<String> sns = new LinkedList<String>();
        for (BlockPresentPojo blockPresentPojo : pojos) {
            for (PresentPojo presentPojo : blockPresentPojo.getPojos()) {
                sns.add(presentPojo.getRequestSerialNum());
            }
        }

        return sns;
    }

    /**
     * 
     * @Title: buildMonthlyPresentRecords 
     * @Description: 构建赠送记录
     * @param rule
     * @return
     * @return: List<MonthlyPresentRecord>
     */
    private List<MonthlyPresentRecord> buildMonthlyPresentRecords(MonthlyPresentRule rule) {
        List<MonthlyPresentRecord> monthlyPresentRecordList = new LinkedList<MonthlyPresentRecord>();
        for (int i = 0; i < rule.getPhonesList().size(); i++) {
            //赠送记录
            MonthlyPresentRecord monthlyPresentRecord = new MonthlyPresentRecord();
            monthlyPresentRecord.setRuleId(rule.getId());
            monthlyPresentRecord.setPrdId(rule.getPrdId());
            monthlyPresentRecord.setMobile(rule.getPhonesList().get(i));
            monthlyPresentRecord.setStatus(ChargeRecordStatus.WAIT.getCode().byteValue());
            monthlyPresentRecord.setEffectType(0);
            //monthlyPresentRecord.setStatusCode("");
            //monthlyPresentRecord.setErrorMessage("");
            monthlyPresentRecord.setCreateTime(new Date());
            monthlyPresentRecord.setOperateTime(new Date());
            monthlyPresentRecord.setSysSerialNum(SerialNumGenerator.buildSerialNum());
            monthlyPresentRecord.setBossSerialNum("");
            monthlyPresentRecord.setGiveMonth(rule.getMonthCount());
            monthlyPresentRecordList.add(monthlyPresentRecord);
        }
        return monthlyPresentRecordList;
    }

    /**
     * 
     * @Title: buildPojos 
     * @Description: 按块大小进行拆分，生成块赠送的列表
     * @param entId
     * @param records
     * @param serialNum
     * @return
     * @return: List<BlockPresentPojo>
     */
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

    /**
     * 
     * @Title: buildPojo 
     * @Description: 根据记录生成块赠送对象
     * @param records
     * @param entId
     * @param serialNum
     * @return
     * @return: BlockPresentPojo
     */
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

    /**
     * 
     * @Title: buildBlockPresentPojo 
     * @Description: 构建消息
     * @param pojos
     * @param serialNum
     * @return
     * @return: BlockPresentPojo
     */
    private BlockPresentPojo buildBlockPresentPojo(List<PresentPojo> pojos, String serialNum) {
        BlockPresentPojo blockPresentPojo = new BlockPresentPojo();
        blockPresentPojo.setSerialNum(serialNum);
        blockPresentPojo.setPojos(pojos);

        return blockPresentPojo;
    }

    /**
     * 
     * @Title: getBatchBlockSize 
     * @Description: 块大小
     * @return
     * @return: int
     */
    public int getBatchBlockSize() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BATCH_PRESENT_BLOCK_SIZE.getKey()), 50);
    }

    @Override
    public MonthlyPresentRule getDetailByRuleId(Long ruleId) {
        return monthlyPresentRuleMapper.getDetailByRuleId(ruleId);
    }

    /**
     * 
     * @Title: transferMiniUnit 
     * @Description: 根据产品类型进行单位转换，转为最小单位
     * @param type
     * @param size
     * @return
     * @return: String
     */
    private String transferMiniUnit(Byte type, String size) {
        ProductType productType = ProductType.fromValue(type);
        if (productType == null) {
            return size;
        } else if (ProductType.FLOW_ACCOUNT.getValue() == productType.getValue()) {//流量池产品，MB转KB
            Long productSize = Long.parseLong(size);
            productSize = SizeUnits.MB.toKB(productSize);//MB转KB
            return productSize.toString();
        } else if (ProductType.MOBILE_FEE.getValue() == productType.getValue()) {//话费产品，元转分
            Long productSize = Long.parseLong(size);
            productSize *= 100;
            return productSize.toString();
        } else {//其他类型不做转换
            return size;
        }
    }

    /**
     * buildChargeRecords
     */
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

    /**
     * 
     * @Title: buildSerialNums 
     * @Description: TODO
     * @param records
     * @return
     * @return: List<SerialNum>
     */
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

    @Override
    public void updateRuleStatusFini() {
        monthlyPresentRuleMapper.updateRuleStatusFini();
    }

    @Override
    public String unSdcreate(MonthlyPresentRule rule) {

        if (rule == null) {
            return "参数错误";
        }
        //所有参数校验通过，没有错误信息返回
        Product product = productService.selectProductById(rule.getPrdId());
        Enterprise enterprise = null;
        //校验企业和产品
        if ((enterprise = enterprisesService.selectById(rule.getEntId())) == null) {
            logger.info("企业 ID :" + rule.getEntId() + "不存在！");
            return "参数错误：企业不存在";
        } else if (enterprise.getDeleteFlag() != 0
                || (enterprise.getStartTime() != null && enterprise.getStartTime().after(new Date()))
                || (enterprise.getEndTime() != null && (new Date()).after(enterprise.getEndTime()))) {
            logger.info("企业状态异常或企业合作时间已过期，企业对象：" + JSON.toJSONString(enterprise));
            return "参数错误：企业状态异常";
        } else if (entProductService.selectByProductIDAndEnterprizeID(rule.getPrdId(), rule.getEntId()) == null) {
            logger.info("企业 ID :" + rule.getEntId() + " 尚未订购产品ID: " + rule.getPrdId());
            return "参数错误:企业未订购该产品";
        }
        if (product == null) {
            logger.info("产品ID: " + rule.getPrdId() + "不存在");
            return "参数错误：产品不存在";
        }

        //校验产品类型，衍生出虚拟产品
        try {
            product = virtualProductService.initProcess(rule.getPrdId(),
                    transferMiniUnit(product.getType().byteValue(), rule.getSize()));
        } catch (ProductInitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.getMessage();
        }

/*        //校验账户信息
        String serialNum = SerialNumGenerator.buildSerialNum();
        MinusCountReturnType checkType = accountService.minusCount(rule.getEntId(), product.getId(),
                AccountType.ENTERPRISE, (double) rule.getMonthCount() * rule.getTotal(), serialNum, "包月赠送", false);
        if (!checkType.equals(MinusCountReturnType.OK)) {
            return checkType.getMsg();
        }*/

        //更新产品信息
        rule.setPrdId(product.getId());
        //更新赠送月数
        rule.setGivenCount(0);

        //生成活动记录
        if (monthlyPresentRuleMapper.insertSelective(rule) != 1) {
            String errMsg = "生成包月赠送活动失败:" + new Gson().toJson(rule);
            logger.info(errMsg);
            return "生成包月赠送活动失败";
        }
        if (!activityCreatorService.insert(ActivityType.MONTHLY_PRESENT, rule.getId(), rule.getCreatorId())){
            logger.info("包月赠送活动失败，插入活动创建者表失败,id="+rule.getId());
            return "包月赠送活动失败，插入活动创建者表失败";
        }

        //构造赠送记录
        List<MonthlyPresentRecordCopy> monthlyPresentRecordCopyList = buildMonthlyPresentRecordCopys(rule);

        //生成赠送记录
        if (!monthlyPresentRecordCopyService.batchInsert(monthlyPresentRecordCopyList)) {
            String errMsg = "生成包月赠送充值记录失败:" + new Gson().toJson(monthlyPresentRecordCopyList);
            logger.info(errMsg);
            return "生成包月赠送充值记录失败";
        }
        
        //判断起始时间
        Date beginTime = rule.getStartTime();
        if (beginTime.before(new Date())) {
            if (!createMonthlyPresentSchedule(rule, DateUtil.addMins(new Date(), 5))) {
                logger.info("生成定时任务失败" + beginTime);
                return "生成定时任务失败";
            }
            
        } else {
            if (!createMonthlyPresentSchedule(rule, beginTime)) {
                logger.info("生成定时任务失败" + beginTime);
                return "生成定时任务失败";
            }
        }

/*        //批量生成serialNum
        List<SerialNum> serialNums = buildSerialNums(monthlyPresentRecordList);
        if (!serialNumService.batchInsert(serialNums)) {
            String errMsg = "包月赠送生成serialNum记录失败:" + new Gson().toJson(monthlyPresentRecordList);
            logger.info(errMsg);
            return "包月赠送生成serialNum记录失败";
        }

        *//**
         * 插入批量更新记录
         *//*
        List<ChargeRecord> crList = buildChargeRecords(monthlyPresentRecordList, rule);
        if (!chargeRecordService.batchInsert(crList)) {
            String errMsg = "插入充值记录时出错." + new Gson().toJson(crList);
            logger.info(errMsg);
            return "插入充值记录时出错";
        }

        //将充值对象放入队列中
        List<BlockPresentPojo> pojos = buildPojos(rule.getEntId(), monthlyPresentRecordList,
                SerialNumGenerator.buildSerialNum());

        if (!taskProducer.produceMonthPresentMsg(pojos)) {
            String errMsg = "包月赠送活动失败，进队列失败:" + new Gson().toJson(pojos);
            logger.info(errMsg);
            return "包月赠送活动失败，进队列失败";
        }

        List<Long> ids = new ArrayList<Long>();
        for (MonthlyPresentRecord pojo : monthlyPresentRecordList) {
            ids.add(pojo.getId());
        }

        *//**
         * 入业务队列及更新
         *//*
        if (!monthlyPresentRecordService.batchUpdateStatusCode(ids, ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
            String errMsg = "包月赠送活动失败，更新赠送记录状态码失败:" + new Gson().toJson(ids);
            logger.info(errMsg);
            return "包月赠送活动失败，更新赠送记录状态码失败";
        }

        if (!chargeRecordService.batchUpdateStatusCode(ChargeResult.ChargeMsgCode.businessQueue.getCode(),
                parseSns(pojos))) {
            String errMsg = "包月赠送活动失败，更新充值记录失败:" + new Gson().toJson(pojos);
            logger.info(errMsg);
            return "包月赠送活动失败，更新充值记录失败";
        }*/

        //校验通过，没有错误信息
        return null;
    }

    private List<MonthlyPresentRecordCopy> buildMonthlyPresentRecordCopys(MonthlyPresentRule rule) {
        List<MonthlyPresentRecordCopy> monthlyPresentRecordCopyList = new LinkedList<MonthlyPresentRecordCopy>();
        for (int i = 0; i < rule.getPhonesList().size(); i++) {
            //赠送记录
            MonthlyPresentRecordCopy monthlyPresentRecord = new MonthlyPresentRecordCopy();
            monthlyPresentRecord.setRuleId(rule.getId());
            monthlyPresentRecord.setPrdId(rule.getPrdId());
            monthlyPresentRecord.setMobile(rule.getPhonesList().get(i));
            monthlyPresentRecord.setStatus(ChargeRecordStatus.WAIT.getCode().byteValue());
            monthlyPresentRecord.setEffectType(0);
            //monthlyPresentRecord.setStatusCode("");
            //monthlyPresentRecord.setErrorMessage("");
            monthlyPresentRecord.setCreateTime(new Date());
            monthlyPresentRecord.setOperateTime(new Date());
            monthlyPresentRecord.setSysSerialNum(SerialNumGenerator.buildSerialNum());
            monthlyPresentRecord.setBossSerialNum("");
            monthlyPresentRecordCopyList.add(monthlyPresentRecord);
        }
        return monthlyPresentRecordCopyList;
    }

    /**
     * @param rule
     * @return
     */
    private boolean createMonthlyPresentSchedule(MonthlyPresentRule rule, Date beginTime) {
        MonthlyPresentJobPojo pojo = new MonthlyPresentJobPojo(rule.getId(), 1);
        String jsonStr = JSON.toJSONString(pojo);

        // 创建包月赠送定时任务
        String msg = scheduleService.createScheduleJob(MonthlyPresentJob.class,
                SchedulerType.MONTHLY_PRESENT.getCode(), jsonStr, rule.getId().toString(),
                beginTime);
        logger.info("创建定时任务-" + msg);
        if ("success".equals(msg)) {
            return true;
        }

        return false;
    }
}

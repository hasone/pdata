package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.dao.PresentRecordMapper;
import com.cmcc.vrp.province.dao.PresentRuleMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.PresentRuleService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.BlockPresentPojo;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.google.gson.Gson;

/**
* <p>Title: </p>
* <p>Description: </p>
* @date 2016年12月2日 下午3:53:37
*/
@Service
public class PresentRuleServiceImp implements PresentRuleService {

    private static Logger logger = LoggerFactory.getLogger(PresentRuleService.class);

    @Autowired
    PresentRuleMapper presentRuleMapper;

    @Autowired
    PresentRecordMapper presentRecordMapper;
    @Autowired
    PresentRecordService presentRecordService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    private EnterprisesService enterprisesService;
    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private AccountService accountService;
    @Autowired
    PresentSerialNumService presentSerialNumService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    SerialNumService serialNumService;
    
    @Autowired
    ActivityCreatorService activityCreatorService;


    //批量赠送时分块处理，这里定义了块的大小
    //注意： 块的大小设置要适中，太小容易导致账户的乐观锁失败，太大容易导致账户余额不足
    private int batchBlockSize;


    public int deleteDraft(long id) {

        return presentRuleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据条件 查询规则总数
     */
    public int queryCount(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String endTime = (String) map.get("endTime");
        if (!StringUtils.isEmpty(endTime)) {
            map.put("endTime", endTime + "23:59:59");
        }
        return presentRuleMapper.selectCount(map);
    }

    /**
     * 根据条件 查询指定页数
     */
    public List<PresentRule> queryPage(QueryObject queryObject) {
        Map map = queryObject.toMap();
        String endTime = (String) map.get("endTime");
        if (!StringUtils.isEmpty(endTime)) {
            map.put("endTime", endTime + "23:59:59");
        }
        return presentRuleMapper.selectPageRule(map);
    }

    /**
     * 根据ID 查询规则
     */
    public PresentRule selectByPrimaryKey(Long id) {

        return presentRuleMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据ID 查询规则详情 包括企业 产品
     */
    public PresentRule selectRuleDetails(Long id) {

        return presentRuleMapper.selectRuleDetails(id);
    }

    @Override
    @Transactional
    public boolean addRule(Administer administer, PresentRule presentRule, List<PresentRecordJson> prs) {
        // 校验参数
        if (prs == null || prs.size() <= 0 || presentRule == null) {
            logger.error("无效的赠送规则参数.");
            return false;
        }

        // 检查企业信息
        if (!checkEnterprise(presentRule.getEntId(), administer)) {
            logger.error("当前用户没有相应的权限执行普通赠送功能. 用户ID = {}.", administer == null ? null : administer.getId());
            return false;
        }

        if (create(sum(prs), administer.getId(), presentRule) != 1) {
            logger.error("创建普通赠送规则失败! PresentRule = {}, Prs = {}", new Gson().toJson(presentRule), new Gson().toJson(prs));
            return false;
        }

        // 创建规则记录
        if (!presentRecordService.create(presentRule, prs)) {
            logger.error("批量插入普通赠送规则时出错.PresentRule = {}, Prs = {}", new Gson().toJson(presentRule), new Gson().toJson(prs));
            throw new RuntimeException("批量插入普通赠送规则时出错.");
        }

        if(!activityCreatorService.insert(ActivityType.GIVE, presentRule.getId(), administer.getId())){
            logger.error("插入活动创建者表时出错.PresentRule = {}",new Gson().toJson(presentRule));
            throw new RuntimeException("插入活动创建者表时出错.");
        }
        
        
        logger.info("创建普通赠送规则成功，普通赠送记录插入成功.");
        return true;
    }

    public boolean give(Administer administer, PresentRule presentRule, String serialNum) {
        presentRule = presentRuleMapper.selectByPrimaryKey(presentRule.getId());
        // 判断创建人和修改人是否一致
        if (presentRule.getCreatorId().equals(administer.getId())) {
            presentRule.setUpdaterId(administer.getId());
            // 是否为待赠送
            if (presentRule.getStatus() == 0) {
                List<PresentRecord> list = presentRecordMapper.selectByRuleId(presentRule.getId());
                if (list == null || list.isEmpty()) {
                    logger.error("赠送记录为空, 赠送失败.");
                    return false;
                }

                //构建块赠送对象
//                String batchPresentSn = SerialNumGenerator.buildSerialNum();
                List<BlockPresentPojo> pojos = buildPojos(list, presentRule.getEntId(), serialNum);

                //扣钱!
                Long prdId = list.get(0).getPrdId();
//                if (!minusAccount(presentRule, prdId, list.size(), serialNum)) {
//                    logger.error("扣减账户余额时失败，流水号为{}, 赠送参数为{}.", serialNum, new Gson().toJson(pojos));
//                    if (!updatePresentRecordStatus(presentRule.getId())) {
//                	logger.error("更新赠送记录时出错，presentRuleId = ", presentRule.getId());
//                    }
//                    return false;
//                }
                
                //批量插入流水号关联关系表
                List<String> pltSns = parseSns(pojos);
                if (!presentSerialNumService.batchInsert(serialNum, pltSns)) {
                    logger.error("批量插入流水号关联关系时失败，流水号为{}, 赠送参数为{}.", serialNum, new Gson().toJson(pojos));
                    if (!updatePresentRecordStatus(presentRule.getId())) {
                	logger.error("更新赠送记录时出错，presentRuleId = ", presentRule.getId());
                    }
                    return false;
                }
                
                //插入流水号serialnum
                if (!serialNumService.batchInsert(buildPltSerialNum(pltSns))) {
                    logger.error("插入流水号记录时出错，presentRuleId = ", presentRule.getId()); 
                    return false;
                }
                
                List<ChargeRecord> crList = buildChargeRecords(list, presentRule);
                if (!chargeRecordService.batchInsert(crList)) {
                    logger.error("插入充值记录时出错. ");
                    return false;
                }

                List<Long> ids = new ArrayList();
                for (PresentRecord pojo:list) {
                    ids.add(pojo.getId());
                }
                // 存入队列
                if (taskProducer.produceBatchPresentMsg(pojos)) {
                    presentRule.setStatus(new Byte("2"));
                    presentRuleMapper.updateByPrimaryKeySelective(presentRule);
                    
                    if (!presentRecordService.batchUpdateStatusCode(ids, ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                	logger.error("入业务队列成功，更新赠送记录状态码失败");
                    }
                    if (!chargeRecordService.batchUpdateStatusCode(ChargeResult.ChargeMsgCode.businessQueue.getCode(), parseSns(pojos))) {
                	logger.error("入业务队列成功，更新充值记录状态码失败");
                    }
                    logger.info("入业务队列成功，更新赠送记录id = {}，状态码={}", ids,ChargeResult.ChargeMsgCode.businessQueue.getCode());
                    return true;
                } else {
                    Date updateChargeTime = new Date();
                    Integer financeStatus = null;
                    logger.error("入业务队列失败.");
                    if (!accountService.returnFunds(serialNum, ActivityType.GIVE, prdId, list.size())) {
                	logger.error("退款失败");
                    }else{
                        financeStatus = FinanceStatus.IN.getCode();
                    }
                    
                    if (!presentRecordService.batchUpdateStatus(ids, ChargeRecordStatus.FAILED)) {
                	logger.error("更新赠送记录状态码失败");
                    }
                    if (!chargeRecordService.batchUpdateStatus(buildRecords(crList, ChargeRecordStatus.FAILED, financeStatus, updateChargeTime))) {
                	logger.error("更新充值记录状态码失败");
                    }
                }
            }
        }
        return false;
    }

    //提取所有的订单流水号
    private List<String> parseSns(List<BlockPresentPojo> pojos) {
        List<String> sns = new LinkedList<String>();
        for (BlockPresentPojo blockPresentPojo : pojos) {
            for (PresentPojo presentPojo : blockPresentPojo.getPojos()) {
                sns.add(presentPojo.getRequestSerialNum());
            }
        }

        return sns;
    }

    // 修改
    @Override
    public boolean updateRule(Administer administer, PresentRule presentRule,
                              String[] phones, Integer type) {

        presentRule.setUpdateTime(new Date());

        PresentRule rule = presentRuleMapper.selectByPrimaryKey(presentRule
            .getId());

        if (rule != null) {
            presentRule.setTotal(phones.length);
            // 修改规则
            if (presentRuleMapper.updateByPrimaryKeySelective(presentRule) > 0) {
                // 删除所有记录
                return presentRecordMapper.deleteByRuleId(presentRule.getId()) > 0;
            }
        }
        return false;
    }

    /**
     * 检测某条规则的创建者是否和现在的登陆管理员为同一用户
     * <p>
     *
     * @param administer
     * @param ruleId
     * @return
     * @Title: isSameAdminCreated
     * @Description: 检测某条规则的创建者是否和现在的登陆管理员为同一用户
     * @see com.cmcc.vrp.province.service.PresentRuleService#isSameAdminCreated(com.cmcc.vrp.province.model.Administer, java.lang.Long)
     */
    @Override
    public boolean isSameAdminCreated(Administer administer, Long ruleId) {
        PresentRule rule = presentRuleMapper.selectByPrimaryKey(ruleId);
        if (rule != null && administer.getId().equals(rule.getCreatorId())) {
            return true;
        }
        return false;
    }

    /**
     * @param id
     * @return
     * @Title: chargeAgain
     * @Description: 将失败的充值记录进行再次充值
     * @see com.cmcc.vrp.province.service.PresentRuleService#chargeAgain(java.lang.Long)
     */
    @Override
    public boolean chargeAgain(Long id) {
        PresentRecord record = presentRecordMapper.selectByPrimaryKey(id);
        PresentRule presentRule = null;

        if (record == null
            || (presentRule = presentRuleMapper.selectByPrimaryKey(record.getRuleId())) == null) {
            return false;
        }

        // 检测状态是否为充值失败
        int status = record.getStatus();
        if (status != ChargeRecordStatus.FAILED.getCode()) {
            return false;
        }

        // 先将状态改为待充值再放入队列中
        Byte newStatus = 1;
        record.setStatus(newStatus);
        if (presentRecordMapper.updateByPrimaryKeySelective(record) < 1) {
            return false;
        }

        // 加入队列
        return taskProducer.produceBatchPresentMsg(buildPojos(record, presentRule.getEntId()));
    }

    // 根据企业ID 和姓名称查询 该用户是否能操作该企业
    // by qihang 增加客户经理和非客户经理时的不同判断
    public boolean checkEnterprise(Long eId, Administer admin) {
        if (eId == null || admin == null) {
            return false;
        }
        List<Enterprise> enterprises = enterprisesService.getEnterpriseListByAdminId(admin);
        if (enterprises != null && enterprises.size() > 0) {
            for (Enterprise e : enterprises) {
                if (e.getId().equals(eId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int create(int total, long creId, PresentRule presentRule) {
        presentRule.setTotal(total);
        presentRule.setCreatorId(creId);
        presentRule.setUpdaterId(creId);
        presentRule.setCreateTime(new Date());
        presentRule.setUpdateTime(new Date());
        presentRule.setStatus(new Byte("0"));
        presentRule.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        presentRule.setVersion((byte) 1);

        return presentRuleMapper.insertSelective(presentRule);
    }

    private int sum(List<PresentRecordJson> prs) {
        int total = 0;
        for (PresentRecordJson pr : prs) {
            total = total + pr.getGiveNum();
        }

        return total;
    }

    private boolean minusAccount(PresentRule presentRule, Long prdId, double delta, String serialNum) {
        try {
            return accountService.minusCount(presentRule.getEntId(), prdId, AccountType.ENTERPRISE, delta, serialNum, ActivityType.GIVE.getname());
        } catch (RuntimeException e) {
            logger.error("扣减批量赠送余额失败. 赠送失败.");
        }

        return false;
    }

    //按块大小进行拆分，生成块赠送的列表
    private List<BlockPresentPojo> buildPojos(List<PresentRecord> records, Long entId, String batchPresentSn) {
        List<BlockPresentPojo> pojos = new LinkedList<BlockPresentPojo>();

        int blockSize = getBatchBlockSize();
        int size = records.size();
        int index = 0;

        //分块构建
        while (index + blockSize <= size) {
            pojos.add(buildPojo(records.subList(index, index + blockSize), entId, batchPresentSn));
            index += blockSize;
        }

        //最后那一点点
        pojos.add(buildPojo(records.subList(index, size), entId, batchPresentSn));
        return pojos;
    }

    private List<BlockPresentPojo> buildPojos(PresentRecord record, Long endId) {
        List<BlockPresentPojo> pojos = new LinkedList<BlockPresentPojo>();
        pojos.add(buildPojo(record, endId));

        return pojos;
    }

    //根据记录生成块赠送对象
    private BlockPresentPojo buildPojo(PresentRecord record, Long entId) {
        List<PresentPojo> pojos = new LinkedList<PresentPojo>();

        PresentPojo pojo = new PresentPojo();
        pojo.setMobile(record.getMobile());
        pojo.setRecordId(record.getId());
        pojo.setEnterpriseId(entId);
        pojo.setProductId(record.getPrdId());
        pojo.setRuleId(record.getRuleId());
        pojo.setRequestSerialNum(record.getSysSerialNum());

        pojos.add(pojo);
        return buildBlockPresentPojo(pojos, record.getSysSerialNum());
    }

    private BlockPresentPojo buildBlockPresentPojo(List<PresentPojo> pojos, String serialNum) {
        BlockPresentPojo blockPresentPojo = new BlockPresentPojo();
        blockPresentPojo.setSerialNum(serialNum);
        blockPresentPojo.setPojos(pojos);

        return blockPresentPojo;
    }

    //根据记录生成块赠送对象
    private BlockPresentPojo buildPojo(List<PresentRecord> records, Long entId, String batchPresentSn) {
        List<PresentPojo> pojos = new LinkedList<PresentPojo>();
        for (PresentRecord record : records) {
            PresentPojo pojo = new PresentPojo();
            pojo.setMobile(record.getMobile());
            pojo.setRecordId(record.getId());
            pojo.setEnterpriseId(entId);
            pojo.setProductId(record.getPrdId());
            pojo.setRuleId(record.getRuleId());
            pojo.setRequestSerialNum(record.getSysSerialNum());

            pojos.add(pojo);
        }

        return buildBlockPresentPojo(pojos, batchPresentSn);
    }

    public int getBatchBlockSize() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BATCH_PRESENT_BLOCK_SIZE.getKey()), 50);
    }
    
    private boolean updatePresentRecordStatus(Long ruleId) {
    	List<PresentRecord> prList = presentRecordService.selectByRuleId(ruleId);
        for (PresentRecord pr:prList) {
    	    pr.setStatus(ChargeRecordStatus.FAILED.getCode().byteValue());
    	    pr.setErrorMessage(ChargeRecordStatus.FAILED.getMessage());
        }
        return presentRecordService.batchUpdateChargeResult(prList);
    }
    
    private List<ChargeRecord> buildChargeRecords(List<PresentRecord> prList, PresentRule presentRule) {
	List<ChargeRecord> crList = new ArrayList();
	
	for(PresentRecord pr:prList) {
	    ChargeRecord cr = new ChargeRecord();
	    cr.setEnterId(presentRule.getEntId());
	    cr.setPrdId(pr.getPrdId());
	    cr.setStatus(ChargeRecordStatus.WAIT.getCode());
	    cr.setType(ActivityType.GIVE.getname());
	    cr.setaName(presentRule.getaName());
	    cr.setTypeCode(ActivityType.GIVE.getCode());
	    cr.setPhone(pr.getMobile());
	    cr.setRecordId(pr.getId());
	    cr.setSystemNum(pr.getSysSerialNum());
	    cr.setChargeTime(new Date());
	    cr.setEffectType(pr.getEffectType());
	    crList.add(cr);
	}
	return crList;
    }
    
    private List<ChargeRecord> buildRecords(List<ChargeRecord> records, ChargeRecordStatus chargeRecordStatus, 
            Integer financeStatus, Date updateChargeTime) {
        List<ChargeRecord> list = new ArrayList<ChargeRecord>();
        for (ChargeRecord record : records) {
            record.setStatus(chargeRecordStatus.getCode());
            record.setErrorMessage(chargeRecordStatus.getMessage());
            record.setUpdateChargeTime(updateChargeTime);
            record.setFinanceStatus(financeStatus);
            list.add(record);
        }
        return list;
    }
    
    private List<SerialNum> buildPltSerialNum(List<String> pltSns) {
	List<SerialNum> serialNums = new ArrayList();
	for (String sn :pltSns) {
	    SerialNum serialNum = new SerialNum();
	    serialNum.setPlatformSerialNum(sn);
	    serialNum.setCreateTime(new Date());
	    serialNum.setUpdateTime(new Date());
	    serialNum.setDeleteFlag(0);
	    serialNums.add(serialNum);
	}
	return serialNums;
    }
}

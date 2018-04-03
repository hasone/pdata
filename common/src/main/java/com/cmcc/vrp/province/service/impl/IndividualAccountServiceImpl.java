package com.cmcc.vrp.province.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.service.IndividualBossService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountRecordStatus;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.dao.IndividualAccountMapper;
import com.cmcc.vrp.province.dao.TmpaccountMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.module.Membership;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowcoinPurchaseService;
import com.cmcc.vrp.province.service.IndividualFlowcoinRecordService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.WxGradeService;
import com.cmcc.vrp.wx.model.Tmpaccount;
import com.cmcc.vrp.wx.model.WxGrade;

/**
 * IndividualAccountServiceImpl.java
 */
@Service("individualAccountService")
public class IndividualAccountServiceImpl implements IndividualAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualAccountServiceImpl.class);
    @Autowired
    IndividualAccountMapper mapper;
    @Autowired
    IndividualFlowcoinPurchaseService individualFlowcoinPurchaseService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualAccountRecordService individualAccountRecordService;
    @Autowired
    IndividualFlowcoinRecordService individualFlowcoinRecordService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    //@Autowired
    //AdministerService administerService;
    @Autowired
    WxAdministerService wxAdministerService;
    @Autowired
    IndividualBossService individualBossService;
    @Autowired
    TmpaccountMapper tmpaccountMapper;
    @Autowired
    WxGradeService wxGradeService;
    @Autowired
    IndividualProductMapService individualProductMapService;
    
    @Override
    public IndividualAccount selectByPrimaryKey(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean batchInsert(List<IndividualAccount> accounts) {
        return mapper.batchInsert(accounts) == accounts.size();
    }

    @Override
    public IndividualAccount getAccountByOwnerIdAndProductId(Long ownerId, Long productId, Integer type) {
        return mapper.getAccountByOwnerIdAndProductId(ownerId, productId, type);
    }

    /**
     * 增加账户余额
     */
    @Override
    public boolean addCount(IndividualAccount account, BigDecimal deltaCount) {
        return mapper.addCount(account.getId(), deltaCount) == 1;
    }

    /**
     * 根据用户手机号增加相应的账户, 如果用户相应账户不存在, 则会先创建账户
     *
     * @param mobile    用户手机号
     * @param serialNum 操作流水号
     * @param prdId     个人用户产品ID
     * @param count     增加的值
     * @return 增加成功返回true, 否则false
     */
    @Override
    public boolean addCountForcely(String mobile, Long prdId, String serialNum, BigDecimal count, ActivityType activityType, String description) {
        if (!StringUtils.isValidMobile(mobile) //无效的手机号
                || org.apache.commons.lang.StringUtils.isBlank(serialNum)) {  //空流水号
            LOGGER.info("无效的手机号或操作流水号. Mobile = {}, SerialNum = {}.", mobile, serialNum);
            return false;
        }

        //通过手机号获取用户
        WxAdminister administer = wxAdministerService.selectByMobilePhone(mobile);
        //Administer administer = administerService.selectByMobilePhone(mobile);
        if (administer == null) {
            LOGGER.info("无法根据用户手机号获取用户信息. Mobile = {}.", mobile);
            return false;
        }

        //获取个人账户,不存在则创建
        IndividualAccount individualAccount = getAccountByOwnerIdAndProductId(administer.getId(), prdId, IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if (individualAccount == null) {//不存在则开始创建账户
            individualAccount = build(administer.getId(), prdId);
            if (!insert(individualAccount)) {
                LOGGER.error("账户不存在,创建新账户时返回失败,无法增加相应的账户余额. Mobile = {}, UserId = {}, PrdId = {}, SerialNum = {}, Count= {}.",
                        mobile, administer.getId(), prdId, serialNum, count);
                return false;
            }
        }

        //开始加余额
        if(mapper.addCount(individualAccount.getId(), count)!=1){
            LOGGER.error("增加账户余额失败. individualAccount.id= {}, count = {}.", individualAccount.getId(), count);
            return false;
        }
        //开始增加账户记录
        if(!individualAccountRecordService.create(build(individualAccount, serialNum, AccountRecordType.INCOME, count, activityType, description))){
            LOGGER.error("增加账户变更记录失败. individualAccount.id= {}, serialNum={}, activityType.name={}, count = {}.", 
                    individualAccount.getId(), serialNum, activityType.getname(), count);
            return false;
        }
        return true;
    }

    private IndividualAccount build(Long userId, Long prdId) {
        IndividualAccount individualAccount = new IndividualAccount();
        individualAccount.setAdminId(userId);
        individualAccount.setOwnerId(userId);
        individualAccount.setIndividualProductId(prdId);
        individualAccount.setType(IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        individualAccount.setCount(new BigDecimal(0));  //初始账户的余额为0
        individualAccount.setCreateTime(new Date());
        individualAccount.setUpdateTime(new Date());
        individualAccount.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        individualAccount.setVersion(0);   //账户的初始版本号为0

        return individualAccount;
    }
    

    /** 
     * 构造账户记录
     * @Title: build 
     */
    private IndividualAccountRecord build(IndividualAccount account, String serialNum, AccountRecordType type, 
            BigDecimal deltaCount, ActivityType activityType, String description) {
        IndividualAccountRecord record = new IndividualAccountRecord();
        record.setAdminId(account.getAdminId());
        record.setOwnerId(account.getOwnerId());
        record.setAccountId(account.getId());
        record.setType((int) type.getValue()); //0代表收入, 1代表支出
        record.setStatus(IndividualAccountRecordStatus.SUCCESS.getValue());
        record.setSerialNum(serialNum);
        record.setCount(deltaCount);
        record.setDescription(description);
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        record.setActivityType(activityType.getCode());
        record.setBack(0); //0代表不是回退操作,1代表回退

        return record;
    }

    /**
     * 扣减账户余额
     */
    @Override
    public boolean minusCount(IndividualAccount account, BigDecimal deltaCount) {
        return mapper.minusCount(account.getId(), deltaCount) == 1;
    }

    /**
     * 变更boss处的账户（话费、流量币） 是否是退回的操作，0表示否，1表示是
     */
    @Override
    @Transactional
    public Boolean changeBossAccount(Long adminId, BigDecimal count, Long productId, String systemSerial,
                                     Integer accountRecordType, String desc, Integer activityType, Integer back) {

        //1、增加individual_account_record记录话费扣减记录（进行中）
        if (productId == null || adminId == null) {
            LOGGER.error("changeBossAccount参数为空,productId={},adminId={}", productId, adminId);
            throw new RuntimeException("changeBossAccount参数为空");
        }

        WxAdminister admin = wxAdministerService.selectWxAdministerById(adminId);
        //Administer admin = administerService.selectAdministerById(adminId);
        if (admin == null || StringUtils.isEmpty(admin.getMobilePhone())) {
            LOGGER.error("用户手机号码不存在,adminId={}", adminId);
            return false;
        }
        IndividualProduct product = individualProductService.selectByPrimaryId(productId);
        if (product == null) {
            LOGGER.error("个人产品不存在,productId={}", productId);
            return false;
        }

        IndividualAccount account = getAccountByOwnerIdAndProductId(adminId, productId,
                IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if (account == null) {
            LOGGER.error("账户为空,adminId={},productId={}", adminId, productId);
            throw new RuntimeException("账户为空");
        }
        IndividualAccountRecord record = bulidIndividualAccountRecord(adminId, adminId, count, systemSerial,
                account.getId(), accountRecordType, desc, activityType, back);

        if (!individualAccountRecordService.create(record)) {
            LOGGER.error("创建boss账户变更Individual_account_record记录失败，record={}", JSONObject.toJSONString(record));
            throw new RuntimeException("创建boss账户变更Individual_account_record记录失败");
        }

        //2、TODO：向boss请求（boss实时返回?）（根据产品类型和支出收入，向boss发请求）
        IndividualAccountRecord newRecord = new IndividualAccountRecord();
        newRecord.setId(record.getId());

        Boolean bossResult = false;
        //通信账户变更
        if (product.getType() == 0) {
            bossResult = individualBossService.changeBossPhoneFare(admin.getMobilePhone(), count, accountRecordType, systemSerial);
        }
        //流量币账户变更
        if (product.getType() == 1) {
            bossResult = individualBossService.changeBossFlowcoin(admin.getMobilePhone(), count.longValue(), accountRecordType, systemSerial);
        }

        if (bossResult) {
            //3、根据boss返回结果更新accountRecord状态
            newRecord.setStatus(IndividualAccountRecordStatus.SUCCESS.getValue());
            newRecord.setErrorMsg("成功");
            if (!individualAccountRecordService.updateByPrimaryKeySelective(newRecord)) {
                LOGGER.error("更新accountRecord失败，record={}", JSONObject.toJSONString(newRecord));
            }
            //如果变更的boss产品是流量币，则记录流量币变更记录IndividualFlowcoinRecord
            if (productId.equals(individualProductService.getFlowcoinProduct().getId())) {
                if (!individualFlowcoinRecordService.createRecord(record)) {
                    LOGGER.error("创建流量币变更记录失败，individualFlowcoinRecord失败，账户记录={}", JSONObject.toJSONString(record));
                }
            }
            return true;
        } else {
            newRecord.setStatus(IndividualAccountRecordStatus.FAIL.getValue());
            newRecord.setErrorMsg("随机错误");
            if (!individualAccountRecordService.updateByPrimaryKeySelective(newRecord)) {
                LOGGER.error("更新accountRecord失败，record={}", JSONObject.toJSONString(newRecord));
            }
            return false;
        }

    }

    /**
     * 变更冻结账户（活动账户）
     */
    @Override
    @Transactional
    public boolean changeFrozenAccount(Long adminId, Long ownerId, Long accountId, BigDecimal count, Long productId,
                                       String systemSerial, Integer accountRecordType, String desc, Integer activityType, Integer back) {
        if (productId == null || adminId == null || accountId == null) {
            LOGGER.error("changeFrozenAccount参数为空,productId={},adminId={},accountId={}", productId, adminId, accountId);
            throw new RuntimeException("changeFrozenAccount参数为空");
        }
        //1、增加individual_account_record记录话费扣减记录（进行中）
        IndividualAccountRecord record = bulidIndividualAccountRecord(adminId, ownerId, count, systemSerial, accountId,
                accountRecordType, desc, activityType, back);
        if (!individualAccountRecordService.create(record)) {
            LOGGER.error("创建冻结账户变更Individual_account_record记录失败，record={}", JSONObject.toJSONString(record));
            throw new RuntimeException("创建冻结账户变更Individual_account_record记录失败");
        }

        //2、变更冻结账户余额
        IndividualAccount account = selectByPrimaryKey(accountId);
        if (account == null) {
            LOGGER.error("account为空，accountId={}", accountId);
            throw new RuntimeException("account为空");
        }
        IndividualAccountRecord newRecord = new IndividualAccountRecord();
        newRecord.setId(record.getId());

        //2-1 收入，增加余额
        if (accountRecordType.equals((int) AccountRecordType.INCOME.getValue())) {
            if (addCount(account, count)) {
                newRecord.setStatus(IndividualAccountRecordStatus.SUCCESS.getValue());
                newRecord.setErrorMsg("成功");
                //3、更新账户变更记录状态
                if (!individualAccountRecordService.updateByPrimaryKeySelective(newRecord)) {
                    LOGGER.error("更新accountRecord失败，record={}", JSONObject.toJSONString(newRecord));
                    throw new RuntimeException("更新accountRecord失败");
                }
                return true;
            } else {
                throw new RuntimeException("更新accountRecord失败");
            }
        } else if (accountRecordType.equals((int) AccountRecordType.OUTGO.getValue())) { //2-1 支出，扣减余额
            if (minusCount(account, count)) {
                newRecord.setStatus(IndividualAccountRecordStatus.SUCCESS.getValue());
                newRecord.setErrorMsg("成功");
                //3、更新账户变更记录状态
                if (!individualAccountRecordService.updateByPrimaryKeySelective(newRecord)) {
                    LOGGER.error("更新accountRecord失败，record={}", JSONObject.toJSONString(newRecord));
                    throw new RuntimeException("更新accountRecord失败");
                }
                ;
                return true;
            } else {
                throw new RuntimeException("更新accountRecord失败");
            }
        } else {
            LOGGER.error("账户变更类型异常！accountRecordType={}", accountRecordType);
            throw new RuntimeException("账户变更类型异常！accountRecordType=" + accountRecordType);
        }
    }

    private IndividualAccountRecord bulidIndividualAccountRecord(Long adminId, Long ownerId, BigDecimal count,
            String systemSerial, Long accountId, Integer type, String desc, Integer activityType, Integer back) {
        IndividualAccountRecord record = new IndividualAccountRecord();
        record.setAdminId(adminId);
        record.setOwnerId(ownerId);
        record.setAccountId(accountId);
        record.setType(type);
        record.setStatus(IndividualAccountRecordStatus.PROCESSING.getValue());
        record.setSerialNum(systemSerial);
        record.setCount(count);
        record.setDescription(desc);
        record.setDeleteFlag(0);
        record.setActivityType(activityType);
        record.setBack(back);
        return record;
    }

    @Override
    public boolean insert(IndividualAccount individualAccount) {
        if (individualAccount == null) {
            return false;
        }
        return mapper.insert(individualAccount) == 1;
    }

    @Override
    public boolean createAccountForActivity(Activities activities, ActivityInfo activityInfo,
                                            List<ActivityPrize> activityPrizes, IndividualAccount individualAccount) {
        if (activities != null && activityInfo != null && activityPrizes != null && individualAccount != null) {
            //创建冻结账户
            try {
                return createFrozenAccount(individualAccount, activities.getActivityId(), activities.getName(),
                        activities.getType());
            } catch (Exception e) {
                LOGGER.info("createAccountForActivity异常" + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean chargeFlowcoinForIndividualActivity(String activityId, Integer count, String mobile,
                                                       String activityWinRecordId) {
        if (!StringUtils.isEmpty(activityId) && count != null && !StringUtils.isEmpty(mobile)) {
            Activities activities = activitiesService.selectByActivityId(activityId);
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(activityId);
            
            WxAdminister administer = wxAdministerService.selectByMobilePhone(mobile); 
            //Administer administer = administerService.selectByMobilePhone(mobile);

            if (activities != null && activityPrizes != null && activityPrizes.size() > 0 && administer != null) {
                if (!changeBossAccount(administer.getId(), BigDecimal.valueOf(Long.valueOf(count)),
                        activityPrizes.get(0).getProductId(), activityWinRecordId,
                        (int) AccountRecordType.INCOME.getValue(), activities.getName(), activities.getType(), 0)) {
                    LOGGER.info("活动ID-" + activityId + "充值流量币失败。充值手机号-" + mobile + "; 流量币大小-" + count);
                    return false;
                }
                return true;
            }
        }
        LOGGER.info("活动参数缺失，充值流量币操作失败!");
        return false;
    }

    @Override
    public boolean giveBackForActivity(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return false;
        }
        Activities activities = activitiesService.selectByActivityId(activityId);
        List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(activityId);
        if (activities != null && activities.getId() != null && activityPrizes != null && activityPrizes.size() > 0
                && activityPrizes.get(0).getProductId() != null) {
            IndividualAccount individualAccount = getAccountByOwnerIdAndProductId(activities.getId(), activityPrizes
                    .get(0).getProductId(), IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());

            if (individualAccount != null) {
                LOGGER.info("开始将活动【" + activityId + "】剩余的【" + individualAccount.getCount() + "】个流量币(活动冻结账户为【"
                        + individualAccount.getId() + "】)回退到boss账户。");

                //1、将剩余流量币回退给boss账户
                if (!changeBossAccount(activities.getCreatorId(), individualAccount.getCount(),
                        individualAccount.getIndividualProductId(), activities.getActivityId(),
                        (int) AccountRecordType.INCOME.getValue(), activities.getName(),
                        ActivityType.COMMON_REDPACKET.getCode(), 0)) {
                    LOGGER.error("活动【" + activityId + "】剩余的【" + individualAccount.getCount() + "】个流量币回退到boss账户失败!");
                    return false;
                }
                LOGGER.info("活动【" + activityId + "】剩余的【" + individualAccount.getCount() + "】个流量币回退到boss账户成功!");

                //2、将该冻结活动账户的count扣减为0
                //String serialNum = SerialNumGenerator.buildSerialNum();  //这里的流水号暂时没有用上,但先加上, by sunyiwei
                if (minusCount(individualAccount, individualAccount.getCount())) {
                    LOGGER.info("扣减平台冻结账户【" + individualAccount.getId() + "】的【" + individualAccount.getCount()
                            + "】个流量币成功!");
                    return true;
                }
                LOGGER.error("扣减平台冻结账户【" + individualAccount.getId() + "】的【" + individualAccount.getCount()
                        + "】个流量币失败!");
                return false;
            }
        }
        LOGGER.info("活动【" + activityId + "】剩余的参数缺失，退回流量币操作失败!");
        return false;
    }
    
    /** 
     * 四川个人红包流量回退
     * @Title: giveBackFlowForPage 
     */
    @Override
    @Transactional
    public boolean giveBackFlow(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return false;
        }
        Activities activities = activitiesService.selectByActivityId(activityId);
        List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(activityId);
        if (activities != null && activities.getId() != null && activityPrizes != null && activityPrizes.size() > 0
                && activityPrizes.get(0).getProductId() != null) {            
            IndividualAccount activityAccount = getAccountByOwnerIdAndProductId(activities.getId(), activityPrizes
                    .get(0).getProductId(), IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());

            if (activityAccount != null && activityAccount.getCount().intValue()>0){
                LOGGER.info("开始将活动【" + activities.getActivityId() + "】剩余的【" + activityAccount.getCount() + "】M流量(活动冻结账户为【"
                        + activityAccount.getId() + "】)回退到对应的订购账户。");
                //1、获取流量回退的账户
                IndividualAccount flowAccount= getAccountByOwnerIdAndProductId(activities.getCreatorId(), 
                            activityPrizes.get(0).getProductId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());                    
                if(flowAccount == null){
                    LOGGER.info("活动【" + activities.getActivityId() + "】剩余的【" + activityAccount.getCount() + "】M流量(活动冻结账户为【"
                            + activityAccount.getId() + "】)回退到对应的订购账户。");
                    return false;
                }

                //2、将该冻结活动账户的count扣减为0
                if (!changeAccount(flowAccount, activityAccount.getCount(), activities.getActivityId(),
                        (int) AccountRecordType.OUTGO.getValue(), "活动账户余额退回订购账户，活动账户扣减", activities.getType(), 1)) {
                    LOGGER.error("扣减活动账户失败，活动【" + activities.getActivityId() + "】剩余的【" + activityAccount.getCount() + "】M流量回退到订购账户，活动账户扣减失败!");
                    return false;
                }

                //3、将剩余流量回退给订购流量账户
                if (!changeAccount(flowAccount, activityAccount.getCount(), activities.getActivityId(),
                        (int) AccountRecordType.INCOME.getValue(), "活动账户余额退回订购账户，订购账户增加", activities.getType(), 1)) {
                    LOGGER.error("订购账户增加失败，活动【" + activities.getActivityId() + "】剩余的【" + activityAccount.getCount() + "】M流量回退到订购账户，订购账户增加失败!");
                    throw new RuntimeException("订购账户增加失败");
                }
                LOGGER.info("活动【" + activities.getActivityId() + "】剩余的【" + activityAccount.getCount() + "】M流量回退到订购账户成功!");
                return true;
            }           
        }
        LOGGER.info("活动【" + activityId + "】剩余的参数缺失，退回流量操作失失败");
        return false;
    }

    @Override
    @Transactional
    public boolean createFlowcoinExchangeAndPurchaseAccount(IndividualAccount individualAccount, String systemSerial,
                                                            String desc, Integer activityType) {
        //1、创建冻结账户
        try {
            createFrozenAccount(individualAccount, systemSerial, desc + "，冻结账户增加(2)", activityType);
        } catch (Exception e) {
            LOGGER.info("createFlowcoinExchangeAndPurchaseAccount创建冻结账户失败" + e.getMessage());
            return false;
        }

        //2、扣除BOSS账户
        if (changeBossAccount(individualAccount.getAdminId(), individualAccount.getCount(),
                individualAccount.getIndividualProductId(), systemSerial, (int) AccountRecordType.OUTGO.getValue(),
                desc + "，boss账户扣除(1)", activityType, 0)) {
            return true;
        }
        throw new RuntimeException("变更BOSS账户失败！");
    }

    @Override
    @Transactional
    public boolean createFrozenAccount(IndividualAccount individualAccount, String systemSerial, String desc,
                                       Integer activityType) {
        if (!insert(individualAccount)) {
            LOGGER.error("插入冻结账户individualAccount失败，individualAccount={}", JSONObject.toJSONString(individualAccount));
            throw new RuntimeException("插入冻结账户individualAccount失败");
        }
        IndividualAccountRecord record = bulidIndividualAccountRecord(individualAccount.getAdminId(),
                individualAccount.getOwnerId(), individualAccount.getCount(), systemSerial, individualAccount.getId(),
                (int) AccountRecordType.INCOME.getValue(), desc + "，冻结账户增加", activityType, 0);
        record.setStatus(IndividualAccountRecordStatus.SUCCESS.getValue());
        record.setErrorMsg("成功");
        if (!individualAccountRecordService.create(record)) {
            LOGGER.error("插入冻结账户individualAccountRecord失败，individualAccountRecord={}", JSONObject.toJSONString(record));
            throw new RuntimeException("插入冻结账户变更记录individualAccountRecord失败");
        }
        return true;
    }
    
    /**
     * 变更账户（活动账户）
     */
    @Override
    @Transactional
    public boolean changeAccount(IndividualAccount account, BigDecimal count,
            String systemSerial, Integer accountRecordType, String desc, Integer activityType, Integer back) {
        if (account == null) {
            LOGGER.error("changeAccount参数account为空");
            throw new RuntimeException("changeAccount参数为空");
        }
        
        LOGGER.info("变更账户，accountId={}，count={}，systemSerial={}，accountRecordType={}，desc={}，activityType={}，back={}",
                account.getId(), count.intValue(), systemSerial, accountRecordType, desc, activityType, back);
        
        //1、增加individual_account_record记录
        IndividualAccountRecord record = bulidIndividualAccountRecord(account.getAdminId(), account.getOwnerId(), count, systemSerial, account.getId(),
                accountRecordType, desc, activityType, back);
        if (!individualAccountRecordService.create(record)) {
            LOGGER.error("创建账户变更Individual_account_record记录失败，record={}", JSONObject.toJSONString(record));
            throw new RuntimeException("创建冻结账户变更Individual_account_record记录失败");
        }

        //2、变更账户余额
        IndividualAccountRecord newRecord = new IndividualAccountRecord();
        newRecord.setId(record.getId());

        //2-1 收入，增加余额
        if (accountRecordType.equals((int) AccountRecordType.INCOME.getValue())) {
            if (addCount(account, count)) {
                newRecord.setStatus(IndividualAccountRecordStatus.SUCCESS.getValue());
                newRecord.setErrorMsg("成功");
                //3、更新账户变更记录状态
                if (!individualAccountRecordService.updateByPrimaryKeySelective(newRecord)) {
                    LOGGER.error("更新accountRecord失败，record={}", JSONObject.toJSONString(newRecord));
                    throw new RuntimeException("更新accountRecord失败");
                }
                return true;
            } else {
                throw new RuntimeException("更新accountRecord失败");
            }
        } else if (accountRecordType.equals((int) AccountRecordType.OUTGO.getValue())) { //2-1 支出，扣减余额
            if (minusCount(account, count)) {
                newRecord.setStatus(IndividualAccountRecordStatus.SUCCESS.getValue());
                newRecord.setErrorMsg("成功");
                //3、更新账户变更记录状态
                if (!individualAccountRecordService.updateByPrimaryKeySelective(newRecord)) {
                    LOGGER.error("更新accountRecord失败，record={}", JSONObject.toJSONString(newRecord));
                    throw new RuntimeException("更新accountRecord失败");
                }
                return true;
            } else {
                throw new RuntimeException("更新accountRecord失败");
            }
        } else {
            LOGGER.error("账户变更类型异常！accountRecordType={}", accountRecordType);
            throw new RuntimeException("账户变更类型异常！accountRecordType=" + accountRecordType);
        }
    }

    @Override
    public List<IndividualAccount> getExpireAccount(Date date) {
        return mapper.getExpireAccount(date);
    }

    @Override
    public boolean updateExpireTimeAndOrderId(Long id, Date expireTime, Long orderId) {
        IndividualAccount account = new IndividualAccount();
        account.setId(id);
        account.setExpireTime(expireTime);
        account.setCurrentOrderId(orderId);
        return mapper.updateByPrimaryKeySelective(account) == 1;
    }

    @Override
    @Transactional
    public boolean checkAndInsertAccountForWx(Long adminId, String openid) {  

        IndividualProduct point = individualProductService.getIndivialPointProduct();
        IndividualAccount individualAccount = getAccountByOwnerIdAndProductId(adminId, point.getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if(individualAccount!=null){
            LOGGER.info("用户已存在个人积分账户，adminId={}， openid={}", adminId, openid);
            return true;
        }
        LOGGER.info("用户不存在个人积分账户，需要创建个人账户，adminId={}， openid={}", adminId, openid);
        List<IndividualAccount> accounts = new ArrayList<IndividualAccount>();
        List<IndividualProduct> products = individualProductService.selectByDefaultValue(1);
        if (products != null && products.size() > 0) {
            for (IndividualProduct product : products) {
                IndividualAccount account = new IndividualAccount();
                account.setAdminId(adminId);
                account.setOwnerId(adminId);
                account.setCount(new BigDecimal(0));
                account.setDeleteFlag(0);
                account.setIndividualProductId(product.getId());
                account.setType(IndividualAccountType.INDIVIDUAL_BOSS.getValue());//创建的账户都是个人的
                account.setVersion(0);
                accounts.add(account);
            }

            if (!batchInsert(accounts)) {
                LOGGER.error("插入individual_account失败！");
                throw new RuntimeException("插入individual_account失败！");
            }
        }
        List<Tmpaccount> tmpAccounts = tmpaccountMapper.selectByOpenid(openid);
        if(tmpAccounts!=null && tmpAccounts.size()==1 && tmpAccounts.get(0).getCount().intValue()>0){

            IndividualAccount account = getAccountByOwnerIdAndProductId(adminId, point.getId(), 
                    IndividualAccountType.INDIVIDUAL_BOSS.getValue());
            if(account!=null){
                if(!changeAccount(account, tmpAccounts.get(0).getCount(), SerialNumGenerator.buildSerialNum(), 
                        (int)AccountRecordType.INCOME.getValue(), "历史流量币", -1, 0)){
                    LOGGER.error("插入历史流量币失败！");
                    throw new RuntimeException("插入历史流量币失败！");
                }
            }
        }
        
        return true;
    }

    @Override
    public List<Membership> getMembershipList(Map map) {
        IndividualProduct product = individualProductService.getIndivialPointProduct();
        map.put("individualProductId", product.getId());
        map.put("type", IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        /*List<String> mobiles = new ArrayList<String>();
        if(!StringUtils.isEmpty((String) map.get("openid"))){
            //向微信公众号发送请求，查询openid对应的手机号码
            
        }
        if(!StringUtils.isEmpty((String) map.get("nickname"))){
            //向微信公众号发送请求，查询包含nickname的手机号码列表
            
        }
        if(!StringUtils.isEmpty((String) map.get("mobile"))){
            mobiles.add((String) map.get("mobile"));
        }
        
        map.put("mobiles", org.apache.commons.lang.StringUtils.join(mobiles, ","));*/
        List<Membership> list = mapper.getMembershipList(map);
        if(list!=null && list.size()>0){
            for(Membership m : list){
                WxAdminister admin = wxAdministerService.selectByMobilePhone(m.getMobile());
                //Administer admin = administerService.selectByMobilePhone(m.getMobile());
                if(admin!=null){
                    IndividualAccount account = getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), 
                            IndividualAccountType.INDIVIDUAL_BOSS.getValue());
                    BigDecimal accumulateCount = individualAccountRecordService.selectAccumulateAccount(account.getId());
                    Long accumulateCountLong = 0L;
                    if(accumulateCount!=null){
                        accumulateCountLong = accumulateCount.longValue();
                    }
                    m.setAccumulateCount(accumulateCountLong);
                    m.setGrade(getGrade(accumulateCountLong));
                }                
            }
        }
        return list;
    }
    
    private String getGrade(Long accumulateCount){
        List<WxGrade> grades = wxGradeService.selectAllGrade();
        String gradeStr = "";
        for(WxGrade grade : grades){
            //当前积分大于该级需要的积分
            if(accumulateCount >= grade.getPoints()){
                gradeStr = grade.getName();
            }
        }
        return gradeStr;
    }
    
    @Override
    public Integer countMembershipList(Map map) {
        IndividualProduct product = individualProductService.getIndivialPointProduct();
        map.put("individualProductId", product.getId());
        map.put("type", IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        /*List<String> mobiles = new ArrayList<String>();
        if(!StringUtils.isEmpty((String) map.get("openid"))){
            //向微信公众号发送请求，查询openid对应的手机号码
            
        }
        if(!StringUtils.isEmpty((String) map.get("nickname"))){
            //向微信公众号发送请求，查询包含nickname的手机号码列表
            
        }
        if(!StringUtils.isEmpty((String) map.get("mobile"))){
            mobiles.add((String) map.get("mobile"));
        }
        
        map.put("mobiles", org.apache.commons.lang.StringUtils.join(mobiles, ","));
*/
        return mapper.countMembershipList(map);
    }
    
    
    /**
     * 四川红包检查账户，如账户不存在则创建账户
     */
    @Override
    @Transactional
    public boolean insertAccountForScJizhong(Long adminId) {
        List<IndividualAccount> currentAccounts = selectByTypeAndOwnerId(IndividualAccountType.INDIVIDUAL_BOSS.getValue(), adminId);        
        //当前用户的BOSS账户不为空，说明用户在之前已经插入过默认的BOSS账户
        if(currentAccounts!=null&&currentAccounts.size()>0){
            return true;
        }
        LOGGER.error("平台已存在该用户，但是用户没有个人账户，现在开始创建个人账户，adminId={}", adminId);
        //插入individual_product_map
        //插入individual_account
        List<IndividualProductMap> records = new ArrayList<IndividualProductMap>();
        List<IndividualAccount> accounts = new ArrayList<IndividualAccount>();
        List<IndividualProduct> products = individualProductService.selectByDefaultValue(1);
        if (products != null && products.size() > 0) {
            for (IndividualProduct product : products) {
                IndividualProductMap record = new IndividualProductMap();
                record.setAdminId(adminId);
                record.setDeleteFlag(0);
                record.setDiscount(100);
                record.setPrice(product.getPrice());
                record.setIndividualProductId(product.getId());
                records.add(record);

                IndividualAccount account = new IndividualAccount();
                account.setAdminId(adminId);
                account.setOwnerId(adminId);
                account.setCount(new BigDecimal(0));
                account.setDeleteFlag(0);
                account.setIndividualProductId(product.getId());
                account.setType(-1);//创建的账户都是个人的
                account.setVersion(0);
                accounts.add(account);
            }

            if (!individualProductMapService.batchInsert(records)) {
                LOGGER.error("插入individual_product_map失败！");
                throw new RuntimeException("插入individual_product_map失败！");
            }
            if (!batchInsert(accounts)) {
                LOGGER.error("插入individual_account失败！");
                throw new RuntimeException("插入individual_account失败！");
            }
        }

        return true;
    }

    @Override
    public List<IndividualAccount> selectByTypeAndOwnerId(Integer type, Long ownerId) {
        return mapper.selectByTypeAndOwnerId(type, ownerId);
    }


}

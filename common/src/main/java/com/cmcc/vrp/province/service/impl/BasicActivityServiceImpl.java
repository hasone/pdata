package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ActivityPrizeRank;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityTemplateType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.province.activity.model.AutoGeneratePojo;
import com.cmcc.vrp.province.activity.model.AutoPrizesPojo;
import com.cmcc.vrp.province.activity.model.AutoResponsePojo;
import com.cmcc.vrp.province.activity.model.AutoTimePojo;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.quartz.jobs.ActivityEndJob;
import com.cmcc.vrp.province.quartz.jobs.ActivityJobPojo;
import com.cmcc.vrp.province.quartz.jobs.ActivityStartJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qinqinyan on 2016/11/10. 营销活动基础服务类
 */
public abstract class BasicActivityServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BasicActivityServiceImpl.class);

    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ActivityTemplateService activityTemplateService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ProductService productService;
    @Autowired
    ActivityCreatorService activityCreatorService;

    /**
     * 插入营销活动基本信息
     *
     * @author qinqinyan
     */
    @Transactional
    public boolean insertActivity(Activities activities, List<ActivityPrize> activityPrizes,
            ActivityTemplate activityTemplate, ActivityInfo activityInfo) throws RuntimeException {
        if (activities == null || activityPrizes == null || activityPrizes.size() < 1 || activityTemplate == null) {
            return false;
        }
        // 1、插入活动
        initActivity(activities);
        if (!activitiesService.insert(activities)) {
            logger.info("insert activity failed.{}", JSON.toJSONString(activities));
            return false;
        }

        //1.1、插入活动创建者
        if(!activityCreatorService.insert(ActivityType.fromValue(activities.getType()), activities.getId(), activities.getCreatorId())){
            logger.info("insert activity creator failed.{}", JSON.toJSONString(activities));
            return false;
        }
        
        // 2、插入活动奖品
        initActivityPrizeList(activities, activityPrizes);
        if (!activityPrizeService.batchInsert(activityPrizes)) {
            logger.info("bacth insert ActivityPrize failed.{}", JSON.toJSONString(activityPrizes));
            throw new RuntimeException();
        }

        // 3、插入营销活动模板
        initActivityTemplate(activities, activityTemplate);
        if (!activityTemplateService.insertSelective(activityTemplate)) {
            logger.info("insert ActivityTemplate failed.{}", JSON.toJSONString(activityTemplate));
            throw new RuntimeException();
        }

        // 4、插入活动信息
        initActivityInfo(activities, activityInfo);
        if (!activityInfoService.insertSelective(activityInfo)) {
            logger.info("insert activityInfo failed.{}", JSON.toJSONString(activityInfo));
            throw new RuntimeException();
        }

        return true;
    }

    /**
     * 更新营销活动信息
     *
     * @author qinqinyan
     */
    public boolean updateActivity(Activities activities, List<ActivityPrize> activityPrizes,
            ActivityTemplate activityTemplate, ActivityInfo activityInfo) throws RuntimeException {
        if (!activitiesService.updateByPrimaryKeySelective(activities)) {
            logger.info("更新活动失败!activities-{}, activityPrizes-{}, activityTemplate-{}, activityInfo-{}",
                    JSON.toJSONString(activities), JSON.toJSONString(activityPrizes),
                    JSON.toJSONString(activityTemplate), JSON.toJSONString(activityInfo));
            return false;
        }

        // 奖品信息:删除原有产品，重新插入
        if (!activityPrizeService.deleteByActivityId(activities.getActivityId())) {
            logger.info("删除原活动奖品失败!activities-{}, activityPrizes-{}, activityTemplate-{}, activityInfo-{}",
                    JSON.toJSONString(activities), JSON.toJSONString(activityPrizes),
                    JSON.toJSONString(activityTemplate), JSON.toJSONString(activityInfo));
            throw new RuntimeException();
        }

        initActivityPrizeList(activities, activityPrizes);
        if (!activityPrizeService.batchInsert(activityPrizes)) {
            logger.info("更新活动奖品失败!activities-{}, activityPrizes-{}, activityTemplate-{}, activityInfo-{}",
                    JSON.toJSONString(activities), JSON.toJSONString(activityPrizes),
                    JSON.toJSONString(activityTemplate), JSON.toJSONString(activityInfo));
            throw new RuntimeException();
        }

        if (!activityTemplateService.updateByPrimaryKeySelective(activityTemplate)) {
            logger.info("更新活动失败!activities-{}, activityPrizes-{}, activityTemplate-{}, activityInfo-{}",
                    JSON.toJSONString(activities), JSON.toJSONString(activityPrizes),
                    JSON.toJSONString(activityTemplate), JSON.toJSONString(activityInfo));
            throw new RuntimeException();
        }

        if (!activityInfoService.updateByPrimaryKeySelective(activityInfo)) {
            logger.info("更新活动失败!activities-{}, activityPrizes-{}, activityTemplate-{}, activityInfo-{}",
                    JSON.toJSONString(activities), JSON.toJSONString(activityPrizes),
                    JSON.toJSONString(activityTemplate), JSON.toJSONString(activityInfo));
            throw new RuntimeException();
        }

        return true;
    }

    /**
     * 对于流量池和话费，如果修改了产品大小， 需要重新搜索虚拟产品是否有这个大小的产品，如果没有，需要重新创建；否则替换掉奖品中的产品id
     * 
     */

    /**
     * 初始化活动对象
     *
     * @author qinqinyan
     * @Date
     */
    public void initActivity(Activities activities) {
        activities.setActivityId(SerialNumGenerator.buildSerialNum());
        if ((activities.getType().toString().equals(ActivityType.COMMON_REDPACKET.getCode().toString())
                || activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString()))
                && "sc_jizhong".equals(getProvinceFlag())) {
            // 四川集中化平台的红包创建即是上架状态，原因： 四川目前只有随机红包，不需要审核，而且创建时间就是当下，所以直接是上架状态
            activities.setStatus(ActivityStatus.ON.getCode());
        } else {
            activities.setStatus(ActivityStatus.SAVED.getCode());
        }
        activities.setDeleteFlag(0);
        activities.setCreateTime(new Date());
        activities.setUpdateTime(new Date());
    }

    /**
     * 营销活动生成活动url
     */
    public String getProvinceFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
    }

    /**
     * 初始化活动奖品对象
     *
     * @author qinqinyan
     */
    public void initActivityPrizeList(Activities activities, List<ActivityPrize> activityPrizes) {
        if (activityPrizes != null && activityPrizes.size() > 0) {
            for (ActivityPrize record : activityPrizes) {
                record.setActivityId(activities.getActivityId());
                record.setCreateTime(new Date());
                record.setUpdateTime(new Date());
                record.setDeleteFlag(0);
            }
        }
    }

    /**
     * 初始化活动模板信息
     *
     * @author qinqinyan
     */
    public void initActivityTemplate(Activities activities, ActivityTemplate activityTemplate) {
        activityTemplate.setActivityId(activities.getActivityId());
        activityTemplate.setCreateTime(new Date());
        activityTemplate.setUpdateTime(new Date());
        activityTemplate.setDeleteFlag(0);

        // 用户类型，砸金蛋，大转盘，红包的用户类型有页面设置
        if (!activities.getType().toString().equals(ActivityType.LOTTERY.getCode().toString())
                && !activities.getType().toString().equals(ActivityType.REDPACKET.getCode().toString())
                && !activities.getType().toString().equals(ActivityType.GOLDENBALL.getCode().toString())
                && !activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
            activityTemplate.setUserType(0);
        }

        // 所有活动均为手动设置概率，因此设置为1
        activityTemplate.setFixedProbability(1);
        // 鉴权类型，当前暂且设置为不做鉴权
        activityTemplate.setCheckType(0);
        String checkUrl = globalConfigService.get("GD_LOTTERY_CHECK_URL");
        if ("YES".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.IS_CROWDFUNDING_PLATFORM.getKey()))
                && !StringUtils.isEmpty(checkUrl)) {
            activityTemplate.setCheckType(1);
            activityTemplate.setCheckUrl(checkUrl);
        }
    }

    /**
     * 初始化活动信息
     *
     * @author qinqinyan
     */
    public void initActivityInfo(Activities activities, ActivityInfo activityInfo) {
        activityInfo.setActivityId(activities.getActivityId());
        activityInfo.setCreateTime(new Date());
        activityInfo.setUpdateTime(new Date());
        activityInfo.setDeleteFlag(0);
    }

    /**
     * 创建活动开始定时任务
     *
     * @param schedulerType
     *            定时任务类型
     * @param startTime
     *            活动开始时间
     */
    public boolean createActivityStartSchedule(String activityId, String schedulerType, Date startTime) {
        if (!StringUtils.isEmpty(activityId) && StringUtils.isNotBlank(schedulerType) && startTime != null) {
            ActivityJobPojo pojo = new ActivityJobPojo(activityId);
            String jsonStr = JSON.toJSONString(pojo);

            String msg = "";
            msg = scheduleService.createScheduleJob(ActivityStartJob.class, schedulerType, jsonStr,
                    activityId.toString(), startTime);
            if ("success".equals(msg)) {
                logger.info("常见活动开始定时任务成功，活动ID-{}", activityId);
                return true;
            }
        }
        logger.info("常见活动开始定时任务失败，活动ID-{}", activityId);
        return false;
    }

    /**
     * 创建活动结束定时任务
     *
     * @param schedulerType
     *            定时任务类型
     * @param endTime
     *            活动开始时间
     */
    public boolean createActivityEndSchedule(String activityId, String schedulerType, Date endTime) {
        if (!StringUtils.isEmpty(activityId) && StringUtils.isNotBlank(schedulerType) && endTime != null) {
            ActivityJobPojo pojo = new ActivityJobPojo(activityId);
            String jsonStr = JSON.toJSONString(pojo);

            String msg = "";
            msg = scheduleService.createScheduleJob(ActivityEndJob.class, schedulerType, jsonStr, activityId.toString(),
                    endTime);
            if ("success".equals(msg)) {
                logger.info("常见活动结束定时任务成功，活动ID-{}", activityId);
                return true;
            }
        }
        logger.info("常见活动结束定时任务失败，活动ID-{}", activityId);
        return false;
    }

    /**
     * 活动上架
     *
     * @return 上架成功，返回“success”;上架失败，返回失败原因
     * @author qinqinyan
     */
    public String onShelf(String activityId, String schedulerStart, String schedulerEnd) {

        String result = activitiesService.judgeEnterpriseForActivity(activityId);
        if ("success".equals(result)) {

            // 先向营销模板服务发送上架请求
            AutoResponsePojo pojo = generateActivity(activityId);
            if (pojo.getCode() != 200) {
                logger.info("请求返回:code-" + pojo.getCode() + " ;msg-" + pojo.getMsg());
                return pojo.getMsg() + ",上架失败!";
            }

            // 通过校验，活动直接上架
            Activities activities = activitiesService.selectByActivityId(activityId);

            /**
             * 判断是否需要审核已决定活动是否能上架 1、需要审核，活动状态为6（审核完成） 2、不需要审核。活动状态为0（已保存）
             */
            boolean approvalFlag = approvalProcessDefinitionService
                    .wheatherNeedApproval(ApprovalType.Activity_Approval.getCode());

            if (activities.getStatus().toString().equals(ActivityStatus.FINISH_APPROVAL.getCode().toString())
                    || !approvalFlag
                            && activities.getStatus().toString().equals(ActivityStatus.SAVED.getCode().toString())) {
                // 判断当前时间，如果当前时间在活动之间之后，直接设置为活动开始
                if (System.currentTimeMillis() >= activities.getStartTime().getTime()) {
                    if (activitiesService.changeStatus(activities.getActivityId(),
                            ActivityStatus.PROCESSING.getCode())) {

                        if (!createActivityEndSchedule(activityId, schedulerEnd, activities.getEndTime())) {
                            logger.error("创建定时任务(用于活动结束)失败,活动ID-" + activities.getActivityId());
                            return "常见定时任务失败!";
                        }
                        return result;
                    }
                    return "活动上架失败";
                } else {
                    if (activitiesService.changeStatus(activities.getActivityId(), ActivityStatus.ON.getCode())) {
                        // 创建定时任务
                        if (!createActivityStartSchedule(activityId, schedulerStart, activities.getStartTime())) {
                            logger.error("创建定时任务(用于活动开始)失败,活动ID-" + activities.getActivityId());
                            return "创建定时任务失败!";
                        }
                        if (!createActivityEndSchedule(activityId, schedulerEnd, activities.getEndTime())) {
                            logger.error("创建定时任务(用于活动结束)失败,活动ID-" + activities.getActivityId());
                            return "创建定时任务失败!";
                        }
                        return result;
                    }
                    return "活动上架失败";
                }
            }
            return "活动状态异常,上架失败!";
        }
        return result;
    }

    /**
     * 关闭活动(下架)
     *
     * @author qinqinyan
     */
    @Transactional
    public boolean offShelf(String activityId) throws RuntimeException {
        if (!StringUtils.isBlank(activityId)) {
            ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
            if (activityInfo != null && StringUtils.isNotBlank(activityInfo.getUrl())) {
                // 1、向营销模板服务发送下架请求
                if (!activityTemplateService.notifyTemplateToClose(activityInfo.getUrl(), activityId)) {
                    logger.info("像营销模板服务请求下架失败，活动ID-{}", activityId);
                    return false;
                }
                // 2、更改活动状态
                if (!activitiesService.changeStatus(activityId, ActivityStatus.DOWN.getCode())) {
                    logger.info("修改活动状态失败，活动ID-{}", activityId);
                    throw new RuntimeException();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 向营销模板发送请求生成活动
     *
     * @author qinqinyan
     */
    public AutoResponsePojo generateActivity(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return null;
        }
        String sendData = getSendData(activityId);
        logger.info("开始向模板侧发起创建活动请求");

        String response = HttpConnection.doPost(getUrl(), sendData, "application/json", "utf-8", true);
        logger.info("创建活动生成结果:" + response);

        AutoResponsePojo result = JSONObject.parseObject(response, AutoResponsePojo.class);

        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId(activityId);
        activityInfo.setUrl(result.getUrl());
        Integer code = result.getCode();
        activityInfo.setCode(code.toString());
        activityInfoService.updateByPrimaryKeySelective(activityInfo);

        return result;
    }

    /**
     * 营销活动生成活动url
     */
    public String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_GENERATE_URL.getKey());
    }

    /**
     * 构造请求参数
     *
     * @author qinqinyan
     */
    public String getSendData(String activityId) {
        if (StringUtils.isNotBlank(activityId)) {
            Activities activities = activitiesService.selectByActivityId(activityId);
            ActivityTemplate activityTemplate = activityTemplateService.selectByActivityId(activityId);
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityId(activityId);

            if (activities != null && activities.getEntId() != null && activityTemplate != null
                    && activityPrizes != null && activityPrizes.size() > 0) {

                Enterprise enterprise = enterprisesService.selectById(activities.getEntId());

                // 活动时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                AutoTimePojo timePojo = new AutoTimePojo();
                timePojo.setStartTime(sdf.format(activities.getStartTime()));
                timePojo.setEndTime(sdf.format(activities.getEndTime()));

                // 奖品信息
                List<AutoPrizesPojo> autoPrizesPojoList = getPrizesLists(activities, activityPrizes);

                // 平台信息
                AutoGeneratePojo pojo = new AutoGeneratePojo();
                pojo.setPlateName(getPlateName());
                pojo.setEnterpriseId(activities.getEntId().toString());
                pojo.setEnterpriseName(enterprise.getName());
                pojo.setActiveId(activities.getActivityId());
                pojo.setActiveName(activities.getName());
                pojo.setUserType(activityTemplate.getUserType());
                pojo.setGameType(converterForGameType(activities.getType()));
                pojo.setGivedNumber(activityTemplate.getGivedNumber());
                pojo.setMaxPlayNumber(activityTemplate.getMaxPlayNumber());
                if (activityTemplate.getDaily().intValue() == 0) {
                    pojo.setDaily("true");
                } else {
                    pojo.setDaily("false");
                }
                pojo.setCheckType(activityTemplate.getCheckType());
                pojo.setCheckUrl(activityTemplate.getCheckUrl());
                pojo.setFixedProbability(1); // 默认都是固定概率

                if (activities.getType().toString().equals(ActivityType.REDPACKET.getCode().toString())
                        || activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
                    String object = "";
                    if (StringUtils.isNotBlank(activityTemplate.getObject())) {
                        String[] objectArray = activityTemplate.getObject().split("\\|");
                        for (int i = 0; i < objectArray.length; i++) {
                            if (i == 0) {
                                object = objectArray[i];
                            } else {
                                object += "<br>" + objectArray[i] + "</br>";
                            }
                        }
                    }
                    pojo.setObject(object);
                } else {
                    pojo.setObject(activityTemplate.getObject());
                }

                if (activities.getType().toString().equals(ActivityType.REDPACKET.getCode().toString())
                        || activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
                    String rules = "";
                    if (StringUtils.isNotBlank(activityTemplate.getRules())) {
                        String[] ruleArray = activityTemplate.getRules().split("\\|");
                        for (int i = 0; i < ruleArray.length; i++) {
                            if (i == 0) {
                                rules = ruleArray[i];
                            } else {
                                rules += "<br>" + ruleArray[i] + "</br>";
                            }
                        }
                    }
                    pojo.setRules(rules);
                } else {
                    pojo.setRules(activityTemplate.getRules());
                }
                pojo.setStatus(ActivityStatus.ON.getCode());
                pojo.setChargeUrl(getCallbackUrl());
                
                /**
                 * edit by qinqinyan on 2017/07/14
                 * 增加两个参数：对于微信相关活动，是否向公众号请求绑定用户的手机号
                 * */
                if("gd_mdrc".equals(getProvinceFlag())
                        && "YES".equals(globalConfigService.get(GlobalConfigKeyEnum.IS_CROWDFUNDING_PLATFORM.getKey()))
                        && "1".equals(activityTemplate.getUserType().toString())){
                    //广东众筹平台要向微信相关活动请求用户手机号
                    pojo.setGetUserFromWxFlag(getActivityGetMobileFromWxFlag());
                    pojo.setGetUserFromWxUrl(getActivityGetMobileFromWxUrl());
                }
                
                pojo.setTime(timePojo);
                pojo.setPrizes(autoPrizesPojoList);

                return JSON.toJSONString(pojo);
            }
        }
        return null;
    }
    
    /**
     * 营销活动是否向公众号获取用户手机号标志位
     * @author qinqinyan
     * @date 2017/07/14
     * */
    public String getActivityGetMobileFromWxFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_GET_MOBILE_FROM_WX_FLAG.getKey());
    }
    
    /**
     * 营销活动是否向公众号获取用户手机号url
     * @author qinqinyan
     * @date 2017/07/14
     * */
    public String getActivityGetMobileFromWxUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_GET_MOBILE_FROM_WX_URL.getKey());
    }

    /**
     * 获取回调url,返回中奖记录
     */
    public String getCallbackUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_CALLBACK_URL.getKey());
    }

    /**
     * 平台活动类型与营销活动服务活动类型转化器
     *
     * @param plateGameType
     *            平台活动类型
     * @author qinqinyan
     */
    public Integer converterForGameType(Integer plateGameType) {
        if (ActivityType.REDPACKET.getCode().toString().equals(plateGameType.toString())) {
            return ActivityTemplateType.REDPACKET.getCode();
        } else if (ActivityType.LOTTERY.getCode().toString().equals(plateGameType.toString())) {
            return ActivityTemplateType.LOTTERY.getCode();
        } else if (ActivityType.GOLDENBALL.getCode().toString().equals(plateGameType.toString())) {
            return ActivityTemplateType.GOLDENBALL.getCode();
        }
        if (ActivityType.LUCKY_REDPACKET.getCode().toString().equals(plateGameType.toString())) {
            return ActivityTemplateType.LUCKY_REDPACKET.getCode();
        } else {
            return null;
        }
    }

    /**
     * 平台标示
     */
    public String getPlateName() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_PLATFORM_NAME.getKey());
    }

    private List<AutoPrizesPojo> getPrizesLists(Activities activities, List<ActivityPrize> activityPrizes) {
        List<AutoPrizesPojo> autoPrizesPojoList = new ArrayList<AutoPrizesPojo>();

        List<ActivityPrize> activityPrize0 = getPrizesByIdPrefix(activityPrizes,
                ActivityPrizeRank.FirstRankPrize.getIdPrefix());
        List<ActivityPrize> activityPrize1 = getPrizesByIdPrefix(activityPrizes,
                ActivityPrizeRank.SecondRankPrize.getIdPrefix());
        List<ActivityPrize> activityPrize2 = getPrizesByIdPrefix(activityPrizes,
                ActivityPrizeRank.ThirdRankPrize.getIdPrefix());
        List<ActivityPrize> activityPrize3 = getPrizesByIdPrefix(activityPrizes,
                ActivityPrizeRank.ForthRankPrize.getIdPrefix());
        List<ActivityPrize> activityPrize4 = getPrizesByIdPrefix(activityPrizes,
                ActivityPrizeRank.FifthRankPrize.getIdPrefix());
        List<ActivityPrize> activityPrize5 = getPrizesByIdPrefix(activityPrizes,
                ActivityPrizeRank.SixthRankPrize.getIdPrefix());

        AutoPrizesPojo autoPrizesPojo0 = createAutoPrizesPojo(activityPrize0, activities.getEntId());
        AutoPrizesPojo autoPrizesPojo1 = createAutoPrizesPojo(activityPrize1, activities.getEntId());
        AutoPrizesPojo autoPrizesPojo2 = createAutoPrizesPojo(activityPrize2, activities.getEntId());
        AutoPrizesPojo autoPrizesPojo3 = createAutoPrizesPojo(activityPrize3, activities.getEntId());
        AutoPrizesPojo autoPrizesPojo4 = createAutoPrizesPojo(activityPrize4, activities.getEntId());
        AutoPrizesPojo autoPrizesPojo5 = createAutoPrizesPojo(activityPrize5, activities.getEntId());

        if (autoPrizesPojo0 != null) {
            autoPrizesPojoList.add(autoPrizesPojo0);
        }
        if (autoPrizesPojo1 != null) {
            autoPrizesPojoList.add(autoPrizesPojo1);
        }
        if (autoPrizesPojo2 != null) {
            autoPrizesPojoList.add(autoPrizesPojo2);
        }
        if (autoPrizesPojo3 != null) {
            autoPrizesPojoList.add(autoPrizesPojo3);
        }
        if (autoPrizesPojo4 != null) {
            autoPrizesPojoList.add(autoPrizesPojo4);
        }
        if (autoPrizesPojo5 != null) {
            autoPrizesPojoList.add(autoPrizesPojo5);
        }
        return autoPrizesPojoList;
    }

    private AutoPrizesPojo createAutoPrizesPojo(List<ActivityPrize> activityPrizes, Long entId) {
        if (activityPrizes != null && activityPrizes.size() > 0) {
            AutoPrizesPojo autoPrizesPojo = new AutoPrizesPojo();

            autoPrizesPojo.setIdPrefix(Integer.parseInt(activityPrizes.get(0).getIdPrefix()));
            autoPrizesPojo.setRankName(activityPrizes.get(0).getRankName());
            autoPrizesPojo.setProbability(activityPrizes.get(0).getProbability());
            Integer count = activityPrizes.get(0).getCount().intValue();
            autoPrizesPojo.setCount(count);

            for (ActivityPrize item : activityPrizes) {
                Product product = productService.selectProductById(item.getProductId());

                // 判断奖品类型
                Integer flowType = 1; // 默认流量包
                Integer totalFlowPoolCount = count; // 流量池产品总大小，随机红包使用该字段，为了通过营销模板校验，其余活动用产品count
                if (product.getType().byteValue() == Constants.ProductType.FLOW_ACCOUNT.getValue()) {
                    // 流量池
                    flowType = 0;
                    // 数据库存储的是流量池大小时KB,所以这里需要转化成MB
                    totalFlowPoolCount = (int) (product.getProductSize().longValue() / 1024);
                }
                if (product.getIsp().equals(IspType.CMCC.getValue())) {
                    autoPrizesPojo.setCmccName(item.getPrizeName());
                    autoPrizesPojo.setCmccEnterId(entId.toString());
                    autoPrizesPojo.setCmccId(item.getProductId().toString());
                    autoPrizesPojo.setCmccType(flowType);
                    autoPrizesPojo.setCmccResponse(1);
                    autoPrizesPojo.setCmccCount(totalFlowPoolCount);
                } else if (product.getIsp().equals(IspType.UNICOM.getValue())) {
                    autoPrizesPojo.setCuccName(item.getPrizeName());
                    autoPrizesPojo.setCuccEnterId(entId.toString());
                    autoPrizesPojo.setCuccId(item.getProductId().toString());
                    autoPrizesPojo.setCuccType(flowType);
                    autoPrizesPojo.setCuccResponse(1);
                    autoPrizesPojo.setCuccCount(totalFlowPoolCount);
                } else if (product.getIsp().equals(IspType.TELECOM.getValue())) {
                    autoPrizesPojo.setCtccName(item.getPrizeName());
                    autoPrizesPojo.setCtccEnterId(entId.toString());
                    autoPrizesPojo.setCtccId(item.getProductId().toString());
                    autoPrizesPojo.setCtccType(flowType);
                    autoPrizesPojo.setCtccResponse(1);
                    autoPrizesPojo.setCtccCount(totalFlowPoolCount);
                } else {
                    autoPrizesPojo.setOtherName(item.getPrizeName());
                    autoPrizesPojo.setOtherEnterId(entId.toString());
                    autoPrizesPojo.setOtherId(item.getProductId().toString());
                    autoPrizesPojo.setOtherType(flowType);
                    autoPrizesPojo.setOtherResponse(1);
                    autoPrizesPojo.setOtherCount(totalFlowPoolCount);
                }
            }
            return autoPrizesPojo;
        }
        return null;
    }

    /***
     * 根据idPrefix获取指定等级的所有奖品
     *
     * @param idPrefix
     *            奖品等级
     * @author qinqinyan
     */
    private List<ActivityPrize> getPrizesByIdPrefix(List<ActivityPrize> activityPrizes, String idPrefix) {
        List<ActivityPrize> list = new ArrayList<ActivityPrize>();
        for (int i = 0; i < activityPrizes.size(); i++) {
            if (activityPrizes.get(i).getIdPrefix().equals(idPrefix)) {
                list.add(activityPrizes.get(i));
            }
        }
        return list;
    }

    /**
     * 获取指定等级的奖项，用于前端渲染
     *
     * @param idPrefix
     *            奖项等级
     * @author qinqinyan
     */
    public AutoPrizesPojo getAutoPrizesPojo(List<ActivityPrize> activityPrizes, String idPrefix) {
        AutoPrizesPojo pojo = new AutoPrizesPojo();
        if (activityPrizes != null && activityPrizes.size() > 0) {

            for (ActivityPrize item : activityPrizes) {
                if (item.getIdPrefix().equals(idPrefix)) {

                    if (pojo.getIdPrefix() == null) {
                        pojo.setIdPrefix(Integer.getInteger(idPrefix));
                    }
                    if (pojo.getCount() == null) {
                        pojo.setCount(item.getCount().intValue());
                    }
                    if (StringUtils.isBlank(pojo.getProbability())) {
                        pojo.setProbability(item.getProbability());
                    }

                    if (item.getIsp().equals(IspType.CMCC.getValue())) {
                        pojo.setCmccName(item.getProductName());
                        pojo.setCmccId(item.getProductId().toString());
                        pojo.setCmccPrice(item.getPrice());
                        pojo.setCmccOwnershipRegion(item.getOwnershipRegion());
                        pojo.setCmccRoamingRegion(item.getRoamingRegion());
                        pojo.setCmccProductSize(item.getProductSize());
                        pojo.setCmccProductCode(item.getProductCode());
                        pojo.setCmccType(item.getType());
                    } else if (item.getIsp().equals(IspType.UNICOM.getValue())) {
                        pojo.setCuccName(item.getProductName());
                        pojo.setCuccId(item.getProductId().toString());
                        pojo.setCuccPrice(item.getPrice());
                        pojo.setCuccOwnershipRegion(item.getOwnershipRegion());
                        pojo.setCuccRoamingRegion(item.getRoamingRegion());
                        pojo.setCuccProductSize(item.getProductSize());
                        pojo.setCuccProductCode(item.getProductCode());
                        pojo.setCuccType(item.getType());
                    } else if (item.getIsp().equals(IspType.TELECOM.getValue())) {
                        pojo.setCtccName(item.getProductName());
                        pojo.setCtccId(item.getProductId().toString());
                        pojo.setCtccPrice(item.getPrice());
                        pojo.setCtccOwnershipRegion(item.getOwnershipRegion());
                        pojo.setCtccRoamingRegion(item.getRoamingRegion());
                        pojo.setCtccProductSize(item.getProductSize());
                        pojo.setCtccProductCode(item.getProductCode());
                        pojo.setCtccType(item.getType());
                    } else {
                        pojo.setOtherName(item.getProductName());
                        pojo.setOtherId(item.getProductId().toString());
                        pojo.setOtherPrice(item.getPrice());
                        pojo.setOtherOwnershipRegion(item.getOwnershipRegion());
                        pojo.setOtherRoamingRegion(item.getRoamingRegion());
                        pojo.setOtherProductSize(item.getProductSize());
                        pojo.setOtherProductCode(item.getProductCode());
                        pojo.setOtherType(item.getType());
                    }
                }
            }
        }
        return pojo;
    }

    /**
     * 对奖项进行按一等奖，移动，联通，电信进行排序
     *
     * @author qinqinyan
     */
    public List<List<ActivityPrize>> sortActivityPrizes(List<ActivityPrize> activityPrizes) {
        if (activityPrizes != null && activityPrizes.size() > 0) {
            List<List<ActivityPrize>> sortList = new ArrayList<List<ActivityPrize>>();

            for (int i = 0; i < 6; i++) {
                Integer idPrefix = i;
                List<ActivityPrize> rankPrizes = getPrizesByIdPrefix(activityPrizes, idPrefix.toString());
                if (rankPrizes != null && rankPrizes.size() > 0) {

                    List<ActivityPrize> ispSortList = new ArrayList<ActivityPrize>();

                    ActivityPrize cmccActivityPrize = getByIsp(rankPrizes, IspType.CMCC.getValue());
                    ActivityPrize cuccActivityPrize = getByIsp(rankPrizes, IspType.UNICOM.getValue());
                    ActivityPrize ctccActivityPrize = getByIsp(rankPrizes, IspType.TELECOM.getValue());

                    if (cmccActivityPrize != null) {
                        ispSortList.add(cmccActivityPrize);
                    }
                    if (cuccActivityPrize != null) {
                        ispSortList.add(cuccActivityPrize);
                    }
                    if (ctccActivityPrize != null) {
                        ispSortList.add(ctccActivityPrize);
                    }

                    sortList.add(ispSortList);
                }
            }
            return sortList;
        }
        return null;
    }

    /**
     * 根据运营商查找产品
     *
     * @param activityPrizes
     *            已经根据奖项等级筛选出来的产品列表
     * @param isp
     *            运营商
     * @author qinqinyan
     */
    private ActivityPrize getByIsp(List<ActivityPrize> activityPrizes, String isp) {
        if (activityPrizes != null && activityPrizes.size() > 0) {
            for (ActivityPrize item : activityPrizes) {
                if (item.getIsp().equals(isp)) {
                    return item;
                }
            }
        }
        return null;
    }

}

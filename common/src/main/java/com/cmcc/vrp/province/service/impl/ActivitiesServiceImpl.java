package com.cmcc.vrp.province.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityTemplateType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.BlackAndWhiteListType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.CrowdFundingJoinType;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.enums.PaymentStatus;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.enums.QRCodeExtendParam;
import com.cmcc.vrp.enums.SchedulerType;
import com.cmcc.vrp.enums.SupplierType;
import com.cmcc.vrp.enums.WxTemplateMsgType;
import com.cmcc.vrp.province.activity.model.AutoGeneratePojo;
import com.cmcc.vrp.province.activity.model.AutoPrizesPojo;
import com.cmcc.vrp.province.activity.model.AutoResponsePojo;
import com.cmcc.vrp.province.activity.model.AutoTimePojo;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.dao.ActivitiesMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.UrlMap;
import com.cmcc.vrp.province.quartz.jobs.ActivityEndJob;
import com.cmcc.vrp.province.quartz.jobs.ActivityJobPojo;
import com.cmcc.vrp.province.quartz.jobs.ActivityStartJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.ApprovalRequestSmsService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.UrlMapService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.queue.pojo.ActivitySendMessagePojo;
import com.cmcc.vrp.queue.pojo.FlowcoinPresentPojo;
import com.cmcc.vrp.util.AES;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.util.UUID8;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.cmcc.webservice.crowdfunding.CrowdfundingCallbackService;
import com.google.gson.Gson;

/**
 * Created by qinqinyan on 2016/8/17.
 */
@Service("activitiesService")
public class ActivitiesServiceImpl extends AbstractCacheSupport implements ActivitiesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiesServiceImpl.class);
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    ActivityBlackAndWhiteService activityBlackAndWhiteService;
    @Autowired
    AccountService accountService;
    @Autowired
    ProductService productService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalDetailDefinitionService approvalDetailDefinitionService;
    @Autowired
    RoleService roleService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    IndividualProductMapService individualProductMapService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    ActivityTemplateService activityTemplateService;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    EntFlowControlService entFlowControlService;
    @Autowired
    RedisUtilService redisUtilService;
    @Autowired
    UrlMapService urlMapService;
    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    @Autowired
    CrowdfundingCallbackService crowdfundingCallbackService;
    @Autowired
    WetchatService wetchatService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;
    @Autowired
    AdminManagerService adminManagerService;    
    @Autowired
    private ApprovalRequestSmsService approvalRequestSmsService;
    
    @Autowired
    ActivityCreatorService activityCreatorService;

    /**
     * insert
     * */
    @Override
    public boolean insert(Activities record) {
        if (record == null) {
            return false;
        }
        return activitiesMapper.insert(record) == 1;
    }

    /**
     * selectByPrimaryKey
     * */
    @Override
    public Activities selectByPrimaryKey(Long id) {
        if (id == null) {
            return null;
        }
        return activitiesMapper.selectByPrimaryKey(id);
    }

    /**
     * updateByPrimaryKeySelective
     * */
    @Override
    public boolean updateByPrimaryKeySelective(Activities record) {
        if (record == null) {
            return false;
        }
        return activitiesMapper.updateByPrimaryKeySelective(record) == 1;
    }

    /**
     * selectByActivityId
     * */
    @Override
    public Activities selectByActivityId(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return null;
        }
        return activitiesMapper.selectByActivityId(activityId);
    }

    /**
     * selectActivityListByActivityId
     * */
    @Override
    public List<Activities> selectActivityListByActivityId(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return null;
        }
        return activitiesMapper.selectActivityListByActivityId(activityId);
    }

    /**
     * selectByMapForGDCrowdFunding
     * */
    @Override
    public List<Activities> selectByMapForGDCrowdFunding(Map map) {

        String managerId = map.get("managerId").toString();

        if (map.get("status") != null) {
            map.put("statusList", map.get("status").toString().split(","));
            map.remove("status");
        }

        if (!StringUtils.isEmpty(managerId)) {
            List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(Long.valueOf(managerId));
            if (enterprises != null && enterprises.size() > 0) {
                map.put("enterprises", enterprises);
                return activitiesMapper.selectByMapForGDCrowdFunding(map);
            }
        }
        return null;
    }

    /**
     * countByMapGDCrowdFunding
     * */
    @Override
    public Long countByMapGDCrowdFunding(Map map) {
        String managerId = map.get("managerId").toString();

        if (map.get("status") != null) {
            map.put("statusList", map.get("status").toString().split(","));
            map.remove("status");
        }

        if (!StringUtils.isEmpty(managerId)) {
            List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(Long.valueOf(managerId));
            if (enterprises != null && enterprises.size() > 0) {
                map.put("enterprises", enterprises);
                return activitiesMapper.countByMapGDCrowdFunding(map);
            }
        }
        return 0L;
    }

    /**
     * selectByMap
     * */
    @Override
    public List<Activities> selectByMap(Map map) {
        String managerId = map.get("managerId").toString();

        if (map.get("status") != null) {
            map.put("statusList", map.get("status").toString().split(","));
            map.remove("status");
        }

        if(map.get("type")!=null){
            map.put("types", map.get("type").toString().split(","));
        }

        if (!StringUtils.isEmpty(managerId)) {
            List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(Long.valueOf(managerId));
            if (enterprises != null && enterprises.size() > 0) {
                map.put("enterprises", enterprises);
                if (map.get("type").toString().equals("7,8")) {
                    return activitiesMapper.selectByMapForRedpacket(map);
                } else {
                    return activitiesMapper.selectByMapForActivityTemplate(map);
                }
            } else {
                return activitiesMapper.selectByMapForActivityTemplate(map);
            }
        }
        return null;
    }

    /**
     * countByMap
     * */
    @Override
    public Long countByMap(Map map) {
        String managerId = map.get("managerId").toString();
        if (map.get("status") != null) {
            map.put("statusList", map.get("status").toString().split(","));
            map.remove("status");
        }

        if(map.get("type")!=null){
            map.put("types", map.get("type").toString().split(","));
        }

        if (!StringUtils.isEmpty(managerId)) {
            List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(Long.valueOf(managerId));
            if (enterprises != null && enterprises.size() > 0) {
                map.put("enterprises", enterprises);
                if (map.get("type").toString().equals("7,8")) {
                    return activitiesMapper.countByMapForRedpacket(map);
                } else {
                    return activitiesMapper.countByMap(map);
                }
            } else {
                return activitiesMapper.countByMap(map);
            }
        }
        return null;
    }

    /**
     * insertFlowcardActivity
     * */
    @Override
    @Transactional
    public boolean insertFlowcardActivity(Activities activities, Long cmProductId, Long cuProductId, Long ctProductId,
            String cmMobileList, String cuMobileList, String ctMobileList, String cmUserSet, String cuUserSet,
            String ctUserSet) {
        // 1、插入活动记录
        initActivity(activities);
        if (!insert(activities)) {
            LOGGER.info("插入活动记录失败：" + JSONArray.toJSONString(activities));
            return false;
        }

        // 2、插入赠送手机号码
        if (!activityWinRecordService.batchInsertForFlowcard(activities.getActivityId(), cmProductId, cuProductId,
                ctProductId, cmMobileList, cuMobileList, ctMobileList)) {
            LOGGER.info("插入手机号失败：" + JSONArray.toJSONString(activities));
            throw new RuntimeException();
        }

        // 3、插入活动奖品信息
        if (!activityPrizeService.batchInsertForFlowCard(activities, cmProductId, cuProductId, ctProductId,
                cmMobileList, cuMobileList, ctMobileList)) {
            LOGGER.info("插入活动奖品信息失败：" + JSONArray.toJSONString(activities));
            throw new RuntimeException();
        }

        // 4、插入活动详情
        if (!activityInfoService.insertActivityInfo(activities, cmProductId, cuProductId, ctProductId, cmMobileList,
                cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet)) {
            LOGGER.info("插入活动详情失败：" + JSONArray.toJSONString(activities));
            throw new RuntimeException();
        }

        return true;
    }

    /**
     * 插入二维码
     *
     * @author qinqinyan
     */
    @Override
    @Transactional
    public boolean insertQRcodeActivity(Activities activities, ActivityInfo activityInfo, Long cmProductId,
            Long cuProductId, Long ctProductId, String correctMobiles) {
        // 1、插入活动记录
        initActivity(activities);
        if (!insert(activities)) {
            LOGGER.info("插入活动记录失败：" + JSONArray.toJSONString(activities));
            return false;
        }
        //1.1 插入活动创建者表
        if(!activityCreatorService.insert(ActivityType.QRCODE, activities.getId(), activities.getCreatorId())){
            LOGGER.info("二维码插入活动创建者记录失败：" + JSONArray.toJSONString(activities));
            return false;
        }

        // 2、插入活动奖品信息
        if (!activityPrizeService.batchInsertForQRcord(activities, cmProductId, cuProductId, ctProductId)) {
            LOGGER.info("插入活动奖品信息失败：" + JSONArray.toJSONString(activities));
            throw new RuntimeException();
        }

        // 3、插入黑白名单
        if (activityInfo.getHasWhiteOrBlack().intValue() != BlackAndWhiteListType.NOLIST.getCode().intValue()) {
            if (!activityBlackAndWhiteService.batchInsert(activities.getActivityId(), activityInfo.getHasWhiteOrBlack(),
                    correctMobiles)) {
                LOGGER.info("插入黑白名单失败：" + JSONArray.toJSONString(activities) + " ;名单类型:"
                        + activityInfo.getHasWhiteOrBlack());
                throw new RuntimeException();
            }
        }

        // 4、插入活动详情
        if (!activityInfoService.insertActivityInfoForQrcode(activities, activityInfo, cmProductId, cuProductId,
                ctProductId)) {
            LOGGER.info("插入活动详情失败：" + JSONArray.toJSONString(activities));
            throw new RuntimeException();
        }

        return true;
    }

    /**
     * participate
     * */
    @Override
    public boolean participate(String activityId, String mobile, Map<Object, Object> extendParams, 
            String channel) {
        // 校验参数
        Activities activities = null;
        ActivityType type = null;

        if (!org.apache.commons.lang.StringUtils.isBlank(activityId) && "flowcard".equals(activityId)) {
            if (!StringUtils.isValidMobile(mobile) || (type = ActivityType.FLOWCARD) == null) {
                LOGGER.error("流量卡活动无效的充值手机号码{}", mobile);
                return false;
            }
        } else { // 不是流量券的活动，按照以下校验进行
            if (org.apache.commons.lang.StringUtils.isBlank(activityId) || !StringUtils.isValidMobile(mobile)
                    || (activities = activitiesMapper.selectByActivityId(activityId)) == null || !validate(activities)
                    || (type = ActivityType.fromValue(activities.getType())) == null) {
                LOGGER.error("无效的活动ID参数或手机号码，活动ID = {}, 手机号码为{}, 活动详情为{}.", activityId, mobile,
                        new Gson().toJson(activities));
                return false;
            }
        }

        // 按活动类型处理
        boolean result = false;
        switch (type) {
            case QRCODE: // 二维码
                result = participateQRCode(activities, mobile, extendParams);
                break;
            case FLOWCARD: // 流量券
                result = participateFlowCard(mobile, extendParams, channel);
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * participateQRCode
     * */
    // 参与二维码活动
    private boolean participateQRCode(Activities activities, String mobile, Map<Object, Object> extendParams) {
        ActivityWinRecord activityWinRecord = null;
        String activityWinRecordId = (String) extendParams.get(QRCodeExtendParam.ACTIVITY_RECORD_ID.getKey());
        if (StringUtils.isEmpty(activityWinRecordId)
                || (activityWinRecord = activityWinRecordService.selectByRecordId(activityWinRecordId)) == null) {
            LOGGER.error("无效的活动记录ID, ActivityWinRecordId = {}.", activityWinRecordId);
            return false;
        }

        if (!StringUtils.isEmpty(activityWinRecord.getOwnMobile())) {
            LOGGER.error("该二维码已被使用, ActivityWinRecordId = {}, status = {}.", activityWinRecordId,
                    activityWinRecord.getStatus());
            return false;
        }

        // 判断活动状态是否正常
        if (activities.getEndTime().before(new Date()) || activities.getStartTime().after(new Date())
                || !activities.getStatus().equals(ActivityStatus.PROCESSING.getCode())) {
            LOGGER.error("活动状态异常，activities={}.", JSON.toJSONString(activities));
            return false;
        }

        // 判断卡状态是否正常
        ChargeRecordStatus crs = null;
        if (activityWinRecord.getStatus() != null
                && (crs = ChargeRecordStatus.fromValue(activityWinRecord.getStatus())) != null
                && crs != ChargeRecordStatus.UNUSED) {
            LOGGER.error("当前的记录状态异常，当前的记录状态为{}.", activityWinRecord.getStatus());
            return false;
        }

        ActivityInfo activityInfo = null;
        if (StringUtils.isEmpty(activities.getActivityId())
                || (activityInfo = activityInfoService.selectByActivityId(activities.getActivityId())) == null) {
            LOGGER.error("活动信息不全, activityId = {}.", activities.getActivityId());
            return false;
        }

        // 判断是否在黑白名单中
        if (activityInfo.getHasWhiteOrBlack() != null && activityInfo.getHasWhiteOrBlack() != 0) {
            if (activityInfo.getHasWhiteOrBlack() == 1) {
                Map map = new HashMap();
                map.put("activityId", activities.getActivityId());
                map.put("isWhite", "1");
                map.put("mobile", mobile);
                List<String> phones = activityBlackAndWhiteService.selectPhonesByMap(map);
                if (!phones.contains(mobile)) {
                    LOGGER.info("号码{}不在活动{}的白名单中", mobile, activities.getActivityId());

                    // activityWinRecord.setStatus(ChargeRecordStatus.INVALIDED.getCode());
                    activityWinRecord.setChargeMobile(mobile);
                    // activityWinRecord.setOwnMobile(mobile);
                    activityWinRecord.setReason("非白名单用户");
                    activityWinRecord.setWinTime(new Date());
                    // 更新记录状态，直接废弃该二维码
                    activityWinRecordService.updateByPrimaryKey(activityWinRecord);
                    return false;
                }
            }
            if (activityInfo.getHasWhiteOrBlack() == 2) {
                Map map = new HashMap();
                map.put("activityId", activities.getActivityId());
                map.put("isWhite", "2");
                map.put("mobile", mobile);
                List<String> phones = activityBlackAndWhiteService.selectPhonesByMap(map);
                if (phones.contains(mobile)) {
                    // activityWinRecord.setStatus(ChargeRecordStatus.INVALIDED.getCode());
                    activityWinRecord.setChargeMobile(mobile);
                    // activityWinRecord.setOwnMobile(mobile);
                    activityWinRecord.setReason("黑名单用户");
                    activityWinRecord.setWinTime(new Date());
                    // 更新记录状态，直接废弃该二维码
                    activityWinRecordService.updateByPrimaryKey(activityWinRecord);
                    LOGGER.info("号码{}在活动{}的黑名单中", mobile, activities.getActivityId());
                    return false;
                }
            }
        }

        // 查询号码运营商信息
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        if (phoneRegion == null) {
            LOGGER.error("查询不到号码归属地. Mobile = {}", mobile);
            return false;
        }

        // 活动奖品与中奖用户的运营商是否匹配
        List<ActivityPrize> prizes = activityPrizeService.getDetailByActivityId(activities.getActivityId());
        if (prizes == null) {
            LOGGER.error("活动奖品为空. activityId = {}.", activities.getActivityId());
            return false;
        }
        // 判断中奖用户的运营商信息与活动奖品的运营商信息是否匹配
        // 获取奖品ID
        boolean ispTag = false;
        Long productId = null;
        for (ActivityPrize prize : prizes) {
            if (prize.getIsp().equals(SupplierType.fromCode(phoneRegion.getSupplier()).getCode())) {
                ispTag = true;
                productId = prize.getProductId();
                break;
            }
        }

        if (!ispTag) {
            LOGGER.error("中奖用户的运营商信息与活动奖品的运营商信息不匹配. Mobile = {}, PhoneRegion = {}, activityId = {}.", mobile,
                    new Gson().toJson(phoneRegion), activities.getActivityId());
            // activityWinRecord.setStatus(ChargeRecordStatus.INVALIDED.getCode());
            activityWinRecord.setChargeMobile(mobile);
            // activityWinRecord.setOwnMobile(mobile);
            activityWinRecord.setReason("中奖用户的运营商信息与活动奖品的运营商信息不匹配");
            activityWinRecord.setWinTime(new Date());
            // 更新记录状态，直接废弃该二维码
            activityWinRecordService.updateByPrimaryKey(activityWinRecord);
            return false;
        }

        // 回填产品信息、运营商信息
        activityWinRecord.setPrizeId(productId);
        activityWinRecord.setIsp(SupplierType.fromCode(phoneRegion.getSupplier()).getCode());

        if (!activityWinRecordService.updateByPrimaryKey(activityWinRecord)) {
            LOGGER.error("更新活动产品Id时出错. ActivityWinRecordId = {}.", activityWinRecordId);
            return false;
        }

        // 判断企业状态
        Enterprise enter = enterprisesService.selectById(activities.getEntId());
        if (enter == null
                || !(enter.getStatus().toString().equals("3") && enter.getDeleteFlag().toString().equals("0"))) {
            LOGGER.error("企业异常, entId = {}， activityId = {}.", activities.getEntId(), activities.getActivityId());
            // activityWinRecord.setStatus(ChargeRecordStatus.INVALIDED.getCode());
            activityWinRecord.setChargeMobile(mobile);
            // activityWinRecord.setOwnMobile(mobile);
            activityWinRecord.setReason("企业状态异常");
            activityWinRecord.setWinTime(new Date());
            // 更新记录状态，直接废弃该二维码
            activityWinRecordService.updateByPrimaryKey(activityWinRecord);
            return false;
        }

        // 判断企业与产品是否已经取消关联关系
        EntProduct entProduct = getEntProduct(productId, activities.getEntId());
        if (entProduct == null) {
            LOGGER.error("企业与产品取消关联关系. entId = {}, productId = {}.", activities.getEntId(), productId);
            // activityWinRecord.setStatus(ChargeRecordStatus.INVALIDED.getCode());
            activityWinRecord.setChargeMobile(mobile);
            // activityWinRecord.setOwnMobile(mobile);
            activityWinRecord.setReason("企业产品不存在");
            activityWinRecord.setWinTime(new Date());
            // 更新记录状态，直接废弃该二维码
            activityWinRecordService.updateByPrimaryKey(activityWinRecord);
            return false;
        }

        // 校验企业余额
        if (!accountService.isEnoughInAccount(productId, activities.getEntId())) {
            LOGGER.error("企业余额不足. entId = {}, productId = {}.", activities.getEntId(), productId);
            // activityWinRecord.setStatus(ChargeRecordStatus.INVALIDED.getCode());
            activityWinRecord.setChargeMobile(mobile);
            // activityWinRecord.setOwnMobile(mobile);
            activityWinRecord.setReason("企业余额不足");
            activityWinRecord.setWinTime(new Date());
            // 更新记录状态，直接废弃该二维码
            activityWinRecordService.updateByPrimaryKey(activityWinRecord);
            return false;
        }

        // 流控异常
        Product product = productService.selectProductById(productId);
        Integer price = product.getPrice() * entProduct.getDiscount() / 100;
        if (!entFlowControlService.isFlowControl((double) price, activities.getEntId(), false)) {
            LOGGER.error("流控异常. entId = {}, productId = {}.", activities.getEntId(), productId);
            // activityWinRecord.setStatus(ChargeRecordStatus.INVALIDED.getCode());
            activityWinRecord.setChargeMobile(mobile);
            // activityWinRecord.setOwnMobile(mobile);
            activityWinRecord.setReason("充值失败");
            activityWinRecord.setWinTime(new Date());
            // 更新记录状态，直接废弃该二维码
            activityWinRecordService.updateByPrimaryKey(activityWinRecord);
            return false;
        }

        // 先扣钱
        if (!accountService.minusCount(activities.getEntId(), activityWinRecord.getPrizeId(), AccountType.ENTERPRISE,
                1.0, activityWinRecord.getRecordId(), ActivityType.QRCODE.getname())) {
            LOGGER.error("扣钱失败, recordId={}.", activityWinRecord.getRecordId());
            // activityWinRecord.setStatus(ChargeRecordStatus.INVALIDED.getCode());
            activityWinRecord.setChargeMobile(mobile);
            // activityWinRecord.setOwnMobile(mobile);
            activityWinRecord.setReason("企业扣款失败");
            activityWinRecord.setWinTime(new Date());
            // 更新记录状态，直接废弃该二维码
            activityWinRecordService.updateByPrimaryKey(activityWinRecord);
            return false;
        }

        // 校验通过了,更新状态并开始充值
        activityWinRecord.setStatus(ActivityWinRecordStatus.WAIT.getCode());
        activityWinRecord.setChargeMobile(mobile);
        activityWinRecord.setOwnMobile(mobile);
        activityWinRecord.setWinTime(new Date());
        activityWinRecord.setChargeTime(new Date());

        if (!activityWinRecordService.updateByPrimaryKey(activityWinRecord)) {
            LOGGER.error("更新活动记录状态时出错. ActivityWinRecordId = {}.", activityWinRecordId);
            if (!accountService.returnFunds(activityWinRecordId, ActivityType.QRCODE, activityWinRecord.getPrizeId(),
                    1)) {
                LOGGER.error("退款时出错. pltSerailNum = {}.", activityWinRecordId);
                return false;
            }
            return false;
        }

        // 插入流水号记录
        if (!serialNumService.insert(buildSerailNum(activityWinRecordId))) {
            LOGGER.error("插入平台流水号记录出错. pltSerailNum = {}.", activityWinRecordId);
            if (!accountService.returnFunds(activityWinRecordId, ActivityType.QRCODE, activityWinRecord.getPrizeId(),
                    1)) {
                LOGGER.error("退款时出错. pltSerailNum = {}.", activityWinRecordId);
                return false;
            }
            return false;
        }

        // 插入充值记录
        ChargeRecord cr = buildChargeRecord(activityWinRecord, activities);
        if (!chargeRecordService.create(cr)) {
            LOGGER.error("插入充值记录出错. pltSerailNum = {}.", activityWinRecordId);
            if (!accountService.returnFunds(activityWinRecordId, ActivityType.QRCODE, activityWinRecord.getPrizeId(),
                    1)) {
                LOGGER.error("退款时出错. pltSerailNum = {}.", activityWinRecordId);
                return false;
            }
            return false;
        }

        // OK, 现在可以扔给队列去处理了...
        if (!taskProducer.produceActivityWinMsg(buildPojo(activities, activityWinRecordId))) {
            LOGGER.error("生产消息到活动队列中时出错. Activites = {}, RecordId = {}.", new Gson().toJson(activities),
                    activityWinRecordId);
            Integer financeStatus = null;
            Date updateChargeTime = new Date();
            if (!accountService.returnFunds(activityWinRecordId, ActivityType.QRCODE, activityWinRecord.getPrizeId(),
                    1)) {
                LOGGER.error("退款时出错. pltSerailNum = {}.", activityWinRecordId);
            } else {
                financeStatus = FinanceStatus.IN.getCode();
            }
            changeActivityWinStatus(activityWinRecordId, ChargeResult.ChargeMsgCode.refuseToEnterQueue.getCode(),
                    ActivityWinRecordStatus.FALURE.getCode());
            if (!chargeRecordService.updateStatusAndStatusCode(cr.getId(),
                    ChargeResult.ChargeMsgCode.refuseToEnterQueue.getCode(), ChargeRecordStatus.FAILED.getCode(),
                    ChargeRecordStatus.FAILED.getMessage(), financeStatus, updateChargeTime)) {
                LOGGER.error("更新充值记录失败, id = {}.", cr.getId());
            }
            return false;
        } else {
            LOGGER.info("入业务队列成功.");
            if (!activityWinRecordService.updateStatusCodeByRecordId(activityWinRecordId,
                    ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                LOGGER.error("入业务队列成功, 更新活动记录状态码失败");
            }

            if (!chargeRecordService.updateStatusCode(cr.getId(), ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                LOGGER.error("入业务队列成功, 更新充值记录状态码失败");
            }
            LOGGER.error("入业务队列成功, recordId = {}, 状态码 = {}", activityWinRecordId,
                    ChargeResult.ChargeMsgCode.businessQueue.getCode());

            // 哦耶！终于可以返回true了！
            return true;
        }
    }
    /**
     * getEntProduct
     * */
    // 校验企业与产品的关联关系
    private EntProduct getEntProduct(Long prdId, Long entId) {
        if (prdId == null || entId == null) {
            return null;
        }

        Product product = productService.get(prdId);
        if (product == null) {
            return null;
        }

        // 判断是否为虚拟产品
        Long parentPrdId = null;
        if (product.getFlowAccountFlag() != null
                && FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue() == product.getFlowAccountFlag()) {
            parentPrdId = product.getFlowAccountProductId(); // 转化成真实的产品ID
        } else {
            parentPrdId = prdId; // 其它的为真实产品，父产品为它本身
        }

        return entProductService.selectByProductIDAndEnterprizeID(parentPrdId, entId);
    }

    /**
     * 参与流量券活动-流量券充值
     *
     * @Title: participateFlowCard
     * @Author: wujiamin
     * @date 2016年8月25日上午11:57:01
     */
    private boolean participateFlowCard(String chargePhone, Map<Object, Object> extendParams, 
            String channel) {
        String idsStr = (String) extendParams.get("recordIds");
        String ownerPhone = (String) extendParams.get("ownerPhone");
        if (StringUtils.isEmpty(idsStr) || StringUtils.isEmpty(ownerPhone)) {
            LOGGER.error("流量券充值，参数错误，中奖记录id={}，拥有者号码={}", idsStr, ownerPhone);
        }

        String[] recordIds = com.cmcc.vrp.util.StringUtils.split(idsStr, ",");

        // 校验通过的记录
        List<String> successRecordIdList = new ArrayList<String>();

        List<ActivitiesWinPojo> pojos = new ArrayList<ActivitiesWinPojo>();

        for (int i = 0; i < recordIds.length; i++) {
            ActivityWinRecord record = activityWinRecordService.selectByRecordId(recordIds[i]);
            Activities activity = selectByActivityId(record.getActivityId());
            if (activity != null) {
                if (activity.getStatus().intValue() != ActivityStatus.PROCESSING.getCode().intValue()) {
                    LOGGER.error("流量券活动不处于进行中状态，activityId={}, 充值请求传入的拥有者phone={}", activity.getId(), ownerPhone);
                    continue;
                }

                if (!record.getOwnMobile().equals(ownerPhone)) {
                    LOGGER.error("流量券recordId={}的拥有者phone={}，充值请求传入的拥有者phone={}", recordIds[i], record.getOwnMobile(),
                            ownerPhone);
                    continue;
                }

                // 判断企业状态
                Enterprise enter = enterprisesService.selectById(activity.getEntId());
                if (enter == null || !(enter.getStatus().toString().equals("3")
                        && enter.getDeleteFlag().toString().equals("0"))) {
                    LOGGER.error("流量券企业异常, entId = {}， activityId = {}, 充值请求传入的拥有者phone={}", activity.getEntId(),
                            activity.getActivityId(), ownerPhone);
                    record.setStatus(ChargeRecordStatus.FAILED.getCode());
                    record.setReason("企业状态异常");
                    record.setWinTime(new Date());
                    record.setChargeTime(new Date());
                    record.setChargeMobile(chargePhone);
                    // 更新记录状态
                    activityWinRecordService.updateByPrimaryKey(record);
                    continue;
                }

                // 判断企业与产品是否已经取消关联关系
                EntProduct entProduct = getEntProduct(record.getPrizeId(), activity.getEntId());
                if (entProduct == null) {
                    LOGGER.error("流量券企业与产品取消关联关系. entId = {}, productId = {}, 充值请求传入的拥有者phone={}.", activity.getEntId(),
                            record.getPrizeId(), ownerPhone);
                    record.setStatus(ChargeRecordStatus.FAILED.getCode());
                    record.setReason("企业产品不存在");
                    record.setWinTime(new Date());
                    record.setChargeTime(new Date());
                    record.setChargeMobile(chargePhone);
                    // 更新记录状态
                    activityWinRecordService.updateByPrimaryKey(record);
                    continue;
                }

                // 流控异常
                Product product = productService.selectProductById(record.getPrizeId());
                Integer price = product.getPrice() * entProduct.getDiscount() / 100;
                if (!entFlowControlService.isFlowControl((double) price, activity.getEntId(), false)) {
                    LOGGER.error("流控异常. entId = {}, productId = {}.", activity.getEntId(), record.getPrizeId());
                    record.setStatus(ChargeRecordStatus.FAILED.getCode());
                    record.setReason("充值失败");
                    record.setWinTime(new Date());
                    record.setChargeTime(new Date());
                    record.setChargeMobile(chargePhone);
                    // 更新记录状态
                    activityWinRecordService.updateByPrimaryKey(record);
                    continue;
                }

                // 扣款
                if (!accountService.minusCount(activity.getEntId(), record.getPrizeId(), AccountType.ENTERPRISE, 1.0,
                        record.getRecordId(), ActivityType.FLOWCARD.getname())) {
                    LOGGER.error("流量券扣款失败, recordId = {}, 充值请求传入的拥有者phone={}.", record.getRecordId(), ownerPhone);
                    record.setStatus(ChargeRecordStatus.FAILED.getCode());
                    record.setReason("企业扣款失败");
                    record.setWinTime(new Date());
                    record.setChargeTime(new Date());
                    record.setChargeMobile(chargePhone);
                    // 更新记录状态
                    activityWinRecordService.updateByPrimaryKey(record);
                    continue;
                }

                // 插入流水号
                if (!serialNumService.insert(buildSerailNum(record.getRecordId()))) {
                    LOGGER.error("插入流水号失败, pltSerailNum = {}.", record.getRecordId());
                    if (!accountService.returnFunds(record.getRecordId(), ActivityType.FLOWCARD, record.getPrizeId(),
                            1)) {
                        LOGGER.error("退款时出错. pltSerailNum = {}.", record.getRecordId());
                        continue;
                    }
                    continue;
                }

                // 插入chargeRecord
                if (!chargeRecordService.create(buildChargeRecord(record, activity))) {
                    LOGGER.error("插入充值记录失败, pltSerailNum = {}.", record.getRecordId());
                    if (!accountService.returnFunds(record.getRecordId(), ActivityType.FLOWCARD, record.getPrizeId(),
                            1)) {
                        LOGGER.error("退款时出错. pltSerailNum = {}.", record.getRecordId());
                        continue;
                    }
                    continue;
                }

                successRecordIdList.add(recordIds[i]);
                ActivitiesWinPojo pojo = new ActivitiesWinPojo();
                pojo.setActivities(activity);
                pojo.setActivitiesWinRecordId(recordIds[i]);
                pojos.add(pojo);
            }
        }

        if (successRecordIdList.size() == 0) {
            return true;
        }

        // 更新状态
        if (activityWinRecordService.batchUpdateForFlowcard(successRecordIdList, chargePhone,
                ActivityWinRecordStatus.WAIT.getCode(), ActivityWinRecordStatus.UNUSE.getCode(), new Date(), channel)) {
            // 存入队列
            if (taskProducer.produceBatchActivityWinMsg(pojos)) {
                List<String> sns = new ArrayList();
                for (ActivitiesWinPojo pojo : pojos) {
                    sns.add(pojo.getActivitiesWinRecordId());
                }
                LOGGER.info("入业务队列成功.");
                if (!activityWinRecordService.batchUpdateStatusCodeByRecordId(sns,
                        ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                    LOGGER.error("入业务队列成功, 更新活动记录状态码失败");
                }

                if (!chargeRecordService.batchUpdateStatusCode(ChargeResult.ChargeMsgCode.businessQueue.getCode(),
                        sns)) {
                    LOGGER.error("入业务队列成功, 更新充值记录状态码失败");
                }
                LOGGER.error("入业务队列成功, recordId = {}, 状态码 = {}", sns,
                        ChargeResult.ChargeMsgCode.businessQueue.getCode());

                return true;
            } else {
                LOGGER.error("生产消息到活动队列中时出错. pojos={}.", new Gson().toJson(pojos));
                LOGGER.info("开始退款");
                for (ActivitiesWinPojo pojo : pojos) {
                    Date updateChargeTime = new Date();
                    Integer financeStatus = null;
                    ActivityWinRecord awr = activityWinRecordService.selectByRecordId(pojo.getActivitiesWinRecordId());
                    ChargeRecord cr = chargeRecordService.getRecordBySN(pojo.getActivitiesWinRecordId());
                    if (!accountService.returnFunds(pojo.getActivitiesWinRecordId(), ActivityType.FLOWCARD,
                            awr.getPrizeId(), 1)) {
                        LOGGER.error("退款时出错. pltSerailNum = {}.", pojo.getActivitiesWinRecordId());
                    } else {
                        financeStatus = FinanceStatus.IN.getCode();
                    }

                    if (!changeActivityWinStatus(pojo.getActivitiesWinRecordId(),
                            ChargeResult.ChargeMsgCode.refuseToEnterQueue.getCode(),
                            ActivityWinRecordStatus.FALURE.getCode())) {
                        LOGGER.error("更新活动记录出错. pltSerailNum = {}.", pojo.getActivitiesWinRecordId());
                    }
                    if (!chargeRecordService.updateStatusAndStatusCode(cr.getId(),
                            ChargeResult.ChargeMsgCode.refuseToEnterQueue.getCode(),
                            ChargeRecordStatus.FAILED.getCode(), ChargeRecordStatus.FAILED.getMessage(), financeStatus,
                            updateChargeTime)) {
                        LOGGER.error("更新充值记录失败, id = {}.", cr.getId());
                    }
                }
                return false;
            }
        }

        return false;
    }

    /**
     * buildPojo
     * */
    private ActivitiesWinPojo buildPojo(Activities activities, String activityWinRecordId) {
        ActivitiesWinPojo pojo = new ActivitiesWinPojo();
        pojo.setActivities(activities);
        pojo.setActivitiesWinRecordId(activityWinRecordId);

        return pojo;
    }

    /**
     * validate
     * */
    // 校验活动是否开始，是否结束等等信息
    private boolean validate(Activities activities) {
        return activities != null && activities.getStatus().equals(ActivityStatus.PROCESSING.getCode()) // 正在进行中
                && new DateTime(activities.getStartTime()).isBeforeNow() // 已经开始
                && new DateTime(activities.getEndTime()).isAfterNow() // 还没结束
                && activities.getDeleteFlag() == Constants.DELETE_FLAG.UNDELETED.getValue(); // 还没删除
    }

    /**
     * initActivity
     * */
    private void initActivity(Activities activities) {
        activities.setActivityId(SerialNumGenerator.buildSerialNum());
        if (activities.getType().toString().equals(ActivityType.COMMON_REDPACKET.getCode().toString())
                || activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
            // 普通红包，拼手气红包
            activities.setStatus(ActivityStatus.ON.getCode());
        } else {
            activities.setStatus(ActivityStatus.SAVED.getCode());

        }
        activities.setDeleteFlag(0);
        activities.setCreateTime(new Date());
        activities.setUpdateTime(new Date());
    }

    /**
     * editFlowcardActivity
     * */
    @Override
    @Transactional
    public boolean editFlowcardActivity(Activities activities, Long cmProductId, Long cuProductId, Long ctProductId,
            String cmMobileList, String cuMobileList, String ctMobileList, String cmUserSet, String cuUserSet,
            String ctUserSet) {
        // 1、更新活动记录
        if (!updateByPrimaryKeySelective(activities)) {
            LOGGER.info("更新活动信息失败:" + JSONArray.toJSONString(activities));
            return false;
        }

        // 三大运营商手机号
        Long cmMobileCnt = 0L;
        Long cuMobileCnt = 0L;
        Long ctMobileCnt = 0L;
        // 三大运营商用户
        Long cmUserCnt = 0L;
        Long cuUserCnt = 0L;
        Long ctUserCnt = 0L;

        if (StringUtils.isEmpty(cmMobileList) && StringUtils.isEmpty(cuMobileList)
                && StringUtils.isEmpty(ctMobileList)) {
            cmMobileCnt = getMobileCnt(activities.getActivityId(), IspType.CMCC.getValue());
            cmUserCnt = getUserCnt(activities.getActivityId(), IspType.CMCC.getValue());

            cuMobileCnt = getMobileCnt(activities.getActivityId(), IspType.UNICOM.getValue());
            cuUserCnt = getUserCnt(activities.getActivityId(), IspType.UNICOM.getValue());

            ctMobileCnt = getMobileCnt(activities.getActivityId(), IspType.TELECOM.getValue());
            ctUserCnt = getUserCnt(activities.getActivityId(), IspType.TELECOM.getValue());
        } else {
            if (!StringUtils.isEmpty(cmMobileList)) {
                cmMobileCnt = (long) cmMobileList.split(",").length;
                cmUserCnt = (long) cmUserSet.split(",").length;
            }
            if (!StringUtils.isEmpty(cuMobileList)) {
                cuMobileCnt = (long) cuMobileList.split(",").length;
                cuUserCnt = (long) cuUserSet.split(",").length;
            }
            if (!StringUtils.isEmpty(ctMobileList)) {
                ctMobileCnt = (long) ctMobileList.split(",").length;
                ctUserCnt = (long) ctUserSet.split(",").length;
            }
        }

        // 2更新活动奖品信息
        List<Long> deleteProducts = new ArrayList<Long>();
        List<Long> addProducts = new ArrayList<Long>();
        List<Long> updateProducts = new ArrayList<Long>();
        updateProducts(deleteProducts, addProducts, updateProducts, cmProductId, cuProductId, ctProductId,
                activities.getActivityId());
        if ((deleteProducts != null && deleteProducts.size() > 0) || (addProducts != null && addProducts.size() > 0)
                || (updateProducts != null && updateProducts.size() > 0)) {
            if (!(activityPrizeService.deleteActivityPrize(deleteProducts, activities.getActivityId())
                    && activityPrizeService.addActivityPrize(addProducts, activities.getActivityId(), cmMobileCnt,
                            cuMobileCnt, ctMobileCnt)
                    && activityPrizeService.updateActivityPrize(updateProducts, activities.getActivityId(), cmMobileCnt,
                            cuMobileCnt, ctMobileCnt))) {
                LOGGER.info("更新活动产品信息失败，需要删除产品：" + JSONArray.toJSONString(deleteProducts) + " ;需要增加:"
                        + JSONArray.toJSONString(addProducts) + " ;需要更新:" + JSONArray.toJSONString(updateProducts));
                throw new RuntimeException();
            }
        }

        // 3、更新赠送手机号
        if (!StringUtils.isEmpty(cmMobileList) || !StringUtils.isEmpty(cuMobileList)
                || !StringUtils.isEmpty(ctMobileList)) {
            if (!(activityWinRecordService.deleteByActivityId(activities.getActivityId())
                    && activityWinRecordService.batchInsertForFlowcard(activities.getActivityId(), cmProductId,
                            cuProductId, ctProductId, cmMobileList, cuMobileList, ctMobileList))) {
                LOGGER.info("更新赠送手机号失败");
                throw new RuntimeException();
            }
        }

        // 4、更新活动详细信息
        if (!(activityInfoService.updateActivityInfo(activities.getActivityId(), cmProductId, cuProductId, ctProductId,
                cmMobileCnt, cuMobileCnt, ctMobileCnt, cmUserCnt, cuUserCnt, ctUserCnt))) {
            LOGGER.info("更新纪录详情失败失败");
            throw new RuntimeException();
        }
        return true;
    }

    /**
     * editQRcodeActivity
     * */
    @Override
    @Transactional
    public boolean editQRcodeActivity(Activities activities, ActivityInfo activityInfo, Long cmProductId,
            Long cuProductId, Long ctProductId, String correctMobiles) {

        if (activities == null || StringUtils.isEmpty(activities.getActivityId())) {
            LOGGER.info("更新活动信息失败:" + JSONArray.toJSONString(activities));
            return false;
        }

        ActivityInfo oldActivityInfo = activityInfoService.selectByActivityId(activities.getActivityId());

        // 1、更新活动记录
        if (!updateByPrimaryKeySelective(activities)) {
            LOGGER.info("更新活动信息失败:" + JSONArray.toJSONString(activities));
            return false;
        }

        // 2、更新活动信息
        List<Long> deleteProducts = new ArrayList<Long>();
        List<Long> addProducts = new ArrayList<Long>();
        List<Long> updateProducts = new ArrayList<Long>();
        updateProducts(deleteProducts, addProducts, updateProducts, cmProductId, cuProductId, ctProductId,
                activities.getActivityId());
        if ((deleteProducts != null && deleteProducts.size() > 0) || (addProducts != null && addProducts.size() > 0)) {
            if (!(activityPrizeService.deleteActivityPrize(deleteProducts, activities.getActivityId())
                    && activityPrizeService.addActivityPrize(addProducts, activities.getActivityId(), null, null,
                            null))) {
                LOGGER.info("更新活动产品信息失败，需要删除产品：" + JSONArray.toJSONString(deleteProducts) + " ;需要增加:"
                        + JSONArray.toJSONString(addProducts));
                throw new RuntimeException();
            }
        }

        // 3、跟新活动规模信息
        if (!activityInfoService.updateActivityInfoForQrcode(activities, activityInfo, cmProductId, cuProductId,
                ctProductId)) {
            LOGGER.info("更新活动规模信息失败");
            throw new RuntimeException();
        }

        // 更新黑白名单
        if (activityInfo.getHasWhiteOrBlack().intValue() == BlackAndWhiteListType.NOLIST.getCode().intValue()) {
            // 无黑白名单，将原名单删除
            if (!activityBlackAndWhiteService.deleteByActivityId(activities.getActivityId())) {
                LOGGER.info("删除黑白名单失败: " + JSONArray.toJSONString(activities));
                throw new RuntimeException();
            }
        } else {
            if (!StringUtils.isEmpty(correctMobiles)) {
                // 重新上传名单，则更新黑白名单
                if (!(activityBlackAndWhiteService.deleteByActivityId(activities.getActivityId())
                        && activityBlackAndWhiteService.batchInsert(activities.getActivityId(),
                                activityInfo.getHasWhiteOrBlack(), correctMobiles))) {
                    LOGGER.info("更新黑白名单失败: " + JSONArray.toJSONString(activities));
                    throw new RuntimeException();
                }
            } else {
                // 更改名单属性
                if (oldActivityInfo.getHasWhiteOrBlack().intValue() != activityInfo.getHasWhiteOrBlack().intValue()) {
                    if (!activityBlackAndWhiteService.updateIsWhiteByActivityId(activities.getActivityId(),
                            activityInfo.getHasWhiteOrBlack())) {
                        LOGGER.info("更新黑白名单失败: " + JSONArray.toJSONString(activities));
                        throw new RuntimeException();
                    }
                }
            }
        }
        return true;
    }

    /**
     * getUserCnt
     * */
    private Long getUserCnt(String activityId, String isp) {
        List<ActivityWinRecord> records = activityWinRecordService.selectByActivityIdAndIsp(activityId, isp);
        Set<ActivityWinRecord> recordSet = new HashSet<ActivityWinRecord>();
        for (ActivityWinRecord item : records) {
            recordSet.add(item);
        }
        return (long) recordSet.size();
    }

    /**
     * getMobileCnt
     * */
    private Long getMobileCnt(String activityId, String isp) {
        List<ActivityWinRecord> records = activityWinRecordService.selectByActivityIdAndIsp(activityId, isp);
        if (records != null && records.size() > 0) {
            return (long) records.size();
        }
        return 0L;
    }

    /**
     * updateProducts
     * */
    private void updateProducts(List<Long> deleteProducts, List<Long> addProducts, List<Long> updateProducts,
            Long cmProductId, Long cuProductId, Long ctProductId, String activityId) {
        List<Long> prodIds = new ArrayList<Long>();
        if (cmProductId != null) {
            addProducts.add(cmProductId);
            prodIds.add(cmProductId);
        }
        if (cuProductId != null) {
            addProducts.add(cuProductId);
            prodIds.add(cuProductId);
        }
        if (ctProductId != null) {
            addProducts.add(ctProductId);
            prodIds.add(ctProductId);
        }

        List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityId(activityId);
        for (ActivityPrize item : activityPrizes) {
            deleteProducts.add(item.getProductId());
            updateProducts.add(item.getProductId());
        }

        Iterator<Long> delIterator = deleteProducts.iterator();
        Iterator<Long> addIterator = addProducts.iterator();
        Iterator<Long> updateIterator = updateProducts.iterator();

        // 获取删除的产品
        while (delIterator.hasNext()) {
            Long delProdId = delIterator.next();
            for (Long prodId : prodIds) {
                if (prodId.toString().equals(delProdId.toString())) {
                    delIterator.remove();
                }
            }
        }
        // 获取增加的产品
        while (addIterator.hasNext()) {
            Long addProdId = addIterator.next();
            for (ActivityPrize item : activityPrizes) {
                if (addProdId.toString().equals(item.getProductId().toString())) {
                    addIterator.remove();
                }
            }
        }
        // 获取要更新的产品
        while (updateIterator.hasNext()) {
            Long updateProdId = updateIterator.next();
            for (Long delId : deleteProducts) {
                if (delId.toString().equals(updateProdId.toString())) {
                    updateIterator.remove();
                }
            }
        }
    }

    /**
     * changeStatus
     * */
    @Override
    public boolean changeStatus(String activityId, Integer status) {
        if (StringUtils.isEmpty(activityId) || status == null) {
            return false;
        }
        return activitiesMapper.changeStatus(activityId, status) == 1;
    }

    /**
     * batchChangeStatus
     * */
    @Override
    public boolean batchChangeStatus(List<Activities> activities, Integer status) {
        if (activities == null || status == null) {
            return false;
        }
        if (activities.size() == 0) {
            return true;
        }
        Map map = new HashMap();
        map.put("activities", activities);
        map.put("status", status);
        return activitiesMapper.batchChangeStatus(map) > 0;
    }

    /**
     * 批量下架活动
     *
     * @Title: batchDownShelf
     */
    @Override
    public void batchDownShelf(List<Activities> activities) {
        if (activities == null || activities.size() <= 0) {
            return;
        }

        for (Activities a : activities) {
            ActivityInfo activityInfo = activityInfoService.selectByActivityId(a.getActivityId());
            // 1、向营销模板服务发送下架请求
            if ((a.getStatus().equals(ActivityStatus.ON.getCode())
                    || a.getStatus().equals(ActivityStatus.PROCESSING.getCode())) && activityInfo != null
                    && !activityTemplateService.notifyTemplateToClose(activityInfo.getUrl(), a.getActivityId())) {
                LOGGER.info("像营销模板服务请求下架失败，活动ID-{}", a.getActivityId());
            }
        }
    }

    /**
     * createActivityStartSchedule
     * */
    @Override
    public boolean createActivityStartSchedule(Activities activities) {
        ActivityJobPojo pojo = new ActivityJobPojo(activities.getActivityId());
        String jsonStr = JSON.toJSONString(pojo);
        // 创建流量券活动开始任务
        String msg = "";
        if (activities.getType().toString().equals(ActivityType.FLOWCARD.getCode().toString())) {
            // 流量券
            msg = scheduleService.createScheduleJob(ActivityStartJob.class, SchedulerType.FLOWCARD_START.getCode(),
                    jsonStr, activities.getActivityId().toString(), activities.getStartTime());
        }
        if (activities.getType().toString().equals(ActivityType.QRCODE.getCode().toString())) {
            // 二维码
            msg = scheduleService.createScheduleJob(ActivityStartJob.class, SchedulerType.QRCODE_START.getCode(),
                    jsonStr, activities.getActivityId().toString(), activities.getStartTime());
        }
        if (activities.getType().toString().equals(ActivityType.CROWD_FUNDING.getCode().toString())) {
            // 流量众筹
            msg = scheduleService.createScheduleJob(ActivityStartJob.class, SchedulerType.CROWDFUNDING_START.getCode(),
                    jsonStr, activities.getActivityId().toString(), activities.getStartTime());
        }

        if (activities.getType().toString().equals(ActivityType.COMMON_REDPACKET.getCode().toString())) {
            // 普通个人红包
            msg = scheduleService.createScheduleJob(ActivityStartJob.class,
                    SchedulerType.COMMON_REDPACKET_START.getCode(), jsonStr, activities.getActivityId().toString(),
                    activities.getStartTime());
        }
        if (activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
            // 个人拼手气红包
            msg = scheduleService.createScheduleJob(ActivityStartJob.class,
                    SchedulerType.LUCKY_REDPACKET_START.getCode(), jsonStr, activities.getActivityId().toString(),
                    activities.getStartTime());
        }

        if ("success".equals(msg)) {
            return true;
        }
        return false;
    }

    /**
     * createActivityEndSchedule
     * */
    @Override
    public boolean createActivityEndSchedule(Activities activities) {
        ActivityJobPojo pojo = new ActivityJobPojo(activities.getActivityId());
        String jsonStr = JSON.toJSONString(pojo);
        // 创建流量券活动结束任务
        String msg = "";
        if (activities.getType().toString().equals(ActivityType.FLOWCARD.getCode().toString())) {
            // 流量券
            msg = scheduleService.createScheduleJob(ActivityEndJob.class, SchedulerType.FLOWCARD_END.getCode(), jsonStr,
                    activities.getActivityId().toString(), activities.getEndTime());
        }
        if (activities.getType().toString().equals(ActivityType.QRCODE.getCode().toString())) {
            // 二维码
            msg = scheduleService.createScheduleJob(ActivityEndJob.class, SchedulerType.QRCODE_END.getCode(), jsonStr,
                    activities.getActivityId().toString(), activities.getEndTime());
        }
        if (activities.getType().toString().equals(ActivityType.CROWD_FUNDING.getCode().toString())) {
            // 流量众筹
            msg = scheduleService.createScheduleJob(ActivityEndJob.class, SchedulerType.CROWDFUNDING_END.getCode(),
                    jsonStr, activities.getActivityId().toString(), activities.getEndTime());
        }

        if (activities.getType().toString().equals(ActivityType.COMMON_REDPACKET.getCode().toString())) {
            // 个人普通红包
            msg = scheduleService.createScheduleJob(ActivityEndJob.class, SchedulerType.COMMON_REDPACKET_END.getCode(),
                    jsonStr, activities.getActivityId().toString(), activities.getEndTime());
        }
        if (activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
            // 拼手气红包
            msg = scheduleService.createScheduleJob(ActivityEndJob.class, SchedulerType.LUCKY_REDPACKET_END.getCode(),
                    jsonStr, activities.getActivityId().toString(), activities.getEndTime());
        }
        if ("success".equals(msg)) {
            return true;
        }
        return false;
    }

    /**
     * notifyUserForFlowcard
     * */
    @Override
    public boolean notifyUserForFlowcard(Activities activities) {
        if (activities == null || org.apache.commons.lang.StringUtils.isBlank(activities.getActivityId())) {
            return false;
        }
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(activities.getEntId());

        List<ActivityWinRecord> activityWinRecords = activityWinRecordService.selectByActivityId(activities
                .getActivityId());
        String key = getActivityURLKey();
        String chargeURL = getFlowcardURL();

        if (activityWinRecords != null && activityWinRecords.size() > 0) {
            for (ActivityWinRecord item : activityWinRecords) {
                byte[] encryptResult = AES.encrypt(item.getOwnMobile().getBytes(), key.getBytes());
                // 发送短信
                Product product = productService.selectProductById(item.getPrizeId());
                if (product.getType() == 3) {
                    String content = "";
                    
                    if("shanghai".equals(globalConfigService.get("PROVINCE_FLAG"))){

                        
                        //改成采用短信模板
                        EnterpriseSmsTemplate smsmTemplate = enterpriseSmsTemplateService
                                .getChoosedSmsTemplate(enterprise.getId());
                        
                        if (smsmTemplate != null) {
                            String template = smsmTemplate.getContent();
                            content = MessageFormat.format(template,enterprise.getEntName(),activities.getName(),
                                    sizeFun(product.getProductSize()));
                            LOGGER.info("短信内容：content = {}", content);
                        
                        }else{
                            LOGGER.info("企业未有关联短信模板. enterId = {}", enterprise.getId());
                        }
                        
                        /*content = "您好!"+ enterprise.getName()
                        +"向您赠送"+activities.getName()+"流量券"+ sizeFun(product.getProductSize())
                        +",请到上海移动政企微厅微信公众号或和教授app http://dx.10086.cn/profZQ 查收,谢谢!";*/


                    }else{
                        String uuid = UUID8.generateShortUuid();
                        String virtualUrl = getFlowcardVirtaulURL() + "/" + uuid + ".html";
                        content = "您获赠" + sizeFun(product.getProductSize()) + "流量券," + virtualUrl;
                        String realUrl = chargeURL + DatatypeConverter.printHexBinary(encryptResult) + ".html";
                        if (!createUrlMap(realUrl, uuid)) {
                            LOGGER.info("创建UrlMap失败");
                        }
                    }
                    
                    if (!sendMsgService.sendMessage(item.getOwnMobile(), content,
                            MessageType.FLOWCARD_NOTICE.getCode())) {
                        LOGGER.info("流量券发送短信失败,活动ID-" + activities.getActivityId() + " ;手机号-" + item.getOwnMobile());
                    }
                } else {
                    
                    String content = "";
                    if("shanghai".equals(globalConfigService.get("PROVINCE_FLAG"))){

                        
                        //改成采用短信模板
                        EnterpriseSmsTemplate smsmTemplate = enterpriseSmsTemplateService
                                .getChoosedSmsTemplate(enterprise.getId());
                        
                        if (smsmTemplate != null) {
                            String template = smsmTemplate.getContent();
                            content = MessageFormat.format(template,enterprise.getEntName(),activities.getName(),
                                    sizeFun(product.getProductSize()));
                            LOGGER.info("短信内容：content = {}", content);
                        
                        }else{
                            LOGGER.info("企业未有关联短信模板. enterId = {}", enterprise.getId());
                        }
                        
                        /*content = "您好!"+ enterprise.getName()
                        +"向您赠送"+activities.getName()+"流量券"+ sizeFun(product.getProductSize())
                        +",请到上海移动政企微厅微信公众号或和教授app http://dx.10086.cn/profZQ 查收,谢谢!";*/

                    }else{
                        content = "尊敬的用户您好!" + enterprise.getName() + "向您赠送" + activities.getName() + "流量券"
                                + sizeFun(product.getProductSize()) + ",请登录网站 " + chargeURL

                                + DatatypeConverter.printHexBinary(encryptResult) + ".html 查收,谢谢!";
                    }
                    
                    if (!sendMsgService.sendMessage(item.getOwnMobile(), content, MessageType.FLOWCARD_NOTICE.getCode())) {
                        LOGGER.info("流量券发送短信失败,活动ID-" + activities.getActivityId() + " ;手机号-" + item.getOwnMobile());
                    }
                }
            }
        } else {
            LOGGER.info("流量券活动ID-" + activities.getActivityId() + "无充值手机号");
        }
        return true;
    }

    /**
     * 1、缓存uuid与realUrl,有效期为一周 2、存入数据进行数据的持久化
     */
    private boolean createUrlMap(String realUrl, String uuid) {
        UrlMap urlMap = new UrlMap();
        urlMap.setCreateTime(new Date());
        urlMap.setRealUrl(realUrl);
        urlMap.setType(0);
        urlMap.setUuid(uuid);
        urlMap.setUpdateTime(new Date());
        return redisUtilService.set(uuid, realUrl, 10080).equals("success") && urlMapService.createUrlMap(urlMap);
    }

    /**
     * sizeFun
     * */
    private String sizeFun(Long size) {
        if (size == null) {
            return "-";
        }

        if (size < 1024) {
            return size + "KB";
        } else if (size >= 1024 && size < 1024 * 1024) {
            return (size * 1.0 / 1024) + "MB";
        } else {
            return (size * 1.0 / 1024 / 1024) + "GB";
        }
    }

    /**
     * 获取流量券充值真实url
     */
    public String getFlowcardURL() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOTTERY_FLOWCARD_URL.getKey());
    }

    /**
     * 获取流量券充值虚拟url
     */
    public String getFlowcardVirtaulURL() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOTTERY_FLOWCARD_VIRTUAL_URL.getKey());
    }

    /**
     * 获取加密秘钥
     */
    public String getActivityURLKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_URL_KEY.getKey());
    }

    /**
     * checkAccountEnough
     * */
    @Override
    public boolean checkAccountEnough(Activities activities) {
        if (activities == null || StringUtils.isEmpty(activities.getActivityId())) {
            LOGGER.error("余额检查时，活动信息异常!");
            return false;
        }
        List<ActivityPrize> activityPrizes = activityPrizeService.getDetailByActivityId(activities.getActivityId());

        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activities.getActivityId());
        if (activityPrizes == null || activityInfo == null) {
            LOGGER.error("余额检查时，奖品信息为空或奖品详情为空!");
            return false;
        }

        // else {
        // List<AccountPrizeMap> list = new ArrayList<AccountPrizeMap>();
        // // 二维码的余额校验不同，取奖品中价格最低的奖品
        // if
        // (ActivityType.fromValue(activities.getType()).getCode().equals(ActivityType.QRCODE.getCode()))
        // {
        // Long minPrice = 0L;
        // ActivityPrize minPrize = null;
        // for (ActivityPrize activityPrize : activityPrizes) {
        // if (activityPrize.getPrice() >= minPrice) {
        // minPrice = activityPrize.getPrice();
        // minPrize = activityPrize;
        // }
        // }
        // Double accountCount = 0d;
        // Account account = accountService.get(activities.getEntId(),
        // minPrize.getProductId(),
        // AccountType.ENTERPRISE.getValue());
        // if (account != null) {
        // accountCount = account.getCount();
        // }
        // AccountPrizeMap map = new AccountPrizeMap();
        // map.setAccountCount(accountCount);
        // map.setPrizeCount(activityInfo.getUserCount());
        // map.setPrdId(minPrize.getProductId());
        // list.add(map);
        // } else {
        // for (ActivityPrize activityPrize : activityPrizes) {
        // boolean tag = true;
        // // 如果是重复的奖品，则将奖项数累加
        // for (AccountPrizeMap apm : list) {
        // if (apm.getPrdId().equals(activityPrize.getProductId())) {
        // apm.setPrizeCount(apm.getPrizeCount() + activityPrize.getCount());
        // tag = false;
        // break;
        // }
        // }
        // // 找到该企业该产品账户的余额
        // Long productId = activityPrize.getProductId();
        // Product product = productService.get(activityPrize.getProductId());
        // //如果是衍生类产品，则获取企业流量池产品,校验企业原生流量池产品账户
        // if (product != null
        // && product.getFlowAccountFlag() != null
        // && FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue() ==
        // product.getFlowAccountFlag()
        // .intValue()) {
        // productId = product.getFlowAccountProductId();
        // }
        //
        // Account account = accountService.get(activities.getEntId(),
        // productId,
        // AccountType.ENTERPRISE.getValue());
        // Double accountCount = 0d;
        // if (account != null) {
        // accountCount = account.getCount();
        // }
        // if (tag) {
        // AccountPrizeMap map = new AccountPrizeMap();
        // map.setAccountCount(accountCount);
        // map.setPrizeCount(activityPrize.getCount());
        // map.setPrdId(productId);
        // list.add(map);
        // }
        // }
        // }
        // accountService.isDebt2Account，负债则返回true
        // if (!accountService.isDebt2Account(list, activities.getEntId())) {
        // return true;
        // }
        List<Product> srcProducts1 = new ArrayList<Product>();
        List<Product> srcProducts2 = new ArrayList<Product>();
        List<Product> srcProducts3 = new ArrayList<Product>();
        List<Product> srcProducts4 = new ArrayList<Product>();

        // 考虑最糟糕情况
        for (ActivityPrize activityPrize : activityPrizes) {// 几等奖
            Product product = productService.get(activityPrize.getProductId());
            if (product != null) {
                if (ProductType.FLOW_ACCOUNT.getValue() == product.getType().byteValue()) {
                    for (int i = 0; i < activityInfo.getPrizeCount().intValue(); i++) {// 每个奖品的数量
                        srcProducts1.add(product);
                    }
                } else if (ProductType.FLOW_PACKAGE.getValue() == product.getType().byteValue()) {
                    for (int i = 0; i < activityInfo.getPrizeCount().intValue(); i++) {// 每个奖品的数量
                        srcProducts2.add(product);
                    }
                } else if (ProductType.MOBILE_FEE.getValue() == product.getType().byteValue()) {
                    for (int i = 0; i < activityInfo.getPrizeCount().intValue(); i++) {// 每个奖品的数量
                        srcProducts3.add(product);
                    }
                } else if(ProductType.PRE_PAY_PRODUCT.getValue() == product.getType().byteValue()) {
                    for (int i = 0; i < activityInfo.getPrizeCount().intValue(); i++) {// 每个奖品的数量
                        srcProducts4.add(product);
                    }
                }
            }
        }
        return accountService.isEnoughInAccount(srcProducts1, activities.getEntId())
                && accountService.isEnoughInAccount(srcProducts2, activities.getEntId())
                && accountService.isEnoughInAccount(srcProducts3, activities.getEntId())
                && accountService.isEnoughInAccount(srcProducts4, activities.getEntId());
        // }
        // return false;
    }

    /**
     * 检验活动奖品是否有效
     *
     * @Title: checkPrizeAvailable
     * @Author: wujiamin
     * @date 2016年9月18日下午5:43:19
     */
    @Override
    public String checkPrizeAvailable(Activities activities) {
        if (activities == null || StringUtils.isEmpty(activities.getActivityId())) {
            LOGGER.error("参数为空!");
            return "参数为空";
        }
        List<ActivityPrize> activityPrizes = activityPrizeService.getDetailByActivityId(activities.getActivityId());
        // ActivityInfo activityInfo =
        // activityInfoService.selectByActivityId(activities.getActivityId());
        // || activityInfo == null
        if (activityPrizes == null) {
            LOGGER.error("余额检查时，奖品信息为空或奖品详情为空!");
            return "奖品信息为空";
        } else {
            for (ActivityPrize activityPrize : activityPrizes) {
                Product product = productService.selectProductById(activityPrize.getProductId());
                if (product == null) {
                    LOGGER.error("产品不存在，id = " + activityPrize.getProductId());
                    return "含有不存在的产品";
                }
                // 虚拟产品关联其父产品
                if (product.getFlowAccountFlag().equals(FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode())) {
                    // 校验虚拟产品是否上下架
                    if (!product.getStatus().equals(ProductStatus.NORMAL.getCode())) {
                        LOGGER.error("虚拟产品已下架，id = " + activityPrize.getProductId());
                        return "含有已下架的产品";
                    }

                    // 父产品
                    product = productService.selectProductById(product.getFlowAccountProductId());

                }

                if (product == null) {
                    LOGGER.error("父产品不存在，id = " + activityPrize.getProductId());
                    return "含有不存在的产品";
                }

                // 校验真实产品是否上下架
                if (!product.getStatus().equals(ProductStatus.NORMAL.getCode())) {
                    LOGGER.error("父产品已下架，id = " + product.getFlowAccountProductId());
                    return "含有已下架的产品";
                }

                // 校验真实产品与企业的关联关系
                EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(product.getId(),
                        activities.getEntId());
                if (entProduct == null || entProduct.getDeleteFlag().equals(DELETE_FLAG.DELETED.getValue())) {
                    LOGGER.error("产品与企业关联关系不存在，pid = " + product.getFlowAccountProductId() + ", ent_id = "
                            + activities.getEntId());
                    return "含有企业未订购的产品";
                }
            }
        }
        return null;
    }

    /**
     * selectByEntId
     * */
    @Override
    public List<Activities> selectByEntId(Map map) {
        return activitiesMapper.selectByEntId(map);
    }

    /**
     * submitApproval
     * */
    @Override
    public Map<String, Object> submitApproval(String activityId, Long currentId, Long roleId) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        if (StringUtils.isEmpty(activityId)) {
            map.put("submitRes", "fail");
            map.put("errorMsg", "参数异常，查找不到该活动");
        } else {

            Activities activity = selectByActivityId(activityId);
            //先校验
            if(activity != null && activity.getEntId() != null){
                //获取当前用户的manager
                AdminManager adminManager = adminManagerService.selectByAdminId(currentId);
                //检查activity的enterId是否是该账号有权操作的企业
                if(!enterprisesService.isParentManage(activity.getEntId(), adminManager.getManagerId())){
                    LOGGER.info("活动参数中的企业id={}，当前用户managerId={}，不存在上下级管理关系!", activity.getEntId(), adminManager.getManagerId());
                    map.put("submitRes", "fail");
                    map.put("errorMsg", "参数异常，无操作权限");
                    return map;
                }               
                
                // 校验企业状态是否正常
                String entResultMsg = enterprisesService.judgeEnterprise(activity.getEntId());
                if ("success".equals(entResultMsg)) {
                    // 企业状态正常
                    // 校验活动时间
                    if ((new Date()).after(activity.getEndTime())) {
                        map.put("submitRes", "fail");
                        map.put("errorMsg", "活动已过期，请修改活动时间！");
                        return map;
                    }

                    // 检查活动产品是否有效
                    String errMsg = checkPrizeAvailable(activity);
                    if (errMsg != null) {// 校验不通过
                        map.put("submitRes", "fail");
                        map.put("errorMsg", errMsg);
                        return map;
                    }

                    //随机红包特定的余额校验
                    if (activity.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
                        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
                        List<ActivityPrize> prizes = activityPrizeService.selectByActivityId(activityId);
                        if (!accountService.verifyAccountForRendomPacket(activity.getEntId(), activityInfo.getTotalProductSize(), prizes.get(0).getProductId())) {
                            map.put("submitRes", "fail!");
                            map.put("errorMsg", "企业账户余额不足！");
                            return map;
                        }
                    }

                    if(!StringUtils.isEmpty(cacheService.get(activityId))){
                        map.put("submitRes", "fail");
                        map.put("errorMsg", "活动已提交审核");
                    }else{
                        cacheService.setNxAndExpireTime(activityId, "1", -1);

                        if (!(activity.getType().toString().equals(ActivityType.CROWD_FUNDING.getCode().toString()) ||
                            activity.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString()) ||
                            activity.getType().toString().equals(ActivityType.FLOWCARD.getCode().toString()) ||
                            activity.getType().toString().equals(ActivityType.QRCODE.getCode().toString()))) {
                            // 广东流量众筹、二维码、流量券不需要进行余额校验，随机红包不适合这个余额校验
                            if (!checkAccountEnough(activity)) {
                                map.put("submitRes", "fail");
                                map.put("errorMsg", "企业账户余额不足！");
                                cacheService.delete(activityId);
                                return map;
                            }
                        }

                        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                            .selectByType(ApprovalType.Activity_Approval.getCode());
                        if (approvalProcessDefinition.getStage().toString().equals("0")) {
                            // 不需要审核
                            if (changeStatus(activityId, ActivityStatus.SAVED.getCode())) {
                                map.put("submitRes", "success");
                            } else {
                                map.put("submitRes", "fail");
                                map.put("errorMsg", "提交审核失败!");
                            }
                        } else {
                            // 需要审核
                            List<ApprovalDetailDefinition> approvalRecordList = approvalDetailDefinitionService
                                .getByApprovalProcessId(approvalProcessDefinition.getId());
                            Role role = roleService.getRoleById(approvalRecordList.get(0).getRoleId());

                            ApprovalRequest approvalRequest = createApprovalRequest(approvalProcessDefinition.getId(), activity.getEntId(),
                                    currentId);
                            if (approvalRequestService.submitApprovalForActivity(
                                    approvalRequest,
                                createApprovalRecord(null, currentId, role.getName()),
                                createActivityApprovalDetail(activityId, activity.getType(), activity.getName()))) {
                                approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
                                map.put("submitRes", "success");

                            } else {
                                map.put("submitRes", "fail");
                                map.put("errorMsg", "提交审核失败!");
                            }
                        }
                        cacheService.delete(activityId);
                    }

                }else{
                    // 企业状态不正常
                    map.put("submitRes", "fail");
                    map.put("errorMsg", entResultMsg);
                }
            }else{
                map.put("submitRes", "fail");
                map.put("errorMsg", "参数异常，查找不到该活动");
            }
        }
        return map;
    }

    /**
     * createApprovalRequest
     * */
    private ApprovalRequest createApprovalRequest(Long processId, Long entId, Long currUserId) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setProcessId(processId);
        approvalRequest.setEntId(entId);
        approvalRequest.setCreatorId(currUserId);
        approvalRequest.setStatus(0);
        approvalRequest.setCreateTime(new Date());
        approvalRequest.setUpdateTime(new Date());
        approvalRequest.setDeleteFlag(0);
        approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
        return approvalRequest;
    }

    /**
     * 构造审批记录
     * */
    private ApprovalRecord createApprovalRecord(Long requestId, Long currUserId, String roleName) {
        ApprovalRecord approvalRecord = new ApprovalRecord();
        approvalRecord.setRequestId(requestId);
        approvalRecord.setCreatorId(currUserId);
        approvalRecord.setDescription("待" + roleName + "审核");
        approvalRecord.setIsNew(1);
        approvalRecord.setDeleteFlag(0);
        approvalRecord.setCreateTime(new Date());
        approvalRecord.setOperatorId(null);
        approvalRecord.setComment(null);
        approvalRecord.setUpdateTime(null);
        return approvalRecord;
    }

    /**
     * 构造审批的活动详情
     * */
    private ActivityApprovalDetail createActivityApprovalDetail(String activityId, Integer activityType,
            String activityName) {
        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setActivityId(activityId);
        activityApprovalDetail.setActivityType(activityType);
        activityApprovalDetail.setActivityName(activityName);
        activityApprovalDetail.setDeleteFlag(0);
        return activityApprovalDetail;
    }

    /**
     * 检查账户是否足够支付砸金蛋活动
     */
    /*boolean checkEnoughAccountForActivity(String activityId, Long entId) {
        if (!StringUtils.isEmpty(activityId) && entId != null) {
            Double totalAccount = getEnterpriseAccount(entId);
            Double cost = caculateCostForActivity(activityId, entId);
            return totalAccount.longValue() >= cost.longValue() ? true : false;
        }
        return false;
    }
*/
    /**
     * 获取企业账户余额（余额+信用额度）
     */
    /*private Double getEnterpriseAccount(Long entId) {
        Double totalAccount = 0D;
        if (entId != null) {
            Product product = productService.getCurrencyProduct();
            Account account = accountService.get(entId, product.getId(), AccountType.ENTERPRISE.getValue());
            if (account != null && account.getCount() != null) {
                totalAccount = account.getCount() / 100.0;
                if (account.getMinCount() != null) {
                    totalAccount += account.getMinCount() / (-100.0);
                }
            }
        }
        return totalAccount;
    }
*/
    /**
     * 计算活动规模
     */
    /*private Double caculateCostForActivity(String activityId, Long entId) {
        Double cost = 0D;
        if (!StringUtils.isEmpty(activityId) && entId != null) {
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityId(activityId);
            if (activityPrizes != null && activityPrizes.size() > 0) {
                for (ActivityPrize item : activityPrizes) {
                    Product product = productService.selectProductById(item.getProductId());
                    EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(item.getProductId(),
                            entId);
                    if (product != null && product.getPrice() != null && entProduct != null
                            && entProduct.getDiscount() != null && item.getCount() != null) {
                        cost += product.getPrice().doubleValue() / 100.0 * entProduct.getDiscount().longValue() / 100.0
                                * item.getCount().longValue();
                    }
                }
            }
        }
        return cost;
    }*/

    /**
     * generateActivivty
     * */
    @Override
    public AutoResponsePojo generateActivivty(Activities activities, ActivityPrize activityPrize) {
        /**
         * 1、插入红包活动信息（活动信息、创建活动账户） 2、创建活动定时任务 3、冻结活动账户
         */
        if (insertRedpacketForIndividual(activities, activityPrize)
                && createActivitySchedule(activities.getActivityId())
                && frozenActivityAccount(activities.getActivityId())) {
            // 向营销模板发送活动请求
            return sendToGenerateActivity(activities.getActivityId());
        }
        return null;
    }

    /**
     * 冻结活动账户
     */
    @Override
    public boolean frozenActivityAccount(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return false;
        }
        Activities activities = selectByActivityId(activityId);
        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
        List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(activityId);
        if (activities != null && activityInfo != null && activityPrizes != null && activityPrizes.size() > 0) {
            // 冻结账户
            if (individualAccountService.changeBossAccount(activities.getCreatorId(),
                    new BigDecimal(activityInfo.getTotalProductSize()), activityPrizes.get(0).getProductId(),
                    activities.getActivityId(), (int) AccountRecordType.OUTGO.getValue(), activities.getName(),
                    activities.getType(), 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建活动定时任务
     */
    @Override
    public boolean createActivitySchedule(String activityId) {
        if (!StringUtils.isEmpty(activityId)) {
            Activities activities = selectByActivityId(activityId);
            if (activities != null) {
                // 创建定时任务
                // 判断当前时间，如果当前时间在活动之间之后，直接设置为活动开始
                if (System.currentTimeMillis() >= activities.getStartTime().getTime()) {
                    if (changeStatus(activities.getActivityId(), ActivityStatus.PROCESSING.getCode())) {
                        if (!createActivityEndSchedule(activities)) {
                            LOGGER.error("创建定时任务(用于活动结束)失败,活动ID-" + activities.getActivityId());
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    // 创建定时任务
                    if (!createActivityStartSchedule(activities)) {
                        LOGGER.error("创建定时任务(用于活动开始)失败,活动ID-" + activities.getActivityId());
                        return false;
                    }
                    if (!createActivityEndSchedule(activities)) {
                        LOGGER.error("创建定时任务(用于活动结束)失败,活动ID-" + activities.getActivityId());
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * insertRedpacketForIndividual
     * */
    @Override
    @Transactional
    public boolean insertRedpacketForIndividual(Activities activities, ActivityPrize activityPrize) {
        if (activities == null || activityPrize == null) {
            return false;
        }
        // 插入活动信息
        // 1、插入活动记录
        initActivity(activities);
        if (!insert(activities)) {
            LOGGER.info("插入活动记录失败：" + JSONArray.toJSONString(activities));
            return false;
        }

        // 2、插入奖品记录
        ActivityPrize record = initActivityPrize(activities, activityPrize);
        if (!activityPrizeService.insertForRedpacket(record)) {
            throw new RuntimeException();
        }

        // 3、插入活动详情
        ActivityInfo activityInfo = initActivityInfo(activities, activityPrize);
        if (!activityInfoService.insertForRedpacket(activityInfo)) {
            throw new RuntimeException();
        }

        // 4、创建活动账户
        IndividualAccount individualAccount = initIndividualAccount(activities, activityPrize, activityInfo);
        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        activityPrizes.add(record);
        if (!individualAccountService.createAccountForActivity(activities, activityInfo, activityPrizes,
                individualAccount)) {
            throw new RuntimeException();
        }
        return true;
    }

    /**
     * 初始化个人账户
     * */
    private IndividualAccount initIndividualAccount(Activities activities, ActivityPrize activityPrize,
            ActivityInfo activityInfo) {
        IndividualAccount individualAccount = new IndividualAccount();
        individualAccount.setAdminId(activities.getCreatorId());
        individualAccount.setOwnerId(activities.getId());
        individualAccount.setIndividualProductId(activityPrize.getProductId());
        individualAccount.setType(IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());
        individualAccount.setCount(new BigDecimal(activityInfo.getTotalProductSize()));
        individualAccount.setCreateTime(new Date());
        individualAccount.setUpdateTime(new Date());
        individualAccount.setVersion(0);
        individualAccount.setDeleteFlag(0);
        return individualAccount;
    }

    /**
     * 初始化活动奖品信息
     * */
    private ActivityPrize initActivityPrize(Activities activities, ActivityPrize activityPrize) {
        activityPrize.setActivityId(activities.getActivityId());
        activityPrize.setEnterpriseId(activities.getEntId());

        // 查找相应产品
        IndividualProductMap individualProductMap = individualProductMapService
                .getByAdminIdAndProductType(activities.getCreatorId(), IndividualProductType.FLOW_COIN.getValue());
        activityPrize.setProductId(individualProductMap.getIndividualProductId());
        activityPrize.setPrizeName(individualProductMap.getProductName());
        return activityPrize;
    }

    /**
     * 初始化活动其他信息
     * */
    private ActivityInfo initActivityInfo(Activities activities, ActivityPrize activityPrize) {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId(activityPrize.getActivityId());
        activityInfo.setPrizeCount(activityPrize.getCount());
        activityInfo.setUserCount(activityPrize.getCount());

        if (activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
            // 拼手气红包
            activityInfo.setTotalProductSize(activityPrize.getSize());
        } else if (activities.getType().toString().equals(ActivityType.COMMON_REDPACKET.getCode().toString())) {
            activityInfo
                    .setTotalProductSize(activityPrize.getCount().longValue() * activityPrize.getSize().longValue());
        }

        // 产品信息
        IndividualProductMap individualProductMap = individualProductMapService
                .getByAdminIdAndProductType(activities.getCreatorId(), IndividualProductType.FLOW_COIN.getValue());
        Long price = individualProductMap.getPrice().intValue() * activityPrize.getCount().longValue()
                * individualProductMap.getDiscount().longValue() / 100;
        activityInfo.setPrice(price);

        return activityInfo;

    }

    /**
     * sendToGenerateActivity
     * */
    @Override
    public AutoResponsePojo sendToGenerateActivity(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return null;
        }
        String sendData = getSendData(activityId);
        LOGGER.info("开始向模板侧发起创建活动请求,请求参数：" + sendData);

        String response = HttpConnection.doPost(getUrl(), sendData, "application/json", "utf-8", true);
        LOGGER.info("创建活动生成结果:" + response);

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
     * judgeEnterpriseForActivity
     * */
    @Override
    public String judgeEnterpriseForActivity(String activityId) {

        if (!StringUtils.isEmpty(activityId)) {
            Activities activities = selectByActivityId(activityId);
            if (activities != null && activities.getEntId() != null) {

                // 校验企业结果
                String result = enterprisesService.judgeEnterprise(activities.getEntId());

                if ("success".equals(result)) {
                    // 校验活动时间
                    if ((new Date()).after(activities.getEndTime())) {
                        return "活动已过期，请修改活动时间！";
                    }

                    // 检查活动产品是否有效
                    String errMsg = checkPrizeAvailable(activities);
                    if (errMsg != null) {
                        return errMsg;
                    }

                    // 余额检查
                    if (!(activities.getType().toString().equals(ActivityType.CROWD_FUNDING.getCode().toString()) ||
                        activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString()) ||
                        activities.getType().toString().equals(ActivityType.QRCODE.getCode().toString()) ||
                        activities.getType().toString().equals(ActivityType.FLOWCARD.getCode().toString()))) {
                        if (!checkAccountEnough(activities)) {
                            return "企业账户余额不足！";
                        }
                    }

                    if(activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())){
                        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);

                        List<ActivityPrize> prizes = activityPrizeService.selectByActivityId(activityId);

                        //随机红包余额校验
                        if(!accountService.verifyAccountForRendomPacket(activities.getEntId(), activityInfo.getTotalProductSize(), prizes.get(0).getProductId())){
                            return "企业账户余额不足！";
                        }
                    }
                }
                return result;
            }
        }
        return "参数缺失，上架失败!";
    }

    /**
     * onShelf
     * */
    @Override
    public String onShelf(String activityId) {
        String result = judgeEnterpriseForActivity(activityId);
        
        if ("success".equals(result)) {

            // 通过校验，活动直接上架
            Activities activities = selectByActivityId(activityId);

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
                    if (changeStatus(activities.getActivityId(), ActivityStatus.PROCESSING.getCode())) {
                        // 流量券需发送短信
                        LOGGER.info("开始塞短信队列。。。。。");
                        ActivitySendMessagePojo pojo = new ActivitySendMessagePojo();
                        pojo.setActivityId(activities.getActivityId());
                        if (ActivityType.fromValue(activities.getType()).equals(ActivityType.FLOWCARD)
                                && !taskProducer.produceActivitySendMessage(pojo)) { //!notifyUserForFlowcard(activities)
                            LOGGER.error("短信下发失败：活动ID-" + activities.getActivityId());
                        }

                        if (!createActivityEndSchedule(activities)) {
                            LOGGER.error("创建定时任务(用于活动结束)失败,活动ID-" + activities.getActivityId());
                        }

                        // 众筹回调
                        if (ActivityType.CROWD_FUNDING.getCode().toString().equals(activities.getType().toString())) {
                            CrowdfundingActivityDetail detail = crowdfundingActivityDetailService
                                    .selectByActivityId(activityId);
                            if (detail.getJoinType().toString()
                                    .equals(CrowdFundingJoinType.Big_Enterprise.getCode().toString())) {
                                // 众筹活动，并且是企业报名，所以回调
                                if (!crowdfundingCallbackService.notifyCreateActivity(activityId)) {
                                    LOGGER.info("回调失败");
                                }
                            }
                        }
                        return result;
                    }
                    return "活动上架失败";
                } else {
                    if (changeStatus(activities.getActivityId(), ActivityStatus.ON.getCode())) {
                        // 创建定时任务
                        if (!createActivityStartSchedule(activities)) {
                            LOGGER.error("创建定时任务(用于活动开始)失败,活动ID-" + activities.getActivityId());
                        }
                        if (!createActivityEndSchedule(activities)) {
                            LOGGER.error("创建定时任务(用于活动结束)失败,活动ID-" + activities.getActivityId());
                        }

                        // 众筹回调
                        if (ActivityType.CROWD_FUNDING.getCode().toString().equals(activities.getType().toString())) {
                            CrowdfundingActivityDetail detail = crowdfundingActivityDetailService
                                    .selectByActivityId(activityId);
                            if (detail.getJoinType().toString()
                                    .equals(CrowdFundingJoinType.Big_Enterprise.getCode().toString())) {
                                // 众筹活动，并且是企业报名，所以回调
                                if (!crowdfundingCallbackService.notifyCreateActivity(activityId)) {
                                    LOGGER.info("回调失败");
                                }
                            }
                        }

                        return result;
                    }
                    return "活动上架失败";
                }
            }
        }
        return result;
    }

    /**
     * insertIndividualPresentActivity
     * */
    @Override
    @Transactional
    public boolean insertIndividualPresentActivity(String activityName, String phonesNum, Integer coinsCount,
            Long adminId, Long productId, String ownerMobile) {

        Integer prizeCount = phonesNum.split(",").length;
        Integer totalCoinsCount = coinsCount * prizeCount;

        // 1、插入活动记录
        Activities activity = initIndividualPresentActivity(activityName, adminId);
        if (!insert(activity)) {
            LOGGER.info("插入活动记录失败：" + JSONArray.toJSONString(activity));
            return false;
        }

        // 2、插入活动详情
        if (!activityInfoService.insert(initIndividualPresentActivityInfo(activity.getActivityId(),
                prizeCount.longValue(), totalCoinsCount.longValue()))) {
            LOGGER.info("插入活动详情失败：" + JSONArray.toJSONString(activity));
            throw new RuntimeException();
        }

        // 3、插入活动奖品信息
        ActivityPrize activityPrizes = initIndividualPresentPrize(activity.getActivityId(), productId, adminId,
                prizeCount.longValue(), coinsCount.longValue());
        if (!activityPrizeService.insert(activityPrizes)) {
            LOGGER.info("插入活动奖品信息失败：" + JSONArray.toJSONString(activity));
            throw new RuntimeException();
        }

        // 4、插入赠送手机号码
        if (!activityWinRecordService.batchInsertFlowcoinPresent(activity.getActivityId(), phonesNum, productId,
                ownerMobile)) {
            LOGGER.info("插入手机号失败：" + JSONArray.toJSONString(activity));
            throw new RuntimeException();
        }

        // 5.创建账户
        IndividualAccount individualAccount = initIndividualPresentAccount(activity.getId(), adminId, totalCoinsCount,
                productId);
        if (!individualAccountService.createFlowcoinExchangeAndPurchaseAccount(individualAccount,
                activity.getActivityId(), "流量币赠送", ActivityType.FLOWCOIN_PRESENT.getCode())) {
            LOGGER.info("创建账户失败：" + JSONArray.toJSONString(activity));
            throw new RuntimeException();
        }

        // 6.加入队列开始赠送
        List<ActivityWinRecord> activityWinRecords = activityWinRecordService
                .selectByActivityId(activity.getActivityId());
        individualAccount = individualAccountService.getAccountByOwnerIdAndProductId(activity.getId(), productId,
                IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());
        FlowcoinPresentPojo pojo = new FlowcoinPresentPojo();
        for (ActivityWinRecord activityWinRecord : activityWinRecords) {
            pojo.setIndividualAccount(individualAccount);
            pojo.setActivityWinRecord(activityWinRecord);
            boolean bFlag = taskProducer.produceFlowcoinPresentMsg(pojo);
            if (!bFlag) {
                LOGGER.error("Adding to flowcoin present queue returns {}.", bFlag);
                throw new RuntimeException("插入队列失败！");
            }
        }

        return true;
    }
    /**
     * 初始化个人赠送活动其他信息
     * */
    private ActivityInfo initIndividualPresentActivityInfo(String activityId, Long prizeCount, Long totalProductSize) {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId(activityId);
        activityInfo.setPrizeCount(prizeCount);
        activityInfo.setUserCount(null);
        activityInfo.setTotalProductSize(totalProductSize);
        activityInfo.setCreateTime(new Date());
        activityInfo.setUpdateTime(new Date());
        activityInfo.setDeleteFlag(0);
        return activityInfo;
    }
    /**
     * 初始化个人赠送活动信息
     * */
    private Activities initIndividualPresentActivity(String activityName, Long adminId) {
        Activities activities = new Activities();
        activities.setActivityId(SerialNumGenerator.buildSerialNum());
        activities.setEntId(adminId);
        activities.setName(activityName);
        activities.setStartTime(new Date());
        activities.setEndTime(new Date());
        activities.setType(ActivityType.FLOWCOIN_PRESENT.getCode());
        activities.setCreatorId(adminId);
        activities.setStatus(ActivityStatus.END.getCode());
        activities.setDeleteFlag(0);
        activities.setCreateTime(new Date());
        activities.setUpdateTime(new Date());
        return activities;
    }
    /**
     * 初始化个人赠送活动奖品信息
     * */
    private ActivityPrize initIndividualPresentPrize(String activityId, Long productId, Long adminId, Long count,
            Long productSize) {
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setActivityId(activityId);
        activityPrize.setIdPrefix("0");
        activityPrize.setEnterpriseId(adminId);
        activityPrize.setProductId(productId);
        activityPrize.setCount(count);
        activityPrize.setSize(productSize);
        activityPrize.setCreateTime(new Date());
        activityPrize.setUpdateTime(new Date());
        activityPrize.setDeleteFlag(0);
        activityPrize.setType(3);
        return activityPrize;
    }
    /**
     * 初始化个人赠送账户
     * */
    private IndividualAccount initIndividualPresentAccount(Long activityId, Long adminId, Integer count,
            Long productId) {
        IndividualAccount individualAccount = new IndividualAccount();
        individualAccount.setAdminId(adminId);
        individualAccount.setOwnerId(activityId);
        individualAccount.setCount(new BigDecimal(count));
        individualAccount.setType(0);
        individualAccount.setIndividualProductId(productId);
        individualAccount.setCreateTime(new Date());
        individualAccount.setDeleteFlag(0);
        individualAccount.setUpdateTime(new Date());
        individualAccount.setVersion(0);
        return individualAccount;
    }

    /**
     * 获取生成活动url
     * */
    public String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_GENERATE_URL.getKey());
    }

    /**
     * getSendData
     * */
    @Override
    public String getSendData(String activityId) {
        if (StringUtils.isEmpty(activityId)) {
            return null;
        }
        Activities activities = selectByActivityId(activityId);
        if (activities != null) {
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(activityId);

            if (activityPrizes != null && activityPrizes.size() > 0) {
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
                // 个人集中平台，活动为个人创建，entId为creatorId
                pojo.setEnterpriseId(activities.getCreatorId().toString());
                pojo.setEnterpriseName("集中平台" + activities.getName());
                pojo.setActiveId(activities.getActivityId());
                pojo.setActiveName(activities.getName());
                pojo.setUserType(0); // 用户类型0，以手机号为中奖用户标识
                if (activities.getType().toString().equals(ActivityType.COMMON_REDPACKET.getCode().toString())) {
                    pojo.setGameType(ActivityTemplateType.COMMON_REDPACKET.getCode()); // 游戏类型,强制转化成普通红包
                } else {
                    pojo.setGameType(ActivityTemplateType.LUCKY_REDPACKET.getCode()); // 游戏类型,强制转化成普通红包
                }
                pojo.setGivedNumber(1); // 用户可中最大数量
                pojo.setDaily("false"); // 用户次数每天是否重置
                pojo.setFixedProbability(1);

                ActivityTemplate template = activityTemplateService.selectByActivityId(activityId);
                pojo.setCheckType(template.getCheckType());// 白名单
                pojo.setCheckUrl(template.getCheckUrl()); // 鉴权接口地址
                pojo.setObject(template.getObject());

                String rules = "";
                if (!StringUtils.isEmpty(template.getRules())) {
                    String[] ruleArray = template.getRules().split("\\|");
                    for (int i = 0; i < ruleArray.length; i++) {
                        if (i == 0) {
                            rules = ruleArray[i];
                        } else {
                            rules += "<br>" + ruleArray[i] + "</br>";
                        }
                    }
                }
                pojo.setRules(rules);
                /*
                 * pojo.setObject("四川移动用户");
                 * pojo.setRules("本次红包活动奖品为流量币，奖品将发放至用户的流量币账户中，" +
                 * "中奖用户可登录四川移动官方平台" + getLoginUrl() +
                 * "进行奖品兑换，流量币奖品有效期至中奖时间的次月月底。");
                 */
                pojo.setStatus(ActivityStatus.ON.getCode());
                pojo.setChargeUrl(getCallbackUrl());
                pojo.setTime(timePojo);
                pojo.setPrizes(autoPrizesPojoList);
                return JSON.toJSONString(pojo);
            }
        }
        return null;
    }
    /**
     * getLoginUrl
     * */
    // 获取登录平台URL
    public String getLoginUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.LOGIN_URL.getKey());
    }
    /**
     * 转发奖品信息
     * */
    private List<AutoPrizesPojo> getPrizesLists(Activities activities, List<ActivityPrize> activityPrizes) {
        List<AutoPrizesPojo> autoPrizesPojoList = new ArrayList<AutoPrizesPojo>();
        if (activityPrizes != null) {
            int i = 0;
            for (ActivityPrize prize : activityPrizes) {
                AutoPrizesPojo autoPrizesPojo = new AutoPrizesPojo();
                autoPrizesPojo.setIdPrefix(i);
                autoPrizesPojo.setRankName("一等奖");

                autoPrizesPojo.setCmccId(prize.getId().toString());

                autoPrizesPojo.setCmccEnterId(activities.getCreatorId().toString());

                autoPrizesPojo.setCmccCount(prize.getSize().intValue());
                if (prize.getProductId().toString()
                        .equals(individualProductService.getDefaultFlowProduct().getId().toString())) {
                    if (activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
                        // 流量币总量
                        autoPrizesPojo.setCmccName("M流量");
                    } else {
                        autoPrizesPojo.setCmccName(prize.getSize().toString() + "M流量");
                    }
                } else {
                    if (activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
                        // 流量币总量
                        autoPrizesPojo.setCmccName("个流量币");
                    } else {
                        autoPrizesPojo.setCmccName(prize.getSize().toString() + "个流量币");
                    }
                }

                autoPrizesPojo.setCmccType(2); // 流量币
                autoPrizesPojo.setCmccResponse(1); // 采用回调方式
                autoPrizesPojo.setCount(prize.getCount().intValue());
                autoPrizesPojo.setProbability("1.0");

                autoPrizesPojoList.add(autoPrizesPojo);
                i++;
            }
        }
        return autoPrizesPojoList;
    }

    /**
     * 获取活动平台名称
     * */
    public String getPlateName() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_PLATFORM_NAME.getKey());
    }

    /**
     * 获取回调地址
     * */
    public String getCallbackUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_CALLBACK_URL.getKey());
    }
    /**
     * closeActivity
     * */
    @Override
    @Transactional
    public boolean closeActivity(Activities activities, ActivityInfo activityInfo, List<ActivityPrize> activityPrizes) {
        // 1、更改活动状态
        if (!changeStatus(activities.getActivityId(), ActivityStatus.DOWN.getCode())) {
            LOGGER.info("修改活动状态失败，活动ID-" + activities.getActivityId());
            return false;
        }

        // 2、流量币回退
        if (!individualAccountService.giveBackForActivity(activities.getActivityId())) {
            LOGGER.info("回退流量币失败，活动ID-" + activities.getActivityId());
            throw new RuntimeException();
        }

        // 3、发送请求停止活动
        if (!activityTemplateService.notifyTemplateToClose(activityInfo.getUrl(), activities.getActivityId())) {
            LOGGER.info("像营销模板服务请求下架失败，活动ID-" + activities.getActivityId());
            throw new RuntimeException();
        }

        return true;
    }

    /**
     * 构造流水号
     * */
    private SerialNum buildSerailNum(String platformSerialNum) {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum(platformSerialNum);
        serialNum.setUpdateTime(new Date());
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(0);
        return serialNum;
    }

    /**
     * 构造充值记录
     * */
    private ChargeRecord buildChargeRecord(ActivityWinRecord activityWinRecord, Activities activities) {
        ActivityType activityType = ActivityType.fromValue(activities.getType());
        ChargeRecord cr = new ChargeRecord();
        cr.setPrdId(activityWinRecord.getPrizeId());
        cr.setEnterId(activities.getEntId());
        cr.setTypeCode(activities.getType());
        cr.setRecordId(activityWinRecord.getId());
        cr.setStatus(ChargeRecordStatus.WAIT.getCode());
        cr.setType(activityType.getname());
        cr.setPhone(activityWinRecord.getOwnMobile());
        cr.setaName(activities.getName());
        cr.setSystemNum(activityWinRecord.getRecordId());
        cr.setChargeTime(new Date());
        return cr;
    }

    /**
     * 更改中奖纪录状态
     * */
    private boolean changeActivityWinStatus(String serialNum, String statusCode, Integer status) {
        LOGGER.info("start to change activity_win_record fail. recordId-{}, statusCode-{}," + "status-{}", serialNum,
                statusCode, status);
        if (!StringUtils.isEmpty(serialNum) && !StringUtils.isEmpty(statusCode) && status != null) {
            ActivityWinRecord activityWinRecord = new ActivityWinRecord();
            activityWinRecord.setRecordId(serialNum);
            activityWinRecord.setStatus(status);
            activityWinRecord.setStatusCode(statusCode);
            if (activityWinRecordService.updateByPrimaryKeySelective(activityWinRecord)) {
                LOGGER.info("succeed to change activity_win_record status");
                return true;
            }
        }
        LOGGER.info("fail to change activity_win_record status");
        return false;
    }
    /**
     * wxCrowdfundingActivityList
     * */
    @Override
    public List<Activities> wxCrowdfundingActivityList(Map map) {
        if(map.get("mobile")!=null){
            List<String> activityIds = activityWinRecordService.selectActivityIdByMobile((String) map.get("mobile"));
            if(!activityIds.isEmpty()){
                map.put("activityIds", activityIds);
            }
        }
        return activitiesMapper.selectWxCrowdfundingActivityList(map);
    }
    /**
     * myCrowdfundingActivityList
     * */
    @Override
    public List<Activities> myCrowdfundingActivityList(Map map) {
        return activitiesMapper.selectMyCrowdfundingActivityList(map);
    }
    /**
     * joinCrowdfundingActivity
     * */
    @Override
    public boolean joinCrowdfundingActivity(String activityId, String ownMobile, Long prizeId, String wxOpenid) {
        // 1、增加数据库相关记录
        CrowdfundingActivityDetail detail = crowdfundingActivityDetailService.selectByActivityId(activityId);
        if (detail == null) {
            LOGGER.info("众筹活动详情为空，activityId={}", activityId);
            return false;
        }
        if (!addJoinCrowdfundingActivityRecord(activityId, ownMobile, prizeId, wxOpenid, detail)) {
            LOGGER.info("数据库中插入众筹活动参与记录失败");
            return false;
        }
        // 2、活动众筹结果为众筹中，且当前人数大于等于目标人数，活动众筹结果需要更新为成功变成(中小企业版)，大企业版众筹成功的修改在支付成功后进行
        if (detail.getJoinType().toString().equals(CrowdFundingJoinType.Small_Enterprise.getCode().toString())
                && detail.getResult().equals(0) && detail.getCurrentCount() >= detail.getTargetCount()) {
            detail.setResult(1);
            crowdfundingActivityDetailService.updateByPrimaryKeySelective(detail);
            List<TemplateMsgPojo> pojos = createTemplateMsgPojos(activityId);
            if (!taskProducer.produceBatchWxSendTemplateMsg(pojos)) {
                LOGGER.info("众筹成功通知，用户发送微信公众号模板消息入队失败，失败");
            }
        }
        return true;
    }

    /**
     * 增加参加众筹活动需要的记录
     * 
     * @Title: addJoinCrowdfundingActivityRecord
     */
    @Transactional
    private boolean addJoinCrowdfundingActivityRecord(String activityId, String ownMobile, Long prizeId,
            String wxOpenid, CrowdfundingActivityDetail detail) {
        // 1、插入activityWinRecord表
        ActivityWinRecord record = new ActivityWinRecord();
        record.setActivityId(activityId);
        record.setRecordId(SerialNumGenerator.buildSerialNum());
        record.setOwnMobile(ownMobile);
        record.setChargeMobile(ownMobile);
        record.setPrizeId(prizeId);
        record.setWinTime(new Date());
        record.setStatus(ChargeRecordStatus.WAIT.getCode());
        record.setDeleteFlag(0);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setWxOpenid(wxOpenid);
        record.setPayResult(PaymentStatus.Not_Pay.getStatus());// 表示未支付
        if (activityWinRecordService.insertSelective(record)) {
            // 2、增加当前活动参与人数值
            detail.setCurrentCount(detail.getCurrentCount() + 1);
            if (crowdfundingActivityDetailService.updateCurrentCount(detail)) {
                return true;
            }
        }

        throw new RuntimeException();
    }

    /**
     * 构造模板消息即将入队的对象
     * 
     * @Title: createTemplateMsgPojos
     */
    private List<TemplateMsgPojo> createTemplateMsgPojos(String activityId) {
        List<TemplateMsgPojo> pojos = new ArrayList<TemplateMsgPojo>();
        List<ActivityWinRecord> records = activityWinRecordService.selectByActivityId(activityId);
        for (ActivityWinRecord record : records) {
            TemplateMsgPojo pojo = new TemplateMsgPojo();
            pojo.setMobile(record.getOwnMobile());
            pojo.setActivityWinRecordId(record.getRecordId());
            pojo.setActivityId(activityId);
            pojo.setType(WxTemplateMsgType.CROWDFUNDING_SUCCESS);
            pojos.add(pojo);
        }
        return pojos;
    }
    /**
     * checkUser
     * */
    @Override
    public boolean checkUser(String mobile, String activityId) {
        // 判断是否在黑白名单中
        Map map = new HashMap();
        map.put("activityId", activityId);
        map.put("isWhite", "1");
        map.put("mobile", mobile);
        List<String> phones = activityBlackAndWhiteService.selectPhonesByMap(map);
        if (!phones.contains(mobile)) {
            LOGGER.info("号码{}不在活动{}的白名单中", mobile, activityId);
            return false;
        }
        return true;
    }
    /**
     * decryptActivityId
     * */
    @Override
    public Map<String, String> decryptActivityId(String encryptActivitId, String enterpriseCode, String ecProductCode) {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        String code = "fail";
        String msg = "异常参数";
        if (!StringUtils.isEmpty(encryptActivitId) && !StringUtils.isEmpty(enterpriseCode) && !StringUtils.isEmpty(ecProductCode)) {
            List<EnterprisesExtInfo> extInfos = enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(enterpriseCode, ecProductCode);
            if(extInfos.size()!=1){
                LOGGER.info("企业信息验证失败，extInfo异常，ecCode={}, ecProductCode={}", enterpriseCode, ecProductCode);
                map.put("code", code);
                map.put("message", msg);
                return map;
            }
            Enterprise enterprise = enterprisesService.selectByPrimaryKey(extInfos.get(0).getEnterId());
            if (enterprise != null && !StringUtils.isEmpty(enterprise.getAppKey())
                    && !StringUtils.isEmpty(enterprise.getAppSecret())) {

                byte[] encryptArray = DatatypeConverter.parseHexBinary(encryptActivitId);
                byte[] decryptResult = null;
                try {
                    decryptResult = AES.decrypt(encryptArray, enterprise.getAppKey().getBytes());
                    if (decryptResult == null) {
                        LOGGER.info("解密结果为空！");
                    } else {
                        String result = new String(decryptResult);
                        LOGGER.info("解密结果=" + result);

                        String[] resultArray = result.split(";");
                        if (resultArray != null && resultArray.length == 2
                                && resultArray[0].equals(enterprise.getAppSecret())) {
                            if (selectByActivityId(resultArray[1]) != null) {
                                code = "success";
                                msg = resultArray[1];
                            } else {
                                LOGGER.info("未查找到相应活动！活动ID-" + resultArray[1]);
                            }
                        } else {
                            LOGGER.info("活动ID异常，解密失败！");
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("活动ID解密失败，失败原因：" + e.getMessage());
                }
            } else {
                LOGGER.info("企业参数异常，解密失败！");
            }
        }
        map.put("code", code);
        map.put("message", msg);
        return map;
    }
    /**
     * encryptActivityId
     * */
    @Override
    public String encryptActivityId(String activitId) {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(activitId)) {
            Activities activities = selectByActivityId(activitId);
            if (activities != null) {
                Enterprise enterprise = enterprisesService.selectById(activities.getEntId());
                if (!StringUtils.isEmpty(enterprise.getAppKey()) && !StringUtils.isEmpty(enterprise.getAppSecret())) {
                    String content = enterprise.getAppSecret() + ";" + activitId;
                    // String content = activitId;
                    byte[] encryptResult = AES.encrypt(content.getBytes(), enterprise.getAppKey().getBytes());
                    return DatatypeConverter.printHexBinary(encryptResult);
                }
            }
        }
        return null;
    }
    /**
     * downShelfIndividualFlowRedpacket
     * */
    @Override
    public boolean downShelfIndividualFlowRedpacket(String activityId) {
        Activities activities = selectByActivityId(activityId);
        if (activities == null) {
            LOGGER.info("活动不存在，activityId=" + activityId);
            return false;
        }
        if (activities.getStatus().toString().equals(ActivityStatus.PROCESSING.getCode().toString())
                && activities.getDeleteFlag().toString().equals("0")) {
            // 1、通知营销模板活动下架
            LOGGER.info("通知营销模板活动下架,活动ID-" + activities.getActivityId());
            ActivityInfo activityInfo = activityInfoService.selectByActivityId(activities.getActivityId());
            if (activityInfo == null || !activityTemplateService.notifyTemplateToClose(activityInfo.getUrl(), activityId)) {
                LOGGER.info("向营销模板服务请求下架失败，活动ID-{}", activities.getActivityId());
            }

            // 2、变更数据库中的活动状态
            if (changeStatus(activities.getActivityId(), ActivityStatus.END.getCode())) {
                LOGGER.info("营销活动状态变更为已结束,活动ID-" + activities.getActivityId());
                // 3、个人流量红包需要进行退回操作
                List<ActivityPrize> activityPrizeList = activityPrizeService
                        .selectByActivityIdForIndividual(activities.getActivityId());
                if (activityPrizeList != null) {
                    IndividualAccount individualAccount = individualAccountService.getAccountByOwnerIdAndProductId(
                            activities.getId(), activityPrizeList.get(0).getProductId(),
                            IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());
                    if (individualAccount != null && individualAccount.getCount().intValue() > 0) {
                        IndividualProduct product = individualProductService
                                .selectByPrimaryId(activityPrizeList.get(0).getProductId());
                        // 活动账户里有剩余流量，需要进行回退操作
                        // 产品是流量,调用退回个人流量账户的方法
                        if (product.getType().equals(IndividualProductType.DEFAULT_FLOW_PACKAGE.getValue())) {
                            if (!individualAccountService.giveBackFlow(activities.getActivityId())) {
                                LOGGER.info("个人红包活动剩余流量退回个人流量账户操作失败，活动ID-" + activities.getActivityId());
                                return false;
                            }
                        }
                    }
                }
                return true;
            } else {
                LOGGER.info("营销活动状态变更为已结束失败,活动ID-" + activities.getActivityId());
                return false;
            }
        }

        LOGGER.info("该活动并非进行中状态，不需做下架操作，活动ID-" + activities.getActivityId() + " ;活动状态-" + activities.getStatus()
                + " ;活动删除状态-" + activities.getDeleteFlag());
        return true;

    }
    /**
     * selectForOrder
     * */
    @Override
    public List<Activities> selectForOrder(Long creatorId, String orderSystemNum, Integer type, Integer status) {
        return activitiesMapper.selectForOrder(creatorId, orderSystemNum, type, status);
    }
    /**
     * selectLastWxLottery
     * */
    @Override
    public List<Activities> selectLastWxLottery(Map map) {
        return activitiesMapper.selectLastWxLottery(map);
    }

    @Override
    protected String getPrefix() {        
        return "activity.";
    }

}

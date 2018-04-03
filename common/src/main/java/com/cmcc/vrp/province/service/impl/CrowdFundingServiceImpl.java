package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.GDCrowdFundingResult;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.CrowdfundingQueryUrl;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.CrowdFundingService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.CrowdfundingQueryUrlService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.PayResultQueryService;
import com.google.gson.Gson;

/**
 * Created by qinqinyan on 2017/1/21.
 */
@Service("crowdFundingService")
public class CrowdFundingServiceImpl implements CrowdFundingService {
    private static final Logger logger = LoggerFactory.getLogger(CrowdFundingServiceImpl.class);

    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ActivityBlackAndWhiteService activityBlackAndWhiteService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    CrowdfundingQueryUrlService crowdfundingQueryUrlService;
    @Autowired
    ActivityPaymentInfoService activityPaymentInfoService;
    @Autowired
    PayResultQueryService payResultQueryService;
    /**
     * 众筹活动插入
     *
     * @author qinqinyan
     */
    @Override
    @Transactional
    public boolean insertActivity(Activities activities, CrowdfundingActivityDetail crowdfundingActivityDetail,
                                  List<ActivityPrize> activityPrizes, String phones, String queryurl) throws RuntimeException {
        if (activities == null || crowdfundingActivityDetail == null || activityPrizes == null
                || activityPrizes.size() < 1) {
            logger.info("插入参数为空");
            return false;
        }

        if (!activitiesService.insert(initActivities(activities))) {
            logger.info("插入活动基本信息失败");
            return false;
        }

        crowdfundingActivityDetail.setActivityId(activities.getActivityId());
        if (!crowdfundingActivityDetailService.insert(initCrowdfundingActivityDetail(crowdfundingActivityDetail))) {
            logger.info("插入活动详细信息失败");
            throw new RuntimeException();
        }

        if (!activityPrizeService
                .batchInsertForCrowdFunding(initActivityPrizes(activityPrizes, activities.getActivityId()))) {
            logger.info("插入活动奖品信息失败");
            throw new RuntimeException();
        }

        if (crowdfundingActivityDetail.getUserList() == 1
                && !StringUtils.isEmpty(phones)) {
            if (!activityBlackAndWhiteService.batchInsert(activities.getActivityId(),
                    crowdfundingActivityDetail.getHasWhiteOrBlack(), phones)) {
                logger.info("插入活动黑白名单失败");
                throw new RuntimeException();
            }
        }
        
        if (crowdfundingActivityDetail.getUserList() == 3) {
            CrowdfundingQueryUrl crowdfundingQueryUrl = new CrowdfundingQueryUrl();
            crowdfundingQueryUrl.setCrowdfundingActivityDetailId(crowdfundingActivityDetail.getId());
            crowdfundingQueryUrl.setDeleteFlag(0);
            crowdfundingQueryUrl.setQueryUrl(queryurl);
            if (!crowdfundingQueryUrlService.insert(crowdfundingQueryUrl)) {
                logger.info("插入企业查询接口失败");
                throw new RuntimeException();
            }
        }
        return true;
    }

    /**
     * 初始化活动信息
     */
    private Activities initActivities(Activities record) {
        record.setStatus(ActivityStatus.SAVED.getCode());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(0);
        return record;
    }

    /**
     * 初始化活动信息
     */
    private CrowdfundingActivityDetail initCrowdfundingActivityDetail(CrowdfundingActivityDetail record) {
        record.setCurrentCount(0L);
        record.setResult(GDCrowdFundingResult.Crowd_Funding.getCode());
        record.setDeleteFlag(0);
        record.setVersion(0L);
        return record;
    }

    /**
     * 初始化奖品信息
     */
    private List<ActivityPrize> initActivityPrizes(List<ActivityPrize> records, String activityId) {
        for (ActivityPrize item : records) {
            item.setActivityId(activityId);
            item.setCreateTime(new Date());
            item.setUpdateTime(new Date());
            item.setDeleteFlag(0);
        }
        return records;
    }

    @Override
    @Transactional
    public boolean updateActivity(Activities activities, CrowdfundingActivityDetail crowdfundingActivityDetail,
                                  List<ActivityPrize> activityPrizes, String phones, String queryurl) {
        // TODO Auto-generated method stub

        // 1、更新奖品信息
        Activities historyActivity = activitiesService.selectByActivityId(activities.getActivityId());
        List<ActivityPrize> historyPrizes = activityPrizeService.selectByActivityId(activities.getActivityId());
        /*CrowdfundingActivityDetail historyDetail = crowdfundingActivityDetailService
                .selectByActivityId(activities.getActivityId());*/

        if (historyActivity.getEntId().longValue() == activities.getEntId().longValue()) {
            // 如果企业不变，更新奖品信息
            List<ActivityPrize> addList = getAddActivityPrizes(activityPrizes, historyPrizes);
            List<Long> delList = getDelelteActivityPrizes(activityPrizes, historyPrizes);
            List<ActivityPrize> updateList = getUpdateActivityPrizes(activityPrizes, historyPrizes);

            // 1.1 增加新奖品
            if (addList != null && addList.size() > 0) {
                if (!activityPrizeService
                        .batchInsertForCrowdFunding(initActivityPrizes(addList, historyActivity.getActivityId()))) {
                    logger.info("插入新奖品失败。");
                    return false;
                }
            }

            // 1.2 删除奖品
            if (delList != null && delList.size() > 0) {
                if (!activityPrizeService.deleteActivityPrize(delList, activities.getActivityId())) {
                    logger.info("删除奖品失败。");
                    throw new RuntimeException();
                }
            }

            // 1.3 更新奖品
            if (updateList != null && updateList.size() > 0) {
                if (!activityPrizeService.batchUpdateDiscount(updateList)) {
                    logger.info("更新奖品折扣失败。");
                    throw new RuntimeException();
                }
            }

        } else {
            // 企业信息改变，则删除原有奖品信息，重新插入新奖品信息
            if (!activityPrizeService.deleteByActivityId(activities.getActivityId()) || !activityPrizeService
                    .batchInsert(initActivityPrizes(activityPrizes, activities.getActivityId()))) {
                logger.info("插入活动奖品信息失败。");
                throw new RuntimeException();
            }
        }

        // 2、更新活动基本信息
        if (!activitiesService.updateByPrimaryKeySelective(activities)) {
            logger.info("更新活动基本信息失败。");
            throw new RuntimeException();
        }

        // 3、更新活动详情
        if (!crowdfundingActivityDetailService.updateByPrimaryKeySelective(crowdfundingActivityDetail)) {
            logger.info("更新活动详情失败。");
            throw new RuntimeException();
        }

        //4.只有用户列表（都是白名单）
        if (crowdfundingActivityDetail.getUserList() == 1
                && !StringUtils.isEmpty(phones)) {
            //删除原有名单
            if (!activityBlackAndWhiteService.deleteByActivityId(activities.getActivityId())) {
                logger.info("删除用户名单失败。");
                throw new RuntimeException();
            }

            //插入现有名单
            if (!activityBlackAndWhiteService.batchInsert(activities.getActivityId(),
                    crowdfundingActivityDetail.getHasWhiteOrBlack(), phones)) {
                logger.info("插入用户名单失败");
                throw new RuntimeException();
            }
        }
        //5.更新企业查询接口记录
        if (crowdfundingActivityDetail.getUserList() == 3
                && !StringUtils.isEmpty(queryurl)) {
            CrowdfundingActivityDetail newCrowdfundingActivityDetail = crowdfundingActivityDetailService.selectByActivityId(activities.getActivityId());
            if (crowdfundingQueryUrlService.getByCrowdfundingActivityDetailId(newCrowdfundingActivityDetail.getId()) == null) {
                CrowdfundingQueryUrl crowdfundingQueryUrl = new CrowdfundingQueryUrl();
                crowdfundingQueryUrl.setCrowdfundingActivityDetailId(newCrowdfundingActivityDetail.getId());
                crowdfundingQueryUrl.setDeleteFlag(0);
                crowdfundingQueryUrl.setQueryUrl(queryurl);
                if (!crowdfundingQueryUrlService.insert(crowdfundingQueryUrl)) {
                    logger.info("插入企业查询接口失败");
                    throw new RuntimeException();
                }
            } else {
                CrowdfundingQueryUrl crowdfundingQueryUrl = new CrowdfundingQueryUrl();
                crowdfundingQueryUrl.setCrowdfundingActivityDetailId(newCrowdfundingActivityDetail.getId());
                crowdfundingQueryUrl.setQueryUrl(queryurl);
                if (!crowdfundingQueryUrlService.updateByCrowdfundingActivityDetailId(crowdfundingQueryUrl)) {
                    logger.info("更新企业查询接口失败");
                    throw new RuntimeException();
                }
            }
        }

        // 4、更新活动黑白名单
        // 4.1先删除
        /*if ((!historyDetail.getHasWhiteOrBlack().toString().equals(BlackAndWhiteListType.NOLIST.getCode().toString())
                && !crowdfundingActivityDetail.getHasWhiteOrBlack().toString()
                        .equals(BlackAndWhiteListType.NOLIST.getCode().toString())
                && !StringUtils.isEmpty(phones))
                || !historyDetail.getHasWhiteOrBlack().toString()
                        .equals(BlackAndWhiteListType.NOLIST.getCode().toString())
                        && crowdfundingActivityDetail.getHasWhiteOrBlack().toString()
                                .equals(BlackAndWhiteListType.NOLIST.getCode().toString())) {
            // (1)原设置黑白名单，现在设置了，并且上传的手机号不为空，先删除原有;
            // (2)原有设置了黑白名单，现在设置为不需要黑白名单，则直接删除所有名单
            if (!activityBlackAndWhiteService.deleteByActivityId(activities.getActivityId())) {
                logger.info("删除黑白名单失败。");
                throw new RuntimeException();
            }
        }
        // 4.2再插入
        if (historyDetail.getHasWhiteOrBlack().toString().equals(BlackAndWhiteListType.NOLIST.getCode().toString())
                && !crowdfundingActivityDetail.getHasWhiteOrBlack().toString()
                        .equals(BlackAndWhiteListType.NOLIST.getCode().toString())
                || !historyDetail.getHasWhiteOrBlack().toString()
                        .equals(BlackAndWhiteListType.NOLIST.getCode().toString())
                        && !crowdfundingActivityDetail.getHasWhiteOrBlack().toString()
                                .equals(BlackAndWhiteListType.NOLIST.getCode().toString())) {
            // (1)原未设置黑白名单，现在设置了，直接插入黑白名单；
            // (2)原设置黑白名单，现在设置了，再插入;
            if (!StringUtils.isEmpty(phones)) {
                if (!activityBlackAndWhiteService.batchInsert(activities.getActivityId(),
                        crowdfundingActivityDetail.getHasWhiteOrBlack(), phones)) {
                    logger.info("插入活动黑白名单失败");
                    throw new RuntimeException();
                }
            }
        }*/
        return true;
    }

    /**
     * 获取要删除的奖品信息
     *
     * @author qinqinyan
     */
    private List<Long> getDelelteActivityPrizes(List<ActivityPrize> prizes, List<ActivityPrize> historyPrizes) {
        // List<ActivityPrize> delPrizes = new ArrayList<ActivityPrize>();
        List<Long> delProdIds = new ArrayList<Long>();
        for (ActivityPrize historyItem : historyPrizes) {
            int i = 0;
            for (ActivityPrize prize : prizes) {
                if (prize.getProductId().toString().equals(historyItem.getProductId().toString())) {
                    break;
                }
                i++;
            }
            if (i == prizes.size()) {
                // delPrizes.add(historyItem);
                delProdIds.add(historyItem.getProductId());
            }
        }
        // return delPrizes;
        return delProdIds;
    }

    /**
     * 获取要增加的奖品信息
     *
     * @author qinqinyan
     */
    private List<ActivityPrize> getAddActivityPrizes(List<ActivityPrize> prizes, List<ActivityPrize> historyPrizes) {
        List<ActivityPrize> addPrizes = new ArrayList<ActivityPrize>();
        for (ActivityPrize prize : prizes) {
            int i = 0;
            for (ActivityPrize historyItem : historyPrizes) {
                if (prize.getProductId().toString().equals(historyItem.getProductId().toString())) {
                    break;
                }
                i++;
            }
            if (i == historyPrizes.size()) {
                addPrizes.add(prize);
            }
        }
        return addPrizes;
    }

    /**
     * 获取要更新的奖品信息
     *
     * @author qinqinyan
     */
    private List<ActivityPrize> getUpdateActivityPrizes(List<ActivityPrize> prizes, List<ActivityPrize> historyPrizes) {
        List<ActivityPrize> updatePrizes = new ArrayList<ActivityPrize>();
        for (ActivityPrize prize : prizes) {
            for (ActivityPrize historyItem : historyPrizes) {
                if (prize.getProductId().toString().equals(historyItem.getProductId().toString())
                        && !prize.getDiscount().toString().equals(historyItem.getDiscount().toString())) {
                    ActivityPrize updateItem = historyItem;
                    updateItem.setIdPrefix(prize.getIdPrefix());
                    updateItem.setDiscount(prize.getDiscount());
                    updatePrizes.add(updateItem);
                    break;
                }
            }
        }
        return updatePrizes;
    }

    @Override
    public boolean offShelf(String activityId) {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(activityId)) {
            if (activitiesService.changeStatus(activityId, ActivityStatus.DOWN.getCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onShelf(String activityId) {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(activityId)) {
            if (activitiesService.changeStatus(activityId, ActivityStatus.ON.getCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean charge(String activityWinRecordId) {
        // TODO Auto-generated method stub
        ActivityWinRecord activityWinRecord = activityWinRecordService.selectByRecordId(activityWinRecordId);
        activityWinRecord.setStatus(ActivityWinRecordStatus.PROCESSING.getCode());
        //流量众筹里，将createTime当成领取流量时间，所以在此更新(大企业版为流量领取时间，中小企业版为发起充值流程时间)
        activityWinRecord.setCreateTime(new Date());
        //activityWinRecord.setWinTime(new Date());
        //activityWinRecord.setChargeTime(new Date());

        if (!activityWinRecordService.updateByPrimaryKey(activityWinRecord)) {
            logger.error("更新活动记录状态时出错. ActivityWinRecordId = {}.", activityWinRecordId);
            return false;
        }

        //插入流水号记录
        if (!serialNumService.insert(buildSerailNum(activityWinRecordId))) {
            logger.error("插入平台流水号记录出错. pltSerailNum = {}.", activityWinRecordId);
            return false;
        }

        //插入充值队列失败
        if (!insertRabbitmq(activityWinRecordId)) {
            logger.error("插入充值队列出错. pltSerailNum = {}.", activityWinRecordId);
            return false;
        }

        return true;
        
        /*ChargeRecord cr = buildChargeRecord(activityWinRecord, activities);
        if (!chargeRecordService.create(cr)) {
            logger.error("插入充值记录出错. pltSerailNum = {}.", activityWinRecordId);
            return false;
        }
        
        if (!taskProducer.produceActivityWinMsg(buildPojo(activities, activityWinRecordId))) {
            logger.error("生产消息到活动队列中时出错. Activites = {}, RecordId = {}.", new Gson().toJson(activities),
                    activityWinRecordId);
            return false;
        } else {
            logger.info("入业务队列成功.");
            if (!activityWinRecordService.updateStatusCodeByRecordId(activityWinRecordId,
                    ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                logger.error("入业务队列成功, 更新活动记录状态码失败");
            }

            if (!chargeRecordService.updateStatusCode(cr.getId(), ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                logger.error("入业务队列成功, 更新充值记录状态码失败");
            }
            logger.info("入业务队列成功, recordId = {}, 状态码 = {}", activityWinRecordId,
                    ChargeResult.ChargeMsgCode.businessQueue.getCode());
            return true;
        }*/
    }

    @Override
    @Transactional
    public boolean insertRabbitmq(String recordId) {
        ActivityWinRecord activityWinRecord = activityWinRecordService.selectByRecordId(recordId);
        Activities activities = activitiesService.selectByActivityId(activityWinRecord.getActivityId());

        ChargeRecord cr = buildChargeRecord(activityWinRecord, activities);
        if (!chargeRecordService.create(cr)) {
            logger.error("插入充值记录出错. recordId = {}.", recordId);
            return false;
        }

        if (!taskProducer.produceActivityWinMsg(buildPojo(activities, recordId))) {
            logger.error("生产消息到活动队列中时出错. Activites = {}, RecordId = {}.", new Gson().toJson(activities),
                    recordId);
            throw new RuntimeException();
        } else {
            logger.info("入业务队列成功.");
            if (!activityWinRecordService.updateStatusCodeByRecordId(recordId,
                    ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                logger.error("入业务队列成功, 更新活动记录状态码失败");
            }

            if (!chargeRecordService.updateStatusCode(cr.getId(),
                    ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                logger.error("入业务队列成功, 更新充值记录状态码失败");
            }
            logger.info("入业务队列成功, recordId = {}, 状态码 = {}", recordId,
                    ChargeResult.ChargeMsgCode.businessQueue.getCode());
            return true;
        }
    }

    private ChargeRecord buildChargeRecord(ActivityWinRecord activityWinRecord, Activities
            activities) {
        ActivityType activityType = ActivityType.fromValue(activities.getType());
        ChargeRecord cr = new ChargeRecord();
        if(activities.getType().toString()
                .equals(ActivityType.CROWD_FUNDING.getCode().toString())){
            //众筹活动，中奖纪录里的prizeId是奖品的id，其余活动中奖纪录的prizeId是产品id
            ActivityPrize prize = activityPrizeService.selectByPrimaryKey(activityWinRecord.getPrizeId());
            cr.setPrdId(prize.getProductId());
        }else{
            cr.setPrdId(activityWinRecord.getPrizeId());
        }
        cr.setEnterId(activities.getEntId());
        cr.setTypeCode(activities.getType());
        cr.setRecordId(activityWinRecord.getId());
        cr.setStatus(ChargeRecordStatus.WAIT.getCode());
        cr.setType(activityType.getname());
        cr.setPhone(activityWinRecord.getOwnMobile());
        cr.setaName(activities.getName());
        //中奖uuid即为充值uuid
        cr.setSystemNum(activityWinRecord.getRecordId());
        cr.setChargeTime(new Date());
        return cr;
    }

    private ActivitiesWinPojo buildPojo(Activities activities, String activityWinRecordId) {
        ActivitiesWinPojo pojo = new ActivitiesWinPojo();
        pojo.setActivities(activities);
        pojo.setActivitiesWinRecordId(activityWinRecordId);

        return pojo;
    }

    private SerialNum buildSerailNum(String platformSerialNum) {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum(platformSerialNum);
        serialNum.setUpdateTime(new Date());
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(0);
        return serialNum;
    }

    /** 
     * 查询某个用户的所有活动的支付情况，如果存在支付中的订单，返回活动参加记录，没有支付中的订单，返回null
     * @Title: queryPayResult 
     * @param mobile
     * @return
     * @Author: wujiamin
     * @date 2017年6月7日
    */
    @Override
    public String queryPayResult(String mobile) {
        Map queryMap = new HashMap();
        queryMap.put("activityType", ActivityType.CROWD_FUNDING.getCode());
        queryMap.put("mobile", mobile);
        queryMap.put("joinType", 2);
        queryMap.put("payResult", 1);//支付状态为支付中

        List<ActivityWinRecord> winRecords = activityWinRecordService.getWinRecordsForCrowdFundingByMap(queryMap);
        if(winRecords.size()>0){
            ActivityWinRecord record = winRecords.get(0);
            List<ActivityPaymentInfo> payInfos = activityPaymentInfoService.selectByWinRecordId(record.getRecordId());
            boolean payTag = false;//标记是否存在未支付完成的支付记录
            for(ActivityPaymentInfo payInfo : payInfos){
                if(payInfo.getStatus().equals(1)||payInfo.getStatus().equals(7)
                        ||payInfo.getStatus().equals(8)||payInfo.getStatus().equals(9)){
                    //如果有交易订单未收到回调、收到回调为等待付款、已取消、超时则系统调用获取交易单信息接口查询交易订单
                    //开始查询订单结果
                    //查询结果为：成功，失败，超时，更新activity_payment_info/activity_win_record的支付结果为成功，失败，未知错误
                    //其他状态不做变更，如果存在其他状态，则将支付标识置为true
                    if(payResultQueryService.checkPayingStatus(payInfo.getSysSerialNum())){
                        payTag = true;
                    }                       
                }
            }
            if(payTag){
                return record.getActivityId();
            }
        }
       
        return null;
    }
    
    /** 
     * 处理支付中状态的支付记录及活动参加记录
     * @Title: processPayingRecord 
     * @param activityId
     * @return
     * @Author: wujiamin
     * @date 2017年6月7日
    */
    @Override
    public boolean processPayingRecord(String activityId, String currentMobile) {
        Map queryMap = new HashMap();
        queryMap.put("activityType", ActivityType.CROWD_FUNDING.getCode());
        queryMap.put("mobile", currentMobile);
        queryMap.put("joinType", 2);
        queryMap.put("payResult", 1);//支付状态为支付中
        queryMap.put("activityId", activityId);

        //找到该用户所有该活动支付中状态的支付记录
        List<ActivityWinRecord> winRecords = activityWinRecordService.getWinRecordsForCrowdFundingByMap(queryMap);
        logger.info("用户处理支付中的参与记录，size={}", winRecords.size());
        if(winRecords.size()<=0){
            logger.info("用户处理支付中的参与记录不存在");
            return false;
        }

        ActivityWinRecord record = winRecords.get(0);
        List<ActivityPaymentInfo> payInfos = activityPaymentInfoService.selectByWinRecordId(record.getRecordId());
        for(ActivityPaymentInfo payInfo : payInfos){
            if(payInfo.getStatus().equals(1) || payInfo.getStatus().equals(7) || payInfo.getStatus().equals(8) || payInfo.getStatus().equals(9)){
                //支付状态为支付中，等待付款，已取消，超时，将activity_payment_info的支付状态改成关闭支付
                logger.info("将支付记录改成支付关闭状态，活动支付记录SysSerialNum={}，原支付状态{}", payInfo.getSysSerialNum(), payInfo.getStatus());
                payInfo.setChargeUpdateTime(new Date());
                payInfo.setStatus(10);//支付关闭状态
                if(!activityPaymentInfoService.updateBySysSerialNumSelective(payInfo)){
                    logger.info("将支付记录改成关闭状态失败，活动支付记录SysSerialNum={}", payInfo.getSysSerialNum());
                    return false;
                }                      
            }
        }
        logger.info("用户存在支付中订单，已将所有支付记录置为关闭，将活动中奖记录改成待支付，activityWinRecord={}", record.getRecordId());
        record.setPayResult(0);//activity_win_record重新设置成未支付
        record.setUpdateTime(new Date());
        if(!activityWinRecordService.updateByPrimaryKeySelective(record)){
            logger.info("将活动中奖记录改成待支付失败，activityWinRecord={}", record.getRecordId());
            return false;
        }
        return true;
    }


}

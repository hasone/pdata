/**
 *
 */
package com.cmcc.vrp.province.quartz.jobs;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.GetDataFromTemplateResp;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.google.gson.Gson;

/**
 * 活动结束更改活动状态
 */
public class ActivityEndJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ActivityEndJob.class);

    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
        logger.info("进入营销活动定时任务：活动结束");

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jsonStr = (String) jobDataMap.get("param");

        logger.info("请求参数：" + jsonStr);
        ActivityJobPojo pojo = check(jsonStr);//检查参数

        Activities activities = activitiesService.selectByActivityId(pojo.getActivityId());

        if (activities == null) {
            logger.info("该营销活动不存在，活动ID-" + pojo.getActivityId());
            return;
        } else {
            //1、活动结束，变更活动状态
            if (activities.getStatus().toString().equals(ActivityStatus.PROCESSING.getCode().toString())
                && activities.getDeleteFlag().toString().equals("0")) {
                if (activitiesService.changeStatus(pojo.getActivityId(), ActivityStatus.END.getCode())) {
                    logger.info("营销活动状态变更为已结束,活动ID-" + pojo.getActivityId());
                } else {
                    logger.info("营销活动状态变更为已结束失败,活动ID-" + pojo.getActivityId());
                }
            } else {
                logger.info("营销活动状态变更为已结束失败,活动ID-" + pojo.getActivityId() + " ;活动状态-"
                    + activities.getStatus() + " ;活动删除状态-" + activities.getDeleteFlag());
            }
            
            //2、向营销活动请求数据
            ActivityInfo activityInfo = activityInfoService.selectByActivityId(pojo.getActivityId());
            String token = parse(activityInfo.getUrl());
            
            String delDataUrl = getDelDataUrl();
            logger.info("delDataUrl = {}", delDataUrl);
            String delResponse = HttpConnection.doPost(delDataUrl, token, "text/html",
                    "utf-8", true);
            logger.info("营销模板返回的删除数据结果结果-{}",delResponse);
            if(!org.apache.commons.lang.StringUtils.isBlank(delResponse)){
                GetDataFromTemplateResp getDataFromTemplateResp = new Gson().fromJson(delResponse,
                        GetDataFromTemplateResp.class);
                ActivityInfo updateActivityInfo = new ActivityInfo();
                updateActivityInfo.setActivityId(pojo.getActivityId());
                updateActivityInfo.setId(activityInfo.getId());
                updateActivityInfo.setPlayCount(getDataFromTemplateResp.getPlayCount());
                updateActivityInfo.setVisitCount(getDataFromTemplateResp.getVisitCount());
                updateActivityInfo.setGivedUserCount(getDataFromTemplateResp.getWinCount());
                
                if(!activityInfoService.updateByPrimaryKeySelective(updateActivityInfo)){
                    logger.info("更新活动  = {} 访问游戏次数  = {}, 参与人次 = {}, 中奖人次 = {} 失败",
                            updateActivityInfo.getActivityId(), updateActivityInfo.getVisitCount(),
                            updateActivityInfo.getPlayCount(), updateActivityInfo.getGivedUserCount());
                }else{
                    logger.info("更新活动  = {} 访问游戏次数  = {}, 参与人次 = {}, 中奖人次 = {} 成功",
                            updateActivityInfo.getActivityId(), updateActivityInfo.getVisitCount(),
                            updateActivityInfo.getPlayCount(), updateActivityInfo.getGivedUserCount());
                }
            }

            //3、如果是个人红包，需要检查余额，如果还有剩余，需要进行退回操作
            if (activities.getType().toString().equals(ActivityType.COMMON_REDPACKET.getCode().toString())
                || activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())) {
                List<ActivityPrize> activityPrizeList = activityPrizeService.selectByActivityIdForIndividual(pojo.getActivityId());
                if (activityPrizeList != null) {
                    IndividualAccount individualAccount = individualAccountService.getAccountByOwnerIdAndProductId(activities.getId(),
                        activityPrizeList.get(0).getProductId(), IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());
                    if (individualAccount != null && individualAccount.getCount().intValue()>0) {
                        IndividualProduct product = individualProductService.selectByPrimaryId(activityPrizeList.get(0).getProductId());
                        //活动账户里有剩余流量币，需要进行回退操作
                        //1、产品是流量,调用退回个人流量账户的方法
                        if(product.getType().equals(IndividualProductType.DEFAULT_FLOW_PACKAGE.getValue())){
                            if (!individualAccountService.giveBackFlow(pojo.getActivityId())) {
                                logger.info("个人红包活动剩余流量退回个人流量账户操作失败，活动ID-" + pojo.getActivityId());
                            }
                        }
                        //2、产品是流量币
                        if(product.getType().equals(IndividualProductType.FLOW_COIN.getValue())){
                            if (!individualAccountService.giveBackForActivity(pojo.getActivityId())) {
                                logger.info("个人红包活动剩余流量币退回boss操作失败，活动ID-" + pojo.getActivityId());
                            }
                        }                        
                    }
                }
            }
        }
    }

    /**
     * 检查传入参数
     */
    private ActivityJobPojo check(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            logger.error("参数为空");
            return null;
        }
        ActivityJobPojo pojo = JSON.parseObject(jsonStr, ActivityJobPojo.class);
        if (pojo == null || StringUtils.isBlank(pojo.getActivityId())) {
            logger.error("参数为空");
            return null;
        }
        return pojo;
    }
    
    //获取删除营销数据url
    public String getDelDataUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_DEl_DATA_URL
            .getKey());
    }
    
    // 解析活动URL中的特定字符串
    private String parse(String url) {
        String regex = "/lottery/game/(.*)/index.html";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }
}

package com.cmcc.vrp.province.model.json;

import com.cmcc.vrp.province.model.ActivityPrize;

import java.util.List;


/**
 * @ClassName: LotteryPage
 * @Description: 页面信息
 * @author: Rowe
 * @date: 2016年1月12日 下午1:50:48
 */
public class LotteryPage {
    private Long id;

    private String activityId;//活动ID

    private Long entId;//企业ID

    private String activityName;//活动名称

    private String startTime;//开始时间

    private String endTime;//结束时间

    private Long givedNumber;//每用户可中最大数量（新增），不填：无限

    private Long maxPlayNumber;//用户最大可玩数量（变更），不填：无限(现在主要面向不显示次数的活动，比如红包)

    private Integer chargeType;//充值类型，是否延后充值:0立即充值；1延后充值

    private Integer flowType;//奖品类型 0:流量池 | 1：流量包

    private Integer lotteryType;//活动参与类型：每天  ,活动期间

    private Integer probabilityType;//奖项概率是否可设置:0设置奖品概率（此时奖品信息prizes中的probability值不为空）；1自动调整奖品概率（此时奖品信息prizes中的probability值为空,不需要设置）.

    private String activityRule;//活动规则

    private String url;//大转盘地址

    private List<ActivityPrize> prizes;//奖项信息

    private Integer wechatAuth;  //0不做微信授权 1 微信授权

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getGivedNumber() {
        return givedNumber;
    }

    public void setGivedNumber(Long givedNumber) {
        this.givedNumber = givedNumber;
    }

    public Long getMaxPlayNumber() {
        return maxPlayNumber;
    }

    public void setMaxPlayNumber(Long maxPlayNumber) {
        this.maxPlayNumber = maxPlayNumber;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public Integer getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(Integer lotteryType) {
        this.lotteryType = lotteryType;
    }

    public Integer getProbabilityType() {
        return probabilityType;
    }

    public void setProbabilityType(Integer probabilityType) {
        this.probabilityType = probabilityType;
    }

    public String getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(String activityRule) {
        this.activityRule = activityRule;
    }

    public List<ActivityPrize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<ActivityPrize> prizes) {
        this.prizes = prizes;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(Integer wechatAuth) {
        this.wechatAuth = wechatAuth;
    }


}

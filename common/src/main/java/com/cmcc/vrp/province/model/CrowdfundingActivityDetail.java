package com.cmcc.vrp.province.model;

/**
 * 广东众筹流量活动详情对象类
 * */
public class CrowdfundingActivityDetail {
    private Long id;

    private String activityId;

    private Long currentCount;

    private Long targetCount;

    private String rules;

    private Integer result;

    private String banner;

    private String logo;

    private Integer chargeType;

    private String appendix; //附件

    private Integer hasWhiteOrBlack;

    private String url;

    private Integer deleteFlag;
    
    private Long version; //乐观锁
    
    private Integer joinType;

    private String bannerKey;

    private String logoKey;

    private String appendixKey;
    
    private Integer userList;

    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public Long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Long currentCount) {
        this.currentCount = currentCount;
    }

    public Long getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Long targetCount) {
        this.targetCount = targetCount;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules == null ? null : rules.trim();
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner == null ? null : banner.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public String getAppendix() {
        return appendix;
    }

    public void setAppendix(String appendix) {
        this.appendix = appendix == null ? null : appendix.trim();
    }

    public Integer getHasWhiteOrBlack() {
        return hasWhiteOrBlack;
    }

    public void setHasWhiteOrBlack(Integer hasWhiteOrBlack) {
        this.hasWhiteOrBlack = hasWhiteOrBlack;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getBannerKey() {
        return bannerKey;
    }

    public void setBannerKey(String bannerKey) {
        this.bannerKey = bannerKey;
    }

    public String getLogoKey() {
        return logoKey;
    }

    public void setLogoKey(String logoKey) {
        this.logoKey = logoKey;
    }

    public String getAppendixKey() {
        return appendixKey;
    }

    public void setAppendixKey(String appendixKey) {
        this.appendixKey = appendixKey;
    }

    public Integer getUserList() {
        return userList;
    }

    public void setUserList(Integer userList) {
        this.userList = userList;
    }
}
package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 活动其他特殊信息类
 * */
public class ActivityInfo {
    private Long id;

    private String activityId;

    private Long prizeCount;

    private Long userCount;

    private Long totalProductSize;

    private Long price;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Integer hasWhiteOrBlack;

    private Integer qrcodeSize; //二维码长度

    private Integer download;//是否已经下载，已下载为1

    private String code; //请求返回代码

    private String url; //返回url

    /**
     * (随机红包需求定义)已发数量：已发数量为一共有多少用户抽奖。
     * （开发理解）已发用户数量（一个红包活动只能中奖一次，实质就是已发奖品数量）
     * */
    private Long givedUserCount;

    /**
     * （随机红包需求定义）已发额度：显示当前有多少产品被抢走，流量池产品单位为MB，话费产品单位为元。
     * （开发理解）发送流量总大小，以KB形式存储，如果后续做话费，以分存储
     * */
    private Long usedProductSize;
    
    private Long visitCount; //访问游戏次数
    
    private Long playCount; //玩游戏人次

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHasWhiteOrBlack() {
        return hasWhiteOrBlack;
    }

    public void setHasWhiteOrBlack(Integer hasWhiteOrBlack) {
        this.hasWhiteOrBlack = hasWhiteOrBlack;
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

    public Long getPrizeCount() {
        return prizeCount;
    }

    public void setPrizeCount(Long prizeCount) {
        this.prizeCount = prizeCount;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Long getTotalProductSize() {
        return totalProductSize;
    }

    public void setTotalProductSize(Long totalProductSize) {
        this.totalProductSize = totalProductSize;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getQrcodeSize() {
        return qrcodeSize;
    }

    public void setQrcodeSize(Integer qrcodeSize) {
        this.qrcodeSize = qrcodeSize;
    }

    public Integer getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

    public Long getGivedUserCount() {
        return givedUserCount;
    }

    public void setGivedUserCount(Long givedUserCount) {
        this.givedUserCount = givedUserCount;
    }

    public Long getUsedProductSize() {
        return usedProductSize;
    }

    public void setUsedProductSize(Long usedProductSize) {
        this.usedProductSize = usedProductSize;
    }

    public Long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }
    
    
}
package com.cmcc.vrp.province.model.json;

import java.util.Date;
import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:40:50
*/
public class RedPacketPage {
    private Long entId;
    private String activityName;
    private Integer maxPerUser;
    private String description;
    private String people;
    private String activityDes;
    private Boolean startFlag;
    private Integer probabilityType;
    private Date startTime;
    private Date endTime;
    private List<RedPacketPrizeJson> prizes;


    private Integer wechatAuth;

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Integer getMaxPerUser() {
        return maxPerUser;
    }

    public void setMaxPerUser(Integer maxPerUser) {
        this.maxPerUser = maxPerUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getActivityDes() {
        return activityDes;
    }

    public void setActivityDes(String activityDes) {
        this.activityDes = activityDes;
    }

    public List<RedPacketPrizeJson> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<RedPacketPrizeJson> prizes) {
        this.prizes = prizes;
    }

    public Integer getProbabilityType() {
        return probabilityType;
    }

    public void setProbabilityType(Integer probabilityType) {
        this.probabilityType = probabilityType;
    }

    public Boolean getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(Boolean startFlag) {
        this.startFlag = startFlag;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(Integer wechatAuth) {
        this.wechatAuth = wechatAuth;
    }


}

package com.cmcc.vrp.province.model;

/**
 * 黑白名单类
 *
 * */
public class BlackAndWhiteList {

    private Long id;

    private Long activityId;

    private String activityType;

    private String phone;

    private Integer isWhiteFlag;  //1在白名单里；2在黑名单里

    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIsWhiteFlag() {
        return isWhiteFlag;
    }

    public void setIsWhiteFlag(Integer isWhiteFlag) {
        this.isWhiteFlag = isWhiteFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}

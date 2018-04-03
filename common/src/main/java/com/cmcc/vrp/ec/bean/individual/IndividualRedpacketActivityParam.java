package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * IndividualRedpackActivityParam.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@XStreamAlias("ActivityParam")
public class IndividualRedpacketActivityParam {
    @XStreamAlias("Mobile")
    private String mobile;
    
    @XStreamAlias("ActivityName")
    private String activityName;
    
    @XStreamAlias("StartTime")
    private String startTime;
    
    @XStreamAlias("Type")
    private Integer type;//游戏类型
    
    @XStreamAlias("Size")
    private Long size;//随机红包为产品总大小（M）为单位，普通红包为单个红包的大小
    
    @XStreamAlias("Count")
    private Long count;
    
    @XStreamAlias("Object")
    private String object;
    
    @XStreamAlias("Rules")
    private String rules;
    
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
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
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public String getObject() {
        return object;
    }
    public void setObject(String object) {
        this.object = object;
    }
    public String getRules() {
        return rules;
    }
    public void setRules(String rules) {
        this.rules = rules;
    }

    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    
}

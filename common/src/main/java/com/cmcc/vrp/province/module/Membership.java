package com.cmcc.vrp.province.module;

import java.util.Date;

/**
 * Membership.java
 * 广东众筹会员
 * @author wujiamin
 * @date 2017年4月11日
 */
public class Membership {
    private String mobile;
    
    private Long count;//流量币数量
    
    private Long accumulateCount;//累积流量币数量
    
    private String grade;//等级
    
    private Date createTime;//创建时间
    
    private String openid;
    
    private String nickname;
    
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public Long getAccumulateCount() {
        return accumulateCount;
    }
    public void setAccumulateCount(Long accumulateCount) {
        this.accumulateCount = accumulateCount;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
}

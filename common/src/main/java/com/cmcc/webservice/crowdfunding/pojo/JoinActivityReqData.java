package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * JoinActivityReqData.java
 * 广东众筹活动报名接口，请求对象，业务数据
 * @author wujiamin
 * @date 2017年2月8日
 */
@XStreamAlias("Join")
public class JoinActivityReqData {
    @XStreamAlias("ActivityId")
    String activityId;
    
    @XStreamAlias("EnterpriseCode")
    String enterpriseCode;
    
    @XStreamAlias("Mobile")
    String mobile;
    
    @XStreamAlias("PrizeId")
    Long prizeId;
    
    public String getActivityId() {
        return activityId;
    }
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    public String getEnterpriseCode() {
        return enterpriseCode;
    }
    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Long getPrizeId() {
        return prizeId;
    }
    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    } 
}

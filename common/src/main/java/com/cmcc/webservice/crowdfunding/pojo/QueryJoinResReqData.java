package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 广东众筹报名结果查询，请求对象，业务参数
 * QueryJoinResReqData.java
 * @author wujiamin
 * @date 2017年2月9日
 */
@XStreamAlias("QueryJoinRes")
public class QueryJoinResReqData {
    @XStreamAlias("ActivityId")
    String activityId;
    
    @XStreamAlias("EnterpriseCode")
    String enterpriseCode;
    
    @XStreamAlias("Mobile")
    String mobile;
    
    @XStreamAlias("EcProductCode")
    String ecProductCode;

    public String getEcProductCode() {
        return ecProductCode;
    }

    public void setEcProductCode(String ecProductCode) {
        this.ecProductCode = ecProductCode;
    }

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
}

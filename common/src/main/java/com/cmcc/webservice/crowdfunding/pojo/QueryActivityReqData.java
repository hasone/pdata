package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 广东众筹活动查询接口，请求对象，业务数据
 * @author wujiamin
 * @date 2017年2月8日
 */
@XStreamAlias("QueryActivity")
public class QueryActivityReqData {
    @XStreamAlias("ActivityId")
    String activityId;
    
    @XStreamAlias("EnterpriseCode")
    String enterpriseCode;
    
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
}

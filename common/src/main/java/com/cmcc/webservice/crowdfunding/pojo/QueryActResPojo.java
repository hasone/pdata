package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * QueryActResPojo.java
 */
@XStreamAlias("QueryActRes")
public class QueryActResPojo {
    
    @XStreamAlias("ActivityId")
    private String activityId;
    
    @XStreamAlias("EnterpriseCode")
    private String enterpriseCode;
    
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

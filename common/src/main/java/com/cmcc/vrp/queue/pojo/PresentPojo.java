package com.cmcc.vrp.queue.pojo;

import java.io.Serializable;

/**
 * 赠送时的对象(包括红包，转盘，赠送,包月赠送等都可以归为赠送）
 */
public class PresentPojo implements Serializable {
    private static final long serialVersionUID = -1000418150745572541L;

    private Long ruleId;

    private Long recordId;

    private String mobile;

    private Long productId;

    private Long enterpriseId;

    private String requestSerialNum;//充值请求序列号
    
    private Integer count=1;//拓展，用于山东的流量包个数

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getRequestSerialNum() {
        return requestSerialNum;
    }

    public void setRequestSerialNum(String requestSerialNum) {
        this.requestSerialNum = requestSerialNum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    
}
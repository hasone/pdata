package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/24.
 */
@XStreamAlias("Record")
public class QueryItem {

    @XStreamAlias("EnterpriseId")
    Long enterpriseId;

    @XStreamAlias("ProductId")
    Long productId;

    @XStreamAlias("Mobile")
    String mobile;

    @XStreamAlias("Status")
    Integer status;

    @XStreamAlias("Description")
    String description;

    @XStreamAlias("ChargeTime")
    String chargeTime;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    @Override
    public String toString() {
        return "QueryItem [enterpriseId=" + enterpriseId + ", productId=" + productId + ", mobile=" + mobile
                + ", status=" + status + ", description=" + description + ", chargeTime=" + chargeTime + "]";
    }
    
}

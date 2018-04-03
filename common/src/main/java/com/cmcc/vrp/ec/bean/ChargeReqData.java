package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/25.
 */
@XStreamAlias("ChargeData")
public class ChargeReqData {

    @XStreamAlias("Mobile")
    String mobile;

    @XStreamAlias("ProductId")
    String productCode;

    @XStreamAlias("SerialNum")
    String serialNum;
    
    @XStreamAlias("Count")
    String flowSize;
    
    @XStreamAlias("EffectType")
    String effectType;

    public String getFlowSize() {
        return flowSize;
    }

    public void setFlowSize(String flowSize) {
        this.flowSize = flowSize;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }
}

package com.cmcc.vrp.queue.pojo;

/**
 * Created by leelyn on 2016/5/17.
 */
public class ChargePojo {
    private Long chargeRecordId;
    private Long enterpriseId;
    private Long productId;
    private String systemNum;
    private String ecSerialNum;
    private String mobile;

    private String appKey;
    private String remoteIpAddr;
    private String fingerprint;
    
    private Integer effectType;

    public Long getChargeRecordId() {
        return chargeRecordId;
    }

    public void setChargeRecordId(Long chargeRecordId) {
        this.chargeRecordId = chargeRecordId;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEcSerialNum() {
        return ecSerialNum;
    }

    public void setEcSerialNum(String ecSerialNum) {
        this.ecSerialNum = ecSerialNum;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getRemoteIpAddr() {
        return remoteIpAddr;
    }

    public void setRemoteIpAddr(String remoteIpAddr) {
        this.remoteIpAddr = remoteIpAddr;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }
}
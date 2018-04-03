package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Info")
public class MemOperateInfo {

    @XStreamAlias("EnterCode")
    private String enterCode;
    
    @XStreamAlias("EnterPrdCode")
    private String enterPrdCode;
    
    @XStreamAlias("Mobile")
    private String mobile;
    
    @XStreamAlias("PrdCode")
    private String prdCode;
    
    @XStreamAlias("OprType")
    private String oprType;
    
    @XStreamAlias("OprTime")
    private String oprTime;
    
    @XStreamAlias("EffectTime")
    private String effectTime;

    public String getEnterCode() {
        return enterCode;
    }

    public void setEnterCode(String enterCode) {
        this.enterCode = enterCode;
    }

    public String getEnterPrdCode() {
        return enterPrdCode;
    }

    public void setEnterPrdCode(String enterPrdCode) {
        this.enterPrdCode = enterPrdCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getOprType() {
        return oprType;
    }

    public void setOprType(String oprType) {
        this.oprType = oprType;
    }

    public String getOprTime() {
        return oprTime;
    }

    public void setOprTime(String oprTime) {
        this.oprTime = oprTime;
    }

    public String getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(String effectTime) {
        this.effectTime = effectTime;
    }
}


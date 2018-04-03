package com.cmcc.vrp.wx.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月25日 下午1:42:52
*/
public class Feature {
    private String serviceCode;
    
    private String monthFlag;
    
    private String extendPrd;
    
    private String months;
    
    private String mainPrdCode;
    
    private String mainServiceCode;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getMonthFlag() {
        return monthFlag;
    }

    public void setMonthFlag(String monthFlag) {
        this.monthFlag = monthFlag;
    }

    public String getExtendPrd() {
        return extendPrd;
    }

    public void setExtendPrd(String extendPrd) {
        this.extendPrd = extendPrd;
    }

    @Override
    public String toString() {
        return "Feature [serviceCode=" + serviceCode + ", monthFlag=" + monthFlag + ", extendPrd=" + extendPrd + "]";
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getMainPrdCode() {
        return mainPrdCode;
    }

    public void setMainPrdCode(String mainPrdCode) {
        this.mainPrdCode = mainPrdCode;
    }

    public String getMainServiceCode() {
        return mainServiceCode;
    }

    public void setMainServiceCode(String mainServiceCode) {
        this.mainServiceCode = mainServiceCode;
    }
    
}

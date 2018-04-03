/**
 *
 */
package com.cmcc.webservice.sichuan.pojo;

/**
 * @author JamieWu
 *         查询接口的返回结果
 */
public class QueryResult {
    private String serialNumber;
    private String mobile;
    private String serviceCode;
    private String discntCode;
    private String effectiveWay;
    private String startDate;
    private String endDate;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getDiscntCode() {
        return discntCode;
    }

    public void setDiscntCode(String discntCode) {
        this.discntCode = discntCode;
    }

    public String getEffectiveWay() {
        return effectiveWay;
    }

    public void setEffectiveWay(String effectiveWay) {
        this.effectiveWay = effectiveWay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}

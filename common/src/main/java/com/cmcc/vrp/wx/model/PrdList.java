package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月5日 上午8:49:59
*/
@XStreamAlias("PrdList")
public class PrdList {

    @XStreamAlias("PrdCode")
    private String prdCode;

    @XStreamAlias("OptType")
    private String optType;

    @XStreamAlias("Amount")
    private String amount;
    
    @XStreamAlias("MarketingCode")
    private String marketingCode;

    @XStreamAlias("Service")
    private Service service;

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMarketingCode() {
        return marketingCode;
    }

    public void setMarketingCode(String marketingCode) {
        this.marketingCode = marketingCode;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    
}

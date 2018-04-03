package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Request")
public class CFChargeReq {
    
    @XStreamAlias("Datetime")
    private String dateTime;
    
    @XStreamAlias("CFCharge")
    private CFChargePojo cfChargePojo;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public CFChargePojo getCfChargePojo() {
        return cfChargePojo;
    }

    public void setCfChargePojo(CFChargePojo cfChargePojo) {
        this.cfChargePojo = cfChargePojo;
    }
}

package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CFChargeResult")
public class CFChargeResultPojo {
    
    @XStreamAlias("SerialNum")
    private String serialNum;
    
    @XStreamAlias("SystemNum")
    private String systemNum;

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }
}

package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CFCharge")
public class CFChargePojo {
    
    @XStreamAlias("RecordId")
    private String recordId;
    
    @XStreamAlias("Mobile")
    private String mobile;
    
    @XStreamAlias("SerialNum")
    private String serialNum;
    
    /*@XStreamAlias("EnterpriseCode")
    private String enterpriseCode;*/

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    /*public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }*/
   
}

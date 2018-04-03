package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 充值查询返回对象
 * @author qinqinyan
 * */
@XStreamAlias("Response")
public class CFChargeResultResp {
    
    @XStreamAlias("Datetime")
    private String dateTime;
    
    @XStreamAlias("Code")
    private String code;
    
    @XStreamAlias("Message")
    private String message;
    
    @XStreamAlias("CFChargeResult")
    private CFChargeResultRespPojo cfChargeResultRespPojo;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CFChargeResultRespPojo getCfChargeResultRespPojo() {
        return cfChargeResultRespPojo;
    }

    public void setCfChargeResultRespPojo(CFChargeResultRespPojo cfChargeResultRespPojo) {
        this.cfChargeResultRespPojo = cfChargeResultRespPojo;
    }

}

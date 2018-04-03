package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sunyiwei on 2016/7/6.
 */
@XStreamAlias("Response")
public class CallbackResp {
    @XStreamAlias("Datetime")
    private String dateTime;

    @XStreamAlias("Code")
    private String code;

    @XStreamAlias("Message")
    private String message;

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
}

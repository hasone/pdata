package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月6日 上午11:21:03
*/
@XStreamAlias("NotifyResp")
public class NotifyResp {
    @XStreamAlias("MessageID")
    private String messageID;
    
    @XStreamAlias("Result")
    private String result;
    
    @XStreamAlias("Description")
    private String description;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }    

}

package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月10日 下午4:54:50
*/
@XStreamAlias("SyncResult")
public class SyncCardStatusRespData {
    @XStreamAlias("CardNumber")
    String cardNumber;
    
    @XStreamAlias("SyncCode")
    String syncCode;
    
    @XStreamAlias("SyncInfo")
    String syncInfo;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSyncCode() {
        return syncCode;
    }

    public void setSyncCode(String syncCode) {
        this.syncCode = syncCode;
    }

    public String getSyncInfo() {
        return syncInfo;
    }

    public void setSyncInfo(String syncInfo) {
        this.syncInfo = syncInfo;
    }
    
    
}

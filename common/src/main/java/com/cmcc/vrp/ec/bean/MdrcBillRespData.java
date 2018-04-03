package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月15日 下午12:12:03
*/
@XStreamAlias("CardTransaction")
public class MdrcBillRespData {
    @XStreamAlias("CardNum")
    String cardNum;
    
    @XStreamAlias("Status")
    String status;
    
    @XStreamAlias("Message")
    String message;
    
    @XStreamAlias("Trans_ID_BOSS")
    String transIdBoss;
    
    @XStreamAlias("Trans_ID")
    String transId;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransIdBoss() {
        return transIdBoss;
    }

    public void setTransIdBoss(String transIdBoss) {
        this.transIdBoss = transIdBoss;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

}

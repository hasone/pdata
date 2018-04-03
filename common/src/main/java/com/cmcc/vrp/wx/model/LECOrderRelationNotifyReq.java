package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年3月6日 下午4:08:05
*/
@XStreamAlias("LECOrderRelationNotifyReq")
public class LECOrderRelationNotifyReq {
    @XStreamAlias("ECCode")
    String entCode;
    
    @XStreamAlias("ECPrdCode")
    String entPrdCode;
    
    @XStreamAlias("Status")
    String status;

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getEntPrdCode() {
        return entPrdCode;
    }

    public void setEntPrdCode(String entPrdCode) {
        this.entPrdCode = entPrdCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LECOrderRelationNotifyReq [entCode=" + entCode + ", entPrdCode=" + entPrdCode + ", status=" + status
                + "]";
    }

}

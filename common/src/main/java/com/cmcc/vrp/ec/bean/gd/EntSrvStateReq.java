package com.cmcc.vrp.ec.bean.gd;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("EntSrvStateReq")
public class EntSrvStateReq {

    @XStreamAlias("SICode")
    private String siCode;
    
    @XStreamAlias("OptType")
    private String optType;
    
    @XStreamAlias("EntCode")
    private String entCode;
    
    @XStreamAlias("prdOrdCode")
    private String prdOrdCode;
    
    @XStreamAlias("ECAccessPort")
    private String ecAccessPort;
    
    @XStreamAlias("OptTime")
    private String optTime;
    
    @XStreamAlias("ExecDate")
    private String execDate;
    
    @XStreamAlias("ModiReason")
    private String modiReason;
    
    @XStreamAlias("Memo")
    private String memo;

    public String getSiCode() {
        return siCode;
    }

    public void setSiCode(String siCode) {
        this.siCode = siCode;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getPrdOrdCode() {
        return prdOrdCode;
    }

    public void setPrdOrdCode(String prdOrdCode) {
        this.prdOrdCode = prdOrdCode;
    }

    public String getEcAccessPort() {
        return ecAccessPort;
    }

    public void setEcAccessPort(String ecAccessPort) {
        this.ecAccessPort = ecAccessPort;
    }

    public String getOptTime() {
        return optTime;
    }

    public void setOptTime(String optTime) {
        this.optTime = optTime;
    }

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    public String getModiReason() {
        return modiReason;
    }

    public void setModiReason(String modiReason) {
        this.modiReason = modiReason;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
